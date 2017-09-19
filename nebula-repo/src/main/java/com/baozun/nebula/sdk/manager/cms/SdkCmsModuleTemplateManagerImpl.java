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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsModuleTemplateDao;
import com.baozun.nebula.model.cms.CmsModuleTemplate;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * CmsModuleTemplateManager
 * 
 * @author 何波
 *
 */
@Transactional
@Service("sdkCmsModuleTemplateManager")
public class SdkCmsModuleTemplateManagerImpl implements SdkCmsModuleTemplateManager{

    @Autowired
    private CmsModuleTemplateDao cmsModuleTemplateDao;

    /**
     * 保存CmsModuleTemplate
     * 
     */
    public CmsModuleTemplate saveCmsModuleTemplate(CmsModuleTemplate cmsModuleTemplate){
        Long id = cmsModuleTemplate.getId();
        if (id != null){
            CmsModuleTemplate db = cmsModuleTemplateDao.getByPrimaryKey(id);
            if (cmsModuleTemplate.getData() != null && cmsModuleTemplate.getData() != ""){
                db.setData(cmsModuleTemplate.getData());
            }
            db.setImg(cmsModuleTemplate.getImg());
            db.setName(cmsModuleTemplate.getName());
            db.setVersion(new Date());
            return cmsModuleTemplateDao.save(db);
        }
        cmsModuleTemplate.setCreateTime(new Date());
        cmsModuleTemplate.setLifecycle(1);
        cmsModuleTemplate.setVersion(new Date());
        return cmsModuleTemplateDao.save(cmsModuleTemplate);
    }

    /**
     * 模块模板修改
     * 
     */
    public CmsModuleTemplate editCmsModuleTemplate(CmsModuleTemplate model){
        CmsModuleTemplate db = cmsModuleTemplateDao.getByPrimaryKey(model.getId());
        if (model.getData() != null && model.getData() != ""){
            //<html>标签之前的内容会被js删掉，所以此处从old里边取出重新添加在新的html之前
            String newData = model.getData();
            String oldData = db.getData();
            if (oldData != null && oldData.indexOf("<html") > newData.indexOf("<html")){
                newData = oldData.substring(0, oldData.indexOf("<html")).concat(newData);
            }

            db.setData(newData);
        }
        return cmsModuleTemplateDao.save(db);
    }

    /**
     * 通过id获取CmsModuleTemplate
     * 
     */
    @Transactional(readOnly = true)
    public CmsModuleTemplate findCmsModuleTemplateById(Long id){

        return cmsModuleTemplateDao.getByPrimaryKey(id);
    }

    /**
     * 获取所有CmsModuleTemplate列表
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleTemplate> findAllCmsModuleTemplateList(){

        return cmsModuleTemplateDao.findAllCmsModuleTemplateList();
    };

    /**
     * 通过ids获取CmsModuleTemplate列表
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleTemplate> findCmsModuleTemplateListByIds(List<Long> ids){

        return cmsModuleTemplateDao.findCmsModuleTemplateListByIds(ids);
    };

    /**
     * 通过参数map获取CmsModuleTemplate列表
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleTemplate> findCmsModuleTemplateListByQueryMap(Map<String, Object> paraMap){

        return cmsModuleTemplateDao.findCmsModuleTemplateListByQueryMap(paraMap);
    };

    /**
     * 分页获取CmsModuleTemplate列表
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<CmsModuleTemplate> findCmsModuleTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){

        return cmsModuleTemplateDao.findCmsModuleTemplateListByQueryMapWithPage(page, sorts, paraMap);
    };

    /**
     * 通过ids批量启用或禁用CmsModuleTemplate
     * 设置lifecycle =0 或 1
     * 
     * @param ids
     * @return
     */
    public void enableOrDisableCmsModuleTemplateByIds(List<Long> ids,Integer state){
        cmsModuleTemplateDao.enableOrDisableCmsModuleTemplateByIds(ids, state);
    }

    /**
     * 通过ids批量删除CmsModuleTemplate
     * 设置lifecycle =2
     * 
     * @param ids
     * @return
     */
    public void removeCmsModuleTemplateByIds(List<Long> ids){
        cmsModuleTemplateDao.removeCmsModuleTemplateByIds(ids);
    }

    /**
     * 获取有效的CmsModuleTemplate列表
     * lifecycle =1
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleTemplate> findAllEffectCmsModuleTemplateList(){

        return cmsModuleTemplateDao.findAllEffectCmsModuleTemplateList();
    };

    /**
     * 通过参数map获取有效的CmsModuleTemplate列表
     * 强制加上lifecycle =1
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleTemplate> findEffectCmsModuleTemplateListByQueryMap(Map<String, Object> paraMap){

        return cmsModuleTemplateDao.findEffectCmsModuleTemplateListByQueryMap(paraMap);
    };

    /**
     * 分页获取有效的CmsModuleTemplate列表
     * 强制加上lifecycle =1
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<CmsModuleTemplate> findEffectCmsModuleTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){

        return cmsModuleTemplateDao.findEffectCmsModuleTemplateListByQueryMapWithPage(page, sorts, paraMap);
    };

}
