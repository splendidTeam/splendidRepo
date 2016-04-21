/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;

/**
 * Pdp Controller Test
 * 
 * @author chengchao
 */
@SuppressWarnings("unused")
public class NebulaPdpControllerTest extends BaseControllerTest{


	private NebulaPdpController nebulaPdpController;
	
	@Before
	public void setUp(){

	}

	@Test
	public void testGetBuyLimit(){
		
	}
}
