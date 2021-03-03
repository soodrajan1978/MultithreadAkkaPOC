package com.ibm.cai.employee.actor.pojo;


/*
 * Actor POJO with  request param

 * @auhor rajasood@in.ibm.com
 */
 
public class RouterRequest {
	
	private int pageNumber;
	private int pageSize;
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
