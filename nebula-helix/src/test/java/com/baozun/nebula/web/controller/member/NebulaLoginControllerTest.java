/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;

/**
 * 登录controller测试类
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年3月24日 上午11:21:46
 */
public class NebulaLoginControllerTest extends BaseControllerTest{

	private NebulaLoginController	nebulaLoginController;

	private LoginFormValidator		loginFormValidator;

	private MemberManager			memberManager;

	private MemberExtraManager		memberExtraManager;

	@Before
	public void setUp(){
		nebulaLoginController = new NebulaLoginController();
		loginFormValidator = new LoginFormValidator();

		memberManager = EasyMock.createMock("memberManager", MemberManager.class);
		memberExtraManager = EasyMock.createMock("memberExtraManager", MemberExtraManager.class);

		ReflectionTestUtils.setField(nebulaLoginController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaLoginController, "loginFormValidator", loginFormValidator);
		ReflectionTestUtils.setField(nebulaLoginController, "memberExtraManager", memberExtraManager);
	}

	@Test
	public void testShowLogin(){
		control.replay();
		MemberDetails memberDetails = null;
		// 验证结果
		assertEquals(NebulaLoginController.VIEW_MEMBER_LOGIN, nebulaLoginController.showLogin(memberDetails, request, response, model));
		control.verify();
	}

	@Test
	public void testLogin(){
		// Replay
		control.replay();
		LoginForm loginForm = new LoginForm();
		
		loginForm.setLoginName("minglei");
		loginForm.setPassword("123456");

		BindingResult bindingResult =mockBindingResult(loginForm);
		//new BindException(loginForm, "loginForm");

		assertEquals(DefaultReturnResult.SUCCESS, nebulaLoginController.login(loginForm, bindingResult, request, response, model));

		// 验证交互行为
		control.verify();
	}
}
