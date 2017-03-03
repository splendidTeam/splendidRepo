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
package com.baozun.nebula.web.controller.shoppingcart.converter;

import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShopSubViewCommand;

/**
 * 将 {@link ShopCommand} 转成 {@link ShopSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public class ShopCommandToViewCommandTransformer implements Transformer<ShopCommand, ShopSubViewCommand>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopCommandToViewCommandTransformer.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public ShopSubViewCommand transform(ShopCommand shopCommand){
        ShopSubViewCommand viewCommand = new ShopSubViewCommand();
        viewCommand.setCode(shopCommand.getShopcode());
        viewCommand.setId(shopCommand.getShopid());
        viewCommand.setLifecycle(shopCommand.getLifecycle());
        viewCommand.setName(shopCommand.getShopname());
        return viewCommand;
    }

}
