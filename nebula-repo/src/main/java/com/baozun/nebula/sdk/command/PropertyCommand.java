/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.command;

import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;

import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 * 
 */
public class PropertyCommand extends BaseModel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 4617176479433995219L;
	/** PK. */
	private Long							id;

	/**
	 * 所属属性分组行业
	 */
	private Long							industryId;

	/** 名称 */
	private String							name;

	/** 顺序. */
	private Integer							sortNo;

	/** 创建时间. */
	private Date							createTime;

	/** 修改时间 */
	private Date							modifyTime;

	/** version. */
	private Date							version;

	/**
	 * 是否系统属性
	 */
	@Deprecated
	private Boolean							isSystem;
	
	/**
	 * 是否行业公共属性
	 */
	private Boolean				isCommonIndustry;

	/**
	 * 生命周期
	 */
	private Integer							lifecycle;

	/**
	 * 编辑类型 ：1 单行输入2可输入单选3单选4多选
	 */
	private Integer							editingType;

	/**
	 * 值类型 1 文本 2 数值 3日期 4日期时间
	 */
	private Integer							valueType;

	/**
	 * 是否销售属性
	 */
	private Boolean							isSaleProp;

	/**
	 * 是否颜色属性
	 */
	private Boolean							isColorProp;

	/**
	 * 是否必输
	 */
	private Boolean							required;

	/**
	 * 是否用于搜索
	 */
	private Boolean							searchable;

	/**
	 * 是否配图
	 */
	private Boolean							hasThumb;

	/**
	 * 属性分组
	 */
	private String							groupName;

	private String							industryName;

	private Map<Long, PropertyValueCommand>	valueMap;

	@Transient
	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the industryId
	 */
	public Long getIndustryId() {
		return industryId;
	}

	/**
	 * @param industryId
	 *            the industryId to set
	 */
	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sortNo
	 */
	public Integer getSortNo() {
		return sortNo;
	}

	/**
	 * @param sortNo
	 *            the sortNo to set
	 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the version
	 */
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**使用isCommonIndustry 代替
	 * @return the isSystem
	 */
	@Deprecated
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**使用isCommonIndustry 代替
	 * @param isSystem
	 *            the isSystem to set
	 */
	@Deprecated
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	/**
	 * @return the lifecycle
	 */
	public Integer getLifecycle() {
		return lifecycle;
	}

	/**
	 * @param lifecycle
	 *            the lifecycle to set
	 */
	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	/**
	 * @return the editingType
	 */
	public Integer getEditingType() {
		return editingType;
	}

	/**
	 * @param editingType
	 *            the editingType to set
	 */
	public void setEditingType(Integer editingType) {
		this.editingType = editingType;
	}

	/**
	 * @return the valueType
	 */
	public Integer getValueType() {
		return valueType;
	}

	/**
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the isSaleProp
	 */
	public Boolean getIsSaleProp() {
		return isSaleProp;
	}

	/**
	 * @param isSaleProp
	 *            the isSaleProp to set
	 */
	public void setIsSaleProp(Boolean isSaleProp) {
		this.isSaleProp = isSaleProp;
	}

	/**
	 * @return the isColorProp
	 */
	public Boolean getIsColorProp() {
		return isColorProp;
	}

	/**
	 * @param isColorProp
	 *            the isColorProp to set
	 */
	public void setIsColorProp(Boolean isColorProp) {
		this.isColorProp = isColorProp;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the searchable
	 */
	public Boolean getSearchable() {
		return searchable;
	}

	/**
	 * @param searchable
	 *            the searchable to set
	 */
	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * @return the hasThumb
	 */
	public Boolean getHasThumb() {
		return hasThumb;
	}

	/**
	 * @param hasThumb
	 *            the hasThumb to set
	 */
	public void setHasThumb(Boolean hasThumb) {
		this.hasThumb = hasThumb;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setValueMap(Map<Long, PropertyValueCommand> valueMap) {
		this.valueMap = valueMap;
	}

	public Map<Long, PropertyValueCommand> getValueMap() {
		return valueMap;
	}

	public void setIsCommonIndustry(Boolean isCommonIndustry) {
		this.isCommonIndustry = isCommonIndustry;
	}

	public Boolean getIsCommonIndustry() {
		return isCommonIndustry;
	}

}
