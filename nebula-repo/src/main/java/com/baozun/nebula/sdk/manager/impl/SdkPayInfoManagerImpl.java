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
package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPayInfoManager;

/**
 * The Class SdkPayInfoManagerImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午4:08:03
 * @since 5.3.1
 */
@Transactional
@Service("sdkPayInfoManager")
public class SdkPayInfoManagerImpl implements SdkPayInfoManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkPayInfoManagerImpl.class);

    /** The pay info dao. */
    @Autowired
    private PayInfoDao          payInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoManager#savePayInfoOfPayMain(com.baozun.nebula.sdk.command.SalesOrderCommand,
     * java.lang.Long, java.math.BigDecimal)
     */
    @Override
    public PayInfo savePayInfoOfPayMain(SalesOrderCommand salesOrderCommand,Long orderId,BigDecimal payMainMoney){
        Integer payment = salesOrderCommand.getPayment();

        PayInfo payInfo = new PayInfo();
        payInfo.setOrderId(orderId);
        Boolean codFlag = payment.toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD);
        if (codFlag){
            payInfo.setPaySuccessStatus(true);
        }else{
            payInfo.setPaySuccessStatus(false);
        }
        payInfo.setPayType(payment);
        // 用于保存银行
        payInfo.setPayInfo(salesOrderCommand.getPaymentStr());

        //XXX feilong 好奇怪的两个参数 PayMoney PayNumerical 好像一样的, 有区别吗
        payInfo.setPayMoney(payMainMoney);
        payInfo.setPayNumerical(payMainMoney);
        // 分期期数
        payInfo.setPeriods(salesOrderCommand.getPeriods());
        payInfo.setCreateTime(new Date());
        return payInfoDao.save(payInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoManager#savePayInfo(java.lang.Long, int, java.math.BigDecimal)
     */
    @Override
    public PayInfo savePayInfo(Long orderId,int payType,BigDecimal payMoney){
        PayInfo payInfo = new PayInfo();

        payInfo.setOrderId(orderId);
        payInfo.setPaySuccessStatus(true);
        payInfo.setPayType(payType);
        payInfo.setPayMoney(payMoney);
        payInfo.setPayNumerical(payMoney);

        payInfo.setCreateTime(new Date());
        // 付款时间
        payInfo.setModifyTime(new Date());
        return payInfoDao.save(payInfo);
    }
}
