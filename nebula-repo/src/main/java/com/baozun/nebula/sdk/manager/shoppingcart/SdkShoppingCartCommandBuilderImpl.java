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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;
import com.baozun.nebula.sdk.manager.shoppingcart.handler.PromotionBriefBuilder;
import com.baozun.nebula.sdk.manager.shoppingcart.handler.PromotionOrderManager;
import com.baozun.nebula.sdk.manager.shoppingcart.handler.ShopCartCommandByShopBuilder;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPropertyValueEqualsPredicate;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 专门用来构建 ShoppingCartCommand.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartCommandBuilder")
public class SdkShoppingCartCommandBuilderImpl implements SdkShoppingCartCommandBuilder{

    /** The Constant log. */
    private static final Logger                        LOGGER        = LoggerFactory.getLogger(SdkShoppingCartCommandBuilderImpl.class);

    /** The Constant CHECKED_STATE. */
    private static final int                           CHECKED_STATE = 1;

    /** The sdk shopping cart line command behaviour factory. */
    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                           sdkEngineManager;

    /** The sdk shopping cart group manager. */
    @Autowired
    private SdkShoppingCartGroupManager                sdkShoppingCartGroupManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager                         sdkMataInfoManager;

    @Autowired
    private ShopCartCommandByShopBuilder               shopCartCommandByShopBuilder;

    /** The promotion brief builder. */
    @Autowired
    private PromotionBriefBuilder                      promotionBriefBuilder;

    /** The promotion order manager. */
    @Autowired
    private PromotionOrderManager                      promotionOrderManager;

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

            ShoppingCartLineCommandBehaviour shoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                            .getShoppingCartLineCommandBehaviour(shoppingCartLineCommand);
            shoppingCartLineCommandBehaviour.packShoppingCartLine(shoppingCartLineCommand); // 封装购物车行信息
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

        //XXX feilong 全不选的情况
        if (Validator.isNotNullOrEmpty(shoppingCartCommand.getShoppingCartLineCommands())){
            // 设置购物车促销信息
            setShopCartPromotionInfos(shoppingCartCommand, calcFreightCommand);
        }

        //***************************************************************************************
        //XXX feilong what's this?
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
    /**
     * Do with rebuild shopping cart line commands.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param noChooseShoppingCartLineCommandList
     *            the no choose shopping cart line command list
     * @param lineIdAndShopIdMapList
     *            the line id and shop id map list
     * @param shopIdAndShoppingCartCommandMap
     *            the shop id and shopping cart command map
     */
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
     *///XXX feilong 感觉下面的逻辑没有用
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
                shopCartCommandByShop.setRealPayAmount(ZERO);
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
        shoppingCartCommand.setOriginPayAmount(ShoppingCartUtil.getOriginPayAmount(validedLines));

        // 购物车行信息
        shoppingCartCommand.setShoppingCartLineCommands(validedLines);

        //**********************************************************************************************

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
     * 设置购物车的促销信息.
     *
     * @param shoppingCartCommand
     *            the shopping cart
     * @param calcFreightCommand
     *            the calc freight command
     */
    private void setShopCartPromotionInfos(ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand){
        // 获取促销数据.需要调用促销引擎计算优惠价格
        List<PromotionBrief> promotionBriefList = promotionBriefBuilder.getPromotionBriefList(shoppingCartCommand);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(promotionBriefList));
        }

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

        setShoppingCartCommandPrice(shoppingCartCommand, summaryShopCartList, allShoppingCartLines);
    }

    /**
     * @param shoppingCartCommand
     * @param summaryShopCartList
     * @param allShoppingCartLines
     */
    private void setShoppingCartCommandPrice(
                    ShoppingCartCommand shoppingCartCommand,
                    List<ShopCartCommandByShop> summaryShopCartList,
                    List<ShoppingCartLineCommand> allShoppingCartLines){
        // 设置应付金额
        shoppingCartCommand.setOriginPayAmount(ShoppingCartUtil.getOriginPayAmount(allShoppingCartLines));

        Map<String, BigDecimal> priceMap = CollectionsUtil.sum(summaryShopCartList, "realPayAmount", "originShoppingFee", "offersShipping");

        // 实际支付金额
        shoppingCartCommand.setCurrentPayAmount(priceMap.get("realPayAmount").setScale(2, ROUND_HALF_UP));

        // 应付运费
        shoppingCartCommand.setOriginShoppingFee(priceMap.get("originShoppingFee").setScale(2, ROUND_HALF_UP));

        // 运费优惠
        BigDecimal offersShippingAmount = priceMap.get("offersShipping");

        // 计算实付运费
        BigDecimal originShoppingFee = shoppingCartCommand.getOriginShoppingFee();
        BigDecimal currentShippingAmountByShopCart = originShoppingFee.compareTo(offersShippingAmount) >= 0
                        ? originShoppingFee.subtract(offersShippingAmount) : ZERO;
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
        if (isNotNullOrEmpty(promotionBriefList)){
            // 计算分店铺的优惠.只计算有效的购物车行
            ShopCartCommandByShop shopCartCommandByShop = promotionOrderManager
                            .buildShopCartCommandByShop(shoppingCartCommand, calcFreightCommand, promotionBriefList);

            shoppingCartCommand.setShoppingCartLineCommands(
                            sdkShoppingCartGroupManager
                                            .groupShoppingCartLinesToDisplayByLinePromotionResult(shoppingCartCommand, promotionBriefList));
            return shopCartCommandByShop;
        }

        return shopCartCommandByShopBuilder.build(shopId, shoppingCartCommand, calcFreightCommand);
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

    /**
     * 获得 shop id and shopping cart line command list.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the shop id and shopping cart line command list
     */
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

}
