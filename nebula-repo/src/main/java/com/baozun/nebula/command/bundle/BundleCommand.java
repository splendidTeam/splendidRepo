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
package com.baozun.nebula.command.bundle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.model.bundle.Bundle;
/**
 * 
  * @ClassName: BundleCommand
  * @author chainyu
  * @date 2016年4月15日 上午11:11:40
  *
 */
public class BundleCommand extends Bundle{
	private static final long serialVersionUID = -3173190425455269096L;
	
	/**
	 * 最小原销售价
	 */
	private BigDecimal minOriginalSalesPrice ;
	/**
	 * 最大原销售价
	 */
	private BigDecimal maxOriginalSalesPrice ;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minOriginalListPrice ;
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxOriginalListPrice ;
	/**
	 * 最小销售价
	 */
	private BigDecimal minSalesPrice ;
	/**
	 * 最大销售价
	 */
	private BigDecimal maxSalesPrice ;
	
	/**
	 * 
	 */
	private List<BundleElementCommand> bundleElementCommands = new ArrayList<BundleElementCommand>();
	
	public List<BundleElementCommand> getBundleElementCommands() {
		return bundleElementCommands;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
	}

	public void setBundleElementCommands(
			List<BundleElementCommand> bundleElementCommands) {
		this.bundleElementCommands = bundleElementCommands;
	}

	public void setMinSalesPrice(BigDecimal minSalesPrice) {
		this.minSalesPrice = minSalesPrice;
	}
	public void setMaxSalesPrice(BigDecimal maxSalesPrice) {
		this.maxSalesPrice = maxSalesPrice;
	}

	public void setMinOriginalListPrice(BigDecimal minOriginalListPrice) {
		this.minOriginalListPrice = minOriginalListPrice;
	}

	public void setMaxOriginalListPrice(BigDecimal maxOriginalListPrice) {
		this.maxOriginalListPrice = maxOriginalListPrice;
	}

	/**
     * <h3>检验bundle返回值的类型</h3>
     * <ul>
     *      <li>1 : 正常</li>
     *      <li>2 : bundle不存在</li>
     *      <li>3 : bundle中的某个商品不可售（包括：商品的下架 未上架 和 不存在）</li>
     *      <li>4 : bundle未上架</li>
     *      <li>5 : bundle下架</li>
     *      <li>6 : bundle库存不足</li>
     *      <li>7 : bundle中的某个sku库存不足</li>
     * </ul>
     * @Company  : BAOZUN
     * @author :  jiaolong.chen
     * @data : 2016年4月19日上午11:07:00
     */
	public enum BundleStatus{
		/**
		 * 正常
		 */
		BUNDLE_CAN_SALE(1),
		/**
		 * bundle不存在
		 */
		BUNDLE_NOT_EXIST(2),
		/**
		 * bundle中的某个商品不可售（包括：商品的下架 未上架 和 不存在）
		 */
		BUNDLE_ITEM_NOT_EXIST(3),
		/**
		 * bundle未上架
		 */
		BUNDLE_NOT_PUTAWAY(4),
		/**
		 * bundle下架
		 */
		BUNDLE_SOLD_OUT(5),
		/**
		 * bundle库存不足
		 */
		BUNDLE_NO_INVENTORY(6),
		/**
		 * bundle中的某个商品的sku库存不足
		 */
		BUNDLE_ITEM_NO_INVENTORY(7);
		
		private int status ;
		
		BundleStatus(int status){
			this.status = status;
		}
		
	    public int getStatus(){
	    	return status;
	    }
		
	}
	
	public BigDecimal getMinOriginalSalesPrice(){
	    for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMinOriginalSalesPrice());
			}
			minOriginalSalesPrice = minOriginalSalesPrice.add(itemSum);
		}
	    	
	    return minOriginalSalesPrice ;
	 }
	    
	public BigDecimal getMaxOriginalSalesPrice(){
		for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMaxOriginalSalesPrice());
			}
			maxOriginalSalesPrice = maxOriginalSalesPrice.add(itemSum);
		}
	    	
	    return maxOriginalSalesPrice ;
	  }
	    
	public BigDecimal getMinSalesPrice(){

		for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMinSalesPrice());
			}
			minSalesPrice = minSalesPrice.add(itemSum);
		}
	    	
	    return minSalesPrice ;
	   }
	    
	public BigDecimal getMaxSalesPrice(){
		for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMaxSalesPrice());
			}
			maxSalesPrice = maxSalesPrice.add(itemSum);
		}
	    	
	    return maxSalesPrice ;
	}
	
	public BigDecimal getMinOriginalListPrice() {
		return minOriginalListPrice;
	}
	
	public BigDecimal getMaxOriginalListPrice() {
		return maxOriginalListPrice;
	}
    
}
