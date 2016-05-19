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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;

/**
 * Order dao
 * 
 * @author chuanyang.zheng
 * @param <SimpleOrderCommand>
 */

public interface SdkOrderDao extends GenericEntityDao<SalesOrder, Long>{

	@NativeQuery(model = SalesOrderCommand.class)
	SalesOrderCommand findOrderByCode(@QueryParam("code") String code,@QueryParam("sdkQueryType") Integer type);
	
	@NativeUpdate
	Integer updateOrderLogisticsStatus(@QueryParam("code")String code,@QueryParam("state")Integer state,@QueryParam("modifyTime")Date modifyTime);
	
	@NativeUpdate
	Integer updateOrderFinancialStatus(@QueryParam("code")String code,@QueryParam("state")Integer state,@QueryParam("modifyTime")Date modifyTime);
	
	/**
	 * 查询订单列表
	 * @param page
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	Pagination<SalesOrderCommand> findOrders(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery(model = SalesOrderCommand.class)
	List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 查询未付款订单列表
	 * @param page
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	List<SalesOrderCommand> findNoPayOrders(Sort[] sorts, @QueryParam("memberId") Long memberId);
	
	/**
	 * 查询符合取消条件的订单（非COD，未付款，时间大于xx分钟）
	 * 2016-02-14 Fix  增加 FINANCIAL_STATUS = 4 即部分付款的
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts, @QueryParam Map<String, Object> paraMap);
	
	/**
	 * 客服下单->添加商品
	 * 查询商品及sku信息
	 * @param page
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = ItemSkuCommand.class)
	Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 根据订单id查询未评价的订单行
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = OrderLineCommand.class)
	public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 根据订单id查询订单
	 * @param id
	 * @param type
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	SalesOrderCommand findOrderById(@QueryParam("id") Long id,@QueryParam("sdkQueryType") Integer type);

	/**
	 * 根据会员ID查询已申请的取消订单列表
	 * @param page
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = CancelOrderCommand.class, pagable = true)
	Pagination<CancelOrderCommand> findCanceledOrderList(Page page, @QueryParam("memberId") Long memberId);

	/**
	 * 根据会员ID查询已申请的退换货列表
	 * @param page
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = ReturnOrderCommand.class, pagable = true)
	Pagination<ReturnOrderCommand> findReturnedOrderList(Page page, @QueryParam("memberId") Long memberId);

	/**
	 * 根据订单行ID查询订单
	 * @param orderLineId
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	SalesOrderCommand findOrderByLineId(@QueryParam("lineId") Long orderLineId);
	
	@NativeUpdate
	Integer updateOrderFinancialStatusById(@QueryParam("orderIds") List<Long> orderIds,@QueryParam("financialStatus")Integer financialStatus);
	
	@NativeQuery(alias="COUNT",clazzes = Integer.class)
	Integer findCountOfOrder(@QueryParam("status") List<Integer> status,@QueryParam("memberId")Long memberId);
	
	@NativeQuery(alias="COUNT",clazzes = Integer.class)
	Integer findCountOfOrderByShopId(@QueryParam("shopId")Long shopId,@QueryParam("memberId")Long memberId);
	
	@NativeQuery(alias="COUNT",clazzes = Integer.class)
	Integer findItemCountOfOrderByShopId(@QueryParam("shopId")Long shopId,@QueryParam("memberId")Long memberId);

	/**
	 * 根据订单code列表获取订单信息
	 * 
	 * @param orderCodeList
	 * @return
	 */
	List<SalesOrderCommand> findOrderByCodeList(@QueryParam("codeList")List<String> orderCodeList);

	/**
	 * 根据创建时间和订单行中的extentionList查找对应的订单信息
	 * 
	 * @param extentionList
	 * @param minOpTime
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(
			@QueryParam("extentionList")List<String> extentionList, @QueryParam("startTime")Date startTime);

	/**
	 * 根据订单状态和extentionList查找对应的订单信息
	 * 
	 * @param extentionList
	 * @param orderStatus
	 * @return
	 */
	@NativeQuery(model = SalesOrderCommand.class)
	List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(
			@QueryParam("extentionList")List<String> extentionList, @QueryParam("orderStatus")List<Integer> orderStatus);
	
	/**
	 * 获取订单号
	 * @return
	 */
	@NativeQuery(alias="CODESEQ",clazzes = Long.class)
	Long findCodeSeq();

	/**
	 * 生成流水号序列
	 * @return
	 */
	@NativeQuery(alias="SERIALNO",clazzes = Long.class)
	Long findOrderSerialNO();
	
	/**
	 * 修改物流信息
	 * 
	 * @param actualFreight
	 * @param express
	 * @param expressCodes
	 * @param date
	 */
	@NativeUpdate
	void updateLogisticsInfo(@QueryParam("orderCode") String orderCode,@QueryParam("actualFreight") BigDecimal actualFreight,
			@QueryParam("logisticsProviderCode") String logisticsProviderCode,
			@QueryParam("logisticsProviderName") String logisticsProviderName,
			@QueryParam("transCode")String transCode, @QueryParam("modifyTime")Date modifyTime);
	/**
	 * 
	 * 说明：根据order查询条件返回SimpleOrderCommand的分页
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 * @author 张乃骐
	 * @time：2016年5月9日 下午3:34:54
	 */
       @NativeQuery(model =SimpleOrderCommand.class)
       Pagination<SimpleOrderCommand> findSimpleOrderByOrderQueryCommand(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);
       
       /**
        * 
        * 说明：根据order查询条件返回SimpleOrderCommand的List 状态码判断为or
        * @param page
        * @param sorts
        * @param paraMap
        * @return
        * @author 张乃骐
        * @time：2016年5月9日 下午3:34:54
        */
      @NativeQuery(model =SimpleOrderCommand.class)
      List<SimpleOrderCommand> findSimpleOrderListByOrderQueryCommand(@QueryParam Map<String, Object> paraMap);
}
