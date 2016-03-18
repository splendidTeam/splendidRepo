package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.controller.member.command.TirdPartyMemberCommand;

public class NebulaQQLoginController extends NebulaThirdPartyLoginController{

	//去QQ联合登陆页 ，“/member/showQQLogin”
	public String showQQLogin(){
		return "redirect:"+this.showTirdParty();
	}
	
	//联合登录回调地址，“/member/qqCallBack”
	public String qqLoginCallBack(HttpServletRequest request,HttpServletResponse response){
		//1.校验授权
		TirdPartyMemberCommand tirdPartyMember=this.checkOauth(request);
		
		//2.第三方登录
		
		return thirdParyLogin(request, response, tirdPartyMember);
	}
	
	
	@Override
	public String showTirdParty() {
		//第三方API生成URL
		return "";
	}

	@Override
	public TirdPartyMemberCommand checkOauth(HttpServletRequest request) {
		//1.校验授权
		
		//2.组装信息
		
		return new TirdPartyMemberCommand();
	}
}
