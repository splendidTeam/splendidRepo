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
import com.baozun.nebula.model.payment.PayWarnningLog;
import com.baozun.nebula.sdk.manager.SdkPayWarnningLogManager;
import com.baozun.nebula.dao.payment.PayWarnningLogDao;

/**
 * PayWarnningLogManager
 * @author  lxy
 *
 */
@Transactional
@Service("sdkPayWarnningLogManager") 
public class SdkPayWarnningLogManagerImpl implements SdkPayWarnningLogManager {

	@Autowired
	private PayWarnningLogDao payWarnningLogDao;


	/**
	 * 保存PayWarnningLog
	 * 
	 */
	public PayWarnningLog savePayWarnningLog(PayWarnningLog model){
	
		return payWarnningLogDao.save(model);
	}
	
	/**
	 * 通过id获取PayWarnningLog
	 * 
	 */
	@Transactional(readOnly=true)
	public PayWarnningLog findPayWarnningLogById(Long id){
	
		return payWarnningLogDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有PayWarnningLog列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PayWarnningLog> findAllPayWarnningLogList(){
	
		return payWarnningLogDao.findAllPayWarnningLogList();
	};
	
	/**
	 * 通过ids获取PayWarnningLog列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PayWarnningLog> findPayWarnningLogListByIds(List<Long> ids){
	
		return payWarnningLogDao.findPayWarnningLogListByIds(ids);
	};
	
	/**
	 * 通过参数map获取PayWarnningLog列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PayWarnningLog> findPayWarnningLogListByQueryMap(Map<String, Object> paraMap){
	
		return payWarnningLogDao.findPayWarnningLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取PayWarnningLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<PayWarnningLog> findPayWarnningLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return payWarnningLogDao.findPayWarnningLogListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用PayWarnningLog
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisablePayWarnningLogByIds(List<Long> ids,Integer state){
		payWarnningLogDao.enableOrDisablePayWarnningLogByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除PayWarnningLog
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removePayWarnningLogByIds(List<Long> ids){
		payWarnningLogDao.removePayWarnningLogByIds(ids);
	}
	
	
	/**
	 * 获取有效的PayWarnningLog列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PayWarnningLog> findAllEffectPayWarnningLogList(){
	
		return payWarnningLogDao.findAllEffectPayWarnningLogList();
	};
	
	/**
	 * 通过参数map获取有效的PayWarnningLog列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PayWarnningLog> findEffectPayWarnningLogListByQueryMap(Map<String, Object> paraMap){
	
		return payWarnningLogDao.findEffectPayWarnningLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的PayWarnningLog列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<PayWarnningLog> findEffectPayWarnningLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return payWarnningLogDao.findEffectPayWarnningLogListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
}
