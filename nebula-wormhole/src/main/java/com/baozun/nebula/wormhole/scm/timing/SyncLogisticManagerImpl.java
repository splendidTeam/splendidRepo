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
package com.baozun.nebula.wormhole.scm.timing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.wormhole.scm.manager.LogisticTrackingManager;

/**
 * SyncLogisticManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月9日
 **/
@Service("syncLogisticManager")
public class SyncLogisticManagerImpl implements SyncLogisticManager {

    @Autowired
    private SdkMsgManager sdkMsgManager;
    
    @Autowired
    private SyncCommonManager syncCommonManager;
    
    @Autowired
    private LogisticsManager logisticsManager;
    
    @Autowired
    private LogisticTrackingManager logisticTrackingManager;
    
    private Logger logger = LoggerFactory.getLogger(SyncLogisticManagerImpl.class);
    
    @Override
    public void syncLogisticInfo() {
        
        Map<String, Object> queryMap=new HashMap<String, Object>();
        queryMap.put("ifIdentify", IfIdentifyConstants.IDENTIFY_LOGISTICS_TRACKING);
        queryMap.put("isProccessed", false);
        
        List<MsgReceiveContent> msgReceiveContents=sdkMsgManager.findMsgReceiveContentListByQueryMap(queryMap);
        
        //接收的消息内容
        if(msgReceiveContents!=null&&!msgReceiveContents.isEmpty()){
            for(MsgReceiveContent msgReceiveContent:msgReceiveContents){
                try{
                //处理一条消息
                    logisticTrackingManager.syncLogistic(msgReceiveContent);
                }catch(Exception e){
                    logger.error("物流同步状态异常: id:"+msgReceiveContent.getId()+" msg_id:"+msgReceiveContent.getMsgId(),e);
                }
            }
        }
        
    }
    
   

}

