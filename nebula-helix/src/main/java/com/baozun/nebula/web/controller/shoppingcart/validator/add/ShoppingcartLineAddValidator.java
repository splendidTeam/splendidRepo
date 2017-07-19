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
package com.baozun.nebula.web.controller.shoppingcart.validator.add;

import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLinePackageInfoFormListValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartOneLineMaxQuantityValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartTotalLineMaxSizeValidator;

/**
 * 添加购物车行的校验.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 * @since 5.3.2.14 move new package
 */
public interface ShoppingcartLineAddValidator{

    /**
     * 添加购物车时候的校验.
     * 
     * <p>
     * 注意:此接口目前不是 Immutable 的,可能含融合操作,在校验的同时 会添加到购物车行
     * </p>
     * 
     * <h3>校验流程:</h3>
     * <blockquote>
     * <ol>
     * <li>先校验 {@link ShoppingcartLinePackageInfoFormListValidator} 包装信息参数</li>
     * <li>再使用 {@link ShoppingcartLineOperateCommonValidator} 购物车操作的通用校验</li>
     * <li>再校验 {@link ShoppingcartOneLineMaxQuantityValidator} 单行最大数量校验器</li>
     * <li>再校验 {@link ShoppingcartTotalLineMaxSizeValidator} 总行最大行数校验器</li>
     * <li>再校验 {@link ShoppingCartInventoryValidator} sku 库存校验</li>
     * </ol>
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户原来的购物车,如果没有那么构造empty list进来
     * @param shoppingCartLineAddForm
     *            购买表单
     * @return 如果校验没有问题,返回null; <br>
     *         否则返回 {@link ShoppingcartResult}
     * @since 5.3.2.13
     */
    ShoppingcartResult validator(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineAddForm shoppingCartLineAddForm);
}
