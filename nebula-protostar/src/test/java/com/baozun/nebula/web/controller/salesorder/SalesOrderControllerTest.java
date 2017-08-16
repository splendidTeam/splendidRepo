package com.baozun.nebula.web.controller.salesorder;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.OrderCommand;
import com.baozun.nebula.web.command.PtsSalesOrderCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })

@ActiveProfiles("dev")
public class SalesOrderControllerTest {
	private IMocksControl		control;
	private Model				model;
	private HttpServletRequest	request;

	private HttpServletResponse	response;
	private SalesOrderController salesOrderController;
	private SalesOrderManager salesOrderManager;
	
	@Before
	public void init(){
		salesOrderController=new SalesOrderController();
		control = createNiceControl();
		request = control.createMock("HttpServletRequest", HttpServletRequest.class);
		response = control.createMock("HttpServletResponse", HttpServletResponse.class);
		
		salesOrderManager=control.createMock("SalesOrderManager", SalesOrderManager.class);
		
		ReflectionTestUtils.setField(salesOrderController, "salesOrderManager", salesOrderManager);
		
		model = control.createMock("Model", Model.class);
		
	}
	
	@Test
	public void testOrderDetail(){
		String orderCode="";
		OrderCommand orderCommand=new OrderCommand();
		EasyMock.expect(salesOrderManager.findOrderByCode(orderCode)).andReturn(orderCommand);
		control.replay();
		assertEquals(orderCommand, salesOrderManager.findOrderByCode(orderCode));
	}

	@Test
	public void testOrderList() {
		fail("Not yet implemented");
	}

	@Test
	public void testOrderListJson() {
		Page page = new Page(1,5);
		Sort[] sorts = Sort.parse("o.CREATE_TIME asc");
		Map<String, Object> searchParam = new HashMap<String, Object>();
		
		searchParam.put("sdkQueryType","1");
		Pagination<PtsSalesOrderCommand> orderList = new Pagination<PtsSalesOrderCommand>();
		
		EasyMock.expect(salesOrderManager
				.findOrderListByQueryMapWithPage(page,
						sorts, searchParam)).andReturn(orderList);
		
		control.replay();
		
		QueryBean queryBean = new QueryBean();
		
		queryBean.setPage(page);
		queryBean.setSorts(sorts);
		queryBean.setParaMap(searchParam);
		
		assertEquals(orderList,salesOrderController.orderListJson(model, queryBean, request, response));
	}

	@Test
	public void testGetLogistics() {
		Long orderId=40L;		
		LogisticsCommand logistics=new LogisticsCommand();
		EasyMock.expect(salesOrderManager.findLogisticsByOrderId(orderId)).andReturn(logistics);
		control.replay();
		assertEquals(logistics, salesOrderManager.findLogisticsByOrderId(orderId));
	}


	@Test
	public void testCreateOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveOrder() {
		fail("Not yet implemented");
	}

}
