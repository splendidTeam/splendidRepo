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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

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
import com.baozun.nebula.manager.cms.pageversion.CmsPageInstanceVersionManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsEditVersionAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceVersionManager;
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
public class CmsPageInstanceVersionController extends BaseController {

	private static Logger			log	= LoggerFactory.getLogger(CmsPageInstanceVersionController.class);

	@Autowired
	private CmsPageInstanceManager	cmsPageInstanceManager;

	@Autowired
	private CmsPageInstanceVersionManager	cmsPageInstanceVersionManager;
	
	@Autowired
	private PageTemplateManager pageTemplateManager;
	
	
	private final static Integer       PAGE_VERSION_NOT_SAVE  = 0;
	
	private final static Integer       PAGE_INSTANCE_NOT_PUBLISHED  = 1;
	@Autowired
	private SdkCmsEditAreaManager sdkCmsEditAreaManager;
	@Autowired
	private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;
	@Autowired
	private  SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	@Autowired
	private SdkCmsPageInstanceVersionManager sdkCmsPageInstanceVersionManager;
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;
	/**
	 * 通过实例ID查询对应管理版本页面
	 * 
	 * @param model
	 * @param instanceId
	 * @return
	 */
	@RequestMapping("/page/findPageInstanceVersionListByInstanceId.htm")
	public String findPageInstanceListByTemplateId(Model model, @RequestParam("instanceId") Long instanceId) {
		CmsPageInstance cmsPageInstance = cmsPageInstanceManager.findCmsPageInstanceById(instanceId);
		model.addAttribute("cmsPageInstance", cmsPageInstance);
		return "/cms/newpage/page-instance-version-list";
	}
	
