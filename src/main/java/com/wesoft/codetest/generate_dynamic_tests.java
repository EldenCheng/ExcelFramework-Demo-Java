package com.wesoft.codetest;

import com.wesoft.commom.Constants;
import com.wesoft.commom.Excel;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.commons.compiler.CompileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


public class generate_dynamic_tests {

    private Object[] tcno;
    private String[] tcdsp;
	private Excel exc;
	private String errorlog;
	
    @TestFactory
    @DisplayName("To add tests according to the testcases list")
    public Collection<DynamicTest> running_tests() throws Throwable {
        List<DynamicTest> tests = new LinkedList<>();
        
    	try {
    	exc= new Excel(Constants.Get_DATAPATH());
    	exc.selectSheetByName("summary");
    	tcno = exc.getDatasetAccordly("Case No", "execute", "yes");
    	//tcdsp = (String[])exc.Get_dataset_accordly("Description", "execute", "yes");
    	} catch(Throwable e) {
    		errorlog = errorlog + "\nError on loading test data; Error message is:" + e.getMessage();
    	}

        for(Object no : tcno) {
        	//System.out.println(no);
        	String script = (String)exc.getValueByColname("Script", new Double((double)no).intValue());
        	if(script.toLowerCase().equals("done")) {
	        	//Add the need to run test case number into a List first
	        	test_executer.set_tcno(new Double((double)no).intValue());
	        	//Using DynamicTest feature of Junit5 to add those test cases need to be run into test plan
	        	//Junit5 Runner will auto run the test plan and execute the execute function of test_executer class
	        	tests.add(DynamicTest.dynamicTest((String)exc.getValueByColname("Description", new Double((double)no).intValue()), new test_executer()));
        	}
        }
        return tests;    
    }
}
