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
package com.baozun.nebula.web.command;

import java.util.Map;

import com.baozun.nebula.command.Command;

/**
 * 用于上传属性值的command
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月14日 下午3:30:26
 */
public class PropertyValueUploadCommand implements Command{

	private static final long	serialVersionUID	= 1L;

	/** 属性值id */
	private Long				id;

	/** 属性值 */
	private String				value;

	/** 排序 */
	private Integer				sortNo;

	/** 属性值id的国际化,key:语言；value:值 */
	private Map<String, String>	valueLangMap;

	/**
	 * get id
	 * 
	 * @return id
	 */
	public Long getId(){
		return id;
	}

	/**
	 * set id
	 * 
	 * @param id
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * get value
	 * 
	 * @return value
	 */
	public String getValue(){
		return value;
	}

	/**
	 * set value
	 * 
	 * @param value
	 */
	public void setValue(String value){
		this.value = value;
	}

	/**
	 * get sortNo
	 * 
	 * @return sortNo
	 */
	public Integer getSortNo(){
		return sortNo;
	}

	/**
	 * set sortNo
	 * 
	 * @param sortNo
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}

	/**
	 * get valueLangMap
	 * 
	 * @return valueLangMap
	 */
	public Map<String, String> getValueLangMap(){
		return valueLangMap;
	}

	/**
	 * set valueLangMap
	 * 
	 * @param valueLangMap
	 */
	public void setValueLangMap(Map<String, String> valueLangMap){
		this.valueLangMap = valueLangMap;
	}

}
