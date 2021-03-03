package com.ibm.cai.util;

import java.io.FileWriter;
/*
 * @auth rajasood@in.ibm.com
 */
import java.io.IOException;
import java.util.List;

import com.ibm.cai.employee.model.EmployeeUser;

public class CsvFileWriter {

	// Delimiter used in CSV file

	private static final String COMMA_DELIMITER = ",";

	private static final String NEW_LINE_SEPARATOR = "\n";

	private static final String FILE_HEADER = "id,age,salary,firstName,lastName";

	public static void writeCsvFile(String fileName, List<EmployeeUser> dataList) {

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName);

			fileWriter.append(FILE_HEADER.toString());

			fileWriter.append(NEW_LINE_SEPARATOR);

			for (EmployeeUser employeeDTO : dataList) {

				fileWriter.append(String.valueOf(employeeDTO.getId()));

				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append("" + employeeDTO.getAge());

				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(String.valueOf(employeeDTO.getSalary()));

				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(employeeDTO.getFirst_name());

				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(employeeDTO.getLast_name());

				fileWriter.append(NEW_LINE_SEPARATOR);

			}

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {

			System.out.println("Error in CsvFileWriter !!!");

			e.printStackTrace();

		} finally {

			try {

				fileWriter.flush();

				fileWriter.close();

			} catch (IOException e) {

				System.out.println("Error while flushing/closing fileWriter !!!");

				e.printStackTrace();

			}

		}

	}}

