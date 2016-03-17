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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsModuleInstanceDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.zk.ModuleMapWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;

/**
 * CmsModuleInstanceManager
 * @author  何波
 *
 */
@Transactional
@Service("sdkCmsModuleInstanceManager") 
public class SdkCmsModuleInstanceManagerImpl implements SdkCmsModuleInstanceManager {

	@Autowired
	private CmsModuleInstanceDao cmsModuleInstanceDao;

	@Autowired
	private SdkCmsModuleTemplateManager sdkCmsModuleTemplateManager;
	
	@Autowired
	private SdkCmsEditAreaManager sdkCmsEditAreaManager;
	
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	
	public final static String			CMS_HTML_EDIT_CLASS			= ".cms-html-edit";

	public final static String			CMS_DIV_EDIT_BUTTON_CLASS	= ".wui-tips";

	public final static String			NOEDIT_START				= "<!--noedit-start-->";

	public final static String			NOEDIT_END					= "<!--noedit-end-->";
	
	public final static String			ONLYEDIT_START				= "<!--onlyedit-start-->";
	public final static String			ONLYEDIT_END				= "<!--onlyedit-end-->";
	
	public final static Integer			PULISHED					= 1;
	public final static String			CMS_IMGARTICLE_EDIT_CLASS			= ".cms-imgarticle-edit";
	
	public final static String			CMS_PRODUCT_EDIT_CLASS			= ".cms-product-edit";
	
	@Autowired
	private  SdkCmsPublishedManager sdkCmsPublishedManager;
	
	@Autowired
	private ZooKeeperOperator			zooKeeperOperator;

	@Autowired
	private CacheManager				cacheManager;
	
	public static Map<String,String> moduleMap=new HashMap<String,String>();

	/**
	 * 保存CmsModuleInstance
	 * 
	 */
	public CmsModuleInstance saveCmsModuleInstance(CmsModuleInstance model){
	
		return cmsModuleInstanceDao.save(model);
	}
	
