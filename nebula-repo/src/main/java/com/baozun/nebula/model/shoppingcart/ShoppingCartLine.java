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
package com.baozun.nebula.model.shoppingcart;

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
 * 购物车行信息
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-26
 */
@Entity
@Table(name = "t_sc_shoppingcartline")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ShoppingCartLine extends BaseModel {


	private static final long serialVersionUID = 2250754313263734498L;
	
	/** PK. */
	private Long				id;

	/** 商品条形码sku */
	private String 				extentionCode;
	
	/** 商品id 其实是sku表中的id*/
	private Long 				skuId;
		
	/** 商品数量 */
	private Integer 			quantity;

	/** 会员id */
	private Long				memberId;
	
	/** 加入时间 */
	private Date 				createTime;
	
	/** 行分组*/
	private String              lineGroup;
	
	/** 促销号*/
	private Long              promotionId;
	
	/**
	 * 保存用户选择的赠品行，备选赠品行有引擎提供
	 * 有行合并时，需要标识购物车更新
	 */
	private boolean isGift;

	/**
	 * 结算状态 
	 * 0未选中结算
	 * 1选中结算
	 */
	private Integer 			settlementState;
	
	/** 店铺id **/
	private Long 				shopId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SC_SHOPPINGCARTLINE",sequenceName = "S_T_SC_SHOPPINGCARTLINE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SC_SHOPPINGCARTLINE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SKU_ID")
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	

	@Column(name = "EXTENTION_CODE",length=200)
	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}
	
	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "SETTLEMENT_STATE")
	public Integer getSettlementState() {
		return settlementState;
	}

	public void setSettlementState(Integer settlementState) {
		this.settlementState = settlementState;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "LINE_GROUP")
	public String getLineGroup() {
		return lineGroup;
	}

	public void setLineGroup(String lineGroup) {
		this.lineGroup = lineGroup;
	}

	@Column(name = "PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name = "IS_GIFT")
	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}
	
}
