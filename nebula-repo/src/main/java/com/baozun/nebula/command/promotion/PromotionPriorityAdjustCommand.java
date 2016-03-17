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

package com.baozun.nebula.command.promotion;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustDetail;

/**
 * 优先级调整 
 */  
public class PromotionPriorityAdjustCommand extends BaseModel {  
	    
	private static final long serialVersionUID = 2100375102361992478L;
	
	/** 状态：待启用 */
	public static final Integer STATUS_INACTIVE = 0; 
	/** 状态：已启用 */
	public static final Integer STATUS_ACTIVE = 1; 
	/** 状态：已过期 */
	public static final Integer STATUS_EXPIRED = 2; 
	/** 状态：已禁用 */
	public static final Integer STATUS_FORBIDDEN = 3; 

	
	/** PK */
	private Long id;

	/** 调整名称 */
	private String adjustName;	 

	/**开始时间**/
	private Date startTime;
	
	/**结束时间**/
	private Date endTime;

	/** 生效标志：1生效，0不生效。针对调整 */
	private Integer activeMark; 
	/** * 所属店铺 */
	private Long shopId; 

	/** 修改人员id */
	private Long lastUpdateId;  

	/** 修改时间 */
	private Date lastUpdateTime;
	
	/** 状态 */
	private Integer status;
	
	/** 优先级详细列表的JSON字符串 */
	private String priorityDetailListString;
	
	/***详情信息****************/
	private List<PromotionPriorityAdjustDetail> promotionPriorityAdjustDetail;
	
	private String realname; 
	 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "ADJUSTNAME")
	public String getAdjustName() {
		return adjustName;
	}

	public void setAdjustName(String adjustName) {
		this.adjustName = adjustName;
	}
	@Column(name = "STARTTIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(name = "ENDTIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name = "ACTIVEMARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}
	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	@Column(name = "LASTUPDATEID")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}
	@Column(name = "LASTUPDATETIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	} 

	public List<PromotionPriorityAdjustDetail> getPromotionPriorityAdjustDetail() {
		return promotionPriorityAdjustDetail;
	}

	public void setPromotionPriorityAdjustDetail(
			List<PromotionPriorityAdjustDetail> promotionPriorityAdjustDetail) {
		this.promotionPriorityAdjustDetail = promotionPriorityAdjustDetail;
	}
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPriorityDetailListString() {
		return priorityDetailListString;
	}

	public void setPriorityDetailListString(String priorityDetailListString) {
		this.priorityDetailListString = priorityDetailListString;
	}

}
