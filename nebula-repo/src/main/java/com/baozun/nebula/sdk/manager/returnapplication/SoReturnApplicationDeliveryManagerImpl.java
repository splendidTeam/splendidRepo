package com.baozun.nebula.sdk.manager.returnapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.dao.salesorder.SdkSoReturnApplicationDeliveryInfoDao;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;

@Service("soReturnApplicationDeliveryManager")
public class SoReturnApplicationDeliveryManagerImpl implements SoReturnApplicationDeliveryManager{
	
	@Autowired
	private SdkSoReturnApplicationDeliveryInfoDao sdkSoReturnApplicationDeliveryInfoDao;

	@Override
	public SoReturnApplicationDeliveryInfo findDeliveryInfoByReturnId(
			Long returnId) {
		SoReturnApplicationDeliveryInfo returnDeliveryInfo=sdkSoReturnApplicationDeliveryInfoDao.findReturnDeliveryByReturnId(returnId);
		return returnDeliveryInfo;
	}

}
