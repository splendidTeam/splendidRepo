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
	 * 生命周期
	 */
	private Integer lifecycle;
	
	/**
	 * 最小商品原销售价
	 */
	private BigDecimal minOriginalSalesPrice = BigDecimal.ZERO;
	
	/**
	 * 最大商品原销售价
	 */
	private BigDecimal maxOriginalSalesPrice = BigDecimal.ZERO;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice = BigDecimal.ZERO ;
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice = BigDecimal.ZERO ;
	
	/**
	 * bundle最小商品销售价
	 */
	private BigDecimal minSalesPrice = BigDecimal.ZERO;
	
	/**
	 * bundle最大商品销售价
	 */
	private BigDecimal maxSalesPrice = BigDecimal.ZERO;
	
	private List<BundleSkuCommand> bundleSkus = new ArrayList<BundleSkuCommand>();
	
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public void setMinListPrice(BigDecimal minListPrice) {
		this.minListPrice = minListPrice;
	}

	public void setMaxListPrice(BigDecimal maxListPrice) {
		this.maxListPrice = maxListPrice;
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
		minOriginalSalesPrice = BigDecimal.ZERO;
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
		maxOriginalSalesPrice = BigDecimal.ZERO;
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
		minSalesPrice = BigDecimal.ZERO;
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
		maxSalesPrice = BigDecimal.ZERO;
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
	public BigDecimal getMaxListPrice() {
		maxListPrice = BigDecimal.ZERO;
		for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				maxListPrice = bundleSkus.get(i).getListPrice();
			}else{
				if(maxListPrice.compareTo(bundleSkus.get(i).getListPrice()) == -1){
					maxListPrice = bundleSkus.get(i).getListPrice();
				}
			}
		}
		return maxListPrice;
	}
	/**
	 * 
	 * 查找商品中sku的最小吊牌价格
	 */
	public BigDecimal getMinListPrice() {
		minListPrice = BigDecimal.ZERO;
		for (int i = 0 ; i<bundleSkus.size() ;i++) {
			if( i == 0 ){
				minListPrice = bundleSkus.get(i).getListPrice();
			}else{
				if(minListPrice.compareTo(bundleSkus.get(i).getListPrice()) == 1){
					minListPrice = bundleSkus.get(i).getListPrice();
				}
			}
		}
		return minListPrice;
	}
}
