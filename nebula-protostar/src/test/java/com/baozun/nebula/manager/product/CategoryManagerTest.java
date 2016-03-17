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

import java.util.List;

import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;

import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.product.CategoryCommand;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CategoryManagerTest{

	private static final Logger	logger	= (Logger) LoggerFactory.getLogger(CategoryManagerTest.class);

	@Autowired
	private CategoryManager		categoryManager;

	/**
	 * Test method for
	 * {@link com.baozun.nebula.manager.product.CategoryManagerImpl#addLeafCategory(java.lang.Long, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddCategory(){
		// 重复的code。。exception() ConstraintViolationException
		try{
			categoryManager.addLeafCategory(5L, "6", "蛋白粉6");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.CategoryManagerImpl#findCategoryById(java.lang.Long)}.
	 */
	@Test
	public void testFindCategoryById(){
		Category cg = categoryManager.findCategoryById(14L);
		logger.info("==============-----------=====" + cg.getName());
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.manager.product.CategoryManagerImpl#modifyCategory(java.lang.Long, java.lang.String, java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testModifyCategory() throws Exception{
		categoryManager.updateCategory(14L, "4", "百事淘宝2");
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.CategoryManagerImpl#removeCategoryById(java.lang.Long)}.
	 */
	@Test
	public void testRemoveCategoryById(){
		categoryManager.removeCategoryById(20L);
	}

	@Test
	public void testFindEnableCategoryList(){
		Sort[] sort = Sort.parse("PARENT_ID asc,SORT_NO asc");
		List<Category> list = categoryManager.findEnableCategoryList(sort);
		logger.info("the size of the list is :{}", list.size());
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void addMutlCategory(){
		ProfileConfigUtil.setMode("dev");
		CategoryCommand command = new CategoryCommand();
		command.setParentId(7l);
		command.setCode("c2");
		MutlLang lang = new MutlLang();
		String[]  values = new String[]{"中文1","English1"};
		String[]  langs = new String[]{"zh_cn","en_us"};
		lang.setValues(values);
		lang.setLangs(langs);
		command.setName(lang);
		categoryManager.addLeafCategory(command);
	}
	@Test
	@Transactional
	@Rollback(false)
	public void mutlupdateCategory(){
		ProfileConfigUtil.setMode("dev");
		CategoryCommand command = new CategoryCommand();
		command.setId(64l);
		command.setParentId(7l);
		command.setCode("c2");
		MutlLang lang = new MutlLang();
		String[]  values = new String[]{"中文2","English2"};
		String[]  langs = new String[]{"zh_cn","en_us"};
		lang.setValues(values);
		lang.setLangs(langs);
		command.setName(lang);
		categoryManager.updateCategory(command);
	}
	
	
}
