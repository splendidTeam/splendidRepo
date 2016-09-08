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

import java.math.BigDecimal;

/**
 * 
 * @author yufei.kong
 *
 */
public class ItemUpdatePriceCommand implements Command {

	private static final long serialVersionUID = -3134310251651746158L;

	/**
	 * 商品id
	 */
	private Long itemId;

	/**
	 * 商品编号
	 */
	private String code;

	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 款式
	 */
	private String style;

	/**
	 * 商品副标题
	 */
	private String subTitle;

	/**
	 * item销售价格
	 */
	private BigDecimal itemSalePrice;

	/**
	 * item吊牌价
	 */
	private BigDecimal itemListPrice;

	/**
	 * outID
	 */
	private String extentionCode;

	/**
	 * 销售价格
	 */
	private BigDecimal salePrice;

	/**
	 * 吊牌价
	 */
	private BigDecimal listPrice;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public BigDecimal getItemSalePrice() {
		return itemSalePrice;
	}

	public void setItemSalePrice(BigDecimal itemSalePrice) {
		this.itemSalePrice = itemSalePrice;
	}

	public BigDecimal getItemListPrice() {
		return itemListPrice;
	}

	public void setItemListPrice(BigDecimal itemListPrice) {
		this.itemListPrice = itemListPrice;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
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
	

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
