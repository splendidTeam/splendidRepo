package com.baozun.nebula.sdk.manager.returnapplication;

import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;



public interface SdkReturnApplicationDeliveryManager {
	
	/**
	 *通过returnId查询换货物流 
	 * @param returnId
	 * @return
	 */
	public ReturnApplicationDeliveryInfo findDeliveryInfoByReturnId(Long returnId);

}
