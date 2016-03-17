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
import java.util.List;

import loxia.annotation.Column;

import com.baozun.nebula.calculateEngine.condition.AtomicAudience;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicScope;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.param.ConditionMasterType;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.promotion.PromotionAudiences;
import com.baozun.nebula.model.promotion.PromotionScope;

/**
 * @author - 项硕
 */
public class PromotionCommand implements Command {

	private static final long serialVersionUID = 6646021135553114497L;

	private ConditionNormalCommand conditionNormal;

	private SettingNormalCommand settingNormal;

	private List<ConditionComplexCommand> conditionComplexList;

	private List<SettingComplexCommand> settingComplexList;

	private PromotionAudiences audiences;

	private PromotionScope scope;

	/** PK */
	private Long promotionId;

	/** 店铺id */
	private Long shopId;

	/** 缺省优先级，为空。都为空时，按启用时间倒序排，启用时间和当前时间越近的优先级越高。1~10空冲突时自行调整。 */
	private Integer defaultpriority;
	
	/** 优先级 */
	private Integer priority; 
	
	/** 优先级 */
	private String groupname; 
	
	/** 优先级分组0：N选1排他逻辑，1：共享排他逻辑 */
	private Integer groupType = 0; 
	
	/** 排他标识 */
	private Integer exclusivemark = 0;
	
	/** 更新人员名称 */
	private String realname;

	/** 促销名 */
	private String promotionName;

	/** 活动开始时间 */
	private Date startTime;

	/** 活动结束时间 */
	private Date endTime;

	/** 活动角标图片id */
	private Long logo;

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

	/***************************** 人群 *******************/
	/** PK */
	private Long audiencesId;

	/** 会员组合类型：1会员列表，2会员分组，3自定义,4组合 */
	private Integer memComboType;

	/** 人群组合表达式对应的名称 */
	private String memComboName;

	/** 人群组合表达式对应的名称 */
	private String memComboExpression;
	private List<AtomicAudience> atomicAudienceList;
	/***************************** 商品范围 *******************/
	/** PK */
	private Long scopeId;

	/** 商品范围类型：1商品，2目录，3自定义，4组合 */
	private Integer scopeType;

	/** 商品组合名称 */
	private String scopeName;

	/** 商品组合名称 */
	private String scopeExpression;

	private List<AtomicScope> atomicScopeList;
	/***************************** Normal条件 *******************/
	/** PK */
	private Long conditionId;
	/**
	 * 条件类型: Normal常规 Step阶梯 Choice选购 NormalStep常规加阶梯 NormalChoice常规加选购
	 */
	private String conditionType;

	/** 促销条件表达式名称 */
	private String conditionName;

	/** 促销条件表达式名称 */
	private String conditionExpression;

	private List<AtomicCondition> atomicConditionList;
	private List<AtomicCondition> atomicComplexConditionList;

	/***************************** Normal优惠 *******************/
	/** PK */
	private Long settingId;

	/** 促销id */
	private String settingName;

	/** 促销id */
	private String settingExpression;

	private List<AtomicSetting> atomicSettingList;

	private List<AtomicSetting> atomicComplexSettingList;

	/** t_mem_custommembergroup组合表关联，组合编号 */
	private Long memComboId;

	/** 商品组合编号 */
	private Long productComboId;
	
