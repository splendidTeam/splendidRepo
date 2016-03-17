package com.baozun.nebula.wormhole.scm.timing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * 推送公共相关的测试类
 * 
 * @author lihao_mamababa
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SyncCommonManagerTest {

	@Autowired
	private SyncCommonManager syncCommonManager;

	@Test
	public void queryMsgBody() throws EncryptionException {

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		String msgBody = EncryptUtil.getInstance().encrypt(MsgUtils.listToJson(list));

		List<Integer> newList = syncCommonManager.queryMsgBody(msgBody, Integer.class);

		Assert.assertEquals(list, newList);
	}
}
