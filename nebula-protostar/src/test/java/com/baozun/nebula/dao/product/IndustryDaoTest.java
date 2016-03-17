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
 */
package com.baozun.nebula.dao.product;

import java.util.List;

import loxia.dao.Sort;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;

/**
 * 
 * @author kefan.chen
 * 
 * @date 2013-6-20 下午02:12:19
 */
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class IndustryDaoTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(IndustryDaoTest.class);

	@Autowired
	private IndustryDao industryDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.IndustryDao#insertIndustry(java.lang.Long, java.lang.Integer, java.lang.String)}.
	 */
	public void testInsertIndustry() {
		 Integer result = industryDao.insertIndustry(0L, 1, "Dao测试新增");
		 log.info("InsertIndustry  方法返回{}",result);
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.IndustryDao#findAllIndustryList()}.
	 */
	public void testFindAllIndustryList() {
		List<Industry> list = industryDao.findAllIndustryList();
		for(Industry l:list){
			log.info("FindAllIndustryList  方法返回{}",l.getName());
		}
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.IndustryDao#removeIndustryById(java.lang.Long)}.
	 */
	public void testRemoveIndustryById() {
		industryDao.removeIndustryById(5L);
		Industry industry= industryDao.findIndustryById(5L);
		log.info("RemoveIndustryById  逻辑删除后状态{}",industry==null?"null":industry.getLifecycle()); 
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.IndustryDao#updateIndustryById(java.lang.Long, java.lang.Integer, java.lang.String)}.
	 */
	public void testUpdateIndustryById() {
		industryDao.updateIndustryById(5L, 2, "百事");
		Industry industry = industryDao.findIndustryById(5L);
		log.info("UpdateIndustryById 修改行业后名称为{}", industry.getName());
		log.info("UpdateIndustryById 修改行业后状态为{}", industry.getLifecycle());
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.IndustryDao#findIndustryListByShopId(java.lang.Long)}.
	 */
	@Test
	public void testFindIndustryListByShopId() {
		
		List<Industry> list = industryDao.findIndustryListByShopId(2L)	;	
		
		
		log.info("@_@:::{}", list.size());
	}
}
