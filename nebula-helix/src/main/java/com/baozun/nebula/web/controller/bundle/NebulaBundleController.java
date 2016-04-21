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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.command.bundle.BundleElementCommand;
import com.baozun.nebula.command.bundle.BundleItemCommand;
import com.baozun.nebula.command.bundle.BundleSkuCommand;
import com.baozun.nebula.manager.bundle.NebulaBundleManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.bundle.convert.BundleViewCommandConvert;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleElementViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleItemViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleSkuViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.feilong.core.Validator;

/**
 * @author yue.ch
 *
 */
public class NebulaBundleController extends NebulaAbstractBundleController {

	private static final Logger LOG = LoggerFactory.getLogger(NebulaBundleController.class);

	private static final String VIEW_BUNDLE_LIST = "bundle.list";

	private static final String VIEW_BUNDLE_DETAIL = "bundle.detail";
	
	public static final String MODEL_KEY_BUNDLE_LIST = "bundleList";
	
	public static final String MODEL_KEY_BUNDLE_VIEW_COMMAND = "bundleCommand";

	@Autowired
	private NebulaBundleManager nebulaBundleManager;

	@Autowired
	private SdkItemManager sdkItemManager;

	@Autowired
	@Qualifier("bundleViewCommandConvert")
	private BundleViewCommandConvert bundleViewCommandConvert;
	

