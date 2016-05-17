package com.baozun.nebula.web.command;

import java.util.Date;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;

public class MemberFrontendCommand {

	/**
	 * id
	 */
	private Long						id;

	/**
	 * 登录名
	 */
	private String						loginName;

	/**
	 * 登录邮箱
	 */
	private String						loginEmail;

	/**
	 * 登录手机
	 */
	private String						loginMobile;

	/**
	 * 密码
	 */
	private String						password;

	/**
	 * 第三方标识
	 */
	private String						thirdPartyIdentify;

	/**
	 * 来源： 自注册 微博登录 QQ登录 等。 value在ChooseOption中配置
	 */
	private Integer						source;

	/**
	 * 类型: 自有会员 自注册会员 第三方会员 等。 value在ChooseOption中配置
	 */
	private Integer						type;

	/**
	 * 是否已绑定分类:1表示已加入 0表示未加入
	 */
//	private Integer						isaddgroup;

	/**
	 * 性别 ：0表示男 1表示女
	 */
	private Integer						sex;

	/**
	 * 生日
	 */
	private String						birthday;

	private String						repassword;

	/**
	 * 生命周期
	 */
	private Integer						lifecycle;

	/**
	 * 真实姓名
	 */
	private String						realName;

	/**
	 * 1 接收 0 不接收
	 */
	private Integer						receiveMail;

	private String						newPassword;

	/**
	 * 验证码
	 */
	private String						randomCode;

	/**
	 * 记住用户名
	 */
	private String						chkRememberMe;
	
	/**
	 * 姓名
	 */
	private String							nickname;

	// /**
	// * 登录次数
	// */
	// private Integer loginCount;
	//
	// /**
	// * 注册时间
	// */
	// private Date registerTime;
	//
	// /**
	// * 登录时间(最近)
	// */
	// private Date loginTime;
	//
	// /**
	// * 注册ip
	// */
	// private String registerIp;
	//
	// /**
	// * 登录ip(最近)
	// */
	// private String loginIp;

	private MemberPersonalDataCommand	memberPersonalDataCommand;

	private MemberConductCommand		memberConductCommand;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getLoginMobile() {
		return loginMobile;
	}

	public void setLoginMobile(String loginMobile) {
		this.loginMobile = loginMobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getThirdPartyIdentify() {
		return thirdPartyIdentify;
	}

	public void setThirdPartyIdentify(String thirdPartyIdentify) {
		this.thirdPartyIdentify = thirdPartyIdentify;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

//	public Integer getIsaddgroup() {
//		return isaddgroup;
//	}
//
//	public void setIsaddgroup(Integer isaddgroup) {
//		this.isaddgroup = isaddgroup;
//	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getReceiveMail() {
		return receiveMail;
	}

	public void setReceiveMail(Integer receiveMail) {
		this.receiveMail = receiveMail;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	// public Integer getLoginCount() {
	// return loginCount;
	// }
	//
	// public void setLoginCount(Integer loginCount) {
	// this.loginCount = loginCount;
	// }
	//
	// public Date getRegisterTime() {
	// return registerTime;
	// }
	//
	// public void setRegisterTime(Date registerTime) {
	// this.registerTime = registerTime;
	// }
	//
	// public Date getLoginTime() {
	// return loginTime;
	// }
	//
	// public void setLoginTime(Date loginTime) {
	// this.loginTime = loginTime;
	// }
	//
	// public String getRegisterIp() {
	// return registerIp;
	// }
	//
	// public void setRegisterIp(String registerIp) {
	// this.registerIp = registerIp;
	// }
	//
	// public String getLoginIp() {
	// return loginIp;
	// }
	//
	// public void setLoginIp(String loginIp) {
	// this.loginIp = loginIp;
	// }

	public MemberPersonalDataCommand getMemberPersonalDataCommand() {
		return memberPersonalDataCommand;
	}

	public void setMemberPersonalDataCommand(MemberPersonalDataCommand memberPersonalDataCommand) {
		this.memberPersonalDataCommand = memberPersonalDataCommand;
	}

	public MemberConductCommand getMemberConductCommand() {
		return memberConductCommand;
	}

	public void setMemberConductCommand(MemberConductCommand memberConductCommand) {
		this.memberConductCommand = memberConductCommand;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	public String getChkRememberMe() {
		return chkRememberMe;
	}

	public void setChkRememberMe(String chkRememberMe) {
		this.chkRememberMe = chkRememberMe;
	}
	

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "MemberFrontendCommand [id=" + id + ", loginName=" + loginName + ", loginEmail=" + loginEmail
				+ ", loginMobile=" + loginMobile + ", password=" + password + ", thirdPartyIdentify="
				+ thirdPartyIdentify + ", source=" + source + ", type=" + type /*+ ", isaddgroup=" + isaddgroup*/
				+ ", sex=" + sex + ", birthday=" + birthday + ", repassword=" + repassword + ", lifecycle=" + lifecycle
				+ ", realName=" + realName + ", receiveMail=" + receiveMail+ ", nickname=" + nickname + ", newPassword=" + newPassword
				+ ", randomCode=" + randomCode + ", chkRememberMe=" + chkRememberMe + ", memberPersonalDataCommand="
				+ memberPersonalDataCommand + ", memberConductCommand=" + memberConductCommand + "]";
	}

}
