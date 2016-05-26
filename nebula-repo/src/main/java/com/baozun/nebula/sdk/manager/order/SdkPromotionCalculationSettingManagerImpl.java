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
package com.baozun.nebula.sdk.manager.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.calculateEngine.param.SettingType;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.SettingComplexCommand;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationSettingManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponCodeManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;

@Transactional
@Service("sdkPromotionCalculationSettingManager")
public class SdkPromotionCalculationSettingManagerImpl implements SdkPromotionCalculationSettingManager{

    private static final String                      SETTINGREGX     = "[\\(\\,\\:\\)]";

    @Autowired
    private SdkShoppingCartManager                   shoppingCartmanager;

    @Autowired
    private SdkPromotionCouponCodeManager            sdkPromotionCouponCodeManager;

    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    /** 百分百 **/
    private static final BigDecimal                  HUNDRED_PERCENT = new BigDecimal(1);

    private static final BigDecimal                  ONE_PIECE_QTY   = new BigDecimal(1);

    // nolmt
    // ordamt(200)整单金额大于等于200
    // ordpcs(5)整单件数大于等于5件
    // scpordamt(500,cid:188)男鞋整单金额大于等于500
    // scpordpcs(3,cid:188)男鞋整单件数大于等于3
    // scpprdamt(300,cid:188)男鞋单品金额大于等于300
    // scpprdpcs(2,cid:188)男鞋单品件数大于等于2
    // ordcoupon(2)整单5元券
    // scpcoupon(1,cid:188)男鞋类10元券

    /*
     * 解析表达式，返回原子表达式对象列表 ordamt(3000） | scpordamt(1000,pid:21) |
     * scpcoupon(1,cid:41) | ordcoupon(2)
     */
    @Override
    public List<AtomicSetting> parseSettingByExpression(String expression){
        expression = expression.replaceAll("\\ ", "");
        List<AtomicSetting> list = new ArrayList<AtomicSetting>();
        if (expression == null || expression.isEmpty())
            return list;

        AtomicSetting setting = new AtomicSetting();

        // BigDecimal settingValue = BigDecimal.ZERO;
        String leftExpression = expression;
        String operate = "";
        // int scopeValue = 0;
        // 按照&或|原子分表达式
        String[] expressions = expression.split("[\\&\\|]");
        for (String exp : expressions){
            setting = parseAtomicSettingByExpression(exp);
            setting.setOperateTag(operate);
            leftExpression = leftExpression.replace(exp, "").trim();// 移除处理过的原子，以获得&
                                                                    // |操作符
            if (leftExpression.startsWith("&")){
                operate = "&";
                leftExpression = leftExpression.replaceFirst("\\&", "").trim();
            }
            if (leftExpression.startsWith("|")){
                operate = "|";
                leftExpression = leftExpression.replaceFirst("\\|", "").trim();
            }

            setting.setSettingExpression(exp);
            if (setting != null){
                list.add(setting);
            }
        }

        return list;
    }

    @Override
    public AtomicSetting parseAtomicSettingByExpression(String atomicExpression){
        AtomicSetting setting = new AtomicSetting();
        setting.setSettingExpression(atomicExpression);
        if (atomicExpression.equalsIgnoreCase(SettingType.EXP_FREESHIP)){
            setting.setSettingTag(SettingType.EXP_FREESHIP);
            return setting;
        }

        BigDecimal settingValue = BigDecimal.ZERO;
        boolean onePieceMark = false;

        int scopeValue = 0;
        // orddisc(30,0), scporddisc(5,cmbid:21,0)两种格式
        String[] atomicSplits = atomicExpression.split(SETTINGREGX);
        // orddisc(30,1）必定是这种格式
        if (atomicSplits.length == 3){
            setting.setSettingTag(atomicSplits[0].trim());
            settingValue = new BigDecimal(atomicSplits[1].trim());
            setting.setSettingValue(settingValue);

            if (SettingType.ONEPIECEMARK.contains(atomicSplits[2]))
                onePieceMark = true;
            else
                onePieceMark = false;

            setting.setOnePieceMark(onePieceMark);

            return setting;
        }
        // scpmrkdnprice(cmbid:21，0)
        if (atomicSplits.length == 4){
            setting.setSettingTag(atomicSplits[0].trim());
            setting.setScopeTag(atomicSplits[1]);

            scopeValue = Integer.parseInt(atomicSplits[2]);
            setting.setScopeValue(scopeValue);
            if (SettingType.ONEPIECEMARK.contains(atomicSplits[3]))
                onePieceMark = true;
            else
                onePieceMark = false;

            setting.setOnePieceMark(onePieceMark);

            return setting;
        }
        // scpordrate(5,cmbid:21,1)必定是这种格式
        if (atomicSplits.length >= 5){
            setting.setSettingTag(atomicSplits[0]);
            settingValue = new BigDecimal(atomicSplits[1]);
            setting.setSettingValue(settingValue);

            setting.setScopeTag(atomicSplits[2]);

            if (atomicSplits[2].equalsIgnoreCase(ItemTagRule.EXP_PREFIX_ALLPRODUCT))
                scopeValue = 0;
            else
                scopeValue = Integer.parseInt(atomicSplits[3]);

            setting.setScopeValue(scopeValue);

            if (SettingType.ONEPIECEMARK.contains(atomicSplits[4]))
                onePieceMark = true;
            else
                onePieceMark = false;

            setting.setOnePieceMark(onePieceMark);
            // 倍增因子缺省为1
            setting.setMultiplicationFactor(1);
            setting.setGiftChoiceType(GiftChoiceType.NoNeedChoice);
            if (atomicSplits.length >= 6){
                Integer giftChoiceType = Integer.parseInt(atomicSplits[5]);
                setting.setGiftChoiceType(giftChoiceType);
            }
            if (atomicSplits.length >= 7){
                Integer giftCountLimited = Integer.parseInt(atomicSplits[6]);
                setting.setGiftCountLimited(giftCountLimited);
            }
            return setting;
        }
        return null;
    }

