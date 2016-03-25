package com.baozun.nebula.web.controller.member;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.constants.MessageConstants;
import com.baozun.nebula.manager.member.MemberEmailManager;
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
 * <pre>
 * 
 * 忘记密码 
 *  1，从登陆页面跳转到忘记密码页面
 *  2，在忘记密码页面进行身份的验证 判断下拉菜单中选择的是邮箱验证还是手机验证：
 * 	a，邮箱验证（前端发送ajax请求到后台，后台通过查询，最终验证该邮箱是否存在）：
 * 	     邮箱存在时，点击获取验证码，调用发送邮件的方法（到哪里去调啊？），发送验证码；
 *        输入邮箱中收到的验证码（如何判断输入的验证码是否和发送的验证码相同？大概思路：发送时做一个记录，然后拿这个数据跟输入的数据进行比较）
 * 		问题：验证码输入的正确与否是直接在输入时发送ajax请求校验呢？还是在点击了提交按钮之后再去验证？
 * 	b，手机验证（前端发送ajax请求到后台，后台通过查询，最终验证该手机号是否存在）
 * 	    手机号存在时，点击获取验证码，调用发送短信的方法（到哪里去调啊？），发送验证码；
 * 	    输入手机收到的验证码（如何判断输入的验证码是否和发送的验证码相同？大概思路：发送时做一个记录《用redis来存储》，然后拿这个数据跟输入的数据进行比较）
 * 		问题：验证码输入的正确与否是直接在输入时发送ajax请求校验呢？还是在点击了提交按钮之后再去验证？
 *  3，验证成功后跳转到重置密码页面；
 * 
 * 重置密码
 *  4，重置密码需要输入两次密码：一次为新密码，一次为确认新密码（js验证密码的格式是否符合要求，后台是否需要再次验证？）。
 *  5，提交页面修改了密码后的表单，controller接收表单数据，调用service层的方法，对对应用户下的密码进行重置操作。（将原有密码直接覆盖掉？还是要删除掉原来的密码？）
 *  6，密码设置完成后，跳转到密码重置成功页面（此处代码可以与修改密码通用）
 * 
 * 问题整理： 
 *  1,获取验证码有没有次数限制？(待解决)
 *  2，在哪里去调用发送邮件和发送手机验证码的方法？（后续通知）
 *  3，如何判断输入的验证码是否和发送的验证码相同？（待解决）
 *  4，验证码输入的正确与否是直接在输入时发送ajax请求校验呢？还是在点击了提交按钮之后再去验证？（两次都需要验证）
 *  5，js验证密码的格式是否符合要求，后台是否需要再次验证？（要的） 
 *  6，将原有密码直接覆盖掉？还是要删除掉原来的密码？（覆盖）
 *  7，再次发送验证码的时间间隔为1分钟，该怎么实现？计时器？（待解决）
 * 
 * </pre>
 * 
 * @author Wanrong.Wang
 * @Date 2016/03/22
 * @Controller
 * 
 */

