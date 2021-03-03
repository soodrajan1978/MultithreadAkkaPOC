package com.ibm.cai.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ibm.cai.employee.service.EmployeeService;
import com.ibm.cai.employee.serviceimpl.EmployeeServiceImpl;

  /*
   * Actor main api 
   * Data Insertion  in H2 in memory on system bootstrap on spring boot start
 * Author rajasood@in.ibm.com
 */
 
@SpringBootApplication
@EnableScheduling
public class MVP9HistoricalDataExtractionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MVP9HistoricalDataExtractionApplication.class, args);
		try {
			EmployeeService es = new EmployeeServiceImpl();
			//Add data to DB
			 es.employeeAdd();
			// es.ListEmployeeWithAkkaActor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
