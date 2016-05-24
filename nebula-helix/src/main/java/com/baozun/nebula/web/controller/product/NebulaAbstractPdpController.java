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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.ItemExtraDataCommand;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.exception.IllegalItemStateException.IllegalItemState;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemRateManager;
import com.baozun.nebula.manager.product.ItemRecommandManager;
import com.baozun.nebula.manager.system.AbstractCacheBuilder;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.converter.ItemReviewViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.RelationItemViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ReviewMemberViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.BrowsingHistoryResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.BrowsingHistoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.DefaultBrowsingHistoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemCategoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.RelationItemViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;
import com.feilong.tools.jsonlib.JsonUtil;


/**
 * 商品详情页controller
 * 
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年4月14日  上午10:15:30
 */
public abstract class NebulaAbstractPdpController extends NebulaBasePdpController {

	/**
	 * log定义
	 */
	private static final Logger		LOG									= LoggerFactory.getLogger(NebulaAbstractPdpController.class);
	
	//PDP的展示模式
	/** PDP的展示模式  模式一, 商品到款，PDP到款显示. [value: pdp_mode_style] */
	public static final String 		PDP_MODE_STYLE 						= "pdp_mode_style";
	
	/** PDP的展示模式  模式二， 商品到色，PDP根据款号聚合（到款显示）. [value: pdp_mode_color_combine] */
	public static final String 		PDP_MODE_COLOR_COMBINE 				= "pdp_mode_color_combine";
	
	/** PDP的展示模式 模式三， 商品到色，PDP不需要聚合（到色显示）. [value: pdp_mode_color_uncombine] */
	public static final String 		PDP_MODE_COLOR_UNCOMBINE 			= "pdp_mode_color_uncombine";
	
	// 每个sku默认最大购买的数量
	/** 每个sku默认最大购买的数量. [value: 6] */
	public static final Integer 	DEFAULT_SKU_BUY_LIMIT 				= 6;
	
	//model key的常量定义
	/** 商品详情页 的相关展示数据 */
	public static final String		MODEL_KEY_PRODUCT_DETAIL			= "product";
	
	/** 商品库存 */
	public static final String      MODEL_KEY_INVENTORY                 = "inventory";
	
	//view的常量定义
	/** 商品详情页 的默认定义 */
	public static final String		VIEW_PRODUCT_DETAIL					= "product.detail";
	
	/** QuickView商品详情页 的定义 */
	public static final String		VIEW_PRODUCT_DETAIL_QUICKVIEW		= "product.detail.quickview";
	
	//缓存key的定义
	/** pdpViewCommand的缓存key */
	public static final String 		CACHE_KEY_PDP_VIEW_COMMAND			= "nebula_cache_pdp_view_command";
	
	/** simplePdpViewCommand的缓存key */
	public static final String 		CACHE_KEY_SIMPLE_PDP_VIEW_COMMAND	= "cache_key_simple_pdp_view_command";
	
	/** 商品的extra数据的缓存key */
	public static final String 		CACHE_KEY_ITEM_EXTRA				= "nebula_cache_item_extra";
	
	/** 商品的推荐数据的缓存key */
	public static final String 		CACHE_KEY_ITEM_RECOMMEND 			= "nebula_cache_item_recommend";
	
	
	@Autowired
	private ItemRecommandManager itemRecommandManager;
	
	@Autowired
	protected ItemRateManager itemRateManager;
	
	@Autowired
	protected MemberManager memberManager;
	
	@Autowired
	protected ItemColorSwatchViewCommandResolver itemColorSwatchViewCommandResolver;
	
	@Autowired
	@Qualifier("relationItemViewCommandConverter")
	RelationItemViewCommandConverter relationItemViewCommandConverter;
	
    /** The browsing history resolver. */
    @Autowired
    private BrowsingHistoryResolver browsingHistoryResolver;
    
