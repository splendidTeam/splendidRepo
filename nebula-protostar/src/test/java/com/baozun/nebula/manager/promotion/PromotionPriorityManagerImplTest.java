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

package com.baozun.nebula.manager.promotion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.manager.SdkPriorityAdjustManager;

/**
 * @author - 项硕
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class PromotionPriorityManagerImplTest {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PromotionPriorityManagerImplTest.class);
	
	@Autowired
	private PromotionPriorityManager priorityManager;
	@Autowired
	private SdkPriorityAdjustManager sdkPriorityAdjustManager;
	@Autowired
	private MemberDao memberDao;

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionPriorityManagerImpl#findAllPriorityDefaultList()}.
	 */
	@Test
	public void testFindAllPriorityDefaultList() {
		Member m1 = memberDao.getByPrimaryKey(125L);
		Member m2 = memberDao.getByPrimaryKey(125L);
		System.out.println(m1 == m2);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.promotion.PromotionPriorityManagerImpl#updateAllPriorityDefault(java.util.List, java.lang.Long)}.
	 */
	@Test
	public void testUpdateAllPriorityDefault() {
		
	}

}
