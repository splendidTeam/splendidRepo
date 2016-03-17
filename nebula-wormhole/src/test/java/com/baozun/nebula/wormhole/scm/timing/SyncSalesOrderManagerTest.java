/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.wormhole.scm.timing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.system.MsgReceiveContentDao;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.utilities.common.encryptor.AESEncryptor;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.wormhole.constants.OrderStatusV5Constants;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * @author yfxie
 * 订单状态同步测试
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SyncSalesOrderManagerTest {
	
	@Autowired
	private SyncSalesOrderManager syncSalesOrderManager;
	
	@Autowired
	private MsgReceiveContentDao msgReceiveContentDao;
	
	@Test
	public void syncSoStatusTest(){
		createSomeData(IfIdentifyConstants.IDENTIFY_STATUS_SCM2SHOP_SYNC);
		//syncSalesOrderManager.syncSoStatus();
	}
	
	@Test
	public void getNotHandledSoOrderTest(){
		
	}
	
	/**
	 * 先造一点同步数据
	 */
	private void createSomeData(String inf){
		MsgReceiveContent msgContent = new MsgReceiveContent();
		String msgBody = createBodyMsg(inf);
		msgContent.setIfIdentify(inf);
		msgContent.setIsProccessed(false);
		msgContent.setMsgBody(msgBody);
		msgContent.setSendTime(new Date());
		msgContent.setMsgId(inf+"-02");
		msgContent.setVersion(new Date());
		msgReceiveContentDao.save(msgContent);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private String createBodyMsg(String identify){
		//创建一个mq body信息
		String bodyStr = "";
		List<OrderStatusV5> orderStatusV5List = new ArrayList<OrderStatusV5>();
		OrderStatusV5 osV5 = new OrderStatusV5();
		osV5.setBsOrderCode("139877007572306851");
		//设置aptime
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -50);
		osV5.setOpTime(calendar.getTime());
		osV5.setOpType(OrderStatusV5Constants.ORDER_CANCEL);
		orderStatusV5List.add(osV5);
		bodyStr = MsgUtils.listToJson(orderStatusV5List);
		return encyptInfo(bodyStr);
	}
	
	private String encyptInfo(String bodyStr){
		String aesString = "";
		try {
			aesString = new AESEncryptor().encrypt(bodyStr);
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
		return aesString;
	}

}
