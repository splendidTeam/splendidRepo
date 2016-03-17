package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface SdkCustomizeConditionManager extends BaseManager{
	/**
	 * 
	 * @param currentPromotionId
	 * 			:当前活动编号可获得活动SdkPromotionGuideManager.getPromotionById
	 * @param shopCart
	 * 			:当前购物车
	 * @param briefListPrevious
	 * 			:检查到当前活动的自定义条件，需要检查之前已经满足的活动明细
	 * @return
	 * 		:倍增因子是0，不符合条件。大于0符合条件。
	 */
	public int getCustomConditionFactor(Long currentPromotionId,ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious);
}
