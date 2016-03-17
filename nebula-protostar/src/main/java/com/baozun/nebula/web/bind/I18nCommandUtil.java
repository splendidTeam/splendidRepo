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

package com.baozun.nebula.web.bind;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;

public class I18nCommandUtil {
	private static Map<String,Map<String, LangProperty>> i18nCommandCache = new ConcurrentHashMap<String, Map<String,LangProperty>>();
	
	public static Map<String, LangProperty> setI18nInfo(Class<?> clazz ,String parameterName) {
		String clsName = clazz.getName();
		Map<String, LangProperty>  map = new  HashMap<String, LangProperty>();
		boolean  i18n =  LangProperty.getI18nOnOff();
		LangProperty lang = null;
		if(i18nCommandCache.containsKey(clsName)){
			Map<String, LangProperty> lps = i18nCommandCache.get(clsName);
			Set<String> keys = lps.keySet();
			for (String key : keys) {
				if(i18n){
					lang = new MutlLang();
				}else{
					lang = new SingleLang();
				}
				map.put(key, lang);
			}
			return map;
		}
		//取出langProperty对象
		List<Field> fields = getFieldColumnMap(clazz) ;
	
		for (Field field : fields) {
			String name =  field.getName();
			if(i18n){
				lang = new MutlLang();
			}else{
				lang = new SingleLang();
			}
			map.put(parameterName+"."+name+".", lang);
		}
		i18nCommandCache.put(clazz.getName(), map);
		return map;
	}
	
	public static List<Field> getFieldColumnMap(Class<?> clazz) {
		List<Field> fieldColumns = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isFinal(field.getModifiers())
					|| Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			Class<?> type = field.getType();
			if(type.equals(LangProperty.class)){
				fieldColumns.add(field);
			}
		}
		if (clazz.getSuperclass() != null) {
			Class<?> superClass = clazz.getSuperclass();
			fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				Class<?> type = field.getType();
				if(type.equals(LangProperty.class)){
					fieldColumns.add(field);
				}
			}
		}
		
		return fieldColumns;
	}

}
