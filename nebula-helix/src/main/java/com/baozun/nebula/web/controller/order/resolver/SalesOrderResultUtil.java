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
package com.baozun.nebula.web.controller.order.resolver;

/**
 * {@link SalesOrderResult} 的工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public final class SalesOrderResultUtil{

    /** Don't let anyone instantiate this class. */
    private SalesOrderResultUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------------

    /**
     * 判断 {@link SalesOrderResult} 是否是成功.
     * 
     * <p>
     * 如果 salesOrderResult 是null 或者是 {@link SalesOrderResult#SUCCESS},标识为成功
     * </p>
     * 
     * @param salesOrderResult
     * @return 如果是成功 返回true,否则返回false
     */
    public static boolean isSuccess(SalesOrderResult salesOrderResult){
        return null == salesOrderResult || SalesOrderResult.SUCCESS == salesOrderResult;
    }

    /**
     * 判断 {@link SalesOrderResult} 不是成功.
     * 
     * <p>
     * 如果 salesOrderResult 不是null 并且不是 {@link SalesOrderResult#SUCCESS},标识为不成功
     * </p>
     * 
     * @param salesOrderResult
     * @return 如果不是成功 返回true,否则返回false
     */
    public static boolean isNotSuccess(SalesOrderResult salesOrderResult){
        return !isSuccess(salesOrderResult);
    }
}
