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
 *
 */
package com.baozun.nebula.utilities.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtil {
	private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

	/**
	 * 取得泛型类定义中的第一个泛型定义的类型，如SkuDao extends GenericDao<Sku,Long>，返回Sku.class
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericModelClass(Class<?> clazz){
        Type type = clazz.getGenericSuperclass();
        while (!(type instanceof ParameterizedType) && clazz != null && clazz != Object.class){
                clazz = clazz.getSuperclass();
                type = clazz.getGenericSuperclass();
        }
        if (!(type instanceof ParameterizedType)){
                Class<?>[] iclazzs = clazz.getInterfaces();
                if (iclazzs.length > 0){
                        int index = -1;
                        if (index >= 0){
                                if (clazz.getGenericInterfaces()[index] instanceof ParameterizedType)
                                        type = clazz.getGenericInterfaces()[index];
                        }
                }
        }
        if (!(type instanceof ParameterizedType)){
                throw new RuntimeException("Can not find the right Generic Class.");
        }
        ParameterizedType pType = (ParameterizedType) type;
        return (Class<T>) pType.getActualTypeArguments()[0];
	}
	
	/**
	 * 获取对象中的属性值，对象可以是Java bean或者Map，属性定义可以多层，以"."分隔，如dept.name
	 * @param obj
	 * @param prop
	 * @return
	 */
	public static Object getPropertyValue(Object obj, String prop){
		//TODO
		return null;
	}
	
	/**
	 * 设置对象中的属性值，对象可以是Java bean或者Map，属性定义可以多层，以"."分隔，如dept.name
	 * @param obj
	 * @param prop
	 * @param value
	 */
	public static void setPropertyValue(Object obj, String prop, String value){
		//TODO
	}
	
	/**
	 * 获取对象中某个Field的值
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws IllegalArgumentException, 
			IllegalAccessException{
		if(obj == null || fieldName == null) return null;
		Field f = getField(obj.getClass(), fieldName);
		f.setAccessible(true);
		return f.get(obj);
	}
	
	/**
	 * 设置对象中某个Field的值
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalArgumentException, 
			IllegalAccessException{
		if(obj == null || fieldName == null) return;
		Field f = getField(obj.getClass(), fieldName);
		f.setAccessible(true);
		f.set(obj, value);
	}
	
	/**
	 * 获取类中的某个Field，可以查找到父类中的私有Field
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName){
		if(clazz == null || fieldName == null) return null;
		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			try {
				f = clazz.getField(fieldName);
			} catch (NoSuchFieldException e1) {
				if(clazz.getSuperclass() == null)
					return f;
				return getField(clazz.getSuperclass(), fieldName);
			}
		}
		return f;
	}
	
	/**
	 * 调用无参数方法
	 * @param obj
	 * @param methodName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invoke(Object obj, String methodName) throws IllegalArgumentException, 
			IllegalAccessException, InvocationTargetException{
		if(obj == null) throw new IllegalArgumentException();
		Method m = getMethod(obj.getClass(), methodName);
		return invoke(obj, m);
	}
	
	/**
	 * 调用方法
	 * @param obj
	 * @param m
	 * @param params
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invoke(Object obj, Method m, Object... params) throws IllegalArgumentException, 
			IllegalAccessException, InvocationTargetException{
		if(obj == null || m == null) throw new IllegalArgumentException();
		m.setAccessible(true);
		return m.invoke(obj, params);
	}
	
	/**
	 * 查找方法并返回，可以查找到该方法甚至父类方法中的私有方法
	 * @param clazz
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes){
		if(clazz == null || methodName == null) return null;
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			try {
				m = clazz.getMethod(methodName, paramTypes);
			} catch (NoSuchMethodException e1) {
				if(clazz.getSuperclass() == null)
					return m;
				return getMethod(clazz.getSuperclass(), methodName, paramTypes);
			}
		}
		return m;
	}
}
