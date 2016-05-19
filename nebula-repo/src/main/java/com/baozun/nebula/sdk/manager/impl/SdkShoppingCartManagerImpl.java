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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.promotion.PromotionCoupon;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
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
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkFilterManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationSettingManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCouponManager;
import com.baozun.nebula.sdk.manager.SdkPromotionGuideManager;
import com.baozun.nebula.sdk.manager.SdkPromotionManager;
import com.baozun.nebula.sdk.manager.SdkPromotionRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartGroupManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartLinesManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;

@Transactional
@Service("sdkShoppingCartService")
public class SdkShoppingCartManagerImpl implements SdkShoppingCartManager{

    private static final Logger                      log             = LoggerFactory.getLogger(SdkShoppingCartManagerImpl.class);

    /** 程序返回结果 **/
    private static final Integer                     SUCCESS         = 1;

    private static final Integer                     FAILURE         = 0;

    private static final int                         CHECKED_STATE   = 1;

    /** 优惠设置是否按单件计算，是指按Qty计算，还是Qty为1来计算 **/
    private static final BigDecimal                  ONE_PIECE_QTY   = new BigDecimal(1);

    /** 百分百 **/
    private static final BigDecimal                  HUNDRED_PERCENT = new BigDecimal(1);

    @Autowired
    private SdkShoppingCartLineDao                   sdkShoppingCartLineDao;

    @Autowired
    private ItemInfoDao                              itemInfoDao;

    @Autowired
    private SkuDao                                   skuDao;

    @Autowired
    private ItemCategoryDao                          itemCategoryDao;

    @Autowired
    private SdkFilterManager                         sdkFilterManager;

    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    @Autowired
    private SdkPromotionCalculationManager           sdkPromotionCalculationManager;

    @Autowired
    private SdkPromotionRuleFilterManager            sdkPromotionRuleFilterManager;

    @Autowired
    private LogisticsManager                         logisticsManager;

    @Autowired
    private SdkOrderLineDao                          sdkOrderLineDao;

    @Autowired
    private SdkPurchaseLimitRuleFilterManager        sdkPurchaseRuleFilterManager;

    @Autowired
    private SdkEffectiveManager                      sdkEffectiveManager;

    @Autowired
    private SdkPromotionCouponManager                sdkPromotionCouponManager;

    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    @Autowired
    private SdkPromotionCalculationSettingManager    sdkPromotionSettingManager;

    @Autowired
    private SdkShoppingCartGroupManager              sdkShoppingCartGroupManager;

    @Autowired
    private SdkPromotionGuideManager                 sdkPromotionGuideManager;

    @Autowired
    private SdkMataInfoManager                       sdkMataInfoManager;

    @Autowired
    private SdkShoppingCartLinesManager              sdkShoppingCartLinesManager;

    @Autowired
    private SdkPromotionManager                      sdkPromotionManager;

    /**
     * 是否包含此行进行计算
     * 
     * @return
     */
    private boolean needContainsLineCalc(Integer SettlementState,boolean isValid){
        /**
         * 购物车行用于计算的级别
         */
        String calcLevel = sdkMataInfoManager.findValue(MataInfo.KEY_SC_CALC_LEVEL);
        // 选中级别
        if (calcLevel == null || calcLevel.equals("CHECKED")){
            if (SettlementState.equals(CHECKED_STATE)){
                return true;
            }else{
                return false;
            }
        }
        // 有效级别
        else if (calcLevel.equals("AVAILABLE")){
            if (isValid){
                return true;
            }else{
                return false;
            }
        }
        // 有效，并且选中
        else if (calcLevel.equals("CHECKEDANDAVAILABLE")){
            if (SettlementState.equals(CHECKED_STATE) && isValid){
                return true;
            }else{
                return false;
            }

        }
        // 其它，all
        else{
            return true;
        }

    }

    /**
     * 通过系统配置的计算级别选出参与促销计算以及不参与促销计算的购物车行
     * 
     * @param allLines
     * @param chooseLines
     * @param notChooseLines
     * @return
     */
    private List<ShoppingCartLineCommand> splitByCalcLevel(
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

        return allLines;
    }

    private boolean needContainsLineCalc2(Integer SettlementState,boolean isValid){

        return false;

    }

    /**
     * @param userId
     * @param memComIds
     *            组合id
     * @param coupons
     *            优惠券
     * @param calFreightCommand
     * @param shoppingCartLines
     *            获取购物车列表时候要经过 有效性引擎和促销引擎。 不走限购检查引擎 callType==1是为点结算按钮提供的判断条件 callType==2是为点立即购买按钮提供的判断条件
     *            CalcFreightCommand 不为空时计算运费 为空不计算运费
     */
    @Override
    @Transactional(readOnly = true)
    public ShoppingCartCommand findShoppingCart(
                    Long userId,
                    Set<String> memComIds,
                    List<String> coupons,
                    CalcFreightCommand calcFreightCommand,
                    List<ShoppingCartLineCommand> shoppingCartLines){
        if (null == shoppingCartLines || shoppingCartLines.size() == 0)
            return null;
        // 返回购物车对象
        ShoppingCartCommand shopCart = getShoppingCart(shoppingCartLines, coupons, userId, calcFreightCommand, memComIds);
        return shopCart;
    }

    /**
     * 获取购物车对象
     * 
     * @param validedLines
     * @param userDetails
     * @param shoppingCartLines
     * @param coupons
     * @return
     */
    private ShoppingCartCommand getShoppingCart(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<String> coupons,
                    Long memberId,
                    CalcFreightCommand calcFreightCommand,
                    Set<String> memComIds){

        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();// 购物车对象
        List<ShoppingCartLineCommand> chooseLines = new ArrayList<ShoppingCartLineCommand>();// 被选中的购物车行
        List<ShoppingCartLineCommand> notChooseLines = new ArrayList<ShoppingCartLineCommand>();// 未选中的购物车行
        List<String> lineSortIds = new ArrayList<String>();// 给每一条购物记录添加唯一值

        int i = 0;
        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartLines){

            if (shoppingCartLine.getId() == null){
                shoppingCartLine.setId(new Long(--i));
            }

            sdkEngineManager.packShoppingCartLine(shoppingCartLine); // 封装购物车行信息

            shoppingCartLine.setType(Constants.ITEM_TYPE_SALE);// 主卖品

            lineSortIds.add(shoppingCartLine.getId() + "," + shoppingCartLine.getShopId());

            // 购物车行 金额小计
            shoppingCartLine.setSubTotalAmt(new BigDecimal(shoppingCartLine.getQuantity()).multiply(shoppingCartLine.getSalePrice()));

        }

        // 判断是否是被选中的购物车行
        splitByCalcLevel(shoppingCartLines, chooseLines, notChooseLines);

        // 设置memcombo
        UserDetails userDetails = new UserDetails();
        userDetails.setMemberId(memberId);
        if (null != memberId){
            userDetails.setMemComboList(memComIds);
        }else{
            userDetails.setMemComboList(getMemboIds());
        }

        // 封装购物车的基本信息
        packShopBaseInfo(shoppingCart, userDetails, coupons, chooseLines);

        // 所有购物车行数据
        shoppingCart.setShoppingCartLineCommands(chooseLines);

        // 根据店铺封装购物车对象
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = getShopCartByShopId(shoppingCart);

        // 设置分店铺的购物车
        shoppingCart.setShoppingCartByShopIdMap(shopCartByShopIdMap);

        // 获取购物车促销信息
        getShopCartPromotionInfos(shoppingCart, calcFreightCommand);

        List<ShoppingCartLineCommand> newLines = new ArrayList<ShoppingCartLineCommand>();// 所有商品行数据包括选中和不选中的
        // 所有门店商品行数据包括选中和不选中的
        Map<Long, List<ShoppingCartLineCommand>> shopCommandMap = new HashMap<Long, List<ShoppingCartLineCommand>>();

        // 因为某些NOTCHOOSE的购物车行不进行促销计算,如果全是无效数据,shopCartByShopIdMap中的值为空,所以这里帮助进行初始化shopCartByShopIdMap的数据
        for (ShoppingCartLineCommand sclc : notChooseLines){
            Long shopId = sclc.getShopId();
            ShoppingCartCommand scc = shoppingCart.getShoppingCartByShopIdMap().get(shopId);
            if (scc == null){
                scc = new ShoppingCartCommand();
                List<ShoppingCartLineCommand> sclcList = new ArrayList<ShoppingCartLineCommand>();

                scc.setShoppingCartLineCommands(sclcList);
                shoppingCart.getShoppingCartByShopIdMap().put(shopId, scc);
                List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

                ShopCartCommandByShop summaryShopCart = new ShopCartCommandByShop();
                summaryShopCart.setQty(0);
                summaryShopCart.setRealPayAmount(BigDecimal.ZERO);
                summaryShopCart.setShopId(shopId);
                summaryShopCartList.add(summaryShopCart);
                shoppingCart.setSummaryShopCartList(summaryShopCartList);
            }

        }

