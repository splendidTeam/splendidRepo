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
package com.baozun.nebula.web.controller.cms;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsModuleInstanceVersion;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceVersionManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * CmsModuleInstanceController
 * 
 * @author 何波
 * 
 */
@Controller
public class CmsModuleInstanceVersionController extends BaseController{

    @Autowired
    private SdkCmsModuleInstanceManager cmsModuleInstanceManager;

    @Autowired
    private SdkCmsModuleInstanceVersionManager cmsModuleInstanceVersionManager;

    @Autowired
    private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;

    private static Logger log = LoggerFactory.getLogger(CmsModuleInstanceVersionController.class);

    @RequestMapping("/cmsModuleInstanceVersion/list.htm")
    public String list(Model model,@RequestParam("moduleId") Long moduleId){
        CmsModuleInstance cmi = cmsModuleInstanceManager.findCmsModuleInstanceById(moduleId);
        model.addAttribute("cmi", cmi);
        return "/cms/module/cmsModuleInstanceVersion";
    }

    /**
     * 保存CmsModuleInstance
     * 
     */
    @RequestMapping("/cmsModuleInstanceVersion/save.json")
    @ResponseBody
    public BackWarnEntity saveCmsModuleInstance(CmsModuleInstance model){
        cmsModuleInstanceManager.saveCmsModuleInstance(model);
        return SUCCESS;
    }

    /**
     * 通过id获取CmsModuleInstance
     * 
     */
    @RequestMapping("/cmsModuleInstanceVersion/findByid.json")
    @ResponseBody
    public CmsModuleInstance findCmsModuleInstanceById(Long id){

        return cmsModuleInstanceManager.findCmsModuleInstanceById(id);
    }

    /**
     * 获取所有CmsModuleInstance列表
     * 
     * @return
     */
    @RequestMapping("/cmsModuleInstanceVersion/findAll.json")
    @ResponseBody
    public List<CmsModuleInstance> findAllCmsModuleInstanceList(){

        return cmsModuleInstanceManager.findAllCmsModuleInstanceList();
    };

    /**
     * 通过ids获取CmsModuleInstance列表
     * 
     * @param ids
     * @return
     */
    @RequestMapping("/cmsModuleInstanceVersion/findByIds.json")
    @ResponseBody
    public List<CmsModuleInstance> findCmsModuleInstanceListByIds(Long[] ids){

        return cmsModuleInstanceManager.findCmsModuleInstanceListByIds(Arrays.asList(ids));
    };

