/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
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

	private RegisterForm			registerForm;

	private BindingResult			bindingResult;

	@Before
	public void setUp(){
		registerForm = new RegisterForm();

		nebulaLoginController = new NebulaLoginController();
		memberManager = EasyMock.createMock("memberManager", MemberManager.class);
		bindingResult = EasyMock.createMock("bindingResult", BindingResult.class);

		ReflectionTestUtils.setField(nebulaLoginController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaLoginController, "loginFormValidator", loginFormValidator);
		ReflectionTestUtils.setField(nebulaLoginController, "memberExtraManager", memberExtraManager);
	}

	@Test
	public void testShowLogin(){
	
	}

	@Test
	public void testLogin(){
		// Record
		// EasyMock.expect(sdkMemberManager.findContactsByMemberId(null, null, 1L)).andReturn(new Pagination<ContactCommand>());

		// Replay
		control.replay();

		

		// 验证交互行为
		control.verify();
	}
}
