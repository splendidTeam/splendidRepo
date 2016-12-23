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
package com.baozun.nebula.web.command;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yue.ch
 * @time 2016年6月13日 上午11:33:06
 */
public class BundleItemViewCommand implements Serializable,Comparable<BundleItemViewCommand> {

	private static final long serialVersionUID = 3701579978086490700L;
	
	private Long itemId;
	
	private String itemCode;
	
	private String title;
	
	private String picUrl;
	
	private BigDecimal salesPrice;
	
	private List<BundleSkuViewCommand> bundleSkuViewCommands;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public List<BundleSkuViewCommand> getBundleSkuViewCommands() {
		return bundleSkuViewCommands;
	}

	public void setBundleSkuViewCommands(List<BundleSkuViewCommand> bundleSkuViewCommands) {
		this.bundleSkuViewCommands = bundleSkuViewCommands;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public int compareTo(BundleItemViewCommand o) {
		return getItemId().intValue() - o.getItemId().intValue();
	}

}
