/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 付款详细
 * 与payinfolog是一对多的关联，主要是用于合并付款
 * 所以每次合并付款，都会在payinfolog、当前这个表中重新生成一条记录，payinfolog有一条记录的拆单号与当前记录的拆单号是一致的
 * 当然，没有合并付款也会使用这一套体系，但是如果不支持重新选银行，这里不会每次重新生成记录.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "T_SO_PAYCODE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayCode extends BaseModel{


	/**  */
	private static final long serialVersionUID = -3350505308978769968L;

	/** PK. */
	private Long				id;
	
	/** 支付数值. */
	private BigDecimal			payNumerical;
	
	/** 支付金额(已包含运费). */
	private BigDecimal			payMoney;
	
	/** 支付类型. */
	private Integer				payType;
	
	/** 修改时间. */
	private Date				modifyTime;
	
	/** version. */
	private Date				version;
	
	/** 是否支付成功状态 *. */
	private Boolean 			paySuccessStatus;
	
	/** 拆单号 *. */
	private String 				subOrdinate;
	
	/** 创建时间 *. */
	private Date 				createTime;
	
	/**
     * 获得 pK.
     *
     * @return the pK
     */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_ORDERDETAIL",sequenceName = "S_T_SAL_ORDERDETAIL",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERDETAIL")
	public Long getId() {
		return id;
	}


	/**
     * 设置 pK.
     *
     * @param id
     *            the new pK
     */
	public void setId(Long id) {
		this.id = id;
	}

	/**
     * 获得 支付数值.
     *
     * @return the 支付数值
     */
	@Column(name = "PAY_NUMERICAL")
	public BigDecimal getPayNumerical() {
		return payNumerical;
	}


	/**
     * 设置 支付数值.
     *
     * @param payNumerical
     *            the new 支付数值
     */
	public void setPayNumerical(BigDecimal payNumerical) {
		this.payNumerical = payNumerical;
	}

	/**
     * 获得 支付金额(已包含运费).
     *
     * @return the 支付金额(已包含运费)
     */
	@Column(name = "PAY_MONEY")
	public BigDecimal getPayMoney() {
		return payMoney;
	}


	/**
     * 设置 支付金额(已包含运费).
     *
     * @param payMoney
     *            the new 支付金额(已包含运费)
     */
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	/**
     * 获得 支付类型.
     *
     * @return the 支付类型
     */
	@Column(name = "PAY_TYPE")
    @Index(name = "IDX_PAYCODE_PAY_TYPE")
	public Integer getPayType() {
		return payType;
	}


	/**
     * 设置 支付类型.
     *
     * @param payType
     *            the new 支付类型
     */
	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	/**
     * 获得 修改时间.
     *
     * @return the 修改时间
     */
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
     * 设置 修改时间.
     *
     * @param modifyTime
     *            the new 修改时间
     */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
     * 获得 version.
     *
     * @return the version
     */
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
     * 设置 version.
     *
     * @param version
     *            the new version
     */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**
     * 获得 是否支付成功状态 *.
     *
     * @return the 是否支付成功状态 *
     */
	@Column(name = "PAY_SUCCESS_STATUS")
    @Index(name = "IDX_PAYCODE_PAY_SUCCESS_STATUS")
	public Boolean getPaySuccessStatus() {
		return paySuccessStatus;
	}

	/**
     * 设置 是否支付成功状态 *.
     *
     * @param paySuccessStatus
     *            the new 是否支付成功状态 *
     */
	public void setPaySuccessStatus(Boolean paySuccessStatus) {
		this.paySuccessStatus = paySuccessStatus;
	}

	/**
     * 获得 拆单号 *.
     *
     * @return the 拆单号 *
     */
	@Column(name = "SUB_ORDINATE")
    @Index(name = "IDX_PAYCODE_SUB_ORDINATE")
	public String getSubOrdinate() {
		return subOrdinate;
	}

	/**
     * 设置 拆单号 *.
     *
     * @param subOrdinate
     *            the new 拆单号 *
     */
	public void setSubOrdinate(String subOrdinate) {
		this.subOrdinate = subOrdinate;
	}
	
	/**
     * 获得 创建时间 *.
     *
     * @return the 创建时间 *
     */
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
     * 设置 创建时间 *.
     *
     * @param createTime
     *            the new 创建时间 *
     */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
