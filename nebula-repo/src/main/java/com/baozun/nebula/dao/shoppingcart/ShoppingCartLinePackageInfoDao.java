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
package com.baozun.nebula.dao.shoppingcart;

import java.util.List;

import com.baozun.nebula.model.shoppingcart.ShoppingCartLinePackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface ShoppingCartLinePackageInfoDao extends GenericEntityDao<ShoppingCartLinePackageInfo, Long>{

    /**
     * 根据购物车行查询包装信息.
     *
     * @param shoppingCartLineIdList
     * @return 根据购物车行查询包装信息.
     */
    @NativeQuery(model = ShoppingCartLinePackageInfoCommand.class)
    List<ShoppingCartLinePackageInfoCommand> findShoppingCartLinePackageInfoCommandList(@QueryParam("shoppingCartLineIdList") List<Long> shoppingCartLineIdList);

    /**
     * 基于shoppingCartLineId 删除 对应的包装信息.
     * 
     * @param shoppingCartLineId
     *            购物车行id
     * @return 可能影响多行信息
     */
    @NativeUpdate
    Integer deleteByShoppingCartLineId(@QueryParam("shoppingCartLineId") Long shoppingCartLineId);
}
