/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.wormhole.scm.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.model.system.WarningConfig;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.SdkWarningConfigManager;
import com.baozun.nebula.wormhole.mq.entity.logistics.LogisticTrackingV5;

/**
 * WarningPartManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月16日
 **/
@Transactional
@Service("warningPartManager")
public class WarningPartManagerImpl implements WarningPartManager {
    
    @Autowired
    private EventPublisher eventPublisher;

    static Properties emailProperties=null;
    @Autowired
	private SdkWarningConfigManager sdkWarningConfigManager;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
    
    @Override
    public void warnPayOrderNoChangStatus(SalesOrderCommand salesOrderCommand,String uniqueContent) {
//        if(emailProperties==null)
//            emailProperties=ProfileConfigUtil.findPro("config/email_receiver.properties");
//        
//        String value=emailProperties.getProperty("warning.email.receiver");
//        String[] strs=value.split(",");
//        
//        for(String email:strs){
//            
//            if(StringUtils.isBlank(email)) continue;
//            Map<String,Object> map=new HashMap<String,Object>();
//            map.put("desc", "发现24小时已经付款，订单状态仍然为新建的订单<br/>订单Id:"+salesOrderCommand.getId()+"<br/>订单号:"+salesOrderCommand.getCode());
//            EmailEvent ev=new EmailEvent(this,email,"EMAIL_WARNING",map);
//            eventPublisher.publish(ev);
//        }
    	WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.ORDER_NO_CHANGE_WARNING_CODE);
		String emailCode = null;
		String desc = null;
		String code = salesOrderCommand.getCode();
		if (wc != null) {
			desc = wc.getWarningDesc() + "<br/>订单Id:" + salesOrderCommand.getId()+ "<br/>订单号:" + code;
			emailCode = WarningConfig.ORDER_NO_CHANGE_WARNING_CODE;
		} else {
			emailCode = "EMAIL_WARNING";
			desc = "发现24小时已经付款，订单状态仍然为新建的订单<br/>订单Id:"
					+ salesOrderCommand.getId() + "<br/>订单号:"
					+ code;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("desc", desc);
		emailTemplateManager.sendWarningEmail(emailCode, uniqueContent+"-"+code, dataMap);
    }

    @Override
    public void warnNotCancelOrderNoFinish(SalesOrderCommand salesOrderCommand,String uniqueContent) {
//        if(emailProperties==null)
//            emailProperties=ProfileConfigUtil.findPro("config/email_receiver.properties");
//        
//        String value=emailProperties.getProperty("warning.email.receiver");
//        String[] strs=value.split(",");
//        
//        for(String email:strs){
//            
//            if(StringUtils.isBlank(email)) continue;
//            Map<String,Object> map=new HashMap<String,Object>();
//            map.put("desc", "发现非取消状态的订单10天还没有变更为交易完成状态<br/>订单Id:"+salesOrderCommand.getId()+"<br/>订单号:"+salesOrderCommand.getCode());
//            EmailEvent ev=new EmailEvent(this,email,"EMAIL_WARNING",map);
//            eventPublisher.publish(ev);
//        }
    	WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.NOT_CANCEL_ORDER_NO_FINISH_WARNING_CODE);
		String emailCode = null;
		String desc = null;
		String code =  salesOrderCommand.getCode();
		if (wc != null) {
			desc = wc.getWarningDesc() + "<br/>订单Id:" + salesOrderCommand.getId()+ "<br/>订单号:" + code;
			emailCode = WarningConfig.NOT_CANCEL_ORDER_NO_FINISH_WARNING_CODE;
		} else {
			emailCode = "EMAIL_WARNING";
			desc = "发现非取消状态的订单10天还没有变更为交易完成状态<br/>订单Id:"
					+ salesOrderCommand.getId() + "<br/>订单号:"
					+ code;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("desc", desc);
		emailTemplateManager.sendWarningEmail(emailCode, uniqueContent+"-"+code, dataMap);
    }

    @Override
    public void warnFullInventorySyncLater(LogisticsCommand command,String uniqueContent) {
//        if(emailProperties==null)
//            emailProperties=ProfileConfigUtil.findPro("config/email_receiver.properties");
//        
//        String value=emailProperties.getProperty("warning.email.receiver");
//        String[] strs=value.split(",");
//        
//        for(String email:strs){
//            
//            if(StringUtils.isBlank(email)) continue;
//            Map<String,Object> map=new HashMap<String,Object>();
//            map.put("desc", "发现48小时内未全量库存同步的物流同步信息<br/>订单ID:"+command.getOrderId()+"<br/>物流信息Id:"+command.getId());
//            EmailEvent ev=new EmailEvent(this,email,"EMAIL_WARNING",map);
//            eventPublisher.publish(ev);
//        }
        WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.FULL_INVENTORY_SYNC_LATER_WARNING_CODE);
		String emailCode = null;
		String desc = null;
		Long id = command.getId();
		if (wc != null) {
			desc = wc.getWarningDesc() + "<br/>订单Id:" +command.getOrderId()+"<br/>物流信息Id:"+id;
			emailCode = WarningConfig.FULL_INVENTORY_SYNC_LATER_WARNING_CODE;
		} else {
			emailCode = "EMAIL_WARNING";
			desc = "发现48小时内未全量库存同步的物流同步信息<br/>订单ID:"+command.getOrderId()+"<br/>物流信息Id:"+id;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("desc", desc);
		emailTemplateManager.sendWarningEmail(emailCode, uniqueContent+"-"+id, dataMap);
    }
    
    @Override
    public void warnSyncLogisticErrorByNotCode(LogisticTrackingV5 command,MsgReceiveContent msgReceiveContent,String uniqueContent) {
//        if(emailProperties==null)
//            emailProperties=ProfileConfigUtil.findPro("config/email_receiver.properties");
//        
//        String value=emailProperties.getProperty("warning.email.receiver");
//        String[] strs=value.split(",");
//        
//        for(String email:strs){
//            
//            if(StringUtils.isBlank(email)) continue;
//            Map<String,Object> map=new HashMap<String,Object>();
//            map.put("desc", "订单CODE不存在,无法处理同步物流信息<br/>订单CODE:"+command.getBsOrderCode()+"<br/>消息Id:"+msgReceiveContent.getId());
//            EmailEvent ev=new EmailEvent(this,email,"EMAIL_WARNING",map);
//            eventPublisher.publish(ev);
//        }
        WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.SYNC_LOGISTIC_ERROR_NOT_CODE_WARNING_CODE);
		String emailCode = null;
		String desc = null;
		Long msgId = msgReceiveContent.getId();
		if (wc != null) {
			desc = wc.getWarningDesc() + "<br/>订单CODE:" +command.getBsOrderCode()+"<br/>消息Id:"+msgId;
			emailCode = WarningConfig.SYNC_LOGISTIC_ERROR_NOT_CODE_WARNING_CODE;
		} else {
			emailCode = "EMAIL_WARNING";
			desc = "订单CODE不存在,无法处理同步物流信息<br/>订单CODE:"+command.getBsOrderCode()+"<br/>消息Id:"+msgId;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("desc", desc);
		emailTemplateManager.sendWarningEmail(emailCode, uniqueContent+"-"+msgId, dataMap);
    }

}

