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

import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;
import com.baozun.nebula.dao.product.SkuInventoryChangeLogDao;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryChangeLogManager;

/**
 * SkuInventoryChangeLogManager
 * @author  dongliang.ma
 *
 */
@Transactional
@Service("sdkSkuInventoryChangeLogManager") 
public class SdkSkuInventoryChangeLogManagerImpl implements SdkSkuInventoryChangeLogManager {

	@Autowired
	private SkuInventoryChangeLogDao skuInventoryChangeLogDao;


	/**
	 * 保存SkuInventoryChangeLog
	 * 
	 */
	@Override
	public SkuInventoryChangeLog saveSkuInventoryChangeLog(SkuInventoryChangeLog model){
	
		return skuInventoryChangeLogDao.save(model);
	}
	
	/**
	 * 通过id获取SkuInventoryChangeLog
	 * 
	 */
	@Override
	public SkuInventoryChangeLog findSkuInventoryChangeLogById(Long id){
	
		return skuInventoryChangeLogDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有SkuInventoryChangeLog列表
	 * @return
	 */
	@Override
	public List<SkuInventoryChangeLog> findAllSkuInventoryChangeLogList(){
	
		return skuInventoryChangeLogDao.findAllSkuInventoryChangeLogList();
	};
	
	/**
	 * 通过ids获取SkuInventoryChangeLog列表
	 * @param ids
	 * @return
	 */
	@Override
	public List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByIds(List<Long> ids){
	
		return skuInventoryChangeLogDao.findSkuInventoryChangeLogListByIds(ids);
	};
	
	/**
	 * 通过参数map获取SkuInventoryChangeLog列表
	 * @param paraMap
	 * @return
	 */
	@Override
	public List<SkuInventoryChangeLog> findSkuInventoryChangeLogListByQueryMap(Map<String, Object> paraMap){
	
		return skuInventoryChangeLogDao.findSkuInventoryChangeLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取SkuInventoryChangeLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		return skuInventoryChangeLogDao.findSkuInventoryChangeLogListByQueryMapWithPage(page,sorts,paraMap);
	}

	/* 
	 * @see com.baozun.nebula.sdk.manager.SdkSkuInventoryChangeLogManager#clearSkuInventoryChangeLogOneMonthAgo()
	 */
	@Override
	public void clearSkuInventoryChangeLogOneMonthAgo() {
		skuInventoryChangeLogDao.clearSkuInventoryChangeLogOneMonthAgo();
	};
	
	
	
}
