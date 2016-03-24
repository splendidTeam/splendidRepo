package com.baozun.nebula.web.controller.member.form;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.BaseForm;

/**
 * 会员注册提交的属性
 * 
 * @author D.C
 * @author Viktor Huang
 * @date 2016年3月21日 下午8:03:29
 */
public class RegisterForm extends BaseForm{

	private static final long	serialVersionUID	= 1265756122959961908L;

	private static final Logger	LOGGER				= LoggerFactory.getLogger(RegisterForm.class);

	private String				loginMobile;

	private String				loginEmail;

	private String				loginName;

	/** 密码 */
	private String				password;

	/** 再次输入密码 */
	private String				repassword;

	/** 验证码 */
	private String				randomCode;

	/** 是否接收AD邮件:1 接收 0 不接收 */
	private int					receiveMail;

	private String				sex;

	private String				age;

	private String				birthday;

	private String				nickName;

	/**
	 * 是否愿意接收推送信息 可选值：1.接受，2.不接受
	 */
	private String				receiveMessage;

	/**
	 * RegisterForm 对象装换为 MemberFrontendCommand
	 * 
	 * @return
	 */
	public MemberFrontendCommand toMemberFrontendCommand(){
		MemberFrontendCommand memberFrontendCommand = new MemberFrontendCommand();
		// 数据转换
		try{
			BeanUtils.copyProperties(memberFrontendCommand, this);
		}catch (Exception e){
			LOGGER.error("", e);
		}

		return new MemberFrontendCommand();
	}

	public String getSex(){
		return sex;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getAge(){
		return age;
	}

	public void setAge(String age){
		this.age = age;
	}

	public String getBirthday(){
		return birthday;
	}

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getNickName(){
		return nickName;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getReceiveMessage(){
		return receiveMessage;
	}

	public void setReceiveMessage(String receiveMessage){
		this.receiveMessage = receiveMessage;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * @return the repassword
	 */
	public String getRepassword(){
		return repassword;
	}

	/**
	 * @param repassword
	 *            the repassword to set
	 */
	public void setRepassword(String repassword){
		this.repassword = repassword;
	}

	/**
	 * @return the randomCode
	 */
	public String getRandomCode(){
		return randomCode;
	}

	/**
	 * @param randomCode
	 *            the randomCode to set
	 */
	public void setRandomCode(String randomCode){
		this.randomCode = randomCode;
	}

	/**
	 * @return the loginMobile
	 */
	public String getLoginMobile(){
		return loginMobile;
	}

	/**
	 * @param loginMobile
	 *            the loginMobile to set
	 */
	public void setLoginMobile(String loginMobile){
		this.loginMobile = loginMobile;
	}

	/**
	 * @return the loginEmail
	 */
	public String getLoginEmail(){
		return loginEmail;
	}

	/**
	 * @param loginEmail
	 *            the loginEmail to set
	 */
	public void setLoginEmail(String loginEmail){
		this.loginEmail = loginEmail;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName(){
		return loginName;
	}

	/**
	 * @param loginName
	 *            the loginName to set
	 */
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	/**
	 * @return the receiveMail
	 */
	public int getReceiveMail(){
		return receiveMail;
	}

	/**
	 * @param receiveMail
	 *            the receiveMail to set
	 */
	public void setReceiveMail(int receiveMail){
		this.receiveMail = receiveMail;
	}

}
