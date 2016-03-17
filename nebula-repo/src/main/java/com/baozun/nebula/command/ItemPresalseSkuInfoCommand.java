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
package com.baozun.nebula.command;

import java.math.BigDecimal;

/**
 * 预售商品SKU价格库存信息Command
 * 
 * @author jinbao.ji
 * @date 2016年2月2日 下午5:46:41
 */
public class ItemPresalseSkuInfoCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Long				id;

	/** 商品ItemId */
	private Long				itemId;

	/**
	 * 商家编码
	 */
	private String				extentionCode;

	/**
	 * 销售属性(如白色+32G+联通版)
	 */
	private String				saleProperty;

	/**
	 * 吊牌价
	 */
	private BigDecimal			listPrice;

	/**
	 * 销售价
	 */
	private BigDecimal			salePrice;

	/**
	 * 当前库存
	 */
	private Integer				currentInventory;

	/**
	 * 全款
	 */
	private BigDecimal			fullMoney;

	/**
	 * 定金
	 */
	private BigDecimal			earnest;

	/**
	 * 尾款
	 */
	private BigDecimal			balance;

	/** 前端提交的库存增量数据 */
	private Integer				inventoryIncrement;

	public String getExtentionCode(){
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode){
		this.extentionCode = extentionCode;
	}

	public String getSaleProperty(){
		return saleProperty;
	}

	public void setSaleProperty(String saleProperty){
		this.saleProperty = saleProperty;
	}

	public BigDecimal getListPrice(){
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice){
		this.listPrice = listPrice;
	}

	public BigDecimal getSalePrice(){
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice){
		this.salePrice = salePrice;
	}

	public Integer getCurrentInventory(){
		return currentInventory;
	}

	public void setCurrentInventory(Integer currentInventory){
		this.currentInventory = currentInventory;
	}

	public BigDecimal getFullMoney(){
		return fullMoney;
	}

	public void setFullMoney(BigDecimal fullMoney){
		this.fullMoney = fullMoney;
	}

	public BigDecimal getEarnest(){
		return earnest;
	}

	public void setEarnest(BigDecimal earnest){
		this.earnest = earnest;
	}

	public BigDecimal getBalance(){
		return balance;
	}

	public void setBalance(BigDecimal balance){
		this.balance = balance;
	}

	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	public Integer getInventoryIncrement(){
		return inventoryIncrement;
	}

	public void setInventoryIncrement(Integer inventoryIncrement){
		this.inventoryIncrement = inventoryIncrement;
	}

	
	public Long getId(){
		return id;
	}

	
	public void setId(Long id){
		this.id = id;
	}

}
