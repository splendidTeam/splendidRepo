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
package com.baozun.nebula.web.controller.order.builder;

import java.util.List;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.web.controller.order.form.ShippingInfoSubForm;

/**
 * CalcFreightCommand 构造器
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface CalcFreightCommandBuilder{

    /**
     * To calc freight command.
     *
     * @param shippingInfoSubForm
     * @return the calc freight command
     * @see <a href="http://jira.baozun.cn/browse/NB-384?filter=10744">NB-384</a>
     * @since 5.3.2.3
     */
    CalcFreightCommand build(ShippingInfoSubForm shippingInfoSubForm);

    /**
     * 通过 addressList 来构造CalcFreightCommand
     *
     * @param contactCommandList
     * @return
     */
    CalcFreightCommand build(List<ContactCommand> contactCommandList);
}
