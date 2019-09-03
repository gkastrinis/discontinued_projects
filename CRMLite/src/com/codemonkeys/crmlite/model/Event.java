package com.codemonkeys.crmlite.model;

public class Event {

	static final String schema = ""
			+ "DROP TABLE IF EXISTS Event ; "

			+ "CREATE TABLE Event ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "date INTEGER, "
			+ "status INTEGER, "
			+ "description TEXT "
			+ ") ; ";

}
