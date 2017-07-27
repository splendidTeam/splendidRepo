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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.PayInfoLog;

/**
 * 专注于查询.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public interface SdkPayInfoQueryManager extends BaseManager{

    /**
     * 查询付款日志.
     * 
     * <p>
     * 迁移 自 {@link com.baozun.nebula.sdk.manager.SdkPaymentManager#findPayInfoLogListByQueryMap(Map) } ,逻辑完全一样
     * </p>
     * 
     * <p>
     * 推荐使用 {@link #findPayInfoLogListBySubOrdinate(String, boolean)}
     * </p>
     *
     * @param paraMap
     *            the para map
     * @return the list
     * @see com.baozun.nebula.sdk.manager.SdkPaymentManager#findPayInfoLogListByQueryMap(Map)
     */
    List<PayInfoLog> findPayInfoLogListByQueryMap(Map<String, Object> paraMap);

    /**
     * 根据流水号和支付状态查询支付log.
     *
     * @param subOrdinate
     *            流水号
     * @param paySuccessStatus
     *            支付状态, <br>
     *            true表示支付成功的, false 表示支付失败的 <br>
     * @return the pay info log list by sub ordinate
     */
    List<PayInfoLog> findPayInfoLogListBySubOrdinate(String subOrdinate,boolean paySuccessStatus);

    /**
     * 根据order id和支付状态查询支付log.
     *
     * @param orderId
     *            the order id
     * @param paySuccessStatus
     *            the pay success status
     * @return the pay info log list by order id
     * @since 5.3.2.22
     */
    List<PayInfoLog> findPayInfoLogListByOrderId(Long orderId,boolean paySuccessStatus);

    /**
     * 判断一个支付流水号是否支付成功.
     *
     * @param subOrdinate
     *            the sub ordinate
     * @return true, if is pay success
     * @since 5.3.2.22
     */
    boolean isPaySuccess(String subOrdinate);

}
