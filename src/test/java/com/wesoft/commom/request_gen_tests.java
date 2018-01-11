package com.wesoft.commom;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wesoft.commom.RequestGenerater;;

public class request_gen_tests {
	
	//@Disabled
	@Test
    @DisplayName("To test request generate")
	public void startup_start() {
		try {
			RequestGenerater.generateRequest();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
