package com.baozun.nebula.manager.message;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.dao.system.SMSTemplateDao;
import com.baozun.nebula.manager.VelocityManager;
import com.baozun.nebula.model.system.SMSTemplate;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.baozun.nebula.sdk.manager.SdkSMSManager.SendResult;
import com.baozun.nebula.sdk.manager.impl.SdkSMSManagerImpl;
import com.baozun.nebula.web.controller.BaseControllerTest;

/**
 * 短信验证码manager的Test
 * @author shouqun.li
 * @version 2016年3月24日 下午3:09:58
 */
public class SmsManagerTest extends BaseControllerTest{
	
	private SdkSMSManager smsManager;
	private List<SMSTemplate> smsTemplate;
	private String content;
	private SMSTemplateDao smsTemplateDao;
	private VelocityManager velocityManager;
	private SMSCommand smsCommand;
	private SMSTemplate template;
	private Map<String, Object> paramMap;
	@Before
	public void setUp(){
		paramMap = new HashMap<String, Object>();
	    smsTemplate = new ArrayList<SMSTemplate>();
		template = new SMSTemplate();
		template.setBody("the content");
		template.setCode("12");
		smsTemplate.add(template);
		content = "the message content";
		smsCommand = new SMSCommand();
		smsCommand.setMobile("18271265526");
		smsCommand.setTemplateCode("12");
		paramMap.put("code", smsCommand.getTemplateCode());
		smsTemplateDao = control.createMock("smsTemplateDao", SMSTemplateDao.class);
		velocityManager = control.createMock("velocityManager", VelocityManager.class);
		smsManager = new SdkSMSManagerImpl();
		ReflectionTestUtils.setField(smsManager, "smsTemplateDao", smsTemplateDao);
		ReflectionTestUtils.setField(smsManager, "velocityManager", velocityManager);
		
	}
	
	@Test
	public void smsSend() throws Exception{
		EasyMock.expect(smsTemplateDao.findEffectSMSTemplateListByQueryMap(paramMap)).andReturn(smsTemplate);
		EasyMock.expect(velocityManager.parseVMContent(template.getBody(), smsCommand.getVars())).andReturn(content);
		EasyMock.expectLastCall();
		control.replay();
		assertEquals(SendResult.SUCESS, smsManager.send(smsCommand));
		control.verify();
	}
}
