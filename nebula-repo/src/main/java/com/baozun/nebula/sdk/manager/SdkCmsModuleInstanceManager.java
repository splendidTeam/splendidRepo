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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsTemplateHtml;

/**
 * CmsModuleInstanceManager
 * @author  何波
 *
 */
public interface SdkCmsModuleInstanceManager extends BaseManager{

	/**
	 * 保存CmsModuleInstance
	 * 
	 */
	CmsModuleInstance saveCmsModuleInstance(CmsModuleInstance model);
	
	/**
	 * 通过id获取CmsModuleInstance
	 * 
	 */
	CmsModuleInstance findCmsModuleInstanceById(Long id);

	/**
	 * 获取所有CmsModuleInstance列表
	 * @return
	 */
	List<CmsModuleInstance> findAllCmsModuleInstanceList();
	
	/**
	 * 通过ids获取CmsModuleInstance列表
	 * @param ids
	 * @return
	 */
	List<CmsModuleInstance> findCmsModuleInstanceListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取CmsModuleInstance列表
	 * @param paraMap
	 * @return
	 */
	List<CmsModuleInstance> findCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsModuleInstance列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CmsModuleInstance> findCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsModuleInstance
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableCmsModuleInstanceByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除CmsModuleInstance
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeCmsModuleInstanceByIds(List<Long> ids);
	
	/**
	 * 获取有效的CmsModuleInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<CmsModuleInstance> findAllEffectCmsModuleInstanceList();
	
	/**
	 * 通过参数map获取有效的CmsModuleInstance列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsModuleInstance列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 
	* @author 何波
	* @Description: 查询出编辑数据 
	* @param templateId
	* @param moduleId
	* @param isEdit
	* @return   
	* String   
	* @throws
	 */
	//String findUpdatedCmsModuleInstance(Long templateId, Long moduleId, Boolean isEdit) ;
	
	/**
	 * 保存模块实例
	 * 
	 * @param cmsPageInstance
	 * @param html
	 * @return
	 */
	CmsModuleInstance createOrUpdateModuleInstance(CmsModuleInstance cmsModuleInstance , String html);
	/**
	 * 
	* @author 何波
	* @Description: 发布模块实例
	* @param moduleId   
	* void   
	* @throws
	 */
	void publishModuleInstance(Long moduleId);
	
	/**]
	 * 
	* @author 何波
	* @Description: 取消发布模块实例
	* @param moduleId   
	* void   
	* @throws
	 */
	void cancelPublishedModuleInstance(Long moduleId);
	/**'
	 * 
	* @author 何波
	* @Description:重置最新模块编辑内容 
	* @return   
	* String   
	* @throws
	 */
	String recoverTemplateCodeArea(Long templateId, Long versionId, String code);
	/**
	 * 
	* @author 何波
	* @Description: 加载发布模块实例数据    
	* void   
	* @throws
	 */
	void loadModuleMap();
	/**
	 * 
	* @author 何波
	* @Description: 获取模块发布数据
	* @return   
	* Map<String,String>   
	* @throws
	 */
	Map<String, CmsTemplateHtml>  getModuleMap();

	//public String getPublishData(List<CmsPublished> editAreaList, Long templateId);

//	public String getInstancePublishData(List<CmsEditArea> editAreaList,
//			Long templateId);

//	/**
//	 * 获取页面内容
//	 * @param editAreaList
//	 * @param templateId
//	 * @return
//	 */
//	public <T> String getHtmlDataByArea(List<T> editAreaList, Long templateId);

	/**
	 *根据templateId来获取module实例
	 * @param ids
	 * @return
	 */
	public List<CmsModuleInstance> findCmsModuleInstanceListByTemplateIds(List<Long> ids);

}
