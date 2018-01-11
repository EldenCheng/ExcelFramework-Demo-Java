package com.wesoft.codetest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.function.Executable;

public class exec implements Executable {
	public exec() {
		
	}

	public void execute() throws Throwable {
		//System.out.print("The init count is: ");
		//System.out.println(count);
		countadd();
		//System.out.print("The added count is: ");
		//System.out.println(count);
		//System.out.print("The new count is: ");
		//System.out.println(count);
		Object[] l = ctcno.toArray();
		int j=(int) l[count];
		System.out.print("Current teno is: ");
		System.out.println(j);
	}
	
	public static void set_tcno(int i) {
		ctcno.add(i);
		//ctcno=i;
		//System.out.print("The value import is: ");
		//System.out.println(i);
	}
	
	private static void countadd() {
		count=count+1;
	}
	private static Set<Integer> ctcno = new HashSet();
	private static int count=-1;
	

}
