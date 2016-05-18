
/**
 * Copyright (c) 2016 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PriceViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;
import com.feilong.core.TimeInterval;


/**
 * 商品详情页controller
 * 
 * <p>在初始构建一个官方商城的时候，一个极其重要的事项就是定义商品的结构。良好的商品结构定义会大大减少运营的工作量，也会简化商品相关功能的开发，比如列表、搜索、促销等。
 * Nebula中比较常见的商品结构定义有两种：一种是定义到款，另一种是定义到色（这里的色是个笼统的概念）。后一种是服装类官方商城比较推荐的模式。</p>
 * 
 * <ol>针对上述两种商品结构，在pdp展示时会有三种不同的模式：
 * <li>商品定义到款，PDP到款显示. {@link NebulaAbstractPdpController#PDP_MODE_STYLE}</li>
 * <li>商品定义到色，PDP根据款号聚合（到款显示）. {@link NebulaAbstractPdpController#PDP_MODE_COLOR_COMBINE}</li>
 * <li>商品定义到色，PDP不需要聚合（到色显示）. {@link NebulaAbstractPdpController#PDP_MODE_COLOR_UNCOMBINE}</li>
 * </ol>
 * 
 * <p>在构造pdp页面时，需要确定pdp的展示模式，该controller支持上述三种模式，默认为模式二，如果需要定制，需要重写 {@link NebulaAbstractPdpController#getPdpMode(ItemBaseInfoViewCommand)}</p>
 * 
 * <p>一般情况下商品详情页会包含两部分内容：数据展示部分和操作部分，该controller主要实现数据展示部分，操作部分会在该操作对应的模块中定义。</p>
 * 
 * <ul>对于数据部分，主要构造了以下内容：
 * <li>商品基本信息({@link ItemBaseInfoViewCommand})，包括商品名称、编码、标题、简述、描述、类型、SEO等</li>
 * <li>商品属性({@link ItemPropertyViewCommand})，包括销售属性和非销售属性</li>
 * <li>商品切换({@link ItemColorSwatchViewCommand})，模式二时，pdp切换颜色相当于切换了商品</li>
 * <li>商品图片({@link ItemImageViewCommand})</li>
 * <li>Sku({@link SkuViewCommand})，Sku中包含了的库存信息，并不是一个准确的库存，因为pdp的这些数据会被缓存。</li>
 * <li>商品价格({@link PriceViewCommand})，这里的价格是对商品价格的封装，主要用于页面展示。</li>
 * <li>商品扩展信息({@link ItemExtraViewCommand})，包括销量、收藏量、评论量等，默认不同步加载，需要加载时重写 {@link NebulaAbstractPdpController#isSyncLoadItemExtra()}</li>
 * <li>商品推荐({@link RelationItemViewCommand})，默认不同步加载，需要加载时重写 {@link NebulaAbstractPdpController#isSyncLoadRecommend()}</li>
 * </ul>
 * 
 * <ul>提供的公开方法：
 * <li>进入商品详情页: {@link #showPdp(String, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>获取浏览历史记录: {@link #getItemBrowsingHistory(Long, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>获取推荐商品: {@link #getItemPdpRecommend(Long, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>加载商品库存: {@link #getItemInventory(Long, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>商品切换: {@link #getItemColorSwatch(String, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>获得商品评论: {@link #showItemReview(Long, PageForm, HttpServletRequest, HttpServletResponse, Model)}</li>
 * </ul>
 * 
 * <ul>提供了一些扩展点：
 * <li>{@link #getPdpMode(ItemBaseInfoViewCommand)} : 获取Pdp的显示模式，默认模式二</li>
 * <li>{@link #getItemRecommendCacheExpireSeconds()} : 获取itemRecommend缓存的有效期，默认1天</li>
 * <li>{@link #getPdpViewCommandCacheExpireSeconds()} : 获取pdp数据缓存的有效期，默认5分钟</li>
 * <li>{@link #isSyncLoadRecommend()} : 是否在进入pdp时即同步加载推荐商品，默认false</li>
 * <li>{@link #isSyncLoadItemExtra()} : 是否在进入pdp时即同步加载商品扩展信息，默认false</li>
 * <li>{@link #getBuyLimit(Long)} : 获取sku最大可购买的数量，默认6</li>
 * <li>{@link #getItemMainImageType()} : 获取商品主图的图片类型，默认 ItemImage.IMG_TYPE_LIST</li>
 * <li>{@link #getPdpView(Long, HttpServletRequest, HttpServletResponse, Model)} : 获取pdp的View，ajax请求返回{@link NebulaAbstractPdpController#VIEW_PRODUCT_DETAIL_QUICKVIEW} ，页面请求返回{@link NebulaAbstractPdpController#VIEW_PRODUCT_DETAIL}</li>
 * </ul>
 * 
 * <p>其他的proteced的方法，项目可以根据自己的逻辑重写。</p>
 * 
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年4月14日  上午10:15:30
 */
public class NebulaPdpController extends NebulaAbstractPdpController {

