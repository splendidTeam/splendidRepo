package com.baozun.nebula.command.promotion;

import java.math.BigDecimal;
import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * 
 * @author lihao
 * 
 */
public class PromotionCouponInfoCommand implements Command {

	private static final long serialVersionUID = 298096170491175813L;

	/**
	 * 优惠券ID
	 */
	private Long couponId;
	/**
	 * 优惠券编码
	 */
	private String couponCode;
	/**
	 * 起始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 0未使用 1已使用
	 */
	private Integer isUsed;
	/**
	 * 店铺ID
	 */
	private Long shopId;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 优惠券名
	 */
	private String couponName;
	/**
	 * 金额：5元，10元，20元 折：百分比95
	 */
	private BigDecimal discount;
	/**
	 * 1金额 2折扣
	 */
	private Integer type;

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
