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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.constants.MetaInfoConstants;
import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.sdk.utils.BankCodeConvertUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.form.PaymentInfoSubForm;
import com.baozun.nebula.web.controller.shoppingcart.persister.GuestShoppingcartPersister;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.predicate.MainLinesPredicate;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class AbstractShoppingcartResolver.
 *
 * @author weihui.tang
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
@Component
public class SalesOrderResolverImpl implements SalesOrderResolver {

	/** The Constant log. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderResolverImpl.class);

	@Autowired
	private MataInfoManager mataInfoManager;

	@Autowired
	private SalesOrderManager salesOrderManager;

	/** The guest shoppingcart resolver. */
	@Autowired
	@Qualifier("guestShoppingcartResolver")
	private ShoppingcartResolver guestShoppingcartResolver;

	/** The member shoppingcart resolver. */
	@Autowired
	@Qualifier("memberShoppingcartResolver")
	private ShoppingcartResolver memberShoppingcartResolver;

	/** The sdk shopping cart manager. */
	@Autowired
	private SdkShoppingCartManager sdkShoppingCartManager;

	@Autowired
	private SdkPaymentManager sdkPaymentManager;

	@Autowired
	private OrderManager orderManager;

	@Autowired
	private GuestShoppingcartPersister guestShoppingcartPersister;

	@Autowired
	private ShoppingcartCountPersister shoppingcartCountPersister;

	@Override
	public SalesOrderCommand buildSalesOrderCommand(MemberDetails memberDetails, OrderForm orderForm,
			HttpServletRequest request) {
		// 需要封装的对象
		SalesOrderCommand salesOrderCommand = new SalesOrderCommand();
		// 设置收货地址信息
		PropertyUtil.copyProperties(salesOrderCommand, orderForm.getShippingInfoSubForm(), "countryId", "provinceId",
				"cityId", "areaId", "townId");
		salesOrderCommand.setBuyerName(orderForm.getShippingInfoSubForm().getBuyerName());
		salesOrderCommand.setBuyerTel(orderForm.getShippingInfoSubForm().getBuyerTel());
		// 设置支付信息
		PaymentInfoSubForm paymentInfoSubForm = orderForm.getPaymentInfoSubForm();
		salesOrderCommand.setPayment(Integer.parseInt(paymentInfoSubForm.getPaymentType()));
		salesOrderCommand.setPaymentStr(BankCodeConvertUtil.getPayTypeDetail(paymentInfoSubForm.getBankcode(),
				Integer.parseInt(paymentInfoSubForm.getPaymentType())));
		// 设置运费
		setFreghtCommand(salesOrderCommand);
		// 设置优惠券信息
		setCoupon(salesOrderCommand, orderForm.getCouponInfoSubForm().getCouponCode());
		// 用户信息
		salesOrderCommand.setMemberName(memberDetails == null ? "" : memberDetails.getLoginName());
		salesOrderCommand.setMemberId(memberDetails == null ? null : memberDetails.getMemberId());
		salesOrderCommand.setIp(RequestUtil.getClientIp(request));
		// 发票信息
		salesOrderCommand.setReceiptTitle(orderForm.getInvoiceInfoSubForm().getInvoiceTitle());
		salesOrderCommand.setReceiptContent(orderForm.getInvoiceInfoSubForm().getInvoiceContent());
		salesOrderCommand.setReceiptType(orderForm.getInvoiceInfoSubForm().getInvoiceType());
		salesOrderCommand.setReceiptConsignee(orderForm.getInvoiceInfoSubForm().getConsignee());
		salesOrderCommand.setReceiptAddress(orderForm.getInvoiceInfoSubForm().getAddress());
		salesOrderCommand.setReceiptTelphone(orderForm.getInvoiceInfoSubForm().getTelphone());
		// 订单来源
		salesOrderCommand.setSource(SalesOrder.SO_SOURCE_NORMAL);

		return salesOrderCommand;
	}

