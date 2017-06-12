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
package com.baozun.nebula.utilities.integration.payment;

import com.feilong.core.lang.NumberUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;

/**
 * 和支付相关的工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.17
 */
public final class PaymentUtil{

    /** Don't let anyone instantiate this class. */
    private PaymentUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 一个字符串分转成 字符串元的方法.
     * 
     * <p>
     * 由于 订单金额 total_fee 是 Int 100 订单总金额，单位为分,需要转成元 以判断
     * </p>
     *
     * @param totalFeeFen
     *            字符串的金额,单位分, 常用于 微信 以及银联等
     * @return 如果 <code>totalFeeFen</code> 是null,返回null<br>
     *         如果 <code>totalFeeFen</code> 是blank,返回null<br>
     *         其他 将转成BigDecimal 并除以100,再转成字符串
     */
    public static String toYuanString(String totalFeeFen){
        if (isNullOrEmpty(totalFeeFen)){
            return null;
        }
        return NumberUtil.getDivideValue(toBigDecimal(totalFeeFen), 100, 2).toString();
    }
}
