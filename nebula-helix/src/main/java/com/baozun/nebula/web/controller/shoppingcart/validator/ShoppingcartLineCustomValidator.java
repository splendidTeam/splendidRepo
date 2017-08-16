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

import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;

/**
 * 自定义购物车校验.
 * 
 * <p>
 * 可以实现,自定义校验,购物的商品如果是hype 类型的商品, 那么不可以加入购物车和不能走普通的结算通道
 * </p>
 * 
 * <p>
 * 如果实现了该接口并配置了,那么会执行该接口方法; 如果没有配置将会忽略
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 * @since 5.3.2.22 rename from ShoppingcartLineOperateCustomValidator
 */
public interface ShoppingcartLineCustomValidator{

    /**
     * 自定义的校验.
     *
     * @param shoppingcartLineCustomValidatorEntity
     *            自定义校验的相关参数属性.
     * @param shoppingcartLineValidatorChannel
     *            购车行自定义校验渠道
     * @return 如果校验成功, 可以返回 null 或者 {@link ShoppingcartResult#SUCCESS}
     * @since 5.3.2.22
     */
    ShoppingcartResult validate(ShoppingcartLineCustomValidatorEntity shoppingcartLineCustomValidatorEntity,ShoppingcartLineValidatorChannel shoppingcartLineValidatorChannel);

}
