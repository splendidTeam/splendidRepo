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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger	LOG									= LoggerFactory.getLogger(NebulaAbstractBundleController.class);

	/**
	 * 构造商品详情页面捆绑类商品视图层对象
	 * @param bundleCommand
	 * @return
	 */
	protected abstract BundleViewCommand buildBundleViewCommandForPDP(BundleCommand bundleCommand);
	
	/**
	 * 构造捆绑类商品页面捆绑类商品视图层对象
	 * @param bundleCommand
	 * @return
	 */
	protected abstract BundleViewCommand buildBundleViewCommandForBundlePage(BundleCommand bundleCommand);
	
	/**
	 * 构造捆绑类商品成员的视图层对象
	 * @param bundleElementCommand
	 * @return
	 */
	protected abstract BundleElementViewCommand buildBundleElementViewCommand(BundleElementCommand bundleElementCommand);
	
	/**
	 * 构造捆绑类商品中的商品的视图层对象
	 * @param bundleItemCommand
	 * @return 
	 */
	protected abstract BundleItemViewCommand buildBundleItemViewCommand(BundleItemCommand bundleItemCommand);
	
	/**
	 * 构造捆绑类商品SKU的视图层对象
	 * @param bundleSkuCommand
	 * @return 
	 */
	protected abstract BundleSkuViewCommand buildBundleSkuViewCommand(BundleSkuCommand bundleSkuCommand);
	 
	/**
	 * 获取捆绑类商品中某一个具体商品的展示图片
	 * @param itemId
	 * @return 
	 */
	protected abstract String getItemImage(Long itemId);
	
}
