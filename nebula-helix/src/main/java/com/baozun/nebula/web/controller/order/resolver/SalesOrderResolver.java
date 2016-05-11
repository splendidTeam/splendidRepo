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
package com.baozun.nebula.web.controller.order.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.OrderForm;

/**
 * 订单操作处理器.
 * 
 * <h3>主要定义以下接口或者操作:</h3> <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>
 * {@link #buildSalesOrderCommand(MemberDetails memberDetails, OrderForm orderForm, HttpServletRequest request)
 * buildeSalesOrderCommand}</td>
 * <td>封装订单信息</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>
 * {@link #buildShoppingCartForOrder(MemberDetails memberDetails,SalesOrderCommand salesOrderCommand, HttpServletRequest request)
 * buildeShoppingCartForOrder}</td>
 * <td>获得用户的购物车信息</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #getSalesOrderCommand(String subOrdinate) getSalesOrderCommand}
 * </td>
 * <td>通过支付流水号获取订单信息</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>
 * {@link #updateCookieShoppingcart(List, HttpServletRequest, HttpServletResponse) updateCookieShoppingcart}
 * </td>
 * <td>游客下单成功后更新cookie中的购物车数据</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>
 * {@link #updateCookieShoppingcartCount(MemberDetails memberDetails, HttpServletRequest request, HttpServletResponse response) updateCookieShoppingcartCount}
 * </td>
 * <td>游客下单成功后更新cookie中的购物车商品数量</td>
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
public interface SalesOrderResolver {

	/**
	 * 封装订单信息(所有的包括选中的及没有选中的).
	 *
	 * @param memberDetails
	 *            memberDetails,通常实现只需要使用memberid,
	 *            传入memberDetails一来便于controller调用,二来可能实现类需要记录一些日志,可以用到其他字段
	 * @param orderForm
	 *            orderForm,页面上传过来的表单对象，数据的来源
	 * @param request
	 *            the request
	 * @return 返回封装之后的SalesOrderCommand
	 */
	SalesOrderCommand buildSalesOrderCommand(MemberDetails memberDetails, OrderForm orderForm,
			HttpServletRequest request);

	/**
	 * 获取订单所需购物车信息
	 * 
	 * @param memberDetails
	 * @param salesOrderCommand
	 * @param request
	 * @return
	 */
	ShoppingCartCommand buildShoppingCartForOrder(MemberDetails memberDetails, SalesOrderCommand salesOrderCommand,
			HttpServletRequest request);

	/**
	 * 通过支付流水号查询订单
	 * 
	 * @param subOrdinate
	 * @return
	 */
	SalesOrderCommand getSalesOrderCommand(String subOrdinate);

	/**
	 * 下单成功后更新cookie中的购物车数据
	 * 
	 * @param ShoppingCartLineCommandList
	 * @param request
	 * @param response
	 */
	void updateCookieShoppingcart(List<ShoppingCartLineCommand> ShoppingCartLineCommandList, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 下单成功后更新cookie中的购物车商品的数量
	 * 
	 * @param ShoppingCartLineCommandList
	 * @param request
	 * @param response
	 */
	void updateCookieShoppingcartCount(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response);
}
