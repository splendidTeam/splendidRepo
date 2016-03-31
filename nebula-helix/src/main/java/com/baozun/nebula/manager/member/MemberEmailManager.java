package com.baozun.nebula.manager.member;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberPersonalData;

/**
 * 会员邮件相关业务类
 * 
 * @author yufei.kong 2016.3.23
 *
 */
public interface MemberEmailManager extends BaseManager {
	
	/**
	 * 是否验证最大邮件发送数
	 * @param email
	 * @return
	 */
	public boolean validEmailSendMaxNum(String email); 
	
	
	/**
	 * 是否验证邮件发送时间间隔
	 * @param email
	 * @return
	 */
	public boolean validEmailSendIntervalTime(String email); 
	
	
	/**
	 * 发送激活邮件
	 * @param memberId
	 * @param path
	 */
	public String sendActiveEmail(Long memberId, String path,String email);
	
	/**
	 * 判断会员邮箱是否激活
	 * 
	 * @param memberId
	 * @return
	 */
	public boolean isMemberEmailActive(Long memberId);
	
	/**
	 * 发送普通會員註冊成功邮件
	 * 
	 */
	public void sendRegsiterSuccessEmail(String email,MemberPersonalData personalData);
	
    public void sendEmailValidateCode(String code, String email);
}
