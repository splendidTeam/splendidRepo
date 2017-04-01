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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

/**
 * 购物车渲染的时候(通常添加购物车),是否选中解析器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public interface ShoppingCartLineSettlementStateResolver{

    /** 默认是true的实现 */
    public static final ShoppingCartLineSettlementStateResolver TRUE_RESOLVER = new ShoppingCartLineSettlementStateTrueResolver();

    /** 默认是false的实现 */
    public static final ShoppingCartLineSettlementStateResolver FALSE_RESOLVER = new ShoppingCartLineSettlementStateFalseResolver();

    /**
     * 判定是否选中.
     * 
     * @param shoppingCartLineAddForm
     *            shoppingCartLineAddForm
     * @param extentionCode
     * @return
     */
    boolean resolver(ShoppingCartLineAddForm shoppingCartLineAddForm,String extentionCode);

}
