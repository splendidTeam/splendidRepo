package com.baozun.nebula.sdk.manager.order;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;

/**
 * 
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午2:38:35
 * @since 5.3.1
 */
public interface SdkOrderManager extends BaseManager{

    /**
     * 根据shopId保存订单概要
     * 
     * @param shopId
     * @param salesOrderCommand
     * @param shopCartCommandByShop
     * @return
     * @since 5.3.1
     */
    SalesOrder savaOrder(Long shopId,SalesOrderCommand salesOrderCommand,ShopCartCommandByShop shopCartCommandByShop);

}
