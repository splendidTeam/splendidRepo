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
package com.baozun.nebula.manager.member;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.manager.system.RollingTimeWindow;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;



/**
 * 通用邮件实现类
 * @author yufei.kong
 *
 */
@Transactional
@Service("commonEmailManager")
public  class CommonEmailManagerImpl implements CommonEmailManager{
	
	private static final Logger log = LoggerFactory.getLogger(CommonEmailManagerImpl.class);
	
	
	@Autowired
	private EventPublisher eventPublisher;
	
	@Autowired
	private TokenManager tokenManager;
	
	//通过value注解获取properties的value
	@Value("#{meta['send.mail.key']}")
	private String sendMailKey;
	
	//通过value注解获取properties的value 获取是否开启时间间隔的验证开关
	@Value("#{meta['sendEmail.isValidate.intervalTime.key]}")
	private String isValidIntervalTime;
	
	//通过value注解获取properties的value 获取是否开启最大发送数的验证开关
	@Value("#{meta['sendEmail.isValidate.maxSendNumber.key']}")
	private String isValidMaxSendNumber;
	
	//通过value注解获取properties的value 获取有效期内的允许最大发送数
	@Value("#{meta['sendEmail.maxSendNumber.key]}")
	private String maxSendNumber;
	
	//通过value注解获取properties的value 获取有效期内的发送时间间隔
	@Value("#{meta['sendEmail.intervalTime.key']}")
	private String intervalTime;
	
	//通过value注解获取properties的value 获取缓存有效期
	@Value("#{meta['sendEmail.expireTime.key']}")
	private String emailCacheExpireTime;


	@Override
	public SendEmailResultCode sendEmail(String email, String emailTemplateCode, Map<String, Object> dataMap) {
		//判断邮箱是否为空 如为空则返回失败
		if(Validator.isNullOrEmpty(email)){
			log.info("email can not be null");
			return SendEmailResultCode.FAILURE;
		}
		
		//判断是否开启最大次数验证 如开启 组装访问策略pojo
		//RollingTimeWindow limit 为最大发送数 ,window为该窗口有效期
		if("Y".equals(isValidMaxSendNumber)){
			RollingTimeWindow rollingTimeWindow=new RollingTimeWindow(Long.parseLong(maxSendNumber),Long.parseLong(emailCacheExpireTime));
			VerifyResult resultcode= tokenManager.verifyAccess("validEmailMaxSendNum",email, rollingTimeWindow);
			log.debug("validate result is"+resultcode);
			//这个地方不对  要改 TODO
			if(resultcode.equals(resultcode.LIMITED)){
				return SendEmailResultCode.MAXSENDNUMBERERROR;
			}
		}
		
		//判断是否开始期间间隔验证 如开启 组装访问策略pojo
		//RollingTimeWindow limit为时间间隔 ,window为该窗口有效期
		if("Y".equals(isValidIntervalTime)){
			RollingTimeWindow rollingTimeWindow=new RollingTimeWindow(Long.parseLong(intervalTime),Long.parseLong(emailCacheExpireTime));
			VerifyResult resultcode= tokenManager.verifyAccess("validEmailIntervalTime",email, rollingTimeWindow);
			log.debug("validate result is"+resultcode);
			//这个地方不对  要改 TODO
			if(resultcode.equals(resultcode.LIMITED)){
				return SendEmailResultCode.INTERVALTIMEERROR;
			}
		}
		
		String lang = LangUtil.getCurrentLang();
		lang = lang == null ? "zh_HK" : lang;
		dataMap.put("lang", lang);
		EmailEvent emailEvent = new EmailEvent(this, email, emailTemplateCode, dataMap);
		eventPublisher.publish(emailEvent);
		
		return SendEmailResultCode.SUCESS;
	}

	
}
