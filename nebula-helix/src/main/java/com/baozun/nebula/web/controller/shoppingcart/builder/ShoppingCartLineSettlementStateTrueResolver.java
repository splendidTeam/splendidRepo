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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

/**
 * 默认选中.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public class ShoppingCartLineSettlementStateTrueResolver implements ShoppingCartLineSettlementStateResolver{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartLineSettlementStateTrueResolver.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartLineSettlementStateResolver#resolver(com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm, java.lang.String)
     */
    @Override
    public boolean resolver(ShoppingCartLineAddForm shoppingCartLineAddForm,String extentionCode){
        return true;
    }
}
