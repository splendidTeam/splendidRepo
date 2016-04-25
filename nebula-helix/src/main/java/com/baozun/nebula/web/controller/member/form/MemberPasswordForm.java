package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.controller.BaseForm;

/**
 * 修改密码表单
 */

public class MemberPasswordForm extends BaseForm{

	private static final long	serialVersionUID	= 1L;

	/* 用户id */
	private long				memberId;

	/* 用户手机 */
	private String				mobile;

	/* 用户邮箱 */
	private String				email;

	/* 旧密码 */
	private String				oldPassword;

	/* 新密码 */
	private String				newPassword;

	/* 确认密码 */
	private String				confirmPassword;

	public long getMemberId(){
		return memberId;
	}

	public void setMemberId(long memberId){
		this.memberId = memberId;
	}

	public String getConfirmPassword(){
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword){
		this.confirmPassword = confirmPassword;
	}

	public String getNewPassword(){
		return newPassword;
	}

	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}

	public String getOldPassword(){
		return oldPassword;
	}

	public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}

	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

}
