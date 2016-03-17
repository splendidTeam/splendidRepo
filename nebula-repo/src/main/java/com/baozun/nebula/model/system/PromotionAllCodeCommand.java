/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.system;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: CouponCodeCommand
 * @Description:(这里用一句话描述这个类的作用)
 * @author GEWEI.LU
 * @date 2016年1月18日 下午12:53:17
 */
public class PromotionAllCodeCommand{

	/** 对应的活动名字 */
	private String	couponName;

	/** codeid */

	private Long	codeid;

	public void setCodeid(Long codeid){
		this.codeid = codeid;
	}

	/** PK */
	private Long		id;

	/** 促销 */
	private String		couponCode;

	/** 优惠券id */
	private Long		couponId;

	/** 店铺id */
	private Long		shopId;

	/** 起始时间 */
	private Date		startTime;

	/** 结束时间 */
	private Date		endTime;

	/** 创建人 */
	private Long		createId;

	/** 0未使用 1已使用 */
	private Integer		isused		= 0;

	/** 创建时间 */
	private Date		createTime;

	/** 1有效 0无效 */
	private Integer		activeMark	= 1;

	/** version. */
	private Date		version;

	/**
	 * 使用次数
	 */
	private Integer		limitTimes;

	/** 1是常规的金额优惠券，0是折扣优惠券 */
	private Integer		couponType	= 1;

	/**
	 * couponType为1时：金额：5元，10元，20元 couponType为0时：折扣 百分比95
	 */
	private BigDecimal	couponDiscount;

	public PromotionAllCodeCommand(String couponName, Long codeid, Long id, String couponCode, Long couponId, Long shopId, Date startTime,
			Date endTime, Long createId, Integer isused, Date createTime, Integer activeMark, Date version, Integer limitTimes,
			Integer couponType, BigDecimal couponDiscount){
		super();
		this.couponName = couponName;
		this.codeid = codeid;
		this.id = id;
		this.couponCode = couponCode;
		this.couponId = couponId;
		this.shopId = shopId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createId = createId;
		this.isused = isused;
		this.createTime = createTime;
		this.activeMark = activeMark;
		this.version = version;
		this.limitTimes = limitTimes;
		this.couponType = couponType;
		this.couponDiscount = couponDiscount;
	}

	public PromotionAllCodeCommand(String couponName, Long codeid, Long id, String couponCode, Long couponId, Long shopId, Date startTime,
			Date endTime, Long createId, Integer isused, Date createTime, Integer activeMark, Date version, Integer limitTimes){
		super();
		this.couponName = couponName;
		this.codeid = codeid;
		this.id = id;
		this.couponCode = couponCode;
		this.couponId = couponId;
		this.shopId = shopId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createId = createId;
		this.isused = isused;
		this.createTime = createTime;
		this.activeMark = activeMark;
		this.version = version;
		this.limitTimes = limitTimes;
	}

	/**
	 * @return couponName
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public String getCouponName(){
		return couponName;
	}

	/**
	 * @param couponName
	 *            要设置的 couponName
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setCouponName(String couponName){
		this.couponName = couponName;
	}

	/**
	 * @return id
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Long getId(){
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * @return couponCode
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public String getCouponCode(){
		return couponCode;
	}

	/**
	 * @param couponCode
	 *            要设置的 couponCode
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setCouponCode(String couponCode){
		this.couponCode = couponCode;
	}

	/**
	 * @return couponId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Long getCouponId(){
		return couponId;
	}

	/**
	 * @param couponId
	 *            要设置的 couponId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setCouponId(Long couponId){
		this.couponId = couponId;
	}

	/**
	 * @return shopId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Long getShopId(){
		return shopId;
	}

	/**
	 * @param shopId
	 *            要设置的 shopId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setShopId(Long shopId){
		this.shopId = shopId;
	}

	/**
	 * @return startTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Date getStartTime(){
		return startTime;
	}

	/**
	 * @param startTime
	 *            要设置的 startTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}

	/**
	 * @return endTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Date getEndTime(){
		return endTime;
	}

	/**
	 * @param endTime
	 *            要设置的 endTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}

	/**
	 * @return createId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Long getCreateId(){
		return createId;
	}

	/**
	 * @param createId
	 *            要设置的 createId
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setCreateId(Long createId){
		this.createId = createId;
	}

	/**
	 * @return isused
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Integer getIsused(){
		return isused;
	}

	/**
	 * @param isused
	 *            要设置的 isused
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setIsused(Integer isused){
		this.isused = isused;
	}

	/**
	 * @return createTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * @param createTime
	 *            要设置的 createTime
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	/**
	 * @return activeMark
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Integer getActiveMark(){
		return activeMark;
	}

	/**
	 * @param activeMark
	 *            要设置的 activeMark
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setActiveMark(Integer activeMark){
		this.activeMark = activeMark;
	}

	/**
	 * @return version
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Date getVersion(){
		return version;
	}

	/**
	 * @param version
	 *            要设置的 version
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setVersion(Date version){
		this.version = version;
	}

	/**
	 * @return limitTimes
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public Integer getLimitTimes(){
		return limitTimes;
	}

	/**
	 * @param limitTimes
	 *            要设置的 limitTimes
	 * @date 2016年1月18日 下午12:53:53
	 * @author GEWEI.LU
	 */
	public void setLimitTimes(Integer limitTimes){
		this.limitTimes = limitTimes;
	}

	/**
	 * @return codeid
	 * @date 2016年1月20日 上午11:30:30
	 * @author GEWEI.LU
	 */
	public Long getCodeid(){
		return codeid;
	}

	/**
	 * @param codeid
	 *            要设置的 codeid
	 * @date 2016年1月20日 上午11:30:30
	 * @author GEWEI.LU
	 */
	public PromotionAllCodeCommand(){
		super();
	}

	public Integer getCouponType(){
		return couponType;
	}

	public void setCouponType(Integer couponType){
		this.couponType = couponType;
	}

	public BigDecimal getCouponDiscount(){
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount){
		this.couponDiscount = couponDiscount;
	}

}
