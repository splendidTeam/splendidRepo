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
 */
package com.baozun.nebula.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 行业维护controller
 * 
 * @author chen.kefan
 * @date 2013-6-27 下午14:00:00
 */
@Controller
public class IndustryController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(IndustryController.class);
	
	@Autowired
	
	private IndustryManager		industryManager;

	/**
	 * 进入行业管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/industry/industryList.htm")
	public String findAllIndustryList(Model model){		
		List<Industry> list = industryManager.findAllIndustryList();
		model.addAttribute("industryList", list);
		return "product/industry/industry";
	}
	
	/**
	 * 新增或修改行业
	 * @param pId 
	 * @param model
	 * @param name
	 * @param lifecycle
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/industry/saveIndustry.json")
	@ResponseBody
	public BackWarnEntity saveIndustry(
			@ModelAttribute  Industry industry,
			@RequestParam("ids")String ids,
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response) {
		Industry newIndustry = new Industry();
		
		if(industry.getId()!=-1){
			//修改
			newIndustry =industryManager.findIndustryById(industry.getId());
			newIndustry.setModifyTime(new Date());
		}else{
			//新增
			newIndustry.setParentId(industry.getParentId());
			newIndustry.setCreateTime(new Date());
		}	
		newIndustry.setLifecycle(industry.getLifecycle());
		newIndustry.setName(industry.getName());
		boolean isSuccess =industryManager.createOrUpdateIndustry(newIndustry,ids);
		if(!isSuccess){
			throw new BusinessException(ErrorCodes.INDUSTRY_SAVE_FAIL);
		}
		SUCCESS.setDescription(newIndustry.getId());
		return SUCCESS;
	}
	
	/**
	 * 删除行业(逻辑)
	 * @param Id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/industry/removeIndustry.json")
	@ResponseBody
	public Object removeIndustry(
			@RequestParam("id")Long id,
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		Integer count = industryManager.removeIndustryByIds(ids);
		if (count!=1){
			throw new BusinessException(ErrorCodes.INDUSTRY_REMOVE_FAIL);
		}
		return SUCCESS;
	}
	
	/**
	 * 验证行业名(同父结点)
	 * @param Id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/industry/validateIndustryName.json")
	@ResponseBody
	public BackWarnEntity validateIndustryName(
			@RequestParam("pId")Long pId,
			@RequestParam("name")String name,
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response){
		boolean isSuccess = industryManager.validateIndustryName(pId,name);
		if (!isSuccess){
			throw new BusinessException(ErrorCodes.NAME_EXISTS);
		}
		return SUCCESS; 
	}
}