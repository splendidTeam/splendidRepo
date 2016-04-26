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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.exception.IllegalItemStateException.IllegalItemState;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.manager.product.ItemRecommandManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.web.controller.product.converter.BreadcrumbsViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.RelationItemViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.BrowsingHistoryResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.BrowsingHistoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.DefaultBrowsingHistoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemCategoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;
import com.feilong.tools.jsonlib.JsonUtil;


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
public abstract class NebulaAbstractPdpController extends NebulaBasePdpController {

	/**
	 * log定义
	 */
	private static final Logger	LOG										= LoggerFactory.getLogger(NebulaAbstractPdpController.class);
	
	//PDP的展示模式
	/** PDP的展示模式  模式一, 商品到款，PDP到款显示. [value: pdp_mode_style] */
	public static final String 		PDP_MODE_STYLE 						= "pdp_mode_style";
	
	/** PDP的展示模式  模式二， 商品到色，PDP根据款号聚合（到款显示）. [value: pdp_mode_color_combine] */
	public static final String 		PDP_MODE_COLOR_COMBINE 				= "pdp_mode_color_combine";
	
	/** PDP的展示模式 模式三， 商品到色，PDP不需要聚合（到色显示）. [value: pdp_mode_color_uncombine] */
	public static final String 		PDP_MODE_COLOR_UNCOMBINE 			= "pdp_mode_color_uncombine";
	
	//面包屑的模式
	/** 面包屑的模式  模式一, 基于前端导航构建. [value: breadcrumbs_mode_navigation] */
	public static final String 		BREADCRUMBS_MODE_NAVIGATION 		= "breadcrumbs_mode_navigation";
	
	/** 面包屑的模式  模式二, 基于后端分类构建. [value: breadcrumbs_mode_category] */
	public static final String 		BREADCRUMBS_MODE_CATEGORY 			= "breadcrumbs_mode_category";
	
	/** 商品推荐的模式  模式一, 后台配置. [value: recommend_mode_general] */
	public static final String 		RECOMMEND_MODE_GENERAL 			    = "recommend_mode_general";
	
	/** 商品推荐的模式  模式一, 自定义 需要商城自己实现. [value: recommend_mode_custom] */
	public static final String 		RECOMMEND_MODE_CUSTOM 				= "recommend_mode_custom";
	
	// 每个sku默认最大购买的数量
	/** 每个sku默认最大购买的数量. [value: 6] */
	public static final Integer 	DEFAULT_SKU_BUY_LIMIT 				= 6;
	
	//model key的常量定义
	/** 商品详情页 的相关展示数据 */
	public static final String		MODEL_KEY_PRODUCT_DETAIL			= "product";
	
	/** 用户浏览商品历史记录 */
	public static final String      MODEL_KEY_BROWSING_HISTORY          = "history";
	 
	/** 商品库存 */
	public static final String      MODEL_KEY_INVENTORY                 = "inventory";
	
	/** PDP商品推荐 */
	public static final String      MODEL_KEY_PDP_RECOMMEND             = "recommend";
	
	//view的常量定义
	/** 商品详情页 的默认定义 */
	public static final String		VIEW_PRODUCT_DETAIL					= "product.detail";
	
	public static final String 		ITEM_EXTRA_CACHE_KEY				= "item_extra_cache_key";
	
	@Autowired
	private ItemRecommandManager itemRecommandManager;
	
	@Qualifier("breadcrumbsViewCommandConverter")
	private BreadcrumbsViewCommandConverter breadcrumbsViewCommandConverter;
	
	@Autowired
	protected ItemColorSwatchViewCommandResolver colorSwatchViewCommandResolver;
	
	@Autowired
	RelationItemViewCommandConverter relationItemViewCommandConverter;
	
    /** The browsing history resolver. */
    @Autowired
    private BrowsingHistoryResolver     browsingHistoryResolver;
	
