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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.manager.cms.pagetemplate.PageTemplateManager;
import com.baozun.nebula.manager.system.UploadManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.image.ImageOpeartion;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * @author jumbo
 * 
 */
@Controller
public class CmsPageTemplateController extends BaseController {

	@SuppressWarnings("unused")
	private final static Logger			log					= LoggerFactory.getLogger(CmsPageTemplateController.class);

	/** 上传图片的域名 */
	@Value("#{meta['upload.img.domain.base']}")
	private String						UPLOAD_IMG_DOMAIN	= "";

	@Autowired
	private UploadManager				uploadManager;

	@Autowired
	private SdkCmsPageTemplateManager	sdkCmsPageTemplateManager;
	
	@Autowired
	private SdkCmsPageInstanceManager	sdkCmsPageInstanceManager;

	@Autowired
	private PageTemplateManager			pageTemplateManager;

	/** 进去模板列表 */
	@RequestMapping(value = "/newcms/pageTemplateList.htm")
	public String entryTemplateList(Model model) {
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		return "/cms/cms-page-template-list";
	}

	/**
	 * 获取数据库有效的页面模板
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cms/pageTemplateAllList.json")
	@ResponseBody
	public Pagination<CmsPageTemplate> getTemplateList(Model model, @QueryBeanParam QueryBean queryBean,
			HttpServletRequest request, HttpServletResponse response) {
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			Sort sort = new Sort("create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		Pagination<CmsPageTemplate> cmsPageTemplateList = sdkCmsPageTemplateManager
				.findCmsPageTemplateListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
		return cmsPageTemplateList;
	}

	/** 到创建模板页面 */
	@RequestMapping(value = "/cms/createtemplate.htm")
	public String entryCreateTemplate(Model model) {
		return "/cms/cms-create-template";
	}

	/**
	 * 保存模板
	 * 
	 * @return
	 */
	@RequestMapping("/cms/savePageTemplate.json")
	// @ResponseBody
	public String saveTemplate(CmsPageTemplate cmd, @RequestParam("templateFile") CommonsMultipartFile templateFile,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		if (templateFile.getSize() > 0) {
			String data = uploadManager.uploadFileToString(templateFile);
			cmd.setData(data);
		}
		pageTemplateManager.saveOrUodateCmsPageTemplate(cmd, UPLOAD_IMG_DOMAIN);
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		return "redirect:/newcms/pageTemplateList.htm";
	}

	/**
	 * 修改模板
	 * 
	 * @return
	 */
	@RequestMapping("/cms/updatetemplate.htm")
	public String modifyTemplate(@RequestParam("id") Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		CmsPageTemplate page = sdkCmsPageTemplateManager.findCmsPageTemplateById(id);
		page.setImg(ImageOpeartion.imageUrlConvert(page.getImg(), UPLOAD_IMG_DOMAIN, false));
		model.addAttribute("pagetemplate", page);
		return "/cms/cms-update-template";
	}

	/**
	 * 查看html
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cms/viewhtml.htm")
	public String findCmsPageTemplate(@RequestParam("id") Long id, Model model) {
		CmsPageTemplate page = sdkCmsPageTemplateManager.findCmsPageTemplateById(id);
		page.setImg(ImageOpeartion.imageUrlConvert(page.getImg(), UPLOAD_IMG_DOMAIN, false));
		model.addAttribute("pagetemplate", page);
		return "/cms/cms-page-template-html";
	}

	/**
	 * 检查模板是否有管理页面
	 * @return
	 */
	@RequestMapping("/cms/checkInstance.htm")
	@ResponseBody
	public Object checkInstance(@RequestParam("ids") List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
		
		List<CmsPageInstance> instanceList = sdkCmsPageInstanceManager.findCmsPageInstanceListByTemplateIds(ids);
		if(Validator.isNotNullOrEmpty(instanceList)){
			return FAILTRUE;
		}
		return SUCCESS;
	}
	
	/**
	 * 删除模板
	 * 
	 * @return
	 */
	@RequestMapping("/cms/deletetemplate.htm")
	@ResponseBody
	public Object deleteTemplate(@RequestParam("ids") List<Long> ids, HttpServletRequest request, HttpServletResponse response) {
		sdkCmsPageTemplateManager.removeCmsPageTemplateByIds(ids);
		return SUCCESS;
	}

	/**
	 * 通过ids逻辑删除 批量删除模板
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping("/cms/butchremove.json")
	@ResponseBody
	public String deleteTemplate(@RequestParam("ids") List<Long> ids, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		sdkCmsPageTemplateManager.removeCmsPageTemplateByIds(ids);
		return "success";
	}
}
