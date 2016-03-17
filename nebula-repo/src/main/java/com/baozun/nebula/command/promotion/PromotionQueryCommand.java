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

import com.baozun.nebula.command.Command;

/**
 * 促销VO，专用于查询列表显示<br>
 * 只包含促销的头部、受益人群和范围的相关信息
 * 
 * @author - 项硕
 */
public class PromotionQueryCommand implements Command {
	
	private static final long serialVersionUID = -7091062605153262446L;

	/** PK */
	private Long id;

	/** 店铺id */
	private Long shopId;
	
	/** 促销名 */
	private String name;

	/** 缺省优先级，为空。都为空时，按启用时间倒序排，启用时间和当前时间越近的优先级越高。1~10空冲突时自行调整。 */
	private Integer defaultpriority;

	/** 活动开始时间 */
	private Date startTime;

	/** 活动结束时间 */
	private Date endTime;

	/** 活动角标图片id */
	private Long logoType;

	/** 创建时间 */
	private Date createTime;

	/** 创建人员id */
	private Long createId;

	/** 创建人员姓名 */
	private String createName;

	/** 最后修改时间 */
	private Date updateTime;

	/** 最后修改人员id */
	private Long updateId;
	
	/** 最后修改人员姓名 */
	private String updateName;

	/** 发布时间 */
	private Date publishTime;

	/** 发布人员id */
	private Long publishId;
	
	/** 发布人员姓名 */
	private String publishName;

	/** 取消启用时间 */
	private Date cancelTime;

	/** 取消启用人员id */
	private Long cancelId;
	
	/** 取消启用人员姓名 */
	private String cancelName;

	/** 生命周期：0待启用（包括设置未结束的），1已启用，2已生效，3已取消（包括按有效期正常结束的） */
	private Integer lifecycle;

	/** 来源促销id：生效期内修改，复制一份待启用（草稿），记录草稿复制来源 */
	private Long copyFrom;
	
	/** PK */
	private Long audienceId;

	/** 会员筛选器ID */
	private Long memberComboId;

	/** 会员筛选器类型 */
	private Integer memberComboType;
	
	/** 会员筛选器名称 */
	private String memberComboName;
	
	/** 会员筛选器文本 */
	private String memberComboText;
	
	/** 会员筛选器表达式 */
	private String memberComboExpression;
	
	/** 限购范围ID */
	private Long scopeId;

	/** 商品筛选器ID */
	private Long productComboId;

	/** 商品筛选器类型 */
	private Integer productComboType;
	
	/** 商品筛选器名称 */
	private String productComboName;
	
	/** 商品筛选器文本 */
	private String productComboText;
	
	/** 商品筛选器表达式 */
	private String productComboExpression;
	
	/**
	 * 何波 促销条件
	 */
	private  String   conditionType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDefaultpriority() {
		return defaultpriority;
	}

	public void setDefaultpriority(Integer defaultpriority) {
		this.defaultpriority = defaultpriority;
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

	public Long getLogoType() {
		return logoType;
	}

	public void setLogoType(Long logoType) {
		this.logoType = logoType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Long getCancelId() {
		return cancelId;
	}

	public void setCancelId(Long cancelId) {
		this.cancelId = cancelId;
	}

	public String getCancelName() {
		return cancelName;
	}

	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getCopyFrom() {
		return copyFrom;
	}

	public void setCopyFrom(Long copyFrom) {
		this.copyFrom = copyFrom;
	}

	public Long getAudienceId() {
		return audienceId;
	}

	public void setAudienceId(Long audienceId) {
		this.audienceId = audienceId;
	}

	public Long getMemberComboId() {
		return memberComboId;
	}

	public void setMemberComboId(Long memberComboId) {
		this.memberComboId = memberComboId;
	}

	public Integer getMemberComboType() {
		return memberComboType;
	}

	public void setMemberComboType(Integer memberComboType) {
		this.memberComboType = memberComboType;
	}

	public String getMemberComboName() {
		return memberComboName;
	}

	public void setMemberComboName(String memberComboName) {
		this.memberComboName = memberComboName;
	}

	public String getMemberComboText() {
		return memberComboText;
	}

	public void setMemberComboText(String memberComboText) {
		this.memberComboText = memberComboText;
	}

	public String getMemberComboExpression() {
		return memberComboExpression;
	}

	public void setMemberComboExpression(String memberComboExpression) {
		this.memberComboExpression = memberComboExpression;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Long getProductComboId() {
		return productComboId;
	}

	public void setProductComboId(Long productComboId) {
		this.productComboId = productComboId;
	}

	public Integer getProductComboType() {
		return productComboType;
	}

	public void setProductComboType(Integer productComboType) {
		this.productComboType = productComboType;
	}

	public String getProductComboName() {
		return productComboName;
	}

	public void setProductComboName(String productComboName) {
		this.productComboName = productComboName;
	}

	public String getProductComboText() {
		return productComboText;
	}

	public void setProductComboText(String productComboText) {
		this.productComboText = productComboText;
	}

	public String getProductComboExpression() {
		return productComboExpression;
	}

	public void setProductComboExpression(String productComboExpression) {
		this.productComboExpression = productComboExpression;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
}
