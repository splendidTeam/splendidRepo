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
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

/**
 * SkuCommand
 * @author chenguang.zhou
 * @date 2014-3-12 下午02:20:34
 **/
public class SkuCommand implements Command {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -669062912005060008L;

	/**
	 * skuId
	 */
	private Long				id;
	
	/**
	 * 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：   现在更改为([商品属性值id];)*([商品属性值id])  
	 * 这里的商品属性值id为ItemProperties实体的id
	 */
	private String				properties;

	/**
	 * 销售价
	 */
	private BigDecimal			salePrice;

	/**
	 * 1 商品已上架 0 商品未上架
	 */
	private String				state;

	/**
	 * 吊牌价(原单价)
	 */
	private BigDecimal			listPrice;

	/**
	 * 和oms 沟通交互的 唯一编码,extension1.
	 */
	private String				extentionCode;

	/**
	 * 商品可用量.
	 */
	private Integer				availableQty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}
}