        for (String lineSortIdAndShop : lineSortIds){

            String[] idAndShop = lineSortIdAndShop.split(",");
            Long id = Long.parseLong(idAndShop[0]);
            Long shopId = Long.parseLong(idAndShop[1]);

            // 循环所有原始记录行 开始
            for (ShoppingCartLineCommand chooseLine : shoppingCart.getShoppingCartLineCommands()){
                if (id.equals(chooseLine.getId())){
                    newLines.add(chooseLine);
                    break;
                }
            }
            for (ShoppingCartLineCommand notChooseLine : notChooseLines){
                if (id.equals(notChooseLine.getId())){
                    newLines.add(notChooseLine);
                    break;
                }
            }
            // 循环所有原始记录行 结束

            // 循环添加门店所有行 开始
            for (Entry<Long, ShoppingCartCommand> shopCommand : shoppingCart.getShoppingCartByShopIdMap().entrySet()){

                Long shopIdKey = shopCommand.getKey();// 店铺ID
                List<ShoppingCartLineCommand> shopLineValues = shopCommand.getValue().getShoppingCartLineCommands();// 店铺的商品行数据

                if (!shopId.equals(shopIdKey)){
                    continue;
                }

                ShoppingCartLineCommand tempShopLine = null;

                for (ShoppingCartLineCommand shopLine : shopLineValues){
                    if (id.equals(shopLine.getId())){
                        tempShopLine = shopLine;
                        break;
                    }
                }
                if (null == tempShopLine){
                    for (ShoppingCartLineCommand newLine : newLines){
                        if (id.equals(newLine.getId()) && shopId.equals(newLine.getShopId())){
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

        // 循环添加所有赠品记录行 开始
        for (ShoppingCartLineCommand giftLine : shoppingCart.getShoppingCartLineCommands()){
            if (giftLine.isGift() || giftLine.isCaptionLine()){
                newLines.add(giftLine);
            }
        }
        shoppingCart.setShoppingCartLineCommands(newLines);

        // 循环添加所有赠品记录行 结束

        // 循环添加店铺所有增品记录行 开始
        for (Entry<Long, ShoppingCartCommand> shopCommand : shoppingCart.getShoppingCartByShopIdMap().entrySet()){
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
                shoppingCart.getShoppingCartByShopIdMap().get(shopShopLine.getKey()).setShoppingCartLineCommands(shopShopLine.getValue());
            }
        }
        // 循环添加店铺所有增品记录行 结束
        return shoppingCart;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> findShoppingCartLinesByMemberId(Long memberId,Integer callType){
        List<ShoppingCartLineCommand> shoppingCartLines = null;
        if (callType == Constants.CHECKED_CHOOSE_STATE){// 查询选中状态的购物车数据
            shoppingCartLines = sdkShoppingCartLineDao.findShopCartLineByMemberId(memberId, Constants.CHECKED_CHOOSE_STATE);
        }else{
            // 查询全部的购物车数据
            shoppingCartLines = sdkShoppingCartLineDao.findShopCartLineByMemberId(memberId, null);
        }
        return shoppingCartLines;
    }

    /**
     * 获取当前人员的选择的赠品行
     * 
     * @param memberId
     * @param callType
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> findShoppingCartGiftLinesByMemberId(Long memberId,Integer callType){
        callType = Constants.CHECKED_CHOOSE_STATE;

        List<ShoppingCartLineCommand> shoppingCartLines = null;
        if (callType == Constants.CHECKED_CHOOSE_STATE){// 查询选中状态的购物车数据
            shoppingCartLines = sdkShoppingCartLineDao.findShopCartGiftLineByMemberId(memberId, Constants.CHECKED_CHOOSE_STATE);
        }else{
            // 查询全部的购物车数据
            shoppingCartLines = sdkShoppingCartLineDao.findShopCartGiftLineByMemberId(memberId, null);
        }
        return shoppingCartLines;
    }

    /**
     * 封装购物车的基本信息
     * 
     * @param shoppingCart
     * @param userDetails
     * @param coupons
     * @param validedLines
     */
    private void packShopBaseInfo(
                    ShoppingCartCommand shoppingCart,
                    UserDetails userDetails,
                    List<String> coupons,
                    List<ShoppingCartLineCommand> validedLines){
        // 会员信息
        shoppingCart.setUserDetails(userDetails);

        // 获取应付金额
        BigDecimal originPayAmount = getOriginPayAmount(validedLines);

        // 设置应付金额
        shoppingCart.setOriginPayAmount(originPayAmount);

        // 优惠券编码
        shoppingCart.setCoupons(coupons);

        // 设置当前时间
        if (null == shoppingCart.getCurrentTime()){
            shoppingCart.setCurrentTime(new Date());
        }

        // 购物车行信息
        shoppingCart.setShoppingCartLineCommands(validedLines);

        // 商品数量
        shoppingCart.setOrderQuantity(getOrderQuantity(validedLines));
    }

    /**
     * 无促销情况下计算运费
     * 
     * @param calcFreightCommand
     * @param cartByShop
     * @param cart
     * @param shopId
     */
    private BigDecimal getFreightFee(CalcFreightCommand calcFreightCommand,List<ShoppingCartLineCommand> validLines,Long shopId){
        BigDecimal originShippingFee = BigDecimal.ZERO;
        if (null != calcFreightCommand){
            Boolean flag = logisticsManager.hasDistributionMode(calcFreightCommand, shopId);
            if (flag){
                // 无促销情况下统计金额小计
                List<ItemFreightInfoCommand> itemList = new ArrayList<ItemFreightInfoCommand>();
                for (ShoppingCartLineCommand line : validLines){
                    ItemFreightInfoCommand itemInfo = new ItemFreightInfoCommand();
                    itemInfo.setItemId(line.getItemId());
                    itemInfo.setCount(line.getQuantity());
                    itemList.add(itemInfo);
                }
                originShippingFee = logisticsManager.findFreight(
                                itemList,
                                calcFreightCommand.getDistributionModeId(),
                                shopId,
                                calcFreightCommand.getProvienceId(),
                                calcFreightCommand.getCityId(),
                                calcFreightCommand.getCountyId(),
                                calcFreightCommand.getTownId());
            }

        }
        return originShippingFee;
    }

    /**
     * 获取购物车的促销信息
     * 
     * @param shoppingCart
     * @param calcFreightCommand
     */
    private void getShopCartPromotionInfos(ShoppingCartCommand shoppingCart,CalcFreightCommand calcFreightCommand){
        // 获取促销数据.需要调用促销引擎计算优惠价格
        List<PromotionBrief> promotionBriefList = calcuPromoBriefs(shoppingCart);

        HashMap<Long, ArrayList<PromotionBrief>> proBriefsMap = new HashMap<Long, ArrayList<PromotionBrief>>();

        if (null != promotionBriefList && promotionBriefList.size() > 0){
            // 区分店铺的促销
            for (PromotionBrief brief : promotionBriefList){
                Long shopId = brief.getShopId();
                ArrayList<PromotionBrief> briefs = proBriefsMap.get(shopId);
                // 不存在
                if (null == briefs){
                    briefs = new ArrayList<PromotionBrief>();
                }
                briefs.add(brief);
                proBriefsMap.put(shopId, briefs);
            }
        }

        ShopCartCommandByShop cartByShop = null;
        // 区分店铺的购物车
        Map<Long, ShoppingCartCommand> shopCartMap = shoppingCart.getShoppingCartByShopIdMap();
        List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){

            Long shopId = entry.getKey();
            ShoppingCartCommand cart = entry.getValue();
            List<ShoppingCartLineCommand> lines = cart.getShoppingCartLineCommands();

            ArrayList<PromotionBrief> briefs = proBriefsMap.get(shopId);

            if (null == briefs || briefs.size() == 0){
                BigDecimal originShippingFee = BigDecimal.ZERO;
                if (null != calcFreightCommand){
                    originShippingFee = getFreightFee(calcFreightCommand, lines, shopId);
                }
                cartByShop = new ShopCartCommandByShop();
                // 应付运费
                cartByShop.setOriginShoppingFee(originShippingFee);

                // 应付小计
                cartByShop.setSubtotalCurrentPayAmount(getOriginPayAmount(lines));

                // 应付合计(应付小计+应付运费)
                BigDecimal sumCurrentPayAmt = originShippingFee.add(cartByShop.getSubtotalCurrentPayAmount());
                cartByShop.setSumCurrentPayAmount(sumCurrentPayAmt);

                // 实付合计
                cartByShop.setRealPayAmount(sumCurrentPayAmt);

                // 应付金额
                cart.setOriginPayAmount(cartByShop.getSumCurrentPayAmount());

                // 实付金额
                cart.setCurrentPayAmount(cartByShop.getRealPayAmount());

                // 该店铺的商品数量
                cartByShop.setQty(getOrderQuantity(lines));
            }else{
                // 计算分店铺的优惠.只计算有效的购物车行
                cart.setShoppingCartLineCommands(lines);
                cartByShop = getPromotionDiscountAmtSummarySkuListGroup(briefs, cart, calcFreightCommand);
                lines = sdkShoppingCartGroupManager.groupShoppingCartLinesToDisplayByLinePromotionResult(cart, briefs);
                cart.setShoppingCartLineCommands(lines);
            }
            cartByShop.setShopId(shopId);
            summaryShopCartList.add(cartByShop);
        }

        shoppingCart.setSummaryShopCartList(summaryShopCartList);

        // 购物车促销简介信息
        shoppingCart.setCartPromotionBriefList(promotionBriefList);

        // 所有购物车行(可能包含了礼品\有效、无效)
        shoppingCart.setShoppingCartLineCommands(getAllShoppingCartLines(shopCartMap));

        // 获取应付金额
        BigDecimal originPayAmount = getOriginPayAmount(getNoGiftShoppingCartLines(shopCartMap));

        // 设置应付金额
        shoppingCart.setOriginPayAmount(originPayAmount);

        // 实际支付金额
        BigDecimal realPayAmount = getRealPayAmountByShopCart(summaryShopCartList);

        // 设置实付金额
        shoppingCart.setCurrentPayAmount(realPayAmount);

        // 应付运费
        shoppingCart.setOriginShoppingFee(getOrginShippingAmountByShopCart(summaryShopCartList));

        // 实付运费
        shoppingCart.setCurrentShippingFee(getCurrentShippingAmountByShopCart(summaryShopCartList, shoppingCart.getOriginShoppingFee()));
    }

    /**
     * 更新购物车中相关的促销信息
     * 
     * @param promotionLines
     * @param lines
     * 
     *            更新的内容有 lineGroup ,promotionId,isGift
     */
    private void updateShoppingCartPromotinInfo(
                    List<ShoppingCartLineCommand> promotionLines,
                    List<ShoppingCartLineCommand> lines,
                    Long memberId){
        if (null != promotionLines && promotionLines.size() > 0 && null != lines && lines.size() > 0){
            Map<Long, ShoppingCartLineCommand> cartLineMap = new HashMap<Long, ShoppingCartLineCommand>();
            Map<Long, ShoppingCartLineCommand> promotionLineMap = new HashMap<Long, ShoppingCartLineCommand>();
            List<Long> needDelLineList = new ArrayList<Long>();
            List<ShoppingCartLineCommand> needAdd = new ArrayList<ShoppingCartLineCommand>();
            List<ShoppingCartLineCommand> needUpdate = new ArrayList<ShoppingCartLineCommand>();
            // 封装购物车表里面的map
            for (ShoppingCartLineCommand line : lines){
                cartLineMap.put(line.getId(), line);
            }
            // 遍历从引擎中出来的购物车行
            for (ShoppingCartLineCommand promoLine : promotionLines){
                if (null != promoLine.getId()){
                    // 已经存在id 理论上购物车表里面已经存在 不存在报异常
                    ShoppingCartLineCommand oldLine = cartLineMap.get(promoLine.getId());
                    if (null == oldLine){
                        throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
                    }
                    // 比较判断是否需要更新
                    if (!compareString(promoLine.getLineGroup() + "", oldLine.getLineGroup() + "")){
                        // lingGroup不同 则更新
                        needUpdate.add(oldLine);
                    }else if (!compareString(promoLine.isGift() + "", oldLine.isGift() + "")){
                        // 一个是赠品行一个是非赠品行 则更新
                        needUpdate.add(oldLine);
                    }else if (!compareString(promoLine.getPromotionId() + "", oldLine.getPromotionId() + "")){
                        // promotionId换掉 则更新
                        needUpdate.add(oldLine);
                    }
                    promotionLineMap.put(promoLine.getId(), promoLine);
                }else{
                    // 如果还没有id 但是是被选中的赠品行或者套餐 则加入购物车表
                    if (promoLine.isGift() && new Integer(1).equals(promoLine.getSettlementState())){
                        // 理论上这种情况不存在 不过还是写一下吧
                        needAdd.add(promoLine);
                    }else if (promoLine.isSuitLine()){
                        needAdd.add(promoLine);
                    }
                }
            }
            for (ShoppingCartLineCommand line : lines){
                // 在购物车引擎中不存在
                if (!promotionLineMap.containsKey(line.getId())){
                    needDelLineList.add(line.getId());
                }
            }
            execShoppingCartUpdate(needDelLineList, needAdd, needUpdate, memberId);
        }
    }

    /**
     * 执行购物车更新
     * 
     * @param needDelLineList
     * @param needAdd
     * @param needUpdate
     */
    private void execShoppingCartUpdate(
                    List<Long> needDelLineList,
                    List<ShoppingCartLineCommand> needAdd,
                    List<ShoppingCartLineCommand> needUpdate,
                    Long memberId){
        // 1 搞掉要删除的
        if (null != needDelLineList && needDelLineList.size() > 0){
            for (Long cartLineId : needDelLineList){
                sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, cartLineId);
            }
        }
        // 2更新改更新的
        if (null != needUpdate && needUpdate.size() > 0){
            for (ShoppingCartLineCommand cartLine : needUpdate){
                sdkShoppingCartLineDao.updateCartLinePromotionInfo(
                                cartLine.getId(),
                                cartLine.getLineGroup(),
                                cartLine.isGift(),
                                cartLine.getPromotionId());
            }
        }
        // 3添加该添加的
        if (null != needAdd && needAdd.size() > 0){
            for (ShoppingCartLineCommand cartLine : needAdd){
                sdkShoppingCartLineDao.insertShoppingCartLineWithLineGroup(
                                cartLine.getExtentionCode(),
                                cartLine.getSkuId(),
                                cartLine.getQuantity(),
                                memberId,
                                new Date(),
                                cartLine.getSettlementState(),
                                cartLine.getShopId(),
                                cartLine.getLineGroup() + "",
                                cartLine.isGift(),
                                cartLine.getPromotionId());
            }
        }
    }

    /**
     * 比较两个字符串
     * 
     * @param str1
     * @param str2
     * @return
     */
    private boolean compareString(String str1,String str2){
        return (null == str1 && null == str2) || (null != str1 && str1.equals(str2));
    }

    /**
     * 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
     * 
     * @param shoppingCart
     * @param promotionBriefList
     */
    private void shareDiscountToLine(ShoppingCartCommand shoppingCart,List<PromotionBrief> promotionBriefList){
        // 分摊结果
        List<PromotionSKUDiscAMTBySetting> shareList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCart, promotionBriefList);
        if (null == shareList || shareList.size() == 0 || shoppingCart.getShoppingCartLineCommands() == null
                        || shoppingCart.getShoppingCartLineCommands().size() == 0)
            return;

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
     * 返回整个购物车数据
     * 
     * @param summaryShopCartList
     * @return
     */
    private List<ShoppingCartLineCommand> getAllShoppingCartLines(Map<Long, ShoppingCartCommand> shopCartMap){
        List<ShoppingCartLineCommand> allLines = new ArrayList<ShoppingCartLineCommand>();

        if (null != shopCartMap && shopCartMap.size() > 0){
            for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
                ShoppingCartCommand cart = entry.getValue();
                allLines.addAll(cart.getShoppingCartLineCommands());
            }
        }
        return allLines;
    }

    /**
     * 获取非礼品购物车行
     * 
     * @param shopCartMap
     * @return
     */
    private List<ShoppingCartLineCommand> getNoGiftShoppingCartLines(Map<Long, ShoppingCartCommand> shopCartMap){
        List<ShoppingCartLineCommand> noGiftLines = new ArrayList<ShoppingCartLineCommand>();

        if (null != shopCartMap && shopCartMap.size() > 0){
            for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
                ShoppingCartCommand cart = entry.getValue();
                List<ShoppingCartLineCommand> cartLines = cart.getShoppingCartLineCommands();
                if (null == cartLines || cartLines.size() == 0)
                    continue;
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
     * 构造用于购物车页面促销信息显示的数据
     * 
     * @deprecated
     * 
     * @param promotionBriefList
     * @param shopCart
     * @param calcFreightCommand
     * @return
     */
    private ShopCartCommandByShop getPromotionDiscountAmtSummarySkuList(
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
                if (null == details || details.size() == 0)
                    continue;
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

        ShopCartCommandByShop cartShop = new ShopCartCommandByShop();

        cartShop.setQty(getOrderQuantity(lines));// 商品数量

        cartShop.setSubtotalCurrentPayAmount(getOriginPayAmount(lines)); // 应付小计
        cartShop.setSumCurrentPayAmount(cartShop.getSubtotalCurrentPayAmount()); // 应付合计

        BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)

        cartShop.setOffersShipping(BigDecimal.ZERO);// 计算运费
        cartShop.setOriginShoppingFee(BigDecimal.ZERO);// 源运费

        BigDecimal originShippingFee = BigDecimal.ZERO; // 应付运费
        BigDecimal currentShippingFee = BigDecimal.ZERO;// 实付运费

        if (null != calcFreightCommand){
            originShippingFee = getFreightFee(calcFreightCommand, lines, shopId);
            if (originShippingFee.compareTo(offersShippingDisAmt) >= 0){
                cartShop.setOriginShoppingFee(originShippingFee); // 应付运费
                cartShop.setOffersShipping(offersShippingDisAmt); // 运费优惠
            }else{
                cartShop.setOriginShoppingFee(originShippingFee); // 应付运费
                cartShop.setOffersShipping(originShippingFee); // 运费优惠
            }
            shopCart.setOriginShoppingFee(cartShop.getOriginShoppingFee()); // 应付运费
            currentShippingFee = cartShop.getOriginShoppingFee().subtract(cartShop.getOffersShipping()); // 实付运费
            shopCart.setCurrentShippingFee(currentShippingFee);
        }

        // 当应付合计金额 小于订单优惠时
        if (cartShop.getSumCurrentPayAmount().compareTo(disAmtOnOrder) < 0){
            cartShop.setOffersTotal(cartShop.getSubtotalCurrentPayAmount());
            cartShop.setDisAmtOnOrder(cartShop.getSubtotalCurrentPayAmount());
            cartShop.setOffersShipping(BigDecimal.ZERO);
            cartShop.setDisAmtSingleOrder(BigDecimal.ZERO);
        }else{
            // 当应付合计金额 小于优惠合计时
            if (cartShop.getSumCurrentPayAmount().compareTo(shopCartAllDisAmt) < 0){
                cartShop.setOffersTotal(cartShop.getSumCurrentPayAmount());
                BigDecimal differAmt = cartShop.getSumCurrentPayAmount().subtract(disAmtOnOrder);// 整单优惠=应付金额-订单优惠
                cartShop.setDisAmtSingleOrder(differAmt); // 整单优惠
            }else{
                cartShop.setDisAmtSingleOrder(baseOnOrderDisAmt);
                cartShop.setOffersTotal(shopCartAllDisAmt);
            }
            cartShop.setDisAmtOnOrder(disAmtOnOrder);// 订单优惠
        }

        // 实付合计(应付金额+实付运费-商品优惠总额)
        BigDecimal realPayAmt = cartShop.getSumCurrentPayAmount().add(currentShippingFee).subtract(cartShop.getOffersTotal());
        cartShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        cartShop.setSumCurrentPayAmount(cartShop.getSubtotalCurrentPayAmount().add(originShippingFee));

        lines.addAll(giftList); // 将礼品放入购物车当中
        shopCart.setShoppingCartLineCommands(lines);

        shopCart.setOriginPayAmount(cartShop.getSumCurrentPayAmount());// 应付金额
        shopCart.setCurrentPayAmount(cartShop.getRealPayAmount()); // 实付金额

        // 封装数据
        ShoppingCartCommand cartInMap = new ShoppingCartCommand();
        Map<Long, ShoppingCartCommand> map = new HashMap<Long, ShoppingCartCommand>();
        map.put(shopId, shopCart);
        cartInMap.setShoppingCartLineCommands(shopCart.getShoppingCartLineCommands());
        cartInMap.setShoppingCartByShopIdMap(map);

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLine(cartInMap, promotionBriefList);
        shopCart.setShoppingCartLineCommands(cartInMap.getShoppingCartLineCommands());

        return cartShop;
    }

    /**
     * 构造用于购物车页面促销信息显示的数据
     * 
     * @param promotionBriefList
     * @param shopCart
     * @param calcFreightCommand
     * @return
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
                if (null == details || details.size() == 0)
                    continue;
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

        // lines = sdkShoppingCartGroupManager.groupShoppingCartLinesToDisplayByLinePromotionResult(shopCart,
        // promotionBriefList);

        Long shopId = lines.get(0).getShopId();// 店铺id

        ShopCartCommandByShop cartShop = new ShopCartCommandByShop();

        cartShop.setQty(getOrderQuantity(lines));// 商品数量

        cartShop.setSubtotalCurrentPayAmount(getOriginPayAmount(lines)); // 应付小计
        cartShop.setSumCurrentPayAmount(cartShop.getSubtotalCurrentPayAmount()); // 应付合计

        BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)

        cartShop.setOffersShipping(BigDecimal.ZERO);// 计算运费
        cartShop.setOriginShoppingFee(BigDecimal.ZERO);// 源运费

        BigDecimal originShippingFee = BigDecimal.ZERO; // 应付运费
        BigDecimal currentShippingFee = BigDecimal.ZERO;// 实付运费

        if (null != calcFreightCommand){
            originShippingFee = getFreightFee(calcFreightCommand, lines, shopId);
            if (originShippingFee.compareTo(offersShippingDisAmt) >= 0){
                cartShop.setOriginShoppingFee(originShippingFee); // 应付运费
                cartShop.setOffersShipping(offersShippingDisAmt); // 运费优惠
            }else{
                cartShop.setOriginShoppingFee(originShippingFee); // 应付运费
                cartShop.setOffersShipping(originShippingFee); // 运费优惠
            }
            shopCart.setOriginShoppingFee(cartShop.getOriginShoppingFee()); // 应付运费
            currentShippingFee = cartShop.getOriginShoppingFee().subtract(cartShop.getOffersShipping()); // 实付运费
            shopCart.setCurrentShippingFee(currentShippingFee);
        }

        // 当应付合计金额 小于订单优惠时
        if (cartShop.getSumCurrentPayAmount().compareTo(disAmtOnOrder) < 0){
            cartShop.setOffersTotal(cartShop.getSubtotalCurrentPayAmount());
            cartShop.setDisAmtOnOrder(cartShop.getSubtotalCurrentPayAmount());
            cartShop.setOffersShipping(BigDecimal.ZERO);
            cartShop.setDisAmtSingleOrder(BigDecimal.ZERO);
        }else{
            // 当应付合计金额 小于优惠合计时
            if (cartShop.getSumCurrentPayAmount().compareTo(shopCartAllDisAmt) < 0){
                cartShop.setOffersTotal(cartShop.getSumCurrentPayAmount());
                BigDecimal differAmt = cartShop.getSumCurrentPayAmount().subtract(disAmtOnOrder);// 整单优惠=应付金额-订单优惠
                cartShop.setDisAmtSingleOrder(differAmt); // 整单优惠
            }else{
                cartShop.setDisAmtSingleOrder(baseOnOrderDisAmt);
                cartShop.setOffersTotal(shopCartAllDisAmt);
            }
            cartShop.setDisAmtOnOrder(disAmtOnOrder);// 订单优惠
        }

        // 实付合计(应付金额+实付运费-商品优惠总额)
        BigDecimal realPayAmt = cartShop.getSumCurrentPayAmount().add(currentShippingFee).subtract(cartShop.getOffersTotal());
        cartShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        cartShop.setSumCurrentPayAmount(cartShop.getSubtotalCurrentPayAmount().add(originShippingFee));

        lines.addAll(giftList); // 将礼品放入购物车当中
        shopCart.setShoppingCartLineCommands(lines);

        shopCart.setOriginPayAmount(cartShop.getSumCurrentPayAmount());// 应付金额
        shopCart.setCurrentPayAmount(cartShop.getRealPayAmount()); // 实付金额

        // 封装数据
        ShoppingCartCommand cartInMap = new ShoppingCartCommand();
        Map<Long, ShoppingCartCommand> map = new HashMap<Long, ShoppingCartCommand>();
        map.put(shopId, shopCart);
        cartInMap.setShoppingCartLineCommands(shopCart.getShoppingCartLineCommands());
        cartInMap.setShoppingCartByShopIdMap(map);

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLine(cartInMap, promotionBriefList);
        shopCart.setShoppingCartLineCommands(cartInMap.getShoppingCartLineCommands());

        return cartShop;
    }

    /**
     * 封装礼品购物车行
     * 
     * @return
     */
    private ShoppingCartLineCommand getGiftShoppingCartLineCommand(PromotionSKUDiscAMTBySetting proSku){
        ShoppingCartLineCommand giftLine = new ShoppingCartLineCommand();
        giftLine.setItemId(proSku.getItemId());
        giftLine.setItemName(proSku.getItemName());
        giftLine.setQuantity(proSku.getQty());
        giftLine.setGift(proSku.getGiftMark());
        giftLine.setShopId(proSku.getShopId());
        giftLine.setType(Constants.ITEM_TYPE_PREMIUMS);
        giftLine.setSkuId(proSku.getSkuId());
        if (proSku.getSettingId() != null)
            giftLine.setLineGroup(proSku.getSettingId());
        giftLine.setStock(proSku.getQty());
        // 赠品都设置为有效
        giftLine.setValid(true);
        sdkEngineManager.packShoppingCartLine(giftLine);

        giftLine.setPromotionId(proSku.getPromotionId());
        giftLine.setSettingId(proSku.getSettingId());
        giftLine.setGiftChoiceType(proSku.getGiftChoiceType());
        giftLine.setGiftCountLimited(proSku.getGiftCountLimited());

        return giftLine;
    }

    /**
     * 根据店铺封装shopCart对象
     * 
     * @param shopCart
     */
    private Map<Long, ShoppingCartCommand> getShopCartByShopId(ShoppingCartCommand shopCart){
        // 获取区分了店铺的购物车对象
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = getShoppingCartMapByShop(shopCart.getShoppingCartLineCommands());
        if (null != shopCartByShopIdMap && shopCartByShopIdMap.size() > 0){
            for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartByShopIdMap.entrySet()){
                ShoppingCartCommand entryShopCart = entry.getValue();
                entryShopCart.setCurrentTime(shopCart.getCurrentTime());
                List<ShoppingCartLineCommand> lines = entryShopCart.getShoppingCartLineCommands();
                // 封装购物车信息
                packShopBaseInfo(entryShopCart, shopCart.getUserDetails(), shopCart.getCoupons(), lines);
            }
        }else{
            shopCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();

        }
        return shopCartByShopIdMap;
    }

    /**
     * 计算应付金额
     * 
     * @param shoppingCartLines
     * @return
     */
    private BigDecimal getOriginPayAmount(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal originPayAmount = new BigDecimal(0);
        if (null != shoppingCartLines && shoppingCartLines.size() > 0){
            for (ShoppingCartLineCommand cartLine : shoppingCartLines){
                if (cartLine.isGift() || cartLine.isCaptionLine())
                    continue;
                originPayAmount = originPayAmount.add(new BigDecimal(cartLine.getQuantity()).multiply(cartLine.getSalePrice()));
            }
        }
        return originPayAmount = originPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算整单商品数量
     * 
     * @param shoppingCartLines
     * @return
     */
    private int getOrderQuantity(List<ShoppingCartLineCommand> shoppingCartLines){
        int qtyAll = 0;
        if (null != shoppingCartLines && shoppingCartLines.size() > 0){
            for (ShoppingCartLineCommand lineCommand : shoppingCartLines){
                qtyAll += lineCommand.getQuantity();
            }
        }
        return qtyAll;
    }

    /**
     * 计算促销金额
     * 
     * @param promotionBriefList
     * @return
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
     * 计算应付运费
     * 
     * @param promotionBriefList
     * @return
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
     * 计算实付运费
     * 
     * @param promotionBriefList
     * @return
     */
    private BigDecimal getCurrentShippingAmountByShopCart(List<ShopCartCommandByShop> summaryShopCart,BigDecimal orginShippingAmount){
        // 运费优惠
        BigDecimal offersShippingAmount = BigDecimal.ZERO;
        if (null == summaryShopCart || summaryShopCart.size() == 0)
            return offersShippingAmount;
        for (ShopCartCommandByShop scc : summaryShopCart){
            offersShippingAmount = offersShippingAmount.add(scc.getOffersShipping());
        }
        // 计算实付运费
        if (orginShippingAmount.compareTo(offersShippingAmount) >= 0)
            return orginShippingAmount.subtract(offersShippingAmount);
        else
            return BigDecimal.ZERO;
    }

    /**
     * 引擎检查(限购、有效、库存)
     * 
     * @param cart
     * @param shoppingCartLine
     * @param memboIds
     * @return
     */
    private Integer doEngineChck(ShoppingCartCommand cart,ShoppingCartLineCommand shoppingCartLine,Set<String> memboIds,boolean flag){
        // 购物车的所属店铺
        List<Long> shopIds = getShopIds(cart.getShoppingCartLineCommands());
        // 获取购物车行的itemComboIds
        Set<String> itemComboIds = getItemComboIds(cart.getShoppingCartLineCommands());

        cart = getShoppingCart(
                        cart.getShoppingCartLineCommands(),
                        null,
                        cart.getUserDetails().getMemberId(),
                        null,
                        cart.getUserDetails().getMemComboList());

        // 获取限购规则
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(shopIds, memboIds, itemComboIds, new Date());
        if (null == purchaseLimitationList || purchaseLimitationList.size() == 0)
            purchaseLimitationList = new ArrayList<LimitCommand>();

        // 商品的有效性检查
        sdkEngineManager.doEngineCheck(shoppingCartLine, flag, cart, purchaseLimitationList);

        // 限购检查
        List<ShoppingCartLineCommand> errorLineList = sdkEngineManager.doEngineCheckLimit(cart, purchaseLimitationList);
        if (null != errorLineList && errorLineList.size() > 0){
            StringBuffer errorPrompt = new StringBuffer();
            List<Long> skuIdList = new ArrayList<Long>();
            for (ShoppingCartLineCommand cartLine : errorLineList){
                if ("".equals(errorPrompt.toString())){
                    errorPrompt.append(cartLine.getItemName());
                    for (SkuProperty skuProperty : cartLine.getSkuPropertys()){
                        errorPrompt.append(" ").append(skuProperty.getpName()).append(":").append(skuProperty.getValue());
                    }
                    skuIdList.add(cartLine.getSkuId());
                }else{
                    if (!skuIdList.contains(cartLine.getSkuId())){
                        errorPrompt.append("<br />").append(cartLine.getItemName());
                        for (SkuProperty skuProperty : cartLine.getSkuPropertys()){
                            errorPrompt.append(" ").append(skuProperty.getpName()).append(":").append(skuProperty.getValue());
                        }
                        skuIdList.add(cartLine.getSkuId());
                    }
                }
            }
            throw new BusinessException(Constants.THE_ORDER_CONTAINS_LIMIT_ITEM, new Object[] { errorPrompt.toString() });
        }
        return Constants.SUCCESS;
    }

    /**
     * 限购检查
     * 
     * @param cart
     * @param shoppingCartLine
     * @param memboIds
     * @return
     */
    public Integer doPromotionCheck(ShoppingCartCommand cart,Set<String> memboIds){
        // 购物车的所属店铺
        List<Long> shopIds = getShopIds(cart.getShoppingCartLineCommands());
        // 获取购物车行的itemComboIds
        Set<String> itemComboIds = getItemComboIds(cart.getShoppingCartLineCommands());

        cart = getShoppingCart(
                        cart.getShoppingCartLineCommands(),
                        null,
                        cart.getUserDetails().getMemberId(),
                        null,
                        cart.getUserDetails().getMemComboList());

        // 获取限购规则
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(shopIds, memboIds, itemComboIds, new Date());
        if (null == purchaseLimitationList || purchaseLimitationList.size() == 0)
            purchaseLimitationList = new ArrayList<LimitCommand>();

        // 限购检查
        List<ShoppingCartLineCommand> errorLineList = sdkEngineManager.doEngineCheckLimit(cart, purchaseLimitationList);
        if (null != errorLineList && errorLineList.size() > 0){
            StringBuffer errorPrompt = new StringBuffer();
            List<Long> skuIdList = new ArrayList<Long>();
            for (ShoppingCartLineCommand cartLine : errorLineList){
                if ("".equals(errorPrompt.toString())){
                    errorPrompt.append(cartLine.getItemName());
                    for (SkuProperty skuProperty : cartLine.getSkuPropertys()){
                        errorPrompt.append(" ").append(skuProperty.getpName()).append(":").append(skuProperty.getValue());
                    }
                    skuIdList.add(cartLine.getSkuId());
                }else{
                    if (!skuIdList.contains(cartLine.getSkuId())){
                        errorPrompt.append("<br />").append(cartLine.getItemName());
                        for (SkuProperty skuProperty : cartLine.getSkuPropertys()){
                            errorPrompt.append(" ").append(skuProperty.getpName()).append(":").append(skuProperty.getValue());
                        }
                        skuIdList.add(cartLine.getSkuId());
                    }
                }
            }
            throw new BusinessException(Constants.THE_ORDER_CONTAINS_LIMIT_ITEM, new Object[] { errorPrompt.toString() });
        }
        return Constants.SUCCESS;
    }

    /**
     * 移除购物车行
     * 
     * @param memberId
     * @param guestIndentify
     * @param extentionCode
     * @param request
     * @param response
     * @return 移除购物车行是否成功 success表示成功 failure表示失败
     */
    @Override
    public Integer removeShoppingCartLine(Long memberId,String extentionCode){
        Integer retval = 0;
        if (null != memberId){
            try{
                // 根据memberId和extentionCode删除购物车行
                retval = sdkShoppingCartLineDao.deleteByExtentionCodeAndMemberId(memberId, extentionCode);
            }catch (Exception e){
                log.error("removeShoppingCartLine error: " + e);
                return FAILURE;
            }
        }
        return retval;
    }

    /**
     * 根据购物车id移除购物车行
     * 
     * @param memberId
     * @param shoppingCartLineId
     * @return 移除购物车行是否成功 success表示成功 failure表示失败
     */
    @Override
    public Integer removeShoppingCartLineById(Long memberId,Long shoppingCartLineId){
        Integer retval = 0;
        if (null != memberId){
            try{
                // 根据memberId和购物车行删除购物车行
                retval = sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, shoppingCartLineId);
            }catch (Exception e){
                log.error("removeShoppingCartLineById error: " + e);
                return FAILURE;
            }
        }
        return retval;
    }

    /**
     * 清空购物车
     * 
     * @param memberId
     * @param guestIndentify
     * @param request
     * @param response
     * @return 清空购物车是否成功 success表示成功 failure表示失败
     */
    @Override
    public Integer emptyShoppingCart(Long memberId){

        if (null != memberId){
            // 会员
            try{
                // 清空购物车表中该会员的购物车数据
                Integer retval = sdkShoppingCartLineDao.deleteByMemberId(memberId);
                if (retval < 1){
                    return FAILURE;
                }
                // 设置cookie中的头部购物车商品数量
                // CookieUtil.setCookie(request, response,
                // Constants.GUEST_COOKIE_GC_CNT,null);
            }catch (Exception e){
                log.error("emptyShoppingCart member error");
                return FAILURE;
            }
        }
        // else {
        // //游客
        // try {
        // //重新设置cookie中的购物车值
        // CookieUtil.deleteCookie(request, response, new
        // Cookie(Constants.GUEST_COOKIE_GC,null));
        // CookieUtil.setCookie(request, response,
        // Constants.GUEST_COOKIE_GC_CNT,null);
        // } catch (Exception e) {
        // log.error("emptyShoppingCart cookie error");
        // return FAILURE;
        // }
        // }
        return SUCCESS;
    }

    /**
     * 保存或更新购物行信息
     * 
     * @param shoppingCartLine
     * @return
     */
    private void saveCartLine(ShoppingCartLineCommand shoppingCartLine){

        if (null == shoppingCartLine){
            return;
        }
        // 将command对象转换为entity
        if (shoppingCartLine.getId() != null && shoppingCartLine.getId() > 0){
            // 更新
            Integer updateCount = sdkShoppingCartLineDao.updateCartLineQuantity(
                            shoppingCartLine.getMemberId(),
                            shoppingCartLine.getExtentionCode(),
                            shoppingCartLine.getQuantity());
            if (updateCount < 1){
                // 修改的行数和期望的行数不一致，抛出运行时异常，事务回滚
                Object[] args = { 0, 1 };
                throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, args);
            }
        }else{
            // 保存
            sdkShoppingCartLineDao.insertShoppingCartLine(
                            shoppingCartLine.getExtentionCode(),
                            shoppingCartLine.getSkuId(),
                            shoppingCartLine.getQuantity(),
                            shoppingCartLine.getMemberId(),
                            shoppingCartLine.getCreateTime(),
                            shoppingCartLine.getSettlementState(),
                            shoppingCartLine.getShopId(),
                            shoppingCartLine.isGift(),
                            shoppingCartLine.getPromotionId(),
                            shoppingCartLine.getLineGroup());
        }

    }

