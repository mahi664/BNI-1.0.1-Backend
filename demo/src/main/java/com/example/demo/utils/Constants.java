package com.example.demo.utils;

import java.util.Date;

public class Constants {
	
	private Constants(){
		
	}
	public static final 	String 		P_UPLAOD 	=	"PRODCUTS_UPLOAD";
	public static final 	String 		C_UPLAOD 	=	"CATEGORIES_UPLOAD";
	public static final 	String 		PU_UPLAOD 	=	"PURCHASE_UPLOAD";
	public static final		String 		FILE_PATH	=	"E:/Learning/demo/upload-dir/";
	@SuppressWarnings("deprecation")
	public static final		Date		MAX_DATE	=	new Date(2099-1900, 11, 31);
	public static final  	String 		CGST 		= 	"CGST";
	public static final  	String 		SGST 		= 	"SGST";
	public static final  	String 		IGST 		= 	"IGST";
	public static final		String	 	PIDANDENDDATEWHERECLAUSE = "WHERE PRODUCT_ID=? AND END_DATE_SKEY=?";
	public static final 	String		VALUESFORINSERT = "VALUES(?,?,?,?,?)";
	
}
