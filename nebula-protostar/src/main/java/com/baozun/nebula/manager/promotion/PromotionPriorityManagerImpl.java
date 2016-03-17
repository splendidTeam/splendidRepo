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

package com.baozun.nebula.manager.promotion;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PriorityCommand;
import com.baozun.nebula.dao.promotion.PromotionPriorityChanelAdjustDao;
import com.baozun.nebula.solr.utils.JsonFormatUtil;

/**
 * @author - 项硕
 */
@Transactional
@Service
public class PromotionPriorityManagerImpl implements PromotionPriorityManager {
	
	private static final Logger log = LoggerFactory.getLogger(PromotionPriorityManagerImpl.class);
	
	@Autowired
	private PromotionPriorityChanelAdjustDao priorityChanelAdjustDao;

	@Override
	public List<PriorityCommand> findPromotionPriorityChanelAdjustListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> queryMap) {
		List<PriorityCommand> list = priorityChanelAdjustDao
				.findPromotionPriorityChanelAdjustListByQueryMapWithPage(page, sorts, queryMap);
		log.debug(JsonFormatUtil.format(list));
		return list;
	}

}
