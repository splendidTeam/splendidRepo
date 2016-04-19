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
package com.baozun.nebula.web.command.bundle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class BundleItemCommand implements Serializable{

	private static final long serialVersionUID = -2130525552332744535L;
	
	/**
	 * 商品Id
	 */
	private long itemId;
	
	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 商品图片url
	 */
	private String imageUrl;
	
	/**
	 * 商品吊牌价
	 */
	private BigDecimal listPrice;
	
	/**
	 * 商品销售价
	 * <ul>
	 *    <li>按实际价格：item中的salesPrice</li>
	 *    <li>一口价：bundleElement中的salesPrice</li>
	 *    <li>定制价格：item中的salesPrice</li>
	 * </ul>
	 */
	private BigDecimal salesPrice;
	
	private List<BundleSkuCommand> bundleSkus;
	
	public List<BundleSkuCommand> getBundleSkus() {
		return bundleSkus;
	}

	public void setBundleSkus(List<BundleSkuCommand> bundleSkus) {
		this.bundleSkus = bundleSkus;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}


	
}