	@Column("PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column("REALNAME")
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
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
	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
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

	@Column("DEFAULT_PRIORITY")
	public Integer getDefaultPriority() {
		return defaultpriority;
	}

	public void setDefaultPriority(Integer defaultpriority) {
		this.defaultpriority = defaultpriority;
	}

	@Column("SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column("AUDIENCES_ID")
	public Long getAudiencesId() {
		return audiencesId;
	}

	public void setAudiencesId(Long audiencesId) {
		this.audiencesId = audiencesId;
	}

	@Column("MEM_COMBO_TYPE")
	public Integer getMemComboType() {
		return memComboType;
	}

	public void setMemComboType(Integer memComboType) {
		this.memComboType = memComboType;
	}

	@Column("MEM_COMBO_NAME")
	public String getMemComboName() {
		return memComboName;
	}

	public void setMemComboName(String memComboName) {
		this.memComboName = memComboName;
	}

	@Column("MEM_COMBO_EXPRESSION")
	public String getMemComboExpression() {
		return memComboExpression;
	}

	public void setMemComboExpression(String memComboExpression) {
		this.memComboExpression = memComboExpression;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Column("PRODUCT_COMBO_TYPE")
	public Integer getScopeType() {
		return scopeType;
	}

	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	@Column("PRODUCT_COMBO_NAME")
	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	@Column("PRODUCT_COMBO_EXPRESSION")
	public String getScopeExpression() {
		return scopeExpression;
	}

	public void setScopeExpression(String scopeExpression) {
		this.scopeExpression = scopeExpression;
	}

	@Column("CONDITION_ID")
	public Long getConditionId() {
		return conditionId;
	}

	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}

	@Column("CONDITION_TYPE")
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	@Column("CONDITION_NAME")
	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	@Column("CONDITION_EXPRESS")
	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	@Column("SETTING_ID")
	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	@Column("SETTING_NAME")
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	@Column("SETTING_EXPRESSION")
	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String settingExpression) {
		this.settingExpression = settingExpression;
	}

	public ConditionNormalCommand getConditionNormal() {
		return conditionNormal;
	}

	public void setConditionNormal(ConditionNormalCommand conditionNormal) {
		this.conditionNormal = conditionNormal;
	}

	public SettingNormalCommand getSettingNormal() {
		return settingNormal;
	}

	public void setSettingNormal(SettingNormalCommand settingNormal) {
		this.settingNormal = settingNormal;
	}

	public List<ConditionComplexCommand> getConditionComplexList() {
		return conditionComplexList;
	}

	public void setConditionComplexList(List<ConditionComplexCommand> conditionComplexList) {
		this.conditionComplexList = conditionComplexList;
	}

	public List<SettingComplexCommand> getSettingComplexList() {
		return settingComplexList;
	}

	public void setAtomicSettingList(List<AtomicSetting> list) {
		this.atomicSettingList = list;
	}

	public List<AtomicSetting> getAtomicSettingList() {
		return atomicSettingList;
	}

	public void setSettingComplexList(List<SettingComplexCommand> settingComplexList) {
		this.settingComplexList = settingComplexList;
	}

	public PromotionAudiences getAudiences() {
		return audiences;
	}

	public void setAudiences(PromotionAudiences audiences) {
		this.audiences = audiences;
	}

	public PromotionScope getScope() {
		return scope;
	}

	public void setScope(PromotionScope scope) {
		this.scope = scope;
	}

	public Long getMemComboId() {
		return memComboId;
	}

	public void setMemComboId(Long memComboId) {
		this.memComboId = memComboId;
	}

	public Long getProductComboId() {
		return productComboId;
	}

	public void setProductComboId(Long productComboId) {
		this.productComboId = productComboId;
	}

	public List<AtomicAudience> getAtomicAudienceList() {
		return atomicAudienceList;
	}

	public void setAtomicAudienceList(List<AtomicAudience> list) {
		this.atomicAudienceList = list;
	}

	public List<AtomicScope> getAtomicScopeList() {
		return atomicScopeList;
	}

	public void setAtomicScopeList(List<AtomicScope> list) {
		this.atomicScopeList = list;
	}

	public List<AtomicCondition> getAtomicConditionList() {
		return atomicConditionList;
	}

	public void setAtomicConditionList(List<AtomicCondition> list) {
		this.atomicConditionList = list;
	}

	public List<AtomicCondition> getAtomicComplexConditionList() {
		return atomicComplexConditionList;
	}

	public void setAtomicComplexConditionList(List<AtomicCondition> list) {
		this.atomicComplexConditionList = list;
	}

	public List<AtomicSetting> getAtomicComplexSettingList() {
		return atomicComplexSettingList;
	}

	public void setAtomicComplexSettingList(List<AtomicSetting> list) {
		this.atomicComplexSettingList = list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((promotionId == null) ? 0 : promotionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionCommand other = (PromotionCommand) obj;
		if (promotionId == null) {
			if (other.promotionId != null)
				return false;
		} else if (!promotionId.equals(other.promotionId))
			return false;
		return true;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getGroupName() {
		return groupname;
	}

	public void setGroupName(String groupname) {
		this.groupname = groupname;
	}

	public Integer getExclusiveMark() {
		return exclusivemark;
	}

	public void setExclusiveMark(Integer exclusivemark) {
		this.exclusivemark = exclusivemark;
	}
	/**
	 * 条件类型Id:  Choice选购：1， Step阶梯：2， NormalChoice常规加选购：3，NormalStep常规加阶梯 :4， Normal常规：5
	 * 直接通过conditionType来获得，没持久化到数据库
	 */
	public int getConditionTypeId() {
		int conditionTypeId = 0;
		if (conditionType!=null)
		{
			if (conditionType.equalsIgnoreCase(ConditionMasterType.EXP_CHOICE))
				conditionTypeId = ConditionMasterType.PRIORITY_CHOICE;
			if (conditionType.equalsIgnoreCase(ConditionMasterType.EXP_STEP))
				conditionTypeId = ConditionMasterType.PRIORITY_STEP;
			if (conditionType.equalsIgnoreCase(ConditionMasterType.EXP_NORMALCHOICE))
				conditionTypeId = ConditionMasterType.PRIORITY_NORMAL_CHOICE;
			if (conditionType.equalsIgnoreCase(ConditionMasterType.EXP_NORMALSTEP))
				conditionTypeId = ConditionMasterType.PRIORITY_NORMAL_STEP;
			if (conditionType.equalsIgnoreCase(ConditionMasterType.EXP_NORMAL))
				conditionTypeId = ConditionMasterType.PRIORITY_NORMAL;
		}
		return conditionTypeId;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
}
