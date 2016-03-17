package com.baozun.nebula.web.controller.salesorder;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemTagManager;
import com.baozun.nebula.manager.salesorder.CancelOrderAppManager;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.CancelApplicationCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.salesorder.CancelOrderAppController;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })

@ActiveProfiles("dev")
public class CancelOrderAppControllerTest extends BaseController{
	private IMocksControl		control;
	private Model				model;
	private HttpServletRequest	request;
	private CancelOrderAppManager cancelOrderAppManager;
	private HttpServletResponse	response;
	private CancelOrderAppController cancelOrderAppController;
	
	@Before
	public void init(){
		cancelOrderAppController=new CancelOrderAppController();
		control = createNiceControl();
		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		response = control.createMock("HttpServletResponse", HttpServletResponse.class);
		cancelOrderAppManager=control.createMock("CancelOrderAppManager", CancelOrderAppManager.class);
		
		ReflectionTestUtils.setField(cancelOrderAppController, "cancelOrderAppManager", cancelOrderAppManager);
		
		model = control.createMock("Model", Model.class);
		
	}

	@Test
	public void testOrderList() {
		
		
	}

	@Test
	public void testFindOrderListJson() {
		Page page = new Page(1,15);
		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
		
		Map<String,Object> searchParam = new HashMap<String,Object>();
		searchParam.put("orderCode","138534296847002469");
		//searchParam.put("memberName","lily");
		Pagination<CancelApplicationCommand> result=new Pagination<CancelApplicationCommand>();
		EasyMock.expect(cancelOrderAppManager.findCancelApplicationListByQueryMapWithPage(page, sorts, searchParam)).andReturn(result);
		control.replay();
		
		QueryBean queryBean = new QueryBean();
	    queryBean.setPage(page);
	    queryBean.setSorts(sorts);
	    queryBean.setParaMap(searchParam);
		
		assertEquals(result, cancelOrderAppController.findOrderListJson(model, queryBean, request, response));

	}

}
