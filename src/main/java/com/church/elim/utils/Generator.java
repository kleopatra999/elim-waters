package com.church.elim.utils;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;

public class Generator {
	Parishioner p;
	private static int generateCellsFromFields(Class clazz, String prefix, int start){
		int i=start;
		for(Method method: clazz.getDeclaredMethods()){
			if(!method.getReturnType().getName().contains("com.church")
					&& method.getName().startsWith("get")){
				System.out.println( 
				   method.getReturnType().getSimpleName() + " value"+i+" = " + 
						"datasource.get(i-2)." + prefix + method.getName()+"();\n"
				   + "if (value"+i+" != null) {\n" 
				   + "cell= row.createCell(startColIndex+"+ i + ");\n"		
				   +"cell.setCellValue(value"+i++ +");" 
				   + "cell.setCellStyle("+getMethodCellStyle(method) +");\n}\n"
				   );
				//System.out.println(method.getReturnType());
				System.out.println();
			}
		}
		return i;
	}
	
	private static int generateRowHeaderFromFields(Class clazz, String prefix, int start){
		int i=start;
		for(Method method: clazz.getDeclaredMethods()){
			if(!method.getReturnType().getName().contains("com.church")
					&& method.getName().startsWith("get")){
				System.out.println( "cell= row.createCell(startColIndex+"+ i++ +");\n"
				   + "cell.setCellValue(\""+method.getName().substring(3)+"\");\n"
				   + "cell.setCellStyle(bodyCellStyle);");
				//System.out.println(method.getReturnType());
				System.out.println();
			}
		}
		return i;
	}
	private static String getMethodCellType(Method m){
		Class c = m.getReturnType(); 
		if(c.equals(Long.class)||c.equals(Integer.class) ){
			return "HSSFCell.CELL_TYPE_NUMERIC";
		}else if(c.equals(Date.class)){
			return "HSSFCell.CELL_TYPE_FORMULA";
		}
		
		return "HSSFCell.CELL_TYPE_STRING";
	}
	
	
	private static String getMethodCellStyle(Method m){
		Class c = m.getReturnType(); 
		if(c.equals(Date.class)){
			return "dataCellStyle";
		}
		
		return "bodyCellStyle";
	}
	
	public static String generatePasswordMD5(String password){
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		return encoder.encodePassword(password, null);
		
	}
	public static void main(String[] args){
		System.out.println(generatePasswordMD5("exod15:27"));
		/*generateCellsFromFields(Parishioner.class, "", 
				generateCellsFromFields(Person.class, "getPerson().", 0));*/
		/*generateRowHeaderFromFields(Parishioner.class, "", 
				generateRowHeaderFromFields(Person.class, "getPerson().", 0));*/
	}

}
