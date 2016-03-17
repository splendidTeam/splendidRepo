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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.product.SkuInventoryLog;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryLogManager;
import com.baozun.nebula.dao.product.SkuInventoryLogDao;

/**
 * SkuInventoryLogManager
 * @author  何波
 *
 */
@Transactional
@Service("sdkSkuInventoryLogManager") 
public class SdkSkuInventoryLogManagerImpl implements SdkSkuInventoryLogManager {

	@Autowired
	private SkuInventoryLogDao skuInventoryLogDao;


	/**
	 * 保存SkuInventoryLog
	 * 
	 */
	public SkuInventoryLog saveSkuInventoryLog(SkuInventoryLog model){
	
		return skuInventoryLogDao.save(model);
	}
	
	/**
	 * 通过id获取SkuInventoryLog
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public SkuInventoryLog findSkuInventoryLogById(Long id){
	
		return skuInventoryLogDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有SkuInventoryLog列表
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SkuInventoryLog> findAllSkuInventoryLogList(){
	
		return skuInventoryLogDao.findAllSkuInventoryLogList();
	};
	
	/**
	 * 通过ids获取SkuInventoryLog列表
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SkuInventoryLog> findSkuInventoryLogListByIds(List<Long> ids){
	
		return skuInventoryLogDao.findSkuInventoryLogListByIds(ids);
	};
	
	/**
	 * 通过参数map获取SkuInventoryLog列表
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SkuInventoryLog> findSkuInventoryLogListByQueryMap(Map<String, Object> paraMap){
	
		return skuInventoryLogDao.findSkuInventoryLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取SkuInventoryLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<SkuInventoryLog> findSkuInventoryLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return skuInventoryLogDao.findSkuInventoryLogListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用SkuInventoryLog
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableSkuInventoryLogByIds(List<Long> ids,Integer state){
		skuInventoryLogDao.enableOrDisableSkuInventoryLogByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除SkuInventoryLog
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeSkuInventoryLogByIds(List<Long> ids){
		skuInventoryLogDao.removeSkuInventoryLogByIds(ids);
	}
	
	
	/**
	 * 获取有效的SkuInventoryLog列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SkuInventoryLog> findAllEffectSkuInventoryLogList(){
	
		return skuInventoryLogDao.findAllEffectSkuInventoryLogList();
	};
	
	/**
	 * 通过参数map获取有效的SkuInventoryLog列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SkuInventoryLog> findEffectSkuInventoryLogListByQueryMap(Map<String, Object> paraMap){
	
		return skuInventoryLogDao.findEffectSkuInventoryLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的SkuInventoryLog列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<SkuInventoryLog> findEffectSkuInventoryLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return skuInventoryLogDao.findEffectSkuInventoryLogListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
}
