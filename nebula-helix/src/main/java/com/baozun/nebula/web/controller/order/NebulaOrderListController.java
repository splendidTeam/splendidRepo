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

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.OrderQueryCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.order.converter.SimpleOrderViewCommandConverter;
import com.baozun.nebula.web.controller.order.form.OrderQueryForm;
import com.baozun.nebula.web.controller.order.form.OrderTimeType;
import com.baozun.nebula.web.controller.order.validator.OrderQueryFormValidator;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.bean.PropertyUtil;
import loxia.dao.Page;
import loxia.dao.Pagination;

/**
 * myaccount OrderList 相关方法controller
 * 
 * <ol>
 * <li>{@link #showOrderList(memberDetails,orderQueryForm,bindingResult,pageForm,request,respons,model)} 进入登录页面</li>
 * </ol>
 * 
 * <h3>showOrderList方法,主要有以下几点:</h3>
 * <blockquote>
 * <ol>
 * <li>设置查询的orderform;</li>
 * <li>通过pageform传入分页信息;</li>
 * </ol>
 * </blockquote>
 * @author 张乃骐
 * @version 1.0
 * @date 2016年5月10日
 */
public class NebulaOrderListController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderListController.class);

    @Autowired
    private SalesOrderManager salesOrderManager;

    @Autowired
    private OrderLineManager orderLineManager;

    @Autowired
    @Qualifier("orderQueryFormValidator")
    private OrderQueryFormValidator orderQueryFormValidator;

    @Autowired
    @Qualifier("simpleOrderViewCommandConverter")
    private SimpleOrderViewCommandConverter simpleOrderViewCommandConverter;

    /**
     * 分页查询orderlist
     * @param memberDetails
     * @param orderQueryForm
     * @param bindingResult
     * @param pageForm
     * @param request
     * @param response
     * @param model
     * @return
     * @NeedLogin (guest=false)
     * @RequestMapping(value = "/order/orderlist", method = RequestMethod.GET)
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrders(loxia.dao.Page, loxia.dao.Sort[],
     *      java.util.Map)
     * @see com.baozun.nebula.dao.salesorder.SdkOrderDao#findOrders(loxia.dao.Page,
     *      loxia.dao.Sort[], java.util.Map)
     * 
     * @see com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand
     */
    public String showOrderList(@LoginMember MemberDetails memberDetails,
            @ModelAttribute("orderQueryForm") OrderQueryForm orderQueryForm, BindingResult bindingResult,
            PageForm pageForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 校验查询条件非空
        orderQueryFormValidator.validate(orderQueryForm, bindingResult);
        if (bindingResult.hasErrors()) {
            NebulaReturnResult resultFromBindingResult = super.getResultFromBindingResult(bindingResult);
            model.addAttribute("errors", resultFromBindingResult);
            return "order.orderlist";
        }
        // queryform表单进行转换
        OrderQueryCommand orderQueryCommand = new OrderQueryCommand();
        orderQueryForm.convertToOrderQueryCommand(orderQueryCommand);
        // 当前页，每页10条
        Page page = new Page(pageForm.getCurrentPage(), pageForm.getSize());// 当前页，每页10条
        // 获取数据
        Pagination<SimpleOrderCommand> simpleOrderCommandPagination = salesOrderManager
                .findSimpleOrderCommandPagination(memberDetails.getMemberId(), orderQueryCommand, page);
        // Pagination <SimpleOrderViewCommand> in builder
//        Pagination<SimpleOrderViewCommand> simpleOrderViewCommandPagination = convertToSimpleOrderViewCommandPagination(
//                simpleOrderCommandPagination);
        Pagination<SimpleOrderViewCommand> simpleOrderViewCommandPagination = simpleOrderViewCommandConverter.convert(simpleOrderCommandPagination);
        model.addAttribute("currentSimpleOrderViewCommandPagination", simpleOrderViewCommandPagination);
        model.addAttribute("orderQueryForm", orderQueryForm);
        return "order.orderlist";
    }

    /**
     * 
     * 说明：后台SimpleOrderCommand转换为SimpleOrderViewCommand，SimpleOrderViewCommand添加订单行的的信息
     * 
     * @param ordercommand
     * @return
     * @author 张乃骐
     * @time：2016年5月9日 下午7:12:26
     */
    @Deprecated
    private Pagination<SimpleOrderViewCommand> convertToSimpleOrderViewCommandPagination(
            Pagination<SimpleOrderCommand> ordercommand) {
        Pagination<SimpleOrderViewCommand> simpleorderviewcommand = new Pagination<SimpleOrderViewCommand>();
        List<SimpleOrderViewCommand> itemsview = new ArrayList<SimpleOrderViewCommand>();
        BeanUtil.copyProperties(simpleorderviewcommand, ordercommand);
        List<SimpleOrderCommand> items = ordercommand.getItems();
        for (SimpleOrderCommand simpleOrderCommand : items) {
            SimpleOrderViewCommand svc = new SimpleOrderViewCommand();
            BeanUtil.copyProperties(svc, simpleOrderCommand);
            List<SimpleOrderLineSubViewCommand> list = orderLineManager.findByOrderID(svc.getOrderId());
            svc.setSimpleOrderLineSubViewCommandList(list);
            itemsview.add(svc);
        }
        simpleorderviewcommand.setItems(itemsview);
        return simpleorderviewcommand;
    }

}
