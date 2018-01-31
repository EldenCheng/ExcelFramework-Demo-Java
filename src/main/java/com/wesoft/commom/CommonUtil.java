package com.wesoft.commom;

import java.util.regex.Pattern;

public class CommonUtil {
	public static boolean isNumberic(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	}

}
