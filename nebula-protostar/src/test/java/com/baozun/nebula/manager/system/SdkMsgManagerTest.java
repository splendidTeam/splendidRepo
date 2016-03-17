package com.baozun.nebula.manager.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.sdk.manager.SdkMsgManager;


/**
 * test
 * @author xingyu.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")  
public class SdkMsgManagerTest{
	Logger	logger	= LoggerFactory.getLogger(SdkMsgManagerTest.class);
	
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Test
	public void testQueryNoFeedbackList(){
	
		sdkMsgManager.findAllNoFeedbackMsgSendRecordList();
		
	}
	
	

}
