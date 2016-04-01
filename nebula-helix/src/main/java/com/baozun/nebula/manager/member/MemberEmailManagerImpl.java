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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.member.CommonEmailManager.SendEmailResultCode;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberBehaviorStatus;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.feilong.core.Validator;


/**
 * 
 * 用户邮件相关操作业务实现类
 * @author yufei.kong 2016.3.23
 *
 */

@Transactional
@Service("memberEmailManager")
public  class MemberEmailManagerImpl implements MemberEmailManager{
	
	private static final Logger log = LoggerFactory.getLogger(MemberManagerImpl.class);
	
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberConductDao memberConductDao;
	
	@Autowired
	private CommonEmailManager commonEmailManager;
	
	//通过value注解获取properties的value
	@Value("#{meta['send.mail.key']}")
	private String sendMailKey;

	@Override
	@Transactional(readOnly = true)
	public SendEmailResultCode sendActiveEmail(Long memberId, String path,String email) {
		if(Validator.isNullOrEmpty(memberId)){
			return SendEmailResultCode.FAILURE;
		}
		//组装参数
		String packageActiveEmailUrl = packageActiveEmailUrl(memberId,path);
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("activeEmailUrl", packageActiveEmailUrl);
		// 发送
		SendEmailResultCode resultCode=commonEmailManager.sendEmail(email, EmailConstants.EMAIL_ACTIVE_TEMPLATE, dataMap);
		
		return resultCode;
	}
	
	/**
	 * 组装激活邮件的 内容 URL
	 * @param memberId 
	 * @param path 前缀地址
	 * @return
	 */
	public String packageActiveEmailUrl(Long memberId,String path)  {
		
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
	public SendEmailResultCode sendRegsiterSuccessEmail(String email,String nickName) {
		log.info("send register successfully email start ");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("nickname", nickName);
		
		SendEmailResultCode resultCode=commonEmailManager.sendEmail(email, EmailConstants.EMAIL_ACTIVE_TEMPLATE, dataMap);
		log.info("send register successfully email end ");
		
		return resultCode;
	}

	@Override
	public String activeMemberAccount(Long memberId) {
		//邮箱激活为1
		String type="1";
		MemberBehaviorStatus memberBehaviorStatus=sdkMemberManager.findMemberBehaviorStatusByTypeAndMemberId(type, memberId);
		//判断是否为空 如果不为空则代表有激活数据  空则代表未激活
		if (Validator.isNotNullOrEmpty(memberBehaviorStatus)) {
			//判断有效状态 如果为1代表有效 如果为0代表无效 也可激活
			if(memberBehaviorStatus.getLifecycle()==1){
				
			}else{
				memberBehaviorStatus=new MemberBehaviorStatus();
				memberBehaviorStatus.setMemberId(memberId);
				memberBehaviorStatus.setType("1");
				memberBehaviorStatus.setLifecycle(1);
				sdkMemberManager.saveMemberBehaviorStatus(memberBehaviorStatus);
			}
		}else{
			memberBehaviorStatus=new MemberBehaviorStatus();
			memberBehaviorStatus.setMemberId(memberId);
			memberBehaviorStatus.setType("1");
			memberBehaviorStatus.setLifecycle(1);
			sdkMemberManager.saveMemberBehaviorStatus(memberBehaviorStatus);
		}
		return null;
	}

	@Override
	public void sendEmailValidateCode(String code, String email) {
		// TODO Auto-generated method stub
		
	}
	

//    /***
//     * 发送邮件验证码
//     * 
//     * @Transactional(readOnly = true)
//     * 
//     */
//
//    public void sendEmailValidateCode(String code, String email) {
//	log.info("send successfully email start ");
//	Map<String, Object> dataMap = new HashMap<String, Object>();
//	dataMap.put("code", code);
//	// 发送
//	commonSendEmail(this, "{code}", email, dataMap);
//	log.info("send  successfully email end ");
//    }

	
}
