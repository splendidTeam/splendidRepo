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
package com.baozun.nebula.model.salesorder;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 订单行
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_orderline")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrderLine extends BaseModel{


	private static final long serialVersionUID = 2540443429165056436L;
	
	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long				orderId;
	
	/** UPC */
	private String				extentionCode;
	
	/** sku表中的id*/
	private Long 				skuId;
	
	/** 商品id */
	private Long				itemId;
	
	/** 商品数量 */
	private Integer				count;
	
	/** 原销售单价,吊牌价 */
	private BigDecimal			MSRP;
	
	/** 现销售单价 */
	private BigDecimal			salePrice;
	
	/** 
	 * 行小计
	 * 行小计=现销售单价X数量-折扣
	 */
	private BigDecimal			subtotal;
	
	/** 折扣-包含整单优惠分摊 */
	private BigDecimal			discount;
	
	/** 折扣单价-不包含整单优惠分摊 */
	private BigDecimal			discountPrice;
	
	/** 商品名称 */
	private String				itemName;
	
	/** 商品主图 */
	private String				itemPic;
	
	/** 销售属性信息 */
	private String				saleProperty;
	
	/** 行类型 */
	private Integer				type;
	
	/** 分组号 */
	private Integer				groupId;
	
	/** 评价状态 */
	private Integer				evaluationStatus;
	
	/** 商品快照版本 */
	private Integer				snapshot;
	
	/** version*/
	private Date				version;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_ORDERDETAIL",sequenceName = "S_T_SAL_ORDERDETAIL",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERDETAIL")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "EXTENTION_CODE", length = 200)
	public String getExtentionCode() {
		return extentionCode;
	}


	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}
	
	@Column(name = "SKU_ID")
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Column(name = "ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "MSRP")
	public BigDecimal getMSRP() {
		return MSRP;
	}


	public void setMSRP(BigDecimal mSRP) {
		MSRP = mSRP;
	}

	@Column(name = "SALE_PRICE")
	public BigDecimal getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Column(name = "SUBTOTAL")
	public BigDecimal getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	@Column(name = "DISCOUNT")
	public BigDecimal getDiscount() {
		return discount;
	}


	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(name = "ITEM_NAME", length = 100)
	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "ITEM_PIC", length = 200)
	public String getItemPic() {
		return itemPic;
	}


	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	@Column(name = "SALE_PROPERTY", length = 200)
	public String getSaleProperty() {
		return saleProperty;
	}


	public void setSaleProperty(String saleProperty) {
		this.saleProperty = saleProperty;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "GROUP_ID")
	public Integer getGroupId() {
		return groupId;
	}


	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "EVALUATION_STATUS")
	public Integer getEvaluationStatus() {
		return evaluationStatus;
	}


	public void setEvaluationStatus(Integer evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	@Column(name = "SNAPSHOT")
	public Integer getSnapshot() {
		return snapshot;
	}


	public void setSnapshot(Integer snapshot) {
		this.snapshot = snapshot;
	}

	@Column(name = "DISCOUNT_PRICE")
	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}



	


	
}
