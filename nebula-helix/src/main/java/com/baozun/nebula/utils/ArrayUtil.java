package com.baozun.nebula.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 数组工具类
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2010-4-16 下午01:00:27
 * @since 1.0
 * @deprecated pls use {@link com.feilong.core.lang.ArrayUtil}
 */
@Deprecated
public final class ArrayUtil{

	/** Don't let anyone instantiate this class. */
	private ArrayUtil(){}

	/**
	 * 将数组转成转成Iterator
	 * 
	 * @param arrays
	 *            数组
	 * @return Iterator
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<?> toIterator(Object arrays){
		Iterator<?> iterator = null;
		try{
			// 如果我们幸运的话，它是一个对象数组,我们可以遍历并with no copying
			iterator = Arrays.asList((Object[]) arrays).iterator();
		}catch (ClassCastException e){
			// Rats -- 它是一个基本类型数组
			int length = Array.getLength(arrays);
			ArrayList arrayList = new ArrayList(length);
			for (int i = 0; i < length; ++i){
				arrayList.add(Array.get(arrays, i));
			}
			iterator = arrayList.iterator();
		}
		return iterator;
	}

	/**
	 * 数组转成LinkedList
	 * 
	 * @param strings
	 *            字符串数组
	 * @return 数组转成LinkedList
	 */
	public static LinkedList<String> stringsToLinkedList(String[] strings){
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0, j = strings.length; i < j; ++i){
			list.add(strings[i]);
		}
		return list;
	}

	/**
	 * 数组转成 List,如果objects为空,则返回null
	 * 
	 * @param objects
	 *            object数组
	 * @return 数组转成 List
	 */
	public static List<?> toList(Object[] objects){
		if (Validator.isNullOrEmpty(objects)){
			return null;
		}
		return Arrays.asList(objects);
	}

	/**
	 * 任意的数组转成Integer 数组
	 * 
	 * @param objects
	 * @return 一旦其中有值转换不了integer,则出现参数异常
	 */
	public static Integer[] toIntegers(Object[] objects){
		if (Validator.isNotNullOrEmpty(objects)){
			int length = objects.length;
			Integer[] integers = new Integer[length];
			for (int i = 0; i < length; i++){
				integers[i] = ObjectUtil.toInteger(objects[i]);
			}
			return integers;
		}
		return null;
	}

	/**
	 * 判断 一个数组中,是否包含某个特定的值
	 * 
	 * @param array
	 *            数组
	 * @param value
	 *            特定值
	 * @return 如果包含,则返回true
	 */
	public static <T> boolean isContain(T[] array,T value){
		if (Validator.isNotNullOrEmpty(array)){
			for (T t : array){
				if (t.equals(value)){
					return true;
				}
			}
		}
		return false;
	}
}
