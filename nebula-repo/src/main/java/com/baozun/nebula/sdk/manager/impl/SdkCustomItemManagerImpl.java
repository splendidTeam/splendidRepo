package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.manager.SdkCustomizeFilterExecuteManager;

@Transactional
@Service("sdkCustomItemManager")
public class SdkCustomItemManagerImpl implements SdkCustomizeFilterExecuteManager {

	@Override
	public List<Long> execute() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(13741L);
		ids.add(13734L);
		ids.add(13686L);

		return ids;
	}

}
