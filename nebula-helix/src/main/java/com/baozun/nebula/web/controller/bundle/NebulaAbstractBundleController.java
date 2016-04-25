/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
*
 
* This software is the confidential and proprietary information of Baozun.
 
* You shall not disclose such Confidential Information and shall use it only in
 
* accordance with the terms of the license agreement you entered into
 
* with Baozun.
 
*
 
* BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 
* SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 
* IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 
* PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 
* SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 
* THIS SOFTWARE OR ITS DERIVATIVES.
 
*
 
*/
package com.baozun.nebula.web.controller.bundle;

import java.util.List;

import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.command.bundle.BundleElementCommand;
import com.baozun.nebula.command.bundle.BundleItemCommand;
import com.baozun.nebula.command.bundle.BundleSkuCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleElementViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleItemViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleSkuViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleViewCommand;
import com.baozun.nebula.web.controller.product.NebulaBasePdpController;

/**
 * 捆绑类商品 controller
 * 
 * @author yue.ch
 *
 */
public abstract class NebulaAbstractBundleController extends NebulaBasePdpController {
	
	/**
	 * 构造商品详情页面捆绑类商品视图模型
	 * @param bundleCommands
	 * @return
	 */
	protected abstract List<BundleViewCommand> buildBundleViewCommandForPDP(List<BundleCommand> bundleCommands);
	
	/**
	 * 构造捆绑类商品页面捆绑类商品视图模型
	 * @param bundleCommand
	 * @return
	 */
	protected abstract BundleViewCommand buildBundleViewCommandForBundlePage(BundleCommand bundleCommand);
	
	/**
	 * 构造捆绑类商品成员的视图模型
	 * @param bundleElementCommands
	 * @return
	 */
	protected abstract List<BundleElementViewCommand> buildBundleElementViewCommand(List<BundleElementCommand> bundleElementCommands);
	
	/**
	 * 构造捆绑类商品中的商品的视图模型
	 * @param bundleItemCommands
	 * @return 
	 */
	protected abstract List<BundleItemViewCommand> buildBundleItemViewCommand(List<BundleItemCommand> bundleItemCommands);
	
	/**
	 * 构造捆绑类商品SKU的视图模型
	 * @param bundleSkuCommand
	 * @return 
	 */
	protected abstract List<BundleSkuViewCommand> buildBundleSkuViewCommand(List<BundleSkuCommand> bundleSkuCommands);
	
}
