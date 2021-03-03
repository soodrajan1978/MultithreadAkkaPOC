package com.ibm.cai.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.cai.employee.actor.pojo.RouterRequest;
import com.ibm.cai.employee.actor.request.RequestActorRouter;
import com.ibm.cai.employee.config.SecurityConfig;
import com.ibm.cai.employee.dto.EmployeeDTO;
import com.ibm.cai.employee.model.EmployeeUser;
import com.ibm.cai.util.CsvFileWriter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/*
 * @autor Rajan Sood
 * 
 * https://doc.akka.io/japi/akka/2.3/akka/actor/ActorSystem.html
 * 
 * 
 */
@Repository
@Transactional
public class EmployeeDAO {
	@Autowired
	@PersistenceContext()
	private EntityManager entityManager;

	public void add(int startIndex, int noOfRecord) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO nbc_on_us(id, age,first_name, last_name, dept,SAL) VALUES (?, ?, ? ,?, ?,?)";
			conn = H2DataConnectionPool.getConnection();
			ps = conn.prepareStatement(sql);

			for (int i = startIndex; i <noOfRecord + startIndex; i++) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setId(new Long(i));
				dto.setFirstName("F_name" + i);
				dto.setLastName("l_dto" + i);
				dto.setSalary("10" + i);
				dto.setDept("A" + i);
				dto.setAge("25");

				ps.setLong(1, dto.getId());
				ps.setString(2, dto.getAge());
				ps.setString(3, dto.getFirstName());
				ps.setString(4, dto.getLastName());
				ps.setString(5, dto.getDept());
				ps.setString(6, dto.getSalary());
				ps.executeUpdate();
			}

			return;
		} catch (Exception e) {
			// e.printStackTrace();employee
			throw new Exception(e);
		} finally {
			H2DataConnectionPool.closeConnection(conn);

		}

	}

	public EmployeeUser findById(Long id) {
		return this.entityManager.find(EmployeeUser.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<EmployeeUser> getAllIBMEmployees() {
		System.out.println("Read process starts*********");
		ActorSystem actorSystem = ActorSystem.create();
		RouterRequest routerRequest = new RouterRequest();
		int size = Integer.parseInt(SecurityConfig.hash_map.get("Threadcount"));
		int pSize = Integer.parseInt(SecurityConfig.hash_map.get("PageSize"));
		System.out.println("Total Record count " + size * pSize);
		System.out.println("Thread count " + size + " ***Pages *****" + pSize);
		routerRequest.setPageNumber(size);
		routerRequest.setPageSize(pSize);
		long startTime = System.nanoTime();
		System.out.println("Current  Date & Time :- " + new Date());
		//https://doc.akka.io/japi/akka/2.3/akka/actor/ActorSystem.html
		ActorRef readFileActorRouter = actorSystem.actorOf(Props.create(RequestActorRouter.class));
		Timeout timeout = new Timeout(Duration.create(15, TimeUnit.SECONDS));
		
		//The Patterns.ask () will send a message (the 2nd argument) to the actor
		//provided as first argument and will return an AKKA Future which will be completed 
		//within the specified timeout duration (given as 3rd argument). 
		//Once we have the AKKA Future we are going to await on it for a result.
		Future<Object> future = Patterns.ask(readFileActorRouter, routerRequest, timeout);
		List<EmployeeUser> dataList = null;

		try {
			dataList = (List<EmployeeUser>) Await.result(future, timeout.duration());

			String fileName = System.getProperty("user.home") + "/employeeList_Akka.csv";
			CsvFileWriter.writeCsvFile(fileName, dataList);
			long endTime = System.nanoTime();
			long timeElapsed = endTime - startTime;
			System.out.println("process end******************");
			System.out.println("Response Time  using Akka Actor functionality having  Thread count :" + size + " = "
					+ timeElapsed / 1000000 * 0.001 + " Seconds:");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		actorSystem.stop(readFileActorRouter);
		return dataList;

	}

	public List<EmployeeUser> getList(int threadcount, int recordCountperThread) throws Exception {
		PreparedStatement ps = null;
		List<EmployeeUser> list = new ArrayList<EmployeeUser>();
		Connection conn = null;
		try {
			// System.out.println("GETlIST********" );
			String sql = "SELECT id, age,first_name, last_name, DEPT ,SAL FROM nbc_on_us where  id between  ?  and ?";
			conn = H2DataConnectionPool.getConnection();

			ps = conn.prepareStatement(sql);

			int startIndex = (threadcount - 1) * recordCountperThread;
			int total = recordCountperThread + startIndex;

			ps.setInt(1, startIndex);

			ps.setInt(2, total);

			ResultSet rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {

				int id = rs.getInt("id");
				String age = rs.getString("age");
				String salary = rs.getString("SAL");
				String first = rs.getString("first_name");
				String last = rs.getString("last_name");
				String dept = rs.getString("dept");

				EmployeeUser dto = new EmployeeUser();

				dto.setId(new Long(id));
				dto.setAge(age);
				dto.setDept(dept);
				dto.setSalary(salary);
				dto.setFirst_name(first);
				dto.setLast_name(last);
				i++;

				list.add(dto);

			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			// System.out.println("End Time :- " + new Date());
			H2DataConnectionPool.closeConnection(conn);
		}

	}

}
