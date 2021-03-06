package com.jxd.bookdistribution.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


import android.media.ExifInterface;
import android.preference.PreferenceActivity;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	/*
	 * 创建excel文件
	 */
	public static boolean CreateExcel( String path){
		WritableWorkbook wb = null;

		try {
			wb = Workbook.createWorkbook(new File(path));
			wb.createSheet("sheet1", 0);
			wb.write();
			wb.close();
			return true;
		}catch (WriteException we) {
			// TODO Auto-generated catch block
			we.printStackTrace();
			return false;
		}catch (IOException e) {
			// TODO Auto-generated catch bloc			e.printStackTrace();
			return false;
		}

	}

	/*
	 * 读取Excel文件
	 */
	public static List< LinkedList<String> > ReadExcel( String path , int  sheetIdx ){
		Workbook wb = null;

		try {
			wb = Workbook.getWorkbook(new File(path));
			Sheet st = wb.getSheet(sheetIdx);
			if( st ==null) return null;
			int rows =st.getRows();
			int cols =st.getColumns();
			List< LinkedList<String>> data = new ArrayList< LinkedList<String> >();
			for( int i=0;i<rows;i++){
				LinkedList<String> record = new LinkedList<String>();
				for(int k=0;k<cols;k++){
					record.add( st.getCell(k, i).getContents() );
				}
				data.add(record);
			}
			wb.close();
			wb=null;
			return data;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	protected static Object InvokeMethod( Object owner , String fieldName){
		Class<? extends Object> ownerClass = owner.getClass();
        //fieldName -> FieldName
        String methodName = fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
        Method method = null;
        try
        {
            method = ownerClass.getMethod("get" + methodName);
        }
        catch (SecurityException e)
        {
            //e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            // e.printStackTrace();
            return "";
        }
        //invoke getMethod
        try
        {
            return method.invoke(owner);
        }
        catch (Exception e)
        {
            return "";
        }
	}

	protected static String GetFieldValue( Object obj , Field field){
		Object typeObject= field.getType();
		try {
			if( typeObject.toString().equals( "class java.lang.Integer" )){
				Object temp = InvokeMethod(obj, field.getName());
				return temp ==null? "":temp.toString();
			}else if( typeObject.toString().equals("class java.lang.String")){
				Object temp = InvokeMethod(obj, field.getName());
				return temp ==null?"":temp.toString();
			}else if( typeObject instanceof Double){
				Object temp= InvokeMethod(obj, field.getName());
				return temp == null?"":temp.toString();
			}else if( typeObject instanceof Float){
				Object temp = InvokeMethod(obj, field.getName());
				return temp ==null?"":temp.toString();
			}else if( typeObject instanceof Date ){
				Object temp = InvokeMethod(obj, field.getName());
				return temp ==null?"":temp.toString();
			}else if( typeObject instanceof Boolean){
				Object temp = InvokeMethod(obj, field.getName());
				return temp==null?"":temp.toString();
			}else{
				Object temp = InvokeMethod(obj, field.getName());
				return temp == null?"":temp.toString();
			}

		}  catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}


//	public static void  test(){
//		List list = new ArrayList();
//		Contract c = new Contract();
//		c.setBcompany("乙方公司");
//		c.setContractid(1);
//		c.setContractname("合同");
//		c.setContractnum("2014001");
//	    c.setContractplace("一楼一排");
//		c.setContractrfid("rfid");
//		c.setContractstate("在库");
//		c.setSigningdate("2014-10-01");
//		c.setOperatorname("金向东");
//	    list.add(c);
//
//	    LinkedList<String> maps = new LinkedList<String>();
//	    maps.add("contractNo");
//	    maps.add("contractName");
//	    maps.add("contractPlace");
//	    maps.add("BCompany");
//	    maps.add("SigningDate");
//	    maps.add("contractRFID");
//	    maps.add("contractState");
//	    maps.add("operator");
//
//	    WriteExcel("", list , maps);
//	}

	protected static boolean ExistField(String mapName , Field[] fields ){
		 for( int i=0;i<fields.length;i++){
			 Field f = fields[i];
			 String fieldName = f.getName();
			 if( fieldName.equalsIgnoreCase(mapName)){
				 return true;
			 }
		 }
		 return false;
	}

	protected static Field GetField( String fieldName , Field[] fields){
		 for( int i=0;i<fields.length;i++){
			 Field f = fields[i];
			 if(  f.getName().equalsIgnoreCase(fieldName)){
				 return f;
			 }
		 }
		 return null;
	}

	public static void WriteHeader(WritableSheet wt, LinkedList<String> header){
		for( int k=0;k< header.size() ;k++){
			Label lb = new Label(k, 0 , "1");
			lb.setString( header.get(k) );
			try {
				wt.addCell(lb);
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * 写数据到excel文件
	 */
	 public static <T> void  WriteExcelData( String path ,
										 List<Class<T>> data ,
										 LinkedList<String> map,
										 LinkedList<String> header
										 ){
		 List< LinkedList<String>> records = new ArrayList<LinkedList<String>>();

		 for( Object obj : data){

			 LinkedList<String> line = new LinkedList<String>();

			 Field[] fields   = obj.getClass().getDeclaredFields();
			 for( int idx =0;idx< map.size();idx++){
				 String mapName = map.get(idx);
				 if( ExistField(mapName, fields)){
					 line.add( GetFieldValue(obj,  GetField(mapName, fields) ));
				 }else{
					 line.add("");
				 }
			 }

			 records.add(line);
		 }

		 WriteExcel(path, records , header );
	 }

	 public static void WriteExcel( String path ,
									List< LinkedList<String>> records,
									LinkedList<String> header
									 ){
		 if( path ==null || path.length() == 0 ) return;

		 WritableWorkbook wb = null;
		 try {
			wb= Workbook.createWorkbook(new File(path));
			WritableSheet st = wb.createSheet("Sheet1", 0);

			 int offset =1;

			 WriteHeader(st, header );


			int size = records.size();
			for( int i=0;i< size ; i++){
				int cols = records.get(i).size();
				LinkedList<String> line = records.get(i);
				for( int k=0;k< cols;k++){
					Label lb = new Label(k, i+offset , "1");
					lb.setString( line.get(k) );
					try {
						st.addCell(lb);
					} catch (RowsExceededException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			wb.write();
			wb.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
