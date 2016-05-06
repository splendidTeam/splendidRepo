
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
import javax.validation.Valid;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;


/**
 * 商品详情页controller
 * 一般情况下商品详情页会包含两部分内容：
 * 一、数据展示部分
 * 二、操作部分
 * 
 * 
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
			
			return VIEW_PRODUCT_DETAIL;
			
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
	
	
	/**
	 * 获取面包屑的构建模式，默认根据分类构建
	 */
	@Override
	protected String getBreadcrumbsMode() {
		return BREADCRUMBS_MODE_CATEGORY;
	}
	
	@Override
	protected List<BreadcrumbsViewCommand> customBuildBreadcrumbsViewCommand(
			Long itemId) {
		return null;
	}

	@Override
	protected String buildSizeCompareChart(Long itemId) {
		return null;
	}

	@Override
	protected Integer getBuyLimit(Long itemId) {
		return DEFAULT_SKU_BUY_LIMIT;
	}


	@Override
	protected Long getItemSales(ItemBaseInfoViewCommand itemBaseInfo) {
		return itemDetailManager.findItemSalesCount(itemBaseInfo.getCode()).longValue();
	}


	@Override
	protected Long getItemFavoriteCount(ItemBaseInfoViewCommand itemBaseInfo) {
		return itemDetailManager.findItemFavCount(itemBaseInfo.getCode()).longValue();
	}


	@Override
	protected Float getItemRate(ItemBaseInfoViewCommand itemBaseInfo) {
		return itemDetailManager.findItemAvgReview(itemBaseInfo.getCode());
	}

	@Override
	protected Long getItemReviewCount(ItemBaseInfoViewCommand itemBaseInfo) {
		return itemRateManager.findRateCountByItemCode(itemBaseInfo.getCode()).longValue();
	}
	
	/* 
	 * @see com.baozun.nebula.web.controller.product.NebulaAbstractPdpController#buildItemExtraViewCommandFromDB(com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand)
	 */
	@Override
	protected ItemExtraViewCommand buildItemExtraViewCommandFromDB(
			ItemBaseInfoViewCommand itemBaseInfo) {
		ItemExtraViewCommand extraViewCommand =new ItemExtraViewCommand();
		ItemSolrCommand itemSolrCommand = itemDetailManager.findItemExtraViewCommand(itemBaseInfo.getId());
		if(Validator.isNotNullOrEmpty(itemSolrCommand)){
			if(Validator.isNotNullOrEmpty(itemSolrCommand.getSalesCount())){
				extraViewCommand.setSales(itemSolrCommand.getSalesCount().longValue());
			}
			if(Validator.isNotNullOrEmpty(itemSolrCommand.getFavoredCount())){
				extraViewCommand.setFavoriteCount(itemSolrCommand.getFavoredCount().longValue());
			}
			if(Validator.isNotNullOrEmpty(itemSolrCommand.getRankavg())){
				extraViewCommand.setRate(itemSolrCommand.getRankavg());
			}
			//评论数
			//extraViewCommand.setReviewCount(reviewCount);
		}
		return extraViewCommand;
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
