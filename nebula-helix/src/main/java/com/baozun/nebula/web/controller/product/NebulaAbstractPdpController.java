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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.converter.BreadcrumbsViewCommandConverter;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemCategoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemRecommendViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PdpViewCommand;


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
	private static final Logger	LOG									= LoggerFactory.getLogger(NebulaAbstractPdpController.class);
	
	//PDP的展示模式
	/** PDP的展示模式  模式一, 商品到款，PDP到款显示. [value: pdp_mode_style] */
	public static final String PDP_MODE_STYLE 						= "pdp_mode_style";
	
	/** PDP的展示模式  模式二， 商品到色，PDP根据款号聚合（到款显示）. [value: pdp_mode_color_combine] */
	public static final String PDP_MODE_COLOR_COMBINE 				= "pdp_mode_color_combine";
	
	/** PDP的展示模式 模式三， 商品到色，PDP不需要聚合（到色显示）. [value: pdp_mode_color_uncombine] */
	public static final String PDP_MODE_COLOR_UNCOMBINE 			= "pdp_mode_color_uncombine";
	
	//面包屑的模式
	/** 面包屑的模式  模式一, 基于前端导航构建. [value: breadcrumbs_mode_navigation] */
	public static final String BREADCRUMBS_MODE_NAVIGATION 			= "breadcrumbs_mode_navigation";
	
	/** 面包屑的模式  模式二, 基于后端分类构建. [value: breadcrumbs_mode_category] */
	public static final String BREADCRUMBS_MODE_CATEGORY 			= "breadcrumbs_mode_category";
	
	// 每个sku默认最大购买的数量
	/** 每个sku默认最大购买的数量. [value: 6] */
	public static final Integer DEFAULT_SKU_BUY_LIMIT 				= 6;
	
	//model key的常量定义
	/** 商品详情页 的相关展示数据 */
	public static final String	MODEL_KEY_PRODUCT_DETAIL			= "product";
	
	//view的常量定义
	/** 商品详情页 的默认定义 */
	public static final String	VIEW_PRODUCT_DETAIL					= "product.detail";
	
	/** 商品不存在 的默认定义 */
	public static final String VIEW_PRODUCT_NOTEXIST 				= "product.notexist";
	
	@Autowired
	private SdkItemManager											sdkItemManager;
	
	@Autowired
	private ItemDetailManager										itemDetailManager;
	
	@Autowired
	@Qualifier("breadcrumbsViewCommandConverter")
	private BreadcrumbsViewCommandConverter							breadcrumbsViewCommandConverter;
	
	/**
	 * 构造PdpViewCommand
	 */
	protected PdpViewCommand buildPdpViewCommand(String itemCode) {
		ItemBaseInfoViewCommand itemBaseInfo = buildItemBaseInfoViewCommand(itemCode);
		
		PdpViewCommand pdpViewCommand = new PdpViewCommand();
		
		
		
		return pdpViewCommand;
	}
	
	
	/**
	 * 构造商品的分类信息
	 * @param itemId
	 * @return
	 */
	protected ItemCategoryViewCommand buildItemCategoryViewCommand(Long itemId){
		return new ItemCategoryViewCommand();
	}
	
	/**
	 * 构造商品的扩展数据
	 * @param itemId
	 * @return
	 */
	protected ItemExtraViewCommand buildItemExtraViewCommand(String itemCode){
		ItemExtraViewCommand itemExtraViewCommand = new ItemExtraViewCommand();
		itemExtraViewCommand.setSales(getItemSales(itemCode));
		itemExtraViewCommand.setFavoriteCount(getItemFavoriteCount(itemCode));
		itemExtraViewCommand.setReviewCount(getItemReviewCount(itemCode));
		itemExtraViewCommand.setRate(getItemRate(itemCode));
		return itemExtraViewCommand;
	}
	
	/**
	 * 构造推荐商品信息
	 * TODO 推荐商品的结构未定义,最近浏览的商品和推荐应该是一个结构
	 * @param itemId
	 * @return
	 */
	protected List<ItemRecommendViewCommand> buildItemRecommendViewCommand(Long itemId) {
		List<ItemRecommendViewCommand> itemRecommendList = new ArrayList<ItemRecommendViewCommand>();
		return itemRecommendList;
	}
	
	/**
	 * 构造商品评论信息
	 * TODO 需要新的controller入口，并通过converter转换
	 * @param itemId
	 * @return
	 */
	protected Pagination<ItemReviewViewCommand> buildItemReviewViewCommand(Long itemId, PageForm pageForm) {
		return new Pagination<ItemReviewViewCommand>();
	}
	
	protected abstract Long getItemSales(String itemCode);
	
	protected abstract Long getItemFavoriteCount(String itemCode);
	
	protected abstract Float getItemRate(String itemCode);
	
	protected abstract Long getItemReviewCount(String itemCode);
	
	protected abstract String buildSizeCompareChart(Long itemId);
	
	protected abstract String buildQrCodeUrl(Long itemId,HttpServletRequest request);

	/**
	 * 构造面包屑
	 * @return
	 */
	protected List<BreadcrumbsViewCommand> buildBreadcrumbsViewCommand(Long itemId) {
		
		List<BreadcrumbsViewCommand> breadcrumbsViewCommandList = null;
		
		String breadcrumbsMode = getBreadcrumbsMode();
		
		switch (breadcrumbsMode) {
			case BREADCRUMBS_MODE_NAVIGATION:
				//TODO 基于导航
				break;
			case BREADCRUMBS_MODE_CATEGORY:
				//TODO 基于分类
				List<CurmbCommand> curmbCommandList = itemDetailManager.findCurmbList(itemId);
				breadcrumbsViewCommandList =breadcrumbsViewCommandConverter.convert(curmbCommandList);
				break;
			default:
				breadcrumbsViewCommandList = customBuildBreadcrumbsViewCommand(itemId);
				break;
		}
		
		// TODO
		return breadcrumbsViewCommandList;
	}
	
	protected abstract List<BreadcrumbsViewCommand> customBuildBreadcrumbsViewCommand(Long itemId);
	
	
	/**
	 * 面包屑的模式
	 * @return
	 */
	protected abstract String getBreadcrumbsMode();
	
	/**
	 * sku最大可购买的数量
	 * @param itemId
	 * @return
	 */
	protected abstract Integer getBuyLimit(ItemBuyLimitedBaseCommand itemBuyLimitedCommand);
	
	/**
	 * PDP支持的模式, 默认模式二，商品定义到色，PDP根据款号聚合
	 * 
	 * @return
	 */
	protected String getPdpMode(String itemCode) {
		return PDP_MODE_COLOR_COMBINE;
	}

}
