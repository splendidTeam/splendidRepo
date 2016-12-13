package com.baozun.nebula.payment.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public interface PaymentManager {
	
	/**
	 * 创建支付链接
	 */
	public PaymentRequest createPayment(SalesOrderCommand order);
    
	/**
	 * 同步方式获取返回结果
	 */
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request,String paymentType);
	
	/**
	 * 异步方式获取返回结果
	 */
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request,String paymentType);
	
	/**
	 * 取消订单
	 */
	public PaymentResult cancelPayment(SalesOrderCommand order);
	
	/**
	 * 订单状态查询
	 */
	public PaymentResult getOrderInfo(SalesOrderCommand order);
	
	/**
	 * 创建手机支付链接
	 * @param salesOrderCommand 
	 * 参数取值对应说明（支付宝参数名--实体类属性）:
	 *     out_trade_no -- salesOrderCommand的code
	 *     req_id -- salesOrderCommand的omsCode
	 *     total_fee --salesOrderCommand的total
	 *     out_user --salesOrderCommand的name
	 *     pay_expire --salesOrderCommand中对象OnLinePaymentCommand的ItBPay
	 * @return
	 */
	public PaymentRequest createPaymentForWap(SalesOrderCommand salesOrderCommand);
	
	/**
	 * 同步方式获取返回结果
	 */
	public PaymentResult getPaymentResultForSynOfWap(HttpServletRequest request,String paymentType);
	
	/**
	 * 异步方式获取返回结果
	 */
	public PaymentResult getPaymentResultForAsyOfWap(HttpServletRequest request,String paymentType);
	
	/**
	 * 微信统一下单
	 * @param order
	 * @return
	 */
	public PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType);

	/**
	 * 
	 * @Description SalesOrderCommand.getOnLinePaymentCommand().getPayType()所对应的PaymentFactory.payType
	 * @param additionParams
	 * @param payType 
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-29
	 */
	public PaymentRequest createPayment(Map<String, Object> additionParams, Integer payType);

}
