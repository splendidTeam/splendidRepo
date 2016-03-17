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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class LimitaryDaoTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static final Logger log = LoggerFactory
			.getLogger(LimitaryDaoTest.class);

//	@Autowired
//	private LimitaryImprovedDao limitaryImprovedDao;
//	
//	
//	@Autowired
//	private LimitaryTriggerPointDao limitaryTriggerPointDao;
	
	
	 /**
		 * 获取所有限购规则
		 * 
		 * @param loginName
		 * @return
		 
		  @Test
	 public void findAllLimitCondition() {
			 List list= limitaryImprovedDao.findAllLimitCondition();
			 log.info("findlimitaryImprovedAll  获取所有限购规则方法返回{}", list==null?"null":list.size());
	}
		*/  
		  
		  /**
			 * 获取所有触点对应的限购规则
			 * 
			 * @param loginName
			 * @return
			
			  @Test
		 public void findAll() {
				 List list= limitaryTriggerPointDao.findAll();
				 log.info("findAll  获取所有触点对应的限购规则返回{}", list==null?"null":list.size());
		}
		 */	  
			  
			  /**
				 * 获取制定限购规则
				 * 
				 * @param loginName
				 * @return
				 */
//				  @Test
//			 public void findAllLimitConditionById() {
//					  LimitaryImprovedEntity limitaryImprovedEntity = limitaryImprovedDao.findAllLimitConditionById(1L);
//					 log.info("findAllLimitConditionById  获取制定限购规则{}", limitaryImprovedEntity==null?"null":limitaryImprovedEntity.getDescription());
//			}

}
