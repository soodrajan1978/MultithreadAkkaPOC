package com.ibm.cai.employee.actor.pojo;

import java.util.List;

/*
 * @auhor rajasood@in.ibm.com
 */

import com.ibm.cai.employee.model.EmployeeUser;

/*
 * Response POJO to read response
 */
public class ReadFileResponse {

	private String jobId;
	private List<EmployeeUser> employeeList;
	
	public List<EmployeeUser> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeUser> employeeList) {
		this.employeeList = employeeList;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}


}
