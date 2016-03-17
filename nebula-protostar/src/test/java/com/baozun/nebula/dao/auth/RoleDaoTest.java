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
package com.baozun.nebula.dao.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.model.auth.Role;

/**
 * @author wenxiu.ke
 * 角色Dao测试
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class RoleDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final Logger log = LoggerFactory.getLogger(RoleDaoTest.class);
	
	@Autowired
	private RoleDao roleDao;
	

	/**
	 * Test method for {@link com.baozun.nebula.dao.auth.RoleDao#findAllList()}.
	 * 获取所有角色
	 */
	@Test
	public void testFindAllList() {
		List<Role> roles = roleDao.findAllList();
		log.info("findAllList  方法记录数{}条",roles.size());
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.auth.RoleDao#findByOrgaTypeId(java.lang.Long)}.
	 * 通过机构类型获取角色
	 */
	@Test
	public void testFindByOrgaTypeId() {
		List<Role> roles = roleDao.findByOrgaTypeId(1L);
		for(Role role:roles){
			log.info("findByOrgaTypeId  方法返回{}",role.getName());
		}	
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.auth.RoleDao#findByParaMap(int, int, loxia.dao.Sort[], java.util.Map)}.
	 * 分页获取角色列表
	 */
	@Test
	public void testFindByParaMap() {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("name", "kkk");
		paraMap.put("lifecycle", 1);
		paraMap.put("orgTypeId", 1);
		Sort[] sorts = Sort.parse("r.name asc");
		Pagination<RoleCommand> result = roleDao.findByParaMap(new Page(0, 10), sorts, paraMap);
		log.info("findByParaMap  方法记录数{}条",result.getCount());
		
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.auth.RoleDao#disableRole(java.util.List)}.
	 */
	@Test
	public void testDisableRole() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(3L);
		ids.add(4L);
		roleDao.disableRole(ids);
		Role role = roleDao.findRoleById(3L);
		Role role1 = roleDao.findRoleById(4L);
		log.info("disableRole 方法返回{}",role==null?"null":role.getLifecycle());
		log.info("disableRole 方法返回{}",role1==null?"null":role1.getLifecycle());
	}

}