	/**
	 * 通过实例ID查询对应管理版本页面
	 * 
	 * @param queryBean
	 * @param instanceId
	 * @return
	 */
	@RequestMapping("/page/findPageInstanceVersionListByInstanceId.json")
	@ResponseBody
	public Object findPageInstanceListByTemplateId(@QueryBeanParam QueryBean queryBean) {

		Sort[] sorts = queryBean.getSorts();
		if (null == sorts || sorts.length == 0) {
			Sort sort = new Sort("create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		Pagination<CmsPageInstanceVersion> cmsPageInstanceVersions = cmsPageInstanceVersionManager.findCmsPageInstanceVersionListByParaMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
		return cmsPageInstanceVersions;
	}
	
	/**
	 * 到新增页面实例页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/page/toNewAddVersionPage.htm")
	public String toNewAddPage(Model model, @RequestParam("instanceId") Long instanceId) {
		CmsPageInstance cmsPageInstance = cmsPageInstanceManager.findCmsPageInstanceById(instanceId);
		CmsPageTemplate cmsPageTemplate = null;
		if(Validator.isNotNullOrEmpty(cmsPageInstance)){
			cmsPageTemplate = pageTemplateManager.findCmsPageTemplateById(cmsPageInstance.getTemplateId());
		}
		model.addAttribute("cmsPageTemplate", cmsPageTemplate);
		model.addAttribute("instanceId", instanceId);
		model.addAttribute("isUpdate", false);
		return "/cms/newpage/page-instance-version-add";
	}
	
	/**
	 * 保存模板页面实例
	 * 
	 * @param cmsPageInstance
	 * @return
	 */
	@RequestMapping("/page/saveCmsPageInstanceVersion.json")
	@ResponseBody
	public Object savePageInstanceVersion(@ModelAttribute CmsPageInstanceVersion cmsPageInstanceVersion, @RequestParam("html") String html) {
		CmsPageInstanceVersion pageInstanceVersion = cmsPageInstanceVersionManager.createOrUpdateCmsPageInstanceVersion(cmsPageInstanceVersion, html);
		return pageInstanceVersion;
	}
	
	/**
	 * 跳转到修改页面
	 * @param model
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/updatePageInstanceVersion.htm")
	public String updatePageInstance(Model model,
			@RequestParam("versionId") Long versionId){
		
		CmsPageInstanceVersion cmsPageInstanceVersion = cmsPageInstanceVersionManager.getCmsPageInstanceVersionById(versionId);
		model.addAttribute("cmsPageInstanceVersion", cmsPageInstanceVersion);
		
		CmsPageTemplate cmsPageTemplate = pageTemplateManager.findCmsPageTemplateById(cmsPageInstanceVersion.getTemplateId());
		model.addAttribute("cmsPageTemplate", cmsPageTemplate);
		model.addAttribute("instanceId", cmsPageInstanceVersion.getInstanceId());
		model.addAttribute("isUpdate", true);
		return "/cms/newpage/page-instance-version-add";
	}
	
	/**
	 * 删除页面实例(单个)
	 * @param pageId
	 * @return
	 */
	@RequestMapping("/page/removePageInstanceVersionById.json")
	@ResponseBody
	public Object removePageInstanceById(@RequestParam("versionId") Long versionId){
		List<Long> ids = new ArrayList<Long>();
		ids.add(versionId);
		return cmsPageInstanceVersionManager.removeCmsPageInstanceVersionByIds(ids);
	}
	
	/**
	 * 删除页面实例(批量)
	 * @param pageIds
	 * @return
	 */
	@RequestMapping("/page/removePageInstanceVersionByIds.json")
	@ResponseBody
	public Object removePageInstanceByIds(@ArrayCommand(dataBind = true) Long[] versionIds){
		List<Long> ids = Arrays.asList(versionIds);
		cmsPageInstanceVersionManager.removeCmsPageInstanceVersionByIds(ids);
		return SUCCESS;
	}
	
	
	@RequestMapping("/page/publishPageInstanceVersion.json")
	@ResponseBody
	public Object publishPageInstanceVersion(@RequestParam("versionId") Long versionId,@RequestParam("startTime")Date startTime,@RequestParam("endTime")Date endTime){
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
		BackWarnEntity result = cmsPageInstanceVersionManager.publishPageInstanceVersion(Long.valueOf(versionId),startTime,endTime);
		sdkCmsPageInstanceVersionManager.setPublicVersionCacheInfo();
		return result;
	}

	/**
	 * 取消发布页面实例版本
	 * @param versionId
	 * @return
	 */
	@RequestMapping("/page/cancelPublishPageInstanceVersion.json")
	@ResponseBody
	public Object cancelPublishPageInstance(@RequestParam("versionId") Long versionId){
		log.debug("cancel published page instance id is {}, operator is "+this.getUserDetails().getUsername()+", operatorId is "+this.getUserDetails().getUserId(), versionId);
		return cmsPageInstanceVersionManager.cancelPublishedPageInstanceVersion(versionId);	
	}
	
	/**
	 * 查询模板与编辑数据 
	 * @param model
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/page/findTemplatePageVersionAreaByTemplateId.htm")
	public String findTemplatePageAreaByTemplateId(Model model, 
			@RequestParam("templateId") Long templateId,
			@RequestParam("pageId") Long pageId,
			@RequestParam(value="versionId", required=false) Long versionId,
			@RequestParam("isEdit") Boolean isEdit){
		if(Validator.isNullOrEmpty(versionId)){
			versionId = 0L;
		}
		String data = sdkCmsParseHtmlContentManager.getTemplatePageData(templateId, pageId, versionId, isEdit);
		model.addAttribute("data", data);
		return "/cms/newpage/page-instance-iframe";
	}
	
	/**
	 * 检测发布实例版本时间是否超越发布实例时间
	 * @param model
	 * @param versionId
	 * @param startTime
	 * @param endTime
	 */
	@RequestMapping("/page/checkPublishTime.json")
	@ResponseBody
	public Object checkPublishTime(Model model, 
			@RequestParam("versionId") Long versionId, 
			@RequestParam("startTime") Date startTime,
			@RequestParam("endTime") Date endTime){
		/**
		 * 检测发布实例版本时间是否超越发布实例时间
		 * 1、获取模块版本和模块实例
		 * 2、检测发布时间(标准)
		 *  1)模块实例发布状态必须为已发布
		 *  2)模块版本的发布开始时间必须在模块实例开始时间之后,模块版本的发布的结束时间必须在模块实例结束时间之前。也就是说版本的时间必须在实例的时间的区间内，因为实例发布的时间可以只填写开始时间或者结束时间才有了如下判断
		 */
		BackWarnEntity bwe = new BackWarnEntity();
		if(Validator.isNotNullOrEmpty(versionId)){
			CmsPageInstanceVersion version = cmsPageInstanceVersionManager.getCmsPageInstanceVersionById(versionId);
			CmsPageInstance instance = sdkCmsPageInstanceManager.findCmsPageInstanceById(version.getInstanceId());
			if(Validator.isNotNullOrEmpty(instance)){
				if(Validator.isNullOrEmpty(instance.getIsPublished()) || !instance.getIsPublished()){
					bwe.setErrorCode(PAGE_INSTANCE_NOT_PUBLISHED);
				}else if(Validator.isNotNullOrEmpty(instance.getStartTime()) && instance.getStartTime().compareTo(startTime)<=0 && Validator.isNotNullOrEmpty(instance.getEndTime()) && instance.getEndTime().compareTo(endTime)>=0){
					bwe.setIsSuccess(true);
					return bwe;
				}else if(Validator.isNullOrEmpty(instance.getStartTime()) && Validator.isNotNullOrEmpty(instance.getEndTime()) && instance.getEndTime().compareTo(endTime)>=0){
					bwe.setIsSuccess(true);
					return bwe;
				}else if(Validator.isNotNullOrEmpty(instance.getStartTime()) && instance.getStartTime().compareTo(startTime)<=0 && Validator.isNullOrEmpty(instance.getEndTime())){
					bwe.setIsSuccess(true);				
					return bwe;
				}else if(Validator.isNullOrEmpty(instance.getStartTime()) && Validator.isNullOrEmpty(instance.getEndTime()) && startTime.compareTo(endTime)<=0){
					bwe.setIsSuccess(true);
					return bwe;
				}
			}else{
				bwe.setErrorCode(PAGE_VERSION_NOT_SAVE);
			}
		}else{
			bwe.setErrorCode(PAGE_VERSION_NOT_SAVE);
		}
		bwe.setIsSuccess(false);
		return bwe;
	}
	
	/**
	 * 显示和隐藏版本页面区域元素
	 * @param pageId 实例id
	 * @param templateId 模板id
	 * @param versionId 版本id
	 * @param areaCode 元素编号
	 * @param type 元素类型(page:页面， module:模块)
	 * @param hide 是否隐藏(显示null， 1:隐藏)
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/cms/editAreaVersionHide.json")
	@ResponseBody
	public BackWarnEntity editAreaVersionHide(Long pageId,Long templateId,Long versionId,String areaCode,String type,int hide) throws UnsupportedEncodingException {
		/**
		 * 显示和隐藏版本页面区域元素步骤
		 * 1、查询该编辑区域元素是否存在
		 * 2、更新该编辑区域元素的隐藏显示状态
		 */
		BackWarnEntity back = new BackWarnEntity();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pageId", pageId);
			paraMap.put("templateId", templateId);
			paraMap.put("areaCode", areaCode);
			paraMap.put("type", type);
			paraMap.put("versionId", versionId);
			if(sdkCmsEditVersionAreaManager.queryEditVersionAreaHide(paraMap)== null){
				back.setDescription("编辑区域尚未保存过,请先保存");
				back.setIsSuccess(false);
				return back;
			}
			paraMap.put("hide", hide);
			sdkCmsEditVersionAreaManager.editVersionAreaHide(paraMap);
			back.setIsSuccess(true);
			return back;
		} catch (BusinessException e) {
			back.setDescription(e.getMessage());
			back.setIsSuccess(false);
			return back;
		}
	
	}
	
	
	@RequestMapping("/page/copyPageInstanceVersion.json")
	@ResponseBody
	public BackWarnEntity copyPageInstanceVersion(Model model, 
			@RequestParam("versionId") Long versionId,
			@RequestParam("name") String name){
		return cmsPageInstanceVersionManager.copyPageInstanceVersion(versionId, name);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
