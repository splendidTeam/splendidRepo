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
import com.baozun.nebula.manager.member.entity.SendEmailConfig;
import com.baozun.nebula.manager.system.RollingTimeWindow;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;

/**
 * 通用邮件实现类
 * 
 * @author yufei.kong 2016年4月5日 16:12:42
 *
 */
@Transactional
@Service("commonEmailManager")
public class CommonEmailManagerImpl implements CommonEmailManager {

	private static final Logger log = LoggerFactory.getLogger(CommonEmailManagerImpl.class);

	// 发送邮件event注入
	@Autowired
	private EventPublisher eventPublisher;

	// 策略控制类
	@Autowired
	private TokenManager tokenManager;

	// 通过value注解获取properties的value
	@Value("#{meta['send.mail.key']}")
	private String sendMailKey;

	// 通过value注解获取properties的value
	@Value("#{meta['send.mail.maxSendNumber']}")
	private Long maxSendNumber;

	// 通过value注解获取properties的value
	@Value("#{meta['send.mail.intervalTime']}")
	private Long intervalTime;

	// 通过value注解获取properties的value
	@Value("#{meta['send.mail.emailCacheExpireTime']}")
	private Long emailCacheExpireTime;

	/**
	 * 发送邮件方法 要点如下
	 * <ol>
	 * <li>1.email不可为空,为空返回失败</li>
	 * <li>2.模板code不能为空,不能错误.错误也返回失败</li>
	 * <li>3.sendEmailConfig 属性为
	 * {@link com.baozun.nebula.manager.member.entity.SendEmailConfig}中</li>
	 * <li>4.当调用时默认传sendEmailConfig为null调用配置文件中获取的值,当有特殊需求时,可以自己new一个该对象,set具体的值
	 * </li>
	 * </ol>
	 * 
	 * 
	 * @param email
	 * @param emailTemplateCode
	 * @param dataMap
	 * @param sendEmailConfig
	 */
	public SendEmailResultCode sendEmail(String email, String emailTemplateCode, Map<String, Object> dataMap,
			SendEmailConfig sendEmailConfig) {

		// 判断邮箱是否为空 如为空则返回失败
		if (Validator.isNullOrEmpty(email)) {
			log.info("email can not be null");
			return SendEmailResultCode.FAILURE;
		}

		if (sendEmailConfig != null) {
			sendEmailConfig.setIsValidIntervalTime(false);
			sendEmailConfig.setIsValidMaxSendNumber(true);
			sendEmailConfig.setMaxSendNumber(maxSendNumber);
			sendEmailConfig.setIntervalTime(intervalTime);
			sendEmailConfig.setEmailCacheExpireTime(emailCacheExpireTime);

			// 判断是否开启最大次数验证 如开启 组装访问策略pojo
			// RollingTimeWindow limit 为最大发送数 ,window为该窗口有效期
			if (sendEmailConfig.getIsValidMaxSendNumber()) {
				RollingTimeWindow rollingTimeWindow = new RollingTimeWindow(sendEmailConfig.getMaxSendNumber(),
						sendEmailConfig.getEmailCacheExpireTime());
				VerifyResult resultcode = tokenManager.verifyAccess("validEmailMaxSendNum" + emailTemplateCode, email,
						rollingTimeWindow);
				log.debug("validate result is" + resultcode);
				// 判断结果
				if (resultcode == VerifyResult.LIMITED) {
					return SendEmailResultCode.MAXSENDNUMBERERROR;
				}
			}

			// 判断是否开始期间间隔验证 如开启 组装访问策略pojo
			// RollingTimeWindow limit为时间间隔 ,window为该窗口有效期
			if (sendEmailConfig.getIsValidIntervalTime()) {
				RollingTimeWindow rollingTimeWindow = new RollingTimeWindow(1L, sendEmailConfig.getIntervalTime());
				VerifyResult resultcode = tokenManager.verifyAccess("validEmailIntervalTime" + emailTemplateCode, email,
						rollingTimeWindow);
				log.debug("validate result is" + resultcode);
				// 判断结果
				if (resultcode == VerifyResult.LIMITED) {
					return SendEmailResultCode.INTERVALTIMEERROR;
				}
			}
		}
		String lang = LangUtil.getCurrentLang();
		dataMap.put("lang", lang);
		EmailEvent emailEvent = new EmailEvent(this, email, emailTemplateCode, dataMap);
		eventPublisher.publish(emailEvent);
		return SendEmailResultCode.SUCESS;
	}

	@Override
	public SendEmailResultCode sendEmail(String email, String emailTemplateCode, Map<String, Object> dataMap) {
		return sendEmail(email, emailTemplateCode, dataMap, null);
	}

}
