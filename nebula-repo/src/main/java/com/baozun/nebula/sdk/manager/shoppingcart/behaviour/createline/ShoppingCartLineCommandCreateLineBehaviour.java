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

package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门来创建订单行的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月26日 下午6:21:29
 * @since 5.3.1
 */
public interface ShoppingCartLineCommandCreateLineBehaviour extends BaseManager{

    /**
     * Save order line.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    void saveOrderLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand);

}
