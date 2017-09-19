package com.baozun.nebula.dao.salesorder;

import java.util.List;

import com.baozun.nebula.model.salesorder.OrderLog;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface SdkOrderLogDao extends GenericEntityDao<OrderLog, Long>{

    @NativeQuery(model = OrderLog.class)
    List<OrderLog> findOrderLogByOrderId(@QueryParam("orderId") Long orderId);
}
