package com.ifeed.gameboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;

public class Coordinator {
	
	ServerSocket connectionSocket;

	public boolean start(int port) {
		boolean success = false;
		try {
			connectionSocket = new ServerSocket(port);
			Log.out(this, "start");
			success = true;
		} catch (IOException e) {
			Log.err(this, "start");
		}
		return success;
	}
	
	public void stop() {
		try {
			connectionSocket.close();
			Log.out(this, "stop");
		} catch (IOException e) {
			Log.fail(this, "stop");
		}
	}
	
	public Player accept() {
		try {
			Player p = new Player();
			p.connect(connectionSocket.accept());
			Log.out(this, "accept");
			return p;
		} catch (IOException e) {
			Log.err(this, "accept");
		    return null;
		}
	}
	
	
	public static String getLocalIP() {
		String ip = null;
        try {
            URL url = new URL("http://bot.whatismyipaddress.com");
            ip = (new BufferedReader(new InputStreamReader(url.openStream()))).readLine().trim();
            if (ip.isEmpty())
            	ip = InetAddress.getLocalHost().getHostAddress();
		} catch (IOException e) {
			Log.fail(null, "getLocalIP");
		}
        return ip;
	}
}