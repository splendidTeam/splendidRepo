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
package com.baozun.nebula.dao.cms;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.cms.CmsEditArea;

/**
 * CmsEditAreaDao
 * @author  Justin
 *
 */
public interface CmsEditAreaDao extends GenericEntityDao<CmsEditArea,Long>{

	/**
	 * 获取所有CmsEditArea列表
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	List<CmsEditArea> findAllCmsEditAreaList();
	
	/**
	 * 通过ids获取CmsEditArea列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	List<CmsEditArea> findCmsEditAreaListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取CmsEditArea列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	List<CmsEditArea> findCmsEditAreaListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsEditArea列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	Pagination<CmsEditArea> findCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsEditArea
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableCmsEditAreaByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeCmsEditAreaByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的CmsEditArea列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	List<CmsEditArea> findAllEffectCmsEditAreaList();
	
	/**
	 * 通过参数map获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	List<CmsEditArea> findEffectCmsEditAreaListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsEditArea.class)
	Pagination<CmsEditArea> findEffectCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过pageId批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param pageId
	 */
	@NativeUpdate
	void removeCmsEditAreaByPageId(@QueryParam("pageId") Long pageId);
	

	/**
	 * 通过templateId批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param templateId
	 */
	@NativeUpdate
	void removeCmsEditAreaByTemplateId(@QueryParam("templateId") Long templateId,@QueryParam("code") String code);
	/**
	 * 
	* @author 何波
	* @Description: 删除模块实例对应的编辑数据
	* @param templateId   
	* void   
	* @throws
	 */
	@NativeUpdate
	void removeCmsModuleEditAreaByTemplateId(@QueryParam("templateId") Long templateId,@QueryParam("code") String code);
	/**
	 * 
	* @author 何波
	* @Description: 修改显示或隐藏
	* @param paraMap   
	* void   
	* @throws
	 */
	@NativeUpdate
	void editAreaHide(@QueryParam Map<String, Object> paraMap);
	

	
	@NativeQuery(model = CmsEditArea.class)
	CmsEditArea queryEditAreaHide(@QueryParam Map<String, Object> paraMap);

}
