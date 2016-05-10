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
import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceVersionManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * CmsModuleInstanceController
 * 
 * @author 何波
 * 
 */
@Controller
public class CmsModuleInstanceController extends BaseController {

	@Autowired
	private SdkCmsModuleInstanceManager cmsModuleInstanceManager;

	@Autowired
	private SdkCmsModuleTemplateManager cmsModuleTemplateManager;
	
	@Autowired
	private SdkCmsModuleInstanceVersionManager cmsModuleInstanceVersionManager;
	
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;

	@RequestMapping("/cmsModuleInstance/list.htm")
	public String list(Model model, Long templateId) {
		CmsModuleTemplate cmt = cmsModuleTemplateManager
				.findCmsModuleTemplateById(templateId);
		model.addAttribute("cmt", cmt);
		return "/cms/module/cmsModuleInstance";
	}

	/**
	 * 保存CmsModuleInstance
	 * 
	 */
	@RequestMapping("/cmsModuleInstance/save.json")
	@ResponseBody
	public BackWarnEntity saveCmsModuleInstance(CmsModuleInstance model) {
		cmsModuleInstanceManager.saveCmsModuleInstance(model);
		return SUCCESS;
	}

	/**
	 * 通过id获取CmsModuleInstance
	 * 
	 */
	@RequestMapping("/cmsModuleInstance/findByid.json")
	@ResponseBody
	public CmsModuleInstance findCmsModuleInstanceById(Long id) {

		return cmsModuleInstanceManager.findCmsModuleInstanceById(id);
	}

	/**
	 * 获取所有CmsModuleInstance列表
	 * 
	 * @return
	 */
	@RequestMapping("/cmsModuleInstance/findAll.json")
	@ResponseBody
	public List<CmsModuleInstance> findAllCmsModuleInstanceList() {

		return cmsModuleInstanceManager.findAllCmsModuleInstanceList();
	};

	/**
	 * 通过ids获取CmsModuleInstance列表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleInstance/findByIds.json")
	@ResponseBody
	public List<CmsModuleInstance> findCmsModuleInstanceListByIds(Long[] ids) {

		return cmsModuleInstanceManager.findCmsModuleInstanceListByIds(Arrays
				.asList(ids));
	};

	/**
	 * @Description: 分页获取CmsModuleInstance列表
	 * @param queryBean
	 * @return Pagination<CmsModuleInstance>
	 * @throws
	 */
	@RequestMapping("/cmsModuleInstance/page.json")
	@ResponseBody
	public Pagination<CmsModuleInstance> findCmsModuleInstanceListByQueryMapWithPage(
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		return cmsModuleInstanceManager
				.findCmsModuleInstanceListByQueryMapWithPage(
						queryBean.getPage(), sorts, queryBean.getParaMap());
	}

	/**
	 * 通过ids批量启用或禁用CmsModuleInstance 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleInstance/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity enableOrDisableCmsModuleInstanceByIds(Long[] ids,
			Integer state) {
		cmsModuleInstanceManager.enableOrDisableCmsModuleInstanceByIds(
				Arrays.asList(ids), state);
		return SUCCESS;
	}

	/**
	 * 通过ids批量删除CmsModuleInstance 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleInstance/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeCmsModuleInstanceByIds(Long[] ids) {
		cmsModuleInstanceManager.removeCmsModuleInstanceByIds(Arrays
				.asList(ids));
		cmsModuleInstanceVersionManager.setPublicModuleVersionCacheInfo();
		return SUCCESS;
	}

	@RequestMapping("/cmsModuleInstance/addCmsModuleInstance.htm")
	public String addCmsModuleInstance(Model model, Long templateId) {
		CmsModuleTemplate cmt = cmsModuleTemplateManager
				.findCmsModuleTemplateById(templateId);
		model.addAttribute("cmt", cmt);
		model.addAttribute("isUpdate", false);
		return "/cms/module/module-instance-add";
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param model
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/cmsModuleInstance/updateModuleInstance.htm")
	public String updatePageInstance(Model model, Long moduleId) {

		CmsModuleInstance cmi = cmsModuleInstanceManager
				.findCmsModuleInstanceById(moduleId);
		model.addAttribute("cmi", cmi);

		CmsModuleTemplate cmt = cmsModuleTemplateManager
				.findCmsModuleTemplateById(cmi.getTemplateId());
		model.addAttribute("cmt", cmt);
		model.addAttribute("isUpdate", true);
		return "/cms/module/module-instance-add";
	}
	
	/**
	 * 查询模板与编辑数据 
	 * @param model
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/page/findTemplateModuleAreaByTemplateId.htm")
	public String findTemplateModuleAreaByTemplateId(Model model, 
			@RequestParam("templateId") Long templateId, 
			@RequestParam(value="moduleId", required=false) Long moduleId,
			@RequestParam("isEdit") Boolean isEdit){
		/** 模板信息 */
		//String data = cmsModuleInstanceManager.findUpdatedCmsModuleInstance(templateId, moduleId, isEdit);
		String data = sdkCmsParseHtmlContentManager.getTemplateModuleData(templateId, moduleId, null, isEdit);
		model.addAttribute("data", data);
		return "/cms/newpage/page-instance-iframe";
	}
	
	
	/**
	 * 保存模板页面实例
	 * 
	 * @param cmsPageInstance
	 * @return
	 */
	@RequestMapping("/module/saveModuleInstance.json")
	@ResponseBody
	public BackWarnEntity saveModuleInstance(@ModelAttribute CmsModuleInstance cmsModuleInstance, 
			@RequestParam("html") String html) {
		BackWarnEntity back  = new BackWarnEntity();
		try {
			CmsModuleInstance instance = cmsModuleInstanceManager.createOrUpdateModuleInstance(cmsModuleInstance, html);
			back.setDescription(instance);
			back.setIsSuccess(true);
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
		}
		return back;
	}
	/**
	 * 发布模块实例
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/module/publishModuleInstance.json")
	@ResponseBody
	public Object publishModuleInstance(Long moduleId,Long status){
		if(status==1){
			cmsModuleInstanceManager.publishModuleInstance(moduleId);
		}else{
			cmsModuleInstanceManager.cancelPublishedModuleInstance(moduleId);
		}
		//cmsModuleInstanceVersionManager.setPublicModuleVersionCacheInfo();
		return SUCCESS;
	}
	
	
	@RequestMapping("/module/recoverTemplateCodeArea.json")
	@ResponseBody
	public BackWarnEntity recoverTemplateCodeArea(Long templateId, Long versionId, String code) throws UnsupportedEncodingException {
		BackWarnEntity back = new BackWarnEntity();
		try {
			String data = cmsModuleInstanceManager.recoverTemplateCodeArea(templateId, versionId, code);
			back.setIsSuccess(true);
			back.setDescription(data);
			return back;
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	
	
}
