package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkShoppingCartLinesManager extends BaseManager{
	
	/**
	 * 获取当前购物车中库存不足的行
	 * @param allGroupedLines
	 * @return
	 */
	public List<ShoppingCartLineCommand> getOutOfStockShoppingCartLines(List<ShoppingCartLineCommand> allGroupedLines);
	
	/**
	 * 获取当前购物车中直推的赠品行
	 * @param allGroupedLines
	 * @return
	 */
	public Map<Integer, List<ShoppingCartLineCommand>> getShoppingCartForceSendGiftLines(List<ShoppingCartLineCommand> allGroupedLines, List<LimitCommand> purchaseLimitationList, ShoppingCartCommand shoppingCartCommand);
	
	/**
	 * 获取当前购物车中库存不足的赠品行
	 * @param allGroupedLines
	 * @return
	 */
	public List<ShoppingCartLineCommand> getOutOfStockShoppingCartUserChoiceGiftLines(List<ShoppingCartLineCommand> allGroupedLines);
}
