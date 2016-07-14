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

import com.baozun.nebula.sdk.manager.EmailTemplateManager;

/**
 * @author songdianchao 监听事件
 */
@Service
@Scope("singleton")
public class EmailEventListener implements ApplicationListener<EmailEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	@Override
	public void onApplicationEvent(EmailEvent event) {
		//mailService.sendMail(event.getEmail());
		//logger.debug("to [{}], subject [{}], content [{}].", event.getEmail().getAddress(), event.getEmail().getSubject(), event.getEmail().getContent());
		try{
			emailTemplateManager.sendEmail(event.getReceiverEmail(), event.getCode(), event.getDataMap(),event.getAttachmentList());
		} 
		catch(Exception e){
			logger.error("email send error:"+event.getReceiverEmail());
		}
	}

}
