package com.baozun.nebula.dao.salesorder;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.OrderLog;

public interface SdkOrderLogDao extends GenericEntityDao<OrderLog, Long>{
	@NativeQuery(model = OrderLog.class)
	public List<OrderLog> findOrderLogByOrderId(@QueryParam("orderId") Long orderId);
}
