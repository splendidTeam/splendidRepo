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
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;
import com.baozun.nebula.command.Command;


/**
 * @author dongliang
 *
 */
public class ItemSkuCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1623694857889068531L;
	
	/**
	 * 商品编码
	 */
	private String code;
	
	/**
	 * 商品名称
	 */
	private String title;
	

	/**
	 * 商品id
	 */
	private Long itemId;
	
	/**
	 * 商品最高价格
	 */
	private BigDecimal maxPrice;
	
	/**
	 * 商品最低价格
	 */
	private BigDecimal minPrice;
	
	/**
	 * 商品价格区间
	 */
	private String priceInterval;
	
	/** 商品可用量. */
	private Integer availableQty;


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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public String getPriceInterval() {
		return priceInterval;
	}

	public void setPriceInterval(String priceInterval) {
		this.priceInterval = priceInterval;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}
	
	
	
}
