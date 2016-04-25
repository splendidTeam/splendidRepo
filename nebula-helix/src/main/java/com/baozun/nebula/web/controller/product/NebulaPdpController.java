
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.converter.ItemReviewViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ReviewMemberViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;
import com.feilong.core.Validator;


/**
 * 商品详情页controller
 * 
 * 
 * TODO 商品展示模式的说明和使用 -- 星语
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

	@Autowired
	private ItemRateManager itemRateManager;
	
	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private ItemReviewViewCommandConverter itemReviewViewCommandConverter;
	
	@Autowired
	private ReviewMemberViewCommandConverter reviewMemberViewCommandConverter;
	
	@Autowired
	private ItemDetailManager	itemDetailManager;
	
	@Autowired
	private ItemColorSwatchViewCommandResolver		colorSwatchViewCommandResolver;
	
	
	/**
	 * 进入商品详情页 	
	 * @RequestMapping(value = "/item/{itemCode}", method = RequestMethod.GET)
	 * 
	 * @param itemCode
	 * @param request
	 * @param response
	 * @param model
	 */
	public String showPdp(@PathVariable("itemCode") String itemCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			
			PdpViewCommand pdpViewCommand = buildPdpViewCommand(itemCode);
			
			model.addAttribute(MODEL_KEY_BROWSING_HISTORY, buildItemBrowsingHistoryViewCommand(request, pdpViewCommand.getBaseInfo().getId()));
			model.addAttribute(MODEL_KEY_PRODUCT_DETAIL, pdpViewCommand);
			
			return VIEW_PRODUCT_DETAIL;
			
		} catch (IllegalItemStateException e) {
			
			LOG.error("[PDP_SHOW_PDP] Item state illegal. itemCode:{}, {}", itemCode, e.getState().name());
			
			throw new BusinessException("Show pdp error.");
		}
	}
	
	/**
	 * 加载商品库存
	 * 
	 * @RequestMapping(value = "/item/inventory/get", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult getItemInventory(@PathVariable("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute(MODEL_KEY_INVENTORY, super.buildInventoryViewCommand(itemId));
		return DefaultReturnResult.SUCCESS;
	}
	
	/**
	 * 商品定义到色，到款显示，切换颜色时，实际上是变更了商品，需要ajax加载该商品信息
	 * 商品定义到款，到款显示，切换颜色时，只是更换了销售属性，此处不需要重新加载
	 * 
	 * @RequestMapping(value = "/item/detail/get", method = RequestMethod.GET)
	 * @ResponseBody
	 * 
	 * @return
	 */
	public NebulaReturnResult switchColorForItem(@PathVariable("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		//重新构造PdpViewCommand TODO 整合
		PdpViewCommand pdpViewCommand =new PdpViewCommand();
		//buildItemBaseInfoViewCommand(itemCode);code?
		ItemBaseInfoViewCommand baseInfoViewCommand =buildItemBaseInfoViewCommand(itemId);
		pdpViewCommand.setBaseInfo(baseInfoViewCommand);
		List<ItemImageViewCommand> imageViewCommands =buildItemImageViewCommand(itemId);
		pdpViewCommand.setImages(imageViewCommands);
		pdpViewCommand.setProperties(buildItemPropertyViewCommand(baseInfoViewCommand,
				imageViewCommands));
		//切换
		
		
		// TODO 同时加入库存信息
		List<SkuViewCommand> skus =buildSkuViewCommand(itemId);
		pdpViewCommand.setSkus(skus );
		pdpViewCommand.setPrice(buildPriceViewCommand(baseInfoViewCommand, skus));
		List<InventoryViewCommand> inventoryViewCommands =buildInventoryViewCommand(itemId);
		//pdpViewCommand.set?
		String pMode =getPdpMode(baseInfoViewCommand.getId());
		if(pMode.equals(PDP_MODE_COLOR_COMBINE)){//?
			List<ItemColorSwatchViewCommand>  colorSwatches =colorSwatchViewCommandResolver.resolve(baseInfoViewCommand);
			pdpViewCommand.setColorSwatches(colorSwatches);
		}
		return new DefaultReturnResult();
	}
	
	
	/**
	 * 加入收藏(这个功能应该在用户收藏的Controller中定义)
	 * 
	 * 如果收藏到商品，传入itemId；如果收藏到sku，传入skuId。
	 * 如果itemId和skuId都传入，则以skuId为准
	 * 
	 * @RequestMapping(value = "/favorite/add", method = RequestMethod.POST)
	 * @ResponseBody
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult addFavorite(@LoginMember MemberDetails memberDetails, 
			@PathVariable("itemId") Long itemId, @PathVariable("skuId") Long skuId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		return new DefaultReturnResult();
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
	 * 集中将rate 的memberId进行封装，然后批量查询提高效率
	 * @param rates
	 * @return
	 */
	private List<MemberCommand> getMemberCommandsByRates(List<RateCommand> rates){
		if(Validator.isNullOrEmpty(rates)){
			return new ArrayList<MemberCommand>();
		}
		
		List<Long> memberIds = new ArrayList<Long>();
		for(RateCommand rate : rates){
			memberIds.add(rate.getMemberId());
		}
		return memberManager.findMembersByIds(memberIds);
	}
	
	
	/**
	 * 面包屑的模式
	 * @return
	 */
	@Override
	protected String getBreadcrumbsMode() {
		
		return BREADCRUMBS_MODE_CATEGORY;
	}
	
	@Override
	protected List<BreadcrumbsViewCommand> customBuildBreadcrumbsViewCommand(
			Long itemId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String buildSizeCompareChart(Long itemId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Integer getBuyLimit(ItemBuyLimitedBaseCommand itemBuyLimitedCommand) {
		return itemDetailManager.getItemBuyLimited(itemBuyLimitedCommand, DEFAULT_SKU_BUY_LIMIT);
	}


	@Override
	protected Long getItemSales(String itemCode) {
		return itemDetailManager.findItemSalesCount(itemCode).longValue();
	}


	@Override
	protected Long getItemFavoriteCount(String itemCode) {
		return itemDetailManager.findItemFavCount(itemCode).longValue();
	}


	@Override
	protected Float getItemRate(String itemCode) {
		return itemDetailManager.findItemAvgReview(itemCode);
	}


	@Override
	protected String buildQrCodeUrl(Long itemId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Long getItemReviewCount(String itemCode) {
		return itemRateManager.findRateCountByItemCode(itemCode).longValue();
	}

	@Override
	protected List<RelationItemViewCommand> customBuildItemRecommendViewCommand(
			Long itemId) {
		return new ArrayList<RelationItemViewCommand>();
	}

	@Override
	protected String getItemImageType() {
		return ItemImage.IMG_TYPE_LIST;
	}

	@Override
	protected String getItemRecommendMode() {
		return RECOMMEND_MODE_GENERAL;
	}

}
