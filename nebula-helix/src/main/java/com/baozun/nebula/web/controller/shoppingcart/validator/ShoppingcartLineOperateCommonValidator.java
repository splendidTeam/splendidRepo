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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;

/**
 * 购物车行操作的公共校验,常用于 add/update/立即购买等操作.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月25日 下午4:47:07
 * @since 5.3.1
 */
public interface ShoppingcartLineOperateCommonValidator{

    /**
     * 公共的校验.
     * 
     * <h3>代码流程:</h3>
     * <blockquote>
     * <ol>
     * <li>count必须 {@code >=} 1</li>
     * <li>sku必须不为null</li>
     * <li>sku.getLifecycle() 必须等于 {@link Sku#LIFE_CYCLE_ENABLE}</li>
     * <li>itemCommand.getLifecycle() 必须是 {@link com.baozun.nebula.sdk.constants.Constants#ITEM_ADDED_VALID_STATUS ITEM_ADDED_VALID_STATUS}</li>
     * <li>判断 <code>checkActiveBeginTime</code> 激活时间</li>
     * <li>判断商品是非赠品</li>
     * <li>如果配置了 {@link ShoppingcartLineCustomValidator},那么执行自定义校验 (since 5.3.2.20)</li>
     * </ol>
     * </blockquote>
     * 
     * <p>
     * 内部调用 {@link #validate(Sku, Integer, ShoppingcartLineValidatorChannel)},默认使用 {@link ShoppingcartLineValidatorChannel#SHOPPING_CART}
     * </p>
     *
     * @param sku
     *            买的什么sku
     * @param count
     *            买了几个
     * @return 如果检验没有错 返回null
     * 
     * @see #validate(Sku, Integer, ShoppingcartLineValidatorChannel)
     */
    ShoppingcartResult validate(Sku sku,Integer count);

    /**
     * 公共的校验.
     * 
     * <h3>代码流程:</h3>
     * <blockquote>
     * <ol>
     * <li>count必须 {@code >=} 1</li>
     * <li>sku必须不为null</li>
     * <li>sku.getLifecycle() 必须等于 {@link Sku#LIFE_CYCLE_ENABLE}</li>
     * <li>itemCommand.getLifecycle() 必须是 {@link com.baozun.nebula.sdk.constants.Constants#ITEM_ADDED_VALID_STATUS ITEM_ADDED_VALID_STATUS}</li>
     * <li>判断 <code>checkActiveBeginTime</code> 激活时间</li>
     * <li>判断商品是非赠品</li>
     * <li>如果配置了 {@link ShoppingcartLineCustomValidator},那么执行自定义校验 (since 5.3.2.20)</li>
     * </ol>
     * </blockquote>
     *
     * @param sku
     *            买的什么sku
     * @param count
     *            买了几个
     * @param shoppingcartLineValidatorChannel
     *            购物车行自定义校验渠道.如果值是null,那么使用默认值 {@link ShoppingcartLineValidatorChannel#SHOPPING_CART}
     * @return 如果检验没有错 返回null
     * @since 5.3.2.22
     */
    ShoppingcartResult validate(Sku sku,Integer count,ShoppingcartLineValidatorChannel shoppingcartLineValidatorChannel);
}
