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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Interface SdkShoppingCartLineCommandBehaviour.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public interface ShoppingCartLineCommandBehaviour{

    /**
     * Organize extention code and count map for deduct sku inventory.
     * 
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param extentionCodeAndCountMap
     *            the extention code and count map
     */
    void organizeExtentionCodeAndCountMapForDeductSkuInventory(
                    ShoppingCartLineCommand shoppingCartLineCommand,
                    Map<String, Integer> extentionCodeAndCountMap);

    /**
     * 封装购物车行数据.
     * 
     * <h3>为什么要封装订单行数据?</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>游客购物车持久化信息比较少,需要再次封装</li>
     * <li>立即购买的数据,需要再次封装</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>封装之后的作用是什么?</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>用于前端转成 viewcommand数据的展示</li>
     * <li>用于下订单结算</li>
     * </ol>
     * </blockquote>
     * 
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand);

    /**
     * Save order line.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @see com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline.ShoppingCartLineCommandCreateLineBehaviour#saveOrderLine(Long,
     *      List, List,
     *      ShoppingCartLineCommand)
     */
    void saveOrderLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand);

}
