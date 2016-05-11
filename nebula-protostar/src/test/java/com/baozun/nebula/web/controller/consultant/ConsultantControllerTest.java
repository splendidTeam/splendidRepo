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
 *
 */

package com.baozun.nebula.web.controller.consultant;

import static org.easymock.EasyMock.createNiceControl;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.product.ConsultantManager;
import com.baozun.nebula.utils.query.bean.QueryBean;

/**
 * @author - 项硕
 */
public class ConsultantControllerTest {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ConsultantControllerTest.class);
	
	private IMocksControl control;
	
	private ConsultantController consultantController;
	
	private ConsultantManager consultantManager;
	
	private HttpServletRequest 		request;
	private HttpServletResponse		response;
	private Model					model;
	
	@Before
	public void setup(){
		consultantController = new ConsultantController();
		control = createNiceControl();
		
		consultantManager = control.createMock(ConsultantManager.class);
		ReflectionTestUtils.setField(consultantController, "consultantManager", consultantManager);
		
		request	= control.createMock(HttpServletRequest.class);
		response = control.createMock(HttpServletResponse.class);
		model = control.createMock(Model.class);
	}
	

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.consultant.ConsultantController#consultantList(javax.servlet.http.HttpServletRequest, org.springframework.ui.Model)}.
	 */
	@Test
	public void testConsultantList() {
		String cookieName = "c_un_" + "test_name";
		String cookieValue = "test_value";
		Cookie[] cookies = {new Cookie(cookieName, cookieValue)};
		expect(request.getCookies()).andReturn(cookies);
		expect(model.addAttribute(cookieName, cookieValue)).andReturn(model);
		control.replay();
		assertEquals("consultant/consultant-list",consultantController.consultantList(request, model));
		control.verify();
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.consultant.ConsultantController#getConsultantListData(com.baozun.nebula.utils.query.bean.QueryBean, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testGetConsultantListData() {
		QueryBean q = new QueryBean();
		q.setPage(new Page(0, 3));
		q.setSorts(null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemCode", "110");
		consultantController.getConsultantListData(q, 3, request, response);
	}

}
