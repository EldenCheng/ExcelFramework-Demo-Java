package com.wesoft.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class tc3_tests {
	
    @Test
    @DisplayName("Open Baidu in Firefox")
    public void test_start() {
    	try {
			WebDriver my_dr = new FirefoxDriver();
			my_dr.get("http://www.baidu.com");
			Thread.sleep(1000);
			my_dr.findElement(By.id("kw")).sendKeys("Junit");
			Thread.sleep(1000);
			my_dr.findElement(By.id("su")).click();
			Thread.sleep(1000);
			
			System.out.println(my_dr.getTitle());
			Thread.sleep(1000);
			my_dr.quit();
    	}catch (Throwable e) {
    		fail("There is a failure in testing!");
    	}
    }

}
