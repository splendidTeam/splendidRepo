/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.manager.product;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.SkuPropertyCommand;
import com.baozun.nebula.command.product.ItemImageLangCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemPropertiesCommand;
import com.baozun.nebula.command.product.ItemStyleCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.web.command.DynamicPropertyCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * item 操作接口
 * 
 * @author yi.huang
 * @date 2013-7-1 下午04:08:27
 */
public interface ItemManager extends BaseManager {

	/**
	 * 根据商品ids数组查询商品
	 * 
	 * @param itemIds
	 *            商品id数组
	 * @return
	 */
	List<Item> findItemListByItemIds(Long[] itemIds);

	/**
	 * 根据店铺id和行业id查找对应属性
	 * 
	 * @param shopId
	 * @param industryUd
	 * @return
	 */
	List<DynamicPropertyCommand> findDynamicPropertis(Long shopId,
			Long industryId);

	
	/**
	 * 根据行业id查找对应属性 
	 * 
	 * @param shopId
	 * @param industryUd
	 * @return
	 */
	List<DynamicPropertyCommand> findDynamicPropertisByIndustryId(Long industryId);
	/**
	 * 保存商品
	 * 
	 * @param itemCommand
	 * @param shopId
	 * @param categoriesIds
	 * @param propertyValueIds
	 * @param codes
	 * @param iProperties
	 * @param changePropertyJson
	 * @param defaultCategoryId
	 * @return
	 */

	Item createOrUpdateItem(ItemCommand itemCommand, Long shopId,
			Long[] categoriesIds, Long[] propertyValueIds, String[] codes,
			BigDecimal[] salePrices, BigDecimal[] listPrices,
			ItemProperties[] iProperties, String changePropertyJson,
			Long defaultCategoryId) throws Exception;

	/**
	 * 根据商品id查询商品
	 * 
	 * @param id
	 * @return
	 */
	Item findItemById(Long id);

	/**
	 * 根据商品的ids，更新商品的是否已分类状态为state
	 * 
	 * @param id
	 * @return
	 */
	Integer updateItemIsAddCategory(List<Long> ids, Integer state);

	/**
	 * 根据商品的ids，更新商品的是否已加入标签状态为state
	 * 
	 * @param id
	 * @return
	 */
	Integer updateItemIsAddTag(List<Long> ids, Integer state);

	/**
	 * 查询是否有相同的code
	 * 
	 * @param code
	 * @param shopId
	 * @return
	 */
	Integer validateItemCode(String code, Long shopId);

	/**
	 * 根据itemId查找商品信息
	 * 
	 * @param itemId
	 * @return
	 */
	ItemInfo findItemInfoByItemId(Long itemId);
	
	ItemInfoCommand findItemInfoCommandByItemId(Long itemId);

	/**
	 * 根据itemId查找所有属性值
	 * 
	 * @param itemId
	 * @return
	 */
	List<ItemProperties> findItemPropertiesListyByItemId(Long itemId);
	
	List<ItemPropertiesCommand> findItemPropertiesCommandListyByItemId(Long itemId);

	/**
	 * 根据商品itemid数组查询Sku
	 * 
	 * @param itemid
	 * @return
	 */
	List<Sku> findSkuByItemId(Long itemId);

	Sku findSkuBySkuCode(String code);

	List<String> validateUpdateSkuCode(List<SkuPropertyCommand> cmdList);

	/**
	 * 分页获取店铺所有的商品列表
	 * 
	 * @param sorts
	 * @param Page
	 * @param Map
	 * @param shopId
	 * @return
	 */
	Pagination<ItemCommand> findItemListByQueryMap(Page page, Sort[] sorts,
			Map<String, Object> paraMap, Long shopId);

	/**
	 * 分页获取所有有效的商品列表(lifecycle != 2)
	 * 
	 * @param sorts
	 * @param Page
	 * @param Map
	 * @param shopId
	 * @return
	 */
	Pagination<ItemCommand> findEffectItemInfoListByQueryMap(Page page, Sort[] sorts, Map<String, Object> paraMap);

	/**
	 * 启用禁用商品
	 * @param ids
	 * @param state
	 * @param userName
	 * @return
	 */
	Integer enableOrDisableItemByIds(List<Long> ids, Integer state,String userName);

	/**
	 * 定时上架商品
	 * 
	 * @param ids
	 * @param state
	 * @return
	 */
	Integer activeItemByIds(List<Long> ids, Date activeBeginTime);

	/**
	 * 逻辑删除商品
	 * 
	 * @param ids
	 */
	public void removeItemByIds(List<Long> ids);

	/**
	 * 通过itemProperties查询itemImage
	 * 
	 * @param propertyValueId
	 * @param itemId
	 * @return :List<ItemImage>
	 * @date 2014-1-23 上午10:40:50
	 */
	public List<ItemImage> findItemImageByItemPropAndItemId(
			String itemProperties, Long itemId, Long propertyValueId);
	
