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
 * 支付日志
 * @author 阳羽
 * @createtime 2014-1-13 上午11:07:23
 */
@Entity
@Table(name = "t_so_paymentlog")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PaymentLog extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7964191707553842801L;

	/** PK. */
	private Long				id;
	
	/**支付日志创建时间**/
	private Date createTime;
	
	/** 支付日志信息 **/
	private String message;
	
	/**支付的用户**/
	private String operator;
	
	/**调用支付操作时的返回值**/
	private String returnVal;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SO_PAYMENTLOG",sequenceName = "SEQ_T_SO_PAYMENTLOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_PAYMENTLOG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name="OPERATOR")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name="RETURN_VAL",length=4000)
	public String getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(String returnVal) {
		this.returnVal = returnVal;
	}
}
