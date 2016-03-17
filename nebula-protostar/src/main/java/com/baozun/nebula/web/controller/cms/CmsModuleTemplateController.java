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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.cms.moduletemplate.ModuleTemplateManager;
import com.baozun.nebula.manager.system.UploadManager;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsModuleTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.utils.image.ImageOpeartion;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * CmsModuleTemplateController
 * 
 * @author 何波
 * 
 */
@Controller
public class CmsModuleTemplateController extends BaseController {

	private final static String			NOEDIT_START				= "<!--noedit-start-->";

	private final static String			NOEDIT_END					= "<!--noedit-end-->";
	@Autowired
	private SdkCmsModuleTemplateManager cmsModuleTemplateManager;
	
	@Autowired
	private ModuleTemplateManager moduleTemplateManager;

	@Autowired
	private UploadManager uploadManager;
	
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	

	/** 上传图片的域名 */
	@Value("#{meta['upload.img.domain.base']}")
	private String UPLOAD_IMG_DOMAIN = "";

	@RequestMapping("/newcms/moduleTemplateList.htm")
	public String list(Model model) {
		model.addAttribute("customBaseUrl", UPLOAD_IMG_DOMAIN);
		return "/cms/module/cmsModuleTemplate";
	}

	/**
	 * 保存CmsModuleTemplate
	 * 
	 */
	@RequestMapping("/cmsModuleTemplate/edit.htm")
	public String edit(Model model, Long id) {
		if (id != null) {
			CmsModuleTemplate cmt = cmsModuleTemplateManager
					.findCmsModuleTemplateById(id);
			cmt.setImg(UPLOAD_IMG_DOMAIN + cmt.getImg());
			model.addAttribute("cmt", cmt);
		}
		model.addAttribute("id", id);
		return "/cms/module/cms-module-create";
	}
	
	@RequestMapping("/cmsModuleTemplate/view.htm")
	public String view(Model model, Long id) {
		String data = moduleTemplateManager.packModuleTemplateById(id);
		model.addAttribute("data", data);
		return "/cms/module/cms-module-view";
	}
	
	/**
	 * 保存CmsModuleTemplate
	 * 
	 */
	@RequestMapping("/cmsModuleTemplate/save.json")
	public String saveCmsModuleTemplate(CmsModuleTemplate cmt, Model model,
			@RequestParam("templateFile") CommonsMultipartFile templateFile) {
		if (templateFile.getSize() > 0) {
			String data = uploadManager.uploadFileToString(templateFile);
			cmt.setData(data);
		}
		cmt.setImg(ImageOpeartion.imageUrlConvert(cmt.getImg(),
				UPLOAD_IMG_DOMAIN, true));
		cmsModuleTemplateManager.saveCmsModuleTemplate(cmt);
		return "redirect:/newcms/moduleTemplateList.htm";
	}

	/**
	 * 通过id获取CmsModuleTemplate //没用到，暂时不改--processTemplateBase
	 * 
	 */
	@RequestMapping("/cmsModuleTemplate/findByid.json")
	@ResponseBody
	public CmsModuleTemplate findCmsModuleTemplateById(Long id) {

		return cmsModuleTemplateManager.findCmsModuleTemplateById(id);
	}

	/**
	 * 获取所有CmsModuleTemplate列表
	 * 
	 * @return
	 */
	@RequestMapping("/cmsModuleTemplate/findAll.json")
	@ResponseBody
	public List<CmsModuleTemplate> findAllCmsModuleTemplateList() {

		return cmsModuleTemplateManager.findAllCmsModuleTemplateList();
	};

	/**
	 * 通过ids获取CmsModuleTemplate列表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleTemplate/findByIds.json")
	@ResponseBody
	public List<CmsModuleTemplate> findCmsModuleTemplateListByIds(Long[] ids) {

		return cmsModuleTemplateManager.findCmsModuleTemplateListByIds(Arrays
				.asList(ids));
	};

	/**
	 * @Description: 分页获取CmsModuleTemplate列表
	 * @param queryBean
	 * @return Pagination<CmsModuleTemplate>
	 * @throws
	 */
	@RequestMapping("/cmsModuleTemplate/page.json")
	@ResponseBody
	public Pagination<CmsModuleTemplate> findCmsModuleTemplateListByQueryMapWithPage(
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		return cmsModuleTemplateManager
				.findCmsModuleTemplateListByQueryMapWithPage(
						queryBean.getPage(), sorts, queryBean.getParaMap());
	}

