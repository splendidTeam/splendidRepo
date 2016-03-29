package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.LoginSuccessEvent;
import com.feilong.servlet.http.SessionUtil;

public abstract class NebulaAbstractLoginController extends BaseController {
	
	/**
	 * 重置会话
	 * 
	 * @param request
	 * @param details
	 */
	protected void resetSession(HttpServletRequest request) {
		SessionUtil.replaceSession(request);
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
	
	/**
	 * 构造MemberDetails
	 * 
	 * @param member
	 * @return
	 */
	protected MemberDetails constructMemberDetails(MemberCommand member){
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setActived(this.isActivedMember(member));
		memberDetails.setLoginName(member.getLoginName());
		memberDetails.setLoginMobile(member.getLoginMobile());
		memberDetails.setLoginEmail(member.getLoginEmail());
		memberDetails.setNickName(member.getLoginName());
		memberDetails.setMemberId(member.getId());
		memberDetails.setRealName(member.getRealName());
		return memberDetails;
	}
	
	/**
	 * 用户是否激活
	 * 
	 * @param member
	 * @return
	 */
	protected boolean isActivedMember(MemberCommand member){
		// TODO 判断逻辑
		return false;
	}
}
