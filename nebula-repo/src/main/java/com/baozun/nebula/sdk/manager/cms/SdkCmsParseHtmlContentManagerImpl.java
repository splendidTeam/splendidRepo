package com.baozun.nebula.sdk.manager.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.sdk.manager.cms.builder.CmsHtmlResolver;
import com.feilong.core.Validator;

/**
 * SdkCmsParseHtmlContentManagerImpl
 * 
 * @author 谢楠
 *
 */
@Transactional
@Service("sdkCmsParseHtmlContentManager")
public class SdkCmsParseHtmlContentManagerImpl implements SdkCmsParseHtmlContentManager{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkCmsParseHtmlContentManagerImpl.class);

    public final static String CMS_HTML_EDIT_CLASS = ".cms-html-edit";

    public final static String CMS_IMGARTICLE_EDIT_CLASS = ".cms-imgarticle-edit";

    public final static String ONLYEDIT_START = "<!--onlyedit-start-->";

    public final static String ONLYEDIT_END = "<!--onlyedit-end-->";

    public final static String NOEDIT_START = "<!--noedit-start-->";

    public final static String NOEDIT_END = "<!--noedit-end-->";

    private final static String RESOURCE_START = "<!--resources-start-->";

    private final static String RESOURCE_END = "<!--resources-end-->";

    private final static String BODY_START = "<!--body-start-->";

    private final static String BODY_END = "<!--body-end-->";

    public final static String CMS_DIV_EDIT_BUTTON_CLASS = ".wui-tips";

    public final static String CMS_PRODUCT_EDIT_CLASS = ".cms-product-edit";

    @Autowired
    private CmsHtmlResolver cmsHtmlResolver;

    @Autowired
    private SdkCmsModuleTemplateManager sdkCmsModuleTemplateManager;

    @Autowired
    private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;

    @Autowired
    private SdkCmsEditAreaManager sdkCmsEditAreaManager;

    @Autowired
    private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;

    @Autowired
    private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;

    @Autowired
    private SdkCmsPageInstanceVersionManager sdkCmsPageInstanceVersionManager;

    @Override
    public <T> String getParseModuleData(List<T> editAreaList,Long templateId){
        CmsModuleTemplate template = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId);
        String data = template.getData();

        if (StringUtils.isBlank(data)){
            return "";
        }

        Document document = Jsoup.parse(data);
        for (T area : editAreaList){
            String code = "";
            Integer hide = null;
            String areaData = "";
            if (area instanceof CmsPublished){
                code = ((CmsPublished) area).getAreaCode();
                areaData = ((CmsPublished) area).getData();
                hide = ((CmsPublished) area).getHide();
            }else if (area instanceof CmsEditArea){
                code = ((CmsEditArea) area).getModuleCode();
                areaData = ((CmsEditArea) area).getData();
                hide = ((CmsEditArea) area).getHide();
            }else if (area instanceof CmsEditVersionArea){
                code = ((CmsEditVersionArea) area).getModuleCode();
                areaData = ((CmsEditVersionArea) area).getData();
                hide = ((CmsEditVersionArea) area).getHide();
            }
            Elements elements = document.select(CMS_HTML_EDIT_CLASS);
            if (null != elements && elements.size() > 0){
                for (Element element : elements){
                    if (code.equals(element.attr("code"))){
                        element.html(areaData);
                        if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(hide)){
                            element.remove();
                        }else{
                            element.html(areaData);
                        }
                    }
                }

            }
            Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
            if (null != imgArtiEles && imgArtiEles.size() > 0){
                for (Element element : imgArtiEles){
                    if (code.equals(element.attr("code"))){
                        if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(hide)){
                            element.remove();
                        }else{
                            element.html(areaData);
                        }
                    }
                }

            }
        }
        data = document.toString();
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
    @Override
    public String processOnlyEditData(String data){
        StringBuffer sb = new StringBuffer();
        int indexStart = data.indexOf(ONLYEDIT_START);
        int indexEnd = data.indexOf(ONLYEDIT_END);

        if (indexStart != -1 && indexEnd != -1){
            sb.append(data.substring(0, indexStart));
            sb.append(data.substring(indexEnd + ONLYEDIT_END.length(), data.length()));
            data = sb.toString();
            data = processOnlyEditData(data);
        }
        return data;
    }

    /**
     * 去掉不要加载的数据, 如:不要加载的js 去掉<!--noedit-start-->到<!--noedit-end-->中间的数据
     * 
     * @param Data
     * @return
     */
    @Override
    public String processNoEditData(String data){
        StringBuffer sb = new StringBuffer();
        int indexStart = data.indexOf(NOEDIT_START);
        int indexEnd = data.indexOf(NOEDIT_END);

        if (indexStart != -1 && indexEnd != -1){
            sb.append(data.substring(0, indexStart));
            sb.append(data.substring(indexEnd + NOEDIT_END.length(), data.length()));
            data = sb.toString();
            data = processNoEditData(data);
        }
        return data;
    }

    /**
     * 对于模块，去掉jsoup自动添加的<html></html><body></body>标签
     * 
     * @param data
     * @return
     */
    private String processExtraHtmlTag(String data){
        return data.replaceAll("<html [^>]*>", "").replace("</html>", "").replaceAll("<HTML [^>]*>", "").replace("</HTML>", "").replaceAll("<head [^>]*>", "").replace("</head>", "").replaceAll("<HEAD [^>]*>", "").replace("</HEAD>", "")
                        .replaceAll("<body [^>]*>", "").replace("</body>", "").replaceAll("<BODY [^>]*>", "").replace("</BODY>", "");
    }

    @Override
    public String getParsePageData(Long templateId,Long pageId,Long versionId){
        CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(templateId);
        String data = getTemplatePageData(templateId, pageId, versionId, false);
        if (template.getUseCommonHeader()){
            String resource = findResource(template.getData());
            data = resource + processBody(data);
        }
        return data;
    }

    @Override
    public String getTemplatePageData(Long templateId,Long pageId,Long versionId,Boolean isEdit){
        CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(templateId);

        String data = template.getData();

        if (StringUtils.isBlank(data)){
            return "";
        }

        if (null != pageId && (Validator.isNullOrEmpty(versionId) || versionId == 0)){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("pageId", pageId);
            List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);

            Document document = Jsoup.parse(data);
            for (CmsEditArea area : editAreaList){
                //html模式
                dealEditClass(document, area, CMS_HTML_EDIT_CLASS, isEdit);
                //图文模式
                dealEditClass(document, area, CMS_IMGARTICLE_EDIT_CLASS, isEdit);
                //商品模式
                dealEditClass(document, area, CMS_PRODUCT_EDIT_CLASS, isEdit);
            }
            CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(pageId);
            //处理seo信息
            sdkCmsPageInstanceManager.setSeoInfo(document, cmsPageInstance);
            data = document.toString();
        }else if (null != pageId && Validator.isNotNullOrEmpty(versionId) && versionId > 0){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            CmsPageInstanceVersion cmsPageInstanceVersion = sdkCmsPageInstanceVersionManager.getCmsPageInstanceVersionById(versionId);
            paraMap.put("versionId", versionId);
            paraMap.put("pageId", pageId);
            List<CmsEditVersionArea> editVersionAreaList = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);

            Document document = Jsoup.parse(data);
            for (CmsEditVersionArea area : editVersionAreaList){
                //html模式
                dealEditClass(document, area, CMS_HTML_EDIT_CLASS, isEdit);
                //图文模式
                dealEditClass(document, area, CMS_IMGARTICLE_EDIT_CLASS, isEdit);
                //商品模式
                dealEditClass(document, area, CMS_PRODUCT_EDIT_CLASS, isEdit);
            }
            CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(cmsPageInstanceVersion.getInstanceId());
            //处理seo信息
            sdkCmsPageInstanceManager.setSeoInfo(document, cmsPageInstance);
            data = document.toString();

        }
        data = sdkCmsPageTemplateManager.processTemplateBase(data);
        if (isEdit){
            data = processNoEditData(data);
        }else{
            data = processOnlyEditData(data);
        }
        System.out.println(data);
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public String getTemplateModuleData(Long templateId,Long moduleId,Long versionId,Boolean isEdit){

        CmsModuleTemplate template = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId);

        String data = template.getData();

        if (StringUtils.isBlank(data)){
            return "";
        }

        if (null != moduleId && Validator.isNullOrEmpty(versionId)){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("moduleId", moduleId);
            List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);

            Document document = Jsoup.parse(data);
            for (CmsEditArea area : editAreaList){
                String code = area.getModuleCode();
                Elements elements = document.select(CMS_HTML_EDIT_CLASS);
                if (null != elements && elements.size() > 0){
                    for (Element element : elements){
                        if (code.equals(element.attr("code"))){
                            //预览，处理隐藏
                            if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
                                element.remove();
                                //修改，处理隐藏
                            }else if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
                                element.attr("hide", "0");
                                element.html(area.getData());
                            }else{
                                element.html(area.getData());
                            }
                        }
                    }

                }
                Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
                if (null != imgArtiEles && imgArtiEles.size() > 0){
                    for (Element element : imgArtiEles){
                        if (code.equals(element.attr("code"))){
                            if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
                                element.remove();
                            }else if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
                                element.attr("hide", "0");
                                element.html(area.getData());
                            }else{
                                element.html(area.getData());
                            }
                        }
                    }

                }

                Elements proArtiEles = document.select(CMS_PRODUCT_EDIT_CLASS);
                if (null != proArtiEles && proArtiEles.size() > 0){
                    for (Element element : proArtiEles){
                        if (code.equals(element.attr("code"))){
                            element.html(area.getData());
                        }
                    }

                }

            }
            data = document.toString();
        }else if (null != moduleId && Validator.isNotNullOrEmpty(versionId) && versionId > 0){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("versionId", versionId);
            paraMap.put("moduleId", moduleId);
            List<CmsEditVersionArea> editAreaList = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);

            Document document = Jsoup.parse(data);
            for (CmsEditVersionArea area : editAreaList){
                String code = area.getModuleCode();
                Elements elements = document.select(CMS_HTML_EDIT_CLASS);
                if (null != elements && elements.size() > 0){
                    for (Element element : elements){
                        if (code.equals(element.attr("code"))){
                            //预览，处理隐藏
                            if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
                                element.remove();
                                //修改，处理隐藏
                            }else if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
                                element.attr("hide", "0");
                                element.html(area.getData());
                            }else{
                                element.html(area.getData());
                            }
                        }
                    }

                }
                Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
                if (null != imgArtiEles && imgArtiEles.size() > 0){
                    for (Element element : imgArtiEles){
                        if (code.equals(element.attr("code"))){
                            if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
                                element.remove();
                            }else if (CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
                                element.attr("hide", "0");
                                element.html(area.getData());
                            }else{
                                element.html(area.getData());
                            }
                        }
                    }

                }

                Elements proArtiEles = document.select(CMS_PRODUCT_EDIT_CLASS);
                if (null != proArtiEles && proArtiEles.size() > 0){
                    for (Element element : proArtiEles){
                        if (code.equals(element.attr("code"))){
                            element.html(area.getData());
                        }
                    }

                }

            }
            data = document.toString();
        }
        data = sdkCmsPageTemplateManager.processTemplateBase(data);
        if (isEdit){
            data = processNoEditData(data);
        }
        return data;

    }

    @Override
    public String findResource(String html){

        int index1 = html.indexOf(RESOURCE_START);

        int index2 = html.indexOf(RESOURCE_END);

        if (index1 != -1 && index2 != -1){
            html = html.substring(index1 + RESOURCE_START.length());

            index2 = html.indexOf(RESOURCE_END);
            if (index2 != -1){
                html = html.substring(0, index2);
            }

        }else{
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
    private String processBody(String html){

        if (StringUtils.isBlank(html))
            return "";

        int index = html.indexOf(BODY_START);

        if (index != -1){
            html = html.substring(index + BODY_START.length());
        }

        index = html.indexOf(BODY_END);

        if (index != -1){
            html = html.substring(0, index);
        }

        return html;

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
    private <T> void dealEditClass(Document document,T obj,String cls,boolean isEdit){
        String code = "";
        Integer hide = null;
        String data = "";
        if (obj instanceof CmsEditArea){
            CmsEditArea area = (CmsEditArea) obj;
            hide = area.getHide();
            data = area.getData();
            code = area.getCode();
        }else if (obj instanceof CmsEditVersionArea){
            CmsEditVersionArea area = (CmsEditVersionArea) obj;
            hide = area.getHide();
            data = area.getData();
            code = area.getCode();
        }

        Elements proEles = document.select(cls);
        if (null != proEles && proEles.size() > 0){
            for (Element element : proEles){
                if (code.equals(element.attr("code"))){
                    if (isEdit){
                        element.attr("hide", String.valueOf(hide));
                        element.html(data);
                        if (cls.equals(".cms-product-edit")){
                            sdkCmsPageInstanceManager.setProductInfo(element, isEdit);
                        }
                    }else{
                        if (hide != null && hide == 0){
                            element.remove();
                        }else{
                            element.html(data);
                            if (cls.equals(".cms-product-edit")){
                                sdkCmsPageInstanceManager.setProductInfo(element, isEdit);
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 处理页面的html, 获取code与html
     * 
     * @param html
     * @return
     */
    @Override
    public Map<String, String> processPageHtml(String html,int pageType){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("the param html:{}", html);
        }

        /** key:areaCode, value:html */
        Map<String, String> pageAreaMap = new HashMap<>();
        Document document = Jsoup.parse(html);
        // 去掉 "编辑"按钮的div
        Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
        editButtonElements.remove();
        Elements editHideButtonElements = document.select(".wui-tips-shade");
        editHideButtonElements.remove();

        //---------------------------------------------------------------------
        //编辑html模式
        dealHtml(pageAreaMap, document, CMS_HTML_EDIT_CLASS, pageType);
        //图文模式
        dealHtml(pageAreaMap, document, CMS_IMGARTICLE_EDIT_CLASS, pageType);
        //商品模式
        dealHtml(pageAreaMap, document, CMS_PRODUCT_EDIT_CLASS, pageType);

        //---------------------------------------------------------------------

        return pageAreaMap;
    }

    /**
     * 解析并获取html每个编辑单元的内容
     * 
     * @param pageAreaMap
     *            存放每个编辑单元内容
     * @param document
     *            编辑文档
     * @param cls
     *            编辑元素类型
     * @param pageType
     *            页面类型（0:页面， 1：模块）
     * @return
     */
    private void dealHtml(Map<String, String> pageAreaMap,Document document,String cls,int pageType){
        Integer i = 1;
        Elements elements = document.select(cls);
        for (Element element : elements){
            String areaCode = element.attr("code");
            if (StringUtils.isBlank(areaCode)){
                if (pageType == 0){
                    throw new BusinessException("第" + i + "个可编辑的区域,不能找到code属性");
                }else if (pageType == 1){
                    throw new BusinessException("模块编辑区域的code不存在 ");
                }
            }
            String areaHtml = element.html();
            pageAreaMap.put(areaCode, cmsHtmlResolver.resolver(areaHtml));
            i++;
        }
    }

    /**
     * 处理页面的html, 获取code与html
     * 
     * @param html
     * @return
     */
    @Override
    public Map<String, String> processResetPageHtml(String html){
        /** key:areaCode, value:html */
        Map<String, String> pageAreaMap = new HashMap<String, String>();
        Document document = Jsoup.parse(html);
        // 去掉 "编辑"按钮的div
        Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
        editButtonElements.remove();
        //编辑html模式
        Elements elements = document.select(CMS_HTML_EDIT_CLASS);
        dealHtmlReset(pageAreaMap, elements, "cms-html-edit");
        //图文模式
        Elements imgElements = document.select(CMS_IMGARTICLE_EDIT_CLASS);
        dealHtmlReset(pageAreaMap, imgElements, "cms-imgarticle-edit");

        return pageAreaMap;
    }

    /**
     * 将对应编辑区域的模板元素内容放入到map
     * 
     * @param pageAreaMap
     * @param elements
     * @param cls
     */
    private void dealHtmlReset(Map<String, String> pageAreaMap,Elements elements,String cls){
        Integer i = 1;
        for (Element element : elements){
            String areaCode = element.attr("code");
            if (StringUtils.isBlank(areaCode)){
                throw new BusinessException("模块编辑区域的code不存在");
            }
            String areaHtml = element.html();
            pageAreaMap.put(areaCode, cls + "EDIT_CLASS_SEP" + cmsHtmlResolver.resolver(areaHtml));
            i++;
        }
    }
}
