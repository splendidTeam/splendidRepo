/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.web.controller.bundle.viewcommand;

import java.math.BigDecimal;
import java.util.Map;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * @author yue.ch
 *
 */
public class BundleSkuViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -6910477949202581700L;
	
	/**
	 * sku id
	 */
	private Long skuId;
	
	/**
	 * sku吊牌价
	 */
	private BigDecimal listPrice;
	
	/**
	 * sku销售价
	 */
	private BigDecimal originalSalesPrice;
	
	/**
	 * sku在捆绑类商品中的实际销售价
	 */
	private BigDecimal salesPrice;
	
	/**
	 * sku可用库存
	 */
	private Integer quantity;
	
	/**
	 * sku的属性与属性值<br/><br/>
	 * 
	 * key:propertyId<br/>
	 * value:propertyValueName<br/>
	 * {12:"红色"}
	 */
	private Map<Long, Object> properties;
	
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getOriginalSalesPrice() {
		return originalSalesPrice;
	}

	public void setOriginalSalesPrice(BigDecimal originalSalesPrice) {
		this.originalSalesPrice = originalSalesPrice;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Map<Long, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<Long, Object> properties) {
		this.properties = properties;
	}

}
