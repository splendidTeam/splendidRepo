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
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.model.product.Property;
/**
 * 
 * @author  lin.liu
 *
 *	属性管理dao 测试
 */
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class PropertyDaoTest  extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PropertyDao propertyDao;
	
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(PropertyDaoTest.class);
	@Test
	public void testFindAllPropertyList() {
		List<Property> resultList=propertyDao.findAllPropertyList();
		log.info("------------------testFindAllPropertyList  ={}",resultList.size());
	}

	@Test
	public void testFindPropertyListByIds() {
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
//		List<Property> resultList=propertyDao.findPropertyListByIds(ids);
//		log.info("------------------testFindPropertyListByIds  ={}",resultList.size());
	}

	@Test
	public void testFindPropertyListByQueryMap() {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("lifecycle",0);
		List<Property> resultList=propertyDao.findPropertyListByQueryMap(map);
		log.info("------------------testFindPropertyListByQueryMap  ={}",resultList.size());
	}

	@Test
	public void testFindPropertyListByQueryMapWithPage() {
		Page page=new Page();
		page.setSize(10);
		page.setStart(0);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("lifecycle",0);
		Pagination<PropertyCommand> propertys=propertyDao.findPropertyListByQueryMapWithPage(page, null, map);
		log.info("------------------testFindPropertyListByQueryMapWithPage  ={}",propertys.getItems().size());

	}

	@Test
	public void testEnableOrDisablePropertyByIds() {
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		Integer state=1;
		Integer result=propertyDao.enableOrDisablePropertyByIds(ids, state);
		log.info("------------------testEnableOrDisablePropertyByIds  ={}",result>0?"成功":"失败");

	}

	@Test
	public void testRemovePropertyByIds() {
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		Integer result=propertyDao.removePropertyByIds(ids);
		log.info("------------------testRemovePropertyByIds  ={}",result>0?"成功":"失败");

	}

	@Test
	public void testFindAllEffectPropertyList() {
		List<Property> resultList=propertyDao.findAllEffectPropertyList();
		log.info("------------------testFindAllEffectPropertyList  ={}",resultList.size());

	}

	@Test
	public void testFindEffectPropertyListByQueryMap() { 
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("industryId", 1L);
		map.put("name", "张三");
		List<Property> resultList=propertyDao.findEffectPropertyListByQueryMap(map);
		log.info("------------------testFindEffectPropertyListByQueryMap  ={}",resultList.size());

	}

	@Test
	public void testFindEffectPropertyListByQueryMapWithPage() {
		Page page=new Page();
		page.setSize(10);
		page.setStart(0);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("industryId", 1L);
		map.put("name", "张三");
		Pagination<Property> propertys=propertyDao.findEffectPropertyListByQueryMapWithPage(page, null, map); 
		log.info("------------------testFindEffectPropertyListByQueryMapWithPage  ={}",propertys.getItems().size());

	}
   
	
	@Test
	public void testUpdateSortById() {

		Integer count = propertyDao.updateSortById(36L, 1);
		log.info("updateCount=",count);	
	}
	
	@Test
	public void testFindPropertyListByIndustryId() {

		List<Property> list= propertyDao.findPropertyListByIndustryId(121L);
		log.info("listsize=",list);		
	}
	
}
