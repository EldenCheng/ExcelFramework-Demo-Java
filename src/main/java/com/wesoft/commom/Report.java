package com.wesoft.commom;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.IdentityHashMap;

import org.openqa.selenium.WebDriver;


public class Report {
	
	private static String reportfolderpath;
	private static String reportfilepath;
	private static int finalreportrow=1;
	private static IdentityHashMap<String, IdentityHashMap<String,IdentityHashMap<String,String>>> randomvaluerecoder = new IdentityHashMap<>();
		
	public static void createReportFolderAndFile() throws Throwable{
		String rprootpth = Constants.Get_REPORTPATH();
		
		//Get the formated Date time, and use it as the reportfolder name 
		Date ss = new Date();
		SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        String time = format0.format(ss.getTime());
        
        File dir =new File(rprootpth + "/" + time);
        try {
	        if(dir.isDirectory() == false) {
	        	//If not a directory, create it first
	        	dir.mkdir();
	        }
        	reportfolderpath = dir.getPath();
	        //Copy the data excel file to the report folder, rename it and use it as a report excel file
	        File srcfile = new File(Constants.Get_DATAPATH());
	        File desfile = new File(reportfolderpath + "/" + "Report_" + time + ".xlsx");
	        Files.copy(srcfile.toPath(), desfile.toPath());
	        if(desfile.exists() == false) {
	        	throw new Exception("Copy data excel file to create report excel file fail!");
	        }
	        else {
	        	reportfilepath = desfile.getPath();
	        }
	        
        } catch(Throwable e) {
        	//e.printStackTrace();
        	throw new Exception("Create report folder or report excel file fail!\n" + 
        	                    "The exception cause is: " + e.getMessage());
        }
	}
	
	@SuppressWarnings("unchecked")
	public static void GenerateTCReport(WebPage page, String pass_fail, String tc_no, int data_set_no) throws Throwable {
		Excel ex=null;
		String snapshotpath=null;
		String capturename=null;
		String link = null;
		boolean openpage=true;
		boolean rndthisdataset = false;
		try {
			//Use these methods to check if there is any browser window opened
			WebDriver drv = page.getWebDriver();
			Set<String> wnd = drv.getWindowHandles();
			openpage=true;
		}catch(Throwable e) {
			openpage=false;
		}
		if(openpage) {
			if(pass_fail.equals("Pass")) {
				snapshotpath = "..\\" + page.takeSnapshot(tc_no, Integer.toString(data_set_no), "Success");
				capturename = String.format("TC%s_Dataset_%d_Success.png", tc_no, data_set_no);
			}
			else if(pass_fail.equals("Fail")) {
				snapshotpath = page.takeSnapshot(tc_no, Integer.toString(data_set_no), "Fail");
				capturename = String.format("TC%s_Dataset_%d_Fail.png", tc_no, data_set_no);
			}
		}
		
		try {
			ex = new Excel(reportfilepath);
			ex.selectSheetByName(tc_no);
		} catch (Throwable e) {
    		throw new Exception("Error on loading report excel file; The Exception is: " + e.getMessage());
		}
		
		try {
			if(snapshotpath != null) {
			    snapshotpath = ".\\" + snapshotpath.substring(snapshotpath.indexOf("TC" + tc_no), snapshotpath.length());
			}
            link = String.format("HYPERLINK(\"%s\",\"%s\")" ,snapshotpath, capturename);
		    ex.setValueByColname(pass_fail, "Result", data_set_no, false);
		    ex.setValueByColname(link, "Screen capture", data_set_no,true);
		    ex.setValueByColname("Done", "executed", data_set_no, false);
		    //If this test cases used any random value
		    if(randomvaluerecoder.containsKey(tc_no)) {
		    	IdentityHashMap<String, IdentityHashMap<String, String>> dtsm = randomvaluerecoder.get(tc_no);
		    	String dasvalue = Integer.toString(data_set_no);
		    	Object[] dtsa = dtsm.keySet().toArray();
		    	IdentityHashMap<String, String> randomvaluemap=null;
		    	int j=0;
		    	for(Object o : dtsa) {
		    		if(o.toString().equals(dasvalue)) {
		    			rndthisdataset = true;
		    			Object[] rndvalue = dtsm.values().toArray();
		    			randomvaluemap = (IdentityHashMap<String, String>) rndvalue[j];
		    			break;
		    		}
		    		else {
		    			j=j+1;
		    		}
		    	}
		    	//If this data set used any random value
		    	if(rndthisdataset) {
		    		if(randomvaluemap != null) {
		    			Object[] cols = randomvaluemap.keySet().toArray();
		    			Object[] vals = randomvaluemap.values().toArray();
		    			for(int i =0;i< cols.length;i++) {
		    				String n = (String)cols[i];
		    				String newvalue = (String)ex.getValueByColname(n, data_set_no) + "(val: " + (String)vals[i] + ")";
		    				ex.setValueByColname(newvalue, n, data_set_no, false);
		    			}
		    		}	
		    	}
		    }
		    ex.saveExcel(reportfilepath);
		  
		}catch (Throwable e) {
			//e.printStackTrace();
			throw new Exception("Error on writing result(s) to report excel file; The Exception is: " + e.getMessage());
		}
		
	}
	
