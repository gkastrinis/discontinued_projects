package com.codemonkeys.crmlite.model;

public class Notification {

	static final String schema = ""
			+ "DROP TABLE IF EXISTS Notification ; "

			+ "PRAGMA foreign_keys = ON ; "

			+ "CREATE TABLE Notification ("
			+ "id INTEGER PRIMARY KEY, "
			+ "date INTEGER, "
			+ "duration INTEGER,"
			+ "FOREIGN KEY(id) REFERENCES Event(id) "
			+ ") ; ";
}
