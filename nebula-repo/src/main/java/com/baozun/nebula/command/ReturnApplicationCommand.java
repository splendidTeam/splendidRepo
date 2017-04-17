package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;

public class ReturnApplicationCommand {

	/**
     * PK
     */
    private Long id;

    /**
     * 平台订单编码
     */
    private String soOrderCode;

    /**
     * 退换货申请编码
     */
    private String returnApplicationCode;

    /**
     * 退换货申请类型 <br>
     */
    private int type;

    /**
     * 平台订单ID
     */
    private Long soOrderId;

    /**
     * 平台订单行ID
     */
    private Long soOrderLineId;

    /**
     * 退换货创建时间
     */
    private Date createTime;

    /**
     * Version
     */
    private Date version;

    /**
     * 退货来源.
     */
    private String source;

    /**
     * 退款方式 <br>
     */
    private String refundType;

    /**
     * 
     * /**
     * 退换货申请原因<br>
     */
    private String returnReason;

    /**
     * 退款账户
     */
    private String refundAccount;

    /**
     * 退款银行总称
     */
    private String refundBank;

    /**
     * 退款银行的开户所属的支行
     */
    private String refundBankBranch;

    /**
     * 是否退货发票<br>
     */
    private String isNeededReturnInvoice;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 退换货单审批说明<br>
     * 无论审批通过，或者不通过<br>
     * 都可以填写，也可以为null
     */
    private String approvalDescription;

    /**
     * 退换货申请状态<br>
     */
    private int status;

    /**
     * 后端处理状态
     */
    private int omsStatus;

    /**
     * 退款状态<br>
     */
    private int refundStatus;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 最后修改人（系统用户）
     */
    private String lastModifyUser;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * OMS退换货编码
     */
    private String omsCode;

    /**
     * 退货单的退款价
     */
    private BigDecimal returnPrice;

    /**
     * 用户退货时候填写
     * 物流单号,只有当退货单状态：已发货 时有此值
     */
    private String transCode;

    /**
     * 用户退货时候填写
     * 物流公司名称,只有当退货单状态：已发货 时有此值
     */
    private String transName;

    /**
     * 退货地址
     */
    private String returnAddress;

    /**
     * 退回运费
     */
    private BigDecimal returnFreight;

	
	private List<SoReturnLine> returnLineList;
	
	private SoReturnApplicationDeliveryInfo soReturnApplicationDeliveryInfo;

	public SoReturnApplicationDeliveryInfo getSoReturnApplicationDeliveryInfo() {
		return soReturnApplicationDeliveryInfo;
	}


	public void setSoReturnApplicationDeliveryInfo(
			SoReturnApplicationDeliveryInfo soReturnApplicationDeliveryInfo) {
		this.soReturnApplicationDeliveryInfo = soReturnApplicationDeliveryInfo;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSoOrderCode() {
		return soOrderCode;
	}


	public void setSoOrderCode(String soOrderCode) {
		this.soOrderCode = soOrderCode;
	}


	public String getReturnApplicationCode() {
		return returnApplicationCode;
	}


	public void setReturnApplicationCode(String returnApplicationCode) {
		this.returnApplicationCode = returnApplicationCode;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public Long getSoOrderId() {
		return soOrderId;
	}


	public void setSoOrderId(Long soOrderId) {
		this.soOrderId = soOrderId;
	}


	public Long getSoOrderLineId() {
		return soOrderLineId;
	}


	public void setSoOrderLineId(Long soOrderLineId) {
		this.soOrderLineId = soOrderLineId;
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


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
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


	public String getRefundBank() {
		return refundBank;
	}


	public void setRefundBank(String refundBank) {
		this.refundBank = refundBank;
	}


	public String getRefundBankBranch() {
		return refundBankBranch;
	}


	public void setRefundBankBranch(String refundBankBranch) {
		this.refundBankBranch = refundBankBranch;
	}


	public String getIsNeededReturnInvoice() {
		return isNeededReturnInvoice;
	}


	public void setIsNeededReturnInvoice(String isNeededReturnInvoice) {
		this.isNeededReturnInvoice = isNeededReturnInvoice;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getApprovalDescription() {
		return approvalDescription;
	}


	public void setApprovalDescription(String approvalDescription) {
		this.approvalDescription = approvalDescription;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getOmsStatus() {
		return omsStatus;
	}


	public void setOmsStatus(int omsStatus) {
		this.omsStatus = omsStatus;
	}


	public int getRefundStatus() {
		return refundStatus;
	}


	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
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


	public String getLastModifyUser() {
		return lastModifyUser;
	}


	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}


	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}


	public String getOmsCode() {
		return omsCode;
	}


	public void setOmsCode(String omsCode) {
		this.omsCode = omsCode;
	}


	public BigDecimal getReturnPrice() {
		return returnPrice;
	}


	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}


	public String getTransCode() {
		return transCode;
	}


	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}


	public String getTransName() {
		return transName;
	}


	public void setTransName(String transName) {
		this.transName = transName;
	}


	public String getReturnAddress() {
		return returnAddress;
	}


	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}


	public BigDecimal getReturnFreight() {
		return returnFreight;
	}


	public void setReturnFreight(BigDecimal returnFreight) {
		this.returnFreight = returnFreight;
	}


	public List<SoReturnLine> getReturnLineList() {
		return returnLineList;
	}


	public void setReturnLineList(List<SoReturnLine> returnLineList) {
		this.returnLineList = returnLineList;
	}

	
	
}
