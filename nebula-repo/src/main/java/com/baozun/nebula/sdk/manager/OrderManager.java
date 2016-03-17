package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

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
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface OrderManager extends BaseManager{

	// 通过商品清单下订单
	public String saveOrder(ShoppingCartCommand shoppingCartCommand, SalesOrderCommand salesOrderCommand,
			Set<String> memCombos);

	// 手工下订单
	public String saveManualOrder(ShoppingCartCommand shoppingCartCommand, SalesOrderCommand salesOrderCommand);

	// 通过订单号查询订单(sdk)
	public SalesOrderCommand findOrderByCode(String code, Integer type);

	// 通过多个条件筛选订单(sdk)
	public Pagination<SalesOrderCommand> findOrders(Page page, Sort[] sorts, Map<String, Object> searchParam);

	// 通过多个条件筛选订单(sdk)
	public List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts, Map<String, Object> searchParam);

	// 支付订单 通过支付明细修改订单
	public PayInfoCommand savePayOrder(String code, PayInfoCommand payInfoCommand);

	// 修改订单支付状态，如已付款
	public SalesOrderCommand updateOrderFinancialStatus(String code, Integer financialStatus);

	// 通过商品清单，返回商品价格及总金额
	public BigDecimal findOrderAmount(List<OrderLineCommand> orderLineCommands);

	// 收到货后，申请退换货
	public Integer saveReturnOrder(ReturnOrderCommand returnOrderCommand);

	// 申请取消订单
	public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand);

	// 通过订单号来修改物流状态，如已发货，客户签收
	public Integer updateOrderLogisticsStatus(String code, Integer logisticsStatus);

	// 通过订单号来修改物流状态 取消订单使用
	public Integer cancelOrderLogisticsStatus(String code, Integer logisticsStatus);
	
	// 通过订单号来修改物流状态 取消订单使用, 根据isOms决定是否需要插入待发送消息表
	public Integer cancelOrderLogisticsStatus(String code, Integer logisticsStatus, Boolean isOms);

	// 根据综合筛选条件分页查询申请退换货的列表(sdk)
	public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> searchParam);

	// 根据综合筛选条件分页查询申请修改订单的列表(sdk)
	public Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> searchParam);

	// 根据OMS反馈的结果修改申请状态(取消订单)
	public Integer updateCancelOrders(Long handleId, String code, Integer status, String feedback);

	// 根据OMS反馈的结果修改申请状态(退换货)
	public Integer updateReturnOrders(Long handleId, String code, Integer status, String feedback);

	// 更改订单的收货人、发票、收货时间等，一般由OMS反馈过来
	public Integer updateOrders(SalesOrderCommand salesOrderCommand);

	// 获取订单条数
	public Integer findCountOfOrder(List<Integer> status, Long memberId);

	/**
	 * 客服下单->添加商品 查询商品及sku信息
	 * 
	 * @param page
	 * @param sorts
	 * @return
	 */
	public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap);

	/**
	 * 分页查询未评价的订单行
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap);

	/**
	 * 更新订单行的评价状态
	 * 
	 * @param skuId
	 * @param orderId
	 * @return
	 */
	public Integer updateOrderLineEvaulationStatus(Long skuId, Long orderId);

	// 通过订单号查询订单(sdk)
	public SalesOrderCommand findOrderById(Long id, Integer type);

	/**
	 * 根据会员ID查询已申请的取消订单列表
	 * 
	 * @param page
	 * @param memberId
	 * @return
	 */
	public Pagination<CancelOrderCommand> findCanceledOrderList(Page page, Long memberId);

	/**
	 * 根据会员ID查询已申请的退换货列表
	 * 
	 * @param page
	 * @param memberId
	 * @return
	 */
	public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page, Long memberId);

	/**
	 * 申请退换货
	 * 
	 * @param returnOrderCommand
	 * @return
	 */
	public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand);

	/**
	 * 根据订单行ID查询订单
	 * 
	 * @param orderLineId
	 * @return
	 */
	public SalesOrderCommand findOrderByLineId(Long orderLineId);

	// 修改订单支付状态，如已付款
	public void updateOrderFinancialStatusById(List<Long> orderIds, Integer financialStatus);

	/**
	 * 根据订单id查询订单明细
	 * 
	 * @param orderId
	 * @return
	 */
	public List<OrderLineCommand> findOrderDetailList(Long orderId);

	/**
	 * 通过用户Id查询用户未付款订单
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts, Long memberId);

	/**
	 * 查询符合取消条件的订单（非COD，未付款，时间大于xx分钟）
	 * 
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts, Map<String, Object> searchParam);

	/**
	 * 查找订单优惠
	 * 
	 * @param orderCode
	 * @return
	 */
	public List<OrderPromotionCommand> findOrderPormots(String orderCode);

	/**
	 * 验证优惠券是否存在
	 * 
	 * @param couponCode
	 * @return
	 */
	public PromotionCouponCode validCoupon(String couponCode);

	/**
	 * 发送订单邮件
	 */
	public void sendEmailOfOrder(String code, String emailTemplete);

	/**
	 * 根据Id查询订单状态变更日志
	 * 
	 * @param id
	 */
	public OrderStatusLogCommand findOrderStatusLogById(Long id);

	/**
	 * 根据创建时间和订单行中的extentionList查找对应的订单信息
	 * 
	 * @param extentionList
	 * @param minOpTime
	 * @return
	 */
	public List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(List<String> extentionList,
			Date minOpTime);

	/**
	 * 根据订单状态和extentionList查找对应的订单信息
	 * 
	 * @param extentionList
	 * @param orderStatus
	 * @return
	 */
	public List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(List<String> extentionList,
			List<Integer> orderStatus);

	/**
	 * 根据订单id列表获取订单行记录
	 * 
	 * @param orderIdList
	 * @return
	 */
	public List<OrderLineCommand> findOrderDetailListByOrderIds(List<Long> orderIdList);

	/**
	 * 更新物流信息
	 * 
	 * @param orderCode
	 * @param actualFreight
	 * @param logisticsProviderCode
	 * @param logisticsProviderName
	 * @param transCode
	 * @param date
	 */
	public void updateLogisticsInfo(String orderCode, BigDecimal actualFreight, String logisticsProviderCode,
			String logisticsProviderName, String transCode, Date date);

	/**
	 * 查询coupon
	 * 
	 * @param paraMap
	 * @return
	 */
	public List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(Map<String, Object> paraMap);

	// 获取所有的物流方式
	public List<DistributionMode> getAllDistributionMode();
}
