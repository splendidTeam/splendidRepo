/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.web.controller.member;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.member.converter.MemberAddressViewCommandConverter;
import com.baozun.nebula.web.controller.member.form.MemberAddressForm;
import com.baozun.nebula.web.controller.member.validator.MemberAddressFormValidator;

import loxia.dao.Pagination;

/**
 * NebulaMemberAddressControllerTest.
 * 
 * <h3>什么是Mock:</h3> <blockquote>
 * <p>
 * 用于把次要对象和主要对象分离开，用Mock对象替换次要对象，以保证完成主要测试对象的测试工作;
 * Mock的定义包含了测试逻辑和流程，如输入，调用次数，返回值等; Mock对象一般不复用，都遵循”just enough”原则
 * </p>
 * </blockquote>
 * 
 * <h3>Record-Replay-Verify:</h3> <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>record</td>
 * <td>
 * 
 * <pre>
{@code
        User expectedUser = new User();
        expectedUser.setId("1001");
        expectedUser.setAge(30);
        expectedUser.setName("user-1001");
        UserDao userDao  = EasyMock.createMock(UserDao.class);
        EasyMock.expect(userDao.getById("1001")).andReturn(expectedUser);

    这里我们开始创建mock对象，并期望这个mock对象的方法被调用，同时给出我们希望这个方法返回的结果。
    这就是所谓的"记录mock对象上的操作", 同时我们也会看到"expect"这个关键字。
    总结说，在record阶段，我们需要给出的是我们对mock对象的一系列期望：若干个mock对象被调用，依从我们给定的参数，顺序，次数等，并返回预设好的结果(返回值或者异常).
}
 * </pre>
 * 
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>replay</td>
 * <td>
 * 
 * <pre>
{@code
  UserServiceImpl  service = new UserServiceImpl();
        service.setUserDao(userDao);
        User user = service.query("1001");

    在replay阶段，我们关注的主要测试对象将被创建，之前在record阶段创建的相关依赖被关联到主要测试对象，然后执行被测试的方法，以模拟真实运行环境下主要测试对象的行为。
    在测试方法执行过程中，主要测试对象的内部代码被执行，同时和相关的依赖进行交互：以一定的参数调用依赖的方法，获取并处理返回。
    我们期待这个过程如我们在record阶段设想的交互场景一致，即我们期望在replay阶段所有在record阶段记录的行为都将被完整而准确的重新演绎一遍，从而到达验证主要测试对象行为的目的。
}
 * </pre>
 * 
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>verify</td>
 * <td>
 * 
 * <pre>
{@code
assertNotNull(user);
        assertEquals("1001", user.getId()); 
        assertEquals(30, user.getAge()); 
        assertEquals("user-1001", user.getName()); 
        EasyMock.verify(userDao);

    在verify阶段，我们将验证测试的结果和交互行为。
    通常验证分为两部分，如上所示： 
    一部分是验证结果，即主要测试对象的测试方法返回的结果(对于异常测试场景则是抛出的异常)是否如预期，通常这个验证过程需要我们自行编码实现。
    另一部分是验证交互行为，典型如依赖是否被调用，调用的参数，顺序和次数，这部分的验证过程通常是由mock框架来自动完成，我们只需要简单调用即可。
    在easymock的实现中，verify的部分交互行为验证工作，会提前在replay阶段进行：比如未记录的调用，调用的参数等。
    如果验证失败，则直接结束replay以致整个测试案例。
}
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * 
 * <p>
 * record-replay-verify 模型非常好的满足了大多数测试场景的需要：<b> 先指定测试的期望，然后执行测试，再验证期望是否被满足。</b>
 * <br>
 * 这个模型简单直接，易于实现，也容易被开发人员理解和接受，因此被各个mock框架广泛使用。
 * </p>
 * </blockquote>
 *
 * @author hengheng.wang
 * @version 5.3.0 2016年3月21日 下午4:50:31
 * @since 5.3.0
 */
public class NebulaMemberAddressControllerTest extends BaseControllerTest {

	/** The nebula member address controller. */
	private NebulaMemberAddressController nebulaMemberAddressController;

	/** The member address view command converter. */
	private MemberAddressViewCommandConverter memberAddressViewCommandConverter;
	
	private MemberAddressFormValidator memberAddressFormValidator;

	/** The sdk member manager. */
	private SdkMemberManager sdkMemberManager;

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
		nebulaMemberAddressController = new NebulaMemberAddressController();
		memberAddressViewCommandConverter = new MemberAddressViewCommandConverter();
		memberAddressFormValidator = new MemberAddressFormValidator();

		sdkMemberManager = EasyMock.createMock("sdkMemberManager", SdkMemberManager.class);

