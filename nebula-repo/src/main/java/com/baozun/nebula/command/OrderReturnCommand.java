package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;

public class OrderReturnCommand {

	/**
	 * OMS退换货编码
	 */
	private String platformOMSCode;
	
	/**
	 * 申请退款人（指当先登陆的账号）
	 */
	private String refundPayee;
	
	private String soOrderCode; // 平台订单编码
	
	private String productName;//商品名称
	
	private String code;  //商品款号
	/**
	 * 退货单的退款价
	 */
	private BigDecimal returnPrice;
	
	private int qty; // 退换货商品数量
	
	/**
	 * 退换货申请状态<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private int status = -1; //退货状态
	
	private String approver; // 审批人
	
	private Date approveTime; // 审批时间
	
	private Date createTime; // 退换货创建时间
	
	/**
	 * 用户 退货时候填写 
	 * 物流单号,只有当  退货单状态：已发货 时有此值
	 */
	private String transCode;
	
	/**
	 * 退换货申请原因<br>
	 * 7天无理由：R001A<br>
	 * 商品质量问题：R002A<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private String returnReason;
	

	/**
	 * 用户 退货时候填写 
	 * 物流公司名称,只有当  退货单状态：已发货 时有此值
	 */
	private String transName;


	public String getPlatformOMSCode() {
		return platformOMSCode;
	}


	public void setPlatformOMSCode(String platformOMSCode) {
		this.platformOMSCode = platformOMSCode;
	}


	public String getRefundPayee() {
		return refundPayee;
	}


	public void setRefundPayee(String refundPayee) {
		this.refundPayee = refundPayee;
	}


	public String getSoOrderCode() {
		return soOrderCode;
	}


	public void setSoOrderCode(String soOrderCode) {
		this.soOrderCode = soOrderCode;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public BigDecimal getReturnPrice() {
		return returnPrice;
	}


	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}


	public int getQty() {
		return qty;
	}


	public void setQty(int qty) {
		this.qty = qty;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getApprover() {
		return approver;
	}


	public void setApprover(String approver) {
		this.approver = approver;
	}


	public Date getApproveTime() {
		return approveTime;
	}


	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getTransCode() {
		return transCode;
	}


	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}


	public String getReturnReason() {
		return returnReason;
	}


	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}


	public String getTransName() {
		return transName;
	}


	public void setTransName(String transName) {
		this.transName = transName;
	}
	

}
