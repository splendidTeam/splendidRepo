package com.baozun.nebula.web.controller.order.resolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.salesorder.SdkReturnApplicationDao;
import com.baozun.nebula.manager.SoReturnApplicationManager;
import com.baozun.nebula.manager.SoReturnLineManager;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnApplicationViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

public class ReturnApplicationResolverImpl implements ReturnApplicationResolver{
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
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
			MemberDetails memberDetails,ReturnOderForm returnOrderForm, SalesOrderCommand salesOrder) {
		ReturnApplicationCommand appCommand=new ReturnApplicationCommand();
		Date date=new Date();
		appCommand.setCreateTime(date);
		appCommand.setMemberId(memberDetails.getMemberId());
		appCommand.setRemark(returnOrderForm.getMemo());
		appCommand.setRefundAccount(returnOrderForm.getAccount());
		appCommand.setVersion(date);
		appCommand.setRefundBankBranch(returnOrderForm.getBranch());
		appCommand.setRefundBank(returnOrderForm.getBank());
		appCommand.setType(returnOrderForm.getReturnType());
		appCommand.setSoOrderCode(returnOrderForm.getOrderCode());
		appCommand.setSoOrderId(Long.parseLong(returnOrderForm.getOrderId()));
		appCommand.setReturnApplicationCode("VR" + new Date().getTime());
		appCommand.setRefundType(salesOrder.getPayment().toString());// 退款方式
		appCommand.setIsNeededReturnInvoice(SoReturnConstants.NEEDED_RETURNINVOICE);
		appCommand.setReturnReason(returnOrderForm.getRetrunReason());
		appCommand.setStatus(SoReturnConstants.AUDITING);
		List<SoReturnLine> returnLineList = new ArrayList<SoReturnLine>();

		// 总共的退款金额 
		BigDecimal returnTotalMoney = new BigDecimal(0);
		String[] linedIdSelected=returnOrderForm.getLineIdSelected().split(",");
		String[] reasonSelected=returnOrderForm.getReasonSelected().split(",");
		for (int i = 0; i < linedIdSelected.length; i++) {
			SoReturnLine returnLine = new SoReturnLine();
			Long lineId = Long.parseLong(linedIdSelected[i]);
			OrderLine line = sdkOrderLineDao.getByPrimaryKey(lineId);

			String returnReason = reasonSelected[i].trim();
				returnLine.setReturnReason(returnReason);
		
			String[] sumSelected=returnOrderForm.getSumSelected().split(",");
			returnLine.setQty(Integer.parseInt(sumSelected[i]));
			returnLine.setSoLineId(lineId);
			returnLine.setMemo(returnOrderForm.getMemo());
			returnLine.setType(returnOrderForm.getReturnType());
			returnLine.setCreateTime(date);
			//退货
			if(SoReturnConstants.TYPE_RETURN==returnOrderForm.getReturnType()){
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
			else if(SoReturnConstants.TYPE_EXCHANGE==returnOrderForm.getReturnType()){
				returnLine.setRtnExtentionCode(null);
				returnLine.setChgExtentionCode(line.getExtentionCode());
			}
			returnLineList.add(returnLine);
		}
		appCommand.setReturnLineList(returnLineList);
		appCommand.setReturnPrice(returnTotalMoney);
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


	@Override
	public List<ReturnApplicationViewCommand> toTurnReturnApplicationViewCommand(
			List<ReturnApplicationCommand> returnApplications) {

		List<ReturnApplicationViewCommand> viewCommands=new ArrayList<ReturnApplicationViewCommand>();
		
		for(ReturnApplicationCommand returnApp:returnApplications){
			ReturnApplicationViewCommand viewCommand=new ReturnApplicationViewCommand();
			SalesOrderCommand salesOrder = orderManager.findOrderById(Long
					.valueOf(returnApp.getSoOrderId()), null);
			for (OrderLineCommand line : salesOrder.getOrderLines()) {
				String properties = line.getSaleProperty();
				List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
				line.setSkuPropertys(propList);
			}
			viewCommand.setOrderLineCommands(salesOrder.getOrderLines());
			viewCommand.setReturnApplicationCommand(returnApp);
			viewCommands.add(viewCommand);
		}
		return viewCommands;
	}

	@Override
	public SoReturnApplicationDeliveryInfo toReturnApplicationDelivery(
			ReturnOderForm returnOrderForm) {
		SoReturnApplicationDeliveryInfo	delivery=new SoReturnApplicationDeliveryInfo();
		delivery.setAddress(returnOrderForm.getAddress());
		delivery.setCity(returnOrderForm.getCity());
		delivery.setCountry(returnOrderForm.getCountry());
		delivery.setCreateTime(new Date());
		delivery.setDescription(returnOrderForm.getDescription());
		delivery.setProvince(returnOrderForm.getProvince());
		delivery.setReceiver(returnOrderForm.getReceiver());
		delivery.setReceiverMobile(returnOrderForm.getReceiverMobile());
		delivery.setReceiverPhone(returnOrderForm.getReceiverPhone());
		delivery.setTown(returnOrderForm.getTown());
		delivery.setTransCode(returnOrderForm.getTransCode());
		delivery.setTransName(returnOrderForm.getTransName());
		delivery.setZipcode(returnOrderForm.getZipcode());
		return delivery;
	}




}
