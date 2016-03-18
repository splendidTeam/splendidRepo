package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.controller.member.command.TirdPartyMemberCommand;

public abstract class NebulaThirdPartyLoginController extends NebulaBaseLoginController{
	
	
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
}
