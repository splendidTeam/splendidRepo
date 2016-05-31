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

import java.math.BigDecimal;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * The Interface SdkPayInfoManager.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午4:07:32
 * @since 5.3.1
 */
public interface SdkPayInfoManager extends BaseManager{

    /**
     * Save pay info of pay main.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param orderId
     *            the order id
     * @param payMainMoney
     *            the pay main money
     * @return the pay info
     */
    PayInfo savePayInfoOfPayMain(SalesOrderCommand salesOrderCommand,Long orderId,BigDecimal payMainMoney);

    /**
     * Save pay info.
     *
     * @param orderId
     *            the order id
     * @param payType
     *            the pay type
     * @param payMoney
     *            the pay money
     * @return the pay info
     */
    PayInfo savePayInfo(Long orderId,int payType,BigDecimal payMoney);

}
