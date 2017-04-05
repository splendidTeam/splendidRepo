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
package com.baozun.nebula.web.controller.shoppingcart.validator.update;

import java.util.List;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;

/**
 * 修改购物车行的校验.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 */
public interface ShoppingcartLineUpdateValidator{

    /**
     * 修改购物车的校验(校验成功的话,会同时操作修改数据,比如可能会合并内存购物车行对象).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 shoppingCartLineCommandList 中找不到 id 值是 shoppingcartLineId的行,那么返回 {@link ShoppingcartResult#SHOPPING_CART_LINE_COMMAND_NOT_FOUND}</li>
     * <li>进行 sku 校验{@link ShoppingcartLineOperateCommonValidator#validate(Sku, Integer)}</li>
     * <li>单行最大数量 校验</li>
     * <li>如果需要合并购物车行,那么校验 count和需要被合并行count的总和</li>
     * <li>库存校验</li>
     * </ol>
     * </blockquote>
     * 
     * <p>
     * 注意:此接口可能含融合操作
     * </p>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param shoppingcartLineId
     *            the shoppingcart line id
     * @param shoppingCartLineUpdateSkuForm
     *            the shopping cart line update sku form
     * @return 如果校验没有问题,返回null,否则返回 {@link ShoppingcartResult}
     */
    ShoppingcartResult validator(
                    MemberDetails memberDetails,//
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Long shoppingcartLineId,
                    ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm);
}
