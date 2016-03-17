/**
 * 
 */
package com.baozun.nebula.utils.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.DataBinder;

/**
 * 对象转换工具类
 * @author xianze.zhang
 *@creattime 2013-6-7
 */
public class ObjectConvertUtil {
	private static final DataBinder BINDER = new DataBinder(null);
	/**
	 * 简单类型转换，
	 * 例如：
	 * String
	 * Long
	 * Integer
	 * 
	 * @param value 需要转换的对象
	 * @param requiredType 希望转成的类型
	 * @return
	 */
	
	public static Object simpleTypeConvert(Object value, Class requiredType){
		return BINDER.convertIfNecessary(value,requiredType);
	}
}