    @Autowired
	@Qualifier("itemReviewViewCommandConverter")
    protected ItemReviewViewCommandConverter itemReviewViewCommandConverter;
    
    @Autowired
	@Qualifier("reviewMemberViewCommandConverter")
    protected ReviewMemberViewCommandConverter reviewMemberViewCommandConverter;
    
    
    
    /**
     * 构造PdpViewCommand
     * <p>
     * 先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
     * </p>
     * @param itemCode
     * @return
     * @throws IllegalItemStateException 
     */
    protected PdpViewCommand buildPdpViewCommandWithCache(final String itemCode) throws IllegalItemStateException {
    	
    	//缓存的key
    	String pdpViewCommandCachekey = buildCacheKeyWithLang(CACHE_KEY_PDP_VIEW_COMMAND).concat("-").concat(itemCode);
    	
    	//缓存的有效期
    	Integer pdpViewCommandExpireSeconds = getPdpViewCommandCacheExpireSeconds();
    	
    	//先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
    	PdpViewCommand pdpViewCommand = new AbstractCacheBuilder<PdpViewCommand, IllegalItemStateException>(pdpViewCommandCachekey, pdpViewCommandExpireSeconds) {
			@Override
			protected PdpViewCommand buildCachedObject() throws IllegalItemStateException {
				return buildPdpViewCommand(itemCode);
			}
    	}.getCachedObject();
    	
		return pdpViewCommand;
    }
	
	/**
	 * 构造PdpViewCommand
	 * @throws IllegalItemStateException 
	 */
	protected PdpViewCommand buildPdpViewCommand(String itemCode) throws IllegalItemStateException {
		
		PdpViewCommand pdpViewCommand = new PdpViewCommand();
		
		//商品基本信息
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		pdpViewCommand.setBaseInfo(itemBaseInfo);
		
		//商品图片
		pdpViewCommand.setImages(buildItemImageViewCommand(itemBaseInfo.getId()));
		
		//商品属性
		pdpViewCommand.setProperties(buildItemPropertyViewCommand(itemBaseInfo, pdpViewCommand.getImages()));
		
		//商品分类
		pdpViewCommand.setCategories(buildItemCategoryViewCommand(itemBaseInfo.getId()));
		
		//sku
		pdpViewCommand.setSkus(buildSkuViewCommand(itemBaseInfo.getId()));
		
		//price
		pdpViewCommand.setPrice(buildPriceViewCommand(itemBaseInfo, pdpViewCommand.getSkus()));
		
		//每个商品限制购买的数量
		pdpViewCommand.setBuyLimit(getBuyLimit(itemBaseInfo.getId()));
		
        //extra
		if(isSyncLoadItemExtra()) {
			pdpViewCommand.setExtra(buildItemExtraViewCommand(itemBaseInfo));
		}
		
		//colorSwatch
		if(PDP_MODE_COLOR_COMBINE.equals(getPdpMode(itemBaseInfo))) {
			pdpViewCommand.setColorSwatches(buildItemColorSwatchViewCommands(itemBaseInfo));
		}

		//商品推荐信息
		if(isSyncLoadRecommend()) {
			pdpViewCommand.setRecommend(buildItemRecommendViewCommand(itemBaseInfo.getId()));
		}
		
		return pdpViewCommand;
	}
	
	/**
	 * 从缓存中获取一个简单的PdpViewCommand，主要用于到色商品切换颜色时，取商品基本信息
	 * @param itemCode 商品编码
	 * @return PdpViewCommand 商品信息Command
	 * @throws IllegalItemStateException
	 */
	protected PdpViewCommand buildSimplePdpViewCommandWithCache(final String itemCode) throws IllegalItemStateException{
		//缓存的key
    	String simplePdpViewCommandCachekey = buildCacheKeyWithLang(CACHE_KEY_SIMPLE_PDP_VIEW_COMMAND).concat("-").concat(itemCode);
    	
    	//缓存的有效期
    	Integer simplePdpViewCommandExpireSeconds = getPdpViewCommandCacheExpireSeconds();
    	
    	//先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
    	PdpViewCommand pdpViewCommand = new AbstractCacheBuilder<PdpViewCommand, IllegalItemStateException>(simplePdpViewCommandCachekey, simplePdpViewCommandExpireSeconds) {
			@Override
			protected PdpViewCommand buildCachedObject() throws IllegalItemStateException {
				return buildSimplePdpViewCommand(itemCode);
			}
    	}.getCachedObject();
    	
		return pdpViewCommand;
	}
	
