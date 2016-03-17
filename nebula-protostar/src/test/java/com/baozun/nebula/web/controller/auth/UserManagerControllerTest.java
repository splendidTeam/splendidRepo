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
 *
 */
package com.baozun.nebula.web.controller.auth;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.UserManagerCommand;

/**
 * @author liulin
 * 
 */
public class UserManagerControllerTest {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(UserManagerControllerTest.class);

	private UserManager userManager;

	public UserManagerController userManagerController;

	private IMocksControl control;

	private HttpServletRequest request;

	private Model model;

	private HttpServletResponse response;

	private RoleManager roleManager;

	private OrganizationManager organizationManager;

	private ChooseOptionManager chooseOptionManager;

	@Before
	public void init() {
		userManagerController = new UserManagerController();
		control = createNiceControl();

		userManager = control.createMock("UserManager", UserManager.class);
		roleManager = control.createMock("RoleManager", RoleManager.class);
		organizationManager = control.createMock("OrganizationManager",
				OrganizationManager.class);
		chooseOptionManager = control.createMock("ChooseOptionManager",
				ChooseOptionManager.class);

		ReflectionTestUtils.setField(userManagerController, "userManager",
				userManager);
		ReflectionTestUtils.setField(userManagerController, "roleManager",
				roleManager);
		ReflectionTestUtils.setField(userManagerController,
				"organizationManager", organizationManager);
		ReflectionTestUtils.setField(userManagerController,
				"chooseOptionManager", chooseOptionManager);

		request = control.createMock("HttpServletRequest",
				HttpServletRequest.class);
		response = control.createMock("HttpServletResponse",
				HttpServletResponse.class);
		model = control.createMock("model", Model.class);
	}

