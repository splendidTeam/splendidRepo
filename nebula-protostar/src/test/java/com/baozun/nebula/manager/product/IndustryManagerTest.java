/**
 * 
 */
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.product.Industry;

/**
 * @author kefan.chen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class IndustryManagerTest {
	Logger	logger	= LoggerFactory.getLogger(IndustryManagerTest.class);
	
	
	@Autowired
	private IndustryManager	industryManager;

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.IndustryManagerImpl#findIndustryList()}.
	 */
	@Test
	public void testFindAllIndustryList() {
		
		List<Industry> list =industryManager.findAllIndustryList();
		
		for (Industry l : list){
			logger.info("Industry id is" + l.getId() + "Industry name is" + l.getName());
		}
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.IndustryManagerImpl#addIndustry(java.lang.Long, java.lang.Integer, java.lang.String)}.
	 */
	@Test
	public void testcreateOrUpdateIndustry() {
			Industry industry = new Industry();
			industry.setLifecycle(1);
			industry.setName("测试");
		  industryManager.createOrUpdateIndustry(industry, "-1");
	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.IndustryManagerImpl#removeIndustry(java.lang.Long)}.
	 */
	@Test
	public void testRemoveIndustry() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(5L);
		industryManager.removeIndustryByIds(ids);
	}
}
