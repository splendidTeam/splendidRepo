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

import static com.baozun.nebula.model.system.MataInfo.PAY_URL_PREFIX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
@Component("orderCreateEmailPayUrlBuilder")
public class DefaultOrderCreateEmailPayUrlBuilder implements OrderCreateEmailPayUrlBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderCreateEmailPayUrlBuilder.class);

    /** The frontend base url. */
    @Value("#{meta['frontend.url']}")
    private String frontendBaseUrl;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.payment.OrderCreateEmailPayUrlBuilder#build(java.lang.String, boolean)
     */
    @Override
    public String build(String subOrdinate,boolean isShowPayLink){
        if (isShowPayLink){
            String payUrlPrefix = sdkMataInfoManager.findValue(PAY_URL_PREFIX);
            return frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
        }
        return "#";
    }
}
