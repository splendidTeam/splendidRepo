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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.wormhole.mq.entity.SkuInventoryV5;
import com.baozun.nebula.wormhole.scm.manager.InventoryManager;

/**
 * @author yfxie
 *
 */
@Service("syncInventoryManager")
public class SyncInventoryManagerImpl implements SyncInventoryManager {
	
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Autowired
	private SyncCommonManager syncCommonManager;
	
	@Autowired
	private InventoryManager inventoryManager;
	
	private Logger logger = LoggerFactory.getLogger(SyncInventoryManagerImpl.class);

	@Override
	public void syncFullInventory() {
		/**1:接收库存同步记录*/
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_INVENTORY_ALL);
		paraMap.put("isProccessed", false);
		List<MsgReceiveContent> msgContentList = sdkMsgManager.findMsgReceiveContentListByQueryMap(paraMap);
		Map<String,List<Long>> msgContentIdListMap = new HashMap<String,List<Long>>();
		//List<Long> msgIdList = new ArrayList<Long>();
		/**2:判断同步时间和基线时间*/
		if(Validator.isNotNullOrEmpty(msgContentList)){
			for(MsgReceiveContent receiveContent : msgContentList){
				//msgIdList.add(receiveContent.getId());
				List<Long> msgIdList = null;
				if(msgContentIdListMap.containsKey(receiveContent.getMsgId())){
					msgIdList = msgContentIdListMap.get(receiveContent.getMsgId());
				} else {
					msgIdList = new ArrayList<Long>();
				}
				msgIdList.add(receiveContent.getId());
				msgContentIdListMap.put(receiveContent.getMsgId(), msgIdList);
			}
			Map<String,List<SkuInventoryV5>> map = getSyncSkusMap(msgContentList);
			if(Validator.isNotNullOrEmpty(map)){
				for(java.util.Map.Entry<String, List<SkuInventoryV5>> msgContent : map.entrySet()){
					try {
						/**3:处理同步逻辑 更新基线时间 此处逐条消息进行处理*/
						/**4:设置MsgReceiveContent库存同步记录标志*/
						inventoryManager.syncFullInventory(msgContent.getValue(),msgContentIdListMap.get(msgContent.getKey()));
					} catch(Exception e) {
						e.printStackTrace();
						logger.error("全量库存同步异常,消息id："+msgContent.getKey());
					}
				}
			}
			//sdkMsgManager.updateMsgRecContIsProByIds(msgIdList);
		} else {
			logger.info("无全量库存同步记录");
		}
		
	}

	@Override
	public void syncIncrementInventory() {
		/**1:接收库存同步记录*/
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_INVENTORY_ADD);
		paraMap.put("isProccessed", false);
		List<MsgReceiveContent> msgContentList = sdkMsgManager.findMsgReceiveContentListByQueryMap(paraMap);
		//List<Long> msgIdList = new ArrayList<Long>();
		Map<String,List<Long>> msgContentIdListMap = new HashMap<String,List<Long>>();
		//增量同步之前 先更新掉全量更新没有处理的记录
		syncFullInventory();
		//全量更新end
		/**2:判断同步时间和基线时间*/
		if(Validator.isNotNullOrEmpty(msgContentList)){
			for(MsgReceiveContent receiveContent : msgContentList){
				//msgIdList.add(receiveContent.getId());
				List<Long> msgIdList = null;
				if(msgContentIdListMap.containsKey(receiveContent.getMsgId())){
					msgIdList = msgContentIdListMap.get(receiveContent.getMsgId());
				} else {
					msgIdList = new ArrayList<Long>();
				}
				msgIdList.add(receiveContent.getId());
				msgContentIdListMap.put(receiveContent.getMsgId(), msgIdList);
			}
			Map<String,List<SkuInventoryV5>> map = getSyncSkusMap(msgContentList);
			if(Validator.isNotNullOrEmpty(map)){
				for(java.util.Map.Entry<String, List<SkuInventoryV5>> msgContent : map.entrySet()){
					try {
						/**3:处理同步逻辑 更新基线时间 此处逐条消息进行处理*/
						/**4:设置MsgReceiveContent库存同步记录标志*/
						inventoryManager.syncIncrementInventory(msgContent.getValue(),msgContentIdListMap.get(msgContent.getKey()));
					} catch(Exception e) {
						e.printStackTrace();
						logger.error("增量库存同步异常,消息id："+msgContentIdListMap.get(msgContent.getKey()).get(0));
					}
				}
			}
		} else {
			logger.info("无增量库存同步记录");
		}
	}
	
	/**
	 * 根据类型获取sku更新记录
	 * 将记录的处理状态改成：处理中
	 * 
	 * @return
	 */
	private Map<String,List<SkuInventoryV5>> getSyncSkusMap(List<MsgReceiveContent> msgContentList){
		Map<String,List<SkuInventoryV5>> skuInvListMap = new HashMap<String,List<SkuInventoryV5>>();
		if(!Validator.isNotNullOrEmpty(msgContentList)){
			return skuInvListMap; 
		}
		for(MsgReceiveContent msgContent : msgContentList){
			String msgBody = msgContent.getMsgBody();
			/**解析sku记录*/
			List<SkuInventoryV5> skuInvSpList = syncCommonManager.queryMsgBody(msgBody,SkuInventoryV5.class);
			skuInvListMap.put(msgContent.getMsgId(), skuInvSpList);
		}
		return skuInvListMap;
	}
	
	/**
	 * 将List<Map> 转化为对象 List<OrderStatusV5>
 	 */
//	@SuppressWarnings("unchecked")
//	private List<SkuInventoryV5> transToClass(List<SkuInventoryV5> orderStatusList){
//		List<SkuInventoryV5> list = new ArrayList<SkuInventoryV5>();
//		if(Validator.isNotNullOrEmpty(orderStatusList)) {
//			for(Object obj : orderStatusList){
//				Map<String,Object> o = JsonUtil.toBean(JsonUtil.toJSON(obj), Map.class);
//				SkuInventoryV5 v5 = new SkuInventoryV5();
//				v5.setDirection((Integer)o.get("direction"));
//				v5.setExtentionCode((String)o.get("extentionCode"));
//				v5.setOpTime(new Date((Long)o.get("opTime")));
//				v5.setQty(Long.parseLong(o.get("qty")+""));
//				list.add(v5);
//			}
//		}
//		return list;
//	}
	

}