	/**
	 * 构造PdpViewCommand
	 * @throws IllegalItemStateException 
	 */
	protected PdpViewCommand buildPdpViewCommand(String itemCode) throws IllegalItemStateException {
		
		PdpViewCommand pdpViewCommand = new PdpViewCommand();
		
		//商品基本信息
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		pdpViewCommand.setBaseInfo(itemBaseInfo);
		
		//面包屑
		pdpViewCommand.setBreadcrumbs(buildBreadcrumbsViewCommand(itemBaseInfo.getId()));
		
		//商品图片
		pdpViewCommand.setImages(buildItemImageViewCommand(itemBaseInfo.getId()));
		
		//商品属性
		pdpViewCommand.setProperties(buildItemPropertyViewCommand(itemBaseInfo, pdpViewCommand.getImages()));
		
		//sku
		pdpViewCommand.setSkus(buildSkuViewCommand(itemBaseInfo.getId()));
		
		//price
		pdpViewCommand.setPrice(buildPriceViewCommand(itemBaseInfo, pdpViewCommand.getSkus()));
		
        //extra
		pdpViewCommand.setExtra(buildItemExtraViewCommand(itemCode));
		
		//colorSwatch
		if(PDP_MODE_COLOR_COMBINE.equals(getPdpMode(itemBaseInfo.getId()))) {
			pdpViewCommand.setColorSwatches(buildItemColorSwatchViewCommands(itemBaseInfo));
		}

		//商品推荐信息
		pdpViewCommand.setRecommend(buildItemRecommendViewCommand(itemBaseInfo.getId()));
		
		//移动端分享url
		pdpViewCommand.setMobileShareUrl(buildMobileShareUrl(itemCode));
		
		return pdpViewCommand;
	}
	
