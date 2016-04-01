package com.baozun.nebula.web.controller.member;

import org.junit.Before;
import org.junit.Test;

import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.controller.BaseControllerTest;

import static org.junit.Assert.assertEquals;

public class NebulaWeiboLoginControllerTest extends BaseControllerTest{
	
	
	SdkMemberManager sdkMemberManager;
	NebulaWeiboLoginController controller;
	
	@Before
	public void init(){
		controller = new NebulaWeiboLoginController();
	}
	
	
	@Test
	public void testShowWeiboLogin(){
		control.replay();
		String url="redirect:https://api.weibo.com/oauth2/authorize?response_type=code&redirect_uri=http://xn6774445.imwork.net/member/weibo-login-callback&client_id=1030399418";
		//state随机生成，无法进行相等比较，临时比较长度
		assertEquals(url.length(),controller.constructOAuthLoginURL().length());
		control.verify();
	}
	
	@Test
	public void TestWeiboLoginCallBack(){
		control.replay();
		//需要访问外部服务器无法mock
//		controller.weiboLoginCallBack(request, response, model);
		control.verify();
	}
}
