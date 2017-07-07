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

import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_ALIPAY;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_COD;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_NETPAY;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_PAYPAL;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_UNIONPAY;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_WECHAT;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
@Component("paymentNameBuilder")
public class DefaultPaymentNameBuilder implements PaymentNameBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPaymentNameBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.PaymentNameBuilder#getPaymentName(java.lang.Integer)
     */
    @Override
    public String build(Integer payment){
        Validate.notNull(payment, "payment can't be null!");

        switch ("" + payment) {
            case SO_PAYMENT_TYPE_COD:
                return "货到付款";

            case SO_PAYMENT_TYPE_NETPAY:
                return "网银在线";

            case SO_PAYMENT_TYPE_ALIPAY:
                return "支付宝";

            case SO_PAYMENT_TYPE_UNIONPAY:
                return "银联支付";

            case SO_PAYMENT_TYPE_WECHAT:
                return "微信支付";

            case SO_PAYMENT_TYPE_PAYPAL:
                return "Paypal";

            default:
                LOGGER.warn("payment:[{}] not support convert!", payment);
                return "" + payment;
        }

    }
}
