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
package com.baozun.nebula.manager.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * @author Justin
 *
 */
@Transactional
@Service("chooseOptionManager")
public class ChooseOptionManagerImpl implements ChooseOptionManager {

	@Autowired
	private ChooseOptionDao chooseOptionDao;

	public List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode,String language){
		
		Map<String, Object> paraMap=new HashMap<String, Object>();
				
		paraMap.put("groupCode", groupCode);
		paraMap.put("labelLang",language);
		Sort[] sorts=new Sort[1];
		sorts[0]=new Sort("sort_no","asc");
		Pagination<ChooseOption> page=chooseOptionDao.findEffectChooseOptionListByQueryMapWithPage(0, Integer.MAX_VALUE, sorts, paraMap);
		
		return page.getItems();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.ChooseOptionManager#findEffectChooseOptionListByGroupCode(java.lang.String)
	 */
	@Override
	public List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode) {
		return this.findEffectChooseOptionListByGroupCode(groupCode, LocaleContextHolder.getLocale().toString());
	}
	
}
