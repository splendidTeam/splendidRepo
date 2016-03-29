package com.baozun.nebula.web.command;

/**
 * 返回提示.
 * 
 * @author D.C
 * @version 2010-6-24 上午03:14:56
 * @deprecated 在新的nebula 2016直接弃用
 */
@Deprecated
public class PasswordCommand{

	/** 原密码*/
	private String oldPassword;
	/** 密码*/
	private String password;
	/** 确认密码*/
	private String repassword;
	/** 验证码*/
	private String vcode;
	/** 邮件地址*/
	private String emailAddress;
	/** 加密tqs*/
	private String MD5_t_q_s;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMD5_t_q_s() {
		return MD5_t_q_s;
	}

	public void setMD5_t_q_s(String mD5_t_q_s) {
		MD5_t_q_s = mD5_t_q_s;
	}
	
	
}
