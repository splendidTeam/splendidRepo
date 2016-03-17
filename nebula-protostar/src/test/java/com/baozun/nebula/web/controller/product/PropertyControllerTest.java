///**
// * Copyright (c) 2012 Baozun All Rights Reserved.
// *
// * This software is the confidential and proprietary information of Baozun.
// * You shall not disclose such Confidential Information and shall use it only in
// * accordance with the terms of the license agreement you entered into
// * with Baozun.
// *
// * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
// * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
// * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
// * THIS SOFTWARE OR ITS DERIVATIVES.
// */
//package com.baozun.nebula.web.controller.product;
//
//import static org.easymock.EasyMock.createNiceControl;
//import static org.junit.Assert.assertEquals;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import loxia.dao.Page;
//import loxia.dao.Pagination;
//import loxia.dao.Sort;
//
//import org.easymock.EasyMock;
//import org.easymock.IMocksControl;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.ui.Model;
//
//import com.baozun.nebula.command.PropertyCommand;
//import com.baozun.nebula.manager.product.IndustryManager;
//import com.baozun.nebula.manager.product.PropertyManager;
//import com.baozun.nebula.model.product.Industry;
//import com.baozun.nebula.model.product.Property;
//import com.baozun.nebula.model.product.PropertyValue;
//import com.baozun.nebula.utils.query.bean.QueryBean;
//
///**
// * 
// * @author lin.liu
// * 
// *         属性管理controller
// */
//public class PropertyControllerTest {
//	@SuppressWarnings("unused")
//	private static final Logger log = LoggerFactory
//			.getLogger(PropertyControllerTest.class);
//
//	private PropertyController propertyController;
//
//	private PropertyManager propertyManager;
//
//	private IndustryManager industryManager;
//
//	private HttpServletRequest request;
//
//	private HttpServletResponse response;
//
//	private IMocksControl control;
//
//	private QueryBean queryBean;
//
//	private Model model;
//
//	@Before
//	public void init() {
//		control = createNiceControl();
//		propertyController = new PropertyController();
//
//		propertyManager = control.createMock("PropertyManager",
//				PropertyManager.class);
//		ReflectionTestUtils.setField(propertyController, "propertyManager",
//				propertyManager);
//
//		queryBean = control.createMock("QueryBean", QueryBean.class);
//		ReflectionTestUtils
//				.setField(propertyController, "queryBean", queryBean);
//
//		industryManager = control.createMock("IndustryManager",
//				IndustryManager.class);
//		ReflectionTestUtils.setField(propertyController, "industryManager",
//				industryManager);
//
//		request = control.createMock("HttpServletRequest",
//				HttpServletRequest.class);
//		model = control.createMock("model", Model.class);
//	}
//
//	@Test
//	public void testPropertyManager() {
//		List<Industry> result = new ArrayList<Industry>();
//		result.add(new Industry());
//		EasyMock.expect(industryManager.findAllIndustryList()).andReturn(result);
//		// 设置放方法返回值后replay一下
//		control.replay();
//		// 返回和Controller方法一样的返回值
//		assertEquals("/product/propertylist",
//				propertyController.propertyManager(model));
//	}
//
//	@Test
//	public void testFindProductPropertylist() {
//		Pagination<PropertyCommand> propertys = new Pagination<PropertyCommand>();
//		Map<String, Object> paraMap = new HashMap<String, Object>();
//		Sort[] sorts2 = new Sort[] {};
//		EasyMock.expect(
//				propertyManager.findPropertyListByQueryMapWithPage(new Page(),
//						sorts2, paraMap)).andReturn(propertys);
//		control.replay();
//		assertEquals(propertys, propertyController.findProductPropertylist(
//				model, queryBean, request, response));
//	}
//
//	@Test
//	public void testRemovePropertyByIds() {
//		List<Long> ids = new ArrayList<Long>();
//		ids.add(1L);
//		ids.add(2L);
//		EasyMock.expect(propertyManager.removePropertyByIds(ids)).andReturn(
//				false);
//		control.replay();
//		assertEquals(new HashMap<String, Object>(),
//				propertyController.removePropertyByIds("1,2,3"));
//	}
//
//	@Test
//	 public void testEnableOrDisablePropertyByIds() {
//		Long propertyId=1L;
//		Integer state=1;
//		HashMap<String, Object> map=new HashMap<String, Object>();
//		EasyMock.expect(propertyController.enableOrDisablePropertyByIds(propertyId, state)).andReturn(map);
//		control.replay();
//		assertEquals(map, propertyController.enableOrDisablePropertyByIds(propertyId, state));
//	 }
//	
//	@Test
//	public final void testviewPropertyValue(){
//		Property property = new Property();
//		List<PropertyValue> propertyValue = new ArrayList<PropertyValue>();
//		Industry industry = new Industry();
//		
//		EasyMock.expect(propertyManager.findPropertyById(1L)).andReturn(property);
//		EasyMock.expect(industryManager.findIndustryById(propertyManager.findPropertyById(1L).getIndustryId())).andReturn(industry);
//		EasyMock.expect(propertyManager.findPropertyValueList(1L)).andReturn(propertyValue);
//		
//		EasyMock.expect(model.addAttribute(EasyMock.eq("propertyName"), EasyMock.eq(property.getName()))).andReturn(model);
//		EasyMock.expect(model.addAttribute(EasyMock.eq("industryName"), EasyMock.eq(industry.getName()))).andReturn(model);
//		EasyMock.expect(model.addAttribute(EasyMock.eq("propertyValue"), EasyMock.eq(propertyValue))).andReturn(model);
//		
//		control.replay();
//		// 验证
//		assertEquals("product/shop/shopPropertyValue", propertyController.viewPropertyValue(1L, model, request));
//	} 
//}
