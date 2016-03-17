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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.command.MenuQueryCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.manager.baseinfo.MenuManager;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.model.baseinfo.Menu;
import com.baozun.nebula.sdk.manager.SdkPrivilegeUrlManager;
import com.baozun.nebula.utils.spring.AnnotationScanningUtil;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * @author songdianchao 登录，认证， 授权相关
 */
@Controller
public class MenuController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuManager menuManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private OrganizationManager organizationManager;
	
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private SdkPrivilegeUrlManager sdkPrivilegeUrlManager;

	@RequestMapping("/menu/list.htm")
	public String index(HttpServletRequest request, Model model) {
		List<MenuCommand> menuCommandList = menuManager.getAllMenus();
		for (MenuCommand menuCommand : menuCommandList) {
			if (menuCommand.getParentId() == null) {
				menuCommand.setParentId(0l);
			}
		}
		//菜单类型
		model.addAttribute("menuType", organizationManager.findAllOrgType());
		//分组名称
		List<Privilege> privileges =  roleManager.findAllPrivilege();
		Set<String> list  =new HashSet<String>();
		if(privileges != null){
			for (Privilege p : privileges) {
				list.add(p.getGroupName());
			}
		}
		List<String> urls =  AnnotationScanningUtil.getRequestMappingUrls(Controller.class, "com.baozun.nebula.web.controller");
		model.addAttribute("urls", urls);
		model.addAttribute("privileges", list);
		//菜单树
		model.addAttribute("menuList", menuCommandList);
		return "/auth/menu/menu";
	}

	@RequestMapping("/menu/getMenuByUrl.json")
	@ResponseBody
	public MenuQueryCommand getMenuByUrl(String url,String name,Long id) {
		log.debug("url:{}",url);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if("".equals(url)){
			url = null; 
		}else{
			name = null;
		}
		paraMap.put("url", url);
		paraMap.put("name", name);
		paraMap.put("id", id);
		Map<String, Object> paraMapUrl = new HashMap<String, Object>();
		MenuQueryCommand mqc = menuManager.getMenuByUrl(paraMap);
		if(mqc!= null && mqc.getPriId() != null){
			Long priId =  mqc.getPriId();
			paraMapUrl.put("privilege", priId);
			List<PrivilegeUrl>  privilegeUrls  = sdkPrivilegeUrlManager.findPrivilegeUrlListByQueryMap(paraMapUrl);
			mqc.setPrivilegeUrls(privilegeUrls);
		}else{
			if(mqc == null){
				if(id!=null){
					mqc = menuManager.getMenuById(id);
				}else{
					mqc = new MenuQueryCommand();
				}
			}
			mqc.setPrivilegeUrls(new ArrayList<PrivilegeUrl>());
		}
		return mqc;
	}
	/**
	 * 
	* @author 何波
	* @Description: 生成菜单sql语句
	* @param url
	* @param name
	* @param id
	* @return   
	* MenuQueryCommand   
	* @throws
	 */
	@RequestMapping("/menu/generateMenuSql.json")
	@ResponseBody
	public String generateMenuSql(String url,String name,Long id,HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		log.debug("url:{}",url);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if("".equals(url)){
			url = null; 
		}else{
			name = null;
		}
		paraMap.put("url", url);
		paraMap.put("name", name);
		paraMap.put("id", id);
		Map<String, Object> paraMapUrl = new HashMap<String, Object>();
		MenuQueryCommand mqc = menuManager.getMenuByUrl(paraMap);
		if(mqc!= null && mqc.getPriId() != null){
			Long priId =  mqc.getPriId();
			paraMapUrl.put("privilege", priId);
			List<PrivilegeUrl>  privilegeUrls  = sdkPrivilegeUrlManager.findPrivilegeUrlListByQueryMap(paraMapUrl);
			mqc.setPrivilegeUrls(privilegeUrls);
		}else{
			if(mqc == null){
				if(id!=null){
					mqc = menuManager.getMenuById(id);
				}else{
					mqc = new MenuQueryCommand();
				}
			}
			mqc.setPrivilegeUrls(new ArrayList<PrivilegeUrl>());
		}
		String sql = generateSql(mqc);
		try {
			response.getWriter().write(sql);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private  String generateSql(MenuQueryCommand mqc){
		//菜单sql
		StringBuilder menu = new StringBuilder();
		menu.append("insert into t_au_menu(id,lifecycle,sort_no,url,version,parent_id,icon,label) values (");
		menu.append(mqc.getId()+",");
		menu.append(1+",");
		menu.append("'"+mqc.getSortNo()+"',");
		menu.append("'"+mqc.getUrl()+"',");
		menu.append("now(),");
		menu.append(mqc.getParentId()+",");
		String icon = mqc.getIcon();
		if(icon==null){
			menu.append(""+icon+",");
		}else{
			menu.append("'"+icon+"',");
		}
		menu.append("'"+mqc.getLabel()+"')");
		menu.append(";");
		menu.append("\n");
		//权限sql
		StringBuilder privilege = new StringBuilder();
		privilege.append("insert into t_au_privilege(id,acl,lifecycle,name,version,org_type_id,description) values (");
		privilege.append("nextval('s_t_au_privilege'),");
		privilege.append("'"+mqc.getAcl()+"',");
		privilege.append(1+",");
		privilege.append("'"+mqc.getName()+"',");
		privilege.append("now(),");
		privilege.append(mqc.getOrgType()+",");
		privilege.append("'"+mqc.getDescription()+"')");
		privilege.append(";");
		privilege.append("\n");
		//功能url
	    List<PrivilegeUrl> privilegeUrls = mqc.getPrivilegeUrls();
	    StringBuilder sqls = new StringBuilder();
	    if(privilegeUrls!=null && privilegeUrls.size() > 0){
	    	for (PrivilegeUrl p : privilegeUrls) {
	    		sqls.append("insert into t_au_privilege_url(id,url,pri_id,description) values (");
	    		sqls.append("nextval('s_t_au_privilege_url'),");
	    		sqls.append("'"+p.getUrl()+"',");
	    		sqls.append("currval('s_t_au_privilege'),");
	    		sqls.append("'"+p.getDescription()+"')");
	    		sqls.append(";");
	    		sqls.append("\n");
			}
	    }
		return menu.toString()+privilege.toString()+sqls.toString();
		
	}
	
	@RequestMapping("/menu/getPrivilegeUrlPid.json")
	@ResponseBody
	public List<PrivilegeUrl> getPrivilegeUrlPid(Long pid) {
		Map<String, Object> paraMapUrl = new HashMap<String, Object>();
		paraMapUrl.put("privilege", pid);
		List<PrivilegeUrl>  privilegeUrls  = sdkPrivilegeUrlManager.findPrivilegeUrlListByQueryMap(paraMapUrl);
		return privilegeUrls;
	}
	
	@RequestMapping("/menu/edit.json")
	@ResponseBody
	public BackWarnEntity editMenu(MenuQueryCommand model) {
		BackWarnEntity back = new BackWarnEntity();
		try {
			Menu  m = menuManager.edit(model);
			m.setParent(null);
			back.setDescription(m);
			back.setIsSuccess(true);
			return back;
		} catch (BusinessException e) {
			log.error("编辑菜单错误:"+e.getMessage());
			back.setDescription(e.getMessage());
			return back;
		}
	
	}
	
	@RequestMapping("/menu/delMenu.json")
	@ResponseBody
	public BackWarnEntity delMenu(Long menuId ,Long prId) {
		BackWarnEntity back = new BackWarnEntity();
		try {
			menuManager.delMenu(menuId, prId);
			back.setIsSuccess(true);
		} catch (DataIntegrityViolationException e) {
			 log.warn("菜单权限存在管理", e);
			 back.setIsSuccess(false);
			 back.setDescription("菜单已经被关联到角色中请先从角色中删除");
		}catch (ConstraintViolationException e) {
			 log.warn("菜单权限存在管理", e);
			 back.setIsSuccess(false);
			 back.setDescription("菜单已经被关联到角色中请先从角色中删除");
		}catch (BusinessException e) {
			 back.setIsSuccess(false);
			 back.setDescription(e.getMessage());
		}
		return back;
	}
	
	@RequestMapping("/menu/editFunction.json")
	@ResponseBody
	public BackWarnEntity editFunction(PrivilegeUrl model) {
		BackWarnEntity back = new BackWarnEntity();
		try {
			menuManager.editFunction(model);
			return SUCCESS;
		} catch (Exception e) {
			log.error("编辑功能错误:"+e.getMessage());
			back.setDescription(e.getMessage());
			return back;
		}
	}
	
	@RequestMapping("/menu/delFunction.json")
	@ResponseBody
	public BackWarnEntity delFunction(Long id) {
		menuManager.delFunction(id);
		return SUCCESS;
	}


}
