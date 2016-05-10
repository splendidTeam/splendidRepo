package com.baozun.nebula.manager.salesorder;

import java.util.List;
import java.util.Set;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import com.baozun.nebula.command.promotion.PromotionCouponInfoCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.coupon.CouponType;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.command.OrderQueryCommand;

public interface SalesOrderManager extends BaseManager {

	public String createOrder(SalesOrderCommand salesOrderCommand, Set<String> memComboIds, List<ShoppingCartLineCommand> lines);

	public PromotionCouponCode validCoupon(String couponCode);

	public List<OrderLineCommand> findOrderLineList(String skuId, String count, String ids);

	public CouponType findCouponTypeByCouponNo(String couponNo);

	public Integer immediatelyBuy(Long userId, Set<String> membComboIds, ShoppingCartLineCommand shoppingCartLineCommand, List<ShoppingCartLineCommand> lines);

	// public ShoppingCartCommand findShoppingCartList(Long memberId,Integer
	// callType,HttpServletRequest request,List<String> coupons);

	public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand);

	public Pagination<CancelOrderCommand> findCanceledOrderList(Page page, Long memberId);

	public SalesOrderCommand findOrderByCode(String code, Integer type);

	public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page, Long memberId);

	public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand);

	public SalesOrderCommand findOrderByLineId(Long orderLineId);

	/**
	 * 查询未付款的订单
	 * 
	 * @param page
	 * @param sorts
	 * @param memberId
	 * @return
	 */
	public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts, Long memberId);

	public List<OrderPromotionCommand> findOrderPormots(String orderCode);

	/**
	 * 根据优惠券编码查询符合要求的优惠券信息
	 * 
	 * @param couponCode
	 * @return
	 */
	public PromotionCouponInfoCommand findCouponInfoByCouponCode(String couponCode);
        /**
         * 
         * 说明：根据orderQueryForm查询订单分页列表
         * @param page
         * @param orderQueryForm
         * @param memberId
         * @return
         * @author 张乃骐
         * @time：2016年5月9日 下午2:43:26
         */
        public Pagination<SimpleOrderCommand> finorderByOrderQueryForm(Page page, OrderQueryCommand orderQueryForm,
            Long memberId);
}
