package com.baozun.nebula.dao.baseinfo;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.model.baseinfo.PageItem;

@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback=false)
public class PageItemDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private PageItemDao pageItem;
	
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(PageItemDaoTest.class);

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ItemDao#findItemById(java.lang.Long)}.
	 */
	@Test
	public void testFindPageItemById(){
		PageItem item = pageItem.findPageItemById(1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}",item.getCode());
	}
	
	@Test
	public void testFindPageItemByPageTemplateId(){
		List<PageItem> items = pageItem.findPageItemListByPageTemplateId(1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@{}+++++++++++++++++++++++",items.size());
	}
}
