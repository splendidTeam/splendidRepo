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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.NebulaReturnResult;

/**
 * 
 * <p>
 * 主要由以下方法组成:
 * </p>
 *
 * 
 * @author feilong
 * @since 5.3.1
 */
public class NebulaImmediatelyBuyShoppingCartController extends NebulaAbstractImmediatelyBuyShoppingCartController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaImmediatelyBuyShoppingCartController.class);

    /**
     * (立即购买)不走普通购物车直接走购物通道.
     *
     * @param memberDetails
     *            某个用户
     * @param skuId
     *            the sku id
     * @param count
     *            买几件
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/transaction/immediatelybuy", method = RequestMethod.POST)
     */
    public NebulaReturnResult immediatelyBuyBundle(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "skuId",required = true) Long skuId,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        //TODO feilong validator

        //TODO feilong 构造购物车信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = buildShoppingCartLineCommandList(skuId, count);
        String key = autoKeyAccessor.save((Serializable) shoppingCartLineCommandList, request);

        String checkoutUrl = buildCheckoutUrl(key, request);

        //成功需要返回 跳转到订单确认页面的地址
        //失败就直接返回失败的信息
        //return toNebulaReturnResult(shoppingcartResult);
        return null;
    }

    /**
     * 
     * @param count
     * @param skuId
     * @since 5.3.1
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#addShoppingCart(MemberDetails, Long, Integer,
     *      HttpServletRequest, HttpServletResponse)
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#buildShoppingCartLineCommand(Long, Integer,
     *      String)
     */
    //TODO
    private List<ShoppingCartLineCommand> buildShoppingCartLineCommandList(Long skuId,Integer count){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setQuantity(count);

        shoppingCartLineCommandList.add(shoppingCartLineCommand);
        return shoppingCartLineCommandList;
    }
}
