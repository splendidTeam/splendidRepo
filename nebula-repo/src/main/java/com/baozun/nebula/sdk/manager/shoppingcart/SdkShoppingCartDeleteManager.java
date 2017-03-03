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

import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.manager.BaseManager;

/**
 * 专门处理购物车删除操作的业务类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface SdkShoppingCartDeleteManager extends BaseManager{

    /**
     * 删除指定用户 memberId 的指定购物车行 shoppingCartLineId.
     * 
     * @param memberId
     *            指定用户
     * @param shoppingCartLineId
     *            指定的购物车行
     * @throws NativeUpdateRowCountNotEqualException
     *             在sql操作返回的影响行数不是期待的结果,将会抛出异常
     */
    void deleteShoppingCartLine(Long memberId,Long shoppingCartLineId) throws NativeUpdateRowCountNotEqualException;
}
