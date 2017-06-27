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
 * 此类存放购物车通用且无需大改造的方法:
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * 
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse, Model) addShoppingCart}</td>
 * <td>将指定的sku和数量加入购物车</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse, Model) deleteShoppingCartLine}</td>
 * <td>删除指定的购物车行</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse, Model) updateShoppingCartCount}</td>
 * <td>修改指定购物车行数量</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #toggleShoppingCart(MemberDetails, Long, boolean, HttpServletRequest, HttpServletResponse, Model) toggleShoppingCart}</td>
 * <td>修改指定购物车行选中状态.</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #toggleAllShoppingCartLineCheckStatus(MemberDetails, boolean, HttpServletRequest, HttpServletResponse, Model) toggleAllShoppingCartLineCheckStatus}</td>
 * <td>全选全不选</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * </p>
 * 
 * <h3>Nebula常用的购物车体系:</h3>
 * <blockquote>
 * 目前Nebula常用的购物车体系(即会到购物车页面)分为:
 * 
 * <ol>
 * <li>普通购物车 即 {@link NebulaShoppingCartController}</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @since 5.3.2.13
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
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * 
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

    /**
     * 批量添加购物车.
     * 
     * <p>
     * 用户购买选定的sku 数组,指定数量加入到购物车
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param skuIds
     *            指定的skus
     * @param count
     *            数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @RequestMapping(value = "/shoppingcart/addbatch", method = RequestMethod.POST)
     * @see <a href="http://jira.baozun.cn/browse/NB-708">购物车批量添加</a>
     * @since 5.3.2.18
     */
    public NebulaReturnResult addShoppingCartBatch(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "skuIds",required = true) Long[] skuIds,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.addShoppingCart(memberDetails, skuIds, count, request, response);
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
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * 
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
     * <span style="color:red">服务端必须同时拿shoppingcartLineId和groupId做参数,否则可能会出现安全漏洞</span>
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
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @RequestMapping(value = "/shoppingcart/delete", method =RequestMethod.POST)
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

    /**
     * 删除购物车行.
     * <p>
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为将来会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <br>
     * 比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分
     * 
     * <br>
     * <span style="color:red">服务端必须同时拿shoppingcartLineId和groupId做参数,否则可能会出现安全漏洞</span>
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineIds
     *            the shoppingcartline id
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @RequestMapping(value = "/shoppingcart/batchdelete", method =RequestMethod.POST)
     */
    public NebulaReturnResult batchDeleteShoppingCartLine(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "shoppingcartLineIds",required = true) Long[] shoppingcartLineIds,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    @SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.deleteShoppingCartLine(memberDetails, shoppingcartLineIds, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    /**
     * 清空购物车.
     * 
     * @param memberDetails
     * @param request
     * @param response
     * @param model
     * @return
     * @since 5.3.2.14
     */
    public NebulaReturnResult clearShoppingCartLine(@LoginMember MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response,@SuppressWarnings("unused") Model model){
        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult shoppingcartResult = shoppingcartResolver.clearShoppingCartLine(memberDetails, request, response);
        return toNebulaReturnResult(shoppingcartResult);
    }

    //************************************选中不选中**********************************************************
    /**
     * 修改用户的购物车选中状态.
     * 
     * <h3>关于shoppingcartLineId参数:</h3>
     * <blockquote>
     * 
     * 注意,此处参数设计为shoppingcartLineId 而不是 skuid,因为会出现 一个用户购物车里面会出现相同的sku,
     * 
     * <ol>
     * <li>比如一个属于bundle 一个属于单买的;或者 一个是购买的, 一个属于赠品;将来需要区分</li>
     * <li>再如购买同样的SKU鞋子,一个是定制商品有包装信息(比如reebok鞋子刻字), 一个不需要刻字是普通商品;将来需要区分</li>
     * </ol>
     * <ol>
     * 
     * </blockquote>
     * 
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            指定的购物车行
     * @param checkStatus
     *            选中不选中状态, true为将当前行选中,false为将当前行不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @RequestMapping(value = "/shoppingcart/toggleshoppingcart", method =RequestMethod.POST)
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
     *            用来标识选中还是不选中,true为将全部选中,false为全部不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return 如果操作成功返回 {@link DefaultReturnResult#SUCCESS},否则会基于{@link ShoppingcartResult} 构造 {@link DefaultReturnResult} 并返回
     * @RequestMapping(value = "/shoppingcart/toggleall", method =RequestMethod.POST)
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
