package com.ifeed.gameboard.makabana;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.ifeed.gameboard.Log;

public class Config {
	public static Config instance;

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}
	
	static final String CONFIG_FILE = "config.properties";
	final static String USER_CONFIG_FILE = System.getProperty("user.home") + "/maka-bana.conf";
	Properties prop;

	private Config() {
		Properties generalProp = new Properties();
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
			generalProp.load(input);			
		} catch (IOException e) {
			Log.fail(this, "general properties");
		}
		
		try (InputStream input = new FileInputStream(USER_CONFIG_FILE)) {
			prop = new Properties(generalProp);
			prop.load(input);
		} catch (IOException e) {
			Log.err(this, "get user config");
			prop = generalProp;
		}
	}
	
	public String get(String key) {
		return prop.getProperty(key);
	}
	
	public void putUserConf(String key, String value) {
		try (OutputStream output = new FileOutputStream(USER_CONFIG_FILE)) {
			prop.setProperty(key, value);
			prop.store(output, null);
		} catch (IOException e) {
			Log.fail(this, "put user config");
		}
	}
	
	public Image getImage(String key) {
		return getImage0(prop.getProperty(key));
	}
	
	public Image getRevealImage(String key, boolean paint) {
		String name = prop.getProperty(key).replaceAll(".jpg$", ".png");
		String path = String.format("reveals/%s%s", paint ? "paint_" : "", name);
		return getImage0(path);
	}

	Image getImage0(String path) {
		Image img = null;
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(path)) {
			img = ImageIO.read(is);
		} catch (IOException e) {
			Log.fail(this, "getImage");
		}
		return img;
	}
}
