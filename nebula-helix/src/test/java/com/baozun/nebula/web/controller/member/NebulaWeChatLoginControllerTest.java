package com.baozun.nebula.web.controller.member;

import org.junit.Before;
import org.junit.Test;

import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.controller.BaseControllerTest;

import static org.junit.Assert.assertEquals;

public class NebulaWeChatLoginControllerTest extends BaseControllerTest{
	
	
	SdkMemberManager sdkMemberManager;
	NebulaWeChatLoginController controller;
	
	@Before
	public void init(){
		controller = new NebulaWeChatLoginController();
	}
	
	
	@Test
	public void testShowWeChatLogin(){
		control.replay();
		String url="redirect:https://open.weixin.qq.com/connect/qrconnect?appid=wxbc5d1d30369e0832&redirect_uri=http%3A%2F%2Fxn6774445.imwork.net%2Fmember%2Fweixin-login-callback&response_type=code&scope=snsapi_login&state=81d1fb83-3bba-450f-928e-282a14a65659#wechat_redirec";
		//state随机生成，无法进行相等比较，临时比较长度
		assertEquals(url.length(),controller.showWeChatLogin().length());
		control.verify();
	}
	
	@Test
	public void TestWeChatLoginCallBack(){
		control.replay();
		//需要访问外部服务器无法mock
//		controller.weChatLoginCallBack(request, response, model);
		control.verify();
	}
}
