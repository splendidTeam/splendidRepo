/**
 * Copyright (c) 2008-2012 FeiLong Inc. All Rights Reserved.
 * <p>
 * 	This software is the confidential and proprietary information of FeiLong Network Technology, Inc. ("Confidential Information").  
 * 	You shall not disclose such Confidential Information and shall use it 
 *  only in accordance with the terms of the license agreement you entered into with FeiLong.
 * </p>
 * <p>
 * 	feilong makes no representations or warranties about the suitability of the software, either express or implied, 
 * 	including but not limited to the implied warranties of merchantability, fitness for a particular
 * 	purpose, or non-infringement.  
 * 	feilong shall not be liable for any damages suffered by licensee as a result of using, modifying or distributing
 * 	this software or its derivatives.
 * </p>
 */
package com.baozun.nebula.solr.utils;

/**
 * date pattern. 
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2012 1 21 04:18:00
 */
public final class DatePattern{

	/** <code>{@value}</code> 年月 带水平线,一般用于分类日志,将众多日志按月分类 example:2012-01. */
	public static final String	yearAndMonth				= "yyyy-MM";

	/** <code>{@value}</code> 只有日期 年月日 example:2012-01-22. */
	public static final String	onlyDate					= "yyyy-MM-dd";

	/** <code>{@value}</code> 月日 example:01-22. */
	public static final String	monthAndDay					= "MM-dd";

	/** <code>{@value}</code> 月日带星期 example:01-22(星期四). */
	public static final String	monthAndDayWithWeek			= "MM-dd(E)";

	/** <code>{@value}</code> 不带秒. */
	public static final String	commonWithoutSecond			= "yyyy-MM-dd HH:mm";

	/** <code>{@value}</code>. */
	public static final String	commonWithTime				= "yyyy-MM-dd HH:mm:ss";

	/** <code>{@value}</code>. */
	public static final String	yyyyMMdd					= "yyyyMMdd";

	/** <code>{@value}</code> 带毫秒的时间格式. */
	public static final String	commonWithMillisecond		= "yyyy-MM-dd HH:mm:ss.SSS";

	/** <code>{@value}</code> 不带年 不带秒. */
	public static final String	commonWithoutAndYearSecond	= "MM-dd HH:mm";

	/** <code>{@value}</code> 只有时间且不带秒. */
	public static final String	onlyTime_withoutSecond		= "HH:mm";

	/** <code>{@value}</code> 只有时间. */
	public static final String	onlyTime					= "HH:mm:ss";

	/** 时间戳,<code>{@value}</code>,一般用于拼接文件名称. */
	public static final String	timestamp					= "yyyyMMddHHmmss";

	/** 带毫秒的时间戳,<code>{@value}</code> . */
	public static final String	timestampWithMillisecond	= "yyyyMMddHHmmssSSS";
}
