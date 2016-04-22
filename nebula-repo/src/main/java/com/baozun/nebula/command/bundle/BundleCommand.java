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
	private BigDecimal minOriginalSalesPrice = BigDecimal.ZERO ;
	/**
	 * 最大原销售价
	 */
	private BigDecimal maxOriginalSalesPrice = BigDecimal.ZERO ;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice = BigDecimal.ZERO ;
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice = BigDecimal.ZERO ;
	/**
	 * 最小销售价
	 */
	private BigDecimal minSalesPrice = BigDecimal.ZERO ;
	/**
	 * 最大销售价
	 */
	private BigDecimal maxSalesPrice = BigDecimal.ZERO ;
	
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

	public void setMinListPrice(BigDecimal minListPrice) {
		this.minListPrice = minListPrice;
	}

	public void setMaxListPrice(BigDecimal maxListPrice) {
		this.maxListPrice = maxListPrice;
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
	
	public BigDecimal getMinListPrice() {
		for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMinListPrice());
			}
			minListPrice = minListPrice.add(itemSum);
		}
		return minListPrice;
	}
	
	public BigDecimal getMaxListPrice() {
		for (BundleElementCommand element : bundleElementCommands) {
			List<BundleItemCommand> items = element.getItems();
			BigDecimal itemSum = BigDecimal.ZERO;
			for (BundleItemCommand item : items) {
				itemSum = itemSum.add(item.getMaxListPrice());
			}
			maxListPrice = maxListPrice.add(itemSum);
		}
		return maxListPrice;
	}
    
	/**
	 * <h3>校验bundle是否有效</h3>
	 * <p>校验的范围如下 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>捆绑销售 的商品是否有效 </li>
	 *   	<li>商品中的sku是否有效</li>
	 *   </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息</h3>
	 * @return 　返回结果 布尔类型
	 * <ul>
	 *   <li>true : 有效 </li>
	 *   <li>false : 失效</li>
	 * </ul>
	 */
	public boolean isEnabled(){
		Boolean removeFlag = Boolean.TRUE;
		
		for (BundleElementCommand bundleElementCommand : bundleElementCommands) {
			if(removeFlag){
				removeFlag = validateBundleElement(removeFlag , bundleElementCommand);
			}
		}
		return removeFlag;
	}
	
	

	/**
	 * <h3>验证bundle是否失效</h3>
	 * <p>失效的情景如下 ：</p>
	 * <ul>
	 *   <ol>
	 *   	<li>item 中的 lifecycle != 1 </li>
	 *   	<li>sku 中的 lifecycle != 1 </li>
	 *   </ol>
	 * </ul>
	 * @param removeFlag : 标识
	 * @param bundleElementCommand : 校验对象
	 * @return 　返回结果 布尔类型
	 * <ul>
	 *   <li>true : 失效 </li>
	 *   <li>false : 有效</li>
	 * </ul>
	 */
	private boolean validateBundleElement(Boolean removeFlag , BundleElementCommand bundleElementCommand){

		List<BundleItemCommand> bundleItem = bundleElementCommand.getItems();
		for (BundleItemCommand bundleItemCommand : bundleItem) {
			if(removeFlag){
				//item lifecycle == 1 上架
				if (bundleItemCommand.getLifecycle().intValue() != 1) {
					removeFlag = Boolean.FALSE;
					break;
				}
				//sku lifecycle == 1 上架
				List<BundleSkuCommand> skus = bundleItemCommand.getBundleSkus();
				for (BundleSkuCommand bundleSkuCommand : skus) {
					
					if(bundleSkuCommand.getLifeCycle().intValue() != 1){
						removeFlag = Boolean.FALSE;
						break;
					}
				}
			}
			
		}

		return removeFlag;
	}
}
