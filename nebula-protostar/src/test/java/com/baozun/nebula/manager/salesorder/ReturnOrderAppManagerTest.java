package com.baozun.nebula.manager.salesorder;


import java.util.HashMap;
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

import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ReturnOrderAppManagerTest {
	
	private static final Logger log = LoggerFactory.getLogger(ReturnOrderAppManagerTest.class);

	@Autowired
	private ReturnOrderAppManager returnOrderAppManager;
	
	@Test
	public void testFindReturnApplicationListByQueryMapWithPage() {
		Page page = new Page(1,10);
		Sort[] sorts = Sort.parse("sro.CREATE_TIME asc");
		Map<String,Object> searchParam = new HashMap<String,Object>();
		searchParam.put("sdkQueryType","1");
		
		Pagination<PtsReturnOrderCommand> returnOrderList =returnOrderAppManager.findReturnApplicationListByQueryMapWithPage(page, sorts, searchParam);
		
		log.info("@@@@@@@@@@@@++++++++++++++++:::{},{}", JsonFormatUtil.format(returnOrderList));
	}

}