    /**
     * 把优惠设置累计到promotion层上去
     */
    @Override
    public List<PromotionBrief> summarizeSettingToBrief(List<PromotionBrief> briefList){

        if (briefList != null && briefList.size() > 0){
            for (PromotionBrief oneBrief : briefList){
                BigDecimal oneBriefDiscAMT = BigDecimal.ZERO;
                for (PromotionSettingDetail detail : oneBrief.getDetails()){
                    oneBriefDiscAMT = oneBrief.getPromotionAmount() == null ? BigDecimal.ZERO : oneBrief.getPromotionAmount();
                    if (detail.getFreeShippingMark()){
                        oneBrief.setFreeShippingMark(SettingType.EXP_FREESHIP);
                    }
                    oneBriefDiscAMT = oneBriefDiscAMT
                                    .add(detail.getDiscountAmount() == null ? BigDecimal.ZERO : detail.getDiscountAmount());
                    oneBrief.setPromotionAmount(oneBriefDiscAMT);
                }
            }
        }

        return briefList;
    }

    /**
     * 获取一个SKU下优惠金额，因为倍增
     */
    @Override
    public BigDecimal getSKUSettingDiscountAmount(List<PromotionSKUDiscAMTBySetting> list,Long skuId){
        BigDecimal returnValue = BigDecimal.ZERO;
        for (PromotionSKUDiscAMTBySetting one : list){
            if (one.getSkuId() == skuId){
                returnValue = returnValue.add(one.getDiscountAmount() == null ? BigDecimal.ZERO : one.getDiscountAmount());
            }
        }
        return returnValue;
    }

    /**
     * 获取一个SKU下运费，因为倍增
     */
    @Override
    public BigDecimal getSKUSettingFreeShipping(List<PromotionSKUDiscAMTBySetting> list,Long skuId){
        BigDecimal returnValue = BigDecimal.ZERO;
        for (PromotionSKUDiscAMTBySetting one : list){
            if (one.getSkuId() == skuId){
                returnValue = returnValue.add(one.getShippingDiscountAmount() == null ? BigDecimal.ZERO : one.getShippingDiscountAmount());
            }
        }
        return returnValue;
    }

    /**
     * 转换设置表达式为原子类
     * 
     * @see com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationManager#convertComplexSettingToAtomic(java.util.List)
     */
    @Override
    public List<AtomicSetting> convertComplexSettingToAtomic(List<SettingComplexCommand> complexList){
        if (complexList == null || complexList.size() == 0){
            return null;
        }
        List<AtomicSetting> atomicList = new ArrayList<AtomicSetting>();
        for (SettingComplexCommand comp : complexList){
            AtomicSetting atomic = parseAtomicSettingByExpression(comp.getSettingExpression());
            if (atomic != null){
                atomic.setSettingId(comp.getId());
                atomic.setSettingExpression(comp.getSettingExpression());
                atomic.setPromotionId(comp.getPromotionId() == null ? 0 : comp.getPromotionId());
                atomic.setComplexConditionId(comp.getComplexConditionId() == null ? 0 : comp.getComplexConditionId());
                atomicList.add(atomic);
            }
        }
        return atomicList;
    }

