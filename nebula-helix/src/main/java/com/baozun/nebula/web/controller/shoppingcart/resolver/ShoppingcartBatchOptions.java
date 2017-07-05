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

import java.io.Serializable;

/**
 * 购物车批量操作时候的参数控制.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
public class ShoppingcartBatchOptions implements Serializable,ShoppingcartOptions{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** Static instance. */
    // the static instance works for all types
    public static final ShoppingcartBatchOptions DEFAULT = new ShoppingcartBatchOptions();

    //---------------------------------------------------------------------

    /**
     * 批量时候,如果某条失败了,是否继续.
     * 
     * <h3>场景</h3>
     * 
     * <blockquote>
     * <p>
     * 批量添加5个sku,第二个sku 如果不存在或者其他校验失败的时候
     * </p>
     * 
     * <ul>
     * <li>如果 isSkuAddFailContinue 为true,将会继续添加其他的sku到购物车;</li>
     * <li>如果 isSkuAddFailContinue 为false,那么不会继续 添加其他的sku到购物车;</li>
     * </ul>
     * 
     * <p>
     * 默认是 false
     * </p>
     * </blockquote>
     */
    private boolean isFailContinue = false;

    /**
     * 
     */
    public ShoppingcartBatchOptions(){
        super();
    }

    /**
     * @param isFailContinue
     */
    public ShoppingcartBatchOptions(boolean isFailContinue){
        super();
        this.isFailContinue = isFailContinue;
    }

    //---------------------------------------------------------------------

    /**
     * 批量时候,如果某条失败了,是否继续.
     * 
     * <h3>场景</h3>
     * 
     * <blockquote>
     * <p>
     * 批量添加5个sku,第二个sku 如果不存在或者其他校验失败的时候
     * </p>
     * 
     * <ul>
     * <li>如果 isSkuAddFailContinue 为true,将会继续添加其他的sku到购物车;</li>
     * <li>如果 isSkuAddFailContinue 为false,那么不会继续 添加其他的sku到购物车;</li>
     * </ul>
     * 
     * <p>
     * 默认是 false
     * </p>
     * </blockquote>
     *
     * @return the 批量时候,如果某条失败了,是否继续
     */
    public boolean isFailContinue(){
        return isFailContinue;
    }

    /**
     * 批量时候,如果某条失败了,是否继续.
     * 
     * <h3>场景</h3>
     * 
     * <blockquote>
     * <p>
     * 批量添加5个sku,第二个sku 如果不存在或者其他校验失败的时候
     * </p>
     * 
     * <ul>
     * <li>如果 isSkuAddFailContinue 为true,将会继续添加其他的sku到购物车;</li>
     * <li>如果 isSkuAddFailContinue 为false,那么不会继续 添加其他的sku到购物车;</li>
     * </ul>
     * 
     * <p>
     * 默认是 false
     * </p>
     * </blockquote>
     *
     * @param isSkuAddFailContinue
     *            the new 批量时候,如果某条失败了,是否继续
     */
    public void setIsFailContinue(boolean isSkuAddFailContinue){
        this.isFailContinue = isSkuAddFailContinue;
    }

}
