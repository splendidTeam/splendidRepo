package com.baozun.nebula.web.controller.member;

import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.constants.MessageConstants;
import com.baozun.nebula.manager.member.MemberEmailManager;
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.sms.SmsManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.SecurityCodeUtil;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;

/**
 * 
 * @author Wanrong.Wang
 * @Date 2016/03/22
 * @Controller
 * 
 */
public class NebulaForgetPasswordController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(NebulaForgetPasswordController.class);

    /* 忘记密码的页面定义 */
    public static final String VIEW_FORGET_PASSWORD = "store.login.forgetpassword";

    /* 重置密码的页面定义 */
    public static final String VIEW_RESET_PASSWORD = "store.login.resetpassword";

    /* 重置密码成功的页面定义 */
    public static final String VIEW_RESET_PASSWORD_SUCCESS = "resetpassword.success";

    private static final int MAX_EXIST_TIME = 60;

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private MemberEmailManager memberEmailManager;

    @Autowired
    private SmsManager smsManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private MemberExtraManager memberExtraManager;

    @Autowired
    @Qualifier("forgetPasswordFormValidator")
    private ForgetPasswordFormValidator forgetPasswordFormValidator;

    /**
     * 跳转到忘记密码的页面
     * 
     * @return String
     * @RequestMapping(value = "/forgetpassword.htm", method =
     *                       RequestMethod.GET)
     */
    public String showForgetPassword() {
	return VIEW_FORGET_PASSWORD;
    }

    /**
     * <pre>
     *
     * 判断验证方式是邮箱验证还是手机验证：邮箱验证：type=2； 手机验证：type=1
     * </pre>
     * 
     * @param type
     * @return String
     * 
     * @RequestMapping(value="/member/checkValidateType.json",method=RequestMethod.GET)
     * 
     */

    public String isEmailOrmobile(Integer type) {
	if (type == 1) {
	    // 为手机验证方式
	    return "forward:/member/mobileSendValidateCode.htm";
	} else if (type == 2) {
	    // 为邮箱验证方式
	    return "forward:/member/emailSendValidateCode.htm";
	}
	return null;
    }

    /**
     * 邮箱验证方式发送验证码
     * 
     * @param request
     * @param response
     * @param model
     * @param forgetPasswordForm
     * @param bindingResult
     * @return
     * 
     * @RequestMapping(value="/member/emailSendValidateCode.json",method=RequestMethod.POST)
     */
    public NebulaReturnResult emailSendValidateCode(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) throws Exception {

	String email = forgetPasswordForm.getEmail();
	DefaultReturnResult returnResult = new DefaultReturnResult();
	DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

	LOGGER.info("忘记密码的用户email为--=" + email);

	// 此处只需要对邮箱进行校验，查询邮箱是否符合规格，是否存在
	// 验证邮箱格式
	if (StringUtils.isNotEmpty(email)) {
	    Pattern p1 = Pattern
		    .compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
	    if (!"".equals(email.trim()) && !p1.matcher(email.trim()).matches()) {
		returnResult.setResult(false);
		returnResult.setResultMessage(defaultResultMessage);
		return returnResult;
	    }
	}

	// 邮箱规则正确，通过邮箱查询用户是否存在
	MemberCommand memberCommand = sdkMemberManager
		.findMemberByLoginEmail(email);

	// 1.2 对应邮箱的用户不存在
	if (Validator.isNullOrEmpty(memberCommand)) {
	    returnResult.setResult(false);
	    returnResult.setResultMessage(defaultResultMessage);
	    return returnResult;

	} else {
	    // 1.1 邮箱用户存在：则调用发送邮件的方法，发送相应的修改密码的链接和验证码到邮箱中
	    String code = SecurityCodeUtil.createSecurityCode(
		    MessageConstants.SECURITY_CODE_ORIGINAL_STRING,
		    MessageConstants.SECURITY_CODE_LENGTH);

	    tokenManager.saveToken(null, email, MAX_EXIST_TIME, code);

	    memberEmailManager.sendEmailValidateCode(code, email);// 发送验证码结束
	}
	returnResult.setResult(true);
	return returnResult;
    }

    /**
     * 手机验证方式发送验证码
     * 
     * @param request
     * @param response
     * @param model
     * @param forgetPasswordForm
     * @param bindingResult
     * @return
     * @throws Exception
     * 
     * @RequestMapping(value="/member/mobileSendValidateCode.json",method=RequestMethod.POST)
     */
    public NebulaReturnResult mobileSendValidateCode(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) throws Exception {

	String mobile = forgetPasswordForm.getMobile();
	DefaultReturnResult returnResult = new DefaultReturnResult();
	DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

	LOGGER.info("忘记密码的用户mobile为--=" + mobile);

	// 此处需要对手机进行校验，查询手机号码是否符合规格，是否存在
	// 验证手机格式
	if (StringUtils.isNotEmpty(mobile)) {
	    Pattern p1 = Pattern.compile("^(1[3-9]{1}[0-9]{1})\\d{8}$");
	    if (!"".equals(mobile.trim())
		    && !p1.matcher(mobile.trim()).matches()) {
		returnResult.setResult(false);
		returnResult.setResultMessage(defaultResultMessage);
		return returnResult;
	    }
	}

	// 手机规则正确，通过手机号码查询用户是否存在
	MemberCommand memberCommand = sdkMemberManager
		.findMemberByLoginMobile(mobile);

	// 1.2 对应手机号的用户不存在
	if (Validator.isNullOrEmpty(memberCommand)) {
	    returnResult.setResult(false);
	    returnResult.setResultMessage(defaultResultMessage);
	    return returnResult;

	} else {
	    // 1.1 手机用户存在：则调用发送手机验证码的方法，发送相应的验证码到邮箱中
	    String code = SecurityCodeUtil.createSecurityCode(
		    MessageConstants.SECURITY_CODE_ORIGINAL_STRING,
		    MessageConstants.SECURITY_CODE_LENGTH);

	    // 保存验证码到redis中，并且设置验证码的生存时间
	    tokenManager.saveToken(null, mobile, MAX_EXIST_TIME, code);

	    // 调用发送短信验证码的方式发送短信验证码
	    MessageCommand messageCommand = new MessageCommand();

	    messageCommand.setSecurityCode(code);
	    messageCommand.setMobile(mobile);

	    smsManager.sendMessage(messageCommand);// 发送验证码结束
	}
	returnResult.setResult(true);
	return returnResult;
    }

    /**
     * 点击保存时，对比验证码是否正确
     * 
     * @RequestMapping(value =
     *                       "/member/checkValidateCode.htm",method=RequestMethod
     *                       .POST)
     * 
     */
    public String checkValidateCode(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) {
	// 检测输入的验证码是否和发送的验证码相同
	// 需要区分是手机验证方式还是邮件验证方式（1为手机验证方式，2为邮件验证方式）
	if (1 == forgetPasswordForm.getType()) {
	    if (VerifyResult.SUCESS == tokenManager.verifyToken(null,
		    forgetPasswordForm.getMobile(),
		    forgetPasswordForm.getSecurityCode())) {
		// 2.1 验证成功：则跳转到重置密码页面
		return VIEW_RESET_PASSWORD;

	    } else {
		// 2.2 验证失败：则重新跳转到忘记密码页面，要求重新进行验证
		return VIEW_FORGET_PASSWORD;
	    }
	} else {
	    if (VerifyResult.SUCESS == tokenManager.verifyToken(null,
		    forgetPasswordForm.getEmail(),
		    forgetPasswordForm.getSecurityCode())) {
		// 2.1 验证成功：则跳转到重置密码页面
		return VIEW_RESET_PASSWORD;
	    } else {
		// 2.2 验证失败：则重新跳转到忘记密码页面，要求重新进行验证
		return VIEW_FORGET_PASSWORD;
	    }
	}

    }

    /**
     * 重置密码
     * 
     * @RequestMapping(value = "/member/resetpassword.htm", method =
     *                       RequestMethod.POST)
     * 
     */
    public String resetPassword(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) {

	// 获取到页面表单数据，进行比较后，调用service的方法，将原来的密码覆盖掉
	// 前台传过来的密码解密后的密码
	String newPassword = decryptSensitiveDataEncryptedByJs(
		forgetPasswordForm.getNewPassword(), request);
	String confirmPassword = decryptSensitiveDataEncryptedByJs(
		forgetPasswordForm.getConfirmPassword(), request);
	/* 判断密码是否为空 */
	if (newPassword != null && confirmPassword != null
		&& StringUtils.isNotEmpty(newPassword)
		&& StringUtils.isNotEmpty(confirmPassword)) {
	    /* 不为空 */
	    /* 初始化id */
	    Long memberId = 0l;
	    String loginName = "";
	    if (newPassword.equals(confirmPassword)) {
		/*
		 * 两次输入的密码相等，则判断是邮箱验证还是手机验证，
		 * 若是手机验证方式，则通过登录手机号查询登录的用户信息（获取到用户id）；
		 * 若是邮箱验证方式，则通过登录邮箱获取用户信息（获取到用户id）
		 */
		if (forgetPasswordForm.getType() == 2) {
		    MemberCommand memberCommand = sdkMemberManager
			    .findMemberByLoginEmail(forgetPasswordForm
				    .getEmail());
		    memberId = memberCommand.getId();
		    loginName = memberCommand.getLoginName();

		} else if (forgetPasswordForm.getType() == 1) {

		    MemberCommand memberCommand = sdkMemberManager
			    .findMemberByLoginMobile(forgetPasswordForm
				    .getMobile());
		    memberId = memberCommand.getId();
		    loginName = memberCommand.getLoginName();
		}

		// 调用service层的方法，用新密码替换掉对应原来的密码（newpassword需要加密处理)
		// 加密新密码
		String codePassword = EncryptUtil.getInstance().hash(
			newPassword, loginName);
		// 更新密码
		boolean resetPassword = sdkMemberManager.resetPasswd(memberId,
			codePassword);

		if (resetPassword) {
		    /* 修改密码成功，跳转至修改成功页面 */
		    String short4 = new Date().getTime() + "";
		    memberExtraManager.rememberPwd(memberId, short4);
		    return VIEW_RESET_PASSWORD_SUCCESS;
		}
		/* 修改密码失败，（可以提示修改密码失败，几秒后跳转至重置密码页面）可重新跳转至重置密码页面 */
		return VIEW_RESET_PASSWORD;
	    }
	}
	/* 重置密码时，输入的密码为空，则重新跳转至重置密码页面 */
	return VIEW_RESET_PASSWORD;
    }
}
