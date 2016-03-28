package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.command.member.TirdPartyMemberCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;

/**
 * 第三方登录相关控制器
 * @author jeally
 * @version 2016年3月24日
 */
public abstract class NebulaThirdPartyLoginController extends NebulaAbstractLoginController{
	
	private static final Logger LOG = LoggerFactory.getLogger(NebulaThirdPartyLoginController.class);
	
	/* 用户补全信息页面的默认定义*/
	public static final String VIEW_MEMBER_COMPLETEINFO	=	"member.completeInfo";
	
	/* 第三方帐号绑定页面的默认定义*/
	public static final String VIEW_MEMBER_BINDING	=	"member.binding";
	
	/* 第三方帐号登录成功跳转页面的默认定义*/
	public static final String VIEW_MEMBER_LOGIN_SUCC	=	"member.succ";
	
	/* 第三方帐号登录失败跳转页面的默认定义*/
	public static final String VIEW_MEMBER_LOGIN_FAIL	=	"member.fail";	
	
	@Autowired
	private SdkMemberManager skdMemeberManager;
	
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
		//查询是否存在该用户登录信息
		Member member = skdMemeberManager.findThirdMemberByThirdIdAndSource(tirdPartyMember.getOpenId(),tirdPartyMember.getSource());
		
		//首次登录时，创建用户
		if (member == null) {
			member = generateThirdPartyMember(tirdPartyMember);
		}
		
		// 是否需要完善信息 默认需要
		if (isNeedCompleteInfo()) {
			
			//逻辑判断 是否已经完善过信息，如果完善信息直接登录，如果没有，应去完善信息页面
			return showCompleteInfo(request, response, model);
		}
		
		// 是否需要绑定用户 默认不需要
		if (isNeedBinding()) {
			LOG.info("openId:{} begin bind", tirdPartyMember.getOpenId());
			model.addAttribute("member_id", member.getId());
			return showBinding(request, response, model);
		}
	
		// 第三方登录
		doLogin(request, response, model, member);
		//这里应该跟正常登录逻辑保持一致，返回指定的URL
		return VIEW_MEMBER_LOGIN_SUCC;
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
		return VIEW_MEMBER_BINDING;
	}
	/**
	 * 绑定动作
	 * @return
	 */
	protected NebulaReturnResult binding() {
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

	/**
	 * 保存第三方用户信息
	 * @param tirdPartyMember
	 */
	protected Member generateThirdPartyMember(TirdPartyMemberCommand tirdPartyMember){
		Member member = new Member();
		member.setThirdPartyIdentify(tirdPartyMember.getOpenId());
		member.setSource(tirdPartyMember.getSource());
		skdMemeberManager.rewriteRegister(member);
		return member;
	}
	
	/***
	 * 验证成功后登录动作
	 * @param request
	 * @param response
	 * @param model
	 * @param member
	 */
	protected void doLogin(HttpServletRequest request,HttpServletResponse response,Model model,Member member){
		//同步购物车 暂时省略
		MemberCommand memberCommand = skdMemeberManager.findMemberById(member.getGroupId());
		//登录成功后处理
		super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response);
	}
}
