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
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门处理购物车添加操作的业务类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface SdkShoppingCartAddManager extends BaseManager{

    /**
     * 添加或者修改购物车.
     * 
     * <p>
     * 如果 lineId != null && lineId > 0 那么更新数量;<br>
     * 否则添加
     * </p>
     * 
     * @param memberId
     *            会员
     * @param shoppingCartLineCommand
     *            最终的购物车行,如果是修改会直接拿里面的count 直接覆盖修改
     * @throws NativeUpdateRowCountNotEqualException
     *             在sql操作返回的影响行数不是期待的结果,将会抛出异常
     */
    void addOrUpdateCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand) throws NativeUpdateRowCountNotEqualException;

    /**
     * 添加到购物车.
     * 
     * @param memberId
     *            会员
     * @param shoppingCartLineCommand
     *            购物车行
     * @throws NativeUpdateRowCountNotEqualException
     *             在sql操作返回的影响行数不是期待的结果,将会抛出异常
     */
    void addCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand) throws NativeUpdateRowCountNotEqualException;

}
