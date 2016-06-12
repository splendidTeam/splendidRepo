package com.baozun.nebula.utilities.common.command;

import java.math.BigDecimal;

/**
 * 标准值适配器
 * @author jumbo
 *
 */
public interface BasePayParamCommandAdaptor {

	/**
	 * 银行简码
	 */
	public String getDefault_bank();

	/**
	 * 订单号
	 */
	public String getOrderNo();

	/**
	 * 总金额
	 */
	public BigDecimal getTotalFee();

	/**
	 * 是否是国际卡
	 */
	public boolean isInternationalCard();

	/**
	 * 商品描述
	 */
	public String getBody();

	/**
	 * 取消订单角色
	 */
	public String getTrade_role();

	/**
	 * 交易号
	 */
	public String getTrade_no();

	/**
	 * 支付支付类型
	 */
	public String getPaymentType();
	
	/**
	 * 交易时间
	 */
	public String getPaymentTime();
	
	/**
	 * 持卡人真实IP地址
	 */
	public String getCustomerIp();
	
	/**
	 * 过期时间
	 */
	public String getIt_b_pay();
	
	/**
	 * 扫码支付方式
	 */
	public String getQrPayMode();

}
