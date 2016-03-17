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

import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.wormhole.mq.entity.logistics.LogisticDetailV5;
import com.baozun.nebula.wormhole.mq.entity.logistics.LogisticTrackingV5;

/**
 * WarningPartManager
 *
 * @author: shiyang.lv
 * @date: 2014年5月16日
 **/
public interface WarningPartManager {
    /**
     * 发送一个下单24小时后订单未变更物流状态的告警邮件
     * @param salesOrderCommand
     */
    public void warnPayOrderNoChangStatus(SalesOrderCommand salesOrderCommand,String uniqueContent);
    
    /**
     * 非取消状态的订单10天还没有变更为交易完成状态，发告警邮件
     * @param salesOrderCommand
     */
    public void warnNotCancelOrderNoFinish(SalesOrderCommand salesOrderCommand,String uniqueContent);
    
    /**
     * 48小时还未发现全量库存同步 发送告警邮件
     */
    public void warnFullInventorySyncLater(LogisticsCommand command,String uniqueContent);
    
    /**
     * 同步异常，没有找到对应订单号
     * @param command
     */
    public void warnSyncLogisticErrorByNotCode(LogisticTrackingV5 command,MsgReceiveContent msgReceiveContent,String uniqueContent);
}

