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
package com.baozun.nebula.sdk.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;

/**
 * 业务方法校验,提供常用简化方法.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public class ManagerValidate{

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerValidate.class);

    /** Don't let anyone instantiate this class. */
    private ManagerValidate(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 如果实际影响行数和预期的结果不等,那么抛出异常 {@link NativeUpdateRowCountNotEqualException}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 以前我们需要如此这般抒写校验NativeUpdate SQL执行影响行数结果
     * </p>
     * 
     * <pre class="code">
     * 
     * public void updateCartLineSkuInfo(Long memberId,Long cartLineId,Long newSkuId,Integer quantity){
     *     int result = sdkShoppingCartLineDao.updateCartLineSkuInfo(memberId, cartLineId, newSkuId, quantity);
     * 
     *     if (1 != result){
     *         LOGGER.error("updateCartLineSkuInfo:[{}],lineId:[{}],result is:[{}], not expected 1", memberId, cartLineId, result);
     *         throw new NativeUpdateRowCountNotEqualException(1, result);
     *     }
     * }
     * 
     * </pre>
     * 
     * <b>现在我们只需要这么写:</b>
     * 
     * <pre class="code">
     * 
     * public void updateCartLineSkuInfo(Long memberId,Long cartLineId,Long newSkuId,Integer quantity){
     *     int result = sdkShoppingCartLineDao.updateCartLineSkuInfo(memberId, cartLineId, newSkuId, quantity);
     * 
     *     ManagerValidate.isExpectedResult(1, result, "memberId:[{}],update lineId:[{}],change to newSkuId:[{}],quantity:[{}]", memberId, cartLineId, newSkuId, quantity);
     * }
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * @param expectedAffectedCount
     *            期待影响的行数
     * @param actualAffectedCount
     *            实际影响的行数
     * @param message
     *            the {@link org.slf4j.Logger#error(String, Object...)} exception message if invalid, not null
     * @param params
     *            the optional values for the formatted exception message
     * @see com.baozun.nebula.sdk.constants.Constants#NATIVEUPDATE_ROWCOUNT_NOTEXPECTED
     */
    public static void isExpectedResult(int expectedAffectedCount,int actualAffectedCount,String message,Object...params){
        if (actualAffectedCount != expectedAffectedCount){
            LOGGER.error(message + ",result is:[{}], not expected:{}", ArrayUtils.addAll(params, actualAffectedCount, expectedAffectedCount));
            throw new NativeUpdateRowCountNotEqualException(expectedAffectedCount, expectedAffectedCount);
        }
    }
}
