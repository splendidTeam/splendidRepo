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
package com.baozun.nebula.web.controller.system;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 通用选项
 * @author xingyu.liu
 *
 */
@Controller
public class ChooseOptionController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ChooseOptionController.class);

	@Autowired
	private ChooseOptionManager chooseOptionManager;
	
	/**
	 * 选项分组列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/option/optionGroupList.htm")
	public String optionGroupList(){
		return "/system/option/choose-option-group";
	}
	
	@RequestMapping("/option/optionGroupList.json")
	@ResponseBody
	public List<OptionGroupCommand> optionGroupListJson(@QueryBeanParam QueryBean queryBean){
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("t.group_code","asc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		List<OptionGroupCommand> pageOptionCommandList = chooseOptionManager.findOptionGroupListByQueryMapWithPage(sorts, queryBean.getParaMap());

		return pageOptionCommandList;
	}
	/**
	 * 新建分组及选项
	 * @return
	 */
	@RequestMapping(value = "/option/createOptionGroup.htm")
	public String createOptionGroup(){
		return "/system/option/add-choose-option-group";
	}
	
	
	/**
	 * 保存或更新通用选项及分组
	 * @param chooseOptions
	 * @param groupCode
	 * @param groupDesc
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/option/saveOptionGroup.json", method = RequestMethod.POST)
	@ResponseBody
	public Object saveOptionGroup(@ArrayCommand() ChooseOption[] chooseOptions,
			@RequestParam("groupCode")String groupCode,
			@RequestParam("groupDesc")String groupDesc,
			Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		for(ChooseOption cp:chooseOptions){
			cp.setGroupCode(groupCode);
			cp.setGroupDesc(groupDesc);
			cp.setLabelLang("zh-CN");
			cp.setLifecycle(ChooseOption.LIFECYCLE_ENABLE);
		}

		
		chooseOptionManager.createOrUpdateOptionGroup(chooseOptions);
		
		return SUCCESS;
	}
	
	/**
	 * 验证通用选项编码存在，返回success表示通过验证
	 * @param groupCode
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	
	
	@RequestMapping(value = "/option/validateOptionGroupCode.json",method = RequestMethod.GET)
	@ResponseBody
	public Object validateOptionGroupCode(
			@RequestParam("groupCode") String groupCode,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		
		Boolean existFlag = chooseOptionManager.validateOptionGroupCode(groupCode);
		if (existFlag){
			//throw new BusinessException(ErrorCodes.NAME_EXISTS);
			return FAILTRUE;
		}

		return SUCCESS;
	}
	
	/**
	 * 是否有效分组通用选项列表
	 * @return
	 */
	@RequestMapping(value = "/option/option-list.htm")
	public String optionList(@RequestParam("groupCode")String groupCode,@RequestParam(value="groupDesc",required=false)String groupDesc,Model model){

		 model.addAttribute("groupCode",groupCode);
		 model.addAttribute("groupDesc",groupDesc);
	     
		 return "/system/option/choose-option-list";
	}
	
	
	/**
	 * 保存或更新通用选项及分组
	 * @param querybean
	 */
	@RequestMapping("/option/chooseOptionList.json")
	@ResponseBody
	public Pagination<ChooseOption> chooseOptionList(@RequestParam("groupCode")String groupCode,Model model,@QueryBeanParam QueryBean queryBean) {
	
		    Sort[] sorts=queryBean.getSorts();
			
			if(sorts==null||sorts.length==0){
				Sort sort=new Sort("sort_no","asc");
				sorts=new Sort[1];
				sorts[0]=sort;
			}
			
		    Pagination<ChooseOption> args = chooseOptionManager.findOptionListByQueryMapWithPage(groupCode,queryBean.getPage(), sorts, queryBean.getParaMap());
	        model.addAttribute("groupCode", groupCode);

	        return args;
	}
	
	
	
	/**
	 * 根据ID启用 禁用
	 * @param groupId
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/option/enableOrDisableOption.json")
	public Object enableOrDisableOptionByIds(@RequestParam("groupId") Long groupId,
			@RequestParam("state") Integer state) { 
		List<Long> ids = new ArrayList<Long>();
		ids.add(groupId);

		Integer result = chooseOptionManager.enableOrDisableOptionByIds(ids, state);
		if (result < 1) {
			if(state!=1){
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DISABLED_FAIL);
			}else{
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY__ENABLE_FAIL);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 根据ID获取 
	 * @param groupId
	 * @param state
	 * @return
	 */
	@RequestMapping("/option/updateOption.htm")
	public String UpdateOption(Model model,@RequestParam("groupId") Long groupId,HttpServletRequest request,HttpServletResponse response)
			throws Exception{

		// 获取当前用户信息
		ChooseOption chooseOption = chooseOptionManager.findChooseOptionById(groupId);
		model.addAttribute("chooseOption", chooseOption);

		return "system/option/update-choose-option";
	}
	
	
	
	/**
     * 新增商品页面
     * @param groupCode
	 * @return
	 */
	@RequestMapping(value = "/option/createOption.htm")
	public String createOption(Model model,@RequestParam("groupCode") String groupCode,@RequestParam("groupDesc")String groupDesc)
			throws Exception{
		ChooseOption chooseOption =new ChooseOption();
		
		chooseOption.setGroupCode(groupCode);
		chooseOption.setLifecycle(ChooseOption.LIFECYCLE_ENABLE);
		chooseOption.setGroupDesc(groupDesc);
		chooseOption.setLifecycle(ChooseOption.LIFECYCLE_ENABLE);
        model.addAttribute("chooseOption", chooseOption);
		return "system/option/create-option";
	}

	
	/**
	 * 保存
	 * @param model
	 * @param ChooseOption
	 * @return
	 */
	@RequestMapping(value="/option/saveOption.json",method = RequestMethod.POST)
	@ResponseBody
	public Object saveOption(Model model,@ModelAttribute()ChooseOption chooseOption,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		
	
		chooseOptionManager.createOrUpdateOption(chooseOption);
		model.addAttribute("chooseOption", chooseOption);
		
		return SUCCESS;
	}
	
	/**
	 * 验证名称或者值是否存在
	 * @param optionLabel
	 * @param optionValue
	 * @return
	 */
	@RequestMapping(value ="/option/validateOptionValue.json",method = RequestMethod.GET)
	@ResponseBody
	public Object validateOptionValue(@RequestParam("groupCode") String groupCode,@RequestParam("optionValue") String optionValue,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
	
		
		Boolean existFlag = chooseOptionManager.validateOptionValue(groupCode,optionValue);
		if (existFlag){

			return SUCCESS;
		}

		return FAILTRUE;
	}
	
	/**
	 * 通过groupCode查询选项信息
	 * @param groupCode
	 * @return	:Object
	 * @date 2014-2-25 下午02:38:37
	 */
	@RequestMapping(value = "/option/findChooseOptionByGroupCode.json", method = RequestMethod.POST)
	@ResponseBody
	public Object findChooseOptionByGroupCode(@RequestParam("groupCode") String groupCode){
		List<ChooseOption> chooseOptionList = chooseOptionManager.findOptionListByGroupCode(groupCode);
		return chooseOptionList;
	}
	
	/**
	 * 查询所有的选项信息
	 * @return	:Object
	 * @date 2014-2-25 下午03:51:05
	 */
	@RequestMapping(value = "/option/findAllOptionList.json", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllOptionList(){
		List<ChooseOption> chooseOptionList = chooseOptionManager.findAllOptionList();
		return chooseOptionList;
	}
}
