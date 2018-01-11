package com.wesoft.commom;

import com.wesoft.commom.WebPage;
import com.wesoft.commom.Constants;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;

public class webpage_tests {
	
	@Disabled
	@Test
    @DisplayName("To test webpage start up")
	public void startup_start() {

		try {
			WebPage page = new WebPage("firefox", "0","0");
			WebDriver drv = page.startUp(Constants.Get_URL());
			//System.out.println(drv);
    		Thread.sleep(1000);
    		//drv.quit();
			page.End();
		} catch (Throwable e) {
			e.printStackTrace();
			//fail(e.getCause().getCause().toString());
		}
		
	}
	
	@Disabled
	@Test
    @DisplayName("To test webpage login")
	public void login_start() {

		try {
			WebPage page = new WebPage("chrome", "0", "0");
			WebDriver drv = page.startUp(Constants.Get_URL());
			//System.out.println(drv);
    		Thread.sleep(1000);
    		page.Login("jack.l", "abc@12345");
    		//drv.quit();
    		page.Logout();
    		Thread.sleep(2000);
			page.End();
		} catch (Throwable e) {
			e.printStackTrace();
			//fail(e.getCause().getCause().toString());
		}
		
	}
	
	//@Disabled
	@Test
    @DisplayName("To test snapshot")
	public void shnapshot_start() {
        WebPage page=null;
		try {
			//Report.createReportFolderAndFile();
			page = new WebPage("chrome", "0", "0");
			WebDriver drv = page.startUp(Constants.Get_URL());
			page.takeSnapshot("0", "0", "Success");
			//System.out.println(drv);
    		Thread.sleep(1000);
    		//page.Login("jack.l", "abc@12345");
    		//drv.quit();
    		//page.Logout();
    		Thread.sleep(2000);
			page.End();
		} catch (Throwable e) {
			e.printStackTrace();
			page.takeSnapshot("0", "0", "Fail");
			//fail(e.getCause().getCause().toString());
		}
		
	}

}
