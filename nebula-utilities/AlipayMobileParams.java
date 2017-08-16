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

import java.util.Map;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

/**
 * 封装了 支付宝 mobile 使用的参数值.
 * 
 * <p>
 * ~~ 权宜之策 ~~, 目前支付框架不容易调整
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public final class AlipayMobileParams{

    /** <code>{@value}</code> */
    private static final Map<String, String> ALIPAY_CONFIG_MAP = toMap(getResourceBundle("config/alipay"));

    //---------------------------------------------------------------

    /**
     * 支付网关地址.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * 解析的是 {@link com.baozun.nebula.utilities.integration.payment.PaymentFactory#ALIPAYDEFAULTALIPAYCONFIG config/alipay} 配置文件.
     * </p>
     * 
     * <p>
     * 当使用 <b>mobile</b> 来 gotopayment/notify/return 的时候, 将会使用以下规则(暂不适用于 close trade / query):
     * </p>
     * 
     * <ul>
     * <li>如果配置文件中配置了 <code>payment_gateway_mobile</code> 参数并且值不是 empty或blank, 那么会使用这个值进行和支付宝交互;</li>
     * <li>如果配置文件中没有配置 <code>payment_gateway_mobile</code> 参数或者配置了但是值是 empty/blank, 那么会使用 <code>payment_gateway</code> 参数的值进行和支付宝交互;</li>
     * </ul>
     * </blockquote>
     */
    public static final String GATEWAY = defaultIfNullOrEmpty(ALIPAY_CONFIG_MAP.get("payment_gateway_mobile"), ALIPAY_CONFIG_MAP.get("payment_gateway"));

    /**
     * PARTNER (不解释).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * 解析的是 {@link com.baozun.nebula.utilities.integration.payment.PaymentFactory#ALIPAYDEFAULTALIPAYCONFIG config/alipay} 配置文件.
     * </p>
     * 
     * <p>
     * 当使用 <b>mobile</b> 来 gotopayment/notify/return 的时候, 将会使用以下规则(暂不适用于 close trade / query):
     * </p>
     * 
     * <ul>
     * <li>如果配置文件中配置了 <code>param_partner_mobile</code> 参数并且值不是 empty或blank, 那么会使用这个值进行和支付宝交互;</li>
     * <li>如果配置文件中没有配置 <code>param_partner_mobile</code> 参数或者配置了但是值是 empty/blank, 那么会使用 <code>param.partner</code> 参数的值进行和支付宝交互;</li>
     * </ul>
     * </blockquote>
     */
    public static final String PARTNER = defaultIfNullOrEmpty(ALIPAY_CONFIG_MAP.get("param_partner_mobile"), ALIPAY_CONFIG_MAP.get("param.partner"));

    /**
     * KEY (不解释).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * 解析的是 {@link com.baozun.nebula.utilities.integration.payment.PaymentFactory#ALIPAYDEFAULTALIPAYCONFIG config/alipay} 配置文件.
     * </p>
     * 
     * <p>
     * 当使用 <b>mobile</b> 来 gotopayment/notify/return 的时候, 将会使用以下规则(暂不适用于 close trade / query):
     * </p>
     * 
     * <ul>
     * <li>如果配置文件中配置了 <code>param_key_mobile</code> 参数并且值不是 empty或blank, 那么会使用这个值进行和支付宝交互;</li>
     * <li>如果配置文件中没有配置 <code>param_key_mobile</code> 参数或者配置了但是值是 empty/blank, 那么会使用 <code>param.key</code> 参数的值进行和支付宝交互;</li>
     * </ul>
     * </blockquote>
     */
    public static final String KEY = defaultIfNullOrEmpty(ALIPAY_CONFIG_MAP.get("param_key_mobile"), ALIPAY_CONFIG_MAP.get("param.key"));

    /**
     * 卖家收款账号.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * 解析的是 {@link com.baozun.nebula.utilities.integration.payment.PaymentFactory#ALIPAYDEFAULTALIPAYCONFIG config/alipay} 配置文件.
     * </p>
     * 
     * <p>
     * 当使用 <b>mobile</b> 来 gotopayment/notify/return 的时候, 将会使用以下规则(暂不适用于 close trade / query):
     * </p>
     * 
     * <ul>
     * <li>如果配置文件中配置了 <code>param_seller_email_mobile</code> 参数并且值不是 empty或blank, 那么会使用这个值进行和支付宝交互;</li>
     * <li>如果配置文件中没有配置 <code>param_seller_email_mobile</code> 参数或者配置了但是值是 empty/blank, 那么会使用 <code>param.seller_email</code> 参数的值进行和支付宝交互;</li>
     * </ul>
     * </blockquote>
     */
    public static final String SELLER_EMAIL = defaultIfNullOrEmpty(ALIPAY_CONFIG_MAP.get("param_seller_email_mobile"), ALIPAY_CONFIG_MAP.get("param.seller_email"));

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private AlipayMobileParams(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

}
