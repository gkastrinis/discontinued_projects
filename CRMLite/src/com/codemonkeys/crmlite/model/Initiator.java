package com.codemonkeys.crmlite.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Initiator {

	public void init() {
		Connection connection = Database.connect();

		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			if ( !Database.exists() ) {
				statement.executeUpdate(Customer.schema);
				statement.executeUpdate(Employee.schema);
				statement.executeUpdate(Event.schema);
				statement.executeUpdate(Notification.schema);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
