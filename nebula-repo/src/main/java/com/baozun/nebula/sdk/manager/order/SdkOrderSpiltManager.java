/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.order;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Interface OrderManager.
 */
public interface SdkOrderSpiltManager extends BaseManager{

    /**
     * 拆单并保存订单信息
     * @param shoppingCartCommand 购物车信息
     * @param salesOrderCommand 订单信息
     * @param salesOrderCreateOptions 订单创建的选项设置
     * @param subOrdinate 订单流水号
     * @param shopIdAndPromotionSKUDiscAMTBySettingMap 按店铺拆分的促销信息
     * @param shopIdAndShopCartCommandByShopMap 按店铺拆分的价格以及优惠信息
     * @param isSendEmail 是否发送邮件
     * @param emailDataMapList  邮件参数信息
     * @param entry  按店铺拆分后，每个店铺对应的的购物车行信息
     */
    public void splitOrderAndsaveOrderInfo(
                    ShoppingCartCommand shoppingCartCommand,
                    SalesOrderCommand salesOrderCommand,
                    SalesOrderCreateOptions salesOrderCreateOptions,
                    String subOrdinate,
                    Map<Long, List<PromotionSKUDiscAMTBySetting>> shopIdAndPromotionSKUDiscAMTBySettingMap,
                    Map<Long, ShopCartCommandByShop> shopIdAndShopCartCommandByShopMap,
                    boolean isSendEmail,
                    List<Map<String, Object>> emailDataMapList,
                    Entry<Long, List<ShoppingCartLineCommand>> entry);

}
