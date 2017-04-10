package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.salesorder.SoReturnLine;

public class ReturnApplicationCommand {

	private Long id; // 主键
	private String returnOrderCode; // 退换货申请编码(VR+0000001开始,V为平台特别代码)

	/**
	 * 退换货申请类型 <br>
	 * 退货：1<br>
	 * 换货：2<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private int type;

	private String soOrderCode; // 平台订单编码
	private Long soOrderId; // 平台订单ID

	/**
	 * 退款方式 <br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private String refundType;

	/**
	 * 退换货申请原因<br>
	 * 7天无理由：R001A<br>
	 * 商品质量问题：R002A<br>
	 * 值域参见SoReturnApplicationConstants
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
	private String refundAccountBank;

	/**
	 * 申请退款人（指当先登陆的账号）
	 */
	private String refundPayee;

	/**
	 * 是否退货发票<br>
	 * Y表示需要退回发票<br>
	 * N表示不需要退回发票<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private String isNeededReturnInvoice;

	private String memo; // 用户备注

	/**
	 * 退换货单审批说明<br>
	 * 无论审批通过，或者不通过<br>
	 * 都可以填写，也可以为null
	 */
	private String approvalDescription;

	/**
	 * 退换货申请状态<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private int status = -1;

	private int omsStatus; // oms处理状态

	/**
	 * 退款状态<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private int refundStatus;

	/**
	 * MQ同步状态<br>
	 * 值域参见SoReturnApplicationConstants
	 */
	private int synType;

	private String approver; // 审批人
	private Date approveTime; // 审批时间
	private String lastModifyUser;// 最后修改人
	private Date createTime; // 退换货创建时间
	private Date version; // Version

	private Long memberId; // 会员id
	private String accountName; // 账号名称

	/**
	 * OMS退换货编码
	 */
	private String platformOMSCode;

	/**
	 * 退货单的退款价
	 */
	private BigDecimal returnPrice;

	/**
	 * 用户 退货时候填写 
	 * 物流单号,只有当  退货单状态：已发货 时有此值
	 */
	private String transCode;

	/**
	 * 用户 退货时候填写 
	 * 物流公司名称,只有当  退货单状态：已发货 时有此值
	 */
	private String transName;
	
	/**
	 * 退货地址
	 */
	private String returnAddress;

	/**
	 * 退货单退回运费
	 */
	private BigDecimal returnFreight;
	
	private List<SoReturnLine> returnLineList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReturnOrderCode() {
		return returnOrderCode;
	}

	public void setReturnOrderCode(String returnOrderCode) {
		this.returnOrderCode = returnOrderCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSoOrderCode() {
		return soOrderCode;
	}

	public void setSoOrderCode(String soOrderCode) {
		this.soOrderCode = soOrderCode;
	}

	public Long getSoOrderId() {
		return soOrderId;
	}

	public void setSoOrderId(Long soOrderId) {
		this.soOrderId = soOrderId;
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

	public String getRefundAccountBank() {
		return refundAccountBank;
	}

	public void setRefundAccountBank(String refundAccountBank) {
		this.refundAccountBank = refundAccountBank;
	}

	public String getRefundPayee() {
		return refundPayee;
	}

	public void setRefundPayee(String refundPayee) {
		this.refundPayee = refundPayee;
	}

	public String getIsNeededReturnInvoice() {
		return isNeededReturnInvoice;
	}

	public void setIsNeededReturnInvoice(String isNeededReturnInvoice) {
		this.isNeededReturnInvoice = isNeededReturnInvoice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public int getSynType() {
		return synType;
	}

	public void setSynType(int synType) {
		this.synType = synType;
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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPlatformOMSCode() {
		return platformOMSCode;
	}

	public void setPlatformOMSCode(String platformOMSCode) {
		this.platformOMSCode = platformOMSCode;
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
