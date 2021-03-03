package com.ibm.cai.employee.actor.response;

import java.util.List;

import com.ibm.cai.employee.actor.pojo.ReadFileRequest;
import com.ibm.cai.employee.actor.pojo.ReadFileResponse;
import com.ibm.cai.employee.dao.EmployeeDAO;
import com.ibm.cai.employee.model.EmployeeUser;

import akka.actor.UntypedActor;

/*
 * @auhor rajasood@in.ibm.com
 */

public class ResponseActor extends UntypedActor {	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ReadFileRequest) {
			
			// dispatched message  received by actor receiver
			ReadFileRequest readFileRequest = (ReadFileRequest) message;
			
			// receiver actor  mail box read message from thread pool ,prepare response to dispatch to sender actor 
			ReadFileResponse response = getEmployeeDataList(readFileRequest);
			/*Actor send response using actor self reference to message 
			 dispatcher which enque  message in Message broker
			 and  in return  subscribed by actor  				
			 mailbox queue  using onreceive response
		*/
			getSender().tell(response, getSelf());
		} else {
			unhandled(message);
		}
	}

	private ReadFileResponse getEmployeeDataList(ReadFileRequest readFileRequest) throws Exception {
		EmployeeDAO employeeDAO = new EmployeeDAO();
		ReadFileResponse readFileResponse = new ReadFileResponse();
		readFileResponse.setJobId(readFileRequest.getJobId());
		List<EmployeeUser> employeeList = employeeDAO.getList(readFileRequest.getPageNumber(),
				readFileRequest.getPageSize());
		//Setting data  in response
		readFileResponse.setEmployeeList(employeeList);
		return readFileResponse;
	}
}
