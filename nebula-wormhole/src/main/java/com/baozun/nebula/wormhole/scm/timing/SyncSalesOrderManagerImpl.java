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
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.scm.manager.SalesOrderManager;

/**
 * @author yfxie
 * 
 * 同步oms推送过来的订单状态
 *
 */
@Service("syncSalesOrderManager")
public class SyncSalesOrderManagerImpl implements SyncSalesOrderManager {
	
	private Logger logger = LoggerFactory.getLogger(SyncSalesOrderManagerImpl.class);
	
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Autowired
	private SyncCommonManager syncCommonManager;
	
	@Autowired
	private SalesOrderManager salesOrderManager;
	
	@Override
	public void syncSoStatus() {
		/**1:获取没有处理的订单信息*/
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_STATUS_SCM2SHOP_SYNC);
		paraMap.put("isProccessed", false);
		/**获取相应记录*/
		List<MsgReceiveContent> msgContentList = sdkMsgManager.findMsgReceiveContentListByQueryMap(paraMap);
		Map<String,MsgReceiveContent> msgReceiverMap = new HashMap<String,MsgReceiveContent>();
		Map<String,List<Long>> msgIdMap = new HashMap<String,List<Long>>();
		if(Validator.isNotNullOrEmpty(msgContentList)){
			for(MsgReceiveContent content : msgContentList){
				List<Long> msgIdList = null;
				if(msgIdMap.containsKey(content.getMsgId())){
					msgIdList = msgIdMap.get(content.getMsgId());
				} else {
					msgIdList = new ArrayList<Long>();
				}
				msgIdList.add(content.getId());
				msgIdMap.put(content.getMsgId(), msgIdList);
				//此处用于去重
				msgReceiverMap.put(content.getMsgId(), content);
			}
		}
		if(Validator.isNotNullOrEmpty(msgContentList)){
			for(Map.Entry<String, MsgReceiveContent> msgMap : msgReceiverMap.entrySet()){
				try {
					/**2:逐个处理请求信息*/
					/**3:更新处理逻辑*/
					salesOrderManager.syncSoStatus(msgMap.getValue(), msgIdMap.get(msgMap.getKey()));
				} catch(Exception e){
					logger.error("订单状态同步信息："+msgMap.getValue().getId()+"异常！");
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public List<OrderStatusV5> getNotHandledSoOrder() {
		List<OrderStatusV5> orderStatusV5List = new ArrayList<OrderStatusV5>();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_STATUS_SCM2SHOP_SYNC);
		paraMap.put("isProccessed", false);
		/**获取相应记录*/
		List<MsgReceiveContent> msgContentList = sdkMsgManager.findMsgReceiveContentListByQueryMap(paraMap);
		if(Validator.isNotNullOrEmpty(msgContentList)){
			for(MsgReceiveContent content : msgContentList){
				List<OrderStatusV5> orderSV5List = syncCommonManager.queryMsgBody(content.getMsgBody(),OrderStatusV5.class);
				if(Validator.isNotNullOrEmpty(orderSV5List)){
					orderStatusV5List.addAll(orderSV5List);
				}
			}
		}
		return orderStatusV5List;
	}
	
}
