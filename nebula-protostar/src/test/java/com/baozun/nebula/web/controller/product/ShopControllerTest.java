/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.controller.product;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import loxia.dao.Page;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.baseinfo.ShopController;

/**
 * @author li.feng 2013-7-3上午10:29:21
 */
public class ShopControllerTest{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ShopControllerTest.class);

	private ShopController		shopController;

	private IMocksControl		control;

	private ShopManager			shopManager;

	private IndustryManager		industryManager;

	private PropertyManager		propertyManager;

	private HttpServletRequest	request;

	private HttpServletResponse	response;

	private Model				model;

	private OrganizationManager	organizationManager;

	// private BackWarnEntity backWarnEntity;

	private HttpSession			session;

	@Before
	public void init(){

		shopController = new ShopController();
		control = createNiceControl();

		// mock一个shopManager对象
		shopManager = control.createMock("ShopManager", ShopManager.class);
		industryManager = control.createMock("industryManager", IndustryManager.class);
		organizationManager = control.createMock("organizationManager", OrganizationManager.class);
		propertyManager = control.createMock("propertyManager", PropertyManager.class);

		ReflectionTestUtils.setField(shopController, "shopManager", shopManager);
		ReflectionTestUtils.setField(shopController, "industryManager", industryManager);
		ReflectionTestUtils.setField(shopController, "organizationManager", organizationManager);
		ReflectionTestUtils.setField(shopController, "propertyManager", propertyManager);

		// 使用 EasyMock 生成 Mock 对象；
		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		response = control.createMock("HttpServletResponse", HttpServletResponse.class);
		model = control.createMock("model", Model.class);
		session = control.createMock("session", HttpSession.class);

		// backWarnEntity = new BackWarnEntity();

	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#findRoleListJson(com.baozun.nebula.utils.query.bean.QueryBean, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testfindShopListJson(){

		QueryBean queryBean = new QueryBean();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("lifecycle", "1");
		paraMap.put("name", "%商城%");
		paraMap.put("code", "2000");
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);
		queryBean.setParaMap(paraMap);

		List<ShopCommand> mockResult = new ArrayList<ShopCommand>();
		ShopCommand shopCommand = new ShopCommand();
		shopCommand.setCategoryList(null);
		Date createTime = new Date(123123123L);
		shopCommand.setCreateTime(createTime);
		shopCommand.setDescription("什么店铺呢");
		shopCommand.setOrganizationid(1L);
		shopCommand.setLifecycle(1);
		shopCommand.setShopcode("2000");
		shopCommand.setShopname("耐克官方商城");
		mockResult.add(shopCommand);
