package com.baozun.nebula.web.controller.order.validator;


import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.manager.SoReturnApplicationManager;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;


public  class ReturnApplicationNormalValidator extends ReturnApplicationValidator{
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
	private SoReturnApplicationManager soReturnApplicationManager;

	@Override
	public void processValidate(Object target, Errors errors) {
		ReturnOderForm form = (ReturnOderForm) target;
		//退货数量、退货理由非空
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sumSelected","sumSelected.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reasonSelected","reasonSelected.required");
		
		SalesOrderCommand saleOrder = orderManager.findOrderById(Long.parseLong(form.getOrderId()), null);
		//订单状态未完成不能退款
		if (null==saleOrder 
				||null==saleOrder.getLogisticsStatus() 
				||saleOrder.getLogisticsStatus().compareTo(
								SalesOrder.SALES_ORDER_STATUS_FINISHED) != 0) {
			errors.rejectValue("orderStatus", "order.status.unfinish");
		}
		
		//检查退货数量是否超出可退数量限制
		List<OrderLineCommand> lineCommandList = saleOrder.getOrderLines();
		
		String[] selectedLineId=form.getLineIdSelected();
		for (OrderLineCommand line : lineCommandList) {
			//将订单中的订单行跟页面中选中的订单行id进行匹配，然后判断是否是不允许退款的商品
			for(int i=0;i<selectedLineId.length;i++){
				Long selected=Long.parseLong(selectedLineId[i]);
				if(line.getId().longValue()==selected.longValue()){
					//通过订单行id查询该订单行已经完成的退货数量
					Integer returnedCount=soReturnApplicationManager.countCompletedAppsByPrimaryLineId(Long.parseLong(selectedLineId[i]),SoReturnConstants.TYPE_RETURN);
					Integer count=line.getCount();
					if(count-Integer.parseInt(form.getSumSelected()[i])<returnedCount){
						// 退换货数量超出限制。
						errors.rejectValue("returnCount", "return.count.outrange");
					
					}
				}
			}
			
			SoReturnApplication app = soReturnApplicationManager.findLastApplicationByOrderLineId(line.getId());
			if (null != app && app.getStatus() != SoReturnConstants.RETURN_COMPLETE&&app.getStatus()!=SoReturnConstants.REFUS_RETURN) {
				// 当前订单尚有一笔未完成的退货单！无法再次申请
				errors.rejectValue("returnOrder", "return.unfinish");
			}
			Boolean isOutDayOrder = soReturnApplicationManager
					.isFinishedAndOutDayOrderById(saleOrder.getId());
			if (BooleanUtils.isTrue(isOutDayOrder)) {
				// 如果是 超过收货14天的订单则不能 申请退货
				errors.rejectValue("returnOrder", "return.outOfDate");
			}
		}
		
	}

	

}
