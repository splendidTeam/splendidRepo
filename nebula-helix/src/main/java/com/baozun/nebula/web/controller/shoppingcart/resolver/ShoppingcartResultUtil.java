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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

/**
 * {@link ShoppingcartResult} 的工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
public final class ShoppingcartResultUtil{

    /** Don't let anyone instantiate this class. */
    private ShoppingcartResultUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------------

    /**
     * 判断 {@link ShoppingcartResult} 是否是成功.
     * 
     * <p>
     * 如果 shoppingcartResult 是null 或者是 {@link ShoppingcartResult#SUCCESS},标识为成功
     * </p>
     * 
     * @param shoppingcartResult
     * @return 如果是成功 返回true,否则返回false
     */
    public static boolean isSuccess(ShoppingcartResult shoppingcartResult){
        return null == shoppingcartResult || ShoppingcartResult.SUCCESS == shoppingcartResult;
    }

    /**
     * 判断 {@link ShoppingcartResult} 不是成功.
     * 
     * <p>
     * 如果 shoppingcartResult 不是null 并且不是 {@link ShoppingcartResult#SUCCESS},标识为不成功
     * </p>
     * 
     * @param shoppingcartResult
     * @return 如果不是成功 返回true,否则返回false
     */
    public static boolean isNotSuccess(ShoppingcartResult shoppingcartResult){
        return !isSuccess(shoppingcartResult);
    }
}
