/**
 * 
 */
package com.baozun.nebula.utils.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.DataBinder;

/**
 * @author xianze.zhang
 *@creattime 2013-6-7
 */
public class ListConvertUtil {
	/**
	 * 转换分隔符为逗号的字符串，转成指定类型的数组
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> List<T> convertCommaString(String str,Class<T> type,String splitString){
		if(StringUtils.isBlank(str)){
			return null;
		}

		String[] strArray=str.split(splitString);
		List<T> result=new ArrayList<T>();
		DataBinder binder = new DataBinder(null);
		for(String s:strArray){
			result.add(binder.convertIfNecessary(s, type));
		}
		return result;
	}
	public static void main(String[] args) {
		List<Long> convertCommaString = ListConvertUtil.convertCommaString("1,2,3", Long.class,",");
		System.out.println(convertCommaString.get(2));
	}
}
