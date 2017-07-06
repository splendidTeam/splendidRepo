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
package com.baozun.nebula.web.controller.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.factory.ImmediatelyBuyShoppingCartLineCommandListFactory;
import com.baozun.nebula.web.controller.shoppingcart.form.CommonImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResultUtil;
import com.baozun.nebula.web.controller.shoppingcart.validator.CommonImmediatelyBuyFormValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;

/**
 * 最常见的立即购买.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class NebulaImmediatelyBuyShoppingCartController extends NebulaAbstractImmediatelyBuyShoppingCartController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaImmediatelyBuyShoppingCartController.class);

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /** The immediately buy shopping cart line command list factory. */
    @Autowired
    private ImmediatelyBuyShoppingCartLineCommandListFactory immediatelyBuyShoppingCartLineCommandListFactory;

    @Autowired
    @Qualifier("commonImmediatelyBuyFormValidator")
    private CommonImmediatelyBuyFormValidator commonImmediatelyBuyFormValidator;

    /**
     * (立即购买)不走普通购物车直接走购物通道.
     *
     * @param memberDetails
     *            某个用户
     * @param commonImmediatelyBuyForm
     *            the common immediately buy form
     * @param bindingResult
     *            the binding result
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @NeedLogin(guest = true)
     * @RequestMapping(value = "/transaction/immediatelybuy", method = RequestMethod.POST)
     */
    public NebulaReturnResult immediatelyBuy(
                    @LoginMember MemberDetails memberDetails,
                    @ModelAttribute("commonImmediatelyBuyForm") CommonImmediatelyBuyForm commonImmediatelyBuyForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        commonImmediatelyBuyFormValidator.validate(commonImmediatelyBuyForm, bindingResult);

        if (bindingResult.hasErrors()){
            return super.getResultFromBindingResult(bindingResult);
        }

        Sku sku = sdkSkuManager.findSkuById(commonImmediatelyBuyForm.getSkuId());

        ShoppingcartResult shoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, commonImmediatelyBuyForm.getCount());

        if (ShoppingcartResultUtil.isNotSuccess(shoppingcartResult)){
            return toNebulaReturnResult(shoppingcartResult);
        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = immediatelyBuyShoppingCartLineCommandListFactory.buildShoppingCartLineCommandList(commonImmediatelyBuyForm, request);
        String key = saveToAccessor(shoppingCartLineCommandList, request);

        // 跳转到订单确认页面的地址
        String checkoutUrl = getImmediatelyBuyCheckoutUrl(key, request);
        return toNebulaReturnResult(checkoutUrl);
    }

    /**
     * @param shoppingcartResult
     */
    private NebulaReturnResult toNebulaReturnResult(ShoppingcartResult shoppingcartResult){
        DefaultReturnResult result = new DefaultReturnResult();
        //  失败就直接返回失败的信息
        result.setResult(false);
        String messageStr = getMessage(shoppingcartResult.toString());
        DefaultResultMessage message = new DefaultResultMessage();
        message.setMessage(messageStr);
        result.setResultMessage(message);
        LOGGER.error(messageStr);

        return result;
    }
}
