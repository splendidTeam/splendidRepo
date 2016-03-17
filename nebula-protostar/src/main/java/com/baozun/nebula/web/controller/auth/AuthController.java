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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.auth.PrivilegeCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.manager.baseinfo.MenuManager;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.sdk.manager.SdkPrivilegeManager;
import com.baozun.nebula.sdk.manager.SdkPrivilegeUrlManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.utils.spring.AnnotationScanningUtil;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
* @Description: 权限管理
* @author 何波
* @date 2014年12月1日 下午5:47:33 
*
 */
@Controller
public class AuthController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired
	private MenuManager menuManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private OrganizationManager organizationManager;
	
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private SdkPrivilegeUrlManager sdkPrivilegeUrlManager;

	@Autowired
	private SdkPrivilegeManager sdkPrivilegeManager;
	
	
	@RequestMapping("/auth/list.htm")
	public String index(HttpServletRequest request, Model model) {
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
		
		return "/auth/privilege/list";
	}

	@RequestMapping("/auth/page.json")
	@ResponseBody
	public Pagination<PrivilegeCommand> findPageInstanceListByTemplateId(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (null == sorts || sorts.length == 0) {
			Sort sort = new Sort("id", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		Pagination<PrivilegeCommand> pagination = sdkPrivilegeManager
				.findPrivilegeCommandPageByQueryMap(queryBean.getPage(), sorts, queryBean.getParaMap());
		return pagination;
	}
	@RequestMapping("/auth/removePrivilegeByIds.json")
	@ResponseBody
	public BackWarnEntity removePrivilegeByIds(Long[] ids){
		BackWarnEntity back = new BackWarnEntity();
		if(ids == null || ids.length == 0){
			back.setDescription("请选择需要删除的数据");
			return back;
		}
		sdkPrivilegeManager.removePrivilegeByIds(Arrays.asList(ids));
		back.setIsSuccess(true);
		return back;
	}
	@RequestMapping("/auth/enableOrDisableById.json")
	@ResponseBody
	public BackWarnEntity enableOrDisableById(Long id,int state){
		BackWarnEntity back = new BackWarnEntity();
		if(id == null){
			back.setDescription("请选择需要启用或禁用的的数据");
			return back;
		}
		sdkPrivilegeManager.enableOrDisableById(id,state);
		back.setIsSuccess(true);
		return back;
	}
	
	@RequestMapping("/auth/editPrivilege.json")
	@ResponseBody
	public BackWarnEntity editPrivilege(@RequestBody PrivilegeCommand  privilege) {
		try {
			sdkPrivilegeManager.editPrivilege(privilege);
		} catch (BusinessException e) {
			BackWarnEntity back = new BackWarnEntity();
			back.setDescription(e.getMessage());
			return back;
		}
		return SUCCESS;
	}
	
	@RequestMapping("/auth/existUrl.json")
	@ResponseBody
	public BackWarnEntity existUrl(String url,Long parentId) {
		BackWarnEntity back = new BackWarnEntity();
		try {
			menuManager.check(null ,url,null,parentId);
			return SUCCESS;
		} catch (BusinessException e) {
			log.error("验证url:"+e.getMessage());
			back.setDescription(e.getMessage());
			return back;
		}
	}
	
	/**
	 * 
	* @author 何波
	* @Description: 生成权限sql语句
	* @param url
	* @param name
	* @param id
	* @return   
	* MenuQueryCommand   
	* @throws
	 */
	@RequestMapping("/auth/generateMenuSql.json")
	@ResponseBody
	public String generateMenuSql(Long id,HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		Privilege privilege = sdkPrivilegeManager.findPrivilegeById(id);
		Map<String, Object> paraMapUrl = new HashMap<String, Object>();
		paraMapUrl.put("privilege", id);
		List<PrivilegeUrl>  privilegeUrls  = sdkPrivilegeUrlManager.findPrivilegeUrlListByQueryMap(paraMapUrl);
		String sql = generateSql(privilege,privilegeUrls);
		try {
			response.getWriter().write(sql);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private  String generateSql(Privilege mqc,List<PrivilegeUrl> privilegeUrls){
		//权限sql
		StringBuilder pri = new StringBuilder();
		pri.append("insert into t_au_privilege(id,acl,lifecycle,name,version,org_type_id,description) values (");
		pri.append("nextval('s_t_au_privilege'),");
		pri.append("'"+mqc.getAcl()+"',");
		pri.append(1+",");
		pri.append("'"+mqc.getName()+"',");
		pri.append("now(),");
		pri.append(mqc.getOrgType().getId()+",");
		pri.append("'"+mqc.getDescription()+"')");
		pri.append(";");
		pri.append("\n");
		//功能url
	    StringBuilder sqls = new StringBuilder();
	    if(privilegeUrls != null && privilegeUrls.size() > 0){
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
		return pri.toString()+sqls.toString();
		
	}
	

}
