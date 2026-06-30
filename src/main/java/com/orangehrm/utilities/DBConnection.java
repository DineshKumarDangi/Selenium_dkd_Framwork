package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {
	/*private static final String DB_URL = "jdbc:mysql://localhost:3307/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORDE = "";
	*/
	// Reading credentials from config.properties file
	private static final String DB_URL = BaseClass.getProp().getProperty("DB_URL");
	private static final String DB_USERNAME = BaseClass.getProp().getProperty("DB_USERNAME");
	private static final String DB_PASSWORDE = BaseClass.getProp().getProperty("DB_PASSWORDE");
	private static final Logger logger = BaseClass.logger;
	
	public static Connection getDBConnection() {
		logger.info("Strating DB connection...");
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORDE);
			logger.info("DB connection successful");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while establishing DB connection");
			e.printStackTrace();
		}
		return null;
	}

	// Get the employee details from DB and store in a map
	public static Map<String, String> getEmployeeDetails(String employeeId) {

		String query = "SELECT emp_firstname, emp_middle_name , emp_lastname FROM hs_hr_employee where employee_id="
				+ employeeId;
		Map<String, String> employeeDetails = new HashMap<>();
		try (Connection conn = getDBConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			logger.info("Executing query: " + query);
			if (rs.next()) {
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lastName = rs.getString("emp_lastname");
				// store in map
				employeeDetails.put("firstName", firstName);
				employeeDetails.put("middleName", middleName != null ? middleName : "");
				employeeDetails.put("lastName", lastName);
				logger.info("Querry Executes Successfully");
				logger.info("Employee Data Fetched" + employeeDetails);
			} else {
				logger.error("Employee not found");
			}

		} catch (Exception e) {
			logger.error("Error while executing query");
			e.printStackTrace();
		}
		return employeeDetails;
	}
}
