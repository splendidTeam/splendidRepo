/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

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

	private MemberCommand			member;
	
	@Before
	public void setUp(){
		member = new MemberCommand();
		member.setId(11L);
		
		
		nebulaLoginController = new NebulaLoginController();
		loginFormValidator = new LoginFormValidator();

		memberManager = control.createMock("memberManager", MemberManager.class);
		memberExtraManager = control.createMock("memberExtraManager", MemberExtraManager.class);

		ReflectionTestUtils.setField(nebulaLoginController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaLoginController, "memberExtraManager", memberExtraManager);
		ReflectionTestUtils.setField(nebulaLoginController, "loginFormValidator", loginFormValidator);		
	}

	
	@Test
	public void testLogin(){
		try{
			LoginForm loginForm = new LoginForm();
			
			//初始化登录参数
			loginForm.setLoginName("minglei");
			loginForm.setPassword("123456");
			loginForm.setIsRemberMeLoginName(true);
			
			BindingResult bindingResult =mockBindingResult(loginForm);			
			
			
			EasyMock.expect(memberManager.login(new MemberFrontendCommand())).andReturn(member);			
			
			control.replay();

			assertEquals(DefaultReturnResult.SUCCESS, nebulaLoginController.login(loginForm, bindingResult, request, response, model));

			control.verify();		
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testShowLogin(){
		control.replay();
		MemberDetails memberDetails = null;
		// 验证结果
		assertEquals(NebulaLoginController.VIEW_MEMBER_LOGIN, nebulaLoginController.showLogin(memberDetails, request, response, model));
		control.verify();
	}

}
