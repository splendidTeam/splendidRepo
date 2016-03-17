package com.baozun.nebula.dao.member;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.MemberPersonalDataCommand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:loxia-hibernate-context.xml",
			"classpath*:loxia-service-context.xml",
			"classpath*:spring.xml"})
@ActiveProfiles("dev")
public class MemberPersonalDataDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {
        private static final Logger log = LoggerFactory.getLogger(MemberPersonalDataDaoTest.class);
	
	@Autowired
	private MemberPersonalDataDao memberPersonalDataDao;
	
	@Test
	public void testfindById() {
		MemberPersonalDataCommand mpdc= memberPersonalDataDao.findById(6L);
		log.info("findById  方法返回{}", mpdc==null?"null":mpdc.getLoginName());
	}
	

}



