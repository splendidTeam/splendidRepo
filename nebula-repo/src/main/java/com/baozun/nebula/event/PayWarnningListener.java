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
package com.baozun.nebula.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.payment.PayWarnningLog;
import com.baozun.nebula.sdk.manager.SdkPayWarnningLogManager;

/**
 * @author songdianchao 监听事件
 */
@Service
@Scope("singleton")
public class PayWarnningListener implements ApplicationListener<PayWarnningEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SdkPayWarnningLogManager sdkPayWarnningLogManager;
	@Override
	public void onApplicationEvent(PayWarnningEvent event) {
		//mailService.sendMail(event.getEmail());
		//logger.debug("to [{}], subject [{}], content [{}].", event.getEmail().getAddress(), event.getEmail().getSubject(), event.getEmail().getContent());
		try{
			//PaymentResult paymentResult,String operator,Integer type
	
			 PayWarnningLog payWarnningLog = new PayWarnningLog(event.getOrderCode(),
					 event.getThirPayNo(), event.getCreateTime(),
					 event.getPaystate_shop(), event.getPaystate_payment(), event.getIsSupportQuery(), event.getResult());
             sdkPayWarnningLogManager.savePayWarnningLog(payWarnningLog);
		} 
		catch(Exception e){
			e.printStackTrace();
			logger.error("error:"+event.getOrderCode()+event.getResult());
		}
	}

}
