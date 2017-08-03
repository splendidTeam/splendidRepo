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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsPageTemplateDao;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * CmsPageTemplateManager
 * 
 * @author Justin
 */
@Transactional
@Service("sdkCmsPageTemplateManager")
public class SdkCmsPageTemplateManagerImpl implements SdkCmsPageTemplateManager{

    private final static Logger LOGGER = LoggerFactory.getLogger(SdkCmsPageTemplateManagerImpl.class);

    /**
     * 上传图片的域名
     */
    @Value("#{meta['upload.img.domain.base']}")
    private String UPLOAD_IMG_DOMAIN = "";

    /** 静态base标识. */
    private final static String STATIC_BASE_CHAR = "#{staticbase}";

    /** 图片base标识. */
    private final static String IMG_BASE_CHAR = "#{imgbase}";

    /** 页面base标识. */
    private final static String PAGE_BASE_CHAR = "#{pagebase}";

    @Autowired
    private CmsPageTemplateDao cmsPageTemplateDao;

    @Override
    public String processTemplateBase(String html){
        if (StringUtils.isBlank(html)){
            return "";
        }
        Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");

        String pagebase = StringUtils.trim(properties.getProperty("page.base"));

        String staticbase = StringUtils.trim(properties.getProperty("static.domain.base"));

        String imgbase = UPLOAD_IMG_DOMAIN;
        LOGGER.info("pagebase:[{}], staticbase:[{}],imgbase:[{}]", pagebase, staticbase, imgbase);

        if (StringUtils.isBlank(pagebase)){
            pagebase = "";
        }else if ("/".equals(pagebase)){
            pagebase = "";
        }else if (pagebase.endsWith("/")){
            pagebase = pagebase.substring(0, pagebase.length() - 1);
        }

        if (StringUtils.isBlank(staticbase)){
            staticbase = "";
        }else if ("/".equals(staticbase)){
            staticbase = "";
        }else if (staticbase.endsWith("/")){
            staticbase = staticbase.substring(0, staticbase.length() - 1);
        }

        if (StringUtils.isBlank(imgbase)){
            imgbase = "";
        }else if ("/".equals(imgbase)){
            imgbase = "";
        }else if (imgbase.endsWith("/")){
            imgbase = imgbase.substring(0, imgbase.length() - 1);
        }
        html = html.replace(PAGE_BASE_CHAR, pagebase);
        html = html.replace(STATIC_BASE_CHAR, staticbase);
        html = html.replace(IMG_BASE_CHAR, imgbase);
        return html;
    }

    //---------------------------------------------------------------------

    /**
     * 保存CmsPageTemplate
     * 
     */
    @Override
    public CmsPageTemplate saveCmsPageTemplate(CmsPageTemplate model){
        model.setCreateTime(new Date());
        model.setLifecycle(BaseModel.LIFECYCLE_ENABLE);
        return cmsPageTemplateDao.save(model);
    }

    /**
     * 通过id获取CmsPageTemplate
     * 
     */
    @Override
    @Transactional(readOnly = true)
    public CmsPageTemplate findCmsPageTemplateById(Long id){
        return cmsPageTemplateDao.getByPrimaryKey(id);
    }

    /**
     * 获取所有CmsPageTemplate列表
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CmsPageTemplate> findAllCmsPageTemplateList(){
        return cmsPageTemplateDao.findAllCmsPageTemplateList();
    };

    /**
     * 通过ids获取CmsPageTemplate列表
     * 
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CmsPageTemplate> findCmsPageTemplateListByIds(List<Long> ids){
        return cmsPageTemplateDao.findCmsPageTemplateListByIds(ids);
    };

    /**
     * 通过参数map获取CmsPageTemplate列表
     * 
     * @param paraMap
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CmsPageTemplate> findCmsPageTemplateListByQueryMap(Map<String, Object> paraMap){
        return cmsPageTemplateDao.findCmsPageTemplateListByQueryMap(paraMap);
    };

    /**
     * 分页获取CmsPageTemplate列表
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CmsPageTemplate> findCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return cmsPageTemplateDao.findCmsPageTemplateListByQueryMapWithPage(page, sorts, paraMap);
    };

    /**
     * 通过ids批量启用或禁用CmsPageTemplate
     * 设置lifecycle =0 或 1
     * 
     * @param ids
     * @return
     */
    @Override
    public void enableOrDisableCmsPageTemplateByIds(List<Long> ids,Integer state){
        cmsPageTemplateDao.enableOrDisableCmsPageTemplateByIds(ids, state);
    }

    /**
     * 通过ids批量删除CmsPageTemplate
     * 设置lifecycle =2
     * 
     * @param ids
     * @return
     */
    @Override
    public void removeCmsPageTemplateByIds(List<Long> ids){
        cmsPageTemplateDao.removeCmsPageTemplateByIds(ids);
    }

    /**
     * 获取有效的CmsPageTemplate列表
     * lifecycle =1
     * 
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CmsPageTemplate> findAllEffectCmsPageTemplateList(){
        return cmsPageTemplateDao.findAllEffectCmsPageTemplateList();
    };

    /**
     * 通过参数map获取有效的CmsPageTemplate列表
     * 强制加上lifecycle =1
     * 
     * @param paraMap
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMap(Map<String, Object> paraMap){
        return cmsPageTemplateDao.findEffectCmsPageTemplateListByQueryMap(paraMap);
    };

    /**
     * 分页获取有效的CmsPageTemplate列表
     * 强制加上lifecycle =1
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return cmsPageTemplateDao.findEffectCmsPageTemplateListByQueryMapWithPage(page, sorts, paraMap);
    }
}
