package com.baozun.nebula.manager.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.impl.SdkCmsModuleInstanceManagerImpl;

@Service("cmsPageInstanceManager")
@Transactional
public class CmsPageInstanceManagerImpl implements CmsPageInstanceManager {

	@Autowired
	private SdkCmsPageTemplateManager	sdkCmsPageTemplateManager;

	@Autowired
	private SdkCmsPublishedManager		sdkCmsPublishedManager;

	@Autowired
	private SdkCmsPageInstanceManager	sdkCmsPageInstanceManager;

	@Autowired
	private CacheManager				cacheManager;

	private final static String			BODY_START			= "<!--body-start-->";

	private final static String			BODY_END			= "<!--body-end-->";

	private final static String			RESOURCE_START		= "<!--resources-start-->";

	private final static String			RESOURCE_END		= "<!--resources-end-->";

	private final static String			CMS_HTML_EDIT_CLASS	= "cms-html-edit";
	private final static String			CMS_IMGARRICLE_EDIT_CLASS	= ".cms-imgarticle-edit";
	public final static String			CMS_PRODUCT_EDIT_CLASS			= ".cms-product-edit";

	/**
	 * 取出公共的资源
	 * 
	 * @param html
	 * @return
	 */
	private String findResource(String html) {

		int index1 = html.indexOf(RESOURCE_START);

		int index2 = html.indexOf(RESOURCE_END);

		if (index1 != -1 && index2 != -1) {
			html = html.substring(index1 + RESOURCE_START.length());

			index2 = html.indexOf(RESOURCE_END);
			if (index2 != -1) {
				html = html.substring(0, index2);
			}

		} else {
			html = "";
		}

		return html;
	}

	/**
	 * 使用公共头尾时，需要处理一下body
	 * 
	 * @param html
	 * @return
	 */
	private String processBody(String html) {

		if (StringUtils.isBlank(html))
			return "";

		int index = html.indexOf(BODY_START);

		if (index != -1) {
			html = html.substring(index + BODY_START.length());
		}

		index = html.indexOf(BODY_END);

		if (index != -1) {
			html = html.substring(0, index);
		}

		return html;

	}

	@Override
	public Map<String, String> findPublishPage(CmsPageInstance pageInstance) {
		// TODO 1.还需要处理 使用公共头尾，也就是去掉body之外的内容
		// 2.公共资源的处理方法 <resource></resource>之间的内容

		Map<String, String> result = cacheManager.getMapObject(CacheKeyConstant.CMS_PAGE_KEY, pageInstance.getCode());

		if (result == null) {

			CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(pageInstance.getTemplateId());

			String data = template.getData();

			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pageCode", pageInstance.getCode());
			List<CmsPublished> areaList = sdkCmsPublishedManager.findCmsPublishedListByQueryMap(paraMap);

			Document doc = Jsoup.parse(data); 

			for (CmsPublished area : areaList) {
				//html编辑模式
				dealEditClass(doc, area, "."+CMS_HTML_EDIT_CLASS);
				//图文编辑模式
				dealEditClass(doc, area, CMS_IMGARRICLE_EDIT_CLASS);
				//商品模式
				dealEditClass(doc, area, CMS_PRODUCT_EDIT_CLASS);

			}
			//设置seo信息
			sdkCmsPageInstanceManager.setSeoInfo(doc, pageInstance);
			data = doc.toString();

			data = sdkCmsPageTemplateManager.processTemplateBase(data);

			result = new HashMap<String, String>();
			result.put("resource", findResource(data));

			if (template.getUseCommonHeader()) {
				data = processBody(data);
			}
			data = processOnlyEditData(data);
			result.put("data", data);
			result.put("useCommonHeader", String.valueOf(template.getUseCommonHeader()));

			cacheManager.setMapObject(CacheKeyConstant.CMS_PAGE_KEY, pageInstance.getCode(), result,
					TimeInterval.SECONDS_PER_WEEK);
		}

		return result;
	}
	
	
	/**
	 * 去掉编辑时添加的内容, 如:不要加载的js 去掉<!--onlyedit-start-->到<!--onlyedit-end-->中间的数据
	 * 
	 * @param Data
	 * @return
	 */
	private static String processOnlyEditData(String data) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(SdkCmsModuleInstanceManagerImpl.ONLYEDIT_START);
		int indexEnd = data.indexOf(SdkCmsModuleInstanceManagerImpl.ONLYEDIT_END);

		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			sb.append(data.substring(indexEnd + SdkCmsModuleInstanceManagerImpl.ONLYEDIT_END.length(), data.length()));
			data = sb.toString();
			data = processOnlyEditData(data);
		}
		return data;
	}
	
	/**
	 * 
	* @author 何波
	* @Description: 处理各种编辑class
	* @param document
	* @param area
	* @param cls
	* @param isEdit   
	* void   
	* @throws
	 */
	private  void dealEditClass(Document document,CmsPublished area,String cls){
		String code = area.getAreaCode();
		Elements proEles = document.select(cls);
		if (null != proEles && proEles.size() > 0) {
			for (Element element : proEles) {
				if (code.equals(element.attr("code"))) {
					if(area.getHide()!=null && area.getHide()==0){
						element.remove();
					}else{
						element.html(area.getData());
						if(cls.equals(CMS_PRODUCT_EDIT_CLASS)){
							sdkCmsPageInstanceManager.setProductInfo(element,false);
						}
					}
				}
			}

		}
	}

}
