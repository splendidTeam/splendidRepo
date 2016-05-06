/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.command.ItemBuyLimitedBaseCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.product.ItemExtraDataCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.UserDetails;

/**
 * 商品详细业务逻辑接口层
 * @author chenguang.zhou
 * @date 2014-3-6 下午06:09:10
 *
 */
public interface ItemDetailManager extends BaseManager{

	/**
	 * 获取商品基本信息
	 * @param itemId
	 * @return
	 */
	public ItemBaseCommand findItemBaseInfo(Long itemId, String customBaseInfo);
	
	/**
	 * 根据code获取商品基本信息
	 * @param itemId
	 * @return
	 */
	public ItemBaseCommand findItemBaseInfoByCode(String code);
	
	/**
	 * 获取当前面包屑列表
	 * @param itemId
	 * @return
	 */
	public List<CurmbCommand> findCurmbList(Long itemId);
	
	/**
	 * 获取商品动态属性(规格参数)
	 * @param ItemId
	 * @return
	 */
	public Map<String, Object> findDynamicProperty(Long itemId);
	
	/**
	 * 获取一批商品动态属性(规格参数)
	 * @param itemIds
	 * @return
	 */
	public Map<String, Object> findDynamicPropertyByItemIds(List<Long> itemIds);
	
	/**
	 * <p>新的获取商品动态属性(规格参数)</p>
	 * <p>特殊处理一般属性的获取</p>
	 * @param itemId
	 * @return
	 */
	public Map<String, Object> gatherDynamicProperty(Long itemId);
	
	/**
	 * 通过sku的itemProperties属性获取库存
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public SkuCommand findInventory(Long itemId, String itemProperties);
	
	/**
	 * 最近浏览商品
	 * @return
	 */
	public List<ItemCommand> recentViewItemList(List<Long> itemIds, String imgType);
	
	/**
	 * 获取商品描述
	 * @param itemId
	 * @return
	 */
	public String findItemDesc(Long itemId);
	
	/**
	 * 获取商品图片
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public List<ItemImage> findItemImgList(Long itemId, String itemProperties, String type);
	
	/**
	 * 通过itemId获取商品库存
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public List<SkuCommand> findInventoryByItemId(Long itemId);
	
	/**
	 * 通过code查询商品信息
	 *
	 * @param itemCode
	 * @return
	 */
	public ItemCommand findItemByCode(String itemCode);
	
	/**
	 * 查询商品的平均评分
	 *
	 * @param itemId
	 * @return
	 */
	public Float findItemAvgReview(String itemCode);
	
	/**
	 * 通过itemProperties和itemId查询商品的规格图片
	 * @param itemId
	 * @param itemProp
	 * @param type
	 * @return
	 */
	public List<ItemImage> findItemImgNormsByItemIdItemProp(Long itemId, String itemProp, String type);
	
	/**
	 * 获取商品的促销活动
	 * @param itemId
	 * @return
	 */
	public List<PromotionCommand> findPromotionByItemId(Long itemId, UserDetails userDetails);

	/**
	 * 获取商品的促销活动
	 * @deprecated
	 * @see ItemDetailManager#findPromotionByItemId(itemId, UserDetails)
	 * @param itemId
	 * @return
	 */
	public List<PromotionCommand> findPromotionByItemId(Long itemId, HttpServletRequest request);
	

	/**
	 * 通过商品Id查询同款商品(style相同)
	 * @param itemId
	 * @return
	 */
	public List<ItemCommand> findItemListByItemId(Long itemId, String type);
	
	
	/**
	 * 查询商品默认分类
	 * @param itemId
	 * @return
	 */
	public ItemCategory findDefaultCateoryByItemId(Long itemId);
	
	/**
	 * 商品被收藏的次数
	 * @param itemCode
	 * @return
	 */
	public Integer findItemFavCount(String itemCode);
	
	/**
	 * 通过商品id和商品属性名查询商品的属性
	 * @param itemId
	 * @param propname
	 * @return
	 */
	public List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIds(List<Long> itemIds, String propName);
	

	ItemBaseCommand findItemBaseInfoLang(Long itemId, String customBaseUrl) ;
	
	/**
	 * 通过商品id查询有效的sku
	 * @param itemId
	 * @return
	 */
	List<SkuCommand> findEffectiveSkuInvByItemId(Long itemId);
	
	/**
	 * 根据商品code查询销量
	 * @param itemCode
	 * @return
	 */
	Integer findItemSalesCount(String itemCode);
	
	/**
	 * 根据传入的值进行计算，最终返回计算出的限购数量
	 * @return
	 */
	Integer getItemBuyLimited(ItemBuyLimitedBaseCommand itemBuyLimitedCommand,Integer defaultValue);
	
	/**
	 * 根据extentionCode查询item
	 * @param extentionCode
	 * @return
	 */
	Item findItemByExtentionCode(String extentionCode);
	
	/**
	 * 根据itemId、itemCode查找商品扩展信息
	 * @param itemId
	 * @param itemCode
	 * @return
	 */
	public ItemExtraDataCommand findItemExtraViewCommand(Long itemId, String itemCode);
}
