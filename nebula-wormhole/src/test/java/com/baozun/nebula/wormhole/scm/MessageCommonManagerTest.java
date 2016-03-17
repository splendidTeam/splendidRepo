package com.baozun.nebula.wormhole.scm;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.wormhole.mq.MqMsgBodyV5;
import com.baozun.nebula.wormhole.mq.MqMsgHeadV5;
import com.baozun.nebula.wormhole.mq.MqMsgV5;
import com.baozun.nebula.wormhole.mq.MqResponseV5;
import com.baozun.nebula.wormhole.utils.JacksonUtils;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * 
 * 公共消息的功能 直接与MQ进行交互 形成消息头以及消息体测试类
 * 
 * @author lihao_mamababa
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MessageCommonManagerTest {

	@Autowired
	private MessageCommonManager messageCommonManager;

	@Autowired
	private SdkMsgManager sdkMsgManager;

	@Test
	public void sendToMq() {
		MsgSendRecord msgSendRecord = sdkMsgManager.findMsgSendRecordById(3L);
		boolean flag = messageCommonManager.sendToMq(msgSendRecord);
		Assert.assertTrue(flag);
	}

	@Test
	public void receiveFeedback() throws JsonGenerationException, JsonMappingException, IOException, EncryptionException {
		MqMsgV5 mqMsgV5 = new MqMsgV5();
		MqMsgHeadV5 mqMsgHeadV5 = new MqMsgHeadV5();
		MqMsgBodyV5 mqMsgBodyV5 = new MqMsgBodyV5();
		MqResponseV5 mqResponseV5 = new MqResponseV5();

		mqMsgHeadV5.setMsgId("3");
		mqMsgHeadV5.setShop("2");
		mqMsgHeadV5.setIfVersion("3");
		mqMsgHeadV5.setIfIdentify("4");
		mqMsgHeadV5.setMsgSendTime("5");

		mqResponseV5.setMsgId("3");
		mqResponseV5.setIfIdentify("3");

		mqMsgBodyV5.setMsgContent(EncryptUtil.getInstance().encrypt(JacksonUtils.getObjectMapper().writeValueAsString(mqResponseV5)));

		String sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(), mqMsgBodyV5.getMsgContent());
		mqMsgHeadV5.setSign(sign);

		mqMsgV5.setHead(mqMsgHeadV5);
		mqMsgV5.setBody(mqMsgBodyV5);

		String jsonStr = JacksonUtils.getObjectMapper().writeValueAsString(mqMsgV5);

		String content = EncryptUtil.getInstance().base64Encode(jsonStr);

		messageCommonManager.receiveFeedback(content);
		
	}

	@Test
	public void receiveFromScm() throws JsonGenerationException, JsonMappingException, EncryptionException, IOException {
		MqMsgV5 mqMsgV5 = new MqMsgV5();
		MqMsgHeadV5 mqMsgHeadV5 = new MqMsgHeadV5();
		MqMsgBodyV5 mqMsgBodyV5 = new MqMsgBodyV5();

		mqMsgHeadV5.setMsgId("3");
		mqMsgHeadV5.setShop("2");
		mqMsgHeadV5.setIfVersion("3");
		mqMsgHeadV5.setIfIdentify("4");
		mqMsgHeadV5.setMsgSendTime("2014-04-22 12:22:22");

		mqMsgBodyV5.setMsgContent(EncryptUtil.getInstance().encrypt(JacksonUtils.getObjectMapper().writeValueAsString("lalalala")));

		String sign = MsgUtils.makeSign(mqMsgHeadV5.getMsgId(), mqMsgHeadV5.getShop(), mqMsgHeadV5.getIfVersion(), mqMsgHeadV5.getIfIdentify(), mqMsgHeadV5.getMsgSendTime(), mqMsgBodyV5.getMsgContent());
		mqMsgHeadV5.setSign(sign);

		mqMsgV5.setHead(mqMsgHeadV5);
		mqMsgV5.setBody(mqMsgBodyV5);

		String jsonStr = JacksonUtils.getObjectMapper().writeValueAsString(mqMsgV5);

		String content = EncryptUtil.getInstance().base64Encode(jsonStr);

		String returnContent = messageCommonManager.receiveFromScm(content);

		MqMsgV5 mqMsgV5New = JacksonUtils.getObjectMapper().readValue(returnContent, new TypeReference<MqMsgV5>() {
		});

		MqMsgHeadV5 mqMsgHeadV5New = mqMsgV5New.getHead();
		String signNew = MsgUtils.makeSign(mqMsgHeadV5New.getMsgId(), mqMsgHeadV5New.getShop(), mqMsgHeadV5New.getIfVersion(), mqMsgHeadV5New.getIfIdentify(), mqMsgHeadV5New.getMsgSendTime(), mqMsgV5New.getBody().getMsgContent());

		Assert.assertEquals(signNew, mqMsgHeadV5New.getSign());
	}
}
