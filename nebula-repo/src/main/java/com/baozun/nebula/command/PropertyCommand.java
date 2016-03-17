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
package com.baozun.nebula.command;

import java.util.Date;
import java.util.Map;

import com.baozun.nebula.model.product.PropertyValue;

/**
 * @author lin.liu
 */
public class PropertyCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2544112989421233359L;

	/** PK. */
	private Long				id;

	/**
	 * 所属属性分组行业
	 */
	private Long				industryId;
	
	/**
	 * 共通所属属性
	 */
	private Long				commonPropertyId;

	/** 名称 */
	private String				name;

	/** 顺序. */
	private Integer				sortNo;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/**
	 * 是否系统属性
	 */
	@Deprecated
	private Boolean				isSystem;
	
	/**
	 * 是否行业公共属性
	 */
	private Boolean				isCommonIndustry;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 编辑类型 ：1 单行输入2可输入单选3单选4多选
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
	private String				groupName;

	private String				industryName;
	
	private Map<String, PropertyValue>	valueMap;
	
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
	public String getName(){
		return name;
	}

	/**
	 * Sets the 分类名称.
	 * 
	 * @param name
	 *            the new 分类名称
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Gets the 顺序.
	 * 
	 * @return the 顺序
	 */
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
	public Date getVersion(){
		return version;
	}

	public Long getIndustryId(){
		return industryId;
	}

	public void setIndustryId(Long industryId){
		this.industryId = industryId;
	}
	
	public Long getCommonPropertyId(){
		return commonPropertyId;
	}
	
	public void setCommonPropertyId(Long commonPropertyId){
		this.commonPropertyId = commonPropertyId;
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

	public Date getModifyTime(){
		return modifyTime;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public Integer getLifecycle(){
		return lifecycle;
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

	public Boolean getRequired(){
		return required;
	}

	public void setRequired(Boolean required){
		this.required = required;
	}

	public Boolean getSearchable(){
		return searchable;
	}

	public void setSearchable(Boolean searchable){
		this.searchable = searchable;
	}

	public Boolean getHasThumb(){
		return hasThumb;
	}

	public void setHasThumb(Boolean hasThumb){
		this.hasThumb = hasThumb;
	}

	/**
	 * 使用isCommonIndustry 代替
	 * @return
	 */
	@Deprecated
	public Boolean getIsSystem(){
		return isSystem;
	}

	/**
	 * 使用isCommonIndustry 代替
	 * @return
	 */
	@Deprecated
	public void setIsSystem(Boolean isSystem){
		this.isSystem = isSystem;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public void setIsCommonIndustry(Boolean isCommonIndustry) {
		this.isCommonIndustry = isCommonIndustry;
	}

	public Boolean getIsCommonIndustry() {
		return isCommonIndustry;
	}

	public Map<String, PropertyValue> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, PropertyValue> valueMap) {
		this.valueMap = valueMap;
	}
}
