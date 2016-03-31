package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberPasswordManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;

/**
 * 忘记密码Test
 * 
 * @author Wanrong.Wang
 */
public class NebulaForgetPasswordControllerTest extends BaseControllerTest{

	private NebulaForgetPasswordController	nebulaForgetPasswordController;

	private ForgetPasswordFormValidator		forgetPasswordFormValidator;

	private TokenManager					tokenManager;

	private MemberPasswordManager			memberPasswordManager;

	private MemberCommand					memberCommand;

	private HttpSession						session;

	private BindingResult					bindingResult;

	private ForgetPasswordForm				forgetPasswordForm;

	private String							email						= "wanrong.wang@baozun.com";

	private static final String				TOKEN						= "VALIDATED_USER_MESSAGE_KEY";

	/* 重置密码成功的页面定义 */
	public static final String				VIEW_RESET_PASSWORD_SUCCESS	= "resetpassword.success";

	public static final String				PASSWORD					= "password";

	public static final String				CONFIRM_PASSWORD			= "confirmPassword";

	@Before
	public void setUp(){

		// 要验证的controller和validate都需要new出来
		nebulaForgetPasswordController = new NebulaForgetPasswordController();
		forgetPasswordFormValidator = new ForgetPasswordFormValidator();

		// 创建Mock对象
		tokenManager = control.createMock("tokenManager", TokenManager.class);
		memberPasswordManager = control.createMock("memberPasswordManager", MemberPasswordManager.class);
		session = control.createMock("HttpSession", HttpSession.class);
		// 此步骤相当于将创建好的对象都注入到对应的controller中去
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "tokenManager", tokenManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "memberPasswordManager", memberPasswordManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "forgetPasswordFormValidator", forgetPasswordFormValidator);
	}

	/**
	 * 测试发送验证码
	 */
	@Test
	public void testSendValidateCode(){
		try{

			memberCommand = new MemberCommand();
			memberCommand.setLoginEmail(email);
			// 初始化forgetPasswordForm参数
			forgetPasswordForm = new ForgetPasswordForm();
			forgetPasswordForm.setEmail(email);
			forgetPasswordForm.setType(2);
			bindingResult = mockBindingResult(forgetPasswordForm);
			DefaultReturnResult returnResult = new DefaultReturnResult();
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			returnResult.setResult(true);
			returnResult.setResultMessage(defaultResultMessage);
			// 没有返回值的数据模拟

			EasyMock.expect(memberPasswordManager.sendValidateCode(forgetPasswordForm)).andReturn(true);
			EasyMock.expect(request.getSession()).andReturn(session);

			session.setAttribute(TOKEN, forgetPasswordForm);
			EasyMock.expectLastCall();

			control.replay();
			DefaultReturnResult sendValidateCode = (DefaultReturnResult) nebulaForgetPasswordController.sendValidateCode(
					request,
					response,
					model,
					forgetPasswordForm,
					bindingResult);
			assertEquals(true, (boolean) sendValidateCode.isResult());

			control.verify();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 测试重置密码
	 */
	@Test
	public void testResetPassword(){
		try{

			memberCommand = new MemberCommand();
			memberCommand.setLoginEmail(email);

			// 初始化forgetPasswordForm参数
			forgetPasswordForm = new ForgetPasswordForm();
			forgetPasswordForm.setEmail(email);
			forgetPasswordForm.setType(2);
			bindingResult = mockBindingResult(forgetPasswordForm);

			// EasyMock.expect(sdkMemberManager.findMemberByLoginEmail(forgetPasswordForm.getEmail())).andReturn(memberCommand);
			EasyMock.expect(memberPasswordManager.resetPassword(forgetPasswordForm, "123")).andReturn(true);
			EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
			EasyMock.expect(session.getAttribute(TOKEN)).andReturn(forgetPasswordForm).anyTimes();
			EasyMock.expect(request.getParameter(PASSWORD)).andReturn("123").anyTimes();
			EasyMock.expect(request.getParameter(CONFIRM_PASSWORD)).andReturn("123").anyTimes();

			control.replay();

			assertEquals(
					VIEW_RESET_PASSWORD_SUCCESS,
					nebulaForgetPasswordController.resetPassword(request, response, model, forgetPasswordForm, bindingResult));

			control.verify();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 测试页面跳转
	 */
	@Test
	public void testShowForgetPassword(){
		control.replay();
		// 验证结果
		assertEquals(NebulaForgetPasswordController.VIEW_FORGET_PASSWORD, nebulaForgetPasswordController.showForgetPassword());
		control.verify();
	}
}
