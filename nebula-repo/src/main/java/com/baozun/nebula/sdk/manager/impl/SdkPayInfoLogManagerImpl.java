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
import com.feilong.core.bean.ConvertUtil;

import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.getValue;

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
        Integer payment = salesOrderCommand.getPayment();

        boolean paySuccessStatus = payment.toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD);
        String payInfoStr = salesOrderCommand.getPaymentStr();

        Validate.notBlank(payInfoStr, "payInfoStr can't be blank!");

        PayInfoLog payInfoLog = new PayInfoLog();

        payInfoLog.setOrderId(payInfo.getOrderId());
        payInfoLog.setPaySuccessStatus(paySuccessStatus);
        payInfoLog.setCallCloseStatus(false);

        payInfoLog.setPayType(payment);
        payInfoLog.setPayMoney(payInfo.getPayMoney());
        payInfoLog.setPayNumerical(payInfo.getPayNumerical());
        // 用于保存银行
        payInfoLog.setPayInfo(payInfoStr);
        payInfoLog.setSubOrdinate(subOrdinate);
        payInfoLog.setPayInfoId(payInfo.getId());

        payInfoLog.setThirdPayType(getThirdPayType(payInfoStr));

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
    private Integer getThirdPayType(String payInfoStr){
        return ConvertUtil.toInteger(getValue(getResourceBundle("config.payMentInfo"), payInfoStr + ".payType"));
    }
}
