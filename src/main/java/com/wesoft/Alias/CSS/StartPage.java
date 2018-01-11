package com.wesoft.Alias.CSS;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class StartPage {
	
	public static String LB_LOGONUSERNAME;
	public static String TXT_PAGETITLE;
	
	static {
		
        JsonParser parse =new JsonParser();  //Create jsonParser
        try {
            JsonObject json=(JsonObject) parse.parse(new FileReader("./Element_mapping/StartPage.json"));  //Create jsonObject
            
            LB_LOGONUSERNAME = json.get("LB_LOGONUSERNAME").getAsString();
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
