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
package com.baozun.nebula.dao.product;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.product.PropertyValue;

/**
 * @author wenxiu.ke
 * 商品属性值设置Dao测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class PropertyValueDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	protected static final Logger log = LoggerFactory.getLogger(PropertyValueDaoTest.class);
	@Autowired
	private PropertyValueDao propertyValueDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.PropertyValueDao#findPropertyValueListById(java.lang.Long)}.
	 */
	@Test
	public void testFindPropertyValueListById() {
		List<PropertyValue> propertyValues = propertyValueDao.findPropertyValueListById(1L);
		for(PropertyValue pv:propertyValues){
			log.info("findPropertyValueListById 方法返回{}",pv.getValue());
		}
		
	}



}
