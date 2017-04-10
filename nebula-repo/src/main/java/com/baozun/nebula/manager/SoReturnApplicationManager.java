package com.baozun.nebula.manager;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

public interface SoReturnApplicationManager {

	/** 查询 当前订单行 已经退过货的商品个数（退换货状态为已完成) <br/>
	 *  primaryLineId:orderlineid 或者packageOrderLineId*/
	public Integer countCompletedAppsByPrimaryLineId(Long primaryLineId);
	
	/** 根据orderLineId  查询退换货单(时间最近的一个) */
	public SoReturnApplication findLastApplicationByOrderLineId(Long orderLineId);
	
	
	/** 根据订单id判断是不是 已经超过14天收货的订单*/
	public Boolean isFinishedAndOutDayOrderById(Long orderId);
	
	/** 新增退换货申请单
	 * @return */
	public SoReturnApplication createReturnApplication(ReturnApplicationCommand appCommand,SalesOrderCommand orderCommand);
	
	/**
	 * 根据订单id查询最近的退货单
	 * @param orderId
	 * @return
	 */
	public SoReturnApplication findLastApplicationByOrderId(Long orderId);
	
	
}
