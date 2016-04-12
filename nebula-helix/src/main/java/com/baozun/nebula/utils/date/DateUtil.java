package com.baozun.nebula.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xianze.zhang
 * @creattime 2013-1-3
 */
@Deprecated
public class DateUtil {
	/**
	 * 验证当前时间是否在开始时间和结束时间内
	 * 
	 * @return
	 */
	public static boolean validateBetweenTwoDate(Date beginTime, Date endTime) {
		if (beginTime == null && endTime == null) {
			return true;
		}
		Date now = new Date();
		if (beginTime != null && endTime == null) {
			return beginTime.before(now);
		}
		if (beginTime == null && endTime != null) {
			return endTime.after(now);
		}
		// 在有效时间之内
		if (beginTime.before(now) && endTime.after(now)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean countTime(long begin, long end) {
		Long total_minute;

		total_minute = (end - begin) / (1000 * 60);

		Long twodayMin = new Long(24 * 60 * 2);

		if (total_minute.compareTo(twodayMin) > 0) {
			return false;
		}
		return true;
	}
	public static boolean countTime(long begin, long end,int hours ) {
		Long total_minute;

		total_minute = (end - begin) / (1000 * 60);

		Long twodayMin = new Long(hours * 60 * 2);

		if (total_minute.compareTo(twodayMin) > 0) {
			return false;
		}
		return true;
	}

	public static String parseDateToStr(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static void main(String[] args) {
		Date now = new Date();
		now.after(null);
	}
	
	public static Date parseStrToDate(String source){
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(source);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
