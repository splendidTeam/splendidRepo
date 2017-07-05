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

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;

/**
 * 自定义购物车校验.
 * 
 * <p>
 * 如果实现了该接口并配置了,那么会执行该接口方法; 如果没有配置将会忽略
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
public interface ShoppingcartLineOperateCustomValidator{

    /**
     * 自定义的校验.
     * 
     * @param sku
     * @param itemCommand
     * @param count
     * @return 如果校验成功, 可以返回 null 或者 {@link ShoppingcartResult#SUCCESS}
     */
    ShoppingcartResult validate(Sku sku,ItemCommand itemCommand,Integer count);

}
