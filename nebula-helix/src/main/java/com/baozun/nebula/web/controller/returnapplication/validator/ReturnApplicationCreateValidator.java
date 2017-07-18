package com.baozun.nebula.web.controller.returnapplication.validator;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;
import com.feilong.core.date.DateUtil;


public  class ReturnApplicationCreateValidator implements Validator{
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
    private SdkOrderLineDao sdkOrderLineDao;
	
	@Autowired
	private SdkReturnApplicationManager sdkReturnApplicationManager;
	
    @Autowired
    private SdkSkuManager sdkSkuManager;

    public final static Integer[] statusArr = { ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE };

	@Override
	public void validate(Object target, Errors errors) {
		ReturnOrderForm form = (ReturnOrderForm) target;
		validateReturnOrderForm(form,errors);
		validateSalesOrder(form,errors);
		validateReturnApplication(form,errors);
	}

    private void validateReturnApplication(ReturnOrderForm form,Errors errors){
        //检查退货数量书否超出可退数量限制
        Long[] lineCommandList = form.getLineIdSelected();
        for (Long line : lineCommandList) {
            ReturnApplication app = sdkReturnApplicationManager.findLastApplicationByOrderLineId(line);
            if (com.feilong.core.Validator.isNotNullOrEmpty(app) && !ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE.equals(app.getStatus()) && !ReturnApplication.SO_RETURN_STATUS_REFUS_RETURN.equals(app.getStatus())) {
                // 当前订单尚有一笔未完成的退货单！无法再次申请
                errors.rejectValue("orderCode", "return.unfinish");
            }
        }
    }

    private void validateSalesOrder(ReturnOrderForm form,Errors errors){
        SalesOrderCommand saleOrder = null;
        if(com.feilong.core.Validator.isNotNullOrEmpty(form.getOrderId())){
            saleOrder = orderManager.findOrderById(form.getOrderId(), null);
        }
        if(com.feilong.core.Validator.isNotNullOrEmpty(form.getOrderCode())){
            saleOrder = orderManager.findOrderByCode(form.getOrderCode(), null);
        }
        if (com.feilong.core.Validator.isNotNullOrEmpty(saleOrder)) {
            // 订单行信息
            List<Long> orderIds = new ArrayList<Long>();
            orderIds.add(saleOrder.getId());
            List<OrderLineCommand> orderLines = sdkOrderLineDao.findOrderDetailListByOrderIds(orderIds);
            for (OrderLineCommand orderLineCommand : orderLines){
                String properties = orderLineCommand.getSaleProperty();
                List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
                orderLineCommand.setSkuPropertys(propList);
            }
            saleOrder.setOrderLines(orderLines);
        }else{
            errors.rejectValue("orderCode", "saleOrder.is.notexist");
        }
        //订单无法判断节点状态
        if (com.feilong.core.Validator.isNullOrEmpty(saleOrder.getLogisticsStatus())) {
            errors.rejectValue("orderCode", "saleOrder.has.notLogisticsStatus");
        }
        //订单节点状态为完成
        if (!saleOrder.getLogisticsStatus().equals(SalesOrder.SALES_ORDER_STATUS_FINISHED)) {
            errors.rejectValue("orderCode", "order.status.unfinish");
        }
        Date date = new Date(System.currentTimeMillis());
        Date createTime = DateUtil.addDay(saleOrder.getCreateTime(), 14); 
        if(date.after(createTime)){
            errors.rejectValue("orderCode", "order.createTime.overtime");
        }
        
        //检查退货数量书否超出可退数量限制
        List<OrderLineCommand> lineCommandList = saleOrder.getOrderLines();
        Long[] selectedLineId = form.getLineIdSelected();
        for (OrderLineCommand line : lineCommandList) {
            //将订单中的订单行跟页面中选中的订单行id进行匹配，然后判断是否是不允许退款的商品
            for(int i=0; i<selectedLineId.length; i++){
                Long selected = selectedLineId[i];
                if(line.getId().equals(selected)){
                    //通过订单行id查询该订单行已经完成的退货数量
                    Integer returnedCount=  sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(selectedLineId[i],statusArr);
                    Integer count=line.getCount();
                    if(count-form.getSumSelected()[i]<returnedCount){
                        // 退货数量超出限制。
                        errors.rejectValue("orderCode", "return.count.outrange");
                    
                    }
                }
            }
        }
    }

    private void validateReturnOrderForm(ReturnOrderForm form,Errors errors){
        //退货数量、退货理由非空
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sumSelected","sumSelected.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reasonSelected","reasonSelected.required");
        if(com.feilong.core.Validator.isNullOrEmpty(form.getOrderCode())&&com.feilong.core.Validator.isNullOrEmpty(form.getOrderId())){
            errors.rejectValue("returnorder", "returnorder.order.notexist");
            return;
        }
    }

    @Override
    public boolean supports(Class<?> clazz){
        return false;
    }


	

}
