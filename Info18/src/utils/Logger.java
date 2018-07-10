package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

	public static PrintWriter out;
	
	public static void init(String s) throws IOException {
	    out = new PrintWriter(new BufferedWriter(new FileWriter(new File(s))));
	}
	
	public static void log(Object o) {
		out.println(o);
	}
	
	public static void flush() {
		out.flush();
	}
	
}
