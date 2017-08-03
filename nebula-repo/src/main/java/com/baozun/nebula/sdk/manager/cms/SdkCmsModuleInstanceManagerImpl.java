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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.curator.ZKWatchPath;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.ModuleMapWatchInvoke;
import com.baozun.nebula.dao.cms.CmsModuleInstanceDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.constants.Constants;
import com.feilong.core.Validator;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * CmsModuleInstanceManager
 * 
 * @author 何波
 *
 */
@Transactional
@Service("sdkCmsModuleInstanceManager")
public class SdkCmsModuleInstanceManagerImpl implements SdkCmsModuleInstanceManager{

    private static Logger LOGGER = LoggerFactory.getLogger(SdkCmsModuleInstanceManagerImpl.class);

    public final static Integer PULISHED = 1;

    @Autowired
    private CmsModuleInstanceDao cmsModuleInstanceDao;

    @Autowired
    private SdkCmsModuleTemplateManager sdkCmsModuleTemplateManager;

    @Autowired
    private SdkCmsEditAreaManager sdkCmsEditAreaManager;

    @Autowired
    private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;

    @Autowired
    private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;

    @Autowired
    private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;

    @Autowired
    private SdkCmsPublishedManager sdkCmsPublishedManager;

    @Autowired
    private ZkOperator zkOperator;

    @Autowired(required = false)
    private ZKWatchPath zkWatchPath;

    @Autowired
    private SdkCmsModuleInstanceVersionManager sdkCmsModuleInstanceVersionManager;

    @Autowired
    private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;

    public static Map<String, CmsTemplateHtml> moduleMap = new HashMap<>();

    @Override
    @Transactional(readOnly = true)
    public void loadModuleMap(){
        Map<String, CmsTemplateHtml> swapModuleMap = new HashMap<>(moduleMap.size());
        /**
         * 1、获取所有的发布的模块
         * 2、将发布模块的版本放入到缓存中
         * 3、清空发布模块的缓存 !!!!!不能清空!!!! added by D.C 2017/7/22
         * 4、将当前发布模块的内容放入到缓存中
         */
        Map<String, Object> param = toMap("isPublished", (Object) true);

        List<CmsModuleInstance> moduleList = cmsModuleInstanceDao.findEffectCmsModuleInstanceListByQueryMap(param);
        Map<String, Object> moduleparam = new HashMap<>();
        //moduleMap.clear(); 老的数据不能清空，清空后会造成短时的访问空白
        sdkCmsModuleInstanceVersionManager.setPublicModuleVersionCacheInfo();

        for (CmsModuleInstance cmsModuleInstance : moduleList){
            String moduleCode = cmsModuleInstance.getCode();
            moduleparam.put("startTimeEnd", new Date());
            moduleparam.put("endTimeStart", new Date());
            moduleparam.put("moduleCode", moduleCode);
            List<CmsTemplateHtml> publisingTemplate = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlListByQueryMap(moduleparam);

            if (isNotNullOrEmpty(publisingTemplate) && publisingTemplate.size() == 1){
                swapModuleMap.put(moduleCode, publisingTemplate.get(0));
            }else{

                Map<String, Object> baseParam = new HashMap<>();
                baseParam.put("moduleCode", moduleCode);
                baseParam.put("versionId", -1);
                publisingTemplate = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlListByQueryMap(baseParam);

                if (isNotNullOrEmpty(publisingTemplate) && publisingTemplate.size() == 1){
                    swapModuleMap.put(moduleCode, publisingTemplate.get(0));
                }
            }
        }
        moduleMap = swapModuleMap;
    }

    /**
     * 保存CmsModuleInstance
     * 
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CmsModuleInstance saveCmsModuleInstance(CmsModuleInstance model){
        return cmsModuleInstanceDao.save(model);
    }

    /**
     * 通过id获取CmsModuleInstance
     * 
     */
    @Transactional(readOnly = true)
    public CmsModuleInstance findCmsModuleInstanceById(Long id){
        return cmsModuleInstanceDao.getByPrimaryKey(id);
    }

