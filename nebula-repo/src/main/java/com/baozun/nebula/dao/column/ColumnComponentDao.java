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

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.model.column.ColumnComponent;

/**
 * ColumnComponentDao
 * @author  Justin
 *
 */
public interface ColumnComponentDao extends GenericEntityDao<ColumnComponent,Long>{

	/**
	 * 获取所有ColumnComponent列表
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findAllColumnComponentList();
	
	/**
	 * 通过ids获取ColumnComponent列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findColumnComponentListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ColumnComponent列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findColumnComponentListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ColumnComponent列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	Pagination<ColumnComponent> findColumnComponentListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用ColumnComponent
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableColumnComponentByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 获取有效的ColumnComponent列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findAllEffectColumnComponentList();
	
	/**
	 * 通过参数map获取有效的ColumnComponent列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findEffectColumnComponentListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ColumnComponent列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	Pagination<ColumnComponent> findEffectColumnComponentListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过ids批量删除ColumnComponent
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeColumnComponentByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 根据版块-模块Id查询所有的版块-组件
	 * @param moduleId
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent> findColumnCompListByModuleId(@QueryParam("moduleId") Long moduleId);
	
	/**
	 * 根据页面code查询页面组件
	 * @param pageCode
	 * @return
	 */
	@NativeQuery(model = ColumnComponentCommand.class)
	List<ColumnComponentCommand> findColumnComponentByPageCode(@QueryParam("pageCode") String pageCode);
	
	/**
	 * 根据模块Id列表查询对应的组件
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnComponent.class)
	List<ColumnComponent>  findColumnComponentListByModuleIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 根据模块Id列表查询对应的组件
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ColumnComponentCommand.class)
	List<ColumnComponentCommand>  findColumnComponentCommandListByModuleIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 修改版块-组件信息
	 * @param id
	 * @param description
	 * @param ext
	 * @param img
	 * @param sortNo
	 * @param targetId
	 * @param title
	 * @param url
	 */
	@NativeUpdate
	void updateComlumCompById(@QueryParam("id") Long id,
			@QueryParam("description") String description,
			@QueryParam("ext") String ext,
			@QueryParam("img") String img,
			@QueryParam("sortNo") Integer sortNo,
			@QueryParam("targetId") Long targetId,
			@QueryParam("title") String title,
			@QueryParam("url") String url,
			@QueryParam("imgHeight") Integer imgHeight,
			@QueryParam("imgWidth") Integer imgWidth
			); 
}
