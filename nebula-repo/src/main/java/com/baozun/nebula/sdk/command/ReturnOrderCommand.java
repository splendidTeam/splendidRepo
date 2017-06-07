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
package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;




public class ReturnOrderCommand extends BaseModel{

	
	private static final long serialVersionUID = -6876708139850962496L;

	/** PK. */
	private Long				id;
	
	/** 订单号 */
	private String				orderCode;
	
	/** 订单行id */
	private Long				orderLineId;

	/** 会员id */
	private Long				memberId;
	
	/** 数量 */
	private Integer				count;
	
	/** 服务类型 */
	private Integer				serviceType;
	
	/** 问题描述*/
	private String				describe;
	
	/** 反馈信息*/
	private String				feedback;
	
	/** 是否有发票 */
	private Integer				isReceipt;
	
	/** 问题图片 */
	private String				pic;
	
	/** 收件人姓名 */
	private String				name;
	
	/** 收件人地址 */
	private String				address;
	
	/** 手机号码 */
	private String				mobile;
	
	/** 状态*/
	private Integer				status;
	
	/** 处理人id*/
	private Long				handleId;
	
	/** 处理人姓名*/
	private String				handleName;
	
	/** 创建时间 */
	private Date				createTime;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** version*/
	private Date				version;
	
	/** 会员名称 */
	private String 				memberName;
	
	/** 退单单号
	 * @since 5.3.2.18
	 * */
    private String              code;
    
    /** 外部编码 对应sku.outId
     * @since 5.3.2.18
     * */
    private String              outId;
    
    /** 商品编码 对应item.code
     * @since 5.3.2.18
     * */
    private String              itemCode;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	
	public Integer getIsReceipt() {
		return isReceipt;
	}

	public void setIsReceipt(Integer isReceipt) {
		this.isReceipt = isReceipt;
	}

	
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	public Long getHandleId() {
		return handleId;
	}

	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	
	
	public String getHandleName() {
		return handleName;
	}

	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/**
     * 获取code :退单单号
     * */
    public String getCode(){
        return code;
    }

    
    public void setCode(String code){
        this.code = code;
    }

    /**
     * 获取outId 对应sku.outId
     * */
    public String getOutId(){
        return outId;
    }

    
    public void setOutId(String outId){
        this.outId = outId;
    }

    /**
     * 获取itemCode 对应Item.code
     * */
    public String getItemCode(){
        return itemCode;
    }

    
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }
	
	
	
}
