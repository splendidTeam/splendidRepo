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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.sdk.utils.ManagerValidate;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Service("sdkOrderUpdateManager")
@Transactional
public class SdkOrderUpdateManagerImpl implements SdkOrderUpdateManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkOrderUpdateManagerImpl.class);

    @Autowired
    private SdkOrderDao sdkOrderDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderUpdateManager#updatePaymentTypeByOrderCode(java.lang.String, java.lang.Integer)
     */
    @Override
    public void updatePaymentTypeByOrderCode(String code,Integer payment){
        Integer result = sdkOrderDao.updateOrderPaymentTypeByOrderCode(code, payment);

        ManagerValidate.isExpectedResult(1, result, "update OrderPaymentType By OrderCode,code:[{}],payment:[{}]", code, payment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderUpdateManager#updatePaymentTypeByOrderId(java.lang.Long, java.lang.Integer)
     */
    @Override
    public void updatePaymentTypeByOrderId(Long id,Integer payment){
        Integer result = sdkOrderDao.updateOrderPaymentTypeByOrderId(id, payment);

        ManagerValidate.isExpectedResult(1, result, "update OrderPaymentType By OrderCode,id:[{}],payment:[{}]", id, payment);
    }
}
