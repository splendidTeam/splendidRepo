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
package com.baozun.nebula.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品属性 每个属性都属于一个行业 有字段区分是否系统属性 系统属性表示此属性是某行业公共的属性 非系统属性表示此属性是某店铺自定义的
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_property")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Property extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID					= -7403558333855971007L;

	// 编辑类型 ：1 单行输入2可输入单选3单选4多选5自定义多选
	/** 1 单行输入 **/
	public final static Integer	EDITING_TYPE_INPUT					= 1;

	/** 2可输入单选 **/
	public final static Integer	EDITING_TYPE_CUSTOM_RADIO			= 2;

	/** 3单选 **/
	public final static Integer	EDITING_TYPE_RADIA					= 3;

	/** 4多选 **/
	public final static Integer	EDITING_TYPE_MULTI_SELECT			= 4;

	/** 5自定义多选 **/
	public final static Integer	EDITING_TYPE_CUSTOM_MULTI_SELECT	= 5;

	// 值类型 1 文本 2 数值 3日期 4日期时间
	/** 1 文本 **/
	public final static Integer	VALUE_TYPE_TEXT						= 1;

	/** 2 数值 **/
	public final static Integer	VALUE_TYPE_NUMBER					= 2;

	/** 3日期 **/
	public final static Integer	VALUE_TYPE_DATE						= 3;

	/** 4日期时间 **/
	public final static Integer	VALUE_TYPE_DATE_TIME				= 4;

	/** PK. */
	private Long				id;

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
	private String				groupName;

	private String				industryName;

	/**
	 * 所属属性分组行业
	 * 
	 * @deprecated 属性和行业的关联请使用{@link IndustryPropertyRelation }
	 */
	private Long				industryId;

	/**
	 * 行业属性表（扩展用）
	 * 
	 * @deprecated 骨架之后丢弃使用
	 */
	private Long				commonPropertyId;

	@Transient
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
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_PROPERTY",sequenceName = "S_T_PD_PROPERTY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_PROPERTY")
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
	@Column(name = "NAME",length = 100)
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
	@Column(name = "SORT_NO")
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
	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	@Column(name = "INDUSTRY_ID")
	public Long getIndustryId(){
		return industryId;
	}

	public void setIndustryId(Long industryId){
		this.industryId = industryId;
	}

	@Index(name = "IDX_COMMON_PROPERTY_ID")
	@Column(name = "COMMON_PROPERTY_ID")
	public Long getCommonPropertyId(){
		return commonPropertyId;
	}

	public void setCommonPropertyId(Long commonPropertyId){
		this.commonPropertyId = commonPropertyId;
	}

	@Column(name = "EDITING_TYPE")
	public Integer getEditingType(){
		return editingType;
	}

	public void setEditingType(Integer editingType){
		this.editingType = editingType;
	}

	@Column(name = "VALUE_TYPE")
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
	@Column(name = "CREATE_TIME")
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

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	@Column(name = "IS_SALEP_ROP")
	public Boolean getIsSaleProp(){
		return isSaleProp;
	}

	public void setIsSaleProp(Boolean isSaleProp){
		this.isSaleProp = isSaleProp;
	}

	@Column(name = "IS_COLOR_PROP")
	public Boolean getIsColorProp(){
		return isColorProp;
	}

	public void setIsColorProp(Boolean isColorProp){
		this.isColorProp = isColorProp;
	}

	@Column(name = "REQUIRED")
	public Boolean getRequired(){
		return required;
	}

	public void setRequired(Boolean required){
		this.required = required;
	}

	@Column(name = "SEARCHABLE")
	public Boolean getSearchable(){
		return searchable;
	}

	public void setSearchable(Boolean searchable){
		this.searchable = searchable;
	}

	@Column(name = "HASTHUMB")
	public Boolean getHasThumb(){
		return hasThumb;
	}

	public void setHasThumb(Boolean hasThumb){
		this.hasThumb = hasThumb;
	}

	@Column(name = "IS_COMMON_INDUSTRY")
	public Boolean getIsCommonIndustry(){
		return isCommonIndustry;
	}

	public void setIsCommonIndustry(Boolean isSystem){
		this.isCommonIndustry = isSystem;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

}
