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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 促销日志表
 * @author - 项硕
 */
@Entity
@Table(name = "T_PRM_OPERATIONLOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionOperationLog extends BaseModel {

	private static final long serialVersionUID = -3081591567733416594L;
	
	/** 操作类型： 创建编辑 */
	public static final Integer OPERATION_TYPE_EDIT = 1;
	/** 操作类型： 启用 */
	public static final Integer OPERATION_TYPE_ACTIVE = 2;
	/** 操作类型： 取消启用 */
	public static final Integer OPERATION_TYPE_CANCEL = 3;
	/** 操作类型： 复制 */
	public static final Integer OPERATION_TYPE_COPY = 4;

	/** 操作名称： 创建编辑 */
	public static final String OPERATION_NAME_EDIT = "创建编辑";
	/** 操作名称： 启用 */
	public static final String OPERATION_NAME_ACTIVE = "启用";
	/** 操作名称： 取消启用 */
	public static final String OPERATION_NAME_CANCEL = "取消启用";
	/** 操作名称： 复制 */
	public static final String OPERATION_NAME_COPY = "复制";
	
	/** 操作类型与名称的对应map */
	public static final Map<Integer, String> OPERATION_TYPE_TO_NAME_MAP = new HashMap<Integer, String>();
	
	static {
		OPERATION_TYPE_TO_NAME_MAP.put(OPERATION_TYPE_EDIT, OPERATION_NAME_EDIT);
		OPERATION_TYPE_TO_NAME_MAP.put(OPERATION_TYPE_ACTIVE, OPERATION_NAME_ACTIVE);
		OPERATION_TYPE_TO_NAME_MAP.put(OPERATION_TYPE_CANCEL, OPERATION_NAME_CANCEL);
		OPERATION_TYPE_TO_NAME_MAP.put(OPERATION_TYPE_COPY, OPERATION_NAME_COPY);
	}

	/** PK */
	private Long id;
	
	/** 促销id */
	private Long promotionId;

	/** 操作日志 */
	private String note;

	/** 操作类型：1创建编辑，2启用，3取消启用，4复制 */
	private Integer operationType;

	/** 操作人id */
	private Long operatorId;

	/** 操作人id */
	private Date operateTime;

	/** version. */
	private Date version;
	
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_PRM_OPERATIONLOG", sequenceName = "S_T_PRM_OPERATIONLOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PRM_OPERATIONLOG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="PROMOTION_ID")
    @Index(name = "IDX_OPERATIONLOG_PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name="NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name="OPERATION_TYPE")
    @Index(name = "IDX_OPERATIONLOG_OPERATION_TYPE")
	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	@Column(name="OPERATOR_ID")
    @Index(name = "IDX_OPERATIONLOG_OPERATOR_ID")
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name="OPERATE_TIME")
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
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
