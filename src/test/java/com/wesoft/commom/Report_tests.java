package com.wesoft.commom;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wesoft.commom.Report;

public class Report_tests {
	
	@Disabled
	@Test
    @DisplayName("To test report folder creation")
	public void reportfolder_start() {
		try {
			Report.createReportFolderAndFile();
		} catch(Throwable e) {
			e.printStackTrace();
		}

	}
	
	//@Disabled
	@Test
    @DisplayName("To test final report creation")
	public void finalreport_start() {
		try {
			Report.createReportFolderAndFile();
			Report.GenerateFinalReport("1");
		} catch(Throwable e) {
			e.printStackTrace();
		}

	}

}
