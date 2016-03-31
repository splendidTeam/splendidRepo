package com.baozun.nebula.manager.member;

import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;

/**
 * @author Wanrong.Wang 2016/03/30
 */
public interface MemberPasswordManager{

	boolean sendValidateCode(ForgetPasswordForm forgetPasswordForm) throws Exception;

	boolean resetPassword(ForgetPasswordForm forgetPasswordForm,String password);

	boolean checkOldPassword(Long memberId,String oldPassword);

	boolean modifyPassword(String oldPassword,String newPassword,String confirmPassword,Long memberId);

}
