package com.wesoft.codetest;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.function.Executable;

public class exec2 implements Executable {

	public void execute() throws Throwable {
		System.out.println("A Test!");
	}
	
	public static void set_tcno(int i) {
		ctcno.add(i);
		System.out.print("The value import is: ");
		System.out.println(i);
	}
	private static Set<Integer> ctcno;
	

}
