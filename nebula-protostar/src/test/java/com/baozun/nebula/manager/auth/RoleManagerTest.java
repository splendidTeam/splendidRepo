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
package com.baozun.nebula.manager.auth;


import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;

/**
 * @author li.feng
 * 
 * @date 2013-6-28 下午03:12:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class RoleManagerTest{

	Logger				logger	= LoggerFactory.getLogger(RoleManagerTest.class);

	@Autowired
	private RoleManager	roleManager;

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findByOrgaTypeId(java.lang.Long)}.
	 */
	@Test
	public final void testFindByOrgaTypeId(){
		Long orgaTypeId = 2L;
		List<Role> lr = roleManager.findByOrgaTypeId(orgaTypeId);
		for (Role l : lr){

			logger.info("------------------" + l.getId() + "===================" + l.getName());
		}
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findRoleList()}.
	 */
	@Test
	public final void testFindRoleList(){

		List<Role> lr = roleManager.findRoleList();
		for (Role l : lr){

			logger.info("Role id is" + l.getId() + "Role name is" + l.getName());
		}

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findRoleCommandList(com.baozun.nebula.utils.query.bean.QueryBean)}.
	 */
	@Test
	public final void testFindRoleCommandList(){
		Long id = 1L;
		RoleCommand rc = roleManager.findRoleCommandById(id);
		logger.info("RoleCommand role name is " + rc.getName());
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#disableRoleByIds(java.lang.String)}.
	 */
	@Test
	public final void testDisableRoleByIds(){

		String ids = "1,2";
		roleManager.disableRoleByIds(ids);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findAllPrivilege()}.
	 */
	@Test
	public final void testFindAllPrivilege(){

		List<Privilege> lp = roleManager.findAllPrivilege();
		for (Privilege l : lp){
			logger.info("Privilege id is" + l.getId() + ",Privilege name is" + l.getName());
		}

	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#saveRole(com.baozun.nebula.model.auth.Role, java.lang.Long[])}.
	 */
	@Test
	public final void testSaveRole(){
		Role r = new Role();
		r.setId(44l);
		r.setName("1234");
		r.setLifecycle(1);
		r.setOrgTypeId(2l);
		Long[] l = new Long[] { 1l, 2l };
		roleManager.saveRole(r, l);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findAllPrivilegeMap()}.
	 */
	@Test
	public final void testFindAllPrivilegeMap(){
		//Map<Long, List<Privilege>> a = roleManager.findAllPrivilegeMap();
		//logger.info("PrivilegeList size is" + a.size());
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.auth.RoleManagerImpl#findRoleCommandById(java.lang.Long)}.
	 */
	@Test
	public final void testFindRoleCommandById(){
		Long id = 1L;
		RoleCommand rc = roleManager.findRoleCommandById(id);
		logger.info("RoleCommand id is" + rc.getId() + ",RoleCommand name is " + rc.getName());
	}

}
