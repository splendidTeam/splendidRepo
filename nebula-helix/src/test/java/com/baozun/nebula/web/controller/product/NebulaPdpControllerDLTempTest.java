/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolverImpl;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyValueViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyViewCommand;

/**   
* @Description 
* @author dongliang ma
* @date 2016年4月22日 上午10:40:16 
* @version   
*/
@SuppressWarnings("unused")
public class NebulaPdpControllerDLTempTest extends BaseControllerTest{


	private NebulaPdpController nebulaPdpController;
	
	private ItemDetailManager itemDetailManager;
	
	private ItemRateManager itemRateManager;
	
	@Autowired
	private ItemPropertyViewCommandResolver							itemPropertyViewCommandResolver;
	
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		itemDetailManager = control.createMock(ItemDetailManager.class);
		itemRateManager = control.createMock(ItemRateManager.class);
//		itemPropertyViewCommandResolver =new ItemPropertyViewCommandResolverImpl();
		
		ReflectionTestUtils.setField(nebulaPdpController, "itemRateManager", itemRateManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemDetailManager", itemDetailManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemPropertyViewCommandResolver", itemPropertyViewCommandResolver);
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
		assertEquals(itemExtraViewCommand, actualCommand);
		control.verify();
	}
	
	@Test
	public void testBuildItemPropertyViewCommand(){
		ItemPropertyViewCommand itemPropertyViewCommand =new ItemPropertyViewCommand();
		Map<String, List<PropertyElementViewCommand>> nonSalesProperties = new HashMap<String, List<PropertyElementViewCommand>>();
		List<PropertyElementViewCommand> elementViewCommands =new ArrayList<PropertyElementViewCommand>();
		PropertyElementViewCommand elementViewCommand =new PropertyElementViewCommand();
		PropertyViewCommand propertyViewCommand =new PropertyViewCommand();
		propertyViewCommand.setId(1l);
		propertyViewCommand.setGroupName("123");
		propertyViewCommand.setIsColorProp(false);
		propertyViewCommand.setIsSaleProp(false);
		propertyViewCommand.setName("上市年份");
		elementViewCommand.setProperty(propertyViewCommand);
		List<PropertyValueViewCommand> propertyValueViewCommands =new ArrayList<PropertyValueViewCommand>();
		PropertyValueViewCommand propertyValueViewCommand =new PropertyValueViewCommand();
		propertyValueViewCommand.setPropertyId(1l);
		propertyValueViewCommand.setPropertyName("上市年份");
		propertyValueViewCommand.setPropertyValue("1909-09-09");
		propertyValueViewCommand.setItemPropertiesId(101l);
		propertyValueViewCommands.add(propertyValueViewCommand);
		elementViewCommand.setPropertyValues(propertyValueViewCommands);
		elementViewCommands.add(elementViewCommand);
		nonSalesProperties.put("123", elementViewCommands);
		itemPropertyViewCommand.setNonSalesProperties(nonSalesProperties);
		
		List<PropertyElementViewCommand> salesProperties =new ArrayList<PropertyElementViewCommand>();
		PropertyElementViewCommand elementViewCommand1 =new PropertyElementViewCommand();
		PropertyViewCommand propertyViewCommand1 =new PropertyViewCommand();
		propertyViewCommand1.setId(2l);
		propertyViewCommand1.setGroupName("1233");
		propertyViewCommand1.setIsColorProp(false);
		propertyViewCommand1.setIsSaleProp(true);
		propertyViewCommand1.setName("尺码");
		elementViewCommand1.setProperty(propertyViewCommand1);
		List<PropertyValueViewCommand> propertyValueViewCommands1 =new ArrayList<PropertyValueViewCommand>();
		PropertyValueViewCommand propertyValueViewCommand1 =new PropertyValueViewCommand();
		propertyValueViewCommand1.setPropertyId(1l);
		propertyValueViewCommand1.setPropertyName("尺码");
		propertyValueViewCommand1.setPropertyValue("xxl");
		propertyValueViewCommand1.setItemPropertiesId(101l);
		propertyValueViewCommands1.add(propertyValueViewCommand1);
		elementViewCommand.setPropertyValues(propertyValueViewCommands1);
		salesProperties.add(elementViewCommand);
		
		itemPropertyViewCommand.setSalesProperties(salesProperties);
		
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		baseInfoViewCommand.setId(14060L);
		baseInfoViewCommand.setCode("42611WP364B33");
		
		List<ItemImageViewCommand> images =new ArrayList<ItemImageViewCommand>();
		ItemImageViewCommand itemImageViewCommand =new ItemImageViewCommand();
		itemImageViewCommand.setItemId(14060L);
		Map<String, List<ImageViewCommand>> imagesMap =new HashMap<String, List<ImageViewCommand>>();
		List<ImageViewCommand> imageViewCommands =new ArrayList<ImageViewCommand>();
		ImageViewCommand imageViewCommand =new ImageViewCommand();
		imageViewCommand.setUrl("http://www.baidu.com");
		imageViewCommands.add(imageViewCommand);
		imagesMap.put("sss", imageViewCommands);
		itemImageViewCommand.setImages(imagesMap);
		images.add(itemImageViewCommand);
		
		Map<String, Object> returnMap =new HashMap<String, Object>();
		List<DynamicPropertyCommand> commands =new ArrayList<DynamicPropertyCommand>();
		Map<String, Object> gMap =new HashMap<String, Object>();
		gMap.put("ABC", commands);
		returnMap.put("salePropCommandList", commands);
		returnMap.put("generalGroupPropMap", gMap);
		
		
		EasyMock.expect(itemDetailManager.gatherDynamicProperty(14060L)).andReturn(returnMap).times(1);
		
		
		control.replay();
		ItemPropertyViewCommand actualCommand = nebulaPdpController.buildItemPropertyViewCommand(baseInfoViewCommand, images);
		assertEquals(itemPropertyViewCommand, actualCommand);
		control.verify();
		
	}
}
