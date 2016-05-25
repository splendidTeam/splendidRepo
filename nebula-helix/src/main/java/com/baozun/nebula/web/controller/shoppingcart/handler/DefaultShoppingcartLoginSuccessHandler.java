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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartSyncManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.persister.GuestShoppingcartPersister;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.feilong.core.Validator;

/**
 * The Class DefaultShoppingcartLoginSuccessHandler.
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 上午1:15:13
 * @since 5.3.1
 */
@Component("shoppingcartLoginSuccessHandler")
public class DefaultShoppingcartLoginSuccessHandler implements ShoppingcartLoginSuccessHandler{

    /** The guest shoppingcart persister. */
    @Autowired
    private GuestShoppingcartPersister guestShoppingcartPersister;

    @Autowired
    private SdkShoppingCartSyncManager sdkShoppingCartSyncManager;

    /** The member shoppingcart resolver. */
    @Autowired
    @Qualifier("memberShoppingcartResolver")
    private ShoppingcartResolver       memberShoppingcartResolver;

    /** The shoppingcart count persister. */
    @Autowired
    private ShoppingcartCountPersister shoppingcartCountPersister;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartLoginSuccessHandler#onLoginSuccess(com.baozun.nebula.web.
     * MemberDetails, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void onLoginSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response){
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        Long memberId = memberDetails.getMemberId();

        // 获取游客购物车数据
        List<ShoppingCartLineCommand> guestShoppingCartLineCommandList = guestShoppingcartPersister.load(request);

        boolean hasGuestShoppingcart = Validator.isNotNullOrEmpty(guestShoppingCartLineCommandList);
        if (hasGuestShoppingcart){
            //同步
            sdkShoppingCartSyncManager.syncShoppingCart(memberId, guestShoppingCartLineCommandList);
            //清空游客购物车
            guestShoppingcartPersister.clear(request, response);
        }

        //获得DB购物车总数量 设置count cookie 
        List<ShoppingCartLineCommand> memberShoppingCartLineCommandList = memberShoppingcartResolver
                        .getShoppingCartLineCommandList(memberDetails, request);
        shoppingcartCountPersister.save(memberShoppingCartLineCommandList, request, response);
    }
}
