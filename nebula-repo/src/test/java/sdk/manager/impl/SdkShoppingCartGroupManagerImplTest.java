package sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionConditionSKU;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartGroupManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartGroupManagerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath:spring.xml" })
@ActiveProfiles("dev")
public class SdkShoppingCartGroupManagerImplTest {

	@Resource(name = "sdkShoppingCartGroupManager")
	SdkShoppingCartGroupManager sdkShoppingCartGroupManager;
	@Resource
	SdkPromotionManager sdkPromotionManager;

	@Before
	public void before() {
		// 初始化引擎数据
		Date date = new Date(1406699679024L);
		sdkPromotionManager.publishPromotion(date);
	}

	@Test
	public void getPromotionListBySKUTest() {

		Long skuId = new Long(1213);
		List<PromotionSKUDiscAMTBySetting> skuListSetting = new ArrayList<PromotionSKUDiscAMTBySetting>();

		for (int i = 0; i < 10; i++) {
			PromotionSKUDiscAMTBySetting pss = new PromotionSKUDiscAMTBySetting();
			if (i % 2 == 0) {
				pss.setSkuId(skuId);
				pss.setPromotionId(new Long(351));
			} else {
				pss.setSkuId(new Long(25));
				pss.setPromotionId(new Long(63111));
			}
			skuListSetting.add(pss);
		}
		List<PromotionCommand> promotionCommands = sdkShoppingCartGroupManager
				.getPromotionListBySKU(skuListSetting, skuId);
		Assert.assertSame(5, promotionCommands.size());

	}

	@Test
	public void getConditionSKUListByPromotionId() {
		Long skuId = 1213l;
		List<PromotionConditionSKU> skuListSetting = new ArrayList<PromotionConditionSKU>();

		for (int i = 0; i < 10; i++) {
			PromotionConditionSKU pss = new PromotionConditionSKU();
			if (i % 2 == 0) {
				pss.setPromotionId(skuId);
			}
			skuListSetting.add(pss);
		}
		List<PromotionConditionSKU> promotionCommands = sdkShoppingCartGroupManager
				.getConditionSKUListByPromotionId(skuListSetting, skuId);
		Assert.assertSame(5, promotionCommands.size());

	}

	@Test
	public void getSettingSKUListByPromotionId() {

		Long skuId = 1213l;
		List<PromotionSKUDiscAMTBySetting> skuListSetting = new ArrayList<PromotionSKUDiscAMTBySetting>();

		for (int i = 0; i < 10; i++) {
			PromotionSKUDiscAMTBySetting pss = new PromotionSKUDiscAMTBySetting();
			if (i % 2 == 0) {
				pss.setSkuId(skuId);
			}
			skuListSetting.add(pss);
		}
		List<PromotionSKUDiscAMTBySetting> promotionCommands = sdkShoppingCartGroupManager
				.getSettingSKUListByPromotionId(skuListSetting, skuId);
		Assert.assertSame(5, promotionCommands.size());

	}

	@Test
	public void getLinesOfNormalPromotion() {

		List<ShoppingCartLineCommand> oneShopCartLineList = new ArrayList<ShoppingCartLineCommand>();

		ShoppingCartLineCommand s1 = new ShoppingCartLineCommand();
		s1.setSkuId(3492l);
		s1.setExtentionCode("code");
		s1.setShopId(256l);

		ShoppingCartLineCommand s2 = new ShoppingCartLineCommand();
		s2.setSkuId(3486l);
		s2.setExtentionCode("code");
		s2.setShopId(256l);

		oneShopCartLineList.add(s1);
		oneShopCartLineList.add(s2);

		List<PromotionSKUDiscAMTBySetting> onePromotionSettingSKUList = new ArrayList<PromotionSKUDiscAMTBySetting>();
		PromotionSKUDiscAMTBySetting ps1 = new PromotionSKUDiscAMTBySetting();
		ps1.setSkuId(3486l);
		ps1.setPromotionId(437l);
		ps1.setDiscountAmount(new BigDecimal(3));
		ps1.setShopId(256l);

		PromotionSKUDiscAMTBySetting ps2 = new PromotionSKUDiscAMTBySetting();
		ps2.setPromotionId(438l);
		ps2.setDiscountAmount(new BigDecimal(2));
		ps2.setSkuId(3486l);
		ps2.setShopId(256l);

		onePromotionSettingSKUList.add(ps1);
		onePromotionSettingSKUList.add(ps2);

		List<PromotionConditionSKU> onePromotionConditionSKUList = new ArrayList<PromotionConditionSKU>();

		PromotionConditionSKU pcs0 = new PromotionConditionSKU();
		pcs0.setPromotionId(437l);
		pcs0.setSkuId(3492l);
		pcs0.setShopId(256l);

		PromotionConditionSKU pcs = new PromotionConditionSKU();
		pcs.setPromotionId(437l);
		pcs.setSkuId(3486l);
		pcs.setShopId(256l);

		PromotionConditionSKU pcs1 = new PromotionConditionSKU();
		pcs1.setPromotionId(438l);
		pcs1.setSkuId(3486l);
		pcs1.setShopId(256l);

		onePromotionConditionSKUList.add(pcs0);
		onePromotionConditionSKUList.add(pcs);
		onePromotionConditionSKUList.add(pcs1);

		List<ShoppingCartLineCommand> promotionCommands = sdkShoppingCartGroupManager
				.getLinesOfNormalPromotion(oneShopCartLineList,
						onePromotionSettingSKUList,
						onePromotionConditionSKUList);
		for (ShoppingCartLineCommand shoppingCartLineCommand : promotionCommands) {
			System.out.println();
			System.out.println(shoppingCartLineCommand.getLineGroup() + ":"
					+ shoppingCartLineCommand.getSkuId() + ":"
					+ shoppingCartLineCommand.getDiscount());
		}
		Assert.assertSame(2, promotionCommands.size());

	}

