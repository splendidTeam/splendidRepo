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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * sku 库存manager.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午12:45:56
 * @since 5.3.1
 */
public interface SdkSkuInventoryManager extends BaseManager{

    /**
     * 基于shoppingCartLineCommandList 来执行扣减库存的逻辑.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     */
    void deductSkuInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList);

    /**
     * 基于<code>extentionCodeandCountMap</code> 扣减商品库存.
     * 
     * <p>
     * 很多时候,同一笔订单可能出现两行相同的extentionCode,这是如果分两次去扣减库存,可能会出现死锁的情况,需要先合并再统一调用这个方法来一次性扣减库存
     * </p>
     * 
     * <h3>代码流程:</h3>
     * <blockquote>
     * <ol>
     * <li><code>extentionCodeandCountMap</code>如果是null或者empty直接抛出异常</li>
     * <li>循环 <code>extentionCodeandCountMap</code>来扣减库存,有一条失败直接抛出异常</li>
     * </ol>
     * </blockquote>
     *
     * @param extentionCodeandCountMap
     *            key是 extentionCode value是这个extentionCode 需要被扣减的库存数量
     */
    void deductSkuInventory(Map<String, Integer> extentionCodeandCountMap);

}
