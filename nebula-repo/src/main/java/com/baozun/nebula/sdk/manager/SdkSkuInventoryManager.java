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

import java.util.Map;

import com.baozun.nebula.manager.BaseManager;

/**
 * sku 库存manager.
 *
 * @author feilong
 * @version 5.3.1 2016年5月12日 下午12:45:56
 * @since 5.3.1
 */
public interface SdkSkuInventoryManager extends BaseManager{

    /**
     * 基于<code>extentionCodeandCountMap</code> 扣减商品库存.
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