    /**
     * 由于倍增，避免原ShoppingCartManager中代码变更太大，每倍增一次，就直接调用原方法一次。
     * 从而导致一个SKU下有倍增因子个优惠记录，其实需要压缩成一条记录，以便shopcart line显示。
     * 
     * @param list
     * @param skuId
     * @return
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> compressMultipSKUSetting2One(List<PromotionSKUDiscAMTBySetting> list){
        PromotionSKUDiscAMTBySetting oneInstance = null;

        List<PromotionSKUDiscAMTBySetting> listReturn = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (PromotionSKUDiscAMTBySetting one : list){
            oneInstance = new PromotionSKUDiscAMTBySetting();
            oneInstance.setShopId(one.getShopId());
            oneInstance.setPromotionId(one.getPromotionId());
            oneInstance.setPromotionName(one.getPromotionName());
            oneInstance.setSettingId(one.getSettingId());
            oneInstance.setSettingTypeTag(one.getSettingTypeTag());
            oneInstance.setSettingName(one.getSettingName());
            oneInstance.setSettingExpression(one.getSettingExpression());
            oneInstance.setSkuId(one.getSkuId());
            oneInstance.setItemId(one.getItemId());
            oneInstance.setCategoryList(one.getCategoryList());
            oneInstance.setComboIds(one.getComboIds());
            oneInstance.setItemName(one.getItemName());
            oneInstance.setSalesPrice(one.getSalesPrice());
            oneInstance.setQty(one.getQty());
            oneInstance.setCouponCodes(one.getCouponCodes());
            oneInstance.setBaseOrder(one.getBaseOrder());
            oneInstance.setFreeShippingMark(one.getFreeShippingMark());
            oneInstance.setGiftMark(one.getGiftMark());
            oneInstance.setGiftChoiceType(one.getGiftChoiceType());
            oneInstance.setGiftCountLimited(one.getGiftCountLimited());
            oneInstance.setSettingId(one.getSettingId());
            oneInstance.setPromotionId(one.getPromotionId());

            if (!checkExistsBySKUId(listReturn, oneInstance.getSkuId())){
                listReturn.add(oneInstance);
            }
        }

        for (PromotionSKUDiscAMTBySetting one : listReturn){
            BigDecimal skuDiscountAMT = getSKUSettingDiscountAmount(list, one.getSkuId());
            one.setDiscountAmount(skuDiscountAMT);

            BigDecimal freeShipping = getSKUSettingFreeShipping(list, one.getSkuId());
            one.setShippingDiscountAmount(freeShipping);
        }

        return listReturn;
    }

    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsByCall(List<PromotionBrief> briefListOnePromotion){
        BigDecimal totalDiscAMT = BigDecimal.ZERO;

        for (PromotionBrief brief : briefListOnePromotion){
            if (brief.getFreeShippingMark() != null && brief.getFreeShippingMark().trim().equalsIgnoreCase(SettingType.EXP_FREESHIP))
                continue;
            if (brief.getDetails() != null && brief.getDetails().size() > 0){
                for (PromotionSettingDetail detail : brief.getDetails()){
                    if (detail.getFreeShippingMark() || detail.getSettingTypeTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT))
                        continue;
                    totalDiscAMT = totalDiscAMT.add(detail.getDiscountAmount());
                }
            }
        }

        return totalDiscAMT;
    }

    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsBaseOrder(List<PromotionBrief> briefListOnePromotion){
        BigDecimal totalDiscAMT = BigDecimal.ZERO;

        for (PromotionBrief brief : briefListOnePromotion){
            if (brief.getFreeShippingMark() != null && brief.getFreeShippingMark().trim().equalsIgnoreCase(SettingType.EXP_FREESHIP))
                continue;
            if (brief.getDetails() != null && brief.getDetails().size() > 0){
                for (PromotionSettingDetail detail : brief.getDetails()){
                    if (detail.getFreeShippingMark() || detail.getSettingTypeTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT))
                        continue;
                    if (detail.getAffectSKUDiscountAMTList() == null || detail.getAffectSKUDiscountAMTList().size() == 0){
                        totalDiscAMT = totalDiscAMT.add(detail.getDiscountAmount());
                    }
                }
            }
        }

        return totalDiscAMT;
    }

    /**
     * 检查优惠设置中，是否已经存在skuId。前提是已经分过店铺的。
     * 
     * @param list
     * @param skuId
     * @return
     */
    private boolean checkExistsBySKUId(List<PromotionSKUDiscAMTBySetting> list,long skuId){
        for (PromotionSKUDiscAMTBySetting one : list){
            if (one.getSkuId() == skuId)
                return true;
        }
        return false;
    }

