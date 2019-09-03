package com.codemonkeys.crmlite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	static final String configFile = "config.properties";
	
	public static int majorVersion;
	public static int minorVersion;
	public static String title;
	public static String language;
	public static String author;


	public static void init() {
		try {
			InputStream input = Main.class.getClassLoader().getResourceAsStream(configFile);
			
			Properties prop = new Properties();
			prop.load(input);

			majorVersion = Integer.parseInt( prop.getProperty("majorVersion") );
			minorVersion = Integer.parseInt( prop.getProperty("minorVersion") );
			title = prop.getProperty("title");
			language = prop.getProperty("language");
			author = prop.getProperty("author");
			
			input.close();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
