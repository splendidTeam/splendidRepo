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
package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.coupon.CouponDao;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.payment.PayInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.dao.salesorder.SdkCancelOrderDao;
import com.baozun.nebula.dao.salesorder.SdkConsigneeDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.dao.salesorder.SdkOrderStatusLogDao;
import com.baozun.nebula.dao.salesorder.SdkReturnOrderDao;
import com.baozun.nebula.event.EmailEvent;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.promotion.PromotionCouponCode;
import com.baozun.nebula.model.salesorder.CancelOrderApp;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.OrderPromotion;
import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.model.salesorder.PayInfo;
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
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.handler.SalesOrderHandler;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkConsigneeManager;
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkOrderLineManager;
import com.baozun.nebula.sdk.manager.SdkOrderManager;
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoLogManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.MapUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * The Class OrderManagerImpl.
 */
@Transactional
@Service("sdkOrderService")
public class OrderManagerImpl implements OrderManager{

    /** The Constant log. */
    private static final Logger                      log             = LoggerFactory.getLogger(OrderManagerImpl.class);

    /** 程序返回结果 *. */
    private static final Integer                     SUCCESS         = 1;

    /** The Constant FAILURE. */
    private static final Integer                     FAILURE         = 0;

    /** The Constant SEPARATOR_FLAG. */
    private static final String                      SEPARATOR_FLAG  = "\\|\\|";

    /** The page url base. */
    @Value("#{meta['page.base']}")
    private String                                   pageUrlBase     = "";

    /** The img domain url. */
    @Value("#{meta['upload.img.domain.base']}")
    private String                                   imgDomainUrl    = "";

    /** The frontend base url. */
    @Value("#{meta['frontend.url']}")
    private String                                   frontendBaseUrl = "";

    /** The sdk cancel order dao. */
    @Autowired
    private SdkCancelOrderDao                        sdkCancelOrderDao;

    /** The sdk order dao. */
    @Autowired
    private SdkOrderDao                              sdkOrderDao;

    /** The sdk order manager. */
    @Autowired
    private SdkOrderManager                          sdkOrderManager;

    /** The sdk order promotion dao. */
    @Autowired
    private SdkOrderPromotionDao                     sdkOrderPromotionDao;

    /** The sdk order line dao. */
    @Autowired
    private SdkOrderLineDao                          sdkOrderLineDao;

    /** The sdk pay info dao. */
    @Autowired
    private PayInfoDao                               sdkPayInfoDao;

    /** The sdk return order dao. */
    @Autowired
    private SdkReturnOrderDao                        sdkReturnOrderDao;

    /** The sdk order status log dao. */
    @Autowired
    private SdkOrderStatusLogDao                     sdkOrderStatusLogDao;

    /** The sku dao. */
    @Autowired
    private SkuDao                                   skuDao;

    /** The sdk sku inventory manager. */
    @Autowired
    private SdkSkuInventoryManager                   sdkSkuInventoryManager;

    /** The order code creator. */
    @Autowired(required = false)
    private OrderCodeCreatorManager                  orderCodeCreator;

    /** The sdk consignee dao. */
    @Autowired
    private SdkConsigneeDao                          sdkConsigneeDao;

    /** The coupon dao. */
    @Autowired
    private CouponDao                                couponDao;

    /** The sdk pay code manager. */
    @Autowired
    private SdkPayCodeManager                        sdkPayCodeManager;

    /** The pay info dao. */
    @Autowired
    private PayInfoDao                               payInfoDao;

    /** The sdk pay info manager. */
    @Autowired
    private SdkPayInfoManager                        sdkPayInfoManager;

    /** The sdk pay info log manager. */
    @Autowired
    private SdkPayInfoLogManager                     sdkPayInfoLogManager;

    /** The distribution mode dao. */
    @Autowired
    private DistributionModeDao                      distributionModeDao;

    /** The sdk shopping cart manager. */
    @Autowired
    private SdkShoppingCartManager                   sdkShoppingCartManager;

    /** The promotion coupon code dao. */
    @Autowired
    private PromotionCouponCodeDao                   promotionCouponCodeDao;

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager                            sdkSkuManager;

    /** The sdk promotion calculation share to sku manager. */
    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    /** The sdk purchase rule filter manager. */
    @Autowired
    private SdkPurchaseLimitRuleFilterManager        sdkPurchaseRuleFilterManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager                       sdkMataInfoManager;

    /** The event publisher. */
    @Autowired
    private EventPublisher                           eventPublisher;

    /** The sdk member manager. */
    @Autowired
    private SdkMemberManager                         sdkMemberManager;

    /** The sdk effective manager. */
    @Autowired
    private SdkEffectiveManager                      sdkEffectiveManager;

    /** The sdk msg manager. */
    @Autowired
    private SdkMsgManager                            sdkMsgManager;

    /** The sdk order line manager. */
    @Autowired
    private SdkOrderLineManager                      sdkOrderLineManager;

    /** The sdk order line manager. */
    @Autowired
    private SdkConsigneeManager                      sdkConsigneeManager;

