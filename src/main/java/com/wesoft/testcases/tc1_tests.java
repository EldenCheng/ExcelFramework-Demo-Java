package com.wesoft.testcases;

import com.wesoft.commom.WebPage;
import com.wesoft.commom.Constants;
import com.wesoft.commom.Excel;
import com.wesoft.commom.Report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import org.openqa.selenium.WebDriver;

public class tc1_tests {
	
	private String errorlog =null;
	private String[] browsers;
	private Object[] dt;
	private Excel exc;
	private WebDriver drv;
	private String tcno="1";
	
    @Test
    @DisplayName("Open Kerry UAT in multi browsers")
    public void test_start() {    	
    	int trials=0;
    	int dts =0;
    	String pass_fail = "fail";
    	WebPage page=null;
    	WebDriver drv;
    	try {
    	exc= new Excel(Constants.Get_DATAPATH());
    	exc.selectSheetByName(tcno);
    	exc.setTCNO(tcno);
    	dt= exc.getDatasetAccordly("Data set", "executed", "Not Yet");
    	} catch(Throwable e) {
    		fail("\nError on loading test data; Error message is:" + e.getMessage());
    	}

    	for(Object d:dt) {
        	try {
       		     trials = trials++;
       		     dts = new Double((Double)d).intValue();
        		 page = new WebPage((String)exc.getValueByColname("Browser", dts), tcno, Integer.toString(dts));
        		 drv = page.startUp(Constants.Get_URL());
        		 Thread.sleep(1000);
        		 System.out.println("Success on dataset " + Integer.toString(dts));
        		 pass_fail = "pass";
        	} catch (Throwable e) {
        		//e.printStackTrace();
        		errorlog = errorlog + "\nError on datset " + Integer.toString(dts) + 
        				"; Error message is: " + e.getMessage();
        		pass_fail= "fail";
        	}
            
        	try {
        		Report.GenerateTCReport(page, pass_fail, tcno, dts);
        	}catch(Throwable e) {
        		//e.printStackTrace();
        		errorlog = errorlog + "\nError on generating report for datset " + Integer.toString(dts) + 
            				"; Error message is: " + e.getMessage();
        	}
        	try {
                page.End();
        	}catch(NullPointerException e) {
        		
        	}
    	}
    	try {
    		Report.GenerateFinalReport(tcno, true);
    	}catch(Throwable e) {
    		e.printStackTrace();
    		errorlog = errorlog + "\nError on generating report for datset " + Integer.toString(dts) + 
    				"; Error message is: " + e.getMessage();
    	}
    	if(errorlog != null) {
    		fail(errorlog);    		
    	}
    	
    }
}
