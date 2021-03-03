package com.ibm.cai.employee.dto;

public class EmployeeDTO {
	
	/*
	 * id INT AUTO_INCREMENT  PRIMARY KEY,
age VARCHAR(250) NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
 dept VARCHAR(250) NOT NULL,
  sal VARCHAR(250) DEFAULT NULL
	 */
	private Long id;
	
	private String age;
	
	
	
	private String firstName;
	
	private String lastName;

	private String dept ;
	
	private String salary;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", age=" + age + ", salary=" + salary + ", firstName=" + firstName
				+ ", lastName=" + lastName + ",dept=\" + dept + \",]";
	}
	
	

}
