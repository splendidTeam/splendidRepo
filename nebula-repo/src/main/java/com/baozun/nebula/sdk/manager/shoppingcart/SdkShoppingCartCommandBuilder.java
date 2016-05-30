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
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.List;
import java.util.Set;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门用来构建 ShoppingCartCommand.
 *
 * @author feilong
 * @version 5.3.1 2016年5月23日 下午5:47:16
 * @since 5.3.1
 */
public interface SdkShoppingCartCommandBuilder extends BaseManager{

    /**
     * Builds the shopping cart command.
     *
     * @param memberId
     *            the member id
     * @param shoppingCartLines
     *            获取购物车列表时候要经过 有效性引擎和促销引擎。 不走限购检查引擎 CalcFreightCommand 不为空时计算运费
     *            为空不计算运费
     * @param calcFreightCommand
     *            the calc freight command
     * @param coupons
     *            优惠券
     * @param memberComIds
     *            组合id
     * @return the shopping cart command
     */
    ShoppingCartCommand buildShoppingCartCommand(
                    Long memberId,
                    List<ShoppingCartLineCommand> shoppingCartLines,
                    CalcFreightCommand calcFreightCommand,
                    List<String> coupons,
                    Set<String> memberComIds);
}
