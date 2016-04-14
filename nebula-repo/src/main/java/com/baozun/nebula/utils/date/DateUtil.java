package com.baozun.nebula.utils.date;


import java.util.Date;

/**
 * @author xianze.zhang
 *@creattime 2013-1-3
*/
@Deprecated
public class DateUtil {
	/**
	 * 验证当前时间是否在开始时间和结束时间内
	 * @return
	 */
	public static boolean validateBetweenTwoDate(Date beginTime,Date endTime){
		if(beginTime==null&&endTime==null){
			return true;
		}
		Date now = new Date();
		if(beginTime!=null&&endTime==null){
			return beginTime.before(now);
		}
		if(beginTime==null&&endTime!=null){
			return endTime.after(now);
		}
		//在有效时间之内
		if(beginTime.before(now)&&endTime.after(now)){
			return true;
		}else{
			return false;
		}
		
	}
	public static void main(String[] args){
		Date now = new Date();
		now.after(null);
	}
}
