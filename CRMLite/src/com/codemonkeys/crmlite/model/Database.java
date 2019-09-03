package com.codemonkeys.crmlite.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {

	private static Connection connection = null;

	private Database() {}

	public static Connection connect() {
		if (connection != null) return connection;

		try {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");

			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:crmlite.db");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		
		return connection;
	}
	
	public static void disconnect() {
		if (connection == null) return;
		
		try {
			connection.close();
			connection = null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean exists() throws SQLException {
		connection = connect();
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		// Check if table Customer exists ; if yes assume database has been initialized in the past
		return statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Customer' ; ").next();
	}
	
	public static void setFields(Object obj, ResultSet rs) {
		ResultSetMetaData metadata;
		try {
			metadata = rs.getMetaData();
			for (int i = 1, count = metadata.getColumnCount() ; i <= count ; i++)
				obj.getClass().getDeclaredField( metadata.getColumnName(i) ).set(obj, rs.getObject(i));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
