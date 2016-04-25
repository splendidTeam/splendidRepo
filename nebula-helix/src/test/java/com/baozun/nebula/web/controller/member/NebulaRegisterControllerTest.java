/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.utils.RegulareExpUtils;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.RegisterFormMobileValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormNormalValidator;

/**
 * NebulaRegisterControllerTest
 * 
 * @author Viktor Huang
 * @date 2016年3月22日 下午2:54:36
 */
public class NebulaRegisterControllerTest extends BaseControllerTest{

	private NebulaRegisterController	nebulaRegisterController;

	private RegisterFormNormalValidator	registerFormNormalValidator;

	private RegisterFormMobileValidator	registerFormMobileValidator;

	private MemberManager				memberManager;

	private SdkMemberManager			sdkMemberManager;

	private RegisterForm				registerForm;

	private BindingResult				bindingResult;

	private MemberCommand				memberCommand;

	private RegulareExpUtils			regulareExpUtils;

	private String						loginEmail	= "yi.huang@baozun.cn";

	private String						LoginMobile	= "151618580940";

	@Before
	public void setUp(){
		registerForm = new RegisterForm();

		registerForm.setLoginEmail(loginEmail);

		memberCommand = new MemberCommand();
		memberCommand.setId(11L);
		memberCommand = null;

		nebulaRegisterController = new NebulaRegisterController();

		registerFormNormalValidator = new RegisterFormNormalValidator();

		registerFormMobileValidator = new RegisterFormMobileValidator();

		memberManager = control.createMock("memberManager", MemberManager.class);
		sdkMemberManager = control.createMock("sdkMemberManager", SdkMemberManager.class);

		regulareExpUtils = control.createMock("regulareExpUtils", RegulareExpUtils.class);

		bindingResult = mockBindingResult(registerForm);

		ReflectionTestUtils.setField(nebulaRegisterController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaRegisterController, "sdkMemberManager", sdkMemberManager);

		ReflectionTestUtils.setField(nebulaRegisterController, "registerFormNormalValidator", registerFormNormalValidator);
		ReflectionTestUtils.setField(nebulaRegisterController, "registerFormMobileValidator", registerFormMobileValidator);
	}

	@Test
	public void testShowRegister(){

		// Replay
		control.replay();

		MemberDetails memberDetails = new MemberDetails();
		memberDetails = null;
		// 验证结果
		assertEquals(NebulaRegisterController.VIEW_MEMBER_REGISTER, nebulaRegisterController.showRegister(memberDetails, model, request));
		// 验证交互行为
		control.verify();
	}

	@Test
	public void testCheckLoginEmailAvailable(){

		EasyMock.expect(sdkMemberManager.findMemberByLoginEmail(loginEmail)).andReturn(memberCommand);

		// EasyMock.replay(sdkMemberManager);

		control.replay();

		assertEquals(DefaultReturnResult.SUCCESS, nebulaRegisterController.checkLoginEmailAvailable(loginEmail));

		control.verify();
	}

	@Test
	public void testCheckLoginMobileAvailable(){

		EasyMock.expect(sdkMemberManager.findMemberByLoginMobile(LoginMobile)).andReturn(memberCommand);

		control.replay();

		assertEquals(DefaultReturnResult.SUCCESS, nebulaRegisterController.checkLoginMobileAvailable(LoginMobile));

		control.verify();

	}

	@Test
	public void testSendRegisterMobileMessage(){
		// TODO mock static method
		// EasyMock.expect(RegulareExpUtils.isMobileNO(LoginMobile)).andReturn(true);
		control.replay();

		assertEquals(DefaultReturnResult.SUCCESS, nebulaRegisterController.sendRegisterMobileMessage(request, response, model, LoginMobile));

		control.verify();
	}

	@Test
	public void testRegister(){
		// TODO mock suplerclass protected method
	}
}
