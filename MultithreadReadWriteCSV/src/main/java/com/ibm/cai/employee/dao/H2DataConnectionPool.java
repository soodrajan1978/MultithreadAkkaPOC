package com.ibm.cai.employee.dao;

import java.sql.Connection;

import org.h2.jdbcx.JdbcConnectionPool;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class H2DataConnectionPool {

	private static H2DataConnectionPool h2DataSourse;

	private static ComboPooledDataSource dataSourse;
	private static JdbcConnectionPool jdbcConnectionPool;

	private H2DataConnectionPool() throws Exception {
		dataSourse = new ComboPooledDataSource();
		dataSourse.setDriverClass("org.h2.Driver");
		dataSourse.setJdbcUrl("jdbc:h2:mem:test;LOCK_MODE=0");
		dataSourse.setUser("sa");
		dataSourse.setPassword("");

		dataSourse.setMaxPoolSize(5);
		dataSourse.setMinPoolSize(1);
		dataSourse.setAcquireIncrement(10);
		
		System.out.println("Test connection jdbc:h2:mem:test");

	}

	static {
		jdbcConnectionPool = getConnectionPool();
	}

	public static H2DataConnectionPool getInstance() throws Exception {

		if (h2DataSourse == null) {
			synchronized (H2DataConnectionPool.class) {
				h2DataSourse = new H2DataConnectionPool();
			}
		}

		return h2DataSourse;
	}

	public static Connection getConnection() throws Exception {

	//	System.out.println("current active connection pool" + jdbcConnectionPool.getActiveConnections());
		return jdbcConnectionPool.getConnection();

		// return getInstance().dataSourse.getConnection();
	}

	public static void closeConnection(Connection conn) throws Exception {

		if (conn != null) {
			conn.close();
		}
	}

	public static void closePool() {

		if (dataSourse != null) {
			dataSourse.close();
		}
	}

	private static JdbcConnectionPool getConnectionPool() {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return JdbcConnectionPool.create("jdbc:h2:mem:test;LOCK_MODE=0", "sa", "");
	}

}
