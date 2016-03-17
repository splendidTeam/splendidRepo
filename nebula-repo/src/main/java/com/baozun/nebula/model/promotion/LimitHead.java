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

package com.baozun.nebula.model.promotion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * @author - 项硕
 */
@Entity
@Table(name = "t_sc_limit_head")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class LimitHead extends BaseModel {

	private static final long serialVersionUID = -8496041163877029207L;
	
	/** 待启用 */
	public static final Integer LIFECYCLE_UNACTIVE = 0;
	/** 已启用 */
	public static final Integer LIFECYCLE_ACTIVE = 1;
	/** 已生效 */
	public static final Integer LIFECYCLE_EFFECTIVE = 2;
	/** 已取消（包括已过期） */
	public static final Integer LIFECYCLE_CANCELED = 3;

	/** 优先级-默认 */
	public static final Integer PRIORITY_DEFAULT = 10;
	/** 优先级-1 */
	public static final Integer PRIORITY_ONE = 1;
	/** 优先级-2*/
	public static final Integer PRIORITY_TWO = 2;
	/** 优先级-3 */
	public static final Integer PRIORITY_THREE = 3;
	
	/** 表达式前缀-订单内单品件数 */
	public static final String EXP_PREFIX_ORDSKUQTY = "ordskuqty";
	/** 表达式前缀-订单内商品数 */
	public static final String EXP_PREFIX_ORDITEMQTY = "orditemqty";
	/** 表达式前缀-订单内件数 */
	public static final String EXP_PREFIX_ORDQTY = "ordqty";
	/** 表达式前缀-历史购买件数 */
	public static final String EXP_PREFIX_HISORDSKUQTY = "hisordskuqty";
	/** 表达式前缀-历史购买商品数 */
	public static final String EXP_PREFIX_HISORDITEMQTY = "hisorditemqty";
	/** 表达式前缀-历史购买订单数 */
	public static final String EXP_PREFIX_HISTORDQTY = "histordqty";

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

	/** 更新时间 */
	private Date updateTime;
	
	/** 更新人员ID */
	private Long updateId;

	/** 启用时间 */
	private Date publishTime;
	
	/** 启用人员ID */
	private Long publishId;

	/** 取消时间 */
	private Date cancelTime;
	
	/** 取消人员ID */
	private Long cancelId;
	
	/** version. */
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SC_LIMIT_HEAD",sequenceName = "S_T_SC_LIMIT_HEAD",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SC_LIMIT_HEAD")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DEFAULT_PRIORITY")
	public Integer getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "START_TIME", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", nullable = false)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "UPDATE_ID")
	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Column(name = "PUBLISH_ID")
	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	@Column(name = "CANCEL_TIME")
	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	@Column(name = "CANCEL_ID")
	public Long getCancelId() {
		return cancelId;
	}

	public void setCancelId(Long cancelId) {
		this.cancelId = cancelId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}
