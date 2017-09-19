package com.baozun.nebula.dao.returnapplication;


import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;



/**
 * 订单退换货处理接口
 * 退换货申请
 */
public interface SdkSoReturnApplicationDeliveryInfoDao extends GenericEntityDao<ReturnApplicationDeliveryInfo, Long> {
	
	@NativeQuery(model = ReturnApplicationDeliveryInfo.class,value="SdkSoReturnApplicationDeliveryInfoDao.findReturnDeliveryByReturnId")
	public ReturnApplicationDeliveryInfo findReturnDeliveryByReturnId(@QueryParam("returnId") Long returnId);

}
