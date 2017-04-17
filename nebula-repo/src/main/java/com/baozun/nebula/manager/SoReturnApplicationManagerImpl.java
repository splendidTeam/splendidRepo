package com.baozun.nebula.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.constant.SoReturnConstants;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.dao.salesorder.SdkReturnApplicationDao;
import com.baozun.nebula.dao.salesorder.SdkSoReturnApplicationDeliveryInfoDao;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

public class SoReturnApplicationManagerImpl implements SoReturnApplicationManager{
	
	private static final Logger log = LoggerFactory.getLogger(SoReturnApplicationManagerImpl.class);
	
	@Autowired
	private SdkReturnApplicationDao soReturnApplicationDao;
	@Autowired
	private SdkOrderDao sdkOrderDao;
	@Autowired
	private SoReturnLineManager soReturnLineManager;
	@Autowired
	private SdkSoReturnApplicationDeliveryInfoDao sdkSoReturnApplicationDeliveryInfoDao;
	
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
	public SoReturnApplication createReturnApplication(ReturnApplicationCommand returnCommand,SalesOrderCommand orderCommand,
			SoReturnApplicationDeliveryInfo deliveryInfo) {
		SoReturnApplication app=new SoReturnApplication();
		app.setApprovalDescription(returnCommand.getApprovalDescription());
		app.setApprover(returnCommand.getApprover());
		app.setApproveTime(returnCommand.getApproveTime());
		app.setId(returnCommand.getId());
		app.setIsNeededReturnInvoice(returnCommand.getIsNeededReturnInvoice());
		app.setLastModifyUser(returnCommand.getLastModifyUser());
		app.setMemberId(returnCommand.getMemberId());
		app.setOmsStatus(returnCommand.getOmsStatus());
		app.setOmsCode(returnCommand.getOmsCode());
		app.setRefundAccount(returnCommand.getRefundAccount());
		app.setRefundBank(returnCommand.getRefundBank());
		app.setRefundStatus(returnCommand.getRefundStatus());
		app.setRefundType(returnCommand.getRefundType());
		app.setReturnAddress(returnCommand.getReturnAddress());
		app.setReturnFreight(returnCommand.getReturnFreight());
		app.setReturnPrice(returnCommand.getReturnPrice());
		app.setReturnReason(returnCommand.getReturnReason());
		app.setSoOrderCode(returnCommand.getSoOrderCode());
		app.setSoOrderId(returnCommand.getSoOrderId());
		app.setStatus(returnCommand.getStatus());
		app.setTransCode(returnCommand.getTransCode());
		app.setType(returnCommand.getType());
		app.setVersion(returnCommand.getVersion());
		app.setCreateTime(returnCommand.getCreateTime());
		app.setReturnApplicationCode(returnCommand.getReturnApplicationCode());
		app.setSoOrderLineId(returnCommand.getSoOrderLineId());
		app.setSource(returnCommand.getSource());
		app.setTransName(returnCommand.getTransName());
		app.setRefundBankBranch(returnCommand.getRefundBankBranch());
		
		if(SoReturnConstants.TYPE_EXCHANGE==returnCommand.getType()){
		}
		app=this.saveSoReturnApplication(app);
		if(null!=deliveryInfo){
			deliveryInfo.setRetrunApplicationId(returnCommand.getId());
			sdkSoReturnApplicationDeliveryInfoDao.save(deliveryInfo);
		}
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

	@Override
	public Pagination<SoReturnApplication> findReturnByQueryMapWithPage(
			Page page, Sort[] sorts,  Map<String, Object> paraMap) {
		return soReturnApplicationDao.findReturnByQueryMapWithPage(page, sorts,paraMap);
	}


	@Override
	public SoReturnApplication findByApplicationId(Long id) {
		return soReturnApplicationDao.getByPrimaryKey(id);
	}
	
	// 退货申请
		@Override
		public SoReturnApplication auditSoReturnApplication(String returnCode,Integer status, String description, String lastModifier,String omsCode,String returnAddress) throws Exception {
			Assert.notNull(returnCode, "returnCode is null");
			Assert.notNull(lastModifier, "lastModifier is null ");
			Assert.notNull(status, "Status is null ");
			Assert.notNull(description, "审核备注不能为空");
			// 当审核通过时，允许客户退回商品，同时将退款状态改为待处理
			Date now = new Date();
			SoReturnApplication returnapp = soReturnApplicationDao.findApplicationByCode(returnCode);
			if (returnapp == null) {
				throw new Exception("对应的申请单不存在");
			}
			//审核通过时必须填写退货地址
			if(status.intValue()==2&&returnAddress==""){
				throw new Exception("退货地址为空");
			}else{
				returnapp.setReturnAddress(returnAddress);
			}
			if(omsCode!=null&&omsCode!=""){
				returnapp.setOmsCode(omsCode);
			}
			returnapp.setApprovalDescription(description);
			// 当前退货状态为待审核，并且页面操作为审核
			if (status.intValue() == 2 && returnapp.getStatus() == 0) {// 审核通过
				// status为2时，表示已进行审核操作，需要判断当前退货单是否已审核过
				if (returnapp.getStatus() == SoReturnConstants.TO_DELIVERY
						|| returnapp.getStatus() == SoReturnConstants.REFUS_RETURN) {
					throw new Exception("对应的申请单已审核，请刷新页面 ");
				} else {
					returnapp.setStatus(SoReturnConstants.TO_DELIVERY);// 审核通过
				}
			}
			if (status == 1 && returnapp.getStatus() == 0) {// 审核退回
				// status为1时，表示已进行审核操作，需要判断当前退货单是否已审核过
				if (returnapp.getStatus() == SoReturnConstants.TO_DELIVERY
						|| returnapp.getStatus() == SoReturnConstants.REFUS_RETURN) {
					throw new Exception("对应的申请单已审核，请刷新页面 ");
				} else {
					returnapp.setStatus(SoReturnConstants.REFUS_RETURN);
				}
			}
				// 拒绝退款
				if (status == 1) {
					returnapp.setStatus(SoReturnConstants.REFUS_RETURN);
				}
				// 同意退款
				if (status == 4) {
					returnapp.setStatus(SoReturnConstants.AGREE_REFUND);
				}
			// 当前状态为同意退款并且页面操作为退款完成
			if (status == 5 && returnapp.getStatus() == 4) {
				returnapp.setStatus(SoReturnConstants.RETURN_COMPLETE);
			}
			returnapp.setOmsCode(omsCode);
			returnapp.setLastModifyUser(lastModifier);
			returnapp.setApprover(lastModifier);
			returnapp.setApproveTime(now);
			returnapp.setVersion(now);
			returnapp.setReturnReason("");
			returnapp = soReturnApplicationDao.save(returnapp);
			return returnapp;
		}
		

		@Override
		public List<OrderReturnCommand> findExpInfo(Sort[] sorts,Map<String, Object> paraMap) {
			List<OrderReturnCommand> orderReturn=soReturnApplicationDao.findExpInfo(sorts,paraMap);
			return orderReturn;
		}
		

		@Override
		public List<ReturnApplicationCommand> findReturnApplicationCommandsByIds(
				List<Long> ids) {
		List<ReturnApplicationCommand>	 returnApplicationCommands=soReturnApplicationDao.findReturnApplicationCommandByIds(ids);
			return returnApplicationCommands;
		}
}
