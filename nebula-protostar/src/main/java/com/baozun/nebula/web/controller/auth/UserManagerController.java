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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.UserCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.UserManagerCommand;
import com.baozun.nebula.web.command.UserRoleCommand;

/**
 * 用户管理
 * 
 * @author Justin
 */
@Controller
public class UserManagerController{

	@Autowired
	private OrganizationManager	organizationManager;

	@Autowired
	private UserManager			userManager;

	@Autowired
	private RoleManager			roleManager;

	@Autowired
	private ChooseOptionManager	chooseOptionManager;

	Integer						defaultSize	= 10;

	private SimpleDateFormat	ymdSdf		= new SimpleDateFormat("yyyy-MM-dd");

	// private ObjectMapper objectMapper=new ObjectMapper();

	/**
	 * 显示列表页
	 * 
	 * @param userCommand
	 * @param start
	 * @param size
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/list.htm")
	public String findUserList(

	@ModelAttribute("userCommand") UserManagerCommand userCommand,Model model,

	HttpServletRequest request,HttpServletResponse response){
		List<Organization> list = organizationManager.findAllOrganization();

		model.addAttribute("orgList", list);

		String language = LocaleContextHolder.getLocale().toString();

		List<ChooseOption> avaCoList = chooseOptionManager.findEffectChooseOptionListByGroupCode("IS_AVAILABLE", language);

		model.addAttribute("avaCoList", avaCoList);

		return "auth/user/list";
	}

	/**
	 * ajax获取用户列表
	 * 
	 * @param userCommand
	 * @param start
	 * @param size
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/list.json")
	@ResponseBody
	public Pagination<UserCommand> findUserListJson(

	@QueryBeanParam QueryBean queryBean,Model model,HttpServletRequest request,HttpServletResponse response){

		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("us.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		Pagination<UserCommand> userPagi = userManager.findUserList(queryBean.getPage(), sorts, queryBean.getParaMap());

		return userPagi;
	}

	@RequestMapping("/user/org-list.json")
	@ResponseBody
	public List<Organization> findOrgList(@RequestParam(value = "orgName",required = false) String orgName){

		List<Organization> list = null;
		if (StringUtils.isNotBlank(orgName)){
			list = organizationManager.findByName(orgName);
		}else{
			list = organizationManager.findAllOrganization();
		}

		return list;
	}

	/**
	 * 启用或禁用用户
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/enable-or-disable.json")
	@ResponseBody
	public Map<String, Object> enableOrDisableUser(
			@RequestParam("ids") String ids,
			@RequestParam("state") Integer state,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		Map<String, Object> result = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(ids)){

			String[] idArray = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String sid : idArray){

				idList.add(Long.parseLong(sid));
			}
			userManager.enableOrDisableByIds(idList, state);

			result.put("result", "success");

		}

		return result;
	}

	private void processOrgType(Model model) throws Exception{

		// String language=LocaleContextHolder.getLocale().toString();

		List<OrgType> coList = organizationManager.findAllOrgType();

		List<Map<String, Object>> coMapList = new ArrayList<Map<String, Object>>();

		for (int i = coList.size() - 1; i >= 0; i--){
			OrgType co = coList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", co.getId());
			map.put("name", co.getName());
			coMapList.add(map);
		}

		JSONArray jsonArray = new JSONArray(coMapList, "***");
		String coListStr = jsonArray.toString();
		model.addAttribute("coList", coListStr);
	}

	/**
	 * 进入添加用户页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/to-create.htm")
	public String toCreateUser(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<Organization> orgList = organizationManager.findAllOrganization();
		model.addAttribute("orgList", orgList);

		processOrgType(model);
		return "auth/user/create";
	}

	/**
	 * 添加用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/create.htm")
	public String createUser(
			Model model,
			@ModelAttribute("userCommand") UserManagerCommand userCommand,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		// 做基本的非空及密码一致性验证
		if (StringUtils.isBlank(userCommand.getUserName()) || StringUtils.isBlank(userCommand.getPassword())
				|| StringUtils.isBlank(userCommand.getPasswordAgain()) || !userCommand.getPasswordAgain().equals(userCommand.getPassword())){
			throw new Exception();
		}

		Boolean result = userManager.volidateUserName(userCommand.getUserName());
		if (result){
			throw new BusinessException(ErrorCodes.USER_USERNAME_EXISTS);
		}

		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

		User user = new User();
		user.setId(userCommand.getUserId());
		user.setCreateTime(new Date());
		user.setEmail(userCommand.getEmail());
		user.setLifecycle(User.LIFECYCLE_ENABLE);
		user.setMobile(userCommand.getMobile());
		user.setPassword(encoder.encodePassword(userCommand.getPassword(), userCommand.getUserName()));
		user.setRealName(userCommand.getRealName());
		user.setUserName(userCommand.getUserName());
		user.setOrgId(userCommand.getOrgId());

		userManager.createOrUpdateUser(user);

		return "redirect:/user/to-update.htm?userId=" + user.getId();
	}

	/**
	 * 修改用户
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/update.htm")
	public String updateUser(
			Model model,
			@ModelAttribute("userCommand") UserManagerCommand userCommand,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		// 做基本的非空及密码一致性验证
		if (StringUtils.isBlank(userCommand.getUserName())

		){
			throw new Exception("参数不合法");
		}

		User user = userManager.getUserById(userCommand.getUserId());

		// 如果用户名称发生过改变,就需要验证重名的问题
		if (!user.getUserName().equals(userCommand.getUserName())){
			Boolean result = userManager.volidateUserName(userCommand.getUserName());
			if (result){
				throw new BusinessException(ErrorCodes.USER_USERNAME_EXISTS);
			}
		}

		user.setEmail(userCommand.getEmail());
		user.setLifecycle(User.LIFECYCLE_ENABLE);
		user.setMobile(userCommand.getMobile());
		// user.setPassword(userCommand.getPassword());
		user.setRealName(userCommand.getRealName());
		user.setUserName(userCommand.getUserName());

		userManager.createOrUpdateUser(user);

		return "redirect:/user/list.htm?keepfilter=true";
	}

	/**
	 * 进入修改用户页面
	 * 
	 * @param model
	 * @param userId
	 *            用户id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/to-update.htm")
	public String toUpdateUser(Model model,@RequestParam("userId") Long userId,HttpServletRequest request,HttpServletResponse response)
			throws Exception{

		// 获取当前用户信息
		User user = userManager.getUserById(userId);

		processUserRoleList(model, userId);
		List<Organization> orgList = organizationManager.findAllOrganization();

		model.addAttribute("orgList", orgList);

		model.addAttribute("user", user);

		processOrgType(model);
		return "auth/user/update";
	}

	/**
	 * 进入修改密码页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/to-reset-passwd.htm")
	public String toResetUserPasswd(Model model,@RequestParam("userId") Long userId,HttpServletRequest request,HttpServletResponse response)
			throws Exception{

		// 获取当前用户信息
		User user = userManager.getUserById(userId);

		if (user == null){
			throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		}

		model.addAttribute("user", user);
		return "auth/user/reset-passwd";
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/reset-passwd.htm")
	public String resetUserPasswd(
			Model model,
			@ModelAttribute("userCommand") UserManagerCommand userCommand,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		// 做基本的非空及密码一致性验证
		if (StringUtils.isBlank(userCommand.getPassword()) || StringUtils.isBlank(userCommand.getPasswordAgain())
				|| !userCommand.getPasswordAgain().equals(userCommand.getPassword())){
			throw new Exception("参数不合法");
		}

		User user = userManager.getUserById(userCommand.getUserId());

		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

		user.setPassword(encoder.encodePassword(userCommand.getPassword(), user.getUserName()));

		userManager.createOrUpdateUser(user);

		return "redirect:/user/list.htm?keepfilter=true";
	}

	/**
	 * 将组织信息处理后存于页面,json格式
	 * 
	 * @param model
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private Map<Long, String> processOrgList(Model model) throws Exception{
		// 获取所有组织
		List<Organization> orgList = organizationManager.findAllOrganization();
		Map<Long, String> orgMap = new HashMap<Long, String>();

		List<Map<String, Object>> shopOrgList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sysOrgList = new ArrayList<Map<String, Object>>();

		for (Organization org : orgList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", org.getId());
			map.put("name", org.getName());

			if (org.getOrgTypeId() == 1){ // 如果是系统组织类型
				sysOrgList.add(map);
			}else{
				shopOrgList.add(map);
			}
			orgMap.put(org.getId(), org.getName());
		}
		JSONArray jsonArray = new JSONArray(shopOrgList, "***");

		String shopOrgListStr = jsonArray.toString();

		jsonArray = new JSONArray(sysOrgList, "***");
		String sysOrgListStr = jsonArray.toString();

		model.addAttribute("shopOrgList", shopOrgListStr);
		model.addAttribute("sysOrgList", sysOrgListStr);

		return orgMap;

	}

	/**
	 * 将角色数据处理后存于页面,json格式
	 * 
	 * @param model
	 */
	private Map<Long, Role> processRoleList(Model model) throws Exception{
		// 获取所有角色
		List<Role> roleList = roleManager.findRoleList();

		Map<Long, Role> roleMap = new HashMap<Long, Role>();

		List<Map<String, Object>> shopRoleList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sysRoleList = new ArrayList<Map<String, Object>>();

		for (Role role : roleList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", role.getId());
			map.put("name", role.getName());

			if (role.getOrgTypeId() == 1){ // 如果是系统组织类型
				sysRoleList.add(map);
			}else{
				shopRoleList.add(map);
			}

			roleMap.put(role.getId(), role);
		}

		JSONArray jsonArray = new JSONArray(shopRoleList, "***");
		String shopRoleListStr = jsonArray.toString();

		jsonArray = new JSONArray(sysRoleList, "***");
		String sysRoleListStr = jsonArray.toString();

		model.addAttribute("shopRoleList", shopRoleListStr);
		model.addAttribute("sysRoleList", sysRoleListStr);

		return roleMap;
	}

