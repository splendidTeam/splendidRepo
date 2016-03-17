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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.support.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.manager.baseinfo.MenuManager;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.logs.UserLoginLog;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.UserManagerCommand;
import com.baozun.nebula.web.controller.BaseController;

/**
 * @author songdianchao 登录，认证， 授权相关
 */
@Controller
public class SecurityController extends BaseController{

	private static final Logger	log	= LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	private MenuManager			menuManager;

	@Autowired
	private UserManager			userManager;

	@Autowired
	private MessageSource		messageSource;

	/**
	 * 登录入口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login.htm")
	public String login(){
		return "login";
	}

	/**
	 * 主入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/index.htm", "/", "" })
	public String index(HttpServletRequest request,Model model){
		Cookie cookie = WebUtils.getCookie(request, "orgId");
		String currentOrgId = null;
		UserDetails userDetails = this.getUserDetails();
		if (cookie != null){
			String orgId = cookie.getValue();
			if (isValid(orgId)){
				currentOrgId = orgId;
			}else{
				// return "redirect:orgList.htm";
			}
		}else{
			// return "redirect:orgList.htm";
		}

		if (currentOrgId == null){
			List<Map<String, String>> grantedOrgnizations = userDetails.getGrantedOrgnizations();
			if(grantedOrgnizations != null && !grantedOrgnizations.isEmpty()) {
				currentOrgId = grantedOrgnizations.get(0).get("id");
				for (Map<String, String> orga : grantedOrgnizations){
					if (orga.get("type") == "1"){
						currentOrgId = orga.get("id");
						break;
					}
				}
			} else {
				throw new BusinessException(ErrorCodes.ACCESS_DENIED);
			}
		}

		// 根据用户当前机构进行菜单初始化
		userDetails.setCurrentOrganizationId(Long.valueOf(currentOrgId));

		List<MenuCommand> menuCommandList = menuManager.getUserMenu(userDetails);

		String menus = JsonFormatUtil.format(menuCommandList, "***");
		log.debug("menus:{}", menus);
		
		model.addAttribute("menus", menus);

		return "index";
	}

	/**
	 * 用户是否有权限访问某个机构
	 * 
	 * @param orgCode
	 *            机构编码
	 * @return
	 */
	private boolean isValid(String orgId){
		UserDetails userDetails = this.getUserDetails();
		return userDetails.isGrantedOrgnization(orgId);
	}

	/**
	 * 用户选择机构
	 * 
	 * @param response
	 * @param orgId
	 * @return
	 */
	@RequestMapping({ "/changeOrg.htm" })
	public String change(HttpServletResponse response,@RequestParam("orgId") Long orgId){
		if (!isValid(orgId.toString())){ // 没权限
			return "redirect:orgList.htm";
		}
		// 设置用户当前机构
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName("orgId");
		cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
		cookieGenerator.addCookie(response, orgId.toString());

		UserDetails userDetails = this.getUserDetails();

		userDetails.setCurrentOrganizationId(orgId);
		return "redirect:index.htm";
	}

	/**
	 * 用户选择机构页面
	 * 
	 * @return
	 */
	@RequestMapping({ "/orgList.htm" })
	public String orgList(){
		return "orgList";
	}

	/**
	 * 无权访问，拒绝页
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/access-denied.htm")
	public ModelAndView accessDenied(HttpServletRequest request) throws IOException{
		ModelAndView mv = new ModelAndView("errors/reject");
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>(1);
		Map<String, Object> exceptionMap = new HashMap<String, Object>();
		exceptionMap.put("statusCode", ErrorCodes.ACCESS_DENIED);
		exceptionMap.put(
				"message",
				messageSource.getMessage(
						ErrorCodes.BUSINESS_EXCEPTION_PREFIX + ErrorCodes.ACCESS_DENIED,
						null,
						LocaleContextHolder.getLocale()));
		if (request.getHeader("X-Requested-With") != null){
			mv.setView(new MappingJacksonJsonView());
		}
		result.put("exception", exceptionMap);
		mv.addAllObjects(result);
		return mv;
	}

	@RequestMapping(value = "/dashboard.htm")
	public String dashboard(Model model){
		UserDetails userDetails = this.getUserDetails();
		List<UserLoginLog> logList=userManager.findUserLoginLogByUserId(userDetails.getUserId());
		if(logList!=null&&logList.size()>0)
			model.addAttribute("loginlog", logList.get(0));
		else
			model.addAttribute("loginlog", new UserLoginLog());
		
		User user=userManager.getUserById(userDetails.getUserId());
		model.addAttribute("user", user);
		return "dashboard";
	}

	
	@RequestMapping(value = "/updateUser.htm")
	public String updateUser(Model model){
		
		UserDetails userDetails = this.getUserDetails();
		
		User user = userManager.getUserById(userDetails.getUserId());
		model.addAttribute("user", user);
		
		return "updateUser";
	}
	
	@RequestMapping(value = "/saveUser.json")
	@ResponseBody
	public BackWarnEntity saveUser(Model model,@ModelAttribute("userCommand") UserManagerCommand userCommand)throws Exception{
		

		
		UserDetails userDetails = this.getUserDetails();
		
		User user = userManager.getUserById(userDetails.getUserId());

		if(StringUtils.isNotBlank(userCommand.getPassword())&&userCommand.getPassword().equals(userCommand.getPasswordAgain())){
			
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			user.setPassword(encoder.encodePassword(userCommand.getPassword(), user.getUserName()));
			
		}

		user.setEmail(userCommand.getEmail());
		user.setMobile(userCommand.getMobile());
		user.setRealName(userCommand.getRealName());
		
		userManager.createOrUpdateUser(user);
		
		return SUCCESS;
	}
	
	/**
	 * 用户修改密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/modifyPwd.htm",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> modifyPwd(
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("newPwd") String newPwd,
			@RequestParam("confirmNewPwd") String confirmNewPwd){
		Map<String, String> result = new HashMap<String, String>(1);
		if (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmNewPwd)){
			result.put("result", "0");
		}else if (!newPwd.equals(confirmNewPwd)){
			result.put("result", "0");
		}else{
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			Boolean updateResult = userManager.modifyPwd(
					this.getUserDetails().getUserId(),
					encoder.encodePassword(oldPwd, this.getUserDetails().getUsername()),
					encoder.encodePassword(confirmNewPwd, this.getUserDetails().getUsername()));
			if (updateResult){
				result.put("result", "1");
			}else{
				result.put("result", "2");
			}
		}
		return result;
	}
}
