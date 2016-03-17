/**
 * 
 */
package com.baozun.nebula.utils.property;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtilsBean;


/**
 * @author xianze.zhang
 *@creattime 2013-7-8
 */
public class PropertyUtil{
	private static PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
	/**
	 * 根据属性名获取bean中的属性值
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Object getProperty(Object bean, String propertyName){
		try{
			return propertyUtils.getProperty(bean, propertyName);
		}catch (IllegalAccessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvocationTargetException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchMethodException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