	/**
	 * 构造一个简单的PdpViewCommand，主要用于到色商品切换颜色时，取商品基本信息
	 * @param itemCode 商品编码
	 * @return PdpViewCommand 商品信息Command
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
	
	protected List<ItemColorSwatchViewCommand> buildItemColorSwatchViewCommands(ItemBaseInfoViewCommand baseInfoViewCommand){
		return itemColorSwatchViewCommandResolver.resolve(baseInfoViewCommand, itemImageViewCommandConverter);
	}
	
	/**
	 * 评论到款
	 * @param itemId
	 * @param pageForm
	 * @return
	 */
	protected Pagination<ItemReviewViewCommand> buildStyleItemReviewCommandsWithPage(Long itemId,
			PageForm pageForm){
		LOG.debug("[PDP_BUILD_STYLEITEMREVIEWCOMMANDS]ItemId:{},CurrentPage:{},Sort:{} [{}] \"{}\"",itemId,pageForm.getCurrentPage(),pageForm.getSort(),new Date(),this.getClass().getSimpleName());
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoLang(itemId);
		if(Validator.isNullOrEmpty(itemBaseCommand)){
			LOG.warn(
                    "[PDP_BUILD_STYLEITEMREVIEWCOMMANDS] item doesn't exists:{},can not find item by itemid :[{}]",
                    new Date(),
                    itemId);
			return null;
		}
		String style =itemBaseCommand.getStyle();
		Pagination<RateCommand> rates =null;
		if(Validator.isNullOrEmpty(style)){
			LOG.warn(
                    "[PDP_BUILD_STYLEITEMREVIEWCOMMANDS] style is null:{},can not find by itemid :[{}], itemCode =[{}]",
                    new Date(),
                    itemId,
                    itemBaseCommand.getCode());
			rates= itemRateManager.findItemRateListByItemId(pageForm.getPage(), itemId, pageForm.getSorts());
		}else{
			List<ItemCommand> itemCommands =itemDetailManager.findItemListByItemId(itemId, style);
			List<Long> itemIds = new ArrayList<Long>();
			for (ItemCommand itemCommand : itemCommands) {
				itemIds.add(itemCommand.getId());
			}
			rates= itemRateManager.findItemRateListByItemIds(pageForm.getPage(), itemIds, pageForm.getSorts());
		}
		List<RateCommand> items  = rates.getItems();
		List<MemberCommand> members = getMemberCommandsByRates(items);
		
		//convert to itemreviewViewCommand
		Pagination<ItemReviewViewCommand> itemReviewViewCommands 
					= itemReviewViewCommandConverter.convertFromTwoObjects(rates, reviewMemberViewCommandConverter.convert(members));
		return itemReviewViewCommands;
	}
	