		ReflectionTestUtils.setField(nebulaMemberAddressController, "sdkMemberManager", sdkMemberManager);
		ReflectionTestUtils.setField(nebulaMemberAddressController, "memberAddressViewCommandConverter",
				memberAddressViewCommandConverter);
		ReflectionTestUtils.setField(nebulaMemberAddressController, "memberAddressFormValidator",
				memberAddressFormValidator);
		
	}

	/**
	 * Test show member address.
	 */
	@Test
	public void testShowMemberAddress() {
		// Record
		Pagination<ContactCommand> pagination =  new Pagination<ContactCommand>();
		
		PageForm pageForm = new PageForm();
		pageForm.setCurrentPage(0);
		pageForm.setSize(10);
		
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);
		
		EasyMock.expect(sdkMemberManager.findContactsByMemberId(pageForm.getPage(), pageForm.getSorts(), memberDetails.getMemberId()))
				.andReturn(pagination);

		// Replay
		EasyMock.replay(sdkMemberManager);

		PageForm pageForm2 = new PageForm();
		pageForm.setCurrentPage(0);
		pageForm.setSize(10);
		
		MemberDetails memberDetails2 = new MemberDetails();
		memberDetails.setMemberId(1L);	
		nebulaMemberAddressController.showMemberAddress(memberDetails2, pageForm2, request, response, model);
		System.out.println("dsdsd");
		// 验证结果
		assertEquals(NebulaMemberAddressController.VIEW_MEMBER_ADDRESS_LIST,
				nebulaMemberAddressController.showMemberAddress(memberDetails2, pageForm2, request, response, model));

		// 验证交互行为
		EasyMock.verify(sdkMemberManager);
	}
	
	/**
	 * Test setDefaultAddress.
	 */
	@Test
	public void testSetDefaultAddress() {
		// Record
		ContactCommand command = new ContactCommand();
		command.setId(1L);
		EasyMock.expect(sdkMemberManager.findContactById(1L, 1L))
				.andReturn(command);
		EasyMock.expect(sdkMemberManager.updateContactIsDefault(1L, 1L,true))
		.andReturn(new Integer(0));

		// Replay
		EasyMock.replay(sdkMemberManager);
		PageForm pageForm = new PageForm();
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);
 
		Long addressId = 1L;
		// 验证结果
		assertEquals(DefaultReturnResult.SUCCESS,nebulaMemberAddressController.setDefaultAddress(memberDetails,addressId, request,response, model));

		// 验证交互行为
		EasyMock.verify(sdkMemberManager);
	}
	
	/**
	 * Test updateMemberAddress.
	 */
	@Test
	public void testUpdateMemberAddress() {
		// Record
		ContactCommand command = new ContactCommand();
		command.setId(1L);
		EasyMock.expect(sdkMemberManager.findContactById(1L, 1L))
				.andReturn(command);
		
		EasyMock.expect(sdkMemberManager.saveContactCommand(command))
		.andReturn(command);
		
		
		MemberAddressForm memberAddressForm = new MemberAddressForm();
		memberAddressForm.setProvince(1L);
		memberAddressForm.setCity(1L);
		memberAddressForm.setArea(1L);
		memberAddressForm.setTown(1L);
		memberAddressForm.setAddress("上海市浦东新区川杨新苑三期");
		memberAddressForm.setPhone("13023230767");
		memberAddressForm.setConsignee("wanghengheng");
		memberAddressForm.setPostcode("200000");
		
		BindingResult bindingResult = null;
		memberAddressFormValidator.validate(memberAddressForm, bindingResult);
		EasyMock.expectLastCall();
		
		// Replay
		EasyMock.replay(sdkMemberManager);

		PageForm pageForm = new PageForm();
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);	
		
 		// 验证结果
		assertEquals(NebulaMemberAddressController.VIEW_MEMBER_ADDRESS_LIST,
				nebulaMemberAddressController.updateMemberAddress(memberDetails,
						memberAddressForm, bindingResult, request,
						response , model));

		// 验证交互行为		
		EasyMock.verify(sdkMemberManager);
	}
	
	/**
	 * Test deleteMemberAddress.
	 */
	@Test
	public void testDeleteMemberAddress() {
		// Record
		ContactCommand command = new ContactCommand();
		command.setId(1L);
		EasyMock.expect(sdkMemberManager.findContactById(1L, 1L))
				.andReturn(command);
		EasyMock.expect(sdkMemberManager.removeContactById(1L, 1L))
				.andReturn(new Integer(1));

		// Replay
		EasyMock.replay(sdkMemberManager);

		PageForm pageForm = new PageForm();
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberId(1L);
		Long addressId = 1L;
		// 验证结果
		assertEquals(DefaultReturnResult.SUCCESS,
				nebulaMemberAddressController.deleteMemberAddress(memberDetails,addressId, request, response, model	));

		// 验证交互行为
		EasyMock.verify(sdkMemberManager);
	}
}
