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

import static com.baozun.nebula.web.controller.order.resolver.SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE;
import static com.baozun.nebula.web.controller.order.resolver.SalesOrderResult.ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
import static com.baozun.nebula.web.controller.order.resolver.SalesOrderResult.SUCCESS;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResultUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 订单创建时候的校验.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Component("salesOrderCreateValidator")
public class SalesOrderCreateValidatorImpl implements SalesOrderCreateValidator{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderCreateValidatorImpl.class);

    /**  */
    @Autowired(required = false)
    private SalesOrderCreateValidatorHandler salesOrderCreateValidatorHandler;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.validator.SalesOrderCreateValidator#validate(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, com.baozun.nebula.web.controller.order.form.OrderForm, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public SalesOrderResult validate(ShoppingCartCommand checkStatusShoppingCartCommand,OrderForm orderForm,HttpServletRequest request){
        if (isNullOrEmpty(checkStatusShoppingCartCommand) || isNullOrEmpty(checkStatusShoppingCartCommand.getShoppingCartLineCommands())){// 購物車不能為空
            return ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //-------------------------------------------------------------------------
        //如果实现了接口则执行preHandle()方法
        if (null != salesOrderCreateValidatorHandler){
            LOGGER.debug("salesOrderCreateValidatorHandler:[{}] is not null,will call [preHandle] method.", salesOrderCreateValidatorHandler.getClass().getName());

            SalesOrderResult salesOrderResult = salesOrderCreateValidatorHandler.preHandle(checkStatusShoppingCartCommand, orderForm, request);

            if (SUCCESS != salesOrderResult){ //如果preHandle()方法返回值不是SalesOrderResult.SUCCESS则直接返回
                LOGGER.debug("[{}] call [preHandle] method,not SUCCESS,return :[{}].", salesOrderCreateValidatorHandler.getClass().getName(), salesOrderResult);
                return salesOrderResult;
            }
        }

        //-------------------------------------------------------------------------
        SalesOrderResult salesOrderResult = validateCouponInfo(checkStatusShoppingCartCommand, orderForm);
        if (SalesOrderResultUtil.isNotSuccess(salesOrderResult)){
            return salesOrderResult;
        }
        //-------------------------------------------------------------------------

        if (null != salesOrderCreateValidatorHandler){
            LOGGER.debug("salesOrderCreateValidatorHandler:[{}] is not null,will call [postHandle] method.", salesOrderCreateValidatorHandler.getClass().getName());

            salesOrderResult = salesOrderCreateValidatorHandler.postHandle(checkStatusShoppingCartCommand, orderForm, request);

            if (SUCCESS != salesOrderResult){//如果postHandle()方法返回值不是SalesOrderResult.SUCCESS则直接返回
                LOGGER.debug("[{}] call [postHandle] method,not SUCCESS,return :[{}].", salesOrderCreateValidatorHandler.getClass().getName(), salesOrderResult);
                return salesOrderResult;
            }
        }

        //---------------------------------------------------------------
        return SUCCESS;
    }

    /**
     * @param checkStatusShoppingCartCommand
     * @param orderForm
     * @since 5.3.2.22
     */
    private SalesOrderResult validateCouponInfo(ShoppingCartCommand checkStatusShoppingCartCommand,OrderForm orderForm){
        String couponCode = orderForm.getCouponInfoSubForm().getCouponCode();
        //** 如果输入了优惠券则要进行优惠券验证
        if (isNotNullOrEmpty(couponCode)){
            LOGGER.debug("will check couponCode:[{}]", couponCode);

            //** 校驗优惠券促销 
            SalesOrderResult salesOrderResult = checkCoupon(checkStatusShoppingCartCommand, couponCode);
            if (SUCCESS != salesOrderResult){
                LOGGER.debug("check couponCode:[{}] result is :[{}],return this result", couponCode, salesOrderResult);
                return salesOrderResult;
            }
        }
        return SalesOrderResult.SUCCESS;
    }

    /**
     * 校驗優惠券是否有效 ,有效返回 {@link SUCCESS}.
     *
     * @param shoppingCartCommand
     * @param couponCode
     * @return 有效返回 {@link SUCCESS}
     */
    private SalesOrderResult checkCoupon(ShoppingCartCommand shoppingCartCommand,String couponCode){
        List<PromotionBrief> cartPromotionBriefList = shoppingCartCommand.getCartPromotionBriefList();

        if (isNullOrEmpty(cartPromotionBriefList)){// 無效
            LOGGER.info("check couponCode:[{}] ,but shoppingCartCommand cartPromotionBriefList isNullOrEmpty,return :[{}].", couponCode, ORDER_COUPON_NOT_AVALIBLE);
            return ORDER_COUPON_NOT_AVALIBLE;
        }

        //-------------------------------------------------------------------------------------

        for (PromotionBrief promotionBrief : cartPromotionBriefList){// 從活動中取記錄校驗
            List<PromotionSettingDetail> promotionSettingDetailList = promotionBrief.getDetails();

            //---------------------------------------------------------------

            if (isNullOrEmpty(promotionSettingDetailList)){
                continue;
            }

            //---------------------------------------------------------------
            for (PromotionSettingDetail promotionSettingDetail : promotionSettingDetailList){// 遍曆活動詳情
                SalesOrderResult salesOrderResult = check(promotionSettingDetail, couponCode);

                if (SUCCESS == salesOrderResult){
                    LOGGER.debug("promotionSettingDetail:[{}],check couponCode:[{}],return SUCCESS.", promotionSettingDetail.getId(), promotionSettingDetail);
                    return SUCCESS;//如果有一个校验成功了 那么就返回
                }
                //go on 
                continue;
            }
        }
        return ORDER_COUPON_NOT_AVALIBLE;
    }

    /**
     * 拿 promotionSettingDetail 校验.
     * 
     * @param promotionSettingDetail
     * @param couponCode
     * @return
     * @since 5.3.2.18
     */
    private SalesOrderResult check(PromotionSettingDetail promotionSettingDetail,String couponCode){
        Set<String> promotionSettingDetailCouponCodes = promotionSettingDetail.getCouponCodes();

        if (isNotNullOrEmpty(promotionSettingDetailCouponCodes)){
            return promotionSettingDetailCouponCodes.contains(couponCode) ? SUCCESS : null;
        }

        //---------------------------------------------------------------
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = promotionSettingDetail.getAffectSKUDiscountAMTList();

        if (isNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            return null;
        }
        //---------------------------------------------------------------

        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){// 遍曆商品行優惠記錄
            Set<String> couponCodes = promotionSKUDiscAMTBySetting.getCouponCodes();

            if (isNullOrEmpty(couponCodes)){// 如果整單優惠券沒有，校驗商品行優惠券
                continue;
            }
            if (couponCodes.contains(couponCode)){
                return SUCCESS;
            }
        }
        return null;
    }

}
