package com.ibm.cai.employee.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ibm.cai.employee.model.EmployeeUser;

@RepositoryRestResource(collectionResourceRel="employeeUser", path="employeeUser")
public interface EmployeeRepository extends PagingAndSortingRepository<EmployeeUser, Integer> {

}
