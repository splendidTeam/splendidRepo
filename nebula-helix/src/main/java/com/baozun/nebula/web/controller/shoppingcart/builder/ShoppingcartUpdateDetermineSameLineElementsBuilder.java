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

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingcartUpdateDetermineSameLineElements;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;

/**
 * {@link ShoppingcartUpdateDetermineSameLineElements} 构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface ShoppingcartUpdateDetermineSameLineElementsBuilder{

    /**
     * 基于ShoppingCartLineCommand 和 ShoppingCartLineUpdateSkuForm 来构造 ShoppingcartUpdateDetermineSameLineElements.
     *
     * @param currentShoppingCartLineCommand
     * @param shoppingCartLineUpdateSkuForm
     * @return 如果 <code>currentShoppingCartLineCommand</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>shoppingCartLineUpdateSkuForm</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    ShoppingcartUpdateDetermineSameLineElements build(ShoppingCartLineCommand currentShoppingCartLineCommand,ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm);
}
