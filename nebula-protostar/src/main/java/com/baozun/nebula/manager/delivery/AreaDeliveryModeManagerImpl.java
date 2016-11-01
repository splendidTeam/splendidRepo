package com.baozun.nebula.manager.delivery;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.delivery.AreaDeliveryModeDao;
import com.baozun.nebula.model.delivery.AreaDeliveryMode;

@Transactional
@Service("areaDeliveryModeManager")
public class AreaDeliveryModeManagerImpl implements AreaDeliveryModeManager {

	@Autowired
	private AreaDeliveryModeDao areaDeliveryModeDao;
	
	@Override
	public AreaDeliveryMode findAreaDeliveryModeByAreaId(Long areaId) {
		return areaDeliveryModeDao.findAreaDeliveryModeByAreaId(areaId);
	}

	@Override
	public void updateDeliveryMode(Map<String,Object> map) {
			areaDeliveryModeDao.updateAreaDeliveryMode(map);
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
