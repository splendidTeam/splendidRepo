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
import com.baozun.nebula.manager.SoReturnApplicationManager;
import com.baozun.nebula.manager.SoReturnLineManager;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;

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
			MemberDetails memberDetails,ReturnOderForm returnOrderForm, SalesOrderCommand salesOrder) {
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
		returnApplication.setType(returnOrderForm.getReturnType());
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
				returnLine.setChgExtentionCode(returnOrderForm.getChg_extentionCode()[i]);
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
}
