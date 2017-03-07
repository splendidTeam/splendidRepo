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

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface ShoppingCartLinePackageInfoManager{

    /**
     * 基于shoppingCartLineId 删除 对应的包装信息.
     * 
     * @param shoppingCartLineId
     *            购物车行id
     * @return
     */
    void deleteByShoppingCartLineId(Long shoppingCartLineId);

    /**
     * 保存包装信息.
     *
     * @param shoppingCartLineId
     * @param shoppingCartLinePackageInfoCommandList
     */
    void savePackageInfo(Long shoppingCartLineId,List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList);
}
