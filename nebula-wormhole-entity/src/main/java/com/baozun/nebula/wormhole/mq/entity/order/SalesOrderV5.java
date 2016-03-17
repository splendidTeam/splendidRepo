package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单头信息
 * @author Justin Hu
 * 订单货款+整单折扣=sum（行商品单价X数量）
 * 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额
 */

public class SalesOrderV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2095927848296104351L;
	
	public final static Integer NORMAL_ORDER = 1;
	public final static Integer PRESALE_ORDER = 2;

	/**
	 * 订单类型 1-NORMAL_ORDER, 2-PRESALE_ORDER 见SalesOrderV5类中常量
	 */
	private Integer orderType;

	 /**
     * 商城订单号
     */
    private String bsOrderCode;

    /**
     * 订单创建时间
     */
    private Date createTime;


    /**
     * 是否需要发票
     */
    private Boolean isNeededInvoice;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票内容
     */
    private String invoiceContent;
    
    
    /**
     * 商品总金额 该金额为整单最终实际货款. (不包含运费且未扣减虚拟货币[实际支付金额])
     * 不含运费的客户端显示最终金额
     */
    private BigDecimal totalActual;

    /**
     * 实际运费
     */
    private BigDecimal acutalTransFee;
    
    
    /**
     * 订单整单的折扣，含基于整单促销形成的折扣和基于行的促销形成的折扣
     */
    private BigDecimal totalDiscount;


    /**
     * 支付产生的折扣：由于预付卡或其他支付方式带来的金额折扣
     */
    private BigDecimal payDiscount;

    
    /**
     * 卖家备注
     */
    private String sellerMemo;
    
    
    /**
     * 买家备注
     */
    private String buyerMemo;
    
    /**
     * 订单明细
     */

    private List<OrderLineV5> orderLines;
    
    /**
     * 订单所享受的促销活动
     */

    private List<OrderPromotionV5> promotions;
    
    
    /**
     * 订单支付明细
     */

    private List<OrderPaymentV5> SoPayments;
    
    
    /**
     * 整单包装信息
     */

    private List<ProductPackageV5> productPackages;
    /**
     * 商城会员信息
     */
    private OrderMemberV5 ordeMember;
    

    /**
     * 收货人信息
     */
    private DeliveryInfoV5 deliveryInfo;
    
    
    /**
     * 备注(相关明细信息拼接成的JSON字符串)
     * 用于放置一些扩展信息
     * 
     */
    private String remark;
    
    

    /**
     * 终端来源 
     * 可选值  :
     * 	PC
     * 	WAP   表示所有移动端(与淘宝保持一致)
     */
    private String terminalSource;
    
    
    

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getTerminalSource() {
		return terminalSource;
	}


	public void setTerminalSource(String terminalSource) {
		this.terminalSource = terminalSource;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getBsOrderCode() {
		return bsOrderCode;
	}


	public void setBsOrderCode(String bsOrderCode) {
		this.bsOrderCode = bsOrderCode;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Boolean getIsNeededInvoice() {
		return isNeededInvoice;
	}


	public void setIsNeededInvoice(Boolean isNeededInvoice) {
		this.isNeededInvoice = isNeededInvoice;
	}


	public String getInvoiceTitle() {
		return invoiceTitle;
	}


	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}


	public String getInvoiceContent() {
		return invoiceContent;
	}


	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}


	public BigDecimal getTotalActual() {
		return totalActual;
	}


	public void setTotalActual(BigDecimal totalActual) {
		this.totalActual = totalActual;
	}


	public BigDecimal getAcutalTransFee() {
		return acutalTransFee;
	}


	public void setAcutalTransFee(BigDecimal acutalTransFee) {
		this.acutalTransFee = acutalTransFee;
	}


	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}


	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}


	public BigDecimal getPayDiscount() {
		return payDiscount;
	}


	public void setPayDiscount(BigDecimal payDiscount) {
		this.payDiscount = payDiscount;
	}


	public List<OrderLineV5> getOrderLines() {
		return orderLines;
	}


	public void setOrderLines(List<OrderLineV5> orderLines) {
		this.orderLines = orderLines;
	}


	public List<OrderPromotionV5> getPromotions() {
		return promotions;
	}


	public void setPromotions(List<OrderPromotionV5> promotions) {
		this.promotions = promotions;
	}


	public List<OrderPaymentV5> getSoPayments() {
		return SoPayments;
	}


	public void setSoPayments(List<OrderPaymentV5> soPayments) {
		SoPayments = soPayments;
	}


	public OrderMemberV5 getOrdeMember() {
		return ordeMember;
	}


	public void setOrdeMember(OrderMemberV5 ordeMember) {
		this.ordeMember = ordeMember;
	}


	public DeliveryInfoV5 getDeliveryInfo() {
		return deliveryInfo;
	}


	public void setDeliveryInfo(DeliveryInfoV5 deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}


	public String getSellerMemo() {
		return sellerMemo;
	}


	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}


	public String getBuyerMemo() {
		return buyerMemo;
	}


	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}


	public List<ProductPackageV5> getProductPackages() {
		return productPackages;
	}


	public void setProductPackages(List<ProductPackageV5> productPackages) {
		this.productPackages = productPackages;
	}


    
    

}
