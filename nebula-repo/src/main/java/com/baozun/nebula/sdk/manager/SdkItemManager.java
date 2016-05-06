package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;

public interface SdkItemManager extends BaseManager{
	
	/**
	 * 通过code查询商品信息
	 *
	 * @param itemCode
	 * @return
	 */
	public ItemCommand findItemByCode(String itemCode);

	/**
	 * 通过code查询商品信息
	 *
	 * @param itemCode
	 * @return
	 */
	public ItemCommand findItemCommandById(Long itemId);
	
	/**
	 * 通过商品Id查询商品信息
	 * @param itemId
	 * @return
	 */
	public Item findItemById(Long itemId);
	
	/**
	 * 通过商品Id查询商品信息
	 *
	 * @param itemId
	 * @return
	 */
	public List<ItemCommand> findItemCommandByItemIds(List<Long> itemIds);
	
	/**
	 * 通过商品Id查询商品信息
	 *
	 * @param itemId
	 * @return
	 */
	public ItemInfo findItemInfoByItemId(Long itemId);
	
	List<ItemInfo> findItemInfoIdsByItemIds(List<Long> itemIds);
	/**
	 * 通过商品Id查询商品基本信息
	 * @param itemId
	 * @return
	 */
	public ItemBaseCommand findItemBaseInfo(Long itemId);
	
	/**
	 * 获取商品图片(包括type=3的商品图片)
	 * @param paramMap : itemId, itemProperties
	 * @return
	 */
	public List<ItemImage> findItemImgList(Map<String, Object> paramMap);
	
	/**
	 * 通过sku的itemProperties属性获取库存
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public SkuCommand findInventory(Map<String, Object> paramMap);
	
	/**
	 * 获取商品动态属性(规格参数)
	 * @param ItemId
	 * @return
	 */
	public List<ItemProperties> findItemPropertiesByItemId(Long itemId);
	
	/**
	 * 获取一批商品动态属性(规格参数)
	 * @param itemIds
	 * @return
	 */
	List<ItemProperties> findItemPropertiesByItemIds(List<Long> itemIds);
	
	/**
	 * 获取商品动态属性(规格参数)
	 * @param ids id列表
	 * @return
	 */
	public List<ItemProperties> findItemPropertiesByIds(List<Long> ids);
	
	/**
	 * 通过itemId查询商品的评价
	 * @param page
	 * @param itemId
	 * @return
	 */
	public Pagination<RateCommand> findItemRateListByItemId(Page page, Long itemId, Sort[] sorts);
	
	/**
	 * 通过itemId查询商品的评价(根据itemIds)
	 * @param page
	 * @param itemIds
	 * @param sorts
	 * @return
	 */
	Pagination<RateCommand> findItemRateListByItemIds(Page page,
			List<Long> itemIds, Sort[] sorts);
	
	/**
	 * 通过商品Id查询商品的咨询
	 *
	 * @param page
	 * @param itemId
	 * @return
	 */
	public Pagination<ConsultantCommand> findConsultantsListByItemId(Page page, Long itemId, Sort[] sorts);
	
	/**
	 * 根据行业id和店铺id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 * @param shopId
	 *            店铺id
	 * @return
	 */
	public List<Property> findPropertyListByIndustryIdAndShopId(Long industryId, Long shopId,Sort[] sorts);
	
	/**
	 * 查询商品的默认分类
	 *
	 * @param itemId
	 * @return
	 */
	public ItemCategory findDefaultCateoryByItemId(Long itemId);
	
	/**
	 * 通过id查询属性值
	 *
	 * @param id
	 * @return
	 */
	public PropertyValue findPropertyValueById(Long id);
	
	/**
	 *	通过ids查询属性列表 
	 *
	 * @param propertyIds
	 * @return
	 */
	public List<Property> findPropertyListByIds(List<Long> propertyIds, Sort[] sorts);
	
	/**
	 * 通过ids查询属性值列表 
	 *
	 * @param propertyValueIds
	 * @return
	 */
	public List<PropertyValue> findPropertyValueListByIds(List<Long> propertyValueIds);
	
	/**
	 * 通过ItemId查询sku集合
	 *
	 * @param itemId
	 * @return
	 */
	public List<Sku> findSkuByItemId(Long itemId);
	
	/**
	 * 通过extentionCode集合查询商品的库存数
	 *
	 * @param extentionCodeList
	 * @return
	 */
	public List<SkuInventory> findSkuInventoryByExtentionCodes(List<String> extentionCodeList);
	
	/**
	 * 通过商品id集合查询商品图片(列表页和两者都)
	 *
	 * @param itemIds
	 * @return
	 */
	public List<ItemImage> findItemImageByItemIds(List<Long> itemIds);
	
