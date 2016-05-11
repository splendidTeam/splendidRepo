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
import java.util.Properties;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsPageInstanceDao;
import com.baozun.nebula.dao.cms.CmsPageInstanceVersionDao;
import com.baozun.nebula.dao.cms.CmsPageTemplateDao;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * CmsPageInstanceManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkCmsPageInstanceManager") 
public class SdkCmsPageInstanceManagerImpl implements SdkCmsPageInstanceManager {

	private final static Logger log = LoggerFactory.getLogger(SdkCmsPageInstanceManagerImpl.class);

	@Autowired
	private CmsPageInstanceDao cmsPageInstanceDao;
	@Autowired
	private CmsPageTemplateDao cmsPageTemplateDao;
	@Autowired
	private CmsPageInstanceVersionDao cmsPageInstanceVersionDao;
	
	public final static String	 p_title=".cms-show-product-title";
	public final static String	 p_salesprice=".cms-show-product-salesprice";
	public final static String	 p_listprice=".cms-show-product-listprice";
	public final static String	 p_desc=".cms-show-product-desc";
	public final static String	 p_img=".cms-show-product-img";
	public final static String	 p_img_h=".cms-show-product-href";
	public final static String	 p_text_h=".cms-show-product-text-href";
	
	@Autowired
	private SdkItemManager sdkItemManager;
	/**
	 * urlmap,用于前台查询自己定义cmspage的url地址
	 */
	public static Map<String,CmsPageInstance> urlMap=new HashMap<String,CmsPageInstance>();

	/**
	 * 保存CmsPageInstance
	 * 
	 */
	public CmsPageInstance saveCmsPageInstance(CmsPageInstance model){
	
		return cmsPageInstanceDao.save(model);
	}
	
