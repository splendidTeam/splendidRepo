package com.baozun.nebula.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShopDateUtil {
	
	/**精確到秒*/
	public static final String PATTERN_PRECISE_SECOND="yyyy-MM-dd HH:mm:ss";
	
	public static final String PATTERN_PRECISE_DAY = "dd/MM/yyyy";
	
	public static boolean countTime(long begin, long end) {
		Long total_minute;

		total_minute = (end - begin) / (1000 * 60);

		Long twodayMin = new Long(24 * 60 * 2);

		if (total_minute.compareTo(twodayMin) > 0) {
			return false;
		}
		return true;
	}
	
	
    /**
     * 在日期上增加数个整月
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
    
    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date string2Date(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 使用用户格式格式化日期 没有后缀
     * 
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String date2String(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return  df.format(date);
    }
    
    
    /**
     * 獲取下個月的開始時間和結束時間
     * @return
     * 
     */
    public static Date[] getNextMonth(){
    	Calendar month = Calendar.getInstance();
    	month.set(Calendar.MONTH, month.get(Calendar.MONTH)+1);
    	
    	month.set(Calendar.DAY_OF_MONTH, 1);
    	month.set(Calendar.HOUR_OF_DAY, 0);
    	month.set(Calendar.MINUTE, 0);
    	month.set(Calendar.SECOND,0);
    	
    	Date startDate = month.getTime();
    	
    	month.add(Calendar.MONTH, 1);
    	month.add(Calendar.SECOND, -1);
    	
    	Date endDate = month.getTime();
    	
    	return new Date[]{startDate,endDate};
    }
    
    /** 獲取當月第一天*/
    public static Date getFirstDayOfMonth(){
    	Calendar cal=Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_MONTH,1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	return cal.getTime();
    }
    /** 獲取下個月第一天*/
    public static Date getFirstDayOfNextMonth(){
    	Calendar cal=Calendar.getInstance();
    	cal.add(Calendar.MONTH,1);
    	cal.set(Calendar.DAY_OF_MONTH,1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	return cal.getTime();
    }
    /** 是否周日*/
    public static boolean isSunday(Calendar ca){
    	if(ca.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
    		return true;
    	}
    	return false;
    }
    
    /** 校驗在某某時間之前*/
    public static boolean beforeOfDay(Calendar ca,int our,int min,int sec){
    	Calendar am1030=Calendar.getInstance();
		am1030.set(Calendar.HOUR_OF_DAY, our);
		am1030.set(Calendar.MINUTE, min);
		am1030.set(Calendar.SECOND,sec);
		if(ca.before(am1030)){
			return true;
		}
		return false;
    }
}
