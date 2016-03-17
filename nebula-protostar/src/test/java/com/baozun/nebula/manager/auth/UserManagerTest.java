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
package com.baozun.nebula.manager.auth;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;

/**
 * @author liulin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class UserManagerTest {
	Logger	logger	= LoggerFactory.getLogger(UserManagerTest.class);
 
	@Autowired
	private UserManager userManager;
	
 
	@Test
	public void findUserList(){
		Sort[] sorts = Sort.parse("us.create_time asc,us.create_time desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
//		paraMap.put("lifecycle", 1);
		paraMap.put("userName", "root-14");
//		paraMap.put("mobile", "18717879241");
//		paraMap.put("email", "");
//		paraMap.put("realName", "张三");
//		paraMap.put("orgId", 132L);
//		paraMap.put("startDate", new Date());
//		paraMap.put("endDate", new Date());
		Page page=new Page(1,10);
		Pagination result= userManager.findUserList(page, sorts, paraMap);
		logger.info("-----------------------------{}",result.getCount());
	}
	
	

	@Test
	public void getUserById(){ 
		User user = userManager.getUserById(1L);
		logger.info("-----------------------------{}",user.getUserName());
	}
  
	@Test
	public void createOrUpdateUser(){ 
		User user2=new User();
		user2.setId(999L);
		user2.setUserName("root-14");
		user2.setPassword("123");
		user2.setRealName("张三"); 
		userManager.createOrUpdateUser(user2);  
		findUserList();
		logger.info("------------createOrUpdateUser ------ok");
	}

	
	@Test
	public void volidateUserName(){ 
		logger.info("-------------------{}",userManager.volidateUserName("ZS"));
	}


	 @Test
	public void enableOrDisableByIds(){
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		try {
			userManager.enableOrDisableByIds(ids, 1);
		} catch (Exception e) {
			logger.info("必须确保有效的用户名不允许重复!");
			return;
		} 
			logger.info("------------enableOrDisableByIds ------ok");
	}
	

	
	 
	 @Test
	public void findUORByKindsId(){
		List<UserRole> userRoles=userManager.findUORByKindsId(1L, null, null); 
		logger.info("------------findUORByKindsId ------{}",userRoles.size()); 
	}
 
	 @Test
	public void createOrUpdateUserRole() throws Exception{ 
		userManager.createOrUpdateUserRole(1L, 1L, "1,3");
		logger.info("------------createOrUpdateUserRole ------ok"); 
	}
	
	 @Test
	public void removeUserRole(){
		// TODO Auto-generated method stub 
		 try {
			 userManager.removeUserRole(1L, 2L); 
		} catch (Exception e) { 
			logger.info("------------删除用户角色关系失败! ------");  
		}
		 	logger.info("------------removeUserRole ------ok");  
		 
	}

	 @Test
	public void modifyPwd(){
		  boolean code=userManager.modifyPwd(1L, "zs", "123");
		  logger.info("------------modifyPwd ------{}",code);  
	}

	 
	 @Test
	public void loginLog(){
		userManager.loginLog(1L, "127.0.0.1", "测试sessionId");
		logger.info("------------loginLog ------ok");  
	}

	 @Test
	public void logoutLog(){
		 userManager.logoutLog(1L, "测试sessionId");
		 logger.info("------------logoutLog ------ok");  
	} 
	 
	 @Test
	public void removeByIds() { 
//		 List<Long> ids=new ArrayList<Long>();
//		 ids.add(1L); 
//		 userManager.removeByIds(ids); 
//		 logger.info("------------removeByIds ------ok");  
	}

	
}
