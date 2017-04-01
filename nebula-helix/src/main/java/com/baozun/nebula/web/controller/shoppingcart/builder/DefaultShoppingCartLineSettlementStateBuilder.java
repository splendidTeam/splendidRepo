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

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
@Component("shoppingCartLineSettlementStateBuilder")
public class DefaultShoppingCartLineSettlementStateBuilder implements ShoppingCartLineSettlementStateBuilder{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingCartLineSettlementStateBuilder.class);

    /**  */
    @Autowired(required = false)
    private ShoppingCartLineSettlementStateResolver shoppingCartLineSettlementStateResolver;

    /** 选中. */
    private static final Integer STATE_CHECKED = 1;

    /** 不选中. */
    private static final Integer STATE_UNCHECKED = 0;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartLineSettlementStateBuilder#build(com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm, java.lang.String)
     */
    @Override
    public Integer build(ShoppingCartLineAddForm shoppingCartLineAddForm,String extentionCode){
        ShoppingCartLineSettlementStateResolver useShoppingCartLineSettlementStateResolver = defaultIfNull(shoppingCartLineSettlementStateResolver, ShoppingCartLineSettlementStateResolver.TRUE_RESOLVER);
        boolean checked = useShoppingCartLineSettlementStateResolver.resolver(shoppingCartLineAddForm, extentionCode);
        return checked ? STATE_CHECKED : STATE_UNCHECKED;
    }
}
