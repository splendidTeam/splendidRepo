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
package com.baozun.nebula.web.controller.product;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.model.product.Category;

/**
 * @author yi.huang
 * @date 2013-6-20 下午03:58:14
 */
public class CategoryControllerTest{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(CategoryControllerTest.class);

	private CategoryController	categoryController;

	private IMocksControl		control;

	private CategoryManager		categoryManager;

	private HttpServletRequest	request;

	private Model				model;

	@Before
	public void init(){
		categoryController = new CategoryController();
		control = createNiceControl();
		// mock一个categoryManager对象
		categoryManager = control.createMock("CategoryManager", CategoryManager.class);
		ReflectionTestUtils.setField(categoryController, "categoryManager", categoryManager);

		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		model = control.createMock("model", Model.class);
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.product.CategoryController#categoryManager(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public void testcategoryManager(){

		List<Category> categoryList = new ArrayList<Category>();
		Sort[] sorts = Sort.parse("id asc");
		EasyMock.expect(categoryManager.findEnableCategoryList(sorts)).andReturn(categoryList);
		EasyMock.expect(model.addAttribute(EasyMock.eq("categoryList"), EasyMock.eq(categoryList))).andReturn(model);
		control.replay();
		assertEquals("product/category/manager", categoryController.categoryManager(null));

		// EasyMock.expect(skuInventoryManager.findByExtentionCode("9HR-00010")).andReturn(s);
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.product.CategoryController#addCategory(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, java.lang.Long, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testAddCategory(){

		String code = "20130620";
		String name = "easymock";

		// Integer countSameCodeNo = categoryManager.countSameCodeNo(code);
		// EasyMock.expect(countSameCodeNo).andReturn(countSameCodeNo);

		// boolean addCategory = categoryManager.addCategory(5L, code, name);
		// TODO 当return为true时，controller进入if体，返回model，测试不通过，因为最后的验证返回为 空的。
		EasyMock.expect(model.addAttribute(EasyMock.eq("res"), EasyMock.eq("existSameCode"))).andReturn(model);

		categoryManager.addLeafCategory(5L, code, name);
		EasyMock.expectLastCall();

		// 将mock对象由Recode状态转为Replay状态
		control.replay();

		// 验证
		Object o = new Object();
		// assertEquals(o, categoryController.addLeafCategory(5L, code, name));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.product.CategoryController#updateCategory(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, java.lang.Long, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testUpdateCategory(){
		String newcode = "wwwwww";
		long id = 8L;
		String name = "威古氏";

		categoryManager.updateCategory(id, newcode, name);
		EasyMock.expectLastCall();

		control.replay();

		categoryController.updateCategory(id, newcode, name);

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.product.CategoryController#removeCategoryById(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, java.lang.Long)}
	 * .
	 */
	@Test
	public void testRemoveCategoryById(){
		categoryManager.removeCategoryById(5L);
		EasyMock.expectLastCall();
		control.replay();

		categoryController.removeCategoryById(5l);
		control.verify();

	}
}
