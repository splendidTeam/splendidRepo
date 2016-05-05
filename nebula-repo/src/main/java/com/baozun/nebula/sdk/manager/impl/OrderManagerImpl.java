package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.api.salesorder.DefaultOrderCodeCreatorManager;
import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.coupon.CouponDao;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.payment.PayCodeDao;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.payment.PayInfoLogDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.dao.salesorder.SdkCancelOrderDao;
import com.baozun.nebula.dao.salesorder.SdkConsigneeDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.dao.salesorder.SdkOrderStatusLogDao;
import com.baozun.nebula.dao.salesorder.SdkPayNoDao;
import com.baozun.nebula.dao.salesorder.SdkReturnOrderDao;
import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.CancelOrderApp;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.OrderPromotion;
import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.ReturnOrderApp;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.command.ConsigneeCommand;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.handler.SalesOrderHandler;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.core.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

@Transactional
@Service("sdkOrderService")
public class OrderManagerImpl implements OrderManager {

	private static final Logger log = LoggerFactory.getLogger(OrderManagerImpl.class);

	/** 程序返回结果 **/
	private static final Integer SUCCESS = 1;
	private static final Integer FAILURE = 0;

	private static final String CREATE_ORDER_TRIGGER = Constants.ORDER_CONFIRM_TRIGGER;

	private static final String ACTIVITY_RES = Constants.ACTIVITY_RES;

	private static final String SHOPPINGCARTSUMMARY = Constants.SHOPPINGCARTSUMMARY;

	private static final String SEPARATOR_FLAG = "\\|\\|";

	@Value("#{meta['page.base']}")
	private String pageUrlBase = "";
	@Value("#{meta['upload.img.domain.base']}")
	private String imgDomainUrl = "";
	@Value("#{meta['frontend.url']}")
	private String frontendBaseUrl = "";

	@Autowired
	private SdkCancelOrderDao sdkCancelOrderDao;

	@Autowired
	private SdkOrderDao sdkOrderDao;

	@Autowired
	private SdkOrderPromotionDao sdkOrderPromotionDao;

	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;

	@Autowired
	private PayInfoDao sdkPayInfoDao;

	@Autowired
	private SdkPayNoDao sdkPayNoDao;

	@Autowired
	private SdkReturnOrderDao sdkReturnOrderDao;

	@Autowired
	private SdkOrderStatusLogDao sdkOrderStatusLogDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private SdkSkuInventoryDao sdkSkuInventoryDao;

	@Autowired(required = false)
	private OrderCodeCreatorManager orderCodeCreator;

	@Autowired
	private SdkConsigneeDao sdkConsigneeDao;

	@Autowired
	private CouponDao couponDao;

	@Autowired
	private SdkShoppingCartLineDao sdkShoppingCartLineDao;

	@Autowired
	private PayCodeDao payCodeDao;

	@Autowired
	private PayInfoDao payInfoDao;

	@Autowired
	private PayInfoLogDao payInfoLogDao;

	@Autowired
	private DistributionModeDao distributionModeDao;

	@Autowired
	private SdkShoppingCartManager sdkShoppingCartManager;

	@Autowired
	private PromotionCouponCodeDao promotionCouponCodeDao;

	@Autowired
	private SdkSkuManager sdkSkuManager;

	@Autowired
	private LogisticsManager logisticsManager;

	@Autowired
	private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

	// @Value("#{meta['orderCodeCreator']}")
	// private String
	// orderCodeCreatorPath="com.baozun.nebula.api.salesorder.DefaultOrderCodeCreatorManager";

	@Autowired
	private SdkEngineManager sdkEngineManager;

	@Autowired
	private SdkPurchaseLimitRuleFilterManager sdkPurchaseRuleFilterManager;

	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private SdkMemberManager sdkMemberManager;

	@Autowired
	private SdkEffectiveManager sdkEffectiveManager;

	@Autowired
	private SdkMsgManager sdkMsgManager;

	@Autowired(required = false)
	private SalesOrderHandler salesOrderHandler;

	public OrderManagerImpl() {
		orderCodeCreator = this.getDefaultCreator();
	}

	@Override
	@Transactional(readOnly=true)
	public SalesOrderCommand findOrderByCode(String code, Integer type) {
		SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderByCode(code, type);
		if (null == type)
			return salesOrderCommand;
		if (null == salesOrderCommand)
			return null;
		if (null != salesOrderCommand) {
			if (type == 1) {
				// 订单支付信息
				List<PayInfoCommand> payInfos = sdkPayInfoDao.findPayInfoCommandByOrderId(salesOrderCommand.getId());
				// 订单行信息
				List<Long> orderIds = new ArrayList<Long>();
				orderIds.add(salesOrderCommand.getId());
				List<OrderLineCommand> orderLines = sdkOrderLineDao.findOrderDetailListByOrderIds(orderIds);
				for (OrderLineCommand orderLineCommand : orderLines) {
					String properties = orderLineCommand.getSaleProperty();
					List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
					orderLineCommand.setSkuPropertys(propList);
				}
				// 订单行促销
				List<OrderPromotionCommand> orderPrm = sdkOrderPromotionDao.findOrderProInfoByOrderId(salesOrderCommand.getId(), 1);

				salesOrderCommand.setPayInfo(payInfos);
				salesOrderCommand.setOrderLines(orderLines);
				salesOrderCommand.setOrderPromotions(orderPrm);
			}
		}

		return salesOrderCommand;
	}

	@Override
	public String saveOrder(ShoppingCartCommand shoppingCartCommand, SalesOrderCommand salesOrderCommand, Set<String> memCombos) {

		if (salesOrderCommand == null || shoppingCartCommand == null) {
			throw new BusinessException(Constants.SHOPCART_IS_NULL);
		}
		
		//去除抬头和未选中的商品
		refactoringShoppingCartCommand(shoppingCartCommand);

		// 下单之前的引擎检查
		createOrderDoEngineChck(salesOrderCommand.getMemberId(), memCombos, shoppingCartCommand);

		String order = saveOrderInfo(salesOrderCommand, shoppingCartCommand);
		// 没有成功保存订单
		if (order == null) {
			log.warn("savedOrder returns null!");
			throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
		}
		return order;
	}
	
	
	protected void refactoringShoppingCartCommand(ShoppingCartCommand shoppingCartCommand) {
		Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap =  shoppingCartCommand.getShoppingCartByShopIdMap();
		
		Map<Long, ShoppingCartCommand> newShoppingCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();
		List<ShoppingCartLineCommand> newAllShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
		
		for(Map.Entry<Long, ShoppingCartCommand> entry:shoppingCartByShopIdMap.entrySet()){
			ShoppingCartCommand obj = entry.getValue();
			List<ShoppingCartLineCommand> lines = new ArrayList<ShoppingCartLineCommand>();
			for(ShoppingCartLineCommand shoppingCartLineCommand :obj.getShoppingCartLineCommands()){
				// 排除是标题行 或者（是赠品 1需要用户选择 0未选中） 
				if(!(shoppingCartLineCommand.isCaptionLine() 
						|| (shoppingCartLineCommand.isGift() && shoppingCartLineCommand.getGiftChoiceType()==1 
						&& (shoppingCartLineCommand.getSettlementState() ==null || shoppingCartLineCommand.getSettlementState()==0)))){
					lines.add(shoppingCartLineCommand);
					newAllShoppingCartLineCommandList.add(shoppingCartLineCommand);
				}
			}
			obj.setShoppingCartLineCommands(lines);
			newShoppingCartByShopIdMap.put(entry.getKey(), obj);
		}
		
		shoppingCartCommand.setShoppingCartByShopIdMap(newShoppingCartByShopIdMap);
		shoppingCartCommand.setShoppingCartLineCommands(newAllShoppingCartLineCommandList);
		
	}

	@Override
	public String saveManualOrder(ShoppingCartCommand shoppingCartCommand, SalesOrderCommand salesOrderCommand) {
		if (salesOrderCommand == null || shoppingCartCommand == null) {
			throw new BusinessException(Constants.SHOPCART_IS_NULL);
		}
		// 下单之前的库存检查
		for (ShoppingCartLineCommand shoppingCartLine : shoppingCartCommand.getShoppingCartLineCommands()) {
			Boolean retflag = sdkEffectiveManager.chckInventory(shoppingCartLine.getExtentionCode(), shoppingCartLine.getQuantity());
			if (!retflag) {
				// 库存不足
				throw new BusinessException(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, new Object[] { shoppingCartLine.getItemName() });
			}
		}
		String order = saveOrderInfo(salesOrderCommand, shoppingCartCommand);
		// 没有成功保存订单
		if (order == null) {
			log.warn("savedOrder returns null!");
			throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
		}
		return order;
	}

