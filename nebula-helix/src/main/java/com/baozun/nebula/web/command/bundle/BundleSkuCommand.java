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
import java.util.Map;


public class BundleSkuCommand implements Serializable{

	private static final long serialVersionUID = -3663016675036644108L;

	private long skuId;
	
	/**
	 * sku吊牌价
	 * 
	 */
	private BigDecimal listPrice;
	
	/**
	 * sku销售价
	 *<ul>
	 *    <li>按实际价格：sku中的salesPrice</li>
	 *    <li>一口价：bundleElement中的salesPrice</li>
	 *    <li>定制价格：sku中的salesPrice</li>
	 * </ul>
	 */
	private BigDecimal salesPrice;
	
	/**
	 * sku库存
	 */
	private long quantity;
	
	/**
	 * 商品属性-属性值关系
	 * key:propertyId
	 * value:propertyValueName
	 * {12:"红色"}
	 */
	private Map<Long,String> properties;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
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

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public Map<Long, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<Long, String> properties) {
		this.properties = properties;
	}
	
}
