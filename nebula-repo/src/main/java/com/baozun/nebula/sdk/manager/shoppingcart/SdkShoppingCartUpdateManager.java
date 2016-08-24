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

import java.util.Map;

import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;

/**
 * 专门处理购物车更新操作的业务类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
public interface SdkShoppingCartUpdateManager{

    /**
     * 更新指定会员 指定购物车行的数量.
     *
     * @param memberId
     *            会员id
     * @param lineId
     *            购物车line id
     * @param quantity
     *            数量
     * @throws NativeUpdateRowCountNotEqualException
     *             如果修改的结果,即影响的行数不等于1,那么将抛出该异常
     * @throws NullPointerException
     *             如果 <code>memberId</code> 是null,或者<code>lineId</code> 是null,或者<code>quantity</code> 是null
     */
    void updateCartLineQuantityByLineId(Long memberId,Long lineId,Integer quantity);

    /**
     * 批量更新指定会员 指定购物车行的数量.
     *
     * @param memberId
     *            会员id
     * @param shoppingcartLineIdAndCountMap
     *            the shoppingcart line id and count map
     * @throws NativeUpdateRowCountNotEqualException
     *             如果某条数据执行的结果,即影响的行数不等于1,那么将抛出该异常
     * @throws NullPointerException
     *             如果 <code>shoppingcartLineIdAndCountMap</code> 是null
     * @throws IllegalArgumentException
     *             如果 <code>shoppingcartLineIdAndCountMap</code> 是empty
     */
    void updateCartLineQuantity(Long memberId,Map<Long, Integer> shoppingcartLineIdAndCountMap);
}
