package com.baozun.nebula.sdk.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.delivery.AreaDeliveryModeDao;
import com.baozun.nebula.model.delivery.AreaDeliveryMode;
import com.baozun.nebula.sdk.manager.SdkAreaDeliveryModeManager;

@Transactional
@Service("sdkAreaDeliveryModeManager")
public class SdkAreaDeliveryModeManagerImpl implements SdkAreaDeliveryModeManager {

	@Autowired
	private AreaDeliveryModeDao areaDeliveryModeDao;
	
	@Override
	public AreaDeliveryMode findAreaDeliveryModeByAreaId(Long areaId) {
		return areaDeliveryModeDao.findAreaDeliveryModeByAreaId(areaId);
	}

	@Override
	public AreaDeliveryMode saveDeliveryMode(AreaDeliveryMode adm) {
		return areaDeliveryModeDao.save(adm);
	}

	@Override
	public void deleteDeliveryModeById(Long id) {
		areaDeliveryModeDao.deleteByPrimaryKey(id);
	}

}
