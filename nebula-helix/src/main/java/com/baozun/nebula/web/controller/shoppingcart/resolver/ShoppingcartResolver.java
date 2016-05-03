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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * 购物车操作处理器.
 * 
 * 
 * <h3>主要定义以下接口或者操作:</h3>
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #getShoppingCartLineCommandList(MemberDetails,HttpServletRequest )}</td>
 * <td>获得指定用户的购物车list(所有的包括选中的及没有选中的).</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse)}</td>
 * <td></td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse)}</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse)}</td>
 * <td></td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #selectShoppingCartLine(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse)}</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td></td>
 * <td></td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 *
 * @author weihui.tang
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
public interface ShoppingcartResolver{

    /**
     * 获得指定用户的购物车list(所有的包括选中的及没有选中的).
     *
     * @param request
     *            the request
     * @param memberDetails
     *            the member details
     * @return 如果指定的用户的购物车是空的,那么返回null
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request);

    //***************************************************************************************
    /**
     * 将特定的<code>skuid</code>,指定的数量<code>count</code>,加入到用户的购物车里面去.
     *
     * @param memberDetails
     *            the member details
     * @param skuId
     *            相关skuId
     * @param count
     *            数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     */
    ShoppingcartResult addShoppingCart(
                    MemberDetails memberDetails,
                    Long skuId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 更新指定的购物车行<code>shoppingcartLineId</code>的数量<code>count</code>.
     * 
     * <p>
     * 注意,参数count是全量数量
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param count
     *            the count
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     */
    ShoppingcartResult updateShoppingCartCount(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 删除某个用户的某个特定 <code>shoppingcartLineId</code> 的购物车行.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     */
    ShoppingcartResult deleteShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 勾选购物车行.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingcartLineId
     *            the shoppingcart line id
     * @param settlementState
     *            the settlement state
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     */
    ShoppingcartResult selectShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    Integer settlementState,
                    HttpServletRequest request,
                    HttpServletResponse response);

}
