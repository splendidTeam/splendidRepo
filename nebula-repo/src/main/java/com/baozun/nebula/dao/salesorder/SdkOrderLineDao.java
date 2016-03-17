/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.dao.salesorder;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;

public interface SdkOrderLineDao extends GenericEntityDao<OrderLine, Long> {

	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findOrderDetailList(@QueryParam("orderId") Long orderId);

	/**
	 * 根据订单id集合查询订单行记录
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findOrderDetailListByOrderIds(@QueryParam("orderIds") List<Long> orderIds);

	/**
	 * 根据订单行id集合查询订单行集合
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findOrderDetailListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 更新订单行的评价状态
	 * 
	 * @param code
	 * @param state
	 * @param modifyTime
	 * @return
	 */
	@NativeUpdate
	Integer updateOrderLineEvaulationStatus(@QueryParam("skuId") Long skuId, @QueryParam("orderId") Long orderId);

	/**
	 * 检查商品交易是否完成根据用户名和商品id
	 * 
	 * @param memberId
	 * @param ItemId
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findOrderLineCompletionByItemIdOrUserId(@QueryParam("memberId") Long memberId, @QueryParam("itemId") Long itemId);

	/***
	 * 根据订单行id更新订单行的评价状态
	 * 
	 * @param skuId
	 * @param orderId
	 * @return
	 */
	@NativeUpdate
	Integer updateOrderLineEvaulationStatusByOrderLineid(@QueryParam("skuId") Long skuId, @QueryParam("orderId") Long orderId);

	/**
	 * 根据订单id查询订单行信息
	 * 
	 * @param orderId
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	public List<OrderLineCommand> findOrderLinesByOrderId(@QueryParam("orderId") Long orderId);

	/**
	 * 查询所有订单详细信息
	 * 
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findAllOrderLines();

	/**
	 * 该sku下单的购买总数量
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findOrderLinesCountBySkuId(@QueryParam("skuId") Long skuId, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 该item下单的购买总数量
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findOrderLinesCountByItemId(@QueryParam("itemId") Long itemId, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据itemIds查询下单的购买行总数量
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findOrderLinesCountByItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据categoryId查询下单的购买行总数量
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findOrderLinesCountByCategoryId(@QueryParam("categoryId") Long categoryId, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据历史订单itemId查询订单次数
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountByItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据categoryId查询订单次数
	 * 
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountByCategoryId(@QueryParam("categoryId") Long categoryId, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据历史订单category和itemId查询订单次数
	 * 
	 * @param categoryId
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountItemQtyByCategoryAndItemIds(@QueryParam("categoryIds") List<Long> categoryIds, @QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据历史订单category查询订单次数
	 * 
	 * @param categoryId
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountItemQtyByCategory(@QueryParam("categoryIds") List<Long> categoryIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountItemQtyByItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据categoryIds\itemIds查询订单商品总数量
	 * 
	 * @param categoryIds
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderSumItemQtyByCategoryAndItemIds(@QueryParam("categoryIds") List<Long> categoryIds, @QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderSumItemQtyByCategory(@QueryParam("categoryIds") List<Long> categoryIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据itemIds查询历史下单的购买行总数量
	 * 
	 * @param itemIds
	 * @param shopId
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = OrderLine.class)
	List<OrderLine> findHistoryOrderLinesByItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 根据extentionCode和订单状态获取订单行
	 * 
	 * @param extentionList
	 * @param orderStatus
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	List<OrderLineCommand> findOrderLineByExtentionCodesAndOrderStatus(@QueryParam("extentionList") List<String> extentionList, @QueryParam("orderStatus") List<Integer> orderStatus);

	/**
	 * 根据订单的code信息获取订单行
	 * 
	 * @param orderCodeList
	 * @return
	 */
	List<OrderLineCommand> findOrderLinesByOrderCodes(@QueryParam("orderCodeList") List<String> orderCodeList);

	/**
	 * 查询所有历史订单行商品数量的集合，不包括参数传来的商品ids项
	 * 
	 * @param itemIds
	 *            不包括的商品ids
	 * @param shopId
	 *            店铺id
	 * @param memberId
	 *            用户id
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderLinesCountByNotItmeIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);

	/**
	 * 查询所有历史订单行数量的集合，不包括参数传来的商品ids项
	 * 
	 * @param itemIds
	 *            不包括的商品ids
	 * @param shopId
	 *            店铺id
	 * @param memberId
	 *            用户id
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "count")
	public Integer findHistoryOrderCountItemByNotItemIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("shopId") Long shopId, @QueryParam("memberId") Long memberId);
}
