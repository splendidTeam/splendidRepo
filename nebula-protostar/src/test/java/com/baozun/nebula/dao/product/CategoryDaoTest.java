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

import com.baozun.nebula.dao.sns.ConsultantsDao;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.utils.JsonFormatUtil;

/**
 * @author yi.huang
 * @date 2013-6-20 下午02:12:19
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback = false)
public class CategoryDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	private static final Logger	log	= LoggerFactory.getLogger(CategoryDaoTest.class);

	@Autowired
	private CategoryDao			categoryDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.CategoryDao#insertCategory(java.lang.Long, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testInsertCategory(){
		Integer res = categoryDao.insertCategory(70L, "car_beauty_other", "其他");
		log.info("======================res{}", res);
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.dao.product.CategoryDao#updateCategoryById(java.lang.Long, java.lang.String, java.lang.String)} .
	 */
	@Test
	public void testUpdateCategoryById(){
		Integer res = categoryDao.updateCategoryById(8L, "20130621", "20130621蛋白粉2");
		log.info("======================res{}", res);
	}

	@Test
	public void treeUpdateNodeParentId(){
		Integer res = categoryDao.treeUpdateNodeParentId(105L, 2L);
		log.info("======================res{}", res);
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.CategoryDao#removeCategoryById(java.lang.Long)} .
	 */
	@Test
	public void testRemoveCategoryById(){
		Integer res = categoryDao.removeCategoryById(8L);
		log.info("======================res  :{}", res);
		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.CategoryDao#findCategoryById(java.lang.Long)} .
	 */
	@Test
	public void testFindById(){
		Category cg = categoryDao.findCategoryById(8L);
		log.info(JsonFormatUtil.format(cg));
	}

	@Test
	public void findCategoryByCode(){
		String code = "auto_pillow_all";
		Category category = categoryDao.findCategoryByCode(code);
		log.info(JsonFormatUtil.format(category));
	}

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.CategoryDao#findCategoryList()}
	 */
	@Test
	public void testFindEnableCategoryList(){
		List<Category> list = categoryDao.findEnableCategoryList(Sort.parse("PARENT_ID asc,SORT_NO asc"));
		log.info(JsonFormatUtil.format(list));
	}

	@Test
	public void testFindCategoryListByCategoryIds(){
		Long[] categoryIds = { 22L, 23L };
		List<Category> list = categoryDao.findCategoryListByCategoryIds(categoryIds);
		log.info(JsonFormatUtil.format(list));
	}

	@Test
	public void incrCategorySortNoGtAndEqSelectCategorySortNo(){
		log.info(categoryDao.treeIncrSortNoGtAndEqTargetNodeSortNo(36L) + "");
	}

	@Test
	public void insertCategoryWithSortNo(){

		Long parentId = 19L;
		String code = "3";
		String name = "3";
		Integer sortNo = 3;
		Integer insertCategoryWithSortNo = categoryDao.treeInsertNodeWithSortNo(parentId, code, name, sortNo);
		log.info(insertCategoryWithSortNo + "");
	}
}
