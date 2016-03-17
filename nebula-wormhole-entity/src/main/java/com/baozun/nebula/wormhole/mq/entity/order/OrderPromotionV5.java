package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单促销信息
 * @author Justin Hu
 * 对于订单行关联的促销，sum（金额）= 行上的折扣
 * 对于订单头关联的促销，sum（金额）=  由于整单促销/商城积分形成的未分摊到行上的折扣总额 = 整单折扣-sum（行折扣）
 * 所以要注意的是，商城积分需要增加一行基于订单头关联的促销，促销编码为空，类型为商城积分抵扣
 */

public class OrderPromotionV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5286125580333954929L;

	
    /**
     * 促销类型,并不是商城端所理解的满500减100这种促销类型
     * 而是指整单，单行的类型
     * 1为整单促销
     * 2为单行促销
     * 3为整单拆分到行的促销
     */
    private String type;
    
    /**
     * 促销编码 该字段目前在oms中无需备案,oms中仅记录该信息
     * 商城定义的活动编号
     */
    private String code;
    
    /**
     * 活动描述
     * 商城定义的活动名称/描述
     */
    private String description;
    
    /**
     * 优惠金额
     * 由于促销带来的折扣金额（对赠品类促销此数值为0）
     */
    private BigDecimal discountFee;
    
    
    /**
     * 优惠券代码
     */
    private String couponCode;
    
    
    /**
     * 备注
     * json对象 ,当前这里有可能是isNeedPrint用于在coach
     */
    private String remark;


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public BigDecimal getDiscountFee() {
		return discountFee;
	}


	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}


	public String getCouponCode() {
		return couponCode;
	}


	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
    

}
