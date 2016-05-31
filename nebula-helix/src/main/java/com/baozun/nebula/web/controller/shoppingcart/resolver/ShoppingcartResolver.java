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
 * <h3>主要定义以下接口或者操作:</h3>
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #getShoppingCartLineCommandList(MemberDetails,HttpServletRequest ) getShoppingCartLineCommandList}</td>
 * <td>获得指定用户的购物车list(所有的包括选中的及没有选中的).</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #addShoppingCart(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse) addShoppingCart}</td>
 * <td>将特定的skuid,指定的数量count,加入到用户的购物车里面去.</td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #updateShoppingCartCount(MemberDetails, Long, Integer, HttpServletRequest, HttpServletResponse) updateShoppingCartCount}</td>
 * <td>更新指定的购物车行shoppingcartLineId的数量count.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse) deleteShoppingCartLine}</td>
 * <td>删除某个用户的某个特定 shoppingcartLineId 的购物车行.</td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>{@link #toggleShoppingCartLineCheckStatus(MemberDetails, Long, boolean, HttpServletRequest, HttpServletResponse)
 * toggleShoppingCartLineCheckStatus}</td>
 * <td>切换指定购物车行的选中状态.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #toggleAllShoppingCartLineCheckStatus(MemberDetails, boolean, HttpServletRequest, HttpServletResponse)
 * toggleAllShoppingCartLineCheckStatus}</td>
 * <td>切换所有购物车行的选中状态.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
public interface ShoppingcartResolver{

    /**
     * 获得指定用户的购物车list(所有的包括选中的及没有选中的).
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param request
     *            the request
     * @return 如果指定的用户的购物车是空的,那么返回null
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request);

    //***************************************************************************************
    /**
     * 将特定的<code>skuid</code>,指定的数量<code>count</code>,加入到用户的购物车里面去.
     * 
     * <h3>流程:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/添加到购物车.png"/>
     * </p>
     * 
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param skuId
     *            相关skuId
     * @param count
     *            数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
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
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param count
     *            全量数量
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
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
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行id,以前可能直接通过skuid来进行操作,现在用户的购物车可能相同的skuid存在不同的购物车行里面(比如bundle)
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult deleteShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 切换指定购物车行的选中状态.
     * 
     * <p>
     * 参数 <code>checkStatus</code>,用来标识选中还是不选中,true为将当前行选中,false为将当前行不选中
     * </p>
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param shoppingcartLineId
     *            指定的购物车行
     * @param checkStatus
     *            true为将当前行选中,false为将当前行不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult toggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 切换所有购物车行的选中状态.
     * 
     * <p>
     * 参数 <code>checkStatus</code>,用来标识选中还是不选中,true为将当前行选中,false为将当前行不选中
     * </p>
     *
     * @param memberDetails
     *            memberDetails,通常实现只需要使用memberid,传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
     * @param checkStatus
     *            true为将当前行选中,false为将当前行不选中
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果成功 返回 {@link ShoppingcartResult#SUCCESS},<br>
     *         其他 返回 {@link ShoppingcartResult}其他枚举
     */
    ShoppingcartResult toggleAllShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response);

}