	/**
	 * 通过ids批量启用或禁用CmsModuleTemplate 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleTemplate/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity enableOrDisableCmsModuleTemplateByIds(Long[] ids,
			Integer state) {
		cmsModuleTemplateManager.enableOrDisableCmsModuleTemplateByIds(
				Arrays.asList(ids), state);
		return SUCCESS;
	}

	/**
	 * 通过ids批量删除CmsModuleTemplate 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cmsModuleTemplate/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeCmsModuleTemplateByIds(Long[] ids) {
		cmsModuleTemplateManager.removeCmsModuleTemplateByIds(Arrays
				.asList(ids));
		return SUCCESS;
	}
	
	@RequestMapping("/module/editCmsTemplate.htm")
	public String toCmsTemplate(Model model,@RequestParam("id") Long id) throws UnsupportedEncodingException{
		
		String data = moduleTemplateManager.packModuleTemplateById(id);
		
		StringBuilder repeat = new StringBuilder();
		processNoEditData(data,repeat,0);
		String repeatData = URLEncoder.encode(repeat.toString(),"UTF-8");
		model.addAttribute("repeatData", repeatData);
		model.addAttribute("templateId", id);
		return "/cms/module/template-edit";
	}
	
	@RequestMapping("/module/findTemplateByTemplateId.htm")
	public String findTemplateByTemplateId(Model model, Long templateId) throws UnsupportedEncodingException{
		String data = moduleTemplateManager.packModuleTemplateById(templateId);
		StringBuilder repeat = new StringBuilder();
		data = processNoEditData(data,repeat,0);
		model.addAttribute("data", data);
		return "/cms/template-iframe";
	}
	
	private  String processNoEditData(String data,StringBuilder repeat,int num) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(NOEDIT_START);
		int indexEnd = data.indexOf(NOEDIT_END);
		
		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			String rpt = "<!--repeat"+num+"-->";
			sb.append(rpt);
			repeat.append(rpt+"repeat:"+data.substring(indexStart,indexEnd+NOEDIT_END.length())+"repeat,");
			sb.append(data.substring(indexEnd + NOEDIT_END.length(), data.length()));
			
			data = sb.toString();
			data = processNoEditData(data,repeat,num+1);
		}
		return data;
	}
	
	/**
	* @author 何波
	* @Description: 预览模板 
	* @param model
	* @param id
	* @return   
	* String   
	* @throws
	 */
	@RequestMapping("/module/previewTemplate.htm")
	public String previewTemplate(Model model,Long id){
		String data = cmsModuleTemplateManager.findCmsModuleTemplateById(id).getData();
		model.addAttribute("data", data);
		return "/cms/template-preview";
	}
	
	@RequestMapping("/module/editCmsModuleTemplate.json")
	@ResponseBody
	public BackWarnEntity editCmsPageTemplate(CmsModuleTemplate cmt,String repeatData) throws UnsupportedEncodingException {
		try {
			String data = cmt.getData();
			if(data!=null && repeatData!=null && repeatData!=""){
				String repeat =URLDecoder.decode(repeatData, "UTF-8");
				String[] repeats =  repeat.split("repeat,");
				if(repeats!=null && repeats.length>0){
					for (String re : repeats) {
						if(re!=null && re !=""){
						 String[] rr = re.split("repeat:");
						 if(data.indexOf(rr[0])>-1){
							 data = data.replace(rr[0], rr[1]);
						 }
						}
					}
				}
			}
			cmt.setData(data);
			cmsModuleTemplateManager.editCmsModuleTemplate(cmt);
			return SUCCESS;
		} catch (BusinessException e) {
			BackWarnEntity back = new BackWarnEntity();
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
}
