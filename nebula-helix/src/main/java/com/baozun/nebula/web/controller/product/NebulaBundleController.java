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
package com.baozun.nebula.web.controller.product;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.manager.product.NebulaBundleManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.converter.BundleElementViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleItemViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleSkuViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.BundleViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.BundleDetailViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.BundleElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.BundleItemViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.BundleSkuViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.BundleViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyViewCommand;
import com.feilong.core.Validator;

import loxia.dao.Pagination;

/**
 * 捆绑类商品（Bundle）控制器
 * 
 * <ol>
 * <li>{@link #showBundleDetail(Long, HttpServletRequest, HttpServletResponse, Model)} Bundle详情页</li>
 * <li>{@link #showBundleList(PageForm, HttpServletRequest, HttpServletResponse, Model)} Bundle列表页</li>
 * <li>{@link #loadBundleInfoByMainItemId(Long, HttpServletRequest, HttpServletResponse, Model)} 异步加载Bundle信息</li>
 * </ol>
 * 
 * @author yue.ch
 *
 */
public class NebulaBundleController extends NebulaPdpController {

	private static final Logger LOG = LoggerFactory.getLogger(NebulaBundleController.class);

	private static final String VIEW_BUNDLE_LIST = "bundle.list";

	private static final String VIEW_BUNDLE_DETAIL = "bundle.detail";
	
	public static final String MODEL_KEY_BUNDLE_LIST = "bundleList";
	
	public static final String MODEL_KEY_BUNDLE = "bundle";

	@Autowired
	private NebulaBundleManager nebulaBundleManager;

	@Autowired
	@Qualifier("bundleViewCommandConverter")
	private BundleViewCommandConverter bundleViewCommandConverter;
	
	@Autowired
	@Qualifier("bundleElementViewCommandConverter")
	private BundleElementViewCommandConverter bundleElementViewCommandConverter;
	
	@Autowired
	@Qualifier("bundleItemViewCommandConverter")
	private BundleItemViewCommandConverter bundleItemViewCommandConverter;
	
	@Autowired
	@Qualifier("bundleSkuViewCommandConvert")
	private BundleSkuViewCommandConverter bundleSkuViewCommandConvert;

