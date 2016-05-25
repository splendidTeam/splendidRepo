/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.web.controller.cms;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import com.baozun.nebula.manager.cms.pageinstance.CmsPageInstanceManager;
import com.baozun.nebula.manager.cms.pagetemplate.PageTemplateManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * 模板页面控制器
 * 
 * @author chenguang.zhou
 * @date 2014年7月2日 上午9:34:55
 */
@Controller
public class CmsPageInstanceController extends BaseController {

	private static Logger			log	= LoggerFactory.getLogger(CmsPageInstanceController.class);

	@Autowired
	private CmsPageInstanceManager	cmsPageInstanceManager;
	
	@Autowired
	private PageTemplateManager		pageTemplateManager;
	private final static String			NOEDIT_START				= "<!--noedit-start-->";

	private final static String			NOEDIT_END					= "<!--noedit-end-->";
	@Autowired
	private SdkCmsEditAreaManager sdkCmsEditAreaManager;
	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;
	/**
	 * 通过模板ID查询对应管理页面
	 * 
	 * @param model
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/page/findPageInstanceListByTemplateId.htm")
	public String findPageInstanceListByTemplateId(Model model, @RequestParam("templateId") Long templateId) {
		CmsPageTemplate cmsPageTemplate = pageTemplateManager.findCmsPageTemplateById(templateId);
		model.addAttribute("cmsPageTemplate", cmsPageTemplate);
		return "/cms/newpage/page-instance-list";
	}

	/**
	 * 通过模板ID查询对应页面实例
	 * 
	 * @param queryBean
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/page/findPageInstanceListByTemplateId.json")
	@ResponseBody
	public Object findPageInstanceListByTemplateId(@QueryBeanParam QueryBean queryBean) {

		Sort[] sorts = queryBean.getSorts();
		if (null == sorts || sorts.length == 0) {
			Sort sort = new Sort("create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}

		Pagination<CmsPageInstance> cmsPageInstancePage = cmsPageInstanceManager
				.findCmsPageInstanceListByParaMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
		return cmsPageInstancePage;
	}

	/**
	 * 到新增页面实例页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/page/toNewAddPage.htm")
	public String toNewAddPage(Model model, @RequestParam("templateId") Long templateId) {
		CmsPageTemplate cmsPageTemplate = pageTemplateManager.findCmsPageTemplateById(templateId);
		model.addAttribute("cmsPageTemplate", cmsPageTemplate);
		model.addAttribute("isUpdate", false);
		return "/cms/newpage/page-instance-add";
	}

	/**
	 * 保存模板页面实例
	 * 
	 * @param cmsPageInstance
	 * @return
	 */
	@RequestMapping("/page/saveCmsPageInstance.json")
	@ResponseBody
	public Object savePageInstance(@ModelAttribute CmsPageInstance cmsPageInstance, 
			@RequestParam("html") String html) {
		CmsPageInstance pageInstance = cmsPageInstanceManager.createOrUpdateCmsPageInstance(cmsPageInstance, html);
		return pageInstance;
	}
	
