package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemRateDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.sns.ConsultantsDao;
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
import com.baozun.nebula.model.product.SkuInventoryLog;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryLogManager;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.Validator;

@Service("sdkItemManager")
@Transactional
public class SdkItemManagerImpl implements SdkItemManager {

	@Value("#{meta['upload.img.domain.base']}")
	private String				imgDomainUrl	= "";

	@Autowired
	private ItemDao				itemDao;

	@Autowired
	private ItemInfoDao			itemInfoDao;

	@Autowired
	private ItemImageDao		itemImageDao;

	@Autowired
	private SkuDao				skuDao;

	@Autowired
	private SdkSkuInventoryDao	sdkSkuInventoryDao;

	@Autowired
	private ShopDao				shopDao;

	@Autowired
	private ItemPropertiesDao	itemPropertiesDao;

	@Autowired
	private ItemRateDao			itemRateDao;

	@Autowired
	private ItemCategoryDao		itemCategoryDao;

	@Autowired
	private CategoryDao			categoryDao;

	@Autowired
	private ConsultantsDao		consultantsDao;

	@Autowired
	private ItemSolrManager		itemSolrManager;

	@Autowired
	private PropertyValueDao	propertyValueDao;

	@Autowired
	private PropertyDao			propertyDao;

	@Autowired
	private  SdkSkuInventoryLogManager sdkSkuInventoryLogManager;
	
