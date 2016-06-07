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
package com.baozun.nebula.web.controller.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * Neubla支付Controller
 *
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年5月26日  上午11:33:30
 */
public class NebulaBasePaymentController extends BaseController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaBasePaymentController.class);
    
    @Autowired
	private SdkPaymentManager sdkPaymentManager;
	
	@Autowired
	private OrderManager orderManager;
	
	/**
	 * 根据流水号获取订单信息
	 * 
	 * @param subOrdinate 支付流水号
	 * @param paySuccessStatus 支付状态，1表示支付成功，2表示未支付
	 * @return
	 * @throws IllegalPaymentStateException 
	 */
	protected SalesOrderCommand getSalesOrderBySubOrdinate(String subOrdinate, Integer paySuccessStatus) throws IllegalPaymentStateException {
        List<PayInfoLog> payInfoLogList = getPayInfoLogListBySubOrdinate(subOrdinate, paySuccessStatus);
        
        if (Validator.isNullOrEmpty(payInfoLogList)){
            LOGGER.error("can not get payInfo_log by subOrdinate:[{}] and paySuccessStatus:[{}]", subOrdinate, true);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_UNPAID, "支付信息不存在或尚未支付");
        }
        
        // 根据支付流水号，去取结果页面上要显示的订单信息
        PayInfoLog payInfoLog = payInfoLogList.get(0);
		SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 2);
		
        return salesOrder;
    }
	
	/**
	 * 根据流水号取未付款支付信息日志
	 * @param subOrdinate
	 * @return
	 */
	protected List<PayInfoLog> getUnpaidPayInfoLogsBySubOrdinate(String subOrdinate) {
		//2代表支付没有成功，pay_success_status为false
		return getPayInfoLogListBySubOrdinate(subOrdinate, 2);
	}
	
	/**
	 * 根据流水号取支付信息日志
	 * @param subOrdinate 支付流水号
	 * @param paySuccessStatus 支付状态，1表示支付成功，2表示未支付
	 * @return
	 */
	protected List<PayInfoLog> getPayInfoLogListBySubOrdinate(String subOrdinate, Integer paySuccessStatus) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        paraMap.put("paySuccessStatusStr", paySuccessStatus);
        List<PayInfoLog> payInfoLogList = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);
        return payInfoLogList;
    }
    
}
