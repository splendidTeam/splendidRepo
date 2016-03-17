package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

/**
 * 基于店铺的金额和商品数量统计
 * 
 * @author 阳羽
 * @createtime 2014-4-1 下午01:15:08
 */
public class ShopCartCommandByShop  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5235640413320319863L;

	// 订单优惠
	private BigDecimal disAmtOnOrder = BigDecimal.ZERO;

	// 整单优惠
	private BigDecimal disAmtSingleOrder = BigDecimal.ZERO;
	// 应付小计
	private BigDecimal subtotalCurrentPayAmount = BigDecimal.ZERO;

	// 商品数量
	private Integer qty;

	// 应付运费
	private BigDecimal originShoppingFee = BigDecimal.ZERO;

	// 应付合计
	private BigDecimal sumCurrentPayAmount;

	// 优惠合计
	private BigDecimal offersTotal = BigDecimal.ZERO;

	// 实付合计
	private BigDecimal realPayAmount;

	// 运费优惠
	private BigDecimal offersShipping = BigDecimal.ZERO;

	private Long shopId;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getOffersShipping() {
		return offersShipping;
	}

	public void setOffersShipping(BigDecimal offersShipping) {
		this.offersShipping = offersShipping;
	}

	public BigDecimal getDisAmtOnOrder() {
		return disAmtOnOrder;
	}

	public void setDisAmtOnOrder(BigDecimal disAmtOnOrder) {
		this.disAmtOnOrder = disAmtOnOrder;
	}

	public BigDecimal getDisAmtSingleOrder() {
		return disAmtSingleOrder;
	}

	public void setDisAmtSingleOrder(BigDecimal disAmtSingleOrder) {
		this.disAmtSingleOrder = disAmtSingleOrder;
	}

	public BigDecimal getSubtotalCurrentPayAmount() {
		return subtotalCurrentPayAmount;
	}

	public void setSubtotalCurrentPayAmount(BigDecimal subtotalCurrentPayAmount) {
		this.subtotalCurrentPayAmount = subtotalCurrentPayAmount;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getOriginShoppingFee() {
		return originShoppingFee;
	}

	public void setOriginShoppingFee(BigDecimal originShoppingFee) {
		this.originShoppingFee = originShoppingFee;
	}

	public BigDecimal getSumCurrentPayAmount() {
		return sumCurrentPayAmount;
	}

	public void setSumCurrentPayAmount(BigDecimal sumCurrentPayAmount) {
		this.sumCurrentPayAmount = sumCurrentPayAmount;
	}

	public BigDecimal getOffersTotal() {
		return offersTotal;
	}

	public void setOffersTotal(BigDecimal offersTotal) {
		this.offersTotal = offersTotal;
	}

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}
}
