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

package com.baozun.nebula.manager.sns;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.model.sns.Consultants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;

/**
 * @author - 项硕
 */
@Service
public class ConsultantsManagerImpl implements ConsultantsManager {
	
	@Autowired
	private SdkMemberManager sdkMemberManager;

	@Autowired
	private SdkItemManager sdkItemManager;
	
	@Override
	public Pagination<ConsultantCommand> findConsultantsList(Long memberId, Date beginTime, Date endTime, Page page) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberId", memberId);
		paramMap.put("createDateStart", beginTime);
		paramMap.put("createDateEnd", endTime);
		return sdkMemberManager.findConsultantsList(page, Sort.parse("a.createTime desc"), paramMap);
	}

	@Override
	public Consultants createConsultants(Consultants consultants) {
		consultants.setCreateTime(new Date());
		consultants.setLifeCycle(Consultants.STATUS_UNRESPONSED);
		consultants.setPublishMark(Consultants.PUBLISH_MARK_NO);
		return sdkMemberManager.createConsultants(consultants);
	}

	@Override
	public Pagination<ConsultantCommand> findConsultantsListByItemId(Page page,
			Long itemId, Sort[] sorts) {
		return sdkItemManager.findConsultantsListByItemId(page, itemId, sorts);
	}

}
