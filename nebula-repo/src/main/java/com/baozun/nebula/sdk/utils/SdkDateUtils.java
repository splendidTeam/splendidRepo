package com.baozun.nebula.sdk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @deprecated by D.C 2016/4/6
 * pls use {@link com.feilong.core.text.DateFormatUtil} {@link com.feilong.core.DatePattern} 
 */
@Deprecated
public class SdkDateUtils {
	
	public static Date parseStrToDate(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  date;
		}
		return date;
	}
	
	public static String getDateStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date());
		return dateStr;
	}
	
	public static String parseDateToStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	public static void main(String[] args) {
		String str = "2013-12-23";
		System.out.println(parseStrToDate(str));
	}
}