	/**
	 * log定义
	 */
	private static final Logger	LOG									= LoggerFactory.getLogger(NebulaPdpController.class);
	
	
	/**
	 * 进入商品详情页 	
	 * 
	 * @RequestMapping(value = "/item/{itemCode}", method = RequestMethod.GET)
	 * 
	 * @param itemCode 商品编码
	 * @param request
	 * @param response
	 * @param model
	 */
	public String showPdp(@PathVariable("itemCode") String itemCode, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			
			PdpViewCommand pdpViewCommand = buildPdpViewCommandWithCache(itemCode);
			
			model.addAttribute(MODEL_KEY_PRODUCT_DETAIL, pdpViewCommand);
			
			return getPdpView(pdpViewCommand.getBaseInfo().getId(), request, response, model);
			
		} catch (IllegalItemStateException e) {
			
			LOG.error("[PDP_SHOW_PDP] Item state illegal. itemCode:{}, {}", itemCode, e.getState().name());
			
			throw new BusinessException("Show pdp error.");
		}
	}
	
	/**
	 * 获取浏览历史记录
	 * 
	 * @RequestMapping(value = "/item/history.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId 商品Id
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public List<RelationItemViewCommand> getItemBrowsingHistory(@PathVariable("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
			
        return buildItemBrowsingHistoryViewCommand(itemId, request, response);
	        
	}
	
	/**
	 * 获取推荐商品
	 * 
	 * @RequestMapping(value = "/item/recommend.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public List<RelationItemViewCommand> getItemPdpRecommend(@PathVariable("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
	    return buildItemRecommendViewCommandWithCache(itemId);
		
	}
	
	/**
	 * 加载商品库存
	 * 
	 * @RequestMapping(value = "/item/inventory.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public List<InventoryViewCommand> getItemInventory(@PathVariable("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return buildInventoryViewCommand(itemId);
	}
	
	/**
	 * 商品定义到色，需要到款汇聚显示，当切换颜色时，实际上是变更了商品，需要ajax加载该商品的信息
	 * 
	 * @RequestMapping(value = "/item/colorSwatch.json", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * 
	 */
	public NebulaReturnResult getItemColorSwatch(@PathVariable("itemCode") String itemCode, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		DefaultReturnResult result = new DefaultReturnResult();
		
		try {
			
			Map<String, Object> returnObject = new HashMap<String, Object>();
			
			//商品信息
			PdpViewCommand pdpViewCommand = buildSimplePdpViewCommandWithCache(itemCode);
			returnObject.put(MODEL_KEY_PRODUCT_DETAIL, pdpViewCommand);
			
			//库存信息
			List<InventoryViewCommand> inventoryViewCommands = buildInventoryViewCommand(pdpViewCommand.getBaseInfo().getId());
			returnObject.put(MODEL_KEY_INVENTORY, inventoryViewCommands);
			
			result.setReturnObject(returnObject);
			
		} catch (IllegalItemStateException e) {
			LOG.error("[PDP_SWITCH_PDP] get item exception. itemCode:{}, {}", itemCode, e.getState().name());
			
			throw new BusinessException("Show pdp error.");
		}
		
		return result;
	}
	
	/**
	 * 获得商品评论
	 * 
	 * @RequestMapping(value = "/review/get", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult showItemReview(@RequestParam("itemId") Long itemId, @ModelAttribute("page") PageForm pageForm, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		LOG.debug("[PDP_SHOW_ITEM_REVIEW]ItemId:{},CurrentPage:{},Sort:{} [{}] \"{}\"",itemId,pageForm.getCurrentPage(),pageForm.getSort(),new Date(),this.getClass().getSimpleName());
		
		Pagination<RateCommand> rates = itemRateManager.findItemRateListByItemId(pageForm.getPage(), itemId, pageForm.getSorts());
		
		List<RateCommand> items  = rates.getItems();
		List<MemberCommand> members = getMemberCommandsByRates(items);
		
		//convert to itemreviewViewCommand
		Pagination<ItemReviewViewCommand> itemReviewViewCommands 
					= itemReviewViewCommandConverter.convertFromTwoObjects(rates, reviewMemberViewCommandConverter.convert(members));
		
		model.addAttribute("itemReviewViewCommands", itemReviewViewCommands);
		
		return DefaultReturnResult.SUCCESS;
	}
	
	@Override
	protected Integer getBuyLimit(Long itemId) {
		return DEFAULT_SKU_BUY_LIMIT;
	}

	@Override
	protected String getItemMainImageType() {
		return ItemImage.IMG_TYPE_LIST;
	}
	
	@Override
	protected boolean isSyncLoadItemExtra() {
		return false;
	}
	
	@Override
	protected boolean isSyncLoadRecommend() {
		return false;
	}

	@Override
	protected Integer getPdpViewCommandCacheExpireSeconds() {
		// 5分钟
		return 5 * TimeInterval.SECONDS_PER_MINUTE;
	}
	
	@Override
	protected Integer getItemRecommendCacheExpireSeconds() {
		// 1天
		return TimeInterval.SECONDS_PER_DAY;
	}
	
	/**
	 * PDP支持的模式, 默认模式二，商品定义到色，PDP根据款号聚合
	 */
	@Override
	protected String getPdpMode(ItemBaseInfoViewCommand itemBaseInfo) {
		return PDP_MODE_COLOR_COMBINE;
	}
	
}
