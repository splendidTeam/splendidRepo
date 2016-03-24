package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.baozun.nebula.command.member.TirdPartyMemberCommand;

public class NebulaQQLoginController extends NebulaThirdPartyLoginController{

	/**
	 * 去QQ联合登陆页，默认推荐配置如下
	 * @RequestMapping(value = "/member/showQQLogin", method =  RequestMethod.POST)
	 * @return
	 */
	public String showQQLogin(){
		return "redirect:"+this.showTirdParty();
	}
	
	/**
	 * 联合登录回调地址，默认推荐配置如下
	 * @RequestMapping(value = "/member/qqCallBack", method =  RequestMethod.POST)
	 * @param request
	 * @param response
	 * @return
	 */
	public String qqLoginCallBack(HttpServletRequest request,HttpServletResponse response,Model model){
		//1.校验授权
		TirdPartyMemberCommand tirdPartyMember=this.checkOauth(request);
		
		//2.第三方登录
		
		return thirdParyLogin(tirdPartyMember, request, response, model);
	}
	
	
	@Override
	public String showTirdParty() {
		//TODO 第三方API生成URL
		return "";
	}

	@Override
	public TirdPartyMemberCommand checkOauth(HttpServletRequest request) {
		//TODO 
		//1.校验授权
		
		//2.组装信息
		
		return new TirdPartyMemberCommand();
	}
}
