package com.sonja.url;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * 
 * */
public class StringAsker {
	private final Scanner scanner;
	private final PrintStream out;

	/**
	 * Constructor. 
	 * @param in an InputStream
	 * @param out a PrintStream
	 * */	
	public StringAsker(InputStream in, PrintStream out) {
		scanner = new Scanner(in);
		this.out = out;
	}

	/**
	 * Prints output to command line and reads the corresponding input
	 * @param message the String that will be output to user
	 * @return user input
	 */
	public String ask(String message) {
		out.println(message);
		return scanner.nextLine();
	}
}