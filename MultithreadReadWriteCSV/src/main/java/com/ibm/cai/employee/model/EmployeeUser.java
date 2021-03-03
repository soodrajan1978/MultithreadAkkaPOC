package com.ibm.cai.employee.model;

import javax.persistence.*;

@Entity
@Table(name = "nbc_on_us")
public class EmployeeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String age;
	
	
	
	private String first_name;
	
	private String last_name;

	private String dept ;
	
	private String sal;
	
    public EmployeeUser() {
    }

    public EmployeeUser(String first_name, String last_name,String dept ) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    
    public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSalary() {
		return sal;
	}

	public void setSalary(String salary) {
		this.sal = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@Override
    public String toString() {
        return "EmployeeUser{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name=" + last_name +
                 ", dept=" + dept +
                '}';
    }
}
