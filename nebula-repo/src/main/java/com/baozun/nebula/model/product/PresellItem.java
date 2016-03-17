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
package com.baozun.nebula.model.product;

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
 * 预售商品基本信息
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "t_sto_presell_item")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PresellItem extends BaseModel{

	/**
	 * 全款
	 */
	public static final Integer	PAYMENTMETHOD_FULL				= 0;

	/**
	 * 订金+尾款
	 */
	public static final Integer	PAYMENTMETHOD_EARNEST_BALANCE	= 1;

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 1L;

	/** primary key Id. */
	private Long				id;

	/** 活动名称 */
	private String				activityName;

	/** 商品ItemCode */
	private String				itemCode;

	/** 商品ItemId */
	private Long				itemId;

	/** 创建时间 */
	private Date				createTime;

	/** 最近修改时间 */
	private Date				modifyTime;

	/** 预计发货时间 */
	private Date				deliveryTime;

	/** ( 全款 0 ，订金+尾款 1 ); */
	private Integer				paymentMethod;

	/** 活动开始时间 */
	private Date				activityStartTime;

	/** 活动结束时间 */
	private Date				activityEndTime;

	/** 尾款支付结束时间; */
	private Date				endtime;

	/** 1 上架 2 删除 3 下架; */
	private Integer				lifecycle;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_STO_PRESELL_ITEM",sequenceName = "S_T_STO_PRESELL_ITEM",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_STO_PRESELL_ITEM")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "ITEMCODE")
	public String getItemCode(){
		return itemCode;
	}

	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
	}

	@Column(name = "CREATETIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "MODIFYTIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Column(name = "DELIVERYTIME")
	public Date getDeliveryTime(){
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	@Column(name = "PAYMENTMETHOD")
	public Integer getPaymentMethod(){
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod){
		this.paymentMethod = paymentMethod;
	}

	@Column(name = "ACTIVITYSTARTTIME")
	public Date getActivityStartTime(){
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime){
		this.activityStartTime = activityStartTime;
	}

	@Column(name = "ENDTIME")
	public Date getEndtime(){
		return endtime;
	}

	public void setEndtime(Date endtime){
		this.endtime = endtime;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "ACTIVITYENDTIME")
	public Date getActivityEndTime(){
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime){
		this.activityEndTime = activityEndTime;
	}

	@Column(name = "ACTIVITYNAME")
	public String getActivityName(){
		return activityName;
	}

	public void setActivityName(String activityName){
		this.activityName = activityName;
	}

	@Column(name = "ITEMID")
	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

}
