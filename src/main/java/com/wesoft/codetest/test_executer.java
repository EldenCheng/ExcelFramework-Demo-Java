package com.wesoft.codetest;


import java.util.LinkedList;
import java.util.List;


import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.jupiter.api.function.Executable;

import com.wesoft.testcases.*;

public class test_executer implements Executable {

	public void execute() throws Throwable {

		Object[] l = all_tcno.toArray();
		int j=(int) l[executed_count];
		
        //Dynamic generate the Java code
        String script1 = "com.wesoft.testcases.tc" + Integer.toString(j)+
                "_tests tc = new com.wesoft.testcases.tc" + Integer.toString(j) + "_tests();";
        String script2 = "tc.test_start();";
        
        // Create "ScriptEvaluator" object.
        IScriptEvaluator se = new ScriptEvaluator();
        // No return from the dynamic Java code
        se.setReturnType(null);
        //Prepare the Java code
        se.cook(script1+script2);
        //Evaluate script with actual parameter values.
        se.evaluate(new Object[0]); // no any argument
        
		//Add the counter after executed a test case
		executed_countadd();
	}
	
	//Add all test cases number which need to be run
	public static void set_tcno(int testcasenum) {
		all_tcno.add(testcasenum);
	}
	
	public static void set_reprotpath(String reportpath) {
		rppath=reportpath;
	}
	
	//If a test case in the test plan is executed, counter add 
	private static void executed_countadd() {
		executed_count=executed_count+1;
	}
	private static List<Integer> all_tcno = new LinkedList<Integer>();
	private static int executed_count=0;
	private static String rppath;
	

}
