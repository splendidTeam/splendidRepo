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

import java.util.Date;
import java.util.List;
import java.util.Map;





import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsPageInstance;

/**
 * CmsPageInstanceManager
 * @author  Justin
 *
 */
public interface SdkCmsPageInstanceManager extends BaseManager{

	/**
	 * 保存CmsPageInstance
	 * 
	 */
	CmsPageInstance saveCmsPageInstance(CmsPageInstance model);
	
	/**
	 * 通过id获取CmsPageInstance
	 * 
	 */
	CmsPageInstance findCmsPageInstanceById(Long id);

	/**
	 * 获取所有CmsPageInstance列表
	 * @return
	 */
	List<CmsPageInstance> findAllCmsPageInstanceList();
	
	/**
	 * 通过ids获取CmsPageInstance列表
	 * @param ids
	 * @return
	 */
	List<CmsPageInstance> findCmsPageInstanceListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取CmsPageInstance列表
	 * @param paraMap
	 * @return
	 */
	List<CmsPageInstance> findCmsPageInstanceListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsPageInstance列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CmsPageInstance> findCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsPageInstance
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableCmsPageInstanceByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除CmsPageInstance
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeCmsPageInstanceByIds(List<Long> ids);
	
	/**
	 * 获取有效的CmsPageInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<CmsPageInstance> findAllEffectCmsPageInstanceList();
	
	/**
	 * 通过参数map获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<CmsPageInstance> findEffectCmsPageInstanceListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<CmsPageInstance> findEffectCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 将所有的url地址载入到内存中
	 */
	void loadUrlMap();
	
	/**
	 * 返回内存中的urlmap
	 * @return
	 */
	Map<String,CmsPageInstance> findUrlMap();
	
	/**
	 * 检察页面实例编码是否存在
	 * @param paraMap
	 * @return
	 */
	public CmsPageInstance checkPageInstanceCode(Map<String, Object> paraMap);
	
	/**
	 * 检察页面实例url是否存在
	 * @param paraMap
	 * @return
	 */
	public CmsPageInstance checkPageInstanceUrl(Map<String, Object> paraMap);
	
	/**
	 * 通过templateIds获取CmsPageInstance列表
	 * @param templateIds
	 * @return
	 */
	public List<CmsPageInstance> findCmsPageInstanceListByTemplateIds(List<Long> templateIds);

	
	/**
	 * 
	* @author 何波
	* @Description: 设置seo信息
	* @param doc
	* @param pageInstance   
	* void   
	* @throws
	 */
	void  setSeoInfo(Document doc ,CmsPageInstance pageInstance );
	
	/**
	 * 检察页面实例url是否存在
	 * @param paraMap
	 * @return
	 */
	CmsPageInstance checkPublishPageInstanceUrl(Map<String, Object> paraMap);

	/**
	 * 
	* @author 何波
	* @Description: 设置商品信息
	* @param element
	* @return   
	* String   
	* @throws
	 */
	String  setProductInfo(Element element,boolean isEdit);

	/**
	 * 统计页面中所有版本发布的时间区间（最小值，最大值）
	 * @param pageId
	 * @return
	 */
	Map<String, Date> getPublishedPageInstanceVersionsTimeRang(Long pageId);

}