    /**
     * 新增或者修改购物车行
     * 
     * @param userId
     * @param shoppingCartLine
     * @param saveFlage
     * @return
     */
    private Integer merageShoppingCartLine(Long userId,ShoppingCartLineCommand shoppingCartLine,boolean saveFlage){

        String extentionCode = shoppingCartLine.getExtentionCode();
        if (null == extentionCode){
            return FAILURE;
        }

        // 先查询是否已经有购物车行的信息
        List<ShoppingCartLineCommand> lines = findShoppingCartLinesByMemberId(userId, null);
        boolean existFlag = false;
        boolean updateResultFlag = false;

        if (null != lines && lines.size() > 0){// 如果有
            // 如果有，就修改数量
            // 如果没有，就添加一条记录

            // 判断加入的该商品是否已经在购物车中，
            for (ShoppingCartLineCommand line : lines){
                if (!line.isGift() && extentionCode.equals(line.getExtentionCode())){// 如果在的话，就修改数量
                    existFlag = true;
                    Integer curQuantity = 0;
                    Integer effectedRows = 0;
                    if (saveFlage){// 如果是新增 ， 就合并
                        curQuantity = line.getQuantity() + shoppingCartLine.getQuantity();
                        effectedRows = sdkShoppingCartLineDao.addCartLineQuantity(userId, line.getExtentionCode(), curQuantity);
                    }else{// 如果是更新，就直接替换值
                        curQuantity = shoppingCartLine.getQuantity();
                        effectedRows = sdkShoppingCartLineDao.updateCartLineQuantity(userId, line.getExtentionCode(), curQuantity);
                    }
                    // Integer effectedRows =
                    // sdkShoppingCartLineDao.updateCartLineQuantity(userId,
                    // line.getExtentionCode(), curQuantity);

                    if (1 == effectedRows){
                        updateResultFlag = true;
                        line.setQuantity(curQuantity);
                    }else{
                        updateResultFlag = false;
                    }

                    break;
                }
            }

            if (existFlag){
                if (!updateResultFlag){
                    return FAILURE;
                }else{
                    return SUCCESS;
                }
            }else{// 不存在
                      // 添加
                saveCartLine(shoppingCartLine);
            }

        }else{// 如果表中没有购物车，那么 创建购物车,同时计算价格

            // 保存 shoppingCartLine
            saveCartLine(shoppingCartLine);
        }
        return SUCCESS;
    }

    /**
     * 新增或者修改购物车行
     * 
     * @param userId
     * @param shoppingCartLine
     * @param saveFlage
     * @return
     */
    private Integer merageShoppingCartLineById(Long userId,ShoppingCartLineCommand shoppingCartLine,boolean saveFlage){

        String extentionCode = shoppingCartLine.getExtentionCode();
        if (null == extentionCode){
            return FAILURE;
        }

        // 先查询是否已经有购物车行的信息
        List<ShoppingCartLineCommand> lines = findShoppingCartLinesByMemberId(userId, null);
        boolean existFlag = false;
        boolean updateResultFlag = false;

        if (null != lines && lines.size() > 0){// 如果有
            // 如果有，就修改数量
            // 如果没有，就添加一条记录

            // 判断加入的该商品是否已经在购物车中，
            for (ShoppingCartLineCommand line : lines){
                if (!line.isGift() && extentionCode.equals(line.getExtentionCode())){// 如果在的话，就修改数量
                    existFlag = true;
                    Integer curQuantity = 0;
                    Integer effectedRows = 0;
                    if (saveFlage){// 如果是新增 ， 就合并
                        curQuantity = line.getQuantity() + shoppingCartLine.getQuantity();
                        effectedRows = sdkShoppingCartLineDao.addCartLineQuantity(userId, line.getExtentionCode(), curQuantity);
                    }else{// 如果是更新，就直接替换值
                        curQuantity = shoppingCartLine.getQuantity();
                        effectedRows = sdkShoppingCartLineDao.updateCartLineQuantityByLineId(userId, line.getId(), curQuantity);
                    }
                    // Integer effectedRows =
                    // sdkShoppingCartLineDao.updateCartLineQuantity(userId,
                    // line.getExtentionCode(), curQuantity);

                    if (1 == effectedRows){
                        updateResultFlag = true;
                        line.setQuantity(curQuantity);
                    }else{
                        updateResultFlag = false;
                    }

                    break;
                }
            }

            if (existFlag){
                if (!updateResultFlag){
                    return FAILURE;
                }else{
                    return SUCCESS;
                }
            }else{// 不存在
                      // 添加
                saveCartLine(shoppingCartLine);
            }

        }else{// 如果表中没有购物车，那么 创建购物车,同时计算价格

            // 保存 shoppingCartLine
            saveCartLine(shoppingCartLine);
        }
        return SUCCESS;
    }

    /**
     * 新增或者修改购物车行
     * 
     * @param userId
     * @param shoppingCartLine
     * @param saveFlage
     * @return
     */
    public boolean merageShoppingCartLineById(Long userId,ShoppingCartLineCommand shoppingCartLine){

        String extentionCode = shoppingCartLine.getExtentionCode();
        if (null == extentionCode){
            return false;
        }

        // 先查询是否已经有购物车行的信息
        List<ShoppingCartLineCommand> lines = findShoppingCartLinesByMemberId(userId, null);
        boolean existFlag = false;
        boolean updateResultFlag = false;

        // 如果有，就修改数量
        // 如果没有，就添加一条记录
        if (null != lines && lines.size() > 0){// 如果有

            // 判断加入的该商品是否已经在购物车中，
            for (ShoppingCartLineCommand line : lines){
                if (!line.isGift() && extentionCode.equals(line.getExtentionCode())){// 如果在的话，就修改数量
                    existFlag = true;
                    //如果sku在购物车行中已经存在  添加和修改都 全量修改数量
                    Integer effectedRows = sdkShoppingCartLineDao
                                    .updateCartLineQuantityByLineId(userId, line.getId(), shoppingCartLine.getQuantity());

                    if (1 == effectedRows){
                        updateResultFlag = true;
                    }else{
                        updateResultFlag = false;
                    }
                    break;
                }
            }

            if (existFlag){
                return updateResultFlag;
            }else{// 不存在
                      // 添加
                saveCartLine(shoppingCartLine);
            }

        }else{// 如果表中没有购物车，那么 创建购物车,同时计算价格

            // 保存 shoppingCartLine
            saveCartLine(shoppingCartLine);
        }
        return true;
    }

    /**
     * 修改购物车的选中状态(之前的功能，暂时不用)
     * 
     * @param userId
     * @param guestIndentify
     * @param extentionCodes
     * @param settleState
     * @param request
     * @param response
     */
    @Override
    public Integer updateCartLineSettlementState(Long userId,List<String> extentionCodes,Integer settleState){
        Integer updateCount = 0;
        if (null != userId){
            // 会员
            updateCount = sdkShoppingCartLineDao.updateCartLineSettleState(userId, extentionCodes, settleState);
            if (updateCount != extentionCodes.size()){
                Object[] args = { 0, 1 };
                throw new BusinessException(Constants.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, args);
            }
        }
        return SUCCESS;
    }

    /**
     * 立即购买
     * 
     * @param memberId
     * @param memCombos
     * @param shoppingCartLine
     *            操作的line
     * @param lines
     * @return
     */
    @Override
    public Integer immediatelyBuy(
                    Long memberId,
                    Set<String> memCombos,
                    ShoppingCartLineCommand shoppingCartLine,
                    List<ShoppingCartLineCommand> lines){

        try{
            Integer retval = SUCCESS;
            ShoppingCartCommand cart = new ShoppingCartCommand();
            sdkEngineManager.packShoppingCartLine(shoppingCartLine);
            // 检查商品有效性
            retval = sdkEffectiveManager.checkItemIsValid(shoppingCartLine.isValid());
            if (SUCCESS != retval){
                return Constants.CHECK_VALID_FAILURE;
            }
            Integer qty = 0;
            boolean flag = false;// 如果操作的行不在购物列表中为false,否则为true
            for (ShoppingCartLineCommand line : lines){
                if (line.getExtentionCode().equals(shoppingCartLine.getExtentionCode())){
                    qty = line.getQuantity() + shoppingCartLine.getQuantity();
                    line.setQuantity(qty);
                    flag = true;
                }
            }

            // 如果操作的行不在购物车列表中
            if (!flag){
                lines.add(shoppingCartLine);
            }

            cart.setShoppingCartLineCommands(lines);
            // 设置memComboIds
            Set<String> memboIds = null;
            UserDetails userDetails = new UserDetails();
            if (null != memberId){
                memboIds = memCombos;
            }else{
                memboIds = getMemboIds();
            }
            userDetails.setMemComboList(memboIds);
            userDetails.setMemberId(memberId);
            cart.setUserDetails(userDetails);
            // 引擎检查(限购、库存)
            retval = doEngineChck(cart, shoppingCartLine, memboIds, false);
            if (SUCCESS != retval){
                return retval;
            }
        }catch (Exception e){
            e.printStackTrace();
            return FAILURE;
        }
        return SUCCESS;
    }

