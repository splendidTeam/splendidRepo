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
import java.util.List;
import java.util.Map;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * @author yue.ch
 *
 */
public class BundleItemViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 7084586879135978741L;
	
	/**
	 * 商品ID
	 */
	private Long itemId;
	
	/**
	 * 商品标题
	 */
	private String title;
	
	/**
	 * 商品图片
	 */
	private String imageUrl;
	
	/**
	 * 最小原价（注意：此处的listPrice并非吊牌价，而是这个普通商品所有参与捆绑商品的sku原来的销售价的最小值）
	 */
	private BigDecimal minListPrice;
	
	/**
	 * 最大原价（注意：此处的listPrice并非吊牌价，而是这个普通商品所有参与捆绑商品的sku原来的销售价的最大值）
	 */
	private BigDecimal maxListPrice;
	
	/**
	 * 商品中所有参与捆绑销售的sku设置的捆绑销售价的最小值
	 */
	private BigDecimal minSalesPrice;
	
	/**
	 * 商品中所有参与捆绑销售的sku设置的捆绑销售价的最大值
	 */
	private BigDecimal maxSalesPrice;
	
	/**
	 * 商品属性与属性值
	 */
	private Map<String, Object> properties;
	
	/**
	 * 商品中包含的sku
	 */
	private List<BundleSkuViewCommand> skuViewCommands;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
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

	public BigDecimal getMinListPrice() {
		return minListPrice;
	}

	public void setMinListPrice(BigDecimal minListPrice) {
		this.minListPrice = minListPrice;
	}

	public BigDecimal getMaxListPrice() {
		return maxListPrice;
	}

	public void setMaxListPrice(BigDecimal maxListPrice) {
		this.maxListPrice = maxListPrice;
	}

	public BigDecimal getMinSalesPrice() {
		return minSalesPrice;
	}

	public void setMinSalesPrice(BigDecimal minSalesPrice) {
		this.minSalesPrice = minSalesPrice;
	}

	public BigDecimal getMaxSalesPrice() {
		return maxSalesPrice;
	}

	public void setMaxSalesPrice(BigDecimal maxSalesPrice) {
		this.maxSalesPrice = maxSalesPrice;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public List<BundleSkuViewCommand> getSkuViewCommands() {
		return skuViewCommands;
	}

	public void setSkuViewCommands(List<BundleSkuViewCommand> skuViewCommands) {
		this.skuViewCommands = skuViewCommands;
	}
}