    /**
     * 获取所有CmsModuleInstance列表
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleInstance> findAllCmsModuleInstanceList(){
        return cmsModuleInstanceDao.findAllCmsModuleInstanceList();
    };

    /**
     * 通过ids获取CmsModuleInstance列表
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleInstance> findCmsModuleInstanceListByIds(List<Long> ids){
        return cmsModuleInstanceDao.findCmsModuleInstanceListByIds(ids);
    };

    /**
     * 通过参数map获取CmsModuleInstance列表
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleInstance> findCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap){
        return cmsModuleInstanceDao.findCmsModuleInstanceListByQueryMap(paraMap);
    };

    /**
     * 分页获取CmsModuleInstance列表
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<CmsModuleInstance> findCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        if (paraMap.containsKey("ispublished")){
            Integer isPublished = (Integer) paraMap.get("ispublished");
            if (PULISHED.equals(isPublished)){
                paraMap.put("ispublished", true);
            }else{
                paraMap.put("ispublished", false);
            }
        }
        return cmsModuleInstanceDao.findCmsModuleInstanceListByQueryMapWithPage(page, sorts, paraMap);
    };

    /**
     * 通过ids批量启用或禁用CmsModuleInstance
     * 设置lifecycle =0 或 1
     * 
     * @param ids
     * @return
     */
    public void enableOrDisableCmsModuleInstanceByIds(List<Long> ids,Integer state){
        cmsModuleInstanceDao.enableOrDisableCmsModuleInstanceByIds(ids, state);
    }

