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
	public String constructOAuthLoginURL(){
		return super.constructOAuthLoginURL(ThirdPartyMemberFactory.TYPE_QQ);
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
		ThirdPartyMember member = adaptor.returnMember(request);

		// 判断QQ用户登录信息是否成功获取
		if (member.getErrorCode() == null || member.getErrorCode().trim().length() == 0) {
			LOG.error("thirdParty source {} login failure, errorCode is {}", ThirdPartyMemberFactory.TYPE_QQ, member.getErrorCode());
			return null;
		}

		// 组装QQ用户信息
		ThirdPartyMemberCommand numberCommand = new ThirdPartyMemberCommand();
		numberCommand.setOpenId(member.getUid());
		numberCommand.setNickName(member.getNickName());
		numberCommand.setSource(Member.MEMBER_SOURCE_QQ);
		
		// 头像
		numberCommand.setAvatar(member.getAvatar());
		
		//性别  注意：获取不到时默认返回男
		if("男".equals(member.getSex())){
			numberCommand.setSex("1");
		}else if("女".equals(member.getSex())){
			numberCommand.setSex("2");
		}
		
		return numberCommand;
	}

}
