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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;


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
	
	@Autowired
	private CacheManager cacheManager;
	
	
	@Override
	public boolean validEmailSendMaxNum(String email){
		log.info("valid sendNumber start");
		Integer sendNumber = 0;
		// 激活次數判斷
		if(Validator.isNotNullOrEmpty(cacheManager.getValue(email+EmailConstants.NEBULA_EMAIL_SENDMAXNUMBER_KEY))){
			//获取缓存中的激活次数
			sendNumber = Integer.parseInt(cacheManager.getValue(email+EmailConstants.NEBULA_EMAIL_SENDMAXNUMBER_KEY));
			log.debug("sendNumber --------------- ="+sendNumber);
			////获取最大次数 并对比 通过配置文件获取 TODO
			if(sendNumber>EmailConstants.SEND_EMAIL_NUMBER){
				log.info("cant't send email,sendNumber error");
				return false;
			}
		}
		log.info("valid sendNumber end");
		return true;
	}
	
	
	@Override
	public boolean validEmailSendIntervalTime(String email){
		
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		log.info("valid intervalTime start");
		//获取发送邮件间隔时间 通过配置文件获取 TODO
		Integer timeSpan = EmailConstants.NEBULA_SEND_EMAIL_AGAIN_TIME_SPAN;
		Integer expiredSeconds = timeSpan * 60;
		
		//获取上一次发送时间
		Date expiredTime = null;
		if(Validator.isNotNullOrEmpty(cacheManager.getValue(email+EmailConstants.NEBULA_EMAIL_INTERVALTIME_KEY))){
			try {
				expiredTime=sdf.parse(cacheManager.getValue(email+EmailConstants.NEBULA_EMAIL_INTERVALTIME_KEY));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			log.debug("expiredTime --------------- ="+expiredTime);
		}
		// 还未到再次发送时间，不进行发送 
		Date now = Calendar.getInstance().getTime(); 
		if (expiredTime != null && now.compareTo(expiredTime) > 0) { 
			 expiredSeconds = Long.valueOf((now.getTime() - expiredTime.getTime()) /1000).intValue();
			 
			 log.debug("expiredSeconds --------------- ="+expiredSeconds);
			 //判断时间差是否小于该时间
			 if(expiredSeconds < timeSpan * 60){
				 log.info("cant't send email,expiredSeconds------------"+expiredSeconds);
				 return false;
			 }
		}
		log.info("valid expiredTime end");
		return true;
	}
	

	@Override
	@Transactional(readOnly = true)
	public String sendActiveEmail(Long memberId, String path,String email) {
		String intervalTimeKey = email +EmailConstants.NEBULA_EMAIL_INTERVALTIME_KEY;
		String sendMaxNumKey = email+EmailConstants.NEBULA_EMAIL_SENDMAXNUMBER_KEY;
		
		//暂时写死 等待改成配置文件
		boolean isValidSendMaxNum=true;
		boolean isValidIntervalTime=true;
		
		//获取邮件模板 TODO
		
		//获取是否开启最大次数验证   in config  TODO
		
		//获取是否开启时间间隔验证    in config  TODO
		
		//是否需要开启最大次数验证
		if(isValidSendMaxNum){
			if(!validEmailSendMaxNum(email)){
				return EmailConstants.NEBULA_EMAIL_SENDMAXNUMBER_ERRORCODE;
			}
		}
		//是否需要开启间隔时间验证
		if(isValidIntervalTime){
			if(!validEmailSendIntervalTime(email)){
				return EmailConstants.NEBULA_EMAIL_INTERVALTIME_ERRORCODE;
			}
		}
		//获取用户信息
		Member member = memberDao.findMemberById(memberId);
		//获取用户登录注册信息
		MemberConductCommand conduct = memberConductDao.findMemberConductCommandById(member.getId());
		
		//组装参数
		String packageActiveEmailUrl = packageActiveEmailUrl(member,conduct,path);
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("activeEmailUrl", packageActiveEmailUrl);
		// 发送
		commonSendEmail(this,EmailConstants.EMAIL_REGISTER_VALIDATE,member.getLoginEmail(), dataMap);
		
		// 過期時間
		Date now = new Date();
		Integer expireSeconds = DateUtil.getIntervalSecond(now, DateUtil.getLastDateOfThisDay(now));//离今天24：00 的间隔  单位是s  
		
		if(isValidSendMaxNum){
			Integer sendNumber = 0;
			if(Validator.isNotNullOrEmpty(cacheManager.getValue(sendMaxNumKey))){
				//获取缓存中的激活次数
				sendNumber = Integer.parseInt(cacheManager.getValue(sendMaxNumKey));
			}
			// 發送一次加一次
			sendNumber++;
			cacheManager.setValue(sendMaxNumKey, sendNumber.toString(), expireSeconds);
		}
		
		//是否需要开启间隔时间验证
		if(isValidIntervalTime){
			cacheManager.setValue(intervalTimeKey, ""+now.getTime(),expireSeconds);
		}
		
		return EmailConstants.NEBULA_EMAIL_SEND_SUCCESSCODE;
	}
	
	/**
	 * 组装拼接发送内容
	 * @param member
	 * @param conduct
	 * @param path
	 * @return
	 * @throws EncryptionException 
	 */
	public String packageActiveEmailUrl(Member member,MemberConductCommand conduct,String path)  {
		//拼接内容
		StringBuffer emailContent = new StringBuffer();
		emailContent.append(member.getId())
					.append(member.getLoginEmail())
					.append(member.getType()) 
					.append(member.getSource())
					.append(conduct.getRegisterTime());
		//加密字符串
		String checksum = EncryptUtil.getInstance().hash(emailContent.toString(), sendMailKey);
		//拼接链接字符串并加密
		StringBuffer activeEmailUrl = new StringBuffer();
			StringBuffer encryptParams = new StringBuffer();
			encryptParams.append("member_id=")
						 .append(member.getId())
						 .append("&checksum=").append(checksum)
						 .append("&sendTime=").append(new Date().getTime());
			
			try{
				String encrypt = EncryptUtil.getInstance().encrypt(encryptParams.toString());
				activeEmailUrl.append(path).append("?registerComfirm=").append(EncryptUtil.getInstance().base64Encode(encrypt));
				
				log.info("activeEmailUrl-----------------------: " + activeEmailUrl);
				return activeEmailUrl.toString();
	} catch (EncryptionException e) {
		//FIXME
		String errorCode="";
		throw new BusinessException(errorCode);
	}
	
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
	@Transactional(readOnly = true)
	public void sendRegsiterSuccessEmail(String email, MemberPersonalData personalData) {
		log.info("send register successfully email start ");
		Map<String, Object> dataMap = new HashMap<String, Object>();
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
		String lang = LangUtil.getCurrentLang();
		lang = lang == null ? "zh_HK" : lang;
		dataMap.put("lang", lang);
		EmailEvent emailEvent = new EmailEvent(object, email, emailTemplate, dataMap);
		eventPublisher.publish(emailEvent);
	}
	

    /***
     * 发送邮件验证码
     * 
     * @Transactional(readOnly = true)
     * 
     */

    public void sendEmailValidateCode(String code, String email) {
	log.info("send successfully email start ");
	Map<String, Object> dataMap = new HashMap<String, Object>();
	dataMap.put("code", code);
	// 发送
	commonSendEmail(this, "{code}", email, dataMap);
	log.info("send  successfully email end ");
    }

	
}
