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
package com.baozun.nebula.dao.column;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.model.column.ColumnModule;

/**
 * ColumnModuleDao
 * @author  Justin
 *
 */
public interface ColumnModuleDao extends GenericEntityDao<ColumnModule,Long>{

	/**
	 * 获取所有ColumnModule列表
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	List<ColumnModule> findAllColumnModuleList();
	
	/**
	 * 通过ids获取ColumnModule列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	List<ColumnModule> findColumnModuleListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ColumnModule列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	List<ColumnModule> findColumnModuleListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过参数map获取ColumnModule列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnModuleCommand.class)
	List<ColumnModuleCommand> findColumnModuleCommandListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ColumnModule列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	Pagination<ColumnModule> findColumnModuleListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ColumnModule
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableColumnModuleByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除ColumnModule
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeColumnModuleByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ColumnModule列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	List<ColumnModule> findAllEffectColumnModuleList();
	
	/**
	 * 通过参数map获取有效的ColumnModule列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	List<ColumnModule> findEffectColumnModuleListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ColumnModule列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	Pagination<ColumnModule> findEffectColumnModuleListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过code查询ColumnModule
	 * @param code
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	ColumnModule findColumnModuleByCode(@QueryParam("code") String code, @QueryParam("pageCode") String pageCode);
	

	/**
	 * 通过code查询ColumnModule
	 * @param code
	 * @return
	 */
	@NativeQuery(model = ColumnModuleCommand.class)
	List<ColumnModuleCommand> findColumnModuleCommandByPageCode(@QueryParam("pageCode") String pageCode);
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	@NativeQuery(model = ColumnModule.class)
	ColumnModule findColumnModuleByPageCodeAndModuleCode(@QueryParam("pageCode") String pageCode,@QueryParam("moduleCode") String moduleCode);
	
	/**
	 * 修改发布时间
	 * @param publishTime
	 * @param id
	 */
	@NativeUpdate
	void updateColModPubTimById(@QueryParam("publishTime") Date publishTime, @QueryParam("id") Long id);
}
