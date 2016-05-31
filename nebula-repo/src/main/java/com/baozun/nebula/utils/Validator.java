/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.utils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 飞龙通用验证类,该类最实用的方法,核心类之一<br>
 * isNullOrEmpty(Object value),判断元素是否为空
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Sep 2, 2010 8:35:28 PM
 * @version 1.1 2012-9-23 21:34 api更新,isNullOrEmpty 替换isNull(deprecated)
 * @since 1.0
 * @deprecated pls use {@link com.feilong.core.Validator}
 */
@Deprecated
public final class Validator{

	@SuppressWarnings("unused")
	private final static Logger	log	= LoggerFactory.getLogger(Validator.class);

	/** Don't let anyone instantiate this class. */
	private Validator(){}

	/**
	 * 判断元素是否为Null或者Empty
	 * <p>
	 * 目前可以判断一下元素
	 * <ul>
	 * <li>Collection,使用其isEmpty()</li>
	 * <li>Map,使用其isEmpty()</li>
	 * <li>Object[],判断length==0</li>
	 * <li>String,使用.trim().length()效率高</li>
	 * <li>Enumeration,使用hasMoreElements()</li>
	 * <li>Iterator,使用hasNext()</li>
	 * </ul>
	 * 
	 * @param value
	 *            可以是Collection,Map,Object[],String,Enumeration,Iterator类型
	 * @return 空返回true
	 * @since 1.0
	 */
	@SuppressWarnings("rawtypes")
	public final static boolean isNullOrEmpty(Object value){
		if (null == value){
			return true;
		}
		/*****************************************************************************/
		boolean flag = false;
		// 字符串
		if (value instanceof String){
			// 比较字符串长度, 效率高
			flag = value.toString().trim().length() <= 0;
		}
		// Object[]object数组
		else if (value instanceof Object[]){
			flag = ((Object[]) value).length == 0;
		}
		// ***********************************************************
		// 集合
		else if (value instanceof Collection){
			flag = ((Collection) value).isEmpty();
		}
		// map
		else if (value instanceof Map){
			flag = ((Map) value).isEmpty();
		}
		// 枚举
		else if (value instanceof Enumeration){
			flag = !((Enumeration) value).hasMoreElements();
		}
		// Iterator迭代器
		else if (value instanceof Iterator){
			flag = !((Iterator) value).hasNext();
		}
		return flag;
	}

	/**
	 * 判断元素是否不为Null或者Empty,调用<code>!isNullOrEmpty</code>方法
	 * <p>
	 * 目前可以判断一下元素
	 * <ul>
	 * <li>Collection,使用其isEmpty()</li>
	 * <li>Map,使用其isEmpty()</li>
	 * <li>Object[],判断length==0</li>
	 * <li>String,使用.trim().length()效率高</li>
	 * <li>Enumeration,使用hasMoreElements()</li>
	 * <li>Iterator,使用hasNext()</li>
	 * </ul>
	 * 
	 * @param value
	 *            可以是Collection,Map,Object[],String,Enumeration,Iterator类型
	 * @return 不为空返回true
	 * @since 1.0
	 */
	public final static boolean isNotNullOrEmpty(Object value){
		return !isNullOrEmpty(value);
	}

}