package com.baozun.nebula.manager.salesorder;


import java.util.HashMap;
import java.util.List;
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

import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.model.salesorder.PayNo;
import com.baozun.nebula.sdk.command.PayNoCommand;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.command.OrderCommand;
import com.baozun.nebula.web.command.PtsSalesOrderCommand;

/**
 * 
 * @author qiang.yang
 * @createtime 2013-11-28 下午15:50:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class OrderManagerTest {

	private static final Logger log = LoggerFactory
			.getLogger(OrderManagerTest.class);

	@Autowired
	private SalesOrderManager salesOrderManager;
	
	
	private String testCode = "138778417645409786";

	@Test
	public void testFindOrderByCode() {
		OrderCommand orderCommand= salesOrderManager.findOrderByCode(testCode);
		log.info("---------------"+orderCommand.getSalesOrderCommand().getAddress());
		
	}
	
	
	@Test
	public void testfindPayNoList(){
		OrderCommand orderCommand= salesOrderManager.findOrderByCode(testCode);
		List<PayNoCommand> payNos=salesOrderManager.findPayNoList(orderCommand.getSalesOrderCommand().getId());
	    log.info("---------------"+payNos.size());
	}
	
	@Test
	public void testFindOrderListByQueryMapWithPage(){
		Page page = new Page(1,5);
		Sort[] sorts = Sort.parse("o.CREATE_TIME asc");
		Map<String, Object> searchParam = new HashMap<String, Object>();
		
		searchParam.put("sdkQueryType","1");
		Pagination<PtsSalesOrderCommand> orderList = salesOrderManager.findOrderListByQueryMapWithPage(page, sorts, searchParam);
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(orderList));
	}
	

}