public class NebulaForgetPasswordController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(NebulaForgetPasswordController.class);

    private static final int MAX_EXIST_TIME = 60;

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private ForgetPasswordFormValidator forgetPasswordFormValidator;

    @Autowired
    private MemberEmailManager memberEmailManager;

    @Autowired
    private SmsManager smsManager;

    @Autowired
    private TokenManager tokenManager;

    /**
     * 跳转到忘记密码的页面
     * 
     * @return String
     * @RequestMapping(value = "/forgetpassword.htm", method =
     *                       RequestMethod.GET)
     */

    public String showForgetPassword() {
	return "store.login.forgetpassword";
    }

    /**
     * <pre>
     * 待定方法： 
     * 判断验证方式是邮箱验证还是手机验证：邮箱验证：type=2； 手机验证：type=1
     * </pre>
     * 
     * @param type
     * @return String
     */

    public String isEmailOrmobile(Integer type) {
	if (type == 1) {
	    // 为手机验证方式
	    return "forward:/member/forgetPswUsemobileToValidate.htm";
	} else if (type == 2) {
	    // 为邮箱验证方式
	    return "forward:/member/forgetPswUseEmailToValidate.htm";
	}
	return null;
    }

    /**
     * 邮箱验证方式发送验证码
     * 
     * @param request
     * @param response
     * @param model
     * @param email
     * @param forgetPasswordForm
     * @param bindingResult
     * @return
     * @throws Exception
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
     * @param email
     * @param forgetPasswordForm
     * @param bindingResult
     * @return
     * @throws Exception
     */

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
	// 2,检测输入的验证码是否和发送的验证码相同
	// 获取到发送过去的验证码(假设获取到发送的验证码为123)
	if (1 == forgetPasswordForm.getType()) {
	    if (VerifyResult.SUCESS == tokenManager.verifyToken(null,
		    forgetPasswordForm.getMobile(),
		    forgetPasswordForm.getSecurityCode())) {
		// 2.1 相同：则跳转到重置密码页面
		return "store.login.resetpassword";

	    } else {
		// 2.2 输入不相同：则重新跳转到忘记密码页面，要求重新进行验证
		return "store.login.forgetpassword";
	    }
	} else {
	    if (VerifyResult.SUCESS == tokenManager.verifyToken(null,
		    forgetPasswordForm.getEmail(),
		    forgetPasswordForm.getSecurityCode())) {
		// 2.1 相同：则跳转到重置密码页面
		return "store.login.resetpassword";
	    } else {
		// 2.2 输入不相同：则重新跳转到忘记密码页面，要求重新进行验证
		return "store.login.forgetpassword";
	    }
	}

    }

    /**
     * 发送验证码
     * 
     * @param request
     * @param response
     * @param model
     * @param email
     * @param forgetPasswordForm
     * @param bindingResult
     * @return
     * @throws Exception
     */
    public NebulaReturnResult mobleSendValidateCode(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) throws Exception {

	DefaultReturnResult returnResult = new DefaultReturnResult();
	DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

	LOGGER.info("忘记密码的用户Mobile为--=" + forgetPasswordForm.getMobile());

	// 此处只需要对邮箱进行校验，查询邮箱是否符合规格，是否存在
	// 验证邮箱格式
	if (StringUtils.isNotEmpty(forgetPasswordForm.getMobile())) {
	    Pattern p1 = Pattern.compile("^[0-9]{11}$");
	    if (!"".equals(forgetPasswordForm.getMobile().trim())
		    && !p1.matcher(forgetPasswordForm.getMobile().trim())
			    .matches()) {
		returnResult.setResult(false);
		returnResult.setResultMessage(defaultResultMessage);
		return returnResult;
	    }
	}

	// 邮箱规则正确，通过邮箱查询用户是否存在
	MemberCommand memberCommand = sdkMemberManager
		.findMemberByLoginMobile(forgetPasswordForm.getMobile());

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

	    tokenManager.saveToken(null, forgetPasswordForm.getMobile(),
		    MAX_EXIST_TIME, code);

	    memberEmailManager.sendEmailValidateCode(code,
		    forgetPasswordForm.getMobile());// 发送验证码结束
	}
	returnResult.setResult(true);
	return returnResult;
    }

    /**
     * 手机验证方式
     * 
     * @param response
     * @param request
     * @param model
     * @param mobile
     * 
     * @return
     * @throws Exception
     * 
     * @RequestMapping(value = "/member/forgetPswUsemobileToValidate.htm")
     * 
     */

    // public String useMobileToValidate(
    // HttpServletRequest request,
    // HttpServletResponse response,
    // Model model,
    // @RequestParam("mobile") String mobile,
    // @ModelAttribute("forgetPasswordForm") ForgetPasswordForm
    // forgetPasswordForm,
    // BindingResult bindingResult) throws Exception {
    //
    // MemberCommand memberCommand = null;
    //
    // // 1,检测手机是否存在
    //
    // // 通过手机查询用户是否存在
    // memberCommand = sdkMemberManager.findMemberByLoginMobile(mobile);
    // LOGGER.info("忘记密码的用户为--=" + mobile);
    // HttpSession session = request.getSession();
    //
    // // 1.2 对应手机的用户不存在：在输入手机的时候提示手机不存在
    // if (Validator.isNullOrEmpty(memberCommand)) {
    // model.addAttribute("loginMobile_error", "该账号不存在");
    // return "FAILED";
    //
    // } else {
    // // 1.1 手机用户存在：则调用发送短信的方法，发送相应的修改密码的验证码到手机中
    //
    // MessageCommand messageCommand = new MessageCommand();
    //
    // messageCommand.setMobile(mobile);
    // messageCommand.setType(1);
    //
    // String code = SecurityCodeUtil.createSecurityCode(
    // MessageConstants.SECURITY_CODE_ORIGINAL_STRING,
    // MessageConstants.SECURITY_CODE_LENGTH);
    //
    // smsManager.sendMessage(messageCommand);
    //
    // session.setAttribute("mobile_validate_code", code);
    //
    // }
    //
    // // 2,检测输入的验证码是否和发送的验证码相同
    //
    // String sendCode = (String) session.getAttribute("email_validate_code");
    //
    // if (sendCode.equals(forgetPasswordForm.getSecurityCode())) {
    // // 2.1 相同：则跳转到重置密码页面
    // return "store.login.resetpassword";
    //
    // } else {
    // // 2.2 输入不相同：则重新跳转到忘记密码页面，要求重新进行验证
    // return "store.login.forgetpassword";
    // }
    // }

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
		    return "resetpassword.success";
		}
		/* 修改密码失败，（可以提示修改密码失败，几秒后跳转至重置密码页面）可重新跳转至重置密码页面 */
		return "store.login.resetpassword";
	    }
	}
	/* 重置密码时，输入的密码为空，则重新跳转至重置密码页面 */
	return "store.login.resetpassword";
    }

}
