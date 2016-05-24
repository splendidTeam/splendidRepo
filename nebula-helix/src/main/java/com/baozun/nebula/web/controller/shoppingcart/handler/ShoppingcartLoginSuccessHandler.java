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

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.persister.GuestShoppingcartPersister;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;

/**
 * 登陆成功之后,购物车相关处理.
 *
 * @author feilong
 * @version 5.3.1 2016年5月7日 上午12:02:18
 * @since 5.3.1
 */
public interface ShoppingcartLoginSuccessHandler{

    /**
     * 登录成功之后,购物车处理.
     * 
     * <h3>流程:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/登录之后购物车处理.png"/>
     * </p>
     * 
     * <p>
     * 说明:
     * </p>
     * 
     * <ol>
     * <li>获得游客购物车数据</li>
     * <li>如果游客购物车数据不是空,那么和DB购物车 同步 {@link com.baozun.nebula.sdk.manager.SdkShoppingCartSyncManager#syncShoppingCart(Long, List)},并清空游客购物车
     * see
     * {@link GuestShoppingcartPersister#clear(HttpServletRequest, HttpServletResponse)}</li>
     * <li>获得会员购物车,并设置cookie count购物车数量,
     * {@link ShoppingcartCountPersister#save(List, HttpServletRequest, HttpServletResponse)}
     * </li>
     * </ol>
     * 
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @param response
     *            the response
     */
    void onLoginSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response);

}