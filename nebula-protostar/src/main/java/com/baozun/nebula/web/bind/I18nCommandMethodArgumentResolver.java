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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.baozun.nebula.utilities.common.StringUtil;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;

public class I18nCommandMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (!parameter.hasParameterAnnotation(I18nCommand.class)) {
			return false;
		}
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		I18nCommand c = parameter.getParameterAnnotation(I18nCommand.class);
		String parameterName = c.name();
		if (parameter.getParameterType().isArray()) {
			Object obj =resolveArgumentAarry(parameter, mavContainer, webRequest, binderFactory);
			return obj;
		}
		Class<?> itemClass = parameter.getParameterType();
		if ("".equals(parameterName)) {
			parameterName = parameter.getParameterName();
		}
		Map<String, LangProperty> map = I18nCommandUtil.setI18nInfo(itemClass,parameterName);
		Set<String> i18nKeys = map.keySet();
		List<String> candidateParamNames = new ArrayList<String>();
		List<String> i18nCandidateParamNames = new ArrayList<String>();
		Iterator<String> spit = webRequest.getParameterNames();
		while (spit.hasNext()) {
			String paramName = spit.next();
			if (paramName.startsWith(parameterName + ".")){
				boolean i18nParam = true;
				for (String key : i18nKeys) {
					if(paramName.startsWith(key)){
						i18nCandidateParamNames.add(paramName);
						i18nParam = false;
						break;
					}
				}
				if(i18nParam){
					candidateParamNames.add(paramName);
				}
			}
		}
		if (candidateParamNames.size() == 0 && i18nCandidateParamNames.size() == 0) {
			// no input
			return itemClass.newInstance();
		} else {
			Map<String, String[]> paramValues = new HashMap<String, String[]>();
			int size = -1;
			for (String cpname : candidateParamNames) {
				String[] paramStrs = webRequest.getParameterValues(cpname);

				if (paramStrs == null)
					paramStrs = new String[0];
				if (size < 0)
					size = paramStrs.length;
				else if (size != paramStrs.length) {
					throw new IllegalArgumentException(
							"It seems that the number of "
									+ cpname
									+ " is not right, it is different with others: "
									+ paramStrs.length + "|" + size);
				}
				String propName = cpname
						.substring(parameterName.length() + 1);
				if (propName.endsWith("[]")){
					propName = propName.substring(0, propName.length() - 2);
				}
				paramValues.put(propName, paramStrs);
			}
			Object rtn = itemClass.newInstance();
			for (int i = 0; i < size; i++) {
				Object obj = BeanUtils.instantiateClass(itemClass);
				DataBinder db = new DataBinder(obj);
				Map<String, String> paramMap = new HashMap<String, String>();
				for (String key : paramValues.keySet()){
					paramMap.put(key, paramValues.get(key)[i]);
				}
				db.bind(new MutablePropertyValues(paramMap));
				rtn = db.getTarget();
			}
			//取出国际化数据并设置
			Map<String, List<String>> propertyValueMap = new  HashMap<String, List<String>>();
			boolean  i18n =  LangProperty.getI18nOnOff();
			if(i18n){
				sortI18nArr(i18nCandidateParamNames);
			}
			for (String cpname : i18nCandidateParamNames) {
				String[] paramStrs = webRequest.getParameterValues(cpname);
				if (paramStrs == null){
					paramStrs = new String[0];
				}
				String key = null;
				if(i18n){
					key = cpname.substring(0, cpname.indexOf("["));
				}else{
					key = cpname;
				}
				List<String> paramList = Arrays.asList(paramStrs);
				if(propertyValueMap.containsKey(key)){
					if(i18n){
						String num = cpname.substring(cpname.indexOf("[")+1, cpname.indexOf("]"));
						int index = Integer.parseInt(num);
						if(propertyValueMap.get(key).size()<index){
							propertyValueMap.get(key).addAll(paramList);
						}else{
							propertyValueMap.get(key).addAll(index,paramList);
						}
					}else{
						propertyValueMap.get(key).addAll(paramList);
					}
				}else{
					List<String>  values = new ArrayList<String>();
					values.addAll(paramList);
					propertyValueMap.put(key, values);
				}
			}
			Set<String>  propertyKeys = propertyValueMap.keySet();
			if(propertyKeys!=null && propertyKeys.size()>0){
				for (String cpname : propertyKeys) {
					List<String> paramStrs = propertyValueMap.get(cpname);
					String pname = cpname.substring(cpname.indexOf(".")+1, cpname.lastIndexOf("."));
					String i18nPro = cpname.substring(cpname.lastIndexOf(".")+1,cpname.length());
					LangProperty langProperty = map.get(parameterName+"."+pname+".");
					if(i18n){
						PropertyUtils.setProperty(langProperty, i18nPro, paramStrs.toArray(new String[]{}));
					}else{
						PropertyUtils.setProperty(langProperty, i18nPro, paramStrs.get(0));
					}
					PropertyUtils.setProperty(rtn, pname, langProperty);
				}
			}
			return rtn;
		}
	}
	
	private  void sortI18nArr(List<String> list){
		if(list != null){
			Map<String, String> map = new HashMap<String, String>();
			for (String cpname : list) {
				String str = cpname.substring(0,cpname.length()-3);
				map.put(str, cpname);
			}
			Set<String> sortNums = map.keySet();
			list.clear();
			for (String key : sortNums) {
				for (int i = 0; i < MutlLang.i18nSize(); i++) {
					list.add(key+"["+i+"]");
				}
			}
		}
	
	}
	public Object  resolveArgumentAarry(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception{
		I18nCommand c = parameter.getParameterAnnotation(I18nCommand.class);
		String parameterName = c.name();
		Class<?> itemClass = parameter.getParameterType().getComponentType();
		if("".equals(parameterName)){
			parameterName = parameter.getParameterName();
		}
		Map<String, LangProperty> map = I18nCommandUtil.setI18nInfo(itemClass,parameterName);
		Set<String> i18nKeys = map.keySet();
		List<String> candidateParamNames = new ArrayList<String>();
		List<String> i18nCandidateParamNames = new ArrayList<String>();
		Iterator<String> spit = webRequest.getParameterNames();
		while(spit.hasNext()){
			String paramName = spit.next();
			if (paramName.startsWith(parameterName + ".")){
				boolean i18nParam = true;
				for (String key : i18nKeys) {
					if(paramName.startsWith(key)){
						i18nCandidateParamNames.add(paramName);
						i18nParam = false;
						break;
					}
				}
				if(i18nParam){
					candidateParamNames.add(paramName);
				}
			}
				
		}
		if(candidateParamNames.size() ==0){
			//no input
			return Array.newInstance(itemClass, 0);
		}
		else{
			Map<String, String[]> paramValues = new HashMap<String, String[]>();
			int size = -1;
			for(String cpname: candidateParamNames){
				String[] paramStrs = webRequest.getParameterValues(cpname);	
				
				if(paramStrs == null)
					paramStrs = new String[0];
				if(size<0) size = paramStrs.length;
				else if(size != paramStrs.length){
					throw new IllegalArgumentException("It seems that the number of " + cpname + " is not right, it is different with others: " +
								paramStrs.length + "|" + size);
				}
				String propName = cpname.substring(parameterName.length()+1);
				if(propName.endsWith("[]")){
					propName = propName.substring(0, propName.length()-2);
				}
				paramValues.put(propName, paramStrs);
			}
			Object rtn = Array.newInstance(itemClass, size);
			for(int i=0; i< size; i++){
				Object obj = BeanUtils.instantiateClass(itemClass);
				DataBinder db = new DataBinder(obj);
				Map<String,String> paramMap = new HashMap<String, String>();
				for(String key: paramValues.keySet()){
					paramMap.put(key, paramValues.get(key)[i]);
				}
				db.bind(new MutablePropertyValues(paramMap));
				Object command = db.getTarget();
				//处理国际化数据
				Map<String, List<String>> propertyValueMap = new  HashMap<String, List<String>>();
				boolean  i18n =  LangProperty.getI18nOnOff();
				
				Map<String,String[]> keyValArrayMap =new  HashMap<String, String[]>(); 
				
				String[] i18nValues = null;
				int i18nSize =MutlLang.i18nSize();
				
				for (String cpname : i18nCandidateParamNames) {
					String[] paramStrs = webRequest.getParameterValues(cpname);
					
					
					if (paramStrs == null){
						paramStrs = new String[0];
					}
					String key = null;
					if(i18n){
						key = cpname.substring(0, cpname.indexOf("-"));
					}else{
						if(cpname.endsWith("[]")){
							cpname = cpname.substring(0, cpname.length()-2);
						}
						key = cpname;
					}
					
					if(i18n){
						String num = cpname.substring(cpname.indexOf("[")+1, cpname.indexOf("]"));
						if(num.indexOf("-")>-1){
							num = num.split("-")[1];
						}
						int index = Integer.parseInt(num);
						
						if(keyValArrayMap.containsKey(key)){
							i18nValues =keyValArrayMap.get(key);
						}else{
							i18nValues =new String[i18nSize];
							
						}
						i18nValues[index] =paramStrs[0];
						keyValArrayMap.put(key, i18nValues);
						
						List<String> paramList =Arrays.asList(keyValArrayMap.get(key));
						
						propertyValueMap.put(key, paramList);						
						
					}else{
						List<String> paramList = Arrays.asList(paramStrs);
						if(propertyValueMap.containsKey(key)){
							propertyValueMap.get(key).addAll(paramList);
						}else{
							List<String>  values = new ArrayList<String>();
							values.addAll(paramList);
							propertyValueMap.put(key, values);
						}
					}
					
					
				}
				Set<String>  propertyKeys = propertyValueMap.keySet();
				if(propertyKeys!=null && propertyKeys.size()>0){
					for (String cpname : propertyKeys) {
						String num = null;
						if(i18n){
							num = cpname.substring(cpname.indexOf("[")+1);
						}else{
							num = cpname.substring(cpname.indexOf("[")+1,cpname.indexOf("]"));
						}
						if(num.indexOf("-")>-1){
							num = num.split("-")[0];
						}
						if(StringUtil.isNotNull(num)){
							int index = Integer.parseInt(num);
							if(index != i){
								continue;
							}
						}
						
						List<String> paramStrs = propertyValueMap.get(cpname);
						String pname = cpname.substring(cpname.indexOf(".")+1, cpname.lastIndexOf("."));
						String i18nPro = cpname.substring(cpname.lastIndexOf(".")+1,cpname.indexOf("["));
						LangProperty langProperty = null;
						if(i18n){
							Object value = PropertyUtils.getProperty(command, pname);
							if(value == null){
								langProperty = new MutlLang();
							}else{
								langProperty = (LangProperty) value;
							}
							PropertyUtils.setProperty(langProperty, i18nPro, paramStrs.toArray(new String[]{}));
						}else{
							Object value = PropertyUtils.getProperty(command, pname);
							if(value == null){
								langProperty = new SingleLang();
							}else{
								langProperty = (SingleLang)value;
							}
							PropertyUtils.setProperty(langProperty, i18nPro, paramStrs.get(0));
						}
						PropertyUtils.setProperty(command, pname, langProperty);
					}
				}
				Array.set(rtn, i, command);
			}
			return rtn;
		}
		
	}
}
