package com.baozun.nebula.manager.message;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.constants.MessageConstants;
import com.baozun.nebula.manager.sms.SmsManager;
import com.baozun.nebula.manager.sms.SmsManagerImpl;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utils.SecurityCodeUtil;
import com.baozun.nebula.web.controller.BaseControllerTest;

/**
 * @author shouqun.li
 * 2016年3月24日 下午3:09:58
 */
public class SmsManagerTest extends BaseControllerTest{
	
	private SmsManager smsManager;
	@Before
	public void setUp(){
		smsManager = new SmsManagerImpl();
	}
	
	@Test
	public void smsSend() throws Exception{
		control.replay();
		MessageCommand messageCommand = new MessageCommand();
		messageCommand.setMobile("18221537147");
		String code = SecurityCodeUtil.createSecurityCode(MessageConstants.SECURITY_CODE_ORIGINAL_STRING, MessageConstants.SECURITY_CODE_LENGTH);
		System.out.println(code);
		//ProfileConfigUtil.setMode("dev");
		//这个sign是必须的（可能是短信供应商那边有设置），每个商城的sign不一样。若没有sign，程序这边显示的短信发送成功，用户实际上是没有收到的。
		//String sign = ProfileConfigUtil.findPro("config/sms.properties").getProperty("sms.sign");
		messageCommand.setContent(code + "测试使用");
		assertEquals(true, smsManager.sendMessage(messageCommand));
		control.verify();
	}
}