    /**
     * @Description: 分页获取CmsModuleInstance列表
     * @param queryBean
     * @return Pagination<CmsModuleInstance>
     * @throws
     */
    @RequestMapping("/cmsModuleInstanceVersion/page.json")
    @ResponseBody
    public Pagination<CmsModuleInstanceVersion> findCmsModuleInstanceListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean){
        Sort[] sorts = queryBean.getSorts();
        if (null == sorts || sorts.length == 0){
            Sort sort = new Sort("version", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }
        return cmsModuleInstanceVersionManager.findCmsModuleInstanceVersionListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
    }

    /**
     * 通过ids批量删除CmsModuleInstance 设置lifecycle =2
     * 
     * @param ids
     * @return
     */
    @RequestMapping("/cmsModuleInstanceVersion/removeByIds.json")
    @ResponseBody
    public BackWarnEntity removeCmsModuleInstanceByIds(Long[] ids){
        cmsModuleInstanceVersionManager.removeCmsModuleInstanceVersionPublishByIds(Arrays.asList(ids));
        return SUCCESS;
    }

    @RequestMapping("/cmsModuleInstanceVersion/addCmsModuleInstanceVersion.htm")
    public String addCmsModuleInstance(Model model,@RequestParam("moduleId") Long moduleId){

        CmsModuleInstance cmi = cmsModuleInstanceManager.findCmsModuleInstanceById(moduleId);
        model.addAttribute("cmi", cmi);
        model.addAttribute("isUpdate", false);
        return "/cms/module/module-instance-version-add";
    }

    /**
     * 跳转到修改页面
     * 
     * @param model
     * @param pageId
     * @return
     */
    @RequestMapping("/cmsModuleInstanceVersion/updateModuleInstanceVersion.htm")
    public String updatePageInstance(Model model,@RequestParam("versionId") Long versionId){

        CmsModuleInstanceVersion cmiv = cmsModuleInstanceVersionManager.findCmsModuleInstanceVersionById(versionId);
        CmsModuleInstance cmi = null;
        if (Validator.isNotNullOrEmpty(cmiv)){
            cmi = cmsModuleInstanceManager.findCmsModuleInstanceById(cmiv.getInstanceId());
        }
        model.addAttribute("cmi", cmi);
        model.addAttribute("cmiv", cmiv);
        model.addAttribute("isUpdate", true);
        return "/cms/module/module-instance-version-add";
    }

    /**
     * 查询模板与编辑数据
     * 
     * @param model
     * @param templateId
     * @return
     */
    @RequestMapping("/module/findTemplateModuleVersionAreaByTemplateId.htm")
    public String findTemplateModuleAreaByTemplateId(
                    Model model,
                    @RequestParam("templateId") Long templateId,
                    @RequestParam(value = "moduleId") Long moduleId,
                    @RequestParam(value = "versionId",required = false) Long versionId,
                    @RequestParam("isEdit") Boolean isEdit){
        /** 模板信息 */
        if (Validator.isNullOrEmpty(versionId)){
            versionId = 0L;
        }
        String data = sdkCmsParseHtmlContentManager.getTemplateModuleData(templateId, moduleId, versionId, isEdit);
        model.addAttribute("data", data);
        return "/cms/newpage/page-instance-iframe";
    }

    /**
     * 保存模板页面实例
     * 
     * @param cmsPageInstance
     * @return
     */
    @RequestMapping("/module/saveModuleInstanceVersion.json")
    @ResponseBody
    public BackWarnEntity saveModuleInstance(@ModelAttribute CmsModuleInstanceVersion cmsModuleInstanceVersion,@RequestParam("html") String html){
        BackWarnEntity back = new BackWarnEntity();
        try{
            CmsModuleInstanceVersion version = cmsModuleInstanceVersionManager.createOrUpdateModuleInstanceVersion(cmsModuleInstanceVersion, html);
            back.setDescription(version);
            back.setIsSuccess(true);
        }catch (BusinessException e){
            back.setDescription(e.getMessage());
            back.setIsSuccess(false);
        }
        return back;
    }

    /**
     * 发布模块实例
     * 
     * @param pageId
     * @return
     */
    @RequestMapping("/module/publishModuleInstanceVersion.json")
    @ResponseBody
    public Object publishModuleInstance(@RequestParam("versionId") Long versionId,@RequestParam(value = "startTime",required = false) Date startTime,@RequestParam(value = "endTime",required = false) Date endTime){
        log.debug("published module version id is {} operator is " + this.getUserDetails().getUsername() + ", operatorId is " + this.getUserDetails().getUserId(), versionId);
        BackWarnEntity back = new BackWarnEntity();
        if (startTime == null && endTime != null){
            if (endTime.compareTo(new Date()) <= 0){
                back.setDescription("结束时间应大于当前时间");
                return back;
            }
        }
        if (startTime != null && endTime != null){
            if (startTime.compareTo(endTime) >= 0){
                back.setDescription("开始时应大于间结束时间");
                return back;
            }
        }
        BackWarnEntity bwe = new BackWarnEntity(true, Constants.CMS_PUBLISH_SUCCESS);
        try{
            cmsModuleInstanceVersionManager.publishModuleInstanceVersion(versionId, startTime, endTime);
            cmsModuleInstanceVersionManager.setPublicModuleVersionCacheInfo();
        }catch (BusinessException e){
            bwe.setIsSuccess(false);
            bwe.setDescription(e.getMessage());
        }

        return bwe;
    }

    /**
     * 取消发布模块版本
     * 
     * @param versionId
     * @return
     */
    @RequestMapping("/module/cancelPublishPageInstanceVersion.json")
    @ResponseBody
    public Object cancelPublishModuleInstance(@RequestParam("versionId") Long versionId){
        log.debug("cancel published module version id is {} operator is " + this.getUserDetails().getUsername() + ", operatorId is " + this.getUserDetails().getUserId(), versionId);
        /**
         * 取消发布页面版本步骤
         * 1、取消发布模块版本
         * 2、重新更新发布模块版本的队列缓存
         */
        BackWarnEntity bwe = new BackWarnEntity(true, Constants.CMS_PUBLISH_SUCCESS);
        try{
            cmsModuleInstanceVersionManager.cancelPublishedModuleInstanceVersion(versionId);
            cmsModuleInstanceVersionManager.setPublicModuleVersionCacheInfo();
        }catch (BusinessException e){
            bwe.setIsSuccess(false);
            bwe.setDescription(e.getMessage());
        }

        return bwe;
    }

    @RequestMapping("/module/recoverTemplateCodeVersionArea.json")
    @ResponseBody
    public BackWarnEntity recoverTemplateCodeArea(Long templateId,Long versionId,String code) throws UnsupportedEncodingException{
        BackWarnEntity back = new BackWarnEntity();
        try{
            String data = cmsModuleInstanceManager.recoverTemplateCodeArea(templateId, versionId, code);
            back.setIsSuccess(true);
            back.setDescription(data);
            return back;
        }catch (BusinessException e){
            back.setDescription(e.getMessage());
            back.setIsSuccess(false);
            return back;
        }

    }

    @RequestMapping("/module/copyModuleInstanceVersion.json")
    @ResponseBody
    public BackWarnEntity copyPageInstanceVersion(Model model,@RequestParam("versionId") Long versionId,@RequestParam("name") String name){
        BackWarnEntity backWarnEntity = new BackWarnEntity(true, Constants.CMS_COPY_SUCCESS);
        try{
            cmsModuleInstanceVersionManager.copyModuleInstanceVersion(versionId, name);
        }catch (BusinessException e){
            backWarnEntity.setIsSuccess(false);
            backWarnEntity.setDescription(e.getMessage());
        }

        return backWarnEntity;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
