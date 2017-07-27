/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * PayInfoLog 的业务.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午4:07:32
 * @since 5.3.1
 */
public interface SdkPayInfoLogManager extends BaseManager{

    void savePayInfoLogOfPayMain(String subOrdinate,SalesOrderCommand salesOrderCommand,PayInfo payInfo);

    /**
     * 更新未支付的payInfoLog信息支付方式.
     * 
     * @param id
     *            the order id
     * @param payType
     *            the pay type
     * @param payInfo
     *            the pay info
     * @param thirdPayType
     *            the third pay type
     * @return the integer
     * @since 5.3.2.22
     */
    void updateNoPayPayInfoLogPayTypeById(//
                    Long id,
                    Integer payType,
                    String payInfo,
                    Integer thirdPayType);

}
