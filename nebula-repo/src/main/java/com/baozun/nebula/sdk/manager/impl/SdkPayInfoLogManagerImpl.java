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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPayInfoLogManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.core.Validator;

/**
 * The Class SdkPayInfoLogManagerImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午4:11:21
 * @since 5.3.1
 * @see com.baozun.nebula.sdk.manager.impl.SdkPayInfoManagerImpl
 */
@Transactional
@Service("sdkPayInfoLogManager")
public class SdkPayInfoLogManagerImpl implements SdkPayInfoLogManager{

    /** The pay info log dao. */
    @Autowired
    private PayInfoLogDao payInfoLogDao;

    @Override
    public void savePayInfoLogOfPayMain(SalesOrderCommand salesOrderCommand,String subOrdinate,PayInfo payInfo){
        Boolean codFlag = salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD);

        PayInfoLog payInfoLog = new PayInfoLog();

        payInfoLog.setOrderId(payInfo.getOrderId());
        if (codFlag){
            payInfoLog.setPaySuccessStatus(true);
        }else{
            payInfoLog.setPaySuccessStatus(false);
        }
        payInfoLog.setCallCloseStatus(false);
        payInfoLog.setPayType(salesOrderCommand.getPayment());
        payInfoLog.setPayMoney(payInfo.getPayMoney());
        payInfoLog.setPayNumerical(payInfo.getPayNumerical());
        // 用于保存银行
        payInfoLog.setPayInfo(salesOrderCommand.getPaymentStr());
        payInfoLog.setSubOrdinate(subOrdinate);
        payInfoLog.setPayInfoId(payInfo.getId());

        String payType = getPayType(salesOrderCommand);
        if (Validator.isNotNullOrEmpty(payType)){
            payInfoLog.setThirdPayType(Integer.parseInt(payType));
        }

        payInfoLog.setCreateTime(new Date());
        payInfoLogDao.save(payInfoLog);

    }

    /**
     * 获得 pay type.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @return the pay type
     * @since 5.3.1
     */
    private String getPayType(SalesOrderCommand salesOrderCommand){
        Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
        String payInfoStr = salesOrderCommand.getPaymentStr();
        return pro.getProperty(payInfoStr + ".payType").trim();
    }
}
