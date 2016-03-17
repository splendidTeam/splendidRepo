package com.baozun.nebula.dao.salesorder;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * @author 阳羽
 * @createtime 2013-11-21 下午12:50:19
 */
@ContextConfiguration(locations = {
		"classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ApplyCancelOrderDaoTest {

	@Test
	public void testFindApplyCancelOrderList() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCancelOrders() {
		fail("Not yet implemented");
	}

}
