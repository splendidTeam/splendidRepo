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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.model.product.ShopProperty;


/**
 *
 * @author yi.huang
 *
 * @date 2013-7-4 下午03:51:16 
 */@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback = false)
public class ShopPropertyDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	private static final Logger	log	= LoggerFactory.getLogger(ShopPropertyDaoTest.class);
	
	@Autowired
	private ShopPropertyDao shopPropertyDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopPropertyDao#addShopProperty(java.lang.Long, java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testAddShopProperty(){
		Long industryId = 1L;
		Long propertyId = 2L;
		Long shopId = 3L;
		Integer result = shopPropertyDao.addShopProperty(industryId, propertyId, shopId);
		log.info("@_@:::{}",result);
	}
	
	@Test
	public void testFindShopPropertyByshopId(){
		List<ShopProperty> list = shopPropertyDao.findShopPropertyByshopId(2L);
		log.info("@_@:::{}",list.size());
	}
	
}

