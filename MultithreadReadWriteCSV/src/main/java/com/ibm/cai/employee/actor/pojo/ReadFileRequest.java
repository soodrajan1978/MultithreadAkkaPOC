package com.ibm.cai.employee.actor.pojo;

/*
 * @auhor rajasood@in.ibm.com
 */

public class ReadFileRequest {

	private String jobId;
	private int pageNumber;
	private int pageSize;
	

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