	/**
	 * 通过id获取CmsModuleInstance
	 * 
	 */
	@Transactional(readOnly=true)
	public CmsModuleInstance findCmsModuleInstanceById(Long id){
	
		return cmsModuleInstanceDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有CmsModuleInstance列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsModuleInstance> findAllCmsModuleInstanceList(){
	
		return cmsModuleInstanceDao.findAllCmsModuleInstanceList();
	};
	
	/**
	 * 通过ids获取CmsModuleInstance列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsModuleInstance> findCmsModuleInstanceListByIds(List<Long> ids){
	
		return cmsModuleInstanceDao.findCmsModuleInstanceListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CmsModuleInstance列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsModuleInstance> findCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap){
	
		return cmsModuleInstanceDao.findCmsModuleInstanceListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CmsModuleInstance列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsModuleInstance> findCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		if(paraMap.containsKey("ispublished")){
			Integer isPublished = (Integer)paraMap.get("ispublished");
			if(PULISHED.equals(isPublished)){
				paraMap.put("ispublished", true);
			}else{
				paraMap.put("ispublished", false);
			}
		}
		return cmsModuleInstanceDao.findCmsModuleInstanceListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsModuleInstance
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableCmsModuleInstanceByIds(List<Long> ids,Integer state){
		cmsModuleInstanceDao.enableOrDisableCmsModuleInstanceByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除CmsModuleInstance
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeCmsModuleInstanceByIds(List<Long> ids){
		cmsModuleInstanceDao.removeCmsModuleInstanceByIds(ids);
	}
	
	
	/**
	 * 获取有效的CmsModuleInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsModuleInstance> findAllEffectCmsModuleInstanceList(){
	
		return cmsModuleInstanceDao.findAllEffectCmsModuleInstanceList();
	};
	
	/**
	 * 通过参数map获取有效的CmsModuleInstance列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap){
	
		return cmsModuleInstanceDao.findEffectCmsModuleInstanceListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的CmsModuleInstance列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsModuleInstanceDao.findEffectCmsModuleInstanceListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public String findUpdatedCmsModuleInstance(Long templateId, Long moduleId,
			Boolean isEdit) {

		CmsModuleTemplate template = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId);

		String data = template.getData();
		
		if(StringUtils.isBlank(data)){
			return "";
		}

		if (null != moduleId) {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("moduleId", moduleId);
			List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);

			Document document = Jsoup.parse(data);
			for (CmsEditArea area : editAreaList) {
				String code = area.getModuleCode();
				Elements elements = document.select(CMS_HTML_EDIT_CLASS);
				if (null != elements && elements.size() > 0) {
					for (Element element : elements) {
						if (code.equals(element.attr("code"))) {
							//预览，处理隐藏
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
								element.remove();
							//修改，处理隐藏
							}else if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
								element.attr("hide", "0");
								element.html(area.getData());
							}else{
								element.html(area.getData());
							}
						}
					}

				}
				Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
				if (null != imgArtiEles && imgArtiEles.size() > 0) {
					for (Element element : imgArtiEles) {
						if (code.equals(element.attr("code"))) {
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
								element.remove();
							}else if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
								element.attr("hide", "0");
								element.html(area.getData());								
							}else{
								element.html(area.getData());
							}
						}
					}

				}
				
				Elements proArtiEles = document.select(CMS_PRODUCT_EDIT_CLASS);
				if (null != proArtiEles && proArtiEles.size() > 0) {
					for (Element element : proArtiEles) {
						if (code.equals(element.attr("code"))) {
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
								element.remove();
							}else if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
								element.attr("hide", "0");
								element.html(area.getData());								
							}else{
								element.html(area.getData());
							}
						}
					}

				}
				
			}
			data = document.toString();
		}
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		if (isEdit) {
			data = processNoEditData(data);
		}
		return data;
	
	}
	
	/**
	 * 去掉不要加载的数据, 如:不要加载的js 去掉<!--noedit-start-->到<!--noedit-end-->中间的数据
	 * 
	 * @param Data
	 * @return
	 */
	private static String processNoEditData(String data) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(NOEDIT_START);
		int indexEnd = data.indexOf(NOEDIT_END);

		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			sb.append(data.substring(indexEnd + NOEDIT_END.length(), data.length()));
			data = sb.toString();
			data = processNoEditData(data);
		}
		return data;
	}
	

	@Override
	public CmsModuleInstance createOrUpdateModuleInstance(CmsModuleInstance cmsModuleInstance, String html) {
		/** 保存页面实例 */
		CmsModuleInstance instance = null;
		Long id = cmsModuleInstance.getId();
		
		checkPageInstanceCode(cmsModuleInstance, id);
		
		if (null != id) {
			// 修改
			CmsModuleInstance dbModule = cmsModuleInstanceDao.getByPrimaryKey(id);
			dbModule.setCode(cmsModuleInstance.getCode());
			dbModule.setName(cmsModuleInstance.getName());
			dbModule.setTemplateId(cmsModuleInstance.getTemplateId());
			dbModule.setModifyTime(new Date());
			instance = cmsModuleInstanceDao.save(dbModule);
		} else {
			// 保存
			cmsModuleInstance.setCreateTime(new Date());
			cmsModuleInstance.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
			cmsModuleInstance.setIsPublished(false);
			instance = cmsModuleInstanceDao.save(cmsModuleInstance);
		}

		Map<String, String> editAreaMap = processPageHtml(html);
		Long moduleId = instance.getId();
		// 保存页面编辑区域
		CmsEditArea cmsEditArea = null;
		for (Map.Entry<String, String> entry : editAreaMap.entrySet()) {
			//根据moduleCode查询
			Map<String, Object> paraMap = new  HashMap<String, Object>();
			paraMap.put("moduleCode", entry.getKey());
			paraMap.put("moduleTemplateId", instance.getTemplateId());
			paraMap.put("moduleId", moduleId);			
			List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
			//修改
			if(cmsEditAreas!=null && cmsEditAreas.size()>0){
				cmsEditArea = sdkCmsEditAreaManager.findCmsEditAreaById(cmsEditAreas.get(0).getId());
				cmsEditArea.setData(entry.getValue());
				cmsEditArea.setModifyTime(new Date());
				sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
			}else{
				//新增
				cmsEditArea = new CmsEditArea();
				cmsEditArea.setModuleCode(entry.getKey());
				cmsEditArea.setData(entry.getValue());
				cmsEditArea.setLifecycle(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditArea.setModuleId(moduleId);
				cmsEditArea.setModuleTemplateId(instance.getTemplateId());
				cmsEditArea.setCreateTime(new Date());
				
				sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
			}
		}
		return instance;
	}
	
	@Transactional(readOnly=true)
	private void checkPageInstanceCode(CmsModuleInstance cmsModuleInstance, Long id){
		// 检察 页面编码, url 是否已存在
		Map<String, Object> paraMap = new HashMap<String, Object>(); 
		if(null != id){
			paraMap.put("id", id);
		}
		paraMap.put("code", cmsModuleInstance.getCode());
		List<CmsModuleInstance> cmis = cmsModuleInstanceDao.checkModuleInstanceCode(paraMap);
		if(cmis != null && cmis.size()>0){
			throw new BusinessException("模块实例编码已存在 ");
		}
	
	}
	/**
	 * 处理页面的html, 获取code与html
	 * 
	 * @param html
	 * @return
	 */
	private Map<String, String> processPageHtml(String html) {
		/** key:areaCode, value:html */
		Map<String, String> pageAreaMap = new HashMap<String, String>();
		Document document = Jsoup.parse(html);
		// 去掉 "编辑"按钮的div
		Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
		editButtonElements.remove();
		Elements editHideButtonElements = document.select(".wui-tips-shade");
		editHideButtonElements.remove();
		//编辑html模式
		Elements elements = document.select(CMS_HTML_EDIT_CLASS);
		dealHtml(pageAreaMap, elements);
		//图文模式
		Elements imgElements = document.select(CMS_IMGARTICLE_EDIT_CLASS);
		dealHtml(pageAreaMap, imgElements);
		
		//商品模式
		Elements proElements =document.select(CMS_PRODUCT_EDIT_CLASS);
		dealHtml(pageAreaMap, proElements);
		
		return pageAreaMap;
	}
	private  void dealHtml(Map<String, String> pageAreaMap ,Elements elements){
		Integer i = 1;
		for (Element element : elements) {
			String areaCode = element.attr("code");
			if (StringUtils.isBlank(areaCode)) {
				throw new BusinessException("模块编辑区域的code不存在 ");
			}
			String areaHtml = element.html();
			//处理静态base信息
			areaHtml = sdkCmsPageTemplateManager.addTemplateBase(areaHtml);
			pageAreaMap.put(areaCode, areaHtml);
			i++;
		}
	}

	@Override
	public void publishModuleInstance(Long moduleId) {
		
		CmsModuleInstance moduleInstance = cmsModuleInstanceDao.getByPrimaryKey(moduleId);
		String moduleCode = moduleInstance.getCode();
		// 先删除, 再添加
		sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleCode);

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("moduleId", moduleInstance.getId());
		List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
		for (CmsEditArea cmsEditArea : editAreaList) {
			CmsPublished cmsPublished = new CmsPublished();
			cmsPublished.setAreaCode(cmsEditArea.getModuleCode());
			cmsPublished.setModuleCode(moduleCode);
			cmsPublished.setHide(cmsEditArea.getHide());
			cmsPublished.setData(cmsEditArea.getData());
			cmsPublished.setPublishTime(new Date());
			sdkCmsPublishedManager.saveCmsPublished(cmsPublished);
		}
		// 修改成已发布状态
		moduleInstance.setIsPublished(true);
		cmsModuleInstanceDao.save(moduleInstance);
		
		zooKeeperOperator.noticeZkServer(ModuleMapWatchInvoke.LISTEN_PATH);
		//cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, moduleInstance.getCode());
		
	}

	@Override
	public void cancelPublishedModuleInstance(Long moduleId) {
		CmsModuleInstance moduleInstance = cmsModuleInstanceDao.getByPrimaryKey(moduleId);
		// 先删除, 再添加
		sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleInstance.getCode());
		moduleInstance.setIsPublished(false);
		cmsModuleInstanceDao.save(moduleInstance);
		zooKeeperOperator.noticeZkServer(ModuleMapWatchInvoke.LISTEN_PATH);
	}

	@Override
	public String recoverTemplateCodeArea(Long templateId, String code) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("moduleTemplateId", templateId);
		paraMap.put("moduleCode", code);
		List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
		if(cmsEditAreas == null || cmsEditAreas.size()==0){
			throw new BusinessException("编辑部分无需重置");
		}
		//根据code在模板中找出需要还原的数据
		String html = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId).getData();
		Map<String, String> map = processResetPageHtml(html);
		if(map.size()==0){
			throw new BusinessException("编辑部分无需重置");
		}
		String data = map.get(code);
		Document document = Jsoup.parse(data);
		Elements eles =	document.getElementsByTag("body");
		if(eles!=null&& eles.size()!=0){
			data =eles.get(0).html();
		}
		//根据模板id删除编辑区域数据
		sdkCmsEditAreaManager.removeCmsModuleEditAreaByTemplateId(templateId,code);
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		return data;
	}
	
	
	/**
	 * 处理页面的html, 获取code与html
	 * 
	 * @param html
	 * @return
	 */
	private Map<String, String> processResetPageHtml(String html) {
		/** key:areaCode, value:html */
		Map<String, String> pageAreaMap = new HashMap<String, String>();
		Document document = Jsoup.parse(html);
		// 去掉 "编辑"按钮的div
		Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
		editButtonElements.remove();
		//编辑html模式
		Elements elements = document.select(CMS_HTML_EDIT_CLASS);
		dealHtmlReset(pageAreaMap, elements,"cms-html-edit");
		//图文模式
		Elements imgElements = document.select(CMS_IMGARTICLE_EDIT_CLASS);
		dealHtmlReset(pageAreaMap, imgElements,"cms-imgarticle-edit");
		
		return pageAreaMap;
	}

	private  void dealHtmlReset(Map<String, String> pageAreaMap ,Elements elements,String cls){
		Integer i = 1;
		for (Element element : elements) {
			String areaCode = element.attr("code");
			if (StringUtils.isBlank(areaCode)) {
				throw new BusinessException("模块编辑区域的code不存在");
			}
			String areaHtml = element.html();
			pageAreaMap.put(areaCode, cls+"EDIT_CLASS_SEP"+sdkCmsPageTemplateManager.addTemplateBase(areaHtml));
			i++;
		}
	}

	@Override
	public void loadModuleMap() {
		
		moduleMap.clear();
		
		List<CmsModuleInstance> moduleList= cmsModuleInstanceDao.findAllCmsModuleInstanceList();
		for(CmsModuleInstance module:moduleList){
			if(module.getIsPublished()!=null && module.getIsPublished()){
				String moduleCode = module.getCode();
				String data = getPublishData(moduleCode, module.getTemplateId());
				moduleMap.put(moduleCode,data);
			}
		}
		
	}
	
	private String getPublishData(String moduleCode ,Long templateId){
		CmsModuleTemplate template = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId);
		String data = template.getData();
		
		if(StringUtils.isBlank(data)){
			return "";
		}
		if (null != moduleCode) {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("moduleCode", moduleCode);
			List<CmsPublished> editAreaList = sdkCmsPublishedManager.findCmsPublishedListByQueryMap(paraMap);

			Document document = Jsoup.parse(data);
			for (CmsPublished area : editAreaList) {
				String code = area.getAreaCode();
				Elements elements = document.select(CMS_HTML_EDIT_CLASS);
				if (null != elements && elements.size() > 0) {
					for (Element element : elements) {
						if (code.equals(element.attr("code"))) {
							element.html(area.getData());
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide())){
								element.remove();
							}else{
								element.html(area.getData());
							}
						}
					}

				}
				Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
				if (null != imgArtiEles && imgArtiEles.size() > 0) {
					for (Element element : imgArtiEles) {
						if (code.equals(element.attr("code"))) {
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide())){
								element.remove();
							}else{
								element.html(area.getData());
							}
						}
					}

				}
				Elements product = document.select(CMS_PRODUCT_EDIT_CLASS);
				if (null != product && product.size() > 0) {
					for (Element element : product) {
						if (code.equals(element.attr("code"))) {
							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide())){
								element.remove();
							}else{
								element.html(area.getData());
							}
						}
					}

				}
			}
			data = document.toString();
		}
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		data = processOnlyEditData(data);
		data = processExtraHtmlTag(data);
		return data;
		
	}
	
	/**
	 * 去掉编辑时添加的内容, 如:不要加载的js 去掉<!--onlyedit-start-->到<!--onlyedit-start-->中间的数据
	 * 
	 * @param Data
	 * @return
	 */
	private static String processOnlyEditData(String data) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(ONLYEDIT_START);
		int indexEnd = data.indexOf(ONLYEDIT_END);

		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			sb.append(data.substring(indexEnd + ONLYEDIT_END.length(), data.length()));
			data = sb.toString();
			data = processOnlyEditData(data);
		}
		return data;
	}
	
	/**
	 * 对于模块，去掉jsoup自动添加的<html></html><body></body>标签
	 * @param data
	 * @return
	 */
	private String processExtraHtmlTag(String data) {
		return data.replaceAll("<html [^>]*>", "").replace("</html>", "").replaceAll("<HTML [^>]*>", "").replace("</HTML>", "")
				.replaceAll("<head [^>]*>", "").replace("</head>", "").replaceAll("<HEAD [^>]*>", "").replace("</HEAD>", "")
				.replaceAll("<body [^>]*>", "").replace("</body>", "").replaceAll("<BODY [^>]*>", "").replace("</BODY>", "");
	}
	
	@Override
	public Map<String, String> getModuleMap() {
		
		if(moduleMap.size() == 0){
			loadModuleMap();
		}
		return moduleMap;
	}
	
}
