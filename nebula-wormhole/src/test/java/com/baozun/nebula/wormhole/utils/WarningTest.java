package com.baozun.nebula.wormhole.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.wormhole.scm.timing.WarningManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class WarningTest {

	@Autowired
	private WarningManager warningManager;
	
	@Test
	public void testpayOrderNoChangStatus(){
		try {
			warningManager.payOrderNoChangStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testnotCancelOrderNoFinish(){
		try {
			warningManager.notCancelOrderNoFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testfullInventorySyncLater(){
		try {
			warningManager.fullInventorySyncLater();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
