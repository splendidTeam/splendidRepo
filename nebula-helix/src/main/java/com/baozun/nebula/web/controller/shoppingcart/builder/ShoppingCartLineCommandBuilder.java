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
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

/**
 * {@link ShoppingCartLineCommand} 构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public interface ShoppingCartLineCommandBuilder{

    /**
     * 基于传入的add form和extentionCode 来构造一条 ShoppingCartLineCommand.
     *
     * @param shoppingCartLineAddForm
     *            the shopping cart line add form
     * @param extentionCode
     *            the extention code
     * @return the shopping cart line command
     */
    ShoppingCartLineCommand build(ShoppingCartLineAddForm shoppingCartLineAddForm,String extentionCode);
}
