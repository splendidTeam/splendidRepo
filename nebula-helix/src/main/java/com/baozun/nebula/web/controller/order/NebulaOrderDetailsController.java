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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.order.builder.OrderViewCommandBuilder;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * myaccount Orderdetail 相关方法controller
 * 
 * <ol>
 * <li>{@link #showOrderDetails(MemberDetails, String, HttpServletRequest, Model)} 进入订单详情页</li>
 * </ol>
 * 
 * <h3>showOrderDetails方法,主要有以下几点:</h3> <blockquote>
 * <ol>
 * <li>当会员查询时传入MemberDetails MemberDetails必须有memberid</li>
 * <li>当游客查询时构造MemberDetails,并将收货人姓名setRealName()中便可进行查询</li>
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

    /** The order manager. */
    @Autowired
    private OrderManager orderManager;

    @Autowired
    private OrderViewCommandBuilder orderViewCommandBuilder;

    /**
     * 显示订单明细.当memberid和后台查出的memberid相同或者收货人名字和查处的收货人名字相同才进行显示
     * 
     * 当会员查询时传入memberid
     * 
     * 当游客查询是构造MemberDetails,并将收货人姓名setRealName()中便可进行查询
     * 
     * 成功则构造返回订单详情页面
     * 
     * 失败返回空的字符串,商城端去进行判断
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
    public String showOrderDetails(@LoginMember MemberDetails memberDetails,@RequestParam(value = "orderCode",required = true) String orderCode,HttpServletRequest request,Model model){
        Validate.notNull(memberDetails, "memberDetails can't be null!");

        // 通过orderCode查询 command
        SalesOrderCommand salesOrderCommand = orderManager.findOrderByCode(orderCode, 1);
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        // 判断是否为本人进行操作
        boolean isSelfOrder = isSelfOrder(memberDetails, salesOrderCommand);
        Validate.isTrue(isSelfOrder, "order:%s,is not his self:[%s] order", orderCode, JsonUtil.format(memberDetails, 0, 0));

        model.addAttribute("orderViewCommand", orderViewCommandBuilder.build(salesOrderCommand));
        return "order.orderdetails";

    }

    /**
     * 说明：判断是否可以查询订单
     * 
     * memberid相同 or 收货人姓名相同.
     *
     * @author 张乃骐
     * @param memberDetails
     *            the member details
     * @param salesOrderCommand
     *            the sales order command
     * @return true, if validate order<br>
     * @time：2016年5月23日 下午2:56:06
     */
    protected boolean isSelfOrder(MemberDetails memberDetails,SalesOrderCommand salesOrderCommand){
        Long memberId = salesOrderCommand.getMemberId();
        String name = salesOrderCommand.getName();

        if (null != memberDetails.getMemberId()){
            return memberDetails.getMemberId().longValue() == memberId.longValue();
        }
        if (Validator.isNotNullOrEmpty(memberDetails.getRealName())){
            return memberDetails.getRealName().equals(name);
        }
        return false;
    }

}
