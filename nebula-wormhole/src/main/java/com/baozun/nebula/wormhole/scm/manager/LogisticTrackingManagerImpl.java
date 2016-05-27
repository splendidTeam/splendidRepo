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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import loxia.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.wormhole.mq.entity.logistics.LogisticDetailV5;
import com.baozun.nebula.wormhole.mq.entity.logistics.LogisticTrackingV5;
import com.baozun.nebula.wormhole.scm.timing.SyncCommonManager;

/**
 * LogisticTrackingManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月16日
 **/
@Transactional
@Service("logisticTrackingManager")
public class LogisticTrackingManagerImpl implements LogisticTrackingManager {
	
	/** 配置文件配置的物流信息的同步类型的Key */
	private static final String LOGISTIC_TYPE_KEY = "logistic_type";
	
	/** 物流信息的同步类型，全量:full. 已和OMS确实这是默认的方式，如果要改为增量，可在 config/common.properties中配置：logistic_type=increment*/
	private static final String LOGISTIC_TYPE_FULL = "full";
	
	/** 物流信息的同步类型，增量:increment*/
	private static final String LOGISTIC_TYPE_INCREMENT = "increment";

    @Autowired
    private SdkMsgManager sdkMsgManager;
    
    @Autowired
    private SyncCommonManager syncCommonManager;
    
    @Autowired
    private LogisticsManager logisticsManager;
    
    @Autowired
    private OrderManager sdkOrderService;
    
    @Autowired
    private WarningPartManager warningPartManager;
    
    @Autowired
	private EmailTemplateManager emailTemplateManager;
    
    private final Logger logger = LoggerFactory.getLogger(LogisticTrackingManagerImpl.class);
    @Override
    public void syncLogistic(MsgReceiveContent msgReceiveContent) {
        String contentBody=msgReceiveContent.getMsgBody();
        
        //物流信息，可能有多个
        List<LogisticTrackingV5> logisticTrackings=syncCommonManager.queryMsgBody(contentBody,LogisticTrackingV5.class);
        if(logisticTrackings!=null&&!logisticTrackings.isEmpty()){
            for(LogisticTrackingV5 logisticTracking:logisticTrackings){
                //根据OrderCode查询OrderId
                SalesOrderCommand oCommand=sdkOrderService.findOrderByCode(logisticTracking.getBsOrderCode(), null);
                //先查询出以前的数据
                LogisticsCommand beforeLogistics=logisticsManager.findLogisticsByOrderId(oCommand.getId());
                //转换为Model对象
                Logistics logistics=convertToLogistics(logisticTracking,beforeLogistics);
                
                //如果订单CODE是不存在的
                if(logistics==null){
                    try{
                    	Map<String,Object> map=new HashMap<String,Object>();
        				map.put("desc", "发现找不到订单的SCM物流跟踪消息记录!<br/>ordercode:"+logisticTracking.getBsOrderCode());
        				//uniquecontent表示为消息发送,msr.getId()表示，某一个消息只会发送指定次数的报警
        				emailTemplateManager.sendWarningEmail("EMAIL_WARNING", "MQ_MSG_LOGISTIC_TRACKING-"+logisticTracking.getBsOrderCode(), map);
                       // warningPartManager.warnSyncLogisticErrorByNotCode(logisticTracking, msgReceiveContent);
                    }catch(Exception e){
                        logger.error("物流信息因订单不存在同步失败,msgReceiveContentId:"+msgReceiveContent.getId()+",订单CODE:"+logisticTracking.getBsOrderCode(),e);
                    }
                    continue;
                }
                //入库
                logisticsManager.saveLogistics(logistics);
            }
            
        }
        
        //变更状态为已处理
        List<Long> idList=new ArrayList<Long>();
        idList.add(msgReceiveContent.getId());
        sdkMsgManager.updateMsgRecContIsProByIds(idList);
    }
    
    /**
     * 转换为DBModel对象
     * @param logisticTracking
     * @param beforeLogistics
     * @return
     */
    private Logistics convertToLogistics(LogisticTrackingV5 logisticTracking,LogisticsCommand beforeLogistics){
        
        Logistics logistics = new Logistics();
        if(logisticTracking!=null&&logisticTracking.getDetails()!=null&&!logisticTracking.getDetails().isEmpty()){
            //物流详细信息,如果之前存在数据
            if(beforeLogistics!=null){
                logistics.setId(beforeLogistics.getId());
                //如果物流信息为增量同步，则先取出老的物流信息用于追加；如果是全量同步则不用执行该操作
                if(LOGISTIC_TYPE_INCREMENT.equalsIgnoreCase(getLogisticType())) {
                	logistics.setTrackingDescription(beforeLogistics.getTrackingDescription());
                }
            }
            Date date=new Date();
            logistics.setModifyTime(date);
            //根据OrderCode查询OrderId
            SalesOrderCommand oCommand=sdkOrderService.findOrderByCode(logisticTracking.getBsOrderCode(), null);
            
            if(oCommand==null){
                return null;
            }
            
            //设置orderId
            logistics.setOrderId(oCommand.getId());
            List<LogisticDetailV5> details=logisticTracking.getDetails();
            for(LogisticDetailV5 detail:details){
                if(null!=logistics.getTrackingDescription()){
                    logistics.setTrackingDescription(logistics.getTrackingDescription() + "<br/>" + DateUtil.format(detail.getOpTime(), "yyyy-MM-dd HH:mm") + " " + detail.getRemark());
                }else{
                    logistics.setTrackingDescription(DateUtil.format(detail.getOpTime(), "yyyy-MM-dd HH:mm") + " " +detail.getRemark());
                }
                logistics.setVersion(detail.getOpTime());
            }
        }
        
        return logistics;
    }
    
    /**
     * 物流信息的同步方式默认为全量，如果要改为增量，可以在config/common.properties中配置：logistic_type=increment
     * @return
     */
    private String getLogisticType() {
    	Properties properties = ProfileConfigUtil.findCommonPro("config/common.properties");
		String reg =  properties.getProperty(LOGISTIC_TYPE_KEY, LOGISTIC_TYPE_FULL);
		return reg;
    }

}

