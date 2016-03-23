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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.dao.member.MemberConductDao;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.Validator;

import loxia.utils.DateUtil;


/**
 * 
 * 用户邮件相关操作业务实现类
 * @author yufei.kong 2016.3.23
 *
 */

@Transactional
@Service("memberEmailManager")
public abstract class MemberEmailManagerImpl implements MemberEmailManager{
	
	private static final Logger log = LoggerFactory.getLogger(MemberManagerImpl.class);
	
	
	@Autowired
	private EventPublisher eventPublisher;
	
	@Value("#{meta['send.mail.key']}")
	private String sendMailKey;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberConductDao memberConductDao;
	
	
	

	@Override
	public Object sendActiveEmail(Long memberId, HttpServletRequest request) {
		//获取发送邮件间隔时间
		Integer timeSpan = EmailConstants.NEBULA_SEND_EMAIL_AGAIN_TIME_SPAN;
		Integer expiredSeconds = timeSpan * 60;
		
		//获取上一次发送时间
		Date expiredTime = null;
		if (request.getSession().getAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME) != null) { 
			 expiredTime = (Date)request.getSession().getAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME); 
		} 
		
		// 还未到再次发送时间，不进行发送 
		Date now = Calendar.getInstance().getTime(); 
		if (expiredTime != null && now.compareTo(expiredTime) < 0) { 
			 expiredSeconds = Long.valueOf((expiredTime.getTime() - now.getTime()) /1000).intValue();
			 return expiredSeconds;
		}
		
		//获取用户信息
		Member member = memberDao.findMemberById(memberId);
		//获取用户登录注册信息
		MemberConductCommand conduct = memberConductDao.findMemberConductCommandById(member.getId());
		
		//拼接内容
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId())
					.append(member.getLoginEmail())
					.append(member.getType()) 
					.append(member.getSource())
					.append(conduct.getRegisterTime());
		//加密字符串
		String checksum = EncryptUtil.getInstance().hash(emailContent.toString(), sendMailKey);
		
		StringBuffer activeEmailUrl = new StringBuffer();
		try {
			StringBuffer encryptParams = new StringBuffer();
			encryptParams.append("member_id=")
						 .append(member.getId())
						 .append("&checksum=")
						 .append(checksum).append("&sendTime=")
						 .append(new Date().getTime());
			String encrypt = EncryptUtil.getInstance().encrypt(encryptParams.toString());
			
			String path = getRegEmailValidPath(request);
			activeEmailUrl.append(path).append("?registerComfirm=").append(EncryptUtil.getInstance().base64Encode(encrypt));

		} catch (Exception e) {
			log.error("sendErr-----------------------: ",e);
		}
		log.info("activeEmailUrl-----------------------: " + activeEmailUrl);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		String lang = LangUtil.getCurrentLang();
		lang = lang == null ? "zh_HK" : lang;
		dataMap.put("lang", lang);
		dataMap.put("activeEmailUrl", activeEmailUrl);
		// 发送
		commonSendEmail(this,EmailConstants.EMAIL_REGISTER_VALIDATE,member.getLoginEmail(), dataMap);
		
		expiredTime = DateUtil.addMinutes(now, timeSpan);
		request.getSession().setAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME, expiredTime);
		return expiredSeconds;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isMemberEmailActive(Long memberId) {
		boolean flag = false;
		MemberPersonalData personalData = sdkMemberManager.findMemberPersonData(memberId);
		if (personalData != null && StringUtils.equals(EmailConstants.EMAIL_ACTIVE_YES,personalData.getShort2())) {
			flag = true;
		}
		return flag;
	}
	

	@Override
	public void sendRegsiterSuccessEmail(String email, MemberPersonalData personalData) {
		log.info("send register successfully email start ");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String lang = LangUtil.getCurrentLang();
		lang = lang == null ? "zh_HK" : lang;
		dataMap.put("lang", lang);
		dataMap.put("nickname", personalData.getNickname());
		// 发送
		commonSendEmail(this,EmailConstants.EMAIL_REGISTER_VALIDATE,email, dataMap);
		log.info("send register successfully email end ");
	}
	
	/**
	 * 通用接口
	 * @param object 在哪里调用就写this
	 * @param emailTemplate 模板
	 * @param email email 地址
	 * @param dataMap 内容
	 */
	public void commonSendEmail(Object object, String emailTemplate, String email, Map<String, Object> dataMap) {
		if(Validator.isNullOrEmpty(email)){
			return;
		}
		EmailEvent emailEvent = new EmailEvent(object, email, emailTemplate, dataMap);
		eventPublisher.publish(emailEvent);
	}
	
	/**
	 * 拼接邮件链接
	 * 
	 * @param request
	 * @return
	 */
	private String getRegEmailValidPath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path
				+ "/m/validEmailActiveUrl";
		return basePath;
	}


	
}
