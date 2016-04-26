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
import com.baozun.nebula.model.product.ItemImage;
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
		
		ReflectionTestUtils.setField(itemPropertyViewCommandResolver, "itemDetailManager", itemDetailManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemPropertyViewCommandResolver", itemPropertyViewCommandResolver);
		ReflectionTestUtils.setField(nebulaPdpController, "colorSwatchViewCommandResolver", colorSwatchViewCommandResolver);
		
		ReflectionTestUtils.setField(colorSwatchViewCommandResolver, "sdkItemManager", sdkItemManager);
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
		itemIds.add(57502L);
		
		List<ItemImage> itemImageList =new ArrayList<ItemImage>();
		ItemImage img1=new ItemImage();
		img1.setItemId(57502l);
		img1.setId(3081l);
		img1.setPicUrl("2014/11/12/14157706660105740_800X800.JPG");
		img1.setType("IMG_TYPE_COLOR");
		itemImageList.add(img1);
		
		//
		
		List<ItemColorSwatchViewCommand> colorSwatchViewCommands =new ArrayList<ItemColorSwatchViewCommand>();
		
		
		
		EasyMock.expect(sdkItemManager.findItemImageByItemIds(itemIds, null)).andReturn(itemImageList).times(1);
		EasyMock.expect(itemImageViewCommandConverter.convert(itemImageList)).andReturn(images);
		
		control.replay();
		List<ItemColorSwatchViewCommand> actualCommand = nebulaPdpController.buildItemColorSwatchViewCommands(baseInfoViewCommand);
		assertEquals(colorSwatchViewCommands, actualCommand);
		control.verify();
		
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
		
		
		List<DynamicPropertyCommand> commands =new ArrayList<DynamicPropertyCommand>();
		Map<String, Object> gMap =new HashMap<String, Object>();
		gMap.put("ABC", commands);
		returnMap.put("salePropCommandList", commands);
		returnMap.put("generalGroupPropMap", gMap);
		return itemPropertyViewCommand;
	}
}
