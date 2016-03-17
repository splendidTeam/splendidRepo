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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.wormhole.mq.entity.ProductPriceV5;
import com.baozun.nebula.wormhole.mq.entity.sku.SkuInfoV5;
import com.baozun.nebula.wormhole.scm.handler.SyncItemHandler;
import com.baozun.nebula.wormhole.scm.manager.ItemManager;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * 
 * 定时取出MsgReceiveContent表商品信息接口相关未处理的数据 进行处理 实现类
 * 
 * @author chenguang.zhou
 * @date 2014年5月9日 下午2:12:13
 */
@Service("syncItemManager")
public class SyncItemManagerImpl implements SyncItemManager {

	private static Logger		log	= LoggerFactory.getLogger(SyncItemManagerImpl.class);
	
	@Autowired(required = false)
	private SyncItemHandler		syncItemHandler;

	@Autowired
	private SdkMsgManager		sdkMsgManager;

	@Autowired
	private ItemManager			scmItemManager;

	@Autowired
	private SyncCommonManager	syncCommonManager;

	@Autowired
	private SdkItemManager		sdkItemManager;

	@Autowired
	private SdkSkuManager		sdkSkuManager;

	@Override
	public void syncBaseInfo() {
		try {
			List<MsgReceiveContent> msgReceiveList = getMsgBody(IfIdentifyConstants.IDENTIFY_ITEM_SYNC);
			log.debug("product base info interface synchronization data is {}", MsgUtils.listToJson(msgReceiveList));
			// 处理数据, 获取msgBody
			List<Long> msgReceiveIds = new ArrayList<Long>();
			for (MsgReceiveContent msgReceive : msgReceiveList) {
				msgReceiveIds.add(msgReceive.getId());
				// aes 解密msgBody内容
				String msgBody = msgReceive.getMsgBody();
				List<SkuInfoV5> skuV5List = syncCommonManager.queryMsgBody(msgBody, SkuInfoV5.class);
				log.debug("product base info interface msgBody data is ", MsgUtils.listToJson(skuV5List));
				try {
					scmItemManager.syncBaseInfo(skuV5List, msgReceive);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	} 

	@Override
	public void syncItemPrice() {
		try {
			List<MsgReceiveContent> msgReceiveList = getMsgBody(IfIdentifyConstants.IDENTIFY_ITEM_PRICE_SYNC);
			log.debug("product price interface synchronization data is {}", MsgUtils.listToJson(msgReceiveList));
			// 处理数据, 获取msgBody
			for (MsgReceiveContent msgReceive : msgReceiveList) {
				// aes 解密msgBody内容
				String msgBody = msgReceive.getMsgBody();
				List<ProductPriceV5> pdpriceV5List = syncCommonManager.queryMsgBody(msgBody, ProductPriceV5.class);
				log.debug("product price interface msgBody data is {}.", MsgUtils.listToJson(pdpriceV5List));
				try {
					scmItemManager.syncItemPrice(pdpriceV5List, msgReceive);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("product price interface synchronization failure.");
			e.printStackTrace();
		}
	}




	/**
	 * 获取scm同步,且未处理的数据
	 * 
	 * @param ifIdentify
	 * @return
	 */
	private List<MsgReceiveContent> getMsgBody(String ifIdentify) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ifIdentify", ifIdentify);
		paraMap.put("isProccessed", false);
		List<MsgReceiveContent> msgReceiveList = sdkMsgManager.findMsgReceiveContentListByQueryMap(paraMap);
		return msgReceiveList;
	}
}
