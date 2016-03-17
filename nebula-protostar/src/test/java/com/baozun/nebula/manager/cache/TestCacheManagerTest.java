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
package com.baozun.nebula.manager.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.exception.CacheException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.auth.User;

/**
 * @author liulin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class TestCacheManagerTest {
	Logger	logger	= LoggerFactory.getLogger(TestCacheManagerTest.class);
 
	@Autowired
	private CacheManager cacheManager;
	
 
	@Test
	public void testGenerateMapField(){
		
		User user=new User();
		user.setId(123l);
		user.setUserName("张三李四");
		user.setPassword("sdsddd");
		user.setMobile("12345678");
		user.setCreateTime(new Date());
		String str=cacheManager.generateMapFieldByDefault();
		
		System.out.println(str);
		try{
			cacheManager.setMapValue("tteess2sss", str, "abc", 10);
		}
		catch(CacheException e){
			e.printStackTrace();
		}
		
		cacheManager.setMapValue("tteess", str, "abcdddd", 10);
		
		System.out.println(cacheManager.getMapObject("tteess2", str));
		
		
		cacheManager.setMapValue("tteess2sss", str, "123", 10);
	}
	
	
	public void testMap(){
		List<User> userList=new ArrayList<User>();
		
		User user=new User();
		user.setId(1l);
		user.setUserName("asdfasdf");
		user.setEmail("asdfsf");;
		
		userList.add(user);
		
		cacheManager.setMapObject("testuMap", "abc", userList, 20);
		
		List<User> userList2=cacheManager.getMapObject("testuMap", "abc");
		
	}
	
	
	public void testSimpleObject(){
		
		Integer i=1;
		
		//cacheManager.setObject("test-integer", i);
		
		Integer j=cacheManager.getObject("test-integer");
		
		System.out.println(j);
	}
	
	
	public void findList(){
		
		String[] values={"t1","t2","t3"};
		//cacheManager.pushToListHead("findList-list", values);
		
		List<String> list=cacheManager.findLists("findList-list", 0, Long.MAX_VALUE);
		
		for(String str:list){
			System.out.println(str);
		}
		
		System.out.println(cacheManager.popListFooter("findList-list"));
		
		list=cacheManager.findLists("findList-list", 0, Long.MAX_VALUE);
		for(String str:list){
			System.out.println(str);
		}
	}

	@Test
	public void testRemove(){
		String key="test_remove";
		String value="test_remove_value";
		cacheManager.setValue(key, value);
		
		value=cacheManager.getValue(key);
		System.out.println(value);
		
		cacheManager.remove(key);
		value=cacheManager.getValue(key);
		System.out.println("被删除以后:"+value);
	}
	
}
