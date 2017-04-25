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
package com.baozun.nebula.web.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.order.builder.ShoppingCartCommandShowBuilder;
import com.baozun.nebula.web.controller.order.form.CouponInfoSubForm;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartCommandBuilder;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * 
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
public class NebulaOrderRecalcController extends NebulaAbstractTransactionController{

    /** The order manager. */
    @Autowired
    private OrderManager orderManager;

    /**  */
    @Autowired
    private ShoppingCartCommandBuilder shoppingCartCommandBuilder;

    /**  */
    @Autowired
    private ShoppingCartCommandShowBuilder shoppingCartCommandShowBuilder;

    /**
     * 再次计算金额 .
     * 
     * <h3>使用情景:</h3>
     * <blockquote>
     * 
     * <ol>
     * <li>使用优惠券</li>
     * <li>取消使用优惠券</li>
     * <li>切换地址</li>
     * </ol>
     * 
     * <p>
     * 本方法并不对入参进行有效性校验，请在各商城端对其校验
     * </p>
     * 
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param key
     *            the key
     * @param orderForm
     *            the order form
     * @param bindingResult
     *            the binding result
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the string
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "/transaction/recalc", method = RequestMethod.POST)
     */
    public NebulaReturnResult recalc(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "key",required = false) String key, // 隐藏域中传递
                    @ModelAttribute("orderForm") OrderForm orderForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        CouponInfoSubForm couponInfoSubForm = orderForm.getCouponInfoSubForm();

        //----------------------------------------------------
        // 优惠券   
        String couponCode = null;
        if (couponInfoSubForm != null && isNotNullOrEmpty(couponInfoSubForm.getCouponCode())){
            couponCode = couponInfoSubForm.getCouponCode();
            //非用户绑定优惠券
            if (couponInfoSubForm.getId() == null){
                PromotionCouponCode promotionCouponCode = orderManager.validCoupon(couponCode);
                if (promotionCouponCode == null){
                    return toNebulaReturnResult(SalesOrderResult.ORDER_COUPON_NOT_AVALIBLE.toString());
                }
            }
        }

        //----------------------------------------------------
        ShoppingCartCommand shoppingCartCommand = shoppingCartCommandBuilder.buildShoppingCartCommandWithCheckStatus(memberDetails, key, orderForm, request);

        shoppingCartCommand = shoppingCartCommandShowBuilder.build(shoppingCartCommand);
        return toNebulaReturnResult(shoppingCartCommand);

    }

    //**************************************************************

    /**
     * To nebula return result.
     *
     * @param key
     *            the key
     * @return the nebula return result
     */
    private NebulaReturnResult toNebulaReturnResult(String key){
        DefaultReturnResult result = new DefaultReturnResult();
        result.setResult(false);
        result.setReturnObject(getMessage(key));
        return result;
    }

    /**
     * To nebula return result.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the nebula return result
     */
    private static NebulaReturnResult toNebulaReturnResult(ShoppingCartCommand shoppingCartCommand){
        DefaultReturnResult result = new DefaultReturnResult();
        result.setResult(true);
        result.setReturnObject(shoppingCartCommand);
        return result;
    }
}
