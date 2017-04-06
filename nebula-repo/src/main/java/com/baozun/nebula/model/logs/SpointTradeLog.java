/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.model.logs;

import java.math.BigDecimal;
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

/**
 * 积分交易记录
 * 
 * @author pengfei.fang
 * @date 2016年02月02日
 */
@Entity
@Table(name = "T_STORE_SPOINT_TRADE_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SpointTradeLog{

	private Long		id;

	/** 创建时间 */
	private Date		createTime	= new Date();

	/** 修改时间 */
	private Date		updateTime;

	/** 订单表Id */
	private Long		orderId;

	/** 消费的积分数量 */
	private BigDecimal	spoint;

	/** 该笔订单可获得的积分 */
	private BigDecimal	receivePoints;

	/** 类型: 1:冻结 2:消费 3:赎回 4:取消5：未使用积分 */
	private String		type;

	/** 状态: 10010:初始, 10020:处理成功, 10030:处理中, 10040:处理失败,10050:取消操作，用于积分还未冻结成功的订单，就发生取消订单操作, 10060:退货操作，用于积分还未冻结成功的订单，就发生退货订单操作 */
	private String		status;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SPOINT_TRADE_LOG",sequenceName = "S_T_SPOINT_TRADE_LOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SPOINT_TRADE_LOG")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "CREATETIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "UPDATETIME")
	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	@Column(name = "ORDERID")
    @Index(name = "IDX_SPOINT_TRADE_ORDERID")
	public Long getOrderId(){
		return orderId;
	}

	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}

	@Column(name = "SPOINT")
	public BigDecimal getSpoint(){
		return spoint;
	}

	public void setSpoint(BigDecimal spoint){
		this.spoint = spoint;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_SPOINT_TRADE_TYPE")
	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	@Column(name = "STATUS")
    @Index(name = "IDX_SPOINT_TRADE_STATUS")
	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	@Column(name = "RECEIVE_POINTS")
	public BigDecimal getReceivePoints(){
		return receivePoints;
	}

	public void setReceivePoints(BigDecimal receivePoints){
		this.receivePoints = receivePoints;
	}

}