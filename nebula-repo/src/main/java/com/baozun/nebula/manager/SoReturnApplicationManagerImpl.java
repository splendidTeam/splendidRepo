package com.baozun.nebula.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkReturnApplicationDao;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

public class SoReturnApplicationManagerImpl implements SoReturnApplicationManager{
	
	private static final Logger log = LoggerFactory.getLogger(SoReturnApplicationManagerImpl.class);
	
	@Autowired
	private SdkReturnApplicationDao soReturnApplicationDao;
	
	private SdkOrderDao sdkOrderDao;
	
	private SoReturnLineManager soReturnLineManager;
	
	public final static Integer[] statusArr = {new Integer(SoReturnConstants.RETURN_COMPLETE) };

	@Override
	public Integer countCompletedAppsByPrimaryLineId(Long primaryLineId) {
		Integer count = null;
			count = soReturnApplicationDao.countItemByOrderLineIdAndStatus(primaryLineId, statusArr);
			if (count == null) {
				count = new Integer(0);
			}
		return count;
	}
	
	@Override
	public SoReturnApplication findLastApplicationByOrderLineId(Long orderLineId) {
		if (orderLineId == null) {
			log.error("===orderLineId is null ==");
			return null;
		}
		return soReturnApplicationDao.findLastApplicationByOrderLineId(orderLineId);
	}

	@Override
	public Boolean isFinishedAndOutDayOrderById(Long orderId) {
		if (orderId == null) {
			log.error("==orderId is null==");
			return false;
		}
		SalesOrder salesOrder = sdkOrderDao.findFinishedOrderById(orderId, SalesOrder.SALES_ORDER_STATUS_FINISHED);
		return salesOrder!=null;
		
	}

	@Transactional
	@Override
	public SoReturnApplication createReturnApplication(ReturnApplicationCommand returnCommand,SalesOrderCommand orderCommand) {
		SoReturnApplication app=new SoReturnApplication();
		app.setAccountName(returnCommand.getAccountName());
		app.setApprovalDescription(returnCommand.getApprovalDescription());
		app.setApprover(returnCommand.getApprover());
		app.setApproveTime(returnCommand.getApproveTime());
		app.setId(returnCommand.getId());
		app.setIsNeededReturnInvoice(returnCommand.getIsNeededReturnInvoice());
		app.setLastModifyUser(returnCommand.getLastModifyUser());
		app.setMemberId(returnCommand.getMemberId());
		app.setMemo(returnCommand.getMemo());
		app.setOmsStatus(returnCommand.getOmsStatus());
		app.setPlatformOMSCode(returnCommand.getPlatformOMSCode());
		app.setRefundAccount(returnCommand.getRefundAccount());
		app.setRefundAccountBank(returnCommand.getRefundAccountBank());
		app.setRefundBank(returnCommand.getRefundBank());
		app.setRefundPayee(returnCommand.getRefundPayee());
		app.setRefundStatus(returnCommand.getRefundStatus());
		app.setRefundType(returnCommand.getRefundType());
		app.setReturnAddress(returnCommand.getReturnAddress());
		app.setReturnFreight(returnCommand.getReturnFreight());
		app.setReturnOrderCode(returnCommand.getReturnOrderCode());
		app.setReturnPrice(returnCommand.getReturnPrice());
		app.setReturnReason(returnCommand.getReturnReason());
		app.setSoOrderCode(returnCommand.getSoOrderCode());
		app.setSoOrderId(returnCommand.getSoOrderId());
		app.setStatus(returnCommand.getStatus());
		app.setSynType(returnCommand.getSynType());
		app.setTransCode(returnCommand.getTransCode());
		app.setType(returnCommand.getType());
		app.setVersion(returnCommand.getVersion());
		app=this.saveSoReturnApplication(app);
		List<SoReturnLine> returnLine=returnCommand.getReturnLineList();
		for(SoReturnLine line:returnLine){
			line.setReturnOrderId(app.getId());
		}
		soReturnLineManager.saveReturnLine(returnLine);
		if(null!=app){
			return app;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 退换货申请表 对象保存
	 * 
	 * @author yinglong.xu
	 */
	private SoReturnApplication saveSoReturnApplication(SoReturnApplication soReturnApplication) {
		soReturnApplication = soReturnApplicationDao.save(soReturnApplication);
		return soReturnApplication;
	}
	
	@Override
	public SoReturnApplication findLastApplicationByOrderId(Long orderId) {
		SoReturnApplication app=soReturnApplicationDao.findLastApplicationByOrderId(orderId);
		return app;
	}

}
