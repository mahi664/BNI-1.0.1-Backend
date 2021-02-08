package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static int dateSkey(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(sdf.format(date));
	}
	
	public static Date getDate(int dateSkey){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(Integer.toString(dateSkey));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static java.sql.Date getSqlDate(Date date){
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}
}