	/**
	 * 查询模板与编辑数据 
	 * @param model
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/page/findTemplatePageAreaByTemplateId.htm")
	public String findTemplatePageAreaByTemplateId(Model model, 
			@RequestParam("templateId") Long templateId, 
			@RequestParam(value="pageId", required=false) Long pageId,
			@RequestParam("isEdit") Boolean isEdit){
		/** 模板信息 */
		String data = sdkCmsParseHtmlContentManager.getTemplatePageData(templateId, pageId, null, isEdit);
		model.addAttribute("data", data);
		return "/cms/newpage/page-instance-iframe";
	}
	
	@RequestMapping("/cms/editCmsTemplate.htm")
	public String toCmsTemplate(Model model,@RequestParam("id") Long id) throws UnsupportedEncodingException{
		model.addAttribute("templateId", id);
		String data = pageTemplateManager.findUpdatedCmsPageInstance(id);
		StringBuilder repeat = new StringBuilder();
		processNoEditData(data,repeat,0);
		String repeatData = URLEncoder.encode(repeat.toString(),"UTF-8");
		model.addAttribute("repeatData", repeatData);
		return "/cms/template-edit";
	}
	
	@RequestMapping("/cms/findTemplateByTemplateId.htm")
	public String findTemplateByTemplateId(Model model,@RequestParam("templateId") Long templateId){
		/** 模板信息 */
		String data = pageTemplateManager.findUpdatedCmsPageInstance(templateId);
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
	@RequestMapping("/cms/previewTemplate.htm")
	public String previewTemplate(Model model,Long id){
		String data = pageTemplateManager.findUpdatedCmsPageInstance(id);
		model.addAttribute("data", data);
		return "/cms/template-preview";
	}
	
	
	/**
	 * 跳转到修改页面
	 * @param model
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/updatePageInstance.htm")
	public String updatePageInstance(Model model,
			@RequestParam("pageId") Long pageId){
		
		CmsPageInstance cmsPageInstance = cmsPageInstanceManager.findCmsPageInstanceById(pageId);
		model.addAttribute("cmsPageInstance", cmsPageInstance);
		
		CmsPageTemplate cmsPageTemplate = pageTemplateManager.findCmsPageTemplateById(cmsPageInstance.getTemplateId());
		model.addAttribute("cmsPageTemplate", cmsPageTemplate);
		model.addAttribute("isUpdate", true);
		return "/cms/newpage/page-instance-add";
	}
	
	/**
	 * 删除页面实例(单个)
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/removePageInstanceById.json")
	@ResponseBody
	public Object removePageInstanceById(@RequestParam("pageId") Long pageId){
		List<Long> ids = new ArrayList<Long>();
		ids.add(pageId);
		cmsPageInstanceManager.removeCmsPageInstanceByIds(ids);
		return SUCCESS;
	}

	/**
	 * 删除页面实例(批量)
	 * @param pageIds
	 * @return
	 */
	@RequestMapping("/page/removePageInstanceByIds.json")
	@ResponseBody
	public Object removePageInstanceByIds(@ArrayCommand(dataBind = true) Long[] pageIds){
		List<Long> ids = Arrays.asList(pageIds);
		cmsPageInstanceManager.removeCmsPageInstanceByIds(ids);
		return SUCCESS;
	}
	
	/**
	 * 发布页面实例
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/publishPageInstance.json")
	@ResponseBody
	public Object publishPageInstance(@RequestParam("pageId") Long pageId,@RequestParam("startTime")Date startTime,@RequestParam("endTime")Date endTime){
		BackWarnEntity back = new BackWarnEntity();
		if(startTime == null && endTime!=null){
			if(endTime.compareTo(new Date())<=0){
				back.setDescription("结束时间应大于当前时间");
				return back;
			}
		}
		if(startTime != null && endTime!=null){
			if(startTime.compareTo(endTime)>=0){
				back.setDescription("开始时应大于间结束时间");
				return back;
			}
		}	
		cmsPageInstanceManager.cancelExpireVersion(pageId);
		if(!checkPublishedPageInstance(pageId,startTime,endTime)){
			back.setDescription("当前页面发布时间与其版本页面的发布时间有冲突");
			return back;
		}
		cmsPageInstanceManager.publishPageInstance(pageId,startTime,endTime);
		return SUCCESS;
	}

	/**
	 * 取消发布页面实例
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/cancelPublishPageInstance.json")
	@ResponseBody
	public Object cancelPublishPageInstance(@RequestParam("pageId") Long pageId){
		cmsPageInstanceManager.cancelPublishedPageInstance(pageId);
		log.debug("cancel published page instance id is {}", pageId);
		return SUCCESS;
	}
	
	/**
	 * 检察页面实例编码是否存在
	 * @param code
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/checkPageInstanceCode.json")
	@ResponseBody
	public Object checkPageInstanceCode(@RequestParam("code") String code, 
			@RequestParam(value="pageId", required=false) Long pageId){
		CmsPageInstance cmsPageInstance = cmsPageInstanceManager.checkPageInstanceCode(code, pageId);
		return cmsPageInstance;
	}

	
	/**
	 * 检察页面实例url是否存在
	 * @param code
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/checkPageInstanceUrl.json")
	@ResponseBody
	public Object checkPageInstanceUrl(@RequestParam("url") String url, 
			@RequestParam(value="pageId", required=false) Long pageId,Integer type){
		CmsPageInstance cmsPageInstance = cmsPageInstanceManager.checkPageInstanceUrl(url, pageId,type);
		return cmsPageInstance;
	}
	
	
	@RequestMapping("/cms/editCmsPageTemplate.json")
	@ResponseBody
	public BackWarnEntity editCmsPageTemplate(CmsPageTemplate cmd,String repeatData) throws UnsupportedEncodingException {
		try {
			String data = cmd.getData();
			if(Validator.isNotNullOrEmpty(data) && Validator.isNotNullOrEmpty(repeatData)){
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
			cmd.setData(data);
			pageTemplateManager.editCmsPageTemplate(cmd);
			return SUCCESS;
		} catch (BusinessException e) {
			BackWarnEntity back = new BackWarnEntity();
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	@RequestMapping("/cms/recoverTemplateCodeArea.json")
	@ResponseBody
	public BackWarnEntity recoverTemplateCodeArea(Long templateId,String code, Long versionId) throws UnsupportedEncodingException {
		BackWarnEntity back = new BackWarnEntity();
		try {
			String data = pageTemplateManager.recoverTemplateCodeArea(templateId, code, versionId);
			back.setIsSuccess(true);
			back.setDescription(data);
			return back;
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	@RequestMapping("/cms/editAreaHide.json")
	@ResponseBody
	public BackWarnEntity editAreaHide(Long pageId,Long templateId,Long versionId,String areaCode,String type,int hide) throws UnsupportedEncodingException {
		BackWarnEntity back = new BackWarnEntity();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pageId", pageId);
			paraMap.put("templateId", templateId);
			paraMap.put("areaCode", areaCode);
			paraMap.put("type", type);
			paraMap.put("version", versionId);
			if(sdkCmsEditAreaManager.queryEditAreaHide(paraMap)== null){
				back.setDescription("编辑区域尚未保存过,请先保存");
				back.setIsSuccess(false);
				return back;
			}
			paraMap.put("hide", hide);
			sdkCmsEditAreaManager.editAreaHide(paraMap);
			back.setIsSuccess(true);
			return back;
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 检察页面实例url是否存在
	 * @param code
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/checkPublishPageInstanceUrl.json")
	@ResponseBody
	public Object checkPublishPageInstanceUrl(@RequestParam("url") String url, 
			@RequestParam(value="pageId", required=false) Long pageId,Integer tmpId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("url", url);
		params.put("pageId", pageId);
		params.put("tmpId", tmpId);
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.checkPublishPageInstanceUrl(params);
		return cmsPageInstance;
	}
	
	@RequestMapping("/cms/getItemEditArea.json")
	@ResponseBody
	public BackWarnEntity getItemEditArea(String editArea) throws UnsupportedEncodingException {
		BackWarnEntity back = new BackWarnEntity();
		try {
			Document document = Jsoup.parse(editArea);
			Elements eles = document.select(".cms-product-edit");
			if(eles != null && eles.size()!=0){
				String data = sdkCmsPageInstanceManager.setProductInfo(eles.get(0), true);
				back.setIsSuccess(true);
				back.setDescription(data);
			}
			return back;
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	private boolean checkPublishedPageInstance(Long pageId, Date startTime, Date endTime){
		Map<String, Date> timeRang = sdkCmsPageInstanceManager.getPublishedPageInstanceVersionsTimeRang(pageId);
		if(Validator.isNotNullOrEmpty(timeRang)&& Validator.isNotNullOrEmpty(timeRang.get("firstTime")) &&   Validator.isNotNullOrEmpty(timeRang.get("lastTime"))){
			Date firstTime = timeRang.get("firstTime");
			Date lastTime = timeRang.get("lastTime");
			if(Validator.isNotNullOrEmpty(startTime) && startTime.compareTo(firstTime)<=0 && Validator.isNotNullOrEmpty(endTime) &&endTime.compareTo(lastTime) >=0){
				return true;
			}else if(Validator.isNullOrEmpty(startTime) && Validator.isNotNullOrEmpty(endTime) &&endTime.compareTo(lastTime) >=0){
				return true;
			}else if(Validator.isNotNullOrEmpty(startTime) && startTime.compareTo(firstTime)<=0 && Validator.isNullOrEmpty(endTime)){
				return true;
			}else if(Validator.isNullOrEmpty(startTime) && Validator.isNullOrEmpty(endTime)){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
}
