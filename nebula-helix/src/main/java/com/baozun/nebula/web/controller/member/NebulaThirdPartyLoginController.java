package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.baozun.nebula.command.member.TirdPartyMemberCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;

/**
 * 第三方登录相关控制器
 * @author jeally
 * @version 2016年3月24日
 */
public abstract class NebulaThirdPartyLoginController extends NebulaAbstractLoginController{
	
	/* 用户补全信息页面的默认定义*/
	public static final String VIEW_MEMBER_COMPLETEINFO	=	"member.completeInfo";
	
	/* 第三方帐号绑定页面的默认定义*/
	public static final String VIEW_MEMBER_BINDING	=	"member.binding";
	
	/** 
	 * 构建去第三方登录url
	 * @return
	 */
	protected abstract String showTirdParty();
	
	/**
	 * 校验授权并组装信息
	 * @return
	 */
	protected abstract TirdPartyMemberCommand checkOauth(HttpServletRequest request);
	
	/**
	 * 第三方登录
	 * @param tirdPartyMember
	 * @return
	 */
	protected String thirdParyLogin(TirdPartyMemberCommand tirdPartyMember,HttpServletRequest request,HttpServletResponse response,Model model){
		
		//是否需要完善信息 默认需要
		if(isNeedCompleteInfo()){
			//TODO 逻辑判断 是否已经完善过信息，如果完善信息直接登录，如果没有，应去完善信息页面
			return showCompleteInfo(request, response, model);
		}
		//是否需要绑定用户 默认不需要
		if(isNeedBinding()){
			return showBinding(request, response, model);
		}
		
		//第三方登录
		//TODO 业务方法
		
		//登录成功后的认证
//		super.onAuthenticationSuccess(memberDetails, request, response);
		
		//这里应该跟正常登录逻辑保持一致，返回指定的URL
		return "";
	}
	/**
	 * 是否需要绑定
	 * @return
	 */
	protected boolean isNeedBinding() {
		return false;
	}
	
	/**
	 * 是否需要补全用户信息
	 * @return
	 */
	protected boolean isNeedCompleteInfo() {
		return true;
	}
	
	/**
	 * 去绑定页面，默认推荐配置如下
	 * @RequestMapping(value = "/member/binding", method =  RequestMethod.GET)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	protected String showBinding(HttpServletRequest request,HttpServletResponse response,Model model) {
		//TODO 
		return VIEW_MEMBER_BINDING;
	}
	/**
	 * 绑定动作
	 * @return
	 */
	protected NebulaReturnResult binding() {
		//TODO 
		return DefaultReturnResult.SUCCESS;
	}
	
	/**
	 * 去完善信息页面，默认推荐配置如下
	 * @RequestMapping(value = "/member/completeInfo", method =  RequestMethod.GET)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	protected String showCompleteInfo(HttpServletRequest request,HttpServletResponse response,Model model) {
		return VIEW_MEMBER_COMPLETEINFO;
	}
	
	/**
	 * 完善用户信息，默认推荐配置如下
	 * @RequestMapping(value = "/member/completeInfo.json", method =  RequestMethod.POST)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	protected NebulaReturnResult completeInfo(HttpServletRequest request,HttpServletResponse response,Model model) {
		return null;
	}

}