	@Test
	public void appendShoppingCartLinesOfNoPromotion() {

		SdkShoppingCartGroupManagerImpl groupManagerImpl = new SdkShoppingCartGroupManagerImpl();

		List<ShoppingCartLineCommand> oneShopCartLineList = new ArrayList<ShoppingCartLineCommand>();
		ShoppingCartLineCommand s1 = new ShoppingCartLineCommand();
		s1.setSkuId(3492l);
		s1.setExtentionCode("code");
		s1.setShopId(256l);

		ShoppingCartLineCommand s2 = new ShoppingCartLineCommand();
		s2.setSkuId(3486l);
		s2.setExtentionCode("code");
		s2.setShopId(256l);

		ShoppingCartLineCommand s3 = new ShoppingCartLineCommand();
		s3.setSkuId(34l);
		s3.setExtentionCode("code");
		s3.setShopId(256l);
		oneShopCartLineList.add(s1);
		oneShopCartLineList.add(s2);
		oneShopCartLineList.add(s3);
		List<ShoppingCartLineCommand> promotionCommands = groupManagerImpl
				.appendShoppingCartLinesOfNoPromotion(oneShopCartLineList,
						oneShopCartLineList);
		Assert.assertSame(3, promotionCommands.size());

		List<ShoppingCartLineCommand> goneShopCartLineList = new ArrayList<ShoppingCartLineCommand>();
		ShoppingCartLineCommand gs1 = new ShoppingCartLineCommand();
		gs1.setSkuId(3492l);
		gs1.setExtentionCode("code");
		gs1.setShopId(256l);

		ShoppingCartLineCommand gs2 = new ShoppingCartLineCommand();
		gs2.setSkuId(3486l);
		gs2.setExtentionCode("code");
		gs2.setShopId(256l);
		goneShopCartLineList.add(gs1);
		goneShopCartLineList.add(gs2);

		promotionCommands = groupManagerImpl
				.appendShoppingCartLinesOfNoPromotion(oneShopCartLineList,
						goneShopCartLineList);
		Assert.assertSame(3, promotionCommands.size());

	}

	@Test
	public void appendShoppingCartLines() throws Exception {
		List<PromotionCommand> pcs = new ArrayList<PromotionCommand>();
		pcs.add(new PromotionCommand());

		List<ShoppingCartLineCommand> shopCartLineList = new ArrayList<ShoppingCartLineCommand>();

		ShoppingCartLineCommand gs1 = new ShoppingCartLineCommand();
		gs1.setSkuId(3492l);
		gs1.setExtentionCode("code");
		gs1.setShopId(256l);
		gs1.setPromotionList(pcs);

		ShoppingCartLineCommand gs2 = new ShoppingCartLineCommand();
		gs2.setSkuId(3486l);
		gs2.setExtentionCode("code");
		gs2.setShopId(256l);
		gs2.setPromotionList(pcs);

		ShoppingCartLineCommand gs3 = new ShoppingCartLineCommand();
		gs3.setSkuId(34l);
		gs3.setExtentionCode("code");
		gs3.setShopId(256l);
		gs3.setPromotionList(pcs);

		shopCartLineList.add(gs1);
		shopCartLineList.add(gs2);
		shopCartLineList.add(gs3);

		List<ShoppingCartLineCommand> oneShopCartLineList = new ArrayList<ShoppingCartLineCommand>();
		ShoppingCartLineCommand s1 = new ShoppingCartLineCommand();
		s1.setSkuId(3492l);
		s1.setExtentionCode("code");
		s1.setShopId(256l);
		s1.setDiscount(new BigDecimal(10));
		s1.setPromotionList(pcs);

		ShoppingCartLineCommand s2 = new ShoppingCartLineCommand();
		s2.setSkuId(348l);
		s2.setExtentionCode("code");
		s2.setShopId(256l);
		s2.setPromotionList(pcs);

		oneShopCartLineList.add(s1);
		oneShopCartLineList.add(s2);

		SdkShoppingCartGroupManagerImpl groupManagerImpl = new SdkShoppingCartGroupManagerImpl();

		List<ShoppingCartLineCommand> promotionCommands = groupManagerImpl
				.appendShoppingCartLines(shopCartLineList, oneShopCartLineList);
		for (ShoppingCartLineCommand shoppingCartLineCommand : promotionCommands) {
			System.out.println("skuid:" + shoppingCartLineCommand.getSkuId()
					+ "prolist:"
					+ shoppingCartLineCommand.getPromotionList().size());
		}
		for (ShoppingCartLineCommand shoppingCartLineCommand : promotionCommands) {
			System.out.println("skuid:" + shoppingCartLineCommand.getSkuId()
					+ "dicount:" + shoppingCartLineCommand.getDiscount());
		}
		Assert.assertSame(4, promotionCommands.size());

	}

