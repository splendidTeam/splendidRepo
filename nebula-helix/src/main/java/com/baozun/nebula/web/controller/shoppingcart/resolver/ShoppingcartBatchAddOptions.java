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
 * 购物车批量添加时候的参数控制.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class ShoppingcartBatchAddOptions extends ShoppingcartBatchOptions{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** Static instance. */
    // the static instance works for all types
    public static final ShoppingcartBatchAddOptions DEFAULT = new ShoppingcartBatchAddOptions();

    /**
     * 
     */
    public ShoppingcartBatchAddOptions(){
        super();
    }

    /**
     * @param isFailContinue
     */
    public ShoppingcartBatchAddOptions(boolean isFailContinue){
        super(isFailContinue);
    }

}
