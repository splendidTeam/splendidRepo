package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.controller.BaseForm;

public class ForgetPasswordForm extends BaseForm {

    private static final long serialVersionUID = -2376678079825089890L;

    /* 验证方式 可选值 1.mobile 2.email */
    private Integer type;

    private String mobile;

    private String email;

    /* 验证码 */
    private String SecurityCode;

    /* 新密码 */
    private String newPassword;

    /* 确认密码 */
    private String confirmPassword;

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public String getSecurityCode() {
	return SecurityCode;
    }

    public void setSecurityCode(String securityCode) {
	SecurityCode = securityCode;
    }

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
