package com.baozun.nebula.dao.system;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.model.system.MsgReceiveContent;
/**
 * test
 * @author xingyu.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class MsgReceiveContentDaoTest {
	private  final Logger log = LoggerFactory
			.getLogger(this.getClass());
	@Autowired
	private MsgReceiveContentDao msgReceiveContentDao;
	@Test
	public final void testSave() {
		
		MsgReceiveContent mrc=new MsgReceiveContent();
		mrc.setIfIdentify("1-2");
		mrc.setIsProccessed(false);
		mrc.setMsgBody("dddd");
		mrc.setSendTime(new Date());
		msgReceiveContentDao.save(mrc);
	}

	@Test
	public final void findChooseOptionById() {

		MsgReceiveContent mrc=msgReceiveContentDao.getByPrimaryKey(1l);
				System.out.println(mrc.getMsgBody());
	}

}
