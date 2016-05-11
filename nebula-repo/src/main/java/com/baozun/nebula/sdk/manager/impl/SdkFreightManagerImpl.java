/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.impl;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.freight.SupportedAreaDao;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.sdk.manager.SdkFreightManager;

/**
 * @author sunbaobao
 *
 */
@Transactional
@Service("sdkFreightManager") 
public class SdkFreightManagerImpl implements SdkFreightManager{

	
	@Autowired
	private DistributionModeDao distributionModeDao;
	
	@Autowired
	private SupportedAreaDao  supportedAreaDao;
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkFreightManager#getAllDistributionMode()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<DistributionMode> getAllDistributionMode() {
		return distributionModeDao.getAllDistributionMode();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkFreightManager#findSupportedAreaByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<SupportedAreaCommand> findSupportedAreaByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> searchParam) {
		return supportedAreaDao.findSupportedAreaByQueryMapWithPage(page, sorts, searchParam);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkFreightManager#findDistributionModeById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public DistributionMode findDistributionModeById(Long id) {
		return distributionModeDao.findDistributionModeById(id);
	}

}