	/**
	 * 查看捆绑类商品详细信息
	 * 
	 * @RequestMapping(value = "/bundle/{bundleId}", method = RequestMethod.GET)
	 * 
	 * @param bundleId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBundleDetail(@PathVariable("bundleId") Long bundleId, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		BundleViewCommand bundleViewCommand=null;
		BundleCommand bundleCommand = nebulaBundleManager.findBundleCommandByBundleId(bundleId);
		if(bundleCommand!=null){
			bundleViewCommand=buildBundleViewCommandForBundlePage(bundleCommand);
		}
		return VIEW_BUNDLE_DETAIL;
	}

	/**
	 * 查看捆绑类商品列表
	 * 
	 * @RequestMapping(value = "/bundle/list", method = RequestMethod.GET)
	 * 
	 * @param pageForm
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBundleList(@ModelAttribute("page") PageForm pageForm, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		Pagination<BundleCommand> bundleCommandPage = nebulaBundleManager.findBundleCommandByPage(pageForm.getPage(), pageForm.getSorts());
		model.addAttribute(MODEL_KEY_BUNDLE_LIST, bundleViewCommandConvert.convert(bundleCommandPage));
		return VIEW_BUNDLE_LIST;
	}

	/**
	 * PDP页面异步加载bundle信息
	 * 
	 * @RequestMapping(value = "/bundle/loadBundles.json", method =
	 *                       RequestMethod.GET)
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult loadBundleInfo(@RequestParam("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		List<BundleViewCommand> viewCommands = null;

		// 根据当前pdp的商品id查询针对该商品为主卖品配置的bundle
		List<BundleCommand> bundleCommands = nebulaBundleManager.findBundleCommandByItemId(itemId);
		if (Validator.isNotNullOrEmpty(bundleCommands)) {
			for(BundleCommand command : bundleCommands) {
				BundleViewCommand bvc = buildBundleViewCommandForPDP(command);
				bvc.setBundleElementViewCommands(convert(command.getBundleElementCommands()));
			}
		}

		DefaultReturnResult result = new DefaultReturnResult();
		result.setResult(true);
		result.setStatusCode(String.valueOf(HttpStatus.OK));
		result.setReturnObject(viewCommands);

		return result;
	}

	/**
	 * 构造商品详情页面捆绑类商品视图层对象
	 * 
	 * <p>
	 * 一般情况下商品详情页面不需要加载捆绑类商品本身的商品描述、seo等扩展信息以及图片等， 
	 * 仅需要加载捆绑类商品中具体组成商品的相关信息。如果有特殊需求，需要重写该方法。
	 * </p>
	 * 
	 */
	@Override
	protected BundleViewCommand buildBundleViewCommandForPDP(BundleCommand bundleCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 构造捆绑商品详情页视图对象
	 * <p>
	 * 默认加载捆绑商品本身的商品描述、seo等扩展信息以及图片等
	 * </p>
	 * 
	 */
	@Override
	protected BundleViewCommand buildBundleViewCommandForBundlePage(BundleCommand bundleCommand) {
		BundleViewCommand bundleViewCommand=bundleViewCommandConvert.convert(bundleCommand);
		//setter itemBaseInfoViewCommand
		bundleViewCommand.setItemBaseInfoViewCommand(super.buildProductBaseInfoViewCommand(bundleViewCommand.getItemId()));
		 //convert bundleElementViewCommand
		 List<BundleElementCommand> bundleElementCommands = bundleCommand.getBundleElementCommands();
		 List<BundleElementViewCommand> bundleElementViewCommands=convert(bundleElementCommands);
		 bundleViewCommand.setBundleElementViewCommands(bundleElementViewCommands);
		return bundleViewCommand;
	}

	/**
	 * 默认实现
	 */
	@Override
	protected BundleElementViewCommand buildBundleElementViewCommand(BundleElementCommand bundleElementCommand) {
		//BundleElementViewCommand bundleElementViewCommand=bundleElementViewCommandConverter.convert(bundleElementCommand);
		//propertyElementViewCommands:TODO super
		BundleElementViewCommand bundleElementViewCommand=null;
		List<PropertyElementViewCommand> propertyElementViewCommands=new ArrayList<PropertyElementViewCommand>();
		 bundleElementViewCommand.setPropertyElementViewCommands(propertyElementViewCommands);
		return bundleElementViewCommand;
	}

	/**
	 * 默认实现
	 */
	@Override
	protected BundleItemViewCommand buildBundleItemViewCommand(BundleItemCommand bundleItemCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 默认实现<br/>
	 * </br/>
	 */
	@Override
	protected BundleSkuViewCommand buildBundleSkuViewCommand(BundleSkuCommand bundleSkuCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取捆绑类商品中某一个具体商品的展示图片
	 * 
	 * <p>
	 * 默认取商品列表页图片作为商品的展示图片,商城端可以重写该方法自定义商品图片的展示方式。
	 * </p>
	 */
	@Override
	protected String getItemImage(Long itemId) {
		if (itemId != null) {
			List<Long> itemIds = new ArrayList<Long>();
			itemIds.add(itemId);
			List<ItemImageCommand> itemImages = sdkItemManager.findItemImagesByItemIds(itemIds,
					ItemImage.IMG_TYPE_LIST);
			if (Validator.isNotNullOrEmpty(itemImages)) {
				return itemImages.get(0).getPicUrl();
			}
		}

		return null;
	}

	private List<BundleElementViewCommand> convert(List<BundleElementCommand> bundleElementCommands) {
		if (bundleElementCommands == null) {
			return null;
		}

		List<BundleElementViewCommand> result = new ArrayList<BundleElementViewCommand>();
		for (BundleElementCommand bec : bundleElementCommands) {
			List<BundleItemCommand> bundleItemCommands = bec.getItems();
			List<BundleItemViewCommand> bundleItemViewCommands = new ArrayList<BundleItemViewCommand>();
			for (BundleItemCommand bic : bundleItemCommands) {
				List<BundleSkuCommand> bundleSkuCommands = bic.getBundleSkus();
				List<BundleSkuViewCommand> bundleSkuViewCommands = new ArrayList<BundleSkuViewCommand>();
				for (BundleSkuCommand bsc : bundleSkuCommands) {
					bundleSkuViewCommands.add(buildBundleSkuViewCommand(bsc));
				}
				BundleItemViewCommand bivc = buildBundleItemViewCommand(bic);
				// 设置商品的展示图片
				bivc.setImageUrl(getItemImage(bivc.getItemId()));
				bivc.setSkuViewCommands(bundleSkuViewCommands);
				bundleItemViewCommands.add(bivc);
			}
			BundleElementViewCommand bevc = buildBundleElementViewCommand(bec);
			bevc.setBundleItemViewCommands(bundleItemViewCommands);
			result.add(bevc);
		}

		return result;
	}
}