    /**
     * 分成以ShopId为Key值的数组 同时返回的都是当前时间有效的Coupons实例 以减少查询数据库的次数
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<PromotionCouponCodeCommand>> seperateCouponCodesByShopID(List<String> coupons){
        if (coupons == null || coupons.size() == 0)
            return null;
        List<PromotionCouponCodeCommand> couponList = sdkPromotionCouponCodeManager
                        .findAndCheckPromotionCouponCodeCommandListByCodes(coupons, new Date());
        Map<Long, List<PromotionCouponCodeCommand>> couponListByShopId = new HashMap<Long, List<PromotionCouponCodeCommand>>();
        List<PromotionCouponCodeCommand> oneList = null;
        for (PromotionCouponCodeCommand one : couponList){
            if (couponListByShopId.containsKey(one.getShopId())){
                couponListByShopId.get(one.getShopId()).add(one);
            }else{
                oneList = new ArrayList<PromotionCouponCodeCommand>();
                oneList.add(one);
                couponListByShopId.put(one.getShopId(), oneList);
            }
        }
        return couponListByShopId;
    }

    /**
     * 分成以ShopId为Key值的数组 同时返回的都是当前时间有效的Coupons实例 以减少查询数据库的次数
     */
    @Override
    public List<PromotionCouponCodeCommand> getLeftCouponsKickoffPrevious(
                    List<PromotionCouponCodeCommand> currentCoupons,
                    List<PromotionBrief> kickCoupons){
        if (currentCoupons == null || currentCoupons.size() == 0)
            return null;
        if (kickCoupons == null || kickCoupons.size() == 0)
            return currentCoupons;
        @SuppressWarnings("unchecked")
        List<PromotionCouponCodeCommand> afterKicked = (List<PromotionCouponCodeCommand>) ((ArrayList<PromotionCouponCodeCommand>) currentCoupons)
                        .clone();

        for (PromotionCouponCodeCommand oneCoupon : currentCoupons){
            for (PromotionBrief oneBrief : kickCoupons){
                if (oneBrief.getDetails() == null || oneBrief.getDetails().size() == 0)
                    continue;
                for (PromotionSettingDetail oneSetting : oneBrief.getDetails()){
                    if (oneSetting.getAffectSKUDiscountAMTList() == null || oneSetting.getAffectSKUDiscountAMTList().size() == 0)
                        continue;
                    if (oneSetting.getCouponCodes() != null && oneSetting.getCouponCodes().size() > 0){
                        if (oneSetting.getCouponCodes().contains(oneCoupon.getCouponCode())){
                            afterKicked.remove(oneCoupon);
                        }
                        continue;
                    }
                    for (PromotionSKUDiscAMTBySetting oneSKUSetting : oneSetting.getAffectSKUDiscountAMTList()){
                        if (oneSKUSetting.getCouponCodes() == null || oneSKUSetting.getCouponCodes().size() == 0)
                            continue;
                        if (oneSKUSetting.getCouponCodes().contains(oneCoupon.getCouponCode())){
                            afterKicked.remove(oneCoupon);
                        }
                    }
                }
            }
        }

        return afterKicked;
    }

    /**
     * 从Coupon实体列表中获取，CouponCode Coupon列表中是经过有效性检查的实体
     */
    @Override
    public List<String> getCouponCodeFromCommandList(List<PromotionCouponCodeCommand> couponCommandList){
        if (couponCommandList == null || couponCommandList.size() == 0){
            return null;
        }
        List<String> returnList = new ArrayList<String>();
        for (PromotionCouponCodeCommand one : couponCommandList){
            if (returnList.contains(one.getCouponCode())){
                continue;
            }else{
                returnList.add(one.getCouponCode());
            }
        }
        return returnList;
    }

    /**
     * 根据CouponTypeId，过滤实体列表
     */
    @Override
    public List<PromotionCouponCodeCommand> filterCouponsByCouponTypeId(
                    List<PromotionCouponCodeCommand> couponCommandList,
                    Long couponTypeId){
        if (couponCommandList == null || couponCommandList.size() == 0)
            return null;
        List<PromotionCouponCodeCommand> returnList = new ArrayList<PromotionCouponCodeCommand>();
        for (PromotionCouponCodeCommand one : couponCommandList){
            if (one.getCouponId().longValue() == couponTypeId.longValue())
                returnList.add(one);
        }
        return returnList;
    }

