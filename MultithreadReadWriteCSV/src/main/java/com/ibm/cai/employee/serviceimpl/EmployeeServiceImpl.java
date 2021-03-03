package com.ibm.cai.employee.serviceimpl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ibm.cai.employee.actor.pojo.RouterRequest;
import com.ibm.cai.employee.actor.request.RequestActorRouter;
import com.ibm.cai.employee.config.SecurityConfig;
import com.ibm.cai.employee.dao.EmployeeDAO;
import com.ibm.cai.employee.model.EmployeeUser;
import com.ibm.cai.employee.service.EmployeeService;
import com.ibm.cai.util.CsvFileWriter;
import com.ibm.cai.util.StringUtil;
import com.ibm.cai.util.ZipFiles;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Service(value = "employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	int i = 1;

	public void employeeAdd() throws Exception {
		System.out.println("Insert process starts ******************");
		// EmployeeDAO.printTotalRecourdAndLastPk();
		long startTime = System.nanoTime();
		System.out.println("Current  Date & Time :- " + new Date());
		int tRecord = Integer.parseInt(SecurityConfig.hash_map.get("TotalRecords"));
		System.out.println("Total Records to insert******************: " + tRecord);

		EmployeeDAO empDAO = new EmployeeDAO();
		empDAO.add(0, tRecord);
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;

		System.out.println("Total Insert Response Time  in Seconds: " + timeElapsed / 1000000 * 0.001);

	}

	@SuppressWarnings("unchecked")
	@Scheduled(initialDelay = 1000, fixedRate = 30000)
	public void ListEmployeeWithMultithreadAPI() throws Exception {
		System.out.println("Read process starts ******************");
		ActorSystem actorSystem = ActorSystem.create();
		RouterRequest routerRequest = new RouterRequest();
		int threadCount = Integer.parseInt(SecurityConfig.hash_map.get("Threadcount"));
		int pSize = Integer.parseInt(SecurityConfig.hash_map.get("PageSize"));
		routerRequest.setPageNumber(threadCount);
		routerRequest.setPageSize(pSize);
		long startTime = System.nanoTime();
		System.out.println("Current  Date & Time :- " + new Date());
		ActorRef readFileActorRouter = actorSystem.actorOf(Props.create(RequestActorRouter.class));
		Timeout timeout = new Timeout(Duration.create(15, TimeUnit.MINUTES));
		Future<Object> future = Patterns.ask(readFileActorRouter, routerRequest, timeout);
		List<EmployeeUser> dataList = (List<EmployeeUser>) Await.result(future, timeout.duration());

		String filedir = System.getProperty("user.home") + "/NBC_CSV/NBC_CSV"+i;
		String filename = System.getProperty("user.home") + "/NBC_CSV/NBC_CSV"+i+".csv";
		CsvFileWriter.writeCsvFile(filename, dataList);
	
		zipCSV(filedir,filename);
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		System.out.println("process end******************");
		// System.out.println("Response Data : - " + dataList.size());
		System.out.println("Total Response Time  in Seconds: " + timeElapsed / 1000000 * 0.001);
		actorSystem.stop(readFileActorRouter);
		i++;
	}

	public static void zipCSV(String filedir,String fileName) {

		createDirectory(filedir);
		File dir = new File(filedir);
		StringUtil zipFiles = new StringUtil();
		zipFiles.zipDirectory(dir, filedir+"/NBC_CSV.ZIP");
		System.out.println("Zip is created");
		//copy CSV file to zip directory
		
		System.out.println("Copy  CSV  & TIFF and create  Zip with max 2 GB limit  ");
	}
	
	public static void createDirectory(String PATH)
	
	{

	

        File file = new File(PATH);

        // true if the directory was created, false otherwise
        if (file.mkdirs()) {
            System.out.println("Directory is created!");
        } else {
            System.out.println("Failed to create directory!");
        }
		 
	}


}
