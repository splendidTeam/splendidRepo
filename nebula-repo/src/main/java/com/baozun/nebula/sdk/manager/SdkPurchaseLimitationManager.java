package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkPurchaseLimitationManager extends BaseManager{
	//计算购物车的优惠
	public List<ShoppingCartLineCommand> checkSKUPurchaseByLimitaionList(List<LimitCommand> limitationList,ShoppingCartCommand shopCart);
}
