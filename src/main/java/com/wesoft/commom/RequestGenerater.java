package com.wesoft.commom;


import org.junit.platform.launcher.LauncherDiscoveryRequest;

import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

public class RequestGenerater {
	
	public static LauncherDiscoveryRequest generateRequest() throws Throwable {
		LauncherDiscoveryRequest r=null;
		String errorlog =null;
		Object[] dt=null;
		Excel exc=null;
		
    	try {
    	exc= new Excel(Constants.Get_DATAPATH());
    	exc.selectSheetByName("summary");
    	dt= exc.getDatasetAccordly("Case No", "execute", "yes");
    	} catch(Throwable e) {
    		errorlog = errorlog + "\nError on loading test data; Error message is:" + e.getMessage();
    	}
		
		String script1 = "import static org.junit.platform.engine.discovery.DiscoverySelectors.*;\r\n" + 
				" import static org.junit.platform.engine.discovery.ClassNameFilter.*;\r\n" + 
				" import static org.junit.platform.launcher.EngineFilter.*;\r\n" +
				" import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;" +
				" import com.wesoft.testcases.*;" +
				" import static org.junit.platform.launcher.TagFilter.*;";		
		String script2 = "LauncherDiscoveryRequestBuilder rb =LauncherDiscoveryRequestBuilder.request();";
		String script3="rb.selectors(";
		String script5=null;
		//Dynamic add the test cases which is set execute in test data summary
		for(Object d: dt) {
			String sc = (String)exc.getValueByColname("Script", new Double((double)d).intValue());
			if(sc.toLowerCase().equals("done")) {
				script5 = script5 + ",selectClass(tc" + Integer.toString(new Double((double)d).intValue()) +"_tests.class)";
			}		
		}
		script3 = script3 + script5.substring(5) + ");\r\n";		
		String script4 = "rb.filters(includeClassNamePatterns(\".*tests\"));\r\n" + 
				"return rb.build();";
		
		IScriptEvaluator se = new ScriptEvaluator();
		se.setReturnType(LauncherDiscoveryRequest.class);
		se.cook(script1+script2+script3+script4);
		r = (LauncherDiscoveryRequest) se.evaluate(new Object[0]);
		
		return r;
	}
	
}