    /**
     * 通过ids批量删除CmsModuleInstance
     * 设置lifecycle =2
     * 
     * @param ids
     * @return
     */
    public void removeCmsModuleInstanceByIds(List<Long> ids){
        List<CmsModuleInstance> cmsModuleInstances = findCmsModuleInstanceListByIds(ids);
        //List<String> removeCodes = new ArrayList<String>();
        String removeids = "";
        for (CmsModuleInstance cmsModuleInstance : cmsModuleInstances){
            sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(cmsModuleInstance.getCode());
            sdkCmsTemplateHtmlManager.removeCmsTemplateHtmlByModuleCode(cmsModuleInstance.getCode());
            removeids += cmsModuleInstance.getId();
        }
        sdkCmsModuleInstanceVersionManager.removeModuleVersionByModuleIds(ids);
        // 先删除, 再添加
        cmsModuleInstanceDao.removeCmsModuleInstanceByIds(ids);
        LOGGER.info("remove module Success, module's id is " + removeids);
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(ModuleMapWatchInvoke.class));
    }

    /**
     * 获取有效的CmsModuleInstance列表
     * lifecycle =1
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleInstance> findAllEffectCmsModuleInstanceList(){
        return cmsModuleInstanceDao.findAllEffectCmsModuleInstanceList();
    };

    /**
     * 通过参数map获取有效的CmsModuleInstance列表
     * 强制加上lifecycle =1
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMap(Map<String, Object> paraMap){
        return cmsModuleInstanceDao.findEffectCmsModuleInstanceListByQueryMap(paraMap);
    };

    /**
     * 分页获取有效的CmsModuleInstance列表
     * 强制加上lifecycle =1
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<CmsModuleInstance> findEffectCmsModuleInstanceListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return cmsModuleInstanceDao.findEffectCmsModuleInstanceListByQueryMapWithPage(page, sorts, paraMap);
    }

    @Override
    public CmsModuleInstance createOrUpdateModuleInstance(CmsModuleInstance cmsModuleInstance,String html){
        Long id = cmsModuleInstance.getId();

        checkPageInstanceCode(cmsModuleInstance, id);

        CmsModuleInstance instance = saveCmsModuleInstance(cmsModuleInstance, id);
        

        //---------------------------------------------------------------------

        Map<String, String> editAreaMap = sdkCmsParseHtmlContentManager.processPageHtml(html, Constants.CMS_MODULE);
        Long moduleId = instance.getId();
        // 保存页面编辑区域
        for (Map.Entry<String, String> entry : editAreaMap.entrySet()){
            //根据moduleCode查询
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("moduleCode", entry.getKey());
            paraMap.put("moduleTemplateId", instance.getTemplateId());
            paraMap.put("moduleId", moduleId);
            List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
            //修改
            if (isNotNullOrEmpty(cmsEditAreas)){
                CmsEditArea cmsEditArea = sdkCmsEditAreaManager.findCmsEditAreaById(cmsEditAreas.get(0).getId());
                cmsEditArea.setData(entry.getValue());
                cmsEditArea.setModifyTime(new Date());
                sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
            }else{
                //新增
                CmsEditArea cmsEditArea = new CmsEditArea();
                cmsEditArea.setModuleCode(entry.getKey());
                cmsEditArea.setData(entry.getValue());
                cmsEditArea.setLifecycle(CmsEditArea.LIFECYCLE_ENABLE);
                cmsEditArea.setModuleId(moduleId);
                cmsEditArea.setModuleTemplateId(instance.getTemplateId());
                cmsEditArea.setCreateTime(new Date());

                sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
            }
        }
        LOGGER.info("save Success, module's id is [{}], code is [{}]", instance.getId(), instance.getCode());
        return instance;
    }

    /**
     * @param cmsModuleInstance
     * @param id
     * @return
     * @since 5.3.2.22
     */
    private CmsModuleInstance saveCmsModuleInstance(CmsModuleInstance cmsModuleInstance,Long id){
        //---------------------------------------------------------------------
        /** 保存页面实例 */
        CmsModuleInstance instance = null;
        if (null != id){
            // 修改
            CmsModuleInstance dbModule = cmsModuleInstanceDao.getByPrimaryKey(id);
            dbModule.setCode(cmsModuleInstance.getCode());
            dbModule.setName(cmsModuleInstance.getName());
            dbModule.setTemplateId(cmsModuleInstance.getTemplateId());
            dbModule.setModifyTime(new Date());
            instance = cmsModuleInstanceDao.save(dbModule);
        }else{
            // 保存
            cmsModuleInstance.setCreateTime(new Date());
            cmsModuleInstance.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
            cmsModuleInstance.setIsPublished(false);
            instance = cmsModuleInstanceDao.save(cmsModuleInstance);
        }
        return instance;
    }

    @Transactional(readOnly = true)
    private void checkPageInstanceCode(CmsModuleInstance cmsModuleInstance,Long id){
        // 检察 页面编码, url 是否已存在
        Map<String, Object> paraMap = new HashMap<>();
        if (null != id){
            paraMap.put("id", id);
        }
        paraMap.put("code", cmsModuleInstance.getCode());
        List<CmsModuleInstance> cmis = cmsModuleInstanceDao.checkModuleInstanceCode(paraMap);
        if (cmis != null && cmis.size() > 0){
            throw new BusinessException("模块实例编码已存在 ");
        }

    }

    /**
     * 模块实例发布
     */
    @Override
    public void publishModuleInstance(Long moduleId){
        /**
         * 模块实例发布步骤
         * 1、获取模块实例
         * 2、从发布信息表中删除已发布的该模块信息（根据code）
         * 3、将该模块的区域元素信息同步到发布信息表中
         * 4、更改模块的发布状态
         * 5、zk通知模块已被发布（这里是重新将发布模块的信息读取到缓存中）
         */
        CmsModuleInstance moduleInstance = cmsModuleInstanceDao.getByPrimaryKey(moduleId);
        String moduleCode = moduleInstance.getCode();
        // 先删除, 再添加
        sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleCode);

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("moduleId", moduleInstance.getId());
        List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
        List<CmsPublished> CmsPublishedList = new ArrayList<CmsPublished>();
        for (CmsEditArea cmsEditArea : editAreaList){
            CmsPublished cmsPublished = new CmsPublished();
            cmsPublished.setAreaCode(cmsEditArea.getModuleCode());
            cmsPublished.setModuleCode(moduleCode);
            cmsPublished.setHide(cmsEditArea.getHide());
            cmsPublished.setData(cmsEditArea.getData());
            cmsPublished.setPublishTime(new Date());
            CmsPublished newCmsPublished = sdkCmsPublishedManager.saveCmsPublished(cmsPublished);
            CmsPublishedList.add(newCmsPublished);
        }
        // 修改成已发布状态
        moduleInstance.setIsPublished(true);
        cmsModuleInstanceDao.save(moduleInstance);

        CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByModuleCodeAndVersionId(moduleCode, -1L);
        if (Validator.isNullOrEmpty(cmsTemplateHtml)){
            cmsTemplateHtml = new CmsTemplateHtml();
            cmsTemplateHtml.setCreateTime(new Date());
            cmsTemplateHtml.setModuleCode(moduleCode);
            //基础版本的版本号设置为-1
            cmsTemplateHtml.setVersionId(-1L);
        }
        cmsTemplateHtml.setLifecycle(1);
        String data = sdkCmsParseHtmlContentManager.getParseModuleData(CmsPublishedList, moduleInstance.getTemplateId());
        cmsTemplateHtml.setData(data);
        sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
        LOGGER.info("publishModuleInstance Success, module's id is " + moduleInstance.getId() + ", code is " + moduleInstance.getCode());
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(ModuleMapWatchInvoke.class));

    }

    /**
     * 取消发布模块
     */
    @Override
    public void cancelPublishedModuleInstance(Long moduleId){
        CmsModuleInstance moduleInstance = cmsModuleInstanceDao.getByPrimaryKey(moduleId);
        // 先删除, 再添加
        sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleInstance.getCode());
        moduleInstance.setIsPublished(false);
        cmsModuleInstanceDao.save(moduleInstance);
        sdkCmsModuleInstanceVersionManager.cancelInstanceVersionInModuleId(moduleInstance.getId());
        sdkCmsTemplateHtmlManager.removeCmsTemplateHtmlByModuleCode(moduleInstance.getCode());
        LOGGER.info("cancelpublishModuleInstance success, module's id is " + moduleId + ", code is " + moduleInstance.getCode());
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(ModuleMapWatchInvoke.class));
    }

    @Override
    public String recoverTemplateCodeArea(Long templateId,Long versionId,String code){
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("moduleTemplateId", templateId);
        paraMap.put("moduleCode", code);
        paraMap.put("versionId", versionId);
        if (Validator.isNotNullOrEmpty(versionId)){
            List<CmsEditVersionArea> cmsEditVersionAreas = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);
            if (cmsEditVersionAreas == null || cmsEditVersionAreas.size() == 0){
                throw new BusinessException("编辑部分无需重置");
            }
        }else{
            List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
            if (cmsEditAreas == null || cmsEditAreas.size() == 0){
                throw new BusinessException("编辑部分无需重置");
            }
        }
        //根据code在模板中找出需要还原的数据
        String html = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId).getData();
        Map<String, String> map = sdkCmsParseHtmlContentManager.processResetPageHtml(html);
        if (map.size() == 0){
            throw new BusinessException("编辑部分无需重置");
        }
        String data = map.get(code);
        Document document = Jsoup.parse(data);
        Elements eles = document.getElementsByTag("body");
        if (eles != null && eles.size() != 0){
            data = eles.get(0).html();
        }
        //根据模板id删除编辑区域数据
        if (Validator.isNotNullOrEmpty(versionId)){
            sdkCmsEditVersionAreaManager.removeCmsEditVersionAreaByTemplateIdAndModuleCode(templateId, code, versionId);
        }else{
            sdkCmsEditAreaManager.removeCmsModuleEditAreaByTemplateId(templateId, code);
        }
        data = sdkCmsPageTemplateManager.processTemplateBase(data);
        return data;
    }

    @Override
    public Map<String, CmsTemplateHtml> getModuleMap(){

        if (moduleMap.size() == 0){
            loadModuleMap();
        }
        return moduleMap;
    }

    @Override
    public List<CmsModuleInstance> findCmsModuleInstanceListByTemplateIds(List<Long> ids){
        return cmsModuleInstanceDao.findCmsModuleInstanceListByTemplateIds(ids);
    }

}
