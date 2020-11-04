package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static int dateSkey(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(sdf.format(date));
	}
}
