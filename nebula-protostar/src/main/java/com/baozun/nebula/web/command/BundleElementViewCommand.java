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
 * @time 2016年6月13日 上午11:21:31
 */
public class BundleElementViewCommand implements Serializable, Comparable<BundleElementViewCommand>{

	private static final long serialVersionUID = -6481777444954106633L;
	
	private boolean isMainElement;
	
	private int sort;
	
	private String styleCode;
	
	private String itemCode;
	
	private BigDecimal salesPrice;
	
	private List<BundleItemViewCommand> bundleItemViewCommands;

	public boolean getIsMainElement() {
		return isMainElement;
	}

	public void setIsMainElement(boolean isMainElement) {
		this.isMainElement = isMainElement;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
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

	public List<BundleItemViewCommand> getBundleItemViewCommands() {
		return bundleItemViewCommands;
	}

	public void setBundleItemViewCommands(List<BundleItemViewCommand> bundleItemViewCommands) {
		this.bundleItemViewCommands = bundleItemViewCommands;
	}

	@Override
	public int compareTo(BundleElementViewCommand o) {
		return  getSort() - o.getSort();
	}
}
