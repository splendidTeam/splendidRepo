package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberEmailManager;
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
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
@SuppressWarnings("unused")
public class NebulaForgetPasswordControllerTest extends BaseControllerTest{

	private NebulaForgetPasswordController	nebulaForgetPasswordController;

	private ForgetPasswordFormValidator		forgetPasswordFormValidator;

	private MemberExtraManager				memberExtraManager;

	private SdkMemberManager				sdkMemberManager;

	private TokenManager					tokenManager;

	private MemberCommand					memberCommand;

	private BindingResult					bindingResult;

	private ForgetPasswordForm				forgetPasswordForm;

	private String							email	= "wanrong.wang@baozun.com";

	private String							mobile	= "18291809551";

	private MemberEmailManager				memberEmailManager;

	private SdkSMSManager					smsManager;

	@Before
	public void setUp(){

		// 要验证的controller和validate都需要new出来
		nebulaForgetPasswordController = new NebulaForgetPasswordController();
		forgetPasswordFormValidator = new ForgetPasswordFormValidator();

		// 创建Mock对象
		memberExtraManager = control.createMock("memberExtraManager", MemberExtraManager.class);
		sdkMemberManager = control.createMock("sdkMemberManager", SdkMemberManager.class);
		memberEmailManager = control.createMock("memberEmailManager", MemberEmailManager.class);
		smsManager = control.createMock("smsManager", SdkSMSManager.class);
		tokenManager = control.createMock("tokenManager", TokenManager.class);

		// 此步骤相当于将创建好的对象都注入到对应的controller中去
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "sdkMemberManager", sdkMemberManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "memberExtraManager", memberExtraManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "forgetPasswordFormValidator", forgetPasswordFormValidator);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "memberEmailManager", memberEmailManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "smsManager", smsManager);
		ReflectionTestUtils.setField(nebulaForgetPasswordController, "tokenManager", tokenManager);
	}

	/**
	 * 测试邮箱发送验证码
	 */
	@Test
	public void testEmailSendValidateCode(){
		try{

			memberCommand = new MemberCommand();
			memberCommand.setLoginEmail(email);

			// 初始化forgetPasswordForm参数
			forgetPasswordForm = new ForgetPasswordForm();
			forgetPasswordForm.setEmail(email);
			// forgetPasswordForm.setType(2);
			// forgetPasswordForm.setNewPassword("123");
			// forgetPasswordForm.setConfirmPassword("123");
			// bindingResult = mockBindingResult(forgetPasswordForm);

			EasyMock.expect(sdkMemberManager.findMemberByLoginEmail(forgetPasswordForm.getEmail())).andReturn(memberCommand);

			// 没有返回值的数据模拟
			tokenManager.saveToken(null, email, 60, "code");
			EasyMock.expectLastCall();

			control.replay();

			DefaultReturnResult returnResult = new DefaultReturnResult();
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			returnResult.setResultMessage(defaultResultMessage);
			returnResult.setResult(true);
			// assertEquals(
			// DefaultReturnResult.SUCCESS,
			// nebulaForgetPasswordController.emailSendValidateCode(request, response, model, forgetPasswordForm, bindingResult));

			control.verify();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 测试手机发送验证码
	 */
	@Test
	public void testmobileSendValidateCode(){
		try{

			memberCommand = new MemberCommand();
			memberCommand.setLoginMobile(mobile);

			// 初始化forgetPasswordForm参数
			forgetPasswordForm = new ForgetPasswordForm();
			forgetPasswordForm.setMobile(mobile);
			bindingResult = mockBindingResult(forgetPasswordForm);

			EasyMock.expect(sdkMemberManager.findMemberByLoginMobile(mobile)).andReturn(memberCommand);
			control.replay();
			// assertEquals(
			// DefaultReturnResult.SUCCESS,
			// nebulaForgetPasswordController.mobileSendValidateCode(request, response, model, forgetPasswordForm, bindingResult));
			// control.verify();

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
