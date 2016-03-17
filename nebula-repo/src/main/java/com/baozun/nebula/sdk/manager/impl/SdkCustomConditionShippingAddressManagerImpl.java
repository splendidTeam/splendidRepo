package com.baozun.nebula.sdk.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.SdkCustomizeConditionManager;

@Transactional
@Service("SdkCustomConditionShippingAddressManager")
public class SdkCustomConditionShippingAddressManagerImpl implements SdkCustomizeConditionManager {
	/* 开放自定义条件
	 * currentPromotionId，当前活动编号可获得活动SdkPromotionGuideManager.getPromotionById。请不要对该参数值进行修改。
	 * shopCart，当前购物车。请不要对该参数值进行修改。
	 * briefListPrevious，检查到当前活动的自定义条件，需要检查之前已经满足的活动明细。请不要对该参数值进行修改。
	 * getConditionFactor(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, java.lang.Long, java.util.List)
	 * @返回参数
	 * 倍增因子是0，不符合条件。大于0符合条件。
	 */
	@Override
	public int getCustomConditionFactor(Long currentPromotionId,ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious){
		//店铺有了briefListPrevious优惠明细，可以介入到活动条件判断逻辑中		

		return 1;
	}

}
