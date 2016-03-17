/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.integration.payment;

/**
 * 支付服务状态
 */
public enum PaymentServiceStatus{
	/**
	 * 未定义，当状态不明时使用该状态
	 */
	UNDEFINED,

	/**
	 * 服务方成功操作通知
	 */
	SUCCESS,

	/**
	 * 支付成功，收到此状态时一定可以确定订单已经付款成功
	 */
	PAYMENT_SUCCESS,

	/**
	 * 支付动作成功，但在服务提供方尚未结束完整的检查流程，需根据实际情况来确定能否认定订单已经付款成功。默认此时不宜将订单设置为付款成功
	 */
	UNCONFIRUMED_PAYMENT_SUCCESS,

	/**
	 * 失败
	 */
	FAILURE,
	
	/**
	 * 交易关闭
	 */
	TRADE_CLOSED,
	
	/**
	 * 等待买家付款
	 */
	WAIT_BUYER_PAY,
	
	/**
	 * 不支持该类型
	 */
	NOT_SUPPORT,
	
	/**
	 * 处理中
	 */
	PROCESSING;
	
}
