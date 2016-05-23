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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.SdkPromotionRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartCommandBuilder;
import com.baozun.nebula.sdk.manager.SdkShoppingCartGroupManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartLinePackManager;
import com.feilong.core.Validator;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class SdkShoppingCartCommandBuilderImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月23日 下午5:48:22
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartCommandBuilder")
public class SdkShoppingCartCommandBuilderImpl implements SdkShoppingCartCommandBuilder{

    @Autowired
    private SdkShoppingCartLinePackManager           sdkShoppingCartLinePackManager;

    /** The Constant CHECKED_STATE. */
    private static final int                         CHECKED_STATE = 1;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    /** The sdk shopping cart group manager. */
    @Autowired
    private SdkShoppingCartGroupManager              sdkShoppingCartGroupManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager                       sdkMataInfoManager;

    /** The sdk promotion calculation manager. */
    @Autowired
    private SdkPromotionCalculationManager           sdkPromotionCalculationManager;

    /** The sdk promotion calculation share to sku manager. */
    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    /** The logistics manager. */
    @Autowired
    private LogisticsManager                         logisticsManager;

    /** The sdk promotion rule filter manager. */
    @Autowired
    private SdkPromotionRuleFilterManager            sdkPromotionRuleFilterManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartCommandBuilder#buildShoppingCartCommand(java.lang.Long, java.util.List,
     * com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.util.List, java.util.Set)
     */
    @Override
    @Transactional(readOnly = true)
    public ShoppingCartCommand buildShoppingCartCommand(
                    Long memberId,
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    CalcFreightCommand calcFreightCommand,
                    List<String> coupons,
                    Set<String> memberComIds){
        Validate.notEmpty(shoppingCartLines, "shoppingCartLines can't be null/empty!");

        //*******************************************************************************************************
        int i = 0;
        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartLines){
            if (shoppingCartLine.getId() == null){
                shoppingCartLine.setId(new Long(--i));
            }
            //TODO feilong bundle不是这么玩的
            sdkShoppingCartLinePackManager.packShoppingCartLine(shoppingCartLine); // 封装购物车行信息
            shoppingCartLine.setType(Constants.ITEM_TYPE_SALE);// 主卖品

            // 购物车行 金额小计
            shoppingCartLine.setSubTotalAmt(NumberUtil.getMultiplyValue(shoppingCartLine.getQuantity(), shoppingCartLine.getSalePrice()));
        }
        //*******************************************************************************************************

        Map<Long, Long> lineIdAndShopIdMapList = CollectionsUtil.getPropertyValueMap(shoppingCartLines, "id", "shopId");

        //*******************************************************************************************************
        List<ShoppingCartLineCommand> chooseLinesShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();// 被选中的购物车行
        List<ShoppingCartLineCommand> noChooseShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();// 未选中的购物车行
        // 判断是否是被选中的购物车行
        splitByCalcLevel(shoppingCartLines, chooseLinesShoppingCartLineCommandList, noChooseShoppingCartLineCommandList);

        //*************************************************************************************************

        // 封装购物车的基本信息
        ShoppingCartCommand shoppingCartCommand = buildShoppingCartCommand(
                        buildUserDetails(memberId, memberComIds),
                        coupons,
                        chooseLinesShoppingCartLineCommandList);

        // 设置分店铺的购物车
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = buildShopCartByShopIdMap(shoppingCartCommand);
        shoppingCartCommand.setShoppingCartByShopIdMap(shopIdAndShoppingCartCommandMap);

        // 获取购物车促销信息
        setShopCartPromotionInfos(shoppingCartCommand, calcFreightCommand);

        //***************************************************************************************

        // 因为某些NOTCHOOSE的购物车行不进行促销计算,如果全是无效数据,shopCartByShopIdMap中的值为空,所以这里帮助进行初始化shopCartByShopIdMap的数据
        for (ShoppingCartLineCommand sclc : noChooseShoppingCartLineCommandList){
            Long shopId = sclc.getShopId();
            ShoppingCartCommand shoppingCartCommandByShopId = shopIdAndShoppingCartCommandMap.get(shopId);
            if (shoppingCartCommandByShopId == null){
                shoppingCartCommandByShopId = new ShoppingCartCommand();
                List<ShoppingCartLineCommand> sclcList = new ArrayList<ShoppingCartLineCommand>();

                shoppingCartCommandByShopId.setShoppingCartLineCommands(sclcList);
                shopIdAndShoppingCartCommandMap.put(shopId, shoppingCartCommandByShopId);

                List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

                ShopCartCommandByShop summaryShopCart = new ShopCartCommandByShop();
                summaryShopCart.setQty(0);
                summaryShopCart.setRealPayAmount(BigDecimal.ZERO);
                summaryShopCart.setShopId(shopId);
                summaryShopCartList.add(summaryShopCart);

                shoppingCartCommand.setSummaryShopCartList(summaryShopCartList);
            }
        }

        //************************************************************************************************

        // 所有门店商品行数据包括选中和不选中的
        Map<Long, List<ShoppingCartLineCommand>> shopCommandMap = new HashMap<Long, List<ShoppingCartLineCommand>>();

        List<ShoppingCartLineCommand> newLines = new ArrayList<ShoppingCartLineCommand>();// 所有商品行数据包括选中和不选中的

        //************************************************************************************************

        for (Map.Entry<Long, Long> entry : lineIdAndShopIdMapList.entrySet()){
            Long lineId = entry.getKey();
            Long shopId = entry.getValue();

            //TODO feilong 这是什么鬼逻辑?

            // 循环所有原始记录行 开始
            for (ShoppingCartLineCommand chooseLine : shoppingCartCommand.getShoppingCartLineCommands()){
                if (lineId.equals(chooseLine.getId())){
                    newLines.add(chooseLine);
                    break;
                }
            }
            for (ShoppingCartLineCommand notChooseLine : noChooseShoppingCartLineCommandList){
                if (lineId.equals(notChooseLine.getId())){
                    newLines.add(notChooseLine);
                    break;
                }
            }
            //****************************************************************************************

            // 循环添加门店所有行
            for (Entry<Long, ShoppingCartCommand> shopCommand : shopIdAndShoppingCartCommandMap.entrySet()){
                Long shopIdKey = shopCommand.getKey();// 店铺ID
                List<ShoppingCartLineCommand> shopShoppingCartLineList = shopCommand.getValue().getShoppingCartLineCommands();// 店铺的商品行数据

                if (!shopId.equals(shopIdKey)){
                    continue;
                }

                ShoppingCartLineCommand tempShopLine = null;

                for (ShoppingCartLineCommand shopLine : shopShoppingCartLineList){
                    if (lineId.equals(shopLine.getId())){
                        tempShopLine = shopLine;
                        break;
                    }
                }
                if (null == tempShopLine){
                    for (ShoppingCartLineCommand newLine : newLines){
                        if (lineId.equals(newLine.getId()) && shopId.equals(newLine.getShopId())){
                            tempShopLine = newLine;
                            break;
                        }
                    }
                }

                if (null != tempShopLine){
                    if (shopCommandMap.get(shopIdKey) == null){
                        List<ShoppingCartLineCommand> shopNewLines = new ArrayList<ShoppingCartLineCommand>();
                        shopNewLines.add(tempShopLine);
                        shopCommandMap.put(shopIdKey, shopNewLines);
                    }else{
                        shopCommandMap.get(shopIdKey).add(tempShopLine);
                    }
                }
            }
            // 循环添加门店所有行 结束
        }

        //******************************************************************************************
        // 循环添加所有赠品记录行
        for (ShoppingCartLineCommand giftLine : shoppingCartCommand.getShoppingCartLineCommands()){
            if (giftLine.isGift() || giftLine.isCaptionLine()){
                newLines.add(giftLine);
            }
        }
        shoppingCartCommand.setShoppingCartLineCommands(newLines);

        //***********************************************************************************************
        // 循环添加店铺所有增品记录行
        for (Entry<Long, ShoppingCartCommand> shopCommand : shopIdAndShoppingCartCommandMap.entrySet()){
            Long shopIdKey = shopCommand.getKey();// 店铺ID
            List<ShoppingCartLineCommand> shopLineValues = shopCommand.getValue().getShoppingCartLineCommands();// 店铺的商品行数据
            for (ShoppingCartLineCommand shopLine : shopLineValues){
                if (shopLine.isGift() || shopLine.isCaptionLine()){
                    shopCommandMap.get(shopIdKey).add(shopLine);
                }
            }
        }
        if (shopCommandMap.size() > 0){
            for (Entry<Long, List<ShoppingCartLineCommand>> shopShopLine : shopCommandMap.entrySet()){
                shopIdAndShoppingCartCommandMap.get(shopShopLine.getKey()).setShoppingCartLineCommands(shopShopLine.getValue());
            }
        }
        return shoppingCartCommand;
    }

    /**
     * 通过系统配置的计算级别选出参与促销计算以及不参与促销计算的购物车行.
     *
     * @param allLines
     *            the all lines
     * @param chooseLines
     *            the choose lines
     * @param notChooseLines
     *            the not choose lines
     */
    private void splitByCalcLevel(
                    List<ShoppingCartLineCommand> allLines,
                    List<ShoppingCartLineCommand> chooseLines,
                    List<ShoppingCartLineCommand> notChooseLines){
        for (ShoppingCartLineCommand shoppingCartLine : allLines){
            // 判断有效.促销计算时只计算有效的、被选中的购物车行
            if (needContainsLineCalc(shoppingCartLine.getSettlementState(), shoppingCartLine.isValid())){
                chooseLines.add(shoppingCartLine);
            }else{
                notChooseLines.add(shoppingCartLine);
            }
        }
    }

    /**
     * @param memberId
     * @param memberComIds
     * @return
     * @since 5.3.1
     */
    private UserDetails buildUserDetails(Long memberId,Set<String> memberComIds){
        UserDetails userDetails = new UserDetails();

        userDetails.setMemberId(memberId);
        userDetails.setMemComboList(null != memberId ? memberComIds : sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null));

        return userDetails;
    }

    /**
     * 是否包含此行进行计算.
     *
     * @param settlementState
     *            the Settlement state
     * @param isValid
     *            the is valid
     * @return true, if need contains line calc
     */
    //TODO feilong  what's this?
    private boolean needContainsLineCalc(Integer settlementState,boolean isValid){
        //购物车行用于计算的级别
        String calcLevel = sdkMataInfoManager.findValue(MataInfo.KEY_SC_CALC_LEVEL);
        // 选中级别
        if (calcLevel == null || calcLevel.equals("CHECKED")){
            return settlementState.equals(CHECKED_STATE);
        }
        // 有效级别
        else if (calcLevel.equals("AVAILABLE")){
            return isValid;
        }
        // 有效，并且选中
        else if (calcLevel.equals("CHECKEDANDAVAILABLE")){
            return settlementState.equals(CHECKED_STATE) && isValid;
        }
        // 其它，all
        return true;
    }

    /**
     * 封装购物车的基本信息.
     *
     * @param shoppingCartCommand
     *            the shopping cart
     * @param userDetails
     *            the user details
     * @param coupons
     *            the coupons
     * @param validedLines
     *            the valided lines
     */
    private ShoppingCartCommand buildShoppingCartCommand(
                    UserDetails userDetails,
                    List<String> coupons,
                    List<ShoppingCartLineCommand> validedLines){

        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();// 购物车对象

        // 设置应付金额
        shoppingCartCommand.setOriginPayAmount(getOriginPayAmount(validedLines));

        // 购物车行信息
        shoppingCartCommand.setShoppingCartLineCommands(validedLines);

        // 商品数量
        shoppingCartCommand.setOrderQuantity(getOrderQuantity(validedLines));

        // 优惠券编码
        shoppingCartCommand.setCoupons(coupons);
        // 会员信息
        shoppingCartCommand.setUserDetails(userDetails);

        // 设置当前时间
        if (null == shoppingCartCommand.getCurrentTime()){
            shoppingCartCommand.setCurrentTime(new Date());
        }

        return shoppingCartCommand;
    }

    /**
     * 计算应付金额.
     *
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return the origin pay amount
     */
    private BigDecimal getOriginPayAmount(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal originPayAmount = new BigDecimal(0);
        for (ShoppingCartLineCommand cartLine : shoppingCartLines){
            if (cartLine.isGift() || cartLine.isCaptionLine()){
                continue;
            }
            originPayAmount = originPayAmount.add(NumberUtil.getMultiplyValue(cartLine.getQuantity(), cartLine.getSalePrice()));
        }
        return originPayAmount = originPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置购物车的促销信息.
     *
     * @param shoppingCartCommand
     *            the shopping cart
     * @param calcFreightCommand
     *            the calc freight command
     */
    private void setShopCartPromotionInfos(ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand){
        // 获取促销数据.需要调用促销引擎计算优惠价格
        List<PromotionBrief> promotionBriefList = calcuPromoBriefs(shoppingCartCommand);

        Map<Long, List<PromotionBrief>> shopIdAndPromotionBriefListMap = CollectionsUtil.group(promotionBriefList, "shopId");

        // 区分店铺的购物车
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        List<ShopCartCommandByShop> summaryShopCartList = buildShopCartCommandByShopList(
                        calcFreightCommand,
                        shopIdAndPromotionBriefListMap,
                        shopIdAndShoppingCartCommandMap);

        //********************************************************************************************

        shoppingCartCommand.setSummaryShopCartList(summaryShopCartList);

        // 购物车促销简介信息
        shoppingCartCommand.setCartPromotionBriefList(promotionBriefList);

        // 所有购物车行(可能包含了礼品\有效、无效)
        shoppingCartCommand.setShoppingCartLineCommands(getAllShoppingCartLines(shopIdAndShoppingCartCommandMap));

        // 设置应付金额
        shoppingCartCommand.setOriginPayAmount(getOriginPayAmount(getNoGiftShoppingCartLines(shopIdAndShoppingCartCommandMap)));

        // 实际支付金额
        shoppingCartCommand.setCurrentPayAmount(getRealPayAmountByShopCart(summaryShopCartList));

        // 应付运费
        shoppingCartCommand.setOriginShoppingFee(getOrginShippingAmountByShopCart(summaryShopCartList));

        // 实付运费
        shoppingCartCommand.setCurrentShippingFee(
                        getCurrentShippingAmountByShopCart(summaryShopCartList, shoppingCartCommand.getOriginShoppingFee()));
    }

    /**
     * @param calcFreightCommand
     * @param proBriefsMap
     * @param shopIdAndShoppingCartCommandMap
     * @return
     */
    private List<ShopCartCommandByShop> buildShopCartCommandByShopList(
                    CalcFreightCommand calcFreightCommand,
                    Map<Long, List<PromotionBrief>> proBriefsMap,
                    Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){
        List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopIdAndShoppingCartCommandMap.entrySet()){

            Long shopId = entry.getKey();
            ShoppingCartCommand cart = entry.getValue();
            List<ShoppingCartLineCommand> lines = cart.getShoppingCartLineCommands();

            List<PromotionBrief> briefs = proBriefsMap.get(shopId);

            ShopCartCommandByShop shopCartCommandByShop = null;

            if (null == briefs || briefs.size() == 0){
                BigDecimal originShippingFee = BigDecimal.ZERO;
                if (null != calcFreightCommand){
                    originShippingFee = getFreightFee(shopId, calcFreightCommand, lines);
                }
                shopCartCommandByShop = new ShopCartCommandByShop();
                // 应付运费
                shopCartCommandByShop.setOriginShoppingFee(originShippingFee);

                // 应付小计
                shopCartCommandByShop.setSubtotalCurrentPayAmount(getOriginPayAmount(lines));

                // 应付合计(应付小计+应付运费)
                BigDecimal sumCurrentPayAmt = originShippingFee.add(shopCartCommandByShop.getSubtotalCurrentPayAmount());
                shopCartCommandByShop.setSumCurrentPayAmount(sumCurrentPayAmt);

                // 实付合计
                shopCartCommandByShop.setRealPayAmount(sumCurrentPayAmt);

                // 应付金额
                cart.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());

                // 实付金额
                cart.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount());

                // 该店铺的商品数量
                shopCartCommandByShop.setQty(getOrderQuantity(lines));
            }else{
                // 计算分店铺的优惠.只计算有效的购物车行
                cart.setShoppingCartLineCommands(lines);
                shopCartCommandByShop = getPromotionDiscountAmtSummarySkuListGroup(briefs, cart, calcFreightCommand);

                cart.setShoppingCartLineCommands(
                                sdkShoppingCartGroupManager.groupShoppingCartLinesToDisplayByLinePromotionResult(cart, briefs));
            }
            shopCartCommandByShop.setShopId(shopId);
            summaryShopCartList.add(shopCartCommandByShop);
        }
        return summaryShopCartList;
    }

    /**
     * 构造用于购物车页面促销信息显示的数据.
     *
     * @param promotionBriefList
     *            the promotion brief list
     * @param shopCart
     *            the shop cart
     * @param calcFreightCommand
     *            the calc freight command
     * @return the promotion discount amt summary sku list group
     */
    private ShopCartCommandByShop getPromotionDiscountAmtSummarySkuListGroup(
                    List<PromotionBrief> promotionBriefList,
                    ShoppingCartCommand shopCart,
                    CalcFreightCommand calcFreightCommand){

        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();// 有效的购物车行
        List<ShoppingCartLineCommand> giftList = new ArrayList<ShoppingCartLineCommand>();// 礼品行

        BigDecimal disAmtOnOrder = BigDecimal.ZERO;// 商品行优惠金额
        BigDecimal baseOnOrderDisAmt = BigDecimal.ZERO;// 整单优惠金额
        BigDecimal offersShippingDisAmt = BigDecimal.ZERO;// 运费整单优惠金额

        if (null != promotionBriefList && promotionBriefList.size() > 0){
            for (PromotionBrief promotionBrief : promotionBriefList){
                List<PromotionSettingDetail> details = promotionBrief.getDetails();
                if (null == details || details.size() == 0){
                    continue;
                }
                for (PromotionSettingDetail detail : details){
                    List<PromotionSKUDiscAMTBySetting> affectSkuList = detail.getAffectSKUDiscountAMTList();
                    if (null != affectSkuList && affectSkuList.size() > 0){
                        for (PromotionSKUDiscAMTBySetting proSku : affectSkuList){
                            // 如果是礼品
                            if (proSku.getGiftMark()){
                                giftList.add(getGiftShoppingCartLineCommand(proSku));
                            }else{
                                BigDecimal skuDisAmt = proSku.getDiscountAmount();
                                disAmtOnOrder = disAmtOnOrder.add(skuDisAmt);// 非礼品才算优惠
                            }
                        }
                    }else{
                        BigDecimal disAmt = detail.getDiscountAmount();// 基于整单的
                        if (detail.getFreeShippingMark()){
                            offersShippingDisAmt = offersShippingDisAmt.add(disAmt); // 运费优惠
                        }else{
                            baseOnOrderDisAmt = baseOnOrderDisAmt.add(disAmt);
                        }
                    }
                }
            }
        }

        Long shopId = lines.get(0).getShopId();// 店铺id

        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();

        shopCartCommandByShop.setQty(getOrderQuantity(lines));// 商品数量

        shopCartCommandByShop.setSubtotalCurrentPayAmount(getOriginPayAmount(lines)); // 应付小计
        shopCartCommandByShop.setSumCurrentPayAmount(shopCartCommandByShop.getSubtotalCurrentPayAmount()); // 应付合计

        BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)

        shopCartCommandByShop.setOffersShipping(BigDecimal.ZERO);// 计算运费
        shopCartCommandByShop.setOriginShoppingFee(BigDecimal.ZERO);// 源运费

        BigDecimal originShippingFee = BigDecimal.ZERO; // 应付运费
        BigDecimal currentShippingFee = BigDecimal.ZERO;// 实付运费

        if (null != calcFreightCommand){
            originShippingFee = getFreightFee(shopId, calcFreightCommand, lines);
            shopCartCommandByShop.setOriginShoppingFee(originShippingFee); // 应付运费
            shopCartCommandByShop.setOffersShipping(
                            originShippingFee.compareTo(offersShippingDisAmt) >= 0 ? offersShippingDisAmt : originShippingFee); // 运费优惠
            shopCart.setOriginShoppingFee(originShippingFee); // 应付运费
            currentShippingFee = shopCartCommandByShop.getOriginShoppingFee().subtract(shopCartCommandByShop.getOffersShipping()); // 实付运费
            shopCart.setCurrentShippingFee(currentShippingFee);
        }

        // 当应付合计金额 小于订单优惠时
        if (shopCartCommandByShop.getSumCurrentPayAmount().compareTo(disAmtOnOrder) < 0){
            shopCartCommandByShop.setOffersTotal(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setDisAmtOnOrder(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setOffersShipping(BigDecimal.ZERO);
            shopCartCommandByShop.setDisAmtSingleOrder(BigDecimal.ZERO);
        }else{
            // 当应付合计金额 小于优惠合计时
            if (shopCartCommandByShop.getSumCurrentPayAmount().compareTo(shopCartAllDisAmt) < 0){
                shopCartCommandByShop.setOffersTotal(shopCartCommandByShop.getSumCurrentPayAmount());
                BigDecimal differAmt = shopCartCommandByShop.getSumCurrentPayAmount().subtract(disAmtOnOrder);// 整单优惠=应付金额-订单优惠
                shopCartCommandByShop.setDisAmtSingleOrder(differAmt); // 整单优惠
            }else{
                shopCartCommandByShop.setDisAmtSingleOrder(baseOnOrderDisAmt);
                shopCartCommandByShop.setOffersTotal(shopCartAllDisAmt);
            }
            shopCartCommandByShop.setDisAmtOnOrder(disAmtOnOrder);// 订单优惠
        }

        // 实付合计(应付金额+实付运费-商品优惠总额)
        BigDecimal realPayAmt = shopCartCommandByShop.getSumCurrentPayAmount().add(currentShippingFee)
                        .subtract(shopCartCommandByShop.getOffersTotal());
        shopCartCommandByShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        shopCartCommandByShop.setSumCurrentPayAmount(shopCartCommandByShop.getSubtotalCurrentPayAmount().add(originShippingFee));

        lines.addAll(giftList); // 将礼品放入购物车当中
        shopCart.setShoppingCartLineCommands(lines);

        shopCart.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());// 应付金额
        shopCart.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount()); // 实付金额

        // 封装数据
        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();
        Map<Long, ShoppingCartCommand> map = new HashMap<Long, ShoppingCartCommand>();
        map.put(shopId, shopCart);

        shoppingCartCommand.setShoppingCartLineCommands(shopCart.getShoppingCartLineCommands());
        shoppingCartCommand.setShoppingCartByShopIdMap(map);

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLine(shoppingCartCommand, promotionBriefList);
        shopCart.setShoppingCartLineCommands(shoppingCartCommand.getShoppingCartLineCommands());

        return shopCartCommandByShop;
    }

    /**
     * 计算促销金额.
     *
     * @param summaryShopCart
     *            the summary shop cart
     * @return the real pay amount by shop cart
     */
    private BigDecimal getRealPayAmountByShopCart(List<ShopCartCommandByShop> summaryShopCart){
        BigDecimal cartPromotionAmount = BigDecimal.ZERO;
        if (null == summaryShopCart || summaryShopCart.size() == 0)
            return cartPromotionAmount;
        for (ShopCartCommandByShop scc : summaryShopCart){
            cartPromotionAmount = cartPromotionAmount.add(scc.getRealPayAmount());
        }
        return cartPromotionAmount = cartPromotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值.
     *
     * @param shoppingCart
     *            the shopping cart
     * @param promotionBriefList
     *            the promotion brief list
     */
    private void shareDiscountToLine(ShoppingCartCommand shoppingCart,List<PromotionBrief> promotionBriefList){
        // 分摊结果
        List<PromotionSKUDiscAMTBySetting> shareList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCart, promotionBriefList);
        if (null == shareList || shareList.size() == 0 || shoppingCart.getShoppingCartLineCommands() == null
                        || shoppingCart.getShoppingCartLineCommands().size() == 0){
            return;
        }

        for (ShoppingCartLineCommand shoppingCartLine : shoppingCart.getShoppingCartLineCommands()){
            BigDecimal curSKUDiscount = BigDecimal.ZERO;
            if (shoppingCartLine.isGift())
                continue;
            for (PromotionSKUDiscAMTBySetting skuSetting : shareList){
                if (skuSetting.getFreeShippingMark() || skuSetting.getGiftMark())
                    continue;
                // 行优惠
                if (String.valueOf(shoppingCartLine.getSkuId()).equals(String.valueOf(skuSetting.getSkuId()))){
                    curSKUDiscount = curSKUDiscount.add(skuSetting.getDiscountAmount());
                }
            }
            shoppingCartLine.setDiscount(curSKUDiscount);
            // 购物车行小计
            BigDecimal lineSubTotalAmt = new BigDecimal(shoppingCartLine.getQuantity()).multiply(shoppingCartLine.getSalePrice())
                            .subtract(curSKUDiscount);
            if (lineSubTotalAmt.compareTo(BigDecimal.ZERO) < 0){
                lineSubTotalAmt = BigDecimal.ZERO;
            }
            shoppingCartLine.setSubTotalAmt(lineSubTotalAmt);
        }
    }

    /**
     * 封装礼品购物车行.
     *
     * @param promotionSKUDiscAMTBySetting
     *            the pro sku
     * @return the gift shopping cart line command
     */
    private ShoppingCartLineCommand getGiftShoppingCartLineCommand(PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
        Integer qty = promotionSKUDiscAMTBySetting.getQty();
        Long settingId = promotionSKUDiscAMTBySetting.getSettingId();

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setItemId(promotionSKUDiscAMTBySetting.getItemId());
        shoppingCartLineCommand.setItemName(promotionSKUDiscAMTBySetting.getItemName());
        shoppingCartLineCommand.setQuantity(qty);
        shoppingCartLineCommand.setGift(promotionSKUDiscAMTBySetting.getGiftMark());
        shoppingCartLineCommand.setShopId(promotionSKUDiscAMTBySetting.getShopId());
        shoppingCartLineCommand.setType(Constants.ITEM_TYPE_PREMIUMS);
        shoppingCartLineCommand.setSkuId(promotionSKUDiscAMTBySetting.getSkuId());

        if (settingId != null){
            shoppingCartLineCommand.setLineGroup(settingId);
        }
        shoppingCartLineCommand.setStock(qty);
        // 赠品都设置为有效
        shoppingCartLineCommand.setValid(true);
        sdkShoppingCartLinePackManager.packShoppingCartLine(shoppingCartLineCommand);

        shoppingCartLineCommand.setPromotionId(promotionSKUDiscAMTBySetting.getPromotionId());
        shoppingCartLineCommand.setSettingId(settingId);
        shoppingCartLineCommand.setGiftChoiceType(promotionSKUDiscAMTBySetting.getGiftChoiceType());
        shoppingCartLineCommand.setGiftCountLimited(promotionSKUDiscAMTBySetting.getGiftCountLimited());

        return shoppingCartLineCommand;
    }

    /**
     * 返回整个购物车数据.
     *
     * @param shopCartMap
     *            the shop cart map
     * @return the all shopping cart lines
     */
    private List<ShoppingCartLineCommand> getAllShoppingCartLines(Map<Long, ShoppingCartCommand> shopCartMap){
        List<ShoppingCartLineCommand> allLines = new ArrayList<ShoppingCartLineCommand>();

        if (null != shopCartMap && shopCartMap.size() > 0){
            for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
                allLines.addAll(entry.getValue().getShoppingCartLineCommands());
            }
        }
        return allLines;
    }

    /**
     * 计算实付运费.
     *
     * @param summaryShopCart
     *            the summary shop cart
     * @param orginShippingAmount
     *            the orgin shipping amount
     * @return the current shipping amount by shop cart
     */
    private BigDecimal getCurrentShippingAmountByShopCart(List<ShopCartCommandByShop> summaryShopCart,BigDecimal orginShippingAmount){
        // 运费优惠
        if (null == summaryShopCart || summaryShopCart.size() == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal offersShippingAmount = BigDecimal.ZERO;
        for (ShopCartCommandByShop scc : summaryShopCart){
            offersShippingAmount = offersShippingAmount.add(scc.getOffersShipping());
        }
        // 计算实付运费
        if (orginShippingAmount.compareTo(offersShippingAmount) >= 0){
            return orginShippingAmount.subtract(offersShippingAmount);
        }else{
            return BigDecimal.ZERO;
        }
    }

    //**********************************************************************************************

    /**
     * 无促销情况下计算运费.
     * 
     * @param shopId
     *            the shop id
     * @param calcFreightCommand
     *            the calc freight command
     * @param validLines
     *            the valid lines
     *
     * @return the freight fee
     */
    //TODO feilong add javadoc and flow image
    private BigDecimal getFreightFee(Long shopId,CalcFreightCommand calcFreightCommand,List<ShoppingCartLineCommand> validLines){
        if (null == calcFreightCommand){
            return BigDecimal.ZERO;
        }

        Boolean flag = logisticsManager.hasDistributionMode(calcFreightCommand, shopId);

        if (!flag){
            return BigDecimal.ZERO;
        }

        List<ItemFreightInfoCommand> itemList = toItemFreightInfoCommandList(validLines);
        return logisticsManager.findFreight(
                        itemList,
                        calcFreightCommand.getDistributionModeId(),
                        shopId,
                        calcFreightCommand.getProvienceId(),
                        calcFreightCommand.getCityId(),
                        calcFreightCommand.getCountyId(),
                        calcFreightCommand.getTownId());
    }

    /**
     * @param validLines
     * @return
     */
    private List<ItemFreightInfoCommand> toItemFreightInfoCommandList(List<ShoppingCartLineCommand> validLines){
        // 无促销情况下统计金额小计
        List<ItemFreightInfoCommand> itemList = new ArrayList<ItemFreightInfoCommand>();
        for (ShoppingCartLineCommand line : validLines){
            ItemFreightInfoCommand itemInfo = new ItemFreightInfoCommand();
            itemInfo.setItemId(line.getItemId());
            itemInfo.setCount(line.getQuantity());
            itemList.add(itemInfo);
        }
        return itemList;
    }

    /**
     * 计算整单商品数量.
     *
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return the order quantity
     */
    private int getOrderQuantity(List<ShoppingCartLineCommand> shoppingCartLines){
        return Validator.isNullOrEmpty(shoppingCartLines) ? 0 : CollectionsUtil.sum(shoppingCartLines, "quantity").intValue();
    }

    /**
     * 获取非礼品购物车行.
     *
     * @param shopCartMap
     *            the shop cart map
     * @return the no gift shopping cart lines
     */
    private List<ShoppingCartLineCommand> getNoGiftShoppingCartLines(Map<Long, ShoppingCartCommand> shopCartMap){
        List<ShoppingCartLineCommand> noGiftLines = new ArrayList<ShoppingCartLineCommand>();

        if (null != shopCartMap && shopCartMap.size() > 0){
            for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
                ShoppingCartCommand cart = entry.getValue();
                List<ShoppingCartLineCommand> cartLines = cart.getShoppingCartLineCommands();
                if (null == cartLines || cartLines.size() == 0){
                    continue;
                }
                for (ShoppingCartLineCommand line : cartLines){
                    if (!line.isGift()){
                        noGiftLines.add(line);
                    }
                }
            }
        }
        return noGiftLines;
    }

    /**
     * 用于计算获取促销数据.
     *
     * @param shoppingCartCommand
     *            the shop cart
     * @return the list< promotion brief>
     */
    private List<PromotionBrief> calcuPromoBriefs(ShoppingCartCommand shoppingCartCommand){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();
        Set<String> memboSet = shoppingCartCommand.getUserDetails().getMemComboList();

        // 获取人群和商品促销的交集
        Set<Long> shopIdSet = CollectionsUtil.getPropertyValueSet(shoppingCartLineCommandList, "shopId");
        Set<String> propertyValueSet = CollectionsUtil.getPropertyValueSet(shoppingCartLineCommandList, "comboIds");

        List<PromotionCommand> promotionList = sdkPromotionRuleFilterManager.getIntersectActivityRuleData(
                        new ArrayList<Long>(shopIdSet),
                        memboSet,
                        propertyValueSet,
                        shoppingCartCommand.getCurrentTime());

        if (Validator.isNotNullOrEmpty(promotionList)){
            // 通过购物车和促销集合计算商品促销
            return sdkPromotionCalculationManager.calculationPromotion(shoppingCartCommand, promotionList);
        }
        return Collections.emptyList();
    }

    /**
     * 计算应付运费.
     *
     * @param summaryShopCart
     *            the summary shop cart
     * @return the orgin shipping amount by shop cart
     */
    private BigDecimal getOrginShippingAmountByShopCart(List<ShopCartCommandByShop> summaryShopCart){
        BigDecimal orginShippingAmount = BigDecimal.ZERO;
        if (null == summaryShopCart || summaryShopCart.size() == 0)
            return orginShippingAmount;
        for (ShopCartCommandByShop scc : summaryShopCart){
            orginShippingAmount = orginShippingAmount.add(scc.getOriginShoppingFee());
        }
        return orginShippingAmount = orginShippingAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据店铺封装shopCart对象.
     *
     * @param shoppingCartCommand
     *            the shop cart
     * @return the shop cart by shop id
     */
    private Map<Long, ShoppingCartCommand> buildShopCartByShopIdMap(ShoppingCartCommand shoppingCartCommand){
        // 获取区分了店铺的购物车对象
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();

        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandMap = getShopIdAndShoppingCartLineCommandList(
                        shoppingCartLineCommandList);

        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();

        UserDetails userDetails = shoppingCartCommand.getUserDetails();
        List<String> coupons = shoppingCartCommand.getCoupons();

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : shopIdAndShoppingCartLineCommandMap.entrySet()){
            Long shopId = entry.getKey();
            List<ShoppingCartLineCommand> lines = entry.getValue();

            // 封装购物车信息
            ShoppingCartCommand scc = buildShoppingCartCommand(userDetails, coupons, lines);
            shopCartByShopIdMap.put(shopId, scc);
        }

        return shopCartByShopIdMap;
    }

    private Map<Long, List<ShoppingCartLineCommand>> getShopIdAndShoppingCartLineCommandList(
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList){

        //TODO feilong 提取
        Predicate<ShoppingCartLineCommand> predicate = new Predicate<ShoppingCartLineCommand>(){

            @Override
            public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){
                return !shoppingCartLineCommand.isCaptionLine();
            }
        };
        return CollectionsUtil.group(shoppingCartLineCommandList, "shopId", predicate);
    }
}
