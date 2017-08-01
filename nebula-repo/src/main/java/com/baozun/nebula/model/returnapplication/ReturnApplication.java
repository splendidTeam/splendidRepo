package com.baozun.nebula.model.returnapplication;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * 退换货申请表<br>
 * @author yaohua.wang@baozun.cn
 */
@Entity
@Table(name = "T_SO_RETURN_APPLICATION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ReturnApplication extends BaseModel{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3411890079966352309L;
    
    /**
     * returnReason属性值域: 退换货申请原因<br>
     */
    /**不想买了*/
    public static final String SO_RETURN_REASON_CHEANGE_MIND="R001A";
    /** 商品质量问题 */
    public static final String SO_RETURN_REASON_DAMAGED_GOOD = "R002A";
    /** 包装破损 */
    public static final String SO_RETURN_REASON_DAMAGED_PACKAGE = "R003A";
    /** 尺码与商品描述不符*/
    public static final String SO_RETURN_REASON_SIZE_UNMATCH = "R004A";
    /** 颜色、图案、款式与商品描述不符*/
    public static final String SO_RETURN_REASON_PRODUCT_UNMATCH = "R005A";
    /** 其他原因*/
    public static final String SO_RETURN_REASON_OTHER_REASON = "R006A";
    

    /**
     * isNeededReturnInvoice属性值域<br>
     * 是否退货发票<br>
     * Y表示需要退回发票:RETURN_APPLICATION_NEEDEDRETURNINVOICE<br>
     * N表示不需要退回发票:RETURN_APPLICATION_NOTNEEDEDRETURNINVOICE(已过期)<br>
     * 不在使用此值域<br>
     * 现在实际情况用户无论选不选需要发票，官网商城都会开据发票，所以现在统一值域为：Y<br>
     */
    /**
     * Y表示需要退回发票:RETURN_APPLICATION_NEEDEDRETURNINVOICE<br>
     */
    public static final String SO_RETURN_NEEDED_RETURNINVOICE = "Y";
    /**
     * N表示不需要退回发票:RETURN_APPLICATION_NOTNEEDEDRETURNINVOICE<br>
     */
    public static final String SO_RETURN_NOTNEEDED_RETURNINVOICE = "N";
    
    /** 
     * type属性值域<br>
     * 退换货申请类型<br>
     */
    /** 退货申请 */
    public static final Integer SO_RETURN_TYPE_RETURN = 1;
    /** 换货申请 */
    public static final Integer SO_RETURN_TYPE_EXCHANGE = 2;
    
    /**
     * status属性值域<br>
     * 退换货申请状态<br>
    */
    /** 待审核  （即新建）*/
    public static final Integer SO_RETURN_STATUS_AUDITING = 1;
    /** 拒绝退货  */
    public static final Integer SO_RETURN_STATUS_REFUS_RETURN = 2;
    /** 退货中（即客服审核通过） */
    public static final Integer SO_RETURN_STATUS_TO_DELIVERY  = 3;
    /** 已发货 （目前该状态没用上，审核通过后的状态都为退货中，知道同意退换货）*/
    public static final Integer SO_RETURN_STATUS_DELIVERIED = 4;
    /** 同意退换货 (指仓库收到客服退回的商品，确认无误后同意退换货)*/
    public static final Integer SO_RETURN_STATUS_AGREE_REFUND = 5;
    /** 已完成 */
    public static final Integer SO_RETURN_STATUS_RETURN_COMPLETE = 6;
    /** 取消退货*/
    public static final Integer SO_RETURN_STATUS_RETURN_CANCEL = 7;
    
    /**
     * refundStatus属性值域<br>
     * 退款状态<br>
     */
    /**
     * 待处理:REFUND_TYPE_WAIT
     */
    public static final Integer SO_RETURN_REFUNDSTATUS_REFUND_TYPE_WAIT = 0;
    /**
     * 同意退款:REFUND_TYPE_HANDLING
     */
    public static final Integer SO_RETURN_REFUNDSTATUS_REFUND_TYPE_HANDLING = 1;
    /**
     * 拒绝退款退换:REFUND_TYPE_SUCCESS
     */
    public static final Integer SO_RETURN_REFUNDSTATUS_REFUND_TYPE_SUCCESS = 2;
    /**
     * 待处理:REFUND_TYPE_WAIT
     */
    public static final Integer SO_RETURN_REFUNDSTATUS_REFUND_TYPE_FAIL = 3;
    
    /**
     * synType属性值域<br>
     * MQ状态<br>
     */
    /**
     * 未发送MQ:IS_NOT_SEND_MQ<br>
     */
    public static final Integer SO_RETURN_SYNTYPE_IS_NOT_SEND_MQ = 0;
    /**
     * 发送MQ:IS_SEND_MQ
     */
    public static final Integer SO_RETURN_SYNTYPE_IS_SEND_MQ = 1;
    /**
     * 发送异常:SEND_MQ_ERROR
     */
    public static final Integer SO_RETURN_SYNTYPE_SEND_MQ_ERROR = 2;
    
    
    /**
     * key : 退换货数字状态 
     * value ： 退换货状态中文描述 
     * */
    public static Map<Integer,String> getStatusDescMap(){
        Map<Integer,String>  map =  new HashMap<Integer,String>();
            map.put(new Integer(SO_RETURN_STATUS_AUDITING), "待审核");
            map.put(new Integer(SO_RETURN_STATUS_REFUS_RETURN), "拒绝退换货");
        map.put(new Integer(SO_RETURN_STATUS_TO_DELIVERY), "退回中");
        map.put(new Integer(SO_RETURN_STATUS_DELIVERIED), "已发货");
        map.put(new Integer(SO_RETURN_STATUS_AGREE_REFUND), "同意退换货");
            map.put(new Integer(SO_RETURN_STATUS_RETURN_COMPLETE), "已完成");
            map.put(new Integer(SO_RETURN_STATUS_RETURN_CANCEL), "取消退货");
        return map ;
    }
    
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
    private Integer type;

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
    private Integer status;

    /**
     * 后端处理状态
     */
    private Integer omsStatus;

    /**
     * 退款状态<br>
     */
    private Integer refundStatus;

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
    @Index(name = "IDX_SO_RETURN_APPLICATION_SO_ORDER_CODE")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_RETURN_APPLICATION_CODE")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_TYPE")
    public Integer getType(){
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * @return the soOrderId
     */
    @Column(name = "SO_ORDER_ID")
    @Index(name = "IDX_SO_RETURN_APPLICATION_SO_ORDER_ID")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_SO_ORDER_LINE_ID")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_SOURCE")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_REFUND_TYPE")
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
    @Column(name = "IS_NEEDED_RETURN_INVOICE")
    @Index(name = "IDX_SO_RETURN_APPLICATION_IS_NEEDED_RETURN_INVOICE")
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
    @Index(name = "IDX_SO_RETURN_APPLICATION_STATUS")
    public Integer getStatus(){
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status){
        this.status = status;
    }

    /**
     * @return the omsStatus
     */
    @Column(name = "OMS_STATUS")
    @Index(name = "IDX_SO_RETURN_APPLICATION_OMS_STATUS")
    public Integer getOmsStatus(){
        return omsStatus;
    }

    /**
     * @param omsStatus
     *            the omsStatus to set
     */
    public void setOmsStatus(Integer omsStatus){
        this.omsStatus = omsStatus;
    }

    /**
     * @return the refundStatus
     */
    @Column(name = "REFUND_STATUS")
    @Index(name = "IDX_SO_RETURN_APPLICATION_REFUND_STATUS")
    public Integer getRefundStatus(){
        return refundStatus;
    }

    /**
     * @param refundStatus
     *            the refundStatus to set
     */
    public void setRefundStatus(Integer refundStatus){
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
    @Column(name = "LAST_MODIFY_USER")
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
    @Column(name = "MEMBER_ID")
    @Index(name = "IDX_SO_RETURN_APPLICATION_MEMBER_ID")
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
