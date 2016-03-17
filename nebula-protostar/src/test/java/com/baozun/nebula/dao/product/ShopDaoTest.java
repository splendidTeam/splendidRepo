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

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.auth.OrganizationDao;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;

/**
 * @author yi.huang
 * @date 2013-7-2 上午11:25:52
 */
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback = false)
public class ShopDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	private static final Logger	log	= LoggerFactory.getLogger(ShopDaoTest.class);

	@Autowired
	private ShopDao				shopDao;
	
	@Autowired
	private OrganizationDao				organizationDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#enableOrDisableShopByIds(java.lang.Long[], java.lang.Integer)}.
	 */
	@Test
	public void testEnableOrDisableShopByIds(){
		Long[] shopIds = {4L,5L};
		Integer type = 0;//禁用
		Integer result = shopDao.enableOrDisableShopByIds(shopIds, type);
		
		log.info("@_@result:{}",result);
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#removeShopByIds(java.lang.Long[])}.
	 */
	@Test
	public void testRemoveShopByIds(){
		Long[] shopIds = {6L};
		Integer result = shopDao.removeShopByIds(shopIds);
		log.info("@_@result:{}",result);
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#findShopById(java.lang.Long[])}.
	 */
	@Test
	public void testFindShopById(){
		ShopCommand shop = shopDao.findShopById(2L);
		log.info("@_@result:{}",shop.getShopname());
	}
	
	
	
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#findIndustryList(List<Industry>)}.
	 */
	@Test
	public void testFindIndustryList()
	{
		Sort[] sorts = Sort.parse("id desc");
		List<Industry> industryList = shopDao.findIndustryList(sorts);
		log.info("@_@result:{}",industryList);
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#createShopproperty(boolean)}.
	 */
	@Test
	public void testCreateShopproperty()
	{
		Integer flag = shopDao.createShopproperty(105L,2L,10L);
//		Integer flag = shopDao.createShopproperty(105L,2L,null);
		log.info("@_@result:{}",flag);
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#removeShopPropertyByshopId(boolean)}.
	 */
	
	@Test
	public void testRemoveShopPropertyByshopId()
	{
		Integer flag = shopDao.removeShopPropertyByshopId(14L);
		log.info("@_@result:{}",flag);
	}
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#updateOrganization(Integer)}.
	 */
	@Test
	public void testUpdateOrganization()
	{
		Integer flag = shopDao.updateOrganization(2L,"耐克官方商城", "2000", "uuuuu", 1);
		log.info("@_@result:{}",flag);		
	}	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#updateShopproperty(Integer)}.
	 */
	@Test
	public void testUpdateShopproperty()
	{
		Integer flag = shopDao.updateShopproperty(105, 4,20L);
		log.info("@_@result:{}",flag);		
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopDao#findPropertyListByIndustryIdAndShopId(List<Property>)}.
	 */
	@Test
	public void testFindPropertyListByIndustryIdAndShopId()
	{
		List<Property> flag = shopDao.findPropertyListByIndustryIdAndShopId(169L,23L,null);
		log.info("@_@result:{}",flag.get(0).getId());		
	}
	
}
