package com.baozun.nebula.manager.member;

import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;

/**
 * MemberPasswordManager接口
 * 
 * @author Wanrong.Wang 2016/03/30
 */
public interface MemberPasswordManager{

	// 发送验证码
	boolean sendValidateCode(ForgetPasswordForm forgetPasswordForm);

	// 重置密码
	boolean resetPassword(ForgetPasswordForm forgetPasswordForm,String password);

	// 检查旧密码是否正确的
	boolean checkOldPassword(Long memberId,String oldPassword);

	// 修改密码
	boolean modifyPassword(String oldPassword,String newPassword,String confirmPassword,Long memberId);

}
