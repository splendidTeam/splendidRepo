package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.manager.member.MemberPasswordManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;
import com.baozun.nebula.web.controller.member.validator.MemberPasswordFormValidator;

public class NebulaModifyPasswordControllerTest extends BaseControllerTest{

	private NebulaModifyPasswordController	nebulaModifyPasswordController;

	private MemberPasswordFormValidator		memberPasswordFormValidator;

	private MemberPasswordManager			memberPasswordManager;

	private BindingResult					result;

	private String							email	= "wanrong.wang@baozun.com";

	private String							mobile	= "18291809551";

	@Before
	public void setUp(){

		// 要验证的controller和validate都需要new出来
		nebulaModifyPasswordController = new NebulaModifyPasswordController();
		memberPasswordFormValidator = new MemberPasswordFormValidator();

		// 创建Mock对象
		memberPasswordManager = control.createMock("memberPasswordManager", MemberPasswordManager.class);

		// 此步骤相当于将创建好的对象都注入到对应的controller中去
		ReflectionTestUtils.setField(nebulaModifyPasswordController, "memberPasswordManager", memberPasswordManager);
		ReflectionTestUtils.setField(nebulaModifyPasswordController, "memberPasswordFormValidator", memberPasswordFormValidator);
	}

	/**
	 * 测试修改密码
	 */
	@Test
	public void testModifyPassword(){
		try{
			MemberDetails memberDetails = new MemberDetails();
			memberDetails.setLoginEmail(email);
			memberDetails.setLoginName("haha");
			memberDetails.setLoginMobile(mobile);
			memberDetails.setMemberId(1l);

			MemberPasswordForm memberPasswordForm = new MemberPasswordForm();

			memberPasswordForm.setNewPassword("123456Ww");
			memberPasswordForm.setOldPassword("222222ww");
			memberPasswordForm.setConfirmPassword("123456Ww");
			result = mockBindingResult(memberPasswordForm);

			EasyMock.expect(memberPasswordManager.modifyPassword("222222ww", "123456Ww", "123456Ww", 1l)).andReturn(true);

			// 没有返回值的数据模拟
			memberPasswordFormValidator.validate(memberPasswordForm, result);
			EasyMock.expectLastCall();

			control.replay();

			NebulaReturnResult nebulaReturnResult = nebulaModifyPasswordController.modifyPassword(
					memberDetails,
					memberPasswordForm,
					result,
					model,
					request,
					response);

			assertEquals(true, ((DefaultReturnResult) nebulaReturnResult).isResult());

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
		assertEquals(NebulaModifyPasswordController.VIEW_MODIFY_PASSWORD, nebulaModifyPasswordController.showModifyPassword());
		control.verify();
	}

}
