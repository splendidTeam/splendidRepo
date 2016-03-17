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
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * 
 * 
 * @author chenguang.zhou
 * @date 2014年4月10日 下午4:31:16
 */
public class RecommandItemCommand implements Command {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7133051593904746734L;

	/** PK. */
	private Long				id;

	/**
	 * 描述
	 */
	private String				description;

	/**
	 * 推荐类型 1.公共类型 param属性表示是哪一种公共推荐 类型有 ： 1 购物车相关 2 pdp 3热门 等 2.分类推荐 param属性表示是分类id 3.商品搭配 param属性表示需要搭配的商品id，
	 * 
	 * 如id为1的分类推荐的商品列表： type=2,param=1,itemId=352 type=2,param=1,itemId=236 type=2,param=1,itemId=186
	 * 表示353,236,186是推荐的商品,但通过id为1分类进行定位
	 */
	private Integer				type;

	/**
	 * 推荐参数
	 */
	private Long				param;

	/**
	 * 推荐的商品id
	 */
	private Long				itemId;

	/**
	 * 排序
	 */
	private Integer				sort;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** 最后操作者 */
	private Long				opeartorId;

	/**
	 * 生命周期 (1:有效;0:无效)
	 */
	private Integer				lifecycle;

	// *****************************************************
	/**
	 * 推荐商品code
	 */
	private String				code;

	/**
	 * 主商品code
	 */
	private String				itemCode;

	/**
	 * 商品名称
	 */
	private String				title;

	/**
	 * 类型名称
	 */
	private String				typeName;

	/**
	 * 分类名称
	 */
	private String				categoryName;

	/**
	 * 分类code
	 */
	private String				categoryCode;
	/**
	 * 参数名称
	 */
	private String				paramName;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getParam() {
		return param;
	}

	public void setParam(Long param) {
		this.param = param;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
}
