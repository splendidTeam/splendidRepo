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

package com.baozun.nebula.sdk.manager.order;

import java.util.Collections;
import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.OrderLineCommand;

/**
 * 封装soline 包装信息.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface SdkOrderLinePackInfoManager extends BaseManager{

    /**
     * 完善包装信息.
     * 
     * @param <T>
     *
     * @param orderLineCommandList
     * @return 如果 <code>orderLines</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartQueryManagerImpl#handleShoppingCartLineCommandList(List<ShoppingCartLineCommand>)
     * @since 5.3.2.13
     */
    <T extends OrderLineCommand> List<T> packOrderLinesPackageInfo(List<T> orderLineCommandList);

}
