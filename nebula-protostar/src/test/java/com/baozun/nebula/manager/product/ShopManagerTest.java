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
package com.baozun.nebula.manager.product;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import loxia.dao.Page;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.auth.OrganizationDao;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;


/**
 *
 * @author yi.huang
 *
 * @date 2013-7-2 上午11:35:33 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")  
public class ShopManagerTest{

	private static final Logger	log	= LoggerFactory.getLogger(ShopManagerTest.class);
	
	@Autowired
	private ShopManager shopManager;
	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.ShopManager#enableOrDisableShopByIds(java.lang.Long[], java.lang.Integer)}.
	 */
	@Test
	public void testEnableOrDisableShopByIds(){
		Long[] shopIds = {4L,5L};
		Integer type = 1;//禁用
		Integer flag = shopManager.enableOrDisableShopByIds(shopIds, type);
		log.info("@_@result:{}",flag);
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.ShopManager#findShopListByOrgaTypeId(java.lang.Long, java.util.Map)}.
	 */
	@Test
	public final void testFindShopListByOrgaTypeId(){
//		Long orgaTypeId = 1L;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("lifecycle", 1);
		paraMap.put("name", "%商城%");
		paraMap.put("code", "2000");
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);
//		Page page = new Page(0, 100);
		Sort[] sorts = null;
		List<ShopCommand> shopCommandList = shopManager.findShopListByQueryMap(paraMap,sorts);
		for (ShopCommand s : shopCommandList){

			log.info("shopCommandList is shopname" + s.getShopname());
		}
		// fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.ShopManager#removeShopByIds(java.lang.Long[])}.
	 */
	@Test
	public void testRemoveShopByIds(){
		Long[] shopIds = {4L,5L};
		Integer flag = shopManager.removeShopByIds(shopIds);
		log.info("@_@result:{}",flag);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ShopManager#addShopByOrgId(Shop)}.
	 *//*
	@Test
	public void testAddShopByOrgId()
	{
		Organization org = new Organization();
		Shop shop = shopManager.addShopByOrgId("tired", "tired", "tired",1,  new String[]{"105","101","102"},org);
		log.info("@_@result:{}",shop);
	}*/
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.ShopManager#AddShop(boolean)}.
	 */
	@Test
	public void testAddShop()
	{
		/*Organization org = new Organization();
		Long flag = shopManager.addShop("噢噢噢噢","iiiiii","iii", 1, new String[]{"105","101","102"});
		log.info("@_@result:{}",flag);*/
	}
	/**
	 * Test method for {@link com.baozun.nebula.manager.baseinfo.ShopManager#updateShop(boolean)}.
	 */
	@Test
	public void testUpdateShop()
	{
		/*//boolean flag = shopManager.updateShop(23L, 14L, "SOA商城", "0001","测试通过啊",0,null);
		boolean flag = shopManager.updateShop(4L, 4L,"匡威官方商城", "2131", "匡威官方商城", 1,new String[]{"110","123","108"});
		log.info("@_@result:{}",flag);*/
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ShopManager#findIndustryList(List<Industry>)}.
	 */
	@Test
	public void testFindIndustryList()
	{
		Sort[] sorts = Sort.parse("id desc");
		List<Industry> industryList = shopManager.findAllIndustryList(sorts);
		log.info("@_@result:{}",industryList);
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopManager#findPropertyListByIndustryIdAndShopId(List<Property>)}.
	 */
	@Test
	public void testFindPropertyListByIndustryIdAndShopId()
	{
		List<Property> flag = shopManager.findPropertyListByIndustryIdAndShopId(121L,2L,null);
		log.info("@_@result:{}",flag);		
	}
	
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopManager#createOrUpdateShop(Long}.
	 */
	@Test
	public void testCreateOrUpdateShop()
	{
		/*//Long flag = shopManager.createOrUpdateShop(32L, 23L, "ceceshishi", "ceceshishi", "ceceshishi", 1, new String[]{"105","101","102"});
		Long flag = shopManager.createOrUpdateShop(null, null, "ceceshishi", "ceceshishi", "ceceshishi", 1, new String[]{"105","101","102"});
		log.info("@_@result:{}",flag);	*/
	}
	
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopManager#findSameOrgName(Integer}.
	 */
	@Test
	public void testFindSameOrgName()
	{
		Integer flag = organizationDao.validateShopCode("嘎嘎嘎嘎");
		log.info("@_@result:{}",flag);	
	}
}

