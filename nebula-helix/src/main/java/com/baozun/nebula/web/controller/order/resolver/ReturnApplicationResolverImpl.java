package com.baozun.nebula.web.controller.order.resolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.salesorder.SdkReturnApplicationDao;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.manager.returnapplication.SoReturnApplicationManager;
import com.baozun.nebula.sdk.manager.returnapplication.SoReturnLineManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

@Service("returnApplicationResolver")
public class ReturnApplicationResolverImpl implements ReturnApplicationResolver{
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private SdkSkuManager sdkSkuManager;
	
	@Autowired
	private SoReturnApplicationManager soReturnApplicationManager;
	
	@Autowired
	private SoReturnLineManager soReturnLineManager;
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
	private SdkReturnApplicationDao sdkReturnApplicationDao;
	
	@Autowired
	private OrderManager OrderManager;

	@Override
	public ReturnApplicationCommand toReturnApplicationCommand(
			MemberDetails memberDetails,ReturnOrderForm returnOrderForm, SalesOrderCommand salesOrder) {
		ReturnApplicationCommand appCommand=new ReturnApplicationCommand();
		SoReturnApplication returnApplication=new SoReturnApplication();
		Date date=new Date();
		returnApplication.setCreateTime(date);
		returnApplication.setMemberId(memberDetails.getMemberId());
		returnApplication.setRemark(returnOrderForm.getMemo());
		returnApplication.setRefundAccount(returnOrderForm.getAccount());
		returnApplication.setVersion(date);
		returnApplication.setRefundBankBranch(returnOrderForm.getBranch());
		returnApplication.setRefundBank(returnOrderForm.getBank());
		returnApplication.setType(Integer.parseInt(returnOrderForm.getReturnType()[0]));
		returnApplication.setSoOrderCode(returnOrderForm.getOrderCode());
		returnApplication.setSoOrderId(Long.parseLong(returnOrderForm.getOrderId()));
		returnApplication.setReturnApplicationCode("VR" + new Date().getTime());
		returnApplication.setRefundType(salesOrder.getPayment().toString());// 退款方式
		returnApplication.setIsNeededReturnInvoice(SoReturnConstants.NEEDED_RETURNINVOICE);
		returnApplication.setReturnReason(returnOrderForm.getRetrunReason());
		returnApplication.setStatus(SoReturnConstants.AUDITING);
		List<SoReturnLine> returnLineList = new ArrayList<SoReturnLine>();

		// 总共的退款金额 
		BigDecimal returnTotalMoney = new BigDecimal(0);
		String[] linedIdSelected=returnOrderForm.getLineIdSelected();
		String[] reasonSelected=returnOrderForm.getReasonSelected();
		String[] sumSelected=returnOrderForm.getSumSelected();
		for (int i = 0; i < linedIdSelected.length; i++) {
			SoReturnLine returnLine = new SoReturnLine();
			Long lineId = Long.parseLong(linedIdSelected[i]);
			OrderLine line = sdkOrderLineDao.getByPrimaryKey(lineId);
			String returnReason = reasonSelected[i].trim();
			returnLine.setReturnReason(returnReason);
			returnLine.setQty(Integer.parseInt(sumSelected[i]));
			returnLine.setSoLineId(lineId);
			returnLine.setMemo(returnOrderForm.getMemo());
			returnLine.setType(Integer.parseInt(returnOrderForm.getReturnType()[i]));
			returnLine.setCreateTime(date);
			//退货
			if(SoReturnConstants.TYPE_RETURN==Integer.parseInt(returnOrderForm.getReturnType()[i])){
				returnLine.setRtnExtentionCode(line.getExtentionCode());
				returnLine.setChgExtentionCode(null);
				returnTotalMoney = returnTotalMoney.add(line.getSubtotal().divide(new BigDecimal(line.getCount()))
						.multiply(
								new BigDecimal(Integer.parseInt(sumSelected[i]))));
				returnLine.setReturnPrice(line.getSubtotal().divide(new BigDecimal(line.getCount()))
						.multiply(
								new BigDecimal(Integer.parseInt(sumSelected[i]))));
			}
			//换货，不涉及到金额
			else if(SoReturnConstants.TYPE_EXCHANGE==Integer.parseInt(returnOrderForm.getReturnType()[i])){
				returnLine.setRtnExtentionCode(null);
				returnLine.setChgExtentionCode(returnOrderForm.getExtentionCode()[i]);
			}
			returnLineList.add(returnLine);
		}
		returnApplication.setReturnPrice(returnTotalMoney);
		SoReturnApplicationDeliveryInfo	delivery=returnOrderForm.getReturnDeliveryInfo();
		appCommand.setReturnLineList(returnLineList);
		appCommand.setSoReturnApplicationDeliveryInfo(delivery);
		appCommand.setReturnApplication(returnApplication);
		return appCommand;
	}

	@Override
    public List<ReturnLineViewCommand> toReturnLineViewCommand(List<Long> orderLineIds) {
        List<OrderLineCommand>  orderLineCommands=sdkOrderLineDao.findOrderDetailListByIds(orderLineIds);
        List<ReturnLineViewCommand> soReturnLineViews = new ArrayList<ReturnLineViewCommand>();
        for (OrderLineCommand line : orderLineCommands) {
            String properties = line.getSaleProperty();
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
            line.setSkuPropertys(propList);
            ReturnLineViewCommand lineView = new ReturnLineViewCommand();
            if (null != line.getType() && line.getType() != 0) {
                // 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成)
                Integer count = soReturnApplicationManager
                        .countCompletedAppsByPrimaryLineId(line.getId());
                //剩余可退数量
                lineView.setCount(line.getCount()-count);
                lineView.setOrderLineCommand(line);
                soReturnLineViews.add(lineView);
            }
        }
        return soReturnLineViews;
    }
}
