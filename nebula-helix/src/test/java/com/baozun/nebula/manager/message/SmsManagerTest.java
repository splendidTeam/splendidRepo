package com.baozun.nebula.manager.message;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.constants.MessageConstants;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.baozun.nebula.sdk.manager.impl.SdkSMSManagerImpl;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.feilong.core.util.RandomUtil;

/**
 * 短信验证码manager的Test
 * @author shouqun.li
 * @version 2016年3月24日 下午3:09:58
 */
public class SmsManagerTest extends BaseControllerTest{
	
	private SdkSMSManager smsManager;
	@Before
	public void setUp(){
		smsManager = new SdkSMSManagerImpl();
	}
	
	@Test
	public void smsSend() throws Exception{
		SMSCommand messageCommand = new SMSCommand();
		messageCommand.setMobile("18271265526");
		/*生成验证码*/
		String code = RandomUtil.createRandomFromString(MessageConstants.SECURITY_CODE_ORIGINAL_STRING, MessageConstants.SECURITY_CODE_LENGTH);
		System.out.println(code);
		//ProfileConfigUtil.setMode("dev");
		//这个sign是必须的（可能是短信供应商那边有设置），每个商城的sign不一样。若没有sign，程序这边显示的短信发送成功，用户实际上是没有收到的。
		//String sign = ProfileConfigUtil.findPro("config/sms.properties").getProperty("sms.sign");
		//messageCommand.setContent(code + "测试使用");
		control.replay();
		//assertEquals(true, smsManager.sendMessage(messageCommand));
		control.verify();
	}
}
