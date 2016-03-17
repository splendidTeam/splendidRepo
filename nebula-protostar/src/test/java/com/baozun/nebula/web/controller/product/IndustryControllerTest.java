/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.model.product.Industry;

/**
 * @author jumbo
 *
 */
public class IndustryControllerTest {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(IndustryControllerTest.class);
	
	private IndustryController industryController;
	
	private IMocksControl control;

	private IndustryManager industryManager;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private Model model ;
	@Before
	public void init() {
		industryController = new IndustryController();
		control = createNiceControl();
		//mock一个categoryManager对象
		industryManager = control.createMock("IndustryManager", IndustryManager.class);
		ReflectionTestUtils.setField(industryController, "industryManager", industryManager);

		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		model = control.createMock("model", Model.class);
	}	

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.product.IndustryController#findAllIndustryList(org.springframework.ui.Model)}.
	 */
	@Test
	public void testFindAllIndustryList() {	
		List<Industry> list = new ArrayList<Industry>() ;
		EasyMock.expect(industryManager.findAllIndustryList()).andReturn(list);
		control.replay();
		assertEquals("product/industry/industry", industryController.findAllIndustryList(model));
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.product.IndustryController#saveIndustry(java.lang.Long, java.lang.Integer, java.lang.String, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testSaveIndustry() {
		Industry industry = new Industry();
		industry.setLifecycle(1);
		industry.setName("测试");
		industry.setId(1L);
		EasyMock.expect(industryManager.createOrUpdateIndustry(industry, "-1")).andReturn(true);
		//将mock对象由Recode状态转为Replay状态
		control.replay();
		
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("result", "success");
		result.put("nodeId",40L);
		//验证
		assertEquals(result, industryController.saveIndustry(industry,"null", model, request, response));
	}

	/**
	 * Test method for {@link com.baozun.nebula.web.controller.product.IndustryController#removeIndustry(java.lang.Long, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testRemoveIndustry() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(5L);
		EasyMock.expect(industryManager.removeIndustryByIds(ids)).andReturn(1);
		control.replay();

		assertEquals("success", industryController.removeIndustry(5l,model,request,response));
	}
}
