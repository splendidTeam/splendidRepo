package com.baozun.nebula.dao.salesorder;

import java.util.List;

import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.sdk.command.OrderStatusLogCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface SdkOrderStatusLogDao extends GenericEntityDao<OrderStatusLog, Long>{

    @NativeQuery(model = OrderStatusLog.class)
    List<OrderStatusLog> findOrderStatusLogByOrderId(@QueryParam("orderId") Long orderId);

    @NativeQuery(model = OrderStatusLogCommand.class)
    OrderStatusLogCommand findOrderStatusLogById(@QueryParam("id") Long id);
}
