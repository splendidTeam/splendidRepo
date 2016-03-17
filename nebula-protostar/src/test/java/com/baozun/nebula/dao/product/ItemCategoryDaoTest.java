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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.utils.JsonFormatUtil;

/**
 * @author yi.huang
 * @date 2013-6-28 下午06:36:50
 */
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback = false)
public class ItemCategoryDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private ItemCategoryDao		itemCategoryDao;

	private static final Logger	log	= LoggerFactory.getLogger(ItemCategoryDaoTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ItemCategoryDao#addItemCategory(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testBindItemCategory(){

		Integer res = itemCategoryDao.bindItemCategory(7L, 25L, false);
		log.info("res+++++++++++#########::::{}", res);
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ItemCategoryDao#deleteItemCategory(java.lang.Long[], java.lang.Long)}.
	 */
	@Test
	public void testUnBindItemCategory(){
		Long[] itemIds = { 7L};
		Long categoryId = 25L;
		Integer res = itemCategoryDao.unBindItemCategory(itemIds, categoryId);
		log.info("delete .....========:{}", res);
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.dao.product.ItemCategoryDao#findItemCategoryByItemIdAndCategoryId(java.lang.Long[], java.lang.Long[])}.
	 */
	@Test
	public void testFindItemCategoryByItemIdAndCategoryId(){
		Long[] itemIds = { 1L };
		Long[] categoryIds = { 22L };
		List<ItemCategory> compareList = itemCategoryDao.findItemCategoryByItemIdAndCategoryId(itemIds, categoryIds);
		log.info("====########:{},{}", compareList.size(), compareList.get(0).getCategoryId());
	}

	@Test
	public void testfindItemIdByCategoryId(){
	/*
	 * List<Long> list = itemCategoryDao.findItemIdByCategoryId(22L); System.out.println(list); log.info("====########:{}",list.get(0));
	 */
	}

	@Test
	public void testFindItemByCategoryId(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Pagination<Item> page = itemCategoryDao.findItemListByCategoryId(22L, p, null);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", page.getItems().size(), page.getTotalPages());
	}
	
	/*@Test
	public void testFindItemListEmptyCategoryByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("tpit.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "%20%");
		//Pagination<ItemCommand> page = itemCategoryDao.findItemListEmptyCategoryByQueryMapWithPage(p, sorts, map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}*/
	
	@Test
	public void testFindAllItemCategoryList(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		List<ItemCategoryCommand> itemCategoryCommands = itemCategoryDao.findAllItemCategoryList();
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(itemCategoryCommands));
	}
	
/*	@Test
	public void testFindItemNoctListByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("tpit.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "%dfd%");
		Pagination<ItemCommand> page = itemCategoryDao.findItemNoctListByQueryMapWithPage(p, sorts, map);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}*/
}