	@Test
	public void appendCaptionLinesToGroupedLines() {

		List<ShoppingCartLineCommand> oneShopCartLineList = new ArrayList<ShoppingCartLineCommand>();
		

		ShoppingCartLineCommand s1 = new ShoppingCartLineCommand();
		List<PromotionCommand> commands = new ArrayList<PromotionCommand>();
		PromotionCommand pc = new PromotionCommand();
		pc.setCreateTime(new Date());
		pc.setPromotionName("满减1");
		commands.add(pc);
		s1.setSkuId(3492l);
		s1.setExtentionCode("code");
		s1.setLineGroup(1l);
		s1.setShopId(256l);
		s1.setPromotionList(commands);

		
		
		ShoppingCartLineCommand s2 = new ShoppingCartLineCommand();
		List<PromotionCommand> commands1 = new ArrayList<PromotionCommand>();
		PromotionCommand pc1 = new PromotionCommand();
		pc1.setPromotionName("满减2");
		pc1.setCreateTime(new Date());
		commands1.add(pc);
		s2.setSkuId(3486l);
		s2.setExtentionCode("code");
		s2.setLineGroup(1l);
		s2.setShopId(256l);
		s2.setPromotionList(commands1);
		
		ShoppingCartLineCommand s3 = new ShoppingCartLineCommand();
		s3.setSkuId(3486l);
		s3.setExtentionCode("code");
		s3.setLineGroup(2l);
		s3.setShopId(256l);
		List<PromotionCommand> commands2 = new ArrayList<PromotionCommand>();
		PromotionCommand pc2 = new PromotionCommand();
		pc2.setPromotionName("满减3");
		pc2.setCreateTime(new Date());
		commands2.add(pc2);
		s3.setPromotionList(commands2);
		
		ShoppingCartLineCommand s4 = new ShoppingCartLineCommand();
		s4.setSkuId(3486l);
		s4.setExtentionCode("code");
		s4.setLineGroup(3l);
		s4.setShopId(256l);
		List<PromotionCommand> commands3 = new ArrayList<PromotionCommand>();
		PromotionCommand pc3 = new PromotionCommand();
		pc3.setPromotionName("满减4");
		pc3.setCreateTime(new Date());
		commands3.add(pc3);
		s4.setPromotionList(commands3);
		
		
		// caption
		oneShopCartLineList.add(s3);
		oneShopCartLineList.add(s1);
		// caption
		oneShopCartLineList.add(s4);
		// caption
		oneShopCartLineList.add(s2);
		try {
			SdkShoppingCartGroupManagerImpl groupManagerImpl = new SdkShoppingCartGroupManagerImpl();
			List<ShoppingCartLineCommand> promotionCommands = groupManagerImpl
					.appendCaptionLinesToGroupedLines(oneShopCartLineList);
			for (int i = 0; i < promotionCommands.size(); i++) {
				ShoppingCartLineCommand l = promotionCommands.get(i);
				if (l.isCaptionLine()) {
					System.out.println("====="
							+ (promotionCommands.get(0) == promotionCommands
									.get(2)));
					System.out.println("lineGroup :" + l.getLineGroup()
							+ "skuid:" + l.getSkuId() + "caption:"
							+ l.getPromotionList().get(0).getPromotionName());
				} else {
					System.out.println("lineGroup :" + l.getLineGroup()
							+ "skuid:" + l.getSkuId());
				}

			}
			// Assert.assertSame(10, promotionCommands.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