    /**
     * 获取购物车中的所有店铺id
     * 
     * @param lines
     * @return
     */
    public List<Long> getShopIds(List<ShoppingCartLineCommand> lines){
        List<Long> shopIds = new ArrayList<Long>();
        if (null == lines || lines.size() == 0)
            return shopIds;
        Set<Long> ids = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            ids.add(line.getShopId());
        }
        shopIds.addAll(ids);
        return shopIds;
    }

    /**
     * 根据购物车行获取ItemForCheckCommand集合
     * 
     * @param lines
     * @return
     */
    private Set<String> getItemComboIds(List<ShoppingCartLineCommand> lines){
        Set<String> set = new HashSet<String>();
        if (null != lines && lines.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                set.addAll(line.getComboIds());
            }
        }
        return set;
    }

    /**
     * 游客的memboIds
     * 
     * @return
     */
    private Set<String> getMemboIds(){
        return sdkEngineManager.getCrowdScopeListByMemberAndGroup(null, null);
    }

    /**
     * 用于计算获取促销数据
     * 
     * @param shoppingCartLines
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionBrief> calcuPromoBriefs(ShoppingCartCommand shopCart){
        List<PromotionBrief> briefs = new ArrayList<PromotionBrief>();
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Set<String> memboSet = shopCart.getUserDetails().getMemComboList();
        // 获取人群和商品促销的交集
        List<PromotionCommand> promotionList = sdkPromotionRuleFilterManager
                        .getIntersectActivityRuleData(getShopIds(lines), memboSet, getItemComboIds(lines), shopCart.getCurrentTime());

        if (null != promotionList && promotionList.size() > 0){
            // 通过购物车和促销集合计算商品促销
            briefs = sdkPromotionCalculationManager.calculationPromotion(shopCart, promotionList);
        }
        return briefs;
    }

    /**
     * 获取每款商品总金额
     * 
     * @param itemId
     *            商品id
     * @param shoppingLines
     * @return
     */
    @Override
    public BigDecimal getProductAmount(Long itemId,List<ShoppingCartLineCommand> shoppingLines){
        BigDecimal productAmount = new BigDecimal(0.0);
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (shoppingLine.isGift())
                    continue;
                if (itemId.equals(shoppingLine.getItemId())){
                    // 计算该itemId下的sku的金额总和
                    productAmount = productAmount.add(new BigDecimal(shoppingLine.getQuantity()).multiply(shoppingLine.getSalePrice()));
                }
            }
        }
        productAmount = productAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return productAmount;
    }

    /**
     * 获取购物车应付金额，除去礼品以外
     */
    @Override
    public BigDecimal getAllAmount(List<ShoppingCartLineCommand> shoppingLines){
        BigDecimal allAmount = BigDecimal.ZERO;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (shoppingLine.isGift())
                    continue;
                if (shoppingLine.isCaptionLine())
                    continue;
                allAmount = allAmount.add(new BigDecimal(shoppingLine.getQuantity()).multiply(shoppingLine.getSalePrice()));
            }
        }
        allAmount = allAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return allAmount;
    }

    /**
     * 获取每款商品购买数量
     * 
     * @param itemId
     * @param shoppingLines
     * @return
     */
    @Override
    public Integer getProductQuantity(Long itemId,List<ShoppingCartLineCommand> shoppingLines){
        int count = 0;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (shoppingLine.isGift()){
                    continue;
                }
                if (itemId.equals(shoppingLine.getItemId())){
                    // 计算该itemId下的sku的商品总数量
                    count += shoppingLine.getQuantity();
                }
            }
        }
        return count;
    }

    /**
     * 获取某个分类下商品的金额
     * 
     * @param categoryId
     * @param shoppingLines
     * @return
     */
    @Override
    public BigDecimal getCategoryAmount(Long categoryId,List<ShoppingCartLineCommand> shoppingLines){
        BigDecimal totalCategoryAmount = new BigDecimal(0.0);
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                // 计算该分类下的商品总金额
                totalCategoryAmount = totalCategoryAmount.add(calcCategoryAmount(categoryId, shoppingLine));
            }
        }
        totalCategoryAmount = totalCategoryAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return totalCategoryAmount;
    }

    /**
     * 计算分类金额
     * 
     * @param categoryId
     * @param categoryAmount
     * @param shoppingLine
     * @return
     */
    private BigDecimal calcCategoryAmount(Long categoryId,ShoppingCartLineCommand shoppingLine){
        BigDecimal categoryAmount = new BigDecimal(0.0);
        if (null == shoppingLine.getCategoryList() || shoppingLine.getCategoryList().size() == 0 || shoppingLine.isGift()){
            return BigDecimal.ZERO;
        }
        if (shoppingLine.getCategoryList().contains(categoryId)){
            // 计算该分类下的商品总金额
            categoryAmount = categoryAmount.add(new BigDecimal(shoppingLine.getQuantity()).multiply(shoppingLine.getSalePrice()));
        }
        return categoryAmount;
    }

    /**
     * 获取某个分类下商品的数量
     * 
     * @param categoryId
     * @param shoppingLines
     * @return
     */
    @Override
    public Integer getCategoryQuantity(Long categoryId,List<ShoppingCartLineCommand> shoppingLines){
        int count = 0;
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand shoppingLine : shoppingLines){
                if (null == shoppingLine.getCategoryList() || shoppingLine.getCategoryList().size() == 0 || shoppingLine.isGift()){
                    continue;
                }
                // 计算该分类下的商品总数量
                if (shoppingLine.getCategoryList().contains(categoryId)){
                    count += shoppingLine.getQuantity();
                }
            }
        }
        return count;
    }

    /**
     * @param comboId
     * @param shoppingLines
     *            获取组合金额
     */
    @Override
    public BigDecimal getComboAmount(Long comboId,List<ShoppingCartLineCommand> shoppingLines){
        BigDecimal scopeAMT = BigDecimal.ZERO;
        // 变量安全
        if (shoppingLines == null || shoppingLines.size() == 0)
            return scopeAMT;
        for (ShoppingCartLineCommand line : shoppingLines){
            // 变量安全
            if (line.getComboIds() == null || line.getComboIds().size() == 0 || line.isGift()){
                continue;
            }
            // 计算该combo下的商品总金额
            if (line.getComboIds().contains(String.valueOf(comboId))){
                scopeAMT = scopeAMT.add(new BigDecimal(line.getQuantity()).multiply(line.getSalePrice()));
            }
        }
        return scopeAMT;
    }

    /**
     * @param comboId
     * @param shoppingLines
     *            获取组合数量
     */
    @Override
    public Integer getComboQuantity(Long comboId,List<ShoppingCartLineCommand> shoppingLines){
        int count = 0;
        // 变量安全
        if (shoppingLines == null || shoppingLines.size() == 0)
            return count;
        for (ShoppingCartLineCommand line : shoppingLines){
            // 变量安全
            if (line.getComboIds() == null || line.getComboIds().size() == 0 || line.isGift()){
                continue;
            }
            // 计算该combo下的商品总数量
            if (line.getComboIds().contains(String.valueOf(comboId))){
                count += line.getQuantity();
            }
        }
        return count;
    }

    /**
     * 统计各个分类下SKU、QTY*SalesPrice
     * 
     * @param shopCart
     * @param categoryId
     * @return Map<Long, BigDecimal> long 代表skuId，BigDecimal代表sku总计金额
     */
    @Override
    public Map<Long, BigDecimal> getSKUSalesInfoByCategory(ShoppingCartCommand shopCart,Long categoryId){
        if (null == shopCart || null == categoryId)
            return null;
        Map<Long, BigDecimal> result = new HashMap<Long, BigDecimal>();
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        if (null != lines && lines.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                Long skuId = line.getSkuId();

                if (null == line.getCategoryList() || line.getCategoryList().size() == 0)
                    continue;
                // 是否在该category范围内
                if (!line.getCategoryList().contains(categoryId)){
                    continue;
                }
                BigDecimal categoryAmount = new BigDecimal(0.0);
                // 计算该category下每个sku的总金额
                if (null == result.get(skuId)){
                    categoryAmount = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                    result.put(skuId, categoryAmount);
                }else{
                    categoryAmount = result.get(skuId);
                    categoryAmount = categoryAmount.add(new BigDecimal(line.getQuantity()).multiply(line.getSalePrice()));
                }
                categoryAmount = categoryAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                result.put(skuId, categoryAmount);
            }
        }
        return result;
    }

    /**
     * 统计每个item的总金额
     * 
     * @param shopCart
     * @param itemId
     */
    @Override
    public BigDecimal getItemAmount(ShoppingCartCommand shopCart,Long itemId){
        if (null == shopCart || null == itemId)
            return BigDecimal.ZERO;
        BigDecimal itemAmount = new BigDecimal(0.0);
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        if (null != lines && lines.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                // 计算该item下的总金额
                if (String.valueOf(itemId).equals(String.valueOf(line.getItemId()))){
                    itemAmount = itemAmount.add(new BigDecimal(line.getQuantity()).multiply(line.getSalePrice()));
                }
            }
            itemAmount = itemAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            return itemAmount;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 统计每个item下的sku的金额
     * 
     * @param shopCart
     * @param itemId
     */
    @Override
    public Map<Long, BigDecimal> getSKUSalesInfoByItem(ShoppingCartCommand shopCart,Long itemId){
        if (null == shopCart || null == itemId)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        if (null != lines && lines.size() > 0){
            Map<Long, BigDecimal> result = new HashMap<Long, BigDecimal>();
            for (ShoppingCartLineCommand line : lines){
                Long skuId = line.getSkuId();
                BigDecimal itemAmount = new BigDecimal(0.0);
                // 如果该商品不在该item下
                if (!String.valueOf(itemId).equals(String.valueOf(line.getItemId()))){
                    continue;
                }
                // 计算每个item下的每个sku的总金额
                if (null == result.get(skuId)){
                    itemAmount = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                    result.put(skuId, itemAmount);
                }else{
                    itemAmount = result.get(skuId);
                    itemAmount = itemAmount.add(new BigDecimal(line.getQuantity()).multiply(line.getSalePrice()));
                }
                itemAmount = itemAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                result.put(skuId, itemAmount);
            }
            return result;
        }
        return null;
    }

    /**
     * 根据会员统计购物车中的商品数量
     * 
     * @param memberId
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getShopCartItemQty(Long memberId){
        Integer qty = sdkShoppingCartLineDao.findShopCartLineCountByMemberId(memberId, Constants.CHECKED_CHOOSE_STATE);
        if (null == qty)
            qty = 0;
        return qty;
    }

    /**
     * 按整单折扣率，计算累计折扣金额
     */
    @Override
    public BigDecimal getDiscountAMTOrderDiscountRateByRate(
                    ShoppingCartCommand shopCart,
                    BigDecimal discountRate,
                    BigDecimal previousDiscAMTAll){
        if (null == shopCart)
            return BigDecimal.ZERO;
        // 应付金额
        BigDecimal discountAmount = shopCart.getOriginPayAmount();
        if (null == discountAmount || discountAmount.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;
        discountAmount = discountAmount.subtract(previousDiscAMTAll);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // 计算应付金额的折扣
        discountAmount = discountAmount.multiply(HUNDRED_PERCENT.subtract(discountRate));
        discountAmount = discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return discountAmount;
    }

    /**
     * 计算礼品的累计金额,按ItemID。一个item下多个SKU，按不定顺序取足QTY返回优惠金额
     * 
     * @param lines
     * @param itemId
     * @param qty
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByItemID(
                    long shopId,
                    AtomicSetting setting,
                    long itemId,
                    Integer qty,
                    Integer displayCountLimited){
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 礼品不计价，只显示名称
        // long shopId,赠品要分店铺的
        // ToDo Simoncheng
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(itemId);

        settingList = getDiscountAMTGiftByItemList(itemIds, shopId, setting, qty, displayCountLimited);

        return settingList;
    }

    /**
     * 计算礼品的累计金额,按CategoryID。一个CategoryID下多个SKU，按不定顺序取足QTY返回优惠金额
     * 
     * @param lines
     * @param categoryId
     * @param qty
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByCategoryID(
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited){
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 礼品不计价，只显示名称
        // long shopId,赠品要分店铺的
        // ToDo Simoncheng
        long categoryId = setting.getScopeValue();
        List<Long> cidList = new ArrayList<Long>();
        cidList.add(categoryId);

        List<Long> itemIds = itemCategoryDao.findEffectItemIdListByCategoryIdList(cidList, shopId);

        settingList = getDiscountAMTGiftByItemList(itemIds, shopId, setting, qty, displayCountLimited);

        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByItemList(
                    List<Long> itemIdsLong,
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited){
        PromotionSKUDiscAMTBySetting proSkuSetting = null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();

        List<ItemCommand> itemList = itemInfoDao.findItemCommandListByItemIds((List<Long>) itemIdsLong);
        if (itemList == null)
            return null;
        for (ItemCommand item : itemList){
            proSkuSetting = new PromotionSKUDiscAMTBySetting();
            proSkuSetting.setItemId(item.getId());
            proSkuSetting.setShopId(shopId);

            proSkuSetting.setItemName(item.getTitle());

            proSkuSetting.setGiftMark(true);
            proSkuSetting.setGiftChoiceType(setting.getGiftChoiceType());
            proSkuSetting.setGiftCountLimited(setting.getGiftCountLimited());
            proSkuSetting.setSettingId(setting.getSettingId());
            proSkuSetting.setPromotionId(setting.getPromotionId());
            proSkuSetting.setSalesPrice(item.getSalePrice());
            proSkuSetting.setDiscountAmount(item.getSalePrice().multiply(new BigDecimal(qty)));
            proSkuSetting.setSkuId(item.getId());
            proSkuSetting.setQty(qty);

            List<SkuCommand> invSKUList = skuDao.findInventoryByItemId(item.getId());
            SkuCommand invMaxSKU = null;

            if (invSKUList != null && invSKUList.size() > 0){
                invMaxSKU = invSKUList.get(0);
                for (SkuCommand invt : invSKUList){
                    if (null != invt.getState() && !invt.getState().equalsIgnoreCase(String.valueOf(Sku.LIFECYCLE_DISABLE))){
                        invMaxSKU = invt;
                        break;
                    }
                }

                if (null == invMaxSKU.getAvailableQty())
                    invMaxSKU.setAvailableQty(0);
                for (SkuCommand invt : invSKUList){
                    if (null != invt.getState() && invt.getState().equalsIgnoreCase(String.valueOf(Sku.LIFECYCLE_DISABLE)))
                        continue;
                    if (null != invt.getAvailableQty() && invt.getAvailableQty() > invMaxSKU.getAvailableQty()){
                        invMaxSKU = invt;
                    }
                }
                proSkuSetting.setSalesPrice(invMaxSKU.getSalePrice());
                proSkuSetting.setDiscountAmount(invMaxSKU.getSalePrice().multiply(new BigDecimal(qty)));
                proSkuSetting.setSkuId(invMaxSKU.getId());
            }
            settingList.add(proSkuSetting);
        }

        return settingList;
    }

    /**
     * 计算礼品的累计金额,按ComboID。一个cmbId下多个SKU，按不定顺序取足QTY返回优惠金额
     * 
     * @param lines
     * @param itemId
     * @param qty
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByComboID(
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited){
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 礼品不计价，只显示名称
        // long shopId,赠品要分店铺的
        // ToDo Simoncheng
        long cmbId = setting.getScopeValue();
        String expression = getItemIdsFromComboId(cmbId, shopId);
        if (expression == null)
            return null;
        Set<Long> itemIds = sdkFilterManager.analyzeProductExpression(expression, shopId);
        if (itemIds == null || itemIds.size() == 0)
            return null;

        List<Long> itemIdsLong = new ArrayList<Long>();
        for (Long id : itemIds){
            itemIdsLong.add(id);
        }

        settingList = getDiscountAMTGiftByItemList(itemIdsLong, shopId, setting, qty, displayCountLimited);

        return settingList;
    }

    private String getItemIdsFromComboId(long comboId,long shopId){
        List<ItemTagRuleCommand> scopList = EngineManager.getInstance().getItemScopeList();
        for (ItemTagRuleCommand tag : scopList){
            if (tag.getId() == comboId && tag.getShopId() == shopId){
                return tag.getExpression();
            }
        }
        return null;
    }

    /**
     * 整单按Item，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param disAmount
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal itemDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);
        BigDecimal itemOriginal = getNeedToPayAmountInShoppingCartByItemId(lines, itemId);
        BigDecimal itemPreviousDisc = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);

        BigDecimal itemNeedPay = itemOriginal.subtract(itemPreviousDisc);
        if (itemNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 只要购物车中有该item下的商品就进行金额优惠
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                BigDecimal lineShareDisc = itemDiscAMT.multiply(lineNeedPay).divide(itemNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
                settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
            }
        }
        return settingList;
    }

    /**
     * 整单按Item，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param rate
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal rate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemNeedToPay = getProductAmount(itemId, lines);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(lines, briefListPrevious, itemId);

        BigDecimal itemOriginNeedToPay = itemNeedToPay;
        // BigDecimal totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(itemOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        itemNeedToPay = itemNeedToPay.subtract(previousDiscAMT);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal itemDiscAMT = itemNeedToPay.multiply(HUNDRED_PERCENT.subtract(rate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 只要购物车中有该item下的商品就进行折扣优惠
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                BigDecimal lineNeedToPay = BigDecimal.ZERO;
                if (onePieceMark){
                    lineNeedToPay = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(rate));
                }else{
                    lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                }
                BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                    continue;
                // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                BigDecimal lineDiscAMT = itemDiscAMT.multiply(lineNeedToPay).divide(itemOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);

                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                continue;
            }
        }
        return settingList;
    }

    /**
     * 按Item，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);
        BigDecimal itemOriginal = getNeedToPayAmountInShoppingCartByItemId(lines, itemId);
        BigDecimal itemPreviousDisc = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);

        BigDecimal itemNeedPay = itemOriginal.subtract(itemPreviousDisc);
        if (itemNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();

        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                BigDecimal lineShareDisc = itemDiscAMT.multiply(lineNeedPay).divide(itemNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
                settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemDiscAMT = BigDecimal.valueOf(factorDefault).multiply(discAmount);
        BigDecimal itemOriginal = getNeedToPayAmountInShoppingCartByItemId(lines, itemId);
        BigDecimal itemPreviousDisc = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);

        BigDecimal itemNeedPay = itemOriginal.subtract(itemPreviousDisc);
        if (itemNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();

        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                ItemFactor itemFactor = getItemInFactorList(itemFactorList, line.getItemId());
                if (itemFactor != null){
                    // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                    BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    BigDecimal lineOriginNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    BigDecimal lineNeedPay = lineOriginNeedPay.subtract(skuPreviousDiscAMT);
                    if (lineNeedPay.compareTo(BigDecimal.ZERO) <= 0)
                        continue;

                    BigDecimal lineShareDisc = BigDecimal.ZERO;
                    if (factorDefault > itemFactor.getFactor())
                        lineShareDisc = BigDecimal.valueOf(itemFactor.getFactor()).multiply(discAmount);
                    else
                        lineShareDisc = itemDiscAMT.multiply(lineOriginNeedPay).divide(itemOriginal, 2, BigDecimal.ROUND_HALF_EVEN);
                    if (lineShareDisc.compareTo(lineNeedPay) >= 0)
                        lineShareDisc = lineNeedPay;
                    settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
                }
            }
        }
        return settingList;
    }

    /**
     * 封装PromotionSKUDiscAMTBySetting对象信息
     * 
     * @param line
     * @param disAmt
     * @return
     */
    @Override
    public PromotionSKUDiscAMTBySetting getPromotionSkuAMTSetting(ShoppingCartLineCommand line,BigDecimal disAmt){
        PromotionSKUDiscAMTBySetting setting = new PromotionSKUDiscAMTBySetting();
        BigDecimal lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
        if (lineNeedToPay.compareTo(disAmt) > 0)
            setting.setDiscountAmount(disAmt);
        else
            setting.setDiscountAmount(lineNeedToPay);
        setting.setItemId(line.getItemId());
        setting.setItemName(line.getItemName());
        setting.setSkuId(line.getSkuId());
        setting.setCategoryList(line.getCategoryList());
        setting.setComboIds(line.getComboIds());
        setting.setQty(line.getQuantity());
        setting.setShopId(line.getShopId());
        setting.setSalesPrice(line.getSalePrice());
        return setting;
    }

    /**
     * 按Item，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemNeedToPay = getProductAmount(itemId, lines);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, itemId);

        BigDecimal itemOriginNeedToPay = itemNeedToPay;
        // BigDecimal totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(itemOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        itemNeedToPay = itemNeedToPay.subtract(previousDiscAMT);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal itemDiscAMT = itemNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        // 折扣优惠计算方式一样
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                BigDecimal lineNeedToPay = BigDecimal.ZERO;
                if (onePieceMark){
                    lineNeedToPay = line.getSalePrice().multiply(HUNDRED_PERCENT.subtract(discRate)).multiply(ONE_PIECE_QTY);
                }else{
                    lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                }
                BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                    continue;
                // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                BigDecimal lineDiscAMT = itemDiscAMT.multiply(lineNeedToPay).divide(itemOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);

                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemNeedToPay = getProductAmount(itemId, lines);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);

        BigDecimal itemOriginNeedToPay = itemNeedToPay;
        // BigDecimal totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(itemOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        itemNeedToPay = itemNeedToPay.subtract(previousDiscAMT);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal itemDiscAMT = itemNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        // 折扣优惠计算方式一样
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                ItemFactor itemFactor = getItemInFactorList(itemFactorList, line.getItemId());
                if (itemFactor != null){
                    BigDecimal lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                    BigDecimal lineOriginNeedToPay = lineNeedToPay;
                    BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    lineNeedToPay = lineOriginNeedToPay.subtract(previousDiscAMTSKU);
                    if (lineOriginNeedToPay.subtract(previousDiscAMTSKU).compareTo(BigDecimal.ZERO) <= 0)
                        continue;
                    BigDecimal lineDiscAMT = BigDecimal.ZERO;
                    if (onePieceMark){
                        factorDefault = factorDefault < itemFactor.getFactor() ? factorDefault : itemFactor.getFactor();
                        lineDiscAMT = BigDecimal.valueOf(factorDefault).multiply(line.getSalePrice())
                                        .multiply(HUNDRED_PERCENT.subtract(discRate));
                    }else{
                        lineDiscAMT = itemDiscAMT.multiply(lineNeedToPay).divide(itemOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                    }
                    if (lineNeedToPay.compareTo(lineDiscAMT) < 0)
                        lineDiscAMT = lineNeedToPay;

                    lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                    settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                }
            }
        }
        return settingList;
    }

    /**
     * 按件，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();

        for (ShoppingCartLineCommand line : lines){
            // 购物车中该item下的商品进行按件进行金额优惠
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);
                BigDecimal lineDiscAMT = BigDecimal.ZERO;
                if (lineNeedPay.compareTo(curLineDiscAmount.multiply(BigDecimal.valueOf(factor))) > 0)
                    lineDiscAMT = curLineDiscAmount.multiply(BigDecimal.valueOf(factor));
                else
                    lineDiscAMT = lineNeedPay;

                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 购物车中该item下的商品进行按件进行金额优惠
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
                ItemFactor itemFactor = getItemInFactorList(itemFactorList, line.getItemId());
                if (itemFactor != null){
                    factorDefault = factorDefault < itemFactor.getFactor() ? factorDefault : itemFactor.getFactor();

                    BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                        continue;
                    else
                        lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                    BigDecimal lineDiscAMT = BigDecimal.ZERO;

                    if (lineNeedPay.compareTo(curLineDiscAmount.multiply(BigDecimal.valueOf(factorDefault))) > 0)
                        lineDiscAMT = curLineDiscAmount.multiply(BigDecimal.valueOf(factorDefault));
                    else
                        lineDiscAMT = lineNeedPay;

                    settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                }
            }
        }
        return settingList;
    }

    /**
     * 按件，计算Item下的累计金额
     * 
     * @param lines
     * @param itemId
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTItemPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal itemNeedToPay = getProductAmount(itemId, lines);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);
        BigDecimal itemOriginNeedToPay = itemNeedToPay;
        // BigDecimal totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(itemOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        itemNeedToPay = itemNeedToPay.subtract(previousDiscAMT);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal itemDiscAMT = itemNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        // 折扣优惠计算方式一样
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                BigDecimal lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                BigDecimal lineOriginNeedToPay = lineNeedToPay;
                BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                // lineNeedToPay = lineOriginNeedToPay.subtract(previousDiscAMTSKU);
                if (lineOriginNeedToPay.subtract(previousDiscAMTSKU).compareTo(BigDecimal.ZERO) <= 0)
                    continue;
                BigDecimal lineDiscAMT = BigDecimal.ZERO;
                if (onePieceMark){
                    lineDiscAMT = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(discRate));
                }else{
                    lineDiscAMT = itemDiscAMT.multiply(lineNeedToPay).divide(itemOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                }
                if (lineNeedToPay.compareTo(lineDiscAMT) < 0)
                    lineDiscAMT = lineNeedToPay;

                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTItemPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal itemNeedToPay = getProductAmount(itemId, lines);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(lines, briefListPrevious, itemId);

        BigDecimal itemOriginNeedToPay = itemNeedToPay;
        // BigDecimal totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(itemOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        itemNeedToPay = itemNeedToPay.subtract(previousDiscAMT);
        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal itemDiscAMT = itemNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        // 折扣优惠计算方式一样
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在itemid下
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                ItemFactor itemFactor = null;
                itemFactor = getItemInFactorList(itemFactorList, line.getItemId());
                if (itemFactor != null){
                    BigDecimal lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                    BigDecimal lineOriginNeedToPay = lineNeedToPay;
                    BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    // lineNeedToPay = lineOriginNeedToPay.subtract(previousDiscAMTSKU);
                    if (lineOriginNeedToPay.subtract(previousDiscAMTSKU).compareTo(BigDecimal.ZERO) <= 0)
                        continue;
                    BigDecimal lineDiscAMT = BigDecimal.ZERO;
                    if (onePieceMark){
                        factorDefault = factorDefault < itemFactor.getFactor() ? factorDefault : itemFactor.getFactor();
                        lineDiscAMT = BigDecimal.valueOf(factorDefault).multiply(line.getSalePrice())
                                        .multiply(HUNDRED_PERCENT.subtract(discRate));
                    }else{
                        lineDiscAMT = itemDiscAMT.multiply(lineNeedToPay).divide(itemOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                    }
                    if (lineNeedToPay.compareTo(lineDiscAMT) < 0)
                        lineDiscAMT = lineNeedToPay;

                    lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                    settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                }

            }
        }
        return settingList;
    }

    /**
     * 整单按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal skuPreviousDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedPay = BigDecimal.ZERO;
        BigDecimal lineShareDisc = BigDecimal.ZERO;

        BigDecimal categoryDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);

        BigDecimal categoryOriginal = getNeedToPayAmountInShoppingCartByCategoryId(lines, categoryId);
        BigDecimal categoryPreviousDisc = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(lines, briefListPrevious, categoryId);

        BigDecimal categoryNeedPay = categoryOriginal.subtract(categoryPreviousDisc);
        if (categoryNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 只要购物车中有该category下的商品
            if (line.getCategoryList().contains(categoryId)){
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                lineShareDisc = categoryDiscAMT.multiply(lineNeedPay).divide(categoryNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
                settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
            }
        }
        return settingList;
    }

    /**
     * 整单按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param rate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal rate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal categoryNeedToPay = BigDecimal.ZERO;
        BigDecimal categoryDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal categoryOriginNeedToPay = BigDecimal.ZERO;
        // BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        categoryNeedToPay = getCategoryAmount(categoryId, lines);
        if (categoryNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(lines, briefListPrevious, categoryId);

        categoryOriginNeedToPay = categoryNeedToPay;
        // totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(categoryOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        categoryNeedToPay = categoryNeedToPay.subtract(previousDiscAMT);
        if (categoryNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        categoryDiscAMT = categoryNeedToPay.multiply(HUNDRED_PERCENT.subtract(rate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift())
                continue;
            // 只要购物车中有该category下的商品就进行折扣优惠
            if (line.getCategoryList().contains(categoryId)){
                if (onePieceMark){
                    lineNeedToPay = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(rate));
                }else{
                    lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                }
                previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                    continue;
                // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                lineDiscAMT = categoryDiscAMT.multiply(lineNeedToPay).divide(categoryOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);

                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                continue;
            }
        }
        return settingList;
    }

    /**
     * 单品按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();
        Set<Long> itemIds = new HashSet<Long>();
        itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        for (Long itemId : itemIds){
            settingOne = getDiscountAMTItemPerItemByAMT(lines, itemId, discAmount, factor, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 单品按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal categoryNeedToPay = BigDecimal.ZERO;
        BigDecimal categoryDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal categoryOriginNeedToPay = BigDecimal.ZERO;
        // BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        categoryNeedToPay = getCategoryAmount(categoryId, lines);
        if (categoryNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(lines, briefListPrevious, categoryId);

        categoryOriginNeedToPay = categoryNeedToPay;
        // totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(categoryOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        categoryNeedToPay = categoryNeedToPay.subtract(previousDiscAMT);
        if (categoryNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        categoryDiscAMT = categoryNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));
        Set<Long> itemIds = new HashSet<Long>();
        // 折扣优惠计算方式一样
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift())
                continue;
            // 判断line是否在categoryId下
            if (line.getCategoryList().contains(categoryId)){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());

                lineOriginNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
                previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                // lineNeedToPay = lineOriginNeedToPay.subtract(previousDiscAMTSKU);
                if (lineOriginNeedToPay.subtract(previousDiscAMTSKU).compareTo(BigDecimal.ZERO) <= 0)
                    continue;
                if (onePieceMark){
                    lineDiscAMT = line.getSalePrice().multiply(HUNDRED_PERCENT.subtract(discRate)).multiply(ONE_PIECE_QTY);
                }else{
                    lineDiscAMT = categoryDiscAMT.multiply(lineNeedToPay).divide(categoryOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                }

                if (lineDiscAMT.compareTo(lineNeedToPay) >= 0)
                    lineDiscAMT = lineNeedToPay;

                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 单件按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal skuPreviousDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 该category下的商品进行按件金额优惠
            if (line.getCategoryList().contains(categoryId)){
                BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                if (lineNeedPay.compareTo(curLineDiscAmount.multiply(BigDecimal.valueOf(factor))) > 0)
                    lineDiscAMT = curLineDiscAmount.multiply(BigDecimal.valueOf(factor));
                else
                    lineDiscAMT = lineNeedPay;

                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 单件按Category，计算Category下的累计金额
     * 
     * @param lines
     * @param categoryId
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCategoryPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();
        Set<Long> itemIds = new HashSet<Long>();
        itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        for (Long itemId : itemIds){
            settingOne = getDiscountAMTItemPerPCSByRate(lines, itemId, discRate, onePieceMark, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCategoryPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCategoryId(lines, categoryId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 全场优惠，计算全场下的累计金额
     * 
     * @param lines
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal callDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);
        BigDecimal callOriginal = getNeedToPayAmountInShoppingCartByAll(lines);
        BigDecimal callPreviousDisc = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
        BigDecimal callNeedPay = callOriginal.subtract(callPreviousDisc);

        if (callNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();

        for (ShoppingCartLineCommand line : lines){
            // 如果行上现有的优惠已经超过行实付时，跳到下一个行
            BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                            .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
            BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
            if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                continue;
            else
                lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

            BigDecimal lineShareDisc = callDiscAMT.multiply(lineNeedPay).divide(callNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
            settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
        }
        return settingList;
    }

    /**
     * 全场折扣，计算全场下的累计金额
     * 
     * @param lines
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal orderNeedToPay = getAllAmount(lines);
        if (orderNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal orderOriginNeedToPay = orderNeedToPay;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);

        // 整单折扣的基数，需要直接减去整单的优惠。分摊不在这，分摊在行上。
        orderNeedToPay = orderNeedToPay.subtract(previousDiscAMT);
        if (orderNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal orderDiscAMT = orderNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift())
                continue;

            BigDecimal lineNeedToPay = BigDecimal.ZERO;

            if (onePieceMark){
                lineNeedToPay = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(discRate));
            }else{
                lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
            }

            BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                            .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
            if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                continue;
            // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);
            BigDecimal lineDiscAMT = orderDiscAMT.multiply(lineNeedToPay).divide(orderOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);

            lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
            settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
        }
        return settingList;
    }

    /**
     * 全场按Item优惠，计算全场下的累计金额
     * 
     * @param lines
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getDiscountAMTItemPerItemByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factor,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 全场按Item优惠，计算全场下的累计金额
     * 
     * @param lines
     * @param discRate
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal orderNeedToPay = getAllAmount(lines);
        if (orderNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
        BigDecimal orderOriginNeedToPay = orderNeedToPay;

        previousDiscAMT = previousDiscAMT.multiply(orderOriginNeedToPay).divide(orderOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
        orderNeedToPay = orderNeedToPay.subtract(previousDiscAMT);
        if (orderNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        BigDecimal orderDiscAMT = orderNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift())
                continue;

            BigDecimal lineNeedToPay = BigDecimal.ZERO;

            if (onePieceMark){
                lineNeedToPay = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(discRate));
            }else{
                lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
            }

            BigDecimal previousDiscAMTSKU = sdkPromotionSettingManager
                            .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
            if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                continue;
            // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

            BigDecimal lineDiscAMT = orderDiscAMT.multiply(lineNeedToPay).divide(orderOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);

            lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
            settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 全场按件优惠，计算全场下的累计金额
     * 
     * @param lines
     * @param discAmount
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
            // 如果行上现有的优惠已经超过行实付时，跳到下一个行
            BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                            .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
            BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
            if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                continue;
            else
                lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

            BigDecimal lineDiscAMT = BigDecimal.ZERO;

            if (lineNeedPay.compareTo(BigDecimal.valueOf(factor).multiply(curLineDiscAmount)) > 0)
                lineDiscAMT = BigDecimal.valueOf(factor).multiply(curLineDiscAmount);
            else
                lineDiscAMT = lineNeedPay;

            settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 全场按件优惠，计算全场下的累计金额
     * 
     * @param lines
     * @param discRate
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCALLPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getDiscountAMTItemPerPCSByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCALLPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByCall(lines);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 组合优惠，计算该组合下的累计金额
     * 
     * @param lines
     * @param comboId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal skuPreviousDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedPay = BigDecimal.ZERO;
        BigDecimal lineShareDisc = BigDecimal.ZERO;

        BigDecimal comboDiscAMT = BigDecimal.ZERO;
        comboDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);

        BigDecimal comboOriginal = getNeedToPayAmountInShoppingCartByComboId(lines, comboId);
        BigDecimal comboPreviousDisc = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(lines, briefListPrevious, comboId);

        BigDecimal comboNeedPay = comboOriginal.subtract(comboPreviousDisc);
        if (comboNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            Set<String> comboIds = line.getComboIds();
            if (null == comboIds || comboIds.size() == 0){
                continue;
            }
            // 判断是否是Combo下的行
            if (comboIds.contains(String.valueOf(comboId))){
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                lineShareDisc = comboDiscAMT.multiply(lineNeedPay).divide(comboNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
                settingList.add(getPromotionSkuAMTSetting(line, lineShareDisc));
            }
        }
        return settingList;
    }

    /**
     * 组合折扣，计算该组合下的累计金额
     * 
     * @param lines
     * @param comboId
     * @param discRate
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal comboNeedToPay = BigDecimal.ZERO;
        BigDecimal comboDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal comboOriginNeedToPay = BigDecimal.ZERO;
        // BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        comboNeedToPay = getComboAmount(comboId, lines);
        if (comboNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(lines, briefListPrevious, comboId);

        comboOriginNeedToPay = comboNeedToPay;
        // totalOriginNeedToPay = getAllAmount(lines);

        // previousDiscAMT = previousDiscAMT.multiply(comboOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        comboNeedToPay = comboNeedToPay.subtract(previousDiscAMT);
        if (comboNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        comboDiscAMT = comboNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            if (line.isGift() || null == line.getComboIds() || line.getComboIds().size() == 0){
                continue;
            }

            if (line.getComboIds().contains(Long.toString(comboId))){
                lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());

                previousDiscAMTSKU = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                    continue;
                // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                lineDiscAMT = comboDiscAMT.multiply(lineNeedToPay).divide(comboOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
                continue;
            }
        }
        return settingList;
    }

    @Override
    public Set<Long> getItemIdsFromShoppingCartByComboId(List<ShoppingCartLineCommand> lines,long comboId){
        Set<Long> itemIds = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getComboIds() || line.getComboIds().size() == 0){
                continue;
            }
            // 计算该combo下的金额优惠,By Item。落在第一个该Item的SKU上
            if (line.getComboIds().contains(String.valueOf(comboId))){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());
            }
        }
        return itemIds;
    }

    @Override
    public Set<Long> getItemIdsFromShoppingCartByCategoryId(List<ShoppingCartLineCommand> lines,long categoryId){
        Set<Long> itemIds = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getCategoryList() || line.getCategoryList().size() == 0){
                continue;
            }
            // 计算该categoryId下的金额优惠,By Item。落在第一个该Item的SKU上
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());
            }
        }
        return itemIds;
    }

    @Override
    public Set<Long> getItemIdsFromShoppingCartByCustomItemIds(List<ShoppingCartLineCommand> lines,List<Long> itemIdList){
        Set<Long> itemIds = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isGift()){
                continue;
            }
            // 计算该categoryId下的金额优惠,By Item。落在第一个该Item的SKU上
            if (itemIdList.contains(line.getItemId())){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());
            }
        }
        return itemIds;
    }

    @Override
    public Set<Long> getItemIdsFromShoppingCartByCall(List<ShoppingCartLineCommand> lines){
        Set<Long> itemIds = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId()){
                continue;
            }
            if (itemIds.contains(line.getItemId()))
                continue;
            itemIds.add(line.getItemId());
        }
        return itemIds;
    }

    /**
     * 组合按Item优惠，计算该comboId下的累计金额
     * 
     * @param lines
     * @param comboId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();
        Set<Long> itemIds = new HashSet<Long>();
        itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        for (Long itemId : itemIds){
            settingOne = getDiscountAMTItemPerItemByAMT(lines, itemId, discAmount, factor, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }

        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 组合按Item折扣，计算该Item下的累计金额
     * 
     * @param lines
     * @param itemid
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal comboNeedToPay = BigDecimal.ZERO;
        BigDecimal comboDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal comboOriginNeedToPay = BigDecimal.ZERO;
        // BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        comboNeedToPay = getComboAmount(comboId, lines);
        if (comboNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(lines, briefListPrevious, comboId);

        comboOriginNeedToPay = comboNeedToPay;
        // totalOriginNeedToPay = getAllAmount(lines);
        // previousDiscAMT = previousDiscAMT.multiply(comboOriginNeedToPay).divide(totalOriginNeedToPay, 2,
        // BigDecimal.ROUND_HALF_EVEN);
        comboNeedToPay = comboNeedToPay.subtract(previousDiscAMT);
        if (comboNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        comboDiscAMT = comboNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        Set<Long> itemIds = new HashSet<Long>();
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            if (line.isGift() || null == line.getComboIds() || line.getComboIds().size() == 0){
                continue;
            }

            if (line.getComboIds().contains(Long.toString(comboId))){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());

                if (onePieceMark){
                    lineDiscAMT = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(discRate));
                }else{
                    lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());

                    previousDiscAMTSKU = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                        continue;
                    // lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                    lineDiscAMT = comboDiscAMT.multiply(lineNeedToPay).divide(comboOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                }
                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    /**
     * 按组合按件优惠，计算该Combo下的累计金额
     * 
     * @param lines
     * @param comboId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal skuPreviousDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            if (null == line.getComboIds() || line.getComboIds().size() == 0){
                continue;
            }
            // 计算该combo下的金额优惠,By PCS
            if (line.getComboIds().contains(String.valueOf(comboId))){
                BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = sdkPromotionSettingManager
                                .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                if (lineNeedPay.compareTo(curLineDiscAmount.multiply(BigDecimal.valueOf(factor))) > 0)
                    lineDiscAMT = curLineDiscAmount.multiply(BigDecimal.valueOf(factor));
                else
                    lineDiscAMT = lineNeedPay;

                settingList.add(getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 按组合按件折扣，计算该Combo下的累计金额
     * 
     * @param lines
     * @param comboId
     * @param discAmount
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTComboPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();
        Set<Long> itemIds = new HashSet<Long>();
        itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        for (Long itemId : itemIds){
            settingOne = getDiscountAMTItemPerPCSByRate(lines, itemId, discRate, onePieceMark, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    /**
     * 检查整单Coupon
     * 
     * @param couponTypeToCheck
     * @param couponCodes
     */
    @Override
    public boolean checkCouponByCALL(long couponTypeID,List<PromotionCouponCodeCommand> couponCodes,long shopID){
        if (null == couponCodes || couponCodes.size() == 0)
            return false;
        // 检查coupon是否符合条件
        return checkCouponWithCouponTypeIdShopId(couponCodes, couponTypeID, shopID);
    }

    /**
     * 检查ItemCoupon
     * 
     * @param shoppingCartLines
     * @param itemId
     * @param couponCodes
     */
    @Override
    public boolean checkOnLineCouponByItemId(List<ShoppingCartLineCommand> shoppingCartLines,long itemId,long couponTypeID,long shopId){
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.getItemId() == itemId){
                PromotionCouponCodeCommand couponOnLine = line.getCouponCodeOnLine();
                if (couponOnLine != null && couponOnLine.getIsused() == 0 && couponOnLine.getCouponId() == couponTypeID
                                && couponOnLine.getShopId() == shopId)
                    return true;
            }
        }
        return false;
    }

    /**
     * 行Coupon优先。行优惠在Line上 List<PromotionCouponCodeCommand> couponCodes,整单上的优惠
     */
    @Override
    public boolean checkCouponByItemId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long itemId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId){
        if (null == couponCodes || couponCodes.size() == 0 || null == shoppingCartLines || shoppingCartLines.size() == 0)
            return false;
        // TODO check coupon on line directly
        // 行Coupon优先，coupon在行上
        boolean flagOnLine = checkOnLineCouponByItemId(shoppingCartLines, itemId, couponTypeID, shopId);
        if (flagOnLine)
            return flagOnLine;
        // 判断购物车行是否有itemId下的商品
        boolean flag = checkItemInShoppingCartLines(shoppingCartLines, itemId);
        if (!flag)
            return flag;
        // 检查coupon是否符合条件
        return checkCouponWithCouponTypeIdShopId(couponCodes, couponTypeID, shopId);
    }

    /**
     * 检查分类Coupon
     * 
     * @param shoppingCartLines
     * @param categoryId
     * @param couponTypeToCheck
     * @param couponCodes
     */
    @Override
    public boolean checkOnLineCouponByCategoryId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeID,
                    long shopId){
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                PromotionCouponCodeCommand couponOnLine = line.getCouponCodeOnLine();
                if (couponOnLine != null && couponOnLine.getCouponId() == couponTypeID && couponOnLine.getShopId() == shopId)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkCouponByCategoryId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId){
        if (null == couponCodes || couponCodes.size() == 0 || null == shoppingCartLines || shoppingCartLines.size() == 0)
            return false;
        // TODO check coupon on line directly
        // 行Coupon优先，coupon在行上
        boolean flagOnLine = checkOnLineCouponByCategoryId(shoppingCartLines, categoryId, couponTypeID, shopId);
        if (flagOnLine)
            return flagOnLine;
        // 判断购物车行是否有categoryId分类下的商品
        boolean flag = checkCategoryInShoppingCartLines(shoppingCartLines, categoryId);
        if (!flag)
            return flag;
        // 检查coupon是否符合条件
        return checkCouponWithCouponTypeIdShopId(couponCodes, couponTypeID, shopId);
    }

    /**
     * 检查ComboCoupon
     * 
     * @param shoppingCartLines
     * @param comboId
     * @param couponTypeToCheck
     * @param couponCodes
     */
    @Override
    public boolean checkOnLineCouponByComboId(List<ShoppingCartLineCommand> shoppingCartLines,long comboId,long couponTypeID,long shopId){
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.getComboIds().contains(String.valueOf(comboId))){
                PromotionCouponCodeCommand couponOnLine = line.getCouponCodeOnLine();
                if (couponOnLine != null && couponOnLine.getCouponId() == couponTypeID && couponOnLine.getShopId() == shopId)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkCouponByComboId(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long comboId,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId){
        if (null == couponCodes || couponCodes.size() == 0 || null == shoppingCartLines || shoppingCartLines.size() == 0)
            return false;
        // 行Coupon优先，coupon在行上
        boolean flagOnLine = checkOnLineCouponByComboId(shoppingCartLines, comboId, couponTypeID, shopId);
        if (flagOnLine)
            return flagOnLine;
        // 判断购物车行是否有comboId下的商品
        boolean flag = checkComboInShoppingCartLines(shoppingCartLines, comboId);
        if (!flag)
            return flag;
        // 检查coupon是否符合条件
        return checkCouponWithCouponTypeIdShopId(couponCodes, couponTypeID, shopId);
    }

    private ShoppingCartLineCommand getMaxLineNeedToPayByCall(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice()).compareTo(lineNeedToPay) > 0){
                lineNeedToPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                maxLine = line;
            }
        }
        return maxLine;
    }

    private ShoppingCartLineCommand getMaxPriceLineByCall(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal maxPrice = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (line.getSalePrice().compareTo(maxPrice) > 0){
                maxPrice = line.getSalePrice();
                maxLine = line;
            }
        }
        return maxLine;
    }

    /**
     * 获取整单Coupon金额。根据CouponCodes List，检查当前Type的优惠券，计算出该Type的优惠金额
     */
    @Override
    public Map<String, BigDecimal> getDiscountAMTByCALLCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    BigDecimal previousDiscAMTAll){
        if (null == couponCodes || couponCodes.size() == 0)
            return null;
        // 根据couponCodes、couponTypeToCheck获取金额
        Map<String, BigDecimal> usedCouponList = new HashMap<String, BigDecimal>();
        ShoppingCartLineCommand maxLine = null;
        if (onePieceMark){
            BigDecimal lineTotal = BigDecimal.ZERO;
            maxLine = getMaxPriceLineByCall(shoppingCartLines);
            if (maxLine == null)
                return null;
            lineTotal = lineTotal.add(ONE_PIECE_QTY.multiply(maxLine.getSalePrice()));

            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeID, lineTotal, shopId);
        }else{
            maxLine = getMaxLineNeedToPayByCall(shoppingCartLines);
            if (maxLine == null)
                return null;
            BigDecimal originPayAmount = getOriginPayAmount(shoppingCartLines);
            originPayAmount = originPayAmount.subtract(previousDiscAMTAll);
            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeID, originPayAmount, shopId);
        }
        return usedCouponList;
    }

    private ShoppingCartLineCommand getMaxLineNeedToPayByItemId(List<ShoppingCartLineCommand> shoppingCartLines,long itemId){
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (String.valueOf(itemId).equals(String.valueOf(line.getItemId()))){
                if (BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice()).compareTo(lineNeedToPay) > 0){
                    lineNeedToPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    private ShoppingCartLineCommand getMaxPriceLineByItemId(List<ShoppingCartLineCommand> shoppingCartLines,long itemId){
        BigDecimal maxPrice = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (String.valueOf(itemId).equals(String.valueOf(line.getItemId()))){
                if (line.getSalePrice().compareTo(maxPrice) > 0){
                    maxPrice = line.getSalePrice();
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    /**
     * 获取ItemCoupon金额。根据CouponCodes List，检查当前Type的优惠券，计算出该Type下itemId的优惠金额
     * 
     * @param shoppingCartLines
     * @param itemId
     * @param couponTypeToCheck
     * @param couponCodes
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByItemIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long itemId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious){
        if (null == couponCodes || couponCodes.size() == 0)
            return null;
        if (null == shoppingCartLines || shoppingCartLines.size() == 0)
            return null;
        Map<String, BigDecimal> usedCouponList = new HashMap<String, BigDecimal>();

        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscShare = BigDecimal.ZERO;
        BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal itemNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        itemNeedToPay = getProductAmount(itemId, shoppingCartLines);

        if (itemNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 判断购物车行是否有itemId下的商品
        boolean flag = checkItemInShoppingCartLines(shoppingCartLines, itemId);
        if (!flag)
            return null;
        // TODO Change coupon to On Line Coupon
        // 如果行上有Coupon，直接用行上的Coupon覆盖掉order coupon
        boolean flagOnLine = checkOnLineCouponByItemId(shoppingCartLines, itemId, couponTypeId, shopId);

        totalOriginNeedToPay = getAllAmount(shoppingCartLines);

        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
        ShoppingCartLineCommand maxLine = getMaxLineNeedToPayByItemId(shoppingCartLines, itemId);
        if (maxLine == null)
            return null;
        if (flagOnLine && maxLine.getCouponCodeOnLine() != null){
            couponCodes = Arrays.asList(maxLine.getCouponCodeOnLine());
        }
        if (onePieceMark){
            maxLine = getMaxPriceLineByItemId(shoppingCartLines, itemId);
            if (maxLine == null)
                return null;
            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, ONE_PIECE_QTY.multiply(maxLine.getSalePrice()), shopId);
        }else{
            lineNeedToPay = new BigDecimal(maxLine.getQuantity()).multiply(maxLine.getSalePrice());

            lineDiscShare = previousDiscAMT.multiply(lineNeedToPay).divide(totalOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
            lineNeedToPay = lineNeedToPay.subtract(lineDiscShare);

            previousDiscAMTSKU = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                            shoppingCartLines,
                            briefListPrevious,
                            maxLine.getSkuId());

            lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, lineNeedToPay, shopId);
        }

        BigDecimal couponAmt = getCouponAmtFromUsedList(usedCouponList);
        couponAmt = couponAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
        PromotionSKUDiscAMTBySetting skuSetting = getPromotionSkuAMTSetting(maxLine, couponAmt);
        skuSetting.setCouponCodes(usedCouponList.keySet());

        settingList.add(skuSetting);

        return settingList;
    }

    private ShoppingCartLineCommand getMaxPriceLineByCategoryId(List<ShoppingCartLineCommand> shoppingCartLines,long categoryId){
        BigDecimal maxPrice = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                if (line.getSalePrice().compareTo(maxPrice) > 0){
                    maxPrice = line.getSalePrice();
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    private ShoppingCartLineCommand getMaxLineNeedToPayByCategoryId(List<ShoppingCartLineCommand> shoppingCartLines,long categoryId){
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (line.isGift())
                continue;
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                if (BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice()).compareTo(lineNeedToPay) > 0){
                    lineNeedToPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    /**
     * 获取分类Coupon金额.根据CouponCodes List，检查当前Type的优惠券，计算出该Type下categoryId的优惠金额
     * 
     * @param shoppingCartLines
     * @param categoryId
     * @param couponCodes
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByCategoryIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long categoryId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious){
        if (null == couponCodes || couponCodes.size() == 0)
            return null;
        if (null == shoppingCartLines || shoppingCartLines.size() == 0)
            return null;
        Map<String, BigDecimal> usedCouponList = new HashMap<String, BigDecimal>();

        BigDecimal categoryNeedToPay = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscShare = BigDecimal.ZERO;
        BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        categoryNeedToPay = getCategoryAmount(categoryId, shoppingCartLines);

        if (categoryNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 判断购物车行是否有itemId下的商品
        boolean flag = checkCategoryInShoppingCartLines(shoppingCartLines, categoryId);
        if (!flag)
            return null;
        // TODO Change coupon to On Line Coupon
        // 如果行上有Coupon，直接用行上的Coupon覆盖掉order coupon
        boolean flagOnLine = checkOnLineCouponByCategoryId(shoppingCartLines, categoryId, couponTypeId, shopId);

        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = sdkPromotionSettingManager
                        .getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shoppingCartLines, briefListPrevious, categoryId);

        ShoppingCartLineCommand maxLine = getMaxLineNeedToPayByCategoryId(shoppingCartLines, categoryId);

        if (flagOnLine && maxLine.getCouponCodeOnLine() != null){
            couponCodes = Arrays.asList(maxLine.getCouponCodeOnLine());
        }
        if (onePieceMark){
            maxLine = getMaxPriceLineByCategoryId(shoppingCartLines, categoryId);
            if (maxLine == null)
                return null;

            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, ONE_PIECE_QTY.multiply(maxLine.getSalePrice()), shopId);
        }else{
            lineNeedToPay = new BigDecimal(maxLine.getQuantity()).multiply(maxLine.getSalePrice());

            totalOriginNeedToPay = getCategoryAmount(categoryId, shoppingCartLines);
            lineDiscShare = previousDiscAMT.multiply(lineNeedToPay).divide(totalOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
            lineNeedToPay = lineNeedToPay.subtract(lineDiscShare);

            previousDiscAMTSKU = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                            shoppingCartLines,
                            briefListPrevious,
                            maxLine.getSkuId());

            lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, lineNeedToPay, shopId);
        }

        BigDecimal couponAmt = getCouponAmtFromUsedList(usedCouponList);
        couponAmt = couponAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
        PromotionSKUDiscAMTBySetting skuSetting = getPromotionSkuAMTSetting(maxLine, couponAmt);
        if (onePieceMark)
            skuSetting.setQty(ONE_PIECE_QTY.intValue());
        skuSetting.setCouponCodes(usedCouponList.keySet());

        settingList.add(skuSetting);
        return settingList;
    }

    private ShoppingCartLineCommand getMaxLineNeedToPayByComboId(List<ShoppingCartLineCommand> shoppingCartLines,long comboId){
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            Set<String> comboIds = line.getComboIds();
            if (null == comboIds || comboIds.size() == 0){
                continue;
            }
            // 检查购物车中是否包含该combo下的商品
            if (comboIds.contains(String.valueOf(comboId))){
                if (BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice()).compareTo(lineNeedToPay) > 0){
                    lineNeedToPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    private ShoppingCartLineCommand getMaxPriceLineByComboId(List<ShoppingCartLineCommand> shoppingCartLines,long comboId){
        BigDecimal maxPrice = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            Set<String> comboIds = line.getComboIds();
            if (null == comboIds || comboIds.size() == 0){
                continue;
            }
            // 检查购物车中是否包含该combo下的商品
            if (comboIds.contains(String.valueOf(comboId))){
                if (line.getSalePrice().compareTo(maxPrice) > 0){
                    maxPrice = line.getSalePrice();
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }

    /**
     * 获取ComboCoupon金额.根据CouponCodes List，检查当前Type的优惠券，计算出该Type下comboId的优惠金额
     * 
     * @param shoppingCartLines
     * @param comboId
     * @param couponCodes
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByComboIdCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    long comboId,
                    long couponTypeId,
                    List<PromotionCouponCodeCommand> couponCodes,
                    boolean onePieceMark,
                    long shopId,
                    List<PromotionBrief> briefListPrevious){
        if (null == couponCodes || couponCodes.size() == 0)
            return null;
        if (null == shoppingCartLines || shoppingCartLines.size() == 0)
            return null;

        Map<String, BigDecimal> usedCouponList = new HashMap<String, BigDecimal>();
        BigDecimal comboNeedToPay = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        // BigDecimal lineDiscShare = BigDecimal.ZERO;
        // BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        comboNeedToPay = getComboAmount(comboId, shoppingCartLines);

        if (comboNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        // 判断购物车行是否有comboId下的商品
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        boolean flag = checkComboInShoppingCartLines(shoppingCartLines, comboId);
        if (!flag)
            return null;
        // TODO Change coupon to On Line Coupon
        // 如果行上有Coupon，直接用行上的Coupon覆盖掉order coupon
        boolean flagOnLine = checkOnLineCouponByComboId(shoppingCartLines, comboId, couponTypeId, shopId);
        // BigDecimal previousDiscAMT =
        // sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);

        ShoppingCartLineCommand maxLine = getMaxLineNeedToPayByComboId(shoppingCartLines, comboId);
        if (maxLine == null)
            return null;
        // 根据couponCodes、couponTypeToCheck获取金额
        if (flagOnLine && maxLine.getCouponCodeOnLine() != null){
            couponCodes = Arrays.asList(maxLine.getCouponCodeOnLine());
        }
        if (onePieceMark){
            maxLine = getMaxPriceLineByComboId(shoppingCartLines, comboId);
            if (maxLine == null)
                return null;
            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, ONE_PIECE_QTY.multiply(maxLine.getSalePrice()), shopId);
        }else{
            lineNeedToPay = new BigDecimal(maxLine.getQuantity()).multiply(maxLine.getSalePrice());

            // totalOriginNeedToPay = getAllAmount(shoppingCartLines);
            // lineDiscShare = previousDiscAMT.multiply(lineNeedToPay).divide(totalOriginNeedToPay, 2,
            // BigDecimal.ROUND_HALF_EVEN);
            // lineNeedToPay = lineNeedToPay.subtract(lineDiscShare);

            previousDiscAMTSKU = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                            shoppingCartLines,
                            briefListPrevious,
                            maxLine.getSkuId());

            lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);
            usedCouponList = getCouponTotalAmount(couponCodes, couponTypeId, lineNeedToPay, shopId);
        }

        BigDecimal couponAmt = getCouponAmtFromUsedList(usedCouponList);
        couponAmt = couponAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
        PromotionSKUDiscAMTBySetting skuSetting = getPromotionSkuAMTSetting(maxLine, couponAmt);
        if (onePieceMark)
            skuSetting.setQty(ONE_PIECE_QTY.intValue());
        skuSetting.setCouponCodes(usedCouponList.keySet());

        settingList.add(skuSetting);

        return settingList;
    }

    /**
     * 累计优惠券金额
     * 
     * @param usedCouponList
     * @return
     */
    @Override
    public BigDecimal getCouponAmtFromUsedList(Map<String, BigDecimal> usedCouponList){
        BigDecimal amt = BigDecimal.ZERO;

        if (usedCouponList == null || usedCouponList.isEmpty())
            return BigDecimal.ZERO;
        else{
            for (String key : usedCouponList.keySet()){
                amt = amt.add(usedCouponList.get(key));
            }
        }
        return amt;
    }

    /**
     * 检查购物车行的商品是否有在itemId下的
     * 
     * @param lines
     * @return
     */
    private boolean checkItemInShoppingCartLines(List<ShoppingCartLineCommand> lines,Long itemId){
        for (ShoppingCartLineCommand line : lines){
            if (line.getItemId().equals(itemId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查购物车行的商品是否有在categoryId下的
     * 
     * @param lines
     * @return
     */
    private boolean checkCategoryInShoppingCartLines(List<ShoppingCartLineCommand> lines,Long categoryId){
        for (ShoppingCartLineCommand line : lines){
            if (line.getCategoryList().contains(categoryId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查购物车行的商品是否有在comboId下的
     * 
     * @param lines
     * @return
     */
    private boolean checkComboInShoppingCartLines(List<ShoppingCartLineCommand> lines,Long comboId){
        for (ShoppingCartLineCommand line : lines){
            Set<String> comboIds = line.getComboIds();
            if (null == comboIds || comboIds.size() == 0)
                continue;
            // 检查购物车中是否包含该combo下的商品
            if (comboIds.contains(String.valueOf(comboId)))
                return true;
        }
        return false;
    }

    /**
     * 检查coupon是否符合条件
     * 
     * @param couponTypeID
     * @param couponCodes
     * @return
     */
    private boolean checkCouponWithCouponTypeIdShopId(List<PromotionCouponCodeCommand> couponCodes,long couponTypeID,long shopID){
        // 根据couponType获取所有的促销coupon
        if (null == couponCodes || couponCodes.size() == 0)
            return false;
        for (PromotionCouponCodeCommand code : couponCodes){
            if (code.getCouponId().longValue() == couponTypeID && code.getShopId() == shopID){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据couponCodes、couponTypeToCheck获取金额
     * 
     * @param couponCodes
     * @param couponTypeId
     * @param totalPrice
     * @param shopID
     * @return
     */
    @Override
    public Map<String, BigDecimal> getCouponTotalAmount(
                    List<PromotionCouponCodeCommand> couponCodes,
                    long couponTypeId,
                    BigDecimal totalPrice,
                    long shopID){
        Map<String, BigDecimal> usedCouponList = new HashMap<String, BigDecimal>();
        BigDecimal totalAmount = new BigDecimal(0.0);
        BigDecimal rateDisc = new BigDecimal(0.0);
        // 根据couponType获取所有的促销coupon
        // List<PromotionCouponCodeCommand> promotionCouponCodes =
        // promotionCouponCodeDao.findPromotionCouponCodeCommandListByCouponCodeList(couponCodes,couponTypeId,new
        // Date(),shopID);
        if (null == couponCodes || couponCodes.size() == 0)
            return null;
        for (PromotionCouponCodeCommand promotionCouponCode : couponCodes){
            if (String.valueOf(PromotionCoupon.TYPE_RATE).equals(String.valueOf(promotionCouponCode.getCouponType()))){
                if (promotionCouponCode.getDiscount().compareTo(new BigDecimal(1)) > 0)
                    rateDisc = promotionCouponCode.getDiscount().divide(new BigDecimal(100));
                else
                    rateDisc = promotionCouponCode.getDiscount();

                rateDisc = HUNDRED_PERCENT.subtract(rateDisc);
                totalAmount = totalAmount.add(rateDisc.multiply(totalPrice));
            }else{
                if (totalPrice.compareTo(promotionCouponCode.getDiscount()) > 0)
                    totalAmount = promotionCouponCode.getDiscount();
                else
                    totalAmount = totalPrice;
            }
            totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            usedCouponList.put(promotionCouponCode.getCouponCode(), totalAmount);
            totalAmount = BigDecimal.ZERO;
        }
        return usedCouponList;
    }

    /**
     * 订单内单品件数，都有三种范围类型，PID 订单内单款件数: sku级别的限购
     * 
     * @param shopCart
     * @param itemId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderSKUQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();

        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }

            // 通过skuId分组, 判断哪一组的qtyCount违背了限购原则
            if (new Long(itemId).equals(line.getItemId())){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }

            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    /**
     * 订单内单品件数，都有三种范围类型CID 订单内单款件数: sku级别的限购
     * 
     * @param shopCart
     * @param categoryId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderSKUQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }

            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }

            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    @Override
    public List<ShoppingCartLineCommand> getOrderSKUQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        List<ShoppingCartLineCommand> cartLineList = null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }

            if (customItemIds.contains(line.getItemId())){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();//add by sku;
                errorLineList.add(cartLine);
            }

            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    /**
     * 订单内单品件数，都有三种范围类型CMBID 订单内单款件数: sku级别的限购
     * 
     * @param shopCart
     * @param comboId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderSKUQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        List<ShoppingCartLineCommand> cartLineList = null;
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }

            Set<String> comboIds = line.getComboIds();
            if (comboIds.contains(String.valueOf(comboId))){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }

            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }
        return null;
    }

    /**
     * 订单内商品数，都有三种范围类型，PID 商品的样数,
     * 
     * @param shopCart
     * @param itemId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderItemQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (new Long(itemId).equals(line.getItemId())){
                qty += 1;
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    @Override
    public List<ShoppingCartLineCommand> getOrderItemQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (customItemIds.contains(line.getItemId())){
                qty += 1;
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 订单内商品数，都有三种范围类型，CID
     * 
     * @param shopCart
     * @param categoryId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderItemQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Set<Long> itemIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        if (itemIdSet.size() > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 订单内商品数，都有三种范围类型CMBID 订单内商品数:可以说是商品的样数, 商品筛选器中出现的商品集合中的任选几样
     * 
     * @param shopCart
     * @param comboId
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderItemQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Set<Long> itemIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            Set<String> comboIds = line.getComboIds();
            if (comboIds != null && comboIds.contains(String.valueOf(comboId))){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        if (itemIdSet.size() > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 订单内件数，都有三种范围类型，PID
     * 
     * @param shopCart
     * @param itemId
     * @param qtyLimited
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (new Long(itemId).equals(line.getItemId())){
                qty += line.getQuantity();
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 订单内件数，都有三种范围类型，CID
     * 
     * @param shopCart
     * @param categoryId
     * @param qtyLimited
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                qty += line.getQuantity();
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    @Override
    public List<ShoppingCartLineCommand> getOrderQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (customItemIds.contains(line.getItemId())){
                qty += line.getQuantity();
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 订单内件数，都有三种范围类型，CMBID
     * 
     * @param shopCart
     * @param comboId
     * @param qtyLimited
     */
    @Override
    public List<ShoppingCartLineCommand> getOrderQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            Set<String> comboIds = line.getComboIds();
            if (comboIds.contains(String.valueOf(comboId))){
                qty += line.getQuantity();
                errorLineList.add(line);
            }
        }
        if (qty > qtyLimited){
            return errorLineList;
        }
        return null;
    }

    /**
     * 历史购买件数，都有三种范围类型，PID
     * 
     * @param shopCart
     * @param itemId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();

        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        Map<Long, Integer> qtyCountBySkuIdMap = new HashMap<Long, Integer>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (new Long(itemId).equals(line.getItemId())){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
            qtyCountBySkuIdMap.put(entry.getKey(), qtyCount);
        }

        Set<Long> skuIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            Long currSkuId = line.getSkuId();
            Integer qtyCount = qtyCountBySkuIdMap.get(currSkuId);
            if (new Long(itemId).equals(line.getItemId())){
                if (skuIdSet.contains(currSkuId)){
                    continue;
                }else{
                    skuIdSet.add(currSkuId);
                }
                // 该sku查询历史订单下单数量
                Integer historyQty = sdkOrderLineDao.findOrderLinesCountBySkuId(currSkuId, line.getShopId(), getMemberId(shopCart));
                if (null != historyQty && historyQty > 0){
                    qtyCount += historyQty;
                }
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    /**
     * 历史购买件数，都有三种范围类型，CID
     * 
     * @param shopCart
     * @param categoryId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();

        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        Map<Long, Integer> qtyCountBySkuIdMap = new HashMap<Long, Integer>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
            qtyCountBySkuIdMap.put(entry.getKey(), qtyCount);
        }

        Set<Long> skuIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            Long currSkuId = line.getSkuId();
            Integer qtyCount = qtyCountBySkuIdMap.get(currSkuId);
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                if (skuIdSet.contains(currSkuId)){
                    continue;
                }else{
                    skuIdSet.add(currSkuId);
                }
                // 该sku查询历史订单下单数量
                Integer historyQty = sdkOrderLineDao.findOrderLinesCountBySkuId(currSkuId, line.getShopId(), getMemberId(shopCart));
                if (null != historyQty && historyQty > 0){
                    qtyCount += historyQty;
                }
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByCustomId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();

        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        Map<Long, Integer> qtyCountBySkuIdMap = new HashMap<Long, Integer>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();//add by sku;
                errorLineList.add(cartLine);
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
            qtyCountBySkuIdMap.put(entry.getKey(), qtyCount);
        }

        Set<Long> skuIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            Long currSkuId = line.getSkuId();
            Integer qtyCount = qtyCountBySkuIdMap.get(currSkuId);
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                if (skuIdSet.contains(currSkuId)){
                    continue;
                }else{
                    skuIdSet.add(currSkuId);
                }
                // 该sku查询历史订单下单数量
                Integer historyQty = sdkOrderLineDao.findOrderLinesCountBySkuId(currSkuId, line.getShopId(), getMemberId(shopCart));
                if (null != historyQty && historyQty > 0){
                    qtyCount += historyQty;
                }
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
        }

        return null;
    }

    /**
     * 历史购买件数，都有三种范围类型，CMBID
     * 
     * @param shopCart
     * @param comboId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderSKUQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Map<Long, List<ShoppingCartLineCommand>> cartLineBySkuMap = new HashMap<Long, List<ShoppingCartLineCommand>>();
        Map<Long, Integer> qtyCountBySkuIdMap = new HashMap<Long, Integer>();
        List<ShoppingCartLineCommand> cartLineList = null;
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getComboIds().contains(String.valueOf(comboId))){
                Long skuId = line.getSkuId();
                if (cartLineBySkuMap.containsKey(skuId)){
                    cartLineList = cartLineBySkuMap.get(skuId);
                    cartLineList.add(line);
                }else{
                    cartLineList = new ArrayList<ShoppingCartLineCommand>();
                    cartLineList.add(line);
                }

                cartLineBySkuMap.put(skuId, cartLineList);
            }
        }

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : cartLineBySkuMap.entrySet()){
            Integer qtyCount = 0;
            List<ShoppingCartLineCommand> cartLineListBySku = entry.getValue();
            for (ShoppingCartLineCommand cartLine : cartLineListBySku){
                qtyCount += cartLine.getQuantity();// add by sku;
                errorLineList.add(cartLine);
            }
            if (qtyCount > qtyLimited){
                return errorLineList;
            }
            qtyCountBySkuIdMap.put(entry.getKey(), qtyCount);
        }

        Set<Long> skuIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (line.getComboIds().contains(String.valueOf(comboId))){
                Long currSkuId = line.getSkuId();
                Integer qtyCount = qtyCountBySkuIdMap.get(currSkuId);
                if (skuIdSet.contains(currSkuId)){
                    continue;
                }else{
                    skuIdSet.add(currSkuId);
                }
                // 该sku查询历史订单下单数量
                Integer historyQty = sdkOrderLineDao.findOrderLinesCountBySkuId(currSkuId, line.getShopId(), getMemberId(shopCart));
                if (null != historyQty && historyQty > 0){
                    qtyCount += historyQty;
                }
                if (qtyCount > qtyLimited){
                    return errorLineList;
                }
            }
        }
        return null;
    }

    private Long getMemberId(ShoppingCartCommand shopCart){
        Long memberId = null;
        if (null != shopCart.getUserDetails())
            memberId = shopCart.getUserDetails().getMemberId();
        return memberId;
    }

    /**
     * 历史购买商品数，都有三种范围类型，PID
     * 
     * @param shopCart
     * @param itemId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;

        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        boolean flag = true;
        Long shopId = 0L;
        Set<Long> itemIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (new Long(itemId).equals(line.getItemId())){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        // 如果购物车中的数量已经大于了qtyLimited
        qty = itemIdSet.size();
        if (qty > qtyLimited)
            return errorLineList;
        // 查询item历史订单
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(itemId);
        Integer histroyQty = sdkOrderLineDao.findOrderLinesCountByItemIds(itemIds, shopId, getMemberId(shopCart));
        if (null != histroyQty && histroyQty > 0){
            qty += histroyQty;
            if (qty > qtyLimited){
                return errorLineList;
            }
        }
        return null;
    }

    /**
     * 历史购买商品数，都有三种范围类型，CID
     * 
     * @param shopCart
     * @param categoryId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByCategoryId(
                    ShoppingCartCommand shopCart,
                    long categoryId,
                    Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        boolean flag = true;
        Long shopId = 0L;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Set<Long> itemIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        // 如果购物车中的数量已经大于了qtyLimited
        qty = itemIdSet.size();
        if (qty > qtyLimited)
            return errorLineList;
        // 根据category查出所有的itemId，再根据itemId去统计
        Integer histroyQty = sdkOrderLineDao.findOrderLinesCountByCategoryId(categoryId, shopId, getMemberId(shopCart));
        if (null != histroyQty && histroyQty > 0){
            qty += histroyQty;
            if (qty > qtyLimited)
                return errorLineList;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        boolean flag = true;
        Long shopId = 0L;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<Long> itemIdSet = new ArrayList<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (customItemIds.contains(line.getItemId())){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        // 如果购物车中的数量已经大于了qtyLimited
        qty = itemIdSet.size();
        if (qty > qtyLimited)
            return errorLineList;
        // 根据category查出所有的itemId，再根据itemId去统计
        Integer histroyQty = sdkOrderLineDao.findHistoryOrderCountByItemIds(itemIdSet, shopId, getMemberId(shopCart));
        if (null != histroyQty && histroyQty > 0){
            qty += histroyQty;
            if (qty > qtyLimited)
                return errorLineList;
        }
        return null;
    }

    /**
     * 历史购买商品数，都有三种范围类型，CMBID 历史购买商品样数
     * 
     * @param shopCart
     * @param categoryId
     * @param qtyLimited
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderItemQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        Integer qty = 0;
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        boolean flag = true;
        Long shopId = 0L;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        Set<Long> itemIdSet = new HashSet<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (line.getComboIds().contains(String.valueOf(comboId))){
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
            }
        }
        // 如果购物车中的数量已经大于了qtyLimited
        qty = itemIdSet.size();
        if (qty > qtyLimited)
            return errorLineList;

        Map<String, Map<String, Set<Long>>> rs = sdkFilterManager.analyzeProductExpression(comboId);

        Integer incluQty = 0;// 规则数量

        if (null != rs && rs.size() > 0){

            Long memberId = getMemberId(shopCart);

            Map<String, Set<Long>> in = rs.get(ItemTagRule.ANALYSIS_KEY_IN);// 规则项
            Map<String, Set<Long>> out = rs.get(ItemTagRule.ANALYSIS_KEY_OUT);// 排除项

            Set<Long> inAll = in.get(ItemTagRule.EXP_ALLPRODUCT);// 规则所有项（所有商品）
            Set<Long> inItems = in.get(ItemTagRule.EXP_PRODUCT);// 规则商品
            Set<Long> inCategorys = in.get(ItemTagRule.EXP_CATEGORY);// 规则商品分类

            Set<Long> outItems = out.get(ItemTagRule.EXP_PRODUCT);// 排除商品
            Set<Long> outCategorys = out.get(ItemTagRule.EXP_CATEGORY);// 排除商品分类

            // 如果规则所有项为空
            if (null == inAll){
                // 执行商品分类的去重
                if (null != inCategorys && inCategorys.size() != 0 && null != outCategorys && outCategorys.size() != 0){
                    Iterator<Long> chkOutCategory = outCategorys.iterator();// 循环排除集合
                    while (chkOutCategory.hasNext()){
                        Long outCategory = chkOutCategory.next();
                        if (inCategorys.contains(outCategory)){
                            chkOutCategory.remove();
                            inCategorys.remove(outCategory);
                        }
                    }
                }
                // 规则商品分类集合不为空
                if (null != inCategorys && inCategorys.size() != 0){
                    List<Long> dbInItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(inCategorys));
                    if (null != dbInItemids && dbInItemids.size() != 0){
                        inItems.addAll(dbInItemids);// 查询出的商品ids添加到规则商品集合。
                    }
                }
                // 排除商品分类集合不为空
                if (null != outCategorys && outCategorys.size() != 0){
                    List<Long> dbOutItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(outCategorys));
                    if (null != dbOutItemids && dbOutItemids.size() != 0){
                        outItems.addAll(dbOutItemids); // 查询出的商品ids添加到排除商品集合。
                    }
                }
                // 执行商品的去重
                if (null != inItems && inItems.size() != 0 && null != outItems && outItems.size() != 0){
                    Iterator<Long> chkOutItem = outItems.iterator();
                    while (chkOutItem.hasNext()){
                        Long outItem = chkOutItem.next();
                        if (inItems.contains(outItem)){
                            chkOutItem.remove();
                            inItems.remove(outItem);
                        }
                    }
                }
                // 规则商品不为空查询商品数量
                if (null != inItems && inItems.size() != 0){
                    List<OrderLine> orderLineList = sdkOrderLineDao
                                    .findHistoryOrderLinesByItemIds(new ArrayList<Long>(inItems), shopId, memberId);

                    if (null != orderLineList && orderLineList.size() > 0){
                        List<Long> historyOrderItemIdList = new ArrayList<Long>();
                        for (OrderLine orderLine : orderLineList){
                            historyOrderItemIdList.add(orderLine.getItemId());
                        }

                        itemIdSet.addAll(historyOrderItemIdList);
                        qty = itemIdSet.size();
                    }
                }
            }else{
                // 排除商品分类集合不为空
                if (null != outCategorys && outCategorys.size() != 0){
                    List<Long> dbOutItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(outCategorys));
                    if (null != dbOutItemids && dbOutItemids.size() != 0){
                        outItems.addAll(dbOutItemids); // 查询出的商品ids添加到排除商品集合。
                    }
                }
                if (null != outItems && outItems.size() != 0){
                    incluQty = sdkOrderLineDao.findHistoryOrderLinesCountByNotItmeIds(new ArrayList<Long>(outItems), shopId, memberId);
                    if (null != incluQty && incluQty > 0){
                        qty += incluQty;
                    }
                }
            }
            if (qty > qtyLimited){
                return errorLineList;
            }
        }
        return null;
    }

    /**
     * 历史购买订单数，都有三种范围类型，PID
     * 
     * @param shopCart
     * @param itemId
     * @param qtyLimited
     *            限制购买的次数
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderQtyByItemId(ShoppingCartCommand shopCart,long itemId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        Integer orderCount = 0;
        Long shopId = 0L;
        boolean flag = true;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                shopId = line.getShopId();
                flag = false;
            }
            if (new Long(itemId).equals(line.getItemId())){
                orderCount = 1;
                errorLineList.add(line);
                break;
            }
        }
        // 根据itemId查询历史订单次数
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(itemId);
        Integer histroyQty = sdkOrderLineDao.findHistoryOrderCountByItemIds(itemIds, shopId, getMemberId(shopCart));
        if (null != histroyQty && histroyQty > 0){
            orderCount += histroyQty;
            if (orderCount > qtyLimited)
                return errorLineList;
        }
        return null;
    }

    /**
     * 历史购买订单数，都有三种范围类型CID
     * 
     * @param shopCart
     * @param categoryId
     * @param qtyLimited
     *            限制购买的次数
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderQtyByCategoryId(ShoppingCartCommand shopCart,long categoryId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        Integer orderCount = 0;
        Long shopId = 0L;
        boolean flag = true;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (line.getCategoryList().contains(Long.valueOf(categoryId))){
                orderCount = 1;
                errorLineList.add(line);
                break;
            }
        }
        // 根据itemId查询历史订单次数
        Integer histroyQty = sdkOrderLineDao.findHistoryOrderCountByCategoryId(categoryId, shopId, getMemberId(shopCart));
        if (null != histroyQty){
            orderCount += histroyQty;
            if (orderCount > qtyLimited)
                return errorLineList;
        }
        return null;
    }

    /**
     * 历史购买订单数，都有三种范围类型CMBID
     * 
     * @param shopCart
     * @param comboId
     * @param qtyLimited
     *            限制购买的次数
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderQtyByComboId(ShoppingCartCommand shopCart,long comboId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        Integer orderCount = 0;
        Long shopId = 0L;
        boolean flag = true;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (line.getComboIds().contains(String.valueOf(comboId))){
                orderCount = 1;
                errorLineList.add(line);
                break;
            }
        }

        Integer incluQty = 0;

        // 根据商品筛选器ID，解析出所对应商品分类ID与商品ID列表
        Map<String, Map<String, Set<Long>>> rs = sdkFilterManager.analyzeProductExpression(comboId);

        if (null != rs && rs.size() > 0){

            Long memberId = getMemberId(shopCart);

            Map<String, Set<Long>> in = rs.get(ItemTagRule.ANALYSIS_KEY_IN);// 规则项
            Map<String, Set<Long>> out = rs.get(ItemTagRule.ANALYSIS_KEY_OUT);// 排除项

            Set<Long> inAll = in.get(ItemTagRule.EXP_ALLPRODUCT);// 规则所有项（所有商品）
            Set<Long> inItems = in.get(ItemTagRule.EXP_PRODUCT);// 规则商品
            Set<Long> inCategorys = in.get(ItemTagRule.EXP_CATEGORY);// 规则商品分类

            Set<Long> outItems = out.get(ItemTagRule.EXP_PRODUCT);// 排除商品
            Set<Long> outCategorys = out.get(ItemTagRule.EXP_CATEGORY);// 排除商品分类

            // 如果规则所有项为空
            if (null == inAll){
                // 执行商品分类的去重
                if (null != inCategorys && inCategorys.size() != 0 && null != outCategorys && outCategorys.size() != 0){
                    Iterator<Long> chkOutCategory = outCategorys.iterator();// 循环排除集合
                    while (chkOutCategory.hasNext()){
                        Long outCategory = chkOutCategory.next();
                        if (inCategorys.contains(outCategory)){
                            chkOutCategory.remove();
                            inCategorys.remove(outCategory);
                        }
                    }
                }
                // 规则商品分类集合不为空
                if (null != inCategorys && inCategorys.size() != 0){
                    List<Long> dbInItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(inCategorys));
                    if (null != dbInItemids && dbInItemids.size() != 0){
                        inItems.addAll(dbInItemids);// 查询出的商品ids添加到规则商品集合。
                    }
                }
                // 排除商品分类集合不为空
                if (null != outCategorys && outCategorys.size() != 0){
                    List<Long> dbOutItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(outCategorys));
                    if (null != dbOutItemids && dbOutItemids.size() != 0){
                        outItems.addAll(dbOutItemids); // 查询出的商品ids添加到排除商品集合。
                    }
                }
                // 执行商品的去重
                if (null != inItems && inItems.size() != 0 && null != outItems && outItems.size() != 0){
                    Iterator<Long> chkOutItem = outItems.iterator();
                    while (chkOutItem.hasNext()){
                        Long outItem = chkOutItem.next();
                        if (inItems.contains(outItem)){
                            chkOutItem.remove();
                            inItems.remove(outItem);
                        }
                    }
                }
                // 规则商品不为空查询商品数量
                if (null != inItems && inItems.size() != 0){
                    incluQty = sdkOrderLineDao.findHistoryOrderCountItemQtyByItemIds(new ArrayList<Long>(inItems), shopId, memberId);
                    if (null != incluQty && incluQty > 0){
                        orderCount += incluQty;
                    }
                }
            }else{
                // 排除商品分类集合不为空
                if (null != outCategorys && outCategorys.size() != 0){
                    List<Long> dbOutItemids = itemCategoryDao.findDistinctItemIdByCategory(new ArrayList<Long>(outCategorys));
                    if (null != dbOutItemids && dbOutItemids.size() != 0){
                        outItems.addAll(dbOutItemids); // 查询出的商品ids添加到排除商品集合。
                    }
                }
                if (null != outItems && outItems.size() != 0){
                    incluQty = sdkOrderLineDao.findHistoryOrderCountItemByNotItemIds(new ArrayList<Long>(outItems), shopId, memberId);
                    if (null != incluQty && incluQty > 0){
                        orderCount += incluQty;
                    }
                }
            }
            if (orderCount > qtyLimited){
                return errorLineList;
            }
        }
        return null;
    }

    /**
     * 将购物车行进行区分店铺
     * 
     * @param shoppingCartLineCommandList
     * @return
     */
    @Override
    public Map<Long, ShoppingCartCommand> getShoppingCartMapByShop(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();
        if (null != shoppingCartLineCommandList && shoppingCartLineCommandList.size() > 0){
            List<ShoppingCartLineCommand> newLines = null;
            for (ShoppingCartLineCommand line : shoppingCartLineCommandList){
                if (line.isCaptionLine()){
                    continue;
                }
                Long shopId = line.getShopId();
                ShoppingCartCommand cart = shopCartByShopIdMap.get(shopId);
                if (null == cart){
                    // 如果map中不存在购物车对象
                    cart = new ShoppingCartCommand();
                    newLines = new ArrayList<ShoppingCartLineCommand>();
                }else{
                    // 如果map中存在购物车对象
                    newLines = cart.getShoppingCartLineCommands();
                }
                newLines.add(line);
                cart.setShoppingCartLineCommands(newLines);
                shopCartByShopIdMap.put(shopId, cart);
            }
        }
        return shopCartByShopIdMap;
    }

    /**
     * 同步购物车
     * 
     * @param memberId
     * @param lines
     */
    @Override
    public void synchronousShoppingCart(Long memberId,List<ShoppingCartLineCommand> shoppingLines){
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand line : shoppingLines){
                // 不同步赠品数据
                if (line.isGift()){
                    continue;
                }
                ShoppingCartLineCommand cartLine = sdkShoppingCartLineDao.findShopCartLine(memberId, line.getExtentionCode());
                if (null != cartLine){
                    // 如果数据库购物车表中会员有该商品，则将把该商品的数量相加
                    Integer qty = cartLine.getQuantity() + line.getQuantity();
                    cartLine.setQuantity(qty);
                    merageShoppingCartLine(memberId, cartLine, false);
                }else{
                    line.setMemberId(memberId);
                    merageShoppingCartLine(memberId, line, true);
                }
            }
        }
    }

    /**
     * 同步购物车
     * 
     * @param memberId
     * @param lines
     */
    @Override
    public void synchronousShoppingCartById(Long memberId,List<ShoppingCartLineCommand> shoppingLines){
        if (null != shoppingLines && shoppingLines.size() > 0){
            for (ShoppingCartLineCommand line : shoppingLines){
                // 不同步赠品数据
                if (line.isGift()){
                    continue;
                }
                ShoppingCartLineCommand cartLine = sdkShoppingCartLineDao.findShopCartLine(memberId, line.getExtentionCode());
                if (null != cartLine){
                    // 如果数据库购物车表中会员有该商品，则将把该商品的数量相加
                    Integer qty = cartLine.getQuantity() + line.getQuantity();
                    cartLine.setQuantity(qty);
                    merageShoppingCartLineById(memberId, cartLine, false);
                }else{
                    line.setMemberId(memberId);
                    merageShoppingCartLineById(memberId, line, true);
                }
            }
        }
    }

    @Override
    public Integer addOrUpdateShoppingCart(
                    Long memberId,
                    String extentionCode,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce){
        if (null == lines || lines.size() == 0)
            return FAILURE;
        Integer retval = SUCCESS;
        ShoppingCartCommand cart = new ShoppingCartCommand();
        ShoppingCartLineCommand shoppingCartLine = null;
        // 有效的购物车行(不包含当前添加或修改的行)
        List<ShoppingCartLineCommand> validLines = new ArrayList<ShoppingCartLineCommand>();

        for (ShoppingCartLineCommand line : lines){

            if (line.isGift() || line.isCaptionLine()){
                continue;
            }

            // 封装购物车行数据
            sdkEngineManager.packShoppingCartLine(line);
            if (extentionCode.equals(line.getExtentionCode())){
                // 检查商品有效性
                if (new Integer(1).equals(line.getValidType()) && !line.isValid()){
                    // 有效性检验失败
                    throw new BusinessException(Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM, new Object[] { line.getItemName() });
                }
                // 库存不足
                else if (new Integer(2).equals(line.getValidType()) && !line.isValid()){
                    throw new BusinessException(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, new Object[] { line.getItemName() });
                }
                // // 赠品不可以购买
                // else if(ItemInfo.TYPE_GIFT.equals(line.getType())){
                // throw new BusinessException(Constants.THE_ITEM_IS_GIFT, new Object[] { line.getItemName() });
                // }
                shoppingCartLine = line;
            }
            // 有效的购物车行
            if (line.isValid()){
                validLines.add(line);
            }
        }
        if (null == shoppingCartLine)// 购物车列表中无extentionCode
            return FAILURE;

        // 设置memcombo\memberId
        UserDetails userDetails = new UserDetails();
        Set<String> memboIds = null;
        if (null != memberId){
            memboIds = memComboIds;
        }else{
            // 获取游客的memboIds
            memboIds = getMemboIds();
        }
        userDetails.setMemComboList(memboIds);
        userDetails.setMemberId(memberId);
        cart.setUserDetails(userDetails);

        // 设置购物车有效的购物车行
        cart.setShoppingCartLineCommands(validLines);

        // 引擎检查(限购、库存)
        retval = doEngineChck(cart, shoppingCartLine, memboIds, isReduce);
        if (SUCCESS != retval){
            return retval;
        }

        if (null != memberId){
            shoppingCartLine.setMemberId(memberId);
            if (exist){
                // 该sku在购物车中存在。则更新该购物行
                retval = merageShoppingCartLine(memberId, shoppingCartLine, false);
            }else{
                // 该sku在购物车中不存在
                retval = merageShoppingCartLine(memberId, shoppingCartLine, true);
            }
        }
        return retval;
    }

    @Override
    public Integer addOrUpdateShoppingCart(
                    Long memberId,
                    Long lineId,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce){
        if (null == lines || lines.size() == 0)
            return FAILURE;
        Integer retval = SUCCESS;
        ShoppingCartCommand cart = new ShoppingCartCommand();
        ShoppingCartLineCommand shoppingCartLine = null;
        // 有效的购物车行(不包含当前添加或修改的行)
        List<ShoppingCartLineCommand> validLines = new ArrayList<ShoppingCartLineCommand>();

        for (ShoppingCartLineCommand line : lines){
            // 封装购物车行数据
            sdkEngineManager.packShoppingCartLine(line);
            if (line.getId().equals(lineId)){
                // 检查商品有效性
                if (new Integer(1).equals(line.getValidType()) && !line.isValid()){
                    // 有效性检验失败
                    throw new BusinessException(Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM, new Object[] { line.getItemName() });
                }else if (new Integer(2).equals(line.getValidType()) && !line.isValid()){
                    // 库存不足
                    throw new BusinessException(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, new Object[] { line.getItemName() });
                }
                shoppingCartLine = line;
            }
            // 有效的购物车行
            if (line.isValid()){
                validLines.add(line);
            }
        }
        if (null == shoppingCartLine)// 购物车列表中无extentionCode
            return FAILURE;

        // 设置memcombo\memberId
        UserDetails userDetails = new UserDetails();
        Set<String> memboIds = null;
        if (null != memberId){
            memboIds = memComboIds;
        }else{
            // 获取游客的memboIds
            memboIds = getMemboIds();
        }
        userDetails.setMemComboList(memboIds);
        userDetails.setMemberId(memberId);
        cart.setUserDetails(userDetails);

        // 设置购物车有效的购物车行
        cart.setShoppingCartLineCommands(validLines);

        // 引擎检查(限购、库存)
        retval = doEngineChck(cart, shoppingCartLine, memboIds, isReduce);
        if (SUCCESS != retval){
            return retval;
        }

        if (null != memberId){
            shoppingCartLine.setMemberId(memberId);
            if (exist){
                // 该sku在购物车中存在。则更新该购物行
                retval = merageShoppingCartLine(memberId, shoppingCartLine, false);
            }else{
                // 该sku在购物车中不存在
                retval = merageShoppingCartLine(memberId, shoppingCartLine, true);
            }
        }
        return retval;
    }

    @Override
    public Integer addOrUpdateShoppingCartById(
                    Long memberId,
                    String extentionCode,
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComboIds,
                    boolean exist,
                    boolean isReduce){
        if (null == lines || lines.size() == 0)
            return FAILURE;
        Integer retval = SUCCESS;
        ShoppingCartCommand cart = new ShoppingCartCommand();
        ShoppingCartLineCommand shoppingCartLine = null;
        // 有效的购物车行(不包含当前添加或修改的行)
        List<ShoppingCartLineCommand> validLines = new ArrayList<ShoppingCartLineCommand>();

        for (ShoppingCartLineCommand line : lines){
            // 封装购物车行数据
            sdkEngineManager.packShoppingCartLine(line);
            if (line.getExtentionCode().equals(extentionCode)){
                // 检查商品有效性
                if (new Integer(1).equals(line.getValidType()) && !line.isValid()){
                    // 有效性检验失败
                    throw new BusinessException(
                                    Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM,
                                    new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
                }else if (new Integer(2).equals(line.getValidType()) && !line.isValid()){
                    // 库存不足
                    throw new BusinessException(
                                    Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                    new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
                }
                shoppingCartLine = line;
            }
            // 有效的购物车行
            if (line.isValid()){
                validLines.add(line);
            }
        }
        if (null == shoppingCartLine)// 购物车列表中无extentionCode
            return FAILURE;

        // 设置memcombo\memberId
        UserDetails userDetails = new UserDetails();
        Set<String> memboIds = null;
        if (null != memberId){
            memboIds = memComboIds;
        }else{
            // 获取游客的memboIds
            memboIds = getMemboIds();
        }
        userDetails.setMemComboList(memboIds);
        userDetails.setMemberId(memberId);
        cart.setUserDetails(userDetails);

        // 设置购物车有效的购物车行
        cart.setShoppingCartLineCommands(validLines);

        // 引擎检查(限购、库存)
        retval = doEngineChck(cart, shoppingCartLine, memboIds, isReduce);
        if (SUCCESS != retval){
            return retval;
        }

        if (null != memberId){
            shoppingCartLine.setMemberId(memberId);
            if (exist){
                // 该sku在购物车中存在。则更新该购物行
                retval = merageShoppingCartLineById(memberId, shoppingCartLine, false);
            }else{
                // 该sku在购物车中不存在
                retval = merageShoppingCartLineById(memberId, shoppingCartLine, true);
            }
        }
        return retval;
    }

    @Override
    @Transactional(readOnly = true)
    public void checkShoppingCartOnCalc(Long memberId,List<ShoppingCartLineCommand> lines,Set<String> memCombos){
        if (null == lines || lines.size() == 0){
            // 购物车不能为空
            throw new BusinessException(com.baozun.nebula.sdk.constants.Constants.SHOPCART_IS_NULL);
        }
        // 返回值
        // Integer retval = SUCCESS;
        Set<String> memboIds = null;
        if (null == memberId){
            // 游客
            memboIds = getMemboIds();
        }else{
            memboIds = memCombos;
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setMemberId(memberId);
        userDetails.setMemComboList(memboIds);

        // 封装购物车行数据
        for (ShoppingCartLineCommand line : lines){
            sdkEngineManager.packShoppingCartLine(line);
        }

        // 获取限购规则
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(getShopIds(lines), memboIds, getItemComboIds(lines), new Date());

        ShoppingCartCommand cart = new ShoppingCartCommand();
        // 设置购物车行
        cart.setShoppingCartLineCommands(lines);
        // 设置用户信息
        cart.setUserDetails(userDetails);

        cart = getShoppingCart(lines, null, memberId, null, userDetails.getMemComboList());

        Map<Long, ShoppingCartCommand> shoppingCartCommandByShopIdMap = cart.getShoppingCartByShopIdMap();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommandByShopIdMap.entrySet()){
            if (null == entry.getValue()){
                continue;
            }
            for (ShoppingCartLineCommand line : entry.getValue().getShoppingCartLineCommands()){

                // caption行与非卖品是不须要有效性检察
                if (line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                    continue;
                }

                if (new Integer(CHECKED_STATE).equals(line.getSettlementState())){
                    // 检查商品有效性
                    sdkEngineManager.doEngineCheck(line, false, cart, purchaseLimitationList);
                }

            }

            // 限购校验
            List<ShoppingCartLineCommand> errorLineList = sdkEngineManager.doEngineCheckLimit(cart, purchaseLimitationList);
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

    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMiniSKUQTYInShoppingCartByAll(ShoppingCartCommand shopCart){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Set<Integer> qtys = new HashSet<Integer>();
        for (ShoppingCartLineCommand line : lines){
            qtys.add(line.getQuantity());
        }
        if (null != qtys && qtys.size() > 0){
            // 返回集合中最小值
            return Collections.min(qtys);
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMiniSKUQTYInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Set<Integer> qtys = new HashSet<Integer>();
        for (ShoppingCartLineCommand line : lines){
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                qtys.add(line.getQuantity());
            }
        }
        if (null != qtys && qtys.size() > 0){
            // 返回集合中最小值
            return Collections.min(qtys);
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMiniSKUQTYInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Set<Integer> qtys = new HashSet<Integer>();
        for (ShoppingCartLineCommand line : lines){
            List<Long> categoryIds = line.getCategoryList();
            if (null == categoryIds || categoryIds.size() == 0){
                // categoryIds为空
                continue;
            }
            if (categoryIds.contains(categoryId)){
                // categoryIds包含categoryId
                qtys.add(line.getQuantity());
            }
        }
        if (null != qtys && qtys.size() > 0){
            // 返回集合中最小值
            return Collections.min(qtys);
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMiniSKUQTYInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Set<Integer> qtys = new HashSet<Integer>();
        for (ShoppingCartLineCommand line : lines){
            Set<String> cids = line.getComboIds();
            if (null == cids || cids.size() == 0){
                continue;
            }
            if (cids.contains(String.valueOf(comboId))){
                // cids包含comboId
                qtys.add(line.getQuantity());
            }
        }
        if (null != qtys && qtys.size() > 0){
            // 返回集合中最小值
            return Collections.min(qtys);
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByAll(ShoppingCartCommand shopCart){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return BigDecimal.ZERO;
        return getNeedToPayAmountInShoppingCartByAll(shopCart.getShoppingCartLineCommands());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByAll(List<ShoppingCartLineCommand> lines){
        if (null == lines || lines.size() == 0)
            return BigDecimal.ZERO;

        BigDecimal totalAmt = BigDecimal.ZERO;// 总金额
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift()){
                continue;
            }
            totalAmt = totalAmt.add(line.getSalePrice().multiply(new BigDecimal(line.getQuantity())));
        }
        return totalAmt;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return BigDecimal.ZERO;
        return getNeedToPayAmountInShoppingCartByItemId(shopCart.getShoppingCartLineCommands(), itemId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByItemId(List<ShoppingCartLineCommand> lines,Long itemId){
        if (null == lines || lines.size() == 0)
            return BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;// 总金额
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift()){
                continue;
            }
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                totalAmt = totalAmt.add(line.getSalePrice().multiply(new BigDecimal(line.getQuantity())));
            }
        }
        return totalAmt;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return BigDecimal.ZERO;
        return getNeedToPayAmountInShoppingCartByCategoryId(shopCart.getShoppingCartLineCommands(), categoryId);
    }

    public BigDecimal getNeedToPayAmountInShoppingCartByCategoryId(List<ShoppingCartLineCommand> lines,Long categoryId){
        if (null == lines || lines.size() == 0)
            return BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;// 总金额
        for (ShoppingCartLineCommand line : lines){
            List<Long> categoryIds = line.getCategoryList();
            if (null == categoryIds || categoryIds.size() == 0 || line.isGift()){
                continue;
            }
            if (categoryIds.contains(categoryId)){
                // categoryIds包含categoryId
                totalAmt = totalAmt.add(line.getSalePrice().multiply(new BigDecimal(line.getQuantity())));
            }
        }
        return totalAmt;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getNeedToPayAmountInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return BigDecimal.ZERO;

        return getNeedToPayAmountInShoppingCartByComboId(shopCart.getShoppingCartLineCommands(), comboId);
    }

    public BigDecimal getNeedToPayAmountInShoppingCartByComboId(List<ShoppingCartLineCommand> lines,Long comboId){
        if (null == lines || lines.size() == 0)
            return BigDecimal.ZERO;

        BigDecimal totalAmt = BigDecimal.ZERO;// 总金额
        for (ShoppingCartLineCommand line : lines){
            Set<String> cids = line.getComboIds();
            if (null == cids || cids.size() == 0 || line.isGift()){
                continue;
            }
            if (cids.contains(String.valueOf(comboId))){
                // cids包含comboId
                totalAmt = totalAmt.add(line.getSalePrice().multiply(new BigDecimal(line.getQuantity())));
            }
        }
        return totalAmt;
    }

    @Override
    public Integer getQuantityInShoppingCartByAll(ShoppingCartCommand shopCart){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Integer totalQty = 0;// 总数量
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift()){
                continue;
            }
            totalQty += line.getQuantity();
        }
        return totalQty;
    }

    @Override
    public Integer getQuantityInShoppingCartByItemId(ShoppingCartCommand shopCart,Long itemId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Integer totalQty = 0;// 总数量
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift()){
                continue;
            }
            if (String.valueOf(line.getItemId()).equals(String.valueOf(itemId))){
                totalQty += line.getQuantity();
            }
        }
        return totalQty;
    }

    @Override
    public Integer getQuantityInShoppingCartByCategoryId(ShoppingCartCommand shopCart,Long categoryId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Integer totalQty = 0;// 总数量
        for (ShoppingCartLineCommand line : lines){
            List<Long> categoryIds = line.getCategoryList();
            if (null == categoryIds || categoryIds.size() == 0 || line.isGift()){
                continue;
            }
            if (categoryIds.contains(categoryId)){
                // categoryIds包含categoryId
                totalQty += line.getQuantity();
            }
        }
        return totalQty;
    }

    @Override
    public Integer getQuantityInShoppingCartByComboId(ShoppingCartCommand shopCart,Long comboId){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        Integer totalQty = 0;// 总数量
        for (ShoppingCartLineCommand line : lines){
            Set<String> cids = line.getComboIds();
            if (null == cids || cids.size() == 0 || line.isGift()){
                continue;
            }
            if (cids.contains(String.valueOf(comboId))){
                // cids包含comboId
                totalQty += line.getQuantity();
            }
        }
        return totalQty;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCouponTypeByCouponTypeID(long couponTypeId){
        return sdkPromotionCouponManager.findPromotionCouponCommandById(couponTypeId).getType();
    }

    private ItemFactor getItemInFactorList(List<ItemFactor> itemFactorList,long itemId){
        ItemFactor returnFactor = null;
        if (itemFactorList == null || itemFactorList.size() == 0)
            return null;
        for (ItemFactor factor : itemFactorList){
            if (factor.getItemId() == itemId)
                return factor;
        }
        return returnFactor;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTComboPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        Set<Long> itemIds = getItemIdsFromShoppingCartByComboId(lines, comboId);
        if (null == itemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : itemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer manualBuy(BigDecimal buyPrice,ShoppingCartLineCommand shoppingCartLine,List<ShoppingCartLineCommand> lines){
        try{
            Integer retval = SUCCESS;
            sdkEngineManager.packShoppingCartLine(shoppingCartLine);
            shoppingCartLine.setSalePrice(buyPrice);
            // 检查商品有效性
            retval = sdkEffectiveManager.checkItemIsValid(shoppingCartLine.isValid());
            if (SUCCESS != retval){
                return Constants.CHECK_VALID_FAILURE;
            }
            Integer qty = 0;
            boolean flag = false;// 如果操作的行不在购物列表中为false,否则为true
            for (ShoppingCartLineCommand line : lines){
                if (line.getExtentionCode().equals(shoppingCartLine.getExtentionCode())){
                    qty = line.getQuantity() + shoppingCartLine.getQuantity();
                    line.setQuantity(qty);
                    flag = true;
                }
            }

            // 如果操作的行不在购物车列表中
            if (!flag){
                qty = shoppingCartLine.getQuantity();
                lines.add(shoppingCartLine);
            }

            // 如果更改购物车行时，若为增加购买时才检查库存

            Boolean res = sdkEffectiveManager.chckInventory(shoppingCartLine.getExtentionCode(), qty);
            if (!res){
                return Constants.CHECK_INVENTORY_FAILURE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartCommand findManualShoppingCart(List<ShoppingCartLineCommand> shoppingCartLines){
        if (null == shoppingCartLines || shoppingCartLines.size() == 0)
            return null;
        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
        // 被选中的购物车行
        List<ShoppingCartLineCommand> chooseLines = new ArrayList<ShoppingCartLineCommand>();
        List<ShoppingCartLineCommand> notChooseLines = new ArrayList<ShoppingCartLineCommand>();

        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartLines){
            // 封装购物车行信息
            // sdkEngineManager.packShoppingCartLine(shoppingCartLine);
            shoppingCartLine.setType(Constants.ITEM_TYPE_SALE);
            // 购物车行 金额小计
            shoppingCartLine.setSubTotalAmt(new BigDecimal(shoppingCartLine.getQuantity()).multiply(shoppingCartLine.getSalePrice()));
            // 判断是否是被选中的购物车行

        }

        splitByCalcLevel(shoppingCartLines, chooseLines, notChooseLines);

        // 封装购物车的基本信息
        packShopBaseInfo(shoppingCart, null, null, chooseLines);

        // 所有购物车行数据
        shoppingCart.setShoppingCartLineCommands(shoppingCartLines);

        // 根据店铺封装购物车对象
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = getShopCartByShopId(shoppingCart);

        // 设置分店铺的购物车
        shoppingCart.setShoppingCartByShopIdMap(shopCartByShopIdMap);

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // 区分店铺的购物车
        Map<Long, ShoppingCartCommand> shopCartMap = shoppingCart.getShoppingCartByShopIdMap();

        List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<ShopCartCommandByShop>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
            ShopCartCommandByShop cartByShop = new ShopCartCommandByShop();
            Long shopId = entry.getKey();
            ShoppingCartCommand cart = entry.getValue();
            List<ShoppingCartLineCommand> lines = cart.getShoppingCartLineCommands();
            // 有效的购物车行
            chooseLines = new ArrayList<ShoppingCartLineCommand>();
            notChooseLines = new ArrayList<ShoppingCartLineCommand>();

            splitByCalcLevel(lines, chooseLines, notChooseLines);

            BigDecimal originShippingFee = BigDecimal.ZERO;
            // if (null != calcFreightCommand) {
            // originShippingFee = getFreightFee(calcFreightCommand,
            // chooseLines, shopId);
            // }

            // 应付运费
            cartByShop.setOriginShoppingFee(originShippingFee);// ////////一定要传参哦

            // 应付小计
            cartByShop.setSubtotalCurrentPayAmount(getOriginPayAmount(chooseLines));

            // 应付合计(应付小计+应付运费)
            BigDecimal sumCurrentPayAmt = originShippingFee.add(cartByShop.getSubtotalCurrentPayAmount());
            cartByShop.setSumCurrentPayAmount(sumCurrentPayAmt);

            // 实付合计
            cartByShop.setRealPayAmount(sumCurrentPayAmt);

            // 应付金额
            cart.setOriginPayAmount(cartByShop.getSumCurrentPayAmount());

            // 实付金额
            cart.setCurrentPayAmount(cartByShop.getRealPayAmount());

            // 该店铺的商品数量
            cartByShop.setQty(getOrderQuantity(chooseLines));

            cartByShop.setShopId(shopId);
            summaryShopCartList.add(cartByShop);
        }

        shoppingCart.setSummaryShopCartList(summaryShopCartList);

        // 购物车促销简介信息
        shoppingCart.setCartPromotionBriefList(null);

        // 所有购物车行(可能包含了礼品\有效、无效)
        shoppingCart.setShoppingCartLineCommands(getAllShoppingCartLines(shopCartMap));

        // 获取应付金额
        BigDecimal originPayAmount = getOriginPayAmount(getNoGiftShoppingCartLines(shopCartMap));

        // 设置应付金额
        shoppingCart.setOriginPayAmount(originPayAmount);

        // 实际支付金额
        BigDecimal realPayAmount = getRealPayAmountByShopCart(summaryShopCartList);

        // 设置实付金额
        shoppingCart.setCurrentPayAmount(realPayAmount);

        // 应付运费
        shoppingCart.setOriginShoppingFee(getOrginShippingAmountByShopCart(summaryShopCartList));

        // 实付运费
        shoppingCart.setCurrentShippingFee(getCurrentShippingAmountByShopCart(summaryShopCartList, shoppingCart.getOriginShoppingFee()));

        return shoppingCart;
    }

    /**
     * 更新购物车礼品行的信息
     * 
     */
    @Override
    public void updateShoppingCartlineGift(Long[] skuIds,String[] lineGroups,Long memberId,Long promotionId){

        for (int i = 0; i < lineGroups.length; i++){
            String lineGroup = lineGroups[i];
            List<ShoppingCartLineCommand> shoppingCartLines = sdkShoppingCartLineDao
                            .findShopCartLineByLineGroupAndSkuId(memberId, null, lineGroup);
            /** 先删除现有行 */
            if (null != shoppingCartLines && shoppingCartLines.size() > 0){
                sdkShoppingCartLineDao.deleteGiftLineByMemberIdAndPromotionId(memberId, promotionId);
            }
        }

        for (int i = 0; i < skuIds.length; i++){
            Long skuId = skuIds[i];
            String lineGroup = lineGroups[i];
            ShoppingCartLineCommand command = new ShoppingCartLineCommand();
            command.setSkuId(skuId);
            command.setMemberId(memberId);
            command.setQuantity(1);
            command.setGift(true);
            command.setPromotionId(promotionId);
            sdkEngineManager.packShoppingCartLine(command);
            /** 添加赠品行 */
            sdkShoppingCartLineDao.insertShoppingCartLineWithLineGroup(
                            command.getExtentionCode(),
                            skuId,
                            1,
                            memberId,
                            new Date(),
                            CHECKED_STATE,
                            command.getShopId(),
                            lineGroup,
                            true,
                            command.getPromotionId());
        }
    }

    @Override
    public Integer removeShoppingCartGiftByIdAndMemberId(Long lineId,Long memberId){
        List<Long> ids = new ArrayList<Long>();
        ids.add(lineId);
        List<ShoppingCartLineCommand> shoppingCartLineList = sdkShoppingCartLineDao.findShopCartLineByIds(ids);
        if (null != shoppingCartLineList && shoppingCartLineList.size() > 0){
            sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, lineId);
            return Constants.SUCCESS;
        }else{
            return Constants.FAILURE;
        }
    }

    @Override
    public Integer removeShoppingCartLineByIdAndMemberId(Long lineId,Long memberId){
        return sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, lineId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, List<ShoppingCartLineCommand>> checkGiftEffective(
                    List<ShoppingCartLineCommand> lines,
                    Set<String> memComIds,
                    Long memberId,
                    List<String> coupons,
                    CalcFreightCommand calcFreightCommand){
        ShoppingCartCommand shoppingCartCommand = getShoppingCart(lines, coupons, memberId, calcFreightCommand, memComIds);
        if (null == shoppingCartCommand){
            return null;
        }
        // 获取所有的直推赠品行
        List<ShoppingCartLineCommand> allShoppingCartLineList = new ArrayList<ShoppingCartLineCommand>();
        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()){
            ShoppingCartCommand shoppingCartCommandEntry = entry.getValue();
            if (null == shoppingCartCommandEntry){
                continue;
            }
            allShoppingCartLineList.addAll(shoppingCartCommandEntry.getShoppingCartLineCommands());
        }

        // 获取限购规则
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(getShopIds(lines), memComIds, getItemComboIds(lines), new Date());

        Map<Integer, List<ShoppingCartLineCommand>> ForceSendGiftMap = sdkShoppingCartLinesManager
                        .getShoppingCartForceSendGiftLines(allShoppingCartLineList, purchaseLimitationList, shoppingCartCommand);
        return ForceSendGiftMap;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkSelectedGiftLimit(
                    Long[] currentSkuIds,
                    String[] lineGroups,
                    Long promotionId,
                    List<ShoppingCartLineCommand> beforeGiftLines){

        // 1, 通过promotionId查询该活动,查看活动中最多可以选中多少个赠品
        PromotionCommand promotionCommand = sdkPromotionGuideManager.getPromotionById(promotionId);
        Integer Limit = 0;
        for (AtomicSetting atomicSetting : promotionCommand.getAtomicSettingList()){
            if (GiftChoiceType.NeedChoice.equals(atomicSetting.getGiftChoiceType())){
                Limit = atomicSetting.getGiftCountLimited();
                break;
            }
        }

        // 2, skuIds.length <= giftCount
        //用户选中的赠品skuID集合
        Set<Long> selectedGiftSkuIdSet = new HashSet<Long>();
        if (null != currentSkuIds){
            selectedGiftSkuIdSet.addAll(Arrays.asList(currentSkuIds));
        }
        for (ShoppingCartLineCommand giftLine : beforeGiftLines){
            if (giftLine.isGift() && Constants.CHECKED_CHOOSE_STATE.equals(giftLine.getSettlementState())
                            && promotionId.equals(giftLine.getPromotionId())){
                selectedGiftSkuIdSet.add(giftLine.getSkuId());
            }
        }
        if (selectedGiftSkuIdSet.size() <= Limit){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> findShopCartGiftLineByMemberId(Long memberId){
        return sdkShoppingCartLineDao.findShopCartGiftLineByMemberId(memberId, Constants.CHECKED_CHOOSE_STATE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : customItemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : customItemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerItemByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discAmount,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : customItemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByAMT(
                            lines,
                            itemId,
                            discAmount,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getSinglePrdDiscountAMTCustomPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    long customId,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    Integer factorDefault,
                    List<ItemFactor> itemFactorList,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (Long itemId : customItemIds){
            List<PromotionSKUDiscAMTBySetting> settingOne = getSinglePrdDiscountAMTItemPerPCSByRate(
                            lines,
                            itemId,
                            discRate,
                            onePieceMark,
                            factorDefault,
                            itemFactorList,
                            briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartLineCommand> getHistoryOrderQtyByCustomId(ShoppingCartCommand shopCart,long customId,Integer qtyLimited){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return null;
        Integer orderCount = 0;
        Long shopId = 0L;
        boolean flag = true;
        List<ShoppingCartLineCommand> lines = shopCart.getShoppingCartLineCommands();
        List<ShoppingCartLineCommand> errorLineList = new ArrayList<ShoppingCartLineCommand>();
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;
        List<Long> itemIdSet = new ArrayList<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(line.getType())){
                continue;
            }
            if (flag){
                flag = false;
                shopId = line.getShopId();
            }
            if (customItemIds.contains(line.getItemId())){
                orderCount = 1;
                itemIdSet.add(line.getItemId());
                errorLineList.add(line);
                break;
            }
        }
        // 根据itemId查询历史订单次数
        Integer histroyQty = sdkOrderLineDao.findHistoryOrderCountByItemIds(itemIdSet, shopId, getMemberId(shopCart));
        if (null != histroyQty){
            orderCount += histroyQty;
            if (orderCount > qtyLimited)
                return errorLineList;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCustomAmount(List<Long> customItemIds,List<ShoppingCartLineCommand> shoppingLines){
        // TODO Auto-generated method stub
        return getNeedToPayAmountInShoppingCartByCustomItemIds(shoppingLines, customItemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCustomQuantity(List<Long> customItemIds,List<ShoppingCartLineCommand> shoppingLines){
        // TODO Auto-generated method stub
        return getQuantityInShoppingCartLinesByCustomItemIds(shoppingLines, customItemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkCouponByCustomItemIds(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> itemIdList,
                    long couponTypeID,
                    List<PromotionCouponCodeCommand> couponCodes,
                    long shopId){
        if (null == couponCodes || couponCodes.size() == 0 || null == shoppingCartLines || shoppingCartLines.size() == 0)
            return false;
        // TODO check coupon on line directly
        // 行Coupon优先，coupon在行上
        boolean flagOnLine = checkOnLineCouponByCustomItemIds(shoppingCartLines, itemIdList, couponTypeID, shopId);
        if (flagOnLine)
            return flagOnLine;
        // 判断购物车行是否有Item Id List分类下的商品
        boolean flag = checkCustomItemIdsInShoppingCartLines(shoppingCartLines, itemIdList);
        if (!flag)
            return flag;
        // 检查coupon是否符合条件
        return checkCouponWithCouponTypeIdShopId(couponCodes, couponTypeID, shopId);
    }

    private boolean checkCustomItemIdsInShoppingCartLines(List<ShoppingCartLineCommand> lines,List<Long> itemIdList){
        if (null == itemIdList || itemIdList.size() == 0)
            return false;
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isGift())
                continue;
            // 检查购物车中是否包含该itemIdList下的商品
            if (itemIdList.contains(line.getItemId()))
                return true;
        }
        return false;
    }

    private boolean checkOnLineCouponByCustomItemIds(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> itemIdList,
                    long couponTypeID,
                    long shopId){
        if (null == itemIdList || itemIdList.size() == 0)
            return false;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (itemIdList.contains(line.getItemId())){
                PromotionCouponCodeCommand couponOnLine = line.getCouponCodeOnLine();
                if (couponOnLine != null && couponOnLine.getCouponId() == couponTypeID && couponOnLine.getShopId() == shopId)
                    return true;
            }
        }
        return false;
    }

    @Override
    public BigDecimal getNeedToPayAmountInShoppingCartByCustomItemIds(List<ShoppingCartLineCommand> lines,List<Long> customItemIdList){
        if (null == lines || lines.size() == 0)
            return BigDecimal.ZERO;
        if (null == customItemIdList || customItemIdList.size() == 0)
            return BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;// 总金额
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isGift())
                continue;
            if (customItemIdList.contains(line.getItemId())){
                // customItemIds包含item id
                totalAmt = totalAmt.add(line.getSalePrice().multiply(new BigDecimal(line.getQuantity())));
            }
        }
        return totalAmt;
    }

    @Override
    public Integer getQuantityInShoppingCartByCustomItemIds(ShoppingCartCommand shopCart,List<Long> customItemIds){
        if (null == shopCart || null == shopCart.getShoppingCartLineCommands() || shopCart.getShoppingCartLineCommands().size() == 0)
            return 0;
        return getQuantityInShoppingCartLinesByCustomItemIds(shopCart.getShoppingCartLineCommands(), customItemIds);
    }

    private Integer getQuantityInShoppingCartLinesByCustomItemIds(List<ShoppingCartLineCommand> lines,List<Long> customItemIds){
        if (null == lines || lines.size() == 0)
            return 0;
        Integer totalQty = 0;// 总数量
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId()){
                continue;
            }
            if (customItemIds.contains(line.getItemId())){
                // customItemIds包含item id
                totalQty += line.getQuantity();
            }
        }
        return totalQty;
    }

    /*
     * 按商品Id获取购物车中的Markdown Price
     * 购物车中的到SKU，Markdown Price也细化到SKU
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartManager#getMarkdownPriceByItemID(java.util.List, long, java.lang.Integer,
     * java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByItemID(
                    List<ShoppingCartLineCommand> lines,
                    long itemId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious){
        List<PromotionMarkdownPrice> markdownPriceList = sdkPromotionManager.getPromotionMarkdownPriceListByItemId(itemId);

        return generatePromotionSKUDiscAMTBySettingByMarkdownPrice(lines, markdownPriceList, briefListPrevious);
    }

    /*
     * 按CategoryId获取购物车中的Markdown Price
     * 购物车中的SKU，Markdown Price也细化到SKU
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartManager#getMarkdownPriceByCategoryID(java.util.List, long, java.lang.Integer,
     * java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByCategoryID(
                    List<ShoppingCartLineCommand> lines,
                    long categoryId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious){
        List<Long> itemIdList = new ArrayList<Long>();

        itemIdList = getItemIdListFromShoppingCartLinesByCategory(lines, categoryId);

        List<PromotionMarkdownPrice> markdownPriceList = sdkPromotionManager.getPromotionMarkdownPriceListByItemIdList(itemIdList);

        return generatePromotionSKUDiscAMTBySettingByMarkdownPrice(lines, markdownPriceList, briefListPrevious);
    }

    /*
     * 按Combo Id获取购物车中的Markdown Price
     * 购物车中的SKU，Markdown Price也细化到SKU
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartManager#getMarkdownPriceByComboID(java.util.List, long, java.lang.Integer,
     * java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByComboID(
                    List<ShoppingCartLineCommand> lines,
                    long comboId,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious){
        List<Long> itemIdList = new ArrayList<Long>();

        itemIdList = getItemIdListFromShoppingCartLinesByCombo(lines, comboId);

        List<PromotionMarkdownPrice> markdownPriceList = sdkPromotionManager.getPromotionMarkdownPriceListByItemIdList(itemIdList);

        return generatePromotionSKUDiscAMTBySettingByMarkdownPrice(lines, markdownPriceList, briefListPrevious);
    }

    /*
     * 按照客户自定义商品范围中的ItemList获取购物车中的Markdown Price
     * 购物车中的SKU，Markdown Price也细化到SKU
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartManager#getMarkdownPriceByCustomItemIds(java.util.List, java.util.List,
     * java.lang.Integer, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionSKUDiscAMTBySetting> getMarkdownPriceByCustomItemIds(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIds,
                    Integer factorMultiplication,
                    List<PromotionBrief> briefListPrevious){
        List<Long> itemIdListAllLines = new ArrayList<Long>();
        itemIdListAllLines = getItemIdListFromShoppingCartLinesByCall(lines);
        customItemIds.retainAll(itemIdListAllLines);
        List<PromotionMarkdownPrice> markdownPriceList = sdkPromotionManager.getPromotionMarkdownPriceListByItemIdList(customItemIds);

        return generatePromotionSKUDiscAMTBySettingByMarkdownPrice(lines, markdownPriceList, briefListPrevious);
    }

    /**
     * 根据Markdownprice生成优惠设置
     * 
     * @param lines
     * @param markdownPriceList
     * @return
     */
    private List<PromotionSKUDiscAMTBySetting> generatePromotionSKUDiscAMTBySettingByMarkdownPrice(
                    List<ShoppingCartLineCommand> lines,
                    List<PromotionMarkdownPrice> markdownPriceList,
                    List<PromotionBrief> briefListPrevious){
        if (null == markdownPriceList)
            return null;
        BigDecimal lineCurrentDiscAMT = BigDecimal.ZERO;
        PromotionSKUDiscAMTBySetting setting = null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (PromotionMarkdownPrice markdown : markdownPriceList){
            for (ShoppingCartLineCommand line : lines){
                if (line.getShopId().equals(markdown.getShopId()) && line.getItemId().equals(markdown.getItemId())
                                && line.getSkuId().equals(markdown.getSkuId())){
                    // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                    BigDecimal skuPreviousDiscAMT = sdkPromotionSettingManager
                                    .getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    BigDecimal lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());

                    if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                        continue;
                    lineCurrentDiscAMT = BigDecimal.ZERO;

                    BigDecimal lineCurrentNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);
                    if (lineCurrentNeedPay.compareTo(BigDecimal.ZERO) <= 0)
                        continue;

                    lineCurrentDiscAMT = lineCurrentNeedPay
                                    .subtract(BigDecimal.valueOf(line.getQuantity()).multiply(markdown.getMarkDownPrice()));
                    if (lineCurrentDiscAMT.compareTo(BigDecimal.ZERO) <= 0)
                        continue;

                    setting = getPromotionSkuAMTSetting(line, lineCurrentDiscAMT);
                    setting.setMarkdownPrice(markdown.getMarkDownPrice());
                    settingList.add(setting);
                }
            }
        }

        return settingList;
    }

    /**
     * 获取当前购车中，属于CategoryId下的商品Id List
     * 
     * @param lines
     * @param categoryId
     * @return
     */
    private List<Long> getItemIdListFromShoppingCartLinesByCategory(List<ShoppingCartLineCommand> lines,Long categoryId){

        List<Long> itemIdList = new ArrayList<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift() || line.isCaptionLine())
                continue;
            if (line.getCategoryList().contains(categoryId) == true){
                if (itemIdList.contains(line.getItemId()) == false)
                    itemIdList.add(line.getItemId());
            }
        }
        return itemIdList;
    }

    /**
     * 获取当前购物车中，属于该组合的商品Id List
     * 
     * @param lines
     * @param comboId
     * @return
     */
    private List<Long> getItemIdListFromShoppingCartLinesByCombo(List<ShoppingCartLineCommand> lines,Long comboId){
        List<Long> itemIdList = new ArrayList<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift() || line.isCaptionLine())
                continue;
            if (line.getComboIds().contains(comboId.toString()) == true){
                if (itemIdList.contains(line.getItemId()) == false)
                    itemIdList.add(line.getItemId());
            }
        }
        return itemIdList;
    }

    /**
     * 获取购物车中所有商品Id List
     * 
     * @param lines
     * @return
     */
    private List<Long> getItemIdListFromShoppingCartLinesByCall(List<ShoppingCartLineCommand> lines){
        List<Long> itemIdList = new ArrayList<Long>();
        for (ShoppingCartLineCommand line : lines){
            if (line.isGift() || line.isCaptionLine())
                continue;

            if (itemIdList.contains(line.getItemId()) == false)
                itemIdList.add(line.getItemId());
        }
        return itemIdList;
    }
}
