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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.factory.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.persister.GuestShoppingcartPersister;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;

/**
 * The Class DefaultShoppingcartOrderCreateSuccessHandler.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月25日 下午3:43:09
 * @since 5.3.1
 */
@Component("shoppingcartOrderCreateSuccessHandler")
public class DefaultShoppingcartOrderCreateSuccessHandler implements ShoppingcartOrderCreateSuccessHandler{

    /** The shoppingcart factory. */
    @Autowired
    private ShoppingcartFactory shoppingcartFactory;

    /** The guest shoppingcart persister. */
    @Autowired
    private GuestShoppingcartPersister guestShoppingcartPersister;

    /** The shoppingcart count persister. */
    @Autowired
    private ShoppingcartCountPersister shoppingcartCountPersister;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingcartOrderCreateSuccessHandler#onOrderCreateSuccess(com.baozun.nebula.
     * web.MemberDetails, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void onOrderCreateSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingcartFactory.getShoppingCartLineCommandList(memberDetails, request);

        // 游客需要更新cookie中的购物车信息
        if (null == memberDetails){
            // 取出没有被选中的 的商品
            shoppingCartLineCommandList = ShoppingCartUtil.getMainShoppingCartLineCommandListWithCheckStatus(shoppingCartLineCommandList, false);

            guestShoppingcartPersister.save(shoppingCartLineCommandList, request, response);
        }

        //下单成功后更新cookie中的购物车商品的数量
        shoppingcartCountPersister.save(shoppingCartLineCommandList, request, response);
    }
}
