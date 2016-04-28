/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.CacheManagerImpl;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.converter.ItemReviewViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ReviewMemberViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;

/**
 * Pdp Controller Test
 * 
 * @author chengchao
 */
public class NebulaPdpControllerTest extends BaseControllerTest{


	private NebulaPdpController nebulaPdpController;
	
	private ItemDetailManager itemDetailManager;
	
	private ItemRateManager itemRateManager;
	
	private MemberManager memberManager;
	
	private ReviewMemberViewCommandConverter reviewMemberViewCommandConverter;
	
	private ItemReviewViewCommandConverter itemReviewViewCommandConverter;
	
	private CacheManager cacheManager;
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		reviewMemberViewCommandConverter = new ReviewMemberViewCommandConverter();
		itemReviewViewCommandConverter = new ItemReviewViewCommandConverter();
		cacheManager = control.createMock(CacheManager.class);
		itemDetailManager = control.createMock(ItemDetailManager.class);
		itemRateManager = control.createMock(ItemRateManager.class);
		memberManager = control.createMock(MemberManager.class);
		
		ReflectionTestUtils.setField(nebulaPdpController, "itemReviewViewCommandConverter", itemReviewViewCommandConverter);
		ReflectionTestUtils.setField(nebulaPdpController, "reviewMemberViewCommandConverter", reviewMemberViewCommandConverter);
		ReflectionTestUtils.setField(nebulaPdpController, "memberManager", memberManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemRateManager", itemRateManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemDetailManager", itemDetailManager);
		ReflectionTestUtils.setField(nebulaPdpController, "cacheManager", cacheManager);
	}

	@Test
	public void testGetBuyLimit(){
		ItemBuyLimitedBaseCommand itemBuyLimitedCommand = new ItemBuyLimitedBaseCommand();
		itemBuyLimitedCommand.setItemId(2L);
		
		EasyMock.expect(itemDetailManager.getItemBuyLimited(EasyMock.isA(ItemBuyLimitedBaseCommand.class),EasyMock.eq(6))).andReturn(Integer.valueOf(3)).times(1);
		EasyMock.replay(itemDetailManager);
		
		assertEquals(Integer.valueOf(3), nebulaPdpController.getBuyLimit(itemBuyLimitedCommand.getItemId()));
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
		ItemBaseInfoViewCommand itemBaseInfo = new ItemBaseInfoViewCommand();
		itemBaseInfo.setCode(itemCode);
		ItemExtraViewCommand actualCommand = nebulaPdpController.buildItemExtraViewCommand(itemBaseInfo);
		assertEquals(itemExtraViewCommand, actualCommand);
		control.verify();
	}
	
	@Test
	public void testBuildItemExtraViewCommandCacheException(){
		
		ItemExtraViewCommand itemExtraViewCommand = new ItemExtraViewCommand();
		itemExtraViewCommand.setFavoriteCount(Long.valueOf(200));
		itemExtraViewCommand.setRate(3.6F);
		itemExtraViewCommand.setReviewCount(Long.valueOf(153));
		itemExtraViewCommand.setSales(Long.valueOf(60));
		
		String itemCode = "testItemCode";
		EasyMock.expect(cacheManager.getObject("item_extra_cache_key-testItemCode")).andThrow(new RuntimeException("Timeout")).times(1);
		EasyMock.expect(itemDetailManager.findItemSalesCount(itemCode)).andReturn(Integer.valueOf(60)).times(1);
		EasyMock.expect(itemDetailManager.findItemFavCount(itemCode)).andReturn(Integer.valueOf(200)).times(1);
		EasyMock.expect(itemRateManager.findRateCountByItemCode(itemCode)).andReturn(Integer.valueOf(153)).times(1);
		EasyMock.expect(itemDetailManager.findItemAvgReview(itemCode)).andReturn(3.6F).times(1);
		
		control.replay();
		ItemBaseInfoViewCommand itemBaseInfo = new ItemBaseInfoViewCommand();
		itemBaseInfo.setCode(itemCode);
		ItemExtraViewCommand actualCommand = nebulaPdpController.buildItemExtraViewCommand(itemBaseInfo);
		assertEquals(itemExtraViewCommand, actualCommand);
		control.verify();
	}
	
	@Test
	public void testShowItemReview(){
		PageForm pageForm = new PageForm();
		pageForm.setSize(5);
		pageForm.setCurrentPage(1);
		pageForm.setSort("order_by:price");
		
		Pagination<RateCommand> rates = new Pagination<RateCommand>();
		rates.setCurrentPage(1);
		rates.setCount(25);
		rates.setSize(5);
		rates.setStart(0);
		rates.setTotalPages(5);
		
		RateCommand rate = new RateCommand();
		rate.setItemId(12345L);
		rate.setMemberId(1L);
		rate.setContent("good");
		rate.setScore(5);
		
		RateCommand rate2 = new RateCommand();
		rate2.setItemId(12345L);
		rate2.setMemberId(2L);
		rate2.setContent("good2");
		rate2.setScore(4);
		
		List<RateCommand> rateList = new ArrayList<RateCommand>();
		rateList.add(rate);
		rateList.add(rate2);
		
		rates.setItems(rateList);
		
		List<Long> memberIds = new ArrayList<Long>();
		memberIds.add(1L);
		memberIds.add(2L);
		
		List<MemberCommand> members = new ArrayList<MemberCommand>();
		MemberCommand member1 = new MemberCommand();
		member1.setLoginName("a");
		member1.setId(1L);
		member1.setSex(0);
		
		MemberCommand member2 = new MemberCommand();
		member2.setLoginName("b");
		member2.setId(2L);
		member2.setSex(1);
		
		members.add(member1);
		members.add(member2);
		
		EasyMock.expect(itemRateManager.findItemRateListByItemId(EasyMock.isA(Page.class), EasyMock.eq(12345L), EasyMock.aryEq(pageForm.getSorts()))).andReturn(rates).times(1);
		EasyMock.expect(memberManager.findMembersByIds(memberIds)).andReturn(members).times(1);
		control.replay();
		
		assertEquals(DefaultReturnResult.SUCCESS, nebulaPdpController.showItemReview(12345L, pageForm, request, response, model));
		control.verify();
	}
}
