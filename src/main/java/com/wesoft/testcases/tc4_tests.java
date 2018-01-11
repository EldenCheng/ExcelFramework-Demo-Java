package com.wesoft.testcases;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class tc4_tests {
    
	/*
	@BeforeAll
    static void testannonce(){
        System.out.println("This is TC1");
    }
    */

    @Test
    @DisplayName("Login whit incorrect Username / Password")
    public void test_start() {
        String str = "Login whit incorrect Username / Password";
        System.out.println(str);
    }

}