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
package com.baozun.nebula.web.controller.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.product.PropertyController;

/**
 * @author xianze.zhang
 * @creattime 2013-6-3
 */
@Controller
public class RoleController extends BaseController{
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(PropertyController.class);

	@Autowired
	private RoleManager	roleManager;

	/**
	 * ajax获取角色列表
	 * 
	 * @param sortStr
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/list.json")
	@ResponseBody
	public Pagination<RoleCommand> findRoleListJson(
			@QueryBeanParam QueryBean queryBean,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("r.name","asc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		Pagination<RoleCommand> args=roleManager.
				findRoleCommandList(queryBean.getPage(), sorts, queryBean.getParaMap());
		return args;
	}

	/**
	 * 角色列表页
	 * 
	 * @param roleCommand
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/list.htm")
	public String findRoleList(Model model,HttpServletRequest request,HttpServletResponse response){
		return "auth/role/list";
	}

	/**
	 * 创建角色页面
	 * 
	 * @param roleCommand
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/create.htm")
	public String createRole(Model model,HttpServletRequest request,HttpServletResponse response){
		Map<Long, Map<String, List<Privilege>>> privilegeMap = roleManager.findAllPrivilegeMap();

		model.addAttribute("privilegeMap", privilegeMap);
		model.addAttribute("isupdate", "false");
		return "auth/role/create";
	}

	/**
	 * 修改角色页面
	 * 
	 * @param roleCommand
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/update.htm")
	public String updateRole(@RequestParam() Long roleId,Model model,HttpServletRequest request,HttpServletResponse response){
		Map<Long, Map<String, List<Privilege>>> privilegeMap = roleManager.findAllPrivilegeMap();

		model.addAttribute("privilegeMap", privilegeMap);
		RoleCommand roleCommand = roleManager.findRoleCommandById(roleId);
		model.addAttribute("roleCommand", roleCommand);
		model.addAttribute("isupdate", "true");
		return "auth/role/create";
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/delete.json")
	@ResponseBody
	public String deleteRole(@RequestParam("ids") String ids,Model model,HttpServletRequest request,HttpServletResponse response){
		roleManager.disableRoleByIds(ids);
		return "success";
	}

	/**
	 * 保存角色
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/role/save.json")
	@ResponseBody
	public Object saveRole(
			@ModelAttribute() Role role,
			@ArrayCommand(dataBind = true) Long[] privilegeIds,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		try {
			roleManager.saveRole(role, privilegeIds);
			return SUCCESS;
		} catch (Exception e) {
			return FAILTRUE;
		}
		
	}
}
