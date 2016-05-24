package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.SettingType;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;

@Transactional
@Service("sdkPromotionCalculationShareToSKUManager")
public class SdkPromotionCalculationShareToSKUManagerImp implements SdkPromotionCalculationShareToSKUManager{

    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    /**
     * 获取整单优惠在行上的分摊
     */
    @Override
    public BigDecimal getLineDiscAMTShareOnOrderBaseByShopIdAndSKUId(List<PromotionSKUDiscAMTBySetting> shareList,Long shopId,Long skuId){
        BigDecimal discAMT = BigDecimal.ZERO;

        if (shareList == null || shareList.size() == 0)
            return discAMT;
        for (PromotionSKUDiscAMTBySetting skuSet : shareList){
            if (skuSet.getBaseOrder() == true && skuSet.getShopId().longValue() == shopId.longValue()
                            && skuSet.getSkuId().longValue() == skuId.longValue()){
                if (skuSet.getFreeShippingMark() != true && skuSet.getGiftMark() != true){
                    discAMT = discAMT.add(skuSet.getDiscountAmount());
                }
            }
        }
        return discAMT;
    }

    /*
     * 购物车分店铺，促销活动分店铺 把活动优惠的总金额加权平均到每一行上去 以便保存到PromotionLine表中去
     * 
     * @see
     * com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager
     * #sharePromotionDiscountToEachLine
     * (com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * java.util.List)
     */
    @Override
    public List<PromotionSKUDiscAMTBySetting> sharePromotionDiscountToEachLine(ShoppingCartCommand shopCart,List<PromotionBrief> briefList){
        // 按店铺取购物车
        Long oneShopId = 0L;
        for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
            if (null != line.getShopId() && line.getShopId() > 0L){
                oneShopId = line.getShopId();
                break;
            }
        }

        Map<Long, ShoppingCartCommand> shopCartAll = shopCart.getShoppingCartByShopIdMap();
        if (shopCartAll == null || shopCartAll.size() == 0){
            shopCartAll = new HashMap<Long, ShoppingCartCommand>();
            shopCartAll.put(oneShopId, shopCart);
        }

        // 取促销列表中的店铺列表
        Map<Long, List<PromotionBrief>> shopIdSetBriefList = getShopIdListFromPromotionBriefList(briefList);
        if (shopIdSetBriefList == null || shopIdSetBriefList.size() == 0){
            return null;
        }

        List<PromotionSKUDiscAMTBySetting> allPromotionShareBySKU = new ArrayList<PromotionSKUDiscAMTBySetting>();