	/**
	 * 构造一个简单的PdpViewCommand，主要用于到色商品切换颜色时，取商品基本信息
	 * @param itemCode 商品编码
	 * @return PdpViewCommand 商品信息Comande
	 * @throws IllegalItemStateException
	 */
	protected PdpViewCommand buildSimplePdpViewCommand(String itemCode) throws IllegalItemStateException{
		PdpViewCommand pdpViewCommand = new PdpViewCommand();
		
		//商品基本信息
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		pdpViewCommand.setBaseInfo(itemBaseInfo);
		
		//商品图片
		pdpViewCommand.setImages(buildItemImageViewCommand(itemBaseInfo.getId()));
		
		//商品属性
		pdpViewCommand.setProperties(buildItemPropertyViewCommand(itemBaseInfo, pdpViewCommand.getImages()));
		
		//sku
		pdpViewCommand.setSkus(buildSkuViewCommand(itemBaseInfo.getId()));
		
		//price
		pdpViewCommand.setPrice(buildPriceViewCommand(itemBaseInfo, pdpViewCommand.getSkus()));
		
		return pdpViewCommand;
	}
	
	
	/**
	 * 获取并校验商品基本信息 
	 * @param itemBaseInfo
	 * @return
	 */
	protected ItemBaseInfoViewCommand getAndValidateItemBaseInfo(String itemCode) throws IllegalItemStateException {
		// 取得商品的基本信息
		ItemBaseInfoViewCommand itemBaseInfo = buildItemBaseInfoViewCommand(itemCode);
		
		// 商品不存在
		if (Validator.isNullOrEmpty(itemBaseInfo)) {
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item not exists. itemCode:{}.", itemCode);
            throw new IllegalItemStateException(IllegalItemState.ITEM_NOT_EXISTS);
        }
		
		if(LOG.isInfoEnabled()) {
			LOG.info("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] itemCode:{}, baseInfo:{}", itemCode, JsonUtil.format(itemBaseInfo));
		}
		
		Integer lifecycle = itemBaseInfo.getLifecycle();
		if(Item.LIFECYCLE_DELETED == lifecycle) {
			// 商品逻辑删除
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item logical deleted. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_LOGICAL_DELETED);
		} else if(Item.LIFECYCLE_UNACTIVE == lifecycle) {
			// 商品新建状态
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status new. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_NEW);
		} else if(Item.LIFECYCLE_DISABLE == lifecycle) {
			// 商品未上架
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status offSale. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_OFFSALE);
		}
		
		Date activeBeginTime = itemBaseInfo.getActiveBeginTime();
		if (Validator.isNotNullOrEmpty(activeBeginTime) && !DateUtil.isAfter(new Date(), activeBeginTime)) {
			// 商品未上架
 			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item before active begin time. itemCode:{}, activeBeginTime:{}.", itemCode, activeBeginTime);
            throw new IllegalItemStateException(IllegalItemState.ITEM_BEFORE_ACTIVE_TIME);
        }
		
		if(Constants.ITEM_TYPE_PREMIUMS == itemBaseInfo.getType()) {
			// 商品是赠品
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item is gift. itemCode:{}, type:{}.", itemCode, itemBaseInfo.getType());
            throw new IllegalItemStateException(IllegalItemState.ITEM_ILLEGAL_TYPE_GIFT);
		}
		
		return itemBaseInfo;
	}
	
	/**
	 * 构造商品的分类信息
	 * @param itemId
	 * @return
	 */
	protected ItemCategoryViewCommand buildItemCategoryViewCommand(Long itemId){
		return new ItemCategoryViewCommand();
	}
	
	protected List<ItemColorSwatchViewCommand> buildItemColorSwatchViewCommands(ItemBaseInfoViewCommand baseInfoViewCommand){
		return colorSwatchViewCommandResolver.resolve(baseInfoViewCommand, itemImageViewCommandConverter);
	}
	
	/**
	 * 构造商品的扩展数据
	 * @param itemId
	 * @return
	 */
	protected ItemExtraViewCommand buildItemExtraViewCommand(String itemCode){
		String key = ITEM_EXTRA_CACHE_KEY + "-" + itemCode;
		
		
		ItemExtraViewCommand itemExtraViewCommand = null;
		try{
			itemExtraViewCommand = cacheManager.getObject(key);
		}catch(Exception e){
			LOG.error("[PDP_BUILD_ITETM_EXTRA_VIEW_COMMAND] item extra view command cache exception.itemCode:{},exception:{} [{}] \"{}\"",itemCode,e.getMessage(),new Date(),this.getClass().getSimpleName());
		}
		
		if(itemExtraViewCommand == null){
			itemExtraViewCommand = new ItemExtraViewCommand();
			itemExtraViewCommand.setSales(getItemSales(itemCode));
			itemExtraViewCommand.setFavoriteCount(getItemFavoriteCount(itemCode));
			itemExtraViewCommand.setReviewCount(getItemReviewCount(itemCode));
			itemExtraViewCommand.setRate(getItemRate(itemCode));
			cacheManager.setObject(key ,
					itemExtraViewCommand, TimeInterval.SECONDS_PER_HOUR);
		}
		
		
		
		return itemExtraViewCommand;
	}
	
	/**
	 * 构造推荐商品信息
	 * 方式一：pts为商品设置的的推荐商品
	 * 方式二：自定义
	 * @param itemId
	 * @return
	 */
	protected List<RelationItemViewCommand> buildItemRecommendViewCommand(Long itemId) {
		List<RelationItemViewCommand> itemRecommendList = null;
		
		String itemRecommendMode = getItemRecommendMode();
	
		switch (itemRecommendMode){
		    case RECOMMEND_MODE_CUSTOM:
		    	itemRecommendList = customBuildItemRecommendViewCommand(itemId);
		    	break;
		    default:
		    	List<ItemCommand> itemCommands = itemRecommandManager.getRecommandItemByItemId(itemId, getItemImageType());
		    	itemRecommendList =  relationItemViewCommandConverter.convert(itemCommands);
		        break;
		}
		
		return itemRecommendList;
	}
	
	protected abstract List<RelationItemViewCommand> customBuildItemRecommendViewCommand(Long itemId);
	
	/**
	 * 构造最近浏览的商品信息
	 * @param itemId
	 * @return
	 */
	protected List<RelationItemViewCommand> buildItemBrowsingHistoryViewCommand(HttpServletRequest request,Long itemId) {
		LinkedList<Long> browsingHistoryItemIds = browsingHistoryResolver.getBrowsingHistory(request, Long.class);
		//delete current item
        browsingHistoryItemIds.remove(itemId);
		List<ItemCommand> itemCommands  = sdkItemManager.findItemCommandByItemIds(browsingHistoryItemIds);
		setImageData(browsingHistoryItemIds, itemCommands);
		return relationItemViewCommandConverter.convert(itemCommands);
	}
	
	/**
	 * 更新最近浏览的商品信息
	 * @param itemId
	 * @return
	 */
	protected void constructBrowsingHistoryViewCommand(HttpServletRequest request,HttpServletResponse response,Long itemId) {
		 BrowsingHistoryViewCommand browsingHistoryCommand = new DefaultBrowsingHistoryViewCommand();
         browsingHistoryCommand.setId(itemId);
         browsingHistoryResolver.resolveBrowsingHistory(request, response, browsingHistoryCommand);
	}
	
	private void setImageData(List<Long> itemIdList,List<ItemCommand> itemCommands ) {

		// picUrlMap key： itemId value：picUrl
		Map<Long, String> picUrlMap = new HashMap<Long, String>();

		// 根据商品找到 对应的列表图
		List<ItemImageCommand> cmdList = sdkItemManager.findItemImagesByItemIds(itemIdList, getItemImageType());

		if (Validator.isNotNullOrEmpty(cmdList)) {
			for (ItemImageCommand cmd : cmdList) {
				if (Validator.isNotNullOrEmpty(cmd)) {
					List<ItemImage> imgList = cmd.getItemIamgeList();

					if (Validator.isNotNullOrEmpty(imgList)) {
						Long itemId = cmd.getItemId();
						String imgStr = imgList.get(0).getPicUrl();
						// imgStr = sdkItemManager.convertItemImageWithDomain(imgStr);

						picUrlMap.put(itemId, imgStr);
					}
				}
			}

		}
		
		if (Validator.isNotNullOrEmpty(itemCommands)) {
			for (ItemCommand itemCmd : itemCommands) {
				String picUrl = picUrlMap.get(itemCmd.getId());
				if (null != picUrl) {
					itemCmd.setPicUrl(picUrl);
				}
			}
		}
	}
	
	protected abstract Long getItemSales(String itemCode);
	
	protected abstract Long getItemFavoriteCount(String itemCode);
	
	protected abstract Float getItemRate(String itemCode);
	
	protected abstract Long getItemReviewCount(String itemCode);
	
	protected abstract String buildSizeCompareChart(Long itemId);
	
	protected abstract String buildMobileShareUrl(String itemCode);

	/**
	 * 构造面包屑
	 * @return
	 */
	protected List<BreadcrumbsViewCommand> buildBreadcrumbsViewCommand(Long itemId) {
		
		List<BreadcrumbsViewCommand> breadcrumbsViewCommandList = null;
		
		String breadcrumbsMode = getBreadcrumbsMode();
		
		switch (breadcrumbsMode) {
			case BREADCRUMBS_MODE_NAVIGATION:
				//TODO 
				//基于导航
				break;
			case BREADCRUMBS_MODE_CATEGORY:
				//基于分类
				List<CurmbCommand> curmbCommandList = itemDetailManager.findCurmbList(itemId);
				breadcrumbsViewCommandList =breadcrumbsViewCommandConverter.convert(curmbCommandList);
				break;
			default:
				breadcrumbsViewCommandList = customBuildBreadcrumbsViewCommand(itemId);
				break;
		}
		
		return breadcrumbsViewCommandList;
	}
	
	protected abstract List<BreadcrumbsViewCommand> customBuildBreadcrumbsViewCommand(Long itemId);
	
	/**
	 * 商品推荐图片类型
	 * @return
	 */
	protected abstract String getItemImageType();
	
	/**
	 * 面包屑的模式
	 * @return
	 */
	protected abstract String getBreadcrumbsMode();
	
	/**
	 * 商品推荐的模式
	 * @return
	 */
	protected abstract String getItemRecommendMode();
	
	/**
	 * sku最大可购买的数量
	 * @param itemId
	 * @return
	 */
	protected abstract Integer getBuyLimit(ItemBuyLimitedBaseCommand itemBuyLimitedCommand);
	
	/**
	 * 获取Pdp的显示模式
	 */
	protected abstract String getPdpMode(Long itemId);
}
