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
package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;

/**
 * 商品预售信息Command
 * 
 * @author jinbao.ji
 * @date 2016年2月2日 下午5:46:41
 */
public class ItemPresalseInfoCommand implements Command{

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;

	/**
	 * PAYMENT_FULLMONEY:全款
	 */
	public static final Integer			PAYMENT_FULLMONEY	= 0;

	/**
	 * EARNEST_AND_BALANCE:定金和尾款
	 */
	public static final Integer			EARNEST_AND_BALANCE	= 1;

	/** PresellItem中的id */
	private Long						id;

	/** 商品ItemCode */
	private String						itemCode;

	/** 商品ItemId */
	private Long						itemId;

	/** 商品名称 */
	private String						itemName;

	/** 商品 的生命周期 */
	private Integer						itemLifecycle;

	/** ( 全款 0 ，订金+尾款 1 ); */
	private Integer						paymentMethod;

	/** 预计发货时间 */
	private Date						deliveryTime;
	
	/** 预计发货时间(用于接收表单参数) */
	private String						deliveryTimeStr;

	/** 活动名称 */
	private String						activityName;

	/** 活动开始时间 */
	private Date						activityStartTime;
	
	/** 活动开始时间 (用于接收表单参数)*/
	private String						activityStartTimeStr;

	/** 活动结束时间 */
	private Date						activityEndTime;
	
	/** 活动结束时间(用于接收表单参数) */
	private String						activityEndTimeStr;

	/** 尾款支付结束时间; */
	private Date						endtime;
	
	/** 尾款支付结束时间(用于接收表单参数) */
	private String						endtimeStr;

	/** 1 上架 2 删除 3 下架(预售信息); */
	private Integer						lifecycle;

	/**
	 * 预售商品SKU价格库存信息
	 */
	List<ItemPresalseSkuInfoCommand>	itemPresalseSkuInfoCommandList;
	
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getItemCode(){
		return itemCode;
	}

	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
	}

	public Integer getItemLifecycle(){
		return itemLifecycle;
	}

	public void setItemLifecycle(Integer itemLifecycle){
		this.itemLifecycle = itemLifecycle;
	}

	public Integer getPaymentMethod(){
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod){
		this.paymentMethod = paymentMethod;
	}

	public Date getDeliveryTime(){
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	public Date getActivityStartTime(){
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime){
		this.activityStartTime = activityStartTime;
	}

	public Date getEndtime(){
		return endtime;
	}

	public void setEndtime(Date endtime){
		this.endtime = endtime;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public Date getActivityEndTime(){
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime){
		this.activityEndTime = activityEndTime;
	}

	public List<ItemPresalseSkuInfoCommand> getItemPresalseSkuInfoCommandList(){
		return itemPresalseSkuInfoCommandList;
	}

	public void setItemPresalseSkuInfoCommandList(List<ItemPresalseSkuInfoCommand> itemPresalseSkuInfoCommandList){
		this.itemPresalseSkuInfoCommandList = itemPresalseSkuInfoCommandList;
	}

	public String getItemName(){
		return itemName;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	
	public String getActivityName(){
		return activityName;
	}

	
	public void setActivityName(String activityName){
		this.activityName = activityName;
	}

	
	public String getDeliveryTimeStr(){
		return deliveryTimeStr;
	}

	
	public void setDeliveryTimeStr(String deliveryTimeStr){
		this.deliveryTimeStr = deliveryTimeStr;
	}

	
	public String getActivityStartTimeStr(){
		return activityStartTimeStr;
	}

	
	public void setActivityStartTimeStr(String activityStartTimeStr){
		this.activityStartTimeStr = activityStartTimeStr;
	}

	
	public String getActivityEndTimeStr(){
		return activityEndTimeStr;
	}

	
	public void setActivityEndTimeStr(String activityEndTimeStr){
		this.activityEndTimeStr = activityEndTimeStr;
	}

	
	public String getEndtimeStr(){
		return endtimeStr;
	}

	
	public void setEndtimeStr(String endtimeStr){
		this.endtimeStr = endtimeStr;
	}

}
