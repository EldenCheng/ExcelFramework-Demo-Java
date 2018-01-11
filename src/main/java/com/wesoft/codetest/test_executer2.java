package com.wesoft.codetest;

import java.lang.reflect.InvocationTargetException;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.jupiter.api.function.Executable;

import com.wesoft.testcases.*;

public class test_executer2 {
	public static Executable executes(int testcasenum) throws CompileException, InvocationTargetException{
		Executable e1 =null; 
        //Generate the Java statement by using the parameters
		//Using the whole package.class because the commons compiler do not know shorten class name here
        String script1 = "com.wesoft.testcases.tc" + Integer.toString(testcasenum)+
                "_tests tc = new com.wesoft.testcases.tc" + Integer.toString(testcasenum) + "_tests();";
        String script2 = "tc.test_start();";
        
        //String script3 = "org.junit.jupiter.api.function.Executable e = () -> {\n" + script1 + "\n" + script2 + "};";
        String script3 = "com.wesoft.commom.test_executer.e = new com.wesoft.commom.exec();\n"
        		+ " try {"
        		+ "com.wesoft.commom.test_executer.e.execute();"
        		+ "		}catch(Throwable e) {"
        		+ "			System.out.println(\"Catched\");\r\n" + 
        		"		}";
        System.out.println(script3);
        // Create "ScriptEvaluator" object.
        IScriptEvaluator se = new ScriptEvaluator();
        //The statements we generate will not return anything
        se.setReturnType(null);
        se.cook(script3);
        //Evaluate script with actual parameter values.
        se.evaluate(new Object[0]); // arguments
        Executable e11 = () -> {com.wesoft.testcases.tc2_tests tc =new com.wesoft.testcases.tc2_tests(); tc.test_start();};
        return e; 
    }
	
	public static Executable e = null;
}
