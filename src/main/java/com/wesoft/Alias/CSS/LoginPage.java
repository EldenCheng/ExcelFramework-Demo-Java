package com.wesoft.Alias.CSS;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class LoginPage {
	
	public static String TXTF_ID;
	public static String TXTF_PW;
	public static String BTN_LOGIN;
	public static String LB_ERRORPROMPT;
	public static String TXTF_VCODE;
	public static String TXT_PAGETITLE;
	
	static {
		
        JsonParser parse =new JsonParser();  //Create jsonParser
        try {
            JsonObject json=(JsonObject) parse.parse(new FileReader("./Element_mapping/LoginPage.json"));  //Create jsonObject
            
            TXTF_ID = json.get("TXTF_ID").getAsString();
        	TXTF_PW = json.get("TXTF_PW").getAsString();
        	BTN_LOGIN = json.get("BTN_LOGIN").getAsString();
        	LB_ERRORPROMPT = json.get("LB_ERRORPROMPT").getAsString();
        	TXTF_VCODE = json.get("TXTF_VCODE").getAsString();
        	TXT_PAGETITLE = json.get("TXT_PAGETITLE").getAsString();

        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

}
