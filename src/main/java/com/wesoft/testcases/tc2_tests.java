package com.wesoft.testcases;

import com.wesoft.commom.WebPage;
import com.wesoft.Alias.CSS.LoginPage;
import com.wesoft.Alias.CSS.StartPage;
import com.wesoft.commom.Constants;
import com.wesoft.commom.Excel;
import com.wesoft.commom.Report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import org.openqa.selenium.WebDriver;

public class tc2_tests {
	
	private String errorlog =null;
	private Object[] dt;
	private Excel exc;
	private WebPage page;
	private WebDriver drv;
	private String tcno="2";
	private String pass_fail = "fail"; 

    @Test
    @DisplayName("Login with correct ID/PW")
    public void test_start() {
    	int trials=1;
    	int dts=0;
    	try {
    	exc= new Excel(Constants.Get_DATAPATH());
    	exc.selectSheetByName(tcno);
    	exc.setTCNO(tcno);
    	dt= exc.getDatasetAccordly("Data set", "executed", "Not Yet");
    	} catch(Throwable e) {
    		pass_fail="fail";
    		errorlog = errorlog + "\nError on loading test data; Error message is:" + e.getMessage();
    	}

    	for(Object d:dt) {
  		    trials = trials++;
  		    dts=new Double((Double)d).intValue();
    		try {
        		 page = new WebPage((String)exc.getValueByColname("Browser", dts), tcno, Integer.toString(dts));
        		 WebDriver drv = page.startUp(Constants.Get_URL());
        		 Thread.sleep(1000);
        		 String id = (String)exc.getValueByColname("ID", dts);
        		 String pw = (String)exc.getValueByColname("PW", dts);
        		 page.Login(id, pw);
        		 Thread.sleep(1000);
        		 
        		 //If success login and switch to Start page
        		 if(drv.getTitle().equals(StartPage.TXT_PAGETITLE)) {
            		 if(page.textIncludedAssertion(StartPage.LB_LOGONUSERNAME, (String)exc.getValueByColname("Assertion", dts))) {
            			 //page.Logout();
            			 pass_fail= "pass";
            			 System.out.println("Success to verify assertion on dataset " + Integer.toString(dts));
            		 }
            		 else {
            			 //page.Logout();
            			 pass_fail="fail";
            			 throw new Exception("Fail to verify assertion on dataset " + Integer.toString(dts));
            		 }
        		 }
        		 //Or still stay in login page
        		 else if(drv.getTitle().equals(LoginPage.TXT_PAGETITLE)) {
        			 //If need to input verification code
 					if(page.textEqualAssertion(LoginPage.LB_ERRORPROMPT, "Please input verification code.")) {
 						page.verificationCode(pw);	
 						//If occurred fail prompt after input verification code
                        if(page.textEqualAssertion(LoginPage.LB_ERRORPROMPT, "Login credential or verification code is not correct.")) {
                        	pass_fail="fail";
        				    throw new Exception("Login fail with incorrect username: " + id + " and password: " + pw);
    					}
                        //Or login success after input verification code
                        else if(drv.getTitle().equals(StartPage.TXT_PAGETITLE)){
                        	if(page.textIncludedAssertion(StartPage.LB_LOGONUSERNAME, (String)exc.getValueByColname("Assertion", dts))) {
                   			     pass_fail="pass";
                        		 //page.Logout();
                   			     System.out.println("Success to verify assertion on dataset " + Integer.toString(dts));
                   		    }
                   		     else {
                   			     //page.Logout();
                   		    	pass_fail="fail";
                   			    throw new Exception("Fail to verify assertion on dataset " + Integer.toString(dts));
                   		     }
                        }
                        else {
                        	pass_fail="fail";
               			    throw new Exception("Login fail with incorrect username: " + id +  " and password: " + pw + " on dataset " + Integer.toString(dts));
                        }
    				}
 					else {
 						pass_fail="fail";
 						throw new Exception("Login fail with incorrect username: " + id + " and password: " + pw);
 					}
        		 }
        	} catch (Throwable e) {
        		//e.printStackTrace();
        		errorlog = errorlog + "\nError on datset " + Integer.toString(dts) + 
        				"; Error message is: " + e.getMessage();
        		
        	}
    		
        	try {
        		Report.GenerateTCReport(page, pass_fail, tcno, dts);
        	}catch(Throwable e) {
        		//e.printStackTrace();
        		errorlog = errorlog + "\nError on generating report for datset " + Integer.toString(dts) + 
            				"; Error message is: " + e.getMessage();
        	}
        	try {
        		if(page.getWebDriver().getTitle().equals(LoginPage.TXT_PAGETITLE) == false) {
        			page.Logout();
        		}
        	}
        	catch(Throwable e2) {

        	}
        	try {
                page.End();
        	}catch(NullPointerException e) {
        		
        	}
    	}
    	try {
    		Report.GenerateFinalReport(tcno, false);
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