	/**
	 * 通过id获取CmsPageInstance
	 * 
	 */
	@Transactional(readOnly=true)
	public CmsPageInstance findCmsPageInstanceById(Long id){
	
		return cmsPageInstanceDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有CmsPageInstance列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findAllCmsPageInstanceList(){
	
		return cmsPageInstanceDao.findAllCmsPageInstanceList();
	};
	
	/**
	 * 通过ids获取CmsPageInstance列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findCmsPageInstanceListByIds(List<Long> ids){
	
		return cmsPageInstanceDao.findCmsPageInstanceListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CmsPageInstance列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findCmsPageInstanceListByQueryMap(Map<String, Object> paraMap){
	
		return cmsPageInstanceDao.findCmsPageInstanceListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CmsPageInstance列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsPageInstance> findCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsPageInstanceDao.findCmsPageInstanceListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsPageInstance
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableCmsPageInstanceByIds(List<Long> ids,Integer state){
		cmsPageInstanceDao.enableOrDisableCmsPageInstanceByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除CmsPageInstance
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeCmsPageInstanceByIds(List<Long> ids){
		cmsPageInstanceDao.removeCmsPageInstanceByIds(ids);
	}
	
	
	/**
	 * 获取有效的CmsPageInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findAllEffectCmsPageInstanceList(){
	
		return cmsPageInstanceDao.findAllEffectCmsPageInstanceList();
	};
	
	/**
	 * 通过参数map获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findEffectCmsPageInstanceListByQueryMap(Map<String, Object> paraMap){
	
		return cmsPageInstanceDao.findEffectCmsPageInstanceListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsPageInstance> findEffectCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsPageInstanceDao.findEffectCmsPageInstanceListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public void loadUrlMap() {
		urlMap.clear();
		List<CmsPageInstance> pageList=cmsPageInstanceDao.findAllPublishedCmsPageInstanceList();
		for(CmsPageInstance page:pageList){

			if(page.getIsPublished()!=null&&page.getIsPublished()){
				Integer type = page.getSupportType();
				String key = page.getUrl(); 
				if(type != null){
					key =  page.getUrl()+"-"+type;
				}
				urlMap.put(key, page);
			}

				
		}
		
		log.info("urlMap have {}", urlMap);
	}

	@Override
	public Map<String, CmsPageInstance> findUrlMap() {
		// TODO Auto-generated method stub
		return urlMap;
	}

	@Override
	@Transactional(readOnly=true)
	public CmsPageInstance checkPageInstanceCode(Map<String, Object> paraMap) {
		return cmsPageInstanceDao.checkPageInstanceCode(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public CmsPageInstance checkPageInstanceUrl(Map<String, Object> paraMap) {
		return  cmsPageInstanceDao.checkPageInstanceUrl(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CmsPageInstance> findCmsPageInstanceListByTemplateIds(
			List<Long> templateIds) {
		return cmsPageInstanceDao.findCmsPageInstanceListByTemplateIds(templateIds);
	};
	

	/**
	 * 
	* @author 何波
	* @Description: 设置seo信息
	* @param doc
	* @param pageInstance   
	* void   
	* @throws
	 */
	public  void  setSeoInfo(Document doc ,CmsPageInstance pageInstance ){
		if(doc==null || pageInstance==null){
			return;
		}
		//处理标题
		Elements titles = doc.getElementsByTag("title");
		Elements heads = doc.getElementsByTag("head");
		if(titles!=null && titles.size()>0){
			for (Element title : titles) {
				title.html(pageInstance.getSeoTitle());
			}
		}else{
			for (Element element : heads) {
				element.append("<title>"+pageInstance.getSeoTitle()+"</title>");
			}
		}
		//处理keywords
		Elements metas = doc.getElementsByTag("meta");
		if(metas!=null && metas.size()>0){
			for (Element meta : metas) {
				Elements  keywords = meta.getElementsByAttributeValue("name", "keywords");
				if(keywords==null || keywords.size()==0){
					for (Element head : heads) {
						String words = "<meta name=\"keywords\" content=\""+pageInstance.getSeoKeywords()+"\"/>";
						head.append(words);
					}
				}else{
					for (Element key : keywords) {
						key.attr("content", pageInstance.getSeoKeywords());
					}
				}
				Elements  descs = meta.getElementsByAttributeValue("description", "description");
				if(descs==null || descs.size()==0){
					for (Element head : heads) {
						String desc = "<meta name=\"description\" content=\""+pageInstance.getSeoDescription()+"\"/>";
						head.append(desc);
					}
				}else{
					for (Element desc : descs) {
						desc.attr("content", pageInstance.getSeoDescription());
					}
				}
			}
		}else{
			for (Element head : heads) {
				String words = "<meta name=\"keywords\" content=\""+pageInstance.getSeoKeywords()+"\"/>";
				String descs = "<meta name=\"description\" content=\""+pageInstance.getSeoDescription()+"\"/>";
				head.append(words+descs);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public CmsPageInstance checkPublishPageInstanceUrl(
			Map<String, Object> paraMap) {
		Long  tmpId = Long.parseLong(paraMap.get("tmpId").toString());
		CmsPageTemplate cpt =  cmsPageTemplateDao.getByPrimaryKey(tmpId);
		paraMap.put("type", cpt.getSupportType());
		return cmsPageInstanceDao.checkPageInstanceUrl(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public String setProductInfo(Element element,boolean isEdit) {
		Elements list = element.select(".cms-area-list-element");
		if(list != null && list.size() > 0){
			for (Element ele : list) {
				String productCode = ele.attr("cms-area-product-code");
				String type =  ele.attr("img-type");
				if ((StringUtils.isBlank(productCode) || StringUtils.isBlank(type)) && !isEdit) {
					ele.remove();
					log.error("商品编码为空");
					continue;
				}
				ItemBaseCommand item = sdkItemManager.findItemBaseInfoByCode(productCode);
				if(item == null){
					log.error("根据商品编码:"+productCode+"未查询到对应商品");
				}else{
					//查询图片url
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("itemId", item.getItemId());
					paramMap.put("type", type);
					List<ItemImage> itemImageList = sdkItemManager.findItemImgList(paramMap);
					String imgurl = null;
					if(itemImageList==null || itemImageList.size()==0){
						log.error("根据商品id和type"+item.getItemId()+"和"+type+"未查询到对应商品图片");
					}else{
						 imgurl = itemImageList.get(0).getPicUrl();
					}
					// 设置商品信息
					setAllInfo(ele, item,imgurl,isEdit);
				}
			}
		}else{
			String productCode = element.children().attr("cms-area-product-code");
			String type =  element.children().attr("img-type");
			if ((StringUtils.isBlank(productCode) || StringUtils.isBlank(type)) && !isEdit) {
				element.remove();
				log.error("商品编码为空");
				return "";
			}
			ItemBaseCommand item = sdkItemManager.findItemBaseInfoByCode(productCode);
		
			if(item == null ){
				log.error("根据商品编码:"+productCode+"未查询到对应商品");
			}else{
				//查询图片url
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("itemId", item.getItemId());
				paramMap.put("type", type);
				List<ItemImage> itemImageList = sdkItemManager.findItemImgList(paramMap);
				String imgurl = null;
				if(itemImageList==null || itemImageList.size()==0){
					log.error("根据商品id和type"+item.getItemId()+"和"+type+"未查询到对应商品图片");
				}else{
					imgurl = itemImageList.get(0).getPicUrl();
				}
				// 设置商品信息
				setAllInfo(element, item,imgurl,isEdit);
			}
		}
		return element.html();
	}
	
	private void  setAllInfo (Element element, ItemBaseCommand item,String imgurl,boolean isEdit){
		setInfo(element, p_title, item.getTitle(), "comm");
		if(item.getListPrice() != null){
			setInfo(element, p_salesprice, String.valueOf(item.getSalePrice()), "comm");
		}
		if(item.getListPrice() != null){
			setInfo(element, p_listprice, String.valueOf(item.getListPrice()), "comm");
		}
		//商品描述
		setInfo(element, p_desc, item.getSketch(), "comm");
		
		setInfo(element, p_img,imgurl, "img");
		Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");
		//商品url
		String pdpPrefix = StringUtils.trim(properties.getProperty("pdpPrefix"));
		String pdpType = StringUtils.trim(properties.getProperty("pdp.param.type"));
		String itemUrl = pdpPrefix;
		if(pdpType.equals("code")){
			itemUrl = itemUrl.replace("(@)",item.getCode());
		}else{
			itemUrl =  itemUrl.replace("(@)",itemUrl+item.getItemId()) ;
		}
		setInfo(element, p_img_h,itemUrl, "a");
		setInfo(element, p_text_h,itemUrl, "a");
	}

	private void  setInfo (Element ele, String cls,String txt,String type){
		Elements eles= ele.select(cls);
		Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");
		String imgbase = StringUtils.trim(properties.getProperty("upload.img.domain.base"));
		if(eles!=null && eles.size()>0){
			for (Element element : eles) {
				if(txt == null){
					txt = "";
				}
				if(type.equals("img")){
					element.attr("src", imgbase+txt);
				}else if( type.equals("a")){
					element.attr("href", txt);
				}else{
					element.html(txt);
				}
			}
		}
	}

	@Override
	public Map<String, Date> getPublishedPageInstanceVersionsTimeRang(Long pageId) {
		// TODO Auto-generated method stub
		return cmsPageInstanceVersionDao.getPublishedPageInstanceVersionsTimeRang(pageId);
	}
}