    /**
     * 获取赠品Qty
     */
    @Override
    public Integer getGiftQtyFromPromotionResultBriefsByItemId(List<PromotionBrief> briefListOnePromotion,long itemId){
        Integer itemQty = 0;
        for (PromotionBrief brief : briefListOnePromotion){
            if (brief.getFreeShippingMark() != null && brief.getFreeShippingMark().trim().equalsIgnoreCase(SettingType.EXP_FREESHIP.trim()))
                continue;
            if (brief.getDetails() != null && brief.getDetails().size() > 0){
                for (PromotionSettingDetail detail : brief.getDetails()){
                    if (detail.getFreeShippingMark())
                        continue;

                    if (detail.getSettingTypeTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT) && detail.getAffectSKUDiscountAMTList() != null
                                    && detail.getAffectSKUDiscountAMTList().size() > 0){
                        for (PromotionSKUDiscAMTBySetting skuProvider : detail.getAffectSKUDiscountAMTList()){
                            if (skuProvider.getItemId() == itemId){
                                itemQty = itemQty + skuProvider.getQty();
                            }
                        }
                    }
                }
            }
        }
        return itemQty;
    }

    /**
     * 分摊后，按ComboId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(
                    ShoppingCartCommand shopCart,
                    List<PromotionBrief> briefListPrevious,
                    long comboId){
        BigDecimal comboDiscAMT = BigDecimal.ZERO;

        if (briefListPrevious == null || briefListPrevious.size() <= 0)
            return BigDecimal.ZERO;

        List<PromotionSKUDiscAMTBySetting> skuShareByOrderBase = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shopCart, briefListPrevious);
        if (skuShareByOrderBase != null && skuShareByOrderBase.size() > 0){
            for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
                Set<String> comboIds = line.getComboIds();
                if (null == line.getSkuId() || line.isGift() || null == comboIds || comboIds.size() == 0){
                    continue;
                }
                if (comboIds != null && comboIds.contains(String.valueOf(comboId))){
                    for (PromotionSKUDiscAMTBySetting skuShare : skuShareByOrderBase){
                        if (skuShare.getFreeShippingMark() || skuShare.getGiftMark())
                            continue;
                        if (line.getSkuId() == skuShare.getSkuId()){
                            comboDiscAMT = comboDiscAMT.add(skuShare.getDiscountAmount());
                        }
                    }
                }
            }
        }

        return comboDiscAMT;
    }

    /**
     * 分摊后，按ItemId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(
                    ShoppingCartCommand shopCart,
                    List<PromotionBrief> briefListPrevious,
                    long ItemId){
        BigDecimal itemDiscAMT = BigDecimal.ZERO;
        if (briefListPrevious == null || briefListPrevious.size() <= 0)
            return BigDecimal.ZERO;

        List<PromotionSKUDiscAMTBySetting> skuShareByOrderBase = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shopCart, briefListPrevious);
        if (skuShareByOrderBase != null && skuShareByOrderBase.size() > 0){
            for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
                Set<String> comboIds = line.getComboIds();
                if (null == line.getSkuId() || line.isGift() || null == comboIds || comboIds.size() == 0){
                    continue;
                }
                if (line.getItemId() != null && line.getItemId() == ItemId){
                    for (PromotionSKUDiscAMTBySetting skuShare : skuShareByOrderBase){
                        if (skuShare.getFreeShippingMark() || skuShare.getGiftMark())
                            continue;
                        if (line.getSkuId() == skuShare.getSkuId()){
                            itemDiscAMT = itemDiscAMT.add(skuShare.getDiscountAmount());
                        }
                    }
                }
            }
        }

        return itemDiscAMT;
    }

    /**
     * 分摊后，按CategoryId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(
                    ShoppingCartCommand shopCart,
                    List<PromotionBrief> briefListPrevious,
                    long CategoryId){
        BigDecimal categoryDiscAMT = BigDecimal.ZERO;
        if (briefListPrevious == null || briefListPrevious.size() <= 0)
            return BigDecimal.ZERO;

        List<PromotionSKUDiscAMTBySetting> skuShareByOrderBase = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shopCart, briefListPrevious);
        if (skuShareByOrderBase != null && skuShareByOrderBase.size() > 0){
            for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
                Set<String> comboIds = line.getComboIds();
                if (null == line.getSkuId() || line.isGift() || null == comboIds || comboIds.size() == 0){
                    continue;
                }
                if (line.getCategoryList() != null && line.getCategoryList().contains(CategoryId)){
                    for (PromotionSKUDiscAMTBySetting skuShare : skuShareByOrderBase){
                        if (skuShare.getFreeShippingMark() || skuShare.getGiftMark())
                            continue;
                        if (line.getSkuId() == skuShare.getSkuId()){
                            categoryDiscAMT = categoryDiscAMT.add(skuShare.getDiscountAmount());
                        }
                    }
                }
            }
        }

        return categoryDiscAMT;
    }

    /**
     * 分摊后，按SKUId获取行上的优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                    ShoppingCartCommand shopCart,
                    List<PromotionBrief> briefListPrevious,
                    long skuId){
        BigDecimal skuDiscAMT = BigDecimal.ZERO;
        if (briefListPrevious == null || briefListPrevious.size() <= 0)
            return BigDecimal.ZERO;

        List<PromotionSKUDiscAMTBySetting> skuShareByOrderBase = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shopCart, briefListPrevious);
        if (skuShareByOrderBase != null && skuShareByOrderBase.size() > 0){
            for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
                Set<String> comboIds = line.getComboIds();
                if (null == line.getSkuId() || line.isGift() || null == comboIds || comboIds.size() == 0){
                    continue;
                }
                if (line.getSkuId() == skuId){
                    for (PromotionSKUDiscAMTBySetting skuShare : skuShareByOrderBase){
                        if (skuShare.getFreeShippingMark() || skuShare.getGiftMark())
                            continue;
                        if (line.getSkuId() == skuShare.getSkuId()){
                            skuDiscAMT = skuDiscAMT.add(skuShare.getDiscountAmount());
                        }
                    }
                }
            }
        }

        return skuDiscAMT;
    }

    /**
     * 分摊后，按ItemId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(
                    List<ShoppingCartLineCommand> shopCartLines,
                    List<PromotionBrief> briefListPrevious,
                    long ItemId){
        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
        shoppingCart.setShoppingCartLineCommands(shopCartLines);
        return getDiscAMTFromPromotionResultBriefsAfterSharedByItemId(shoppingCart, briefListPrevious, ItemId);
    }

    /**
     * 分摊后，按CategoryId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(
                    List<ShoppingCartLineCommand> shopCartLines,
                    List<PromotionBrief> briefListPrevious,
                    long CategoryId){
        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
        shoppingCart.setShoppingCartLineCommands(shopCartLines);
        return getDiscAMTFromPromotionResultBriefsAfterSharedByCategoryId(shoppingCart, briefListPrevious, CategoryId);
    }

    /**
     * 分摊后，按ComboId获取优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(
                    List<ShoppingCartLineCommand> shopCartLines,
                    List<PromotionBrief> briefListPrevious,
                    long comboId){
        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
        shoppingCart.setShoppingCartLineCommands(shopCartLines);
        return getDiscAMTFromPromotionResultBriefsAfterSharedByComboId(shoppingCart, briefListPrevious, comboId);
    }

    /**
     * 分摊后，按SKUId获取行上的优惠金额
     */
    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                    List<ShoppingCartLineCommand> shopCartLines,
                    List<PromotionBrief> briefListPrevious,
                    long skuId){
        ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
        shoppingCart.setShoppingCartLineCommands(shopCartLines);
        return getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(shoppingCart, briefListPrevious, skuId);
    }

    @Override
    public BigDecimal getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(
                    List<ShoppingCartLineCommand> lines,
                    List<PromotionBrief> briefListPrevious,
                    List<Long> customItemIdList){
        BigDecimal customDiscAMT = BigDecimal.ZERO;

        if (briefListPrevious == null || briefListPrevious.size() <= 0)
            return BigDecimal.ZERO;
        if (lines == null || lines.size() <= 0)
            return BigDecimal.ZERO;
        ShoppingCartCommand shopCart = new ShoppingCartCommand();
        shopCart.setShoppingCartLineCommands(lines);

        List<PromotionSKUDiscAMTBySetting> skuShareByOrderBase = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shopCart, briefListPrevious);
        if (skuShareByOrderBase != null && skuShareByOrderBase.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                if (null == line.getItemId() || line.isGift())
                    continue;
                if (customItemIdList.contains(line.getItemId())){
                    for (PromotionSKUDiscAMTBySetting skuShare : skuShareByOrderBase){
                        if (skuShare.getFreeShippingMark() || skuShare.getGiftMark())
                            continue;
                        if (line.getSkuId() == skuShare.getSkuId()){
                            customDiscAMT = customDiscAMT.add(skuShare.getDiscountAmount());
                        }
                    }
                }
            }
        }

        return customDiscAMT;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerOrderByAMT(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal skuPreviousDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedPay = BigDecimal.ZERO;
        BigDecimal lineShareDisc = BigDecimal.ZERO;

        BigDecimal customDiscAMT = BigDecimal.ZERO;
        customDiscAMT = BigDecimal.valueOf(factor).multiply(discAmount);

        BigDecimal customOriginal = shoppingCartmanager.getNeedToPayAmountInShoppingCartByCustomItemIds(lines, customItemIdList);
        BigDecimal customPreviousDisc = getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(
                        lines,
                        briefListPrevious,
                        customItemIdList);

        BigDecimal customNeedPay = customOriginal.subtract(customPreviousDisc);
        if (customNeedPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isGift())
                continue;
            // 判断line是否在该custom下
            // 判断是否是custom下的行
            if (customItemIdList.contains(line.getItemId())){
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                lineShareDisc = customDiscAMT.multiply(lineNeedPay).divide(customNeedPay, 2, BigDecimal.ROUND_HALF_EVEN);
                settingList.add(shoppingCartmanager.getPromotionSkuAMTSetting(line, lineShareDisc));
            }
        }

        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerOrderByRate(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
                    BigDecimal discRate,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;

        BigDecimal customNeedToPay = BigDecimal.ZERO;
        BigDecimal customDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal customOriginNeedToPay = BigDecimal.ZERO;
        //BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        customNeedToPay = shoppingCartmanager.getCustomAmount(customItemIdList, lines);
        if (customNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        //BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(
                        lines,
                        briefListPrevious,
                        customItemIdList);

        customOriginNeedToPay = customNeedToPay;
        //totalOriginNeedToPay = getAllAmount(lines);

        //previousDiscAMT = previousDiscAMT.multiply(comboOriginNeedToPay).divide(totalOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
        customNeedToPay = customNeedToPay.subtract(previousDiscAMT);
        if (customNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        customDiscAMT = customNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            if (line.isGift() || null == line.getItemId()){
                continue;
            }

            if (customItemIdList.contains(line.getItemId())){
                lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());

                previousDiscAMTSKU = getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                    continue;
                //lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                lineDiscAMT = customDiscAMT.multiply(lineNeedToPay).divide(customOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(shoppingCartmanager.getPromotionSkuAMTSetting(line, lineDiscAMT));
                continue;
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerItemByAMT(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
                    BigDecimal discAmount,
                    Integer factor,
                    List<PromotionBrief> briefListPrevious){
        if (null == lines || lines.size() == 0)
            return null;
        if (null == customItemIdList)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();

        for (Long itemId : customItemIdList){
            settingOne = this.shoppingCartmanager.getDiscountAMTItemPerItemByAMT(lines, itemId, discAmount, factor, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }

        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerItemByRate(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        BigDecimal customNeedToPay = BigDecimal.ZERO;
        BigDecimal customDiscAMT = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal lineDiscAMT = BigDecimal.ZERO;
        BigDecimal customOriginNeedToPay = BigDecimal.ZERO;
        //BigDecimal totalOriginNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        customNeedToPay = this.shoppingCartmanager.getCustomAmount(customItemIdList, lines);
        if (customNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        //BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);
        BigDecimal previousDiscAMT = getDiscAMTFromPromotionResultBriefsAfterSharedByCustomItemIds(
                        lines,
                        briefListPrevious,
                        customItemIdList);

        customOriginNeedToPay = customNeedToPay;
        //totalOriginNeedToPay = getAllAmount(lines);
        //previousDiscAMT = previousDiscAMT.multiply(comboOriginNeedToPay).divide(totalOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
        customNeedToPay = customNeedToPay.subtract(previousDiscAMT);
        if (customNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;
        customDiscAMT = customNeedToPay.multiply(HUNDRED_PERCENT.subtract(discRate));

        Set<Long> itemIds = new HashSet<Long>();
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (ShoppingCartLineCommand line : lines){
            // 判断line是否在该combo下
            if (line.isGift() || null == line.getItemId()){
                continue;
            }

            if (customItemIdList.contains(line.getItemId())){
                if (itemIds.contains(line.getItemId()))
                    continue;
                itemIds.add(line.getItemId());

                if (onePieceMark){
                    lineDiscAMT = ONE_PIECE_QTY.multiply(line.getSalePrice()).multiply(HUNDRED_PERCENT.subtract(discRate));
                }else{
                    lineNeedToPay = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());

                    previousDiscAMTSKU = getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                    if (previousDiscAMTSKU.compareTo(lineNeedToPay) >= 0)
                        continue;
                    //lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);

                    lineDiscAMT = customDiscAMT.multiply(lineNeedToPay).divide(customOriginNeedToPay, 2, BigDecimal.ROUND_HALF_EVEN);
                }
                lineDiscAMT = lineDiscAMT.setScale(2, BigDecimal.ROUND_HALF_UP);
                settingList.add(this.shoppingCartmanager.getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerPCSByAMT(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
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
            if (null == line.getItemId() || line.isGift()){
                continue;
            }
            // 计算该combo下的金额优惠,By PCS
            if (customItemIdList.contains(line.getItemId())){
                BigDecimal curLineDiscAmount = BigDecimal.valueOf(line.getQuantity()).multiply(discAmount);
                // 如果行上现有的优惠已经超过行实付时，跳到下一个行
                skuPreviousDiscAMT = getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(lines, briefListPrevious, line.getSkuId());
                lineNeedPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                if (skuPreviousDiscAMT.compareTo(lineNeedPay) >= 0)
                    continue;
                else
                    lineNeedPay = lineNeedPay.subtract(skuPreviousDiscAMT);

                if (lineNeedPay.compareTo(curLineDiscAmount.multiply(BigDecimal.valueOf(factor))) > 0)
                    lineDiscAMT = curLineDiscAmount.multiply(BigDecimal.valueOf(factor));
                else
                    lineDiscAMT = lineNeedPay;

                settingList.add(this.shoppingCartmanager.getPromotionSkuAMTSetting(line, lineDiscAMT));
            }
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTCustomPerPCSByRate(
                    List<ShoppingCartLineCommand> lines,
                    List<Long> customItemIdList,
                    BigDecimal discRate,
                    boolean onePieceMark,
                    List<PromotionBrief> briefListPrevious){
        // 折扣优惠计算方式一样
        if (null == lines || lines.size() == 0)
            return null;
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        List<PromotionSKUDiscAMTBySetting> settingOne = new ArrayList<PromotionSKUDiscAMTBySetting>();

        if (null == customItemIdList)
            return null;
        for (Long itemId : customItemIdList){
            settingOne = this.shoppingCartmanager.getDiscountAMTItemPerPCSByRate(lines, itemId, discRate, onePieceMark, briefListPrevious);
            if (null != settingOne)
                settingList.addAll(settingOne);
        }
        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTGiftByCustomItemIds(
                    long shopId,
                    AtomicSetting setting,
                    Integer qty,
                    Integer displayCountLimited){
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        // 礼品不计价，只显示名称
        // long shopId,赠品要分店铺的
        // ToDo Simoncheng
        long customId = setting.getScopeValue();
        List<Long> customItemIds = new ArrayList<Long>();
        customItemIds = SdkCustomizeFilterLoader.load(String.valueOf(customId));
        if (null == customItemIds)
            return null;

        settingList = this.shoppingCartmanager.getDiscountAMTGiftByItemList(customItemIds, shopId, setting, qty, displayCountLimited);

        return settingList;
    }

    @Override
    public List<PromotionSKUDiscAMTBySetting> getDiscountAMTByCustomItemIdsCoupon(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> customItemIdList,
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
        BigDecimal customNeedToPay = BigDecimal.ZERO;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        BigDecimal previousDiscAMTSKU = BigDecimal.ZERO;

        customNeedToPay = this.shoppingCartmanager.getCustomAmount(customItemIdList, shoppingCartLines);

        if (customNeedToPay.compareTo(BigDecimal.ZERO) <= 0)
            return null;

        // 判断购物车行是否有comboId下的商品
        List<PromotionSKUDiscAMTBySetting> settingList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        boolean flag = checkCustomInShoppingCartLines(shoppingCartLines, customItemIdList);
        if (!flag)
            return null;
        // TODO Change coupon to On Line Coupon
        // 如果行上有Coupon，直接用行上的Coupon覆盖掉order coupon
        boolean flagOnLine = checkOnLineCouponByCustomItemIds(shoppingCartLines, customItemIdList, couponTypeId, shopId);
        //BigDecimal previousDiscAMT = sdkPromotionSettingManager.getDiscAMTFromPromotionResultBriefsBaseOrder(briefListPrevious);

        ShoppingCartLineCommand maxLine = getMaxLineNeedToPayByCustomItemIds(shoppingCartLines, customItemIdList);
        if (maxLine == null)
            return null;
        // 根据couponCodes、couponTypeToCheck获取金额
        if (flagOnLine && maxLine.getCouponCodeOnLine() != null){
            couponCodes = Arrays.asList(maxLine.getCouponCodeOnLine());
        }
        if (onePieceMark){
            usedCouponList = this.shoppingCartmanager
                            .getCouponTotalAmount(couponCodes, couponTypeId, ONE_PIECE_QTY.multiply(maxLine.getSalePrice()), shopId);
        }else{
            lineNeedToPay = new BigDecimal(maxLine.getQuantity()).multiply(maxLine.getSalePrice());

            previousDiscAMTSKU = getDiscAMTFromPromotionResultBriefsAfterSharedBySKUId(
                            shoppingCartLines,
                            briefListPrevious,
                            maxLine.getSkuId());

            lineNeedToPay = lineNeedToPay.subtract(previousDiscAMTSKU);
            usedCouponList = this.shoppingCartmanager.getCouponTotalAmount(couponCodes, couponTypeId, lineNeedToPay, shopId);
        }

        BigDecimal couponAmt = this.shoppingCartmanager.getCouponAmtFromUsedList(usedCouponList);
        couponAmt = couponAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
        PromotionSKUDiscAMTBySetting skuSetting = this.shoppingCartmanager.getPromotionSkuAMTSetting(maxLine, couponAmt);
        if (onePieceMark)
            skuSetting.setQty(ONE_PIECE_QTY.intValue());
        skuSetting.setCouponCodes(usedCouponList.keySet());

        settingList.add(skuSetting);

        return settingList;
    }

    private boolean checkCustomInShoppingCartLines(List<ShoppingCartLineCommand> lines,List<Long> customItemIdList){
        if (null == customItemIdList || customItemIdList.size() == 0)
            return false;
        for (ShoppingCartLineCommand line : lines){
            if (null == line.getItemId() || line.isGift())
                continue;
            // 检查购物车中是否包含该combo下的商品
            if (customItemIdList.contains(line.getItemId()))
                return true;
        }
        return false;
    }

    public boolean checkOnLineCouponByCustomItemIds(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> customItemIdList,
                    long couponTypeID,
                    long shopId){
        if (null == customItemIdList || null == shoppingCartLines)
            return false;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (null == line.getItemId() || line.isGift())
                continue;
            if (customItemIdList.contains(line.getItemId())){
                PromotionCouponCodeCommand couponOnLine = line.getCouponCodeOnLine();
                if (couponOnLine != null && couponOnLine.getCouponId() == couponTypeID && couponOnLine.getShopId() == shopId)
                    return true;
            }
        }
        return false;
    }

    private ShoppingCartLineCommand getMaxLineNeedToPayByCustomItemIds(
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    List<Long> customItemIdList){
        if (null == customItemIdList || null == shoppingCartLines)
            return null;
        BigDecimal lineNeedToPay = BigDecimal.ZERO;
        ShoppingCartLineCommand maxLine = null;
        for (ShoppingCartLineCommand line : shoppingCartLines){
            if (null == line.getItemId() || line.isGift())
                continue;
            if (customItemIdList.contains(line.getItemId())){
                if (BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice()).compareTo(lineNeedToPay) > 0){
                    lineNeedToPay = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
                    maxLine = line;
                }
            }
        }
        return maxLine;
    }
}
