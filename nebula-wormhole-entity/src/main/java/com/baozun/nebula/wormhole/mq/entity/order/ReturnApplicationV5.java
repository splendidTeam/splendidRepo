package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退换货申请信息
 * @author yimin.qiao
 * @createtime 2016年1月13日 下午4:54:19
 */
public class ReturnApplicationV5 implements Serializable {

	private static final long serialVersionUID = -3097945720176284531L;

	/** 商城退换货申请单号 */
	private String bsRaCode;
	
	/** 商城订单号 */
	private String bsSalesOrderCode;
	
	/** 商城退换货单创建时间 */
	private Date bsRaCreateTime;
	
	/** 退换货申请类型 */
	private Integer type;
	
	/** 退款方式 */
	private String refundType;
	
	/** 申请原因 */
	private String returnReason;
	
	/** 退款账户 */
	private String refundAccount;
	
	/** 退款收款人 */
	private String refundPayee;
	
	/** 退款银行 */
	private String refundBank;
	
	/** 是否需要退回发票 */
	private String isNeededReturnInvoice;
	
	/** 退货来源 */
	private String source;
	
	/** 备注 */
	private String remark;
	
	/** 退换货申请明细行信息 */
	private List<ReturnApplicationLineV5> returnApplicationLines;
	
	/** 换货申请配送信息 */
	private ReturnApplicationDeliveryInfoV5 returnApplicationDeliveryInfo;

	public String getBsRaCode() {
		return bsRaCode;
	}

	public void setBsRaCode(String bsRaCode) {
		this.bsRaCode = bsRaCode;
	}

	public String getBsSalesOrderCode() {
		return bsSalesOrderCode;
	}

	public void setBsSalesOrderCode(String bsSalesOrderCode) {
		this.bsSalesOrderCode = bsSalesOrderCode;
	}

	public Date getBsRaCreateTime() {
		return bsRaCreateTime;
	}

	public void setBsRaCreateTime(Date bsRaCreateTime) {
		this.bsRaCreateTime = bsRaCreateTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundPayee() {
		return refundPayee;
	}

	public void setRefundPayee(String refundPayee) {
		this.refundPayee = refundPayee;
	}

	public String getRefundBank() {
		return refundBank;
	}

	public void setRefundBank(String refundBank) {
		this.refundBank = refundBank;
	}

	public String getIsNeededReturnInvoice() {
		return isNeededReturnInvoice;
	}

	public void setIsNeededReturnInvoice(String isNeededReturnInvoice) {
		this.isNeededReturnInvoice = isNeededReturnInvoice;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ReturnApplicationLineV5> getReturnApplicationLines() {
		return returnApplicationLines;
	}

	public void setReturnApplicationLines(
			List<ReturnApplicationLineV5> returnApplicationLines) {
		this.returnApplicationLines = returnApplicationLines;
	}

	public ReturnApplicationDeliveryInfoV5 getReturnApplicationDeliveryInfo() {
		return returnApplicationDeliveryInfo;
	}

	public void setReturnApplicationDeliveryInfo(
			ReturnApplicationDeliveryInfoV5 returnApplicationDeliveryInfo) {
		this.returnApplicationDeliveryInfo = returnApplicationDeliveryInfo;
	}
	
}
