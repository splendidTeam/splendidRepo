/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.wormhole.scm.timing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.system.MsgReceiveContentDao;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.wormhole.mq.entity.sku.OnSaleSkuV5;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * 商品同步接口, 测试类
 * 
 * @author chenguang.zhou
 * @date 2014年5月13日 下午2:45:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SyncItemManagerTest {
	private final static Logger		log	= LoggerFactory.getLogger(SyncItemManagerTest.class);

	@Autowired
	private SyncItemManager			syncItemManager;

	@Autowired
	private MsgReceiveContentDao	MsgReceiveContentDao;

	@Autowired
	private SyncCommonManager		syncCommonManager;

	@Autowired
	private SdkMsgManager			sdkMsgManager;

	@Test
	public void testSyncBaseInfo() {
		syncItemManager.syncBaseInfo();
		log.info("item sync successfully!");
	}

	@Test
	public void testSyncItemPrice() {
		syncItemManager.syncItemPrice();
		log.info("item price sync successfully!");
	}

	@Test
	public void getDate() throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_ITEM_PRICE_SYNC);
		paraMap.put("msgId", "9601");
		List<MsgReceiveContent> contentList = MsgReceiveContentDao.findMsgReceiveContentListByQueryMap(paraMap);
		if(contentList != null && contentList.size() > 0){
			log.info(contentList.get(0).getMsgBody());
			String centent = EncryptUtil.getInstance().decrypt(contentList.get(0).getMsgBody());
			log.info("---->  "+centent);
			List<OnSaleSkuV5> onList = MsgUtils.jsonToList(centent, OnSaleSkuV5.class);
			log.info("-->>" + centent +" -->> "+onList.size());
		}
			
	}

}
