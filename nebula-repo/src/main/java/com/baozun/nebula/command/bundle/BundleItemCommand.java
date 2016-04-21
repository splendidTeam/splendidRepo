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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class BundleItemCommand implements Serializable{

	private static final long serialVersionUID = -2130525552332744535L;
	
	/**
	 * 商品Id
	 */
	private long itemId;
	
	/**
	 * 最小商品原销售价
	 */
	private BigDecimal minOriginalSalesPrice;
	
	/**
	 * 最大商品原销售价
	 */
	private BigDecimal maxOriginalSalesPrice;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minOriginalListPrice ;
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxOriginalListPrice ;
	
	/**
	 * bundle最小商品销售价
	 */
	private BigDecimal minSalesPrice;
	
	/**
	 * bundle最大商品销售价
	 */
	private BigDecimal maxSalesPrice;
	
	private List<BundleSkuCommand> bundleSkus = new ArrayList<BundleSkuCommand>();
	
	public void setMinOriginalListPrice(BigDecimal minOriginalListPrice) {
		this.minOriginalListPrice = minOriginalListPrice;
	}

	public void setMaxOriginalListPrice(BigDecimal maxOriginalListPrice) {
		this.maxOriginalListPrice = maxOriginalListPrice;
	}

	public void setMinSalesPrice(BigDecimal minSalesPrice) {
		this.minSalesPrice = minSalesPrice;
	}

	public void setMaxSalesPrice(BigDecimal maxSalesPrice) {
		this.maxSalesPrice = maxSalesPrice;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
	}

	public List<BundleSkuCommand> getBundleSkus() {
		return bundleSkus;
	}

	public void setBundleSkus(List<BundleSkuCommand> bundleSkus) {
		this.bundleSkus = bundleSkus;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	/**
	 * 
	 * 查找商品中sku的最小原销售价格
	 */
	public BigDecimal getMinOriginalSalesPrice(){
	    for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				minOriginalSalesPrice = bundleSkus.get(i).getOriginalSalesPrice();
			}else{
				if(minOriginalSalesPrice.compareTo(bundleSkus.get(i).getOriginalSalesPrice()) == 1){
					minOriginalSalesPrice = bundleSkus.get(i).getOriginalSalesPrice();
				}
			}
		}
	    return minOriginalSalesPrice ;
	 }
	 
	/**
	 * 
	 * 查找商品中sku的最大原销售价格
	 */
	public BigDecimal getMaxOriginalSalesPrice(){
	    for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				maxOriginalSalesPrice = bundleSkus.get(i).getOriginalSalesPrice();
			}else{
				if(maxOriginalSalesPrice.compareTo(bundleSkus.get(i).getOriginalSalesPrice()) == -1){
					maxOriginalSalesPrice = bundleSkus.get(i).getOriginalSalesPrice();
				}
			}
		}
	    return maxOriginalSalesPrice ;
	  }
	 
	/**
	 * 
	 * 查找bundle商品中sku的最小售价格
	 */
	public BigDecimal getMinSalesPrice(){
	    for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				minSalesPrice = bundleSkus.get(i).getSalesPrice();
			}else{
				if(minSalesPrice.compareTo(bundleSkus.get(i).getSalesPrice()) == 1){
					minSalesPrice = bundleSkus.get(i).getSalesPrice();
				}
			}
		}
	    return minSalesPrice ;
	   }
	/**
	 * 
	 * 查找bundle商品中sku的最大售价格
	 */    
	public BigDecimal getMaxSalesPrice(){
	    for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				maxSalesPrice = bundleSkus.get(i).getSalesPrice();
			}else{
				if(maxSalesPrice.compareTo(bundleSkus.get(i).getSalesPrice()) == -1){
					maxSalesPrice = bundleSkus.get(i).getSalesPrice();
				}
			}
		}
	    return maxSalesPrice ;
	}
	/**
	 * 
	 * 查找商品中sku的最大吊牌价格
	 */
	public BigDecimal getMaxOriginalListPrice() {
		for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				maxOriginalListPrice = bundleSkus.get(i).getOriginalListPrice();
			}else{
				if(maxOriginalListPrice.compareTo(bundleSkus.get(i).getOriginalListPrice()) == -1){
					maxOriginalListPrice = bundleSkus.get(i).getOriginalListPrice();
				}
			}
		}
		return maxOriginalListPrice;
	}
	/**
	 * 
	 * 查找商品中sku的最小吊牌价格
	 */
	public BigDecimal getMinOriginalListPrice() {
		for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				minOriginalListPrice = bundleSkus.get(i).getOriginalListPrice();
			}else{
				if(minOriginalListPrice.compareTo(bundleSkus.get(i).getOriginalListPrice()) == 1){
					minOriginalListPrice = bundleSkus.get(i).getOriginalListPrice();
				}
			}
		}
		return minOriginalListPrice;
	}
}
