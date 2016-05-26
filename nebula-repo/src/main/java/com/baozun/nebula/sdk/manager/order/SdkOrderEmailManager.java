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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 订单相关的邮件处理.
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午8:25:54
 * @since 5.3.1
 */
public interface SdkOrderEmailManager extends BaseManager{

    void sendEmail(String emailTemplete,String email,Map<String, Object> dataMap);

    void sendEmailOfCreateOrder(List<Map<String, Object>> dataMapList);

    Map<String, Object> buildDataMap(String emailTemplete,SalesOrderCommand salesOrderCommand,String nickName);

    Map<String, Object> buildDataMapForCreateOrder(
                    String subOrdinate,
                    SalesOrder salesOrder,
                    SalesOrderCommand salesOrderCommand,
                    List<ShoppingCartLineCommand> sccList,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    SalesOrderCreateOptions salesOrderCreateOptions);
}
