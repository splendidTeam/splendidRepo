package com.baozun.nebula.payment.manager;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.command.BeforePaymentCancelOrderCommand;
import com.baozun.nebula.command.wechat.WechatJsApiConfigCommand;
import com.baozun.nebula.command.wechat.WechatJsApiPayCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public interface PayManager {
	
	/**
	 * 保存支付信息
	 * @param so 订单信息
	 * @param paymentRequest 
	 * @param operator 当前登录用户
	 */
	void savePayInfos(SalesOrderCommand so,PaymentRequest paymentRequest,String operator);

	/**
	 *
	 * 更新支付信息
	 * @param paymentResult
	 * @param operator 当前登录用户
	 * @param payType 支付类型
	 * @param flag 是否为同步获取通知
	 */
	void updatePayInfos(PaymentResult paymentResult,String operator,Integer payType,boolean flag,HttpServletRequest request);
	
	/**
	 * 记录回调或关闭交易请求结果日志
	 */
	void savePaymentResultPaymentLog(PaymentResult paymentResult,String operator,Integer type);

	/**
	 * 是否支持关闭支付
	 */
	boolean isSupportClosePaymentRequest();

	/**
	 * 此方法不再建议使用,请使用:closeTradeUpdateOrderStatus(Long orderId,PaymentResult paymentResult,
	 * 			String operator,String orderNo,String userType,String payCode,Boolean isOms)关系到库存释放
	 * (1)更新订单状态
	 * (2)如果使用了优惠券则退回优惠券
	 * 关闭交易成功后的处理
	 * @param paymentResult 支付宝返回的结果
	 * @param operator 取消订单的操作用户
	 * @param orderNo orderNo取消的订单号(t_so_salesorder表中的code)
	 * @param userType  userType buy表示用户取消 seller表示商城取消
	 * @param payCode 支付订单号(t_so_payinfo表中的sub_ordinate)
	 */
	public void closeTradeUpdateOrderStatus(Long orderId,PaymentResult paymentResult,String operator,String orderNo,String userType,String payCode);
	
	
	/**
	 * 
	 * 多了个是否oms取消，若oms：将不释放库存
	 * @param orderId
	 * @param paymentResult
	 * @param operator
	 * @param orderNo
	 * @param userType
	 * @param payCode
	 * @param isOms
	 */
	public void closeTradeUpdateOrderStatus(Long orderId,PaymentResult paymentResult,String operator,String orderNo,String userType,String payCode,Boolean isOms);
	
	
	
	/**
	 * 记录获取支付链接请求结果日志
	 */
	public void savePaymentRequestPaymentLog(PaymentRequest paymentRequest,String operator);
	
	
	/**
	 * 此方法不再建议使用,请使用:cancelOrderUpdateInfo(Long orderId,String code,Integer logisticsStatus,Boolean isOms)
	 * (1)更新订单状态
	 * (2)如果使用了优惠券则退回优惠券
	 * (3)增加库存
	 * @param orderId
	 * @param code
	 * @param financialStatus
	 */
	public void cancalOrderUpdateInfo(Long orderId,String code,Integer logisticsStatus);
	
	
	/**
	 * 多了个是否oms取消，若oms：将不释放库存
	 * @param orderId
	 * @param code
	 * @param logisticsStatus
	 * @param isOms
	 */
	public void cancelOrderUpdateInfo(Long orderId,String code,Integer logisticsStatus,Boolean isOms);
	
	
	/**
	 * 付款之前取消订单
	 * @param beforePaymentCancelOrderCommand
	 */
	public void cancelOrder(BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand);
	
	/**
	 * 微信支付配置
	 */
	public WechatJsApiConfigCommand getWechatJsApiConfigCommand(String url,String jsapi_ticket);
	/**
	 * 微信支付
	 */
	public WechatJsApiPayCommand getWechatJsApiPayCommand(String prepayId);
}
