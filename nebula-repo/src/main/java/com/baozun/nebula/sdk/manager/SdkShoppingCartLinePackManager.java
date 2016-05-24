package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 
 *
 * @author feilong
 * @since 5.3.1
 */
public interface SdkShoppingCartLinePackManager extends BaseManager{

    /**
     * 封装购物车行数据
     * 
     * @param shoppingCartLineCommand
     */
    void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand);
}
