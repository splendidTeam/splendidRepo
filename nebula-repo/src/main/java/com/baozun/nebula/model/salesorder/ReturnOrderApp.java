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
 * 申请退换货
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "T_SO_RETURNORDERAPP")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ReturnOrderApp extends BaseModel{

	private static final long serialVersionUID = -4145871224446205724L;

	/** oms反馈结果 **/
	public static final Integer APPLYRETURN_NEW = 1;
	public static final Integer FEEDBACK_AGREE = 2;
	public static final Integer FEEDBACK_NOT_AGREE = 3;
	
	/** 服务类型 **/
	public static final Integer SERVICE_RETURN = 10;
	public static final Integer SERVICE_CHANGE = 11;

	/** 是否有发票 **/
	public static final Integer HAS_RECEIPT = 1;
	public static final Integer HAS_NOT_RECEIPT = 0;
	
	/** PK. */
	private Long				id;
	
	/** 订单号 */
	private String				orderCode;
	
	/** 订单行id */
	private Long				orderLineId;

	/** 会员id */
	private Long				memberId;
	
	/** 会员姓名 */
	private String				memberName;
	
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
	
	

    @Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_RETURNORDERAPP",sequenceName = "S_T_SAL_RETURNORDERAPP",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_RETURNORDERAPP")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "ORDER_CODE")
    @Index(name = "IDX_RETURNORDERAPP_ORDER_CODE")
	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	@Column(name = "ORDER_LINE_ID")
    @Index(name = "IDX_RETURNORDERAPP_ORDER_LINE_ID")
	public Long getOrderLineId() {
		return orderLineId;
	}


	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	@Column(name = "MEMBER_ID")
    @Index(name = "IDX_RETURNORDERAPP_MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	
	@Column(name = "MEMBER_NAME")
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "SERVICE_TYPE")
	public Integer getServiceType() {
		return serviceType;
	}


	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	@Column(name = "DESCRIBE",length=500)
	public String getDescribe() {
		return describe;
	}


	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Column(name = "FEEDBACK",length=500)
	public String getFeedback() {
		return feedback;
	}


	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	@Column(name = "IS_RECEIPT")
	public Integer getIsReceipt() {
		return isReceipt;
	}


	public void setIsReceipt(Integer isReceipt) {
		this.isReceipt = isReceipt;
	}

	@Column(name = "PIC",length=200)
	public String getPic() {
		return pic;
	}


	public void setPic(String pic) {
		this.pic = pic;
	}

	@Column(name = "NAME",length=100)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ADDRESS",length=500)
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "MOBILE",length=100)
	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "STATUS")
    @Index(name = "IDX_RETURNORDERAPP_STATUS")
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "HANDLE_ID")
    @Index(name = "IDX_RETURNORDERAPP_HANDLE_ID")
	public Long getHandleId() {
		return handleId;
	}


	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	@Column(name = "HANDLE_NAME",length=100)
	public String getHandleName() {
		return handleName;
	}


	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	/**
     * 获取code :退单单号
     * @since 5.3.2.18
     * */
    @Column(name = "CODE")
    @Index(name = "IDX_RETURNORDERAPP_CODE")
    public String getCode(){
        return code;
    }


    /**
     * @since 5.3.2.18
     * */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * 获取outId 对应sku.outId
     * @since 5.3.2.18
     * */
    @Column(name = "OUT_ID")
    public String getOutId(){
        return outId;
    }


    /**
     * @since 5.3.2.18
     * */
    public void setOutId(String outId){
        this.outId = outId;
    }

    /**
     * 获取itemCode 对应Item.code
     * @since 5.3.2.18
     * */
    @Column(name = "ITEM_CODE")
    public String getItemCode(){
        return itemCode;
    }


    /**
     * @since 5.3.2.18
     * */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }


	
}
