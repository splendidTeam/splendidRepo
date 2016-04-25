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
package com.baozun.nebula.web.controller.product.viewcommand;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;

/**
 * 捆绑类商品的商品视图模型
 * 
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
	 * 商品副标题
	 */
	private String subTitle;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice;
	
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice;
	
	/**
	 * 最小原价
	 */
	private BigDecimal minOriginalSalesPrice;
	
	/**
	 * 最大原价
	 */
	private BigDecimal maxOriginalSalesPrice;
	
	/**
	 * 商品中所有参与捆绑销售的sku设置的捆绑销售价的最小值
	 */
	private BigDecimal minSalesPrice;
	
	/**
	 * 商品中所有参与捆绑销售的sku设置的捆绑销售价的最大值
	 */
	private BigDecimal maxSalesPrice;
	
	/**
	 * 商品销售属性
	 */
	private List<PropertyElementViewCommand> salesProperties;
	
	/**
	 * 商品图片
	 */
	private List<ItemImageViewCommand> images;
	
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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
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

	public BigDecimal getMinOriginalSalesPrice() {
		return minOriginalSalesPrice;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public BigDecimal getMaxOriginalSalesPrice() {
		return maxOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
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

	public List<PropertyElementViewCommand> getSalesProperties() {
		return salesProperties;
	}

	public void setSalesProperties(List<PropertyElementViewCommand> salesProperties) {
		this.salesProperties = salesProperties;
	}

	public List<BundleSkuViewCommand> getSkuViewCommands() {
		return skuViewCommands;
	}

	public void setSkuViewCommands(List<BundleSkuViewCommand> skuViewCommands) {
		this.skuViewCommands = skuViewCommands;
	}

	public List<ItemImageViewCommand> getImages() {
		return images;
	}

	public void setImages(List<ItemImageViewCommand> images) {
		this.images = images;
	}

}
