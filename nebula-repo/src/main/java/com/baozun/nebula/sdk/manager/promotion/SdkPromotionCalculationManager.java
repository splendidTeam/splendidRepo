package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.calculateEngine.condition.ItemFactor;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface SdkPromotionCalculationManager extends BaseManager{

    public static final Integer CHECKFAILURE = 0;

    public static final Integer MULTIPONE    = 1;

    //计算购物车的优惠
    public List<PromotionBrief> calculationPromotion(ShoppingCartCommand shoppingCartCommand,List<PromotionCommand> promotionList);// throws Exception;	

    public List<PromotionBrief> calculationPromotionByShopId(
                    ShoppingCartCommand shopCart,
                    PromotionCommand promotionOne,
                    List<PromotionBrief> briefListPrevious);

    //计算购物车一个活动的优惠
    public PromotionBrief calculationPromotionByAtomicSetting(
                    ShoppingCartCommand shopCart,
                    PromotionCommand promotion,
                    List<AtomicSetting> settingList,
                    Integer multipFactor,
                    List<PromotionBrief> briefListPrevious);

    //计算购物车一个活动的一个设置的优惠，整单类型的：免运费，整单金额，整单件数
    public PromotionSettingDetail calculationPromotionByAtomicSetting(
                    ShoppingCartCommand shopCart,
                    PromotionCommand promotion,
                    AtomicSetting setting,
                    List<PromotionBrief> briefListPrevious);

    //计算购物车一个活动的一个设置的优惠，商品范围类型的，包括范围礼品，范围Coupon
    public PromotionSettingDetail calculationPromotionByAtomicSettingByScope(
                    ShoppingCartCommand shopCart,
                    PromotionCommand promotion,
                    AtomicSetting setting,
                    List<PromotionBrief> briefListPrevious);

    public PromotionSettingDetail calculationSinglePrdPromotionByAtomicSettingByScope(
                    ShoppingCartCommand shopCart,
                    List<ItemFactor> itemFactorList,
                    PromotionCommand promotion,
                    AtomicSetting setting,
                    List<PromotionBrief> briefListPrevious);
}
