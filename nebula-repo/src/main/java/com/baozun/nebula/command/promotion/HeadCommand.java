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

import java.util.Date;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;

/**
 * @author - 项硕
 */
public class HeadCommand implements Command {

	private static final long serialVersionUID = -2101870150244398395L;

	/** PK */
	private Long id;
	
	/** 店铺id */
	private Long shopId;
	
	/** 缺省优先级，为空。都为空时，按启用时间倒序排，启用时间和当前时间越近的优先级越高。1~10空冲突时自行调整。 */
	private Integer defaultpriority;

	/** 促销名 */
	private String promotionName;

	/** 活动开始时间 */
	private Date startTime;

	/** 活动结束时间 */
	private Date endTime;

	/** 活动角标图片id */
	private Long promotionLogoType;

	/** 创建时间 */
	private Date createTime;

	/** 创建人员id */
	private Long createId;

	/** 最后修改时间 */
	private Date lastUpdateTime;

	/** 最后修改人员id */
	private Long lastUpdateId;

	/** 发布时间 */
	private Date publishTime;

	/** 发布人员id */
	private Long publishId;

	/** 取消启用时间 */
	private Date cancelTime;

	/** 取消启用人员id */
	private Long cancelId;

	/** 生命周期：0待启用（包括设置未结束的），1已启用，2已生效，3已取消（包括按有效期正常结束的） */
	private Integer lifecycle;

	/** 来源促销id：生效期内修改，复制一份待启用（草稿），记录草稿复制来源 */
	private Long copyFrom;

	@Column("PROMOTION_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column("PROMOTION_NAME")
	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	@Column("START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column("END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column("PROMOTION_LOGO_TYPE")
	public Long getPromotionLogoType() {
		return promotionLogoType;
	}

	public void setPromotionLogoType(Long promotionLogoType) {
		this.promotionLogoType = promotionLogoType;
	}

	@Column("CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column("CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column("LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column("LAST_UPDATE_ID")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	@Column("PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Column("PUBLISH_ID")
	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	@Column("CANCEL_TIME")
	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	@Column("CANCEL_ID")
	public Long getCancelId() {
		return cancelId;
	}

	public void setCancelId(Long cancelId) {
		this.cancelId = cancelId;
	}
	
	@Column("LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column("COPY_FROM")
	public Long getCopyFrom() {
		return this.copyFrom;
	}

	public void setCopyFrom(Long copyFrom) {
		this.copyFrom = copyFrom;
	}

	@Column("SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column("DEFAULT_PRIORITY")
	public Integer getDefaultpriority() {
		return defaultpriority;
	}

	public void setDefaultpriority(Integer defaultpriority) {
		this.defaultpriority = defaultpriority;
	}
	
}
