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
import java.util.Date;
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

import com.baozun.nebula.command.UserCommand;
import com.baozun.nebula.command.auth.OrgAclCommand;
import com.baozun.nebula.command.auth.PrivilegeUrlCommand;
import com.baozun.nebula.model.auth.User;

/**
 * @author liulin 用户数据访问层测试
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class UserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final Logger log = LoggerFactory
			.getLogger(UserDaoTest.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PrivilegeDao privilegeDao;

	@Autowired
	private UserRoleDao userRoleDao;
	
	
	@Test
	public void findUserPrivilegeByUserId(){
		List<OrgAclCommand> list=userRoleDao.findUserAclByUserId(13l);
		
		System.out.println(list.size());
	}
	
	@Test
	public void findAllPriUrl(){
		List<PrivilegeUrlCommand> list=privilegeDao.findAllEffectivePrivilegeUrl();
		
		System.out.println(list.size());
	}
	
	/**
	 * 分页获取用户列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@Test
	public void findUserList() {
		Sort[] sorts = Sort.parse("us.create_time asc,us.create_time desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("lifecycle", 1);
		paraMap.put("userName", "root-4");
		paraMap.put("mobile", "18717879241");
		paraMap.put("email", "");
		paraMap.put("realName", "张三");
		paraMap.put("orgId", 132L);
		paraMap.put("startDate", new Date());
		paraMap.put("endDate", new Date());
		Page page=new Page(1,10);
		Pagination<UserCommand> result = userDao.findUserList(page, sorts,
				paraMap);
		log.info("findUserList 方法记录数{}条", result.getCount());
		
	}

	/**
	 * 通过ids查询用户列表
	 * 
	 * @param ids
	 * @return
	 */
	
	 @Test 
	 public void findUserListByIds() {
		 List<Long> ids=new ArrayList<Long>();
		 ids.add(new Long(6L));
		 ids.add(new Long(7L));
		 List<User> users= userDao.findUserListByIds(ids);
		 log.info("findUserListByIds  方法记录数{}条", users.size());
	 }
	 /*
	 *//**
	 * 通过多个用户名称查询用户列表
	 * 
	 * @param ids
	 * @return
	 */
	  @Test
	  public void findUserListByUserNams() {
		  List<String> userNames=new ArrayList<String>();
		  userNames.add("root-4");
		  userNames.add("root-5");
		  userNames.add("root-1");
		  userNames.add("root-2");  
		  List<User> users=userDao.findUserListByUserNams(userNames);
		  log.info("findUserListByUserNams  方法记录数{}条", users.size());
	  }
	 
	 /**
	 * 通过登录名称获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	  @Test
	  public void findByUserName() { 
		  User user=userDao.findByUserName("root-2");
		  log.info("findByUserName  方法返回{}", user==null?"null":user.getUserName()); 
	  } 
	 /** 验证登录名称获取用户
	 * 
	 * @param loginName
	 * @return
	 */ 
	  @Test
	  public void validByUserName() {
		  User user=userDao.validByUserName("root-2");
		  log.info("validByUserName  方法返回{}", user==null?"null":user.getUserName());
	 }
	 /**
	 * 通过id获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	  @Test
	  public void findById() {
		 User user= userDao.findById(6L);
		 log.info("findById  方法返回{}", user==null?"null":user.getUserName());
	  }
	  
	 /**
	 * 启用或禁用用户
	 * 
	 * @param ids
	 * state  0\1\2
	 */ 
	  @Test
	  public void enableOrDisableUser() {
		  List<Long> ids=new ArrayList<Long>();
		  ids.add(6L);
		 userDao.enableOrDisableUser(ids, 1);
		 User user= userDao.findById(6L);
		 log.info("enableOrDisableUser  禁用后 用户状态{}", user==null?"null":user.getLifecycle()); 
	 }

	 /**
	 * 更新登录时间
	 */  
	  @Test
	  public void updateLatestAccessTime() {  
		  userDao.updateLatestAccessTime(6L);
		  User user=userDao.findById(6L);
		  log.info("updateLatestAccessTime   最后登录时间{}", user.getLatestAccessTime());
	  }
	 /**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	  @Test
	  public void modifyPwd() { 
		  userDao.modifyPwd(6L, "ecc5b1b49fa393bef77b0c44ef3d20fab562a2ea3d6984a6f41d4f1ab5fef466", "123");
		  User user= userDao.findById(6L);
		  log.info("enableOrDisableUser 修改用户密码后密码{}", user.getPassword()); 
	  }

}