        // 按店铺分摊自己店铺的促销到店铺自己的订单行上
        for (Entry<Long, List<PromotionBrief>> entry : shopIdSetBriefList.entrySet()){
            Long shopId = entry.getKey();
            briefList = entry.getValue();
            List<PromotionSettingDetail> allPromotionShareBySKULineBase = null;

            // 按活动遍历
            for (PromotionBrief onePromotion : briefList){
                if (onePromotion.getPromotionAmount() == null || onePromotion.getPromotionAmount().compareTo(BigDecimal.ZERO) <= 0)
                    continue;
                for (PromotionSettingDetail detail : onePromotion.getDetails()){
                    if (detail.getFreeShippingMark() || detail.getAffectSKUDiscountAMTList() == null
                                    || detail.getAffectSKUDiscountAMTList().size() == 0)
                        continue;
                    if (allPromotionShareBySKULineBase == null)
                        allPromotionShareBySKULineBase = new ArrayList<PromotionSettingDetail>();
                    allPromotionShareBySKULineBase.add(detail);
                }

                ShoppingCartCommand shopCartOne = shopCartAll.get(shopId);// 获取购物车根据店铺编号

                List<PromotionSettingDetail> detailListOrderBase = null;
                List<PromotionSettingDetail> detailListLineBase = null;
                List<PromotionSettingDetail> detailListShippingBase = null;

                for (PromotionSettingDetail detail : onePromotion.getDetails()){
                    // 免运费
                    if (detail.getFreeShippingMark()){
                        if (detailListShippingBase == null)
                            detailListShippingBase = new ArrayList<PromotionSettingDetail>();
                        detailListShippingBase.add(detail);
                    }else if (detail.getAffectSKUDiscountAMTList() == null || detail.getAffectSKUDiscountAMTList().size() == 0){
                        if (detailListOrderBase == null)
                            detailListOrderBase = new ArrayList<PromotionSettingDetail>();
                        detailListOrderBase.add(detail);
                    }else{// 行优惠
                        if (detailListLineBase == null)
                            detailListLineBase = new ArrayList<PromotionSettingDetail>();
                        detailListLineBase.add(detail);
                    }
                }

                List<PromotionSKUDiscAMTBySetting> onePromotionShareBySKUShippingBase = null;
                List<PromotionSKUDiscAMTBySetting> onePromotionShareBySKUOrderBase = null;
                List<PromotionSKUDiscAMTBySetting> onePromotionShareBySKULineBase = null;

                if (detailListShippingBase != null && detailListShippingBase.size() > 0){
                    onePromotionShareBySKUShippingBase = convertLinePromotionSetting(shopCartOne, detailListShippingBase);
                }
                if (detailListLineBase != null && detailListLineBase.size() > 0){
                    onePromotionShareBySKULineBase = convertLinePromotionSetting(shopCartOne, detailListLineBase);
                }
                if (detailListOrderBase != null && detailListOrderBase.size() > 0){
                    onePromotionShareBySKUOrderBase = sharePromotionDiscountToEachLineByShop(
                                    shopCartOne,
                                    detailListOrderBase,
                                    allPromotionShareBySKULineBase);
                }

                if (onePromotionShareBySKUShippingBase != null && onePromotionShareBySKUShippingBase.size() > 0){
                    for (PromotionSKUDiscAMTBySetting one : onePromotionShareBySKUShippingBase){
                        one.setShopId(onePromotion.getShopId());
                        one.setPromotionId(onePromotion.getPromotionId());
                        one.setPromotionName(onePromotion.getPromotionName());
                        one.setPromotionType(onePromotion.getConditionType());
                        one.setFreeShippingMark(true);
                        one.setBaseOrder(true);
                    }
                    allPromotionShareBySKU.addAll(onePromotionShareBySKUShippingBase);
                    onePromotionShareBySKUShippingBase = null;
                }
                if (onePromotionShareBySKUOrderBase != null && onePromotionShareBySKUOrderBase.size() > 0){
                    for (PromotionSKUDiscAMTBySetting one : onePromotionShareBySKUOrderBase){
                        one.setShopId(onePromotion.getShopId());
                        one.setPromotionId(onePromotion.getPromotionId());
                        one.setPromotionName(onePromotion.getPromotionName());
                        one.setPromotionType(onePromotion.getConditionType());
                        one.setFreeShippingMark(false);
                        one.setBaseOrder(true);
                    }
                    allPromotionShareBySKU.addAll(onePromotionShareBySKUOrderBase);
                    onePromotionShareBySKUOrderBase = null;
                }
                if (onePromotionShareBySKULineBase != null && onePromotionShareBySKULineBase.size() > 0){
                    for (PromotionSKUDiscAMTBySetting one : onePromotionShareBySKULineBase){
                        one.setShopId(onePromotion.getShopId());
                        one.setPromotionId(onePromotion.getPromotionId());
                        one.setPromotionName(onePromotion.getPromotionName());
                        one.setPromotionType(onePromotion.getConditionType());
                        one.setFreeShippingMark(false);
                        one.setBaseOrder(false);
                    }
                    allPromotionShareBySKU.addAll(onePromotionShareBySKULineBase);
                    onePromotionShareBySKULineBase = null;
                }
            }
        }
        return allPromotionShareBySKU;
    }

    private List<PromotionSKUDiscAMTBySetting> convertLinePromotionSetting(
                    ShoppingCartCommand shopCart,
                    List<PromotionSettingDetail> settingList){
        List<PromotionSKUDiscAMTBySetting> skuDiscAMTList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        for (PromotionSettingDetail set : settingList){
            if (set.getAffectSKUDiscountAMTList() != null && set.getAffectSKUDiscountAMTList().size() > 0){
                skuDiscAMTList.addAll(set.getAffectSKUDiscountAMTList());
            }else{
                PromotionSKUDiscAMTBySetting shippingSetting = new PromotionSKUDiscAMTBySetting();
                shippingSetting.setShopId(set.getShopId());
                shippingSetting.setPromotionId(set.getPromotionId());
                shippingSetting.setCouponCodes(set.getCouponCodes());
                shippingSetting.setDiscountAmount(set.getDiscountAmount());
                shippingSetting.setFreeShippingMark(set.getFreeShippingMark());
                shippingSetting.setPromotionName(set.getPromotionName());
                shippingSetting.setSettingName(set.getSettingName());
                shippingSetting.setSettingTypeTag(set.getSettingTypeTag());

                skuDiscAMTList.add(shippingSetting);
            }
        }
        return skuDiscAMTList;
    }

    private BigDecimal getDiscAMTFromPromotionSKUDiscAMTBySettingsBySKUId(
                    List<PromotionSettingDetail> skuBaseDiscAMTSettingList,
                    long skuId){
        BigDecimal skuDisc = BigDecimal.ZERO;
        if (null == skuBaseDiscAMTSettingList || skuBaseDiscAMTSettingList.size() == 0)
            return skuDisc;
        for (PromotionSettingDetail skuLlist : skuBaseDiscAMTSettingList){
            for (PromotionSKUDiscAMTBySetting skuSetting : skuLlist.getAffectSKUDiscountAMTList()){
                if (skuSetting.getSkuId() == skuId)
                    skuDisc = skuDisc.add(skuSetting.getDiscountAmount());
            }
        }
        return skuDisc;
    }

    private Integer getNoNeedShareLineCount(ShoppingCartCommand shopCart,List<PromotionSettingDetail> skuBaseDiscAMTSettingList){
        Integer linesCount = 0;
        for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
            if (line.isGift() || line.isCaptionLine()){
                continue;
            }
            BigDecimal skuNeed = BigDecimal.valueOf(line.getQuantity()).multiply(line.getSalePrice());
            BigDecimal skuDisc = getDiscAMTFromPromotionSKUDiscAMTBySettingsBySKUId(skuBaseDiscAMTSettingList, line.getSkuId());
            if (skuDisc.compareTo(skuNeed) >= 0){
                linesCount = linesCount + 1;
            }
        }
        return linesCount;
    }

    private List<PromotionSKUDiscAMTBySetting> sharePromotionDiscountToEachLineByShop(
                    ShoppingCartCommand shopCart,
                    List<PromotionSettingDetail> orderBaseSettingList,
                    List<PromotionSettingDetail> skuBaseDiscAMTSettingList){
        BigDecimal totalDiscountAMT = BigDecimal.ZERO;// 总优惠金额
        BigDecimal skuDiscountAmount = BigDecimal.ZERO;// 行分摊优惠
        BigDecimal skuDiscountAmountSum = BigDecimal.ZERO;// 行分摊优惠
        BigDecimal skuAmount = BigDecimal.ZERO;// 行金额，以便计算占比
        BigDecimal skuDiscAmtOnLine = BigDecimal.ZERO;
        Integer linesStep = 0;
        Integer linesCount = shopCart.getShoppingCartLineCommands().size();// 分摊会有四舍五入，累计金额可能和总优惠金额有几分钱的出入，所以会把这个出入放到最后一个line上找平
        Integer linesNoNeedShare = 0;

        linesNoNeedShare = getNoNeedShareLineCount(shopCart, skuBaseDiscAMTSettingList);
        linesCount = linesCount - linesNoNeedShare;

        PromotionSKUDiscAMTBySetting skuShare = null;
        List<PromotionSKUDiscAMTBySetting> skuDiscAMTList = new ArrayList<PromotionSKUDiscAMTBySetting>();
        BigDecimal totalOriginPay = sdkShoppingCartManager.getAllAmount(shopCart.getShoppingCartLineCommands());
        BigDecimal totalOriginPayBaseSKU = BigDecimal.ZERO;
        if (skuBaseDiscAMTSettingList != null){
            for (PromotionSettingDetail sku : skuBaseDiscAMTSettingList){
                if (sku.getSettingTypeTag().equalsIgnoreCase(SettingType.EXP_SCPGIFT))
                    continue;
                totalOriginPayBaseSKU = totalOriginPayBaseSKU.add(sku.getDiscountAmount());
            }
        }
        totalOriginPay = totalOriginPay.subtract(totalOriginPayBaseSKU);

        if (totalOriginPay == null || totalOriginPay.equals(BigDecimal.ZERO))
            return null;

        if (orderBaseSettingList == null){
            return null;
        }else{
            for (PromotionSettingDetail oneSetting : orderBaseSettingList){
                totalDiscountAMT = totalDiscountAMT.add(oneSetting.getDiscountAmount());
            }
        }
        if (totalDiscountAMT.compareTo(BigDecimal.ZERO) < 0)
            return null;
        for (ShoppingCartLineCommand line : shopCart.getShoppingCartLineCommands()){
            if (line.isCaptionLine())
                continue;
            if (line.isGift()){
                linesCount = linesCount - 1;
                continue;
            }
            skuAmount = new BigDecimal(line.getQuantity()).multiply(line.getSalePrice());
            skuDiscAmtOnLine = getDiscAMTFromPromotionSKUDiscAMTBySettingsBySKUId(skuBaseDiscAMTSettingList, line.getSkuId());
            if (skuAmount.compareTo(skuDiscAmtOnLine) <= 0){
                linesCount = linesCount - 1;
                continue;
            }

            skuAmount = skuAmount.subtract(skuDiscAmtOnLine);

            // SKU promotion share equals totalDiscountAMT * (QTY * SalesPrice)
            // / totalOriginPay
            skuDiscountAmount = totalDiscountAMT.multiply(skuAmount).divide(totalOriginPay, 2, RoundingMode.HALF_UP);

            linesStep = linesStep + 1;
            skuDiscountAmountSum = skuDiscountAmountSum.add(skuDiscountAmount);

            // 最后一行，找平逻辑
            if (linesStep == linesCount){
                if (skuDiscountAmountSum.compareTo(totalDiscountAMT) != 0)
                    skuDiscountAmount = totalDiscountAMT.subtract(skuDiscountAmountSum.subtract(skuDiscountAmount));
            }

            skuShare = new PromotionSKUDiscAMTBySetting();

            skuShare.setSkuId(line.getSkuId());
            skuShare.setItemName(line.getItemName());
            skuShare.setItemId(line.getItemId());
            skuShare.setQty(line.getQuantity());
            skuShare.setSalesPrice(line.getSalePrice());
            skuShare.setDiscountAmount(skuDiscountAmount);

            skuDiscAMTList.add(skuShare);
        }
        for (PromotionSettingDetail oneSetting : orderBaseSettingList){
            for (PromotionSKUDiscAMTBySetting setting : skuDiscAMTList){
                setting.setFreeShippingMark(oneSetting.getFreeShippingMark());
                if (oneSetting.getCouponCodes() != null && oneSetting.getCouponCodes().size() > 0)
                    setting.setCouponCodes(oneSetting.getCouponCodes());
                if (oneSetting.getAffectSKUDiscountAMTList() != null && oneSetting.getAffectSKUDiscountAMTList().size() > 0){
                    for (PromotionSKUDiscAMTBySetting innerSKU : oneSetting.getAffectSKUDiscountAMTList()){
                        if (innerSKU.getCouponCodes() != null && innerSKU.getCouponCodes().size() > 0){
                            setting.setCouponCodes(innerSKU.getCouponCodes());
                        }
                    }
                }
            }
        }
        // 按活动，把个活动的优惠金额，分摊到SKU上
        return skuDiscAMTList;
    }

    /**
     * 根据优惠列表取ShopId集合
     * 
     * @param promotionList
     * @return
     */
    private Map<Long, List<PromotionBrief>> getShopIdListFromPromotionBriefList(List<PromotionBrief> promotionList){
        Map<Long, List<PromotionBrief>> shopIdList = new HashMap<Long, List<PromotionBrief>>();
        if (promotionList == null || promotionList.size() == 0){
            return shopIdList;
        }
        List<PromotionBrief> list = null;
        for (PromotionBrief one : promotionList){
            if (shopIdList.containsKey(one.getShopId()))
                shopIdList.get(one.getShopId()).add(one);
            else{
                list = new ArrayList<PromotionBrief>();
                list.add(one);
                shopIdList.put(one.getShopId(), list);
            }
        }
        return shopIdList;
    }

    @Override
    public boolean checkCouponConsumedInBriefs(List<PromotionSKUDiscAMTBySetting> shareList,String coupon){
        if (shareList != null){
            for (PromotionSKUDiscAMTBySetting oneSetting : shareList){
                if (oneSetting.getCouponCodes() == null || oneSetting.getCouponCodes().size() == 0)
                    continue;

                if (oneSetting.getCouponCodes().contains(coupon) == true){
                    return true;
                }
            }
        }

        return false;
    }
}
