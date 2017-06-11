/**
 * 
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import loxia.dao.Pagination;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.member.converter.MemberViewCommandConverter;
import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
import com.baozun.nebula.web.controller.member.validator.MemberProfileFormValidator;
import com.baozun.nebula.web.controller.member.viewcommand.MemberViewCommand;

/**
 * MemberProfileController测试类
 * 
 * @author 王耀华
 * @version 1.0
 * @time 2016年3月25日 10:21:46
 */
public class NebulaMemberProfileControllerTest extends BaseControllerTest {

	private NebulaMemberProfileController memberProfileController;

	private MemberManager memberManager;

	private MemberProfileFormValidator memberProfileFormValidator;

	private MemberViewCommandConverter memberViewCommandConverter;

	private MemberProfileForm memberProfileForm;

	private MemberCommand memberCommand;

	private MemberPersonalData memberPersonalData;

	private MemberViewCommand memberViewCommand;

	private BindingResult bindingResult;

	private SdkMemberManager sdkMemberManager;

	@Before
	public void setUp() {
		memberProfileController = new NebulaMemberProfileController();
		memberProfileFormValidator = new MemberProfileFormValidator();
		memberProfileForm = new MemberProfileForm();
		memberViewCommandConverter = new MemberViewCommandConverter();
		memberCommand = new MemberCommand();
		memberViewCommand = new MemberViewCommand();
		memberViewCommand.setLoginName("yaohua");
		memberPersonalData = new MemberPersonalData();
		memberCommand.setId(1L);

		memberManager = EasyMock.createMock("memberManager",
				MemberManager.class);
		sdkMemberManager = control.createMock("sdkMemberManager",
				SdkMemberManager.class);
		bindingResult = control
				.createMock("bindingResult", BindingResult.class);

		ReflectionTestUtils.setField(memberProfileController, "memberManager",
				memberManager);
		ReflectionTestUtils.setField(memberProfileController,
				"memberProfileFormValidator", memberProfileFormValidator);
		ReflectionTestUtils.setField(memberProfileController,
				"memberViewCommandConverter", memberViewCommandConverter);

		ReflectionTestUtils.setField(memberProfileController,
				"sdkMemberManager", sdkMemberManager);

	}

	@Test
	public void testShowMemberProfile() {
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);
		memberDetails.setLoginName("yaohua");
		memberViewCommand.setLoginName("yaohua");

		EasyMock.expect(
				memberManager.findMemberById(memberDetails.getMemberId()))
				.andReturn(memberCommand);
		EasyMock.expect(memberViewCommandConverter.convert(memberCommand));

		// Replay
		control.replay();
		// 验证结果
		assertEquals(NebulaMemberProfileController.VIEW_MEMBER_PROFILE,
				memberProfileController.showMemberProfile(memberDetails,
						request, response, model));
		control.verify();
	}

	@Test
	public void testEditMemberProfile() {

		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);
		memberDetails.setLoginName("yaohua");
		memberCommand.setPassword("1123456");
		memberCommand.setOldPassword("1123456");
		memberCommand.setLoginEmail("yaohua.wang@baozun.cn");
		memberCommand.setLoginMobile("13402053342");

		memberProfileForm.setBirthday("2008-4-24");
		memberProfileForm.setLoginEmail("yaohua.wang@baozun.cn");
		memberProfileForm.setLoginMobile("13402053342");
		memberProfileForm.setLoginName("yaohua");
		memberProfileForm.setSex(1);
		memberProfileForm.setPassword("123456");
		memberProfileForm.setRepassword("123456");
		memberProfileForm.setOldPassword("1123456");

		EasyMock.expect(
				memberManager.findMemberById(memberDetails.getMemberId()))
				.andReturn(memberCommand);
		EasyMock.expect(
				memberManager.findMemberPersonData(memberDetails.getMemberId()))
				.andReturn(memberPersonalData);

//		EasyMock.expect(
//				memberProfileForm.toMemberPersonalData(memberPersonalData))
//				.andReturn(memberPersonalData);

		EasyMock.expect(memberManager.savePersonData(memberPersonalData))
				.andReturn(memberPersonalData);

		EasyMock.expect(memberManager.saveMember(memberCommand)).andReturn(
				memberCommand);
		// Replay
		control.replay();

		assertEquals(DefaultReturnResult.SUCCESS,
				memberProfileController.editMemberProfile(memberDetails,
						memberProfileForm, request, response, bindingResult));

		control.verify();
	}

}
