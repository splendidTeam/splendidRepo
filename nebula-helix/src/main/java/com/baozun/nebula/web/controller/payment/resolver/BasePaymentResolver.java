/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.payment.resolver;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.feilong.servlet.http.RequestUtil;

public abstract class BasePaymentResolver {
	
	private static final Logger	LOG = LoggerFactory.getLogger(BasePaymentResolver.class);
	
	private final static String PAY_EXPIRY_TIME = "payExpiryTime";
	
	@Autowired
	private MataInfoManager mataInfoManager;
	
	/**
	 * 获取支付信息
	 * 
	 * @param bankCode
	 * @param payType
	 * @return
	 */
	protected OnLinePaymentCommand getOnLinePaymentCommand(String bankCode, Integer payType, String itBPay, HttpServletRequest request) {
		OnLinePaymentCommand onLinePaymentCommand = new OnLinePaymentCommand();
		onLinePaymentCommand.setBankCode(bankCode);
		onLinePaymentCommand.setCustomerIp(RequestUtil.getClientIp(request));
		onLinePaymentCommand.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		onLinePaymentCommand.setItBPay(itBPay);
		if (payType == ReservedPaymentType.ALIPAY_CREDIT_INT_M || payType == ReservedPaymentType.ALIPAY_CREDIT_INT_V) {
			onLinePaymentCommand.setIsInternationalCard(true);
		} else {
			onLinePaymentCommand.setIsInternationalCard(false);
		}
		onLinePaymentCommand.setPayType(payType);
		return onLinePaymentCommand;
	}
	
	
	protected String getItBPay(Date orderCreateDate) throws IllegalPaymentStateException {
		String payExpiryTime = mataInfoManager.findValue(PAY_EXPIRY_TIME);

		Date now = new Date();

		long minutes = (now.getTime() - orderCreateDate.getTime()) / 1000 / 60;  

		if (payExpiryTime == null) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAID);
		}

		Long itBPay = Long.valueOf(payExpiryTime) - minutes;

		if (itBPay <= 0L) {
			return "0m";
		} else {
			return itBPay.toString() + "m";
		}

	}

	

}
