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
package com.baozun.nebula.sdk.manager.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * The Interface OrderManager.
 */
public interface OrderManager extends BaseManager{

    // 通过订单号查询订单(sdk)
    /**
     * Find order by code.
     *
     * @param code
     *            the code
     * @param type
     *            the type
     * @return the sales order command
     */
    public SalesOrderCommand findOrderByCode(String code,Integer type);

    // 通过多个条件筛选订单(sdk)
    /**
     * Find orders.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @param searchParam
     *            the search param
     * @return the pagination< sales order command>
     */
    public Pagination<SalesOrderCommand> findOrders(Page page,Sort[] sorts,Map<String, Object> searchParam);

    // 通过多个条件筛选订单(sdk)
    /**
     * Find orders with out page.
     *
     * @param sorts
     *            the sorts
     * @param searchParam
     *            the search param
     * @return the list< sales order command>
     */
    public List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts,Map<String, Object> searchParam);

    // 支付订单 通过支付明细修改订单
    /**
     * Save pay order.
     *
     * @param code
     *            the code
     * @param payInfoCommand
     *            the pay info command
     * @return the pay info command
     */
    public PayInfoCommand savePayOrder(String code,PayInfoCommand payInfoCommand);

    // 修改订单支付状态，如已付款
    /**
     * Update order financial status.
     *
     * @param code
     *            the code
     * @param financialStatus
     *            the financial status
     * @return the sales order command
     */
    public SalesOrderCommand updateOrderFinancialStatus(String code,Integer financialStatus);

    // 通过商品清单，返回商品价格及总金额
    /**
     * Find order amount.
     *
     * @param orderLineCommands
     *            the order line commands
     * @return the big decimal
     */
    public BigDecimal findOrderAmount(List<OrderLineCommand> orderLineCommands);

    // 收到货后，申请退换货
    /**
     * Save return order.
     *
     * @param returnOrderCommand
     *            the return order command
     * @return the integer
     * 
     */
    public Integer saveReturnOrder(ReturnOrderCommand returnOrderCommand);

    // 申请取消订单
    /**
     * Save cancel order.
     *
     * @param cancelOrderCommand
     *            the cancel order command
     * @return the integer
     */
    public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand);

    // 通过订单号来修改物流状态，如已发货，客户签收
    /**
     * Update order logistics status.
     *
     * @param code
     *            the code
     * @param logisticsStatus
     *            the logistics status
     * @return the integer
     */
    public Integer updateOrderLogisticsStatus(String code,Integer logisticsStatus);

    // 通过订单号来修改物流状态 取消订单使用
    /**
     * Cancel order logistics status.
     *
     * @param code
     *            the code
     * @param logisticsStatus
     *            the logistics status
     * @return the integer
     */
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus);

    // 通过订单号来修改物流状态 取消订单使用, 根据isOms决定是否需要插入待发送消息表
    /**
     * Cancel order logistics status.
     *
     * @param code
     *            the code
     * @param logisticsStatus
     *            the logistics status
     * @param isOms
     *            the is oms
     * @return the integer
     */
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus,Boolean isOms);

    // 根据综合筛选条件分页查询申请退换货的列表(sdk)
    /**
     * Find return orders by query map with page.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @param searchParam
     *            the search param
     * @return the pagination< return order command>
     */
    public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);

    // 根据综合筛选条件分页查询申请修改订单的列表(sdk)
    /**
     * Find cancel orders by query map with page.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @param searchParam
     *            the search param
     * @return the pagination< cancel order command>
     */
    public Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);

    // 根据OMS反馈的结果修改申请状态(取消订单)
    /**
     * Update cancel orders.
     *
     * @param handleId
     *            the handle id
     * @param code
     *            the code
     * @param status
     *            the status
     * @param feedback
     *            the feedback
     * @return the integer
     */
    public Integer updateCancelOrders(Long handleId,String code,Integer status,String feedback);

    // 根据OMS反馈的结果修改申请状态(退换货)
    /**
     * Update return orders.
     *
     * @param handleId
     *            the handle id
     * @param code
     *            the code
     * @param status
     *            the status
     * @param feedback
     *            the feedback
     * @return the integer
     */
    public Integer updateReturnOrders(Long handleId,String code,Integer status,String feedback);

    // 更改订单的收货人、发票、收货时间等，一般由OMS反馈过来
    /**
     * Update orders.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @return the integer
     */
    public Integer updateOrders(SalesOrderCommand salesOrderCommand);

    // 获取订单条数
    /**
     * Find count of order.
     *
     * @param status
     *            the status
     * @param memberId
     *            the member id
     * @return the integer
     */
    public Integer findCountOfOrder(List<Integer> status,Long memberId);

    /**
     * 客服下单->添加商品 查询商品及sku信息.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @param paraMap
     *            the para map
     * @return the pagination< item sku command>
     */
    public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

    /**
     * 分页查询未评价的订单行.
     *
     * @param page
     *            the page
     * @param sorts
     *            the sorts
     * @param paraMap
     *            the para map
     * @return the pagination< order line command>
     */
    public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

    /**
     * 更新订单行的评价状态.
     *
     * @param skuId
     *            the sku id
     * @param orderId
     *            the order id
     * @return the integer
     */
    public Integer updateOrderLineEvaulationStatus(Long skuId,Long orderId);

    // 通过订单号查询订单(sdk)
    /**
     * Find order by id.
     *
     * @param id
     *            the id
     * @param type
     *            the type
     * @return the sales order command
     */
    public SalesOrderCommand findOrderById(Long id,Integer type);

    /**
     * 根据会员ID查询已申请的取消订单列表.
     *
     * @param page
     *            the page
     * @param memberId
     *            the member id
     * @return the pagination< cancel order command>
     */
    public Pagination<CancelOrderCommand> findCanceledOrderList(Page page,Long memberId);

    /**
     * 根据会员ID查询已申请的退换货列表.
     *
     * @param page
     *            the page
     * @param memberId
     *            the member id
     * @return the pagination< return order command>
     */
    public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page,Long memberId);

    /**
     * 申请退换货.
     *
     * @param returnOrderCommand
     *            the return order command
     * @return the integer
     */
    public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand);

    /**
     * 根据订单行ID查询订单.
     *
     * @param orderLineId
     *            the order line id
     * @return the sales order command
     */
    public SalesOrderCommand findOrderByLineId(Long orderLineId);

    // 修改订单支付状态，如已付款
    /**
     * Update order financial status by id.
     *
     * @param orderIds
     *            the order ids
     * @param financialStatus
     *            the financial status
     */
    public void updateOrderFinancialStatusById(List<Long> orderIds,Integer financialStatus);

    /**
     * 根据订单id查询订单明细.
     *
     * @param orderId
     *            the order id
     * @return the list< order line command>
     */
    public List<OrderLineCommand> findOrderDetailList(Long orderId);

    /**
     * 通过用户Id查询用户未付款订单.
     *
     * @param sorts
     *            the sorts
     * @param memberId
     *            the member id
     * @return the list< sales order command>
     */
    public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts,Long memberId);

    /**
     * 查询符合取消条件的订单（非COD，未付款，时间大于xx分钟）.
     *
     * @param sorts
     *            the sorts
     * @param searchParam
     *            the search param
     * @return the list< sales order command>
     */
    public List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts,Map<String, Object> searchParam);

    /**
     * 查找订单优惠.
     *
     * @param orderCode
     *            the order code
     * @return the list< order promotion command>
     */
    public List<OrderPromotionCommand> findOrderPormots(String orderCode);

    /**
     * 验证优惠券是否存在.
     *
     * @param couponCode
     *            the coupon code
     * @return the promotion coupon code
     */
    public PromotionCouponCode validCoupon(String couponCode);

    /**
     * 发送订单邮件.
     *
     * @param code
     *            the code
     * @param emailTemplete
     *            the email templete
     */
    public void sendEmailOfOrder(String code,String emailTemplete);

    /**
     * 根据Id查询订单状态变更日志.
     *
     * @param id
     *            the id
     * @return the order status log command
     */
    public OrderStatusLogCommand findOrderStatusLogById(Long id);

    /**
     * 根据创建时间和订单行中的extentionList查找对应的订单信息.
     *
     * @param extentionList
     *            the extention list
     * @param minOpTime
     *            the min op time
     * @return the list< sales order command>
     */
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(List<String> extentionList,Date minOpTime);

    /**
     * 根据订单状态和extentionList查找对应的订单信息.
     *
     * @param extentionList
     *            the extention list
     * @param orderStatus
     *            the order status
     * @return the list< sales order command>
     */
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(List<String> extentionList,List<Integer> orderStatus);

    /**
     * 根据订单id列表获取订单行记录.
     *
     * @param orderIdList
     *            the order id list
     * @return the list< order line command>
     */
    public List<OrderLineCommand> findOrderDetailListByOrderIds(List<Long> orderIdList);

    /**
     * 更新物流信息.
     *
     * @param orderCode
     *            the order code
     * @param actualFreight
     *            the actual freight
     * @param logisticsProviderCode
     *            the logistics provider code
     * @param logisticsProviderName
     *            the logistics provider name
     * @param transCode
     *            the trans code
     * @param date
     *            the date
     */
    public void updateLogisticsInfo(
                    String orderCode,
                    BigDecimal actualFreight,
                    String logisticsProviderCode,
                    String logisticsProviderName,
                    String transCode,
                    Date date);

    /**
     * 查询coupon.
     *
     * @param paraMap
     *            the para map
     * @return the list< promotion coupon code>
     */
    public List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(Map<String, Object> paraMap);

    // 获取所有的物流方式
    /**
     * 获得 all distribution mode.
     *
     * @return the all distribution mode
     */
    public List<DistributionMode> getAllDistributionMode();
    
	/**
	 * 
	 * @Description 根据订单号或者下单时下单者手机号查询订单信息
	 * @param mobile
	 * @param code
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-9-13
	 */
	List<SalesOrderCommand> findOrderByMobileOrCode(String mobile,String code);
}
