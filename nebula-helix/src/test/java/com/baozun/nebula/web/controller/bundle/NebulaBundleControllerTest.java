/**
 * 
 */
package com.baozun.nebula.web.controller.bundle;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.command.bundle.BundleElementCommand;
import com.baozun.nebula.command.bundle.BundleItemCommand;
import com.baozun.nebula.command.bundle.BundleSkuCommand;
import com.baozun.nebula.manager.bundle.NebulaBundleManager;
import com.baozun.nebula.web.controller.BaseControllerTest;
import com.baozun.nebula.web.controller.bundle.convert.BundleElementViewCommandConverter;
import com.baozun.nebula.web.controller.bundle.convert.BundleItemViewCommandConverter;
import com.baozun.nebula.web.controller.bundle.convert.BundleSkuViewCommandConverter;
import com.baozun.nebula.web.controller.bundle.convert.BundleViewCommandConverter;

public class NebulaBundleControllerTest extends BaseControllerTest{


	private NebulaBundleController bebulabundBundleController;
	
	private NebulaBundleManager nebulaBundleManager;
	
	private BundleViewCommandConverter bundleViewCommandConverter;
	
	private BundleElementViewCommandConverter bundleElementViewCommandConverter;
	
	private BundleItemViewCommandConverter bundleItemViewCommandConverter;
	
	private BundleSkuViewCommandConverter bundleSkuViewCommandConvert;
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
		bundleElementCommands.add(bundleElementCommand);
		bundleCommand.setBundleElementCommands(bundleElementCommands);
		
		nebulaBundleManager=control.createMock("NebulaBundleManager", NebulaBundleManager.class);
		bundleViewCommandConverter=new BundleViewCommandConverter();
		bundleElementViewCommandConverter=new BundleElementViewCommandConverter();
		bundleItemViewCommandConverter=new BundleItemViewCommandConverter();
		bundleSkuViewCommandConvert=new BundleSkuViewCommandConverter();
		
		ReflectionTestUtils.setField(bebulabundBundleController, "nebulaBundleManager", nebulaBundleManager);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleViewCommandConverter", bundleViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleElementViewCommandConverter", bundleElementViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleItemViewCommandConverter", bundleItemViewCommandConverter);
		ReflectionTestUtils.setField(bebulabundBundleController, "bundleSkuViewCommandConvert", bundleSkuViewCommandConvert);
	}

	@Test
	public void testShowBundleDetail(){
		
		EasyMock.expect(nebulaBundleManager.findBundleCommandByBundleId(4L)).andReturn(bundleCommand);
		EasyMock.replay(nebulaBundleManager);
		assertEquals(bebulabundBundleController.MODEL_KEY_BUNDLE,bebulabundBundleController.showBundleDetail(4L, request, response, model));
		EasyMock.verify(nebulaBundleManager);
	}
	
	
}
