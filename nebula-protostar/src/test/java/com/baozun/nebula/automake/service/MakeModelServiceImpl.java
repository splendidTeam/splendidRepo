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
package com.baozun.nebula.automake.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.Modifier;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import com.baozun.nebula.automake.model.MakeModel;
import com.baozun.nebula.automake.model.PropertyDesc;

/**
 * @author Justin
 *
 */
public class MakeModelServiceImpl implements MakeModelService {

	/**
	 * 通过字段查找对应的annotation数组
	 * 如果找不到,则在getxxx方法中查找
	 * @param field
	 * @return
	 */
	private Annotation[] findAnnosByFiledName(Class clazz,Field field){
		
		Annotation[] annos=field.getAnnotations();
		
		
		if(annos.length==0){
			String columnName=field.getName();
			 String getMethodName = "get"  
                + columnName.substring(0, 1).toUpperCase()  
                + columnName.substring(1);  
			Method method= ReflectionUtils.findMethod(clazz, getMethodName);
			if(method!=null)
				annos=method.getAnnotations();
		}
		
		 
		return annos;
	}
	
	
	/**
	 * 获取最后一级包名
	 * @param name
	 * @return
	 */
	private String queryLastPackageName(String name){
		int index=name.lastIndexOf(".");
		if(index!=-1){
			name=name.substring(0,index);
		}
		index=name.lastIndexOf(".");
		if(index!=-1){
			name=name.substring(index+1);
		}
		
		return name;
		
	}
	
	/**
	 * 清除包名
	 * @param name
	 * @return
	 */
	private String clearPackage(String name){
		int index=name.lastIndexOf(".");
		if(index!=-1){
			name=name.substring(index+1);
		}
		return name;
	}
	
	/**
	 * 过滤掉一部分属性
	 * 如 id ,version
	 * @param sourceList
	 * @return
	 */
	private List<PropertyDesc> filterPropertyList(List<PropertyDesc> sourceList,MakeModel mm){
		
		List<PropertyDesc> result=new ArrayList<PropertyDesc>();
		
		for(PropertyDesc pd:sourceList){
			
			if(!pd.getFieldName().equals(mm.getPkName()) 
					&& !pd.getFieldName().equals(mm.getVersionField()) 
					&& !pd.getFieldName().equals(mm.getLifecycle())
				){
				result.add(pd);
			}
		}
		
		return result;
	}
	
	/**
	 * 通过class反射机制生成makeModel对象
	 * @param clazz
	 * @param authName
	 * @param lifecycle
	 * @return
	 */
	public MakeModel queryByClass(Class clazz){
		
		MakeModel mm=new MakeModel();
		
		Field[] fields=clazz.getDeclaredFields();
		
		for(Field field:fields){
			
			if(Modifier.isStatic(field.getModifiers())){	//不用处理static字段
				continue;
			}
			
			PropertyDesc pd=new PropertyDesc();
			
			Annotation[] annos=findAnnosByFiledName(clazz,field);
			//是否发现Column,Joincolumn注解
			boolean isfindColumn=false;
			for(Annotation anno:annos){
				
				if(anno instanceof Id){
					mm.setPkName(field.getName());
				}
				else if(anno instanceof Column){
					Column column=(Column)anno;
					pd.setFieldName(field.getName());
					pd.setColumnName(column.name());
					pd.setFieldType(clearPackage(field.getType().getName()));
					isfindColumn=true;
				}
				else if(anno instanceof JoinColumn){
					JoinColumn column=(JoinColumn)anno;
					pd.setFieldName(field.getName());
					pd.setColumnName(column.name());
					pd.setFieldType(clearPackage(field.getType().getName()));
					isfindColumn=true;
				}
				else if(anno instanceof SequenceGenerator){
					SequenceGenerator sg=(SequenceGenerator)anno;
					mm.setSequeneName(sg.sequenceName());
				}
				else if(anno instanceof Version){
					mm.setVersionField(field.getName());
				}
				else if(anno instanceof Transient){	//如果字段不是持久化字段,直接跳过
					continue;
				}
				
			}
			//如果没有发现Column,Joincolumn,则代表此字段使用默认的设置
			if(!isfindColumn){
				pd.setFieldName(field.getName());
				pd.setColumnName(field.getName());
				pd.setFieldType(clearPackage(field.getType().getName()));
			}
			mm.getPropertyList().add(pd);
			
			
		}

		mm.setEntityName(clearPackage(clazz.getName()));
		mm.setPackagName(queryLastPackageName(clazz.getName()));
		Annotation[] classAnnos=clazz.getAnnotations();
		
		for(Annotation anno:classAnnos){
			if(anno instanceof Table){
				Table table=(Table)anno;
				mm.setTableName(table.name());
			}
		}
		//如果不显示定义表名,直接使用类名
		if(mm.getTableName()==null){
			mm.setTableName(mm.getEntityName());
		}
		//过滤掉id以及version字段
		mm.setPropertyList(filterPropertyList(mm.getPropertyList(),mm));
		
		return mm;
	}
}
