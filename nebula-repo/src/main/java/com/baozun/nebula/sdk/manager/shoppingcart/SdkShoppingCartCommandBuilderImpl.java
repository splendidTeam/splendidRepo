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
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionRuleFilterManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.Validator;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPropertyValueEqualsPredicate;

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

    /** The Constant CHECKED_STATE. */
    private static final int                         CHECKED_STATE = 1;

    /** The sdk shopping cart line pack manager. */
    @Autowired
    private SdkShoppingCartLinePackManager           sdkShoppingCartLinePackManager;

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

    /** The sdk freight fee manager. */
    @Autowired
    private SdkFreightFeeManager                     sdkFreightFeeManager;

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
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    CalcFreightCommand calcFreightCommand,
                    List<String> coupons,
                    Set<String> memberComIds){
        Validate.notEmpty(shoppingCartLineCommandList, "shoppingCartLines can't be null/empty!");

        //*******************************************************************************************************
        int i = 0;
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            if (shoppingCartLineCommand.getId() == null){
                shoppingCartLineCommand.setId(new Long(--i));
            }
            //TODO feilong bundle不是这么玩的
            sdkShoppingCartLinePackManager.packShoppingCartLine(shoppingCartLineCommand); // 封装购物车行信息
            shoppingCartLineCommand.setType(Constants.ITEM_TYPE_SALE);// 主卖品

            // 购物车行 金额小计
            shoppingCartLineCommand.setSubTotalAmt(NumberUtil.getMultiplyValue(shoppingCartLineCommand.getQuantity(), shoppingCartLineCommand.getSalePrice()));
        }
        //*******************************************************************************************************

        Map<Long, Long> lineIdAndShopIdMapList = CollectionsUtil.getPropertyValueMap(shoppingCartLineCommandList, "id", "shopId");

        //*******************************************************************************************************
        List<ShoppingCartLineCommand> chooseLinesShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();// 被选中的购物车行
        List<ShoppingCartLineCommand> noChooseShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();// 未选中的购物车行
        // 判断是否是被选中的购物车行
        splitByCalcLevel(shoppingCartLineCommandList, chooseLinesShoppingCartLineCommandList, noChooseShoppingCartLineCommandList);

        //*************************************************************************************************

        // 封装购物车的基本信息
        UserDetails userDetails = buildUserDetails(memberId, memberComIds);
        ShoppingCartCommand shoppingCartCommand = buildShoppingCartCommand(userDetails, coupons, chooseLinesShoppingCartLineCommandList);

        // 设置分店铺的购物车
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = buildShopCartByShopIdMap(shoppingCartCommand);
        shoppingCartCommand.setShoppingCartByShopIdMap(shopIdAndShoppingCartCommandMap);

        //TODO feilong 全不选的情况
        if (Validator.isNotNullOrEmpty(shoppingCartCommand.getShoppingCartLineCommands())){
            // 设置购物车促销信息
            setShopCartPromotionInfos(shoppingCartCommand, calcFreightCommand);
        }

        //***************************************************************************************
        //TODO feilong 
        doWithNoChoose(noChooseShoppingCartLineCommandList, shoppingCartCommand, shopIdAndShoppingCartCommandMap);

        //************************************************************************************************
        doWithRebuildShoppingCartLineCommands(
                        shoppingCartCommand,
                        noChooseShoppingCartLineCommandList,
                        lineIdAndShopIdMapList,
                        shopIdAndShoppingCartCommandMap);

        return shoppingCartCommand;
    }

    //TODO feilong 这是什么鬼逻辑?
    private void doWithRebuildShoppingCartLineCommands(
                    ShoppingCartCommand shoppingCartCommand,
                    List<ShoppingCartLineCommand> noChooseShoppingCartLineCommandList,
                    Map<Long, Long> lineIdAndShopIdMapList,
                    Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){
        // 所有门店商品行数据包括选中和不选中的
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = new HashMap<Long, List<ShoppingCartLineCommand>>();

        List<ShoppingCartLineCommand> newLineList = new ArrayList<ShoppingCartLineCommand>();// 所有商品行数据包括选中和不选中的

        //************************************************************************************************
        for (Map.Entry<Long, Long> entry : lineIdAndShopIdMapList.entrySet()){
            Long lineId = entry.getKey();
            Long shopId = entry.getValue();

            CollectionUtils.addIgnoreNull(
                            newLineList,
                            CollectionsUtil.find(shoppingCartCommand.getShoppingCartLineCommands(), "id", lineId));
            CollectionUtils.addIgnoreNull(newLineList, CollectionsUtil.find(noChooseShoppingCartLineCommandList, "id", lineId));

            //****************************************************************************************
            ShoppingCartLineCommand tempShopLine = getTempShopLine(shopId, lineId, newLineList, shopIdAndShoppingCartCommandMap);

            if (null != tempShopLine){
                List<ShoppingCartLineCommand> list = shopIdAndShoppingCartLineCommandListMap.get(shopId);
                if (list == null){
                    List<ShoppingCartLineCommand> shopNewLines = new ArrayList<ShoppingCartLineCommand>();
                    shopNewLines.add(tempShopLine);

                    shopIdAndShoppingCartLineCommandListMap.put(shopId, shopNewLines);
                }else{
                    list.add(tempShopLine);
                }
            }
        }
        //******************************************************************************************
        // 循环添加所有赠品记录行
        for (ShoppingCartLineCommand giftLine : shoppingCartCommand.getShoppingCartLineCommands()){
            if (giftLine.isGift() || giftLine.isCaptionLine()){
                newLineList.add(giftLine);
            }
        }
        shoppingCartCommand.setShoppingCartLineCommands(newLineList);

        //***********************************************************************************************
        // 循环添加店铺所有增品记录行
        for (Entry<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandEntry : shopIdAndShoppingCartCommandMap.entrySet()){
            Long shopIdKey = shopIdAndShoppingCartCommandEntry.getKey();// 店铺ID
            ShoppingCartCommand shpShoppingCartCommand = shopIdAndShoppingCartCommandEntry.getValue();

            List<ShoppingCartLineCommand> shoppingCartLineCommandList = shpShoppingCartCommand.getShoppingCartLineCommands();// 店铺的商品行数据

            for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
                if (shoppingCartLineCommand.isGift() || shoppingCartLineCommand.isCaptionLine()){
                    List<ShoppingCartLineCommand> list = shopIdAndShoppingCartLineCommandListMap.get(shopIdKey);
                    list.add(shoppingCartLineCommand);
                }
            }
        }
        for (Entry<Long, List<ShoppingCartLineCommand>> shopShopLine : shopIdAndShoppingCartLineCommandListMap.entrySet()){
            Long key = shopShopLine.getKey();
            List<ShoppingCartLineCommand> value = shopShopLine.getValue();

            ShoppingCartCommand shopShoppingCartCommand = shopIdAndShoppingCartCommandMap.get(key);
            shopShoppingCartCommand.setShoppingCartLineCommands(value);
        }
    }

    /**
     * 获得 temp shop line.
     *
     * @param shopId
     *            the shop id
     * @param lineId
     *            the line id
     * @param newLines
     *            the new lines
     * @param shopIdAndShoppingCartCommandMap
     *            the shop id and shopping cart command map
     * @return the temp shop line
     */
    private ShoppingCartLineCommand getTempShopLine(
                    Long shopId,
                    Long lineId,
                    List<ShoppingCartLineCommand> newLines,
                    Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){
        ShoppingCartCommand shopShoppingCartCommand = shopIdAndShoppingCartCommandMap.get(shopId);
        List<ShoppingCartLineCommand> shopShoppingCartLineList = shopShoppingCartCommand.getShoppingCartLineCommands();// 店铺的商品行数据

        ShoppingCartLineCommand tempShopLine = CollectionsUtil.find(shopShoppingCartLineList, "id", lineId);
        if (null == tempShopLine){
            tempShopLine = CollectionsUtil.find(
                            newLines,
                            PredicateUtils.andPredicate(
                                            new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("id", lineId),
                                            new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("shopId", shopId)));
        }
        return tempShopLine;
    }

    /**
     * Do with no choose.
     *
     * @param noChooseShoppingCartLineCommandList
     *            the no choose shopping cart line command list
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param shopIdAndShoppingCartCommandMap
     *            the shop id and shopping cart command map
     *///TODO feilong 感觉下面的逻辑没有用
    private void doWithNoChoose(
                    List<ShoppingCartLineCommand> noChooseShoppingCartLineCommandList,
                    ShoppingCartCommand shoppingCartCommand,
                    Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){

        // 因为某些NOTCHOOSE的购物车行不进行促销计算,如果全是无效数据,shopCartByShopIdMap中的值为空,所以这里帮助进行初始化shopCartByShopIdMap的数据
        for (ShoppingCartLineCommand noChooseShoppingCartLineCommand : noChooseShoppingCartLineCommandList){
            Long shopId = noChooseShoppingCartLineCommand.getShopId();
            ShoppingCartCommand shoppingCartCommandByShopId = shopIdAndShoppingCartCommandMap.get(shopId);

            if (shoppingCartCommandByShopId == null){
                shoppingCartCommandByShopId = new ShoppingCartCommand();
                List<ShoppingCartLineCommand> sclcList = new ArrayList<ShoppingCartLineCommand>();

                shoppingCartCommandByShopId.setShoppingCartLineCommands(sclcList);
                shopIdAndShoppingCartCommandMap.put(shopId, shoppingCartCommandByShopId);

                ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();
                shopCartCommandByShop.setQty(0);
                shopCartCommandByShop.setRealPayAmount(BigDecimal.ZERO);
                shopCartCommandByShop.setShopId(shopId);

                List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();
                summaryShopCartList.add(shopCartCommandByShop);

                shoppingCartCommand.setSummaryShopCartList(summaryShopCartList);
            }
        }
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
     * Builds the user details.
     *
     * @param memberId
     *            the member id
     * @param memberComIds
     *            the member com ids
     * @return the user details
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
     * @param userDetails
     *            the user details
     * @param coupons
     *            the coupons
     * @param validedLines
     *            the valided lines
     * @return the shopping cart command
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
        shoppingCartCommand.setOrderQuantity(ShoppingCartUtil.getSumQuantity(validedLines));

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
        List<ShoppingCartLineCommand> allShoppingCartLines = getAllShoppingCartLines(shopIdAndShoppingCartCommandMap);
        shoppingCartCommand.setShoppingCartLineCommands(allShoppingCartLines);

        // 设置应付金额
        List<ShoppingCartLineCommand> noGiftShoppingCartLines = CollectionsUtil.select(allShoppingCartLines, "gift", false);
        shoppingCartCommand.setOriginPayAmount(getOriginPayAmount(noGiftShoppingCartLines));

        Map<String, BigDecimal> priceMap = CollectionsUtil.sum(summaryShopCartList, "realPayAmount", "originShoppingFee", "offersShipping");

        // 实际支付金额
        shoppingCartCommand.setCurrentPayAmount(priceMap.get("realPayAmount").setScale(2, BigDecimal.ROUND_HALF_UP));

        // 应付运费
        shoppingCartCommand.setOriginShoppingFee(priceMap.get("originShoppingFee").setScale(2, BigDecimal.ROUND_HALF_UP));

        // 运费优惠
        BigDecimal offersShippingAmount = priceMap.get("offersShipping");

        // 计算实付运费
        BigDecimal originShoppingFee = shoppingCartCommand.getOriginShoppingFee();
        BigDecimal currentShippingAmountByShopCart = originShoppingFee.compareTo(offersShippingAmount) >= 0
                        ? originShoppingFee.subtract(offersShippingAmount) : BigDecimal.ZERO;
        // 实付运费
        shoppingCartCommand.setCurrentShippingFee(currentShippingAmountByShopCart);
    }

    /**
     * Builds the shop cart command by shop list.
     *
     * @param calcFreightCommand
     *            the calc freight command
     * @param shopIdAndPromotionBriefListMap
     *            the pro briefs map
     * @param shopIdAndShoppingCartCommandMap
     *            the shop id and shopping cart command map
     * @return the list< shop cart command by shop>
     */
    private List<ShopCartCommandByShop> buildShopCartCommandByShopList(
                    CalcFreightCommand calcFreightCommand,
                    Map<Long, List<PromotionBrief>> shopIdAndPromotionBriefListMap,
                    Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){
        List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopIdAndShoppingCartCommandMap.entrySet()){
            List<PromotionBrief> promotionBriefList = shopIdAndPromotionBriefListMap.get(entry.getKey());
            ShopCartCommandByShop shopCartCommandByShop = buildShopCartCommandByShop(
                            entry.getKey(),
                            entry.getValue(),
                            calcFreightCommand,
                            promotionBriefList);
            summaryShopCartList.add(shopCartCommandByShop);
        }
        return summaryShopCartList;
    }

    /**
     * Builds the shop cart command by shop.
     *
     * @param shopId
     *            the shop id
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param calcFreightCommand
     *            the calc freight command
     * @param promotionBriefList
     *            the promotion brief list
     * @return the shop cart command by shop
     */
    private ShopCartCommandByShop buildShopCartCommandByShop(
                    Long shopId,
                    ShoppingCartCommand shoppingCartCommand,
                    CalcFreightCommand calcFreightCommand,
                    List<PromotionBrief> promotionBriefList){

        if (Validator.isNotNullOrEmpty(promotionBriefList)){
            // 计算分店铺的优惠.只计算有效的购物车行
            ShopCartCommandByShop shopCartCommandByShop = getPromotionDiscountAmtSummarySkuListGroup(
                            shoppingCartCommand,
                            calcFreightCommand,
                            promotionBriefList);

            List<ShoppingCartLineCommand> groupShoppingCartLinesToDisplayByLinePromotionResult = sdkShoppingCartGroupManager
                            .groupShoppingCartLinesToDisplayByLinePromotionResult(shoppingCartCommand, promotionBriefList);

            shoppingCartCommand.setShoppingCartLineCommands(groupShoppingCartLinesToDisplayByLinePromotionResult);

            shopCartCommandByShop.setShopId(shopId);
            return shopCartCommandByShop;
        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();

        BigDecimal originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);

        //*****************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();
        // 应付运费
        shopCartCommandByShop.setOriginShoppingFee(originShippingFee);

        // 应付小计
        shopCartCommandByShop.setSubtotalCurrentPayAmount(getOriginPayAmount(shoppingCartLineCommandList));

        // 应付合计(应付小计+应付运费)
        BigDecimal sumCurrentPayAmt = originShippingFee.add(shopCartCommandByShop.getSubtotalCurrentPayAmount());
        shopCartCommandByShop.setSumCurrentPayAmount(sumCurrentPayAmt);

        // 实付合计
        shopCartCommandByShop.setRealPayAmount(sumCurrentPayAmt);

        // 应付金额
        shoppingCartCommand.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());

        // 实付金额
        shoppingCartCommand.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount());

        // 该店铺的商品数量
        shopCartCommandByShop.setQty(ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList));
        shopCartCommandByShop.setShopId(shopId);
        return shopCartCommandByShop;
    }

    /**
     * 构造用于购物车页面促销信息显示的数据.
     * 
     * @param shopCart
     *            the shop cart
     * @param calcFreightCommand
     *            the calc freight command
     * @param promotionBriefList
     *            the promotion brief list
     *
     * @return the promotion discount amt summary sku list group
     */
    private ShopCartCommandByShop getPromotionDiscountAmtSummarySkuListGroup(
                    ShoppingCartCommand shopCart,
                    CalcFreightCommand calcFreightCommand,
                    List<PromotionBrief> promotionBriefList){

        BigDecimal disAmtOnOrder = BigDecimal.ZERO;// 商品行优惠金额
        BigDecimal baseOnOrderDisAmt = BigDecimal.ZERO;// 整单优惠金额
        BigDecimal offersShippingDisAmt = BigDecimal.ZERO;// 运费整单优惠金额

        List<ShoppingCartLineCommand> giftList = new ArrayList<ShoppingCartLineCommand>();// 礼品行
        if (Validator.isNotNullOrEmpty(promotionBriefList)){

            for (PromotionBrief promotionBrief : promotionBriefList){
                List<PromotionSettingDetail> promotionSettingDetailList = promotionBrief.getDetails();
                if (Validator.isNullOrEmpty(promotionSettingDetailList)){
                    continue;
                }

                for (PromotionSettingDetail promotionSettingDetail : promotionSettingDetailList){
                    List<PromotionSKUDiscAMTBySetting> affectPromotionSKUDiscAMTBySettingList = promotionSettingDetail
                                    .getAffectSKUDiscountAMTList();

                    if (Validator.isNotNullOrEmpty(affectPromotionSKUDiscAMTBySettingList)){
                        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : affectPromotionSKUDiscAMTBySettingList){
                            // 如果是礼品
                            if (promotionSKUDiscAMTBySetting.getGiftMark()){
                                giftList.add(toGiftShoppingCartLineCommand(promotionSKUDiscAMTBySetting));
                            }else{
                                BigDecimal skuDisAmt = promotionSKUDiscAMTBySetting.getDiscountAmount();
                                disAmtOnOrder = disAmtOnOrder.add(skuDisAmt);// 非礼品才算优惠
                            }
                        }
                    }else{
                        BigDecimal disAmt = promotionSettingDetail.getDiscountAmount();// 基于整单的
                        if (promotionSettingDetail.getFreeShippingMark()){
                            offersShippingDisAmt = offersShippingDisAmt.add(disAmt); // 运费优惠
                        }else{
                            baseOnOrderDisAmt = baseOnOrderDisAmt.add(disAmt);
                        }
                    }
                }
            }
        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shopCart.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id

        //***************************************************************************************

        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();

        shopCartCommandByShop.setQty(ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList));// 商品数量

        BigDecimal originPayAmount = getOriginPayAmount(shoppingCartLineCommandList);
        shopCartCommandByShop.setSubtotalCurrentPayAmount(originPayAmount); // 应付小计
        shopCartCommandByShop.setSumCurrentPayAmount(originPayAmount); // 应付合计

        BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)

        shopCartCommandByShop.setOffersShipping(BigDecimal.ZERO);// 计算运费
        shopCartCommandByShop.setOriginShoppingFee(BigDecimal.ZERO);// 源运费

        BigDecimal originShippingFee = BigDecimal.ZERO; // 应付运费
        BigDecimal currentShippingFee = BigDecimal.ZERO;// 实付运费

        if (null != calcFreightCommand){
            originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);
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

        //TODO feilong 原来是这里加礼品的
        shoppingCartLineCommandList.addAll(giftList); // 将礼品放入购物车当中
        shopCart.setShoppingCartLineCommands(shoppingCartLineCommandList);

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
     * 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值.
     *
     * @param shoppingCartCommand
     *            the shopping cart
     * @param promotionBriefList
     *            the promotion brief list
     */
    private void shareDiscountToLine(ShoppingCartCommand shoppingCartCommand,List<PromotionBrief> promotionBriefList){
        // 分摊结果
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCartCommand, promotionBriefList);
        if (null == promotionSKUDiscAMTBySettingList || promotionSKUDiscAMTBySettingList.size() == 0
                        || shoppingCartCommand.getShoppingCartLineCommands() == null
                        || shoppingCartCommand.getShoppingCartLineCommands().size() == 0){
            return;
        }

        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartCommand.getShoppingCartLineCommands()){
            if (shoppingCartLineCommand.isGift()){
                continue;
            }
            BigDecimal curSKUDiscount = getCurSKUDiscount(promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
            shoppingCartLineCommand.setDiscount(curSKUDiscount);
            // 购物车行小计
            BigDecimal subTotalAmt = NumberUtil
                            .getMultiplyValue(shoppingCartLineCommand.getQuantity(), shoppingCartLineCommand.getSalePrice());

            BigDecimal lineSubTotalAmt = subTotalAmt.subtract(curSKUDiscount);

            shoppingCartLineCommand.setSubTotalAmt(lineSubTotalAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : lineSubTotalAmt);
        }
    }

    /**
     * 封装礼品购物车行.
     *
     * @param promotionSKUDiscAMTBySetting
     *            the pro sku
     * @return the gift shopping cart line command
     */
    private ShoppingCartLineCommand toGiftShoppingCartLineCommand(PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
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
        //TODO feilong what logic?
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

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
            allLines.addAll(entry.getValue().getShoppingCartLineCommands());
        }
        return allLines;
    }

    //**********************************************************************************************

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
        Set<String> itemComboIdsSet = ShoppingCartUtil.getItemComboIds(shoppingCartLineCommandList);

        List<PromotionCommand> promotionList = sdkPromotionRuleFilterManager.getIntersectActivityRuleData(
                        new ArrayList<Long>(shopIdSet),
                        memboSet,
                        itemComboIdsSet,
                        shoppingCartCommand.getCurrentTime());

        if (Validator.isNotNullOrEmpty(promotionList)){
            // 通过购物车和促销集合计算商品促销
            return sdkPromotionCalculationManager.calculationPromotion(shoppingCartCommand, promotionList);
        }
        return Collections.emptyList();
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
        return CollectionsUtil.group(shoppingCartLineCommandList, "shopId", new Predicate<ShoppingCartLineCommand>(){

            @Override
            public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){
                return !shoppingCartLineCommand.isCaptionLine();
            }
        });
    }

    private BigDecimal getCurSKUDiscount(
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    final ShoppingCartLineCommand shoppingCartLineCommand){
        final Long skuId = shoppingCartLineCommand.getSkuId();
        //TODO feilong 提取
        BigDecimal discountAmountSum = CollectionsUtil
                        .sum(promotionSKUDiscAMTBySettingList, "discountAmount", new Predicate<PromotionSKUDiscAMTBySetting>(){

                            @Override
                            public boolean evaluate(PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
                                boolean freeShipOrGiftMark = promotionSKUDiscAMTBySetting.getFreeShippingMark()
                                                || promotionSKUDiscAMTBySetting.getGiftMark();
                                return !freeShipOrGiftMark && skuId.equals(promotionSKUDiscAMTBySetting.getSkuId());
                            }
                        });
        return null == discountAmountSum ? BigDecimal.ZERO : discountAmountSum;
    }

}
