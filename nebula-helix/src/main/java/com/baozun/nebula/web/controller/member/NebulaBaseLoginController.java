package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.web.controller.BaseController;

public class NebulaBaseLoginController extends BaseController{

	/**
	 * 重置会话信息
	 * @param request
	 * @param details
	 */
	protected void resetSessionMember(HttpServletRequest request,UserDetails userDetails){
		//
	}
	
	/**
	 * 同步购物车
	 * @param request
	 * @param details
	 */
	protected void syncShoppingCart(HttpServletRequest request,HttpServletResponse response,UserDetails userDetails) {
		//
	}
}
