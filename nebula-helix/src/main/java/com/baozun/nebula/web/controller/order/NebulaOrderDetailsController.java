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
package com.baozun.nebula.web.controller.order;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;

/**
 * myaccount OrderList 相关方法controller
 * 
 * <ol>
 * <li>{@link #showOrderDetails(MemberDetails, String, HttpServletRequest, Model)} 进入登录页面</li>
 * </ol>
 * 
 * <h3>showOrderList方法,主要有以下几点:</h3>
 * <blockquote>
 * <ol>
 * <li>设置查询的orderform;</li>
 * <li>通过pageform传入分页信息;</li>
 * </ol>
 * </blockquote>.
 *
 * @author 张乃骐
 * @version 1.0
 * @date 2016年5月10日
 */
public class NebulaOrderDetailsController extends BaseController{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderDetailsController.class);

    /**
     * 显示订单明细.
     *
     * @param memberDetails
     *            the member details
     * @param orderCode
     *            the order code
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the string
     * @since 5.3.1
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "order/{orderCode}", method = RequestMethod.GET)
     */
    public String showOrderDetails(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "orderCode",required = true) String orderCode,
                    HttpServletRequest request,
                    Model model){
        //通过orderCode查询 command

        //command -->viewCommand

        //model add attribute

        return "order.orderdetails";
    }

}
