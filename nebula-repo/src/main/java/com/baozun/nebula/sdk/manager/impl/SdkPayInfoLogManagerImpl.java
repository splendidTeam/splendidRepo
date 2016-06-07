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

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
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
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午4:11:21
 * @see com.baozun.nebula.sdk.manager.impl.SdkPayInfoManagerImpl
 * @since 5.3.1
 */
@Transactional
@Service("sdkPayInfoLogManager")
public class SdkPayInfoLogManagerImpl implements SdkPayInfoLogManager{

    /** The pay info log dao. */
    @Autowired
    private PayInfoLogDao payInfoLogDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoLogManager#savePayInfoLogOfPayMain(java.lang.String,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, com.baozun.nebula.model.salesorder.PayInfo)
     */
    @Override
    public void savePayInfoLogOfPayMain(String subOrdinate,SalesOrderCommand salesOrderCommand,PayInfo payInfo){
        PayInfoLog payInfoLog = new PayInfoLog();

        payInfoLog.setOrderId(payInfo.getOrderId());
        payInfoLog.setPaySuccessStatus(salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD));
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
     */
    private String getPayType(SalesOrderCommand salesOrderCommand){
        String source = "config/payMentInfo.properties";
        Properties properties = ProfileConfigUtil.findCommonPro(source);
        String payInfoStr = salesOrderCommand.getPaymentStr();
        Validate.notBlank(payInfoStr, "payInfoStr can't be blank!");

        String key = payInfoStr + ".payType";
        String property = properties.getProperty(key);
        Validate.notBlank(property, "property can't be blank!,can not find:[%s] in [%s]", key, source);
        return property.trim();
    }
}
