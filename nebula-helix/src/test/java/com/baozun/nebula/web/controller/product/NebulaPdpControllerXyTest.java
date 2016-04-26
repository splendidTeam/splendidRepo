/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.product.converter.InventoryViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;

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
	
	private InventoryViewCommandConverter inventoryViewCommandConverter;
	
	@Before
	public void setUp(){
		nebulaPdpController = new NebulaPdpController();
		inventoryViewCommandConverter = new InventoryViewCommandConverter();
		sdkItemManager = control.createMock(SdkItemManager.class);
		ReflectionTestUtils.setField(nebulaPdpController, "sdkItemManager", sdkItemManager);
		ReflectionTestUtils.setField(nebulaPdpController, "inventoryViewCommandConverter", inventoryViewCommandConverter);
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

}