	@Override
	@Transactional(readOnly=true)
	public Item findItemById(Long itemId) {
		return itemDao.findItemById(itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemInfo findItemInfoByItemId(Long itemId) {
		return itemInfoDao.findItemInfoByItemId(itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemBaseCommand findItemBaseInfo(Long itemId) {
		return itemDao.findItemBaseInfo(itemId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ItemBaseCommand findItemBaseInfoByCode(String code) {
		boolean  i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			ItemBaseCommand itemBaseCommand = itemDao.findItemBaseInfoByCodeLang(code, lang);
			return itemBaseCommand;
		}else{
			ItemBaseCommand itemBaseCommand = itemDao.findItemBaseInfoByCode(code);
			return itemBaseCommand;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemImage> findItemImgList(Map<String, Object> paramMap) {
		List<ItemImage> itemImageList = itemImageDao.findItemImageByItemPropAndItemId(paramMap);
		return itemImageList;
	}

	@Override
	@Transactional(readOnly=true)
	public SkuCommand findInventory(Map<String, Object> paramMap) {
		SkuCommand skuCommand = skuDao.findInventory(paramMap);
		return skuCommand;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemProperties> findItemPropertiesByItemId(Long itemId) {
		boolean  i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemIdI18n(itemId, lang);
			return itemPropertiesList;
		}else{
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemId(itemId);
			return itemPropertiesList;
		}
	}
	
	@Override
	public List<ItemProperties> findItemPropertiesByIds(List<Long> ids) {
		boolean  i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByIdsI18n(ids, lang);
			return itemPropertiesList;
		}else{
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByIds(ids);
			return itemPropertiesList;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<RateCommand> findItemRateListByItemId(Page page, Long itemId, Sort[] sorts) {
		Pagination<RateCommand> ItemRateCommandPage = itemRateDao.findItemRateListByItemId(page, sorts, itemId);
		return ItemRateCommandPage;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Property> findPropertyListByIndustryIdAndShopId(Long industryId, Long shopId, Sort[] sorts) {
		return shopDao.findPropertyListByIndustryIdAndShopId(industryId, shopId, sorts);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<ConsultantCommand> findConsultantsListByItemId(Page page, Long itemId, Sort[] sorts) {
		return consultantsDao.findConsultantsListByItemId(page, itemId, sorts);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemCategory findDefaultCateoryByItemId(Long itemId) {
		return itemCategoryDao.findDefaultCateoryByItemId(itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public PropertyValue findPropertyValueById(Long id) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return propertyValueDao.findPropertyValueByIdI18n(id,lang);
		}else{
			return propertyValueDao.findPropertyValueById(id);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemCommand> findItemCommandByItemIds(List<Long> itemIds) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return itemInfoDao.findItemCommandListByItemIdsI18n(itemIds,lang);
		}else{
			return itemInfoDao.findItemCommandListByItemIds(itemIds);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Property> findPropertyListByIds(List<Long> propertyIds, Sort[] sorts) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return propertyDao.findPropertyListByIdsI18n(propertyIds, sorts,lang);
		}else{
			return propertyDao.findPropertyListByIds(propertyIds, sorts);
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<PropertyValue> findPropertyValueListByIds(List<Long> propertyValueIds) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return propertyValueDao.findPropertyValueListByIdsI18n(propertyValueIds, lang);
		}else{
			return propertyValueDao.findPropertyValueListByIds(propertyValueIds);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Sku> findSkuByItemId(Long itemId) {
		return skuDao.findSkuByItemId(itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SkuInventory> findSkuInventoryByExtentionCodes(List<String> extentionCodeList) {
		return sdkSkuInventoryDao.findSkuInventoryByExtentionCodes(extentionCodeList);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemImage> findItemImageByItemIds(List<Long> itemIds) {
		return itemImageDao.findItemImageByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemImage> findItemImageByItemIds(List<Long> itemIds, String imageType) {
		if(LangProperty.getI18nOnOff()){
			// 获得商品的图片
			String lang = LangUtil.getCurrentLang();
			return itemImageDao.findItemImageByItemIdsI18n(itemIds, imageType, lang);
		}else{
			// 获得商品的图片
			return itemImageDao.findItemImageByItemIds(itemIds, imageType);
		}
	}

	@Override
	public List<ItemImageCommand> findItemImagesByItemIds(List<Long> itemIds, String type) {
		List<ItemImageCommand> itemImageCommandList = new ArrayList<ItemImageCommand>();
		ItemImageCommand itemImageCommand = null;
		boolean i18n = LangProperty.getI18nOnOff();
		List<ItemImage> itemImageList = null;
		if(i18n){
			// 获得商品的图片
			String lang = LangUtil.getCurrentLang();
			itemImageList = itemImageDao.findItemImageByItemIdsI18n(itemIds, type,lang);
		}else{
			// 获得商品的图片
			itemImageList = itemImageDao.findItemImageByItemIds(itemIds, type);
		}

		// key: itemId|itemProperitesId, value:ItemImage对象
		Map<String, ItemImage> itemPropMap = new HashMap<String, ItemImage>();
		for (ItemImage itemImage : itemImageList) {
			String key = itemImage.getItemId() + "|" + itemImage.getItemProperties();
			if (! itemPropMap.containsKey(key)) {
				itemPropMap.put(key, itemImage);
			}
		}

		for (Long itemId : itemIds) {
			itemImageList = new ArrayList<ItemImage>();
			itemImageCommand = new ItemImageCommand();
			for (Map.Entry<String, ItemImage> entry : itemPropMap.entrySet()) {
				String key = entry.getKey();
				String[] itemIdAndItemPropId = key.split("\\|");
				if (itemIdAndItemPropId[0].equals(String.valueOf(itemId))) {
					itemImageCommand.setItemId(Long.valueOf(itemIdAndItemPropId[0]));
					itemImageList.add(entry.getValue());
				}
			}
			itemImageCommand.setItemIamgeList(itemImageList);
			itemImageCommandList.add(itemImageCommand);
		}
		return itemImageCommandList;
	}

	@Override
	@Transactional(readOnly=true)
	public List<SkuCommand> findInventoryByItemId(Long itemId) {

		return skuDao.findInventoryByItemId(itemId);
	}
	
	@Override
	public List<SkuCommand> findEffectiveSkuInvByItemId(Long itemId) {

		return skuDao.findEffectiveSkuInvByItemId(itemId);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemCommand findItemByCode(String itemCode) {
		return itemDao.findItemCommandByCode(itemCode);
	}

	@Override
	public DataFromSolr findItemInfoByItemCode(QueryConditionCommand queryConditionCommand) {
		return itemSolrManager.queryItemForAll(10, queryConditionCommand, null, null, null, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkItemManager#convertItemImageWithDomain(java.lang.String)
	 */
	@Override
	public String convertItemImageWithDomain(String imgStr) {
		if (Validator.isNotNullOrEmpty(imgStr)) {
			StringBuilder sb = new StringBuilder(imgDomainUrl);
			sb.append(imgStr);
			return sb.toString();
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemImage> findItemImgNormsByItemIdItemProp(Map<String, Object> paramMap) {
		boolean i18n  = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			paramMap.put("lang", lang);
			return itemImageDao.findItemImgNormsByItemIdItemPropI18n(paramMap);
		}else{
			return itemImageDao.findItemImgNormsByItemIdItemProp(paramMap);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Category> findEnableAllCategory() {
		return categoryDao.findEnableAllCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkItemManager#fillItemRankAvg(java.util.List)
	 */
	@Override
	public List<ItemCommand> fillItemRankAvg(List<ItemCommand> itemList) {
		QueryConditionCommand qCmd = new QueryConditionCommand();
		List<String> codeList = new ArrayList<String>();
		if (Validator.isNotNullOrEmpty(itemList)) {
			for (ItemCommand itemCmd : itemList) {
				codeList.add(itemCmd.getCode());
			}

			qCmd.setCodeList(codeList);
			qCmd.setIsSpread(false);
			List<ItemResultCommand> sorlCmdList = itemSolrManager.queryItemByCode(qCmd);

			if (Validator.isNotNullOrEmpty(sorlCmdList)) {
				for (ItemCommand itemCmd : itemList) {
					for (ItemResultCommand solrCmd : sorlCmdList) {
						List<String> codes = solrCmd.getCode();
						List<Float> rankAvgs = solrCmd.getRankavg();

						for (int i = 0; i < codes.size(); i++) {
							if (itemCmd.getCode().equals(solrCmd.getCode().get(0))) {
								itemCmd.setRankavg(rankAvgs.get(i));
								if (itemCmd.getRankavg() == null || itemCmd.getRankavg().equals(0.0F)) {
									itemCmd.setRankavg(5.0F);
								}
								break;
							}
						}

					}
				}
			}

		}
		return itemList;
	}

	@Override
	public void saveSkuInventory(SkuInventory skuInventory) {
		// sdkSkuInventoryDao.insertSkuInventory(extentionCodeList);
		sdkSkuInventoryDao.save(skuInventory);
	}
	/**
	 * 保存商品库存记录 和对应日志记录
	 */
	@Override
	public void saveSkuInventory(SkuInventory skuInventory ,Long userId) {
		saveSkuInventory(skuInventory);
		//何波 添加修改商品库存的日志记录
		SkuInventoryLog log = new SkuInventoryLog();
		log.setUserId(userId);
		log.setExtentionCode(skuInventory.getExtentionCode());
		log.setModifyTime(new Date());
		log.setQty(skuInventory.getAvailableQty());
		log.setType(SkuInventoryLog.TYPE_PTS);
		sdkSkuInventoryLogManager.saveSkuInventoryLog(log);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkItemManager#getSkuInventoryByExtentionCode(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public SkuInventory getSkuInventoryByExtentionCode(String extentionCode) {
		return sdkSkuInventoryDao.findSkuInventoryByExtentionCode(extentionCode);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemCommand> findItemCommandByStyle(String style) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return itemDao.findItemCommandByStyleI18n(style, lang);
		}else{
			return itemDao.findItemCommandByStyle(style);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public ItemCommand findItemCommandById(Long itemId) {
		return itemDao.findItemCommandById(itemId);
	}

	@Override
	public Integer syncItemPriceByCode(BigDecimal salesPrice, BigDecimal listPrice, String itemCode) {
		Integer rows = itemInfoDao.syncItemPriceByCode(salesPrice, listPrice, itemCode);
		return rows;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemCommand> findItemCommandByCodes(List<String> itemCodes) {
		return itemDao.findItemCommandByCodes(itemCodes);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Item> findAllOnSalesItemList() {
		return itemDao.findAllOnSalesItemList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Sku> findAllOnSalesSkuList() {
		return skuDao.findAllOnSalesSkuList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Sku> findOnSalesItemListWithPage(Page page, Sort[] sorts) {
		return skuDao.findOnSalesItemListWithPage(page, sorts);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Sku> findEffectSkuByItemIds(List<Long> itemIds) {
		return skuDao.findEffectSkuByItemIds(itemIds);
	}

	@Override
	public Item saveItem(Item item) {
		return itemDao.save(item);
	}

	@Override
	public ItemInfo saveItemInfo(ItemInfo itemInfo) {
		return itemInfoDao.save(itemInfo);
	}

	@Override
	public Sku saveSku(Sku sku) {
		return skuDao.save(sku);
	}

	@Override
	public Property saveProperty(Property property) {
		return propertyDao.save(property);
	}

	@Override
	public ItemProperties saveItemProperties(ItemProperties itemProperties) {
		return itemPropertiesDao.save(itemProperties);
	}

	@Override
	public PropertyValue savePropertyValue(PropertyValue propertyValue) {
		return propertyValueDao.save(propertyValue);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Item> findAllDeletedItemList() {
		return itemDao.findAllDeletedItemList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIds(List<Long> itemIds, String propName) {
		return itemPropertiesDao.findPropertyValueByPropertyNameAndItemIds(propName, itemIds);
	}

	@Override
	public ItemBaseCommand findItemBaseInfoLang(Long itemId) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			return itemDao.findItemBaseInfoLang(itemId,lang);
		}else{
			return itemDao.findItemBaseInfo(itemId);
		}
	}

	@Override
	public List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIdsI18n(
			List<Long> itemIds, String propName) {
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			String lang = LangUtil.getCurrentLang();
			List<ItemPropertiesCommand>  list = itemPropertiesDao.findPropertyValueByPropertyNameAndItemIdsI18n(propName, itemIds,lang);
			return list;
		}else{
			return itemPropertiesDao.findPropertyValueByPropertyNameAndItemIds(propName, itemIds);
		}
	}

	@Override
	public List<ItemInfo> findItemInfoIdsByItemIds(List<Long> itemIds) {
		 List<ItemInfo> infos = itemInfoDao.findItemInfosByItemIds(itemIds);
		 return infos;
	}
	
	public Integer findRateCountByItemCode(String itemCode){
		return itemRateDao.findItemRateCountByItemCode(itemCode);
	}
}
