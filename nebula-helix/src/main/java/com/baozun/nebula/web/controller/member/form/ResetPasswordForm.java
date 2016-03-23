package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.controller.BaseForm;

public class ResetPasswordForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    private String newPassword;

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

}
