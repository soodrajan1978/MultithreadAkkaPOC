package com.ibm.cai.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {
	
	
	 public EmployeeNotFoundException(String  id) {
	        super("emp_id is not found : " + id);
	    }


}
