/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.wormhole.scm.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.wormhole.constants.OrderStatusV5Constants;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.scm.handler.SyncSalesOrderHandler;
import com.baozun.nebula.wormhole.scm.timing.SyncCommonManager;

/**
 * @author yfxie
 *
 */
@Service("salesOrderManager")
public class SalesOrderManagerImpl implements SalesOrderManager {
	
	private final Logger logger = LoggerFactory.getLogger(SalesOrderManagerImpl.class);
	
	@Autowired
	private SyncCommonManager syncCommonManager;
	
	@Autowired(required=false)
	private SyncSalesOrderHandler syncSalesOrderHandler;
	
	@Autowired
	private OrderManager sdkOrderService;
	
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	
	/** 程序返回结果 **/
	private static final Integer SUCCESS = 1;
	//private static final Integer FAILURE = 0;
	
	private static final String SUCCESS_KEY = "SUCCESS_KEY";
	private static final String NEED_HANDLE_KEY = "NEED_HANDLE_KEY";
	

	@Override
	@Transactional
	public void syncSoStatus(MsgReceiveContent content,List<Long> msgIdList) {
		List<OrderStatusV5> orderSV5List = syncCommonManager.queryMsgBody(content.getMsgBody(),OrderStatusV5.class);
		if(Validator.isNotNullOrEmpty(orderSV5List)){
			boolean result = syncSoStatus(orderSV5List);
			if(null != syncSalesOrderHandler){
				syncSalesOrderHandler.syncSoStatus(orderSV5List);
			}
			if(result){
				logger.info("订单状态同步信息："+content.getId()+"同步完成！");
			} else {
				//发送告警邮件
				try {
					Map<String,Object> dataMap = new HashMap<String,Object>();
					dataMap.put("desc", "订单状态同步失败，失败信息的id为："+content.getMsgId());
					emailTemplateManager.sendWarningEmail("EMAIL_WARNING", "order_status_sync-"+content.getId(), dataMap);
				} catch(Exception e){
					e.printStackTrace();
				}
				logger.error("订单状态同步信息："+content.getId()+"同步失败！");
			}
			sdkMsgManager.updateMsgRecContIsProByIds(msgIdList);
		}
	}
	
	/**
	 * 逐条处理订单信息
	 * 
	 * @param orderSV5List
	 * @return
	 */
	public boolean syncSoStatus(List<OrderStatusV5> orderSV5List) {
		boolean allSuccess = true;
		if(Validator.isNotNullOrEmpty(orderSV5List)){
			handleOrderNotExists(orderSV5List);
			Boolean existsFail = false;
			for(OrderStatusV5 orderStatusV5 : orderSV5List){
				existsFail = handleOrderStatus(orderStatusV5);
				if(!existsFail){
					allSuccess = false;
				}
			}
		}
		return allSuccess;
	}
	
	/**
	 * 处理在商城端不存在的订单
	 * 
	 * @param orderSV5List
	 */
	private void handleOrderNotExists(List<OrderStatusV5> orderSV5List){
		if(null != orderSV5List && orderSV5List.size()>0){
			for(int i=0;i<orderSV5List.size();i++){
			    OrderStatusV5 orderStatusV5 = orderSV5List.get(i);
			    if(null == orderStatusV5){
			    	orderSV5List.remove(i);
			    	i--;
			    	continue;
			    }
				SalesOrderCommand order = sdkOrderService.findOrderByCode(orderSV5List.get(i).getBsOrderCode(), null);
				if(null == order){
					orderSV5List.remove(i);
			    	i--;
				}
			}
		}
	}
	
	/**
	 * 处理订单状态
	 * 
	 * @param orderStatusV5
	 */
	private boolean handleOrderStatus(OrderStatusV5 orderStatusV5){
		boolean handleSucc = true;
		if(null != orderStatusV5){
			Integer status = orderStatusV5.getOpType();
			if(OrderStatusV5Constants.ORDER_NEW.equals(status)){
				return handleOrderNew(orderStatusV5);
			} else if(OrderStatusV5Constants.ORDER_CANCEL.equals(status)){
				return handleCancelOrder(orderStatusV5);
			} else if(OrderStatusV5Constants.ORDER_CONFIRMED.equals(status)){
				return handleOrderConfirm(orderStatusV5);
			} else if(OrderStatusV5Constants.ORDER_DELIVERIED.equals(status)){
				return handleOrderDelivery(orderStatusV5);
			} else if(OrderStatusV5Constants.ORDER_FINISHED.equals(status)){
				return handleOrderFinished(orderStatusV5);
			} else if(OrderStatusV5Constants.FEEDBACK_CONFIRM.equals(status)){
				return handleFeedbackConfirm(orderStatusV5);
			} else if(OrderStatusV5Constants.FEEDBACK_CANCEL_AGREE.equals(status)){
				return handleFeedbackCancelAgree(orderStatusV5);
			} else if(OrderStatusV5Constants.FEEDBACK_CANCEL_REFUSED.equals(status)){
				return handleFeedbackCancelRefused(orderStatusV5);
			}
		}
		return handleSucc;
	}
	
