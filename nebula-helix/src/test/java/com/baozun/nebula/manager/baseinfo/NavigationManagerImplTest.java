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

package com.baozun.nebula.manager.baseinfo;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class NavigationManagerImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger	log	= (Logger) LoggerFactory.getLogger(NavigationManagerImplTest.class);
	
	@Autowired
	private NavigationManager navigationManager;

	@Before
	public void b() {
		ProfileConfigUtil.setMode("dev");
	}

	@Test
	public void testFindStoreNavigation() {

		List<NavigationCommand> commands = navigationManager
				.findStoreNavigation();

		Assert.assertNotNull("导航菜单树不存在", commands);
		for (NavigationCommand c : commands) {

			try {
//				String name = BeanUtils.getProperty(c.getName(), "value");
//				log.info(String.format("菜单【%s】(id=%s, url=%s)", name,
//						c.getId(), c.getUrl()));
				
				MutlLang mutlLang  = (MutlLang)c.getName();
				log.info("==================="+mutlLang.getDefaultValue()+"   "+c.getSubNavigations().size()+"  ");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

}
