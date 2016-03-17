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
package com.baozun.nebula.model.payment;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 付款详细
 * 与payinfolog是一对多的关联，主要是用于合并付款
 * 所以每次合并付款，都会在payinfolog、当前这个表中重新生成一条记录，payinfolog有一条记录的拆单号与当前记录的拆单号是一致的
 * 当然，没有合并付款也会使用这一套体系，但是如果不支持重新选银行，这里不会每次重新生成记录
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_paycode")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayCode extends BaseModel{


	private static final long serialVersionUID = -3350505308978769968L;

	/** PK. */
	private Long				id;
	
	/** 支付数值 */
	private BigDecimal			payNumerical;
	
	/** 支付金额(已包含运费) */
	private BigDecimal			payMoney;
	
	/** 支付类型 */
	private Integer				payType;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** version*/
	private Date				version;
	
	/** 是否支付成功状态 **/
	private Boolean 			paySuccessStatus;
	
	/** 拆单号 **/
	private String 				subOrdinate;
	
	/** 创建时间  **/
	private Date 				createTime;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_ORDERDETAIL",sequenceName = "S_T_SAL_ORDERDETAIL",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERDETAIL")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PAY_NUMERICAL")
	public BigDecimal getPayNumerical() {
		return payNumerical;
	}


	public void setPayNumerical(BigDecimal payNumerical) {
		this.payNumerical = payNumerical;
	}

	@Column(name = "PAY_MONEY")
	public BigDecimal getPayMoney() {
		return payMoney;
	}


	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	@Column(name = "PAY_TYPE")
	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "PAY_SUCCESS_STATUS")
	public Boolean getPaySuccessStatus() {
		return paySuccessStatus;
	}

	public void setPaySuccessStatus(Boolean paySuccessStatus) {
		this.paySuccessStatus = paySuccessStatus;
	}

	@Column(name = "SUB_ORDINATE")
	public String getSubOrdinate() {
		return subOrdinate;
	}

	public void setSubOrdinate(String subOrdinate) {
		this.subOrdinate = subOrdinate;
	}
	
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
