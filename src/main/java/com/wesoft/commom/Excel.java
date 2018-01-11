package com.wesoft.commom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

public class Excel {
	
	private XSSFWorkbook book;
	private XSSFSheet sheet;
	private String filepath;
	private String tcno=null;
	private IdentityHashMap<String, IdentityHashMap> randomdatasetmap;
	private IdentityHashMap<String, String> randomvaluemap;
	private int recordeddataset;
	
	public Excel(String excelpath) throws Throwable {
		
        File excel = new File(excelpath);
        FileInputStream fis;
        randomvaluemap = new IdentityHashMap<>();
        randomdatasetmap = new IdentityHashMap<>();
        recordeddataset = -1;
        
		try {
			fis = new FileInputStream(excel);
			book = new XSSFWorkbook(fis);
			filepath = excelpath;
			
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
			//System.out.println(sheet.getRow(0).getFirstCellNum());
			//System.out.println(sheet.getRow(0).getLastCellNum());
			if(sheet.getLastRowNum() != -1) {
				short num= sheet.getRow(0).getLastCellNum();
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
					else if(sheet.getRow(0).getCell(i).getRawValue() == null){
						continue;
					}
					else {
						//System.out.println(sheet.getRow(0).getCell(i).getRawValue());
						cols = i;
						break;
					}
				}
				
			}
			else {
				throw new Exception("No found any cols in sheet: " + sheet.getSheetName());
			}
		} catch (Throwable e) {
			throw new Exception("No found any cols in sheet: " + sheet.getSheetName() + 
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
								if(tcno != null){
									value = this.getRandomValue(v);
									
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
		/*
		if(value == null) {
			throw new Exception("Cannot find any value in col: " + colname);
		}
		*/
		return value;
	}
	
	private Object getRandomValue(String randomindex) throws Throwable {
		List<Object> values= new LinkedList();
		Excel temp=null;
		XSSFSheet st=null;
		try {
			temp = new Excel(Report.getReportFilePath());
			st = temp.selectSheetByName("data");				
		}catch(Throwable e) {
			
		}		
		int cols = temp.getColNumber() + 1;
		for(int i = 0;i<cols;i++) {
			if(st.getRow(0).getCell(i) != null) {
				if(st.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(randomindex.toLowerCase())) {
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
				if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(accroding_dataset_colname.toLowerCase())) {
					for(int j =0;j<rows;j++) {
						if(sheet.getRow(j).getCell(i) != null) {
							if(sheet.getRow(j).getCell(i).getStringCellValue().toLowerCase().equals(keyword.toLowerCase())) {
								//System.out.println(this.Get_value_by_colname(dataset_colname, j));
								value=this.getValueByColname(dataset_colname, j);
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
	
	public void setValueByColname(String value, String colname, int row, boolean formula) throws Throwable {
		
		int cols = this.getColNumber() + 1;
		int rows = sheet.getLastRowNum() + 1 ;
		if(row > rows) {
			throw new Exception("The specified row num is larger than the max row num: " + Integer.toString(rows));
		}else if(row == rows) {
			sheet.createRow(row);
		}
		
		try {
			for(int i = 0;i<cols;i++) {
				if(sheet.getRow(0).getCell(i) != null) {
					if(sheet.getRow(0).getCell(i).getStringCellValue().toLowerCase().equals(colname.toLowerCase())) {
						XSSFCell c = sheet.getRow(row).createCell(i);
						if(formula) {
							//c.setCellType(CellType.FORMULA);
							//c.setCellValue(value);
							c.setCellFormula(value);
							//c=new XSSFFormulaEvaluator(book).evaluateInCell(c);
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
			e.printStackTrace();
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


