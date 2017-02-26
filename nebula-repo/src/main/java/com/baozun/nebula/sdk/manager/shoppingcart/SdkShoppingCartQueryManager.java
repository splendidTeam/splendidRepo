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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门处理购物车查询的业务类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface SdkShoppingCartQueryManager extends BaseManager{

    /**
     * 查询用户的购物车信息
     * 
     * @param memberId
     *            指定用户
     * @return 如果没有购物车信息,返回null
     */
    List<ShoppingCartLineCommand> findShoppingCartLineCommandList(Long memberId);
}
