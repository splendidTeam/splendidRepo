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

package com.baozun.nebula.command.promotion;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.baozun.nebula.command.Command;

public class PromotionCouponCodeCommand implements Command {

    private static final long serialVersionUID = 2899745531248094053L;

    /** PK */
    private Long id;

    private Long couponId;

    private Long shopId;

    /** 促销 */
    private String couponCode;

    /** 优惠券名 */
    private String couponName;

    /**
     * 金额：5元，10元，20元 折：百分比95'
     */
    private BigDecimal discount;

    /** 起始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 0未使用 1已使用 */
    private Integer isused = 0;

    /** 创建人 */
    private Long createId;

    private String createName;

    /** 创建时间 */
    private Date createTime;

    /** 1有效 0无效 */
    private Integer activeMark = 1;

    /** 1有效 0无效 */
    private Integer couponType = 1;// 常规的金额优惠券，0是折扣优惠券

    /** version. */
    private Date version;
    
    /**
     * 是否过期 0未过期 1已过期
     */
    private Integer isExpired=0;

    /**
     * 适用范围
     */
    private String scopeRange;

    /**
     * 使用次数
     */
    private Integer limitTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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

    public Integer getIsused() {
        return isused;
    }

    public void setIsused(Integer isused) {
        this.isused = isused;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getActiveMark() {
        return activeMark;
    }

    public void setActiveMark(Integer activeMark) {
        this.activeMark = activeMark;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(Integer limitTimes) {
        this.limitTimes = limitTimes;
    }

    public String getScopeRange() {
        return scopeRange;
    }

    public void setScopeRange(String scopeRange) {
        this.scopeRange = scopeRange;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    
    
}
