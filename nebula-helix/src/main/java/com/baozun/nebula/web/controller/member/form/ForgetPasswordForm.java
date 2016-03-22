package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.controller.BaseForm;

public class ForgetPasswordForm extends BaseForm{

	private static final long serialVersionUID = -2376678079825089890L;
	
	/* 验证方式  可选值 1.mobile  2.email*/
	private Integer type;

	private String mobile;
	
	private String email;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

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
	
	
}
