package com.baozun.nebula.wormhole.scm;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 推送公共相关的方法测试类
 * 
 * @author lihao_mamababa
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MessageSendTest {

	

	@Autowired
	private JmsTemplate template;

	@Test
	public void saveMsgBody() {
	
		String content="test---001";
		String destination="test.scm2nebula.resp";
	     
        template.convertAndSend(destination, content);

	}
}