	/**
	 * oms订单新建
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleOrderNew(OrderStatusV5 orderStatusV5){
		if(null != orderStatusV5){
			String orderCode = orderStatusV5.getBsOrderCode();
			Map<String,Object> handMap =
			    judgeBeforeOrderStatus(orderStatusV5.getOpType(),orderCode);
			boolean flag = (Boolean)handMap.get(SUCCESS_KEY);
			if(!flag){
				return false;
			}
			if((Boolean)handMap.get(NEED_HANDLE_KEY)){
				int result = sdkOrderService.updateOrderLogisticsStatus(orderCode, SalesOrder.SALES_ORDER_STATUS_TOOMS);
				return SUCCESS.equals(result);
			}
		}
		return true;
	}
	
	/**
	 * 处理订单取消
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleCancelOrder(OrderStatusV5 orderStatusV5){
		if(null != orderStatusV5){
			String orderCode = orderStatusV5.getBsOrderCode();
			Map<String,Object> handMap =
			    judgeBeforeOrderStatus(orderStatusV5.getOpType(),orderCode);
			boolean flag = (Boolean)handMap.get(SUCCESS_KEY);
			if(!flag){
				return false;
			}
			//int result = orderManager.updateOrderLogisticsStatus(orderCode, SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED);
			//其余逻辑在商城wormhole处理
		}
		return true;
	}
	
	/**
	 * 过单到仓库
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleOrderConfirm(OrderStatusV5 orderStatusV5){
		if(null != orderStatusV5){
			String orderCode = orderStatusV5.getBsOrderCode();
			Map<String,Object> handMap =
			    judgeBeforeOrderStatus(orderStatusV5.getOpType(),orderCode);
			boolean flag = (Boolean)handMap.get(SUCCESS_KEY);
			if(!flag){
				return false;
			}
			if((Boolean)handMap.get(NEED_HANDLE_KEY)){
				int result = sdkOrderService.updateOrderLogisticsStatus(orderCode, SalesOrder.SALES_ORDER_STATUS_CONFIRMED);
				return SUCCESS.equals(result);
			}
		}
		return true;
	}
	
	/**
	 * 销售出库
	 * 1. 状态更新为在途
	 * 2. 更新物流信息
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleOrderDelivery(OrderStatusV5 orderStatusV5){
		if(null != orderStatusV5){
			String orderCode = orderStatusV5.getBsOrderCode();
			Map<String,Object> handMap =
			    judgeBeforeOrderStatus(orderStatusV5.getOpType(),orderCode);
			boolean flag = (Boolean)handMap.get(SUCCESS_KEY);
			if(!flag){
				return false;
			}
			if((Boolean)handMap.get(NEED_HANDLE_KEY)){
				int result = sdkOrderService.updateOrderLogisticsStatus(orderCode, SalesOrder.SALES_ORDER_STATUS_DELIVERIED);
				if(SUCCESS.equals(result)){
					BigDecimal actualFreight = null;
					String logisticsProviderCode = orderStatusV5.getLogisticsProviderCode();
					String transCode = orderStatusV5.getTransCode();
					String logisticsProviderName = orderStatusV5.getLogisticsProviderName();
					sdkOrderService.updateLogisticsInfo(orderCode, actualFreight, logisticsProviderCode, logisticsProviderName, transCode, new Date());
					return true;
				}
			}
		}
		return true;
	}
	
	/**
	 * 订单完成
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleOrderFinished(OrderStatusV5 orderStatusV5){
		if(null != orderStatusV5){
			String orderCode = orderStatusV5.getBsOrderCode();
			Map<String,Object> handMap =
			    judgeBeforeOrderStatus(orderStatusV5.getOpType(),orderCode);
			boolean flag = (Boolean)handMap.get(SUCCESS_KEY);
			if(!flag){
				return false;
			}
			if((Boolean)handMap.get(NEED_HANDLE_KEY)){
				int result = sdkOrderService.updateOrderLogisticsStatus(orderCode, SalesOrder.SALES_ORDER_STATUS_FINISHED);
				return SUCCESS.equals(result);
			}
		}
		return true;
	}
	
	/**
	 * 退货已入库
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleFeedbackConfirm(OrderStatusV5 orderStatusV5){
		//TODO
		return true;
	}
	
	/**
	 * 退换货申请取消成功
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleFeedbackCancelAgree(OrderStatusV5 orderStatusV5){
		//TODO
		return true;
	}
	
	/**
	 * 退换货申请取消失败
	 * 
	 * @param orderStatusV5
	 * @return
	 */
	private Boolean handleFeedbackCancelRefused(OrderStatusV5 orderStatusV5){
		//TODO
		return true;
	}
	
	/**
	 * 判断订单状态变更之前的订单状态
	 * 
	 * @param orderStatusSyncType
	 *        订单状态同步类型
	 * @return
	 */
	private Map<String,Object> judgeBeforeOrderStatus(Integer orderStatusSyncType,String orderCode){
		SalesOrderCommand order = sdkOrderService.findOrderByCode(orderCode, null);
		Map<String,Object> retMap = new HashMap<String,Object>();
		Boolean handleSuccess = true;
		boolean needUpdateStatus = false;
		if(null == order){
			handleSuccess = false;
		} else {
			//是否需要更新订单状态
			needUpdateStatus = 
			    OrderStatusV5Constants.ORDER_STATUS_BEFORE_MAP.containsKey(orderStatusSyncType)
			    && OrderStatusV5Constants.ORDER_STATUS_BEFORE_MAP.get(orderStatusSyncType).contains(order.getLogisticsStatus());
		}
		retMap.put(SUCCESS_KEY, handleSuccess);
		retMap.put(NEED_HANDLE_KEY, needUpdateStatus);
		return retMap;
	}

}
