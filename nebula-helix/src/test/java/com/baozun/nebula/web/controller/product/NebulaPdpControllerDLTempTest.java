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
import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolverImpl;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolverImpl;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
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
	
	private ItemPropertyViewCommandResolver	itemPropertyViewCommandResolver;
	
	private ItemColorSwatchViewCommandResolver	colorSwatchViewCommandResolver;
	
	protected ItemImageViewCommandConverter             itemImageViewCommandConverter;
	
	private SdkItemManager 												sdkItemManager;
	
	
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		itemDetailManager = control.createMock(ItemDetailManager.class);
		sdkItemManager =control.createMock(SdkItemManager.class);
		//DB
		itemPropertyViewCommandResolver = new ItemPropertyViewCommandResolverImpl();
		colorSwatchViewCommandResolver =new ItemColorSwatchViewCommandResolverImpl();
		
		itemImageViewCommandConverter =new ItemImageViewCommandConverter();
		
		ReflectionTestUtils.setField(colorSwatchViewCommandResolver, "sdkItemManager", sdkItemManager);
		ReflectionTestUtils.setField(colorSwatchViewCommandResolver, "itemDetailManager", itemDetailManager);
		ReflectionTestUtils.setField(itemPropertyViewCommandResolver, "itemDetailManager", itemDetailManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemPropertyViewCommandResolver", itemPropertyViewCommandResolver);
		ReflectionTestUtils.setField(nebulaPdpController, "colorSwatchViewCommandResolver", colorSwatchViewCommandResolver);
		
		
		ReflectionTestUtils.setField(nebulaPdpController, "itemImageViewCommandConverter", itemImageViewCommandConverter);
		
	}
	
	@Test
	public void testResolve(){
		Map<String, Object> returnMap =new HashMap<String, Object>();
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		List<ItemImageViewCommand> images =new ArrayList<ItemImageViewCommand>();
		//构造数据
		ItemPropertyViewCommand itemPropertyViewCommand = constrData(returnMap,
				baseInfoViewCommand, images);
		
		EasyMock.expect(itemPropertyViewCommandResolver.resolve(baseInfoViewCommand, images)).andReturn(itemPropertyViewCommand).times(1);
		
		control.replay();
		ItemPropertyViewCommand actualCommand = itemPropertyViewCommandResolver.resolve(baseInfoViewCommand, images);
		assertEquals(itemPropertyViewCommand, actualCommand);
		control.verify();
		
	}
	
	@Test
	public void testbuildItemColorSwatchViewCommands(){
		
		Map<String, Object> returnMap =new HashMap<String, Object>();
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		List<ItemImageViewCommand> images =new ArrayList<ItemImageViewCommand>();
		//构造数据
		ItemPropertyViewCommand itemPropertyViewCommand = constrData(returnMap,
				baseInfoViewCommand, images);
		List<Long> itemIds =new ArrayList<Long>();
		itemIds.add(14060L);
		itemIds.add(14061L);
		
		List<ItemImage> itemImageList =new ArrayList<ItemImage>();
		ItemImage img1=new ItemImage();
		img1.setItemId(14060l);
		img1.setId(3080l);
		img1.setItemProperties(57571l);
		img1.setPicUrl("2014/11/12/14157706660105740_800X800.JPG");
		img1.setType("IMG_TYPE_COLOR");
		ItemImage img2=new ItemImage();
		img2.setItemId(14061l);
		img2.setId(3081l);
		img2.setItemProperties(57573l);
		img2.setPicUrl("2014/11/12/14157706660105740_800X800.JPG");
		img2.setType("IMG_TYPE_COLOR");
		itemImageList.add(img1);
		itemImageList.add(img2);
		//
		
		
		
		List<ItemCommand> itemCommands =new ArrayList<ItemCommand>();
		ItemCommand itemCommand1 =new ItemCommand();
		itemCommand1.setId(14060l);
		itemCommand1.setCode("42611WP364B33");
		itemCommand1.setStyle("42611WP364");
		
		ItemCommand itemCommand2 =new ItemCommand();
		itemCommand2.setId(14061l);
		itemCommand2.setCode("42621WP364B33");
		itemCommand2.setStyle("42611WP364");
		itemCommands.add(itemCommand1);
		itemCommands.add(itemCommand2);
		
		EasyMock.expect(itemDetailManager.findItemListByItemId(EasyMock.eq(14060L),EasyMock.eq("42611WP364"))).andReturn(itemCommands).times(1);
		EasyMock.expect(sdkItemManager.findItemImageByItemIds(EasyMock.eq(itemIds),EasyMock.isNull(String.class))).andReturn(itemImageList).times(1);
		//EasyMock.anyObject(String.class)
		//EasyMock.isNull(String.class)
		//(String) EasyMock.or(EasyMock.isA(String.class), EasyMock.isNull()))
		EasyMock.expect(itemImageViewCommandConverter.convert(itemImageList)).andReturn(images);
		
		Map<String, Object> returnMap2 =new HashMap<String, Object>();
		constructRMap2(returnMap2);
		EasyMock.expect(itemDetailManager.findDynamicPropertyByItemIds(EasyMock.eq(itemIds))).andReturn(returnMap2).times(1);
		
		control.replay();
		
		List<ItemColorSwatchViewCommand> actualCommand = nebulaPdpController.buildItemColorSwatchViewCommands(baseInfoViewCommand);
		assertEquals(2, actualCommand.size());
		control.verify();
		
	}
	
	
	/**
	 * 
	 * @param returnMap2
	 */
	private void constructRMap2(Map<String, Object> map) {
		
		List<DynamicPropertyCommand> commands =new ArrayList<DynamicPropertyCommand>();
		DynamicPropertyCommand dynamicPropertyCommand =new DynamicPropertyCommand();
		
		Property property =new Property();
		property.setId(30l);
		property.setIsColorProp(true);
		property.setName("颜色");
		property.setIsSaleProp(true);
		dynamicPropertyCommand.setProperty(property );
		
		ItemPropertiesCommand itemProperties =new ItemPropertiesCommand();
		itemProperties.setItem_properties_id(57571l);
		itemProperties.setPropertyValue("经典黑");
		dynamicPropertyCommand.setItemProperties(itemProperties );
		commands.add(dynamicPropertyCommand);
		
		DynamicPropertyCommand dynamicPropertyCommand2 =new DynamicPropertyCommand();
		
		Property property2 =new Property();
		property2.setId(30l);
		property2.setIsColorProp(true);
		property2.setName("颜色");
		property.setIsSaleProp(true);
		dynamicPropertyCommand2.setProperty(property2 );
		
		ItemPropertiesCommand itemProperties2 =new ItemPropertiesCommand();
		itemProperties2.setItem_properties_id(57573l);
		itemProperties2.setPropertyValue("土豪金");
		dynamicPropertyCommand2.setItemProperties(itemProperties2 );
		commands.add(dynamicPropertyCommand2);
		
		
		Map<String, Object> gMap =new HashMap<String, Object>();
		gMap.put("ABC", commands);
		map.put("salePropCommandList", commands);
		map.put("generalGroupPropMap", gMap);
		
	}

	@Test
	public void testBuildItemPropertyViewCommand(){
		
		Map<String, Object> returnMap =new HashMap<String, Object>();
		ItemBaseInfoViewCommand baseInfoViewCommand =new ItemBaseInfoViewCommand();
		List<ItemImageViewCommand> images =new ArrayList<ItemImageViewCommand>();
		//构造数据
		ItemPropertyViewCommand itemPropertyViewCommand = constrData(returnMap,
				baseInfoViewCommand, images);
		
		
		//test...
		EasyMock.expect(itemDetailManager.gatherDynamicProperty(EasyMock.eq(14060L))).andReturn(returnMap).times(1);
		
		control.replay();
		ItemPropertyViewCommand actualCommand = nebulaPdpController.buildItemPropertyViewCommand(baseInfoViewCommand, images);
		assertEquals(itemPropertyViewCommand, actualCommand);
		control.verify();
		
	}

	/**
	 * 
	 * @param returnMap
	 * @param baseInfoViewCommand
	 * @param images
	 * @return
	 */
	private ItemPropertyViewCommand constrData(Map<String, Object> returnMap,
			ItemBaseInfoViewCommand baseInfoViewCommand,
			List<ItemImageViewCommand> images) {
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
		
		
		baseInfoViewCommand.setId(14060L);
		baseInfoViewCommand.setCode("42611WP364B33");
		baseInfoViewCommand.setStyle("42611WP364");
		
		
		ItemImageViewCommand itemImageViewCommand =new ItemImageViewCommand();
		itemImageViewCommand.setItemId(14060L);
		itemImageViewCommand.setColorItemPropertyId(57571l);
		Map<String, List<ImageViewCommand>> imagesMap =new HashMap<String, List<ImageViewCommand>>();
		List<ImageViewCommand> imageViewCommands =new ArrayList<ImageViewCommand>();
		ImageViewCommand imageViewCommand =new ImageViewCommand();
		imageViewCommand.setUrl("http://www.baidu.com");
		imageViewCommands.add(imageViewCommand);
		imagesMap.put("IMG_TYPE_COLOR", imageViewCommands);
		itemImageViewCommand.setImages(imagesMap);
		
		ItemImageViewCommand itemImageViewCommand2 =new ItemImageViewCommand();
		itemImageViewCommand2.setItemId(14061L);
		itemImageViewCommand2.setColorItemPropertyId(57573l);
		Map<String, List<ImageViewCommand>> imagesMap2 =new HashMap<String, List<ImageViewCommand>>();
		List<ImageViewCommand> imageViewCommands2 =new ArrayList<ImageViewCommand>();
		ImageViewCommand imageViewCommand2 =new ImageViewCommand();
		imageViewCommand2.setUrl("http://www.baidu.com");
		imageViewCommands2.add(imageViewCommand);
		imagesMap2.put("IMG_TYPE_COLOR", imageViewCommands);
		itemImageViewCommand2.setImages(imagesMap2);
		
		images.add(itemImageViewCommand);
		images.add(itemImageViewCommand2);
		
		
		List<DynamicPropertyCommand> commands =new ArrayList<DynamicPropertyCommand>();
		Map<String, Object> gMap =new HashMap<String, Object>();
		gMap.put("ABC", commands);
		returnMap.put("salePropCommandList", commands);
		returnMap.put("generalGroupPropMap", gMap);
		return itemPropertyViewCommand;
	}
}
