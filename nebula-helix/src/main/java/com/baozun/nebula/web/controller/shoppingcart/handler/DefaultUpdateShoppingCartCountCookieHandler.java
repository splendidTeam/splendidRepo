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
package com.baozun.nebula.web.controller.shoppingcart.handler;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;
import com.google.common.collect.Iterables;

/**
 * 
 * @author bowen.dai
 * @since 5.3.2.23
 */
@Component
public class DefaultUpdateShoppingCartCountCookieHandler implements UpdateShoppingCartCountCookieHandler{

    /** The shoppingcart count persister. */
    @Autowired
    private ShoppingcartCountPersister shoppingcartCountPersister;

    @Override
    public void update(ShoppingCartViewCommand shoppingCartViewCommand,HttpServletRequest request,HttpServletResponse response){
        if (shoppingCartViewCommand != null){
            Collection<List<ShoppingCartLineSubViewCommand>> shoppingCartLineSubViewCommandListList = shoppingCartViewCommand.getShopAndShoppingCartLineSubViewCommandListMap().values();
            if (shoppingCartLineSubViewCommandListList != null){
                Iterable<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandListIterable = Iterables.concat(shoppingCartLineSubViewCommandListList);
                //更新cookie
                shoppingcartCountPersister.save(shoppingCartLineSubViewCommandListIterable, request, response);
            }

        }

    }

}
