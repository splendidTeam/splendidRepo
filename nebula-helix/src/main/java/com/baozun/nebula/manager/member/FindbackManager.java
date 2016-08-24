package com.baozun.nebula.manager.member;

import javax.servlet.http.HttpSession;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;

/**
 * FindbackManager
 * 
 * 2014-2-13 下午4:08:35
 * 
 * @author <a href="xinyuan.guo@baozun.cn">郭馨元</a>
 * 
 */
@Deprecated
public interface FindbackManager extends BaseManager{

	void confirmAccount(String loginName, String vcode, HttpSession session);

	void setPassWord(String pwd, MemberCommand mc, Long ecId);

	void checkQa(String[] q, String[] a);

	void sendEmailCode(HttpSession session);

	void sendMobileCode(HttpSession session);
	
	/** 发送链接地址到邮件*/
	void sendEmailUrl(String email);
	
	/** 验证TQS*/
	boolean chechTQS(String t, String q, String s);
	
	/** 获取当天邮件个数*/
	Integer getEmailCountByDay(String emailAddress);

}
