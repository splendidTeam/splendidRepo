/**
 * 
 */
package com.baozun.nebula.web.controller.plugin.login;

import javax.servlet.http.HttpServletRequest;

import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baozun.nebula.web.controller.BaseController;


/**
 * @author xianze.zhang
 *@creattime 2013-8-1
 */
@Controller
public class Login extends BaseController{
	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login/login.json")
	public String login(HttpServletRequest request){
		request.getSession().setAttribute("user", "user");
		return null;
	}
	/**
	 * 注销
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login/logout.json")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute("user");
		return null;
	}
}