	public static void GenerateFinalReport(String tc_no, boolean ignoreIDPW) throws Throwable {
		Excel excsrc=null;
		Excel excdes=null;
		int srcrows = 0;
		
		try {
			excsrc = new Excel(reportfilepath);
			excdes = new Excel(reportfilepath);
			excsrc.selectSheetByName(tc_no);
			excdes.selectSheetByName("result-timestamp");
		} catch (Throwable e) {
    		throw new Exception("Error on loading report excel file; The Exception is: " + e.getMessage());
		}
		srcrows = excsrc.getRowNumber();
		for(int i=1;i<=srcrows;i++) {
			Object dtsob = excsrc.getValueByColname("Data set", i);
			String dts=null;
			
			if(dtsob != null) {
				dts = Integer.toString(new Double((Double)dtsob).intValue());	
			}
			
			String description = (String)excsrc.getValueByColname("Description", i);
			String exres = (String)excsrc.getValueByColname("Expected result", i);
			String br = (String)excsrc.getValueByColname("Browser", i);
			String executed = (String)excsrc.getValueByColname("executed", i);
			String asst = (String)excsrc.getValueByColname("Assertion", i);
			
			excdes.setValueByColname(tc_no, "Case No", finalreportrow, false);
			excdes.setValueByColname(dts, "Data set", finalreportrow, false);
			excdes.setValueByColname(description, "Description", finalreportrow, false);
			excdes.setValueByColname(exres, "Expected result", finalreportrow, false);
			excdes.setValueByColname(br, "Browser", finalreportrow, false);
			excdes.setValueByColname(asst, "Assertion", finalreportrow, false);
			if(executed ==null || executed.equals("Done") == false) {
				excdes.setValueByColname("Skipped", "executed", finalreportrow, false);
			}
			else if(executed.equals("Done")){
				String res = (String)excsrc.getValueByColname("Result", i);
				String scrsav = (String)excsrc.getValueByColname("Screen capture", i);
				excdes.setValueByColname(executed, "executed", finalreportrow, false);
				excdes.setValueByColname(res, "Result", finalreportrow, false);
				excdes.setValueByColname(scrsav, "Screen capture", finalreportrow, true);
			}
			if(ignoreIDPW == false) {
				String id = (String)excsrc.getValueByColname("ID", i);
				String pw = (String)excsrc.getValueByColname("PW", i);
				excdes.setValueByColname(id, "ID", finalreportrow, false);
				excdes.setValueByColname(pw, "PW", finalreportrow, false);
			}
			finalreportrow = finalreportrow + 1;
			
		}
		
		try {
			excdes.saveExcel(reportfilepath);
		}catch(Throwable e) {
			throw new Exception("Error on saving final report excel file; The Exception is: " + e.getMessage());
		}
	}
	
	public static void recordRandomValue(String tc_no, IdentityHashMap<String, IdentityHashMap<String, String>> datasetmap) {

		randomvaluerecoder.put(tc_no, datasetmap);
		
	}
	
	public static String getReportFolderPath() {
		return reportfolderpath;
	}
	
	public static String getReportFilePath() {
		return reportfilepath;
	}

}
