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
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.product.converter.ImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.InventoryViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;

/**
 * 
 * 1:基本信息
 * 2:库存
 * 3:商品图片
 * 4:推荐商品，搭配商品
 * 5:浏览记录
 * @author xy
 * 
 *
 */
public class NebulaPdpControllerXyTest extends BaseControllerTest{


	private NebulaPdpController nebulaPdpController;
	
	private SdkItemManager sdkItemManager;
	
	private ItemDetailManager itemDetailManager;
	
	private InventoryViewCommandConverter inventoryViewCommandConverter;
	
	private ItemImageViewCommandConverter itemImageViewCommandConverter;
	
	private ImageViewCommandConverter imageViewCommandConverter;
	
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		
		inventoryViewCommandConverter = new InventoryViewCommandConverter();
		itemImageViewCommandConverter = new ItemImageViewCommandConverter();
		imageViewCommandConverter = new ImageViewCommandConverter();
		
		sdkItemManager = control.createMock(SdkItemManager.class);
		itemDetailManager = control.createMock(ItemDetailManager.class);
		
		ReflectionTestUtils.setField(nebulaPdpController, "sdkItemManager", sdkItemManager);
		ReflectionTestUtils.setField(nebulaPdpController, "itemDetailManager", itemDetailManager);
		
		ReflectionTestUtils.setField(nebulaPdpController, "inventoryViewCommandConverter", inventoryViewCommandConverter);
		ReflectionTestUtils.setField(nebulaPdpController, "itemImageViewCommandConverter", itemImageViewCommandConverter);
		ReflectionTestUtils.setField(itemImageViewCommandConverter, "imageViewCommandConverter", imageViewCommandConverter);
		
	}
	
	@Test
	public void testBuildInventoryViewCommand(){
		List<InventoryViewCommand> expected = new ArrayList<InventoryViewCommand>();
		InventoryViewCommand inventoryViewCommand = new InventoryViewCommand();
		inventoryViewCommand.setExtentionCode("code1");
		inventoryViewCommand.setAvailableQty(5);
		inventoryViewCommand.setSkuId(1L);
		expected.add(inventoryViewCommand);
		
		List<SkuCommand> skuCommands = new ArrayList<SkuCommand>();
		SkuCommand skuCommand = new SkuCommand();
		skuCommand.setId(1L);
		skuCommand.setExtentionCode("code1");
		skuCommand.setAvailableQty(5);
		skuCommands.add(skuCommand);
		
		Long itemId = 1L;
		
		EasyMock.expect(sdkItemManager.findEffectiveSkuInvByItemId(itemId)).andReturn(skuCommands).times(1);
        
		control.replay();
		
		List<InventoryViewCommand> actual =  nebulaPdpController.buildInventoryViewCommand(itemId);
		
		assertEquals(expected, actual);
		EasyMock.verify();
		
	}
	
	@Test
	public void testBuildItemBaseInfoViewCommand(){
		ItemBaseInfoViewCommand expected = new ItemBaseInfoViewCommand();
		expected.setCode("code1");
		expected.setId(1L);
		expected.setTitle("衣服");
		
		String code ="code";
		
		ItemBaseCommand itemBaseCommand = new ItemBaseCommand();
		itemBaseCommand.setCode("code1");
		itemBaseCommand.setId(1L);
		itemBaseCommand.setTitle("衣服");
		
		EasyMock.expect(itemDetailManager.findItemBaseInfoByCode(code)).andReturn(itemBaseCommand).times(1);
		
		control.replay();
		
		ItemBaseInfoViewCommand actual = nebulaPdpController.buildItemBaseInfoViewCommand(code);
		
		assertEquals(expected, actual);
		EasyMock.verify();
		
	}
	
	@Test
	public void testBuildItemImageViewCommand(){
		Map<String, List<ImageViewCommand>> images = new HashMap<String, List<ImageViewCommand>>();
		
		List<ImageViewCommand> imageViewCommands = new ArrayList<ImageViewCommand>();
		for(int i =1;i<6;i++){
			ImageViewCommand imageViewCommand = new ImageViewCommand();
			imageViewCommand.setDescription(null);
			imageViewCommand.setUrl("/pic/"+i+".jpg");
			imageViewCommands.add(imageViewCommand);
		}
		images.put(ItemImage.IMG_TYPE_LIST, imageViewCommands);
		
		List<ItemImageViewCommand>  expected = new ArrayList<ItemImageViewCommand>();
		
		ItemImageViewCommand itemImageViewCommand = new ItemImageViewCommand();
		itemImageViewCommand.setColorItemPropertyId(22L);
		itemImageViewCommand.setImages(images);
		itemImageViewCommand.setItemId(1L);
		
		Map<String, List<ImageViewCommand>> images2 = new HashMap<String, List<ImageViewCommand>>();
		imageViewCommands = new ArrayList<ImageViewCommand>();
		for(int i =1;i<4;i++){
			ImageViewCommand imageViewCommand = new ImageViewCommand();
			imageViewCommand.setDescription(null);
			imageViewCommand.setUrl("/pic/"+i+".jpg");
			imageViewCommands.add(imageViewCommand);
		}
		images2.put(ItemImage.IMG_TYPE_LIST, imageViewCommands);
		
		
		ItemImageViewCommand itemImageViewCommand2 = new ItemImageViewCommand();
		itemImageViewCommand2.setColorItemPropertyId(33L);
		itemImageViewCommand2.setImages(images2);
		itemImageViewCommand2.setItemId(1L);
		
		expected.add(itemImageViewCommand2);
		expected.add(itemImageViewCommand);
		
		List<ItemImage> itemImageList = new ArrayList<ItemImage>();
		for(int i =1;i<4;i++){
			ItemImage itemImage = new ItemImage();
			itemImage.setId(Long.valueOf(i));
			itemImage.setItemId(1L);
			itemImage.setItemProperties(22L);
			itemImage.setPicUrl("/pic/"+i+".jpg");
			itemImage.setPosition(Integer.valueOf(i));
			itemImage.setType(ItemImage.IMG_TYPE_LIST);
			itemImageList.add(itemImage);
		}
		
		for(int i =1;i<4;i++){
			ItemImage itemImage = new ItemImage();
			itemImage.setId(Long.valueOf(i));
			itemImage.setItemId(1L);
			itemImage.setItemProperties(33L);
			itemImage.setPicUrl("/pic/"+i+".jpg");
			itemImage.setPosition(Integer.valueOf(i));
			itemImage.setType(ItemImage.IMG_TYPE_LIST);
			itemImageList.add(itemImage);
		}
		
		for(int i =4;i<6;i++){
			ItemImage itemImage = new ItemImage();
			itemImage.setId(Long.valueOf(i));
			itemImage.setItemId(1L);
			itemImage.setItemProperties(22L);
			itemImage.setPicUrl("/pic/"+i+".jpg");
			itemImage.setPosition(Integer.valueOf(i));
			itemImage.setType(ItemImage.IMG_TYPE_LIST);
			itemImageList.add(itemImage);
		}
		
		
		Long itemId = 1L;
		
		List<Long> itemIds = new ArrayList<Long>();
		itemIds.add(itemId);
		
		EasyMock.expect(sdkItemManager.findItemImageByItemIds(itemIds, null)).andReturn(itemImageList).times(1);
		
		control.replay();
		
		List<ItemImageViewCommand> actual = nebulaPdpController.buildItemImageViewCommand(itemId);
		
		assertEquals(expected, actual);
		
		EasyMock.verify();
		
	}
	

}
