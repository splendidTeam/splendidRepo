package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.web.controller.BaseController;

public abstract class NebulaAbstractLoginController extends BaseController {

	/**
	 * 重置会话
	 * 
	 * @param request
	 * @param details
	 */
	protected void resetSession(HttpServletRequest request) {
		// TODO copy old session value
		request.getSession().invalidate();
		// TODO add old session value to new session
	}
}
