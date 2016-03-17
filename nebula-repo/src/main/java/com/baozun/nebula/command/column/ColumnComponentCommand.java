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
 */
package com.baozun.nebula.command.column;

import java.util.Date;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Category;

/**
 * 
 * 
 * @author chenguang.zhou
 * @date 2014年4月4日 上午9:52:25
 */
public class ColumnComponentCommand implements Command,Comparable<ColumnComponentCommand> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4026892677920532460L;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 目标对象id(商品id,分类id)
	 */
	private Long				targetId;

	/**
	 * 标题
	 */
	private String				title;

	/**
	 * 链接url
	 */
	private String				url;

	/**
	 * 描述
	 */
	private String				description;
	/**
	 * 图片链接
	 */
	private String				img;

	private Integer				sortNo;

	/**
	 * 扩展字段，是一个map的json对象
	 */
	private String				ext;

	/**
	 * 模块id
	 */
	private Long				module_id;

	private Date				version;
	/**
	 * 图片宽度,img有值时才有效
	 */
	private Integer				imgWidth;
	
	/**
	 * 图片高度,img有值时才有效
	 */
	private Integer				imgHeight;
	// *******************************************************
	/** 模块Code */
	private String				moduleCode;
	/** 页面code */
	private String				pageCode;
	/** 商品 */
	private ItemCommand			itemCommand;
	/** 分类 */
	private Category			category;
	/** 类型 */
	private Integer				type;

	/** 发布时间 */
	private Date				publishTime;
	
	private Map<String, Object> extMap;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getModuleId() {
		return module_id;
	}

	public void setModuleId(Long moduleId) {
		this.module_id = moduleId;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public ItemCommand getItemCommand() {
		return itemCommand;
	}

	public void setItemCommand(ItemCommand itemCommand) {
		this.itemCommand = itemCommand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ColumnComponentCommand o) {
		if(o==null||o.getSortNo()==null){
			throw new IllegalArgumentException();
		}
		
		return this.sortNo.compareTo(o.getSortNo());
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Long getModule_id() {
		return module_id;
	}

	public void setModule_id(Long module_id) {
		this.module_id = module_id;
	}

	public Integer getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(Integer imgWidth) {
		this.imgWidth = imgWidth;
	}

	public Integer getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(Integer imgHeight) {
		this.imgHeight = imgHeight;
	}

	public Map<String, Object> getExtMap() {
		return extMap;
	}

	public void setExtMap(Map<String, Object> extMap) {
		this.extMap = extMap;
	}
}
