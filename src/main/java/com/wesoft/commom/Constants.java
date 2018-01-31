package com.wesoft.commom;

import java.io.FileNotFoundException;
import java.io.FileReader;
 
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;



public class Constants {
	
	private static String URL;
	private static String DATAPATH;
	private static String REPORTPATH;
	private static String CHROMEDRIVERPATH;
	private static String IEDRIVERPATH;
	private static String GECKODRIVERPATH;
	private static String TESTAPKVERSION;
	private static String TESTER;

	//Initialize the constant values
	//This static block will be executed automatically when the application start
	static {
		
        JsonParser parse =new JsonParser();  //Create jsonParser
        try {
            JsonObject json=(JsonObject) parse.parse(new FileReader("./env.json"));  //Create jsonObject
            
            URL = json.get("URL").getAsString();
            DATAPATH = json.get("DATAPATH").getAsString();
            REPORTPATH = json.get("REPORTPATH").getAsString();
            CHROMEDRIVERPATH = json.get("CHROMEDRIVERPATH").getAsString();
            IEDRIVERPATH = json.get("IEDRIVERPATH").getAsString();
            GECKODRIVERPATH = json.get("GECKODRIVERPATH").getAsString();
            TESTAPKVERSION = json.get("TESTAPKVERSION").getAsString();
            TESTER = json.get("TESTER").getAsString();

        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	public static String Get_URL() {
		return URL;
	}
	
	public static String Get_DATAPATH() {
		return DATAPATH;
	}	

	public static String Get_REPORTPATH() {
		return REPORTPATH;
	}
	
	public static String Get_CHROMEDRIVERPATH() {
		return CHROMEDRIVERPATH;
	}
	
	public static String Get_IEDRIVERPATH() {
		return IEDRIVERPATH;
	}
	
	public static String Get_GECKODRIVERPATH() {
		return GECKODRIVERPATH;
	}
	
	public static String Get_TESTAPKVERSION() {
		return TESTAPKVERSION;
	}
	
	public static String Get_TESTER() {
		return TESTER;
	}	
	
}
