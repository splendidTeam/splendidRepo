package com.baozun.nebula.dao.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.model.baseinfo.PageTemplate;

@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class PageTemplateDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private PageTemplateDao pageTemplate;
	
	
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(PageTemplateDaoTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ItemDao#findItemById(java.lang.Long)}.
	 */
	@Test
	public void testFindPageTemplateById(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		pageTemplate.removePageTemplateByIds(ids);
//		pageTemplate.enableOrDisablePageTemplateByIds(ids, 1);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}","++++++++++++++++++++++++++++sucess");
	}
	
	@Test
	public void testCreateOrUpdatePageTemplate() {
		PageTemplate con = new PageTemplate();
//		con.setId(4L);
		con.setPageCode("sfd");
		con.setPageName("sgggg");
		try {
			pageTemplate.save(con);
			
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",
					"++++++++++++++++++++++++++++sucess");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEnablePageTemplateById(){
		pageTemplate.enableOrDisablePageTemplateById(1L, 0);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}","++++++++++++++++++++++++++++sucess");
	}
	
}
