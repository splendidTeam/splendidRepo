package com.baozun.nebula.web.controller.returnapplication.resolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.api.returnapplication.ReturnApplicationCodeCreatorManager;
import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.returnapplication.SdkReturnApplicationDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationLineManager;
import com.baozun.nebula.sdk.manager.returnapplication.SdkReturnApplicationManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnLineViewCommand;
import com.feilong.core.Validator;

@Service("returnApplicationResolver")
public class ReturnApplicationResolverImpl implements ReturnApplicationResolver{
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private SdkSkuManager sdkSkuManager;
	
	@Autowired
	private SdkReturnApplicationManager sdkReturnApplicationManager;
	
	@Autowired
	private SdkReturnApplicationLineManager soReturnLineManager;
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
	private SdkReturnApplicationDao sdkReturnApplicationDao;
	
	@Autowired
	private OrderManager OrderManager;
	
    /** The ReturnApplication code creator. */
    @Autowired(required = false)
    private ReturnApplicationCodeCreatorManager returnApplicationCodeCreatorManager;

	@Override
	public ReturnApplicationCommand toReturnApplicationCommand(
			MemberDetails memberDetails,ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder) {
		ReturnApplicationCommand appCommand = new ReturnApplicationCommand();
		ReturnApplication returnApplication = new ReturnApplication();
		Date date = new Date();
		returnApplication.setCreateTime(date);
		returnApplication.setMemberId(memberDetails.getMemberId());
		returnApplication.setRemark(returnOrderForm.getMemo());
		returnApplication.setRefundAccount(returnOrderForm.getAccount());
		returnApplication.setVersion(date);
		returnApplication.setRefundBankBranch(returnOrderForm.getBranch());
		returnApplication.setRefundBank(returnOrderForm.getBank());
		returnApplication.setType(Integer.parseInt(returnOrderForm.getReturnType()[0]));
		returnApplication.setSoOrderCode(returnOrderForm.getOrderCode());
		returnApplication.setSoOrderId(returnOrderForm.getOrderId());
		returnApplication.setRefundType(String.valueOf(salesOrder.getPayment()));// 退款方式
		returnApplication.setIsNeededReturnInvoice(ReturnApplication.SO_RETURN_NEEDED_RETURNINVOICE);
		returnApplication.setReturnReason(returnOrderForm.getRetrunReason());
		returnApplication.setStatus(ReturnApplication.SO_RETURN_STATUS_AUDITING);
		List<ReturnApplicationLine> returnLineList = new ArrayList<ReturnApplicationLine>();

		// 总共的退款金额 
		BigDecimal returnTotalMoney = new BigDecimal(0);
		Long[] linedIdSelected = returnOrderForm.getLineIdSelected();
		String[] reasonSelected = returnOrderForm.getReasonSelected();
		Integer[] sumSelected = returnOrderForm.getSumSelected();
		for (int i = 0; i < linedIdSelected.length; i++) {
		    ReturnApplicationLine returnLine = new ReturnApplicationLine();
			Long lineId = linedIdSelected[i];
			OrderLine line = sdkOrderLineDao.getByPrimaryKey(lineId);
			String returnReason = reasonSelected[i].trim();
			returnLine.setReturnReason(returnReason);
			returnLine.setQty(sumSelected[i]);
			returnLine.setSoLineId(lineId);
			returnLine.setMemo(returnOrderForm.getMemo());
			returnLine.setType(Integer.parseInt(returnOrderForm.getReturnType()[i]));
			returnLine.setCreateTime(date);
			//退货
			if(ReturnApplication.SO_RETURN_TYPE_RETURN.equals(returnOrderForm.getReturnType())){
				returnLine.setRtnExtentionCode(line.getExtentionCode());
				returnLine.setChgExtentionCode(null);
				returnTotalMoney = returnTotalMoney.add(line.getSubtotal().divide(new BigDecimal(line.getCount()))
						.multiply(new BigDecimal(sumSelected[i])));
				returnLine.setReturnPrice(line.getSubtotal().divide(new BigDecimal(line.getCount()))
						.multiply(new BigDecimal(sumSelected[i])));
			}
			//换货，不涉及到金额
			else if(ReturnApplication.SO_RETURN_TYPE_EXCHANGE.equals(returnOrderForm.getReturnType()[i])){
				returnLine.setRtnExtentionCode(null);
				returnLine.setChgExtentionCode(returnOrderForm.getExtentionCode()[i]);
			}
			returnLineList.add(returnLine);
		}
		returnApplication.setReturnPrice(returnTotalMoney);
		ReturnApplicationDeliveryInfo delivery = returnOrderForm.getReturnDeliveryInfo();
		appCommand.setReturnLineList(returnLineList);
		appCommand.setSoReturnApplicationDeliveryInfo(delivery);
		appCommand.setReturnApplication(returnApplication);
		String code = returnApplicationCodeCreatorManager.createReturnApplicationCodeBySource(appCommand);
        if(Validator.isNotNullOrEmpty(appCommand)&&Validator.isNotNullOrEmpty(appCommand.getReturnApplication())){
            appCommand.getReturnApplication().setReturnApplicationCode(code);
        }
		return appCommand;
	}

	@Override
    public List<ReturnLineViewCommand> toReturnLineViewCommand(List<Long> orderLineIds) {
        List<OrderLineCommand>  orderLineCommands = sdkOrderLineDao.findOrderDetailListByIds(orderLineIds);
        List<ReturnLineViewCommand> soReturnLineViews = new ArrayList<ReturnLineViewCommand>();
        for (OrderLineCommand line : orderLineCommands) {
            String properties = line.getSaleProperty();
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
            line.setSkuPropertys(propList);
            ReturnLineViewCommand lineView = new ReturnLineViewCommand();
            if (null != line.getType() && line.getType() != 0) {
                // 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成)
                Integer count = sdkReturnApplicationManager.countCompletedAppsByPrimaryLineId(line.getId(), new Integer[]{ ReturnApplication.SO_RETURN_STATUS_RETURN_COMPLETE });
                //剩余可退数量
                lineView.setCount(line.getCount()-count);
                lineView.setOrderLineCommand(line);
                soReturnLineViews.add(lineView);
            }
        }
        return soReturnLineViews;
    }
}
