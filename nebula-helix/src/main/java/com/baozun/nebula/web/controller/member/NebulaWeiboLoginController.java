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
 * 微博登录controller
 * 
 * @author 黄大辉
 */
public class NebulaWeiboLoginController extends NebulaOAuthLoginController{

	private static final Logger LOG = LoggerFactory.getLogger(NebulaWeiboLoginController.class);

	/**
	 * 微博联合登陆页，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/constructOAuthLoginURL", method = RequestMethod.POST)
	 */
	public String constructOAuthLoginURL(){
		return super.constructOAuthLoginURL(ThirdPartyMemberFactory.TYPE_WEIBO);
	}

	/**
	 * 联合登录回调地址，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/weiboLoginCallBack", method = RequestMethod.POST)
	 * @param request
	 * @param response
	 */
	@Override
	public String loginWithCallBack(HttpServletRequest request,HttpServletResponse response,Model model){
		return super.loginWithCallBack(request, response, model);
	}

	@Override
	public ThirdPartyMemberCommand checkOAuthLogin(HttpServletRequest request){

		// 获取系统微博参数
		ThirdPartyMemberAdaptor adaptor = ThirdPartyMemberFactory.getInstance()
				.getThirdPartyMemberAdaptor(ThirdPartyMemberFactory.TYPE_WEIBO);

		// 校验授权
		ThirdPartyMember member = adaptor.returnMember(request);

		// 判断微博用户登录信息是否成功获取
		if (member.getErrorCode() != null && member.getErrorCode().trim().length() >0) {
			LOG.error("thirdParty source {} login failure, errorCode is {}", ThirdPartyMemberFactory.TYPE_WEIBO, member.getErrorCode());
			return null;
		}

		// 组装微博用户信息
		ThirdPartyMemberCommand numberCommand = new ThirdPartyMemberCommand();
		numberCommand.setOpenId(member.getUid());
		numberCommand.setNickName(member.getNickName());
		numberCommand.setSource(Member.MEMBER_SOURCE_SINA);
		
		// 头像  50×50
		numberCommand.setAvatar(member.getAvatar());
		numberCommand.setSex(member.getSex());

		return numberCommand;
	}

}
