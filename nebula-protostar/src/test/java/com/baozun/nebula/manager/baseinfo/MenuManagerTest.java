/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.manager.baseinfo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.UserDetails;

/**
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Jun 21, 2013 1:51:40 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MenuManagerTest{

	private static final Logger	log	= LoggerFactory.getLogger(MenuManagerTest.class);

	@Autowired
	private MenuManager			menuManager;

	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.MenuManager#getUserMenu(com.baozun.nebula.web.UserDetails)}.
	 */
	@Test
	public void testGetUserMenu(){
		String password = null;
		String username = null;
		String realName = null;
		Long userId = 13L;
		Integer lifecycle = null;

		UserDetails userDetails = new UserDetails(password, username, realName, userId, lifecycle);

		userDetails.setCurrentOrganizationId(2L);

		List<MenuCommand> menuCommandList = menuManager.getUserMenu(userDetails);

		String menus = JsonFormatUtil.format(menuCommandList, "***");
		log.debug("menus:{}", menus);
	}

	@Test
	public void pwd(){
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		System.out.println(encoder.encodePassword("123456", "root-1"));
		System.out.println(encoder.encodePassword("123456", "root-3"));
		Map a = new HashMap();
		a.put("颜色", "红色");
		System.out.println(a.get("颜色"));
	}
}
