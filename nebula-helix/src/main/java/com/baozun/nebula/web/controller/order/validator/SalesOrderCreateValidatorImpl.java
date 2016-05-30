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
package com.baozun.nebula.web.controller.order.validator;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.feilong.core.Validator;

/**
 *
 * @author feilong
 * @since 5.3.1
 */
@Component("salesOrderCreateValidator")
public class SalesOrderCreateValidatorImpl implements SalesOrderCreateValidator{

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderCreateValidatorImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.validator.SalesOrderCreateValidator#validate(com.baozun.nebula.sdk.command.shoppingcart.
     * ShoppingCartCommand, java.lang.String)
     */
    @Override
    public SalesOrderResult validate(ShoppingCartCommand shoppingCartCommand,String couponCode){
        if (Validator.isNullOrEmpty(shoppingCartCommand) || Validator.isNullOrEmpty(shoppingCartCommand.getShoppingCartLineCommands())){// 購物車不能為空
            return SalesOrderResult.ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //** 如果输入了优惠券则要进行优惠券验证
        if (Validator.isNotNullOrEmpty(couponCode)){
            //** 校驗优惠券促销 
            SalesOrderResult salesOrderResult = checkCoupon(shoppingCartCommand, couponCode);
            if (null != salesOrderResult){
                return salesOrderResult;
            }
        }

        return SalesOrderResult.SUCCESS;
    }

    /**
     * 校驗優惠券是否有效 success 有效，其他 無效 失败返回错误，正确返回null
     **/
    private SalesOrderResult checkCoupon(ShoppingCartCommand shoppingCartCommand,String couponCode){
        List<PromotionBrief> cartPromotionBriefList = shoppingCartCommand.getCartPromotionBriefList();

        if (Validator.isNullOrEmpty(cartPromotionBriefList)){// 無效
            return SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE;
        }

        for (PromotionBrief promotionBrief : cartPromotionBriefList){// 從活動中取記錄校驗

            List<PromotionSettingDetail> details = promotionBrief.getDetails();

            if (Validator.isNotNullOrEmpty(details)){
                for (PromotionSettingDetail settingDetail : details){// 遍曆活動詳情
                    if (Validator.isNullOrEmpty(settingDetail.getCouponCodes())){// 先校驗整單優惠券有沒有
                        if (Validator.isNotNullOrEmpty(settingDetail.getAffectSKUDiscountAMTList())){
                            for (PromotionSKUDiscAMTBySetting skuSetting : settingDetail.getAffectSKUDiscountAMTList()){// 遍曆商品行優惠記錄
                                Set<String> couponCodes = skuSetting.getCouponCodes();
                                if (Validator.isNullOrEmpty(couponCodes)){// 如果整單優惠券沒有，校驗商品行優惠券
                                    continue;
                                }
                                if (couponCodes.contains(couponCode)){
                                    return null;
                                }
                            }
                        }
                        continue;
                    }
                    if (settingDetail.getCouponCodes().contains(couponCode)){
                        return null;
                    }
                }
            }
        }
        return SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE;

    }
}
