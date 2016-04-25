/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.manager.product.NebulaBundleManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.product.converter.BundleElementViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleItemViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleSkuViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.BundleViewCommand;
import com.feilong.tools.jsonlib.JsonUtil;

public class NebulaBundleControllerTest extends BaseControllerTest{


	private NebulaBundleController bebulabundBundleController;
	
	private NebulaBundleManager nebulaBundleManager;
	
	private SdkItemManager sdkItemManager;
	
	private BundleViewCommandConverter bundleViewCommandConverter;
	
	private BundleElementViewCommandConverter bundleElementViewCommandConverter;
	
	private BundleItemViewCommandConverter bundleItemViewCommandConverter;
	
	private BundleSkuViewCommandConverter bundleSkuViewCommandConvert;
	
	private ItemImageViewCommandConverter itemImageViewCommandConverter;
	
	@Autowired
	private ItemPropertyViewCommandResolver itemPropertyViewCommandResolver;
	
	
	private BundleCommand bundleCommand;
	
	@Before
	public void setUp(){
		bebulabundBundleController=new NebulaBundleController();
		bundleCommand=new BundleCommand();
		bundleCommand.setId(1L);

		List<BundleElementCommand> bundleElementCommands=new ArrayList<BundleElementCommand>();
		BundleElementCommand bundleElementCommand=new BundleElementCommand();
		bundleElementCommand.setItemId(1L);
		
		List<BundleItemCommand> bundletBundleItemCommands=new ArrayList<BundleItemCommand>();
		BundleItemCommand bundleItemCommand=new BundleItemCommand();
		bundleItemCommand.setItemId(1L);
		bundleItemCommand.setLifecycle(1);
		
		List<BundleSkuCommand> bundletBundleSkuCommands=new ArrayList<BundleSkuCommand>();
		BundleSkuCommand bundleSkuCommand=new BundleSkuCommand();
		bundleSkuCommand.setId(1L);
		bundleSkuCommand.setLifeCycle(1);
		
		bundletBundleSkuCommands.add(bundleSkuCommand);
		bundleItemCommand.setBundleSkus(bundletBundleSkuCommands);
		bundletBundleItemCommands.add(bundleItemCommand);
		bundleElementCommand.setItems(bundletBundleItemCommands);
		bundleElementCommands.add(bundleElementCommand);
		bundleCommand.setBundleElementCommands(bundleElementCommands);
		
		sdkItemManager = control.createMock("sdkItemManager", SdkItemManager.class);
		nebulaBundleManager=control.createMock("nebulaBundleManager", NebulaBundleManager.class);
		itemPropertyViewCommandResolver=control.createMock("itemPropertyViewCommandResolver", ItemPropertyViewCommandResolver.class);
		bundleViewCommandConverter=new BundleViewCommandConverter();
		bundleElementViewCommandConverter=new BundleElementViewCommandConverter();
		bundleItemViewCommandConverter=new BundleItemViewCommandConverter();
		bundleSkuViewCommandConvert=new BundleSkuViewCommandConverter();
		itemImageViewCommandConverter = new ItemImageViewCommandConverter();
		
		ReflectionTestUtils.setField(bebulabundBundleController, "sdkItemManager", sdkItemManager);
		ReflectionTestUtils.setField(bebulabundBundleController, "nebulaBundleManager", nebulaBundleManager);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleViewCommandConverter", bundleViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleElementViewCommandConverter", bundleElementViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleItemViewCommandConverter", bundleItemViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleSkuViewCommandConvert", bundleSkuViewCommandConvert);
		ReflectionTestUtils.setField(bebulabundBundleController, "itemImageViewCommandConverter", itemImageViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "itemPropertyViewCommandResolver", itemPropertyViewCommandResolver);
	}

	@Test
	public void testShowBundleDetail(){
		
		EasyMock.expect(nebulaBundleManager.findBundleCommandByBundleId(4L)).andReturn(bundleCommand);
		EasyMock.replay(nebulaBundleManager);
		assertEquals(bebulabundBundleController.MODEL_KEY_BUNDLE,bebulabundBundleController.showBundleDetail(4L, request, response, model));
		EasyMock.verify(nebulaBundleManager);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadBundleInfo(){
		EasyMock.expect(nebulaBundleManager.findBundleCommandByItemId(4L)).andReturn(Arrays.asList(bundleCommand)).times(1);
		control.replay();
		DefaultReturnResult defaultReturnResult = (DefaultReturnResult)bebulabundBundleController.loadBundleInfo(4L, request, response, model);
		System.out.println(JsonUtil.format(defaultReturnResult));
		assertEquals(Long.valueOf(1), ((List<BundleViewCommand>)defaultReturnResult.getReturnObject()).get(0).getId());
		control.verify();
	}
}
