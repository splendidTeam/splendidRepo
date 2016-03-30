package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.command.member.ThirdPartyMemberCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;

/**
 * 第三方登录相关控制器
 * 第三方登录目前默认都是OAuth模式，主要有以下步骤：
 * 1. 去往第三方登录页面，根据第三方的需求提交相应参数 （URL构造在 constructOAuthLoginURL 方法内完成，需要在对应类中实现 ）
 * 2. 登录完成后，第三方会发送请求到 回调地址 来进行后续操作。请注意回调地址的设定有两个位置：Controller的方法注解，和对应对接方的回调URL参数设置
 * 3. 回调响应的默认方法为 loginWithCallBack，之中先判断登录是否成功，如果成功，会根据是否是第一次登录进行后续的登录处理
 * 
 * @author jeally
 * @author Benjamin.Liu
 * @version 2016年3月24日
 */
public abstract class NebulaOAuthLoginController extends NebulaAbstractLoginController{
	
	private static final Logger LOG = LoggerFactory.getLogger(NebulaOAuthLoginController.class);
	
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
	protected abstract String constructOAuthLoginURL();
	
	/**
	 * 校验授权并组装信息。如果验证失败返回null，否则返回封装好的第三方用户信息
	 * @return
	 */
	protected abstract ThirdPartyMemberCommand checkOAuthLogin(HttpServletRequest request);
	
	/**
	 * 根据回调处理登录动作，需要在具体项目中指定回调地址，示例配置为
	 * @RequestMapping(value = "/member/weiboLoginCallBack", method =  RequestMethod.POST)
	 * 请注意配置的URL需和第三方登录配置文件中对应第三方回调地址一致
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String loginWithCallBack(HttpServletRequest request,HttpServletResponse response,Model model){
			//校验授权
			ThirdPartyMemberCommand member=this.checkOAuthLogin(request);
			if(member == null){
				return VIEW_MEMBER_LOGIN_FAIL;
			}
			
			//第三方登录校验通过后的后续操作
			LOG.debug("[{} / {}] is validated. 2=QQ,3=SINA,4=ALIPAY,5=WECHAT", member.getOpenId(), member.getSource());
			return onOAuthSuccess(member,request, response,model);
	}
	
	/**
	 * 第三方登录校验通过后的后续操作
	 * @param thirdPartyMember
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	protected String onOAuthSuccess(ThirdPartyMemberCommand thirdPartyMember,HttpServletRequest request,HttpServletResponse response,Model model){
	
		//	查询是否存在该用户登录信息，如果用户信息存在，则取出的是目前已有的完整信息
		Member member = skdMemeberManager.findThirdMemberByThirdIdAndSource(thirdPartyMember.getOpenId(),
				thirdPartyMember.getSource());
		
		//	首次登录
		if (member == null) {
			LOG.debug("[{} / {}] is the first time to the store", thirdPartyMember.getOpenId(), thirdPartyMember.getSource());
			//	创建用户
			member = generateThirdPartyMember(thirdPartyMember);
		}
		/*
		 * 这部分将交给成功登录后的处理器去操作
		//	是否需要完善信息 默认需要
		if (isNeedCompleteInfo(member)) {
			//	逻辑判断 是否已经完善过信息，如果完善信息直接登录，如果没有，应去完善信息页面
			return showCompleteInfo(request, response, model);
		}
		//	是否需要绑定用户 默认不需要
		if (isNeedBinding(member)) {
			LOG.info("openId:{} begin bind", thirdPartyMember.getOpenId());
			model.addAttribute("member_id", member.getId());
			return showBinding(request, response, model);
		}
		*/
	
		//	第三方登录
		doLogin(request, response, model, member);
		
		//	这里应该跟正常登录逻辑保持一致，返回指定的URL
		return VIEW_MEMBER_LOGIN_SUCC;
	}
	/**
	 * 是否需要绑定
	 * @return
	 */
	protected boolean isNeedBinding(Member member) {
		return false;
	}
	
	/**
	 * 是否需要补全用户信息
	 * @return
	 */
	protected boolean isNeedCompleteInfo(Member member) {
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

	//TODO 把构建和保存分开，保存写在基类中，构建可以支持继承类扩展
	/**
	 * 保存第三方用户信息，这里在后续继承类中需要补充可以拿到的所有相关信息，默认只拿了OpenId
	 * @param thirdPartyMember
	 */
	protected Member generateThirdPartyMember(ThirdPartyMemberCommand thirdPartyMember){
	
		Member member = new Member();
		member.setThirdPartyIdentify(thirdPartyMember.getOpenId());
		member.setSource(thirdPartyMember.getSource());
		//TODO 到底该咋创建这个用户捏?
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
		
		//	同步购物车 暂时省略
		MemberCommand memberCommand = skdMemeberManager.findMemberById(member.getGroupId());
		
		//	登录成功后处理
		super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response);
	}
}
