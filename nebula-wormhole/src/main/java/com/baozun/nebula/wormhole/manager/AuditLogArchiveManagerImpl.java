/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.wormhole.manager;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogHistoryManager;

/**
 * <pre>
 *  日志归档
 *  把一个月之前的审计日志归档，定时一个月一次
 * </pre>
 * @author xingyu
 *
 */
@Service("auditLogArchiveManager")
@Transactional
public class AuditLogArchiveManagerImpl implements AuditLogArchiveManager{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrRefreshManager.class);
	
	@Autowired
	private SdkSysAuditLogHistoryManager sdkSysAuditLogHistoryManager;
	
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	
	@Override
	public void archive() {
		try {
			sdkSysAuditLogHistoryManager.archive();
			if (LOGGER.isDebugEnabled()){
	            LOGGER.debug("[AUDIT_LOG_ARCHIVE_INFO] audit log archive start:{}",new Date());
	        }
		} catch (Exception e) {
			LOGGER.error("[AUDIT_LOG_ARCHIVE_ERROR] error:{}",e.getMessage());
		}
	}

}
