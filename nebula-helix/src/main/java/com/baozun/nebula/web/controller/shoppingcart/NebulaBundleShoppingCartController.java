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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.feilong.core.util.RandomUtil;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaBundleShoppingCartController.class);

    /**
     * (立即购买)不走普通购物车直接走购物通道.
     *
     * @param memberDetails
     *            某个用户
     * @param relatedItemId
     *            买的哪个bundle,本来想用 bundleId的,后来听程哥说 他们定义的很多接口都是用的itemId
     * @param skuIds
     *            里面有哪些skuid 的组合
     * @param count
     *            买几套bundle
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/transaction/immediatelybuybundle", method = RequestMethod.POST)
     */
    public NebulaReturnResult immediatelyBuyBundle(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "relatedItemId",required = true) Long relatedItemId,//听说bundle目前封装的 都是使用itemId做参数
                    @RequestParam(value = "skuIds",required = true) Long[] skuIds,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        //TODO feilong validator

        //TODO feilong 构造bundle购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = null;
        String key = autoKeyAccessor.save((Serializable) shoppingCartLineCommandList, request);

        String checkoutUrl = buildCheckoutUrl(key, request);

        //成功需要返回 跳转到订单确认页面的地址
        //失败就直接返回失败的信息
        //return toNebulaReturnResult(shoppingcartResult);
        return null;
    }

    //TODO feilong 进普通购物车

}
