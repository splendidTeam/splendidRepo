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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.MemberDetails;

/**
 * 订单创建成功之后,购物车相关处理.
 *
 * @author feilong
 * @version 5.3.1 2016年5月25日 下午3:41:15
 * @since 5.3.1
 */
public interface ShoppingcartOrderCreateSuccessHandler{

    /**
     * 订单创建成功之后,购物车相关处理.
     * 
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @param response
     *            the response
     */
    void onOrderCreateSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response);

}