//		Page page = new Page(0, 100);
		Sort[] sorts = null;
		EasyMock.expect(shopManager.findShopListByQueryMap(queryBean.getParaMap(),sorts)).andReturn(mockResult);

		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals(mockResult, shopController.findShopListJson(queryBean, model, request, response));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#toOrganizationList(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 */
	@Test
	public final void testFindShopList(){

		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals("product/shop/shop-list", shopController.findShopList(model, request, response));

		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#removeUser(java.lang.Long[], org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testRemoveShopByIds() throws Exception{
		Long[] ids = { 1L, 2L };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		Integer mockResult=2;
		EasyMock.expect(shopManager.removeShopByIds(ids)).andReturn(mockResult);
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		// 验证
		assertEquals(map, shopController.removeShopByIds(ids, model, request, response));
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.baseinfo.ShopController#createShop(org.springframework.ui.Model)}.
	 */
	@Test
	public final void testCreateShop(){
		List<Map<String, Object>> industryList = new ArrayList<Map<String, Object>>();
		Sort[] sorts = Sort.parse("id desc");
		List<Industry> mockResult = new ArrayList<Industry>();

		EasyMock.expect(shopManager.findAllIndustryList(sorts)).andReturn(mockResult);
		//EasyMock.expect(shopController..processIndusgtryList(mockResult)).andReturn(industryList);

		EasyMock.expect(model.addAttribute(EasyMock.eq("industryList"), EasyMock.eq(industryList))).andReturn(model);
		control.replay();
		// ontrol.replay(shopManager.shopPropertyManager()).andReturn("/product/shop/shop-manager"));
		assertEquals("/product/shop/add-shop", shopController.createShop(model));
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.web.controller.baseinfo.ShopController#updateshop(org.springframework.ui.Model)}.
	 */
	@Test
	public final void testUpdateshop(){
		List<Map<String, Object>> industryList = new ArrayList<Map<String, Object>>();
		Sort[] sorts = Sort.parse("id desc");
		List<Industry> mockResult = new ArrayList<Industry>();
		
		EasyMock.expect(shopManager.findAllIndustryList(sorts)).andReturn(mockResult);
		//EasyMock.expect(shopManager.processIndusgtryList(mockResult)).andReturn(industryList);
		EasyMock.expect(model.addAttribute(EasyMock.eq("industryList"), EasyMock.eq(industryList))).andReturn(model);
		Long up_orgId = 2L;
		/*
		 * //创建一个Mock HttpServletRequest的MockControl对象 MockControl control=MockControl.createControl(HttpServletRequest.class); //获取一个Mock
		 * HttpServletRequest对象 request=(HttpServletRequest)control.getMock(); //设置期望调用的Mock HttpServletRequest对象的方法
		 * request.getParameter("orgId"); //以下后两个参数表示最少调用一次，最多调用一次 control.setReturnValue("21" ,1 ,1); //设置Mock
		 * HttpServletRequest的状态，/表示此Mock HttpServletRequest对象可以被使用 //　control.replay();
		 */
		EasyMock.expect(request.getParameter("orgId")).andReturn("21").once();
		// EasyMock.expect(Long.parseLong(request.getParameter("orgId")));
		EasyMock.expect(model.addAttribute(EasyMock.eq("up_orgId"), EasyMock.eq(up_orgId))).andReturn(model);

		Long shopId = null;
		//EasyMock.expect(shopManager.findShopIdbyOrgId(up_orgId)).andReturn(shopId);
		EasyMock.expect(model.addAttribute(EasyMock.eq("shopId"), EasyMock.eq(shopId))).andReturn(model);

		Organization orgInfo = new Organization();
		EasyMock.expect(organizationManager.findOrgbyId(up_orgId)).andReturn(orgInfo);
		EasyMock.expect(model.addAttribute(EasyMock.eq("orgInfo"), EasyMock.eq(orgInfo))).andReturn(model);

		control.replay();
		//assertEquals("/product/shop/update-shop", shopController.updateshop(model, request, response));
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.product.ShopController#saveShop(Map<String, Object>)}.
	 */
	@Test
	public final void testSaveShop(){
		/*
		 * String up_orgId = "add"; String industrys = "123,118"; String industryAttr[] = industrys.split(","); Long id = 5L; String
		 * updatespp = ""; Map<String, Object> map = new HashMap<String, Object>(); map.put("orgId", 5l); map.put("success", "success");
		 * EasyMock.expect( shopManager.createOrUpdateShop( (Long) EasyMock.isNull(), (Long) EasyMock.isNull(), EasyMock.eq("tttt"),
		 * EasyMock.eq("tttt"), EasyMock.eq("tttt"), EasyMock.eq(1), EasyMock.aryEq(industryAttr))).andReturn(id); control.replay();
		 * assertEquals(map, shopController.saveShop(updatespp, up_orgId, "tttt", "tttt", "tttt", 1, industrys));
		 */

		/*String up_orgId = "add";
		String industrys = "123,118";
		String industryAttr[] = industrys.split(",");
		Long id = 5L;
		String updatespp = "addspp";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "success");
		map.put("shopId", 23L);
		EasyMock.expect(
				shopManager.createOrUpdateShop(
						(Long) EasyMock.isNull(),
						(Long) EasyMock.isNull(),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq(1),
						EasyMock.aryEq(industryAttr))).andReturn(id);
		EasyMock.expect(shopManager.findShopIdbyOrgId(id)).andReturn(23L);
		control.replay();
		assertEquals(map, shopController.saveShop(updatespp, up_orgId, "tttt", "tttt", "tttt", 1, industrys));*/

		
		String industrys = "123,118";
		String industryAttr[] = industrys.split(",");
		String up_orgId = "32";
		String updatespp = "updatespp";
		Long shopId = 23L;
		Long orgId = 1L;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "success");
		map.put("shopId", shopId);
		//EasyMock.expect(shopManager.findShopIdbyOrgId(Long.parseLong(up_orgId))).andReturn(shopId);
		/*EasyMock.expect(
				shopManager.createOrUpdateShop(
						EasyMock.eq(Long.parseLong(up_orgId)),
						EasyMock.eq(shopId),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq(1),
						EasyMock.aryEq(industryAttr))).andReturn(orgId);
		
		control.replay();
		assertEquals(map, shopController.saveShop(updatespp, up_orgId, "tttt", "tttt", "tttt", 1, industrys));*/
		 

		/*String industrys = "123,118";
		String industryAttr[] = industrys.split(",");
		String up_orgId = "32";
		String updatespp = "updatespp";
		Long shopId = 23L;
		Long orgId = 5L;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "success");
		map.put("shopId", shopId);
		
		EasyMock.expect(shopManager.findShopIdbyOrgId(Long.parseLong(up_orgId))).andReturn(shopId);
		EasyMock.expect(
				shopManager.createOrUpdateShop(
						EasyMock.eq(Long.parseLong(up_orgId)),
						EasyMock.eq(shopId),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq("tttt"),
						EasyMock.eq(1),
						EasyMock.aryEq(industryAttr))).andReturn(orgId);
		control.replay();
		assertEquals(map, shopController.saveShop(updatespp, up_orgId, "tttt", "tttt", "tttt", 1, industrys));*/
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#enableOrDisableShopByIds(java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public final void testEnableOrDisableShopByIds(){

		String ids = "1,2";
		String[] shopId = ids.split(",");
		int length = shopId.length;
		Long[] shopIds2 = new Long[length];
		for (int i = 0; i < length; i++){
			shopIds2[i] = Long.parseLong(shopId[i]);
		}
		Integer type = 2;
		Integer mockResult = 2;
/*
		EasyMock.expect(shopManager.enableOrDisableShopByIds(EasyMock.aryEq(shopIds2), EasyMock.eq(type))).andReturn(mockResult);*/
		// 将mock对象由Recode状态转为Replay状态
		control.replay();
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		backWarnEntity.setDescription(null);
		backWarnEntity.setErrorCode(null);
		backWarnEntity.setIsSuccess(true);
		// 验证
		assertEquals(backWarnEntity.getIsSuccess(), shopController.enableOrDisableShopByIds(ids, type).getIsSuccess());
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#shopPropertyManager(java.lang.Long, org.springframework.ui.Model)}.
	 */
	@Test
	public final void testShopPropertyManager(){
		List<Industry> list = new ArrayList<Industry>();
		ShopCommand shopCommand = new ShopCommand();
		// EasyMock.expect(industryManager.findIndustryListByShopId(2L)).andReturn(list);
		EasyMock.expect(shopManager.findShopById(2L)).andReturn(shopCommand);

		EasyMock.expect(model.addAttribute(EasyMock.eq("industryList"), EasyMock.eq(list))).andReturn(model);
		EasyMock.expect(model.addAttribute(EasyMock.eq("shop"), EasyMock.eq(shopCommand))).andReturn(model);
		// HttpSession session = null;

		EasyMock.expect(request.getSession()).andReturn(session);
		// EasyMock.expect(session.setAttribute(EasyMock.eq("shopId"), EasyMock.eq(2L))).andReturn(null);
		// TODO　　mock HttpSession error!
		// EasyMock.expect();

		control.replay();
		assertEquals("/product/shop/shop-manager", shopController.shopPropertyManager(2L, model, request));
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#findPropertyListByShopIdAndIndustryId(java.lang.Long, java.lang.Long, org.springframework.ui.Model)}
	 * .
	 */
	@Test
	public final void testFindPropertyListByShopIdAndIndustryId(){
	/*
	 * Long industryId = 1L; Long shopId = 1L; List<Property> propertyList = new ArrayList<Property>(); Property p = new Property();
	 * propertyList.add(p); propertyList = propertyManager.findPropertyListByIndustryIdAndShopId(industryId, shopId);
	 * EasyMock.expect(propertyList).andReturn(propertyList); control.replay(); assertEquals(propertyList,
	 * shopController.findPropertyListByShopIdAndIndustryId(industryId, shopId, model));
	 */
	}

	/**
	 * Test method for
	 * {@link com.baozun.nebula.web.controller.baseinfo.ShopController#enableOrDisablePropertyByIds(java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public final void testEnableOrDisablePropertyByIds(){

	/*
	 * //boolean isSuccess = true; //Integer errorCode = null; String id = 1 + ""; List<Long> ids = new ArrayList<Long>();
	 * ids.add(Long.parseLong(id)); Integer type = 1; EasyMock.expect(propertyManager.enableOrDisablePropertyByIds(ids, type)).andReturn(1);
	 * // TODO mock error here // EasyMock.expect(backWarnEntity.setDescription("asdasd")); control.replay(); BackWarnEntity backWarnEntity
	 * = new BackWarnEntity(); backWarnEntity.setDescription(null); backWarnEntity.setErrorCode(null); backWarnEntity.setIsSuccess(true);
	 * assertEquals(backWarnEntity.getIsSuccess(), shopController.enableOrDisablePropertyByIds(id, type).getIsSuccess());
	 */
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.baseinfo.ShopController#removePropertyByIds(java.lang.Long)}.
	 */
	@Test
	public final void testRemovePropertyByIds(){

	/*
	 * boolean isSuccess = true; Integer errorCode = null; List<Long> ids = new ArrayList<Long>(); long id = 2L; ids.add(id); //
	 * EasyMock.expect(propertyManager.removePropertyByIds(ids)).andReturn(1); // // Object o = new Object(); control.replay();
	 * BackWarnEntity backWarnEntity = new BackWarnEntity(); backWarnEntity.setDescription(null); backWarnEntity.setErrorCode(null);
	 * backWarnEntity.setIsSuccess(isSuccess); assertEquals(backWarnEntity.getIsSuccess(),
	 * shopController.removePropertyByIds(id).getIsSuccess());
	 */
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.baseinfo.ShopController#saveProperty(Property)}.
	 */
	@Test
	public final void testSaveProperty(){

		Property p = new Property();

		long shopId = 2L;

		EasyMock.expect(request.getSession().getAttribute("shopId")).andReturn(2L);
		// EasyMock.expect(propertyManager.createOrUpdateProperty(p, shopId, 2)).andReturn(true);

		control.replay();
		assertEquals("redirect:/shop/shopPropertymanager.htm?shopId=" + shopId, shopController.saveProperty(p, model, request));

	}

}
