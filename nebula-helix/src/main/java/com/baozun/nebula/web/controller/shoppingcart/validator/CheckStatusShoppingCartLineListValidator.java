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

import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchOptions;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchResult;

/**
 * 被选中的购物车行 校验.
 * 
 * <p>
 * 常用于 购物车点击下一步, 已经创建订单之前的校验
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
public interface CheckStatusShoppingCartLineListValidator{

    /**
     * 校验被选中状态的购物车行.
     *
     * @param memberDetails
     * @param checkedStatusShoppingCartLineCommandList
     *            被选中状态的购物车行 list
     * @param shoppingcartBatchOptions
     * @return
     */
    ShoppingcartBatchResult validate(MemberDetails memberDetails,List<ShoppingCartLineCommand> checkedStatusShoppingCartLineCommandList,ShoppingcartBatchOptions shoppingcartBatchOptions);

}
