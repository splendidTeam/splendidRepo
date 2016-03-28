/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;
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

	private MemberCommand			memberCommand;

	private BindingResult			bindingResult;

	private LoginForm				loginForm;
	
	private HttpSession				session;

	@Before
	public void setUp(){
		memberCommand = new MemberCommand();
		memberCommand.setId(1L);

		// 初始化登录参数
		loginForm = new LoginForm();
		loginForm.setLoginName("minglei");
		loginForm.setPassword("123456");
		loginForm.setIsRemberMeLoginName(true);
		bindingResult = mockBindingResult(loginForm);

		nebulaLoginController = new NebulaLoginController();
		loginFormValidator = new LoginFormValidator();

		memberManager =  control.createMock("memberManager", MemberManager.class);
		memberExtraManager = control.createMock("memberExtraManager", MemberExtraManager.class);
		session = control.createMock("HttpSession", HttpSession.class);

		ReflectionTestUtils.setField(nebulaLoginController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaLoginController, "memberExtraManager", memberExtraManager);
		ReflectionTestUtils.setField(nebulaLoginController, "loginFormValidator", loginFormValidator);
	}

	@Test
	public void testLogin(){
		try{
			EasyMock.expect(memberManager.login(EasyMock.isA(MemberFrontendCommand.class))).andReturn(memberCommand);
			
			EasyMock.expect(request.getSession()).andReturn(session);
			control.replay();
			assertEquals(DefaultReturnResult.SUCCESS, nebulaLoginController.login(loginForm, bindingResult, request, response, model));
			control.verify();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	public void testShowLogin(){
		MemberDetails memberDetails = null;
		control.replay();
		// 验证结果
		assertEquals(NebulaLoginController.VIEW_MEMBER_LOGIN, nebulaLoginController.showLogin(memberDetails, request, response, model));
		control.verify();
	}

}
