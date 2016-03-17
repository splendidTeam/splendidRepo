package com.baozun.nebula.engine.limit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.solr.manager.SolrManager;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SolrTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static final Logger log = LoggerFactory
			.getLogger(SolrTest.class);

	@Autowired
    protected SolrManager solrManager;
	
	
	
	/**
	 * 测试SolrManagerImpl
	 */
	@Test
	public void managerImplTest(){
		
//		solrManager.findAllItemCommandFormSolrByField("code:abc", "code desc", 0, 1, 0, 6);
		
		
/*		QueryConditionCommand queryConditionCommand = new QueryConditionCommand();
		
//		queryConditionCommand.setItemId(2L);
		List<String> sts = new ArrayList<String>();
		sts.add("code");
		queryConditionCommand.setGroupNames(sts);*/
		
//		solrManager.findAllItemCommandFormSolrBySolrQuery(queryConditionCommand, 0, 1, 0, 6);
//		List<String> channels = new ArrayList<String>();
//		channels.add("1");
//		
//		solrManager.createOrUpdateItemCommandToSolr(2844L, new Date(), new Date(), channels);
		

		
	}

}
