package com.baozun.nebula.web.controller.order.resolver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;

public class ReturnApplicationResolverImpl implements ReturnApplicationResolver{
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;

	@Override
	public ReturnApplicationCommand toReturnApplicationCommand(
			MemberDetails memberDetails,ReturnOderForm returnOrderForm, SalesOrderCommand salesOrder) {
		ReturnApplicationCommand appCommand=new ReturnApplicationCommand();
		Date date=new Date();
		appCommand.setCreateTime(date);
		if (memberDetails != null) {
			appCommand.setMemberId(memberDetails.getMemberId());
		}

		appCommand.setMemo(returnOrderForm.getMemo());
		appCommand.setRefundAccount(returnOrderForm.getAccount());
		appCommand.setVersion(date);
		appCommand.setRefundAccountBank(returnOrderForm.getBranch());
		appCommand.setRefundBank(returnOrderForm.getBank());
		appCommand.setType(SoReturnConstants.TYPE_RETURN);
		appCommand.setSoOrderCode(returnOrderForm.getOrderCode());
		appCommand.setSoOrderId(Long.parseLong(returnOrderForm.getOrderId()));
		appCommand.setReturnOrderCode("VR" + new Date().getTime());
		appCommand.setRefundType(salesOrder.getPayment().toString());// 退款方式
		appCommand.setIsNeededReturnInvoice(SoReturnConstants.NEEDED_RETURNINVOICE);
		appCommand.setReturnReason(returnOrderForm.getRetrunReason());
		appCommand.setStatus(0);
		if (memberDetails != null) {// 如果是会员 非游客
			appCommand.setMemberId(memberDetails.getMemberId());
		} else {// 游客下单把下单 邮箱 作冗余过来
			appCommand.setMemberId(-1L);// 游客
			appCommand.setRefundPayee(salesOrder.getEmail());
		}
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
			if (returnReason.equals("我改变主意了")) {
				returnLine.setReturnReason(SoReturnConstants.CHEANGE_MIND);
			} else if (returnReason.equals("商品质量有问题")) {
				returnLine.setReturnReason(SoReturnConstants.DAMAGED_GOOD);
			} else if (returnReason.equals("商品包装破损")) {
				returnLine.setReturnReason(SoReturnConstants.DAMAGED_PACKAGE);
			} else if (returnReason.equals("尺码不合适")) {
				returnLine.setReturnReason(SoReturnConstants.SIZE_UNMATCH);
			} else if (returnReason.equals("颜色/款式与商品不符")) {
				returnLine.setReturnReason(SoReturnConstants.PRODUCT_UNMATCH);
			}else if(returnReason.equals("其他原因")){
				returnLine.setReturnReason(SoReturnConstants.OTHER_REASON);
			}
			else {
				return null;
			}
			String[] sumSelected=returnOrderForm.getSumSelected().split(",");
			returnLine.setQty(Integer.parseInt(sumSelected[i]));
			returnLine.setSoLineId(lineId);
			returnLine.setMemo(returnOrderForm.getMemo());
			returnLine.setRtnExtentionCode(line.getExtentionCode());
			returnTotalMoney = returnTotalMoney.add(line.getSubtotal().divide(new BigDecimal(line.getCount()))
					.multiply(
							new BigDecimal(Integer.parseInt(sumSelected[i]))));
			returnLine.setReturnPrice(line.getSubtotal().divide(new BigDecimal(line.getCount()))
					.multiply(
							new BigDecimal(Integer.parseInt(sumSelected[i]))));
			returnLine.setType(1);
			returnLine.setCreateTime(date);
			returnLineList.add(returnLine);
		}
		appCommand.setReturnLineList(returnLineList);
		appCommand.setReturnPrice(returnTotalMoney);
		return appCommand;
	}

}
