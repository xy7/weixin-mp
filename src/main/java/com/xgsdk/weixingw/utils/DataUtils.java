package com.xgsdk.weixingw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtils {

	public static int date2int(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		int dateInt = Integer.parseInt(sdf2.format(sdf.parse(date)));
		return dateInt;
	}
	
	public static int dayDiff(int day1, int day2){
		DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld1 = LocalDate.parse(Integer.toString(day1), formatterDay);
		LocalDate ld2 = LocalDate.parse(Integer.toString(day2), formatterDay);
		
		int i = 0;
		for(LocalDate ld=ld1; !ld.isAfter(ld2); ld=ld.plusDays(1)){
			i++;
		}
		return i;
	}
	
	public static void main(String[] args){
		
			System.out.println( dayDiff(20141228, 20150110) );

	}
}
