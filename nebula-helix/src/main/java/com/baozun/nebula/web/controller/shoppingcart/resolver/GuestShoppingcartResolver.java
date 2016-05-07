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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.persister.GuestShoppingcartPersister;

/**
 * 游客操作购物车.
 *
 * @author weihui.tang
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午4:23:16
 * @since 5.3.1
 */
@Component("guestShoppingcartResolver")
public class GuestShoppingcartResolver extends AbstractShoppingcartResolver{

    /** The cookie shoppingcart. */
    @Autowired
    private GuestShoppingcartPersister guestShoppingcartPersister;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#getShoppingCartLineCommandList(com.baozun.nebula.web.
     * MemberDetails, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request){
        return guestShoppingcartPersister.load(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doAddShoppingCart(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        // 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息 只有游客需要有这个动作 所以放在这里
        List<ShoppingCartLineCommand> mainLines = getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        guestShoppingcartPersister.save(mainLines, request, response);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doUpdateShoppingCart(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        // 主賣品(剔除 促銷行 贈品) 剔除之后 下次load会补全最新促销信息 只有游客需要有这个动作 所以放在这里
        List<ShoppingCartLineCommand> mainLines = getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        guestShoppingcartPersister.save(mainLines, request, response);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doDeleteShoppingCartLine(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        List<ShoppingCartLineCommand> mainLines = getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        // 将修改后的购物车保存cookie
        guestShoppingcartPersister.save(mainLines, request, response);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doToggleShoppingCartLineCheckStatus(com.baozun.
     * nebula.web.MemberDetails, java.util.List, java.util.List, boolean, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<String> extentionCodeList,
                    List<ShoppingCartLineCommand> needChangeCheckedStatusShoppingCartLineCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){
        // 将修改后的购物车保存cookie
        guestShoppingcartPersister.save(needChangeCheckedStatusShoppingCartLineCommandList, request, response);
        return null;
    }
}
