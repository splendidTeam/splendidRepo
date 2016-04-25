package com.baozun.nebula.command.bundle;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.model.bundle.BundleElement;

/**
 　  * 
  * @ClassName: BundleElementCommand
  * @author chainyu
  * @date 2016年4月15日 上午10:14:39
  * {@link com.baozun.nebula.model.bundle.BundleElement}
 */
public class BundleElementCommand extends BundleElement{

	private static final long serialVersionUID = -8462331832705645692L;
	
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
	 * element最小商品销售价
	 */
	private BigDecimal minSalesPrice = BigDecimal.ZERO;
	
	/**
	 * element最大商品销售价
	 */
	private BigDecimal maxSalesPrice = BigDecimal.ZERO;
	
	/**
	 * <ul>
	 * 	  <li>BundleItemCommand中需要包含商品的基本信息，以及一个ITEM参加bundle的所有sku</li>
	 * 	  <li>sku需要其一些基本信息，以及对应的属性Map<propertyId,propertyValueName></li>
	 * </ul>
	 */
	private List<BundleItemCommand> items = new ArrayList<BundleItemCommand>();

	public List<BundleItemCommand> getItems() {
		return items;
	}

	public void setItems(List<BundleItemCommand> items) {
		this.items = items;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
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
	
	/**
	 * 
	 * 查找element中item的最小原销售价格
	 */
	public BigDecimal getMinOriginalSalesPrice(){
		minOriginalSalesPrice = BigDecimal.ZERO;
	    for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				minOriginalSalesPrice = items.get(i).getMinOriginalSalesPrice();
			}else{
				if(minOriginalSalesPrice.compareTo(items.get(i).getMinOriginalSalesPrice()) == 1){
					minOriginalSalesPrice = items.get(i).getMinOriginalSalesPrice();
				}
			}
		}
	    return minOriginalSalesPrice ;
	 }
	 
	/**
	 * 
	 * 查找element中item的最大原销售价格
	 */
	public BigDecimal getMaxOriginalSalesPrice(){
		maxOriginalSalesPrice = BigDecimal.ZERO;
	    for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				maxOriginalSalesPrice = items.get(i).getMaxOriginalSalesPrice();
			}else{
				if(maxOriginalSalesPrice.compareTo(items.get(i).getMaxOriginalSalesPrice()) == -1){
					maxOriginalSalesPrice = items.get(i).getMaxOriginalSalesPrice();
				}
			}
		}
	    return maxOriginalSalesPrice ;
	  }
	 
	/**
	 * 
	 * 查找element中item的最小售价格
	 */
	public BigDecimal getMinSalesPrice(){
		minSalesPrice = BigDecimal.ZERO;
	    for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				minSalesPrice = items.get(i).getMinSalesPrice();
			}else{
				if(minSalesPrice.compareTo(items.get(i).getMinSalesPrice()) == 1){
					minSalesPrice = items.get(i).getMinSalesPrice();
				}
			}
		}
	    return minSalesPrice ;
	   }
	/**
	 * 
	 * 查找element中item的最大售价格
	 */    
	public BigDecimal getMaxSalesPrice(){
		maxSalesPrice = BigDecimal.ZERO;
	    for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				maxSalesPrice = items.get(i).getMaxSalesPrice();
			}else{
				if(maxSalesPrice.compareTo(items.get(i).getMaxSalesPrice()) == -1){
					maxSalesPrice = items.get(i).getMaxSalesPrice();
				}
			}
		}
	    return maxSalesPrice ;
	}
	/**
	 * 
	 * 查找element中item的最大吊牌价格
	 */
	public BigDecimal getMaxListPrice() {
		maxListPrice = BigDecimal.ZERO;
		for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				maxListPrice = items.get(i).getMaxListPrice();
			}else{
				if(maxListPrice.compareTo(items.get(i).getMaxListPrice()) == -1){
					maxListPrice = items.get(i).getMaxListPrice();
				}
			}
		}
		return maxListPrice;
	}
	/**
	 * 
	 * 查找element中item的最小吊牌价格
	 */
	public BigDecimal getMinListPrice() {
		minListPrice = BigDecimal.ZERO;
		for (int i = 0 ; i<items.size() ;i++) {
			if( i == 0 ){
				minListPrice = items.get(i).getMinListPrice();
			}else{
				if(minListPrice.compareTo(items.get(i).getMinListPrice()) == 1){
					minListPrice = items.get(i).getMinListPrice();
				}
			}
		}
		return minListPrice;
	}
	
}
