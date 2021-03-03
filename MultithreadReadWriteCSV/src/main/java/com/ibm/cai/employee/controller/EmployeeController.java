package com.ibm.cai.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.cai.employee.dao.EmployeeDAO;
import com.ibm.cai.employee.model.EmployeeUser;
import com.ibm.cai.employee.repository.EmployeeRepository;
import com.ibm.cai.employee.service.EmployeeService;
import com.ibm.cai.util.CsvFileWriter;

import io.swagger.annotations.Api;

/*
 * @author rajasood
 */

@RestController
@Api(tags = "EmployeeController")
@RequestMapping("/api/employee")
public class EmployeeController {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeDAO dao;

	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping("/getAllIBMEmployees")
	public Iterable<EmployeeUser> getAllEmployees() {
		System.out.println("process starts******************");
		long startTime = System.nanoTime();
		Iterable<EmployeeUser> itr = employeeRepository.findAll();
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		System.out.println("process end******************");
		// System.out.println("Response Data : - " + dataList.size());
		String fileName = System.getProperty("user.home") + "/employeeList_JPA.csv";
		List<EmployeeUser> empUserList = getCollectionFromIteralbe(itr);
		CsvFileWriter.writeCsvFile(fileName, empUserList);
		System.out.println("Response Time with JPA CRUD functionality (Without Akka FW) = "
				+ (timeElapsed / 1000000 * 0.001) + "  Seconds");

		return itr;

	}

	@GetMapping("/getAllEmployeesWithMultiThreadAkka")
	public List<EmployeeUser> getAllEmployeesWithMultiThreadAkka() {
		
		return dao.getAllIBMEmployees();

	}

	
	// function to convert Iterable into Collection
	public static <T> List<EmployeeUser> getCollectionFromIteralbe(Iterable<EmployeeUser> itr) {
		List<EmployeeUser> cltn = new ArrayList<EmployeeUser>();
		for (EmployeeUser t : itr)
			cltn.add(t);
		return cltn;
	}
}
