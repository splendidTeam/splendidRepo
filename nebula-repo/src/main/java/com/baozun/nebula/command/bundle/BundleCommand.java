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
import java.util.Iterator;
import java.util.List;

import com.baozun.nebula.model.bundle.Bundle;

import com.baozun.nebula.command.bundle.BundleElementCommand;
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
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最小原销售价的累加</li>
	 */
	public BigDecimal getMinOriginalSalesPrice(){
		minOriginalSalesPrice = BigDecimal.ZERO;
	    for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			minOriginalSalesPrice = minOriginalSalesPrice.add(elementCommand.getMinOriginalSalesPrice());
		}
	    	
	    return minOriginalSalesPrice ;
	 }
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最大原销售价的累加</li>
	 */   
	public BigDecimal getMaxOriginalSalesPrice(){
		maxOriginalSalesPrice = BigDecimal.ZERO;
		for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			maxOriginalSalesPrice = maxOriginalSalesPrice.add(elementCommand.getMaxOriginalSalesPrice());
		}
	    	
	    return maxOriginalSalesPrice ;
	  }
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最小销售价的累加</li>
	 */      
	public BigDecimal getMinSalesPrice(){
		minSalesPrice = BigDecimal.ZERO;
		for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			minSalesPrice = minSalesPrice.add(elementCommand.getMinSalesPrice());
		}
	    	
	    return minSalesPrice ;
	   }
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最大销售价的累加</li>
	 */       
	public BigDecimal getMaxSalesPrice(){
		maxSalesPrice = BigDecimal.ZERO;
		for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			maxSalesPrice = maxSalesPrice.add(elementCommand.getMaxSalesPrice());
		}
	    	
	    return maxSalesPrice ;
	}
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最小吊牌价的累加</li>
	 */   
	public BigDecimal getMinListPrice() {
		minListPrice = BigDecimal.ZERO ;
		for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			minListPrice = minListPrice.add(elementCommand.getMinListPrice());
		}
		return minListPrice;
	}
	/**
	 * <ul>
	 * 		<li>每个bundle中可能包含多个element</li>
	 * 		<li>每个element中可能包含多个item</li>
	 * </ul>
	 * @return 返回值<li>每个element中item最大吊牌价的累加</li>
	 */   
	public BigDecimal getMaxListPrice() {
		maxListPrice = BigDecimal.ZERO;
		for (BundleElementCommand elementCommand : bundleElementCommands) {
			
			maxListPrice = maxListPrice.add(elementCommand.getMaxListPrice());
		}
		return maxListPrice;
	}
    
	/**
	 * <h3>校验bundle是否有效</h3>
	 * <p>校验的范围如下 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>最少有一个element中的商品都失效了 ,bundle就失效</li>
	 *   </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息 以及 bundle本身是否上架</h3>
	 * @return 　返回结果 布尔类型
	 * <ul>
	 *   <li>true : 有效 </li>
	 *   <li>false : 失效</li>
	 * </ul>
	 */
	public boolean isEnabled(){
		Boolean removeFlag = Boolean.TRUE;
		
		for (BundleElementCommand bundleElementCommand : bundleElementCommands) {
			removeFlag = validateBundleElement( bundleElementCommand);
			if(!removeFlag){
				break;
			}
		}
		return removeFlag;
	}
	
	/**
	 * <p>校验的步骤 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>踢掉所有不是上架状态的商品</li>
	 *      <li>踢掉商品中所有不是上架状态的sku</li>
	 *      <li>如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉</li>
	 *   </ol>
	 * </ul>
	 * @param bundleElementCommand
	 * @return
	 */
	private boolean validateBundleElement(BundleElementCommand bundleElementCommand){

		List<BundleItemCommand> bundleItem = bundleElementCommand.getItems();
		
		Iterator<BundleItemCommand> iterator = bundleItem.iterator();
		while (iterator.hasNext()) {
			BundleItemCommand bundleItemCommand = iterator.next();
			//1 踢掉所有不是上架状态的商品
			if (bundleItemCommand.getLifecycle().intValue() != 1) {
				iterator.remove();
				continue;
			}
			//2 踢掉商品中所有不是上架状态的sku
			List<BundleSkuCommand> skus = bundleItemCommand.getBundleSkus();
			Iterator<BundleSkuCommand> iterator2 = skus.iterator();
			while (iterator2.hasNext()) {
				BundleSkuCommand bundleSkuCommand = (BundleSkuCommand) iterator2.next();
				if(bundleSkuCommand.getLifeCycle().intValue() != 1){
					iterator2.remove();
					continue;
				}
			}
			//3 如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉
			if(bundleItemCommand.getBundleSkus() == null || bundleItemCommand.getBundleSkus().size() == 0){
				iterator.remove();
				continue;
			}
		}
		
		if(bundleElementCommand.getItems() == null || bundleElementCommand.getItems().size() == 0){
			return false;
		}
		
		return true;
	}
}
