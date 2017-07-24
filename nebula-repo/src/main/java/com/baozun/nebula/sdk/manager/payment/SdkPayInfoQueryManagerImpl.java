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
package com.baozun.nebula.sdk.manager.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * The Class SdkPayInfoQueryManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Service("sdkPayInfoQueryManager")
@Transactional
public class SdkPayInfoQueryManagerImpl implements SdkPayInfoQueryManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkPayInfoQueryManagerImpl.class);

    /** The pay info log dao. */
    @Autowired
    private PayInfoLogDao payInfoLogDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager#findPayInfoLogListByQueryMap(java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PayInfoLog> findPayInfoLogListByQueryMap(Map<String, Object> paraMap){
        return payInfoLogDao.findPayInfoLogListByQueryMap(paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.store.web.manager.payment.StorePaymentManager#getPayInfoLogListBySubOrdinate(java.lang.String, boolean)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PayInfoLog> findPayInfoLogListBySubOrdinate(String subOrdinate,boolean paySuccessStatus){
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("subOrdinate", subOrdinate);

        // 支付状态 1代表支付成功，2代表支付没有成功 // 2代表支付没有成功，pay_success_status为false
        // 底层 sql 写死了, 老子要打死他
        paraMap.put("paySuccessStatusStr", paySuccessStatus ? 1 : 2);

        return findPayInfoLogListByQueryMap(paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager#isPaySuccess(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isPaySuccess(String subOrdinate){
        List<PayInfoLog> payInfoLogList = findPayInfoLogListBySubOrdinate(subOrdinate, true);
        return isNotNullOrEmpty(payInfoLogList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayInfoQueryManager#getPayInfoLogListByOrderId(java.lang.Long, boolean)
     */
    @Override
    public List<PayInfoLog> findPayInfoLogListByOrderId(Long orderId,boolean paySuccessStatus){
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("orderId", orderId);

        // 支付状态 1代表支付成功，2代表支付没有成功 // 2代表支付没有成功，pay_success_status为false
        // 底层 sql 写死了, 老子要打死他
        paraMap.put("paySuccessStatusStr", paySuccessStatus ? 1 : 2);
        return findPayInfoLogListByQueryMap(paraMap);
    }
}
