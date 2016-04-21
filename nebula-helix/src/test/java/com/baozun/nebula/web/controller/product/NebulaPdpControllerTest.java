/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;

/**
 * Pdp Controller Test
 * 
 * @author chengchao
 */
@SuppressWarnings("unused")
public class NebulaPdpControllerTest extends BaseControllerTest{


	private NebulaPdpController nebulaPdpController;
	
	private ItemDetailManager itemDetailManager;
	
	private ItemRateManager itemRateManager;
	
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		itemDetailManager = control.createMock(ItemDetailManager.class);
		itemRateManager = control.createMock(ItemRateManager.class);
		
		ReflectionTestUtils.setField(nebulaPdpController, "itemRateManager", itemRateManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemDetailManager", itemDetailManager);
	}

	@Test
	public void testGetBuyLimit(){
		ItemBuyLimitedBaseCommand itemBuyLimitedCommand = new ItemBuyLimitedBaseCommand();
		itemBuyLimitedCommand.setItemId(2L);
		
		EasyMock.expect(itemDetailManager.getItemBuyLimited(EasyMock.isA(ItemBuyLimitedBaseCommand.class),EasyMock.eq(6))).andReturn(Integer.valueOf(3)).times(1);
		EasyMock.replay(itemDetailManager);
		
		assertEquals(Integer.valueOf(3), nebulaPdpController.getBuyLimit(itemBuyLimitedCommand));
		EasyMock.verify(itemDetailManager);
	}
	
	@Test
	public void testBuildItemExtraViewCommand(){
		
		ItemExtraViewCommand itemExtraViewCommand = new ItemExtraViewCommand();
		itemExtraViewCommand.setFavoriteCount(Long.valueOf(200));
		itemExtraViewCommand.setRate(3.6F);
		itemExtraViewCommand.setReviewCount(Long.valueOf(153));
		itemExtraViewCommand.setSales(Long.valueOf(60));
		
		String itemCode = "testItemCode";
		EasyMock.expect(itemDetailManager.findItemSalesCount(itemCode)).andReturn(Integer.valueOf(60)).times(1);
		EasyMock.expect(itemDetailManager.findItemFavCount(itemCode)).andReturn(Integer.valueOf(200)).times(1);
		EasyMock.expect(itemRateManager.findRateCountByItemCode(itemCode)).andReturn(Integer.valueOf(153)).times(1);
		EasyMock.expect(itemDetailManager.findItemAvgReview(itemCode)).andReturn(3.6F).times(1);
		
		control.replay();
		ItemExtraViewCommand actualCommand = nebulaPdpController.buildItemExtraViewCommand(itemCode);
		
		assertEquals(itemExtraViewCommand.getFavoriteCount(), actualCommand.getFavoriteCount());
		assertEquals(itemExtraViewCommand.getRate(), actualCommand.getRate());
		assertEquals(itemExtraViewCommand.getReviewCount(), actualCommand.getReviewCount());
		assertEquals(itemExtraViewCommand.getSales(), actualCommand.getSales());
		control.verify();
	}
}
