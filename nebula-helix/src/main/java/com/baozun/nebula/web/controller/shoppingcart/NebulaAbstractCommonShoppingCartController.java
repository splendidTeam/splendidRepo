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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.factory.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;

/**
 * 购物车Base控制器.
 * 
 * <p>
 * 目前Nebula常用的购物车体系(即会到购物车页面)分为:
 * 
 * <ol>
 * <li>普通购物车 即 {@link NebulaShoppingCartController}</li>
 * </ol>
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @since 5.3.2.11-Personalise
 */
public class NebulaAbstractCommonShoppingCartController extends NebulaAbstractShoppingCartController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaAbstractCommonShoppingCartController.class);

    /** The shoppingcart factory. */
    @Autowired
    protected ShoppingcartFactory shoppingcartFactory;

    /**
     * 添加购物车.
     * 
     * <p>
     * 用户购买选定的sku,指定数量加入到购物车
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param skuId
     *            指定的sku
     * @param count
     *            数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/add", method = RequestMethod.POST)
     */
    public NebulaReturnResult addShoppingCart(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "skuId",required = true) Long skuId,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.addShoppingCart(memberDetails, skuId, count, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }
    //**********************************updateShoppingCartCount************************************************

    /**
     * 修改用户的购物车数量.
     * 
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param count
     *            最终数量值,而非 incr值
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/update", method = RequestMethod.POST)
     */
    public NebulaReturnResult updateShoppingCartCount(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = true) Long shoppingcartLineId,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.updateShoppingCartCount(memberDetails, shoppingcartLineId, count, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    //**********************************deleteShoppingCartLine************************************************
    /**
     * 删除购物车行.
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * 
     * <br>
     * <span style="color:red">服务端必须同时拿shoppingcartLineId和groupId做参数,
     * 否则可能会出现安全漏洞</span>
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/delete", method =
     *                       RequestMethod.POST)
     */
    public NebulaReturnResult deleteShoppingCartLine(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = true) Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.deleteShoppingCartLine(memberDetails, shoppingcartLineId, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    //************************************选中不选中**********************************************************
    /**
     * 修改用户的购物车选中状态.
     * 
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcartline id
     * @param checkStatus
     *            the check status
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/select", method =
     *                       RequestMethod.POST)
     */
    public NebulaReturnResult toggleShoppingCart(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineId",required = false) Long shoppingcartLineId,
                    @RequestParam(value = "checked",required = true) boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.toggleShoppingCartLineCheckStatus(memberDetails, shoppingcartLineId, checkStatus, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    /**
     * 全选全不选.
     *
     * @param memberDetails
     *            the member details
     * @param checkStatus
     *            the check status
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @RequestMapping(value = "/shoppingcart/select", method =
     *                       RequestMethod.POST)
     */
    public NebulaReturnResult toggleAllShoppingCartLineCheckStatus(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "checked",required = true) boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.toggleAllShoppingCartLineCheckStatus(memberDetails, checkStatus, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    /**
     * 返回结果填充.
     *
     * @param shoppingcartResult
     *            the shoppingcart result
     * @return the nebula return result
     */
    protected NebulaReturnResult toNebulaReturnResult(ShoppingcartResult shoppingcartResult){
        if (ShoppingcartResult.SUCCESS != shoppingcartResult){
            DefaultReturnResult result = new DefaultReturnResult();
            result.setResult(false);

            String messageStr = getMessage(shoppingcartResult.toString());

            DefaultResultMessage message = new DefaultResultMessage();
            message.setMessage(messageStr);
            result.setResultMessage(message);

            LOGGER.error(messageStr);
            return result;
        }
        return DefaultReturnResult.SUCCESS;
    }
}
