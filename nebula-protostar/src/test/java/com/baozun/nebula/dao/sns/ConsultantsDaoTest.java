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

package com.baozun.nebula.dao.sns;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.solr.utils.JsonFormatUtil;

/**
 * @author - 项硕
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class ConsultantsDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger	logger	= (Logger) LoggerFactory.getLogger(ConsultantsDaoTest.class);
	
	@Autowired
	private ConsultantsDao consultantsDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#findConsultants(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)}.
	 */
	@Test
	public void testFindConsultants() {
		Page page = new Page(1, 3);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("code", "110");
		Pagination<ConsultantCommand> pagination = consultantsDao.findConsultants(page, null, paraMap);
		logger.info(JsonFormatUtil.format(pagination.getItems()));
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#resolveConsultant(java.lang.Long, int, java.lang.String, java.lang.Long, int)}.
	 */
	@Test
	public void testResolveConsultant() {
		assertEquals(1 ,consultantsDao.resolveConsultant(14L, 3, "测试回复内容", 13L, 1).intValue());
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#publishConsultant(java.lang.Long, int, java.lang.Long)}.
	 */
	@Test
	public void testPublishConsultant() {
		assertEquals(1, consultantsDao.publishConsultant(14L, 1, 13L).intValue());
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#unpublishConsultant(java.lang.Long, int, java.lang.Long)}.
	 */
	@Test
	public void testUnpublishConsultant() {
		assertEquals(1, consultantsDao.publishConsultant(14L, 0, 13L).intValue());
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#updateConsultant(java.lang.Long, java.lang.String, java.lang.Long, int)}.
	 */
	@Test
	public void testUpdateConsultant() {
		assertEquals(1, consultantsDao.updateConsultant(14L, "ffffff", 13L, 1).intValue());
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.sns.ConsultantsDao#findById(java.lang.Long)}.
	 */
	@Test
	public void testFindById() {
		logger.info(JsonFormatUtil.format(consultantsDao.findById(14L)));
	}

}
