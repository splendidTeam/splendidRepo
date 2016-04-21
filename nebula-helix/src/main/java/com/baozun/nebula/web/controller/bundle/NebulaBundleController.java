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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.bundle.convert.BundleSkuViewCommandConverter;
import com.baozun.nebula.web.controller.bundle.convert.BundleViewCommandConverter;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleElementViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleItemViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleSkuViewCommand;
import com.baozun.nebula.web.controller.bundle.viewcommand.BundleViewCommand;
import com.feilong.core.Validator;

/**
 * @author yue.ch
 *
 */
public class NebulaBundleController extends NebulaAbstractBundleController {

	private static final Logger LOG = LoggerFactory.getLogger(NebulaBundleController.class);

	private static final String VIEW_BUNDLE_LIST = "bundle.list";

	private static final String VIEW_BUNDLE_DETAIL = "bundle.detail";

	@Autowired
	private NebulaBundleManager nebulaBundleManager;

	@Autowired
	private SdkItemManager sdkItemManager;
	
	@Autowired
	private SdkSkuManager sdkSkuManager;

	@Autowired
	@Qualifier("bundleViewCommandConvert")
	private BundleViewCommandConverter bundleViewCommandConvert;
	
	@Autowired
	@Qualifier("bundleSkuViewCommandConvert")
	private BundleSkuViewCommandConverter bundleSkuViewCommandConvert;

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

	@Override
	protected BundleViewCommand buildBundleViewCommandForBundlePage(BundleCommand bundleCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 默认实现
	 */
	@Override
	protected BundleElementViewCommand buildBundleElementViewCommand(BundleElementCommand bundleElementCommand) {
		// TODO Auto-generated method stub
		return null;
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
	 * 构造捆绑类商品SKU的视图层对象
	 * 
	 * <p>
	 * 该方法的默认实现，包含如下的信息，如果需要更多的数据支持，需要重写该方法。
	 * <ul>
	 * <li>skuId</li>
	 * <li>价格，包括listPrice、originalSalesPrice、salesPrice</li>
	 * <li>库存，quantity</li>
	 * <li>sku销售属性的属性id:属性值的key:value，properties</li>
	 * </ul>
	 * </p>
	 */
	@Override
	protected BundleSkuViewCommand buildBundleSkuViewCommand(BundleSkuCommand bundleSkuCommand) {
		BundleSkuViewCommand result = bundleSkuViewCommandConvert.convert(bundleSkuCommand);
		// 查询sku的销售属性
		Sku sku = sdkSkuManager.findSkuById(bundleSkuCommand.getSkuId());
		List<SkuProperty> skuProperty = sdkSkuManager.getSkuPros(sku.getProperties());
		if(Validator.isNotNullOrEmpty(skuProperty)) {
			Map<Long, Object> properties = new HashMap<Long, Object>();
			for(SkuProperty sp : skuProperty) {
				ItemProperties ip = sp.getItemProperties();
				properties.put(ip.getPropertyId(), ip.getPropertyValue());
			}
			
			result.setProperties(properties);
		}
		
		return result;
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