	/**
	 * 创建订单的引擎检查
	 * 
	 * @param request
	 * @param shoppingCartCommand
	 */
	private void createOrderDoEngineChck(Long memberId, Set<String> memCombos, ShoppingCartCommand shoppingCartCommand) {
		// 引擎检查(限购、有效性检查、库存)
		Integer retval = SUCCESS;
		Set<String> memboIds = null;
		if (null == memberId) {
			// 游客
			memboIds = getMemboIds();
		} else {
			memboIds = memCombos;
		}
		List<Long> shopIds = getShopIds(shoppingCartCommand.getShoppingCartLineCommands());
		Set<String> itemComboIds = getItemComboIds(shoppingCartCommand.getShoppingCartLineCommands());
		List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager.getIntersectPurchaseLimitRuleData(
				shopIds, memboIds, itemComboIds, new Date());
		if (null == purchaseLimitationList || purchaseLimitationList.size() == 0)
			purchaseLimitationList = new ArrayList<LimitCommand>();
		
		for(Map.Entry<Long, ShoppingCartCommand> entry:shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()){
			for (ShoppingCartLineCommand shoppingCartLine :entry.getValue().getShoppingCartLineCommands()){
				//直推礼品不做校验
				if(!(shoppingCartLine.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLine.getGiftChoiceType()))){
					sdkEngineManager.doEngineCheck(shoppingCartLine, false, shoppingCartCommand, purchaseLimitationList);
				}
			}
		}
		