	/**
	 * 显示列表页
	 * 
	 * @param userCommand
	 * @param start
	 * @param size
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void findUserList() {
		UserManagerCommand userCommand = new UserManagerCommand();
		EasyMock.expect(organizationManager.findAllOrganization()).andReturn(
				new ArrayList<Organization>());
		EasyMock.expect(
				chooseOptionManager.findEffectChooseOptionListByGroupCode(
						"IS_AVAILABLE", "")).andReturn(
				new ArrayList<ChooseOption>());
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		userCommand.setUserId(2L);
		assertEquals("", userManagerController.findUserList(userCommand, model,
				request, response));
	}

	/*
	*//**
	 * ajax获取用户列表
	 * 
	 * @param userCommand
	 * @param start
	 * @param size
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void findUserListJson() {
		QueryBean queryBean = new QueryBean();
		queryBean.setParaMap(new HashMap<String, Object>());
		userManagerController.findUserListJson(queryBean, model, request,
				response);
	}

	@Test
	public void findOrgList() {
		String orgName = "系统";
		userManagerController.findOrgList(orgName);
	}

	/**
	 * 删除用户
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void removeUser() {
		String ids = "1,2";
		try {
			//userManagerController.removeUser(ids, model, request, response);
		} catch (Exception e) {
			throw new BusinessException("测试异常removeUser");
		}
	}

	/*
	*//**
	 * 启用或禁用用户
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void enableOrDisableUser() {
		String ids = "1,2";
		Integer state = 2;
		try {
			userManagerController.enableOrDisableUser(ids, state, model,
					request, response);
		} catch (Exception e) {
			throw new BusinessException("测试异常enableOrDisableUser");
		}
	}

	@Test
	public void toCreateUser() {
		try {
			EasyMock.expect(organizationManager.findAllOrganization())
					.andReturn(new ArrayList<Organization>());
			EasyMock.expect(organizationManager.findAllOrgType()).andReturn(
					new ArrayList<OrgType>());
			control.replay();
			assertEquals("", userManagerController.toCreateUser(model, request,
					response));
		} catch (Exception e) {
			throw new BusinessException("测试异常toCreateUser");
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void createUser() {
		UserManagerCommand userCommand = new UserManagerCommand();
		userCommand.setUserId(2L);
		userCommand.setUserName("ZS");
		try {
			EasyMock.expect(
					userManager.volidateUserName(userCommand.getUserName()))
					.andReturn(true);
			control.replay();
			assertEquals("", userManagerController.createUser(model,
					userCommand, request, response));
		} catch (Exception e) {
			throw new BusinessException("测试异常createUser");
		}
	}

	/**
	 * 修改用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void updateUser() {
		UserManagerCommand userCommand = new UserManagerCommand();
		userCommand.setUserId(2L);
		userCommand.setUserName("ZS");
		User user = new User();
		user.setId(1L);
		user.setUserName("ZS");
		EasyMock.expect(userManager.getUserById(2L)).andReturn(user);
		EasyMock.expect(userManager.volidateUserName(userCommand.getUserName()))
				.andReturn(true);
		control.replay();
		try {
			assertEquals("", userManagerController.updateUser(model,
					userCommand, request, response));
		} catch (Exception e) {
			throw new BusinessException("测试异常updateUser");
		}
	}

	/**
	 * 进入修改密码页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void toResetUserPasswd() {
		User user = new User();
		user.setId(2L);
		user.setUserName("ZS");
		EasyMock.expect(userManager.getUserById(user.getId())).andReturn(user);
		control.replay();
		try {
			assertEquals("", userManagerController.toResetUserPasswd(model,
					user.getId(), request, response));
		} catch (Exception e) {
			throw new BusinessException(
					" -------------------toResetUserPasswd Exception");
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void resetUserPasswd() {
		User user = new User();
		user.setId(3L);
		UserManagerCommand userCommand = new UserManagerCommand();
		userCommand.setUserId(3L);
		EasyMock.expect(userManager.getUserById(user.getId())).andReturn(user);
		control.replay();
		try {
			assertEquals("", userManagerController.resetUserPasswd(model,
					userCommand, request, response));
		} catch (Exception e) {

		}
	}

	/**
	 * 进入修改用户页面
	 * 
	 * @param model
	 * @param userId
	 *            用户id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void toUpdateUser() {
		User user = new User();
		user.setId(3L);
		EasyMock.expect(organizationManager.findAllOrganization()).andReturn(new ArrayList<Organization>());
		control.replay();
		try {
			assertEquals("", userManagerController.toUpdateUser(model, user.getId(), request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 进入查看用户页面
	 * 
	 * @param model
	 * @param userId
	 *            用户id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void viewUser() {
		User user = new User();
		user.setId(3L);
		EasyMock.expect(organizationManager.findAllOrganization()).andReturn(new ArrayList<Organization>());
		control.replay();
		try {
			assertEquals("", userManagerController.viewUser(model, user.getId(), request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 获取角色列表,如果orgTypeId为空，则查询所有角色
	 * 
	 * @param model
	 * @param orgTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void findRoleList() {
		OrgType orgType=new OrgType();
		orgType.setId(1L);
		EasyMock.expect(roleManager.findByOrgaTypeId(orgType.getId())).andReturn(new ArrayList<Role>());
		EasyMock.expect(roleManager.findRoleList()).andReturn(new ArrayList<Role>());
		control.replay(); 
		try {
			assertEquals("", userManagerController.findRoleList(model, orgType.getId(), request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 获取用户权限关系(修改时使用)
	 * 
	 * @param model
	 * @param orgTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void findUserRoleList() {
		User user=new User();
		user.setId(1L);
		EasyMock.expect(userManager.findUORByKindsId(user.getId(), null, null)).andReturn(new ArrayList<UserRole>());
		control.replay();
		try {
			assertEquals("", userManagerController.findUserRoleList(model, user.getId(), request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 验证用户登录名称
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@Test
	public void volidateLogin() {
		User user=new User();
		user.setUserName("ZS");
		EasyMock.expect(userManager.volidateUserName(user.getUserName())).andReturn(true);
		control.replay();
		try {
			assertEquals("", userManagerController.volidateLogin(user.getUserName(), model, request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 保存用户角色
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void saveUserRole() {
		String orgs="1";
		Long userId=1L;
		Long roleId=1L;
		control.replay();
		try {
			assertEquals("", userManagerController.saveUserRole(orgs, userId, roleId, model, request, response)); 
		} catch (Exception e) { 
		}
	}

	/**
	 * 删除用户角色
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void removeUserRole() {
		String orgs="1";
		Long userId=1L;
		Long roleId=1L;
		control.replay();
		try {
			assertEquals("", userManagerController.removeUserRole(orgs, userId, roleId, model, request, response)); 
		} catch (Exception e) { 
		}
	}
}
