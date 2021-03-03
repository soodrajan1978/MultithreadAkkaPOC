package com.ibm.cai.employee.actor.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.cai.employee.model.EmployeeUser;

import akka.actor.ActorRef;


/*
 * Pojo Class having actor reference  and other attributes  use in RequestActorRouter API
 * @auhor rajasood@in.ibm.com
 */
public class RouterData {

	private List<EmployeeUser> employeeDTO = new ArrayList<EmployeeUser>();
	private Map<String, String> jobStatus = new HashMap<String, String>();
	private Integer jobCompletionCount = 0;
	private Integer jobInprogressCount = 0;

	private ActorRef sender;

	public ActorRef getSender() {
		return sender;
	}

	public void setSender(ActorRef sender) {
		this.sender = sender;
	}

	public Integer incrementJobCompletionCount() {
		return ++jobCompletionCount;
	}

	public Integer incrementJobInprogressCount() {
		return ++jobInprogressCount;
	}

	public Integer getJobCompletionCount() {
		return jobCompletionCount;
	}

	public Integer getJobInprogressCount() {
		return jobInprogressCount;
	}

	public void setJobInprogressCount(Integer jobInprogressCount) {
		this.jobInprogressCount = jobInprogressCount;
	}

	public List<EmployeeUser> getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(List<EmployeeUser> employeeDTO) {
		if (!this.employeeDTO.isEmpty()) {
			this.employeeDTO.addAll(employeeDTO);
		} else {
			this.employeeDTO = employeeDTO;
		}
	}

	public void setJobCompletionCount(Integer jobCompletionCount) {
		this.jobCompletionCount = jobCompletionCount;
	}

	public Map<String, String> getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Map<String, String> jobStatus) {
		this.jobStatus = jobStatus;
	}

}
