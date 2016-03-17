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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;

/**
 * 在售商品接口, 测试类
 * 
 * @author chenguang.zhou
 * @date 2014年5月13日 下午2:07:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", 
		"classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PropellingItemManagerTest {

	private final static Logger		log	= LoggerFactory.getLogger(PropellingItemManagerTest.class);

	@Autowired
	private PropellingItemManager	propellingItemManager;

	@Autowired
	private SdkMsgManager			sdkMsgManager;

	@Test
	public void testPropellingOnSalesItems() {
		MsgSendRecord msr = new MsgSendRecord();
		msr.setId(379L);
		msr.setIfIdentify(IfIdentifyConstants.IDENTIFY_ITEM_ONSALE_SYNC);
		msr.setExt("1");
		MsgSendContent content = propellingItemManager.propellingOnSalesItems(msr);
		log.info("nebula on sale product hava {}", content);
	}

}
