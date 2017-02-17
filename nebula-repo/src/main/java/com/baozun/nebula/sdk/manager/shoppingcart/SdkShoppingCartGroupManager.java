package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

public interface SdkShoppingCartGroupManager extends BaseManager{

    /**
     * 根据促销引擎计算的结果，把原购物车行数据重新分组。
     * 添加Caption行，按lineGroup分组排列好相应的行。
     * jsp负责显示。
     */
    List<ShoppingCartLineCommand> groupShoppingCartLinesToDisplayByLinePromotionResult(ShoppingCartCommand oneShoppingCart,List<PromotionBrief> promotionBriefList);

}
