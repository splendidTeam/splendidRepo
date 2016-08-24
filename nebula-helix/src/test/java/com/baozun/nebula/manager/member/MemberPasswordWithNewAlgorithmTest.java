package com.baozun.nebula.manager.member;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml","classpath*:spring-store-helper.xml" })
@ActiveProfiles("dev")
public class MemberPasswordWithNewAlgorithmTest {
	private static final Logger log = LoggerFactory.getLogger(MemberPasswordWithNewAlgorithmTest.class);
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private MemberPasswordManager memPasswordManager;
	
	@Autowired
	private MemberContactManager contactManager;
	//测试登录
	@Test
	public void testLogin() {
		MemberFrontendCommand m = new MemberFrontendCommand();
		m.setLoginName("ruichao.gao@baozun.cn");
		m.setPassword("speedo11111");
		try {
			this.memberManager.login(m);
		} catch (UserNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserExpiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordNotMatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	//测试修改密码
	@Test
	public void testModifyPwd() {
		memPasswordManager.modifyPassword("speedo11111", "speedo111111", "speedo111111", new Long(1166));
	}
	
	//测试重置密码
	@Test
	public void testResetPwd() {
		
		ForgetPasswordForm f = new ForgetPasswordForm();
		f.setType(2);
		f.setEmail("ruichao.gao@baozun.cn");
		memPasswordManager.resetPassword(f, "speedo11111");
	}
	//测试注册
	@Test
	public void testRegister() {
		MemberFrontendCommand m = new MemberFrontendCommand();
		MemberConductCommand mc = new MemberConductCommand();
		mc.setLoginCount(0);
		mc.setLoginIp("127.0.0.1");
		mc.setRegisterTime(new Date());
		m.setMemberConductCommand(mc);
		m.setLoginEmail("ruichao.gao@baozun.cn");
		m.setPassword("speedo111111");
		m.setType(2);
		this.memberManager.rewriteRegister(m);
		
	}
	
}