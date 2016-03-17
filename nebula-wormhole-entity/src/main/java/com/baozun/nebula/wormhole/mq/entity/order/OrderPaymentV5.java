package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单支付明细
 * @author Justin Hu
 *
 */

public class OrderPaymentV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3757974268678637311L;

    /**
     * 付款方式
     */
    private String paymentType;
    
    /**
     * COD付款方式
     * 1 表示“现金支付”
     * 2 表示“POS机刷卡”
     */
    private Integer codPaymentType;
    
    /**
     * 实际支付金额
     */
    private BigDecimal payActual;
    
    /**
     * 附加价值说明：对于某些支付方式，可以使用此信息记录额外内容，如外部积分可以使用此信息来记录积分分值，预付卡可以用此来记录实际价值
     */
    private BigDecimal additionalWorth;
    

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getCodPaymentType() {
		return codPaymentType;
	}

	public void setCodPaymentType(Integer codPaymentType) {
		this.codPaymentType = codPaymentType;
	}

	public BigDecimal getPayActual() {
		return payActual;
	}

	public void setPayActual(BigDecimal payActual) {
		this.payActual = payActual;
	}

	public BigDecimal getAdditionalWorth() {
		return additionalWorth;
	}

	public void setAdditionalWorth(BigDecimal additionalWorth) {
		this.additionalWorth = additionalWorth;
	}
        
    
}
