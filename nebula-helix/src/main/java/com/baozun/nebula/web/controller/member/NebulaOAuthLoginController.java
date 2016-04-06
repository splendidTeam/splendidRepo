package com.baozun.nebula.web.controller.member;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.member.ThirdPartyMemberCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberFactory;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * 第三方登录相关控制器 第三方登录目前默认都是OAuth模式，主要有以下步骤： 1. 去往第三方登录页面，根据第三方的需求提交相应参数 （URL构造在 constructOAuthLoginURL 方法内完成，需要在对应类中实现 ） 2. 登录完成后，第三方会发送请求到
 * 回调地址 来进行后续操作。请注意回调地址的设定有两个位置：Controller的方法注解，和对应对接方的回调URL参数设置 3. 回调响应的默认方法为 loginWithCallBack，之中先判断登录是否成功，如果成功，会根据是否是第一次登录进行后续的登录处理
 * 
 * @author jeally
 * @author Benjamin.Liu
 * @version 2016年3月24日
 */
public abstract class NebulaOAuthLoginController extends NebulaAbstractLoginController{

	private static final Logger	LOG							= LoggerFactory.getLogger(NebulaOAuthLoginController.class);

	/* 用户补全信息页面的默认定义 */
	public static final String	VIEW_MEMBER_COMPLETEINFO	= "member.completeInfo";

	/* 第三方帐号绑定页面的默认定义 */
	public static final String	VIEW_MEMBER_BINDING			= "member.binding";

	/* 第三方帐号登录成功跳转页面的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN_SUCC		= "member.succ";

	/* 第三方帐号登录失败跳转页面的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN_FAIL		= "member.fail";

	@Autowired
	private SdkMemberManager	skdMemeberManager;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager		memberManager;

	/**
	 * 构建去第三方登录url type 第三方登录名：微信 ，微博，QQ 支付宝等
	 * 
	 * @return
	 */
	protected String constructOAuthLoginURL(String type){
		// 获取第三方登录系统参数
		ThirdPartyMemberAdaptor adaptor = ThirdPartyMemberFactory.getInstance().getThirdPartyMemberAdaptor(type);

		// 第三方登录地址
		String loginUrl = adaptor.generateLoginUrl();
		LOG.info("WeChat generate login url {}", loginUrl);
		return "redirect:" + loginUrl;
	}

	/**
	 * 校验授权并组装信息。如果验证失败返回null，否则返回封装好的第三方用户信息
	 * 
	 * @return
	 */
	public abstract ThirdPartyMemberCommand checkOAuthLogin(HttpServletRequest request);

	/**
	 * 根据回调处理登录动作，需要在具体项目中指定回调地址，示例配置为
	 * 
	 * @RequestMapping(value = "/member/weiboLoginCallBack", method = RequestMethod.POST) 请注意配置的URL需和第三方登录配置文件中对应第三方回调地址一致
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String loginWithCallBack(HttpServletRequest request,HttpServletResponse response,Model model){
		// 校验授权
		ThirdPartyMemberCommand member = this.checkOAuthLogin(request);
		if (member == null) {
			return VIEW_MEMBER_LOGIN_FAIL;
		}

		// 第三方登录校验通过后的后续操作
		LOG.debug("[{} / {}] is validated. 2=QQ,3=SINA,4=ALIPAY,5=WECHAT", member.getOpenId(), member.getSource());
		return onOAuthSuccess(member, request, response, model);
	}

	/**
	 * 第三方登录校验通过后的后续操作
	 * 
	 * @param thirdPartyMember
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	protected String onOAuthSuccess(
			ThirdPartyMemberCommand thirdPartyMember,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){

		// 查询是否存在该用户登录信息，如果用户信息存在，则取出的是目前已有的完整信息
		Member member = skdMemeberManager.findThirdMemberByThirdIdAndSource(thirdPartyMember.getOpenId(), thirdPartyMember.getSource());

		// 首次登录
		if (member == null) {
			LOG.debug("[{} / {}] is the first time to the store", thirdPartyMember.getOpenId(), thirdPartyMember.getSource());
			// 创建用户
			MemberFrontendCommand frontendCommand = generateThirdPartyMember(thirdPartyMember, request);
			memberManager.rewriteRegister(frontendCommand);
		}

		// 第三方登录
		String url = doLogin(request, response, model, member);

		// 判断是否存在用户状态流转，空表示不存在，跳转到默认登录成功页
		if (url == null || url.trim().length() == 0) {
			return VIEW_MEMBER_LOGIN_SUCC;
		}else{
			//重定向到返回的URL 
			return "redirect:" + url;
		}
	}

	/**
	 * 保存第三方用户信息，这里在后续继承类中需要补充可以拿到的所有相关信息，默认只拿了OpenId,source,sex
	 * 
	 * @param thirdPartyMember
	 */
	protected MemberFrontendCommand generateThirdPartyMember(ThirdPartyMemberCommand thirdPartyMember,HttpServletRequest request){

		MemberFrontendCommand frontendCommand = new MemberFrontendCommand();

		// 第三方登录openId
		frontendCommand.setThirdPartyIdentify(thirdPartyMember.getOpenId());

		// 第三方登录来源
		frontendCommand.setSource(thirdPartyMember.getSource());

		// 生命周期：未激活状态
		frontendCommand.setLifecycle(Member.LIFECYCLE_UNACTIVE);

		// 类型：第三方会员
		frontendCommand.setType(Member.MEMBER_TYPE_THIRD_PARTY_MEMBER);

		// 性别
		if (thirdPartyMember.getSex() != null && thirdPartyMember.getSex().trim().length() != 0) {
			frontendCommand.setSex(Integer.parseInt(thirdPartyMember.getSex()));
		}

		int loginCount = 0;
		Date registerTime = new Date();
		String clientIp = RequestUtil.getClientIp(request);

		MemberConductCommand conductCommand = new MemberConductCommand(loginCount, registerTime, clientIp);

		frontendCommand.setMemberConductCommand(conductCommand);

		return frontendCommand;
	}

	/***
	 * 验证成功后登录动作
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param member
	 */
	protected String doLogin(HttpServletRequest request,HttpServletResponse response,Model model,Member member){

		// 同步购物车 暂时省略
		MemberDetails memberDetails = getMemberDetails(member);

		// 登录成功后处理
		DefaultReturnResult result = (DefaultReturnResult) onAuthenticationSuccess(memberDetails, request, response);

		return result.getReturnObject().toString();
	}

	/**
	 * 构建登录会员信息
	 * 
	 * @param member
	 * @return
	 */
	protected MemberDetails getMemberDetails(Member member){
		MemberCommand command = new MemberCommand();
		PropertyUtil.copyProperties(command, member);
		return constructMemberDetails(command);
	}

}
