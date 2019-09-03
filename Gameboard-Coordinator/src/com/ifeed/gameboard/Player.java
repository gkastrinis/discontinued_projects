package com.ifeed.gameboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Player {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	boolean isConnected;
	String localAddress;
	String remoteAddress;
	
	public Player() {}
	
	public Player(Player p) {
		this.socket = p.socket;
		this.in = p.in;
		this.out = p.out;
		this.isConnected = p.isConnected;
		this.localAddress = p.localAddress;
		this.remoteAddress = p.remoteAddress;
	}

	// Client side
	public boolean connect(String host, int port) {
		try {
			socket = new Socket(host, port);
			connectAux();
		} catch (IOException e) {
			Log.err(this, "connect");
		}
		return isConnected;
	}
	
	// Server side
	public boolean connect(Socket socket) {
		this.socket = socket;
		connectAux();
		return isConnected;
	}
	
	void connectAux() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			localAddress = socket.getLocalAddress().toString();
			remoteAddress = socket.getRemoteSocketAddress().toString();
			isConnected = true;
		} catch (IOException e) {
			Log.fail(this, "connectAux");
		}
	}
	
	public void disconnect() {
		try {
			in.close();
			out.close();
			socket.close();
			isConnected = false;
		} catch (IOException e) {
			Log.fail(this, "disconnect");
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
//	public boolean isConnectedRemotely() {
//		return true;
		//		try {
//			socket.getOutputStream().write(42);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return true;
//		int status = 0;
//		try {
//			boolean f = socket.isInputShutdown();
//			SocketChannel ch = socket.getChannel();
//			socket.getChannel().configureBlocking(false);
//			Selector selector = Selector.open();
//			SelectionKey skey = socket.getChannel().register(selector, SelectionKey.OP_READ);
//			boolean foo = skey.isReadable();
//			status = socket.getChannel().read(ByteBuffer.allocateDirect(0));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return status != -1;
//	}
	
	
	public void send(JSONObject json) {
		out.println(json.toJSONString());
		Log.out(String.format("%s (%s send %s)", json.toJSONString(), localAddress, remoteAddress));
	}
	
	public JSONObject receive() {
		try {
			JSONObject json = (JSONObject) JSONValue.parse(in.readLine());
			//Log.out(String.format("%s (%s read %s)", json.toJSONString(), localAddress, remoteAddress));
			return json;
		} catch (IOException e) {
			Log.fail(this, "readJSON");
		    return null;
		}
	}
}