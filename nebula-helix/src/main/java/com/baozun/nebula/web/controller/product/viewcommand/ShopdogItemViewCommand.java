/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

public class ShopdogItemViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = -3799906776769234650L;

	/**
	 * 商品编码
	 */
	private String itemCode;
	
	
	/**
	 * 商品名称
	 */
	private String itemName;
	
	
	/**
	 * 销售属性
	 */
	private List<PropertyElementViewCommand> salesProperties;
	
	/**
	 * sku
	 */
	private List<SkuViewCommand> skus;
	
	/**
	 * 吊牌价
	 */
	private BigDecimal listPrice;
	
	/**
	 * 销售价
	 */
	private BigDecimal salesPrice;
	
	/**
	 * 主图
	 */
	private String mainPicUrl;
	
	/**
	 * 所有图片
	 */
	private List<String> picUrls;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<PropertyElementViewCommand> getSalesProperties() {
		return salesProperties;
	}

	public void setSalesProperties(List<PropertyElementViewCommand> salesProperties) {
		this.salesProperties = salesProperties;
	}

	public List<SkuViewCommand> getSkus() {
		return skus;
	}

	public void setSkus(List<SkuViewCommand> skus) {
		this.skus = skus;
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

	public String getMainPicUrl() {
		return mainPicUrl;
	}

	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}

	public List<String> getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(List<String> picUrls) {
		this.picUrls = picUrls;
	}
	
	

}
