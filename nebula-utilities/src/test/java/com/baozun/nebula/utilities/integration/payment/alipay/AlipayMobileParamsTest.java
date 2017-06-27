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
package com.baozun.nebula.utilities.integration.payment.alipay;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class AlipayMobileParamsTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayMobileParamsTest.class);

    @Test
    public void test(){
        assertEquals("http://wappaygw.alipay.com/service/rest.htm", AlipayMobileParams.GATEWAY);
        assertEquals("2088201564874474", AlipayMobileParams.PARTNER);
        assertEquals("rddf8owy1d4jbbiv98nhykgl1ss05dhk", AlipayMobileParams.KEY);
        assertEquals("alipay-test15@alipay.com", AlipayMobileParams.SELLER_EMAIL);

        //       param.partner=2088201564862550
        //                       param.key=e49z2zjaimzxwzln654gvy1w95fpe4l8
        //                       param.seller_email=alipay-test14@alipay.com
        //                       payment_gateway=https://mapi.alipay.com/gateway.do
        //
    }

    @Test
    public void testNull(){
        assertEquals("https://mapi.alipay.com/gateway.do", AlipayMobileParams.GATEWAY);
        assertEquals("2088201564862550", AlipayMobileParams.PARTNER);
        assertEquals("e49z2zjaimzxwzln654gvy1w95fpe4l8", AlipayMobileParams.KEY);
        assertEquals("alipay-test14@alipay.com", AlipayMobileParams.SELLER_EMAIL);

    }
}
