package com.ifeed.gameboard;

public class Log {
	public static void out(String msg) {
		System.out.println(msg);
	}
	
	public static void out(Object obj, String msg) {
		if (obj == null) System.out.println("static: " + msg);
		else System.out.println(obj.getClass() + ": " + msg);
	}
	
	public static void err(Object obj, String msg) {
		if (obj == null) System.err.println("static: " + msg);
		else System.err.println(obj.getClass() + ": " + msg);
	}
	
	public static void fail(Object obj, String msg) {
		err(obj, msg);
		System.exit(-1);
	}
}
