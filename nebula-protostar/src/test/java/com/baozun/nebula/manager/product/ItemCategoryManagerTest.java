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

import java.util.HashMap;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;


/**
 *
 * @author yi.huang
 *
 * @date 2013-7-1 上午11:18:56 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ItemCategoryManagerTest{
	
	@Autowired
	private ItemCategoryManager itemCategoryManager;

	private static final Logger	log	= LoggerFactory.getLogger(ItemCategoryManagerTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemCategoryManagerImpl#addItemCategorys(java.lang.Long[], java.lang.Long[])}.
	 */
	@Test
	public void testBindItemCategory(){
		//商品"冰冰冰"现未关联任何一个商品,分类"袜子"id:157
		Long[] itemIds = {7L};
		Long[] categoryIds ={157L};
		ItemCategoryResultCommand ic = itemCategoryManager.bindItemCategory(itemIds, categoryIds);
		log.info("=_+:success：{};repeat:{}",ic.getSuccessMap().size(),ic.getRepeatMap().size());
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemCategoryManagerImpl#deleteItemCategory(java.lang.Long[], java.lang.Long)}.
	 */
	@Test
	public void testUnBindItemCategory(){
		//商品"冰冰冰"、"8899"现关联同一个商品分类"袜子",
		//"冰冰冰"id:7,"8899":472;袜子id:157
		Long[] itemIds = {7L,472L};
		Long categoryId = 157L;
		boolean flag = itemCategoryManager.unBindItemCategory(itemIds, categoryId);
		log.info("+_+:{}",flag);
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.ItemCategoryManagerImpl#findItemListByCategoryId(java.lang.Long, loxia.dao.Page, loxia.dao.Sort[])}.
	 */
	@Test
	public void testFindItemListByCategoryId(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Long categoryId = 24L;
		Pagination<Item> page = itemCategoryManager.findItemListByCategoryId(categoryId, p, null);
		log.info("+_+：{},{}",page.getItems().size(),page.getTotalPages());
	}
	
	@Test
	public void testfindItemCtListByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("tpit.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		Long shopId =256L;
		Pagination<ItemCommand> page = itemCategoryManager.findItemCtListByQueryMapWithPage(p, sorts, map,shopId);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}
	
	@Test
	public void testfindItemNoctListByQueryMapWithPage(){
		Page p = new Page();
		p.setSize(5);
		p.setStart(0);
		Sort[] sorts=Sort.parse("tpit.create_time desc");
		Map<String, Object> map = new HashMap<String, Object>();
		Long shopId =256L;
		Pagination<ItemCommand> page = itemCategoryManager.findItemNoctListByQueryMapWithPage(p, sorts, map,shopId);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(page));
	}
}

