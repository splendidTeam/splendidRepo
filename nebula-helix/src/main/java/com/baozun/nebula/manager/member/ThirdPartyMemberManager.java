package com.baozun.nebula.manager.member;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;

public interface ThirdPartyMemberManager extends BaseManager {

	/**
	 * 验证登录回调
	 * 
	 * 1.验证是否第一次登录，如果不是则直接返回用户信息(通过source,用户类型,第三方登录标识进行检索)
	 * 2.如果是第一次登录，需要添加用户信息到member表中(source,用户类型,第三方登录标识) 登录时通过 登录标识@source
	 * 做为loginName保存到cookie中;
	 * 
	 * @param request
	 * @param type
	 *            登录类型
	 * @param type
	 *            登录ip地址
	 * @return 如果为false则表示not first login，跳转到登录页,true is first login
	 */
	public Boolean validFirstLogin(ThirdPartyMember member, Integer source, String ip);
}
