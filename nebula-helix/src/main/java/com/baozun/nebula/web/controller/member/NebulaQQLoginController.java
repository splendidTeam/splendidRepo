package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.baozun.nebula.command.member.ThirdPartyMemberCommand;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberFactory;

/**
 * QQ登录controller
 * 
 * @author 黄大辉
 */
public class NebulaQQLoginController extends NebulaOAuthLoginController{

	private static final Logger LOG = LoggerFactory.getLogger(NebulaQQLoginController.class);

	/**
	 * QQ联合登陆页，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/constructOAuthLoginURL", method = RequestMethod.POST)
	 */
	@Override
	public String constructOAuthLoginURL(){

		// 获取系统QQ参数
		ThirdPartyMemberAdaptor adaptor = ThirdPartyMemberFactory.getInstance().getThirdPartyMemberAdaptor(ThirdPartyMemberFactory.TYPE_QQ);

		// QQ登录地址
		String loginUrl = adaptor.generateLoginUrl();
		LOG.info("qq generate login url {}", loginUrl);
		return "redirect:"+loginUrl;
	}

	/**
	 * 联合登录回调地址，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/qQBack", method = RequestMethod.POST)
	 * @param request
	 * @param response
	 */
	@Override
	public String loginWithCallBack(HttpServletRequest request,HttpServletResponse response,Model model){
		return super.loginWithCallBack(request, response, model);
	}

	@Override
	public ThirdPartyMemberCommand checkOAuthLogin(HttpServletRequest request){

		// 获取系统QQ参数
		ThirdPartyMemberAdaptor adaptor = ThirdPartyMemberFactory.getInstance().getThirdPartyMemberAdaptor(ThirdPartyMemberFactory.TYPE_QQ);

		// 校验授权
		ThirdPartyMember number = adaptor.returnMember(request);

		// 判断QQ用户登录信息是否成功获取
		if (number.getErrorCode() == null || number.getErrorCode().trim().length() == 0) {
			LOG.error("thirdParty source {} login failure, errorCode is {}", ThirdPartyMemberFactory.TYPE_QQ, number.getErrorCode());
			return null;
		}

		// 组装QQ用户信息
		ThirdPartyMemberCommand numberCommand = new ThirdPartyMemberCommand();
		numberCommand.setOpenId(number.getUid());
		numberCommand.setNickName(number.getNickName());
		numberCommand.setSource(Member.MEMBER_SOURCE_QQ);
		return numberCommand;
	}

}
