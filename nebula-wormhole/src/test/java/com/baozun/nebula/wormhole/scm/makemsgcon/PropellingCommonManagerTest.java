package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PropellingCommonManagerTest {

	@Autowired
	private PropellingCommonManager propellingCommonManager;

	@Test
	public void saveMsgBody() {
		List<Integer> object = new ArrayList<Integer>();
		object.add(11);
		object.add(22);
		object.add(33);

		propellingCommonManager.saveMsgBody(object, 3L);
		System.out.println("ok");

	}
}
