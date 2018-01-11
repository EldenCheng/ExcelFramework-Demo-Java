package com.wesoft.commom;

import com.wesoft.commom.Constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Constants_tests {
	
	@Test
    @DisplayName("To test the Constants class")
	public void test_start() {
		//Constants.Set_properties("./env.json");
		System.out.println(Constants.Get_URL());
		System.out.println(Constants.Get_DATAPATH());
		System.out.println(Constants.Get_GECKODRIVERPATH());
		System.out.println(Constants.Get_IEDRIVERPATH());
		System.out.println(Constants.Get_CHROMEDRIVERPATH());
		System.out.println(Constants.Get_REPORTPATH());
		
	}

}