	/**
	 * 集中将rate 的memberId进行封装，然后批量查询提高效率
	 * @param rates
	 * @return
	 */
	protected List<MemberCommand> getMemberCommandsByRates(List<RateCommand> rates){
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
	 * 构造商品的扩展数据
	 * @param itemId
	 * @return
	 */
	protected ItemExtraViewCommand buildItemExtraViewCommand(final ItemBaseInfoViewCommand itemBaseInfo){
		String key = CACHE_KEY_ITEM_EXTRA + "-" + itemBaseInfo.getCode();
		Integer expire = TimeInterval.SECONDS_PER_HOUR;
		
		//先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
		ItemExtraViewCommand itemExtraViewCommand = new AbstractCacheBuilder<ItemExtraViewCommand, RuntimeException>(key, expire) {
			@Override
			protected ItemExtraViewCommand buildCachedObject() {
				return buildItemExtraViewCommandFromDB(itemBaseInfo);
			}
    	}.getCachedObject();

		return itemExtraViewCommand;
	}
	
	/**
	 * 从DB里获取扩展数据
	 * @param itemBaseInfo
	 * @return
	 */
	protected ItemExtraViewCommand buildItemExtraViewCommandFromDB(
			ItemBaseInfoViewCommand itemBaseInfo){
		ItemExtraViewCommand extraViewCommand =new ItemExtraViewCommand();
		ItemExtraDataCommand extraDataCommand = itemDetailManager.findItemExtraViewCommand(itemBaseInfo.getId(),
				itemBaseInfo.getCode());
		if(Validator.isNotNullOrEmpty(extraDataCommand)){
			if(Validator.isNotNullOrEmpty(extraDataCommand.getSalesCount())){
				extraViewCommand.setSales(extraDataCommand.getSalesCount().longValue());
			}
			if(Validator.isNotNullOrEmpty(extraDataCommand.getFavoredCount())){
				extraViewCommand.setFavoriteCount(extraDataCommand.getFavoredCount().longValue());
			}
			if(Validator.isNotNullOrEmpty(extraDataCommand.getRankavg())){
				extraViewCommand.setRate(extraDataCommand.getRankavg());
			}
			//评论数
			extraViewCommand.setReviewCount(extraDataCommand.getReviewCount());
		}
		return extraViewCommand;
	};

	/**
	 * 构造推荐商品信息
	 * <p>
     * 先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
     * </p>
	 * @param itemId
	 * @return
	 */
	protected List<RelationItemViewCommand> buildItemRecommendViewCommandWithCache(final Long itemId) {
				
		//缓存的key
    	String itemRecommendCachekey = buildCacheKeyWithLang(CACHE_KEY_ITEM_RECOMMEND).concat("-").concat(itemId.toString());
    	
    	//缓存的有效期
    	Integer itemRecommendExpireSeconds = getItemRecommendCacheExpireSeconds();
    	
    	//先从缓存中获取，如果缓存中没有则重新生成，并设置入缓存
    	List<RelationItemViewCommand> itemRecommendList = new AbstractCacheBuilder<List<RelationItemViewCommand>, RuntimeException>(itemRecommendCachekey, itemRecommendExpireSeconds) {
			@Override
			protected List<RelationItemViewCommand> buildCachedObject() {
				return buildItemRecommendViewCommand(itemId);
			}
    	}.getCachedObject();
    	
		return itemRecommendList;
	}
	
	/**
	 * 构造推荐商品信息
	 * @param itemId
	 * @return
	 */
	protected List<RelationItemViewCommand> buildItemRecommendViewCommand(Long itemId) {
		List<ItemCommand> itemCommands = itemRecommandManager.getRecommandItemByItemId(itemId, getItemMainImageType());
		List<RelationItemViewCommand> itemRecommendList = relationItemViewCommandConverter.convert(itemCommands);
    	//扩展信息
    	if(Validator.isNotNullOrEmpty(itemRecommendList)) {
    		for(RelationItemViewCommand relationItemViewCommand : itemRecommendList){
    			ItemBaseInfoViewCommand itemBaseInfo = new ItemBaseInfoViewCommand();
    			itemBaseInfo.setCode(relationItemViewCommand.getItemCode());
    			
    			ItemExtraViewCommand itemExtraViewCommand = buildItemExtraViewCommand(itemBaseInfo);
    			relationItemViewCommand.setExtra(itemExtraViewCommand);
    		}
    	}
		return itemRecommendList;
	}
	
	/**
	 * 构造最近浏览的商品信息
	 * @param itemId
	 * @return
	 */
	protected List<RelationItemViewCommand> buildItemBrowsingHistoryViewCommand(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		LinkedList<Long> browsingHistoryItemIds = browsingHistoryResolver.getBrowsingHistory(request, Long.class);
		List<ItemCommand> itemCommands  = sdkItemManager.findItemCommandByItemIds(browsingHistoryItemIds);
		setImageData(browsingHistoryItemIds, itemCommands);
		List<RelationItemViewCommand> browsingHistory = relationItemViewCommandConverter.convert(itemCommands);
		
		//把当前商品放入历史记录
		constructBrowsingHistory(itemId, request, response);
		
		return browsingHistory;
	}
	
	/**
	 * 更新最近浏览的商品信息
	 * @param itemId
	 * @return
	 */
	protected void constructBrowsingHistory(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		 BrowsingHistoryViewCommand browsingHistoryCommand = new DefaultBrowsingHistoryViewCommand();
         browsingHistoryCommand.setId(itemId);
         browsingHistoryResolver.resolveBrowsingHistory(request, response, browsingHistoryCommand);
	}
	
	/**
	 * 构造商品的分类信息
	 * <p>一般情况下后端分类在Pdp展示时不太常用，所以这里暂不实现。需要时请重写该方法。</p>
	 * @param itemId
	 * @return
	 */
	protected List<ItemCategoryViewCommand> buildItemCategoryViewCommand(Long itemId){
		return null;
	}
	
	private void setImageData(List<Long> itemIdList, List<ItemCommand> itemCommands ) {

		// picUrlMap key： itemId value：picUrl
		Map<Long, String> picUrlMap = new HashMap<Long, String>();

		// 根据商品找到 对应的列表图
		List<ItemImageCommand> cmdList = sdkItemManager.findItemImagesByItemIds(itemIdList, getItemMainImageType());

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
	
	/**
	 * 获得pdp的View
	 * <p>如果是ajax请求，返回quickView的pdp，否则返回pdp页面。quickView的pdp一般用于在列表页或其他页面弹出一个pdp的快速查看层。</p>
	 * @return pdp的view
	 */
	protected String getPdpView(Long itemId, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(request.getHeader("X-Requested-With") != null) {
			return VIEW_PRODUCT_DETAIL_QUICKVIEW;
		}
		return VIEW_PRODUCT_DETAIL;
	}
	
	/**
	 * 商品主图的图片类型，主要用于推荐商品等的图片显示
	 */
	protected abstract String getItemMainImageType();
	
	/**
	 * sku最大可购买的数量
	 * @param itemId
	 * @return
	 */
	protected abstract Integer getBuyLimit(Long itemId);
	
	/**
	 * 是否在进入pdp时即同步加载商品扩展信息
	 * @return
	 */
	protected abstract boolean isSyncLoadItemExtra();
	
	/**
	 * 是否在进入pdp时即同步加载推荐商品
	 * @return
	 */
	protected abstract boolean isSyncLoadRecommend();
	
	/**
	 * 获取pdpViewCommand缓存的有效期
	 * @return
	 */
	protected abstract Integer getPdpViewCommandCacheExpireSeconds();
	
	/**
	 * 获取itemRecommend缓存的有效期
	 * @return
	 */
	protected abstract Integer getItemRecommendCacheExpireSeconds();
	
	/**
	 * 获取Pdp的显示模式
	 */
	protected abstract String getPdpMode(ItemBaseInfoViewCommand itemBaseInfo);
	
	/**
	 * 获取带语言的缓存的key
	 * @param cacheKey
	 */
	protected String buildCacheKeyWithLang(String cacheKey) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n) {
			String lang = LangUtil.getCurrentLang();
			return cacheKey.concat("_").concat(lang);
		}
		return cacheKey;
	}
}
