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
package com.baozun.nebula.api.salesorder;

import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 订单Code创建接口.
 *
 * @author Tianlong.Zhang
 */
public interface OrderCodeCreatorManager{

    /**
     * 用于生成订单号.
     *
     * @param source
     *            the source
     * @return the string
     * @deprecated since 5.3.2.24 这个方法将不再使用 ,请实现 {@link #createOrderCode(Long, SalesOrderCommand)},
     */
    @Deprecated
    String createOrderCodeBySource(Integer source);

    /**
     * 生成订单号.
     * 
     * <p>
     * 由于 a项目有特殊需求,mia类型的订单,订单code 长度必须是8位,并且由一定的格式要求,所以订单code的生成方法以后将统一使用 此方法, {@link #createOrderCodeBySource(Integer)} 过时
     * </p>
     *
     * @param shopId
     *            the shop id
     * @param salesOrderCommand
     *            the sales order command
     * @return the string
     * @since 5.3.2.24
     */
    String createOrderCode(Long shopId,SalesOrderCommand salesOrderCommand);

    /**
     * 用于生成交易流水号.
     *
     * @return the string
     */
    String createOrderSerialNO();

}
