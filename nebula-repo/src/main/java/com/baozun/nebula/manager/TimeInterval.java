package com.baozun.nebula.manager;
/**
 * 时间间隔(一般以秒为单位) <br>
 * Integer.MAX_VALUE:2147483647<br>
 * Integer.MIN_VALUE-2147483648<br>
 * 一年数据为 31536000,所以 Integer 最大为 68.096259734906 年
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2012-5-18 下午2:57:14
 */
public final class TimeInterval{

	private TimeInterval(){}

	/**
	 * 1小时 60 * 60
	 */
	public static final Integer	SECONDS_PER_HOUR	= 60 * 60;

	/**
	 * 1天 60 * 60 * 24
	 */
	public static final Integer	SECONDS_PER_DAY		= SECONDS_PER_HOUR * 24;

	/**
	 * 一个星期 60 * 60 * 24 * 7
	 */
	public static final Integer	SECONDS_PER_WEEK	= SECONDS_PER_DAY * 7;

	/**
	 * 30天 一个月 60 * 60 * 24 * 30
	 */
	public static final Integer	SECONDS_PER_MONTH	= SECONDS_PER_DAY * 30;

	/**
	 * 365天 1年 60 * 60 * 24 * 365=31536000<br>
	 * Integer.MAX_VALUE:2147483647<br>
	 * Integer.MIN_VALUE-2147483648<br>
	 * 一年数据为 31536000,所以 Integer 最大为 68.096259734906 年
	 */
	public static final Integer	SECONDS_PER_YEAR	= SECONDS_PER_DAY * 365;
}
