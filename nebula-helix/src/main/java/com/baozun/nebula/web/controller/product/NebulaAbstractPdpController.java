
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

import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.product.viewcommand.BreadcrumbsViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemCategoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemRecommendViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PriceViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;


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
public abstract class NebulaAbstractPdpController extends BaseController {

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

	
	/**
	 * 构造商品基本信息
	 * 这个方法暂时不用考虑pdp显示模式的问题，因为基本信息对于几种模式取法是一样的
	 * @param itemId
	 * @return
	 */
	protected ItemBaseInfoViewCommand buildProductBaseInfoViewCommand(Long itemId) {
		return new ItemBaseInfoViewCommand();
	}
	
	/**
	 * 构造商品的属性信息，包括销售属性和非销售属性
	 * @param itemId
	 * @return
	 */
	protected ItemPropertyViewCommand buildItemPropertyViewCommand(Long itemId) {
		
		return new ItemPropertyViewCommand();
	}
	
	/**
	 * 构造sku信息
	 * @param itemId
	 * @return
	 */
	protected SkuViewCommand buildSkuViewCommand(Long itemId) {
		return new SkuViewCommand();
	}
	
	/**
	 * 构造库存信息
	 * @param itemId
	 * @return
	 */
	protected InventoryViewCommand buildInventoryViewCommand(Long itemId) {
		return new InventoryViewCommand();
	}
	
	/**
	 * 构造价格信息
	 * @return
	 */
	protected PriceViewCommand buildPriceViewCommand() {
		return new PriceViewCommand();
	}
	
	/**
	 * 构造商品的图片
	 * @param itemId
	 * @return
	 */
	protected ItemImageViewCommand buildItemImageViewCommand(Long itemId) {
		
		return new ItemImageViewCommand();
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
	protected ItemExtraViewCommand buildItemExtraViewCommand(Long itemId){
		ItemExtraViewCommand itemExtraViewCommand = new ItemExtraViewCommand();
		itemExtraViewCommand.setSales(getItemSales(itemId));
		itemExtraViewCommand.setFavoriteCount(getItemFavoriteCount(itemId));
		itemExtraViewCommand.setSales(getItemRate(itemId));
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
	
	protected abstract Long getItemSales(Long itemId);
	
	protected abstract Long getItemFavoriteCount(Long itemId);
	
	protected abstract Long getItemRate(Long itemId);
	
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
				//TODO
				break;
			case BREADCRUMBS_MODE_CATEGORY:
				//TODO
				breadcrumbsViewCommandList =new ArrayList<BreadcrumbsViewCommand>();
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
	protected abstract Integer getBuyLimit(Long itemId);
	
	/**
	 * PDP支持的模式, 默认模式二，商品定义到色，PDP根据款号聚合
	 * 
	 * @return
	 */
	protected String getPdpMode(String itemCode) {
		return PDP_MODE_COLOR_COMBINE;
	}

}
