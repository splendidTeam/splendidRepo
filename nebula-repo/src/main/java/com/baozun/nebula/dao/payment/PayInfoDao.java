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
package com.baozun.nebula.dao.payment;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.sdk.command.PayInfoCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * PayDetail 支付详情Dao.
 *
 * @author Tianlong.Zhang
 */
public interface PayInfoDao extends GenericEntityDao<PayInfo, Long>{

    /**
     * Find pay info by order ids.
     *
     * @param orderIds
     *            the order ids
     * @return the list
     */
    @NativeQuery(model = PayInfoCommand.class)
    List<PayInfoCommand> findPayInfoByOrderIds(@QueryParam("orderIds") List<Long> orderIds);

    /**
     * 根据订单Id查询payInfoCommand.
     *
     * @param orderId
     *            the order id
     * @return the list
     */
    @NativeQuery(model = PayInfoCommand.class)
    List<PayInfoCommand> findPayInfoCommandByOrderId(@QueryParam("orderId") Long orderId);

    /**
     * 通过参数map获取PayInfo列表.
     *
     * @param paraMap
     *            the para map
     * @return the list
     */
    @NativeQuery(model = PayInfo.class)
    List<PayInfo> findPayInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);

    /**
     * 根据订单id,修改PayInfo的支付方式.
     *
     * @param id
     *            the id
     * @param payType
     *            the pay type
     * @param payInfo
     *            the pay info
     * @return the integer
     * @since 5.3.2.22
     */
    @NativeUpdate
    Integer updateNoPayPayTypeByOrderId(@QueryParam("id") Long id,@QueryParam("payType") Integer payType,@QueryParam("payInfo") String payInfo);
}
