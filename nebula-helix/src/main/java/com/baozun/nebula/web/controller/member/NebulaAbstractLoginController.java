package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.LoginSuccessEvent;

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
	
	/**
	 * 认证成功，未激活用户也认为是认证成功，如果需要特殊处理未激活用户请重写此方法
	 * 
	 * @param memberDetails
	 * @param request
	 * @param response
	 */
	protected NebulaReturnResult onAuthenticationSuccess(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response) {
		resetSession(request);
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_CONTEXT, memberDetails);
		// 触发登录成功事件，用于异步处理其他的业务
		eventPublisher.publish(new LoginSuccessEvent(memberDetails, getClientContext(request, response)));
		return DefaultReturnResult.SUCCESS;
	}
}