	/**
	 * 设置优惠券
	 * 
	 * @param salesOrderCommand
	 * @param coupon
	 */
	private void setCoupon(SalesOrderCommand salesOrderCommand, String coupon) {
		if (Validator.isNotNullOrEmpty(coupon)) {
			// 校验优惠券
			PromotionCouponCode promotionCouponCode = salesOrderManager.validCoupon(coupon);
			if (Validator.isNotNullOrEmpty(promotionCouponCode)) {
				List<CouponCodeCommand> coupons = new ArrayList<CouponCodeCommand>();
				CouponCodeCommand couponCode = new CouponCodeCommand();
				couponCode.setCouponCode(coupon);
				coupons.add(couponCode);
				salesOrderCommand.setCouponCodes(coupons);
			}
		}
	}

	/**
	 * 设置运费
	 * 
	 * @param salesOrderCommand
	 */
	private void setFreghtCommand(SalesOrderCommand salesOrderCommand) {
		CalcFreightCommand calcFreightCommand = new CalcFreightCommand();
		calcFreightCommand.setProvienceId(salesOrderCommand.getProvinceId());
		calcFreightCommand.setCityId(salesOrderCommand.getCityId());
		calcFreightCommand.setCountyId(salesOrderCommand.getAreaId());
		calcFreightCommand.setTownId(salesOrderCommand.getTownId());
		// 设置默认的物流方式
		String modeId = mataInfoManager.findValue(MetaInfoConstants.DISTRIBUTION_MODE_ID);
		if (modeId != null) {
			calcFreightCommand.setDistributionModeId(Long.parseLong(modeId));
		}

		LOGGER.info("calcFreightCommand is {}", JsonUtil.format(calcFreightCommand));

		salesOrderCommand.setCalcFreightCommand(calcFreightCommand);
	}

	@Override
	public ShoppingCartCommand buildShoppingCartForOrder(List<ShoppingCartLineCommand> cartLines,MemberDetails memberDetails,
			SalesOrderCommand salesOrderCommand) {

		// 获取购物车行信息

		
		Long memberId = null == memberDetails ? null : memberDetails.getMemberId();
		Set<String> memComboList = null == memberDetails ? null : memberDetails.getMemComboList();
		List<String> couponList = new ArrayList<String>();
		if (Validator.isNotNullOrEmpty(salesOrderCommand.getCouponCodes())) {
			couponList.add(salesOrderCommand.getCouponCodes().get(0).getCouponCode());
		}
		return sdkShoppingCartManager.buildShoppingCartCommand(memberId, cartLines, salesOrderCommand.getCalcFreightCommand(),
				couponList, memComboList);

	}

	/**
	 * 通過支付流水號查詢訂單
	 * 
	 * @param subOrdinate
	 * @return
	 */
	@Override
	public SalesOrderCommand getSalesOrderCommand(String subOrdinate) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("subOrdinate", subOrdinate);
		List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

		return orderManager.findOrderById(payInfoLogs.get(0).getOrderId(), SalesOrder.SALES_ORDER_STATUS_NEW);
	}

	/**
	 * Detect shoppingcart resolver.
	 *
	 * @param memberDetails
	 *            the member details
	 * @return the shoppingcart resolver
	 */
	private ShoppingcartResolver detectShoppingcartResolver(MemberDetails memberDetails) {
		return null == memberDetails ? guestShoppingcartResolver : memberShoppingcartResolver;
	}

	@Override
	public void updateCookieShoppingcart(List<ShoppingCartLineCommand> shoppingCartLineCommandList,
			HttpServletRequest request, HttpServletResponse response) {
		// 取出所有选中结算的商品
		// 结算状态 0未选中结算 1选中结算
		List<ShoppingCartLineCommand> mainLines = CollectionsUtil.removeAll(shoppingCartLineCommandList,
				"settlementState", 1);

		// 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息 只有游客需要有这个动作 所以放在这里
		mainLines = CollectionsUtil.select(mainLines, new MainLinesPredicate());
		guestShoppingcartPersister.save(mainLines, request, response);

	}

	@Override
	public void updateCookieShoppingcartCount(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response) {

		ShoppingcartResolver shoppingcartResolver = detectShoppingcartResolver(memberDetails);
		List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartResolver
				.getShoppingCartLineCommandList(memberDetails, request);
		shoppingcartCountPersister.save(shoppingCartLineCommandList, request, response);

	}
}
