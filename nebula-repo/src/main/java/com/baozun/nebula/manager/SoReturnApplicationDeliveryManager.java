package com.baozun.nebula.manager;

import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;

public interface SoReturnApplicationDeliveryManager {
	
	/**
	 *通过returnId查询换货物流 
	 * @param returnId
	 * @return
	 */
	public SoReturnApplicationDeliveryInfo findDeliveryInfoByReturnId(Long returnId);

}
