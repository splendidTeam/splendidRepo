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

import java.util.Map;

/**
 * 购物车批量添加操作时候的结果实体.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "com.feilong.coreextension.entity.BackWarnEntity"
 * @since 5.3.2.18
 */
public class ShoppingcartBatchAddResult extends ShoppingcartBatchResult{

    /**
     * 
     */
    private static final long serialVersionUID = -1084735225793294902L;

    //---------------------------------------------------------------------
    /**
     * 
     */
    public ShoppingcartBatchAddResult(){
        super();
    }

    /**
     * @param isSuccess
     * @param skuIdAndShoppingcartResultFailMap
     */
    public ShoppingcartBatchAddResult(boolean isSuccess, Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap){
        super(isSuccess, skuIdAndShoppingcartResultFailMap);
    }

}
