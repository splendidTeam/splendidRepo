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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.payment.PayCodeDao;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;

/**
 * 处理 PayCode 的业务类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午4:56:19
 * @since 5.3.1
 */
@Transactional
@Service("sdkPayCodeManager")
public class SdkPayCodeManagerImpl implements SdkPayCodeManager{

    /** The pay code dao. */
    @Autowired
    private PayCodeDao payCodeDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkPayCodeManager#savaPayCode(java.math.BigDecimal, java.lang.String)
     */
    @Override
    public void savaPayCode(String subOrdinate,BigDecimal payMoney){
        PayCode payCode = new PayCode();

        payCode.setPayMoney(payMoney);
        payCode.setPayNumerical(payMoney);

        payCode.setPaySuccessStatus(false);
        payCode.setSubOrdinate(subOrdinate);

        payCode.setCreateTime(new Date());

        payCodeDao.save(payCode);
    }
}
