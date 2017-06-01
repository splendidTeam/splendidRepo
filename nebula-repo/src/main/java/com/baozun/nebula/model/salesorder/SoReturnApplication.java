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
 * 退换货申请表<br>
 * @author yaohua.wang@baozun.cn
 */
@Entity
@Table(name = "T_SO_RETURN_APPLICATION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SoReturnApplication extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3411890079966352309L;

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

    /**
     * @return the id
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SO_RETURN_APPLICATION",sequenceName = "S_T_SO_RETURN_APPLICATION",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_RETURN_APPLICATION")
    public Long getId(){
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the soOrderCode
     */
    @Column(name = "SO_ORDER_CODE")
    public String getSoOrderCode(){
        return soOrderCode;
    }

    /**
     * @param soOrderCode
     *            the soOrderCode to set
     */
    public void setSoOrderCode(String soOrderCode){
        this.soOrderCode = soOrderCode;
    }

    /**
     * @return the returnApplicationCode
     */
    @Column(name = "RETURN_APPLICATION_CODE")
    public String getReturnApplicationCode(){
        return returnApplicationCode;
    }

    /**
     * @param returnApplicationCode
     *            the returnApplicationCode to set
     */
    public void setReturnApplicationCode(String returnApplicationCode){
        this.returnApplicationCode = returnApplicationCode;
    }

    /**
     * @return the type
     */
    @Column(name = "TYPE")
    public int getType(){
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type){
        this.type = type;
    }

    /**
     * @return the soOrderId
     */
    @Column(name = "SO_ORDER_ID")
    public Long getSoOrderId(){
        return soOrderId;
    }

    /**
     * @param soOrderId
     *            the soOrderId to set
     */
    public void setSoOrderId(Long soOrderId){
        this.soOrderId = soOrderId;
    }

    /**
     * @return the soOrderLineId
     */
    @Column(name = "SO_ORDER_LINE_ID")
    public Long getSoOrderLineId(){
        return soOrderLineId;
    }

    /**
     * @param soOrderLineId
     *            the soOrderLineId to set
     */
    public void setSoOrderLineId(Long soOrderLineId){
        this.soOrderLineId = soOrderLineId;
    }

    /**
     * @return the createTime
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * @return the version
     */
    @Column(name = "VERSION")
    public Date getVersion(){
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Date version){
        this.version = version;
    }

    /**
     * @return the source
     */
    @Column(name = "SOURCE")
    public String getSource(){
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source){
        this.source = source;
    }

    /**
     * @return the refundType
     */
    @Column(name = "REFUND_TYPE")
    public String getRefundType(){
        return refundType;
    }

    /**
     * @param refundType
     *            the refundType to set
     */
    public void setRefundType(String refundType){
        this.refundType = refundType;
    }

    /**
     * @return the returnReason
     */
    @Column(name = "RETURN_REASON")
    public String getReturnReason(){
        return returnReason;
    }

    /**
     * @param returnReason
     *            the returnReason to set
     */
    public void setReturnReason(String returnReason){
        this.returnReason = returnReason;
    }

    /**
     * @return the refundAccount
     */
    @Column(name = "REFUND_ACCOUNT")
    public String getRefundAccount(){
        return refundAccount;
    }

    /**
     * @param refundAccount
     *            the refundAccount to set
     */
    public void setRefundAccount(String refundAccount){
        this.refundAccount = refundAccount;
    }

    /**
     * @return the refundBank
     */
    @Column(name = "REFUND_BANK")
    public String getRefundBank(){
        return refundBank;
    }

    /**
     * @param refundBank
     *            the refundBank to set
     */
    public void setRefundBank(String refundBank){
        this.refundBank = refundBank;
    }

    /**
     * @return the isNeededReturnInvoice
     */
    @Column(name = "ISNEEDEDRETURNINVOICE")
    public String getIsNeededReturnInvoice(){
        return isNeededReturnInvoice;
    }

    /**
     * @param isNeededReturnInvoice
     *            the isNeededReturnInvoice to set
     */
    public void setIsNeededReturnInvoice(String isNeededReturnInvoice){
        this.isNeededReturnInvoice = isNeededReturnInvoice;
    }

    /**
     * @return the remark
     */
    @Column(name = "REMARK")
    public String getRemark(){
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark){
        this.remark = remark;
    }

    /**
     * @return the approvalDescription
     */
    @Column(name = "APPROVAL_DESCRIPTION")
    public String getApprovalDescription(){
        return approvalDescription;
    }

    /**
     * @param approvalDescription
     *            the approvalDescription to set
     */
    public void setApprovalDescription(String approvalDescription){
        this.approvalDescription = approvalDescription;
    }

    /**
     * @return the status
     */
    @Column(name = "STATUS")
    public int getStatus(){
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status){
        this.status = status;
    }

    /**
     * @return the omsStatus
     */
    @Column(name = "OMS_STATUS")
    public int getOmsStatus(){
        return omsStatus;
    }

    /**
     * @param omsStatus
     *            the omsStatus to set
     */
    public void setOmsStatus(int omsStatus){
        this.omsStatus = omsStatus;
    }

    /**
     * @return the refundStatus
     */
    @Column(name = "REFUND_STATUS")
    public int getRefundStatus(){
        return refundStatus;
    }

    /**
     * @param refundStatus
     *            the refundStatus to set
     */
    public void setRefundStatus(int refundStatus){
        this.refundStatus = refundStatus;
    }

    /**
     * @return the approver
     */
    @Column(name = "APPROVER")
    public String getApprover(){
        return approver;
    }

    /**
     * @param approver
     *            the approver to set
     */
    public void setApprover(String approver){
        this.approver = approver;
    }

    /**
     * @return the approveTime
     */
    @Column(name = "APPROVETIME")
    public Date getApproveTime(){
        return approveTime;
    }

    /**
     * @param approveTime
     *            the approveTime to set
     */
    public void setApproveTime(Date approveTime){
        this.approveTime = approveTime;
    }

    /**
     * @return the lastModifyUser
     */
    @Column(name = "LASTMODIFYUSER")
    public String getLastModifyUser(){
        return lastModifyUser;
    }

    /**
     * @param lastModifyUser
     *            the lastModifyUser to set
     */
    public void setLastModifyUser(String lastModifyUser){
        this.lastModifyUser = lastModifyUser;
    }

    /**
     * @return the memberId
     */
    @Column(name = "MEMBERID")
    public Long getMemberId(){
        return memberId;
    }

    /**
     * @param memberId
     *            the memberId to set
     */
    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    /**
     * @return the returnPrice
     */
    @Column(name = "RETURN_PRICE")
    public BigDecimal getReturnPrice(){
        return returnPrice;
    }

    /**
     * @param returnPrice
     *            the returnPrice to set
     */
    public void setReturnPrice(BigDecimal returnPrice){
        this.returnPrice = returnPrice;
    }

    /**
     * @return the transCode
     */
    @Column(name = "TRANS_CODE")
    public String getTransCode(){
        return transCode;
    }

    /**
     * @param transCode
     *            the transCode to set
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }

    /**
     * @return the transName
     */
    @Column(name = "TRANS_NAME")
    public String getTransName(){
        return transName;
    }

    /**
     * @param transName
     *            the transName to set
     */
    public void setTransName(String transName){
        this.transName = transName;
    }

    /**
     * @return the returnAddress
     */
    @Column(name = "RETURN_ADDRESS")
    public String getReturnAddress(){
        return returnAddress;
    }

    /**
     * @param returnAddress
     *            the returnAddress to set
     */
    public void setReturnAddress(String returnAddress){
        this.returnAddress = returnAddress;
    }

    /**
     * @return the returnFreight
     */
    @Column(name = "RETURN_FREIGHT")
    public BigDecimal getReturnFreight(){
        return returnFreight;
    }

    /**
     * @param returnFreight
     *            the returnFreight to set
     */
    public void setReturnFreight(BigDecimal returnFreight){
        this.returnFreight = returnFreight;
    }

    /**
     * @return the refundBankBranch
     */
    @Column(name = "REFUND_BANKBRANCH")
    public String getRefundBankBranch(){
        return refundBankBranch;
    }

    /**
     * @param refundBankBranch
     *            the refundBankBranch to set
     */
    public void setRefundBankBranch(String refundBankBranch){
        this.refundBankBranch = refundBankBranch;
    }

    /**
     * @return the omsCode
     */
    @Column(name = "OMS_CODE")
    public String getOmsCode(){
        return omsCode;
    }

    /**
     * @param omsCode
     *            the omsCode to set
     */
    public void setOmsCode(String omsCode){
        this.omsCode = omsCode;
    }

}
