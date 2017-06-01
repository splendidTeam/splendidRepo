package com.baozun.nebula.dao.salesorder;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;



/**
 * 订单退换货处理接口
 * 退换货申请
 */
public interface SdkSoReturnApplicationDeliveryInfoDao extends GenericEntityDao<SoReturnApplicationDeliveryInfo, Long> {
	
	@NativeQuery(model = SoReturnApplicationDeliveryInfo.class)
	public SoReturnApplicationDeliveryInfo findReturnDeliveryByReturnId(@QueryParam("returnId") Long returnId);

}
