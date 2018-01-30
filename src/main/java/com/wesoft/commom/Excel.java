package com.wesoft.commom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	
	private XSSFWorkbook book;
	private XSSFSheet sheet;
	private String tcno=null;
	private IdentityHashMap<String, IdentityHashMap<String,String>> randomdatasetmap;
	private IdentityHashMap<String, String> randomvaluemap;
	private int recordeddataset;
	private FormulaEvaluator formulaEvaluator = null;
	
	public Excel(String excelpath) throws Throwable {
		
        File excel = new File(excelpath);
        FileInputStream fis;
        randomvaluemap = new IdentityHashMap<>();
        randomdatasetmap = new IdentityHashMap<>();
        recordeddataset = -1;
        
		try {
			fis = new FileInputStream(excel);
			book = new XSSFWorkbook(fis);
			formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) book);
			
		} catch (FileNotFoundException e) {
			throw new Exception("Not found excel file in the path. " + 
		                        "The Exception is: " + e.getMessage());
		} 
          catch (IOException e) {
        	  throw new Exception("Cannot open the excel file!"+ 
                      "The Exception is: " + e.getMessage());
		} 
	
	}
	
	public XSSFSheet selectSheetByName(String sheet_name) throws Exception {
		
		sheet = book.getSheet(sheet_name);
		if(sheet == null) {
			throw new Exception("No this sheet in excel: " + sheet_name);
		}
		
		return sheet;
	}
	
	//Avoid there is any blank rows to make the Java throws exception
	//Use this method to find out non blank row number
	public int getRowNumber() throws Throwable {
		/*
		int rows=0;
		try {
			int num=sheet.getLastRowNum();
			
			for(int i=num;i>-1;i--) {
				if(i != num) {
				    if(sheet.getRow(i).getCell(0) == null) {
					//if(i != num) {
						continue;
					//}
					//else {
					//	rows =i;
					//	break;
					//}
				    }
				    else {
					    rows = i - 1;
					    break;
				    }
				}
				else {
					rows =i;
					break;
				}
			}

		} catch(Throwable e) {
			throw new Exception("No found any rows in sheet: " + sheet.getSheetName() + 
                    "The Exception is: " + e.getMessage());
		}
		return rows;*/
		return sheet.getLastRowNum();
	}
	
	public int getColNumber() throws Throwable{
		int cols =0;
		try {
			if(sheet.getLastRowNum() != -1) {
				short num= sheet.getRow(0).getLastCellNum();
				//Find the first non null cell in the first row(row 0) order by desc, got the col number
				for(int i=num;i>-1;i--) {
					if(sheet.getRow(0).getCell(i) == null) {
						if(i != 0) {
							continue;
						}
						else {
							cols =1;
							break;
						}
					}
					//It have two scenes, one is the cell itself is not exits, another is the cell value is null
					//This is to find the second scene
					else if(sheet.getRow(0).getCell(i).getRawValue() == null){
						continue;
					}
					else {
						cols = i;
						break;
					}
				}
				
			}
			else {
				throw new Exception("No found any cols in sheet: " + sheet.getSheetName());
			}
		} catch (Throwable e) {
			throw new Exception("Error(s) occurred when finding col number in sheet: " + sheet.getSheetName() + 
                    "The Exception is: " + e.getMessage());
		}
		return cols;
	}
	
	public Object getValueByColname(String colname, int row) throws Throwable{
		Object value =null;
		int cols = this.getColNumber() + 1;
		int rows =sheet.getLastRowNum() + 1 ;
		if(row > rows) {
			throw new Exception("The specified row num is larger than the max row num: " + Integer.toString(rows));
		}
		
		for(int i = 0;i<cols;i++) {
			if(sheet.getRow(0).getCell(i) != null) {
				//When the col name(in row 0) is equals the keyword
				if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(colname.toLowerCase())) {
					if(sheet.getRow(row).getCell(i) != null) {
						switch (sheet.getRow(row).getCell(i).getCellTypeEnum()) {
						
						case NUMERIC:
							value = sheet.getRow(row).getCell(i).getNumericCellValue();
							break;
						case STRING:
							value = sheet.getRow(row).getCell(i).getStringCellValue();
							String v = (String)value;
							//If the value has keyword "random"
							if(v.contains("random")) {
								//To ensure will not exchange random value when generate report need to read the same cell value
								//So when running test cases, set the tcno, if generating report, not set 
								if(tcno != null){
									value = this.getRandomValue(v);	
									//To avoid repeat to add the same randomdatasetmap and create a new randomvaluemap
									//if there is multi random value in one dataset
									if(recordeddataset != row) {
										randomvaluemap = new IdentityHashMap<>();
										randomvaluemap.put(colname, (String)value);
										randomdatasetmap.put(Integer.toString(row), randomvaluemap);
										Report.recordRandomValue(tcno, randomdatasetmap);
										recordeddataset = row;									
									}
									else {
										randomvaluemap.put(colname, (String)value);
									}	
								}
							}
							break;
						case FORMULA:
							value = sheet.getRow(row).getCell(i).getCellFormula();
							break;
						default:
							value = sheet.getRow(row).getCell(i).getRawValue();
						}
						break;
					}
				}	
			}
		}
		return value;
	}
	
	private Object getRandomValue(String randomindex) throws Throwable {
		List<Object> values= new LinkedList<Object>();
		Excel temp=null;
		XSSFSheet st=null;
		try {
			temp = new Excel(Report.getReportFilePath());
			st = temp.selectSheetByName("data");				
		}catch(Throwable e) {
			throw new Exception("Error(s) occurred when opening in data sheet: " + 
                    "The Exception is: " + e.getMessage());
		}		
		int cols = temp.getColNumber() + 1;
		for(int i = 0;i<cols;i++) {
			if(st.getRow(0).getCell(i) != null) {
				if(st.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(randomindex.toLowerCase())) {
					//After found the col, add all the cell value(s) of this col into a List
					for(int row=1;row<st.getLastRowNum();row++) {
						if(st.getRow(row).getCell(i) != null) {
							switch (st.getRow(row).getCell(i).getCellTypeEnum()) {
							
							case NUMERIC:
								values.add(Integer.toString(new Double(st.getRow(row).getCell(i).getNumericCellValue()).intValue()));
								continue;
							case STRING:
								values.add(st.getRow(row).getCell(i).getStringCellValue());
								continue;
							case FORMULA:
								values.add(st.getRow(row).getCell(i).getCellFormula());
								continue;
							default:
								values.add(st.getRow(row).getCell(i).getRawValue());
							}
							continue;
						}
					}
					break;
				}
				
			}
		}
		//Pick a int number between 0 and the random value array size 
		int rnd = new Random().nextInt(values.size());
		temp.closeExcel();
		return values.get(rnd);
	}
		
	public Object[] getDatasetAccordly(String dataset_colname, String accroding_dataset_colname, String keyword) throws Throwable {
		List<Object> dt = new LinkedList<>();
		int cols=0;
		int rows=0;
		Object value=null;
		try {
			cols = this.getColNumber() + 1;
			rows = sheet.getLastRowNum() + 1;
		} catch(Throwable e) {
			throw new Exception("No found any data in sheet: " + sheet.getSheetName());
		}
		
		for(int i=0;i<cols;i++) {
			if(sheet.getRow(0).getCell(i) != null) {
				//Find the specified col name first
				if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(accroding_dataset_colname.toLowerCase())) {
					for(int j =0;j<rows;j++) {
						if(sheet.getRow(j).getCell(i) != null) {
							//Then find the row number which is hit the keyword in the specified col
							if(sheet.getRow(j).getCell(i).getStringCellValue().toLowerCase().equals(keyword.toLowerCase())) {
								int cellidx = this.getCellIndexByColname(dataset_colname);
								XSSFCell c =sheet.getRow(j).getCell(cellidx);
								if (c !=null && c.getCellTypeEnum() == CellType.FORMULA) {
									value = formulaEvaluator.evaluate(sheet.getRow(j).getCell(cellidx)).getNumberValue();
								}
								else {
									value=this.getValueByColname(dataset_colname, j);
								}
								if(value != null) {
									dt.add(value);
								}
							}
						}								
					}
				}	
			}
		}
			
		if(dt.isEmpty()) {
			throw new Exception("Cannot find any value in col: \"" + dataset_colname + "\" by using \"" + keyword + "\" to search in col: \"" + accroding_dataset_colname + "\"" );
		}
		return dt.toArray();
	}
	
	public int getCellIndexByColname(String colname) throws Throwable{
		int cellindex =0;
		int cols = this.getColNumber() + 1;
	
		for(int i = 0;i<cols;i++) {
			if(sheet.getRow(0).getCell(i) != null) {
				//When the col name(in row 0) is equals the keyword
				if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(colname.toLowerCase())) {
					if(sheet.getRow(0).getCell(i) != null) {
						cellindex = i; 
					}
				}	
			}
		}
		return cellindex;
	}
	
	public void setValueByColname(String value, String colname, int row, boolean formula) throws Throwable {
		
		int cols = this.getColNumber() + 1;
		int rows = sheet.getLastRowNum() + 1 ;
		if(row > rows) {
			throw new Exception("The specified row num is larger than the max row num: " + Integer.toString(rows));
		}else if(row == rows) { //As row 0 must be col name, so in fact real row number must + 1
			sheet.createRow(row);
		}
		
		try {
			for(int i = 0;i<cols;i++) {
				if(sheet.getRow(0).getCell(i) != null) {
					if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(colname.toLowerCase())) {
						//The cell is not exist if there is no value in fact, so need to create it first
						//If the cell already existed, create the same cell will use a blank cell cover the ori cell
						//But we are going to use a new value to cover the old value, so has no check the ori cell exists or not here
						XSSFCell c = sheet.getRow(row).createCell(i);
						if(formula) {
							c.setCellFormula(value);
							break;                   	
	                    }
						else {
							c.setCellValue(value);
							break;
						}
					}
				}
			}
		}catch(Throwable e) {
			//e.printStackTrace();
			throw new Exception("Error on writing excel file; The Exception is: " + e.getMessage());
		}
	}
	
	public void closeExcel() throws IOException {
		book.close();
	}
	
	public void setTCNO(String tc_no) {
		tcno=tc_no;
	}
	
	public void saveExcel(String excelpath) throws Throwable{
		try {
			FileOutputStream fileOut = new FileOutputStream(excelpath);
			book.setForceFormulaRecalculation(true);
			book.write(fileOut);
		} catch(Throwable e) {
			throw new Exception("Error on saving excel file; The Exception is: " + e.getMessage());
		}
	}

}


