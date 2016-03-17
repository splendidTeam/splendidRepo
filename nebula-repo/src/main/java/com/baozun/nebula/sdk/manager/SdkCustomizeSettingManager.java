package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface SdkCustomizeSettingManager extends BaseManager{
	/**
	 * 
	 * @param setting
	 * 			:当前活动的优惠设置单元
	 * @param shopCart
	 * 			:当前购物车
	 * @param briefListPrevious
	 * 			:检查到当前活动的自定义条件，需要检查之前已经满足的活动明细
	 * @return
	 * 			:PromotionSettingDetail，当前优惠单元，计算后得出的优惠结果。
	 * 			 整单优惠，affectSKUDiscountAMTList为空（null）；
	 * 			 商品行优惠，affectSKUDiscountAMTList不为空；
	 * 			 当前活动使用过的整单Coupon，放到PromotionSettingDetail的couponCodes属性中;
	 * 			 当前活动使用过的商品行Coupon，放到PromotionSettingDetail.affectSKUDiscountAMTList的couponCodes属性中;
	 */
	public PromotionSettingDetail getCustomSetting(AtomicSetting setting,PromotionCommand currentPromotion,ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious);
}
