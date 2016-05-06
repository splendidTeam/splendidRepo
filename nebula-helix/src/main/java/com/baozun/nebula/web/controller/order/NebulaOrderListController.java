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
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.order.form.OrderQueryForm;

/**
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 下午3:47:11
 * @since 5.3.1
 */
public class NebulaOrderListController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderListController.class);

    /**
     * 
     * @param memberDetails
     * @param orderQueryForm
     * @param bindingResult
     * @param pageForm
     * @param request
     * @param response
     * @param model
     * @return
     * 
     * @NeedLogin (guest=true)
     * @RequestMapping(value = "/order/orderlist", method = RequestMethod.GET)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrders(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     * @see com.baozun.nebula.dao.salesorder.SdkOrderDao#findOrders(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
     * 
     * @see com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand
     */
    public String showOrderList(
                    @LoginMember MemberDetails memberDetails,
                    @ModelAttribute("orderQueryForm") OrderQueryForm orderQueryForm,
                    BindingResult bindingResult,
                    PageForm pageForm,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        //        step1:  validate OrderQueryForm
        //        step2:  convert OrderQueryForm to OrderQueryCommand
        //        step3:  get result Pagination <SimpleOrderCommand> from service
        //        step4:  convert Pagination <SimpleOrderCommand> to 
        //        Pagination <SimpleOrderViewCommand> in builder
        //        step5:  model构建两个key
        //        "currentSimpleOrderViewCommandPagination" 以及
        //        "orderQueryForm"
        //         (理论上 orderQueryForm可以自动随着model返回(隐式),此处是否需要显示设置 可以讨论下) 
        //        此时的orderQueryForm 可以用于前端的判断 (是否需要再转成orderQueryViewCommand?)
        //        step6:  return "order.orderlist"

        return "order.orderlist";
    }

}
