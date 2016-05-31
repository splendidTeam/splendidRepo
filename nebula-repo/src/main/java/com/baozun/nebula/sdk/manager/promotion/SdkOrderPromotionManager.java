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
package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;

import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;

/**
 * The Interface SdkOrderPromotionManager.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午5:22:24
 * @since 5.3.1
 */
public interface SdkOrderPromotionManager{

    /**
     * 设置免运费的促销.
     *
     * @param orderId
     *            the order id
     * @param promotionSKUDiscAMTBySetting
     *            the promotion sku disc amt by setting
     * @since 5.3.1
     */
    void saveOrderShipPromotion(Long orderId,PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting);

    /**
     * Sava order promotion.
     *
     * @param orderId
     *            the order id
     * @param orderLineId
     *            the order line id
     * @param promotionSKUDiscAMTBySetting
     *            the promotion sku disc amt by setting
     * @param couponCodes
     *            the sales order command
     */
    void savaOrderPromotion(
                    Long orderId,
                    Long orderLineId,
                    PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting,
                    List<CouponCodeCommand> couponCodes);

}
