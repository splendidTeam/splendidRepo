package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface SdkPromotionCalculationShareToSKUManager extends BaseManager{
	//计算购物车的优惠
	public boolean checkCouponConsumedInBriefs(List<PromotionSKUDiscAMTBySetting> shareList,String coupon);
	public BigDecimal getLineDiscAMTShareOnOrderBaseByShopIdAndSKUId(List<PromotionSKUDiscAMTBySetting> shareList,Long shopId,Long skuId);
	public List<PromotionSKUDiscAMTBySetting> sharePromotionDiscountToEachLine(ShoppingCartCommand shopCart,List<PromotionBrief> briefList);
}