	/**
	 * 生成UserRoleCommand对象
	 * 
	 * @param roleMap
	 * @param orgMap
	 * @param ur
	 * @param no
	 * @return
	 */
	private UserRoleCommand makeUserRoleCommand(Map<Long, Role> roleMap,Map<Long, String> orgMap,UserRole ur,Integer no){

		UserRoleCommand urc = new UserRoleCommand();
		urc.setUrId(no.longValue());
		
		Role role = roleMap.get(ur.getRoleId());
		if(role==null)  return null;
		urc.setRoleId(role.getId());
		urc.setRole(role.getName());
		if (role.getOrgTypeId().equals(OrgType.ID_SYS_TYPE)){
			urc.setOrgTypeId(OrgType.ID_SYS_TYPE);
			urc.setOrgType(OrgType.NAME_SYS_TYPE);
		}else{
			urc.setOrgTypeId(OrgType.ID_SHOP_TYPE);
			urc.setOrgType(OrgType.NAME_SHOP_TYPE);
		}
		String orgName = orgMap.get(ur.getOrgId());
		if(orgName==null)  return null;
		urc.getOrgIds().add(ur.getOrgId());
		urc.setOrgs(orgName);

		return urc;
	}

	/**
	 * 将用户角色数据处理后存于页面,json格式
	 * 
	 * @param model
	 * @throws Exception
	 */
	private void processUserRoleList(Model model,Long userId) throws Exception{

		Map<Long, Role> roleMap = processRoleList(model);

		Map<Long, String> orgMap = processOrgList(model);

		List<UserRole> userRoleList = userManager.findUORByKindsId(userId, null, null);

		List<UserRoleCommand> uerRoleCommList = new ArrayList<UserRoleCommand>();

		UserRole lastUr = null;
		for (UserRole ur : userRoleList){

			// 上一个为空，或者与当前行是不同的角色
			if (lastUr == null || !lastUr.getRoleId().equals(ur.getRoleId())){
				UserRoleCommand urc=makeUserRoleCommand(roleMap, orgMap, ur, uerRoleCommList.size());
				if(urc==null)	//如果找不到对应的数据，表示可能已经被删除或禁用了，无视之
					continue;
				uerRoleCommList.add(urc);
			}else{
				// 取出最后一位
				UserRoleCommand urc = uerRoleCommList.get(uerRoleCommList.size() - 1);
				
				String orgName = orgMap.get(ur.getOrgId());
				if(orgName==null)	//如果找不到对应的数据，表示可能已被删除或禁用，无视本条数据
					continue;
					
				urc.getOrgIds().add(ur.getOrgId());
				urc.setOrgs(urc.getOrgs() + "," + orgName);
				
				
			}

			lastUr = ur;

		}

		JSONArray jsonArray = new JSONArray(uerRoleCommList, "***");
		String uerRoleCommListStr = jsonArray.toString();

		model.addAttribute("uerRoleCommList", uerRoleCommListStr);

	}

