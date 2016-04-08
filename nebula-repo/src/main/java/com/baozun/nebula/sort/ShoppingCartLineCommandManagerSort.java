package com.baozun.nebula.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
@Deprecated
public class ShoppingCartLineCommandManagerSort {

	/**
	 * 行排序
	 * 
	 * @param shoppingCartCommand
	 * @return
	 */
	public static ShoppingCartCommand shoppingCartLineSort(ShoppingCartCommand shoppingCartCommand) {
		List<ShoppingCartLineCommand> allLineList = null;
		List<ShoppingCartLineCommand> giftList = null;
		for (Entry<Long, ShoppingCartCommand> entry : shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()) {
			allLineList = new ArrayList<ShoppingCartLineCommand>();
			giftList = new ArrayList<ShoppingCartLineCommand>();
			for (ShoppingCartLineCommand shoppingCartLineCommand : entry.getValue().getShoppingCartLineCommands()) {
				if (shoppingCartLineCommand.isGift()) {
					giftList.add(shoppingCartLineCommand);
				} else {
					allLineList.add(shoppingCartLineCommand);
				}
			}
			if (giftList.size() > 1) {
				Collections.sort(giftList, new Comparator<ShoppingCartLineCommand>() {
					public int compare(ShoppingCartLineCommand arg0, ShoppingCartLineCommand arg1) {
						int i = arg0.getPromotionId().compareTo(arg1.getPromotionId());
						if (i == 0) {
							int j = arg0.getSettingId().compareTo(arg1.getSettingId());
							if (j == 0) {
								int h = arg0.getGiftChoiceType().compareTo(arg1.getGiftChoiceType());
								if (h == 0) {
									return arg0.getGiftCountLimited().compareTo(arg1.getGiftCountLimited());
								}
								return h;
							}
							return j;
						}
						return i;
					}
				});
			}
			allLineList.addAll(giftList);
			entry.getValue().setShoppingCartLineCommands(allLineList);
		}
		return shoppingCartCommand;
	}
}
