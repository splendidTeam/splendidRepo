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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
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

    /** The Constant LOGGER. */
    private static final Logger                      LOGGER        = LoggerFactory.getLogger(SdkShoppingCartCommandBuilderImpl.class);

    @Autowired
    private SdkShoppingCartLinePackManager           sdkShoppingCartLinePackManager;

    /** The Constant CHECKED_STATE. */
    private static final int                         CHECKED_STATE = 1;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao                   sdkShoppingCartLineDao;

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
        List<ShoppingCartLineCommand> chooseLines = new ArrayList<ShoppingCartLineCommand>();// 被选中的购物车行
        List<ShoppingCartLineCommand> notChooseLines = new ArrayList<ShoppingCartLineCommand>();// 未选中的购物车行
        // 判断是否是被选中的购物车行
        splitByCalcLevel(shoppingCartLines, chooseLines, notChooseLines);

        //*************************************************************************************************
        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();// 购物车对象

        // 封装购物车的基本信息
        packShoppingCartCommandBaseInfo(shoppingCartCommand, buildUserDetails(memberId, memberComIds), coupons, chooseLines);

        // 设置分店铺的购物车
        Map<Long, ShoppingCartCommand> buildShopCartByShopIdMap = buildShopCartByShopIdMap(shoppingCartCommand);
        shoppingCartCommand.setShoppingCartByShopIdMap(buildShopCartByShopIdMap);

        // 获取购物车促销信息
        getShopCartPromotionInfos(shoppingCartCommand, calcFreightCommand);

        //***************************************************************************************

        // 因为某些NOTCHOOSE的购物车行不进行促销计算,如果全是无效数据,shopCartByShopIdMap中的值为空,所以这里帮助进行初始化shopCartByShopIdMap的数据
        for (ShoppingCartLineCommand sclc : notChooseLines){
            Long shopId = sclc.getShopId();
            ShoppingCartCommand scc = buildShopCartByShopIdMap.get(shopId);
            if (scc == null){
                scc = new ShoppingCartCommand();
                List<ShoppingCartLineCommand> sclcList = new ArrayList<ShoppingCartLineCommand>();

                scc.setShoppingCartLineCommands(sclcList);
                buildShopCartByShopIdMap.put(shopId, scc);

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
            Long id = entry.getKey();
            Long shopId = entry.getValue();

            // 循环所有原始记录行 开始
            for (ShoppingCartLineCommand chooseLine : shoppingCartCommand.getShoppingCartLineCommands()){
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
            //****************************************************************************************

            // 循环添加门店所有行
            for (Entry<Long, ShoppingCartCommand> shopCommand : buildShopCartByShopIdMap.entrySet()){
                Long shopIdKey = shopCommand.getKey();// 店铺ID
                List<ShoppingCartLineCommand> shopShoppingCartLineList = shopCommand.getValue().getShoppingCartLineCommands();// 店铺的商品行数据

                if (!shopId.equals(shopIdKey)){
                    continue;
                }

                ShoppingCartLineCommand tempShopLine = null;

                for (ShoppingCartLineCommand shopLine : shopShoppingCartLineList){
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
        for (Entry<Long, ShoppingCartCommand> shopCommand : buildShopCartByShopIdMap.entrySet()){
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
                buildShopCartByShopIdMap.get(shopShopLine.getKey()).setShoppingCartLineCommands(shopShopLine.getValue());
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
        // 设置memcombo
        UserDetails userDetails = new UserDetails();
        userDetails.setMemberId(memberId);
        if (null != memberId){
            userDetails.setMemComboList(memberComIds);
        }else{
            userDetails.setMemComboList(getMemboIds());
        }
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
        else{
            return true;
        }
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
    private void packShoppingCartCommandBaseInfo(
                    ShoppingCartCommand shoppingCartCommand,
                    UserDetails userDetails,
                    List<String> coupons,
                    List<ShoppingCartLineCommand> validedLines){

        // 设置应付金额
        shoppingCartCommand.setOriginPayAmount(getOriginPayAmount(validedLines));

        // 设置当前时间
        if (null == shoppingCartCommand.getCurrentTime()){
            shoppingCartCommand.setCurrentTime(new Date());
        }

        // 购物车行信息
        shoppingCartCommand.setShoppingCartLineCommands(validedLines);

        // 商品数量
        shoppingCartCommand.setOrderQuantity(getOrderQuantity(validedLines));

        // 优惠券编码
        shoppingCartCommand.setCoupons(coupons);
        // 会员信息
        shoppingCartCommand.setUserDetails(userDetails);
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
            if (cartLine.isGift() || cartLine.isCaptionLine())
                continue;
            originPayAmount = originPayAmount.add(NumberUtil.getMultiplyValue(cartLine.getQuantity(), cartLine.getSalePrice()));
        }
        return originPayAmount = originPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取购物车的促销信息.
     *
     * @param shoppingCart
     *            the shopping cart
     * @param calcFreightCommand
     *            the calc freight command
     */
    private void getShopCartPromotionInfos(ShoppingCartCommand shoppingCart,CalcFreightCommand calcFreightCommand){
        // 获取促销数据.需要调用促销引擎计算优惠价格
        List<PromotionBrief> promotionBriefList = calcuPromoBriefs(shoppingCart);

        Map<Long, ArrayList<PromotionBrief>> proBriefsMap = new HashMap<Long, ArrayList<PromotionBrief>>();

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
     * 执行购物车更新.
     *
     * @param needDelLineList
     *            the need del line list
     * @param needAdd
     *            the need add
     * @param needUpdate
     *            the need update
     * @param memberId
     *            the member id
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
     * 根据店铺封装shopCart对象.
     *
     * @param shoppingCartCommand
     *            the shop cart
     * @return the shop cart by shop id
     */
    private Map<Long, ShoppingCartCommand> buildShopCartByShopIdMap(ShoppingCartCommand shoppingCartCommand){
        // 获取区分了店铺的购物车对象
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = getShoppingCartMapByShop(shoppingCartCommand.getShoppingCartLineCommands());
        for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartByShopIdMap.entrySet()){
            ShoppingCartCommand entryShopCart = entry.getValue();
            entryShopCart.setCurrentTime(shoppingCartCommand.getCurrentTime());
            List<ShoppingCartLineCommand> lines = entryShopCart.getShoppingCartLineCommands();
            // 封装购物车信息
            packShoppingCartCommandBaseInfo(entryShopCart, shoppingCartCommand.getUserDetails(), shoppingCartCommand.getCoupons(), lines);
        }
        return shopCartByShopIdMap;
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
     * 封装礼品购物车行.
     *
     * @param proSku
     *            the pro sku
     * @return the gift shopping cart line command
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
        sdkShoppingCartLinePackManager.packShoppingCartLine(giftLine);

        giftLine.setPromotionId(proSku.getPromotionId());
        giftLine.setSettingId(proSku.getSettingId());
        giftLine.setGiftChoiceType(proSku.getGiftChoiceType());
        giftLine.setGiftCountLimited(proSku.getGiftCountLimited());

        return giftLine;
    }

    /**
     * 比较两个字符串.
     *
     * @param str1
     *            the str1
     * @param str2
     *            the str2
     * @return true, if compare string
     */
    private boolean compareString(String str1,String str2){
        return (null == str1 && null == str2) || (null != str1 && str1.equals(str2));
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
                ShoppingCartCommand cart = entry.getValue();
                allLines.addAll(cart.getShoppingCartLineCommands());
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
     * 无促销情况下计算运费.
     *
     * @param calcFreightCommand
     *            the calc freight command
     * @param validLines
     *            the valid lines
     * @param shopId
     *            the shop id
     * @return the freight fee
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
     * 用于计算获取促销数据.
     *
     * @param shopCart
     *            the shop cart
     * @return the list< promotion brief>
     */
    private List<PromotionBrief> calcuPromoBriefs(ShoppingCartCommand shopCart){
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
     * 将购物车行进行区分店铺.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the shopping cart map by shop
     */
    private Map<Long, ShoppingCartCommand> getShoppingCartMapByShop(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Map<Long, ShoppingCartCommand> shopCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();
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
        return shopCartByShopIdMap;
    }

    /**
     * 获取购物车中的所有店铺id.
     *
     * @param lines
     *            the lines
     * @return the shop ids
     */
    private List<Long> getShopIds(List<ShoppingCartLineCommand> lines){
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
     * 根据购物车行获取ItemForCheckCommand集合.
     *
     * @param lines
     *            the lines
     * @return the item combo ids
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
}
