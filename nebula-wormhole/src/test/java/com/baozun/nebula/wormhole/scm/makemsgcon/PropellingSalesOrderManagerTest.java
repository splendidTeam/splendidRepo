package com.baozun.nebula.wormhole.scm.makemsgcon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.system.MsgSendRecord;

/** 
 * @author xingyu.liu  
 * @version  
 * 类说明 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PropellingSalesOrderManagerTest {

	@Autowired
	private PropellingSalesOrderManager propellingSalesOrder;
	
	@Test
	public void testPropellingSalesOrder() {
		MsgSendRecord msgSendRecord = new  MsgSendRecord();
		msgSendRecord.setTargetId(573L);
		propellingSalesOrder.propellingSalesOrder(msgSendRecord);
	}

	@Test
	public void testPropellingPayment() {
		MsgSendRecord msgSendRecord = new  MsgSendRecord();
		msgSendRecord.setTargetId(573L);
		propellingSalesOrder.propellingPayment(msgSendRecord);
	}

	@Test
	public void testPropellingSoStatus() {
		MsgSendRecord msgSendRecord = new  MsgSendRecord();
		msgSendRecord.setTargetId(469L);
		propellingSalesOrder.propellingSoStatus(msgSendRecord);
	}

}
