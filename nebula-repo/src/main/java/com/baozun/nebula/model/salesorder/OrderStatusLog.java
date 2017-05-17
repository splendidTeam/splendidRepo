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
package com.baozun.nebula.model.salesorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 订单状态变更日志
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "T_SO_ORDERSTATUSLOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrderStatusLog extends BaseModel{


	private static final long serialVersionUID = -8984161864111442677L;

	/** PK. */
	private Long				id;
	
	/** 变更前状态 */
	private Integer				beforeStatus;
	
	/** 变更后状态 */
	private Integer				afterStatus;
	
	/** 操作人id */
	private String				operatorId;
	
	/** 操作人名称 */
	private String				operatorName;
	
	/** 创建时间 */
	private Date				createTime;
	
	/** version*/
	private Date				version;
	/** 订单id **/
	private Long 				orderId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_ORDERSTATUSLOG",sequenceName = "S_T_SAL_ORDERSTATUSLOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERSTATUSLOG")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BEFORE_STATUS")
	public Integer getBeforeStatus() {
		return beforeStatus;
	}


	public void setBeforeStatus(Integer beforeStatus) {
		this.beforeStatus = beforeStatus;
	}

	@Column(name = "AFTER_STATUS")
	public Integer getAfterStatus() {
		return afterStatus;
	}


	public void setAfterStatus(Integer afterStatus) {
		this.afterStatus = afterStatus;
	}

	@Column(name = "OPERATOR_ID", length = 100)
    @Index(name = "IDX_ORDERSTATUSLOG_OPERATOR_ID")
	public String getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "OPERATOR_NAME")
	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "ORDER_ID")
    @Index(name = "IDX_ORDERSTATUSLOG_ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