	/**
	 * 查看捆绑类商品详细信息
	 * 
	 * <li>获取bundle商品评论直接调用父类方法showItemReview：{@link com.baozun.nebula.web.controller.product.NebulaPdpController#showItemReview}</li>
	 * 
	 * @RequestMapping(value = "/bundle/{itemCode}", method = RequestMethod.GET)
	 * 
	 * @param itemCode
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBundleDetail(@PathVariable("itemCode") String bundleItemCode, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		BundleCommand bundleCommand = nebulaBundleManager.findBundleCommandByBundleItemCode(bundleItemCode, isCheckBundleStatus());
		if(Validator.isNotNullOrEmpty(bundleCommand)){
			BundleDetailViewCommand bundleDetailViewCommand=buildBundleViewCommandForBundlePage(bundleCommand,bundleItemCode);
			model.addAttribute(MODEL_KEY_BUNDLE,bundleDetailViewCommand);
		}else{
			LOG.info("Bundle error...bundleCommand is null;bundleItemCode:{} [{}]",bundleItemCode,new Date());
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

		Pagination<BundleCommand> bundleCommandPage = nebulaBundleManager.findBundleCommandByPage(pageForm.getPage(), pageForm.getSorts(), isCheckBundleStatus());
		model.addAttribute(MODEL_KEY_BUNDLE_LIST, bundleViewCommandConverter.convert(bundleCommandPage));
		
		return VIEW_BUNDLE_LIST;
	}

	/**
	 * 异步加载bundle信息（根据主卖品商品ID）
	 * 
	 * @RequestMapping(value = "/bundle/loadBundlesByMainItemId.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @return 封装捆绑类商品视图模型集合， 参考{@link com.baozun.nebula.web.controller.product.viewcommand.BundleViewCommand}
	 */
	public NebulaReturnResult loadBundleInfoByMainItemId(@RequestParam("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		DefaultReturnResult result = new DefaultReturnResult();
		
		try {
			// 根据当前的商品id查询针对该商品为主卖品配置的bundle
			List<BundleCommand> bundleCommands = nebulaBundleManager.findBundleCommandByMainItemId(itemId, isCheckBundleStatus());
			if (Validator.isNotNullOrEmpty(bundleCommands)) {
				result.setReturnObject(buildBundleViewCommandForPDP(bundleCommands));
			}
			result.setResult(true);
			result.setStatusCode(String.valueOf(HttpStatus.OK));
		} catch (Exception e) {
			LOG.error("load bundle info error, itemId=" + itemId, e);
			result.setResult(false);
			result.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}
	
	/**
	 * 异步加载bundle信息（根据主卖品款号）
	 * 
	 * @RequestMapping(value = "/bundle/loadBundlesByMainStyle.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @return 封装捆绑类商品视图模型集合， 参考{@link com.baozun.nebula.web.controller.product.viewcommand.BundleViewCommand}
	 */
	public NebulaReturnResult loadBundleInfoByMainStyle(@RequestParam("style") String style, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		DefaultReturnResult result = new DefaultReturnResult();
		
		try {
			// 根据当前的主卖品款号查询针对该款商品为主卖品配置的bundle
			List<BundleCommand> bundleCommands = nebulaBundleManager.findBundleCommandByMainStyle(style, isCheckBundleStatus());
			if (Validator.isNotNullOrEmpty(bundleCommands)) {
				result.setReturnObject(buildBundleViewCommandForPDP(bundleCommands));
			}
			result.setResult(true);
			result.setStatusCode(String.valueOf(HttpStatus.OK));
		} catch (Exception e) {
			LOG.error("load bundle info error, style=" + style, e);
			result.setResult(false);
			result.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}
	
	/**
	 * bundle的异步校验
	 * 
	 * @RequestMapping(value = "/bundle/validateBundle.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId
	 * @param quantity
	 * @param skuIds
	 * @param request
	 * @param response
	 * @param model
	 * @return 封装捆绑类商品校验结果，参考{@link com.baozun.nebula.command.bundle.BundleCommand.BundleStatus}
	 * 
	 */
	public NebulaReturnResult validateBundle(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity,@ArrayCommand(dataBind = true) Long[] skuIds, HttpServletRequest request,
			HttpServletResponse response, Model model){
		DefaultReturnResult result = new DefaultReturnResult();
		
		try {
			List<Long> skuList = Arrays.asList(skuIds);
			result.setReturnObject(nebulaBundleManager.validateBundle(itemId, skuList, quantity));
			result.setResult(true);
			result.setStatusCode(String.valueOf(HttpStatus.OK));
		} catch (Exception e) {
			LOG.error("validate bundle error, itemId=" + itemId, e);
			result.setResult(false);
			result.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		}
		
		return result;
	}

	/**
	 * 构造商品详情页面捆绑类商品视图模型
	 * 
	 * <p>
	 * 一般情况下商品详情页面不需要加载捆绑类商品本身的商品描述、seo等扩展信息以及图片等， 
	 * 仅需要加载捆绑类商品中具体组成商品的相关信息。如果有特殊需求，需要重写该方法。
	 * </p>
	 * 
	 */
	protected List<BundleViewCommand> buildBundleViewCommandForPDP(List<BundleCommand> bundleCommands) {
		if(Validator.isNullOrEmpty(bundleCommands)) {
			return null;
		}
		
		List<BundleViewCommand> result = bundleViewCommandConverter.convert(bundleCommands);
		for(int i = 0; i < result.size(); i++) {
			BundleViewCommand command = result.get(i);
			command.setBundleElementViewCommands(buildBundleElementViewCommand(bundleCommands.get(i).getBundleElementCommands()));
		}
		
		return result;
	}

	/**
	 * 构造捆绑商品详情页视图模型
	 * <ul>
	 * <li>bundle本身作为无属性类商品。</li>
	 * <li>默认只显示正常上架的bundle，如果需要预览未上架、下架bundle需要重构此方法</li>
	 * <li>默认加载捆绑商品本身的商品描述、seo等扩展信息以及图片</li>
	 * </ul>
	 * 
	 */
	protected BundleDetailViewCommand buildBundleViewCommandForBundlePage(BundleCommand bundleCommand,String bundleItemCode) {
		//bundle 商品的lifecycle状态
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = buildItemBaseInfoViewCommand(bundleCommand.getItemId());
		if(!Item.LIFECYCLE_ENABLE.equals(itemBaseInfoViewCommand.getLifecycle())){
			LOG.info("Bundle error...bundleLifecycle is not active;Lifecycle:{} [{}]",itemBaseInfoViewCommand.getLifecycle(),new Date());
			return null;
		}
		BundleViewCommand bundleViewCommand=bundleViewCommandConverter.convert(bundleCommand);
		//buildBundleElements
		List<BundleElementCommand> bundleElementCommands = bundleCommand.getBundleElementCommands();
		List<BundleElementViewCommand> bundleElementViewCommands = buildBundleElementViewCommand(bundleElementCommands);
		if(Validator.isNullOrEmpty(bundleElementViewCommands)){
			LOG.info("Bundle error...bundleElementViewCommand is null;bundleId:{} [{}]",bundleCommand.getId(),new Date());
			return null;
		}
		bundleViewCommand.setBundleElementViewCommands(bundleElementViewCommands);
		BundleDetailViewCommand bundleDetailViewCommand=new BundleDetailViewCommand();
		BeanUtils.copyProperties(bundleViewCommand, bundleDetailViewCommand);
		//budleItem相关信息
		bundleDetailViewCommand.setBaseInfo(itemBaseInfoViewCommand);
		bundleDetailViewCommand.setExtra(buildItemExtraViewCommand(itemBaseInfoViewCommand));
		List<ItemImageViewCommand> itemImageViewCommands = buildItemImageViewCommand(bundleCommand.getItemId());
		bundleDetailViewCommand.setImages(itemImageViewCommands);
		bundleDetailViewCommand.setProperty(buildItemPropertyViewCommand(itemBaseInfoViewCommand, itemImageViewCommands));
		return bundleDetailViewCommand;
	}

	/**
	 * 构造捆绑类商品成员的视图模型
	 * 
	 * <p>
	 * 该方法的默认实现，包含如下信息，如果需要更多的数据，需要重写该方法。
	 * <ol>
	 * <li>listPrice、originalSalesPrice、salesPrice -- 价格相关</li>
	 * <li>properties -- 销售属性的id和名称（这个属性仅用于确认当前的捆绑类商品成员中的商品有多少个销售属性，具体的销售属性在每一个BundleElementItemViewCommand中定义）</li>
	 * <li>bundleItemViewCommands -- 成员中的商品</li>
	 * </ol>
	 * </p>
	 */
	protected List<BundleElementViewCommand> buildBundleElementViewCommand(List<BundleElementCommand> bundleElementCommands) {
		if(Validator.isNullOrEmpty(bundleElementCommands)) {
			return null;
		}
		
		List<BundleElementViewCommand> result = bundleElementViewCommandConverter.convert(bundleElementCommands);
		
		for(int i = 0; i < result.size(); i++) {
			BundleElementViewCommand command = result.get(i);
			List<BundleItemViewCommand> bundleItemViewCommands = buildBundleItemViewCommand(bundleElementCommands.get(i).getItems());
			command.setBundleItemViewCommands(bundleItemViewCommands);
			
			// 加载销售属性项
			if(Validator.isNotNullOrEmpty(bundleItemViewCommands)){
				LinkedHashMap<Long, Object> properties = new LinkedHashMap<Long, Object>();
				// 同一个捆绑类商品成员中的所有商品具有相同的销售属性，所以这里取第一个商品的销售属性即可。
				List<PropertyElementViewCommand> propertyElementViewCommands = bundleItemViewCommands.get(0).getSalesProperties();
				if(propertyElementViewCommands != null) {
					for(PropertyElementViewCommand p : propertyElementViewCommands) {
						PropertyViewCommand propertyViewCommand = p.getProperty();
						properties.put(propertyViewCommand.getId(), propertyViewCommand.getName());
					}
				}
				command.setProperties(properties);
			}
		}
		
		return result;
	}

	/**
	 * 构造捆绑类商品中的商品的视图模型
	 * 
	 * <p>
	 * 该方法的默认实现，包含如下信息，如果需要更多的数据，需要重写该方法。
	 * <ol>
	 * <li>itemId -- 商品ID</li>
	 * <li>title & subTitle -- 标题</li>
	 * <li>listPrice、originalSalesPrice、salesPrice -- 价格相关</li>
	 * <li>salesProperties -- 商品销售属性</li>
	 * <li>skuViewCommands -- 商品中包含的sku</li>
	 * <li>images -- 商品图片</li>
	 * </ol>
	 * </p>
	 */
	protected List<BundleItemViewCommand> buildBundleItemViewCommand(List<BundleItemCommand> bundleItemCommands) {
		if(Validator.isNullOrEmpty(bundleItemCommands)) {
			return null;
		}
		
		List<BundleItemViewCommand> result = bundleItemViewCommandConverter.convert(bundleItemCommands);
		
		for(int i = 0; i < result.size(); i++) {
			BundleItemViewCommand command = result.get(i);
			Long itemId = command.getItemId();
			
			// 设置商品图片
			command.setImages(buildItemImageViewCommand(itemId));
			
			// 设置商品标题
			ItemBaseInfoViewCommand itemViewCommand = buildItemBaseInfoViewCommand(itemId);
			command.setTitle(itemViewCommand.getTitle());
			command.setSubTitle(itemViewCommand.getSubTitle());
			
			// 加载商品销售属性
			ItemPropertyViewCommand itemPropertyViewCommand = buildItemPropertyViewCommand(buildItemBaseInfoViewCommand(itemId), buildItemImageViewCommand(itemId));
			command.setSalesProperties(itemPropertyViewCommand.getSalesProperties());
			
			// 加载sku
			command.setSkuViewCommands(buildBundleSkuViewCommand(bundleItemCommands.get(i).getBundleSkus()));
		}
		
		return result;
	}

	/**
	 * 构造捆绑类商品sku的视图模型
	 * 
	 * <p>
	 * 该方法的默认实现，包含如下的信息，如果需要更多的数据支持，需要重写该方法。
	 * <ul>
	 * <li>skuId -- sku id</li>
	 * <li>listPrice、originalSalesPrice、salesPrice -- 价格相关</li>
	 * <li>quantity -- 可用库存</li>
	 * <li>properties -- 销售属性</li>
	 * <li>extentionCode -- 外部编码</li>
	 * </ul>
	 * </p>
	 */
	protected List<BundleSkuViewCommand> buildBundleSkuViewCommand(List<BundleSkuCommand> bundleSkuCommands) {
		if(Validator.isNullOrEmpty(bundleSkuCommands)) {
			return null;
		}
		
		return bundleSkuViewCommandConvert.convert(bundleSkuCommands);
	}
	
	/**
	 * 是否校验bundle的状态
	 * 
	 * @return 	true: 不会加载未上架或者bundle中的任意商品未上架的bundle<br/>
	 * 			false: 加载所有的bundle，忽略上架状态
	 */
	protected boolean isCheckBundleStatus(){
		return Boolean.TRUE;
	}
}