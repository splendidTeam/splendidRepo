/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.baozun.nebula.manager.auth.UserManager;
import com.baozun.nebula.web.UserDetails;

/**
 * @author songdianchao 授权成功,记录登录日志,设置图片空间权限,并跳转到首页
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UserManager	userManager;

	@Value("#{meta['image.space.adminrole']}")
	private String		imageSpaceRole	= "";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,Authentication authentication)
			throws IOException,ServletException{
		UserDetails user = (UserDetails) authentication.getPrincipal();
		userManager.loginLog(user.getUserId(), request.getRemoteAddr(), request.getSession().getId());

		// // 设置图片空间权限
		// // 配置ckfinder用户角色,只有admin角色拥有目录删除和图片删除的权限,未登陆用户没有任何权限.
		// request.getSession().setAttribute("CKFinder_UserRole",
		// imageSpaceRole.equals(user.getUsername()) ? "admin" : "user");

		// 修改成可以给多个用户赋予admin操作权限，配置时用户之间用逗号分割
		String[] split = imageSpaceRole.split(",");
		List<String> asList = Arrays.asList(split);
		if (asList.contains(user.getUsername())) {
			// 设置图片空间权限
			// 配置ckfinder用户角色,只有admin角色拥有目录删除和图片删除的权限,未登陆用户没有任何权限.
			request.getSession().setAttribute("CKFinder_UserRole", "admin");
		} else {
			// 设置图片空间权限
			// 配置ckfinder用户角色,只有admin角色拥有目录删除和图片删除的权限,未登陆用户没有任何权限.
			request.getSession().setAttribute("CKFinder_UserRole", "user");
		}

		response.sendRedirect("index.htm");
	}

}
