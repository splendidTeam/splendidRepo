/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.utils.query.bean.QueryBean;

/**
 * @author li.feng
 * 
 * @date 2013-6-26 下午04:28:11
 */
public class RoleControllerTest{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(RoleControllerTest.class);

	private RoleController		roleController;

	private IMocksControl		control;

	private RoleManager			roleManager;

	private HttpServletRequest	request;

	private HttpServletResponse	response;

	private Model				model;

	@Before
	public void init(){

		roleController = new RoleController();
		control = createNiceControl();

		// mock一个categoryManager对象
		roleManager = control.createMock("RoleManager", RoleManager.class);
		ReflectionTestUtils.setField(roleController, "roleManager", roleManager);

		//使用 EasyMock 生成 Mock 对象；
		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		response = control.createMock("HttpServletResponse", HttpServletResponse.class);
		model = control.createMock("model", Model.class);

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#findRoleListJson(com.baozun.nebula.utils.query.bean.QueryBean, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testFindRoleListJson(){
		QueryBean queryBean = new QueryBean();
		queryBean.setPage(new Page(0,2));
		queryBean.setSorts(null);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orgTypeId", 2L);
		paraMap.put("lifecycle", 1);
		paraMap.put("name", "%test%");
		queryBean.setParaMap(paraMap);

		// Pagination<RoleCommand> Roleresult = roleManager.findRoleCommandList(queryBean);

		Pagination<RoleCommand> endResult = new Pagination<RoleCommand>();
		RoleCommand rc = new RoleCommand();
		rc.setId(4L);
		rc.setDesc(null);
		rc.setLifecycle(1);
		rc.setName("test");
		rc.setOrgTypeId(2L);
		Map<Long, Privilege> privileges = new HashMap<Long, Privilege>();
		rc.setPrivileges(privileges);
		rc.setOrgTypeName("店铺级别");
		rc.setVersion(null);
		List<RoleCommand> items = new ArrayList<RoleCommand>();
		items.add(rc);
		endResult.setItems(items);
		endResult.setCount(1);
		endResult.setCurrentPage(1);
		endResult.setSize(2);
		endResult.setSortStr(null);
		endResult.setStart(0);
		endResult.setTotalPages(1);
		EasyMock.expect(roleManager.findRoleCommandList(queryBean.getPage(),queryBean.getSorts(),queryBean.getParaMap())).andReturn(endResult);
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals(endResult, roleController.findRoleListJson(queryBean, model, request, response));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#findRoleList(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testFindRoleList(){

		// EasyMock.expect(null).andReturn(false);
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals("role/list", roleController.findRoleList(model, request, response));

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#createRole(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testCreateRole(){
		Map<Long, List<Privilege>> privilegeMap = new HashMap<Long, List<Privilege>>();

		//EasyMock.expect(roleManager.findAllPrivilegeMap()).andReturn(privilegeMap);
		EasyMock.expect(model.addAttribute("privilegeMap", privilegeMap)).andReturn(model);
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals("role/create", roleController.createRole(model, request, response));

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#updateRole(java.lang.Long, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testUpdateRole(){
		Map<Long, List<Privilege>> privilegeMap = new HashMap<Long, List<Privilege>>();
		//EasyMock.expect(roleManager.findAllPrivilegeMap()).andReturn(privilegeMap);

		Long roleId = 1L;
		RoleCommand roleCommand = new RoleCommand();
		
		EasyMock.expect(roleManager.findRoleCommandById(roleId)).andReturn(roleCommand);
		EasyMock.expect(model.addAttribute("privilegeMap", privilegeMap)).andReturn(model);
		EasyMock.expect(model.addAttribute("roleCommand", roleCommand)).andReturn(model);

		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals("role/create", roleController.updateRole(roleId, model, request, response));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#deleteRole(java.lang.String, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testDeleteRole(){

		String ids = "1";
		roleManager.disableRoleByIds(ids);
		EasyMock.expectLastCall();
		control.replay();
		// 验证
		assertEquals("success", roleController.deleteRole(ids, model, request, response));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.auth.RoleController#saveRole(com.baozun.nebula.model.auth.Role, java.lang.Long[], org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testSaveRole(){

		Role role = new Role();
		Long[] privilegeIds = new Long[1];
		roleManager.saveRole(role, privilegeIds);
		EasyMock.expectLastCall();
		control.replay();
		// 验证
		assertEquals("success", roleController.saveRole(role, privilegeIds, model, request, response));

	}

}
