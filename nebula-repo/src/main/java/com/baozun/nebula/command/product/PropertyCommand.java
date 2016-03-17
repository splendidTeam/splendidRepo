/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command.product;

import java.util.Date;
import java.util.List;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.product.Industry;

/**
 * 商品属性 每个属性都属于一个行业 有字段区分是否系统属性 系统属性表示此属性是某行业公共的属性 非系统属性表示此属性是某店铺自定义的
 * 
 * @author dianchao.song
 */
public class PropertyCommand implements Command{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -7403558333855971007L;

	/** PK. */
	private Long				id;

	/**
	 * 所属属性分组行业
	 */
	private Long				industryId;

	/** 名称 */
	private LangProperty		name;

	/** 对应的公用属性名称 */
	private String		commonName;

	/** 顺序. */
	private Integer				sortNo;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/**
	 * 是否行业公共属性
	 */
	private Boolean				isCommonIndustry;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

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
	 * 是否必输
	 */
	private Boolean				required;

	/**
	 * 是否用于搜索
	 */
	private Boolean				searchable;

	/**
	 * 是否配图
	 */
	private Boolean				hasThumb;

	/**
	 * 属性分组
	 */
	private LangProperty		groupName;

	private String				industryName;

	/**
	 * 行业属性表（扩展用）
	 */
	private Long				commonPropertyId;

	/**
	 * 使用了该属性的行业集合
	 */
	private List<Industry>		industrylist;

	/**
	 * 使用方式
	 */
	private String				usertype;

	public String getIndustryName(){
		return industryName;
	}

	public void setIndustryName(String industryName){
		this.industryName = industryName;
	}

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Column("ID")
	public Long getId(){
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * Gets the 名称.
	 * 
	 * @return the 名称
	 */
	public LangProperty getName(){
		return name;
	}

	/**
	 * Sets the 分类名称.
	 * 
	 * @param name
	 *            the new 分类名称
	 */
	public void setName(LangProperty name){
		this.name = name;
	}

	/**
	 * Gets the 顺序.
	 * 
	 * @return the 顺序
	 */
	@Column("SORT_NO")
	public Integer getSortNo(){
		return sortNo;
	}

	/**
	 * Sets the 顺序.
	 * 
	 * @param priority
	 *            the new 顺序
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Column("VERSION")
	public Date getVersion(){
		return version;
	}

	@Column("INDUSTRY_ID")
	public Long getIndustryId(){
		return industryId;
	}

	public void setIndustryId(Long industryId){
		this.industryId = industryId;
	}

	@Column("EDITING_TYPE")
	public Integer getEditingType(){
		return editingType;
	}

	public void setEditingType(Integer editingType){
		this.editingType = editingType;
	}

	@Column("VALUE_TYPE")
	public Integer getValueType(){
		return valueType;
	}

	public void setValueType(Integer valueType){
		this.valueType = valueType;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(Date version){
		this.version = version;
	}

	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	@Column("CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Column("MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column("LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	@Column("IS_SALEP_ROP")
	public Boolean getIsSaleProp(){
		return isSaleProp;
	}

	public void setIsSaleProp(Boolean isSaleProp){
		this.isSaleProp = isSaleProp;
	}

	@Column("IS_COLOR_PROP")
	public Boolean getIsColorProp(){
		return isColorProp;
	}

	public void setIsColorProp(Boolean isColorProp){
		this.isColorProp = isColorProp;
	}

	@Column("REQUIRED")
	public Boolean getRequired(){
		return required;
	}

	public void setRequired(Boolean required){
		this.required = required;
	}

	@Column("SEARCHABLE")
	public Boolean getSearchable(){
		return searchable;
	}

	public void setSearchable(Boolean searchable){
		this.searchable = searchable;
	}

	@Column("HASTHUMB")
	public Boolean getHasThumb(){
		return hasThumb;
	}

	public void setHasThumb(Boolean hasThumb){
		this.hasThumb = hasThumb;
	}

	@Column("IS_COMMON_INDUSTRY")
	public Boolean getIsCommonIndustry(){
		return isCommonIndustry;
	}

	public void setIsCommonIndustry(Boolean isSystem){
		this.isCommonIndustry = isSystem;
	}

	public LangProperty getGroupName(){
		return groupName;
	}

	public void setGroupName(LangProperty groupName){
		this.groupName = groupName;
	}

	@Column("COMMON_PROPERTY_ID")
	public Long getCommonPropertyId(){
		return commonPropertyId;
	}

	public void setCommonPropertyId(Long commonPropertyId){
		this.commonPropertyId = commonPropertyId;
	}

	public List<Industry> getIndustrylist(){
		return industrylist;
	}

	public void setIndustrylist(List<Industry> industrylist){
		this.industrylist = industrylist;
	}

	public String getCommonName(){
		return commonName;
	}

	public void setCommonName(String commonName){
		this.commonName = commonName;
	}

	public String getUsertype(){
		return usertype;
	}

	public void setUsertype(String usertype){
		this.usertype = usertype;
	}

}
