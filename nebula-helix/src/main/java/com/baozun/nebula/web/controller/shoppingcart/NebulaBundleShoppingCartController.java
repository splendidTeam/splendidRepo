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

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.factory.ImmediatelyBuyShoppingCartLineCommandListFactory;
import com.baozun.nebula.web.controller.shoppingcart.form.BundleImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.validator.ImmediatelyBuyBundleFormValidator;

/**
 * 基于bundle购物车控制器.
 * 
 * <p>
 * 主要由以下方法组成:
 * </p>
 *
 * 
 * @author feilong
 * @version 5.3.1 2016年5月4日 下午7:23:19
 * @see com.baozun.nebula.model.bundle.Bundle
 * @see com.baozun.nebula.model.bundle.BundleElement
 * @see com.baozun.nebula.model.bundle.BundleSku
 * @since 5.3.1
 */
public class NebulaBundleShoppingCartController extends NebulaAbstractImmediatelyBuyShoppingCartController{

    /** The Constant log. */
    private static final Logger                              LOGGER = LoggerFactory.getLogger(NebulaBundleShoppingCartController.class);

    /** The immediately buy bundle form validator. */
    @Autowired
    @Qualifier("immediatelyBuyBundleFormValidator")
    private ImmediatelyBuyBundleFormValidator                immediatelyBuyBundleFormValidator;

    /** The immediately buy shopping cart line command list factory. */
    @Autowired
    private ImmediatelyBuyShoppingCartLineCommandListFactory immediatelyBuyShoppingCartLineCommandListFactory;

    /**
     * (立即购买)不走普通购物车直接走购物通道.
     *
     * @param memberDetails
     *            某个用户
     * @param bundleImmediatelyBuyForm
     *            the bundle immediately buy form
     * @param bindingResult
     *            the binding result
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the nebula return result
     * @NeedLogin(guest = true)
     * @RequestMapping(value = "/transaction/immediatelybuybundle", method = RequestMethod.POST)
     */
    public NebulaReturnResult immediatelyBuyBundle(
                    @LoginMember MemberDetails memberDetails,
                    @ModelAttribute("bundleImmediatelyBuyForm") BundleImmediatelyBuyForm bundleImmediatelyBuyForm,
                    BindingResult bindingResult,
                    HttpServletRequest request,
                    Model model){

        immediatelyBuyBundleFormValidator.validate(bundleImmediatelyBuyForm, bindingResult);

        if (bindingResult.hasErrors()){
            return super.getResultFromBindingResult(bindingResult);
        }
        //TODO feilong validator
        //        ShoppingcartResult shoppingcartResult = null;
        //
        //        if (null != shoppingcartResult){
        //            return toNebulaReturnResult(shoppingcartResult);
        //        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = immediatelyBuyShoppingCartLineCommandListFactory
                        .buildShoppingCartLineCommandList(bundleImmediatelyBuyForm);
        String key = autoKeyAccessor.save((Serializable) shoppingCartLineCommandList, request);

        String checkoutUrl = buildCheckoutUrl(key, request);

        //成功需要返回 跳转到订单确认页面的地址
        //失败就直接返回失败的信息
        return toNebulaReturnResult(checkoutUrl);
    }

    //TODO feilong 构造bundle购物车信息

    /**
     * To nebula return result.
     *
     * @param shoppingcartResult
     *            the shoppingcart result
     * @return the nebula return result
     */
    private NebulaReturnResult toNebulaReturnResult(ShoppingcartResult shoppingcartResult){
        DefaultReturnResult result = new DefaultReturnResult();
        if (shoppingcartResult != null){
            result.setResult(false);

            String messageStr = getMessage(shoppingcartResult.toString());

            DefaultResultMessage message = new DefaultResultMessage();
            message.setMessage(messageStr);
            result.setResultMessage(message);

            LOGGER.error(messageStr);
        }
        return result;
    }

    /**
     * To nebula return result.
     *
     * @param checkoutUrl
     *            the checkout url
     * @return the nebula return result
     */
    private NebulaReturnResult toNebulaReturnResult(String checkoutUrl){
        DefaultReturnResult result = new DefaultReturnResult();
        result.setResult(true);
        DefaultResultMessage message = new DefaultResultMessage();
        message.setMessage(checkoutUrl);
        result.setResultMessage(message);
        return result;
    }

}
