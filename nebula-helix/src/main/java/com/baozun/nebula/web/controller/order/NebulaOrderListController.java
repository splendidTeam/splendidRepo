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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.baozun.nebula.web.controller.order.form.OrderQueryForm;
import com.baozun.nebula.web.controller.order.validator.OrderQueryFormValidator;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.date.DateUtil;
import loxia.dao.Page;
import loxia.dao.Pagination;

/**
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 下午3:47:11
 * @since 5.3.1
 */
public class NebulaOrderListController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaOrderListController.class);
    //三个月内
    private static final String timetype_3month="3month";
    //三个月前
    private static final String timetype_3month_ago="3monthago";
    //新建（未支付）
    private static final String orderStatus_new="new";

    @Autowired
    @Qualifier("SalesOrderManager")
    private  SalesOrderManager salerordermanager;

    @Autowired
    @Qualifier("OrderLineManager")
    private  OrderLineManager orderlinemanager;
    
    
    @Autowired
    @Qualifier("orderqueryformvalidator")
    private OrderQueryFormValidator orderqueryformvalidator;

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
     * @see com.baozun.nebula.sdk.manager.OrderManager#findOrders(loxia.dao.Page, loxia.dao.Sort[],
     *      java.util.Map)
     * @see com.baozun.nebula.dao.salesorder.SdkOrderDao#findOrders(loxia.dao.Page,
     *      loxia.dao.Sort[], java.util.Map)
     * 
     * @see com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand   BindingResult bindingResul
     */
    public   String showOrderList(@LoginMember MemberDetails memberDetails,
            @ModelAttribute("orderQueryForm") OrderQueryForm orderQueryForm,
            PageForm pageForm,HttpServletRequest request,HttpServletResponse response,Model model,BindingResult bindingResult) {

        // step1: validate OrderQueryForm
        orderqueryformvalidator.validate(orderQueryForm, bindingResult);
        //校验查询条件非空
        if(bindingResult.hasErrors()){
           NebulaReturnResult resultFromBindingResult = super.getResultFromBindingResult(bindingResult);
           model.addAttribute("errors", resultFromBindingResult);
           return "order.orderlist";
        }
        // step2: convert OrderQueryForm to OrderQueryCommand
        // queryform表单进行转换
        OrderQueryCommand convert = null;
        try {
            convert = convertToOrderQueryCommand(orderQueryForm);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // step3: get result Pagination <SimpleOrderCommand> from service
        Page page = new Page(pageForm.getCurrentPage(), 5);// 当前页，每页5条
        //获取数据
        Pagination<SimpleOrderCommand> findSimpleOrder = salerordermanager.finorderByOrderQueryForm(page,
                convert, memberDetails.getMemberId());
        // step4: convert Pagination <SimpleOrderCommand> to
        // Pagination <SimpleOrderViewCommand> in builder
        Pagination<SimpleOrderViewCommand> SimpleOrderViewCommand = convertToSimpleOrderViewCommand(findSimpleOrder);
        // step5: model构建两个key
        // "currentSimpleOrderViewCommandPagination" 以及
        // "orderQueryForm"
        model.addAttribute("currentSimpleOrderViewCommandPagination", SimpleOrderViewCommand);
        // (理论上 orderQueryForm可以自动随着model返回(隐式),此处是否需要显示设置 可以讨论下)
        model.addAttribute("orderQueryForm", orderQueryForm);
        // 此时的orderQueryForm 可以用于前端的判断 (是否需要再转成orderQueryViewCommand?)
        // step6: return "order.orderlist"
        return "order.orderlist";
    }

    /**
     * 
     * 说明：转换为OrderQueryCommand，前台条件转换
     * 
     * @param orderqueryform
     * @return
     * @author 张乃骐
     * @throws ParseException 
     * @time：2016年5月9日 下午5:01:05
     */
    private  OrderQueryCommand convertToOrderQueryCommand(OrderQueryForm orderqueryform) throws ParseException {
        OrderQueryCommand querycommdand=new OrderQueryCommand();
        BeanUtil.copyProperties(querycommdand, orderqueryform);
        //订单的时间类型判断及转换
        String orderTimeType = orderqueryform.getOrderTimeType();
        Date date = new Date();
        if(Validator.isNotNullOrEmpty(orderTimeType)){
            if(orderTimeType.equals(timetype_3month)){
                querycommdand.setStartDate(DateUtil.addMonth(date, -3));
                querycommdand.setEndDate(date);
            }else if(orderTimeType.equals(timetype_3month_ago)){
                querycommdand.setEndDate(DateUtil.addMonth(date, -3));
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                Date myDate = dateFormat1.parse("2015-01-01");
                querycommdand.setStartDate(myDate);
            }
        }
        //订单类型（未支付，发货中，以完成，取消）
        String orderStatus = orderqueryform.getOrderStatus();
        if(Validator.isNotNullOrEmpty(orderStatus)){
            List<Integer> logisticsStatusList = new ArrayList<Integer>();
            List<Integer> financestatusList = new ArrayList<Integer>();
            //未支付(新建)
            if(orderStatus.equals(orderStatus_new)){
                logisticsStatusList.add(SalesOrder.SALES_ORDER_STATUS_NEW);//新建订单
                financestatusList.add(SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);//财务状态未支付
            }
            //TODO 其他状态的判断
        }
        return querycommdand;
    }
/**
 * 
 * 说明：后台SimpleOrderCommand转换为SimpleOrderViewCommand，SimpleOrderViewCommand添加订单行的的信息
 * @param ordercommand
 * @return
 * @author 张乃骐
 * @time：2016年5月9日 下午7:12:26
 */
  private  Pagination <SimpleOrderViewCommand> convertToSimpleOrderViewCommand(Pagination <SimpleOrderCommand> ordercommand){
      Pagination<SimpleOrderViewCommand> simpleorderviewcommand =new Pagination<SimpleOrderViewCommand>();
      List<SimpleOrderViewCommand> itemsview = new ArrayList<SimpleOrderViewCommand>();
      BeanUtil.copyProperties(simpleorderviewcommand, ordercommand);
      List<SimpleOrderCommand> items = ordercommand.getItems();
      for (SimpleOrderCommand simpleOrderCommand : items) {
          SimpleOrderViewCommand svc=new SimpleOrderViewCommand();
          BeanUtil.copyProperties(svc, simpleOrderCommand);
          List<SimpleOrderLineSubViewCommand> list= orderlinemanager.findByOrderID(svc.getOrderId());
          svc.setSimpleOrderLineSubViewCommandList(list);
          itemsview.add(svc);
     }
      simpleorderviewcommand.setItems(itemsview);
    return simpleorderviewcommand;
  }
  
  
}
    
