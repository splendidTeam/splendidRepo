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
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 * 
 */
public class ItemRateListCommand extends BaseModel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6415349176638136785L;

	/** 商品主图 */
	private String itemPic;

	/** 商品name */
	private String itemName;

	/** 创建时间 */
	private Date createTime;

	/** 评价状态 */
	private Integer evaluationStatus;

	/** 订单id **/
	private Long orderId; 
	
	/**会员Id*/
	private Long memberId;
 

	/** 订单行id **/
	private Long soLineId;

	 
	/** 订单号 */
	private String code;

	/** 评论id **/
	private Long rateId;

	/** 商品id */
	private Long itemId;

	/** 回复内容 */
	private String reply;
	
	/**生命周期*/
	private Long lifecycle;
	/**
	 * 登录名
	 */
	private String loginName;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Long lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getEvaluationStatus() {
		return evaluationStatus;
	}

	public void setEvaluationStatus(Integer evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	 

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getSoLineId() {
		return soLineId;
	}

	public void setSoLineId(Long soLineId) {
		this.soLineId = soLineId;
	}

}
