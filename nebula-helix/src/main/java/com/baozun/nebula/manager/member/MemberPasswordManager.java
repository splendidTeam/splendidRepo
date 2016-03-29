package com.baozun.nebula.manager.member;

import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;

public interface MemberPasswordManager{

	boolean sendValidateCode(ForgetPasswordForm forgetPasswordForm) throws Exception;

	boolean resetPassword(ForgetPasswordForm forgetPasswordForm,String password);

}
