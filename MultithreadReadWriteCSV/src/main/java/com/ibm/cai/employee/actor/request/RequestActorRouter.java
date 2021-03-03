package com.ibm.cai.employee.actor.request;

import java.util.UUID;

import com.ibm.cai.employee.actor.pojo.ReadFileRequest;
import com.ibm.cai.employee.actor.pojo.ReadFileResponse;
import com.ibm.cai.employee.actor.pojo.RouterData;
import com.ibm.cai.employee.actor.pojo.RouterRequest;
import com.ibm.cai.employee.actor.response.ResponseActor;
import com.ibm.cai.employee.config.SecurityConfig;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
/*
 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
  https://doc.akka.io/api/akka/2.0.5/akka/actor/ActorRef.html
  
  Immutable and serializable handle to an actor,  ActorRef can be obtained from an ActorRefFactory, 
  an interface which is implemented by ActorSystem and ActorContext. 
  
  @ author  rajasood@in.ibm.com
 * 
 */

public class RequestActorRouter extends UntypedActor {

	private RouterData routerStatus = new RouterData();

	private int threadCount = Integer.parseInt(SecurityConfig.hash_map.get("Threadcount"));
	private int pSize = Integer.parseInt(SecurityConfig.hash_map.get("PageSize"));
	int i = 0;
	
	//Actor Life cycle  getContext().actorOf :
	//Props is a configuration class to specify options for the creation of actors.
	//Actors are created by passing a Props instance into the actorOf factory method which is 
	//available on ActorSystem and ActorContext.
	private ActorRef readFilePool = getContext()
			.actorOf(Props.create(ResponseActor.class).withRouter(new RoundRobinRouter(50)));

	@Override
	public void onReceive(Object obj) throws Exception {

		if (obj instanceof RouterRequest) {
			routerStatus.setSender(getSender());
			//actor reference sending message one by one to dispatcher queue

			for (i = 1; i <= threadCount; i++) {
				
				ReadFileRequest request = new ReadFileRequest();
				request.setJobId(getUniqueID());
				System.out.println("Tthread"+ i +"-"+ request.getJobId());
				request.setPageNumber(i);
				request.setPageSize(pSize);
				routerStatus.incrementJobInprogressCount();
				routerStatus.getJobStatus().put(request.getJobId(), "Inprogress");
							
				/*Actor send request using actor self reference to message dispatcher
				 which enque  message (FIFO Sequential manner) in Message box  and  in return  subscribed   
				 by  response actor  mailbox queue  
				*/
						readFilePool.tell(request, getSelf());
			}

		} else if (obj instanceof ReadFileResponse) {
			// dispatched message  response received by actor sender
			ReadFileResponse response = (ReadFileResponse) obj; 
			routerStatus.getJobStatus().put(response.getJobId(), "Completed");
           //getting data  from response
			routerStatus.getEmployeeDTO().addAll(response.getEmployeeList());

			routerStatus.incrementJobCompletionCount();
			if (routerStatus.getJobCompletionCount() == routerStatus.getJobInprogressCount()) {
				
				/*Actor send response using actor self reference to message 
				 dispatcher which enque  message (FIFO as sequential queue) in Message broker
				 and  in return  subscribed by actor  				
				 mailbox queue  using onreceive response
			*/
				routerStatus.getSender().tell(routerStatus.getEmployeeDTO(), getSelf());
				
				//Stop Thread once message receive by actor response
				/*
				 * Stop method to terminate any child actor or the actor itself. 
				 * Stopping is done asynchronously and that the current
				 *  message processing will finish before the actor is terminated. 
				 *  No more incoming messages will be accepted in the actor mailbox.By stopping a parent actor,
				 *   system also send a kill signal to all of the child actors that were spawned by it.
				 */
				getContext().stop(self());
			}

		}

	}

	private String getUniqueID() {
		String uniqueID = UUID.randomUUID().toString();
		return uniqueID;
	}

}
