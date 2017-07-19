/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.web.controller.order.builder.subview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.web.controller.order.viewcommand.LogisticsInfoBarRecordSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.LogisticsInfoSubViewCommand;
import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.date.DateUtil;

/**
 * 专门用来构造 {@link LogisticsInfoSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("logisticsInfoSubViewCommandBuilder")
public class DefaultLogisticsInfoSubViewCommandBuilder implements LogisticsInfoSubViewCommandBuilder{

    /** The logistics manager. */
    @Autowired
    @Qualifier("logisticsManager")
    private LogisticsManager logisticsManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.LogisticsInfoSubViewCommandBuilder#buildLogisticsInfoSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public LogisticsInfoSubViewCommand build(SalesOrderCommand salesOrderCommand){
        // 物流信息
        LogisticsInfoSubViewCommand logisticsInfoSubViewCommand = new LogisticsInfoSubViewCommand();
        LogisticsCommand logisticsCommand = logisticsManager.findLogisticsByOrderId(salesOrderCommand.getId());
        PropertyUtil.copyProperties(logisticsInfoSubViewCommand, salesOrderCommand, "transCode", "logisticsProviderName");
        if (null != logisticsCommand){
            String trackingDescription = logisticsCommand.getTrackingDescription();
            logisticsInfoSubViewCommand.setLogisticsInfoBarRecordSubViewCommandList(transformTrackingDescription(trackingDescription));
        }
        return logisticsInfoSubViewCommand;
    }

    /**
     * 说明：String物流信息转换为 List<LogisticsInfoBarRecordSubViewCommand>.
     *
     * @author 张乃骐
     * @param TrackingDescription
     *            the Tracking description
     * @return the list< logistics info bar record sub view command>
     * @time：2016年5月12日 下午8:28:56
     */
    private static List<LogisticsInfoBarRecordSubViewCommand> transformTrackingDescription(String TrackingDescription){
        if (Validator.isNullOrEmpty(TrackingDescription)){
            return null;
        }
        List<LogisticsInfoBarRecordSubViewCommand> list = new ArrayList<>();
        // String[] split = StringUtils.split(TrackingDescription, "<br/>");
        String[] split = TrackingDescription.split("<br/>");
        for (String string : split){
            LogisticsInfoBarRecordSubViewCommand logisticsInfoBarRecordSubViewCommand = new LogisticsInfoBarRecordSubViewCommand();
            //String date =  StringUtils.substring(string, 0, 17) ;
            String date = StringUtils.trimToEmpty(StringUtils.substring(string, 0, 17));
            logisticsInfoBarRecordSubViewCommand.setBarScanDate(DateUtil.toDate(date, DatePattern.COMMON_DATE_AND_TIME_WITHOUT_SECOND));
            logisticsInfoBarRecordSubViewCommand.setRemark(StringUtils.substring(string, 17));
            list.add(logisticsInfoBarRecordSubViewCommand);
        }
        Collections.reverse(list);
        return list;
    }
}
