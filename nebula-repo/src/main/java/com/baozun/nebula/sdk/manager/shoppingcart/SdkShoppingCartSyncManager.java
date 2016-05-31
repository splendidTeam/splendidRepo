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
 * 同步购物车.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月23日 下午6:24:48
 * @since 5.3.1
 */
public interface SdkShoppingCartSyncManager extends BaseManager{

    /**
     * 将指定的 购物车行 list 同步到指定的用户.
     * 
     * <h3>代码流程:</h3>
     * 
     * <blockquote>
     * 
     * <ol>
     * <li>如果 memberId是null,抛出异常</li>
     * <li>如果 shoppingLines是null,抛出异常</li>
     * <li>循环判断,如果数据库用户购物车存在相同的 extentioncode,那么累加;否则插入一条新的</li>
     * </ol>
     * </blockquote>
     *
     * @param memberId
     *            the member id
     * @param shoppingLines
     *            the shopping lines
     */
    void syncShoppingCart(Long memberId,List<ShoppingCartLineCommand> shoppingLines);
}
