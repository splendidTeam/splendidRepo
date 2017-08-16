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
package com.baozun.nebula.sdk.manager.order;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;

/**
 * The Class SdkOrderQueryManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Service("sdkOrderQueryManager")
public class SdkOrderQueryManagerImpl implements SdkOrderQueryManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkOrderQueryManagerImpl.class);

    /** The order manager. */
    @Autowired
    private OrderManager orderManager;

    /** The sdk payment manager. */
    @Autowired
    protected SdkPaymentManager sdkPaymentManager;

    /** The sdk pay info query manager. */
    @Autowired
    private SdkPayInfoQueryManager sdkPayInfoQueryManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.store.web.controller.payment.builder.PaymentBuilder#getOrderCodeBySubOrdinate(java.lang.String, boolean)
     */
    @Override
    //@Transactional(readOnly = true)
    //TODO 暂时不加 readonly 读库和master 有延迟
    public SalesOrderCommand findSalesOrderCommandBySubOrdinate(String subOrdinate,boolean paySuccessStatus){
        Validate.notEmpty(subOrdinate, "subOrdinate can't be null/empty!");

        //---------------------------------------------------------------------
        // 获取支付详细Log
        List<PayInfoLog> payInfoLogList = sdkPayInfoQueryManager.findPayInfoLogListBySubOrdinate(subOrdinate, paySuccessStatus);
        return build(subOrdinate, payInfoLogList, paySuccessStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderQueryManager#findSalesOrderCommandBySubOrdinate(java.lang.String)
     */
    @Override
    //TODO 暂时不加 readonly 读库和master 有延迟
    @Transactional
    public SalesOrderCommand findSalesOrderCommandBySubOrdinate(String subOrdinate){
        Validate.notEmpty(subOrdinate, "subOrdinate can't be null/empty!");

        Map<String, Object> paraMap = toMap("subOrdinate", (Object) subOrdinate);

        List<PayInfoLog> payInfoLogList = sdkPayInfoQueryManager.findPayInfoLogListByQueryMap(paraMap);
        return build(subOrdinate, payInfoLogList, null);
    }

    //---------------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param subOrdinate
     *            the sub ordinate
     * @param payInfoLogList
     *            the pay info log list
     * @param paySuccessStatus
     *            the pay success status
     * @return the sales order command
     */
    private SalesOrderCommand build(String subOrdinate,List<PayInfoLog> payInfoLogList,Boolean paySuccessStatus){
        if (isNullOrEmpty(payInfoLogList)){
            LOGGER.error("can't get payInfo_log by subOrdinate:[{}] and paySuccessStatus:[{}]", subOrdinate, paySuccessStatus);
            throw new BusinessException(60004);//TODO TRANSACTION_SO_NOT_EXIST = 60004;
        }

        //---------------------------------------------------------------------
        // 根据支付流水号，去取结果页面上要显示的订单号
        for (PayInfoLog payInfoLog : payInfoLogList){
            return orderManager.findOrderById(payInfoLog.getOrderId(), 1);
        }
        throw new BusinessException("");
    }

}
