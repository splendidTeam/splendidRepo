package com.baozun.nebula.model.promotion;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_PRM_PROMOTIONMARKDOWNPRICE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionMarkdownPrice  extends BaseModel {
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = -2887664842134343450L;
	/** PK */
	private Long id;
	
	/**
	 * item,sku,disc_price,lifecycle
	 */
	/**
	 * 店铺
	 */
	private Long				shopId;
	
	/** 商品Id */
	private Long				itemId;

	/**
	 * sku Id
	 */
	private Long skuId ;
	
	/**
	 * 活动价格，一口价，限期内减价销售，结束后价格自动恢复
	 */
	private BigDecimal			markDownPrice;

	/** 生命周期：0没生效，1生效*/
	private Integer lifecycle;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONMARKDOWNPRICE",sequenceName = "S_T_PRM_PROMOTIONMARKDOWNPRICE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONMARKDOWNPRICE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "SHOP_ID")
    @Index(name = "IDX_PROMOTIONMARKDOWNPRICE_SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	@Column(name = "ITEM_ID")
    @Index(name = "IDX_PROMOTIONMARKDOWNPRICE_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	@Column(name = "SKU_ID")
    @Index(name = "IDX_PROMOTIONMARKDOWNPRICE_SKU_ID")
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	@Column(name = "MARKDOWN_PRICE")
	public BigDecimal getMarkDownPrice() {
		return markDownPrice;
	}

	public void setMarkDownPrice(BigDecimal markDownPrice) {
		this.markDownPrice = markDownPrice;
	}
	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_PROMOTIONMARKDOWNPRICE_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}
}
