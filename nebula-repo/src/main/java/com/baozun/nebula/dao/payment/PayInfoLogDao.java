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
package com.baozun.nebula.dao.payment;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.salesorder.PayInfoLog;

/**
 * PayInfoLogDao
 * @author  Justin
 *
 */
public interface PayInfoLogDao extends GenericEntityDao<PayInfoLog,Long>{

	/**
	 * 获取所有PayInfoLog列表
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	List<PayInfoLog> findAllPayInfoLogList();
	
	/**
	 * 通过ids获取PayInfoLog列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	List<PayInfoLog> findPayInfoLogListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取PayInfoLog列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	List<PayInfoLog> findPayInfoLogListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取PayInfoLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	Pagination<PayInfoLog> findPayInfoLogListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用PayInfoLog
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisablePayInfoLogByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除PayInfoLog
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removePayInfoLogByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的PayInfoLog列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	List<PayInfoLog> findAllEffectPayInfoLogList();
	
	/**
	 * 通过参数map获取有效的PayInfoLog列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	List<PayInfoLog> findEffectPayInfoLogListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的PayInfoLog列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PayInfoLog.class)
	Pagination<PayInfoLog> findEffectPayInfoLogListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
}
