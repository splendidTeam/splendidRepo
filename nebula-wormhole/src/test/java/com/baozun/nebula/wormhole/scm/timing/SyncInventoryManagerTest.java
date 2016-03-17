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
import com.baozun.nebula.wormhole.mq.entity.SkuInventoryV5;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * @author yfxie
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SyncInventoryManagerTest {
	
	@Autowired
	private SyncInventoryManager syncInventoryManager;
	
	@Autowired
	private MsgReceiveContentDao msgReceiveContentDao;
	
	@Test
	public void syncIncrementInventoryTest(){
		//createSomeData(IfIdentifyConstants.IDENTIFY_INVENTORY_ADD);
		//syncInventoryManager.syncIncrementInventory();
	}
	
	@Test
	public void syncFullInventoryTest(){
		//createSomeData(IfIdentifyConstants.IDENTIFY_INVENTORY_ALL);
		syncInventoryManager.syncFullInventory();
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
		msgContent.setMsgId("msgId03");
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
		List<SkuInventoryV5> skuInvList = new ArrayList<SkuInventoryV5>();
		SkuInventoryV5 skuInvV5 = new SkuInventoryV5();
		skuInvV5.setExtentionCode("HX313000");
		//设置aptime
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -50);
		skuInvV5.setOpTime(calendar.getTime());
		skuInvV5.setQty(9944L);
		if(IfIdentifyConstants.IDENTIFY_INVENTORY_ADD.equals(identify)){
			//增量
			skuInvV5.setType(2);
			skuInvV5.setDirection(2);
		} else if(IfIdentifyConstants.IDENTIFY_INVENTORY_ALL.equals(identify)){
			//全量
			skuInvV5.setType(1);
			skuInvV5.setDirection(1);
		}
		skuInvList.add(skuInvV5);
		bodyStr = MsgUtils.listToJson(skuInvList);
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