		//限购校验失败
		List<ShoppingCartLineCommand> errorLineList = sdkEngineManager.doEngineCheckLimit(shoppingCartCommand, purchaseLimitationList);
		if(null != errorLineList && errorLineList.size() > 0){
			StringBuffer errorItemName = new StringBuffer();
			List<Long> itemList = new ArrayList<Long>();
			for(ShoppingCartLineCommand cartLine : errorLineList){
				if("".equals(errorItemName.toString())){
					errorItemName.append(cartLine.getItemName());
					itemList.add(cartLine.getItemId());
				}else{
					if(!itemList.contains(cartLine.getItemId())){
						errorItemName.append(",").append(cartLine.getItemName());
						itemList.add(cartLine.getSkuId());
					}
				}
			}
			throw new BusinessException(Constants.THE_ORDER_CONTAINS_LIMIT_ITEM, new Object[] { errorItemName.toString() });
		}
	}

	/**
	 * 保存订单信息
	 * 
	 * @param salesOrderCommand
	 * @param shoppingCartCommand
	 * @return
	 */
	private String saveOrderInfo(SalesOrderCommand salesOrderCommand, ShoppingCartCommand shoppingCartCommand) {

		// 购物车行
		Map<Long,ShoppingCartCommand> shoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

		// shoppingCartLineCommandMap
		Map<Long, List<ShoppingCartLineCommand>> shoppingCartLineCommandMap = new HashMap<Long, List<ShoppingCartLineCommand>>();

		for(Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommandMap.entrySet()){
			shoppingCartLineCommandMap.put(entry.getKey(), entry.getValue().getShoppingCartLineCommands());
		}
		
		// shoppingCartPromotionBriefMap
		List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
				.sharePromotionDiscountToEachLine(shoppingCartCommand, shoppingCartCommand.getCartPromotionBriefList());
		
		Map<Long, List<PromotionSKUDiscAMTBySetting>> promotionSKUDiscAMTBySettingMap = new HashMap<Long, List<PromotionSKUDiscAMTBySetting>>();
		if (Validator.isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)) {
			for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList) {
				List<PromotionSKUDiscAMTBySetting> list = promotionSKUDiscAMTBySettingMap
						.get(promotionSKUDiscAMTBySetting.getShopId());
				if (list == null) {
					list = new ArrayList<PromotionSKUDiscAMTBySetting>();
					promotionSKUDiscAMTBySettingMap.put(promotionSKUDiscAMTBySetting.getShopId(), list);
				}
				list.add(promotionSKUDiscAMTBySetting);
			}
		}
		
		// shopCartCommandByShopMap
		List<ShopCartCommandByShop> shopCartCommandByShopList = shoppingCartCommand.getSummaryShopCartList();
		
		Map<Long, ShopCartCommandByShop> shopCartCommandByShopMap = new HashMap<Long, ShopCartCommandByShop>();
		if (Validator.isNotNullOrEmpty(shopCartCommandByShopList)) {
			for (ShopCartCommandByShop shopCartCommandByShop : shopCartCommandByShopList) {
				shopCartCommandByShopMap.put(shopCartCommandByShop.getShopId(), shopCartCommandByShop);
			}
		}

		// 合并付款
		String subOrdinate = orderCodeCreator.createOrderSerialNO();
		if (subOrdinate == null) {
			throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
		}
		Iterator<Long> it = shoppingCartLineCommandMap.keySet().iterator();

		String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);

		BigDecimal paySum = getPaySum(salesOrderCommand, shoppingCartCommand);

		List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();
		
		try {
			while (it.hasNext()) {
				Long shopId = it.next();
				List<ShoppingCartLineCommand> sccList = shoppingCartLineCommandMap.get(shopId);
				List<PromotionSKUDiscAMTBySetting> psdabsList = promotionSKUDiscAMTBySettingMap.get(shopId);
				ShopCartCommandByShop shopCartCommandByShop = shopCartCommandByShopMap.get(shopId);
				// 根据shopId保存订单概要
				SalesOrder salesOrder = savaOrder(shopId, salesOrderCommand, sccList, shopCartCommandByShop);
				if (salesOrder == null)
					throw new BusinessException(Constants.CREATE_ORDER_FAILURE);

				// 保存订单行、订单行优惠
				savaOrderLinesAndPromotions(salesOrderCommand, salesOrder.getId(), sccList, psdabsList);

				// 保存支付详细
				savePayInfoAndPayInfoLog(salesOrderCommand, subOrdinate, salesOrder, shopId);

				// 保存收货人信息
				saveConsignee(salesOrder, salesOrderCommand);

				// 保存OMS消息发送记录(销售订单信息推送给SCM)
				sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ORDER_SEND, salesOrder.getId(), null);

				// 封装发送邮件数据
				if (isSendEmail != null && isSendEmail.equals("true")) {
					Map<String, Object> dataMap = getDataMap(subOrdinate, salesOrder, salesOrderCommand, sccList, shopCartCommandByShop, psdabsList);
					if (dataMap != null)
						dataMapList.add(dataMap);
				}
				
				// 扣减库存
				liquidateSkuInventory(sccList);

			}

			// 保存支付流水
			savaPayCode(paySum, subOrdinate);
			// 优惠券状体置为已使用 isUsed = 1
			if (Validator.isNotNullOrEmpty(salesOrderCommand.getCouponCodes())) {
				for (CouponCodeCommand couponCodeCommand : salesOrderCommand.getCouponCodes()) {
					List<String> list = new ArrayList<String>();

					if (!couponCodeCommand.getIsOut()) {
						list.add(couponCodeCommand.getCouponCode());
					}

					int res = promotionCouponCodeDao.comsumePromotionCouponCodeByCouponCodes(list);
					if (res != list.size()) {
						if (res < list.size()) {
							throw new BusinessException(Constants.COUPON_IS_USED);
						} else {
							throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
						}
					}
				}
			}
			// 清空购物车

			if (salesOrderCommand.getIsImmediatelyBuy() == null || salesOrderCommand.getIsImmediatelyBuy() == false)
				sdkShoppingCartManager.emptyShoppingCart(salesOrderCommand.getMemberId());

			// 发邮件
			sendEmailOfCreateOrder(dataMapList);

		} catch (BusinessException e) {
			throw e;
		}
		return subOrdinate;
	}

	private BigDecimal getPaySum(SalesOrderCommand salesOrderCommand, ShoppingCartCommand shoppingCartCommand) {
		BigDecimal paySum = shoppingCartCommand.getCurrentPayAmount();
		List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
		if (soPayMentDetails != null) {
			for (String soPayMentDetail : soPayMentDetails) {
				// 支付方式 String格式：shopId||payMentType||金额
				String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
				BigDecimal payMoney = new BigDecimal(strs[2]);
				paySum = paySum.subtract(payMoney);
			}
		}
		return paySum;
	}

	protected void savePayInfoAndPayInfoLog(SalesOrderCommand salesOrderCommand, String subOrdinate,
			SalesOrder salesOrder, Long shopId) {
		List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
		BigDecimal payMainMoney = salesOrder.getTotal().add(salesOrder.getActualFreight());
		// 除主支付方式之外的付款
		if (soPayMentDetails != null) {
			for (String soPayMentDetail : soPayMentDetails) {
				// 支付方式 String格式：shopId||payMentType||金额
				String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
				if (shopId.toString().equals(strs[0]) && strs.length == 3) {
					PayInfo res = savePayInfo(salesOrder, strs);
					payMainMoney = payMainMoney.subtract(res.getPayMoney());
				}
			}
		}
		PayInfo payInfo = savePayInfoOfPayMain(salesOrderCommand, salesOrder, payMainMoney);

		savePayInfoLogOfPayMain(salesOrderCommand, subOrdinate, salesOrder, payMainMoney, payInfo);
	}

	private void savePayInfoLogOfPayMain(SalesOrderCommand salesOrderCommand, String subOrdinate, SalesOrder salesOrder, BigDecimal payMainMoney, PayInfo payInfo) {
		Boolean codFlag = false;
		if (salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)) {
			codFlag = true;
		}
		PayInfoLog payInfoLog = new PayInfoLog();
		payInfoLog.setCreateTime(new Date());
		payInfoLog.setOrderId(salesOrder.getId());
		if (codFlag) {
			payInfoLog.setPaySuccessStatus(true);
		} else {
			payInfoLog.setPaySuccessStatus(false);
		}
		payInfoLog.setCallCloseStatus(false);
		payInfoLog.setPayType(salesOrderCommand.getPayment());
		payInfoLog.setPayMoney(payMainMoney);
		payInfoLog.setPayNumerical(payMainMoney);
		// 用于保存银行
		payInfoLog.setPayInfo(salesOrderCommand.getPaymentStr());
		payInfoLog.setSubOrdinate(subOrdinate);
		payInfoLog.setPayInfoId(payInfo.getId());

		Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
		String payInfoStr = salesOrderCommand.getPaymentStr();
		String payType = pro.getProperty(payInfoStr + ".payType").trim();

		if (Validator.isNotNullOrEmpty(payType)) {
			payInfoLog.setThirdPayType(Integer.parseInt(payType));
		}

		payInfoLogDao.save(payInfoLog);
	}

	private PayInfo savePayInfoOfPayMain(SalesOrderCommand salesOrderCommand, SalesOrder salesOrder, BigDecimal payMainMoney) {
		Boolean codFlag = false;
		if (salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)) {
			codFlag = true;
		}
		PayInfo payInfo = new PayInfo();
		payInfo.setCreateTime(new Date());
		payInfo.setOrderId(salesOrder.getId());
		if (codFlag) {
			payInfo.setPaySuccessStatus(true);
		} else {
			payInfo.setPaySuccessStatus(false);
		}
		payInfo.setPayType(salesOrderCommand.getPayment());
		payInfo.setPayMoney(payMainMoney);
		payInfo.setPayNumerical(payMainMoney);
		// 分期期数
		payInfo.setPeriods(salesOrderCommand.getPeriods());
		// 用于保存银行
		payInfo.setPayInfo(salesOrderCommand.getPaymentStr());
		payInfo = payInfoDao.save(payInfo);

		return payInfo;
	}

	private PayInfo savePayInfo(SalesOrder salesOrder, String[] strs) {
		PayInfo payInfo = new PayInfo();
		payInfo.setCreateTime(new Date());
		// 付款时间
		payInfo.setModifyTime(new Date());
		payInfo.setOrderId(salesOrder.getId());
		payInfo.setPaySuccessStatus(true);
		payInfo.setPayType(Integer.parseInt(strs[1]));
		BigDecimal payMoney = new BigDecimal(strs[2]);
		payInfo.setPayMoney(payMoney);
		payInfo.setPayNumerical(payMoney);
		payInfo = payInfoDao.save(payInfo);

		return payInfo;
	}

	protected void sendEmailOfCreateOrder(List<Map<String, Object>> dataMapList) {
		if (Validator.isNotNullOrEmpty(dataMapList)) {
			for (Map<String, Object> dataMap : dataMapList) {
				EmailEvent emailEvent = new EmailEvent(this, dataMap.get("email").toString(), EmailConstants.CREATE_ORDER_SUCCESS, dataMap);
				eventPublisher.publish(emailEvent);
			}
		}
	}

	protected Map<String, Object> getDataMap(String subOrdinate, SalesOrder salesOrder,
			SalesOrderCommand salesOrderCommand, List<ShoppingCartLineCommand> sccList,
			ShopCartCommandByShop shopCartCommandByShop, List<PromotionSKUDiscAMTBySetting> psdabsList) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 后台下单 并且填写了 邮件地址
		if (Validator.isNotNullOrEmpty(salesOrderCommand.getMemberId())) {
			MemberPersonalData memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrder.getMemberId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

			String nickName = "";

			String email = salesOrderCommand.getEmail();

			if (Validator.isNotNullOrEmpty(memberPersonalData)) {
				nickName = memberPersonalData.getNickname();
				if (Validator.isNullOrEmpty(email)) {
					email = memberPersonalData.getEmail();
				}
			}

			if (Validator.isNullOrEmpty(nickName))
				nickName = salesOrderCommand.getName();

			if (Validator.isNullOrEmpty(email))
				return null;

			// 获取付款地址
			if (salesOrderCommand.getIsBackCreateOrder() && !salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)) {
				String payUrlPrefix = sdkMataInfoManager.findValue(MataInfo.PAY_URL_PREFIX);
				String payUrl = frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
				dataMap.put("isShowPayButton", true);
				dataMap.put("payUrl", payUrl);
			} else {
				dataMap.put("isShowPayButton", false);
				dataMap.put("payUrl", "#");
			}

			dataMap.put("nickName", nickName);
			dataMap.put("createDateOfAll", sdf.format(salesOrder.getCreateTime()));
			dataMap.put("createDateOfSfm", sdf1.format(salesOrder.getCreateTime()));
			dataMap.put("orderCode", salesOrder.getCode());
			dataMap.put("receiveName", salesOrderCommand.getName());
			dataMap.put("ssqStr", salesOrderCommand.getProvince() + salesOrderCommand.getCity() + salesOrderCommand.getArea());
			dataMap.put("address", salesOrderCommand.getAddress());

			dataMap.put("mobile", Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile() : salesOrderCommand.getTel());

			dataMap.put("sumItemFee", salesOrder.getTotal().add(salesOrder.getDiscount()));
			dataMap.put("shipFee", salesOrder.getActualFreight());
			dataMap.put("offersTotal", salesOrder.getDiscount());
			dataMap.put("sumPay", salesOrder.getTotal().add(salesOrder.getActualFreight()));
			dataMap.put("itemLines", sccList);
			dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));
			dataMap.put("pageUrlBase", pageUrlBase);
			dataMap.put("imgDomainUrl", imgDomainUrl);
			dataMap.put("email", email);
		} else {
			if (Validator.isNullOrEmpty(salesOrderCommand.getEmail())) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

			// 获取付款地址
			if (salesOrderCommand.getIsBackCreateOrder() && !salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)) {
				String payUrlPrefix = sdkMataInfoManager.findValue(MataInfo.PAY_URL_PREFIX);
				String payUrl = frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
				dataMap.put("isShowPayButton", true);
				dataMap.put("payUrl", payUrl);
			} else {
				dataMap.put("isShowPayButton", false);
				dataMap.put("payUrl", "#");
			}

			dataMap.put("nickName", salesOrderCommand.getName());
			dataMap.put("createDateOfAll", sdf.format(salesOrder.getCreateTime()));
			dataMap.put("createDateOfSfm", sdf1.format(salesOrder.getCreateTime()));
			dataMap.put("orderCode", salesOrder.getCode());
			dataMap.put("receiveName", salesOrderCommand.getName());
			dataMap.put("ssqStr", salesOrderCommand.getProvince() + salesOrderCommand.getCity() + salesOrderCommand.getArea());
			dataMap.put("address", salesOrderCommand.getAddress());

			dataMap.put("mobile", Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile() : salesOrderCommand.getTel());

			dataMap.put("sumItemFee", salesOrder.getTotal().add(salesOrder.getDiscount()));
			dataMap.put("shipFee", salesOrder.getActualFreight());
			dataMap.put("offersTotal", salesOrder.getDiscount());
			dataMap.put("sumPay", salesOrder.getTotal().add(salesOrder.getActualFreight()));
			dataMap.put("itemLines", sccList);
			dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));
			dataMap.put("pageUrlBase", pageUrlBase);
			dataMap.put("imgDomainUrl", imgDomainUrl);
			dataMap.put("email", salesOrderCommand.getEmail());
		}

		/** 扩展点 如商城需要需要加入特殊字段 各自实现 **/
		if (null != salesOrderHandler) {
			dataMap = salesOrderHandler.getEmailDataOfCreateOrder(subOrdinate, salesOrder, salesOrderCommand, sccList, shopCartCommandByShop, psdabsList, dataMap);
		}

		return dataMap;

	}

	/** 返回支付方式 */
	private String getPaymentName(Integer payment) {
		String name = "支付宝";
		if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_COD)) {
			name = "货到付款";
		} else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_NETPAY)) {
			name = "网银在线";
		} else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY)) {
			name = "支付宝";
		}
		return name;
	}

	//TODO 如果有bundle 逻辑处理
	private void liquidateSkuInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList) {
		if (Validator.isNotNullOrEmpty(shoppingCartLineCommandList)) {
			for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList) {

				//如果直推礼品库存数小于购买量时，扣减现有库存
				if( shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType())){ 
					//下架
					if(!shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1){
						continue;
					}
					if(null==shoppingCartLineCommand.getStock() || shoppingCartLineCommand.getStock()<=0){
						continue;
					}else if(shoppingCartLineCommand.getStock()<shoppingCartLineCommand.getQuantity()){
						shoppingCartLineCommand.setQuantity(shoppingCartLineCommand.getStock());
					}
				}
				
				//主卖品和赠品都扣库存
				int result = sdkSkuInventoryDao.liquidateSkuInventory(shoppingCartLineCommand.getExtentionCode(), shoppingCartLineCommand.getQuantity());
				// 返回的行数是否为 1 如果不是,说明库存不足 就抛出异常
				if (result != 1) {
					throw new BusinessException(Constants.CHECK_INVENTORY_FAILURE, new Object[] { shoppingCartLineCommand.getItemName() });
				}
			}
		}
	}

	protected void savaPayCode(BigDecimal payMoney, String subOrdinate) {
		PayCode payCode = new PayCode();
		payCode.setCreateTime(new Date());
		payCode.setPayMoney(payMoney);
		payCode.setPayNumerical(payMoney);
		payCode.setPaySuccessStatus(false);
		payCode.setSubOrdinate(subOrdinate);
		payCodeDao.save(payCode);
	}

	/**
	 * 根据shopId保存订单概要
	 * 
	 * @param shopId
	 * @param salesOrderCommand
	 * @return
	 */
	private SalesOrder savaOrder(Long shopId, SalesOrderCommand salesOrderCommand, List<ShoppingCartLineCommand> sccList, ShopCartCommandByShop shopCartCommandByShop) {
		SalesOrder order = new SalesOrder();
		ConvertUtils.convertTwoObject(order, salesOrderCommand);
		// 生成订单号
		String orderCode = orderCodeCreator.createOrderCodeBySource(salesOrderCommand.getSource());
		if (orderCode == null) {
			throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
		}
		BigDecimal actualFreight = shopCartCommandByShop.getOriginShoppingFee().subtract(shopCartCommandByShop.getOffersShipping());
		// 总价 不含运费最终货款
		BigDecimal total = shopCartCommandByShop.getRealPayAmount().subtract(actualFreight);
		// 财务状态
		order.setFinancialStatus(SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
		// 物流状态
		order.setLogisticsStatus(SalesOrder.SALES_ORDER_STATUS_NEW);
		order.setCode(orderCode);
		order.setCreateTime(new Date());
		order.setShopId(shopId);
		order.setQuantity(shopCartCommandByShop.getQty());
		order.setTotal(total);
		order.setDiscount(shopCartCommandByShop.getOffersTotal());
		order.setPayableFreight(shopCartCommandByShop.getOriginShoppingFee());
		order.setActualFreight(actualFreight);
		// 设置买家留言 格式一定要shopId_value 不留言就是shopid_
		if (Validator.isNotNullOrEmpty(salesOrderCommand.getRemarks())) {
			for (String remark : salesOrderCommand.getRemarks()) {
				// String a = "shopid||value"
				String[] strs = remark.split(SEPARATOR_FLAG);
				if (strs[0].equals(shopId.toString()) && strs.length == 2) {
					order.setRemark(strs[1]);
				}
			}
		}
		// 快递
		if (Validator.isNotNullOrEmpty(salesOrderCommand.getLogisticsProvider())) {
			for (String logisticsProvider : salesOrderCommand.getLogisticsProvider()) {
				// String a = "shopid_code||value"
				String[] strs = logisticsProvider.split(SEPARATOR_FLAG);
				if (strs[0].equals(shopId.toString()) && strs.length == 3) {
					order.setLogisticsProviderCode(strs[1]);
					order.setLogisticsProviderName(strs[2]);
				}
			}
		}

		SalesOrder res = sdkOrderDao.save(order);
		if (res != null)
			return res;
		return null;
	}

	/**
	 * 保存订单行
	 * 
	 * @param orderId
	 * @param sccList
	 */
	protected void savaOrderLinesAndPromotions(SalesOrderCommand salesOrderCommand, Long orderId,
			List<ShoppingCartLineCommand> sccList, List<PromotionSKUDiscAMTBySetting> psdabsList) {
		if (Validator.isNotNullOrEmpty(sccList)) {
			for (ShoppingCartLineCommand shoppingCartLineCommand : sccList) {
				OrderLine res = this.saveOrderLine(orderId, shoppingCartLineCommand);
				if (res == null) {
					continue;
				}

				if (Validator.isNotNullOrEmpty(psdabsList)) {
					for (PromotionSKUDiscAMTBySetting promo : psdabsList) {
						boolean giftMark = promo.getGiftMark();
						/**
						 * 0代表赠品 1代表主卖品
						 */
						Integer type = null;
						if (!giftMark) {
							type = 1;
						} else {
							type = 0;
						}

						// 非免运费
						if (!promo.getFreeShippingMark() 
								&& promo.getSkuId().equals(res.getSkuId())
								&& res.getType().equals(type)) {
							savaOrderPromotion(orderId, promo, res, salesOrderCommand);
						}
					}
				}
			}
			// 免运费
			if (Validator.isNotNullOrEmpty(psdabsList)) {
				for (PromotionSKUDiscAMTBySetting promo : psdabsList) {
					if (promo.getFreeShippingMark()) {
						OrderPromotion orderPromotionDetail = new OrderPromotion();
						// 订单id
						orderPromotionDetail.setOrderId(orderId);
						// 活动id
						orderPromotionDetail.setActivityId(promo.getPromotionId());
						// 促销码 (暂时没用)
						orderPromotionDetail.setPromotionNo(promo.getPromotionId().toString());
						// 促销类型
						orderPromotionDetail.setPromotionType(promo.getPromotionType());
						// 折扣金额
						orderPromotionDetail.setDiscountAmount(promo.getDiscountAmount());
						// 是否运费折扣
						orderPromotionDetail.setIsShipDiscount(true);
						// 优惠券
						if (promo.getCouponCodes() != null) {
							orderPromotionDetail.setCoupon(promo.getCouponCodes().toString());
						}
						// 描述 ...name
						orderPromotionDetail.setDescribe(promo.getPromotionName());
						// 是否基于整单
						orderPromotionDetail.setBaseOrder(promo.getBaseOrder());
						sdkOrderPromotionDao.save(orderPromotionDetail);
					}
				}
			}

		}
	}

	/**
	 * 保存订单行
	 */
	//TODO bundle 下单要进行拆分
	protected OrderLine saveOrderLine(Long orderId, ShoppingCartLineCommand shoppingCartLineCommand) {
		OrderLine orderLine = new OrderLine();
		// 商品数量
		orderLine.setCount(shoppingCartLineCommand.getQuantity());
		/**
		 * 直推赠品(送完即止) 1：如果数量为零 该行不存入数据库 :2：如果库存量小于购买量时 存入库存量
		 * 
		 */
		if (shoppingCartLineCommand.isGift()
				&& GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType())) {
			// 下架
			if (!shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1) {
				return null;
			}
			// 无库存
			if (null == shoppingCartLineCommand.getStock() || shoppingCartLineCommand.getStock() <= 0) {
				return null;
			} else if (shoppingCartLineCommand.getStock() < shoppingCartLineCommand.getQuantity()) {
				// 库存不足
				orderLine.setCount(shoppingCartLineCommand.getStock());
			}
		}

		// 订单id
		orderLine.setOrderId(orderId);
		// UPC
		orderLine.setExtentionCode(shoppingCartLineCommand.getExtentionCode());
		// skuId
		orderLine.setSkuId(shoppingCartLineCommand.getSkuId());
		// 商品id
		orderLine.setItemId(shoppingCartLineCommand.getItemId());
		// 原销售单价
		orderLine.setMSRP(shoppingCartLineCommand.getListPrice());
		// 现销售单价
		orderLine.setSalePrice(shoppingCartLineCommand.getSalePrice());
		// 行小计
		orderLine.setSubtotal(shoppingCartLineCommand.getSubTotalAmt());
		// 折扣、行类型
		if (shoppingCartLineCommand.isGift()) {
			orderLine.setDiscount(shoppingCartLineCommand.getSalePrice());
			orderLine.setType(ItemInfo.TYPE_GIFT);
		} else {
			orderLine.setDiscount(shoppingCartLineCommand.getDiscount());
			orderLine.setType(ItemInfo.TYPE_MAIN);
		}

		// 商品名称
		orderLine.setItemName(shoppingCartLineCommand.getItemName());
		// 商品主图
		orderLine.setItemPic(shoppingCartLineCommand.getItemPic());
		// 销售属性信息
		orderLine.setSaleProperty(shoppingCartLineCommand.getSaleProperty());
		// 行类型
		orderLine.setType(shoppingCartLineCommand.getType());
		// 分组号
		if (Validator.isNotNullOrEmpty(shoppingCartLineCommand.getLineGroup())) {
			orderLine.setGroupId(Integer.valueOf(shoppingCartLineCommand.getLineGroup().toString()));
		}
		// 评价状态
		orderLine.setEvaluationStatus(null);
		// 商品快照版本
		orderLine.setSnapshot(null);

		return sdkOrderLineDao.save(orderLine);
	}

	protected void savaOrderPromotion(Long orderId, PromotionSKUDiscAMTBySetting promo, OrderLine res,
			SalesOrderCommand salesOrderCommand) {
		OrderPromotion orderPromotionDetail = new OrderPromotion();
		// 订单id
		orderPromotionDetail.setOrderId(orderId);
		// 订单行
		orderPromotionDetail.setOrderLineId(res.getId());
		// 活动id
		orderPromotionDetail.setActivityId(promo.getPromotionId());
		// 促销码 (暂时没用)
		orderPromotionDetail.setPromotionNo(promo.getPromotionId().toString());
		// 促销类型
		orderPromotionDetail.setPromotionType(promo.getPromotionType());
		// 折扣金额
		orderPromotionDetail.setDiscountAmount(promo.getDiscountAmount());
		// 是否运费折扣
		orderPromotionDetail.setIsShipDiscount(false);
		// 优惠券
		Set<String> set = promo.getCouponCodes();
		Set<String> newSet = new HashSet<String>();
		if (promo.getCouponCodes() != null) {
			List<String> list = new ArrayList<String>(set);
			for (String couponcode : list) {
				if (couponcode.equals(CouponCodeCommand.BRUSHHEAD_COUPON)) {
					for (CouponCodeCommand couponCodeCommand : salesOrderCommand.getCouponCodes()) {
						if (couponCodeCommand.getIsOut()) {
							newSet.add(couponCodeCommand.getCouponCode());
						}
					}
				} else {
					newSet.add(couponcode);
				}
			}

			orderPromotionDetail.setCoupon(newSet.toString());
		}

		// 描述 ...name
		orderPromotionDetail.setDescribe(promo.getPromotionName());
		// 是否基于整单
		orderPromotionDetail.setBaseOrder(promo.getBaseOrder());

		sdkOrderPromotionDao.save(orderPromotionDetail);
	}

	/**
	 * 保存收货人信息
	 * 
	 * @param orderId
	 * @param salesOrderCommand
	 */
	protected void saveConsignee(SalesOrder salesOrder, SalesOrderCommand salesOrderCommand) {
		Consignee consignee = new Consignee();
		ConvertUtils.convertFromTarget(consignee, salesOrderCommand);
		List<String> appointTimeQuantums = salesOrderCommand.getAppointTimeQuantums();
		// 设置指定时间段
		if (Validator.isNotNullOrEmpty(appointTimeQuantums)) {
			for (String appointTimeQuantum : appointTimeQuantums) {
				// String a = "shopid||value"
				String[] strs = appointTimeQuantum.split(SEPARATOR_FLAG);
				if (salesOrder.getShopId().toString().equals(strs[0]) && strs.length == 2) {
					consignee.setAppointTimeQuantum(strs[1]);
				}
			}
		}
		// 设置指定日期
		List<String> appointTimes = salesOrderCommand.getAppointTimes();
		if (Validator.isNotNullOrEmpty(appointTimes)) {
			for (String appointTime : appointTimes) {
				// String a = "shopid||value"
				String[] strs = appointTime.split(SEPARATOR_FLAG);
				if (salesOrder.getShopId().toString().equals(strs[0]) && strs.length == 2) {
					consignee.setAppointTime(strs[1]);
				}
			}
		}
		// 设置指定类型
		List<String> appointTypes = salesOrderCommand.getAppointTypes();
		if (Validator.isNotNullOrEmpty(appointTypes)) {
			for (String appointType : appointTypes) {
				// String a = "shopid||value"
				String[] strs = appointType.split(SEPARATOR_FLAG);
				if (salesOrder.getShopId().toString().equals(strs[0]) && strs.length == 2) {
					consignee.setAppointType(strs[1]);
				}
			}
		}
		consignee.setOrderId(salesOrder.getId());
		sdkConsigneeDao.save(consignee);
	}

	/**
	 * 检查优惠券是否有效
	 * 
	 * @param memberId
	 * @param couponNo
	 */
	@Transactional(readOnly=true)
	private void chckCouponNo(Long memberId, String couponNo) {
		if (StringUtils.isNotBlank(couponNo)) {
			CouponCommand couponCommand = couponDao.findCouponCommandByMemberIdAndCardNo(memberId, couponNo);
			if (null == couponCommand) {
				throw new BusinessException(Constants.COUPON_IS_NOT_VALID);
			}
		}
	}

	/**
	 * 得到购物车行对象 先判断是不是立即购买，如果不是，再判断是不是普通购买流程
	 * 
	 * @param salesOrderCommand
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	private List<ShoppingCartLineCommand> getShoppingCartLines(SalesOrderCommand salesOrderCommand, HttpServletRequest request) {

		List<ShoppingCartLineCommand> shoppingCartLines = null;

		if (salesOrderCommand.getIsBackCreateOrder()) {
			// 后台下单
			shoppingCartLines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.BACKORDER_SESSION_SHOPCART);
			request.getSession().removeAttribute(Constants.BACKORDER_SESSION_SHOPCART);

		} else if (salesOrderCommand.getIsImmediatelyBuy()) {
			// 立即购买
			shoppingCartLines = (List<ShoppingCartLineCommand>) request.getSession().getAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
			request.getSession().removeAttribute(Constants.IMMEDIATELYBUY_SESSION_SHOPCART);
		} else {
			if (null == salesOrderCommand.getMemberId()) {
				// 游客
				shoppingCartLines = getCookeCartLines(request, salesOrderCommand.getGuestIdentify());
			} else {
				// 会员
				shoppingCartLines = sdkShoppingCartLineDao.findShopCartLineByMemberId(salesOrderCommand.getMemberId(), Constants.CHECKED_CHOOSE_STATE);
			}
		}

		return shoppingCartLines;
	}

	private List<ShoppingCartLineCommand> getCookeCartLines(HttpServletRequest request, String guestIndentify) {
		List<ShoppingCartLineCommand> cartLineList = new ArrayList<ShoppingCartLineCommand>();
		Cookie cookie = WebUtils.getCookie(request, guestIndentify);
		if (null != cookie) {
			// guestIndentify有购物车信息
			String cookieJson = cookie.getValue();
			try {
				List<CookieShoppingCartLine> cookieCartLineList = JSON.parseObject(cookieJson, new TypeReference<ArrayList<CookieShoppingCartLine>>() {
				});
				if (null != cookieCartLineList && cookieCartLineList.size() > 0) {
					for (CookieShoppingCartLine cookieLine : cookieCartLineList) {
						if (cookieLine.getSettlementState() == Constants.CHECKED_CHOOSE_STATE) {// 被选中的购物车行
							ShoppingCartLineCommand cartLine = new ShoppingCartLineCommand();
							cartLine.setQuantity(cookieLine.getQuantity());
							cartLine.setCreateTime(cookieLine.getCreateTime());
							cartLine.setSettlementState(cookieLine.getSettlementState());
							cartLine.setExtentionCode(cookieLine.getExtentionCode());
							cartLine.setSkuId(cookieLine.getSkuId());
							cartLine.setPromotionId(cookieLine.getPromotionId());
							cartLine.setGift(cookieLine.getIsGift());
							cartLine.setLineGroup(cookieLine.getLineGroup());
							cartLineList.add(cartLine);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("getCookeCartLines method parse cookie json failure");
				return null;
			}
		}
		return cartLineList;
	}

	/**
	 * 将 渠道引擎返回的 List 转化为 字符串
	 * 
	 * @param channelIdList
	 * @return
	 */
	private String getChannelIdByList(List<String> channelIdList) {
		if (null == channelIdList || 0 == channelIdList.size()) {
			return null;
		}

		StringBuilder channelIdStr = new StringBuilder();
		for (String id : channelIdList) {
			channelIdStr.append(id).append(",");
		}

		// 去掉最后一个逗号
		String result = null;
		if (channelIdStr.length() > 0) {
			result = channelIdStr.substring(0, channelIdStr.length() - 1);
		}

		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts, Map<String, Object> searchParam) {
		List<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrdersWithOutPage(sorts, searchParam);

		if (null == salesOrderPage) {
			return null;
		} else {
			List<Long> idList = new ArrayList<Long>(salesOrderPage.size());
			for (SalesOrderCommand cmd : salesOrderPage) {
				idList.add(cmd.getId());
			}

			List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
			for (SalesOrderCommand order : salesOrderPage) {
				Long orderId = order.getId();
				List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
				for (OrderLineCommand line : allLineList) {
					if (line.getOrderId().equals(orderId)) {
						orderLineList.add(line);
						// 属性list
						String properties = line.getSaleProperty();
						List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
						line.setSkuPropertys(propList);
					}
				}

				order.setOrderLines(orderLineList);
			}

		}

		return salesOrderPage;
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<SalesOrderCommand> findOrders(Page page, Sort[] sorts, Map<String, Object> searchParam) {
		Pagination<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrders(page, sorts, searchParam);

		if (null != salesOrderPage.getItems()) {
			List<Long> idList = new ArrayList<Long>(salesOrderPage.getItems().size());
			for (SalesOrderCommand cmd : salesOrderPage.getItems()) {
				idList.add(cmd.getId());
			}

			List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
			for (SalesOrderCommand order : salesOrderPage.getItems()) {
				Long orderId = order.getId();
				List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
				for (OrderLineCommand line : allLineList) {
					if (line.getOrderId().equals(orderId)) {
						orderLineList.add(line);
						// 属性list
						String properties = line.getSaleProperty();
						List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
						line.setSkuPropertys(propList);
					}
				}

				order.setOrderLines(orderLineList);
			}

		}

		return salesOrderPage;
	}

	@Override
	public PayInfoCommand savePayOrder(String code, PayInfoCommand payInfoCommand) {
		// 先根据订单code查询订单是否存在

		SalesOrderCommand salesOrder = judgeOrderIfExist(code);

		PayInfo payInfo = (PayInfo) ConvertUtils.convertFromTarget(new PayInfo(), payInfoCommand);
		payInfo.setOrderId(salesOrder.getId());
		payInfo.setPayInfo(payInfoCommand.getPayInfo());
		// 保存订单详细
		payInfo = sdkPayInfoDao.save(payInfo);

		if (null == payInfo) {
			log.warn(" payInfoDao.save(payInfo) returns null");
			return null;
		}

		// 不保存支付流水
		// payInfoCommand.setId(payInfo.getId());
		// // 保存订单流水
		// payInfoCommand = savePayNos(payInfoCommand);
		//
		// if (null == payInfoCommand) {
		// log.warn(" payInfoDao.save(payInfo) returns null");
		// return null;
		// }

		return (PayInfoCommand) ConvertUtils.convertTwoObject(new PayInfoCommand(), payInfo);
	}

	@Override
	public SalesOrderCommand updateOrderFinancialStatus(String code, Integer financialStatus) {
		SalesOrderCommand salesOrder = judgeOrderIfExist(code);
		Integer retval = sdkOrderDao.updateOrderFinancialStatus(code, financialStatus, new Date());
		if (retval > 0) {
			saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getFinancialStatus(), financialStatus);
			salesOrder.setFinancialStatus(financialStatus);
		} else {
			return null;
		}
		return salesOrder;
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal findOrderAmount(List<OrderLineCommand> orderLineCommands) {
		BigDecimal amount = new BigDecimal(-1);
		try {
			if (null != orderLineCommands && orderLineCommands.size() > 0) {
				amount = new BigDecimal(0);
				for (OrderLineCommand orderLine : orderLineCommands) {
					Sku sku = skuDao.findSkuByExtentionCode(orderLine.getExtentionCode());
					amount = amount.add(sku.getSalePrice().multiply(new BigDecimal(orderLine.getCount())));
				}
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return amount;
		} catch (Exception e) {
			return new BigDecimal(-1);
		}
	}

	@Override
	public Integer saveReturnOrder(ReturnOrderCommand returnOrderCommand) {
		// 判断订单号的有效性
		judgeOrderIfExist(returnOrderCommand.getOrderCode());
		ReturnOrderApp returnOrderApp = new ReturnOrderApp();
		// 将returnOrderCommand对象转换为returnOrderapp对象
		returnOrderApp = (ReturnOrderApp) ConvertUtils.convertFromTarget(returnOrderApp, returnOrderCommand);
		returnOrderApp = sdkReturnOrderDao.save(returnOrderApp);
		if (null != returnOrderApp)
			return SUCCESS;
		return FAILURE;
	}

	@Override
	public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand) {
		if (null == cancelOrderCommand)
			return FAILURE;
		// 判断订单是否存在
		SalesOrderCommand order = judgeOrderIfExist(cancelOrderCommand.getOrderCode());
		Integer orderStatus = order.getLogisticsStatus();
		if (com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_CANCELED.equals(orderStatus) || com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED.equals(orderStatus))
			throw new BusinessException(Constants.ORDER_ALREADY_CANCEL);
		if (com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED.equals(orderStatus))
			throw new BusinessException(Constants.ORDER_ALREADY_COMPELETE);
		// 判断该订单是否已经申请了取消订单
		CancelOrderCommand cancelOrdApp = sdkCancelOrderDao.findCancelOrderAppByCode(cancelOrderCommand.getOrderCode());
		if (null != cancelOrdApp)// 该订单已经申请了取消订单，则不允许再次申请
			throw new BusinessException(Constants.ORDER_ALREADY_APPLY_CANCEL);
		CancelOrderApp cancelOrderApp = new CancelOrderApp();
		// 将 CancelOrderCommand对象转换为CancelOrderApp对象
		cancelOrderApp = (CancelOrderApp) ConvertUtils.convertFromTarget(cancelOrderApp, cancelOrderCommand);
		cancelOrderApp = sdkCancelOrderDao.save(cancelOrderApp);
		if (null != cancelOrderApp)
			return SUCCESS;
		return FAILURE;
	}

	@Override
	public Integer updateOrderLogisticsStatus(String code, Integer logisticsStatus) {
		SalesOrderCommand salesOrder = judgeOrderIfExist(code);
		Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
		if (retval > 0) {
			OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
			return SUCCESS;
		}
		return FAILURE;
	}

	@Override
	@Deprecated
	public Integer cancelOrderLogisticsStatus(String code, Integer logisticsStatus) {
		SalesOrderCommand salesOrder = judgeOrderIfExist(code);
		Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
		if (retval > 0) {
			OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
			// 保存OMS消息发送记录(订单状态同步)
			sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
			return SUCCESS;
		}
		return FAILURE;
	}
	
	@Override
	public Integer cancelOrderLogisticsStatus(String code, Integer logisticsStatus, Boolean isOms) {
		SalesOrderCommand salesOrder = judgeOrderIfExist(code);
		Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
		if (retval > 0) {
			OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
			// 保存OMS消息发送记录(订单状态同步)
			if(!Boolean.TRUE.equals(isOms)) {
				sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
			}
			return SUCCESS;
		}
		return FAILURE;
	}
	
	@Override
	public void sendEmailOfOrder(String code, String emailTemplete) {
		String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
		if (isSendEmail != null && isSendEmail.equals("true")) {
			sendEmail(code, emailTemplete);
		}
	}
	@Transactional(readOnly=true)
	private void sendEmail(String code, String emailTemplete) {
		Map<String, Object> dataMap = new HashMap<String, Object>();

		SalesOrderCommand salesOrderCommand = findOrderByCode(code, 1);

		MemberPersonalData memberPersonalData = null;
		if (Validator.isNotNullOrEmpty(salesOrderCommand.getMemberId())) {
			memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrderCommand.getMemberId());
		}

		String nickName = "";

		String email = salesOrderCommand.getEmail();

		if (Validator.isNotNullOrEmpty(memberPersonalData)) {
			nickName = memberPersonalData.getNickname();
			if (Validator.isNullOrEmpty(email)) {
				email = memberPersonalData.getEmail();
			}
		}

		if (Validator.isNullOrEmpty(nickName))
			nickName = salesOrderCommand.getMemberName();

		// 游客用收货人
		if (Validator.isNullOrEmpty(nickName))
			nickName = salesOrderCommand.getName();

		if (Validator.isNullOrEmpty(email))
			return;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar calendar = Calendar.getInstance();

		dataMap.put("nickName", nickName);
		dataMap.put("createDateOfAll", sdf.format(salesOrderCommand.getCreateTime()));
		dataMap.put("createDateOfSfm", sdf1.format(salesOrderCommand.getCreateTime()));
		dataMap.put("orderCode", salesOrderCommand.getCode());
		dataMap.put("receiveName", salesOrderCommand.getName());
		dataMap.put("ssqStr", salesOrderCommand.getProvince() + salesOrderCommand.getCity() + salesOrderCommand.getArea());
		dataMap.put("address", salesOrderCommand.getAddress());

		dataMap.put("mobile", Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile() : salesOrderCommand.getTel());

		dataMap.put("sumItemFee", salesOrderCommand.getTotal().add(salesOrderCommand.getDiscount()));
		dataMap.put("shipFee", salesOrderCommand.getActualFreight());
		dataMap.put("offersTotal", salesOrderCommand.getDiscount());
		dataMap.put("sumPay", salesOrderCommand.getTotal().add(salesOrderCommand.getActualFreight()));
		dataMap.put("itemLines", salesOrderCommand.getOrderLines());
		dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));

		dataMap.put("pageUrlBase", pageUrlBase);
		dataMap.put("imgDomainUrl", imgDomainUrl);
		dataMap.put("logisticsProvider", salesOrderCommand.getLogisticsProviderName());
		dataMap.put("transCode", salesOrderCommand.getTransCode());
		dataMap.put("nowDay", sdf1.format(calendar.getTime()));

		/** 扩展点 如 商城需要需要加入特殊字段 各自实现 **/
		if (null != salesOrderHandler) {
			dataMap = salesOrderHandler.getEmailData(salesOrderCommand, dataMap, emailTemplete);
		}

		EmailEvent emailEvent = new EmailEvent(this, email, emailTemplete, dataMap);

		eventPublisher.publish(emailEvent);

	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> searchParam) {

		return sdkReturnOrderDao.findReturnOrdersByQueryMapWithPage(page, sorts, searchParam);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> searchParam) {
		return sdkCancelOrderDao.findCancelOrdersByQueryMapWithPage(page, sorts, searchParam);
	}

	@Override
	public Integer updateCancelOrders(Long handleId, String code, Integer status, String feedback) {
		Integer count = sdkCancelOrderDao.updateCancelOrders(handleId, code, status, feedback, new Date());
		if (count == 1)
			return SUCCESS;
		return FAILURE;
	}

	@Override
	public Integer updateReturnOrders(Long handleId, String code, Integer status, String feedback) {
		Integer count = sdkReturnOrderDao.updateReturnOrders(handleId, code, status, feedback, new Date());
		if (count == 1)
			return SUCCESS;
		return FAILURE;
	}

	@Override
	public Integer updateOrders(SalesOrderCommand salesOrderCommand) {
		if (null == salesOrderCommand)
			return FAILURE;
		String orderCode = salesOrderCommand.getCode();
		SalesOrderCommand order = judgeOrderIfExist(orderCode);
		salesOrderCommand.setId(order.getId());
		Integer orderStatus = order.getLogisticsStatus();
		if (orderStatus == com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED)
			throw new BusinessException(Constants.ORDER_ALREADY_COMPELETE);
		com.baozun.nebula.model.salesorder.SalesOrder salesOrder = sdkOrderDao.getByPrimaryKey(order.getId());
		// 将salesOrderCommand转换为SalesOrder对象
		salesOrder = (com.baozun.nebula.model.salesorder.SalesOrder) ConvertUtils.convertFromTarget(salesOrder, salesOrderCommand);
		salesOrder.setModifyTime(new Date());
		salesOrder = sdkOrderDao.save(salesOrder);
		if (null == salesOrder) {
			throw new BusinessException(Constants.ORDER_UPDATE_FAILURE);
		}
		ConsigneeCommand consigneeCommand = sdkConsigneeDao.findConsigneeOrderId(salesOrder.getId());
		if (null != consigneeCommand) {
			Consignee consignee = sdkConsigneeDao.getByPrimaryKey(consigneeCommand.getId());
			salesOrderCommand.setId(consignee.getId());
			// 将salesOrderCommand中的收货人信息转换为Consignee对象
			consignee = (Consignee) ConvertUtils.convertFromTarget(consignee, salesOrderCommand);
			consignee.setModifyTime(new Date());
			consignee = sdkConsigneeDao.save(consignee);
			if (null == consignee) {
				throw new BusinessException(Constants.ORDER_CONSIGNEE_UPDATE_FAILURE);
			}
		}
		return SUCCESS;
	}

	private OrderStatusLog saveOrderStatusLog(Long orderId, Long handleId, Integer beforeStatus, Integer afterStatus) {
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setAfterStatus(afterStatus);
		orderStatusLog.setBeforeStatus(beforeStatus);
		orderStatusLog.setOperatorId(String.valueOf(handleId));
		orderStatusLog.setOrderId(orderId);
		OrderStatusLog res = sdkOrderStatusLogDao.save(orderStatusLog);
		return res;
	}

	/**
	 * 判断订单是否存在
	 * 
	 * @param orderCode
	 */
	@Transactional(readOnly=true)
	private SalesOrderCommand judgeOrderIfExist(String orderCode) {
		if (null == orderCode || orderCode.length() == 0 || "".equals(orderCode))
			throw new BusinessException(Constants.ORDERCODE_NOT_NULL);
		SalesOrderCommand order = sdkOrderDao.findOrderByCode(orderCode, null);
		if (null == order)// 订单不存在
			throw new BusinessException(Constants.ORDER_NOT_EXIST);
		return order;
	}


	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return sdkOrderDao.findItemSkuListByQueryMapWithPage(page, sorts, paraMap);
	}

	private OrderCodeCreatorManager getDefaultCreator() {
		return new DefaultOrderCodeCreatorManager();
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return sdkOrderDao.findNotEvaultionOrderLineQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public Integer updateOrderLineEvaulationStatus(Long skuId, Long orderId) {
		return sdkOrderLineDao.updateOrderLineEvaulationStatusByOrderLineid(skuId, orderId);
	}

	@Override
	@Transactional(readOnly=true)
	public SalesOrderCommand findOrderById(Long id, Integer type) {
		// 包含订单基本信息和收货信息
		SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderById(id, type);

		if (null == salesOrderCommand || null == type) {
			return salesOrderCommand;
		} else {
			// 订单支付信息
			List<PayInfoCommand> payInfos = sdkPayInfoDao.findPayInfoCommandByOrderId(salesOrderCommand.getId());
			salesOrderCommand.setPayInfo(payInfos);
			// 订单行信息
			List<Long> orderIds = new ArrayList<Long>();
			orderIds.add(salesOrderCommand.getId());

			List<OrderLineCommand> orderLines = sdkOrderLineDao.findOrderDetailListByOrderIds(orderIds);
			salesOrderCommand.setOrderLines(orderLines);
			// 订单行促销
			List<OrderPromotionCommand> orderPrm = sdkOrderPromotionDao.findOrderProInfoByOrderId(salesOrderCommand.getId(), null);
			salesOrderCommand.setOrderPromotions(orderPrm);
		}

		return salesOrderCommand;
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<CancelOrderCommand> findCanceledOrderList(Page page, Long memberId) {
		return sdkOrderDao.findCanceledOrderList(page, memberId);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page, Long memberId) {
		return sdkOrderDao.findReturnedOrderList(page, memberId);
	}

	@Override
	public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand) {
		if (null == returnOrderCommand)
			return FAILURE;
		// 判断订单是否存在
		SalesOrderCommand order = judgeOrderIfExist(returnOrderCommand.getOrderCode());
		Integer orderStatus = order.getLogisticsStatus();
		if (!orderStatus.equals(com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED)) // 非完成的订单，不能申请退换货
			throw new BusinessException(Constants.ORDER_NOT_COMPELETE);

		ReturnOrderCommand returnedOrder = sdkReturnOrderDao.findReturnOrderAppByCode(returnOrderCommand.getOrderCode());
		if (null != returnedOrder)// 该订单已经申请了退换货，则不允许再次申请
			throw new BusinessException(Constants.ORDER_ALREADY_APPLY_RETURN);
		ReturnOrderApp returnOrderApp = new ReturnOrderApp();
		// 将 CancelOrderCommand对象转换为CancelOrderApp对象
		returnOrderApp = (ReturnOrderApp) ConvertUtils.convertFromTarget(returnOrderApp, returnOrderCommand);
		returnOrderApp = sdkReturnOrderDao.save(returnOrderApp);
		if (null != returnOrderApp)
			return SUCCESS;
		return FAILURE;
	}

	@Override
	@Transactional(readOnly=true)
	public SalesOrderCommand findOrderByLineId(Long orderLineId) {
		return sdkOrderDao.findOrderByLineId(orderLineId);
	}

	@Override
	public void updateOrderFinancialStatusById(List<Long> orderIds, Integer financialStatus) {
		Integer count = sdkOrderDao.updateOrderFinancialStatusById(orderIds, financialStatus);
		if (count != orderIds.size()) {
			throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { count });
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Integer findCountOfOrder(List<Integer> status, Long memberId) {
		Integer count = sdkOrderDao.findCountOfOrder(status, memberId);
		return count;
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderLineCommand> findOrderDetailList(Long orderId) {
		return sdkOrderLineDao.findOrderDetailList(orderId);
	}

	/**
	 * 获取session中的用户信息
	 * 
	 * @param request
	 * @return
	 */
	private UserDetails getUserDetails(HttpServletRequest request) {
		Object userInfo = request.getSession().getAttribute(Constants.MEMBER_CONTEXT);
		if (null == userInfo)
			throw new BusinessException(Constants.USER_USER_NOTFOUND);
		UserDetails userDetails = (UserDetails) ConvertUtils.convertTwoObject(new UserDetails(), userInfo);
		return userDetails;
	}

	/**
	 * 游客的memboIds
	 * 
	 * @return
	 */
	private Set<String> getMemboIds() {
		return sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null);
	}

	/**
	 * 获取购物车中的所有店铺id
	 * 
	 * @param lines
	 * @return
	 */
	public List<Long> getShopIds(List<ShoppingCartLineCommand> lines) {
		if (null == lines || lines.size() == 0)
			return null;
		Set<Long> ids = new HashSet<Long>();
		for (ShoppingCartLineCommand line : lines) {
			  ids.add(line.getShopId());
		}
		List<Long> shopIds = new ArrayList<Long>(ids);
		return shopIds;
	}

	/**
	 * 根据购物车行获取ItemForCheckCommand集合
	 * 
	 * @param lines
	 * @return
	 */
	protected Set<String> getItemComboIds(List<ShoppingCartLineCommand> lines) {
		Set<String> set = new HashSet<String>();
		if (null != lines && lines.size() > 0) {
			for (ShoppingCartLineCommand line : lines) {
				if (line.getComboIds() != null)
					set.addAll(line.getComboIds());
			}
		}
		return set;
	}

	@Override
	@Transactional(readOnly=true)
	public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts, Long memberId) {
		List<SalesOrderCommand> salesOrderComList = sdkOrderDao.findNoPayOrders(sorts, memberId);
		List<Long> idList = new ArrayList<Long>(salesOrderComList.size());
		for (SalesOrderCommand cmd : salesOrderComList) {
			idList.add(cmd.getId());
		}
		List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
		for (SalesOrderCommand order : salesOrderComList) {
			Long orderId = order.getId();
			List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
			for (OrderLineCommand line : allLineList) {
				if (line.getOrderId().equals(orderId)) {
					orderLineList.add(line);
					// 属性list
					String properties = line.getSaleProperty();
					List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
					line.setSkuPropertys(propList);
				}
			}
			order.setOrderLines(orderLineList);
		}
		return salesOrderComList;
	}

	@Override
	@Transactional(readOnly=true)
	public List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts, Map<String, Object> searchParam) {
		List<SalesOrderCommand> list = sdkOrderDao.findToBeCancelOrders(sorts, searchParam);
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderPromotionCommand> findOrderPormots(String orderCode) {
		SalesOrderCommand so = sdkOrderDao.findOrderByCode(orderCode, null);
		List<OrderPromotionCommand> orderPromots = null;
		if (so != null) {
			orderPromots = sdkOrderPromotionDao.findOrderProsInfoByOrderId(so.getId());
		}
		return orderPromots;
	}

	@Override
	@Transactional(readOnly=true)
	public PromotionCouponCode validCoupon(String couponCode) {
		if (StringUtils.isNotBlank(couponCode)) {
			PromotionCouponCode promotionCouponCode = promotionCouponCodeDao.findPromotionCouponCodeBycoupon(couponCode);
			if (promotionCouponCode != null)
				return promotionCouponCode;
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public OrderStatusLogCommand findOrderStatusLogById(Long id) {
		return sdkOrderStatusLogDao.findOrderStatusLogById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(List<String> extentionList, Date startTime) {
		return sdkOrderDao.findOrderByExntentionListAndOrderCreateTime(extentionList, startTime);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(List<String> extentionList, List<Integer> orderStatus) {
		return sdkOrderDao.findOrderByExntentionListAndOrderStatus(extentionList, orderStatus);
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderLineCommand> findOrderDetailListByOrderIds(List<Long> orderIdList) {
		return sdkOrderLineDao.findOrderDetailListByOrderIds(orderIdList);
	}

	@Override
	public void updateLogisticsInfo(String orderCode, BigDecimal actualFreight, String logisticsProviderCode, String logisticsProviderName, String transCode, Date modifyTime) {
		sdkOrderDao.updateLogisticsInfo(orderCode, actualFreight, logisticsProviderCode, logisticsProviderName, transCode, modifyTime);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(Map<String, Object> paraMap) {
		return promotionCouponCodeDao.findPromotionCouponCodeListByQueryMap(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public List<DistributionMode> getAllDistributionMode() {
		List<DistributionMode> distributionModeList = distributionModeDao.getAllDistributionMode();
		return distributionModeList;
	}

}
