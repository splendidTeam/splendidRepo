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
package com.baozun.nebula.model.salesorder;

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
 * SalesOrderExt 订单拓展表
 * 
 * @author: pengfei.fang
 * @date: 2016年02月02日
 **/

@Entity
@Table(name = "T_STORE_EXT_SO_SALES_ORDER")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SalesOrderExt extends BaseModel{

	private static final long	serialVersionUID					= 8864998764122811849L;

	/** 冻结 */
	public static final String	EPOINTTRADELOG_TYPE_FROZEN			= "1";

	/** 消费 */
	public static final String	EPOINTTRADELOG_TYPE_CONSUMPTION		= "2";

	/** 赎回 */
	public static final String	EPOINTTRADELOG_TYPE_REDEMPTION		= "3";

	/** 取消 */
	public static final String	EPOINTTRADELOG_TYPE_CANCELLATION	= "4";

	/** 未使用积分 */
	public static final String	EPOINTTRADELOG_TYPE_NOUSE			= "5";

	/** 初始 */
	public static final String	EPOINTTRADELOG_STATUS_INITIAL		= "10010";

	/** 处理成功 */
	public static final String	EPOINTTRADELOG_STATUS_SUCCESS		= "10020";

	/** 处理中 */
	public static final String	EPOINTTRADELOG_STATUS_PROCESSING	= "10030";

	/** 处理失败 */
	public static final String	EPOINTTRADELOG_STATUS_FAILURE		= "10040";

	/** 取消操作，用于积分还未冻结成功的订单，就发生取消订单操作 */
	public static final String	EPOINTTRADELOG_STATUS_CANCEL		= "10050";

	/** 退货操作，用于积分还未冻结成功的订单，就发生退货订单操作 */
	public static final String	EPOINTTRADELOG_STATUS_RETURN		= "10060";

	/** PK. */
	private Long				id;

	/** 订单Id */
	private Long				soId;

	/** 积分使用承认编号 */
	private String				evApprNO;

	/** 该笔订单使用的积分 */
	private BigDecimal			samPoints;

	/** 该笔订单可获得的积分 */
	private BigDecimal			receivePoints;

	/** 创建时间 */
	private Date				createTime							= new Date();

	/** 类型: 1:冻结 2:消费 3:赎回 4:取消5：未使用积分 */
	private String				type;

	/** 状态: 10010:初始, 10020:处理成功, 10030:处理中, 10040:处理失败,10050:取消操作，用于积分还未冻结成功的订单，就发生取消订单操作, 10060:退货操作，用于积分还未冻结成功的订单，就发生退货订单操作 */
	private String				status;

	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_STORE_EXT_SO_SALES_ORDER",sequenceName = "S_T_STORE_EXT_SO_SALES_ORDER",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_STORE_EXT_SO_SALES_ORDER")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "SO_ID")
	public Long getSoId(){
		return soId;
	}

	public void setSoId(Long soId){
		this.soId = soId;
	}

	@Column(name = "EV_APPR_NO")
	public String getEvApprNO(){
		return evApprNO;
	}

	public void setEvApprNO(String evApprNO){
		this.evApprNO = evApprNO;
	}

	@Column(name = "SAM_POINTS")
	public BigDecimal getSamPoints(){
		return samPoints;
	}

	public void setSamPoints(BigDecimal samPoints){
		this.samPoints = samPoints;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	@Column(name = "TYPE")
	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	@Column(name = "STATUS")
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
