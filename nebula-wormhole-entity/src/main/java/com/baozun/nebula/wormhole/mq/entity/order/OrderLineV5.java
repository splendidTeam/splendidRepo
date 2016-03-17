package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 订单行数据
 * @author Justin Hu
 *
 */
public class OrderLineV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4301848445621072195L;

	
	/**
	 * 商城的订单行id
	 */
	private Long bsOrderLineId;
	
	 /**
     * oms与bs SKU匹配唯一标识
     */
    private String extentionCode;
    
    /**
     * 商城订单行商品名称(可以为空)
     */
    private String bsSkuName;
    
    /**
     * 数量
     */
    private Integer qty;
    
    /**
     * 吊牌价,参考数据，可以为空
     */
    private BigDecimal listPrice;
    
    /**
     * 销售价(折前单价)
     */
    private BigDecimal unitPrice;
    
    /**
     * 行总计 (扣减所有活动优惠且未扣减积分抵扣) sum(sl.totalActual)=so.totalActual totalActual + discountFee =
     * unitPrice × qty
     * 
     * 最终货款=销售价X数量-折扣
     */
    private BigDecimal totalActual;
    
    /**
     * 行优惠总金额(不包含积分抵扣)
     */
    private BigDecimal discountFee;
    
    /**
     * 是否为赠品
     */
    private Boolean isPrezzie;
    
    /**
     * 保修时长(按月计)
     */
    private Integer warrantyMonths;
    
    /**
     * 订单行所享受的促销活动
     */

    private List<OrderPromotionV5> promotions;
    
    
    /**
     * 订单行包装信息
     */

    private List<ProductPackageV5> productPackages;
    

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	public String getBsSkuName() {
		return bsSkuName;
	}

	public void setBsSkuName(String bsSkuName) {
		this.bsSkuName = bsSkuName;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalActual() {
		return totalActual;
	}

	public void setTotalActual(BigDecimal totalActual) {
		this.totalActual = totalActual;
	}

	public BigDecimal getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}

	public Boolean getIsPrezzie() {
		return isPrezzie;
	}

	public void setIsPrezzie(Boolean isPrezzie) {
		this.isPrezzie = isPrezzie;
	}

	public List<OrderPromotionV5> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<OrderPromotionV5> promotions) {
		this.promotions = promotions;
	}

	public List<ProductPackageV5> getProductPackages() {
		return productPackages;
	}

	public void setProductPackages(List<ProductPackageV5> productPackages) {
		this.productPackages = productPackages;
	}

	public Long getBsOrderLineId() {
		return bsOrderLineId;
	}

	public void setBsOrderLineId(Long bsOrderLineId) {
		this.bsOrderLineId = bsOrderLineId;
	}

	public Integer getWarrantyMonths() {
		return warrantyMonths;
	}

	public void setWarrantyMonths(Integer warrantyMonths) {
		this.warrantyMonths = warrantyMonths;
	}
    
    
    
}
