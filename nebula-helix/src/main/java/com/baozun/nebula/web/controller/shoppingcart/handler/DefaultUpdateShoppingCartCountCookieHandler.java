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

import static java.util.Collections.emptyList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShopSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;
import com.google.common.collect.Iterables;

import static com.feilong.core.Validator.isNullOrEmpty;

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
        Iterable<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandIterable = buildShoppingCartLineSubViewCommandIterable(shoppingCartViewCommand);

        //更新cookie
        shoppingcartCountPersister.save(shoppingCartLineSubViewCommandIterable, request, response);
    }

    /**
     * @param shoppingCartViewCommand
     */
    private Iterable<ShoppingCartLineSubViewCommand> buildShoppingCartLineSubViewCommandIterable(ShoppingCartViewCommand shoppingCartViewCommand){
        if (null == shoppingCartViewCommand){
            return emptyList();
        }

        //---------------------------------------------------------------------
        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = shoppingCartViewCommand.getShopAndShoppingCartLineSubViewCommandListMap();
        if (isNullOrEmpty(shopAndShoppingCartLineSubViewCommandListMap)){
            return emptyList();
        }

        //---------------------------------------------------------------------
        Collection<List<ShoppingCartLineSubViewCommand>> shoppingCartLineSubViewCommandListList = shopAndShoppingCartLineSubViewCommandListMap.values();
        if (isNullOrEmpty(shoppingCartLineSubViewCommandListList)){
            return emptyList();
        }

        //---------------------------------------------------------------------
        return Iterables.concat(shoppingCartLineSubViewCommandListList);
    }
}
