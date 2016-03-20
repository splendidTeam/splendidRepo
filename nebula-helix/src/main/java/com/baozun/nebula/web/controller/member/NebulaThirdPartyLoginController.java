package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.command.member.TirdPartyMemberCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;

public abstract class NebulaThirdPartyLoginController extends NebulaAbstractLoginController{
	
	
	/** 
	 * 构建去第三方登录url
	 * @return
	 */
	public abstract String showTirdParty();
	
	/**
	 * 校验授权并组装信息
	 * @return
	 */
	public abstract TirdPartyMemberCommand checkOauth(HttpServletRequest request);
	
	/**
	 * 第三方登录
	 * @param tirdPartyMember
	 * @return
	 */
	public String thirdParyLogin(HttpServletRequest request,HttpServletResponse response,TirdPartyMemberCommand tirdPartyMember){
		//2.1查询用户信息判断是否第一次登录，如果是第一次登录，去完善信息页面。
//		if(member==null){
//			return "store.member.perfectInfo";
//		}
		
		//2.2如果已完善过信息，直接登录
		
		
		//2.2同步购物车
//		syncShoppingCart(request, userDetails);
		
		//2.3重置session
//		resetSessionMember(request, userDetails);
		
		return "store.member.info";
	}
	/**
	 * 是否需要绑定
	 * @return
	 */
	protected boolean isNeedBinding() {
		return false;
	}
	/**
	 * 绑定页面
	 * @return
	 */
	protected String showBinding() {
		return null;
	}
	/**
	 * 绑定动作
	 * @return
	 */
	protected NebulaReturnResult binding() {
		return DefaultReturnResult.SUCCESS;
	}
	/**
	 * 绑定成功
	 * @return
	 */
	protected NebulaReturnResult onBindingSuccess() {
		return DefaultReturnResult.SUCCESS;
	}
}
