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
package com.baozun.nebula.dao.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.system.MataInfo;

/**
 * MataInfoDao
 * @author  何波
 *
 */
public interface MataInfoDao extends GenericEntityDao<MataInfo,Long>{

	/**
	 * 获取所有MataInfo列表
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	List<MataInfo> findAllMataInfoList();
	
	/**
	 * 通过ids获取MataInfo列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	List<MataInfo> findMataInfoListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MataInfo列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	List<MataInfo> findMataInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取MataInfo列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	Pagination<MataInfo> findMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用MataInfo
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableMataInfoByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除MataInfo
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeMataInfoByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的MataInfo列表     
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	List<MataInfo> findAllEffectMataInfoList();
	
	/**
	 * 通过参数map获取有效的MataInfo列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	List<MataInfo> findEffectMataInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的MataInfo列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MataInfo.class)
	Pagination<MataInfo> findEffectMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery(model = MataInfo.class)
	MataInfo findMataInfoByCode(@QueryParam("code")String code);
	
}
