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
 */
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * 商品属性 每个属性都属于一个行业 有字段区分是否系统属性 系统属性表示此属性是某行业公共的属性 非系统属性表示此属性是某店铺自定义的（扩展用）
 * 
 * @author jinbao.ji
 * @date 2015年11月16日 下午6:50:52
 */
public class CommonPropertyCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/** PK. */
	private Long				id;

	/** 名称 */
	private String				name;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/**
	 * 编辑类型 ：1 单行输入2可输入单选3单选4多选5自定义多选
	 */
	private Integer				editingType;

	/**
	 * 值类型 1 文本 2 数值 3日期 4日期时间
	 */
	private Integer				valueType;

	/**
	 * 是否销售属性
	 */
	private Boolean				isSaleProp;

	/**
	 * 是否颜色属性
	 */
	private Boolean				isColorProp;

	/**
	 * 使用了该属性的行业名称集合(逗号隔开)
	 */
	private String		industrylist;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Date getVersion(){
		return version;
	}

	public Integer getEditingType(){
		return editingType;
	}

	public void setEditingType(Integer editingType){
		this.editingType = editingType;
	}

	public Integer getValueType(){
		return valueType;
	}

	public void setValueType(Integer valueType){
		this.valueType = valueType;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime(){
		return modifyTime;
	}

	public Boolean getIsSaleProp(){
		return isSaleProp;
	}

	public void setIsSaleProp(Boolean isSaleProp){
		this.isSaleProp = isSaleProp;
	}

	public Boolean getIsColorProp(){
		return isColorProp;
	}

	public void setIsColorProp(Boolean isColorProp){
		this.isColorProp = isColorProp;
	}

	
	public String getIndustrylist(){
		return industrylist;
	}

	
	public void setIndustrylist(String industrylist){
		this.industrylist = industrylist;
	}

	
 
}