	/**
	 * 进入查看用户页面
	 * 
	 * @param model
	 * @param userId
	 *            用户id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/view.htm")
	public String viewUser(Model model,@RequestParam("userId") Long userId,HttpServletRequest request,HttpServletResponse response)
			throws Exception{

		// 获取当前用户信息
		User user = userManager.getUserById(userId);

		processUserRoleList(model, userId);
		List<Organization> orgList = organizationManager.findAllOrganization();

		model.addAttribute("orgList", orgList);

		model.addAttribute("user", user);
		model.addAttribute("statue", "view");
		return "auth/user/update";
	}

	/**
	 * 获取角色列表,如果orgTypeId为空，则查询所有角色
	 * 
	 * @param model
	 * @param orgTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/role-list.json")
	@ResponseBody
	public List<Role> findRoleList(
			Model model,
			@RequestParam(value = "orgTypeId",required = false) Long orgTypeId,
			HttpServletRequest request,
			HttpServletResponse response){

		List<Role> list = null;
		if (orgTypeId != null){
			list = roleManager.findByOrgaTypeId(orgTypeId);
		}else{
			list = roleManager.findRoleList();
		}

		return list;
	}

	/**
	 * 获取用户角色关系(修改时使用)
	 * 
	 * @param model
	 * @param orgTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/user-role-list.json")
	@ResponseBody
	public Map<String, Integer> findUserRoleList(
			Model model,
			@RequestParam(value = "userId",required = false) Long userId,
			HttpServletRequest request,
			HttpServletResponse response){

		List<UserRole> list = userManager.findUORByKindsId(userId, null, null);

		Map<String, Integer> urMap = new HashMap<String, Integer>();

		for (UserRole ur : list){
			urMap.put(ur.getOrgId() + "-" + ur.getRoleId(), 1);
		}

		return urMap;
	}

	/**
	 * 验证用户登录名称
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/validate-login.json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> volidateLogin(
			@RequestParam(required = false) String name,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String, String> urMap = new HashMap<String, String>();
		urMap.put("result", "failure");

		Boolean result = userManager.volidateUserName(name);
		if (result){

			urMap.put("result", "success");
		}

		return urMap;
	}

	/**
	 * 保存用户角色
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/save-user-role.json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> saveUserRole(
			@RequestParam("orgs") String orgs,
			@RequestParam("userId") Long userId,
			@RequestParam("roleId") Long roleId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> urMap = new HashMap<String, String>();

		urMap.put("result", "success");
		userManager.createOrUpdateUserRole(userId, roleId, orgs);

		return urMap;
	}

	/**
	 * 删除用户角色
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/remove-user-role.json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> removeUserRole(
			@RequestParam("orgs") String orgs,
			@RequestParam("userId") Long userId,
			@RequestParam("roleId") Long roleId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> urMap = new HashMap<String, String>();

		userManager.removeUserRole(userId, roleId);

		// model.addAttribute("flag", "1");
		urMap.put("result", "success");

		return urMap;
	}
	
	/**
	 * 验证组织与角色是否一致
	 * @return	:Object
	 * @date 2014-2-26 下午07:51:39
	 */
	@RequestMapping(value="/user/validate-org-role.json", method=RequestMethod.GET)
	@ResponseBody
	public Object validateOrgRole(@RequestParam("orgTypeId") Long orgTypeId, @RequestParam("orgs") String orgs){
		Boolean isOrgRoleAccordance = organizationManager.findOrganizationByOrgIdAndorgs(orgTypeId, orgs);
		return isOrgRoleAccordance;
	}
}
