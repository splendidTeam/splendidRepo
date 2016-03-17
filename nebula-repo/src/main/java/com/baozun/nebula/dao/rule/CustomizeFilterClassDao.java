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
package com.baozun.nebula.dao.rule;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.rule.CustomizeFilterClassCommand;
import com.baozun.nebula.model.rule.CustomizeFilterClass;

/**
 * CustomizeFilterClassDao
 * @author  lxy
 *
 */
public interface CustomizeFilterClassDao extends GenericEntityDao<CustomizeFilterClass,Long>{

	/**
	 * 获取所有CustomizeFilterClass列表
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findAllCustomizeFilterClassList();
	
	/**
	 * 通过ids获取CustomizeFilterClass列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findCustomizeFilterClassListByIds(@QueryParam("ids")List<Long> ids);
	
	@NativeQuery(model = CustomizeFilterClass.class)
	CustomizeFilterClass findCustomizeFilterClassListByServiceName(@QueryParam("svcName")String svcName);
	
	/**
	 * 通过参数map获取CustomizeFilterClass列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findCustomizeFilterClassListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取CustomizeFilterClass列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClassCommand.class)
	Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CustomizeFilterClass
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableCustomizeFilterClassByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除CustomizeFilterClass
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeCustomizeFilterClassByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的CustomizeFilterClass列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findAllEffectCustomizeFilterClassList();
	
	/**
	 * 通过参数map获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	Pagination<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 根据ShopId分页获取CustomizeFilterClass列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClassCommand.class)
	Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapAndShopIdWithPage(Page page,Sort[] sorts,
			@QueryParam Map<String, Object> paraMap,@QueryParam("shopId") Long shopId);
	
	
	@NativeQuery(model = SimpleMemberCombo.class)
	List<SimpleMemberCombo> findCustomizerFilterListByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 通过scope type 获取有效的CustomizeFilterClass列表
	 * @param type
	 * @return
	 */
	@NativeQuery(model = CustomizeFilterClass.class)
	List<CustomizeFilterClass> findEffectCustomizeFilterClassListByTypeAndShopId(@QueryParam("type") Integer type, @QueryParam("shopId") Long shopId);
	
}
