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
package com.baozun.nebula.web.controller.shoppingcart.handler;

import java.util.List;

import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;

/**
 * 专门给shoppingCartLineSubViewCommandList的排序器.
 * 
 * <h3>购物车排序:</h3>
 * 
 * <blockquote>
 * 
 * 默认排序规则 {@link DefaultShoppingCartLineSubViewCommandListSorter}:
 * 
 * <ol>
 * <li>
 * 先按照购物车行<b>状态</b>排序, 依次是:
 * 
 * <ol>
 * <li>正常的购物车行</li>
 * <li>没有库存的购物车行</li>
 * <li>下架状态的购物车行</li>
 * </ol>
 * 
 * </li>
 * 
 * <li>再按照 <code>group</code> 排序,<code>group</code> 值一样的排在一起(可能是捆绑类商品或者是促销计算出来的在一起的购物车行)</li>
 * 
 * <li>再按照 <b>添加时间</b> 倒序排序 (越晚添加到购物车的排在上面)</li>
 * </ol>
 * 
 * 当然你也可以 <b>自定义排序</b>, 方案,只需要实现 {@link ShoppingCartLineSubViewCommandListSorter} 接口
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
public interface ShoppingCartLineSubViewCommandListSorter{

    /**
     * 排序.
     * 
     * @param shoppingCartLineSubViewCommandList
     * @return
     */
    List<ShoppingCartLineSubViewCommand> sort(List<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandList);
}
