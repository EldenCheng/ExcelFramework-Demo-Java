package com.wesoft.commom;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;


import com.wesoft.commom.Constants;
import com.wesoft.Alias.CSS.*;

public class WebPage {
	
	private WebDriver driver;
	private GeckoDriverService geckosrv;
	private ChromeDriverService chrsrv;
	private InternetExplorerDriverService iesrv;
	private String browser;
	private int steps;
	private String tcno;
	private String datasetno;
	
	public WebPage(String br, String tc_no, String data_set_no) throws Throwable {
		browser=br;
		steps=1;
		tcno=tc_no;
		datasetno=data_set_no;
		if(Report.getReportFolderPath() == null) {
			Report.createReportFolderAndFile();
		}
		
		if(browser.toLowerCase().equals("firefox")) {

			System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Constants.Get_GECKODRIVERPATH());
			geckosrv = GeckoDriverService.createDefaultService();
			//Specify the output stream of gecko to a file to avoid t0o much info print on console
			geckosrv.sendOutputTo(new FileOutputStream("gecko_log.txt"));
            geckosrv.start();
            
            //To disable insecure connection warning which will cover the password filed
            FirefoxProfile firefoxProfile=new FirefoxProfile();
            firefoxProfile.setPreference("security.insecure_password.ui.enabled",false);
            firefoxProfile.setPreference("security.insecure_field_warning.contextual.enabled",false);
            
            //Set the Firefox log level to Fatal
            FirefoxOptions opts = new FirefoxOptions();
            opts.setLogLevel(FirefoxDriverLogLevel.FATAL);
            opts.setProfile(firefoxProfile);
            //opts.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			
            driver= new FirefoxDriver(geckosrv, opts);
		}
		else if(browser.toLowerCase().equals("chrome")) {
		    
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, Constants.Get_CHROMEDRIVERPATH());
			System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "Chrome_driver.log");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			chrsrv = ChromeDriverService.createDefaultService();
			chrsrv.start();
			
			//Set the Chrome log level to Fatal and silent
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--log-level=3");
			chromeOptions.addArguments("--silent");
			
			driver = new ChromeDriver(chrsrv, chromeOptions);
		}
        else if(browser.toLowerCase().equals("ie")) {
        	iesrv = new InternetExplorerDriverService.Builder().usingDriverExecutable(  
                    new File(Constants.Get_IEDRIVERPATH())) .usingAnyFreePort().build();
        	iesrv.start();
        	
        	driver = new InternetExplorerDriver(iesrv);
		}
        else if(browser.toLowerCase().equals("edge")) {
        	
        	throw new Exception("Had not been support edge yet!");   
		}
        else {
        	
        	throw new Exception("Had not been support \"" + browser + "\" browser yet!");
        }				
	}

	public WebDriver startUp(String url) throws Throwable {
			
		try {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get(url);
		} catch (Throwable e) {
			
			throw new Exception("Cannot open the url" +
                                "The Exception is: " + e.getMessage());
		}
		
		try {
			driver.manage().window().maximize();
		} catch(Throwable e) {
			
		}
		this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
		steps = steps + 1;
		return driver;
	}
	
	public void Login(String username, String password) throws Throwable {
		
		try {
			if(driver.getTitle().equals(ExceptionPage.TXT_PAGETITLE)) {
				this.clickElement(ExceptionPage.LINK_LOGIN);
			}
			
			if(new WebDriverWait(driver, 10, 500).until(ExpectedConditions.titleIs(LoginPage.TXT_PAGETITLE))) {
				this.input_Text(LoginPage.TXTF_ID, username);
				this.input_Text(LoginPage.TXTF_PW, password);
				this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
				steps = steps + 1;
				this.clickElement(LoginPage.BTN_LOGIN);
				Thread.sleep(1000);
			}
			
		} catch (Throwable e) {
			
			throw new Exception("Login fail with username: " + username + " and password: " + password +
                                "The Exception is: " + e.getMessage());
		}
		
	}
	
	public void verificationCode(String password) throws Throwable {
		try {
			WebDriverWait wt = new WebDriverWait(driver, 60, 500);
			this.input_Text(LoginPage.TXTF_PW, password);
			Thread.sleep(100);
			String Jscript1 = "window.promptResponse=prompt('Please input the Verification Code Manually')";
			String Jscript2 = "return window.promptResponse";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			//As prompt value cannot return directly from JS executer, so need to speared to 2 parts
			//Step 1, pop up a prompt and let user input
			js.executeScript(Jscript1);
			//Step 2, wait until user inputed and click OK button, the get the return value
			String code = (String) wt.until(ExpectedConditions.jsReturnsValue(Jscript2));

			if(browser.toLowerCase().equals("firefox")) {
				//As Firefox always report Unhandled alert exception after used Javacript prompt
				//So let the browser pop another alert than handle it by selenium to avoid this exception
				js.executeScript("alert(\"Just for firefox to handle back alert\")");
				Thread.sleep(100);
				driver.switchTo().alert().accept();
				Thread.sleep(100);
				js.executeScript("return \"abc\"");
				Thread.sleep(500);
				this.input_Text(LoginPage.TXTF_VCODE, code);
				Thread.sleep(500);
				this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
				steps = steps + 1;
				this.clickElement(LoginPage.BTN_LOGIN);
			}
			else {
				this.input_Text(LoginPage.TXTF_VCODE, code);
				this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
				steps = steps + 1;
				this.clickElement(LoginPage.BTN_LOGIN);
			}	
		} catch (Throwable e) {
			//e.printStackTrace();
			throw new Exception("The element not found, unclickable or input verification code time out!\n" + 
                                "The Exception is: " + e.getMessage());
		}
	}
	
	public void clickElement(String locater) throws Exception {
		try {
			new WebDriverWait(driver, 5, 500).until(ExpectedConditions.elementToBeClickable(By.cssSelector(locater)));
			driver.findElement(By.cssSelector(locater)).click();
		} catch (Throwable e) {
			throw new Exception("The element not found or unclickable!\n" + 
		                        "The Exception is: " + e.getMessage());
		}
		
	}
	
	public void input_Text(String locater, String text) throws Throwable {
		try {
			new WebDriverWait(driver, 5, 500).until(ExpectedConditions.elementToBeClickable(By.cssSelector(locater)));
			driver.findElement(By.cssSelector(locater)).clear();
			driver.findElement(By.cssSelector(locater)).sendKeys(text);
		} catch (Throwable e) {
			throw new Exception("The element not found or uninputable!\n" + 
		                        "The Exception is: " + e.getMessage());
		}
	}
	
	public boolean textEqualAssertion(String locater, String textToVerify) throws Throwable {
		boolean result=true;
		
		try {
			new WebDriverWait(driver, 5, 500).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locater)));
			JavascriptExecutor js= (JavascriptExecutor)driver;
			WebElement el = driver.findElement(By.cssSelector(locater));
			js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", el);
			this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
			steps = steps + 1;
			if(el.getText().equals(textToVerify)){
				result=true;
			}
			else {
				result=false;
			}
		} catch (Throwable e) {
			throw new Exception("The element not found\n" + 
                                "The Exception is: " + e.getMessage());
		}
		
		return result;
	}
	
	public boolean textIncludedAssertion(String locater, String textToVerify) throws Throwable {
		boolean result=true;
		
		try {
			new WebDriverWait(driver, 5, 500).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locater)));
			JavascriptExecutor js= (JavascriptExecutor)driver;
			WebElement el = driver.findElement(By.cssSelector(locater));
			js.executeScript("arguments[0].setAttribute('style', 'border: 5px solid red;');", el);
			this.takeSnapshot(tcno, datasetno, Integer.toString(steps));
			steps = steps + 1;
			if(el.getText().contains(textToVerify)){
				result=true;
			}
			else {
				result=false;
			}
		} catch (Throwable e) {
			throw new Exception("The element not found\n" +
                                "The Exception is: " + e.getMessage());
		}
		
		return result;
	}
	
	public String takeSnapshot(String tc_no, String data_set_no, String steps) {
	    File dir1=null;
	    File dir2=null;
	    File screenshotT=null;
	    File savefile=null;
		try {
			TakesScreenshot s = (TakesScreenshot)driver;
			screenshotT = s.getScreenshotAs(OutputType.FILE);
			if(steps.equals("Success") || steps.equals("Fail")) {
				dir1 = new File(Report.getReportFolderPath() + "/TC" + tc_no);
				dir2 = new File(Report.getReportFolderPath() + "/TC" + tc_no + "/" + steps);
			}
			else {
				dir1 = new File(Report.getReportFolderPath() + "/TC" + tc_no);
				dir2 = new File(Report.getReportFolderPath() + "/TC" + tc_no + "/Steps");
			}
			if(dir1.isDirectory() == false) {
				dir1.mkdir();
			}
			if(dir2.isDirectory() == false) {
				dir2.mkdir();
			}			
			 savefile = new File(dir2.getPath() + "/TC" + tc_no + "_DataSet_" + data_set_no + "_" + steps + ".png");
			Files.copy(screenshotT.toPath(), savefile.toPath());
		}catch(Throwable e) {
			e.printStackTrace();
		}
		
		return savefile.getPath();
	}
	
	public WebDriver getWebDriver() {
		return driver;
	}
	
	public void Logout() throws Throwable{
		this.clickElement("button#btn_logout-btnEl");
	}
	
	public void End(){
		
		driver.quit();

	}

}
