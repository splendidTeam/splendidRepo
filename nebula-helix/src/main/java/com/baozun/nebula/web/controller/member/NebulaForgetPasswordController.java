package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;

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
 * 
 * 
 * @Controller
 * 
 */

public class NebulaForgetPasswordController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(NebulaForgetPasswordController.class);

    @Autowired
    private SdkMemberManager sdkMemberManager;
    
    @Autowired
	private MemberManager memberManager;

    /**
     * 跳转到忘记密码的页面
     * 
     * @return String
     * @RequestMapping(value = "/forgetpassword.htm", method =
     *                       RequestMethod.GET)
     */

    public String toForgetPasswordPage() {
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
	    return "redirect:/member/forgetPswUsemobileToValidate.htm";
	} else if (type == 2) {
	    // 为邮箱验证方式
	    return "redirect:/member/forgetPswUseEmailToValidate.htm";
	} else {
	    // 未选择验证方式，抛出异常 （这种情况的可能性应该不存在）
	    throw new RuntimeException("类型错误");
	}
    }

    /**
     * 邮箱验证方式
     * 
     * @param response
     * @param request
     * @param model
     * @param email
     * 
     * @return
     * 
     * @RequestMapping(value = "/member/forgetPswUseEmailToValidate.htm",method
     *                       = RequestMethod.POST)
     * 
     */

    public String useEmailToValidate(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @RequestParam("email") String email,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm ForgetPasswordForm,
	    BindingResult bindingResult) {

	MemberCommand memberCommand = null;

	// 1,检测邮箱是否存在（此处表示邮箱输入框里是有值的）
	if (email.indexOf('@') > -1) {

	    // 通过邮箱查询用户是否存在
	    memberCommand = sdkMemberManager.findMemberByLoginEmail(email);
	    LOGGER.info("忘记密码的用户email为--=" + email);

	    // 1.2 对应邮箱的用户不存在：在输入邮箱的时候提示邮箱不存在
	    if (Validator.isNullOrEmpty(memberCommand)) {
		request.setAttribute("loginEmail_error", "该账号不存在");
		/* 返回FAILED给前台，前台通过js判断，如果为failed，则提示错误信息（ajax请求） */
		return "FAILED";

	    } else {
		// 1.1 邮箱用户存在：则调用发送邮件的方法，发送相应的修改密码的链接和验证码到邮箱中

		/**
		 * 此处调用发送邮件的方法，待解决
		 * 
		 * 假设调用成功
		 */

	    }

	}

	// 2,检测输入的验证码是否和发送的验证码相同
	// 获取到发送过去的验证码(假设获取到发送的验证码为123)
	String sendCode = "123";
	if (sendCode.equals(ForgetPasswordForm.getSecurityCode())) {
	    // 2.1 相同：则跳转到重置密码页面
	    return "store.login.resetpassword";

	} else {
	    // 2.2 输入不相同：则重新跳转到忘记密码页面，要求重新进行验证
	    return "store.login.forgetpassword";
	}
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
     * 
     * @RequestMapping(value = "/member/forgetPswUsemobileToValidate.htm")
     * 
     */

    public String useMobileToValidate(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Model model,
	    @RequestParam("mobile") String mobile,
	    @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
	    BindingResult bindingResult) {

	MemberCommand memberCommand = null;

	// 1,检测手机是否存在

	// 通过手机查询用户是否存在
	memberCommand = sdkMemberManager.findMemberByLoginMobile(mobile);
	LOGGER.info("忘记密码的用户为--=" + mobile);

	// 1.2 对应手机的用户不存在：在输入手机的时候提示手机不存在
	if (Validator.isNullOrEmpty(memberCommand)) {
	    model.addAttribute("loginMobile_error", "该账号不存在");
	    return "FAILED";

	} else {
	    // 1.1 手机用户存在：则调用发送短信的方法，发送相应的修改密码的验证码到手机中

	    /**
	     * 此处调用发送短信的方法，待解决
	     * 
	     * 假设调用成功
	     */

	}

	// 2,检测输入的验证码是否和发送的验证码相同
	// 获取到发送过去的验证码(假设获取到发送的验证码为123)
	String sendCode = "123";
	if (sendCode.equals(forgetPasswordForm.getSecurityCode())) {
	    // 2.1 相同：则跳转到重置密码页面
	    return "store.login.resetpassword";

	} else {
	    // 2.2 输入不相同：则重新跳转到忘记密码页面，要求重新进行验证
	    return "store.login.forgetpassword";
	}
    }

    /**
     * 验证完成后，跳转至重置密码页面
     * 
     * @return String
     * @RequestMapping(value = "/resetpassword.htm", method = RequestMethod.GET)
     * 
     */

    public String toResetPasswordPage() {

	return "store.login.resetpassword";
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
	String newPassword = forgetPasswordForm.getNewPassword();
	String confirmPassword = forgetPasswordForm.getConfirmPassword();
	/* 判断密码是否为空 */
	if (newPassword != null && confirmPassword != null
		&& StringUtils.isNotEmpty(newPassword)
		&& StringUtils.isNotEmpty(confirmPassword)) {
	    /* 不为空 */
	    /* 初始化id */
	    Long memberId = 0l;
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

		} else if (forgetPasswordForm.getType() == 1) {

		    MemberCommand memberCommand = sdkMemberManager
			    .findMemberByLoginMobile(forgetPasswordForm
				    .getMobile());
		    memberId = memberCommand.getId();
		}

		// 调用service层的方法，用新密码替换掉对应原来的密码（newpassword需要加密处理，处理方式待定）
		boolean resetPassword = sdkMemberManager.resetPasswd(memberId,
			newPassword);

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