	 List<ItemImageLangCommand> findItemImageByItemPropAndItemIdI18n(
				String itemProperties, Long itemId, Long propertyValueId);

	/**
	 * 修改最后一次选择的属性的ID
	 * 
	 * @param lastSelectPropertyId
	 * @param itemId
	 * @return :Integer
	 * @date 2014-2-20 上午11:08:15
	 */
	public Integer updateItemInfoLSPIdByItemId(Long lastSelectPropertyId,
			Long itemId);

	/**
	 * 修改最后一次选择的属性值的ID
	 * 
	 * @param lastSelectPropertyId
	 * @param itemId
	 * @return :Integer
	 * @date 2014-2-20 上午11:08:15
	 */
	public Integer updateItemInfoLSPVIdByItemId(Long lastSelectPropertyValueId,
			Long itemId);

	public Item createOrUpdateItem(ItemCommand itemCommand,
			Long[] propertyValueIds, // 动态
			Long[] categoriesIds,// 商品分类Id
			ItemProperties[] iProperties,// 普通商品属性
			SkuPropertyCommand[] skuPropertyCommand// sku 的信息，包含每个sku对应的价格
	) throws Exception;

	/**
	 * 保存商品图片
	 * @param itemImages
	 * @param itemId
	 * @param baseImageUrl
	 * @param isImageTypeGroup
	 */
	public void createOrUpdateItemImage(ItemImage[] itemImages, Long itemId, String baseImageUrl, boolean isImageTypeGroup);
	
	void createOrUpdateItemImageIi18n(ItemImageLangCommand[] itemImages, Long itemId, String baseImageUrl, boolean isImageTypeGroup);

	/**
	 * 导入商品
	 * 
	 * @param is
	 * @param shopId
	 * @throws BusinessException
	 */
	public List<Item> importItemFromFile(InputStream is, Long shopId) throws BusinessException;
	public List<Item> importItemFromFileI18n(InputStream is, Long shopId) throws BusinessException;

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 * @param shopId
	 * @param industryId
	 */
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response, Long shopId, Long industryId);

	/**
	 * 上传商品图片
	 * 
	 * @param file
	 */
	public List<Long> importItemImgFromFile(String path, File zipFile, Long shopId, String uploadType)throws Exception;

	/**
	 * 通过code查询item信息
	 * 
	 * @param Code
	 * @return
	 */
	public ItemCommand findItemCommandByCode(String code, String customBaseUrl);
	
	/**
	 * 根据款号查找同款商品
	 * @param styleCode
	 * @return
	 */
	public List<ItemCommand> findItemCommandsByStyle(String styleCode);

	/**
	 * 
	 * @author 何波
	 * @Description: 根据分类id 查询商品
	 * @param cateIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	List<ItemInfo> findItemInfosByCateIds(List<Long> cateIds);

	/**
	 * @author 何波
	 * @Description:根据itemIds查询ItemInfo
	 * @param itemIds
	 * @return
	 */
	List<ItemInfo> findItemInfosByItemIds(List<Long> itemIds);

	/**
	 * 
	 * @author 何波
	 * @Description: 处理全场优惠价格设置
	 * @param cateIds
	 * @param itemIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	List<ItemInfo> findCallItemInfos(List<Long> cateIds, List<Long> itemIds);

	/**
	 * 
	 * @author 何波
	 * @Description: 根据skuid 查询商品信息
	 * @param itemIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	List<ItemInfo> findItemInfosBySkuids(List<Long> skuIds);
	
	/**
	 * 通过商品编码集合查询商品信息
	 * @param itemCodes
	 * @return
	 */
	List<Item> findItemListByCodes(List<String> itemCodes);

	/**
	 * 通过商品Id集合查询商品信息
	 * @param itemCodes
	 * @return
	 */
	List<ItemCommand> findItemCommandListByIds(List<Long> itemIds);
	
	/**
	 * 通过商品ID删除商品图片
	 * @param itemId
	 */
	void removeItemImageByItemId(Long itemId);
	
	/**
	 * 查询商品信息
	 * @param paramMap
	 * @return
	 */
	List<ItemCommand> findItemCommandByQueryMap(Map<String, Object> paramMap, List<String> itemCodeList);
	
	/**
	 * 查询商品信息(国际化)
	 * @param paraMap
	 * @param itemCodeList
	 * @param langKey
	 * @return
	 */
	List<ItemCommand> findItemCommandByQueryMapAndItemCodesI18n(Map<String, Object> paraMap, List<String> itemCodeList, String langKey);

	/**
	 * 根据属性id到itemProperties表中查询商品数量
	 * @return Integer
	 * @param id
	 * @author 冯明雷
	 * @time 2016年4月7日下午4:07:42
	 */
	Integer findItemCountByPropertyId(Long propertyId);
	
	/**
	 * 条件查询店铺商品款号
	 */
	Pagination<ItemStyleCommand> findStyleListByQueryMap(Page page, Sort[] sorts,
			Map<String, Object> paraMap, Long shopId);
}
