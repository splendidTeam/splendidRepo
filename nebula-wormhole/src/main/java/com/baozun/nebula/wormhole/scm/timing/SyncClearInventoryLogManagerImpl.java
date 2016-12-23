/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.wormhole.scm.timing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.sdk.manager.SdkSkuInventoryChangeLogManager;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月6日 下午3:39:46 
 * @version   
 */
@Service("syncClearInventoryLogManager")
public class SyncClearInventoryLogManagerImpl implements
		SyncClearInventoryLogManager {
	private static Logger log = LoggerFactory.getLogger(SyncClearInventoryLogManagerImpl.class);

	@Autowired
	private SdkSkuInventoryChangeLogManager		changeLogManager;
	
	/* 
	 * @see com.baozun.nebula.wormhole.scm.timing.SyncClearSkuInventoryChangeLogManager#clearAllLog()
	 */
	@Override
	public void clearLogBeforeOneMonth() {
		log.debug("start to clear SkuInventoryChangeLog 1 month ago...");
		changeLogManager.clearSkuInventoryChangeLogOneMonthAgo();
	}

}
