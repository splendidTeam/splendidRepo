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
package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单里面的物流信息.
 * 
 * <p>
 * 示例代码:
 * 
 * <pre class="code">
2016-02-14 09:55 順豐速運 已收取快件<br/>2016-02-15 09:55 正在派送途中,請您準備簽收(派件人:黄润平(天安组),电话:11111111111)<br/>2016-02-16 09:55 快件在 【深圳】, 正轉運至 【中環,香港】<br/>2016-02-17 09:55 快件到達 【深圳】<br/>2016-02-18 09:55 快件正送往順豐店/站 【大洲通迅】<br/>2016-02-19 09:55 代理收件<br/>2016-02-20 09:55 已簽收,感謝使用順豐,期待再次為您服務

或者 

2016-01-28 15:08 投递并签收，签收人：张三
 * </pre>
 * 
 * 此时,需要解析
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 * @see com.baozun.nebula.model.salesorder.Logistics
 * @see com.baozun.nebula.sdk.command.logistics.LogisticsCommand
 * @since 5.3.1
 */
public class LogisticsInfoSubViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long                          serialVersionUID = -6400008677521495969L;

    /**
     * 物流单号 快递单号：当出库时会提供.
     * 
     * @see com.baozun.nebula.model.salesorder.SalesOrder#getTransCode()
     */
    private String                                     transCode;

    /**
     * 物流商名称.
     * 
     * @see com.baozun.nebula.model.salesorder.SalesOrder#getLogisticsProviderName()
     */
    private String                                     logisticsProviderName;

    /**
     * 巴枪扫描记录.
     * 
     * @see com.baozun.nebula.model.salesorder.Logistics#getTrackingDescription()
     */
    private List<LogisticsInfoBarRecordSubViewCommand> logisticsInfoBarRecordSubViewCommandList;

    /**
     * 获得 物流单号 快递单号：当出库时会提供.
     *
     * @return the transCode
     */
    public String getTransCode(){
        return transCode;
    }

    /**
     * 设置 物流单号 快递单号：当出库时会提供.
     *
     * @param transCode
     *            the transCode to set
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }

    /**
     * 获得 物流商名称.
     *
     * @return the logisticsProviderName
     */
    public String getLogisticsProviderName(){
        return logisticsProviderName;
    }

    /**
     * 设置 物流商名称.
     *
     * @param logisticsProviderName
     *            the logisticsProviderName to set
     */
    public void setLogisticsProviderName(String logisticsProviderName){
        this.logisticsProviderName = logisticsProviderName;
    }

    /**
     * 获得 巴枪扫描记录.
     *
     * @return the logisticsInfoBarRecordSubViewCommandList
     */
    public List<LogisticsInfoBarRecordSubViewCommand> getLogisticsInfoBarRecordSubViewCommandList(){
        return logisticsInfoBarRecordSubViewCommandList;
    }

    /**
     * 设置 巴枪扫描记录.
     *
     * @param logisticsInfoBarRecordSubViewCommandList
     *            the logisticsInfoBarRecordSubViewCommandList to set
     */
    public void setLogisticsInfoBarRecordSubViewCommandList(
                    List<LogisticsInfoBarRecordSubViewCommand> logisticsInfoBarRecordSubViewCommandList){
        this.logisticsInfoBarRecordSubViewCommandList = logisticsInfoBarRecordSubViewCommandList;
    }

}
