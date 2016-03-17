package com.baozun.nebula.manager.salesorder;

import static org.junit.Assert.*;

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

import com.baozun.nebula.web.command.CancelApplicationCommand;

/**
 * 
 * @author qiang.yang
 * @createtime 2013-11-28 PM 13:45
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CancelOrderAppManagerTest {
	private static final Logger log = LoggerFactory
			.getLogger(CancelOrderAppManagerTest.class);
	@Autowired
	private CancelOrderAppManager cancelOrderAppManager;

	@Test
	public void testFindCancelApplicationListByQueryMapWithPage() {
		Page page = new Page(1,10);
		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
		Map<String,Object> searchParam = new HashMap<String,Object>();
		//searchParam.put("orderCode","138536275495201666");
		searchParam.put("memberName","lily");
		Pagination<CancelApplicationCommand> pagination=
				cancelOrderAppManager.findCancelApplicationListByQueryMapWithPage(page, sorts, searchParam);
		List<CancelApplicationCommand>  applicationCommands=pagination.getItems();
		log.info("============"+applicationCommands.size());
		for(CancelApplicationCommand applicationCommand:applicationCommands){
			log.info("============"+applicationCommand.getHandleName());
			log.info("============"+applicationCommand.getOrderCode());
			log.info("============"+applicationCommand.getReason());
			log.info("============"+applicationCommand.getMessage());
			log.info("============"+applicationCommand.getMemberName());
			log.info("============"+applicationCommand.getMobile());
		}
	}

}
