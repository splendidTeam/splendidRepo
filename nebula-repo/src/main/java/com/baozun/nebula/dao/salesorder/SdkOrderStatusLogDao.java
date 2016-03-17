package com.baozun.nebula.dao.salesorder;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;

public interface SdkOrderStatusLogDao extends GenericEntityDao<OrderStatusLog, Long>{
	@NativeQuery(model = OrderStatusLog.class)
	public List<OrderStatusLog> findOrderStatusLogByOrderId(@QueryParam("orderId") Long orderId);
	
	@NativeQuery(model = OrderStatusLogCommand.class)
	public OrderStatusLogCommand findOrderStatusLogById(@QueryParam("id") Long id);
}
