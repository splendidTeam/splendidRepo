/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;
import loxia.dao.Pagination;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.RegisterFormValidator;

/**
 * NebulaRegisterControllerTest
 * 
 * @author Viktor Huang
 * @date 2016年3月22日 下午2:54:36
 */
public class NebulaRegisterControllerTest extends BaseControllerTest{

	private NebulaRegisterController	nebulaRegisterController;

	private RegisterFormValidator		registerFormValidator;

	private MemberManager				memberManager;

	private RegisterForm				registerForm;

	private BindingResult				bindingResult;

	@Before
	public void setUp(){
		registerForm = new RegisterForm();
		// registerForm.setEmail("viktorhuangchn@163.com");

		nebulaRegisterController = new NebulaRegisterController();
		registerFormValidator = new RegisterFormValidator();

		memberManager = EasyMock.createMock("memberManager", MemberManager.class);
		bindingResult = EasyMock.createMock("bindingResult", BindingResult.class);

		ReflectionTestUtils.setField(nebulaRegisterController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaRegisterController, "registerFormValidator", registerFormValidator);
	}

	@Test
	public void testShowRegister(){

		// // Record
		// EasyMock.expect(sdkMemberManager.findContactsByMemberId(null, null, 1L)).andReturn(new Pagination<ContactCommand>());
		//
		// // Replay
		// control.replay();
		//
		// PageForm pageForm = new PageForm();
		// MemberDetails memberDetails = new MemberDetails();
		//
		// // 验证结果
		// assertEquals(
		// NebulaMemberAddressController.VIEW_MEMBER_ADDRESS_LIST,
		// nebulaMemberAddressController.showMemberAddress(memberDetails, pageForm, request, response, model));
		//
		// // 验证交互行为
		// control.verify();
	}

	@Test
	public void testRegister(){
		// Record
//		EasyMock.expect(sdkMemberManager.findContactsByMemberId(null, null, 1L)).andReturn(new Pagination<ContactCommand>());

		// Replay
		control.replay();

		PageForm pageForm = new PageForm();
		// MemberDetails memberDetails = new MemberDetails();

		// 验证结果
		assertEquals(null, nebulaRegisterController.register(registerForm, bindingResult, request, response, model));

		// 验证交互行为
		control.verify();
	}
}
