package com.baozun.nebula.wormhole.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.wormhole.mq.MqMsgBodyV5;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MsgUtilsTest {

	@Test
	public void makeSign() {
		String sgin = MsgUtils.makeSign("1", "2", "3", "4", "5", "6");
		System.out.println(sgin);
		Assert.assertNotNull(sgin);
	}

	private List<MqMsgBodyV5> paraMqMsgBodyV5List() {
		MqMsgBodyV5 mqMsgBodyV5 = new MqMsgBodyV5();
		mqMsgBodyV5.setMsgContent("aaa");

		List<MqMsgBodyV5> mqMsgBodyV5List = new ArrayList<MqMsgBodyV5>();
		mqMsgBodyV5List.add(mqMsgBodyV5);

		return mqMsgBodyV5List;
	}

	@Test
	public void listToJson() {
		String ss = MsgUtils.listToJson(paraMqMsgBodyV5List());

		System.out.println(ss);
		Assert.assertNotNull(ss);
	}

	@Test
	public void jsonToList() {
		List<MqMsgBodyV5> listSs = MsgUtils.jsonToList(MsgUtils.listToJson(paraMqMsgBodyV5List()), MqMsgBodyV5.class);
		Assert.assertEquals(listSs.size(), paraMqMsgBodyV5List().size());
	}

	@Test
	public void msgDestination() {
		String a = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_ITEM_SYNC);
		Assert.assertEquals(a, "test.nebula.p1-1.queue");

		String b = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_ITEM_ONSALE_SYNC);
		Assert.assertEquals(b, "test.nebula.p2-1.queue");

		String c = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_ITEM_PRICE_SYNC);
		Assert.assertEquals(c, "test.nebula.p1-2.queue");

		String d = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_INVENTORY_ALL);
		Assert.assertEquals(d, "test.nebula.I1-1.queue");

		String e = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_INVENTORY_ADD);
		Assert.assertEquals(e, "test.nebula.I1-2.queue");

		String f = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_ORDER_SEND);
		Assert.assertEquals(f, "test.nebula.O2-1.queue");

		String g = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_PAY_SEND);
		Assert.assertEquals(g, "test.nebula.O2-2.queue");

		String h = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_STATUS_SCM2SHOP_SYNC);
		Assert.assertEquals(h, "test.nebula.O1-1.queue");

		String i = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_STATUS_SHOP2SCM_SYNC);
		Assert.assertEquals(i, "test.nebula.O2-3.queue");

		String j = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_LOGISTICS_TRACKING);
		Assert.assertEquals(j, "test.nebula.L1-1.queue");

		String k = MsgUtils.msgDestination(IfIdentifyConstants.IDENTIFY_SF_TAKE_DATA_ONSITE);
		Assert.assertEquals(k, "test.nebula.L2-1.queue");

		String n = MsgUtils.msgDestination("12");
		Assert.assertNull(n);
	}
}