	/**
	 * 通过商品id集合查询商品颜色属性的图片集合(两者都)
	  			:每一个颜色属性对应的第一张图片
	  			:图片的路径是相对路径(picUrl)
	 * 
	 * itemImageCommand用到的字段:itemId, ItemImageList
	 *
	 * @param itemIds
	 * @param type:ItemImage.TYPE_LIST, type:ItemImage.TYPE_CONTENT
	 * @return
	 */
	public List<ItemImageCommand> findItemImagesByItemIds(List<Long> itemIds,  String type);
	
	
	/**
	 * 通过itemId获取商品库存
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public List<SkuCommand> findInventoryByItemId(Long itemId);
	
	/**
	 * 通过itemId获取lifescyle为有效的sku的库存
	 * @param itemId
	 * @param itemProperties
	 * @return
	 */
	public List<SkuCommand> findEffectiveSkuInvByItemId(Long itemId);
	
	/**
	 * 通过itemCode取solr中的商品信息
	 *
	 * @param itemCode
	 * @return
	 */
	public DataFromSolr findItemInfoByItemCode(QueryConditionCommand queryConditionCommand);
	
	/**
	 * 将 图片url地址转化为 带域名的 地址
	 * @param imgStr
	 * @return
	 */
	public String convertItemImageWithDomain(String imgStr);
	
	/**
	 * 通过itemProperties和itemId查询商品的规格图片
	 * @param paramMap
	 * @return
	 */
	public List<ItemImage> findItemImgNormsByItemIdItemProp(Map<String, Object> paramMap);
	
	/**
	 * 获取所有可用分类
	 * @return
	 */
	List<Category> findEnableAllCategory();
	





	public List<ItemCommand> fillItemRankAvg(List<ItemCommand> itemList);
	/**
	 * 保存库存
	 * 
	 */
	public void saveSkuInventory(SkuInventory skuInventory);
	public void saveSkuInventory(SkuInventory skuInventory ,Long userId);
	
	public SkuInventory getSkuInventoryByExtentionCode(String extentionCode);

	
	/**
	 * 查询同款商品
	 * @param style
	 * @return
	 */
	public List<ItemCommand> findItemCommandByStyle(String style);
	
	/**
	 * 通过itemCodes查询item 
	 * @param itemCodeList
	 * @return
	 */
	List<ItemCommand> findItemCommandByCodes(List<String> itemCodes);
	
	/**
	 * 同步商品的价格(item级别)
	 * @param salePrice
	 * @param listPrice
	 * @param itemCode
	 * @return
	 */
	public Integer syncItemPriceByCode(BigDecimal salesPrice, BigDecimal listPrice, String itemCode);
	
	/**
	 * 获取有效的Item列表 lifecycle != 2
	 * @return
	 */
	public List<Item> findAllOnSalesItemList();
	
	/**
	 * 获取有效的商品的sku列表 lifecycle != 2
	 * @return
	 */
	public List<Sku> findAllOnSalesSkuList();
	
	/**
	 * 分页获取有效的Item列表 lifecycle != 2
	 * @param page
	 * @param sorts
	 * @return
	 */
	public List<Sku> findOnSalesItemListWithPage(Page page, Sort[] sorts);
	
	/**
	 * 根据itemids查询可用的Sku, lifecycle = 1
	 * @param itemIds
	 * @return
	 */
	public List<Sku> findEffectSkuByItemIds(List<Long> itemIds);
	
	/**
	 * 保存item信息
	 * @param item
	 * @return
	 */
	public Item saveItem(Item item);
	
	/**
	 * 保存itemInfo信息
	 * @param itemInfo
	 * @return
	 */
	public ItemInfo saveItemInfo(ItemInfo itemInfo);
	
	/**
	 * 保存sku的信息
	 * @param sku
	 * @return
	 */
	public Sku saveSku(Sku sku);
	
	/**
	 * 保存property的信息
	 * @param property
	 * @return
	 */
	public Property saveProperty(Property property);
	
	/**
	 * 保存itemProperties的信息
	 * @param itemProperties
	 * @return
	 */
	public ItemProperties saveItemProperties(ItemProperties itemProperties);
	
	/**
	 * 保存PropertyValue的信息
	 * @param propertyValue
	 * @return
	 */
	public PropertyValue savePropertyValue(PropertyValue propertyValue);
	
	/**
	 * 找出已经删除的商品列表
	 * @return
	 */
	public List<Item> findAllDeletedItemList();

	public ItemBaseCommand findItemBaseInfoByCode(String code);
	
	/**
	 * 查询商品属性
	 * @param paramMap
	 * @return
	 */
	public List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIds(List<Long> itemIds, String propName);
	
	List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIdsI18n(List<Long> itemIds, String propName);
	
	
	public ItemBaseCommand findItemBaseInfoLang(Long itemId);
	
	
	/**
	 * 通过商品类型和商品Id集合查询商品图片 
	 * @param itemIds
	 * @param imageType
	 * @return
	 */
	public List<ItemImage> findItemImageByItemIds(List<Long> itemIds, String imageType);
	
	/**
	 * 根据itemCode查询Item的评论数量
	 * @param itemCode
	 * @return
	 */
	public Integer findRateCountByItemCode(String itemCode);
	
	/**
	 * 根据extentionCode查询item
	 * @param extentionCode
	 * @return
	 */
	public Item findItemByExtentionCode(String extentionCode);

	
}
