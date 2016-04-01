package com.baozun.nebula.manager.member;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.manager.member.CommonEmailManager.SendEmailResultCode;

/**
 * 会员邮件相关业务类
 * 
 * @author yufei.kong 2016.3.23
 *
 */
public interface MemberEmailManager extends BaseManager {
	
	
	/**
	 * 发送激活邮件
	 * @param memberId
	 * @param path 拼接发送内容中的 前缀地址
	 * @param email 接收邮箱
	 */
	public SendEmailResultCode sendActiveEmail(Long memberId, String path,String email);
	
	/**
	 * 发送注册成功的邮件
	 * @param email 接收地址
	 * @param nickName用户昵称
	 * @return
	 */
	public SendEmailResultCode sendRegsiterSuccessEmail(String email,String nickName);
	
	
	/**
	 * 激活用户
	 * @param memberId
	 * @return
	 */
	public String activeMemberAccount(Long memberId);
	
	
	
    public void sendEmailValidateCode(String code, String email);
}
