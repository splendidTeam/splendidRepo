/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.search.command;

import java.io.Serializable;

/**
 * 元数据的包装类
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午5:07:37
 */
public class MetaDataCommand implements Serializable{

	private static final long	serialVersionUID	= -354234734048388257L;

	/** id */
	private Long				id;

	/** 名称或者值 */
	private String				name;

	/** 排序字段 */
	private Integer				sortNo;
	
	/**父节点id，分类和导航使用到，属性不需要使用*/
	private Long				parentId;

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
	 * get name
	 * 
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * set name
	 * 
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
