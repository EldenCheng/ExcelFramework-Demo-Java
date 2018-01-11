package com.wesoft.commom;

import com.wesoft.commom.Excel;
import com.wesoft.commom.Constants;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class excel_tests {
	
	@Disabled
	@Test
    @DisplayName("To select sheet by name")
	public void select_sheet_start() {
		
		try {
			Excel exc = new Excel(Constants.Get_DATAPATH());
			System.out.println((exc.selectSheetByName("1").getLastRowNum()));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	@Disabled
	@Test
    @DisplayName("Get not null row number")
	public void row_num_start() {
		
		try {
			Excel exc = new Excel(Constants.Get_DATAPATH());
			exc.selectSheetByName("7");
			System.out.println((exc.getRowNumber()));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	@Disabled
	@Test
    @DisplayName("Get not null col number")
	public void col_num_start() {
		
		try {
			Excel exc = new Excel(Constants.Get_DATAPATH());
			exc.selectSheetByName("sheet1");
			System.out.println((exc.getColNumber()));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	@Disabled
	@Test
    @DisplayName("Get value by specified colname and row number")
	public void get_value_start() {
		
		try {
			Excel exc = new Excel(Constants.Get_DATAPATH());
			exc.selectSheetByName("2");
			System.out.println((exc.getValueByColname("Assertion", 1)));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	@Disabled
	@Test
    @DisplayName("Get execution dataset according to col name and keywork")
	public void dataset_start() {
		
		try {
			Excel exc = new Excel(Constants.Get_DATAPATH());
			exc.selectSheetByName("summary");
			Object[] data = exc.getDatasetAccordly("Case No", "execute", "yes");
			for(Object i:data) {
				System.out.println(i);				
			}
			
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	//@Disabled
	@Test
    @DisplayName("Set value by colname name")
	public void setvalue_start() {
		
		try {
			Excel exc = new Excel("./TestReport/Kerry_data_2t.xlsx");
			exc.selectSheetByName("1");
			exc.setValueByColname("3+2", "Screen capture", 2, true);
			exc.saveExcel("./TestReport/Kerry_data_2d.xlsx");
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}


}
