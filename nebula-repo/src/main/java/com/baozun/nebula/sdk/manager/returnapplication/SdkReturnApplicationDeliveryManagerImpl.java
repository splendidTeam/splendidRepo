package com.baozun.nebula.sdk.manager.returnapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.dao.returnapplication.SdkSoReturnApplicationDeliveryInfoDao;
import com.baozun.nebula.model.returnapplication.ReturnApplicationDeliveryInfo;

@Service("soReturnApplicationDeliveryManager")
public class SdkReturnApplicationDeliveryManagerImpl implements SdkReturnApplicationDeliveryManager{
	
	@Autowired
	private SdkSoReturnApplicationDeliveryInfoDao sdkSoReturnApplicationDeliveryInfoDao;

	@Override
	public ReturnApplicationDeliveryInfo findDeliveryInfoByReturnId(Long returnId) {
		ReturnApplicationDeliveryInfo returnDeliveryInfo = sdkSoReturnApplicationDeliveryInfoDao.findReturnDeliveryByReturnId(returnId);
		return returnDeliveryInfo;
	}

}