    /** The sales order handler. */
    @Autowired(required = false)
    private SalesOrderHandler                        salesOrderHandler;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByCode(java.lang.String, java.lang.Integer)
     */
    @Override
    @Transactional(readOnly = true)
    public SalesOrderCommand findOrderByCode(String code,Integer type){
        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderByCode(code, type);
        if (null == type)
            return salesOrderCommand;
        if (null == salesOrderCommand)
            return null;
        if (type == 1){
            // 订单支付信息
            List<PayInfoCommand> payInfos = sdkPayInfoDao.findPayInfoCommandByOrderId(salesOrderCommand.getId());
            // 订单行信息
            List<Long> orderIds = new ArrayList<Long>();
            orderIds.add(salesOrderCommand.getId());
            List<OrderLineCommand> orderLines = sdkOrderLineDao.findOrderDetailListByOrderIds(orderIds);
            for (OrderLineCommand orderLineCommand : orderLines){
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

        return salesOrderCommand;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.Set)
     */
    @Override
    public String saveOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos){
        if (salesOrderCommand == null || shoppingCartCommand == null){
            throw new BusinessException(Constants.SHOPCART_IS_NULL);
        }

        //去除抬头和未选中的商品
        refactoringShoppingCartCommand(shoppingCartCommand);

        // 下单之前的引擎检查
        createOrderDoEngineChck(salesOrderCommand.getMemberId(), memCombos, shoppingCartCommand);

        String order = saveOrderInfo(salesOrderCommand, shoppingCartCommand);
        // 没有成功保存订单
        if (order == null){
            log.warn("savedOrder returns null!");
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }
        return order;
    }

    /**
     * Refactoring shopping cart command.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     */
    protected void refactoringShoppingCartCommand(ShoppingCartCommand shoppingCartCommand){
        Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        Map<Long, ShoppingCartCommand> newShoppingCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();
        List<ShoppingCartLineCommand> newAllShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartByShopIdMap.entrySet()){
            ShoppingCartCommand shopShoppingCartCommand = entry.getValue();
            List<ShoppingCartLineCommand> lines = new ArrayList<ShoppingCartLineCommand>();
            for (ShoppingCartLineCommand shoppingCartLineCommand : shopShoppingCartCommand.getShoppingCartLineCommands()){
                // 排除是标题行 或者（是赠品 1需要用户选择 0未选中） 
                if (!(shoppingCartLineCommand.isCaptionLine() || (shoppingCartLineCommand.isGift()
                                && shoppingCartLineCommand.getGiftChoiceType() == 1 && (shoppingCartLineCommand.getSettlementState() == null
                                                || shoppingCartLineCommand.getSettlementState() == 0)))){
                    lines.add(shoppingCartLineCommand);
                    newAllShoppingCartLineCommandList.add(shoppingCartLineCommand);
                }
            }
            shopShoppingCartCommand.setShoppingCartLineCommands(lines);
            newShoppingCartByShopIdMap.put(entry.getKey(), shopShoppingCartCommand);
        }

        shoppingCartCommand.setShoppingCartByShopIdMap(newShoppingCartByShopIdMap);
        shoppingCartCommand.setShoppingCartLineCommands(newAllShoppingCartLineCommandList);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveManualOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public String saveManualOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand){
        if (salesOrderCommand == null || shoppingCartCommand == null){
            throw new BusinessException(Constants.SHOPCART_IS_NULL);
        }
        // 下单之前的库存检查
        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartCommand.getShoppingCartLineCommands()){
            Boolean retflag = sdkEffectiveManager.chckInventory(shoppingCartLine.getExtentionCode(), shoppingCartLine.getQuantity());
            if (!retflag){
                // 库存不足
                throw new BusinessException(
                                Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                new Object[] { shoppingCartLine.getItemName() });
            }
        }
        String subOrdinate = saveOrderInfo(salesOrderCommand, shoppingCartCommand);
        // 没有成功保存订单
        if (subOrdinate == null){
            log.warn("savedOrder returns null!");
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }
        return subOrdinate;
    }

    /**
     * 创建订单的引擎检查.
     *
     * @param memberId
     *            the member id
     * @param memCombos
     *            the mem combos
     * @param shoppingCartCommand
     *            the shopping cart command
     */
    private void createOrderDoEngineChck(Long memberId,Set<String> memCombos,ShoppingCartCommand shoppingCartCommand){
        // 引擎检查(限购、有效性检查、库存)
        Set<String> memboIds = null;
        if (null == memberId){
            // 游客
            memboIds = getMemboIds();
        }else{
            memboIds = memCombos;
        }
        List<Long> shopIds = getShopIds(shoppingCartCommand.getShoppingCartLineCommands());
        Set<String> itemComboIds = getItemComboIds(shoppingCartCommand.getShoppingCartLineCommands());
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(shopIds, memboIds, itemComboIds, new Date());
        if (null == purchaseLimitationList || purchaseLimitationList.size() == 0)
            purchaseLimitationList = new ArrayList<LimitCommand>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()){
            for (ShoppingCartLineCommand shoppingCartLine : entry.getValue().getShoppingCartLineCommands()){
                //直推礼品不做校验
                if (!isNoNeedChoiceGift(shoppingCartLine)){
                    sdkEngineManager.doEngineCheck(shoppingCartLine, false, shoppingCartCommand, purchaseLimitationList);
                }
            }
        }

        //限购校验失败
        List<ShoppingCartLineCommand> errorLineList = sdkEngineManager.doEngineCheckLimit(shoppingCartCommand, purchaseLimitationList);
        if (null != errorLineList && errorLineList.size() > 0){
            StringBuffer errorItemName = new StringBuffer();
            List<Long> itemList = new ArrayList<Long>();
            for (ShoppingCartLineCommand cartLine : errorLineList){
                if ("".equals(errorItemName.toString())){
                    errorItemName.append(cartLine.getItemName());
                    itemList.add(cartLine.getItemId());
                }else{
                    if (!itemList.contains(cartLine.getItemId())){
                        errorItemName.append(",").append(cartLine.getItemName());
                        itemList.add(cartLine.getSkuId());
                    }
                }
            }
            throw new BusinessException(Constants.THE_ORDER_CONTAINS_LIMIT_ITEM, new Object[] { errorItemName.toString() });
        }
    }

    /**
     * 保存订单信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the string
     */
    private String saveOrderInfo(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand){
        // 购物车行
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        // shoppingCartLineCommandMap
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = MapUtil
                        .extractSubMap(shopIdAndShoppingCartCommandMap, "shoppingCartLineCommands", Long.class);

        // shoppingCartPromotionBriefMap
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCartCommand, shoppingCartCommand.getCartPromotionBriefList());

        Map<Long, List<PromotionSKUDiscAMTBySetting>> shopIdAndPromotionSKUDiscAMTBySettingMap = CollectionsUtil
                        .group(promotionSKUDiscAMTBySettingList, "shopId");

        // shopCartCommandByShopMap
        List<ShopCartCommandByShop> shopCartCommandByShopList = shoppingCartCommand.getSummaryShopCartList();
        Map<Long, ShopCartCommandByShop> shopIdAndShopCartCommandByShopMap = CollectionsUtil.groupOne(shopCartCommandByShopList, "shopId");

        //*****************************************************************************************

        // 合并付款
        String subOrdinate = orderCodeCreator.createOrderSerialNO();
        if (subOrdinate == null){
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }

        String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        BigDecimal paySum = getPaySum(salesOrderCommand, shoppingCartCommand);

        List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : shopIdAndShoppingCartLineCommandListMap.entrySet()){
            Long shopId = entry.getKey();
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = shopIdAndShoppingCartLineCommandListMap.get(shopId);

            List<PromotionSKUDiscAMTBySetting> psdabsList = shopIdAndPromotionSKUDiscAMTBySettingMap.get(shopId);
            ShopCartCommandByShop shopCartCommandByShop = shopIdAndShopCartCommandByShopMap.get(shopId);

            //***************************************************************************************
            // 根据shopId保存订单概要
            SalesOrder salesOrder = sdkOrderManager.savaOrder(shopId, salesOrderCommand, shopCartCommandByShop);
            Long orderId = salesOrder.getId();

            // 保存订单行、订单行优惠
            savaOrderLinesAndPromotions(salesOrderCommand, orderId, shoppingCartLineCommandList, psdabsList);

            // 保存支付详细
            savePayInfoAndPayInfoLog(salesOrderCommand, subOrdinate, salesOrder, shopId);

            // 保存收货人信息
            sdkConsigneeManager.saveConsignee(orderId, shopId, salesOrderCommand);

            // 保存OMS消息发送记录(销售订单信息推送给SCM)
            sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ORDER_SEND, orderId, null);

            // 封装发送邮件数据
            if (isSendEmail != null && isSendEmail.equals("true")){
                Map<String, Object> dataMap = getDataMap(
                                subOrdinate,
                                salesOrder,
                                salesOrderCommand,
                                shoppingCartLineCommandList,
                                shopCartCommandByShop,
                                psdabsList);
                if (dataMap != null)
                    dataMapList.add(dataMap);
            }

            // 扣减库存
            sdkSkuInventoryManager.deductSkuInventory(shoppingCartLineCommandList);
        }

        //*************************************************************************************

        // 保存支付流水
        sdkPayCodeManager.savaPayCode(paySum, subOrdinate);

        //*************************************************************************************
        // 优惠券状体置为已使用 isUsed = 1
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getCouponCodes())){
            for (CouponCodeCommand couponCodeCommand : salesOrderCommand.getCouponCodes()){
                List<String> list = new ArrayList<String>();

                if (!couponCodeCommand.getIsOut()){
                    list.add(couponCodeCommand.getCouponCode());
                }

                int res = promotionCouponCodeDao.comsumePromotionCouponCodeByCouponCodes(list);
                if (res != list.size()){
                    if (res < list.size()){
                        throw new BusinessException(Constants.COUPON_IS_USED);
                    }else{
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

        return subOrdinate;
    }

    /**
     * 获得 pay sum.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the pay sum
     */
    private BigDecimal getPaySum(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand){
        BigDecimal paySum = shoppingCartCommand.getCurrentPayAmount();
        List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
                BigDecimal payMoney = new BigDecimal(strs[2]);
                paySum = paySum.subtract(payMoney);
            }
        }
        return paySum;
    }

    /**
     * Save pay info and pay info log.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param subOrdinate
     *            the sub ordinate
     * @param salesOrder
     *            the sales order
     * @param shopId
     *            the shop id
     */
    protected void savePayInfoAndPayInfoLog(SalesOrderCommand salesOrderCommand,String subOrdinate,SalesOrder salesOrder,Long shopId){
        List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
        BigDecimal payMainMoney = salesOrder.getTotal().add(salesOrder.getActualFreight());
        // 除主支付方式之外的付款
        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 3){
                    PayInfo res = savePayInfo(salesOrder, strs);
                    payMainMoney = payMainMoney.subtract(res.getPayMoney());
                }
            }
        }
        PayInfo payInfo = sdkPayInfoManager.savePayInfoOfPayMain(salesOrderCommand, salesOrder.getId(), payMainMoney);
        sdkPayInfoLogManager.savePayInfoLogOfPayMain(salesOrderCommand, subOrdinate, payInfo);
    }

    /**
     * Save pay info.
     *
     * @param salesOrder
     *            the sales order
     * @param strs
     *            the strs
     * @return the pay info
     */
    private PayInfo savePayInfo(SalesOrder salesOrder,String[] strs){
        PayInfo payInfo = new PayInfo();

        payInfo.setOrderId(salesOrder.getId());
        payInfo.setPaySuccessStatus(true);
        payInfo.setPayType(Integer.parseInt(strs[1]));
        BigDecimal payMoney = new BigDecimal(strs[2]);
        payInfo.setPayMoney(payMoney);
        payInfo.setPayNumerical(payMoney);

        payInfo.setCreateTime(new Date());
        // 付款时间
        payInfo.setModifyTime(new Date());
        return payInfoDao.save(payInfo);
    }

    /**
     * Send email of create order.
     *
     * @param dataMapList
     *            the data map list
     */
    protected void sendEmailOfCreateOrder(List<Map<String, Object>> dataMapList){
        if (Validator.isNotNullOrEmpty(dataMapList)){
            for (Map<String, Object> dataMap : dataMapList){
                EmailEvent emailEvent = new EmailEvent(this, dataMap.get("email").toString(), EmailConstants.CREATE_ORDER_SUCCESS, dataMap);
                eventPublisher.publish(emailEvent);
            }
        }
    }

    /**
     * 获得 data map.
     *
     * @param subOrdinate
     *            the sub ordinate
     * @param salesOrder
     *            the sales order
     * @param salesOrderCommand
     *            the sales order command
     * @param sccList
     *            the scc list
     * @param shopCartCommandByShop
     *            the shop cart command by shop
     * @param psdabsList
     *            the psdabs list
     * @return the data map
     */
    protected Map<String, Object> getDataMap(
                    String subOrdinate,
                    SalesOrder salesOrder,
                    SalesOrderCommand salesOrderCommand,
                    List<ShoppingCartLineCommand> sccList,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> psdabsList){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        // 后台下单 并且填写了 邮件地址
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getMemberId())){
            MemberPersonalData memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrder.getMemberId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

            String nickName = "";

            String email = salesOrderCommand.getEmail();

            if (Validator.isNotNullOrEmpty(memberPersonalData)){
                nickName = memberPersonalData.getNickname();
                if (Validator.isNullOrEmpty(email)){
                    email = memberPersonalData.getEmail();
                }
            }

            if (Validator.isNullOrEmpty(nickName))
                nickName = salesOrderCommand.getName();

            if (Validator.isNullOrEmpty(email))
                return null;

            // 获取付款地址
            if (salesOrderCommand.getIsBackCreateOrder()
                            && !salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)){
                String payUrlPrefix = sdkMataInfoManager.findValue(MataInfo.PAY_URL_PREFIX);
                String payUrl = frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
                dataMap.put("isShowPayButton", true);
                dataMap.put("payUrl", payUrl);
            }else{
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

            dataMap.put(
                            "mobile",
                            Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile()
                                            : salesOrderCommand.getTel());

            dataMap.put("sumItemFee", salesOrder.getTotal().add(salesOrder.getDiscount()));
            dataMap.put("shipFee", salesOrder.getActualFreight());
            dataMap.put("offersTotal", salesOrder.getDiscount());
            dataMap.put("sumPay", salesOrder.getTotal().add(salesOrder.getActualFreight()));
            dataMap.put("itemLines", sccList);
            dataMap.put("payment", getPaymentName(salesOrderCommand.getPayment()));
            dataMap.put("pageUrlBase", pageUrlBase);
            dataMap.put("imgDomainUrl", imgDomainUrl);
            dataMap.put("email", email);
        }else{
            if (Validator.isNullOrEmpty(salesOrderCommand.getEmail())){
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

            // 获取付款地址
            if (salesOrderCommand.getIsBackCreateOrder()
                            && !salesOrderCommand.getPayment().toString().equals(SalesOrder.SO_PAYMENT_TYPE_COD)){
                String payUrlPrefix = sdkMataInfoManager.findValue(MataInfo.PAY_URL_PREFIX);
                String payUrl = frontendBaseUrl + payUrlPrefix + "?code=" + subOrdinate;
                dataMap.put("isShowPayButton", true);
                dataMap.put("payUrl", payUrl);
            }else{
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

            dataMap.put(
                            "mobile",
                            Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile()
                                            : salesOrderCommand.getTel());

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
        if (null != salesOrderHandler){
            dataMap = salesOrderHandler.getEmailDataOfCreateOrder(
                            subOrdinate,
                            salesOrder,
                            salesOrderCommand,
                            sccList,
                            shopCartCommandByShop,
                            psdabsList,
                            dataMap);
        }

        return dataMap;

    }

    /**
     * 返回支付方式.
     *
     * @param payment
     *            the payment
     * @return the payment name
     */
    private String getPaymentName(Integer payment){
        String name = "支付宝";
        if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_COD)){
            name = "货到付款";
        }else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_NETPAY)){
            name = "网银在线";
        }else if (payment == Integer.parseInt(SalesOrder.SO_PAYMENT_TYPE_ALIPAY)){
            name = "支付宝";
        }
        return name;
    }

    /**
     * 保存订单行.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param orderId
     *            the order id
     * @param shoppingCartLineCommandList
     *            the scc list
     * @param promotionSKUDiscAMTBySettingList
     *            the psdabs list
     */
    protected void savaOrderLinesAndPromotions(
                    SalesOrderCommand salesOrderCommand,
                    Long orderId,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            OrderLine orderLine = sdkOrderLineManager.saveOrderLine(orderId, shoppingCartLineCommand);
            if (orderLine == null){
                continue;
            }

            if (Validator.isNullOrEmpty(promotionSKUDiscAMTBySettingList)){
                continue;
            }

            for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
                boolean giftMark = promotionSKUDiscAMTBySetting.getGiftMark();
                //0代表赠品 1代表主卖品
                Integer type = !giftMark ? 1 : 0;

                // 非免运费
                if (!promotionSKUDiscAMTBySetting.getFreeShippingMark()
                                && promotionSKUDiscAMTBySetting.getSkuId().equals(orderLine.getSkuId())
                                && orderLine.getType().equals(type)){
                    savaOrderPromotion(orderId, promotionSKUDiscAMTBySetting, orderLine, salesOrderCommand);
                }
            }
        }
        // 免运费
        if (Validator.isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            for (PromotionSKUDiscAMTBySetting promo : promotionSKUDiscAMTBySettingList){
                if (promo.getFreeShippingMark()){
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
                    if (promo.getCouponCodes() != null){
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

    /**
     * Sava order promotion.
     *
     * @param orderId
     *            the order id
     * @param promotionSKUDiscAMTBySetting
     *            the promo
     * @param orderLine
     *            the res
     * @param salesOrderCommand
     *            the sales order command
     */
    protected void savaOrderPromotion(
                    Long orderId,
                    PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting,
                    OrderLine orderLine,
                    SalesOrderCommand salesOrderCommand){
        OrderPromotion orderPromotion = new OrderPromotion();
        // 订单id
        orderPromotion.setOrderId(orderId);
        // 订单行
        orderPromotion.setOrderLineId(orderLine.getId());
        // 活动id
        orderPromotion.setActivityId(promotionSKUDiscAMTBySetting.getPromotionId());
        // 促销码 (暂时没用)
        orderPromotion.setPromotionNo(promotionSKUDiscAMTBySetting.getPromotionId().toString());
        // 促销类型
        orderPromotion.setPromotionType(promotionSKUDiscAMTBySetting.getPromotionType());
        // 折扣金额
        orderPromotion.setDiscountAmount(promotionSKUDiscAMTBySetting.getDiscountAmount());
        // 是否运费折扣
        orderPromotion.setIsShipDiscount(false);
        // 优惠券
        Set<String> couponCodesSet = promotionSKUDiscAMTBySetting.getCouponCodes();
        Set<String> newSet = new HashSet<String>();
        if (couponCodesSet != null){
            List<String> list = new ArrayList<String>(couponCodesSet);
            for (String couponcode : list){
                if (couponcode.equals(CouponCodeCommand.BRUSHHEAD_COUPON)){
                    for (CouponCodeCommand couponCodeCommand : salesOrderCommand.getCouponCodes()){
                        if (couponCodeCommand.getIsOut()){
                            newSet.add(couponCodeCommand.getCouponCode());
                        }
                    }
                }else{
                    newSet.add(couponcode);
                }
            }
            orderPromotion.setCoupon(newSet.toString());
        }

        // 描述 ...name
        orderPromotion.setDescribe(promotionSKUDiscAMTBySetting.getPromotionName());
        // 是否基于整单
        orderPromotion.setBaseOrder(promotionSKUDiscAMTBySetting.getBaseOrder());
        sdkOrderPromotionDao.save(orderPromotion);
    }

    /**
     * 检查优惠券是否有效.
     *
     * @param memberId
     *            the member id
     * @param couponNo
     *            the coupon no
     */
    @Transactional(readOnly = true)
    private void chckCouponNo(Long memberId,String couponNo){
        if (StringUtils.isNotBlank(couponNo)){
            CouponCommand couponCommand = couponDao.findCouponCommandByMemberIdAndCardNo(memberId, couponNo);
            if (null == couponCommand){
                throw new BusinessException(Constants.COUPON_IS_NOT_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrdersWithOutPage(loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrdersWithOutPage(Sort[] sorts,Map<String, Object> searchParam){
        List<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrdersWithOutPage(sorts, searchParam);

        if (null == salesOrderPage){
            return null;
        }else{
            List<Long> idList = new ArrayList<Long>(salesOrderPage.size());
            for (SalesOrderCommand cmd : salesOrderPage){
                idList.add(cmd.getId());
            }

            List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
            for (SalesOrderCommand order : salesOrderPage){
                Long orderId = order.getId();
                List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
                for (OrderLineCommand line : allLineList){
                    if (line.getOrderId().equals(orderId)){
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrders(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<SalesOrderCommand> findOrders(Page page,Sort[] sorts,Map<String, Object> searchParam){
        Pagination<SalesOrderCommand> salesOrderPage = sdkOrderDao.findOrders(page, sorts, searchParam);

        if (null != salesOrderPage.getItems()){
            List<Long> idList = new ArrayList<Long>(salesOrderPage.getItems().size());
            for (SalesOrderCommand cmd : salesOrderPage.getItems()){
                idList.add(cmd.getId());
            }

            List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
            for (SalesOrderCommand order : salesOrderPage.getItems()){
                Long orderId = order.getId();
                List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
                for (OrderLineCommand line : allLineList){
                    if (line.getOrderId().equals(orderId)){
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#savePayOrder(java.lang.String, com.baozun.nebula.sdk.command.PayInfoCommand)
     */
    @Override
    public PayInfoCommand savePayOrder(String code,PayInfoCommand payInfoCommand){
        // 先根据订单code查询订单是否存在

        SalesOrderCommand salesOrder = judgeOrderIfExist(code);

        PayInfo payInfo = (PayInfo) ConvertUtils.convertFromTarget(new PayInfo(), payInfoCommand);
        payInfo.setOrderId(salesOrder.getId());
        payInfo.setPayInfo(payInfoCommand.getPayInfo());
        // 保存订单详细
        payInfo = sdkPayInfoDao.save(payInfo);

        if (null == payInfo){
            log.warn(" payInfoDao.save(payInfo) returns null");
            return null;
        }

        return (PayInfoCommand) ConvertUtils.convertTwoObject(new PayInfoCommand(), payInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderFinancialStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    public SalesOrderCommand updateOrderFinancialStatus(String code,Integer financialStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderFinancialStatus(code, financialStatus, new Date());
        if (retval > 0){
            saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getFinancialStatus(), financialStatus);
            salesOrder.setFinancialStatus(financialStatus);
        }else{
            return null;
        }
        return salesOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderAmount(java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal findOrderAmount(List<OrderLineCommand> orderLineCommands){
        BigDecimal amount = new BigDecimal(-1);
        try{
            if (null != orderLineCommands && orderLineCommands.size() > 0){
                amount = new BigDecimal(0);
                for (OrderLineCommand orderLine : orderLineCommands){
                    Sku sku = skuDao.findSkuByExtentionCode(orderLine.getExtentionCode());
                    amount = amount.add(sku.getSalePrice().multiply(new BigDecimal(orderLine.getCount())));
                }
                amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            return amount;
        }catch (Exception e){
            return new BigDecimal(-1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveReturnOrder(com.baozun.nebula.sdk.command.ReturnOrderCommand)
     */
    @Override
    public Integer saveReturnOrder(ReturnOrderCommand returnOrderCommand){
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveCancelOrder(com.baozun.nebula.sdk.command.CancelOrderCommand)
     */
    @Override
    public Integer saveCancelOrder(CancelOrderCommand cancelOrderCommand){
        if (null == cancelOrderCommand)
            return FAILURE;
        // 判断订单是否存在
        SalesOrderCommand order = judgeOrderIfExist(cancelOrderCommand.getOrderCode());
        Integer orderStatus = order.getLogisticsStatus();
        if (com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_CANCELED.equals(orderStatus)
                        || com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED.equals(orderStatus))
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderLogisticsStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    public Integer updateOrderLogisticsStatus(String code,Integer logisticsStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#cancelOrderLogisticsStatus(java.lang.String, java.lang.Integer)
     */
    @Override
    @Deprecated
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            // 保存OMS消息发送记录(订单状态同步)
            sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#cancelOrderLogisticsStatus(java.lang.String, java.lang.Integer, java.lang.Boolean)
     */
    @Override
    public Integer cancelOrderLogisticsStatus(String code,Integer logisticsStatus,Boolean isOms){
        SalesOrderCommand salesOrder = judgeOrderIfExist(code);
        Integer retval = sdkOrderDao.updateOrderLogisticsStatus(code, logisticsStatus, new Date());
        if (retval > 0){
            OrderStatusLog orderStatusLog = saveOrderStatusLog(salesOrder.getId(), null, salesOrder.getLogisticsStatus(), logisticsStatus);
            // 保存OMS消息发送记录(订单状态同步)
            if (!Boolean.TRUE.equals(isOms)){
                sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC, orderStatusLog.getId(), null);
            }
            return SUCCESS;
        }
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#sendEmailOfOrder(java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmailOfOrder(String code,String emailTemplete){
        String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        if (isSendEmail != null && isSendEmail.equals("true")){
            sendEmail(code, emailTemplete);
        }
    }

    /**
     * Send email.
     *
     * @param code
     *            the code
     * @param emailTemplete
     *            the email templete
     */
    @Transactional(readOnly = true)
    private void sendEmail(String code,String emailTemplete){
        Map<String, Object> dataMap = new HashMap<String, Object>();

        SalesOrderCommand salesOrderCommand = findOrderByCode(code, 1);

        MemberPersonalData memberPersonalData = null;
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getMemberId())){
            memberPersonalData = sdkMemberManager.findMemberPersonData(salesOrderCommand.getMemberId());
        }

        String nickName = "";

        String email = salesOrderCommand.getEmail();

        if (Validator.isNotNullOrEmpty(memberPersonalData)){
            nickName = memberPersonalData.getNickname();
            if (Validator.isNullOrEmpty(email)){
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

        dataMap.put(
                        "mobile",
                        Validator.isNotNullOrEmpty(salesOrderCommand.getMobile()) ? salesOrderCommand.getMobile()
                                        : salesOrderCommand.getTel());

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
        if (null != salesOrderHandler){
            dataMap = salesOrderHandler.getEmailData(salesOrderCommand, dataMap, emailTemplete);
        }

        EmailEvent emailEvent = new EmailEvent(this, email, emailTemplete, dataMap);

        eventPublisher.publish(emailEvent);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findReturnOrdersByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam){

        return sdkReturnOrderDao.findReturnOrdersByQueryMapWithPage(page, sorts, searchParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCancelOrdersByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam){
        return sdkCancelOrderDao.findCancelOrdersByQueryMapWithPage(page, sorts, searchParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateCancelOrders(java.lang.Long, java.lang.String, java.lang.Integer,
     * java.lang.String)
     */
    @Override
    public Integer updateCancelOrders(Long handleId,String code,Integer status,String feedback){
        Integer count = sdkCancelOrderDao.updateCancelOrders(handleId, code, status, feedback, new Date());
        if (count == 1)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateReturnOrders(java.lang.Long, java.lang.String, java.lang.Integer,
     * java.lang.String)
     */
    @Override
    public Integer updateReturnOrders(Long handleId,String code,Integer status,String feedback){
        Integer count = sdkReturnOrderDao.updateReturnOrders(handleId, code, status, feedback, new Date());
        if (count == 1)
            return SUCCESS;
        return FAILURE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrders(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public Integer updateOrders(SalesOrderCommand salesOrderCommand){
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
        if (null == salesOrder){
            throw new BusinessException(Constants.ORDER_UPDATE_FAILURE);
        }
        ConsigneeCommand consigneeCommand = sdkConsigneeDao.findConsigneeOrderId(salesOrder.getId());
        if (null != consigneeCommand){
            Consignee consignee = sdkConsigneeDao.getByPrimaryKey(consigneeCommand.getId());
            salesOrderCommand.setId(consignee.getId());
            // 将salesOrderCommand中的收货人信息转换为Consignee对象
            consignee = (Consignee) ConvertUtils.convertFromTarget(consignee, salesOrderCommand);
            consignee.setModifyTime(new Date());
            consignee = sdkConsigneeDao.save(consignee);
            if (null == consignee){
                throw new BusinessException(Constants.ORDER_CONSIGNEE_UPDATE_FAILURE);
            }
        }
        return SUCCESS;
    }

    /**
     * Save order status log.
     *
     * @param orderId
     *            the order id
     * @param handleId
     *            the handle id
     * @param beforeStatus
     *            the before status
     * @param afterStatus
     *            the after status
     * @return the order status log
     */
    private OrderStatusLog saveOrderStatusLog(Long orderId,Long handleId,Integer beforeStatus,Integer afterStatus){
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
     * 判断订单是否存在.
     *
     * @param orderCode
     *            the order code
     * @return the sales order command
     */
    @Transactional(readOnly = true)
    private SalesOrderCommand judgeOrderIfExist(String orderCode){
        if (null == orderCode || orderCode.length() == 0 || "".equals(orderCode))
            throw new BusinessException(Constants.ORDERCODE_NOT_NULL);
        SalesOrderCommand order = sdkOrderDao.findOrderByCode(orderCode, null);
        if (null == order)// 订单不存在
            throw new BusinessException(Constants.ORDER_NOT_EXIST);
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findItemSkuListByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){

        return sdkOrderDao.findItemSkuListByQueryMapWithPage(page, sorts, paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findNotEvaultionOrderLineQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[],
     * java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<OrderLineCommand> findNotEvaultionOrderLineQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return sdkOrderDao.findNotEvaultionOrderLineQueryMapWithPage(page, sorts, paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderLineEvaulationStatus(java.lang.Long, java.lang.Long)
     */
    @Override
    public Integer updateOrderLineEvaulationStatus(Long skuId,Long orderId){
        return sdkOrderLineDao.updateOrderLineEvaulationStatusByOrderLineid(skuId, orderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderById(java.lang.Long, java.lang.Integer)
     */
    @Override
    @Transactional(readOnly = true)
    public SalesOrderCommand findOrderById(Long id,Integer type){
        // 包含订单基本信息和收货信息
        SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderById(id, type);

        if (null == salesOrderCommand || null == type){
            return salesOrderCommand;
        }else{
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCanceledOrderList(loxia.dao.Page, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CancelOrderCommand> findCanceledOrderList(Page page,Long memberId){
        return sdkOrderDao.findCanceledOrderList(page, memberId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findReturnedOrderList(loxia.dao.Page, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<ReturnOrderCommand> findReturnedOrderList(Page page,Long memberId){
        return sdkOrderDao.findReturnedOrderList(page, memberId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveReturnedOrder(com.baozun.nebula.sdk.command.ReturnOrderCommand)
     */
    @Override
    public Integer saveReturnedOrder(ReturnOrderCommand returnOrderCommand){
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByLineId(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public SalesOrderCommand findOrderByLineId(Long orderLineId){
        return sdkOrderDao.findOrderByLineId(orderLineId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateOrderFinancialStatusById(java.util.List, java.lang.Integer)
     */
    @Override
    public void updateOrderFinancialStatusById(List<Long> orderIds,Integer financialStatus){
        Integer count = sdkOrderDao.updateOrderFinancialStatusById(orderIds, financialStatus);
        if (count != orderIds.size()){
            throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { count });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findCountOfOrder(java.util.List, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findCountOfOrder(List<Integer> status,Long memberId){
        Integer count = sdkOrderDao.findCountOfOrder(status, memberId);
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderDetailList(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderDetailList(Long orderId){
        return sdkOrderLineDao.findOrderDetailList(orderId);
    }

    /**
     * 游客的memboIds.
     *
     * @return the membo ids
     */
    private Set<String> getMemboIds(){
        return sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null);
    }

    /**
     * 获取购物车中的所有店铺id.
     *
     * @param lines
     *            the lines
     * @return the shop ids
     */
    public List<Long> getShopIds(List<ShoppingCartLineCommand> lines){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> ids = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            ids.add(line.getShopId());
        }
        List<Long> shopIds = new ArrayList<Long>(ids);
        return shopIds;
    }

    /**
     * 根据购物车行获取ItemForCheckCommand集合.
     *
     * @param lines
     *            the lines
     * @return the item combo ids
     */
    protected Set<String> getItemComboIds(List<ShoppingCartLineCommand> lines){
        Set<String> set = new HashSet<String>();
        if (null != lines && lines.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                if (line.getComboIds() != null)
                    set.addAll(line.getComboIds());
            }
        }
        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findNoPayOrders(loxia.dao.Sort[], java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findNoPayOrders(Sort[] sorts,Long memberId){
        List<SalesOrderCommand> salesOrderComList = sdkOrderDao.findNoPayOrders(sorts, memberId);
        List<Long> idList = new ArrayList<Long>(salesOrderComList.size());
        for (SalesOrderCommand cmd : salesOrderComList){
            idList.add(cmd.getId());
        }
        List<OrderLineCommand> allLineList = sdkOrderLineDao.findOrderDetailListByOrderIds(idList);
        for (SalesOrderCommand order : salesOrderComList){
            Long orderId = order.getId();
            List<OrderLineCommand> orderLineList = new ArrayList<OrderLineCommand>();
            for (OrderLineCommand line : allLineList){
                if (line.getOrderId().equals(orderId)){
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

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findToBeCancelOrders(loxia.dao.Sort[], java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findToBeCancelOrders(Sort[] sorts,Map<String, Object> searchParam){
        List<SalesOrderCommand> list = sdkOrderDao.findToBeCancelOrders(sorts, searchParam);
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderPormots(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderPromotionCommand> findOrderPormots(String orderCode){
        SalesOrderCommand so = sdkOrderDao.findOrderByCode(orderCode, null);
        List<OrderPromotionCommand> orderPromots = null;
        if (so != null){
            orderPromots = sdkOrderPromotionDao.findOrderProsInfoByOrderId(so.getId());
        }
        return orderPromots;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#validCoupon(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionCouponCode validCoupon(String couponCode){
        if (StringUtils.isNotBlank(couponCode)){
            PromotionCouponCode promotionCouponCode = promotionCouponCodeDao.findPromotionCouponCodeBycoupon(couponCode);
            if (promotionCouponCode != null)
                return promotionCouponCode;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderStatusLogById(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public OrderStatusLogCommand findOrderStatusLogById(Long id){
        return sdkOrderStatusLogDao.findOrderStatusLogById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByExntentionListAndOrderCreateTime(java.util.List, java.util.Date)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderCreateTime(List<String> extentionList,Date startTime){
        return sdkOrderDao.findOrderByExntentionListAndOrderCreateTime(extentionList, startTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderByExntentionListAndOrderStatus(java.util.List, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderCommand> findOrderByExntentionListAndOrderStatus(List<String> extentionList,List<Integer> orderStatus){
        return sdkOrderDao.findOrderByExntentionListAndOrderStatus(extentionList, orderStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrderDetailListByOrderIds(java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderDetailListByOrderIds(List<Long> orderIdList){
        return sdkOrderLineDao.findOrderDetailListByOrderIds(orderIdList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#updateLogisticsInfo(java.lang.String, java.math.BigDecimal, java.lang.String,
     * java.lang.String, java.lang.String, java.util.Date)
     */
    @Override
    public void updateLogisticsInfo(
                    String orderCode,
                    BigDecimal actualFreight,
                    String logisticsProviderCode,
                    String logisticsProviderName,
                    String transCode,
                    Date modifyTime){
        sdkOrderDao.updateLogisticsInfo(orderCode, actualFreight, logisticsProviderCode, logisticsProviderName, transCode, modifyTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findPromotionCouponCodeListByQueryMap(java.util.Map)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionCouponCode> findPromotionCouponCodeListByQueryMap(Map<String, Object> paraMap){
        return promotionCouponCodeDao.findPromotionCouponCodeListByQueryMap(paraMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#getAllDistributionMode()
     */
    @Override
    @Transactional(readOnly = true)
    public List<DistributionMode> getAllDistributionMode(){
        List<DistributionMode> distributionModeList = distributionModeDao.getAllDistributionMode();
        return distributionModeList;
    }

    /**
     * 是否是不需要用户选择的礼品.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return true, if checks if is no need choice gift
     * @since 5.3.1
     */
    private boolean isNoNeedChoiceGift(ShoppingCartLineCommand shoppingCartLineCommand){
        return shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
    }

}
