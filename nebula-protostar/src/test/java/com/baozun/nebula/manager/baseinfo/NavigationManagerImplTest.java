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

import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.solr.utils.JsonFormatUtil;

/**
 * @author - 项硕
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class NavigationManagerImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger	log	= LoggerFactory.getLogger(NavigationManagerImplTest.class);
	
	@Autowired
	private NavigationManager navigationManager;
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.NavigationManagerImpl#findAllNavigationList()}.
	 */
	@Test
	public void testFindAllNavigationList() {
		log.info(JsonFormatUtil.format(navigationManager.findAllNavigationList(Sort.parse("parent_id asc,sort asc"))));;
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.NavigationManagerImpl#createOrUpdateNavigation(com.baozun.nebula.model.baseinfo.Navigation)}.
	 */
	@Test
	public void testCreateOrUpdateNavigation() {
		Navigation n = new Navigation();
		n.setId(-1L);
		n.setName("测试导航");
		log.info(JsonFormatUtil.format(navigationManager.createOrUpdateNavigation(n)));
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.NavigationManagerImpl#removeNavigationById(java.lang.Long)}.
	 */
	@Test
	public void testRemoveNavigationById() {
		navigationManager.removeNavigationById(39L);
	}

}
