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
package com.baozun.nebula.sdk.manager.cms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsPageTemplateDao;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * CmsPageTemplateManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkCmsPageTemplateManager") 
public class SdkCmsPageTemplateManagerImpl implements SdkCmsPageTemplateManager {
	
	private final static Logger log = LoggerFactory.getLogger(SdkCmsPageTemplateManagerImpl.class);

	@Autowired
	private CmsPageTemplateDao cmsPageTemplateDao;

	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	/**
	 * 静态base标识
	 */
	public final static String STATIC_BASE_CHAR="#{staticbase}";
	
	/**
	 * 页面base标识
	 */
	public final static String PAGE_BASE_CHAR="#{pagebase}";
	
	/**
	 * 图片base标识
	 */
	public final static String IMG_BASE_CHAR="#{imgbase}";
	
	/**
	 * version
	 */
	private final static String VERSION="version=000000";
	
	/**
	 * 保存CmsPageTemplate
	 * 
	 */
	public CmsPageTemplate saveCmsPageTemplate(CmsPageTemplate model){
		model.setCreateTime(new Date());
		model.setLifecycle(BaseModel.LIFECYCLE_ENABLE);
		return cmsPageTemplateDao.save(model);
	}
	
	/**
	 * 通过id获取CmsPageTemplate
	 * 
	 */
	@Transactional(readOnly=true)
	public CmsPageTemplate findCmsPageTemplateById(Long id){
	
		return cmsPageTemplateDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有CmsPageTemplate列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageTemplate> findAllCmsPageTemplateList(){
		
		return cmsPageTemplateDao.findAllCmsPageTemplateList();
	};
	
	/**
	 * 通过ids获取CmsPageTemplate列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageTemplate> findCmsPageTemplateListByIds(List<Long> ids){
	
		return cmsPageTemplateDao.findCmsPageTemplateListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CmsPageTemplate列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageTemplate> findCmsPageTemplateListByQueryMap(Map<String, Object> paraMap){
	
		return cmsPageTemplateDao.findCmsPageTemplateListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CmsPageTemplate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsPageTemplate> findCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsPageTemplateDao.findCmsPageTemplateListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsPageTemplate
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableCmsPageTemplateByIds(List<Long> ids,Integer state){
		cmsPageTemplateDao.enableOrDisableCmsPageTemplateByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除CmsPageTemplate
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeCmsPageTemplateByIds(List<Long> ids){
//		for(Long tempid : ids){
//			if(!CheckPublishedInstanceInTemplate(tempid)){
//				HashMap<String, Object> param = new HashMap<String, Object>();
//				param.put("templateId", tempid);
//				param.put("isPublished", false);
//				List<CmsPageInstance> cancelInstances = sdkCmsPageInstanceManager.findEffectCmsPageInstanceListByQueryMap(param);
//				List<Long> instanceIds = new ArrayList<Long>();
//				for(CmsPageInstance cancelInstance : cancelInstances){
//					instanceIds.add(cancelInstance.getId());
//				}
//				sdkCmsPageInstanceManager.removeCmsPageInstanceByIds(instanceIds);
//			}else{
//				return PublishResult.REMOVETEMPLATEEXISTPUBLISHEDINSTANCE;				
//			}
//		}
		
		cmsPageTemplateDao.removeCmsPageTemplateByIds(ids);
		//return PublishResult.SUCCESS;
	}
	
//	/**
//	 * 检测模板是否包含已发布的实例
//	 * @param tempid 模板id
//	 */
//	private boolean CheckPublishedInstanceInTemplate(Long tempid){
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("templateId", tempid);
//		param.put("isPublished", true);
//		List<CmsPageInstance> cmsPageInstances = sdkCmsPageInstanceManager.findEffectCmsPageInstanceListByQueryMap(param);
//		if(Validator.isNotNullOrEmpty(cmsPageInstances) && cmsPageInstances.size()>0){
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 获取有效的CmsPageTemplate列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageTemplate> findAllEffectCmsPageTemplateList(){
	
		return cmsPageTemplateDao.findAllEffectCmsPageTemplateList();
	};
	
	/**
	 * 通过参数map获取有效的CmsPageTemplate列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMap(Map<String, Object> paraMap){
	
		return cmsPageTemplateDao.findEffectCmsPageTemplateListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的CmsPageTemplate列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsPageTemplateDao.findEffectCmsPageTemplateListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	public String addTemplateBase(String html) {
		Properties metainfoProperties = ProfileConfigUtil.findPro("config/metainfo.properties");
		if(StringUtils.isBlank(html)){
			return "";
		}
		Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");
		String imgbase = StringUtils.trim(properties.getProperty("upload.img.domain.base"));
		
		Document doc = Jsoup.parse(html);
		//process img
		Elements imgs=doc.select("img");
		for(Element ele:imgs){
			String src=ele.attr("src");
			if(StringUtils.isBlank(src) || src.indexOf("version=") != -1){
				continue;
			}else if(src.startsWith("/")){
				src=STATIC_BASE_CHAR+src+"?"+VERSION;
			}else if(!src.startsWith("http")){
				src=STATIC_BASE_CHAR+"/"+src+"?"+VERSION;
			}else if(src.startsWith(imgbase)){
				src=IMG_BASE_CHAR+"/"+src.replace(imgbase, "")+"?"+VERSION;
			}
			
			ele.attr("src", src);
		}
		
		//process script
		Elements scripts=doc.select("script");
		for(Element ele:scripts){
			String src=ele.attr("src");
			if(StringUtils.isBlank(src)){
				continue;
			}
			else if(src.startsWith("/")){
				src=STATIC_BASE_CHAR+src+"?"+VERSION;
			}
			else if(!src.startsWith("http")){
				src=STATIC_BASE_CHAR+"/"+src+"?"+VERSION;
			}
			ele.attr("src", src);
		}
		
		//process css
			
			
		Elements links=doc.select("link");
		for(Element ele:links){
			String href=ele.attr("href");
			if(StringUtils.isBlank(href)){
				continue;
			}
			else if(href.startsWith("/")){
				href=STATIC_BASE_CHAR+href+"?"+VERSION;
			}
			else if(!href.startsWith("http")){
				href=STATIC_BASE_CHAR+"/"+href+"?"+VERSION;
			}
			ele.attr("href", href);
		}
		
			
		//PROCESS a
		Elements as=doc.select("a");
		for(Element ele:as){
			String href=ele.attr("href");
			if(StringUtils.isBlank(href)){
				continue;
			}
			else if(href.startsWith("javascript:void(0)")||href.startsWith("JAVASCRIPT:VOID(0)")||href.startsWith("#")){
				continue;
			}
			if(href.startsWith("/")){
				href=PAGE_BASE_CHAR+href;
			}
			else if(!href.startsWith("http")){
				href=PAGE_BASE_CHAR+"/"+href;
			}
			ele.attr("href", href);
		}
			
		return doc.toString();
	}

	@Override
	public String processTemplateBase(String html) {
		Properties metainfoProperties = ProfileConfigUtil.findPro("config/metainfo.properties");
		
		if(StringUtils.isBlank(html)){
			return "";
		}
		
		Properties properties=ProfileConfigUtil.findPro("config/metainfo.properties");
		
		String pagebase=StringUtils.trim(properties.getProperty("page.base"));
		
		String staticbase=StringUtils.trim(properties.getProperty("static.domain.base"));
		
		String imgbase=StringUtils.trim(properties.getProperty("upload.img.domain.base"));
		log.info("pagebase is {}, staticbase is {} and imgbase is {}", pagebase, staticbase, imgbase);
		if(StringUtils.isBlank(pagebase)){
			pagebase="";
		}else if("/".equals(pagebase)){
			pagebase="";
		}else if(pagebase.endsWith("/")){
			pagebase=pagebase.substring(0,pagebase.length()-1);
		}
		
		if(StringUtils.isBlank(staticbase)){
			staticbase="";
		}else if("/".equals(staticbase)){
			staticbase = "";
		}else if(staticbase.endsWith("/")){
			staticbase = staticbase.substring(0, staticbase.length()-1);
		}
		
		if(StringUtils.isBlank(imgbase)){
			imgbase="";
		}else if("/".equals(imgbase)){
			imgbase = "";
		}else if(imgbase.endsWith("/")){
			imgbase = imgbase.substring(0, imgbase.length()-1);
		}
		
		html = html.replace(PAGE_BASE_CHAR, pagebase);
		
		html = html.replace(STATIC_BASE_CHAR, staticbase);
		
		html = html.replace(IMG_BASE_CHAR, imgbase);
		
		return html;
	}
}
