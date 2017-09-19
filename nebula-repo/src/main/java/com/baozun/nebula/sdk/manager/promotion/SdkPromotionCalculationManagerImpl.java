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
package com.baozun.nebula.sdk.manager.promotion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.calculateEngine.param.ConditionMasterType;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.calculateEngine.param.PromotionConstants;
import com.baozun.nebula.calculateEngine.param.PromotionExclusiveGroupType;
import com.baozun.nebula.calculateEngine.param.SettingType;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.promotion.PromotionCoupon;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkPriorityAdjustManager;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeSettingLoader;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;
import com.feilong.tools.jsonlib.JsonUtil;

@Transactional
@Service("sdkPromotionCalculationManager")
public class SdkPromotionCalculationManagerImpl implements SdkPromotionCalculationManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkPromotionCalculationManagerImpl.class);

    @Autowired
    private SdkShoppingCartManager shoppingCartmanager;

    @Autowired
    private SdkPromotionCalculationConditionManager sdkPromotionConditionManager;

    @Autowired
    private SdkPromotionCalculationSettingManager sdkPromotionSettingManager;

    @Autowired
    SdkPriorityAdjustManager sdkPriorityAdjustManager;

    @Autowired
    SdkPromotionGuideManager sdkPromotionGuideManager;

    /**
     * 获取选购套餐
     * 
     * @param allPromotionList
     * @return
     */
    public List<PromotionCommand> getSuitPromotionsFromAllofOneShop(List<PromotionCommand> allPromotionList){
        List<PromotionCommand> allSuitPromotionList = new ArrayList<PromotionCommand>();
        if (allPromotionList == null || allPromotionList.size() == 0)
            return null;
        for (PromotionCommand promotionOne : allPromotionList){
            if (promotionOne.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_CHOICE)){
                allSuitPromotionList.add(promotionOne);
            }
        }
        return allSuitPromotionList;
    }

    /**
     * 构造套餐购物车
     * 
     * @param lines
     * @param userDetails
     * @return
     */
    public ShoppingCartCommand constructSuitShoppingCartFromSuitLines(List<ShoppingCartLineCommand> lines,UserDetails userDetails){
        ShoppingCartCommand suitShopCart = new ShoppingCartCommand();
        ShoppingCartLineCommand lineTmp = new ShoppingCartLineCommand();
        List<ShoppingCartLineCommand> shoppingCartLineCommands = new ArrayList<ShoppingCartLineCommand>();
        suitShopCart.setUserDetails(userDetails);
        for (ShoppingCartLineCommand line : lines){
            BeanUtils.copyProperties(line, lineTmp);
            shoppingCartLineCommands.add(lineTmp);
        }
        suitShopCart.setShoppingCartLineCommands(shoppingCartLineCommands);
        return suitShopCart;
    }

    /**
     * 
     * @param shopCart
     * @param suitPromotion
     * @return
     */
    public PromotionBrief calculationPromotionSuit(List<ShoppingCartLineCommand> oneSuitLines,PromotionCommand suitPromotion,UserDetails userDetails,List<PromotionBrief> briefListPrevious){
        PromotionBrief briefOnePromotion = null;
        //为套餐构造购物车对象
        ShoppingCartCommand suitShopCart = new ShoppingCartCommand();
        suitShopCart = constructSuitShoppingCartFromSuitLines(oneSuitLines, userDetails);

        //计算套餐优惠设置

        // 主商品，选购商品prmprd： scpprdpcs(1,pid:21,1) | scpprdpcs(1，cmb:4,1)
        // ，addtprd：
        // ChoiceMark:prmprd,addtprd.逻辑关系式：(至少一个prmprd，多个是或的关系) &&
        // (至少一个addtprd，多个是或的关系)
        List<AtomicCondition> choiceConditionList = sdkPromotionConditionManager.convertComplexConditionToAtomic(suitPromotion.getConditionComplexList());
        suitPromotion.setAtomicComplexConditionList(choiceConditionList);

        Integer conditionResultFactor = sdkPromotionConditionManager.checkChoiceByAtomicComplexConditionList(suitShopCart, choiceConditionList, suitPromotion.getShopId(), briefListPrevious);

        if (conditionResultFactor > CHECKFAILURE){
            String conditionExpressionComplex = "";

            for (ConditionComplexCommand complex : suitPromotion.getConditionComplexList()){
                if (complex.getConditionExpress() != null){
                    if (conditionExpressionComplex == null || conditionExpressionComplex.isEmpty())
                        conditionExpressionComplex = complex.getConditionExpress();
                    else
                        conditionExpressionComplex = conditionExpressionComplex + "并且" + complex.getConditionExpress();
                }
            }
            // 满足条件，获取当前优惠活动设置。要放到启用时加载
            List<AtomicSetting> choiceSettingList = sdkPromotionSettingManager.convertComplexSettingToAtomic(suitPromotion.getSettingComplexList());
            suitPromotion.setAtomicComplexSettingList(choiceSettingList);

            choiceSettingList = suitPromotion.getAtomicComplexSettingList();
            briefOnePromotion = calculationPromotionByAtomicSetting(suitShopCart, suitPromotion, choiceSettingList, conditionResultFactor, briefListPrevious);
            briefOnePromotion.setConditionExpressionComplex(conditionExpressionComplex);
        }else{
            LOGGER.debug("活动编号：" + suitPromotion.getPromotionId().toString() + "，名称:" + suitPromotion.getPromotionName());
            LOGGER.debug("---条件表达式：" + suitPromotion.getConditionExpression());
            LOGGER.debug("---条件表达式状态：不满足!");
        }
        return briefOnePromotion;
    }

    /**
     * 获取套餐行
     * 
     * @param lineList
     * @param suitPrm
     * @return
     */
    public List<ShoppingCartLineCommand> getSuitLinesFromPromotion(List<ShoppingCartLineCommand> lineList,PromotionCommand suitPrm){
        List<ShoppingCartLineCommand> listResult = new ArrayList<ShoppingCartLineCommand>();
        for (ShoppingCartLineCommand line : lineList){
            if (line.isGift() == false && line.getPromotionList().equals(suitPrm.getPromotionId()))
                listResult.add(line);
        }
        return listResult;
    }

    private void logPromotionPriority(List<PromotionCommand> allPromotionList){
        if (null == allPromotionList || allPromotionList.size() < 0)
            return;
        for (PromotionCommand one : allPromotionList){
            LOGGER.debug("活动编号：{},优先级：{}", one.getPromotionId(), one.getPriority());
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.formatWithIncludes(allPromotionList, "promotionId", "priority"));
        }

        LOGGER.debug("结束！");
    }

    /**
     * 检查历史活动中，是否有N选一中的一个是否已经存在
     * 存在，组内后序不参与计算。也不影响后序的Normal序列
     */
    private boolean checkExistsInPreviousByShareExclusiveGroup(List<PromotionBrief> briefListPrevious,PromotionCommand promotion){
        if (null == briefListPrevious)
            return false;
        if (null == promotion.getGroupName() || promotion.getGroupName().isEmpty())
            return false;
        PromotionCommand onePromotionInGroup = null;

        for (PromotionBrief oneBrief : briefListPrevious){
            onePromotionInGroup = sdkPromotionGuideManager.getPromotionById(oneBrief.getPromotionId());

            if (null == onePromotionInGroup.getGroupName() || onePromotionInGroup.getGroupName().isEmpty())
                continue;
            if (promotion.getGroupType().equals(PromotionExclusiveGroupType.SHARE) && promotion.getGroupType().equals(onePromotionInGroup.getGroupType()) && promotion.getGroupName().trim().equalsIgnoreCase(onePromotionInGroup.getGroupName().trim())){
                return true;
            }
        }

        return false;
    }

    private boolean checkExistsInPreviousByShareExclusiveGroup(List<PromotionBrief> briefListPrevious){
        if (null == briefListPrevious)
            return false;

        PromotionCommand onePromotionInGroup = null;

        for (PromotionBrief oneBrief : briefListPrevious){
            onePromotionInGroup = sdkPromotionGuideManager.getPromotionById(oneBrief.getPromotionId());

            if (null == onePromotionInGroup.getGroupName() || onePromotionInGroup.getGroupName().isEmpty())
                continue;
            if (onePromotionInGroup.getGroupType().equals(PromotionExclusiveGroupType.SHARE)){
                return true;
            }
        }

        return false;
    }

    /**
     * 检查历史活动中，是否有N选一中的一个是否已经存在
     * 存在，组内后序不参与计算。也不影响后序的Normal序列
     */
    private boolean checkExistsInPreviousBySingleExclusiveGroup(List<PromotionBrief> briefListPrevious,PromotionCommand promotion){
        if (null == briefListPrevious)
            return false;
        if (null == promotion.getGroupName() || promotion.getGroupName().isEmpty())
            return false;
        PromotionCommand onePromotionInGroup = null;

        for (PromotionBrief oneBrief : briefListPrevious){
            onePromotionInGroup = sdkPromotionGuideManager.getPromotionById(oneBrief.getPromotionId());

            if (null == onePromotionInGroup.getGroupName() || onePromotionInGroup.getGroupName().isEmpty())
                continue;
            if (promotion.getGroupType().equals(PromotionExclusiveGroupType.SINGLE) && promotion.getGroupType().equals(onePromotionInGroup.getGroupType()) && promotion.getGroupName().trim().equalsIgnoreCase(onePromotionInGroup.getGroupName().trim())){
                return true;
            }
        }

        return false;
    }

    private boolean checkExistsInPreviousBySingleExclusiveGroup(List<PromotionBrief> briefListPrevious){
        if (null == briefListPrevious)
            return false;

        PromotionCommand onePromotionInGroup = null;

        for (PromotionBrief oneBrief : briefListPrevious){
            onePromotionInGroup = sdkPromotionGuideManager.getPromotionById(oneBrief.getPromotionId());

            if (null == onePromotionInGroup.getGroupName() || onePromotionInGroup.getGroupName().isEmpty())
                continue;
            if (onePromotionInGroup.getGroupType().equals(PromotionExclusiveGroupType.SINGLE)){
                return true;
            }
        }

        return false;
    }

    /**
     * 检查历史活动中是否有排他活动已经存在
     * 存在，就不做后续活动
     */
    private boolean checkExistsInPreviousByFirstLevelExclusive(List<PromotionBrief> briefListPrevious){
        if (null == briefListPrevious)
            return false;

        for (PromotionBrief oneBrief : briefListPrevious){
            for (PromotionCommand onePrm : EngineManager.getInstance().getPromotionCommandList()){
                if (oneBrief.getPromotionId().equals(onePrm.getPromotionId()) && onePrm.getExclusiveMark() == 1 && null != onePrm.getGroupName() && !onePrm.getGroupName().trim().isEmpty()){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkExistsInPreviousByExclusive(List<PromotionBrief> briefListPrevious){
        if (null == briefListPrevious)
            return false;

        for (PromotionBrief oneBrief : briefListPrevious){
            for (PromotionCommand onePrm : EngineManager.getInstance().getPromotionCommandList()){
                if (oneBrief.getPromotionId().equals(onePrm.getPromotionId()) && onePrm.getExclusiveMark() == 1){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkExistsInPreviousByGroup(List<PromotionBrief> briefListPrevious,String groupName){
        if (null == briefListPrevious)
            return false;
        if (null == groupName || groupName.isEmpty())
            return false;
        Long onePromotionIdInGroup = 0L;

        for (PromotionBrief oneBrief : briefListPrevious){
            onePromotionIdInGroup = oneBrief.getPromotionId();
            for (PromotionCommand onePrm : EngineManager.getInstance().getPromotionCommandList()){
                if (null == onePrm.getGroupName() || onePrm.getGroupName().isEmpty())
                    continue;
                if (onePrm.getPromotionId().equals(onePromotionIdInGroup) && onePrm.getGroupName().trim().equalsIgnoreCase(groupName.trim())){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 根据计算购物车详情，和促销列表，计算优惠信息
     * promotionList:经过CurrentTime，人群（当前登录的用户），商品范围（购物车中Lines SKU下的Item）过滤过的活动列表
     * 
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionBrief> calculationPromotion(ShoppingCartCommand shoppingCartCommand,List<PromotionCommand> allPromotionList){
        LOGGER.info("促销引擎开始！");
        if (shoppingCartCommand == null){
            return null;
        }

        Map<Long, ShoppingCartCommand> shopCartAll = shoppingCartCommand.getShoppingCartByShopIdMap();// 按店铺取购物车
        if (shopCartAll == null || shopCartAll.size() == 0){
            return null;
        }

        Set<Long> shopIdSet = getShopIdListFromPromotionList(allPromotionList);// 从促销列表中的取店铺列表,无促销活动返回
        if (shopIdSet == null || shopIdSet.size() == 0){
            return null;
        }

        List<PromotionBrief> briefListAll = new ArrayList<PromotionBrief>();

        // 获取所有有效的Coupons，按Shop Id分
        Map<Long, List<PromotionCouponCodeCommand>> couponsAllByShopId = sdkPromotionSettingManager.seperateCouponCodesByShopID(shoppingCartCommand.getCoupons());

        //try {
        // 按活动遍历,拆分Coupon by shop id
        for (Long oneShopId : shopIdSet){
            // 调整优先级
            logPromotionPriority(allPromotionList);
            List<PromotionCommand> oneShopPromotionList = sdkPriorityAdjustManager.promotionAdjustPriority(allPromotionList, oneShopId, new Date());
            logPromotionPriority(oneShopPromotionList);
            if (oneShopPromotionList == null || oneShopPromotionList.size() == 0){
                continue;
            }else{
                //优先处理套餐活动
                ShoppingCartCommand shopCartOne = shopCartAll.get(oneShopId);// 获取购物车根据店铺编号

                // 按活动遍历
                for (PromotionCommand onePromotion : oneShopPromotionList){
                    if (checkExistsInPreviousByExclusive(briefListAll)){
                        break;
                    }else if (null != onePromotion.getGroupName() && checkExistsInPreviousByGroup(briefListAll, onePromotion.getGroupName())){
                        //N选一
                        continue;
                    }

                    Long shopId = onePromotion.getShopId();

                    if (couponsAllByShopId != null && couponsAllByShopId.size() != 0){
                        List<PromotionCouponCodeCommand> couponsOneShop = couponsAllByShopId.get(shopId);
                        if (couponsOneShop == null || couponsOneShop.size() == 0){
                            shopCartOne.setCoupons(null);
                            shopCartOne.setCouponCodeCommands(null);
                        }else{
                            shopCartOne.setCoupons(sdkPromotionSettingManager.getCouponCodeFromCommandList(couponsOneShop));
                            shopCartOne.setCouponCodeCommands(couponsOneShop);
                        }
                    }

                    //支持多个Coupon，只使用一次，上个活动中的使用过的，就不应该在下次活动中出现
                    List<PromotionBrief> briefListOne = calculationPromotionByShopId(shopCartOne, onePromotion, briefListAll);

                    List<PromotionCouponCodeCommand> couponsOneShopLeft = sdkPromotionSettingManager.getLeftCouponsKickoffPrevious(shopCartOne.getCouponCodeCommands(), briefListOne);

                    if (couponsOneShopLeft != null && couponsOneShopLeft.size() > 0){
                        shopCartOne.setCoupons(sdkPromotionSettingManager.getCouponCodeFromCommandList(couponsOneShopLeft));
                        shopCartOne.setCouponCodeCommands(couponsOneShopLeft);
                    }else{
                        shopCartOne.setCoupons(null);
                        shopCartOne.setCouponCodeCommands(null);
                    }

                    if (briefListOne != null && briefListOne.size() > 0){
                        briefListAll.addAll(briefListOne);
                    }
                }
            }
        }
        LOGGER.info("促销引擎正常结束！");
        LOGGER.debug("订单金额：" + shoppingCartCommand.getOriginPayAmount());
        logBriefsByPromotion(briefListAll);
        return briefListAll;
    }

    /**
     * 检查优惠活动，是否已经超过最大优惠幅度
     * 
     * @param shopCart
     * @param conditionList
     * @param briefListPrevious
     * @return
     */
    @Transactional(readOnly = true)
    private boolean checkWhetherOutOfMarginRate(ShoppingCartCommand shopCart,List<AtomicCondition> conditionList,List<PromotionBrief> briefListPrevious){
        // 整单优惠最大幅度
        if (conditionList == null || conditionList.size() == 0)
            return false;
        for (AtomicCondition condition : conditionList){
            BigDecimal ordAMT = BigDecimal.ZERO;
            BigDecimal prevoiusDiscAMT = BigDecimal.ZERO;
            if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDMARGINRATE)){
                ordAMT = shoppingCartmanager.getNeedToPayAmountInShoppingCartByAll(shopCart);
                prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
            }else if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPMARGINRATE)){
                // 根据范围ID，计算每个Line SKU下Item的金额
                if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)){
                    ordAMT = shoppingCartmanager.getAllAmount(shopCart.getShoppingCartLineCommands());
                    prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
                    ordAMT = shoppingCartmanager.getProductAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
                    prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shopCart, briefListPrevious, condition.getScopeValue());
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
                    ordAMT = shoppingCartmanager.getCategoryAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
                    prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shopCart, briefListPrevious, condition.getScopeValue());
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
                    List<Long> itemIdList = new ArrayList<Long>();
                    itemIdList = SdkCustomizeFilterLoader.load(String.valueOf(condition.getScopeValue()));
                    ordAMT = shoppingCartmanager.getCustomAmount(itemIdList, shopCart.getShoppingCartLineCommands());
                    prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(shopCart.getShoppingCartLineCommands(), briefListPrevious, itemIdList);
                }else if (condition.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
                    ordAMT = shoppingCartmanager.getComboAmount(condition.getScopeValue(), shopCart.getShoppingCartLineCommands());
                    prevoiusDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shopCart, briefListPrevious, condition.getScopeValue());
                }
            }
            if (null == prevoiusDiscAMT || prevoiusDiscAMT.compareTo(BigDecimal.valueOf(0)) == 0){
                return false;
            }
            if (null == ordAMT || ordAMT.compareTo(BigDecimal.valueOf(0)) == 0){
                return false;
            }
            if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_ORDMARGINRATE) || condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPMARGINRATE)){
                //判断最大优惠幅度
                BigDecimal previousMarginRate = prevoiusDiscAMT.divide(ordAMT, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                if (previousMarginRate.compareTo(new BigDecimal(100).subtract(condition.getConditionValue())) > 0){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 根据计算购物车详情，和促销列表，计算优惠信息
     * promotionList:经过CurrentTime，人群（当前登录的用户），商品范围（购物车中Lines SKU下的Item）过滤过的活动列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<PromotionBrief> calculationPromotionByShopId(ShoppingCartCommand shopCart,PromotionCommand promotionOne,List<PromotionBrief> briefListPrevious){
        List<PromotionBrief> briefList = new ArrayList<PromotionBrief>();
        List<PromotionBrief> briefListIncludeCurrent = new ArrayList<PromotionBrief>();
        PromotionBrief briefOnePromotion = new PromotionBrief();

        List<AtomicCondition> conditionList = null;
        List<AtomicCondition> stepConditionList = null;
        long complexConditionId = 0;
        List<AtomicCondition> choiceConditionList = null;

        List<AtomicSetting> settingList = null;
        List<AtomicSetting> stepSettingList = null;
        List<AtomicSetting> choiceSettingList = null;

        Integer conditionResultFactor = MULTIPONE;

        BeanUtils.copyProperties(briefListIncludeCurrent, briefListPrevious);

        // Normal常规
        if (promotionOne.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_NORMAL)){
            // 倍增，是指购物车中的购买金额、件数，成倍满足促销规则的条件，系统自动倍增。首先试出倍增因子。缺省的倍增因子为1。
            // Coupon虽然作为条件出现，但不支持倍增逻辑。
            // 当倍增因子大于1时，优惠的标的就会翻倍。支持倍增的优惠标的有：除去按Qty计（Rate）、Coupon类型以外的所有优惠类型。
            // 倍增因子缺省为1，条件不满足时，倍增因子为0。
            conditionList = promotionOne.getAtomicConditionList();
            conditionResultFactor = sdkPromotionConditionManager.getFactorFromShoppingCartByAtomicConditionList(shopCart, conditionList, promotionOne.getShopId(), briefListPrevious);

            LOGGER.info("活动编号：" + promotionOne.getPromotionId().toString() + "，名称:" + promotionOne.getPromotionName());
            if (conditionResultFactor > CHECKFAILURE){
                settingList = promotionOne.getAtomicSettingList();
                briefOnePromotion = calculationPromotionByAtomicSetting(shopCart, promotionOne, settingList, conditionResultFactor, briefListPrevious);
                briefList.add(briefOnePromotion);

                briefListIncludeCurrent.add(briefOnePromotion);

                //计算当前优惠活动后，是否已经超出最大优惠幅度，大于的话丢掉
                if ((briefListIncludeCurrent != null && briefListIncludeCurrent.size() > 0) && checkWhetherOutOfMarginRate(shopCart, conditionList, briefListIncludeCurrent) == true){
                    return null;
                }
            }else{
                LOGGER.info("---条件表达式：" + promotionOne.getConditionExpression());
                LOGGER.info("---条件表达式状态：不满足!");
            }
            // Summarizes setting details to brief
            if (briefList != null && briefList.size() > 0)
                briefList = sdkPromotionSettingManager.summarizeSettingToBrief(briefList);
            return briefList;
        }
        // Step阶梯
        else if (promotionOne.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_STEP)){
            // 满足条件，获取当前优惠活动设置。要放到启用时加载
            stepConditionList = sdkPromotionConditionManager.convertComplexConditionToAtomic(promotionOne.getConditionComplexList());
            promotionOne.setAtomicComplexConditionList(stepConditionList);

            complexConditionId = sdkPromotionConditionManager.getStepByAtomicComplexConditionList(shopCart, stepConditionList, promotionOne.getShopId(), briefListPrevious);

            if (complexConditionId >= 1){
                String conditionExpressionComplex = "";

                for (ConditionComplexCommand complex : promotionOne.getConditionComplexList()){
                    if (complexConditionId == complex.getId()){
                        conditionExpressionComplex = complex.getConditionExpress();
                        break;
                    }
                }
                // 要放到启用时加载
                stepSettingList = sdkPromotionSettingManager.convertComplexSettingToAtomic(promotionOne.getSettingComplexList());
                promotionOne.setAtomicComplexSettingList(stepSettingList);

                List<AtomicSetting> stepSetting = sdkPromotionConditionManager.getStepAtomicComplexSetting(stepSettingList, complexConditionId);

                conditionResultFactor = 1;// 阶梯不支持倍增
                briefOnePromotion = calculationPromotionByAtomicSetting(shopCart, promotionOne, stepSetting, conditionResultFactor, briefListPrevious);
                briefOnePromotion.setConditionExpressionComplex(conditionExpressionComplex);
                briefList.add(briefOnePromotion);

                briefListIncludeCurrent.add(briefOnePromotion);

                //计算当前优惠活动后，是否已经超出最大优惠幅度，大于的话丢掉
                if ((briefListIncludeCurrent != null && briefListIncludeCurrent.size() > 0) && checkWhetherOutOfMarginRate(shopCart, conditionList, briefListIncludeCurrent) == true){
                    return null;
                }
            }else{
                LOGGER.info("活动编号：" + promotionOne.getPromotionId().toString() + "，名称:" + promotionOne.getPromotionName());
                LOGGER.info("---条件表达式：" + promotionOne.getConditionExpression());
                LOGGER.info("---条件表达式状态：不满足!");
            }
            // summarizes setting details to brief
            if (briefList != null && briefList.size() > 0)
                briefList = sdkPromotionSettingManager.summarizeSettingToBrief(briefList);
            return briefList;
        }
        // Choice选购
        else if (promotionOne.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_CHOICE)){
            // 主商品，选购商品prmprd： scpprdpcs(1,pid:21,1) | scpprdpcs(1，cmb:4,1)
            // ，addtprd：
            // ChoiceMark:prmprd,addtprd.逻辑关系式：(至少一个prmprd，多个是或的关系) &&
            // (至少一个addtprd，多个是或的关系)
            choiceConditionList = sdkPromotionConditionManager.convertComplexConditionToAtomic(promotionOne.getConditionComplexList());
            promotionOne.setAtomicComplexConditionList(choiceConditionList);

            conditionResultFactor = sdkPromotionConditionManager.checkChoiceByAtomicComplexConditionList(shopCart, choiceConditionList, promotionOne.getShopId(), briefListPrevious);

            if (conditionResultFactor > CHECKFAILURE){
                String conditionExpressionComplex = "";

                for (ConditionComplexCommand complex : promotionOne.getConditionComplexList()){
                    if (complex.getConditionExpress() != null){
                        if (conditionExpressionComplex == null || conditionExpressionComplex.isEmpty())
                            conditionExpressionComplex = complex.getConditionExpress();
                        else
                            conditionExpressionComplex = conditionExpressionComplex + "并且" + complex.getConditionExpress();
                    }
                }
                // 满足条件，获取当前优惠活动设置。要放到启用时加载
                choiceSettingList = sdkPromotionSettingManager.convertComplexSettingToAtomic(promotionOne.getSettingComplexList());
                promotionOne.setAtomicComplexSettingList(choiceSettingList);

                choiceSettingList = promotionOne.getAtomicComplexSettingList();
                briefOnePromotion = calculationPromotionByAtomicSetting(shopCart, promotionOne, choiceSettingList, conditionResultFactor, briefListPrevious);
                briefOnePromotion.setConditionExpressionComplex(conditionExpressionComplex);
                briefList.add(briefOnePromotion);

                briefListIncludeCurrent.add(briefOnePromotion);

                //计算当前优惠活动后，是否已经超出最大优惠幅度，大于的话丢掉
                if ((briefListIncludeCurrent != null && briefListIncludeCurrent.size() > 0) && checkWhetherOutOfMarginRate(shopCart, conditionList, briefListIncludeCurrent) == true){
                    return null;
                }
            }else{
                LOGGER.info("活动编号：" + promotionOne.getPromotionId().toString() + "，名称:" + promotionOne.getPromotionName());
                LOGGER.info("---条件表达式：" + promotionOne.getConditionExpression());
                LOGGER.info("---条件表达式状态：不满足!");
            }
            // summarizes setting details to brief
            if (briefList != null && briefList.size() > 0)
                briefList = sdkPromotionSettingManager.summarizeSettingToBrief(briefList);
            return briefList;
        }
        // NormalStep常规加阶梯。NormalChoice常规加选购，暂时不实现
        else if (promotionOne.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_NORMALSTEP)){
            // 常规条件
            conditionList = promotionOne.getAtomicConditionList();
            conditionResultFactor = sdkPromotionConditionManager.getFactorFromShoppingCartByAtomicConditionList(shopCart, conditionList, promotionOne.getShopId(), briefListPrevious);

            // 阶梯条件
            stepConditionList = sdkPromotionConditionManager.convertComplexConditionToAtomic(promotionOne.getConditionComplexList());
            promotionOne.setAtomicComplexConditionList(stepConditionList);

            complexConditionId = sdkPromotionConditionManager.getStepByAtomicComplexConditionList(shopCart, stepConditionList, promotionOne.getShopId(), briefListPrevious);

            if (conditionResultFactor > CHECKFAILURE && complexConditionId >= 1){
                String conditionExpressionComplex = "";

                for (ConditionComplexCommand complex : promotionOne.getConditionComplexList()){
                    if (complexConditionId == complex.getId()){
                        conditionExpressionComplex = complex.getConditionExpress();
                        break;
                    }
                }

                settingList = promotionOne.getAtomicSettingList();
                briefOnePromotion = calculationPromotionByAtomicSetting(shopCart, promotionOne, settingList, conditionResultFactor, briefListPrevious);
                briefList.add(briefOnePromotion);

                briefListIncludeCurrent.add(briefOnePromotion);
                //计算当前优惠活动后，是否已经超出最大优惠幅度，大于的话丢掉
                if ((briefListIncludeCurrent != null && briefListIncludeCurrent.size() > 0) && checkWhetherOutOfMarginRate(shopCart, conditionList, briefListIncludeCurrent) == true){
                    return null;
                }
                // 阶梯优惠
                // 要放到启用时加载
                stepSettingList = sdkPromotionSettingManager.convertComplexSettingToAtomic(promotionOne.getSettingComplexList());
                promotionOne.setAtomicComplexSettingList(stepSettingList);

                List<AtomicSetting> stepSetting = sdkPromotionConditionManager.getStepAtomicComplexSetting(stepSettingList, complexConditionId);

                conditionResultFactor = 1;// 阶梯不支持倍增
                briefOnePromotion = calculationPromotionByAtomicSetting(shopCart, promotionOne, stepSetting, conditionResultFactor, briefListPrevious);
                briefOnePromotion.setConditionExpressionComplex(conditionExpressionComplex);

                briefList.add(briefOnePromotion);

                briefListIncludeCurrent.add(briefOnePromotion);

                //计算当前优惠活动后，是否已经超出最大优惠幅度，大于的话丢掉
                if ((briefListIncludeCurrent != null && briefListIncludeCurrent.size() > 0) && checkWhetherOutOfMarginRate(shopCart, conditionList, briefListIncludeCurrent) == true){
                    return null;
                }
            }else{
                LOGGER.info("活动编号：" + promotionOne.getPromotionId().toString() + "，名称:" + promotionOne.getPromotionName());
                LOGGER.info("---常规条件表达式：" + promotionOne.getConditionExpression());
                if (conditionResultFactor == CHECKFAILURE){
                    LOGGER.info("---常规条件表达式状态：不满足!");
                }
                if (complexConditionId < 1){
                    LOGGER.info("---阶梯表达式状态：不满足!");
                }
            }
            // summarizes setting details to brief
            if (briefList != null && briefList.size() > 0)
                briefList = sdkPromotionSettingManager.summarizeSettingToBrief(briefList);
            return briefList;
        }

        return null;
    }

    /**
     * 根据计算购物车详情，和促销列表，计算一个活动的优惠信息 获取已经通过Condition验证过的活动优惠设置
     * 返回值，PromotionBrief，一个活动brief
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionBrief calculationPromotionByAtomicSetting(ShoppingCartCommand shopCart,PromotionCommand promotion,List<AtomicSetting> settingList,Integer multipFactor,List<PromotionBrief> briefListPrevious){
        PromotionBrief brief = new PromotionBrief();
        List<PromotionSettingDetail> details = new ArrayList<PromotionSettingDetail>();

        Set<String> couponCodesCondition = new HashSet<String>();
        Set<String> couponCodesOfLine = null;

        brief.setShopId(promotion.getShopId());
        brief.setPromotionId(promotion.getPromotionId());
        brief.setPromotionName(promotion.getPromotionName());
        brief.setMemComboId(promotion.getMemComboId());
        brief.setMemComboType(promotion.getMemComboType());
        brief.setProductComboId(promotion.getProductComboId());
        brief.setConditionExpression(promotion.getConditionExpression());
        brief.setConditionType(promotion.getConditionType());

        for (AtomicSetting setting : settingList){
            setting.setMultiplicationFactor(multipFactor);// pass factor to
                                                          // setting
            PromotionSettingDetail detail = calculationPromotionByAtomicSetting(shopCart, promotion, setting, briefListPrevious);
            if (detail == null)
                continue;
            if (detail.getCouponCodes() != null && detail.getCouponCodes().size() > 0 && detail.getDiscountAmount().compareTo(BigDecimal.ZERO) == 0){
                couponCodesCondition.addAll(detail.getCouponCodes());
            }

            detail.setSettingExpression(setting.getSettingExpression());
            detail.setShopId(promotion.getShopId());
            detail.setPromotionId(promotion.getPromotionId());
            detail.setPromotionName(promotion.getPromotionName());

            if (detail.getAffectSKUDiscountAMTList() != null){
                for (PromotionSKUDiscAMTBySetting sku : detail.getAffectSKUDiscountAMTList()){
                    if ((sku.getPromotionId() == null || sku.getPromotionId().equals(0L)) && promotion.getPromotionId() != null){
                        sku.setShopId(promotion.getShopId());
                        sku.setPromotionId(promotion.getPromotionId());
                        sku.setPromotionName(promotion.getPromotionName());
                    }
                    if ((sku.getSettingId() == null || sku.getSettingId().equals(0L))){
                        sku.setSettingId(setting.getSettingId());
                    }
                }
            }
            details.add(detail);
        }
        //如果有整单的条件Coupon，需要把Coupon下传到行上去
        for (PromotionSettingDetail detail : details){
            if (couponCodesCondition != null && detail.getAffectSKUDiscountAMTList() != null && couponCodesCondition.size()>0){
                for (PromotionSKUDiscAMTBySetting sku : detail.getAffectSKUDiscountAMTList()){
                    if (sku.getCouponCodes() == null)
                        sku.setCouponCodes(couponCodesCondition);
                    else{
                        couponCodesOfLine = sku.getCouponCodes();
                        couponCodesOfLine.addAll(couponCodesCondition);
                        sku.setCouponCodes(couponCodesOfLine);
                    }
                }
            }
        }
        brief.setDetails(details);
        return brief;
    }

    /**
     * 一个优惠设置
     * 
     * @see com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager#calculationPromotionByAtomicSetting(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     *      com.baozun.nebula.command.promotion.PromotionCommand,
     *      com.baozun.nebula.calculateEngine.condition.AtomicSetting)
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionSettingDetail calculationPromotionByAtomicSetting(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        // 获取活动原子设置项。一个原子设置项一个Brief，一个Brief下有多个Details
        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discountRate = BigDecimal.ZERO;

        BigDecimal previousDiscAMTAll = BigDecimal.ZERO;

        detail.setShopId(promotion.getShopId());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());
        detail.setSettingTypeTag(setting.getSettingTag());
        // 免运费金额。支持倍增。不支持单单件计
        if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_FREESHIP)){
            detail.setFreeShippingMark(true);
            discAmount = setting.getSettingValue();
            if (setting.getMultiplicationFactor() > MULTIPONE){
                discAmount = discAmount.multiply(new BigDecimal(setting.getMultiplicationFactor()));
            }
            detail.setDiscountAmount(discAmount);
            return detail;
        }
        // 整单减。支持倍增。不支持单单件计
        else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_ORDDISC)){
            discAmount = setting.getSettingValue();
            if (setting.getMultiplicationFactor() > MULTIPONE){
                discAmount = discAmount.multiply(new BigDecimal(setting.getMultiplicationFactor()));
            }
            detail.setDiscountAmount(discAmount);
            return detail;
        }
        // 自定义cstset(4,2)
        else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_CUSTOMSETTING)){
            detail = SdkCustomizeSettingLoader.load(setting, promotion, shopCart, briefListPrevious);
            return detail;
        }
        // 整单折扣率。不支持倍增。不支持按单件计。
        else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_ORDRATE)){
            discountRate = setting.getSettingValue().divide(new BigDecimal(100));
            previousDiscAMTAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
            discAmount = shoppingCartmanager.getDiscountAMTOrderDiscountRateByRate(shopCart, discountRate, previousDiscAMTAll);
            // 整单折扣率，不存在倍增，也不存在按单件计
            detail.setDiscountAmount(discAmount);
            return detail;
        }
        // 整单 Coupon。不支持倍增。不支持按单件计。即使是Rate，但无指定范围。
        else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_ORDCOUPON)){
            long couponTypeId = setting.getSettingValue().longValue();
            previousDiscAMTAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
            // 整单折扣率，不存在倍增，也不存在按单件计
            List<PromotionCouponCodeCommand> filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
            Map<String, BigDecimal> usedCouponList = shoppingCartmanager.getDiscountAMTByCALLCoupon(shopCart.getShoppingCartLineCommands(), couponTypeId, filteredCouponsByTypeId, false, promotion.getShopId(), previousDiscAMTAll);
            if (usedCouponList == null || usedCouponList.size() == 0)
                return null;
            discAmount = shoppingCartmanager.getCouponAmtFromUsedList(usedCouponList);
            discAmount = discAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            detail.setCouponCode(usedCouponList.keySet());
            detail.setDiscountAmount(discAmount);
            return detail;
        }
        // 范围整单优惠\范围整单折扣率
        // 范围优惠券
        if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)
                        || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT)
                        || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPMKDNPRICE)){
            Boolean flagExistSinglePrdInCondition = false;
            Boolean flagExistSinglePrdInConditionComplex = false;
            if (promotion.getAtomicConditionList() != null && promotion.getAtomicConditionList().size() != 0){
                flagExistSinglePrdInCondition = checkExistSinglePrdByConditionList(promotion.getAtomicConditionList());
            }
            if (promotion.getAtomicComplexConditionList() != null && promotion.getAtomicComplexConditionList().size() != 0){
                flagExistSinglePrdInConditionComplex = checkExistSinglePrdByConditionList(promotion.getAtomicComplexConditionList());
            }
            flagExistSinglePrdInCondition = flagExistSinglePrdInCondition || flagExistSinglePrdInConditionComplex;
            // 当前范围按SKU下ItemID存放
            if (flagExistSinglePrdInCondition && (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)
                            || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC) || setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE))){
                List<ItemFactor> itemFactorList = new ArrayList<ItemFactor>();
                List<ItemFactor> itemFactorListStep = new ArrayList<ItemFactor>();

                if (promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_NORMAL)){
                    itemFactorList = sdkPromotionConditionManager.getItemFactorListShoppingCartByAtomicConditions(shopCart, promotion.getAtomicConditionList(), promotion.getShopId(), briefListPrevious);
                }else if (promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_STEP)){
                    itemFactorList = sdkPromotionConditionManager.getItemFactorListShoppingCartByStepAtomicConditions(shopCart, promotion.getAtomicComplexConditionList(), promotion.getShopId(), briefListPrevious);
                }else if (promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_NORMALSTEP)){
                    itemFactorList = sdkPromotionConditionManager.getItemFactorListShoppingCartByAtomicConditions(shopCart, promotion.getAtomicConditionList(), promotion.getShopId(), briefListPrevious);
                    itemFactorListStep = sdkPromotionConditionManager.getItemFactorListShoppingCartByStepAtomicConditions(shopCart, promotion.getAtomicComplexConditionList(), promotion.getShopId(), briefListPrevious);
                    itemFactorList = sdkPromotionConditionManager.getIntersectItemFactorList(itemFactorList, itemFactorListStep);
                }
                if (promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_CHOICE) || promotion.getConditionType().equalsIgnoreCase(ConditionMasterType.EXP_NORMALCHOICE)){
                    detail = calculationPromotionByAtomicSettingByScope(shopCart, promotion, setting, briefListPrevious);
                }else{
                    detail = calculationSinglePrdPromotionByAtomicSettingByScope(shopCart, itemFactorList, promotion, setting, briefListPrevious);
                }
            }else
                detail = calculationPromotionByAtomicSettingByScope(shopCart, promotion, setting, briefListPrevious);

            //向行上设置SettingId
            if (detail.getAffectSKUDiscountAMTList() != null && detail.getAffectSKUDiscountAMTList().size() > 0){
                for (PromotionSKUDiscAMTBySetting sku : detail.getAffectSKUDiscountAMTList()){
                    sku.setSettingId(setting.getSettingId());
                }
            }
            return detail;
        }

        return null;
    }

    private Boolean checkExistSinglePrdByConditionList(List<AtomicCondition> conditionList){
        Boolean flag = false;
        for (AtomicCondition condition : conditionList){
            if (condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDAMT) || condition.getConditionTag().equalsIgnoreCase(ConditionType.EXP_SCPPRDPCS)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionSettingDetail calculationSinglePrdPromotionByAtomicSettingByScope(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = null;

        // 基于全场
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)){
            detail = getSinglePrdPromotionCalculationBriefDetailByCALL(shopCart, itemFactorList, promotion, setting, briefListPrevious);
        }
        // 基于商品ItemID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
            detail = getSinglePrdPromotionCalculationBriefDetailByItem(shopCart, itemFactorList, promotion, setting, briefListPrevious);
        }
        // 基于商品分类CategoryID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
            detail = getSinglePrdPromotionCalculationBriefDetailByCategory(shopCart, itemFactorList, promotion, setting, briefListPrevious);
        }
        // 基于商品分类CustomID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
            detail = getSinglePrdPromotionCalculationBriefDetailByCustomItemIds(shopCart, itemFactorList, promotion, setting, briefListPrevious);
        }
        // 基于组合ComboID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
            detail = getSinglePrdPromotionCalculationBriefDetailByCombo(shopCart, itemFactorList, promotion, setting, briefListPrevious);
        }
        return detail;
    }

    /**
     * 获取范围优惠设置，全场，商品ItemID，商品分类CategoryID，商品组合ComboID
     * 
     * @see com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager#getPromotionCalculationBriefDetailByAtomicSetting(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     *      com.baozun.nebula.command.promotion.PromotionCommand,
     *      com.baozun.nebula.calculateEngine.condition.AtomicSetting)
     */
    @Override
    @Transactional(readOnly = true)
    public PromotionSettingDetail calculationPromotionByAtomicSettingByScope(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = null;

        // 基于全场
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)){
            detail = getPromotionCalculationBriefDetailByCALL(shopCart, promotion, setting, briefListPrevious);
        }
        // 基于商品ItemID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
            detail = getPromotionCalculationBriefDetailByItem(shopCart, promotion, setting, briefListPrevious);
        }
        // 基于商品分类CategoryID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
            detail = getPromotionCalculationBriefDetailByCategory(shopCart, promotion, setting, briefListPrevious);
        }
        // 基于自定义商品过滤器CctgID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
            detail = getPromotionCalculationBriefDetailByCustom(shopCart, promotion, setting, briefListPrevious);
        }
        // 基于组合ComboID
        else if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
            detail = getPromotionCalculationBriefDetailByCombo(shopCart, promotion, setting, briefListPrevious);
        }
        return detail;
    }

    /**
     * 全场 范围整单优惠、范围整单折扣、范围单品优惠、范围单品折扣、范围单件优惠、范围单件折扣
     * 
     * @param shopCart
     * @param promotion
     * @param setting
     * @param briefListPrevious
     * @return
     */
    @Transactional(readOnly = true)
    private PromotionSettingDetail getPromotionCalculationBriefDetailByCALL(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discAmountFetched = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 购物车中所有商品都有优惠
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)){
            BigDecimal previousDiscAMTAll = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsByCall(briefListPrevious);
            // 范围整单优惠。支持倍增。不支持按单件计。
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC)){
                discAmount = setting.getSettingValue();
                discList = shoppingCartmanager.getDiscountAMTCALLPerOrderByAMT(shopCart.getShoppingCartLineCommands(), discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围整单折扣。支持按单件计的时候，支持倍增才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCALLPerOrderByRate(shopCart.getShoppingCartLineCommands(), discRate, true, briefListPrevious);
                }else{
                    discList = shoppingCartmanager.getDiscountAMTCALLPerOrderByRate(shopCart.getShoppingCartLineCommands(), discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();
                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTCALLPerItemByAMT(shopCart.getShoppingCartLineCommands(), discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCALLPerItemByRate(shopCart.getShoppingCartLineCommands(), discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTCALLPerItemByRate(shopCart.getShoppingCartLineCommands(), discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单件优惠,单件就是降低价格，和QTY无关
            // 支持倍增。不支持按单件计。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();
                // Item折扣到SKU
                discList = shoppingCartmanager.getDiscountAMTCALLPerPCSByAMT(shopCart.getShoppingCartLineCommands(), discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单件折扣,单件就是降低价格，和QTY无关
            // 支持按单件计的时候，支持倍增才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCALLPerPCSByRate(shopCart.getShoppingCartLineCommands(), discRate, true, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTCALLPerPCSByRate(shopCart.getShoppingCartLineCommands(), discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围优惠券
            // 不支持倍增。按Rate的折扣券，有按单件计。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON)){
                long couponTypeId = setting.getSettingValue().longValue();
                Integer couponType2Check = shoppingCartmanager.getCouponTypeByCouponTypeID(couponTypeId);
                List<PromotionCouponCodeCommand> filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
                if (filteredCouponsByTypeId != null && filteredCouponsByTypeId.size() > 0){

                    Map<String, BigDecimal> usedCouponList = null;

                    if (couponType2Check == PromotionCoupon.TYPE_RATE){
                        usedCouponList = shoppingCartmanager.getDiscountAMTByCALLCoupon(shopCart.getShoppingCartLineCommands(), couponTypeId, filteredCouponsByTypeId, setting.getOnePieceMark(), promotion.getShopId(), previousDiscAMTAll);
                    }else{
                        usedCouponList = shoppingCartmanager.getDiscountAMTByCALLCoupon(shopCart.getShoppingCartLineCommands(), couponTypeId, filteredCouponsByTypeId, false, promotion.getShopId(), previousDiscAMTAll);
                    }

                    discAmountFetched = shoppingCartmanager.getCouponAmtFromUsedList(usedCouponList);
                    discAmountFetched = discAmountFetched.setScale(2, BigDecimal.ROUND_HALF_UP);
                    detail.setDiscountAmount(discAmountFetched);
                    detail.setCouponCode(usedCouponList.keySet());
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    private PromotionSettingDetail getSinglePrdPromotionCalculationBriefDetailByCALL(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 购物车中所有商品都有优惠
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT)){
            Integer factorDefault = setting.getMultiplicationFactor();
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增。不支持按单件计。
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // 单品折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerItemByAMT(shopCart.getShoppingCartLineCommands(), discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持按单件计的时候，支持倍增才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerItemByRate(shopCart.getShoppingCartLineCommands(), discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerItemByRate(shopCart.getShoppingCartLineCommands(), discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerPCSByAMT(shopCart.getShoppingCartLineCommands(), discAmount, factorDefault, itemFactorList, briefListPrevious);
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerPCSByRate(shopCart.getShoppingCartLineCommands(), discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCALLPerPCSByRate(shopCart.getShoppingCartLineCommands(), discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    /**
     * 商品Item 范围整单优惠、范围整单折扣、范围单品优惠、范围单品折扣、范围单件优惠、范围单件折扣
     */
    private PromotionSettingDetail getPromotionCalculationBriefDetailByItem(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        long shopId = promotion.getShopId();

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 基于商品ItemID
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
            long itemId = setting.getScopeValue();

            // 范围整单优惠。支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC)){
                discAmount = setting.getSettingValue();

                // Item优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTItemPerOrderByAMT(shopCart.getShoppingCartLineCommands(), itemId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }

            // 范围整单折扣。支持倍增，按单件计的时候，才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTItemPerOrderByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, true, briefListPrevious);
                }else{
                    // Item折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTItemPerOrderByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTItemPerItemByAMT(shopCart.getShoppingCartLineCommands(), itemId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTItemPerItemByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTItemPerItemByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单件优惠,单件就是降低价格，和QTY无关
            // 支持倍增，不支持按单件计。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getDiscountAMTItemPerPCSByAMT(shopCart.getShoppingCartLineCommands(), itemId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单件折扣,单件就是降低价格，和QTY无关
            // 支持倍增，在按单件计时才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTItemPerPCSByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, true, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTItemPerPCSByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 把礼品价格到其他SKU上
            // 支持倍增，在按单件计、按Qty计时都有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT)){
                long discItemId = setting.getScopeValue();
                int discItemIdQTY = Integer.parseInt(String.valueOf(setting.getSettingValue()));
                // 把礼品Item价格到其他SKU上
                if (setting.getMultiplicationFactor() > MULTIPONE)
                    discItemIdQTY = setting.getMultiplicationFactor();

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark())
                    discItemIdQTY = 1;
                Integer displayCountLimited = PromotionConstants.GIFTDISPLAYINSHOPCARTLIMITED;
                if (setting.getGiftChoiceType() == GiftChoiceType.NoNeedChoice)
                    displayCountLimited = setting.getGiftCountLimited();

                discList = shoppingCartmanager.getDiscountAMTGiftByItemID(shopId, setting, discItemId, discItemIdQTY, displayCountLimited);
                discList = initSKUSettingListGiftDiplayType(discList, setting.getGiftChoiceType(), setting.getGiftCountLimited());
            }
            //Markdown一口价
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPMKDNPRICE)){
                long discItemId = setting.getScopeValue();
                discList = shoppingCartmanager.getMarkdownPriceByItemID(shopCart.getShoppingCartLineCommands(), discItemId, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围优惠券
            // 不支持倍增，Rate时支持按单件计。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON)){
                Long couponTypeId = setting.getSettingValue().longValue();
                Integer couponType2Check = shoppingCartmanager.getCouponTypeByCouponTypeID(couponTypeId);

                List<PromotionCouponCodeCommand> filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
                if (filteredCouponsByTypeId != null && filteredCouponsByTypeId.size() > 0){
                    // 单品折扣到SKU
                    if (couponType2Check.intValue() == PromotionCoupon.TYPE_RATE.intValue()){
                        discList = shoppingCartmanager.getDiscountAMTByItemIdCoupon(shopCart.getShoppingCartLineCommands(), itemId, couponTypeId, filteredCouponsByTypeId, setting.getOnePieceMark(), promotion.getShopId(), briefListPrevious);
                    }else{
                        discList = shoppingCartmanager.getDiscountAMTByItemIdCoupon(shopCart.getShoppingCartLineCommands(), itemId, couponTypeId, filteredCouponsByTypeId, false, promotion.getShopId(), briefListPrevious);
                    }
                }
            }
            // 根据List获得当前Setting的优惠金额
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }

        }
        return detail;
    }

    private List<PromotionSKUDiscAMTBySetting> initSKUSettingListGiftDiplayType(List<PromotionSKUDiscAMTBySetting> settingList,Integer choiceType,Integer giftCountLimited){
        if (settingList == null || settingList.size() == 0)
            return null;
        for (PromotionSKUDiscAMTBySetting one : settingList){
            one.setGiftChoiceType(choiceType);
            one.setGiftCountLimited(giftCountLimited);
        }
        return settingList;
    }

    private PromotionSettingDetail getSinglePrdPromotionCalculationBriefDetailByItem(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 基于商品ItemID
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_PRODUCT)){
            Integer factorDefault = setting.getMultiplicationFactor();
            long itemId = setting.getScopeValue();
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计。
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerItemByAMT(shopCart.getShoppingCartLineCommands(), itemId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，在按单件计时才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerItemByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerItemByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerPCSByAMT(shopCart.getShoppingCartLineCommands(), itemId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerPCSByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTItemPerPCSByRate(shopCart.getShoppingCartLineCommands(), itemId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            // 根据List获得当前Setting的优惠金额
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    /**
     * 商品分类 范围整单优惠、范围整单折扣、范围单品优惠、范围单品折扣、范围单件优惠、范围单件折扣
     */
    private PromotionSettingDetail getPromotionCalculationBriefDetailByCategory(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;
        // List<PromotionSKUDiscAMTBySetting> discListTmp = null;
        List<PromotionCouponCodeCommand> filteredCouponsByTypeId;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discAmountFetched = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;
        long shopId = promotion.getShopId();
        long couponTypeId = 0L;
        Integer couponType2Check = 0;

        long categoryId = 0L;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 基于商品分类CategoryID
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
            categoryId = setting.getScopeValue();
            // BigDecimal previousDiscAMT =
            // this.sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
            // 范围整单优惠、折扣，基于商品分类的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持安单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTCategoryPerOrderByAMT(shopCart.getShoppingCartLineCommands(), categoryId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围整单折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerOrderByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerOrderByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTCategoryPerItemByAMT(shopCart.getShoppingCartLineCommands(), categoryId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerItemByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerItemByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单件优惠、折扣,单件就是降低价格，和QTY无关
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTCategoryPerPCSByAMT(shopCart.getShoppingCartLineCommands(), categoryId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单件优惠、折扣,单件就是降低价格，和QTY无关
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerPCSByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTCategoryPerPCSByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 把礼品价格到其他SKU上
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT)){
                int discItemIdQTY = Integer.parseInt(String.valueOf(setting.getSettingValue()));
                if (setting.getMultiplicationFactor() > MULTIPONE)
                    discItemIdQTY = setting.getMultiplicationFactor();

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark())
                    discItemIdQTY = 1;
                // 把礼品Item价格到其他SKU上
                Integer displayCountLimited = PromotionConstants.GIFTDISPLAYINSHOPCARTLIMITED;
                if (setting.getGiftChoiceType() == GiftChoiceType.NoNeedChoice)
                    displayCountLimited = setting.getGiftCountLimited();
                discList = shoppingCartmanager.getDiscountAMTGiftByCategoryID(shopId, setting, discItemIdQTY, displayCountLimited);
                discList = initSKUSettingListGiftDiplayType(discList, setting.getGiftChoiceType(), setting.getGiftCountLimited());
            }
            //Markdown一口价
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPMKDNPRICE)){
                discList = shoppingCartmanager.getMarkdownPriceByCategoryID(shopCart.getShoppingCartLineCommands(), categoryId, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围优惠券
            // 不支持倍增，按Rate折扣券支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON)){
                couponTypeId = setting.getSettingValue().longValue();
                couponType2Check = shoppingCartmanager.getCouponTypeByCouponTypeID(setting.getSettingValue().longValue());

                filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
                if (filteredCouponsByTypeId != null && filteredCouponsByTypeId.size() > 0){
                    if (couponType2Check == PromotionCoupon.TYPE_RATE && setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                        discList = shoppingCartmanager.getDiscountAMTByCategoryIdCoupon(shopCart.getShoppingCartLineCommands(), categoryId, couponTypeId, filteredCouponsByTypeId, true, promotion.getShopId(), briefListPrevious);
                    }else{
                        // 单品折扣到SKU
                        discList = shoppingCartmanager.getDiscountAMTByCategoryIdCoupon(shopCart.getShoppingCartLineCommands(), categoryId, couponTypeId, filteredCouponsByTypeId, setting.getOnePieceMark(), promotion.getShopId(), briefListPrevious);
                    }
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
                discAmountFetched = detail.getDiscountAmount();
                detail.setDiscountAmount(discAmountFetched);
            }
        }
        return detail;
    }

    private PromotionSettingDetail getSinglePrdPromotionCalculationBriefDetailByCategory(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 基于商品分类CategoryID
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CATEGORY)){
            Integer factorDefault = setting.getMultiplicationFactor();
            long categoryId = setting.getScopeValue();
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerItemByAMT(shopCart.getShoppingCartLineCommands(), categoryId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerItemByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerItemByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerPCSByAMT(shopCart.getShoppingCartLineCommands(), categoryId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerPCSByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCategoryPerPCSByRate(shopCart.getShoppingCartLineCommands(), categoryId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    private PromotionSettingDetail getSinglePrdPromotionCalculationBriefDetailByCustomItemIds(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 基于商品ItemID
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
            Integer factorDefault = setting.getMultiplicationFactor();
            long customId = setting.getScopeValue();

            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计。
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerItemByAMT(shopCart.getShoppingCartLineCommands(), customId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，在按单件计时才有意义。
            else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerItemByRate(shopCart.getShoppingCartLineCommands(), customId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerItemByRate(shopCart.getShoppingCartLineCommands(), customId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerPCSByAMT(shopCart.getShoppingCartLineCommands(), customId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }else if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerPCSByRate(shopCart.getShoppingCartLineCommands(), customId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTCustomPerPCSByRate(shopCart.getShoppingCartLineCommands(), customId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            // 根据List获得当前Setting的优惠金额
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    /**
     * 自定义 范围整单优惠、范围整单折扣、范围单品优惠、范围单品折扣、范围单件优惠、范围单件折扣
     */
    private PromotionSettingDetail getPromotionCalculationBriefDetailByCustom(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;
        List<PromotionCouponCodeCommand> filteredCouponsByTypeId = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discAmountFetched = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;
        long shopId = promotion.getShopId();
        Integer couponType2Check = 0;
        long couponTypeId = 0L;

        long customId = 0L;
        List<Long> customItemIds = new ArrayList<Long>();

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 购物车中所有组合都有优惠
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_CUSTOM)){
            customId = setting.getScopeValue();
            customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
            // BigDecimal previousDiscAMT =
            // this.sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
            // 范围整单优惠
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC)){
                discAmount = setting.getSettingValue();

                // 优惠到SKU
                discList = sdkPromotionSettingManager.getDiscountAMTCustomPerOrderByAMT(shopCart.getShoppingCartLineCommands(), customItemIds, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }

            // 范围整单折扣
            // 不支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                // Item折扣到SKU
                discList = sdkPromotionSettingManager.getDiscountAMTCustomPerOrderByRate(shopCart.getShoppingCartLineCommands(), customItemIds, discRate, briefListPrevious);
            }
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = sdkPromotionSettingManager.getDiscountAMTCustomPerItemByAMT(shopCart.getShoppingCartLineCommands(), customItemIds, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = sdkPromotionSettingManager.getDiscountAMTCustomPerItemByRate(shopCart.getShoppingCartLineCommands(), customItemIds, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = sdkPromotionSettingManager.getDiscountAMTCustomPerItemByRate(shopCart.getShoppingCartLineCommands(), customItemIds, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单件优惠,单件就是降低价格，和QTY无关
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = sdkPromotionSettingManager.getDiscountAMTCustomPerPCSByAMT(shopCart.getShoppingCartLineCommands(), customItemIds, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单件折扣,单件就是降低价格，和QTY无关
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = sdkPromotionSettingManager.getDiscountAMTCustomPerPCSByRate(shopCart.getShoppingCartLineCommands(), customItemIds, discRate, true, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = sdkPromotionSettingManager.getDiscountAMTCustomPerPCSByRate(shopCart.getShoppingCartLineCommands(), customItemIds, discRate, false, briefListPrevious);
                }
            }
            // 把礼品价格到其他SKU上
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT)){
                int discItemIdQTY = Integer.parseInt(String.valueOf(setting.getSettingValue()));
                if (setting.getMultiplicationFactor() > MULTIPONE)
                    discItemIdQTY = setting.getMultiplicationFactor();

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark())
                    discItemIdQTY = 1;
                // 把礼品Item价格到其他SKU上
                Integer displayCountLimited = PromotionConstants.GIFTDISPLAYINSHOPCARTLIMITED;
                if (setting.getGiftChoiceType() == GiftChoiceType.NoNeedChoice)
                    displayCountLimited = setting.getGiftCountLimited();
                discList = sdkPromotionSettingManager.getDiscountAMTGiftByCustomItemIds(shopId, setting, discItemIdQTY, displayCountLimited);
                discList = initSKUSettingListGiftDiplayType(discList, setting.getGiftChoiceType(), setting.getGiftCountLimited());
            }
            //Markdown一口价
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPMKDNPRICE)){
                discList = shoppingCartmanager.getMarkdownPriceByCustomItemIds(shopCart.getShoppingCartLineCommands(), customItemIds, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围优惠券
            // 不支持倍增，按Rate的折扣券支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON)){
                couponTypeId = setting.getSettingValue().longValue();
                couponType2Check = shoppingCartmanager.getCouponTypeByCouponTypeID(couponTypeId);

                filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
                if (filteredCouponsByTypeId != null && filteredCouponsByTypeId.size() > 0){
                    // 检查couponType
                    // 单品折扣到SKU
                    if (couponType2Check.intValue() == PromotionCoupon.TYPE_RATE.intValue() && setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                        discList = sdkPromotionSettingManager.getDiscountAMTByCustomItemIdsCoupon(shopCart.getShoppingCartLineCommands(), customItemIds, couponTypeId, filteredCouponsByTypeId, true, promotion.getShopId(), briefListPrevious);
                    }else{
                        discList = sdkPromotionSettingManager
                                        .getDiscountAMTByCustomItemIdsCoupon(shopCart.getShoppingCartLineCommands(), customItemIds, couponTypeId, filteredCouponsByTypeId, setting.getOnePieceMark(), promotion.getShopId(), briefListPrevious);
                    }
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
                discAmountFetched = detail.getDiscountAmount();
                detail.setDiscountAmount(discAmountFetched);
            }
        }
        return detail;
    }

    /**
     * 组合 范围整单优惠、范围整单折扣、范围单品优惠、范围单品折扣、范围单件优惠、范围单件折扣
     */
    private PromotionSettingDetail getPromotionCalculationBriefDetailByCombo(ShoppingCartCommand shopCart,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;
        List<PromotionCouponCodeCommand> filteredCouponsByTypeId = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discAmountFetched = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;
        long shopId = promotion.getShopId();
        Integer couponType2Check = 0;
        long couponTypeId = 0L;

        long comboId = 0L;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 购物车中所有组合都有优惠
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
            comboId = setting.getScopeValue();
            // BigDecimal previousDiscAMT =
            // this.sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
            // 范围整单优惠
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDDISC)){
                discAmount = setting.getSettingValue();

                // 优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTComboPerOrderByAMT(shopCart.getShoppingCartLineCommands(), comboId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }

            // 范围整单折扣
            // 不支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPORDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                // Item折扣到SKU
                discList = shoppingCartmanager.getDiscountAMTComboPerOrderByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, briefListPrevious);
            }
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();

                // Category优惠到SKU
                discList = shoppingCartmanager.getDiscountAMTComboPerItemByAMT(shopCart.getShoppingCartLineCommands(), comboId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTComboPerItemByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, true, briefListPrevious);
                }else{
                    // 整单折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTComboPerItemByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, setting.getOnePieceMark(), briefListPrevious);
                }
            }
            // 范围单件优惠,单件就是降低价格，和QTY无关
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getDiscountAMTComboPerPCSByAMT(shopCart.getShoppingCartLineCommands(), comboId, discAmount, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围单件折扣,单件就是降低价格，和QTY无关
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getDiscountAMTComboPerPCSByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, true, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getDiscountAMTComboPerPCSByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, false, briefListPrevious);
                }
            }
            // 把礼品价格到其他SKU上
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT)){
                int discItemIdQTY = Integer.parseInt(String.valueOf(setting.getSettingValue()));
                if (setting.getMultiplicationFactor() > MULTIPONE)
                    discItemIdQTY = setting.getMultiplicationFactor();

                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark())
                    discItemIdQTY = 1;
                // 把礼品Item价格到其他SKU上
                Integer displayCountLimited = PromotionConstants.GIFTDISPLAYINSHOPCARTLIMITED;
                if (setting.getGiftChoiceType() == GiftChoiceType.NoNeedChoice)
                    displayCountLimited = setting.getGiftCountLimited();
                discList = shoppingCartmanager.getDiscountAMTGiftByComboID(shopId, setting, discItemIdQTY, displayCountLimited);
                discList = initSKUSettingListGiftDiplayType(discList, setting.getGiftChoiceType(), setting.getGiftCountLimited());
            }
            //Markdown一口价
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPMKDNPRICE)){
                discList = shoppingCartmanager.getMarkdownPriceByComboID(shopCart.getShoppingCartLineCommands(), comboId, setting.getMultiplicationFactor(), briefListPrevious);
            }
            // 范围优惠券
            // 不支持倍增，按Rate的折扣券支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPCOUPON)){
                couponTypeId = setting.getSettingValue().longValue();
                couponType2Check = shoppingCartmanager.getCouponTypeByCouponTypeID(couponTypeId);

                filteredCouponsByTypeId = sdkPromotionSettingManager.filterCouponsByCouponTypeId(shopCart.getCouponCodeCommands(), (Long) couponTypeId);
                if (filteredCouponsByTypeId != null && filteredCouponsByTypeId.size() > 0){
                    // 检查couponType
                    // 单品折扣到SKU
                    if (couponType2Check.intValue() == PromotionCoupon.TYPE_RATE.intValue() && setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                        discList = shoppingCartmanager.getDiscountAMTByComboIdCoupon(shopCart.getShoppingCartLineCommands(), comboId, couponTypeId, filteredCouponsByTypeId, true, promotion.getShopId(), briefListPrevious);
                    }else{
                        discList = shoppingCartmanager.getDiscountAMTByComboIdCoupon(shopCart.getShoppingCartLineCommands(), comboId, couponTypeId, filteredCouponsByTypeId, setting.getOnePieceMark(), promotion.getShopId(), briefListPrevious);
                    }
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
                discAmountFetched = detail.getDiscountAmount();
                detail.setDiscountAmount(discAmountFetched);
            }
        }
        return detail;
    }

    private PromotionSettingDetail getSinglePrdPromotionCalculationBriefDetailByCombo(ShoppingCartCommand shopCart,List<ItemFactor> itemFactorList,PromotionCommand promotion,AtomicSetting setting,List<PromotionBrief> briefListPrevious){
        PromotionSettingDetail detail = new PromotionSettingDetail();
        List<PromotionSKUDiscAMTBySetting> discList = null;

        BigDecimal discAmount = BigDecimal.ZERO;
        BigDecimal discRate = BigDecimal.ZERO;

        detail.setSettingTypeTag(setting.getSettingTag());
        detail.setPromotionId(promotion.getPromotionId());
        detail.setPromotionName(promotion.getPromotionName());

        // 购物车中所有组合都有优惠
        if (setting.getScopeTag().equalsIgnoreCase(ItemTagRule.EXP_PREFIX_COMBO)){
            Integer factorDefault = setting.getMultiplicationFactor();
            long comboId = setting.getScopeValue();
            // 范围单品优惠，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，不支持按单件计
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDDISC)){
                discAmount = setting.getSettingValue();
                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerItemByAMT(shopCart.getShoppingCartLineCommands(), comboId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            // 范围单品折扣，基于商品的，和QTY有关，要用QTY到SalesPrice上
            // 支持倍增，支持按单件计才有意义
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPRDRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerItemByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerItemByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSDISC)){
                discAmount = setting.getSettingValue();

                // Item折扣到SKU
                discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerPCSByAMT(shopCart.getShoppingCartLineCommands(), comboId, discAmount, factorDefault, itemFactorList, briefListPrevious);
            }
            if (setting.getSettingTag().equalsIgnoreCase(SettingType.EXP_SCPPCSRATE)){
                discRate = setting.getSettingValue().divide(new BigDecimal(100));
                if (setting.getMultiplicationFactor() > MULTIPONE && setting.getOnePieceMark()){
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerPCSByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, true, factorDefault, itemFactorList, briefListPrevious);
                }else{
                    // 单品折扣到SKU
                    discList = shoppingCartmanager.getSinglePrdDiscountAMTComboPerPCSByRate(shopCart.getShoppingCartLineCommands(), comboId, discRate, setting.getOnePieceMark(), factorDefault, itemFactorList, briefListPrevious);
                }
            }
            if (discList != null && discList.size() > 0){
                discList = sdkPromotionSettingManager.compressMultipSKUSetting2One(discList);
                detail.setAffectSKUDiscountAMTList(discList);
            }
        }
        return detail;
    }

    /**
     * 根据优惠列表取ShopId集合
     */
    private Set<Long> getShopIdListFromPromotionList(List<PromotionCommand> promotionList){
        Set<Long> shopIdList = new HashSet<Long>();
        if (promotionList == null || promotionList.size() == 0){
            return shopIdList;
        }
        for (PromotionCommand one : promotionList){
            shopIdList.add(one.getShopId());
        }
        return shopIdList;
    }

    /**
     * 日志记录
     */
    private void logBriefsByPromotion(List<PromotionBrief> briefListOnePromotion){
        List<PromotionSKUDiscAMTBySetting> skulist = null;
        for (PromotionBrief brief : briefListOnePromotion){
            LOGGER.debug("活动编号：" + brief.getPromotionId().toString() + "，名称:" + brief.getPromotionName() + ",店铺编号：" + brief.getShopId().toString());
            LOGGER.debug("优惠类型：" + brief.getConditionType().toString());
            if (brief.getConditionExpression() != null){
                LOGGER.debug("优惠条件：" + brief.getConditionExpression().toString());
            }
            if (brief.getConditionExpressionComplex() != null && brief.getConditionExpressionComplex() != ""){
                LOGGER.debug("Complex优惠条件：" + brief.getConditionExpressionComplex().toString());
            }

            LOGGER.debug("优惠金额：" + brief.getPromotionAmount().toString());

            LOGGER.debug("活动状态：满足！");

            for (PromotionSettingDetail detail : brief.getDetails()){
                LOGGER.debug("---设置类型:" + detail.getSettingTypeTag() + ",设置表达式：" + detail.getSettingExpression() + "，优惠金额：" + (detail.getDiscountAmount() == null ? "0" : detail.getDiscountAmount()));
                skulist = detail.getAffectSKUDiscountAMTList();
                if (skulist != null && skulist.size() > 0){
                    for (PromotionSKUDiscAMTBySetting setting : skulist){
                        LOGGER.debug(
                                        "------PID:" + setting.getItemId() + "------SKU:" + setting.getSkuId() + ",店铺编号：" + setting.getShopId().toString() + ",名称：" + setting.getItemName() + ",QTY：" + setting.getQty() + ",单价：" + setting.getSalesPrice()
                                                        + "，优惠金额：" + setting.getDiscountAmount().toString());
                    }
                }
            }
        }
    }
}