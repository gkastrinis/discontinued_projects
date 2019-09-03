package com.codemonkeys.crmlite.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {

	static final String schema = ""
			+ "DROP TABLE IF EXISTS Customer ; "
			
			+ "CREATE TABLE Customer ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "firstName TEXT NOT NULL, "
			+ "middleName TEXT, "
			+ "lastName TEXT NOT NULL, "
			+ "age INTEGER NOT NULL, "
			+ "sex INTEGER NOT NULL, "
			+ "address TEXT NOT NULL, "
			+ "phone TEXT NOT NULL, "
			+ "mobile TEXT, "
			+ "email TEXT, "
			+ "comments TEXT "
			+ ") ; ";
	
	public int id;
	public String firstName;
	public String middleName;
	public String lastName;
	public int age;
	public String sex;
	public String address;
	public String phone;
	public String mobile;
	public String email;
	public String comments;

	public Customer(int id) {
		Connection connection = Database.connect();
		String query = "SELECT * FROM Customer WHERE id = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			if ( !rs.next() ) { id = -1; return; }

			Database.setFields(this, rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
