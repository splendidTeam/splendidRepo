package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.command.MemberFrontendCommand;

public class RegisterForm {

	private String mobile;
	
	private String email;

	private String sex;
	
	private String age;
	
	private String birthday;
	
	private String name;
	
	private String nickName;
	
	private String password;
	/**
	 * 是否愿意接收推送信息 可选值：1.接受，2.不接受
	 */
	private String receiveMessage;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getReceiveMessage() {
		return receiveMessage;
	}

	public void setReceiveMessage(String receiveMessage) {
		this.receiveMessage = receiveMessage;
	}

	public MemberFrontendCommand toMemberFrontendCommand(){
		//数据转换
		return new MemberFrontendCommand();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
