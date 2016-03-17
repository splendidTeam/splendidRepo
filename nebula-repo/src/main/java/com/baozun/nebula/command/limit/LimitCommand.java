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

package com.baozun.nebula.command.limit;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.command.Command;

/**
 * @author - 项硕
 */
public class LimitCommand implements Command {

	private static final long serialVersionUID = 6498268974192723584L;

	/** PK */
	private Long id;
	
	/** 店铺ID */
	private Long shopId;

	/** 名称 */
	private String name;

	/** 缺省优先级，为空。都为空时，按启用时间倒序排，启用时间和当前时间越近的优先级越高。1~10空冲突时自行调整。 */
	private Integer defaultPriority = 10;
	
	/** 0待启用（包括设置未结束的），1已启用，2已生效，3已取消（包括按有效期正常结束的） */
	private Integer lifecycle;
	
	/** 开始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;
	
	/** 创建时间 */
	private Date createTime;

	/** 创建人员ID */
	private Long createId;

	/** 创建人员名称 */
	private String createName;

	/** 更新时间 */
	private Date updateTime;
	
	/** 更新人员ID */
	private Long updateId;
	
	/** 更新人员名称 */
	private String updateName;

	/** 启用时间 */
	private Date publishTime;
	
	/** 启用人员ID */
	private Long publishId;
	
	/** 启用人员名称 */
	private String publishName;

	/** 取消时间 */
	private Date cancelTime;
	
	/** 取消人员ID */
	private Long cancelId;
	
	/** 取消人员名称 */
	private String cancelName;
	
	/** 受益人群ID */
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
	
	/** 条件ID */
	private Long conditionId;
	
	/** 表达式中文文本 */
	private String conditionText;
	
	/** 表达式 */
	private String conditionExpression;
	
	/** 表达式拆分成的条件项列表 */
	private List<String> conditionExpressionList;

	/** 表达式文本拆分成的条件项列表 */
	private List<String> conditionTextList;

	/** 解析后条件原子对象列表 */
	private List<AtomicCondition> atomicLimitationList;
	
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

	public Integer getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
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

	public String getProductComboExpression() {
		return productComboExpression;
	}

	public void setProductComboExpression(String productComboExpression) {
		this.productComboExpression = productComboExpression;
	}

	public Long getConditionId() {
		return conditionId;
	}

	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}

	public String getConditionText() {
		return conditionText;
	}

	public void setConditionText(String conditionText) {
		this.conditionText = conditionText;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public String getCancelName() {
		return cancelName;
	}

	public void setCancelName(String cancelName) {
		this.cancelName = cancelName;
	}

	public String getMemberComboText() {
		return memberComboText;
	}

	public void setMemberComboText(String memberComboText) {
		this.memberComboText = memberComboText;
	}

	public String getProductComboText() {
		return productComboText;
	}

	public void setProductComboText(String productComboText) {
		this.productComboText = productComboText;
	}

	public List<String> getConditionExpressionList() {
		return conditionExpressionList;
	}

	public void setConditionExpressionList(List<String> conditionExpressionList) {
		this.conditionExpressionList = conditionExpressionList;
	}

	public List<String> getConditionTextList() {
		return conditionTextList;
	}

	public void setConditionTextList(List<String> conditionTextList) {
		this.conditionTextList = conditionTextList;
	}

	public List<AtomicCondition> getAtomicLimitationList() {
		return atomicLimitationList;
	}

	public void setAtomicLimitationList(List<AtomicCondition> atomicLimitationList) {
		this.atomicLimitationList = atomicLimitationList;
	}
}
