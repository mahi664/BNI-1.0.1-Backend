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
	public static final  	String 		GST 		= 	"IGST";
	public static final		String	 	PIDANDENDDATEWHERECLAUSE = "WHERE PRODUCT_ID=? AND END_DATE_SKEY=?";
	public static final 	String		VALUESFORINSERT = "VALUES(?,?,?,?,?)";
	public static final 	String 		EXPIRED     =   "Expired";
	public static final 	String 		ABOUT_TO_EXPIRE     =   "About To Expire";
	public static final 	String 		EMPTY_STRING     =   "";
	
	public static enum ALERTS{
		VEN_RECPT_MAP_ERROR("VEN_RECPT_MAP_ERROR"),
		PUR_ORDR_SUCCESS("PUR_ORDR_SUCCESS"),
		PUR_ORDR_ERROR("PUR_ORDR_ERROR"),
		PROD_STOCK_MAP_UPDATE_ERROR("PROD_STOCK_MAP_UPDATE_ERROR");
		
		private String value;
		private ALERTS(String value){
			this.value = value;
		}
		public String toString(){
			return this.value;
		}
	}
	
	public static enum ALERTS_TYPE{
		SUCCESS("SUCCESS"),WARNING("WARNING"),ERROR("ERROR");
		
		private String value;
		private ALERTS_TYPE(String value){
			this.value = value;
		}
		
		public String toString(){
			return this.value;
		}
	}
}
