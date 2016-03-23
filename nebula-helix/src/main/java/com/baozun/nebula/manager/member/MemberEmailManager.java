package com.baozun.nebula.manager.member;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberPersonalData;

/**
 * 会员邮件相关业务类
 * 
 * from columbia项目相关代码
 * @author yufei.kong 2016.3.23
 *
 */
public interface MemberEmailManager extends BaseManager {
	
	
	/**
	 * 发送激活邮件
	 * 
	 */
	public Object sendActiveEmail(Long memberId, HttpServletRequest request);
	
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
}
