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
package com.baozun.nebula.web.command.bundle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.command.product.ItemInfoCommand;
/**
 * 
  * @ClassName: BundleCommand
  * @author chainyu
  * @date 2016年4月15日 上午11:11:40
  *
 */
public class BundleCommand implements Serializable{
	private static final long serialVersionUID = -3173190425455269096L;
	
	/**
	 * 
	 */
	private Long bundleId ;
	/**
	 * 
	 */
	private Long itemId ;
	/**
	 * 商品编码
	 */
	private String code ;
	/**
	 * 商品生命周期
	 */
	private int lifeCycle;
	
	/**
	 * 商品名称
	 */
	private String				title;

	/**
	 * 副标题
	 */
	private String				subTitle;
	
	/** 商品详细描述 */
	private String				description;
	
	/**
	 * seo搜索描述
	 */
	private String				seoDescription;

	/**
	 * seo搜索关键字
	 */
	private String				seoKeywords;

	/**
	 * seoTitle
	 */
	private String				seoTitle;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice ;
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice ;
	/**
	 * 最小销售价
	 */
	private BigDecimal minSalesPrice ;
	/**
	 * 最大销售价
	 */
	private BigDecimal maxSalesPrice ;
	
	/**
	 * bundle商品的扩展信息
	 */
	private ItemInfoCommand itemInfoCommand ;
	
	/**
	 * 
	 */
	private List<BundleElementCommand> bundleElementCommands;
	
	public List<BundleElementCommand> getBundleElementCommands() {
		return bundleElementCommands;
	}

	public void setBundleElementCommands(
			List<BundleElementCommand> bundleElementCommands) {
		this.bundleElementCommands = bundleElementCommands;
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

	public Long getBundleId() {
		return bundleId;
	}

	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(int lifeCycle) {
		this.lifeCycle = lifeCycle;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public ItemInfoCommand getItemInfoCommand() {
		return itemInfoCommand;
	}

	public void setItemInfoCommand(ItemInfoCommand itemInfoCommand) {
		this.itemInfoCommand = itemInfoCommand;
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
    
}
