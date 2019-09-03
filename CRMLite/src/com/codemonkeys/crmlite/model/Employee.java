package com.codemonkeys.crmlite.model;

public class Employee {

	static final String schema = ""
			+ "DROP TABLE IF EXISTS Employee ; "
			
			+ "PRAGMA foreign_keys = ON ; "

			+ "CREATE TABLE Employee ("
			+ "id INTEGER PRIMARY KEY, "
			+ "alias TEXT, "
			+ "password TEXT,"
			+ "FOREIGN KEY(id) REFERENCES Customer(id) "
			+ ") ; ";

}
