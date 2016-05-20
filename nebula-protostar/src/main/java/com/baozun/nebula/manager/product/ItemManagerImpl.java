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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ItemPropertyCommand;
import com.baozun.nebula.command.ItemTagRelationCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.SkuPropertyCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.ImpItemCommand;
import com.baozun.nebula.command.product.ImpSkuCommand;
import com.baozun.nebula.command.product.ItemImageLangCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemInfoExcelCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.dao.product.ItemImageLangDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemOperateLogDao;
import com.baozun.nebula.dao.product.ItemProValGroupRelationDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemPropertiesLangDao;
import com.baozun.nebula.dao.product.ItemReferenceDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.PropertyValueGroupDao;
import com.baozun.nebula.dao.product.SearchConditionDao;
import com.baozun.nebula.dao.product.SearchConditionItemDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.extend.ItemExtendManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.manager.system.UploadManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemImageLang;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemOperateLog;
import com.baozun.nebula.model.product.ItemProValGroupRelation;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.ItemReference;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.model.product.SearchConditionItem;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.utils.file.CompressUtils;
import com.baozun.nebula.utils.file.FileUtils;
import com.baozun.nebula.utils.image.ImageOpeartion;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.excel.ExcelKit;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ExcelUtil;
import loxia.support.excel.ReadStatus;
import loxia.support.excel.definition.ExcelBlock;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.definition.ExcelSheet;

/**
 * @author yi.huang
 * @date 2013-7-1 下午04:11:23
 */
@Transactional
@Service("itemManager")
public class ItemManagerImpl implements ItemManager{

	private static final Logger			log											= LoggerFactory.getLogger(ItemManagerImpl.class);

	@Autowired
	private ItemDao						itemDao;

	@Autowired
	private ShopDao						shopDao;

	@Autowired
	private SkuDao						skuDao;

	@Autowired
	private CategoryDao					categoryDao;

	@Autowired
	private ItemCategoryDao				itemCategoryDao;

	@Autowired
	private ItemInfoDao					itemInfoDao;

	@Autowired
	private PropertyDao					propertyDao;

	@Autowired
	private PropertyValueDao			propertyValueDao;

	@Autowired
	private ItemPropertiesDao			itemPropertiesDao;

	@Autowired
	private ItemCategoryManager			itemCategoryManager;

	@Autowired
	private ItemTagRelationDao			itemTagRelationDao;

	@Autowired
	private ItemImageDao				itemImageDao;

	@Autowired
	private PropertyManager				propertyManager;

	@Autowired
	private ItemSolrManager				itemSolrManager;

	@Autowired
	private ItemReferenceDao			itemReferenceDao;

	@Autowired
	private ShopManager					shopManager;

	@Autowired
	private IndustryManager				industryManager;

	@Autowired
	private SearchConditionItemDao		searchConditionItemDao;

	@Autowired
	private UploadManager				uploadManager;

	@Autowired
	private CategoryManager				categoryManager;

	@Autowired
	private ChooseOptionManager			chooseOptionManager;

	@Autowired
	private ExcelManipulatorFactory		excelFactory;

	@Autowired
	private SdkMataInfoManager			sdkMataInfoManager;

	@Autowired
	private ItemImageLangDao			itemImageLangDao;

	@Autowired
	private ItemProValGroupRelationDao	itemProValGroupRelationDao;

	@Autowired(required = false)
	private ItemExtendManager			itemExtendManager;
	
	@Autowired
	private ItemOperateLogDao itemOperateLogDao;

	private ByteArrayOutputStream		byteArrayOutputStream						= null;

	private static final String			DEFAULT_PATH								= "excel";

	private static final Integer		HSSFSHEET_1									= 0;

	private static final Integer		HSSFSHEET_2									= 1;

	private static final Integer		TITLE_ROW_INDEX								= 5;

	private static final Integer		DESC_ROW_INDEX								= 6;

	/** 商品图片类型 */
	private static final String			ITEM_IMG_TYPE								= "IMAGE_TYPE";

	/** 商品图片尺寸 */
	private static final String			ITEM_IMG_ROLE								= "THUMBNAIL_CONFIG";

	/**
	 * 第一个sheet是从8行 第12列开始（不包含） 属性Id从第5行开始
	 */
	private static final Integer		ITEMCOMM_SHEETDEFINITION_STARTCOL			= 12;

	private static final Integer		ITEMCOMM_SHEETDEFINITION_STARTROW			= 7;

	private static final Integer		ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW	= 4;

	private static final Integer		ZERO										= 0;

	/**
	 * 第一个sheet是从8行 第2列开始（不包含） 属性Id从第5行开始
	 */
	private static final Integer		SKUCOMM_SHEETDEFINITION_STARTCOL			= 1;

	private static final Integer		SKUCOMM_SHEETDEFINITION_STARTROW			= 7;

	private static final Integer		SKUCOMM_SHEETDEFINITION_PROPID_STARTROW		= 4;

	private static final String			SKUCOMM_SC_DES								= "筛选条件名称";

	private static final String			SKUCOMM_SC_REQ								= "字符串(非必填)";

	// 用于属性多个
	private static final String			DOUBLE_SLASH_SEPARATOR						= "\\|\\|";

	// 分类 筛选条件 如果有||首先以||分割，如果没有以/分割
	private static final String			BACK_SLANT_SEPARATOR						= "/";

	/** windows, mac系统查看缩略图所产生的缓存文件, 在导入商品图片是过滤掉这个文件 **/
	private final static String			CACHE_FILE_NAME								= "Thumbs.db";

	/** 上传文件的类型:1, 增量导入; 0, 全量导入 */
	private static final String			UPLOAD_TYPE									= "0";

	/** 属性列表值最大长度 */
	public static final int				MAX_PROP_VALUE								= 15;

	public static final String			VALUES_SHEET								= "Sheet3";

	@Autowired
	private SdkI18nLangManager			sdkI18nLangManager;

	@Autowired
	private ItemLangManager				itemLangManager;

	@Autowired
	private ItemPropertiesLangDao		itemPropertiesLangDao;

	@Autowired
	private PropertyValueGroupDao		propertyValueGroupDao;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findItemListByItemIds(java .lang.Long[])
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Item> findItemListByItemIds(Long[] itemIds){
		List<Long> itemId = new ArrayList<Long>();
		for (Long id : itemIds){
			itemId.add(id);
		}
		return itemDao.findItemListByIds(itemId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findDynamicPropertis(Long shopId,Long industryId)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DynamicPropertyCommand> findDynamicPropertis(Long shopId,Long industryId){
//		List<Property> propertyList = shopDao.findPropertyListByIndustryIdAndShopId(industryId, shopId, null);
		List<Property> propertyList = propertyDao.findPropertysByIndustryId(industryId);
		
		List<DynamicPropertyCommand> dynamicPropertyCommandList = new ArrayList<DynamicPropertyCommand>();
		List<PropertyValue> propertyValueList;
		Long propertyId;
		for (Property property : propertyList){
			propertyId = property.getId();
			propertyValueList = propertyValueDao.findPropertyValueListById(propertyId);

			DynamicPropertyCommand dynamicPropertyCommand = new DynamicPropertyCommand();
			dynamicPropertyCommand.setProperty(property);
			dynamicPropertyCommand.setPropertyValueList(propertyValueList);

			List<PropertyValueGroup> propertyValueGroupList = propertyValueGroupDao.findByPropertyId(propertyId);
			dynamicPropertyCommand.setPropertyValueGroupList(propertyValueGroupList);

			dynamicPropertyCommandList.add(dynamicPropertyCommand);
		}
		return dynamicPropertyCommandList;

	}

	@Override
	@Transactional(readOnly = true)
	public List<DynamicPropertyCommand> findDynamicPropertisByIndustryId(Long industryId){
		List<Property> propertyList = shopDao.findPropertyListByIndustryId(industryId, null);
		List<DynamicPropertyCommand> dynamicPropertyCommandList = new ArrayList<DynamicPropertyCommand>();
		List<PropertyValue> propertyValueList;
		Long propertyId;
		for (Property property : propertyList){
			propertyId = property.getId();
			propertyValueList = propertyValueDao.findPropertyValueListById(propertyId);

			DynamicPropertyCommand dynamicPropertyCommand = new DynamicPropertyCommand();
			dynamicPropertyCommand.setProperty(property);
			dynamicPropertyCommand.setPropertyValueList(propertyValueList);

			List<PropertyValueGroup> propertyValueGroupList = propertyValueGroupDao.findByPropertyId(propertyId);
			dynamicPropertyCommand.setPropertyValueGroupList(propertyValueGroupList);

			dynamicPropertyCommandList.add(dynamicPropertyCommand);
		}
		return dynamicPropertyCommandList;

	}

	@Override
	public Item createOrUpdateItem(ItemCommand itemCommand,Long[] propertyValueIds, // 动态属性
			Long[] categoriesIds,// 商品分类Id
			ItemProperties[] iProperties,// 普通商品属性
			SkuPropertyCommand[] skuPropertyCommand// sku 的信息，包含每个sku对应的价格
	) throws Exception{

		// 分类校验
		// categoriesIds not empty
		// 1. categoriesIds包含defaultCategoryId
		// 2.defaultCategoryId不为空
		// 3.defaultCategoryId对应分类存在
		if (Validator.isNotNullOrEmpty(categoriesIds)){

			if (Validator.isNotNullOrEmpty(itemCommand.getDefCategroyId())){
				List<Long> tempCateList = Arrays.asList(categoriesIds);
				if (tempCateList.contains(itemCommand.getDefCategroyId())){
					Category tempCategory = categoryDao.findEnableCategoryById(itemCommand.getDefCategroyId());
					if (tempCategory == null){
						throw new BusinessException(ErrorCodes.ITEM_UPDATE_DEFCATE_NOT_EXISTS);
					}
				}else{
					throw new BusinessException(ErrorCodes.ITEM_UPDATE_CATE_NOT_CONTAIN_DEF);
				}
			}else{
				throw new BusinessException(ErrorCodes.ITEM_UPDATE_DEFCATE_NOT_EXISTS);
			}
		}

		// boolean isUpdateSolr =false;

		Item item = null;
		if (itemCommand.getId() != null){// 更新
			item = itemDao.getByPrimaryKey(itemCommand.getId());
			item.setModifyTime(new Date());
			item.setCode(itemCommand.getCode());
			if (categoriesIds != null && categoriesIds.length > 0){
				item.setIsaddcategory(1);
			}else{
				item.setIsaddcategory(0);
			}
			item = itemDao.save(item);
			/*
			 * if(item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){ isUpdateSolr =true; }
			 */
		}else{// 新增

			if (itemCommand.getId() == null){
				Integer count = validateItemCode(itemCommand.getCode(), itemCommand.getShopId());

				if (count > 0){
					throw new BusinessException(ErrorCodes.PRODUCT_CODE_REPEAT);
				}
			}
			item = new Item();
			item.setCode(itemCommand.getCode());
			// Lifecycle状态： 0：无效 1：有效 2：删除 3：未激活
			item.setLifecycle(Item.LIFECYCLE_UNACTIVE);
			item.setCreateTime(new Date());
			item.setShopId(itemCommand.getShopId());
			item.setIndustryId(Long.valueOf(itemCommand.getIndustryId()));

			if (categoriesIds != null && categoriesIds.length > 0){
				item.setIsaddcategory(1);
			}else{
				item.setIsaddcategory(0);
			}
			item.setIsAddTag(0);
			item = itemDao.save(item);
		}

		// 商品所有的属性值集合
		List<ItemProperties> savedItemProperties = this.createOrUpdateItemProperties(
				itemCommand,
				propertyValueIds,
				iProperties,
				item.getId(),
				skuPropertyCommand);

		// 保存Sku
		this.createOrUpdateSku(itemCommand, item.getId(), skuPropertyCommand, savedItemProperties);

		// 保存商品信息
		this.createOrUpdateItemInfo(itemCommand, item.getId());

		if (categoriesIds != null && categoriesIds.length > 0){
			// Long defaultId = itemCategoryManager.getDefaultItemCategoryId(categoriesIds);
			Long defaultId = itemCommand.getDefCategroyId();

			Long[] categoryIdArray = new Long[categoriesIds.length - 1];
			int index = 0;
			for (Long id : categoriesIds){
				if (!defaultId.equals(id)){
					categoryIdArray[index] = id;
					index++;
				}
			}

			// 绑定附加分类
			itemCategoryManager.createOrUpdateItemCategory(itemCommand, item.getId(), categoryIdArray);

			// 绑定默认分类
			itemCategoryManager.createOrUpdateItemDefaultCategory(itemCommand, item.getId(), defaultId);

		}else{
			List<ItemCategory> ctgList = itemCategoryManager.findItemCategoryListByItemId(item.getId());
			Long[] itemIds = new Long[1];
			itemIds[0] = item.getId();
			for (ItemCategory ic : ctgList){
				List<Long> itemIdList = new ArrayList<Long>();
				itemIdList.add(item.getId());
				itemCategoryManager.unBindItemCategory(itemIds, ic.getCategoryId());

			}
		}
		// 执行扩展点
		if (null != itemExtendManager){
			itemExtendManager.extendAfterCreateOrUpdateItem(item, itemCommand, categoriesIds, savedItemProperties, skuPropertyCommand);
		}
		/*
		 * //刷新索引 if(isUpdateSolr){ List<Long> itemIdsForSolr =new ArrayList<Long>(); itemIdsForSolr.add(item.getId());
		 * itemSolrManager.saveOrUpdateItem(itemIdsForSolr); }
		 */
		return item;
	}

	// 得到默认分类，目前是最小的
	private Long getDefaultCategoryId(Long[] categoriesIds){
		Arrays.sort(categoriesIds);

		return categoriesIds[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#createOrUpdateItem(ItemCommand itemCommand,Long shopId,Long[] categoriesIds,Long[]
	 * propertyValueIds,String[] codes,ItemProperties[] iProperties)
	 */
	@Override
	public Item createOrUpdateItem(
			ItemCommand itemCommand,
			Long shopId,
			Long[] categoriesIds,
			Long[] propertyValueIds,
			String[] codes,
			BigDecimal[] salePrices,
			BigDecimal[] listPrices,
			ItemProperties[] iProperties,
			String changePropertyJson,
			Long defaultCategoryId) throws Exception{
		Item item = null;
		if (itemCommand.getId() != null){
			item = itemDao.getByPrimaryKey(itemCommand.getId());
			item.setModifyTime(new Date());
			item.setCode(itemCommand.getCode());
			if (categoriesIds != null && categoriesIds.length > 0){
				item.setIsaddcategory(1);
			}else{
				item.setIsaddcategory(0);
			}
			item = itemDao.save(item);
		}else{
			item = new Item();
			item.setCode(itemCommand.getCode());
			// Lifecycle状态： 0：无效 1：有效 2：删除 3：未激活
			item.setLifecycle(Item.LIFECYCLE_UNACTIVE);
			item.setCreateTime(new Date());
			item.setShopId(shopId);
			item.setIndustryId(Long.valueOf(itemCommand.getIndustryId()));
			if (categoriesIds != null && categoriesIds.length > 0){
				item.setIsaddcategory(1);
			}else{
				item.setIsaddcategory(0);
			}
			item.setIsAddTag(0);
			item = itemDao.save(item);
		}

		// 保存商品属性值
		List<ItemProperties> savedItemProperties = this.createOrUpdateItemProperties(
				itemCommand,
				propertyValueIds,
				item.getId(),
				iProperties);

		// 保存Sku
		this.createOrUpdateSku(itemCommand, item.getId(), codes, salePrices, listPrices, savedItemProperties);

		// 保存商品信息
		this.createOrUpdateItemInfo(itemCommand, item.getId());

		// 绑定附加分类
		int result = itemCategoryManager.createOrUpdateItemCategory(itemCommand, item.getId(), categoriesIds);
		// 绑定默认分类
		int data = itemCategoryManager.createOrUpdateItemDefaultCategory(itemCommand, item.getId(), defaultCategoryId);

		// 保存商品图片
		// if(Validator.isNotNullOrEmpty(changePropertyJson)){
		// this.createOrUpdateItemImage(changePropertyJson, item.getId(),
		// savedItemProperties );
		// }

		return item;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#validateItemCode(String code,Long shopId)
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer validateItemCode(String code,Long shopId){
		return itemDao.validateItemCode(code, shopId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findItemById(Long id)
	 */
	@Override
	@Transactional(readOnly = true)
	public Item findItemById(Long id){
		Item item = itemDao.findItemById(id);
		return item;
	}

	public Integer updateItemIsAddCategory(List<Long> ids,Integer state){
		return itemDao.updateItemIsAddCategory(ids, state);
	}

	public Integer updateItemIsAddTag(List<Long> ids,Integer state){
		return itemDao.updateItemIsAddTag(ids, state);
	}

	// 获取店铺所有的商品信息
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemListByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){

		Pagination<ItemCommand> ItemList = itemDao.findItemListByQueryMap(page, sorts, paraMap, shopId);

		// 分类
		List<ItemCategoryCommand> itemCategory = itemCategoryDao.findAllItemCategoryList();

		// 标签
		List<ItemTagRelationCommand> itemTagList = itemTagRelationDao.findAllItemTagRelationList();

		List<ItemCommand> items = ItemList.getItems();

		List<String> categoryNameList = null;

		List<String> categoryCodeList = null;

		List<String> tagNameList = null;

		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < items.size(); i++){
			categoryNameList = new ArrayList<String>();
			categoryCodeList = new ArrayList<String>();
			tagNameList = new ArrayList<String>();
			for (int j = 0; j < itemCategory.size(); j++){
				if (items.get(i).getId().equals(itemCategory.get(j).getItemId())){
					categoryNameList.add(itemCategory.get(j).getCategoryName());
					categoryCodeList.add(itemCategory.get(j).getCategoryCode());
				}
			}

			for (int j = 0; j < itemTagList.size(); j++){
				if (items.get(i).getId().equals(itemTagList.get(j).getItemId())){
					tagNameList.add(itemTagList.get(j).getTagName());
				}
			}
			items.get(i).setTagNames(tagNameList);

			items.get(i).setCategoryNames(categoryNameList);

			items.get(i).setCategoryCodes(categoryCodeList);

			itemIds.add(items.get(i).getId());

		}

		// 默认分类设置

		List<ItemCategoryCommand> itemCategoryList = itemCategoryDao.findDefaultCategoryByItemIds(itemIds);

		Map<Long, String> itemCategoryMap = new HashMap<Long, String>();
		for (ItemCategoryCommand itemCategoryCommand : itemCategoryList){
			itemCategoryMap.put(itemCategoryCommand.getItemId(), itemCategoryCommand.getCategoryName());
		}
		for (int i = 0; i < items.size(); i++){

			items.get(i).setDefCategory(itemCategoryMap.get(items.get(i).getId()));
		}

		ItemList.setItems(items);

		return ItemList;
	}

	@Override
	public Pagination<ItemCommand> findEffectItemInfoListByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<ItemCommand> itemInfoPage = itemDao.findEffectItemInfoListByQueryMap(page, sorts, paraMap);
		return itemInfoPage;
	}

	@Override
	public Integer enableOrDisableItemByIds(List<Long> ids,Integer state,String userName){
		String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);
		Integer result = itemDao.enableOrDisableItemByIds(ids, state, updateListTimeFlag);
		//添加上下架日志
		addItemOperateLog(ids,state,userName);
		boolean solrFlag = false;
		// state : 0==下架， 1=上架
		if (state == 1){
			// 刷新索引
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				solrFlag = itemSolrManager.saveOrUpdateItemI18n(ids);
			}else{
				solrFlag = itemSolrManager.saveOrUpdateItem(ids);
			}
		}else{
			// 删除索引
			solrFlag = itemSolrManager.deleteItem(ids);
		}

		if (!solrFlag){
			throw new BusinessException(ErrorCodes.SOLR_REFRESH_ERROR);
		}

		return result;
	}
	
	/**
	 * 添加上下架日志
	 * state : 0==下架， 1=上架
	 * @param itemIds
	 * @param state
	 * @param userName
	 */
	private void addItemOperateLog(List<Long> itemIds,Integer state,String userName){
		for(Long itemId : itemIds){
			//按createTime来排序,取最新的一条记录
			Long lastLogId = itemOperateLogDao.findByItemId(itemId);
			
			if (Validator.isNotNullOrEmpty(lastLogId)){
				ItemOperateLog itemOperateLog = itemOperateLogDao.getByPrimaryKey(lastLogId);
				if( Validator.isNullOrEmpty(itemOperateLog.getPushTime()) &&  state == 1 ){//上架
					itemOperateLog.setPushOperatorName(userName);
					itemOperateLog.setPushTime(new Date());
					itemOperateLog.setActiveTime(countActiveTime(new Date(),itemOperateLog.getSoldOutTime()));
					itemOperateLogDao.save(itemOperateLog);
				}else if( Validator.isNullOrEmpty(itemOperateLog.getSoldOutTime()) &&  state == 0 ){//下架
					itemOperateLog.setSoldOutOperatorName(userName);
					itemOperateLog.setSoldOutTime(new Date());
					itemOperateLog.setActiveTime(countActiveTime(new Date(),itemOperateLog.getSoldOutTime()));
					itemOperateLogDao.save(itemOperateLog);
				}else{
					saveItemOperateLog(itemId,state,userName);
				}
			}else{
				saveItemOperateLog(itemId,state,userName);
			}
			
			
		}
	}
	
	/**
	 * 计算时间差（精确到秒）
	 * @param pushTime
	 * @param soldOutTime
	 * @return
	 */
	private Long countActiveTime(Date pushTime,Date soldOutTime){
		return Math.abs((soldOutTime.getTime() - pushTime.getTime()))/1000;
	}

	/**
	 * 保存ItemOperateLog
	 * @param itemId
	 * @param state
	 * @param userName
	 */
	private void saveItemOperateLog(Long itemId,Integer state,String userName){
		ItemOperateLog itemOperateLog = new ItemOperateLog();
		itemOperateLog.setItemId(itemId);
		itemOperateLog.setCreateTime(new Date());
		itemOperateLog.setUpdateTime(new Date());
		if(state == 1){//上架
			itemOperateLog.setPushOperatorName(userName);
			itemOperateLog.setPushTime(new Date());
		}else if(state == 0){//下架
			itemOperateLog.setSoldOutOperatorName(userName);
			itemOperateLog.setSoldOutTime(new Date());
		}
		itemOperateLogDao.save(itemOperateLog);
	}
	
	@Override
	public void removeItemByIds(List<Long> ids){
		// 下架的商品是不存在solr中, 如果商品是上架商品,才删除solr中的索引
		List<Item> itemList = itemDao.findItemListByIds(ids);
		for (Item item : itemList){
			Integer lifecycle = item.getLifecycle();
			if (Item.LIFECYCLE_ENABLE.equals(lifecycle)){
				// 删除索引
				boolean solrFlag = itemSolrManager.deleteItem(ids);
				if (!solrFlag){
					throw new BusinessException(ErrorCodes.SOLR_REFRESH_ERROR);
				}
			}
		}

		// 修改item表中的lifecycle=2
		itemDao.removeItemByIds(ids);
		// 修改sku表中的lifecycle = 0
		skuDao.removeSkuByItemIds(ids);

	}

	private List<Sku> createOrUpdateSku(
			ItemCommand itemCommand,
			Long itemId,
			SkuPropertyCommand[] skuPropertyCommandArray,
			List<ItemProperties> savedItemProperties){

		List<Sku> returnList = new ArrayList<Sku>();
		if (itemCommand.getId() != null){
			// int delcount =skuDao.deleteSkuByItemId(itemCommand.getId());
			// returnList = saveSkus(itemCommand, itemId,
			// skuPropertyCommandArray, savedItemProperties);

			/**
			 * 提交到server的时候，根据传过来的 skuId 和 库里已经有的列表进行比对 如果新增，那么新增的skuId 为null。 根据传过来的 PropertyId 和 propertyValue 拿到已经保存的ItemProperties.
			 * 新增Sku 如果是修改，就是根据skuId 修改。此时不涉及 itemProperties 的修改。 只会修改skuCode， 销售价，吊牌价 如果库里列表中的skuId在提交过来的SkuId 不存在，那么就删除
			 */
			List<Sku> skuListInDb = skuDao.findSkuByItemId(itemId);

			for (SkuPropertyCommand spc : skuPropertyCommandArray){
				Long skuId = spc.getId();
				String extentionCode = spc.getCode();

				/*
				 * 检察sku表中是否存在, itemId, out_id, item_properties与提交的都一致,且lifecycle=0的信息 1, 存在: 修改lifecycle=1 2, 不存在: 新增一条记录
				 */
				String skuItemProperties = getSkuItemProperties(spc, savedItemProperties);
				Map<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put("itemId", itemId);
				paraMap.put("itemProperties", skuItemProperties);
				paraMap.put("outId", extentionCode);
				paraMap.put("lifecycle", Sku.LIFECYCLE_DISABLE);
				List<Sku> savedSkuList = skuDao.findSkuWithParaMap(paraMap);

				if (null != savedSkuList && !savedSkuList.isEmpty()){
					for (Sku sku : savedSkuList){
						sku.setLifecycle(Sku.LIFECYCLE_ENABLE);
						skuDao.save(sku);
						break;
					}
				}else if (skuId == null){// 新增
					Sku skunew = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
					returnList.add(skunew);
				}else{// 修改

					/* 如果修改了extention_code , 将该sku的lifecycle设置为0, 并新增一条数据 */
					Sku dbSku = skuDao.findSkuById(skuId);
					Sku savedSku = null;
					if (extentionCode.equals(dbSku.getOutid())){
						Sku skuToBeUpdate = skuDao.getByPrimaryKey(skuId);
						skuToBeUpdate.setListPrice(spc.getListPrice());
						skuToBeUpdate.setOutid(spc.getCode());
						skuToBeUpdate.setSalePrice(spc.getSalePrice());
						savedSku = skuDao.save(skuToBeUpdate);
					}else{
						dbSku.setLifecycle(Sku.LIFECYCLE_DISABLE);
						skuDao.save(dbSku);

						savedSku = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
					}

					returnList.add(savedSku);
				}

			}

			// 获得要删除的skuId集合
			List<Long> skuToBeDelList = new ArrayList<Long>();
			for (Sku skuInDb : skuListInDb){
				boolean delFlag = true;
				for (Sku curSku : returnList){
					if (skuInDb.getId().equals(curSku.getId())){
						delFlag = false;
						break;
					}
				}

				if (delFlag){
					skuToBeDelList.add(skuInDb.getId());
				}
			}

			// 删除
			skuDao.deleteSkuBySkuIds(skuToBeDelList);

			// List<Sku> alreadyExistSkuList = skuDao.findSkuByItemId(itemId);
			// boolean delFlag = false;
			// if(alreadyExistSkuList.size()!=skuPropertyCommandArray.length){
			// //如果传过来的sku 和 数据库里的数目不相等，说明sku项变化，db的删掉
			// delFlag = true;
			// }else{
			// if(skuPropertyCommandArray!=null&&skuPropertyCommandArray.length>0){
			// // 如果skuPropertyCommandArray中的skuId
			// 在skuPropertyCommandArray中不存在，删掉重新建
			// for(SkuPropertyCommand cmd :skuPropertyCommandArray){
			// if(cmd.getId()!=null){
			// boolean existFlag = false;
			// for(Sku existSku :alreadyExistSkuList){
			// if(cmd.getId().equals(existSku.getId())){
			// existFlag = true;
			// }
			// }
			//
			// if(!existFlag){
			// delFlag = true;
			// break;
			// }
			// }
			// }
			// }
			// }
			//
			// //先删后加
			// if(delFlag){
			// int delcount =skuDao.deleteSkuByItemId(itemCommand.getId());
			// returnList = saveSkus(itemCommand, itemId,
			// skuPropertyCommandArray, savedItemProperties);
			// }else{
			//
			// //更新数据库中sku的值
			// for(Sku sku:alreadyExistSkuList){
			// for(SkuPropertyCommand spc : skuPropertyCommandArray){
			// sku.setItemId(itemId);
			// sku.setCreateTime(new Date());
			// sku.setOutid(spc.getCode());
			// sku.setSalePrice(spc.getSalePrice());
			// sku.setListPrice(spc.getListPrice());
			// sku.setLifecycle(1);
			//
			// List<ItemPropertyCommand> ipCmdList = spc.getPropertyList();
			//
			// List<Long> ipIds = new ArrayList<Long>();
			// if(ipCmdList!=null){
			//
			// for(ItemPropertyCommand ipCmd : ipCmdList){
			// for(ItemProperties ip :savedItemProperties){
			//
			// if(ipCmd.getId()!=null&&ip.getPropertyValueId()!=null){//如果是多选
			// if(ipCmd.getId().equals(ip.getPropertyValueId().toString())){
			// ipIds.add(ip.getId());
			// }
			//
			// }else{//如果是自定义多选
			// if(ipCmd.getValue().equals(ip.getPropertyValue())){
			// ipIds.add(ip.getId());
			// }
			// }
			// }
			// }
			//
			// }
			//
			// Gson sg = new Gson();
			// String ipIdStr = sg.toJson(ipIds);
			// sku.setProperties(ipIdStr);
			// sku.setPropertiesName(null);
			// Sku skunew=skuDao.save(sku);
			// returnList.add(skunew);
			// }
			//
			// }
			// }

		}else{
			returnList = saveSkus(itemCommand, itemId, skuPropertyCommandArray, savedItemProperties);
		}

		return returnList;
	}

	/**
	 * 获得sku中的itemProperties的信息
	 * 
	 * @param spc
	 * @param savedItemProperties
	 * @return
	 */
	private String getSkuItemProperties(SkuPropertyCommand spc,List<ItemProperties> savedItemProperties){
		List<ItemPropertyCommand> ipCmdList = spc.getPropertyList();
		List<Long> ipIds = new ArrayList<Long>();
		if (ipCmdList != null){
			for (ItemPropertyCommand ipCmd : ipCmdList){
				for (ItemProperties ip : savedItemProperties){

					if (ip.getPropertyId().equals(ipCmd.getpId())){
						if (ipCmd.getId() != null && ip.getPropertyValueId() != null){// 如果是多选
							if ((ipCmd.getId().equals(ip.getPropertyValueId().toString())) && (ipCmd.getpId().equals(ip.getPropertyId()))){
								ipIds.add(ip.getId());
							}

						}else{// 如果是自定义多选
							if (ipCmd.getValue() != null){
								if (ipCmd.getValue().equals(ip.getPropertyValue()) && ip.getPropertyId().equals(ipCmd.getpId())){
									ipIds.add(ip.getId());
								}
							}

						}
					}

				}
			}
		}
		Gson sg = new Gson();

		// 按照升序来排列
		Collections.sort(ipIds);
		String ipIdStr = sg.toJson(ipIds);
		return ipIdStr;
	}

	/**
	 * 保存sku
	 * 
	 * @param skuItemProperties
	 * @param spc
	 * @param itemId
	 * @return
	 */
	private Sku createSkuBySkuPropertyCommand(String skuItemProperties,SkuPropertyCommand spc,Long itemId){
		Sku sku = new Sku();
		sku.setItemId(itemId);
		sku.setCreateTime(new Date());
		sku.setOutid(spc.getCode());
		sku.setSalePrice(spc.getSalePrice());
		sku.setListPrice(spc.getListPrice());
		sku.setLifecycle(Sku.LIFECYCLE_ENABLE);
		sku.setProperties(skuItemProperties);
		sku.setPropertiesName(null);
		Sku skunew = skuDao.save(sku);
		return skunew;
	}

	/**
	 * 保存skus
	 * 
	 * @param itemCommand
	 * @param itemId
	 * @param skuPropertyCommandArray
	 * @param savedItemProperties
	 * @return
	 */
	private List<Sku> saveSkus(
			ItemCommand itemCommand,
			Long itemId,
			SkuPropertyCommand[] skuPropertyCommandArray,
			List<ItemProperties> savedItemProperties){
		List<Sku> returnList = new ArrayList<Sku>();
		if (skuPropertyCommandArray != null && skuPropertyCommandArray.length > 0){
			for (SkuPropertyCommand spc : skuPropertyCommandArray){
				String skuItemProperties = getSkuItemProperties(spc, savedItemProperties);
				Sku skunew = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
				returnList.add(skunew);
			}
		}

		return returnList;
	}

	/**
	 * 保存 修改sku
	 * 
	 * @param itemCommand
	 * @param itemId
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	private List<Sku> createOrUpdateSku(
			ItemCommand itemCommand,
			Long itemId,
			String[] codes,
			BigDecimal[] salePrices,
			BigDecimal[] listPrices,
			List<ItemProperties> savedItemProperties) throws Exception{

		List<Sku> skuList = null;
		List<Sku> returnList = new ArrayList<Sku>();
		String str = itemCommand.getJsonSku().replaceAll("\'", "\"");

		ObjectMapper om = new ObjectMapper();
		TypeReference<List<Sku>> valueTypeRef = new TypeReference<List<Sku>>(){};

		if (!"".equals(str)){
			skuList = om.readValue(str, valueTypeRef);
		}

		if (itemCommand.getId() != null){
			// 先删后加
			int delcount = skuDao.deleteSkuByItemId(itemCommand.getId());
			int j = 0;
			if (skuList != null && skuList.size() > 0){
				for (Sku sku : skuList){
					sku.setItemId(itemId);
					sku.setCreateTime(new Date());
					sku.setOutid(codes[j]);
					sku.setSalePrice(salePrices[j]);
					sku.setListPrice(listPrices[j]);
					sku.setLifecycle(1);
					sku.setProperties(sku.getProperties().replace("/", "\""));
					// sku.setPropertiesName(sku.getPropertiesName().replace("/",
					// "\""));
					String propertiesName = sku.getPropertiesName().replace("/", "\"");

					TypeReference<List<ItemPropertyCommand>> cmdRef = new TypeReference<List<ItemPropertyCommand>>(){};

					List<ItemPropertyCommand> ipCmdList = null;
					if (!"".equals(propertiesName)){
						ipCmdList = om.readValue(propertiesName, cmdRef);
					}
					List<Long> ipIds = new ArrayList<Long>();
					if (ipCmdList != null){

						for (ItemPropertyCommand ipCmd : ipCmdList){
							for (ItemProperties ip : savedItemProperties){

								if (ipCmd.getId() != null && ip.getPropertyValueId() != null){// 如果是多选
									if (ipCmd.getId().equals(ip.getPropertyValueId().toString())){
										ipIds.add(ip.getId());
									}

								}else{// 如果是自定义多选
									if (ipCmd.getValue().equals(ip.getPropertyValue())){
										ipIds.add(ip.getId());
									}
								}
							}
						}

					}

					Gson sg = new Gson();
					String ipIdStr = sg.toJson(ipIds);
					sku.setProperties(ipIdStr);
					sku.setPropertiesName(null);
					Sku skunew = skuDao.save(sku);
					returnList.add(skunew);

					j++;
				}
			}

		}else{
			int j = 0;
			if (skuList != null && skuList.size() > 0){
				for (Sku sku : skuList){
					sku.setItemId(itemId);
					sku.setCreateTime(new Date());
					sku.setOutid(codes[j]);
					sku.setSalePrice(salePrices[j]);
					sku.setListPrice(listPrices[j]);
					sku.setLifecycle(1);
					sku.setProperties(sku.getProperties().replace("/", "\""));
					// sku.setPropertiesName(sku.getPropertiesName().replace("/",
					// "\""));
					String propertiesName = sku.getPropertiesName().replace("/", "\"");

					TypeReference<List<ItemPropertyCommand>> cmdRef = new TypeReference<List<ItemPropertyCommand>>(){};

					List<ItemPropertyCommand> ipCmdList = null;
					if (!"".equals(propertiesName)){
						ipCmdList = om.readValue(propertiesName, cmdRef);
					}
					List<Long> ipIds = new ArrayList<Long>();
					if (ipCmdList != null){

						for (ItemPropertyCommand ipCmd : ipCmdList){
							for (ItemProperties ip : savedItemProperties){

								if (ipCmd.getId() != null && ip.getPropertyValueId() != null){// 如果是多选
									if (ipCmd.getId().equals(ip.getPropertyValueId().toString())){
										ipIds.add(ip.getId());
									}

								}else{// 如果是自定义多选
									if (ipCmd.getValue().equals(ip.getPropertyValue())){
										ipIds.add(ip.getId());
									}
								}
							}
						}

					}

					Gson sg = new Gson();
					String ipIdStr = sg.toJson(ipIds);
					sku.setProperties(ipIdStr);
					sku.setPropertiesName(null);
					Sku skunew = skuDao.save(sku);
					returnList.add(skunew);

					j++;
				}
			}
		}

		return returnList;
	}

	/**
	 * 保存商品信息
	 * 
	 * @param itemCommand
	 * @param itemId
	 * @return
	 */
	private ItemInfo createOrUpdateItemInfo(ItemCommand itemCommand,Long itemId){
		ItemInfo itemInfo = null;
		if (itemCommand.getId() != null){
			itemInfo = itemInfoDao.findItemInfoByItemId(itemCommand.getId());
			itemInfo = itemInfoDao.getByPrimaryKey(itemInfo.getId());
			itemInfo.setModifyTime(new Date());
			itemInfo.setTitle(itemCommand.getTitle());
			itemInfo.setSubTitle(itemCommand.getSubTitle());
			itemInfo.setDescription(itemCommand.getDescription());
			itemInfo.setSalePrice(itemCommand.getSalePrice());
			itemInfo.setListPrice(itemCommand.getListPrice());
			itemInfo.setSketch(itemCommand.getSketch());
			itemInfo.setSeoDescription(itemCommand.getSeoDescription());
			itemInfo.setSeoKeywords(itemCommand.getSeoKeywords());
			itemInfo.setSeoTitle(itemCommand.getSeoTitle());
			itemInfo.setStyle(itemCommand.getStyle());
			itemInfo.setType(itemCommand.getType());
			itemInfo = itemInfoDao.save(itemInfo);
		}else{
			itemInfo = new ItemInfo();
			itemInfo.setItemId(itemId);
			itemInfo.setTitle(itemCommand.getTitle());
			itemInfo.setSubTitle(itemCommand.getSubTitle());
			itemInfo.setDescription(itemCommand.getDescription());
			itemInfo.setCreateTime(new Date());
			itemInfo.setSalePrice(itemCommand.getSalePrice());
			itemInfo.setListPrice(itemCommand.getListPrice());
			itemInfo.setSketch(itemCommand.getSketch());
			itemInfo.setSeoDescription(itemCommand.getSeoDescription());
			itemInfo.setSeoKeywords(itemCommand.getSeoKeywords());
			itemInfo.setSeoTitle(itemCommand.getSeoTitle());
			itemInfo.setStyle(itemCommand.getStyle());
			itemInfo.setType(itemCommand.getType());
			itemInfo = itemInfoDao.save(itemInfo);
		}

		return itemInfo;
	}

	/**
	 * 保存商品属性
	 * 
	 * @param itemCommand
	 * @param propertyValueIds
	 * @param itemId
	 * @param iProperties
	 * @return
	 * @throws Exception
	 */
	private List<ItemProperties> createOrUpdateItemProperties(
			ItemCommand itemCommand,
			Long[] propertyValueIds,
			Long itemId,
			ItemProperties[] iProperties) throws Exception{
		List<ItemProperties> itemPropertyList = new ArrayList<ItemProperties>();

		List<Long> list = new ArrayList<Long>();
		for (Long pvs : propertyValueIds){
			list.add(pvs);
		}
		List<PropertyValue> propertyValueList = propertyValueDao.findPropertyValueListByIds(list);
		if (itemCommand.getId() != null){// 修改
			int delcount = itemPropertiesDao.deleteItemPropertiesByItemId(itemCommand.getId());

			// 先保存销售属性， 从sku字符串中拿到销售属性。 销售属性目前有两种 ，一种是 多选，另外一种是自定义多选

			List<Sku> skuList = null;
			String str = itemCommand.getJsonSku().replaceAll("\'", "\"");
			// str = str.replace("/", "");

			ObjectMapper om = new ObjectMapper();
			TypeReference<List<Sku>> valueTypeRef = new TypeReference<List<Sku>>(){};

			if (!"".equals(str)){
				skuList = om.readValue(str, valueTypeRef);
			}
			if (skuList != null && skuList.size() > 0){
				for (Sku sku : skuList){
					String propertiesName = sku.getPropertiesName().replace("/", "\"");

					TypeReference<List<ItemPropertyCommand>> cmdRef = new TypeReference<List<ItemPropertyCommand>>(){};

					List<ItemPropertyCommand> ipCmdList = null;
					if (!"".equals(propertiesName)){
						ipCmdList = om.readValue(propertiesName, cmdRef);
						for (ItemPropertyCommand ip : ipCmdList){

							if (ip.getId() != null && !ip.getId().equals("undefined")){ // 多选

								for (PropertyValue pv : propertyValueList){
									Long vid = Long.parseLong(ip.getId());
									if (vid.equals(pv.getId())){
										boolean existFlag = false;

										for (ItemProperties svItemProperties : itemPropertyList){
											if (vid.equals(svItemProperties.getPropertyValueId())){
												existFlag = true;
												break;
											}
										}

										if (!existFlag){
											ItemProperties itemProperties = new ItemProperties();
											itemProperties.setItemId(itemCommand.getId());
											itemProperties.setPropertyId(ip.getpId());
											itemProperties.setPropertyValueId(vid);
											itemProperties.setCreateTime(new Date());
											itemProperties = itemPropertiesDao.save(itemProperties);
											itemPropertyList.add(itemProperties);
										}

									}
								}

							}else{// 自定义多选
								String itemPropertyValue = ip.getValue();

								boolean existFlag = false;
								for (ItemProperties svItemProperties : itemPropertyList){
									if (itemPropertyValue.equals(svItemProperties.getPropertyValue())){
										existFlag = true;
										break;
									}
								}

								if (!existFlag){
									ItemProperties itemProperties = new ItemProperties();
									itemProperties.setItemId(itemCommand.getId());
									itemProperties.setPropertyId(ip.getpId());
									itemProperties.setPropertyValue(itemPropertyValue);
									itemProperties.setCreateTime(new Date());
									itemProperties = itemPropertiesDao.save(itemProperties);
									itemPropertyList.add(itemProperties);
								}

							}
						}
					}
				}

			}

			// 保存普通属性
			for (int j = 0; j < iProperties.length; j++){
				ItemProperties itemProperties = iProperties[j];
				ItemProperties itemProperties2 = null;
				String propertyValueStr = itemProperties.getPropertyValue();
				if (propertyValueStr.indexOf("||") != -1){
					String[] propertyValues = propertyValueStr.split("\\|\\|");
					if (propertyValues != null && propertyValues.length > 0){
						for (String propertyValue : propertyValues){
							itemProperties2 = new ItemProperties();
							itemProperties2.setPropertyValue(propertyValue);
							itemProperties2.setPropertyId(itemProperties.getPropertyId());
							itemProperties2.setItemId(itemCommand.getId());
							itemProperties2.setCreateTime(new Date());
							itemProperties2.setVersion(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties2);
							itemPropertyList.add(itemProperties);
						}
					}
				}else{
					itemProperties.setItemId(itemCommand.getId());
					itemProperties.setCreateTime(new Date());
					itemProperties = itemPropertiesDao.save(itemProperties);
					itemPropertyList.add(itemProperties);
				}
			}

			// for(int i = 0;i<propertyValueIds.length;i++) {
			// ItemProperties itemProperties = new ItemProperties();
			// for(PropertyValue pv:propertyValueList){
			// if(pv.getId().toString().equals(propertyValueIds[i].toString())){
			// itemProperties.setPicUrl(pv.getThumb());
			// itemProperties.setPropertyId(pv.getPropertyId());
			// }
			// }
			// itemProperties.setItemId(itemCommand.getId());
			// itemProperties.setPropertyValueId(propertyValueIds[i]);
			// itemProperties.setCreateTime(new Date());
			//
			// itemProperties =itemPropertiesDao.save(itemProperties);
			// itemPropertyList.add(itemProperties);
			//
			// }
			// for(int j=0;j<iProperties.length;j++){
			// ItemProperties itemProperties=iProperties[j];
			// itemProperties.setItemId(itemCommand.getId());
			// itemProperties.setCreateTime(new Date());
			// itemProperties=itemPropertiesDao.save(itemProperties);
			// itemPropertyList.add(itemProperties);
			// }
		}else{// 新增

			// 先保存销售属性， 从sku字符串中拿到销售属性。 销售属性目前有两种 ，一种是 多选，另外一种是自定义多选

			List<Sku> skuList = null;
			String str = itemCommand.getJsonSku().replaceAll("\'", "\"");

			ObjectMapper om = new ObjectMapper();
			TypeReference<List<Sku>> valueTypeRef = new TypeReference<List<Sku>>(){};

			if (!"".equals(str)){
				skuList = om.readValue(str, valueTypeRef);
			}
			if (skuList != null && skuList.size() > 0){
				for (Sku sku : skuList){
					String propertiesName = sku.getPropertiesName().replace("/", "\"");

					TypeReference<List<ItemPropertyCommand>> cmdRef = new TypeReference<List<ItemPropertyCommand>>(){};

					List<ItemPropertyCommand> ipCmdList = null;
					if (!"".equals(propertiesName)){
						ipCmdList = om.readValue(propertiesName, cmdRef);
						for (ItemPropertyCommand ip : ipCmdList){

							if (ip.getId() != null && !ip.getId().equals("undefined")){ // 多选

								for (PropertyValue pv : propertyValueList){
									Long vid = Long.parseLong(ip.getId());
									if (vid.equals(pv.getId())){
										boolean existFlag = false;

										for (ItemProperties svItemProperties : itemPropertyList){
											if (vid.equals(svItemProperties.getPropertyValueId())){
												existFlag = true;
												break;
											}
										}

										if (!existFlag){
											ItemProperties itemProperties = new ItemProperties();
											itemProperties.setItemId(itemId);
											itemProperties.setPropertyId(ip.getpId());
											itemProperties.setPropertyValueId(vid);
											itemProperties.setCreateTime(new Date());
											itemProperties = itemPropertiesDao.save(itemProperties);
											itemPropertyList.add(itemProperties);
										}

									}
								}

							}else{// 自定义多选
								String itemPropertyValue = ip.getValue();

								boolean existFlag = false;
								for (ItemProperties svItemProperties : itemPropertyList){
									if (itemPropertyValue.equals(svItemProperties.getPropertyValue())){
										existFlag = true;
										break;
									}
								}

								if (!existFlag){
									ItemProperties itemProperties = new ItemProperties();
									itemProperties.setItemId(itemId);
									itemProperties.setPropertyId(ip.getpId());
									itemProperties.setPropertyValue(itemPropertyValue);
									itemProperties.setCreateTime(new Date());
									itemProperties = itemPropertiesDao.save(itemProperties);
									itemPropertyList.add(itemProperties);
								}

							}
						}
					}
				}

			}

			// 保存销售属性
			// for(int i = 0;i<propertyValueIds.length;i++) {
			// ItemProperties itemProperties = new ItemProperties();
			// for(PropertyValue pv:propertyValueList){
			// if(pv.getId().toString().equals(propertyValueIds[i].toString())){
			// itemProperties.setPicUrl(pv.getThumb());
			// itemProperties.setPropertyId(pv.getPropertyId());
			// }
			// }
			// itemProperties.setItemId(itemId);
			// itemProperties.setPropertyValueId(propertyValueIds[i]);
			// itemProperties.setCreateTime(new Date());
			//
			// itemProperties =itemPropertiesDao.save(itemProperties);
			// itemPropertyList.add(itemProperties);
			//
			// }

			// 保存普通属性
			for (int j = 0; j < iProperties.length; j++){
				ItemProperties itemProperties = iProperties[j];
				ItemProperties itemProperties2 = null;
				String propertyValueStr = itemProperties.getPropertyValue();
				if (propertyValueStr.indexOf("#") != -1){
					String[] propertyValues = propertyValueStr.split("\\|\\|");
					if (propertyValues != null && propertyValues.length > 0){
						for (String propertyValue : propertyValues){
							itemProperties2 = new ItemProperties();
							itemProperties2.setPropertyValue(propertyValue);
							itemProperties2.setPropertyId(itemProperties.getPropertyId());
							itemProperties2.setItemId(itemCommand.getId());
							itemProperties2.setCreateTime(new Date());
							itemProperties2.setVersion(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties2);
							itemPropertyList.add(itemProperties);
						}
					}
				}else{
					itemProperties.setItemId(itemCommand.getId());
					itemProperties.setCreateTime(new Date());
					itemProperties = itemPropertiesDao.save(itemProperties);
					itemPropertyList.add(itemProperties);
				}
			}
		}

		return itemPropertyList;
	}

	/**
	 * 保存商品属性
	 * 
	 * @param itemCommand
	 * @param propertyValueIds
	 * @param iProperties
	 * @param itemId
	 * @param skuPropertyCommandArray
	 * @return
	 * @throws Exception
	 */
	private List<ItemProperties> createOrUpdateItemProperties(
			ItemCommand itemCommand,
			Long[] propertyValueIds,
			ItemProperties[] iProperties,
			Long itemId,
			SkuPropertyCommand[] skuPropertyCommandArray) throws Exception{
		List<ItemProperties> itemPropertyList = new ArrayList<ItemProperties>();

		List<Long> list = new ArrayList<Long>();
		for (Long pvs : propertyValueIds){
			list.add(pvs);
		}
		List<PropertyValue> propertyValueList = propertyValueDao.findPropertyValueListByIds(list);

		if (itemCommand.getId() != null){// 修改

			/**
			 * 销售属性 ItemProperties的修改 根据传过来的 propertyValueIds 和 propertyValueInputs 组合成 itemProperties (已经在 skuPropertyCommandArray 中)
			 * 在properties 表中查找 这个itemProperties 如果没有，就新增 。 对比删除 。不涉及修改
			 */
			List<ItemProperties> ipListInDb = itemPropertiesDao.findItemPropertiesByItemId(itemCommand.getId());

			// skuPropertyCommandArray 中的数据是否在 数据库中 。 循环结束后，
			// skuPropertyCommandArray对应的 itemProperties 都在
			// itemPropertyList中
			for (SkuPropertyCommand cmd : skuPropertyCommandArray){
				List<ItemPropertyCommand> ipcList = cmd.getPropertyList();
				for (ItemPropertyCommand ipc : ipcList){

					// 相等的条件： propertyId 相等， 并且 (pvId相等 或者pvValue相等)

					boolean existFlag = false;

					// 看表单传过来的值 是否在 数据库中。 如果存在，那么就放入 itemPropertyList
					for (ItemProperties ip : ipListInDb){

						if (ip.getPropertyValueId() != null){
							if (ip.getPropertyId().equals(ipc.getpId()) && (null != ipc.getId())
									&& ((ip.getPropertyValueId().equals(Long.parseLong(ipc.getId()))))){
								existFlag = true;

								// 此处添加的时候 要注意判断是否存在该属性 此处可能导致 大循环结束 属性值重复
								if (!isItemPrpertiesExistInList(ip, itemPropertyList)){
									itemPropertyList.add(ip);
								}
								break;
							}
						}

						if (ip.getPropertyValue() != null){
							if (ip.getPropertyId().equals(ipc.getpId()) && (ip.getPropertyValue().equals(ipc.getValue()))){
								existFlag = true;

								if (!isItemPrpertiesExistInList(ip, itemPropertyList)){
									itemPropertyList.add(ip);
								}
								break;
							}
						}

					}

					// 看表单传过来的值是否已经在 itemPropertyList 中了
					for (ItemProperties ip : itemPropertyList){

						if (ip.getPropertyValueId() != null){
							if (ip.getPropertyId().equals(ipc.getpId()) && ((ip.getPropertyValueId().equals(Long.parseLong(ipc.getId()))))){
								existFlag = true;
								// itemPropertyList.add(ip);
								break;
							}
						}

						if (ip.getPropertyValue() != null){
							if (ip.getPropertyId().equals(ipc.getpId()) && (ip.getPropertyValue().equals(ipc.getValue()))){
								existFlag = true;
								// itemPropertyList.add(ip);
								break;
							}
						}
					}

					if (!existFlag){// 不存在， 则新增
						ItemProperties itemProperties = new ItemProperties();
						itemProperties.setItemId(itemId);
						itemProperties.setPropertyId(ipc.getpId());

						String vidStr = ipc.getId();
						Long vId = null;
						if (StringUtils.isNotBlank(vidStr)){
							vId = Long.parseLong(vidStr);
						}

						itemProperties.setPropertyValueId(vId);
						itemProperties.setPropertyValue(ipc.getValue());
						itemProperties.setCreateTime(new Date());
						itemProperties = itemPropertiesDao.save(itemProperties);
						itemPropertyList.add(itemProperties);
					}
				}
			}

			List<Long> ipIdToDelList = new ArrayList<Long>();
			List<ItemProperties> ipToDelList = new ArrayList<ItemProperties>();

			// 数据库中原有的 数据 没有在 新的itemProperties 则删掉 （普通属性也被删掉）
			for (ItemProperties ipInDb : ipListInDb){
				boolean delFlag = true;
				for (ItemProperties ip : itemPropertyList){
					if (ipInDb.getId().equals(ip.getId())){
						delFlag = false;
						break;
					}
				}

				if (delFlag){
					ipToDelList.add(ipInDb);
					ipIdToDelList.add(ipInDb.getId());
				}
			}

			itemPropertiesDao.deleteAllByPrimaryKey(ipIdToDelList);
			// itemPropertiesDao.deleteAll(ipToDelList);

			// 保存普通属性
			for (int j = 0; j < iProperties.length; j++){
				ItemProperties itemProperties = iProperties[j];
				ItemProperties itemProperties2 = null;
				String propertyValueStr = itemProperties.getPropertyValue();
				if (propertyValueStr.indexOf("||") != -1){
					String[] propertyValues = propertyValueStr.split("\\|\\|");
					if (propertyValues != null && propertyValues.length > 0){
						for (String propertyValue : propertyValues){
							itemProperties2 = new ItemProperties();
							itemProperties2.setPropertyValue(propertyValue);
							itemProperties2.setPropertyId(itemProperties.getPropertyId());
							itemProperties2.setItemId(itemId);
							itemProperties2.setCreateTime(new Date());
							itemProperties2.setVersion(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties2);
							itemPropertyList.add(itemProperties);
						}
					}
				}else{
					itemProperties.setItemId(itemId);
					itemProperties.setCreateTime(new Date());
					itemProperties = itemPropertiesDao.save(itemProperties);
					itemPropertyList.add(itemProperties);
				}
			}

			// 普通属性 删掉重新加载

			// itemPropertiesDao.deleteItemPropertiesByItemId(itemCommand.getId());

			// propertyManager.findPropertyById(id)

			// saveItemProperties(itemId, skuPropertyCommandArray,
			// itemPropertyList, propertyValueList, iProperties);

		}else{// 新增
			saveItemProperties(itemId, skuPropertyCommandArray, itemPropertyList, propertyValueList, iProperties);

		}

		return itemPropertyList;
	}

	private boolean isItemPrpertiesExistInList(ItemProperties ip,List<ItemProperties> ipList){

		for (ItemProperties i : ipList){

			if (ip.getPropertyValueId() != null){
				if (ip.getPropertyId().equals(i.getPropertyId()) && ip.getPropertyValueId().equals(i.getPropertyValueId())){
					return true;
				}
			}

			if (ip.getPropertyValue() != null){
				if (ip.getPropertyId().equals(i.getPropertyId()) && ip.getPropertyValue().equals(i.getPropertyValue())){
					return true;
				}
			}
		}
		return false;
	}

	private void saveItemProperties(
			Long itemId,
			SkuPropertyCommand[] skuPropertyCommandArray,
			List<ItemProperties> itemPropertyList,
			List<PropertyValue> propertyValueList,
			ItemProperties[] iProperties){
		// 先保存销售属性， 从skuPropertyCommand中拿到销售属性。 销售属性目前有两种 ，一种是 多选，另外一种是自定义多选

		if (skuPropertyCommandArray != null){

			for (SkuPropertyCommand skuPropertyCommand : skuPropertyCommandArray){
				List<ItemPropertyCommand> ipcList = skuPropertyCommand.getPropertyList();

				for (ItemPropertyCommand ip : ipcList){
					if (ip.getId() != null && !ip.getId().equals("undefined")){ // 多选

						for (PropertyValue pv : propertyValueList){
							Long vid = Long.parseLong(ip.getId());
							if (vid.equals(pv.getId())){
								boolean existFlag = false;

								for (ItemProperties svItemProperties : itemPropertyList){
									if (vid.equals(svItemProperties.getPropertyValueId())){
										existFlag = true;
										break;
									}
								}

								if (!existFlag){
									ItemProperties itemProperties = new ItemProperties();
									itemProperties.setItemId(itemId);
									itemProperties.setPropertyId(ip.getpId());
									itemProperties.setPropertyValueId(vid);
									itemProperties.setCreateTime(new Date());
									itemProperties = itemPropertiesDao.save(itemProperties);
									itemPropertyList.add(itemProperties);
								}

							}
						}

					}else{// 自定义多选
						String itemPropertyValue = ip.getValue();

						boolean existFlag = false;
						for (ItemProperties svItemProperties : itemPropertyList){
							if (itemPropertyValue.equals(svItemProperties.getPropertyValue())){
								existFlag = true;
								break;
							}
						}

						if (!existFlag){
							ItemProperties itemProperties = new ItemProperties();
							itemProperties.setItemId(itemId);
							itemProperties.setPropertyId(ip.getpId());
							itemProperties.setPropertyValue(itemPropertyValue);
							itemProperties.setCreateTime(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties);
							itemPropertyList.add(itemProperties);
						}

					}
				}
			}
		}

		// 保存普通属性 着重检查 可能有错
		for (int j = 0; j < iProperties.length; j++){
			ItemProperties itemProperties = iProperties[j];
			ItemProperties itemProperties2 = null;
			String propertyValueStr = itemProperties.getPropertyValue();
			if (propertyValueStr.indexOf("||") != -1){
				String[] propertyValues = propertyValueStr.split("\\|\\|");
				if (propertyValues != null && propertyValues.length > 0){
					for (String propertyValue : propertyValues){
						itemProperties2 = new ItemProperties();
						itemProperties2.setPropertyValue(propertyValue);
						itemProperties2.setPropertyId(itemProperties.getPropertyId());
						itemProperties2.setItemId(itemId);
						itemProperties2.setCreateTime(new Date());
						itemProperties2.setVersion(new Date());
						itemProperties = itemPropertiesDao.save(itemProperties2);
						itemPropertyList.add(itemProperties);
					}
				}
			}else{
				itemProperties.setItemId(itemId);
				itemProperties.setCreateTime(new Date());
				itemProperties = itemPropertiesDao.save(itemProperties);
				itemPropertyList.add(itemProperties);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findItemInfoByItemId(Long itemId)
	 */
	@Override
	@Transactional(readOnly = true)
	public ItemInfo findItemInfoByItemId(Long itemId){
		ItemInfo itemInfo = itemInfoDao.findItemInfoByItemId(itemId);
		return itemInfo;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findItemPropertiesListyByItemId (Long itemId)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ItemProperties> findItemPropertiesListyByItemId(Long itemId){
		List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemId(itemId);
		return itemPropertiesList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findSkuByItemId(Long itemId)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Sku> findSkuByItemId(Long itemId){
		List<Sku> skuList = skuDao.findSkuByItemId(itemId);
		return skuList;
	}

	@Override
	public void createOrUpdateItemImage(ItemImage[] itemImages,Long itemId,String baseImageUrl,boolean isImageTypeGroup){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);

		Map<Long, ItemImage> newMap = new HashMap<Long, ItemImage>();
		List<Long> deleteList = new ArrayList<Long>();

		List<ItemImage> oldImageList = itemImageDao.findItemImageByItemPropAndItemId(paramMap);

		// 1: 遍历从jsp页面传递过来的itemImages数组, 如果
		// Id为空,则表示该itemImage是新增;否则是修改或不处理的(页面上没有修改过的数据)itemImage,
		// 并放入newMap(key:id,value:itemImage对象)中;

		Map<String, Integer> typeItemPropertiesMap = new HashMap<String, Integer>();

		for (ItemImage itemImage : itemImages){
			// 商品图片的排序: 通过type分组, 每组type中的图片的位置都是1,2,3...
			if (!isImageTypeGroup){
				Integer position = 1;
				String type = itemImage.getType();
				String itemProperties = itemImage.getItemProperties() == null ? "" : String.valueOf(itemImage.getItemProperties());
				if (typeItemPropertiesMap.containsKey(type + itemProperties)){
					position = typeItemPropertiesMap.get(type + itemProperties) + 1;
				}
				typeItemPropertiesMap.put(type + itemProperties, position);
				itemImage.setPosition(position);
			}

			Long id = itemImage.getId();
			String picUrl = replacePicUrl(itemImage.getPicUrl(), baseImageUrl);
			if (null == id){
				if (StringUtils.isNotBlank(picUrl)){
					itemImage.setCreateTime(new Date());
					itemImage.setVersion(new Date());
					itemImage.setModifyTime(new Date());
					itemImage.setItemId(itemId);
					itemImage.setPicUrl(picUrl);
					itemImageDao.save(itemImage);
				}
			}else{
				newMap.put(itemImage.getId(), itemImage);
			}
		}
		// 2: 遍历DB中的数据, 通过Id去newMap中取itemIamge对象, 如果itemImage为null时,
		// 则该数据是被删除的数据,将id加入到deleteList中; 否则判断数据是否需要修改还是不处理
		// 3: 判断数据是否需要修改还是不处理: 通过itemImage中的description(描述),
		// type(类型:列表页,内容页,两者都), position(位置),
		// picUrl(图片路径)来判断该itemImage是否要修改
		for (ItemImage oldImage : oldImageList){
			Long oldId = oldImage.getId();
			ItemImage newImage = newMap.get(oldId);
			if (newImage == null){
				deleteList.add(oldId);
			}else{
				// 判断数据是否需要修改还是不处理的数据
				if (!diffImage(oldImage, newImage, baseImageUrl)){
					itemImageDao.updateItemImageById(
							newImage.getType(),
							replacePicUrl(newImage.getPicUrl(), baseImageUrl),
							newImage.getPosition(),
							newImage.getDescription(),
							newImage.getId());
				}
			}
		}
		// 通过Ids删除图片信息
		if (deleteList != null && deleteList.size() > 0){
			itemImageDao.removeItemImageByIds(deleteList);
		}
	}

	/**
	 * 比较两个图片信息是否一样
	 * 
	 * @param oldImage
	 * @param newImage
	 * @return :Boolean
	 * @date 2014-3-5 下午07:41:22
	 */
	private Boolean diffImage(ItemImage oldImage,ItemImage newImage,String baseImageUrl){
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(oldImage.getDescription(), newImage.getDescription());
		equalsBuilder.append(oldImage.getPicUrl(), replacePicUrl(newImage.getPicUrl(), baseImageUrl));
		equalsBuilder.append(oldImage.getPosition(), newImage.getPosition());
		equalsBuilder.append(oldImage.getType(), newImage.getType());
		return equalsBuilder.isEquals();
	}

	private Boolean diffImage(ItemImage oldImage,ItemImageLangCommand newImage,String baseImageUrl){
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang newDesc = (MutlLang) newImage.getDescription();
			equalsBuilder.append(oldImage.getDescription(), newDesc.getDefaultValue());
			MutlLang newUrl = (MutlLang) newImage.getPicUrl();
			equalsBuilder.append(oldImage.getPicUrl(), replacePicUrl(newUrl.getDefaultValue(), baseImageUrl));
		}else{
			SingleLang newDesc = (SingleLang) newImage.getDescription();
			SingleLang newUrl = (SingleLang) newImage.getPicUrl();
			equalsBuilder.append(oldImage.getDescription(), newDesc.getValue());
			equalsBuilder.append(oldImage.getPicUrl(), replacePicUrl(newUrl.getValue(), baseImageUrl));
		}
		equalsBuilder.append(oldImage.getPosition(), newImage.getPosition());
		equalsBuilder.append(oldImage.getType(), newImage.getType());
		return equalsBuilder.isEquals();
	}

	/**
	 * 转换图片url(绝对口路径转换为相对路径)
	 * 
	 * @param picUrl
	 * @param baseImageUrl
	 * @return :String
	 * @date 2014-3-5 下午05:00:20
	 */
	private String replacePicUrl(String picUrl,String baseImageUrl){
		if (StringUtils.isNotBlank(picUrl) && picUrl.toLowerCase().startsWith(baseImageUrl)){
			picUrl = picUrl.replace(baseImageUrl, "");
		}
		return picUrl;
	}

	private String[] replacePicUrl(String[] picUrls,String baseImageUrl){
		String[] arrs = new String[picUrls.length];
		for (int j = 0; j < picUrls.length; j++){
			String picUrl = picUrls[j];
			if (StringUtils.isNotBlank(picUrl) && picUrl.toLowerCase().startsWith(baseImageUrl)){
				picUrl = picUrl.replace(baseImageUrl, "");
				arrs[j] = picUrl;
			}
		}
		return arrs;
	}

	@Override
	public List<ItemImage> findItemImageByItemPropAndItemId(String itemProperties,Long itemId,Long propertyValueId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);
		if (StringUtils.isNotBlank(itemProperties)){
			paramMap.put("itemProperties", itemProperties);
		}
		// 多选
		if (propertyValueId != null && StringUtils.isNotBlank(String.valueOf(propertyValueId))){
			// paramMap.put("propertyValueId", propertyValueId);
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemId(itemId);
			for (ItemProperties itemProp : itemPropertiesList){
				if (propertyValueId.equals(itemProp.getPropertyValueId())){
					paramMap.put("itemProperties", String.valueOf(itemProp.getId()));
				}
			}
		}
		return itemImageDao.findItemImageByItemPropAndItemId(paramMap);
	}

	@Override
	public Integer updateItemInfoLSPIdByItemId(Long lastSelectPropertyId,Long itemId){
		return itemInfoDao.updateItemInfoLSPIdByItemId(lastSelectPropertyId, itemId);
	}

	@Override
	public Integer updateItemInfoLSPVIdByItemId(Long lastSelectPropertyValueId,Long itemId){
		return itemInfoDao.updateItemInfoLSPVIdByItemId(lastSelectPropertyValueId, itemId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#findSkuBySkuCode(java.lang .String)
	 */
	@Override
	public Sku findSkuBySkuCode(String code){
		Sku sku = skuDao.findSkuByExtentionCode(code);
		return sku;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#validateUpdateSkuCode(java .util.List)
	 */
	@Override
	public List<String> validateUpdateSkuCode(List<SkuPropertyCommand> cmdList){
		List<String> result = new ArrayList<String>();

		for (SkuPropertyCommand cmd : cmdList){
			if (cmd.getId() == null || cmd.getId() < 0){// 修改时候新增的
				Sku sku = skuDao.findSkuByExtentionCode(cmd.getCode());
				if (sku != null){// 根据填写的code，能从数据库中查出来。不合法
					result.add(cmd.getCode());
				}
			}else{// 修改的
				Sku sku = skuDao.findSkuByExtentionCode(cmd.getCode());
				if (sku != null){// 该sku的code 查出有数据，并且id 不相等，不合法
					if (!sku.getId().equals(cmd.getId())){
						result.add(cmd.getCode());
					}
				}
			}
		}

		return result;
	}

	/**
	 * import item start
	 */
	@Override
	public List<Item> importItemFromFile(InputStream is,Long shopId) throws BusinessException{

		BusinessException topE = null, currE = null;

		/** 字节流存到内存方便多次读取 **/

		InputStreamCacher cacher = null;
		try{
			cacher = new InputStreamCacher(is);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				is.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}

		ExcelReader itemUpload = excelFactory.createExcelReader("itemUpload");

		ExcelReader skuUpload = excelFactory.createExcelReader("skuUpload");

		Map<String, String> notSalePropIdMap = new LinkedHashMap<String, String>();
		Map<String, Object> itemBeans = new HashMap<String, Object>();
		itemBeans.put("propIds", notSalePropIdMap);
		ReadStatus rs = itemUpload.readSheet(cacher.getInputStream(), 0, itemBeans);

		/***
		 * 根据读取的数据 1：判断店铺是否一致 2：根据行业 查出该行业所对应的属性 3. 设置映射关系 4：根据属性对比excel属性是否一致
		 */

		/** 店铺ID检查 **/
		checkShopId(itemBeans, shopId);

		Long industryId = (Long) itemBeans.get("industryId");
		if (null == industryId){
			throw new BusinessException(ErrorCodes.INDUSTRY_NOT_MATCH_ERROR);
		}

		// 查询这个开关配置（开关的作用是控制批量新建导入模板字段内容是否排序）
		String pdUploadFieldSortFlag = sdkMataInfoManager.findValue(MataInfo.PD_UPLOAD_TEMPLATE_FIELD_SORT_BY_ID);
		Sort[] sorts = null;
		if ("true".equalsIgnoreCase(pdUploadFieldSortFlag)){
			sorts = new Sort[1];
			sorts[0] = new Sort("p.id", "asc");
		}
		List<Property> propertyList = shopDao.findPropertyListByIndustryIdAndShopId(industryId, shopId, sorts);
		// 拆分成 销售属性List 非销售属性
		List<Long> notSalePropIdList = new ArrayList<Long>();

		List<Long> salePropIdList = new ArrayList<Long>();

		Map<Long, Boolean> isReqMap = new HashMap<Long, Boolean>();
		if (Validator.isNotNullOrEmpty(propertyList)){
			for (Property property : propertyList){
				if (property.getIsSaleProp()){
					salePropIdList.add(property.getId());
				}else{
					notSalePropIdList.add(property.getId());
				}
				isReqMap.put(property.getId(), property.getRequired());
			}
		}

		/** 定义动态属性excel与实体类的关系 **/
		/*** sheet1 ***/
		itemCommSheetDefinition(itemUpload, notSalePropIdList, isReqMap);

		/*** sheet2 ***/
		skuCommSheetDefiniton(skuUpload, salePropIdList, isReqMap);

		/****
		 * 再次读取 重新赋值 *
		 ****/
		notSalePropIdMap = new LinkedHashMap<String, String>();
		itemBeans = new HashMap<String, Object>();
		itemBeans.put("propIds", notSalePropIdMap);
		rs = itemUpload.readSheet(cacher.getInputStream(), 0, itemBeans);

		Map<String, String> salePropIdMap = new LinkedHashMap<String, String>();
		Map<String, Object> skuBeans = new HashMap<String, Object>();
		skuBeans.put("propIds", salePropIdMap);
		ReadStatus rs2 = skuUpload.readSheet(cacher.getInputStream(), 1, skuBeans);

		/**
		 * 检验属性列是否匹配
		 */

		propMatching(notSalePropIdMap, notSalePropIdList);

		propMatching(salePropIdMap, salePropIdList);

		/*** 2：判断excel必填项 */
		/*
		 * topE = rsToE(rs, topE, currE); topE = rsToE(rs2, topE, currE);
		 */

		if (rs.getStatus() != ReadStatus.STATUS_SUCCESS){
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList){
				BusinessException e = new BusinessException(message);
				if (topE == null){
					topE = e; // b-101 : Cell{}错误, new
								// Object[]{ExcelUtil.getCell(1,2)}
					currE = e;
				}else{
					currE.setLinkedException(e);
					currE = e;
				}

			}
		}

		if (rs2.getStatus() != ReadStatus.STATUS_SUCCESS){
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs2, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList){
				BusinessException e = new BusinessException(message);
				if (topE == null){
					topE = e; // b-101 : Cell{}错误, new
								// Object[]{ExcelUtil.getCell(1,2)}
					currE = e;
				}else{
					currE.setLinkedException(e);
					currE = e;
				}

			}
		}

		if (topE != null)
			throw topE;

		return dataValidateAndSave(propertyList, itemBeans, skuBeans, shopId, industryId, notSalePropIdList, salePropIdList);
	}

	/**
	 * 验证并保存
	 * 
	 * @param propertyList
	 * @param itemBeans
	 * @param skuBeans
	 * @param shopId
	 * @param industryId
	 */
	public List<Item> dataValidateAndSave(
			List<Property> propertyList,
			Map<String, Object> itemBeans,
			Map<String, Object> skuBeans,
			Long shopId,
			Long industryId,
			List<Long> notSalePropIdList,
			List<Long> salePropIdList){
		BusinessException topE = null, currE = null;
		int counter = 0;
		List<Item> allItemList = itemDao.findEffectiveItemList();
		List<Long> allItemIds = new ArrayList<Long>();
		getItemIds(allItemList, allItemIds);
		// 查询商品
		List<Item> itemList = itemDao.findItemListByShopIdAndIndustryId(shopId, industryId);
		List<Long> itemIds = new ArrayList<Long>();
		getItemIds(itemList, itemIds);
		// 数据字典1 属性id - 属性
		Map<Long, Property> propMap = getPropMap(propertyList);

		// 数据字典2 属性id- SearchConditionItem搜索条件 map
		Map<Long, Map<String, Long>> conItemMap = getConItemMap(salePropIdList);

		// 数据字典3 属性id - 属性value map
		List<Long> alllPropIdList = new ArrayList<Long>();
		alllPropIdList.addAll(salePropIdList);
		alllPropIdList.addAll(notSalePropIdList);
		Map<Long, Map<String, Long>> propValMap = getPropValMap(alllPropIdList);

		// 数据字典4 商品编码 - 商品Id
		Map<String, Long> itemMap = new HashMap<String, Long>();
		// 数据字典5 商品编码 - 商品是否存在表中 true 表示修改 false表示新增
		Map<String, Boolean> itemStatusMap = new HashMap<String, Boolean>();

		if (Validator.isNotNullOrEmpty(itemList)){
			for (Item item : itemList){
				itemMap.put(item.getCode(), item.getId());
				itemStatusMap.put(item.getCode(), false);
			}

		}

		// 后加 itemCode整个网站不允许重复
		Map<String, Long> otherItemMap = new HashMap<String, Long>();
		// 去除该店铺，该行业的商品
		allItemList.removeAll(itemList);
		if (Validator.isNotNullOrEmpty(allItemList)){
			for (Item item : allItemList){
				otherItemMap.put(item.getCode(), item.getId());
			}

		}
		// 数据字典6 自定义多选 销售属性(或者非销售1 单行输入2可输入单选) 涉及itemproperties
		// key = itemId_propertyId_propertyValue
		// value = id
		Map<String, Long> itemPropVMap = new HashMap<String, Long>();

		// 数据字典7 除了7其他部分 涉及itemproperties
		// key = itemId_propertyId_propertyValueId
		// value = id
		Map<String, Long> itemPropIdMap = new HashMap<String, Long>();

		// 数据字典8 涉及itemproperties
		// key = itemId_propertyId
		// value = id
		Map<String, Long> itemPropMap = new HashMap<String, Long>();

		if (Validator.isNotNullOrEmpty(itemIds)){
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertieListByItemIds(itemIds);
			if (Validator.isNotNullOrEmpty(itemPropertiesList)){
				for (ItemProperties itemProperties : itemPropertiesList){
					String key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId();
					itemPropMap.put(key, itemProperties.getId());
					if (itemProperties.getPropertyValue() != null){
						key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
						itemPropVMap.put(key, itemProperties.getId());
					}else{
						key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
						itemPropIdMap.put(key, itemProperties.getId());
					}
				}

			}

		}
		// 数据字典9 sku
		// key = upc
		// value = id
		Map<String, Long> skuMap = getSkuMap(itemIds);

		// 数据字典10 itemReferenceDao
		// key = propId_conItemId
		// value =
		Map<String, Long> itemReferenceMap = getItemReferenceMap();

		int itemCosIndex = 0;
		int itemRowIndex = 0;
		int skuCosIndex = 0;
		int skuCosIndex1 = 0;
		int skuRowIndex = 0;
		Iterator it = null;

		List<ImpItemCommand> impItemCommandList = (List<ImpItemCommand>) itemBeans.get("impItemCommand");

		// 数据字典11 导入sku 需要用到销售价格 挂牌价格

		// key = itemCode
		// value = ImpItemCommand
		Map<String, ImpItemCommand> impItemCommandMap = new HashMap<String, ImpItemCommand>();

		// 数据字典 12
		// key = categorycode
		// value = categoryid
		// Map<String, Long> categoryMap = getCategoryMap();
		Map<String, Long> leafNodeCategoryMap = categoryManager.getLeafNodeCategoryMap();

		// 数据字典13 判断upc是否重复
		// key = upc
		// value =
		Map<String, String> skuUpcMap = new HashMap<String, String>();
		// 查询该配置是否存在
		String pdValidCode = sdkMataInfoManager.findValue(MataInfo.PD_VALID_CODE);
		/** sheet1 ***********/
		/********* 1 .检验code、属性值、分类 ***********/

		itemRowIndex = ITEMCOMM_SHEETDEFINITION_STARTROW;
		if (Validator.isNotNullOrEmpty(impItemCommandList)){
			for (ImpItemCommand itemCommand : impItemCommandList){
				itemCosIndex = ITEMCOMM_SHEETDEFINITION_STARTCOL;
				// true 表示修改 false表示新增
				if (itemMap.get(itemCommand.getCode()) == null){
					itemStatusMap.put(itemCommand.getCode(), false);
				}else{
					itemStatusMap.put(itemCommand.getCode(), true);
				}
				// 配置了正则并且不满足正则条件的情况下
				if (Validator.isNotNullOrEmpty(pdValidCode)){
					if (!Pattern.matches(pdValidCode, itemCommand.getCode())){
						if (++counter < 50){
							BusinessException e = new BusinessException(ErrorCodes.ITEM_CODE_VALID_ERROR, new Object[] {
									itemCommand.getCode(),
									pdValidCode });

							if (topE == null){
								topE = e; // b-101 : Cell{}错误, new
											// Object[]{ExcelUtil.getCell(1,2)}
								currE = e;
							}else{
								currE.setLinkedException(e);
								currE = e;
							}
						}else{
							throw topE;
						}
					}
				}
				if (otherItemMap.get(itemCommand.getCode()) != null){
					if (++counter < 50){
						BusinessException e = new BusinessException(ErrorCodes.ITEMCODE_REPEAT_ERROR, new Object[] {
								1,
								ExcelUtil.getCellIndex(itemRowIndex, 1) });

						if (topE == null){
							topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
							currE = e;
						}else{
							currE.setLinkedException(e);
							currE = e;
						}
					}else{
						throw topE;
					}
				}

				List<ItemProperties> ipslist = new ArrayList<ItemProperties>();

				Map<String, String> prop = itemCommand.getProps();
				it = prop.keySet().iterator();
				while (it.hasNext()){

					itemCosIndex++;

					String key = it.next().toString();
					Long propId = Long.valueOf(key.substring(1, key.length()));
					Property property = (Property) propMap.get(propId);

					/** 编辑类型 ：1 单行输入2可输入单选3单选4多选 */
					ItemProperties itemProperties = new ItemProperties();

					itemProperties.setPropertyId(propId);

					if (property.getEditingType() == 1){
						itemProperties.setPropertyValue(prop.get(key));
						ipslist.add(itemProperties);
					}else if (property.getEditingType() == 2){
						Long pvId = null;
						if (null != propValMap.get(propId)){
							pvId = propValMap.get(propId).get(prop.get(key));
						}
						if (pvId == null){
							itemProperties.setPropertyValue(prop.get(key));
						}else{
							itemProperties.setPropertyValueId(pvId);
						}
						ipslist.add(itemProperties);
					}else if (property.getEditingType() == 3){
						if (Validator.isNotNullOrEmpty(prop.get(key))){
							Long pvId = null;
							if (null != propValMap.get(propId)){
								pvId = propValMap.get(propId).get(prop.get(key));
							}
							if (pvId == null){
								if (++counter < 50){
									BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
											1,
											ExcelUtil.getCellIndex(itemRowIndex, itemCosIndex),
											prop.get(key) });
									if (topE == null){
										topE = e; // b-101 : Cell{}错误, new
													// Object[]{ExcelUtil.getCell(1,2)}
										currE = e;
									}else{
										currE.setLinkedException(e);
										currE = e;
									}
								}else{
									throw topE;
								}
							}else{
								itemProperties.setPropertyValueId(pvId);
							}
							ipslist.add(itemProperties);
						}
					}else if (property.getEditingType() == 4){
						if (Validator.isNotNullOrEmpty(prop.get(key))){

							String propVals = prop.get(key);

							String[] strs = propVals.split(DOUBLE_SLASH_SEPARATOR);

							for (String str : strs){
								Long pvId = null;
								itemProperties = new ItemProperties();
								itemProperties.setPropertyId(propId);
								if (null != propValMap.get(propId)){
									pvId = propValMap.get(propId).get(str);
								}
								if (pvId == null){
									if (++counter < 50){
										BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
												1,
												ExcelUtil.getCellIndex(itemRowIndex, itemCosIndex),
												str });
										if (topE == null){
											topE = e; // b-101 : Cell{}错误, new
														// Object[]{ExcelUtil.getCell(1,2)}
											currE = e;
										}else{
											currE.setLinkedException(e);
											currE = e;
										}
									}else{
										throw topE;
									}
								}else{
									itemProperties.setPropertyValueId(pvId);
								}
								ipslist.add(itemProperties);
							}
						}
					}

				}
				itemCommand.setItemProps(ipslist);
				// map+
				impItemCommandMap.put(itemCommand.getCode(), itemCommand);

				// 分类
				String categoryCodes = itemCommand.getCategoryCodes();
				if (Validator.isNotNullOrEmpty(categoryCodes)){
					List<ItemCategory> itemCategoryList = itemCommand.getItemCategoryList();
					//反斜杠“/”
					String[] strs = null;
					if(categoryCodes.contains("||")){
						strs = categoryCodes.split(DOUBLE_SLASH_SEPARATOR);
					}else{
						strs = categoryCodes.split(BACK_SLANT_SEPARATOR);
					}
					if (Validator.isNotNullOrEmpty(strs)){
						for (String str : strs){
							if(StringUtils.isBlank(str)){
								continue;
							}
							if (Validator.isNullOrEmpty(leafNodeCategoryMap.get(str))){// 增加判断逻辑：如果不是叶子节点，也提示错误信息！产品挂分类必须挂在叶子节点。
								if (++counter < 50){
									BusinessException e = new BusinessException(ErrorCodes.PRODUCT_CATEGORY_NOT_LEAFNODE, new Object[] {
											1,
											ExcelUtil.getCellIndex(itemRowIndex, 9),
											str });
									if (topE == null){
										topE = e; // b-101 : Cell{}错误, new
													// Object[]{ExcelUtil.getCell(1,2)}
										currE = e;
									}else{
										currE.setLinkedException(e);
										currE = e;
									}
								}else{
									throw topE;
								}
							}else{
								ItemCategory itemCategory = new ItemCategory();
								itemCategory.setCategoryId(leafNodeCategoryMap.get(str));

								if (itemCategoryList != null){
									itemCategoryList.add(itemCategory);
								}else{
									itemCategoryList = new ArrayList<ItemCategory>();
									itemCategoryList.add(itemCategory);
								}
							}
						}
					}
					itemCommand.setItemCategoryList(itemCategoryList);
				}

				itemRowIndex++;

			}
		}

		/** sheet2 ***********/
		/********* 1 .检验code ***********/
		/********* 2 .筛选条件 ***********/
		List<ImpSkuCommand> impSkuCommandList = (List<ImpSkuCommand>) skuBeans.get("impSkuCommand");

		Set<String> set = new HashSet<String>();
		for (ImpSkuCommand impSkuCommand : impSkuCommandList){
			set.add(impSkuCommand.getCode());
		}
		List<Long> itemIdsToBeRemove = new ArrayList<Long>();
		for (String str : set){
			itemIdsToBeRemove.add(itemMap.get(str));
		}

		allItemIds.removeAll(itemIdsToBeRemove);

		List<Sku> skuList = skuDao.findSkuByItemIds(allItemIds);
		if (Validator.isNotNullOrEmpty(skuList)){
			for (Sku sku : skuList){
				skuUpcMap.put(sku.getOutid(), "");
			}
		}

		skuRowIndex = SKUCOMM_SHEETDEFINITION_STARTROW;

		if (Validator.isNotNullOrEmpty(impSkuCommandList)){
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){

				skuCosIndex = SKUCOMM_SHEETDEFINITION_STARTCOL;
				skuCosIndex1 = SKUCOMM_SHEETDEFINITION_STARTCOL;
				// 检验upc是否重复
				if (null == skuUpcMap.get(impSkuCommand.getUpc())){
					skuUpcMap.put(impSkuCommand.getUpc(), "");
				}else{
					if (++counter < 50){
						BusinessException e = new BusinessException(ErrorCodes.UPC_REPEAT_ERROR, new Object[] {
								2,
								ExcelUtil.getCellIndex(skuRowIndex, 1) });
						if (topE == null){
							topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
							currE = e;
						}else{
							currE.setLinkedException(e);
							currE = e;
						}
					}else{
						throw topE;
					}
				}

				// 检验code是否存在
				if (itemStatusMap.get(impSkuCommand.getCode()) == null){
					if (++counter < 50){
						BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
								2,
								ExcelUtil.getCellIndex(skuRowIndex, 0),
								impSkuCommand.getCode() });
						if (topE == null){
							topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
							currE = e;
						}else{
							currE.setLinkedException(e);
							currE = e;
						}
					}else{
						throw topE;
					}
				}else{
					// 属性值
					List<ItemProperties> ipslist = impSkuCommand.getItemProps();
					if (ipslist == null){
						ipslist = new ArrayList<ItemProperties>();
					}
					Map<String, String> props = impSkuCommand.getProps();
					it = props.keySet().iterator();

					while (it.hasNext()){
						skuCosIndex1++;
						skuCosIndex1++;
						String key = it.next().toString();
						Long propId = Long.valueOf(key.substring(1, key.length()));
						Property property = (Property) propMap.get(propId);
						ItemProperties itemProperties = new ItemProperties();
						// 何波 sheet2 添加多选检查 editType == 4
						if (property.getEditingType() == 4){
							if (Validator.isNotNullOrEmpty(props.get(key))){
								String propVals = props.get(key);
								Long pvId = null;
								itemProperties.setPropertyId(propId);
								if (null != propValMap.get(propId)){
									pvId = propValMap.get(propId).get(propVals);
								}
								if (pvId == null){
									if (++counter < 50){
										BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
												2,
												ExcelUtil.getCellIndex(skuRowIndex, skuCosIndex1 - 1),
												propVals });
										if (topE == null){
											topE = e; // b-101 : Cell{}错误, new
														// Object[]{ExcelUtil.getCell(1,2)}
											currE = e;
										}else{
											currE.setLinkedException(e);
											currE = e;
										}
									}else{
										throw topE;
									}
								}else{
									itemProperties.setPropertyValueId(pvId);
								}
								ipslist.add(itemProperties);
							}
						}else{
							itemProperties.setPropertyId(propId);
							itemProperties.setPropertyValue(props.get(key));
							// 不支持不能对应前面item的sku
							ipslist.add(itemProperties);
						}
					}
					impSkuCommand.setItemProps(ipslist);
					// 筛选条件
					List<SearchConditionItem> searchConditionItems = impSkuCommand.getSearchConditionItems();
					Map<String, String> scs = impSkuCommand.getScs();
					it = scs.keySet().iterator();

					while (it.hasNext()){
						skuCosIndex++;
						skuCosIndex++;
						String key = it.next().toString();
						Long propId = Long.valueOf(key.substring(1, key.length()));

						String value = scs.get(key);
						if (Validator.isNotNullOrEmpty(value)){
							// 反斜杠“/”
							String[] strs = null;
							if(value.contains("||")){
								strs = value.split(DOUBLE_SLASH_SEPARATOR);
							}else{
								strs = value.split(BACK_SLANT_SEPARATOR);
							}
							if (Validator.isNotNullOrEmpty(strs)){
								for (String str : strs){
									if(StringUtils.isBlank(str)){
										continue;
									}
									if (null == conItemMap.get(propId) || null == conItemMap.get(propId).get(str)){
										if (++counter < 50){
											BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
													2,
													ExcelUtil.getCellIndex(skuRowIndex, skuCosIndex),
													str });
											if (topE == null){
												topE = e; // b-101 : Cell{}错误,
															// new
															// Object[]{ExcelUtil.getCell(1,2)}
												currE = e;
											}else{
												currE.setLinkedException(e);
												currE = e;
											}
										}else{
											throw topE;
										}
									}else{
										SearchConditionItem searchConditionItem = new SearchConditionItem();
										searchConditionItem.setId(conItemMap.get(propId).get(str));
										searchConditionItem.setPropertyId(propId);
										if (searchConditionItems != null){
											searchConditionItems.add(searchConditionItem);
										}else{
											searchConditionItems = new ArrayList<SearchConditionItem>();
											searchConditionItems.add(searchConditionItem);
										}
									}
								}
							}
						}
					}

					impSkuCommand.setSearchConditionItems(searchConditionItems);

				}

				skuRowIndex++;
			}
		}

		/**
		 * 检验完成之后 保存数据
		 */
		if (topE != null)
			throw topE;
		// 保存。
		List<Item> itemListr = saveImpItemCommand(
				impItemCommandList,
				itemStatusMap,
				itemMap,
				propMap,
				itemPropIdMap,
				itemPropVMap,
				itemPropMap,
				shopId,
				industryId,
				notSalePropIdList);

		saveImpSkuCommand(
				impSkuCommandList,
				itemMap,
				itemReferenceMap,
				impItemCommandMap,
				itemPropVMap,
				propMap,
				itemPropIdMap,
				salePropIdList);

		// 执行扩展点
		if (null != itemExtendManager){
			itemExtendManager.extendAfterSaveImpItemCommand(
					impItemCommandList,
					itemStatusMap,
					itemMap,
					propMap,
					itemPropIdMap,
					itemPropVMap,
					itemPropMap,
					shopId,
					industryId,
					notSalePropIdList);
		}
		return itemListr;
	}

	public List<Item> dataValidateAndSaveI18n(
			List<Property> propertyList,
			Map<String, Object> itemBeans,
			Map<String, Object> skuBeans,
			Long shopId,
			Long industryId,
			List<Long> notSalePropIdList,
			List<Long> salePropIdList,
			Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos){

		BusinessException topE = null, currE = null;
		int counter = 0;
		List<Item> allItemList = itemDao.findEffectiveItemList();
		List<Long> allItemIds = new ArrayList<Long>();
		getItemIds(allItemList, allItemIds);
		// 查询商品
		List<Item> itemList = itemDao.findItemListByShopIdAndIndustryId(shopId, industryId);
		List<Long> itemIds = new ArrayList<Long>();
		getItemIds(itemList, itemIds);
		// 数据字典1 属性id - 属性
		Map<Long, Property> propMap = getPropMap(propertyList);

		// 数据字典2 属性id- SearchConditionItem搜索条件 map
		Map<Long, Map<String, Long>> conItemMap = getConItemMap(salePropIdList);

		// 数据字典3 属性id - 属性value map
		List<Long> alllPropIdList = new ArrayList<Long>();
		alllPropIdList.addAll(salePropIdList);
		alllPropIdList.addAll(notSalePropIdList);
		Map<Long, Map<String, Long>> propValMap = getPropValMap(alllPropIdList);

		// 数据字典4 商品编码 - 商品Id
		Map<String, Long> itemMap = new HashMap<String, Long>();
		// 数据字典5 商品编码 - 商品是否存在表中 true 表示修改 false表示新增
		Map<String, Boolean> itemStatusMap = new HashMap<String, Boolean>();

		if (Validator.isNotNullOrEmpty(itemList)){
			for (Item item : itemList){
				itemMap.put(item.getCode(), item.getId());
				itemStatusMap.put(item.getCode(), false);
			}

		}

		// 后加 itemCode整个网站不允许重复
		Map<String, Long> otherItemMap = new HashMap<String, Long>();
		// 去除该店铺，该行业的商品
		allItemList.removeAll(itemList);
		if (Validator.isNotNullOrEmpty(allItemList)){
			for (Item item : allItemList){
				otherItemMap.put(item.getCode(), item.getId());
			}

		}
		// 数据字典6 自定义多选 销售属性(或者非销售1 单行输入2可输入单选) 涉及itemproperties
		// key = itemId_propertyId_propertyValue
		// value = id
		Map<String, Long> itemPropVMap = new HashMap<String, Long>();

		// 数据字典7 除了7其他部分 涉及itemproperties
		// key = itemId_propertyId_propertyValueId
		// value = id
		Map<String, Long> itemPropIdMap = new HashMap<String, Long>();

		// 数据字典8 涉及itemproperties
		// key = itemId_propertyId
		// value = id
		Map<String, Long> itemPropMap = new HashMap<String, Long>();

		if (Validator.isNotNullOrEmpty(itemIds)){
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertieListByItemIds(itemIds);
			if (Validator.isNotNullOrEmpty(itemPropertiesList)){
				for (ItemProperties itemProperties : itemPropertiesList){
					String key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId();
					itemPropMap.put(key, itemProperties.getId());
					if (itemProperties.getPropertyValue() != null){
						key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
						itemPropVMap.put(key, itemProperties.getId());
					}else{
						key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
						itemPropIdMap.put(key, itemProperties.getId());
					}
				}

			}

		}
		// 数据字典9 sku
		// key = upc
		// value = id
		Map<String, Long> skuMap = getSkuMap(itemIds);

		// 数据字典10 itemReferenceDao
		// key = propId_conItemId
		// value =
		Map<String, Long> itemReferenceMap = getItemReferenceMap();

		int itemCosIndex = 0;
		int itemRowIndex = 0;
		int skuCosIndex = 0;
		int skuCosIndex1 = 0;
		int skuRowIndex = 0;
		Iterator it = null;

		List<ImpItemCommand> impItemCommandList = (List<ImpItemCommand>) itemBeans.get("impItemCommand");

		// 数据字典11 导入sku 需要用到销售价格 挂牌价格

		// key = itemCode
		// value = ImpItemCommand
		Map<String, ImpItemCommand> impItemCommandMap = new HashMap<String, ImpItemCommand>();

		// 数据字典 12
		// key = categorycode
		// value = categoryid
		// Map<String, Long> categoryMap = getCategoryMap();
		Map<String, Long> leafNodeCategoryMap = categoryManager.getLeafNodeCategoryMap();

		// 数据字典13 判断upc是否重复
		// key = upc
		// value =
		Map<String, String> skuUpcMap = new HashMap<String, String>();
		// 查询该配置是否存在
		String pdValidCode = sdkMataInfoManager.findValue(MataInfo.PD_VALID_CODE);

		// 数字字典14，判断excel里code是否重复
		List<String> itemCodeList = new ArrayList<String>();

		/** sheet1 ***********/
		/********* 1 .检验code、属性值、分类 ***********/

		itemRowIndex = ITEMCOMM_SHEETDEFINITION_STARTROW;
		if (Validator.isNotNullOrEmpty(impItemCommandList)){
			for (ImpItemCommand itemCommand : impItemCommandList){
				itemCosIndex = ITEMCOMM_SHEETDEFINITION_STARTCOL;

				// 判断excel是否有重复商品编码
				if (itemCodeList.contains(itemCommand.getCode())){
					BusinessException e = new BusinessException(ErrorCodes.ITEMCODE_REPEAT_ERROR_EXCEL, new Object[] {
							1,
							ExcelUtil.getCellIndex(itemRowIndex, 1) });
					if (topE == null){
						topE = e; // b-101 : Cell{}错误, new
									// Object[]{ExcelUtil.getCell(1,2)}
						currE = e;
					}else{
						currE.setLinkedException(e);
						currE = e;
					}
				}else{
					itemCodeList.add(itemCommand.getCode());
				}

				// true 表示修改 false表示新增
				if (itemMap.get(itemCommand.getCode()) == null){
					itemStatusMap.put(itemCommand.getCode(), false);
				}else{
					itemStatusMap.put(itemCommand.getCode(), true);
				}
				// 配置了正则并且不满足正则条件的情况下
				if (Validator.isNotNullOrEmpty(pdValidCode)){
					if (!Pattern.matches(pdValidCode, itemCommand.getCode())){
						BusinessException e = new BusinessException(ErrorCodes.ITEM_CODE_VALID_ERROR, new Object[] {
								itemCommand.getCode(),
								pdValidCode });

						if (topE == null){
							topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
							currE = e;
						}else{
							currE.setLinkedException(e);
							currE = e;
						}
					}
				}
				if (otherItemMap.get(itemCommand.getCode()) != null){
					BusinessException e = new BusinessException(ErrorCodes.ITEMCODE_REPEAT_ERROR, new Object[] {
							1,
							ExcelUtil.getCellIndex(itemRowIndex, 1) });

					if (topE == null){
						topE = e; // b-101 : Cell{}错误, new
									// Object[]{ExcelUtil.getCell(1,2)}
						currE = e;
					}else{
						currE.setLinkedException(e);
						currE = e;
					}
				}

				List<ItemProperties> ipslist = new ArrayList<ItemProperties>();

				Map<String, String> prop = itemCommand.getProps();
				it = prop.keySet().iterator();
				while (it.hasNext()){

					itemCosIndex++;

					String key = it.next().toString();
					Long propId = Long.valueOf(key.substring(1, key.length()));
					Property property = (Property) propMap.get(propId);

					/** 编辑类型 ：1 单行输入2可输入单选3单选4多选 */
					ItemProperties itemProperties = new ItemProperties();

					itemProperties.setPropertyId(propId);

					if (property.getEditingType() == 1){
						itemProperties.setPropertyValue(prop.get(key));
						ipslist.add(itemProperties);
					}else if (property.getEditingType() == 2){
						Long pvId = null;
						if (null != propValMap.get(propId)){
							pvId = propValMap.get(propId).get(prop.get(key));
						}
						if (pvId == null){
							itemProperties.setPropertyValue(prop.get(key));
						}else{
							itemProperties.setPropertyValueId(pvId);
						}
						ipslist.add(itemProperties);
					}else if (property.getEditingType() == 3){
						if (Validator.isNotNullOrEmpty(prop.get(key))){
							Long pvId = null;
							if (null != propValMap.get(propId)){
								pvId = propValMap.get(propId).get(prop.get(key));
							}
							if (pvId == null){
								BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
										1,
										ExcelUtil.getCellIndex(itemRowIndex, itemCosIndex),
										prop.get(key) });
								if (topE == null){
									topE = e; // b-101 : Cell{}错误, new
									// Object[]{ExcelUtil.getCell(1,2)}
									currE = e;
								}else{
									currE.setLinkedException(e);
									currE = e;
								}
							}else{
								itemProperties.setPropertyValueId(pvId);
							}
							ipslist.add(itemProperties);
						}
					}else if (property.getEditingType() == 4){
						if (Validator.isNotNullOrEmpty(prop.get(key))){

							String propVals = prop.get(key);

							String[] strs = propVals.split(DOUBLE_SLASH_SEPARATOR);

							for (String str : strs){
								Long pvId = null;
								itemProperties = new ItemProperties();
								itemProperties.setPropertyId(propId);
								if (null != propValMap.get(propId)){
									pvId = propValMap.get(propId).get(str);
								}
								if (pvId == null){
									BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
											1,
											ExcelUtil.getCellIndex(itemRowIndex, itemCosIndex),
											str });
									if (topE == null){
										topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
										currE = e;
									}else{
										currE.setLinkedException(e);
										currE = e;
									}
								}else{
									itemProperties.setPropertyValueId(pvId);
								}
								ipslist.add(itemProperties);
							}
						}
					}

				}
				itemCommand.setItemProps(ipslist);
				// map+
				impItemCommandMap.put(itemCommand.getCode(), itemCommand);

				// 分类
				String categoryCodes = itemCommand.getCategoryCodes();
				if (Validator.isNotNullOrEmpty(categoryCodes)){
					List<ItemCategory> itemCategoryList = itemCommand.getItemCategoryList();
					// 反斜杠“/”
					String[] strs = null;
					
					if(categoryCodes.contains("||")){
						strs = categoryCodes.split(DOUBLE_SLASH_SEPARATOR);
					}else{
						strs = categoryCodes.split(BACK_SLANT_SEPARATOR);
					}
					if (Validator.isNotNullOrEmpty(strs)){
						for (String str : strs){
							if(StringUtils.isBlank(str)){
								continue;
							}
							
							if (Validator.isNullOrEmpty(leafNodeCategoryMap.get(str))){// 增加判断逻辑：如果不是叶子节点，也提示错误信息！产品挂分类必须挂在叶子节点。
								BusinessException e = new BusinessException(ErrorCodes.PRODUCT_CATEGORY_NOT_LEAFNODE, new Object[] {
										1,
										ExcelUtil.getCellIndex(itemRowIndex, 9),
										str });
								if (topE == null){
									topE = e; // b-101 : Cell{}错误, new
									// Object[]{ExcelUtil.getCell(1,2)}
									currE = e;
								}else{
									currE.setLinkedException(e);
									currE = e;
								}
							}else{
								ItemCategory itemCategory = new ItemCategory();
								itemCategory.setCategoryId(leafNodeCategoryMap.get(str));

								if (itemCategoryList != null){
									itemCategoryList.add(itemCategory);
								}else{
									itemCategoryList = new ArrayList<ItemCategory>();
									itemCategoryList.add(itemCategory);
								}
							}
						}
					}
					itemCommand.setItemCategoryList(itemCategoryList);
				}

				itemRowIndex++;

			}
		}

		/** sheet2 ***********/
		/********* 1 .检验code ***********/
		/********* 2 .筛选条件 ***********/
		List<ImpSkuCommand> impSkuCommandList = (List<ImpSkuCommand>) skuBeans.get("impSkuCommand");

		Set<String> set = new HashSet<String>();
		for (ImpSkuCommand impSkuCommand : impSkuCommandList){
			set.add(impSkuCommand.getCode());
		}
		List<Long> itemIdsToBeRemove = new ArrayList<Long>();
		for (String str : set){
			itemIdsToBeRemove.add(itemMap.get(str));
		}

		allItemIds.removeAll(itemIdsToBeRemove);

		List<Sku> skuList = skuDao.findSkuByItemIds(allItemIds);
		if (Validator.isNotNullOrEmpty(skuList)){
			for (Sku sku : skuList){
				skuUpcMap.put(sku.getOutid(), "");
			}
		}

		skuRowIndex = SKUCOMM_SHEETDEFINITION_STARTROW;

		if (Validator.isNotNullOrEmpty(impSkuCommandList)){
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){

				skuCosIndex = SKUCOMM_SHEETDEFINITION_STARTCOL;
				skuCosIndex1 = SKUCOMM_SHEETDEFINITION_STARTCOL;
				// 检验upc是否重复
				if (null == skuUpcMap.get(impSkuCommand.getUpc())){
					skuUpcMap.put(impSkuCommand.getUpc(), "");
				}else{
					BusinessException e = new BusinessException(ErrorCodes.UPC_REPEAT_ERROR, new Object[] {
							2,
							ExcelUtil.getCellIndex(skuRowIndex, 1) });
					if (topE == null){
						topE = e; // b-101 : Cell{}错误, new
						// Object[]{ExcelUtil.getCell(1,2)}
						currE = e;
					}else{
						currE.setLinkedException(e);
						currE = e;
					}
				}

				// 检验code是否存在
				if (itemStatusMap.get(impSkuCommand.getCode()) == null){
					BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
							2,
							ExcelUtil.getCellIndex(skuRowIndex, 0),
							impSkuCommand.getCode() });
					if (topE == null){
						topE = e; // b-101 : Cell{}错误, new
						// Object[]{ExcelUtil.getCell(1,2)}
						currE = e;
					}else{
						currE.setLinkedException(e);
						currE = e;
					}
				}else{

					// 属性值
					List<ItemProperties> ipslist = impSkuCommand.getItemProps();
					if (ipslist == null){
						ipslist = new ArrayList<ItemProperties>();
					}
					Map<String, String> props = impSkuCommand.getProps();
					it = props.keySet().iterator();

					while (it.hasNext()){
						skuCosIndex1++;
						skuCosIndex1++;
						String key = it.next().toString();
						Long propId = Long.valueOf(key.substring(1, key.length()));
						Property property = (Property) propMap.get(propId);
						ItemProperties itemProperties = new ItemProperties();
						// 何波 sheet2 添加多选检查 editType == 4
						if (property.getEditingType() == 4){
							if (Validator.isNotNullOrEmpty(props.get(key))){
								String propVals = props.get(key);
								Long pvId = null;
								itemProperties.setPropertyId(propId);
								if (null != propValMap.get(propId)){
									pvId = propValMap.get(propId).get(propVals);
								}
								if (pvId == null){
									BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
											2,
											ExcelUtil.getCellIndex(skuRowIndex, skuCosIndex1 - 1),
											propVals });
									if (topE == null){
										topE = e; // b-101 : Cell{}错误, new
										// Object[]{ExcelUtil.getCell(1,2)}
										currE = e;
									}else{
										currE.setLinkedException(e);
										currE = e;
									}
								}else{
									itemProperties.setPropertyValueId(pvId);
								}
								ipslist.add(itemProperties);
							}
						}else{
							itemProperties.setPropertyId(propId);
							itemProperties.setPropertyValue(props.get(key));
							// 不支持不能对应前面item的sku
							ipslist.add(itemProperties);
						}
					}
					impSkuCommand.setItemProps(ipslist);
					// 筛选条件
					List<SearchConditionItem> searchConditionItems = impSkuCommand.getSearchConditionItems();
					Map<String, String> scs = impSkuCommand.getScs();
					it = scs.keySet().iterator();

					while (it.hasNext()){
						skuCosIndex++;
						skuCosIndex++;
						String key = it.next().toString();
						Long propId = Long.valueOf(key.substring(1, key.length()));

						String value = scs.get(key);
						if (Validator.isNotNullOrEmpty(value)){
							// 反斜杠“/”
							String[] strs = null;
							
							if(value.contains("||")){
								strs = value.split(DOUBLE_SLASH_SEPARATOR);
							}else{
								strs = value.split(BACK_SLANT_SEPARATOR);
							}
							if (Validator.isNotNullOrEmpty(strs)){
								for (String str : strs){
									if(StringUtils.isBlank(str)){
										continue;
									}
									if (null == conItemMap.get(propId) || null == conItemMap.get(propId).get(str)){
										BusinessException e = new BusinessException(ErrorCodes.CELL_ERROR, new Object[] {
												2,
												ExcelUtil.getCellIndex(skuRowIndex, skuCosIndex),
												str });
										if (topE == null){
											topE = e; // b-101 : Cell{}错误,
											// new
											// Object[]{ExcelUtil.getCell(1,2)}
											currE = e;
										}else{
											currE.setLinkedException(e);
											currE = e;
										}
									}else{
										SearchConditionItem searchConditionItem = new SearchConditionItem();
										searchConditionItem.setId(conItemMap.get(propId).get(str));
										searchConditionItem.setPropertyId(propId);
										if (searchConditionItems != null){
											searchConditionItems.add(searchConditionItem);
										}else{
											searchConditionItems = new ArrayList<SearchConditionItem>();
											searchConditionItems.add(searchConditionItem);
										}
									}
								}
							}
						}
					}

					impSkuCommand.setSearchConditionItems(searchConditionItems);

				}

				skuRowIndex++;
			}
		}

		/**
		 * 检验完成之后 保存数据
		 */
		if (topE != null)
			throw topE;
		// 保存。
		List<Item> itemListr = saveImpItemCommandI18n(
				impItemCommandList,
				itemStatusMap,
				itemMap,
				propMap,
				itemPropIdMap,
				itemPropVMap,
				itemPropMap,
				shopId,
				industryId,
				notSalePropIdList,
				allI18nItemInfos);

		saveImpSkuCommandI18n(
				impSkuCommandList,
				itemMap,
				itemReferenceMap,
				impItemCommandMap,
				itemPropVMap,
				propMap,
				itemPropIdMap,
				salePropIdList,
				allI18nItemInfos);

		// 执行扩展点
		if (null != itemExtendManager){
			itemExtendManager.extendAfterSaveImpItemCommandI18n(
					impItemCommandList,
					itemStatusMap,
					itemMap,
					propMap,
					itemPropIdMap,
					itemPropVMap,
					itemPropMap,
					shopId,
					industryId,
					notSalePropIdList,
					allI18nItemInfos);
		}
		return itemListr;
	}

	private void getItemIds(List<Item> itemList,List<Long> itemIds){
		if (Validator.isNotNullOrEmpty(itemList)){
			for (Item item : itemList){
				itemIds.add(item.getId());
			}
		}
	}

	private Map<String, Long> getCategoryMap(){
		Map<String, Long> categoryMap = new HashMap<String, Long>();
		List<Category> categoryList = categoryDao.findEnableCategoryList(null);
		if (Validator.isNotNullOrEmpty(categoryList)){
			for (Category category : categoryList){
				categoryMap.put(category.getCode(), category.getId());
			}
		}
		return categoryMap;
	}

	/**
	 * 保存商品信息
	 * 
	 * @param impItemCommandList
	 * @param itemStatusMap
	 * @param itemMap
	 * @param propMap
	 * @param itemPropIdMap
	 * @param itemPropVMap
	 * @param shopId
	 * @param industryId
	 */
	private List<Item> saveImpItemCommand(
			List<ImpItemCommand> impItemCommandList,
			Map<String, Boolean> itemStatusMap,
			Map<String, Long> itemMap,
			Map<Long, Property> propMap,
			Map<String, Long> itemPropIdMap,
			Map<String, Long> itemPropVMap,
			Map<String, Long> itemPropMap,
			Long shopId,
			Long industryId,
			List<Long> notSalePropIdList){
		Item item = null;
		ItemInfo itemInfo = null;

		List<Item> itemList = new ArrayList<Item>();
		if (Validator.isNotNullOrEmpty(impItemCommandList)){

			/*
			 * List<Long> itemIdsForSolr =new ArrayList<Long>(); boolean isUpdateSolr =false;
			 */

			for (ImpItemCommand impItemCommand : impItemCommandList){
				boolean flag = itemStatusMap.get(impItemCommand.getCode());

				String isStyle = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);

				// 商品分类
				List<ItemCategory> itemCategoryList = impItemCommand.getItemCategoryList();

				// TRUE 表示修改 false表示新增
				if (flag){
					/** 商品item **/
					item = itemDao.getByPrimaryKey(itemMap.get(impItemCommand.getCode()));
					item.setModifyTime(new Date());
					// 分类
					if (Validator.isNotNullOrEmpty(itemCategoryList)){
						item.setIsaddcategory(1);
					}else{
						item.setIsaddcategory(0);
					}
					item = itemDao.save(item);

					if (item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){
						itemList.add(item);
					}

					/** 商品信息itemInfo **/
					ItemInfo res = itemInfoDao.findItemInfoByItemId(itemMap.get(impItemCommand.getCode()));

					itemInfo = itemInfoDao.getByPrimaryKey(res.getId());
					itemInfo.setItemId(item.getId());
					itemInfo.setTitle(impItemCommand.getTitle());
					itemInfo.setSubTitle(impItemCommand.getSubTitle());
					itemInfo.setDescription(impItemCommand.getDescription());
					itemInfo.setModifyTime(new Date());
					itemInfo.setSalePrice(impItemCommand.getSalePrice());
					itemInfo.setListPrice(impItemCommand.getListPrice());
					itemInfo.setSketch(impItemCommand.getSketch());
					itemInfo.setSeoDescription(impItemCommand.getSeoDesc());
					itemInfo.setSeoKeywords(impItemCommand.getSeoKeyWords());
					itemInfo.setSeoTitle(impItemCommand.getSeoTitle());

					// 商品款式
					if (isStyle != null && isStyle.equals("true")){
						itemInfo.setStyle(impItemCommand.getItemStyle());
					}

					// 商品类型
					if (impItemCommand.getItemType().trim().equals("赠品")){
						itemInfo.setType(ItemInfo.TYPE_GIFT);
					}else{
						itemInfo.setType(ItemInfo.TYPE_MAIN);
					}

					itemInfo = itemInfoDao.save(itemInfo);

				}else{
					/** 商品item **/
					item = new Item();
					item.setCode(impItemCommand.getCode());
					// Lifecycle状态： 0：无效 1：有效 2：删除 3：未激活
					item.setLifecycle(Item.LIFECYCLE_UNACTIVE);
					item.setCreateTime(new Date());
					item.setShopId(shopId);
					item.setIndustryId(industryId);
					// 分类
					if (Validator.isNotNullOrEmpty(itemCategoryList)){
						item.setIsaddcategory(1);
					}else{
						item.setIsaddcategory(0);
					}

					item.setIsAddTag(0);
					item = itemDao.save(item);
					// map+
					itemMap.put(item.getCode(), item.getId());

					/** 商品信息itemInfo **/
					itemInfo = new ItemInfo();
					itemInfo.setItemId(item.getId());
					itemInfo.setTitle(impItemCommand.getTitle());
					itemInfo.setSubTitle(impItemCommand.getSubTitle());
					itemInfo.setDescription(impItemCommand.getDescription());
					itemInfo.setCreateTime(new Date());
					itemInfo.setSalePrice(impItemCommand.getSalePrice());
					itemInfo.setListPrice(impItemCommand.getListPrice());
					itemInfo.setSketch(impItemCommand.getSketch());
					itemInfo.setSeoDescription(impItemCommand.getSeoDesc());
					itemInfo.setSeoKeywords(impItemCommand.getSeoKeyWords());
					itemInfo.setSeoTitle(impItemCommand.getSeoTitle());

					// 商品款式
					if (isStyle != null && isStyle.equals("true")){
						itemInfo.setStyle(impItemCommand.getItemStyle());
					}

					// 商品类型
					if (impItemCommand.getItemType().trim().equals("赠品")){
						itemInfo.setType(ItemInfo.TYPE_GIFT);
					}else{
						itemInfo.setType(ItemInfo.TYPE_MAIN);
					}

					itemInfo = itemInfoDao.save(itemInfo);

				}

				itemCategoryDao.deleteItemCategoryByItemId(item.getId());

				if (Validator.isNotNullOrEmpty(itemCategoryList)){
					Long[] icIds = new Long[itemCategoryList.size()];
					int i = 0;
					for (ItemCategory itemCategory : itemCategoryList){
						icIds[i] = itemCategory.getCategoryId();
						i++;
					}
					// Arrays.sort(icIds);
					Long icId = icIds[0];
					for (ItemCategory itemCategory : itemCategoryList){
						boolean isDefault = false;
						if (icId.equals(itemCategory.getCategoryId())){
							isDefault = true;
						}
						itemCategoryDao.bindItemCategory(item.getId(), itemCategory.getCategoryId(), isDefault);
					}
				}

				/** 商品属性 **/

				/***
				 * 编辑类型 ：1 单行输入2可输入单选3单选4多选 类型为1 itemPropMap验证 有则改 无则加 类型为2 vid 不为空 就用itemPropIdMap验证 无则加 val 不为空 就用itemPropVMap验证 无则加 类型为3
				 * itemPropIdMap验证 有则改 无则加 类型为4 itemPropIdMap验证 无则加
				 */
				// 删除多选
				if (Validator.isNotNullOrEmpty(notSalePropIdList)){
					for (Long propId : notSalePropIdList){
						Property prop = propMap.get(propId);
						if (prop.getEditingType() == 4){
							itemPropertiesDao.deleteItemPropertiesByItemIdAndPropId(item.getId(), propId);
						}
					}
				}

				List<ItemProperties> list = impItemCommand.getItemProps();
				if (Validator.isNotNullOrEmpty(list)){

					for (ItemProperties itemProperties : list){
						itemProperties.setItemId(item.getId());

						Property prop = propMap.get(itemProperties.getPropertyId());
						if (prop.getEditingType() == 1){
							String key = item.getId() + "_" + itemProperties.getPropertyId();
							if (Validator.isNullOrEmpty(itemPropMap.get(key))){
								saveImpItemproperties(itemProperties, itemPropMap, key);
							}else{
								updateImpItemproperties(itemProperties, itemPropMap, key);
							}
						}else if (prop.getEditingType() == 2){
							if (itemProperties.getPropertyValue() != null){
								String key = item.getId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
								if (Validator.isNullOrEmpty(itemPropVMap.get(key))){
									saveImpItemproperties(itemProperties, itemPropVMap, key);
								}
							}
							if (itemProperties.getPropertyValueId() != null){
								String key = item.getId() + "_" + itemProperties.getPropertyId() + "_"
										+ itemProperties.getPropertyValueId();
								if (Validator.isNullOrEmpty(itemPropIdMap.get(key))){
									saveImpItemproperties(itemProperties, itemPropIdMap, key);
								}
							}
						}else if (prop.getEditingType() == 3){
							String key = item.getId() + "_" + itemProperties.getPropertyId();
							if (Validator.isNullOrEmpty(itemPropMap.get(key))){
								saveImpItemproperties(itemProperties, itemPropMap, key);
							}else{
								updateImpItemproperties(itemProperties, itemPropMap, key);
							}
						}else if (prop.getEditingType() == 4){
							String key = item.getId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
							/*
							 * if (Validator.isNullOrEmpty(itemPropIdMap.get(key))) { saveImpItemproperties(itemProperties, itemPropIdMap,
							 * key); }
							 */
							// 先删后加 不用验证
							saveImpItemproperties(itemProperties, itemPropIdMap, key);
						}
					}
				}
			}

			/*
			 * if(isUpdateSolr && Validator.isNotNullOrEmpty(itemIdsForSolr)){ itemSolrManager.saveOrUpdateItem(itemIdsForSolr); }
			 */

		}
		return itemList;
	}

	private List<Item> saveImpItemCommandI18n(
			List<ImpItemCommand> impItemCommandList,
			Map<String, Boolean> itemStatusMap,
			Map<String, Long> itemMap,
			Map<Long, Property> propMap,
			Map<String, Long> itemPropIdMap,
			Map<String, Long> itemPropVMap,
			Map<String, Long> itemPropMap,
			Long shopId,
			Long industryId,
			List<Long> notSalePropIdList,
			Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos){
		Item item = null;
		ItemInfo itemInfo = null;

		List<Item> itemList = new ArrayList<Item>();
		if (Validator.isNotNullOrEmpty(impItemCommandList)){
			Map<String, ItemInfoLang> i18nItemInfos = new HashMap<String, ItemInfoLang>();
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n && Validator.isNotNullOrEmpty(allI18nItemInfos)){
				Set<String> keys = allI18nItemInfos.keySet();
				for (String key : keys){
					List<ItemInfoExcelCommand> list = allI18nItemInfos.get(key);
					for (ItemInfoExcelCommand itemExcel : list){
						String code = itemExcel.getCode();
						ItemInfoLang lang = new ItemInfoLang();
						LangProperty.I18nPropertyCopyExcelToLang(itemExcel, lang);
						String newKey = key + "||" + code;
						i18nItemInfos.put(newKey, lang);
					}
				}
			}
			for (ImpItemCommand impItemCommand : impItemCommandList){
				boolean flag = itemStatusMap.get(impItemCommand.getCode());

				String isStyle = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);

				// 商品分类
				List<ItemCategory> itemCategoryList = impItemCommand.getItemCategoryList();
				String code = impItemCommand.getCode();
				// TRUE 表示修改 false表示新增
				if (flag){
					/** 商品item **/
					item = itemDao.getByPrimaryKey(itemMap.get(code));
					item.setModifyTime(new Date());
					// 分类
					if (Validator.isNotNullOrEmpty(itemCategoryList)){
						item.setIsaddcategory(1);
					}else{
						item.setIsaddcategory(0);
					}
					item = itemDao.save(item);

					if (item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){
						itemList.add(item);
					}

					/** 商品信息itemInfo **/
					ItemInfo res = itemInfoDao.findItemInfoByItemId(itemMap.get(code));
					Long itemInfoId = res.getId();
					itemInfo = itemInfoDao.getByPrimaryKey(itemInfoId);
					itemInfo.setItemId(item.getId());
					itemInfo.setTitle(impItemCommand.getTitle());
					itemInfo.setSubTitle(impItemCommand.getSubTitle());
					itemInfo.setDescription(impItemCommand.getDescription());
					itemInfo.setModifyTime(new Date());
					itemInfo.setSalePrice(impItemCommand.getSalePrice());
					itemInfo.setListPrice(impItemCommand.getListPrice());
					itemInfo.setSketch(impItemCommand.getSketch());
					itemInfo.setSeoDescription(impItemCommand.getSeoDesc());
					itemInfo.setSeoKeywords(impItemCommand.getSeoKeyWords());
					itemInfo.setSeoTitle(impItemCommand.getSeoTitle());
					if (i18n){
						// 保存商品默认国际化信息
						itemLangManager.saveOrUpdateItemInfoLang(
								impItemCommand.getTitle(),
								impItemCommand.getSubTitle(),
								impItemCommand.getDescription(),
								impItemCommand.getSketch(),
								impItemCommand.getSeoDesc(),
								impItemCommand.getSeoKeyWords(),
								impItemCommand.getSeoTitle(),
								MutlLang.defaultLang(),
								itemInfoId);
						List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
						for (I18nLang i18nLang : i18nLangs){
							String value = i18nLang.getValue();
							String key = i18nLang.getKey();
							if (key.equals(MutlLang.defaultLang())){
								continue;
							}
							String newKey = value + "||" + code;
							ItemInfoLang infoLang = i18nItemInfos.get(newKey);
							if (infoLang == null){
								throw new BusinessException("商品编码:" + code + "没有编写对应的的多语言");
							}
							itemLangManager.saveOrUpdateItemInfoLang(
									infoLang.getTitle(),
									infoLang.getSubTitle(),
									infoLang.getDescription(),
									infoLang.getSketch(),
									infoLang.getSeoDescription(),
									infoLang.getSeoKeywords(),
									infoLang.getSeoTitle(),
									key,
									itemInfoId);
						}
					}
					// 商品款式
					if (isStyle != null && isStyle.equals("true")){
						itemInfo.setStyle(impItemCommand.getItemStyle());
					}

					// 商品类型
					if (impItemCommand.getItemType().trim().equals("赠品")){
						itemInfo.setType(ItemInfo.TYPE_GIFT);
					}else{
						itemInfo.setType(ItemInfo.TYPE_MAIN);
					}

					itemInfo = itemInfoDao.save(itemInfo);

				}else{
					/** 商品item **/
					item = new Item();
					item.setCode(impItemCommand.getCode());
					// Lifecycle状态： 0：无效 1：有效 2：删除 3：未激活
					item.setLifecycle(Item.LIFECYCLE_UNACTIVE);
					item.setCreateTime(new Date());
					item.setShopId(shopId);
					item.setIndustryId(industryId);
					// 分类
					if (Validator.isNotNullOrEmpty(itemCategoryList)){
						item.setIsaddcategory(1);
					}else{
						item.setIsaddcategory(0);
					}

					item.setIsAddTag(0);
					item = itemDao.save(item);
					// map+
					itemMap.put(item.getCode(), item.getId());

					/** 商品信息itemInfo **/
					itemInfo = new ItemInfo();
					itemInfo.setItemId(item.getId());
					itemInfo.setTitle(impItemCommand.getTitle());
					itemInfo.setSubTitle(impItemCommand.getSubTitle());
					itemInfo.setDescription(impItemCommand.getDescription());
					itemInfo.setCreateTime(new Date());
					itemInfo.setSalePrice(impItemCommand.getSalePrice());
					itemInfo.setListPrice(impItemCommand.getListPrice());
					itemInfo.setSketch(impItemCommand.getSketch());
					itemInfo.setSeoDescription(impItemCommand.getSeoDesc());
					itemInfo.setSeoKeywords(impItemCommand.getSeoKeyWords());
					itemInfo.setSeoTitle(impItemCommand.getSeoTitle());

					// 商品款式
					if (isStyle != null && isStyle.equals("true")){
						itemInfo.setStyle(impItemCommand.getItemStyle());
					}

					// 商品类型
					if (impItemCommand.getItemType().trim().equals("赠品")){
						itemInfo.setType(ItemInfo.TYPE_GIFT);
					}else{
						itemInfo.setType(ItemInfo.TYPE_MAIN);
					}

					itemInfo = itemInfoDao.save(itemInfo);
					Long itemInfoId = itemInfo.getId();
					if (i18n){
						// 保存商品默认国际化信息
						itemLangManager.saveOrUpdateItemInfoLang(
								impItemCommand.getTitle(),
								impItemCommand.getSubTitle(),
								impItemCommand.getDescription(),
								impItemCommand.getSketch(),
								impItemCommand.getSeoDesc(),
								impItemCommand.getSeoKeyWords(),
								impItemCommand.getSeoTitle(),
								MutlLang.defaultLang(),
								itemInfoId);
						List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
						for (I18nLang i18nLang : i18nLangs){
							String value = i18nLang.getValue();
							String key = i18nLang.getKey();
							if (key.equals(MutlLang.defaultLang())){
								continue;
							}
							String newKey = value + "||" + code;
							ItemInfoLang infoLang = i18nItemInfos.get(newKey);
							if (infoLang != null){
								itemLangManager.saveOrUpdateItemInfoLang(
										infoLang.getTitle(),
										infoLang.getSubTitle(),
										infoLang.getDescription(),
										infoLang.getSketch(),
										infoLang.getSeoDescription(),
										infoLang.getSeoKeywords(),
										infoLang.getSeoTitle(),
										key,
										itemInfoId);
							}
						}
					}

				}

				itemCategoryDao.deleteItemCategoryByItemId(item.getId());

				if (Validator.isNotNullOrEmpty(itemCategoryList)){
					Long[] icIds = new Long[itemCategoryList.size()];
					int i = 0;
					for (ItemCategory itemCategory : itemCategoryList){
						icIds[i] = itemCategory.getCategoryId();
						i++;
					}
					// Arrays.sort(icIds);
					Long icId = icIds[0];
					for (ItemCategory itemCategory : itemCategoryList){
						boolean isDefault = false;
						if (icId.equals(itemCategory.getCategoryId())){
							isDefault = true;
						}
						itemCategoryDao.bindItemCategory(item.getId(), itemCategory.getCategoryId(), isDefault);
					}
				}

				/** 商品属性 **/

				/***
				 * 编辑类型 ：1 单行输入2可输入单选3单选4多选 类型为1 itemPropMap验证 有则改 无则加 类型为2 vid 不为空 就用itemPropIdMap验证 无则加 val 不为空 就用itemPropVMap验证 无则加 类型为3
				 * itemPropIdMap验证 有则改 无则加 类型为4 itemPropIdMap验证 无则加
				 */
				// 删除多选
				if (Validator.isNotNullOrEmpty(notSalePropIdList)){
					for (Long propId : notSalePropIdList){
						Property prop = propMap.get(propId);
						if (prop.getEditingType() == 4){
							ItemProperties itemProperties = itemPropertiesDao.findItemPropertiesByItemIdAndPropertyId(item.getId(), propId);
							if (itemProperties != null){
								itemPropertiesDao.deleteItemPropertiesByItemIdAndPropId(item.getId(), propId);
								// 删除对应国际化数据
								List<Long> ids = new ArrayList<Long>();
								ids.add(itemProperties.getId());
								itemPropertiesDao.deleteItemPropertiesLangByIds(ids);
							}
						}
					}
				}

				List<ItemProperties> list = impItemCommand.getItemProps();
				if (Validator.isNotNullOrEmpty(list)){

					for (ItemProperties itemProperties : list){
						itemProperties.setItemId(item.getId());

						Property prop = propMap.get(itemProperties.getPropertyId());
						if (prop.getEditingType() == 1){
							String key = item.getId() + "_" + itemProperties.getPropertyId();
							if (Validator.isNullOrEmpty(itemPropMap.get(key))){
								if (i18n){
									saveImpItempropertiesI18n(itemProperties, itemPropMap, key, allI18nItemInfos, prop, code);
								}else{
									saveImpItemproperties(itemProperties, itemPropVMap, key);
								}
							}else{
								if (i18n){
									updateImpItempropertiesI18n(itemProperties, itemPropMap, key, allI18nItemInfos, prop, code);
								}else{
									updateImpItemproperties(itemProperties, itemPropMap, key);
								}
							}
						}else if (prop.getEditingType() == 2){
							if (itemProperties.getPropertyValue() != null){
								String key = item.getId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
								if (Validator.isNullOrEmpty(itemPropVMap.get(key))){
									saveImpItemproperties(itemProperties, itemPropVMap, key);
								}
							}
							if (itemProperties.getPropertyValueId() != null){
								String key = item.getId() + "_" + itemProperties.getPropertyId() + "_"
										+ itemProperties.getPropertyValueId();
								if (Validator.isNullOrEmpty(itemPropIdMap.get(key))){
									if (i18n){
										saveImpItempropertiesRadioOrMutlSelectI18n(itemProperties, itemPropIdMap, key);
									}else{
										saveImpItemproperties(itemProperties, itemPropIdMap, key);
									}
								}
							}
						}else if (prop.getEditingType() == 3){
							String key = item.getId() + "_" + itemProperties.getPropertyId();
							if (Validator.isNullOrEmpty(itemPropMap.get(key))){
								if (i18n){
									saveImpItempropertiesRadioOrMutlSelectI18n(itemProperties, itemPropMap, key);
								}else{
									saveImpItemproperties(itemProperties, itemPropMap, key);
								}
							}else{
								updateImpItemproperties(itemProperties, itemPropMap, key);
							}
						}else if (prop.getEditingType() == 4){
							String key = item.getId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
							// 先删后加 不用验证
							if (i18n){
								saveImpItempropertiesRadioOrMutlSelectI18n(itemProperties, itemPropIdMap, key);
							}else{
								saveImpItemproperties(itemProperties, itemPropIdMap, key);
							}
						}
					}
				}
			}
		}
		return itemList;
	}

	/**
	 * 保存sku以及筛选条件
	 * 
	 * @param impSkuCommandList
	 * @param itemMap
	 * @param skuMap
	 * @param itemPropVMap
	 * @param itemReferenceMap
	 */
	private void saveImpSkuCommand(
			List<ImpSkuCommand> impSkuCommandList,
			Map<String, Long> itemMap,
			Map<String, Long> itemReferenceMap,
			Map<String, ImpItemCommand> impItemCommandMap,
			Map<String, Long> itemPropVMap,
			Map<Long, Property> propMap,
			Map<String, Long> itemPropIdMap,
			List<Long> salePropIdList){
		if (Validator.isNotNullOrEmpty(impSkuCommandList)){
			Set<String> set = new HashSet<String>();
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){
				set.add(impSkuCommand.getCode());
			}
			List<Long> itemIds = new ArrayList<Long>();
			for (String str : set){
				itemIds.add(itemMap.get(str));
			}

			Set<Long> oldPropertyIds = new HashSet<Long>();
			// key = itemId_ItemPro_outId
			// value = id
			Map<String, Long> skuMap = new HashMap<String, Long>();
			if (Validator.isNotNullOrEmpty(itemIds)){
				List<Sku> skuList = skuDao.findSkuByItemIds(itemIds);
				if (Validator.isNotNullOrEmpty(skuList)){
					for (Sku sku : skuList){
						skuMap.put(sku.getItemId() + "_" + sku.getProperties() + "_" + sku.getOutid(), sku.getId());
						if (Validator.isNotNullOrEmpty(sku.getProperties())){
							List<Long> propertyIds = new Gson().fromJson(sku.getProperties(), new TypeToken<List<Long>>(){}.getType());
							for (Long propertyId : propertyIds){
								oldPropertyIds.add(propertyId);
							}
						}
					}
				}
			}

			// 商品下面的所有sku生命周期全部设为0
			deleteSkuByItemCodes(impSkuCommandList);

			Set<Long> newPropertyIds = new HashSet<Long>();
			List<ItemProperties> dbItemPropertiesIds = new ArrayList<ItemProperties>();
			// 保存sku属性
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){

				// List<Long> itemPropertiesIds 5自定义多选
				List<Long> itemPropertiesIds = new ArrayList<Long>();
				List<ItemProperties> list = impSkuCommand.getItemProps();
				Long itemId = itemMap.get(impSkuCommand.getCode());
				// 删除多选
				if (Validator.isNotNullOrEmpty(salePropIdList)){
					for (Long propId : salePropIdList){
						Property prop = propMap.get(propId);
						if (prop.getEditingType() == 4){
							itemPropertiesDao.deleteItemPropertiesByItemIdAndPropId(itemId, propId);
						}
					}
				}

				if (Validator.isNotNullOrEmpty(list)){
					for (ItemProperties itemProperties : list){
						Property prop = propMap.get(itemProperties.getPropertyId());
						String key = itemMap.get(impSkuCommand.getCode()) + "_" + itemProperties.getPropertyId() + "_"
								+ itemProperties.getPropertyValue();
						itemProperties.setItemId(itemId);
						// 何波 多选保存
						if (prop.getEditingType() == 4){
							// 先删后加 不用验证
							boolean mutlSelect = true;
							for (ItemProperties ip : dbItemPropertiesIds){
								Long pvId = ip.getPropertyValueId();
								if (pvId != null){
									// 如果多选pvid已经保存了就不需要再保存了
									if (itemId.equals(ip.getItemId()) && pvId.equals(itemProperties.getPropertyValueId())){
										mutlSelect = false;
										itemPropertiesIds.add(ip.getId());
									}
								}
							}
							if (mutlSelect){
								ItemProperties res = saveImpItemproperties(itemProperties, itemPropIdMap, key);
								dbItemPropertiesIds.add(res);
								Long id = res.getId();
								newPropertyIds.add(id);
								itemPropertiesIds.add(id);
							}
						}else{
							// 何波 自定义多选保存
							Long propertyId = itemPropVMap.get(key);
							if (Validator.isNullOrEmpty(propertyId)){
								itemProperties.setItemId(itemMap.get(impSkuCommand.getCode()));
								ItemProperties res = saveImpItemproperties(itemProperties, itemPropVMap, key);
								// add
								newPropertyIds.add(res.getId());
							}else{
								newPropertyIds.add(propertyId);
							}
							itemPropertiesIds.add(itemPropVMap.get(key));
						}
						// 保存ref
						List<SearchConditionItem> searchConditionItems = impSkuCommand.getSearchConditionItems();
						if (Validator.isNotNullOrEmpty(searchConditionItems)){
							for (SearchConditionItem searchConditionItem : searchConditionItems){
								String key1 = itemPropVMap.get(key) + "_" + searchConditionItem.getId();
								Long refId = itemReferenceMap.get(key1);
								/****** 查询ref表中是否存在对应关系 ***/
								if (Validator.isNullOrEmpty(refId)){
									ItemReference itemReference = new ItemReference();
									itemReference.setItemPropertyId(itemPropVMap.get(key));
									itemReference.setSearchConditionItemId(searchConditionItem.getId());
									// 新增ref
									ItemReference iref = itemReferenceDao.save(itemReference);
									// map+
									itemReferenceMap.put(key1, iref.getId());
								}
							}
						}
					}
				}

				Gson sg = new Gson();

				// 按照升序来排列
				String ipIdStr = null;
				Collections.sort(itemPropertiesIds);
				ipIdStr = sg.toJson(itemPropertiesIds);

				// //////////////////调换顺序

				Long skuId = skuMap.get(itemMap.get(impSkuCommand.getCode()) + "_" + ipIdStr + "_" + impSkuCommand.getUpc());
				Sku sku = null;
				if (Validator.isNotNullOrEmpty(skuId)){
					// update
					sku = skuDao.getByPrimaryKey(skuId);
					sku.setOutid(impSkuCommand.getUpc());
					sku.setModifyTime(new Date());
					sku.setLifecycle(1);
					if (impItemCommandMap.get(impSkuCommand.getCode()) != null){
						sku.setSalePrice(impItemCommandMap.get(impSkuCommand.getCode()).getSalePrice());
						sku.setListPrice(impItemCommandMap.get(impSkuCommand.getCode()).getListPrice());
					}
					sku = skuDao.save(sku);
				}else{
					sku = new Sku();
					sku.setOutid(impSkuCommand.getUpc());
					sku.setCreateTime(new Date());
					sku.setLifecycle(1);
					if (impItemCommandMap.get(impSkuCommand.getCode()) != null){
						sku.setSalePrice(impItemCommandMap.get(impSkuCommand.getCode()).getSalePrice());
						sku.setListPrice(impItemCommandMap.get(impSkuCommand.getCode()).getListPrice());
					}
					sku.setItemId(itemMap.get(impSkuCommand.getCode()));
					sku.setProperties(ipIdStr);
					sku = skuDao.save(sku);
					skuMap.put(sku.getItemId() + "_" + sku.getProperties(), sku.getId());
				}

			}
			List<Long> ids = new ArrayList<Long>();
			if (Validator.isNotNullOrEmpty(oldPropertyIds)){
				for (Long id : oldPropertyIds){
					if (!newPropertyIds.contains(id)){
						ids.add(id);
					}
				}
			}
			if (Validator.isNotNullOrEmpty(ids)){
				itemPropertiesDao.removeItemPropByIds(ids);
			}

		}

	}

	private void saveImpSkuCommandI18n(
			List<ImpSkuCommand> impSkuCommandList,
			Map<String, Long> itemMap,
			Map<String, Long> itemReferenceMap,
			Map<String, ImpItemCommand> impItemCommandMap,
			Map<String, Long> itemPropVMap,
			Map<Long, Property> propMap,
			Map<String, Long> itemPropIdMap,
			List<Long> salePropIdList,
			Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos){
		if (Validator.isNotNullOrEmpty(impSkuCommandList)){
			Set<String> set = new HashSet<String>();
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){
				set.add(impSkuCommand.getCode());
			}
			List<Long> itemIds = new ArrayList<Long>();
			for (String str : set){
				itemIds.add(itemMap.get(str));
			}

			Set<Long> oldPropertyIds = new HashSet<Long>();
			// key = itemId_ItemPro_outId
			// value = id
			Map<String, Long> skuMap = new HashMap<String, Long>();
			if (Validator.isNotNullOrEmpty(itemIds)){
				List<Sku> skuList = skuDao.findSkuByItemIds(itemIds);
				if (Validator.isNotNullOrEmpty(skuList)){
					for (Sku sku : skuList){
						skuMap.put(sku.getItemId() + "_" + sku.getProperties() + "_" + sku.getOutid(), sku.getId());
						if (Validator.isNotNullOrEmpty(sku.getProperties())){
							List<Long> propertyIds = new Gson().fromJson(sku.getProperties(), new TypeToken<List<Long>>(){}.getType());
							for (Long propertyId : propertyIds){
								oldPropertyIds.add(propertyId);
							}
						}
					}
				}
			}

			// 商品下面的所有sku生命周期全部设为0
			deleteSkuByItemCodes(impSkuCommandList);

			Set<Long> newPropertyIds = new HashSet<Long>();
			List<ItemProperties> dbItemPropertiesIds = new ArrayList<ItemProperties>();
			// 保存sku属性
			for (ImpSkuCommand impSkuCommand : impSkuCommandList){

				// List<Long> itemPropertiesIds 5自定义多选
				List<Long> itemPropertiesIds = new ArrayList<Long>();
				List<ItemProperties> list = impSkuCommand.getItemProps();
				Long itemId = itemMap.get(impSkuCommand.getCode());
				// 删除多选
				if (Validator.isNotNullOrEmpty(salePropIdList)){
					for (Long propId : salePropIdList){
						Property prop = propMap.get(propId);
						if (prop.getEditingType() == 4){
							ItemProperties itemProperties = itemPropertiesDao.findItemPropertiesByItemIdAndPropertyId(itemId, propId);
							if (itemProperties != null){
								itemPropertiesDao.deleteItemPropertiesByItemIdAndPropId(itemId, propId);
								// 删除对应国际化数据
								List<Long> ids = new ArrayList<Long>();
								ids.add(itemProperties.getId());
								itemPropertiesDao.deleteItemPropertiesLangByIds(ids);
							}
						}
					}
				}

				if (Validator.isNotNullOrEmpty(list)){
					// 处理 商品多选国际化
					boolean i18n = LangProperty.getI18nOnOff();
					List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
					for (ItemProperties itemProperties : list){
						Property prop = propMap.get(itemProperties.getPropertyId());
						String code = impSkuCommand.getCode();
						Long pid = itemProperties.getPropertyId();
						String key = itemMap.get(code) + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
						itemProperties.setItemId(itemId);
						// 何波 多选保存
						if (prop.getEditingType() == 4){
							key = itemMap.get(code) + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
							// 先删后加 不用验证
							boolean mutlSelect = true;
							for (ItemProperties ip : dbItemPropertiesIds){
								Long pvId = ip.getPropertyValueId();
								if (pvId != null){
									// 如果多选pvid已经保存了就不需要再保存了
									if (pvId.equals(itemProperties.getPropertyValueId()) && ip.getItemId().equals(itemId)){
										mutlSelect = false;
										itemPropertiesIds.add(ip.getId());
									}
								}
							}
							if (mutlSelect){
								ItemProperties res = null;
								if (i18n){
									res = saveImpItempropertiesRadioOrMutlSelectI18n(itemProperties, itemPropIdMap, key);
								}else{
									res = saveImpItemproperties(itemProperties, itemPropIdMap, key);
								}
								dbItemPropertiesIds.add(res);
								Long id = res.getId();
								newPropertyIds.add(id);
								itemPropertiesIds.add(id);
							}
						}else{
							// 何波 自定义多选保存
							Long propertyId = itemPropVMap.get(key);
							Map<String, String> cusMutlmaps = impSkuCommand.getPropsI18n();

							if (Validator.isNullOrEmpty(propertyId)){
								itemProperties.setItemId(itemMap.get(impSkuCommand.getCode()));
								ItemProperties res = saveImpItemproperties(itemProperties, itemPropVMap, key);
								Long id = res.getId();
								// 保存商品属性默认国际化信息
								ItemPropertiesLang ipl = new ItemPropertiesLang();
								ipl.setItemPropertiesId(id);
								ipl.setLang(MutlLang.defaultLang());
								ipl.setPropertyValue(itemProperties.getPropertyValue());
								itemPropertiesLangDao.save(ipl);
								// 保存商品属性国际化信息
								if (i18n){
									for (I18nLang i18nLang : i18nLangs){
										String lang = i18nLang.getKey();
										if (lang.equals(MutlLang.defaultLang())){
											continue;
										}
										String newKey = lang + pid;
										String resI18n = cusMutlmaps.get(newKey);
										if (resI18n != null){
											ipl = new ItemPropertiesLang();
											ipl.setItemPropertiesId(id);
											ipl.setLang(lang);
											ipl.setPropertyValue(resI18n);
											itemPropertiesLangDao.save(ipl);
										}
									}
								}
								// add
								newPropertyIds.add(res.getId());
							}else{
								newPropertyIds.add(propertyId);
							}
							itemPropertiesIds.add(itemPropVMap.get(key));
						}
						// 保存ref
						List<SearchConditionItem> searchConditionItems = impSkuCommand.getSearchConditionItems();
						if (Validator.isNotNullOrEmpty(searchConditionItems)){
							for (SearchConditionItem searchConditionItem : searchConditionItems){
								String key1 = itemPropVMap.get(key) + "_" + searchConditionItem.getId();
								Long refId = itemReferenceMap.get(key1);
								/****** 查询ref表中是否存在对应关系 ***/
								if (Validator.isNullOrEmpty(refId)){
									ItemReference itemReference = new ItemReference();
									itemReference.setItemPropertyId(itemPropVMap.get(key));
									itemReference.setSearchConditionItemId(searchConditionItem.getId());
									// 新增ref
									ItemReference iref = itemReferenceDao.save(itemReference);
									// map+
									itemReferenceMap.put(key1, iref.getId());
								}
							}
						}
					}
				}

				Gson sg = new Gson();

				// 按照升序来排列
				String ipIdStr = null;
				Collections.sort(itemPropertiesIds);
				ipIdStr = sg.toJson(itemPropertiesIds);

				// //////////////////调换顺序

				Long skuId = skuMap.get(itemMap.get(impSkuCommand.getCode()) + "_" + ipIdStr + "_" + impSkuCommand.getUpc());
				Sku sku = null;
				if (Validator.isNotNullOrEmpty(skuId)){
					// update
					sku = skuDao.getByPrimaryKey(skuId);
					sku.setOutid(impSkuCommand.getUpc());
					sku.setModifyTime(new Date());
					sku.setLifecycle(1);
					if (impItemCommandMap.get(impSkuCommand.getCode()) != null){
						sku.setSalePrice(impItemCommandMap.get(impSkuCommand.getCode()).getSalePrice());
						sku.setListPrice(impItemCommandMap.get(impSkuCommand.getCode()).getListPrice());
					}
					sku = skuDao.save(sku);
				}else{
					sku = new Sku();
					sku.setOutid(impSkuCommand.getUpc());
					sku.setCreateTime(new Date());
					sku.setLifecycle(1);
					if (impItemCommandMap.get(impSkuCommand.getCode()) != null){
						sku.setSalePrice(impItemCommandMap.get(impSkuCommand.getCode()).getSalePrice());
						sku.setListPrice(impItemCommandMap.get(impSkuCommand.getCode()).getListPrice());
					}
					sku.setItemId(itemMap.get(impSkuCommand.getCode()));
					sku.setProperties(ipIdStr);
					sku = skuDao.save(sku);
					skuMap.put(sku.getItemId() + "_" + sku.getProperties(), sku.getId());
				}

			}
			List<Long> ids = new ArrayList<Long>();
			if (Validator.isNotNullOrEmpty(oldPropertyIds)){
				for (Long id : oldPropertyIds){
					if (!newPropertyIds.contains(id)){
						ids.add(id);
					}
				}
			}
			if (Validator.isNotNullOrEmpty(ids)){
				itemPropertiesDao.removeItemPropByIds(ids);
			}

		}

	}

	private void deleteSkuByItemCodes(List<ImpSkuCommand> impSkuCommandList){
		Set<String> itemCodes = new HashSet<String>();
		for (ImpSkuCommand impSkuCommand : impSkuCommandList){
			itemCodes.add(impSkuCommand.getCode());
		}
		List<String> itemCodesList = new ArrayList<String>(itemCodes);
		skuDao.deleteSkuByItemCodes(itemCodesList);
	}

	/**
	 * 下载
	 * 
	 * @param request
	 * @param response
	 * @param filePath
	 *            文件路径
	 * @param shopId
	 *            店铺
	 * @param industryId
	 *            行业
	 */
	public void downloadFile(HttpServletRequest request,HttpServletResponse response,Long shopId,Long industryId){
		String path = DEFAULT_PATH + "/tplt_sku_import.xls";
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
		InputStream is = null;
		OutputStream out = null;
		try{
			is = new FileInputStream(file);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try{
			HSSFWorkbook xls = new HSSFWorkbook(is);
			// 现根据动态属性重新构造excel
			Industry industry = industryManager.findIndustryById(industryId);
			ShopCommand shopCommand = shopManager.findShopById(shopId);
			// 查询这个开关配置（开关的作用是控制批量新建导入模板字段内容是否排序）
			String pdUploadFieldSortFlag = sdkMataInfoManager.findValue(MataInfo.PD_UPLOAD_TEMPLATE_FIELD_SORT_BY_ID);
			Sort[] sorts = null;
			if ("true".equalsIgnoreCase(pdUploadFieldSortFlag)){
				sorts = new Sort[1];
				sorts[0] = new Sort("p.id", "asc");
			}

			// List<Property> propertyList = shopDao.findPropertyListByIndustryIdAndShopId(industry.getId(), shopCommand.getShopid(),
			// sorts);
			List<Property> propertyList = shopDao.findPropertyListByIndustryId(industry.getId(), sorts);

			List<Property> salesList = new ArrayList<Property>();
			List<Property> notSalesList = new ArrayList<Property>();
			List<Property> i18nProprtySelect = new ArrayList<Property>();
			if (Validator.isNotNullOrEmpty(propertyList)){
				for (Property property : propertyList){
					if (property.getIsSaleProp()){
						salesList.add(property);
					}else{
						notSalesList.add(property);
						if (property.getEditingType().equals(1)){
							i18nProprtySelect.add(property);
						}
					}

				}
			}
			/** 重新根据属性增加两个sheet表列 ***/
			reconstructExelOfItem(xls, industry, shopCommand, notSalesList);
			reconstructExelOfSku(xls, industry, shopCommand, salesList);
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				createItemInfoSheet(xls, i18nProprtySelect);
			}
			// 下载
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
			out = response.getOutputStream();
			xls.write(out);
		}catch (Exception e1){
			e1.printStackTrace();
		}finally{
			try{
				out.flush();
				out.close();
				is.close();
			}catch (IOException e){
				e.printStackTrace();
			}

		}

	}

	private void propMatching(Map<String, String> map,List<Long> propIdList){
		Iterator it = map.keySet().iterator();
		int index = 0;
		while (it.hasNext()){

			String key = it.next().toString();

			if (map.get(key) == null || !map.get(key).equals(propIdList.get(index).toString())){
				throw new BusinessException(ErrorCodes.PROP_NOT_MATCH_ERROR);
			}

			index++;
		}
	}

	private BusinessException rsToE(ReadStatus rs,BusinessException topE,BusinessException currE){
		if (rs.getStatus() != ReadStatus.STATUS_SUCCESS){
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList){
				BusinessException e = new BusinessException(message);
				if (topE == null){
					topE = e; // b-101 : Cell{}错误, new
								// Object[]{ExcelUtil.getCell(1,2)}
					currE = e;
				}else{
					currE.setLinkedException(e);
					currE = e;
				}

			}
		}
		return topE;

	}

	private void checkShopId(Map<String, Object> itemBeans,Long shopId){
		Long fileShopId = (Long) itemBeans.get("shopId");
		if (fileShopId == null || !fileShopId.equals(shopId)){
			throw new BusinessException(ErrorCodes.SHOP_NOT_MATCH_ERROR);
		}
	}

	private void skuCommSheetDefinitonI18n(ExcelReader skuUpload,List<Long> ids,Map<Long, Boolean> isReqMap){
		ExcelSheet sheetDefinition = skuUpload.getDefinition().getExcelSheets().get(0);
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A3", "H3");
		int startCol = 7;
		int startRow = 2;
		for (Long pId : ids){
			ExcelCell prop = new ExcelCell();
			// values
			prop = new ExcelCell();
			prop.setCol(startCol);
			prop.setRow(startRow);
			prop.setDataName("propValues.pv" + pId);
			prop.setType("string");
			blockDefinition.addCell(prop);
			startCol++;
		}

	}

	private void skuCommSheetDefiniton(ExcelReader skuUpload,List<Long> ids,Map<Long, Boolean> isReqMap){
		ExcelSheet sheetDefinition = skuUpload.getDefinition().getExcelSheets().get(0);
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A8", "B8");
		int startCol = SKUCOMM_SHEETDEFINITION_STARTCOL;
		int startRow = SKUCOMM_SHEETDEFINITION_STARTROW;
		Long cmutliId = null;
		// 取出自定义多选id
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			for (Long pId : ids){
				Property property = propertyManager.findPropertyById(pId);
				if (property.getEditingType() == 5){
					cmutliId = pId;
				}
			}
		}
		for (Long pId : ids){
			ExcelCell prop = new ExcelCell();
			prop.setCol(++startCol);
			prop.setRow(startRow);
			prop.setDataName("props.p" + pId);
			prop.setType("string");
			prop.setMandatory(isReqMap.get(pId));
			blockDefinition.addCell(prop);
			// scId
			prop = new ExcelCell();
			prop.setCol(++startCol);
			prop.setRow(startRow);
			prop.setDataName("scs.v" + pId);
			prop.setType("string");
			blockDefinition.addCell(prop);
			if (i18n && pId.equals(cmutliId)){
				// 读取国际化信息
				int size = ids.size();
				int baseSize = 4;
				if (size == 1){
					int num = 0;
					for (int i = 0; i < MutlLang.i18nLangs().size(); i++){
						String lang = MutlLang.i18nLangs().get(i);
						if (lang.equals(MutlLang.defaultLang())){
							continue;
						}

						prop = new ExcelCell();
						prop.setCol(baseSize + num);
						prop.setRow(startRow);
						prop.setDataName("propsI18n." + lang + pId);
						prop.setType("string");
						blockDefinition.addCell(prop);
						num++;
					}
				}else if (size == 2){
					baseSize = 6;
					int num = 0;
					for (int i = 0; i < MutlLang.i18nLangs().size(); i++){
						String lang = MutlLang.i18nLangs().get(i);
						if (lang.equals(MutlLang.defaultLang())){
							continue;
						}
						prop = new ExcelCell();
						prop.setCol(baseSize + num);
						prop.setRow(startRow);
						prop.setDataName("propsI18n." + lang + pId);
						prop.setType("string");
						blockDefinition.addCell(prop);
						num++;
					}
				}
			}

		}

		blockDefinition = sheetDefinition.getExcelBlock("A5", "B5");
		startCol = SKUCOMM_SHEETDEFINITION_STARTCOL;
		startRow = SKUCOMM_SHEETDEFINITION_PROPID_STARTROW;
		for (Long id : ids){
			startCol++;
			ExcelCell propId = new ExcelCell();
			propId.setCol(++startCol);
			propId.setRow(startRow);
			propId.setDataName("propIds.id" + id);
			propId.setType("string");
			blockDefinition.addCell(propId);
		}
	}

	private void itemCommSheetDefinition(ExcelReader itemUpload,List<Long> ids,Map<Long, Boolean> isReqMap){
		ExcelSheet sheetDefinition = itemUpload.getDefinition().getExcelSheets().get(ZERO);
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A8", "M8");
		int startCol = ITEMCOMM_SHEETDEFINITION_STARTCOL;
		int startRow = ITEMCOMM_SHEETDEFINITION_STARTROW;
		for (Long pId : ids){
			ExcelCell prop = new ExcelCell();
			prop.setCol(++startCol);
			prop.setRow(startRow);
			prop.setDataName("props.p" + pId);
			prop.setMandatory(isReqMap.get(pId));
			prop.setType("string");
			blockDefinition.addCell(prop);
		}

		blockDefinition = sheetDefinition.getExcelBlock("A5", "I5");
		startCol = ITEMCOMM_SHEETDEFINITION_STARTCOL;
		startRow = ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW;
		for (Long pId : ids){
			ExcelCell propId = new ExcelCell();
			propId.setCol(++startCol);
			propId.setRow(startRow);
			propId.setDataName("propIds.id" + pId);
			propId.setType("string");
			blockDefinition.addCell(propId);
		}
	}

	/**
	 * 保存商品属性
	 * 
	 * @param itemProperties
	 */
	private ItemProperties saveImpItemproperties(ItemProperties itemProperties,Map<String, Long> map,String key){
		itemProperties.setCreateTime(new Date());
		ItemProperties res = itemPropertiesDao.save(itemProperties);
		// map+
		map.put(key, res.getId());
		return res;
	}

	private ItemProperties saveImpItempropertiesRadioOrMutlSelectI18n(ItemProperties itemProperties,Map<String, Long> map,String key){
		itemProperties.setCreateTime(new Date());
		ItemProperties res = itemPropertiesDao.save(itemProperties);
		Long id = res.getId();
		List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
		for (I18nLang i18nLang : i18nLangs){
			String lang = i18nLang.getKey();
			ItemPropertiesLang ipl = new ItemPropertiesLang();
			ipl.setItemPropertiesId(id);
			ipl.setLang(lang);
			itemPropertiesLangDao.save(ipl);
		}
		// map+
		map.put(key, res.getId());
		return res;
	}

	private ItemProperties saveImpItempropertiesI18n(
			ItemProperties itemProperties,
			Map<String, Long> map,
			String key,
			Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos,
			Property property,
			String code2){
		itemProperties.setCreateTime(new Date());
		ItemProperties res = itemPropertiesDao.save(itemProperties);
		Long id = res.getId();
		// 保存商品属性默认国际化信息
		ItemPropertiesLang ipl = new ItemPropertiesLang();
		ipl.setItemPropertiesId(id);
		ipl.setLang(MutlLang.defaultLang());
		ipl.setPropertyValue(itemProperties.getPropertyValue());
		itemPropertiesLangDao.save(ipl);

		Map<String, ItemInfoExcelCommand> i18nItemInfos = new HashMap<String, ItemInfoExcelCommand>();
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n && Validator.isNotNullOrEmpty(allI18nItemInfos)){
			Set<String> keys = allI18nItemInfos.keySet();
			for (String key2 : keys){
				List<ItemInfoExcelCommand> list = allI18nItemInfos.get(key2);
				for (ItemInfoExcelCommand itemExcel : list){
					String code1 = itemExcel.getCode();
					String newKey = key2 + "||" + code1;
					i18nItemInfos.put(newKey, itemExcel);
				}
			}
			List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
			for (I18nLang i18nLang : i18nLangs){
				String key1 = i18nLang.getKey();
				if (key1.equals(MutlLang.defaultLang())){
					continue;
				}
				String value = i18nLang.getValue();
				String newKey = value + "||" + code2;
				ItemInfoExcelCommand infoLang = i18nItemInfos.get(newKey);
				if (infoLang == null){
					throw new BusinessException("商品编码:" + code2 + "没有编写对应的的多语言");
				}
				Map<String, String> pvMap = infoLang.getPropValues();
				String pv = pvMap.get("pv" + property.getId());
				ipl = new ItemPropertiesLang();
				ipl.setItemPropertiesId(id);
				ipl.setLang(key1);
				ipl.setPropertyValue(pv);
				itemPropertiesLangDao.save(ipl);
			}
		}

		// map+
		map.put(key, res.getId());
		return res;
	}

	/**
	 * 修改商品属性
	 * 
	 * @param itemProperties
	 */
	private void updateImpItemproperties(ItemProperties itemProperties,Map<String, Long> map,String key){
		ItemProperties ipt = itemPropertiesDao.getByPrimaryKey(map.get(key));
		ipt.setPropertyValue(itemProperties.getPropertyValue());
		ipt.setPropertyValueId(itemProperties.getPropertyValueId());
		itemProperties.setModifyTime(new Date());
		itemPropertiesDao.save(ipt);
	}

	private void updateImpItempropertiesI18n(
			ItemProperties itemProperties,
			Map<String, Long> map,
			String key,
			Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos,
			Property property,
			String code2){
		ItemProperties ipt = itemPropertiesDao.getByPrimaryKey(map.get(key));
		ipt.setPropertyValue(itemProperties.getPropertyValue());
		ipt.setPropertyValueId(itemProperties.getPropertyValueId());
		itemProperties.setModifyTime(new Date());
		ItemProperties res = itemPropertiesDao.save(ipt);
		Long id = res.getId();
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		List<String> langs = new ArrayList<String>();
		langs.add(MutlLang.defaultLang());
		List<ItemPropertiesLang> ipls = itemPropertiesDao.findItemPropertiesLangByIds(ids, langs);
		if (ipls == null || ipls.size() == 0){
			// 保存商品属性默认国际化信息
			ItemPropertiesLang ipl = new ItemPropertiesLang();
			ipl.setItemPropertiesId(id);
			ipl.setLang(MutlLang.defaultLang());
			ipl.setPropertyValue(itemProperties.getPropertyValue());
			itemPropertiesLangDao.save(ipl);
		}else{
			// 修改
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("value", itemProperties.getPropertyValue());
			params.put("itemPropertiesId", id);
			params.put("lang", MutlLang.defaultLang());
			itemPropertiesDao.updatePropertiesLang(params);
		}

		Map<String, ItemInfoExcelCommand> i18nItemInfos = new HashMap<String, ItemInfoExcelCommand>();
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n && Validator.isNotNullOrEmpty(allI18nItemInfos)){
			Set<String> keys = allI18nItemInfos.keySet();
			for (String key2 : keys){
				List<ItemInfoExcelCommand> list = allI18nItemInfos.get(key2);
				for (ItemInfoExcelCommand itemExcel : list){
					String code1 = itemExcel.getCode();
					String newKey = key2 + "||" + code1;
					i18nItemInfos.put(newKey, itemExcel);
				}
			}
			List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
			for (I18nLang i18nLang : i18nLangs){
				String lang = i18nLang.getKey();
				if (lang.equals(MutlLang.defaultLang())){
					continue;
				}
				String value = i18nLang.getValue();
				String newKey = value + "||" + code2;
				ItemInfoExcelCommand infoLang = i18nItemInfos.get(newKey);
				Map<String, String> pvMap = infoLang.getPropValues();
				String pv = pvMap.get("pv" + property.getId());
				ItemPropertiesLang ipl = new ItemPropertiesLang();
				ipl.setItemPropertiesId(id);
				ipl.setLang(lang);
				ipl.setPropertyValue(pv);
				ids.clear();
				ids.add(id);
				langs.clear();
				langs.add(lang);
				if (ipls == null || ipls.size() == 0){
					itemPropertiesLangDao.save(ipl);
				}else{
					// 修改
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("value", pv);
					params.put("itemPropertiesId", id);
					params.put("lang", lang);
					itemPropertiesDao.updatePropertiesLang(params);
				}

			}
		}

	}

	private Map<String, Long> getItemReferenceMap(){
		Map<String, Long> itemReferenceMap = new HashMap<String, Long>();
		List<ItemReference> itemReferenceList = itemReferenceDao.findAllItemReferenceList();
		if (Validator.isNotNullOrEmpty(itemReferenceList)){
			for (ItemReference itemReference : itemReferenceList){
				String key = itemReference.getItemPropertyId() + "_" + itemReference.getSearchConditionItemId();
				itemReferenceMap.put(key, itemReference.getId());
			}
		}
		return itemReferenceMap;
	}

	private Map<String, Long> getSkuMap(List<Long> itemIds){
		Map<String, Long> skuMap = new HashMap<String, Long>();
		if (Validator.isNotNullOrEmpty(itemIds)){
			List<Sku> skuList = skuDao.findSkuByItemIds(itemIds);
			if (Validator.isNotNullOrEmpty(skuList)){
				for (Sku sku : skuList){
					skuMap.put(sku.getOutid(), sku.getId());
				}
			}
		}
		return skuMap;
	}

	public void reconstructExelOfItem(HSSFWorkbook xls,Industry industry,ShopCommand shopCommand,List<Property> notSalesList){
		/********************************************* 样式start ***************************************/
		// 对齐
		HSSFCellStyle cellStyle = xls.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 带边框
		// cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		// 颜色与填充样式
		cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		/********************************************* 样式end ***************************************/
		// 第一个sheet
		HSSFSheet sheet = xls.getSheetAt(HSSFSHEET_1);

		String isStyle = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);

		// 商品款式
		if (isStyle != null && isStyle.equals("false")){
			sheet.setColumnHidden(10, true);
		}

		sheet.getRow(2).createCell(1).setCellValue(shopCommand.getShopname());
		sheet.getRow(3).createCell(1).setCellValue(industry.getName());
		/** 填充shopId industryId */

		if (sheet.getRow(ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW) != null){
			sheet.getRow(ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(0).setCellValue(shopCommand.getShopid());
		}else{
			sheet.createRow(ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(0).setCellValue(shopCommand.getShopid());
		}
		sheet.getRow(ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(1).setCellValue(industry.getId());
		// 第1个sheet 第5行 第9列开始
		int colNo = ITEMCOMM_SHEETDEFINITION_STARTCOL;
		if (Validator.isNotNullOrEmpty(notSalesList)){
			for (Property property : notSalesList){
				try{
					colNo++;

					sheet.getRow(ITEMCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(colNo).setCellValue(property.getId());

					/*** 创建动态属性 */
					HSSFRow propRow = sheet.getRow(TITLE_ROW_INDEX);
					HSSFCell propCell = propRow.createCell(colNo);
					// 列标题
					propCell.setCellValue(property.getName());
					propCell.setCellStyle(cellStyle);

					/** 值类型 1 文本 2 数值 3日期 4日期时间 */
					String valueTypeStr = "";
					int valueType = property.getValueType();
					if (valueType == 2){
						valueTypeStr = "数值";
					}else if (valueType == 3){
						valueTypeStr = "日期";
					}else if (valueType == 4){
						valueTypeStr = "日期时间";
					}else{
						valueTypeStr = "字符串";
					}

					/** 是否必填 **/
					String isRequired = "";
					if (property.getRequired()){
						isRequired = "(必填)";
					}else{
						isRequired = "(非必填)";
					}

					HSSFRow propTypeRow = sheet.getRow(DESC_ROW_INDEX);
					HSSFCell propTypeCell = propTypeRow.createCell(colNo);
					// 添加动态类型选择
					if (property.getEditingType() == 3){
						addExcelDynamicType(xls, property, colNo, true);
					}else if (property.getEditingType() == 2){
						addExcelDynamicType(xls, property, colNo, false);
					}
					// 列描述
					propTypeCell.setCellValue(valueTypeStr + isRequired);

				}catch (Exception e){
					e.printStackTrace();
				}

			}
		}
	}

	public void reconstructExelOfSku(HSSFWorkbook xls,Industry industry,ShopCommand shopCommand,List<Property> salesList){
		/********************************************* 样式start ***************************************/
		// 对齐
		HSSFCellStyle cellStyle = xls.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 带边框
		// cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		// 颜色与填充样式
		cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		/********************************************* 样式end ***************************************/
		// 第二个sheet
		HSSFSheet sheet = xls.getSheetAt(HSSFSHEET_2);
		sheet.getRow(2).createCell(1).setCellValue(shopCommand.getShopname());
		sheet.getRow(3).createCell(1).setCellValue(industry.getName());
		/** 填充shopId industryId */
		if (sheet.getRow(SKUCOMM_SHEETDEFINITION_PROPID_STARTROW) != null){
			sheet.getRow(SKUCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(0).setCellValue(shopCommand.getShopid());
		}else{
			sheet.createRow(SKUCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(0).setCellValue(shopCommand.getShopid());
		}
		sheet.getRow(SKUCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(1).setCellValue(industry.getId());

		// 第二个sheet 第五行 第三列开始
		int colNo = SKUCOMM_SHEETDEFINITION_STARTCOL;
		boolean i18n = LangProperty.getI18nOnOff();
		if (Validator.isNotNullOrEmpty(salesList)){
			for (Property property : salesList){
				try{
					colNo++;

					/*** 创建动态属性 */
					HSSFRow propRow = sheet.getRow(TITLE_ROW_INDEX);
					HSSFCell propCell = propRow.createCell(colNo);
					// 列标题
					propCell.setCellValue(property.getName());
					propCell.setCellStyle(cellStyle);

					/** 值类型 1 文本 2 数值 3日期 4日期时间 */
					String valueTypeStr = "";
					int valueType = property.getValueType();
					if (valueType == 2){
						valueTypeStr = "数值";
					}else if (valueType == 3){
						valueTypeStr = "日期";
					}else if (valueType == 4){
						valueTypeStr = "日期时间";
					}else{
						valueTypeStr = "字符串";
					}

					/** 是否必填 **/
					String isRequired = "";
					if (property.getRequired()){
						isRequired = "(必填)";
					}else{
						isRequired = "(非必填)";
					}
					Integer editType = property.getEditingType();
					if (i18n){
						if (editType != null && editType == 5){
							List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
							int num = 1;
							for (I18nLang i18nLang : i18nLangs){
								String key = i18nLang.getKey();
								String value = i18nLang.getValue();
								if (key.equals(MutlLang.defaultLang())){
									continue;
								}
								num++;
								propCell = propRow.createCell(colNo + num);
								// 列标题
								propCell.setCellValue(property.getName() + "(" + value + ")");
								propCell.setCellStyle(cellStyle);
								HSSFRow propTypeRow = sheet.getRow(DESC_ROW_INDEX);
								HSSFCell propTypeCell = propTypeRow.createCell(colNo + num);
								// 列描述
								propTypeCell.setCellValue(valueTypeStr + isRequired);
							}
						}
					}

					HSSFRow propTypeRow = sheet.getRow(DESC_ROW_INDEX);
					HSSFCell propTypeCell = propTypeRow.createCell(colNo);
					// 列描述
					propTypeCell.setCellValue(valueTypeStr + isRequired);

					colNo++;

					sheet.getRow(SKUCOMM_SHEETDEFINITION_PROPID_STARTROW).createCell(colNo).setCellValue(property.getId());

					/*** 筛选条件名称 */
					HSSFRow propValueRow = sheet.getRow(TITLE_ROW_INDEX);
					HSSFCell propValueCell = propValueRow.createCell(colNo);
					// 列标题
					propValueCell.setCellValue(SKUCOMM_SC_DES);
					propValueCell.setCellStyle(cellStyle);

					// 描述
					propValueRow = sheet.getRow(DESC_ROW_INDEX);
					propValueCell = propValueRow.createCell(colNo);
					// 列标题
					propValueCell.setCellValue(SKUCOMM_SC_REQ);
					// 添加动态类型选择
					if (property.getEditingType() == 3){
						// 单选
						addExcelDynamicType(xls, property, colNo, true);
					}else if (property.getEditingType() == 2){
						// 可输入单选
						addExcelDynamicType(xls, property, colNo, false);
					}
				}catch (Exception e){
					e.printStackTrace();
				}

			}
		}

	}

	public void createItemInfoSheet(HSSFWorkbook xls,List<Property> cusMutliSelect){
		// 对齐
		HSSFCellStyle cellStyle = xls.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 颜色与填充样式
		cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
		List<String> colNames = new ArrayList<String>();
		colNames.add("商品编码");
		colNames.add("商品名称");
		colNames.add("商品描述");
		colNames.add("商品简述");
		colNames.add("seo关键字");
		colNames.add("seo标题");
		colNames.add("seo描述");
		colNames.add("副标题");
		if (i18nLangs != null){
			for (I18nLang i18nLang : i18nLangs){
				if (i18nLang.getDefaultlang() != null && i18nLang.getDefaultlang() == 1){
					continue;
				}
				HSSFSheet hssfSheet = xls.createSheet(i18nLang.getValue());
				HSSFRow propRow = hssfSheet.createRow(1);
				propRow.setHeight((short) 470);
				// 商品编码
				for (int i = 0; i < colNames.size(); i++){
					String name = colNames.get(i);
					hssfSheet.setColumnWidth(i, 5270);
					HSSFCell propCell = propRow.createCell(i);
					propCell.setCellValue(name);
					propCell.setCellStyle(cellStyle);
				}
				// 添加基本动态属性
				if (cusMutliSelect.size() > 0){
					HSSFRow propertyIdRow = hssfSheet.createRow(0);
					propertyIdRow.setZeroHeight(true);
					for (int i = 0; i < cusMutliSelect.size(); i++){
						Property p = cusMutliSelect.get(i);
						String value = p.getName();
						hssfSheet.setColumnWidth(colNames.size() + i, 5270);
						HSSFCell propCell = propRow.createCell(colNames.size() + i);
						propCell.setCellValue(value);
						propCell.setCellStyle(cellStyle);
						// 设置对应属性id
						propertyIdRow.createCell(colNames.size() + i).setCellValue(p.getId());
						;
					}
				}
			}

		}
	}

	private Map<Long, Property> getPropMap(List<Property> propertyList){
		Map<Long, Property> propMap = new HashMap<Long, Property>();
		if (Validator.isNotNullOrEmpty(propertyList)){
			for (Property property : propertyList){
				propMap.put(property.getId(), property);
			}
		}
		return propMap;
	}

	private Map<Long, Map<String, Long>> getConItemMap(List<Long> propertyIds){

		Map<Long, Map<String, Long>> conItemMap = new HashMap<Long, Map<String, Long>>();

		List<SearchConditionItem> searchConditionItemList = searchConditionItemDao.findSearchItemByPropertyIds(propertyIds);

		if (Validator.isNotNullOrEmpty(searchConditionItemList)){
			Long propId = null;
			Map<String, Long> searchConItemMap = new HashMap<String, Long>();

			int index = 0;
			for (SearchConditionItem searchConditionItem : searchConditionItemList){
				index++;
				if (null == propId){
					propId = searchConditionItem.getPropertyId();
				}

				if (propId.equals(searchConditionItem.getPropertyId())){
					searchConItemMap.put(searchConditionItem.getName(), searchConditionItem.getId());
				}else{
					conItemMap.put(propId, searchConItemMap);
					propId = searchConditionItem.getPropertyId();
					searchConItemMap = new HashMap<String, Long>();
					searchConItemMap.put(searchConditionItem.getName(), searchConditionItem.getId());
				}

				if (index == searchConditionItemList.size()){
					conItemMap.put(propId, searchConItemMap);
				}

			}

		}

		return conItemMap;

	}

	private Map<Long, Map<String, Long>> getPropValMap(List<Long> propertyIds){

		Map<Long, Map<String, Long>> propValMap = new HashMap<Long, Map<String, Long>>();

		List<PropertyValue> propertyValueList = propertyValueDao.findPropertyValueListByPropIds(propertyIds);

		if (Validator.isNotNullOrEmpty(propertyValueList)){
			Long propId = null;
			Map<String, Long> propertyValueMap = new HashMap<String, Long>();

			int index = 0;
			for (PropertyValue propertyValue : propertyValueList){
				index++;
				if (null == propId){
					propId = propertyValue.getPropertyId();
				}

				if (propId.equals(propertyValue.getPropertyId())){
					propertyValueMap.put(propertyValue.getValue(), propertyValue.getId());
				}else{
					propValMap.put(propId, propertyValueMap);
					propId = propertyValue.getPropertyId();
					propertyValueMap = new HashMap<String, Long>();
					propertyValueMap.put(propertyValue.getValue(), propertyValue.getId());
				}

				if (index == propertyValueList.size()){
					propValMap.put(propId, propertyValueMap);
				}

			}

		}

		return propValMap;

	}

	@Override
	public List<Long> importItemImgFromFile(String path,File zipFile,Long shopId,String uploadType) throws Exception{
		// 用于刷新solr索引
		List<Long> itemIdsForSolr = new ArrayList<Long>();

		Map<String, String> tmpImgFilePathMap = new HashMap<String, String>();
		Calendar calendar = Calendar.getInstance();
		String userDefinedPath = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		path = path + userDefinedPath;
		File pathFile = new File(path);
		if (!pathFile.exists()){
			pathFile.mkdirs();
		}
		String tmpPath = zipFile.getParent();
		String fileName = zipFile.getName();
		String itemCode = "";
		ItemImage itemImage = null;
		List<String> itemCodeList = new ArrayList<String>();
		/** itemMap(key: itemCode, value: item_id) */
		Map<String, Long> itemMap = new HashMap<String, Long>();

		List<Long> itemIds = new ArrayList<Long>();

		/** 解压zip文件 */
		String zipExp = ImageOpeartion.getExp(fileName);
		String zipDir = fileName.replace(zipExp, "");
		CompressUtils.unzipImage(zipFile.getPath(), tmpPath + File.separator + zipDir);

		File file = new File(tmpPath + File.separator + zipDir);
		if (!file.exists()){
			file.mkdirs();
		}
		File[] itemFiles = file.listFiles();
		File[] itemImgFiles = null;
		try{
			if (itemFiles != null && itemFiles.length > 0){
				/** 验证商品图片文件名 */
				for (File itemFile : itemFiles){
					itemCodeList.add(itemFile.getName());
				}
				List<Item> itemList = itemDao.findItemListByCodes(itemCodeList, shopId);

				if (!(itemList != null && itemList.size() > 0)){
					throw new BusinessException(ErrorCodes.IMPORT_ITEM_CODE_NOT_EXISTS, new Object[] { "" });
				}

				for (Item item : itemList){
					itemMap.put(item.getCode(), item.getId());
					itemIds.add(item.getId());
					if (item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){
						itemIdsForSolr.add(item.getId());
					}
				}

				if (itemList.size() != itemFiles.length){
					if (itemList.size() > itemFiles.length){
						log.info("The data is error!");
						throw new BusinessException(ErrorCodes.DATA_ERROR);
					}
					String notExistsCode = "";
					for (String codeStr : itemCodeList){
						if (itemMap.get(codeStr) == null){
							notExistsCode += codeStr + ",";
						}
					}
					notExistsCode.substring(0, notExistsCode.length() - 2);
					throw new BusinessException(ErrorCodes.IMPORT_ITEM_CODE_NOT_EXISTS, new Object[] { notExistsCode });
				}

				/** 商品图片类型 */
				List<ChooseOption> itemImgRoleList = chooseOptionManager.findOptionListByGroupCode(ITEM_IMG_ROLE);
				Map<String, String> itemImgRoleMap = new HashMap<String, String>();
				for (ChooseOption itemImgRole : itemImgRoleList){
					itemImgRoleMap.put(itemImgRole.getOptionLabel(), itemImgRole.getOptionValue());
				}
				/** 商品图片尺寸 */
				List<ChooseOption> itemImgTypeList = chooseOptionManager.findOptionListByGroupCode(ITEM_IMG_TYPE);
				Map<String, String> itemImgTypeMap = new HashMap<String, String>();
				for (ChooseOption itemImgRole : itemImgTypeList){
					itemImgTypeMap.put(itemImgRole.getOptionLabel(), itemImgRole.getOptionValue());
				}

				/** 商品的颜色属性 */
				List<ItemPropertiesCommand> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemCodes(itemCodeList);
				/** key: item_id|property_value, value:item_properties_id */
				Map<String, Long> itemPropMap = new HashMap<String, Long>();
				/** key: itemId, value:item_properties_id */
				Map<Long, String> isHaveColorPropMap = new HashMap<Long, String>();
				for (ItemPropertiesCommand itemPropertiesCommand : itemPropertiesList){
					Integer editingType = itemPropertiesCommand.getType();
					String propertyValue = "";
					if (editingType.equals(Property.EDITING_TYPE_MULTI_SELECT)){
						/** 多选, 应该取property_value中的value */
						propertyValue = itemPropertiesCommand.getProValue();
					}else if (editingType.equals(Property.EDITING_TYPE_CUSTOM_MULTI_SELECT)){
						/** 自定义多选 */
						propertyValue = itemPropertiesCommand.getPropertyValue();
					}
					String key = itemPropertiesCommand.getItemId() + "|" + propertyValue;
					itemPropMap.put(key, itemPropertiesCommand.getId());

					isHaveColorPropMap.put(itemPropertiesCommand.getItemId(), propertyValue);
				}

				/** 是否是全量导入, 是全量导入则删除原有的图片 */
				if (UPLOAD_TYPE.equals(uploadType) && null != itemIds && itemIds.size() > 0){
					itemImageDao.removeItemImageByItemIds(itemIds);
				}

				for (File itemFile : itemFiles){

					Boolean isHaveColorProp = false;
					itemCode = itemFile.getName();
					Long itemId = itemMap.get(itemCode);
					itemImgFiles = new File(tmpPath + File.separator + zipDir + File.separator + itemCode).listFiles();
					Map<String, ItemImage> itemImageMap = new HashMap<String, ItemImage>();
					Map<String, ItemImageLang> itemImageLangMap = new HashMap<String, ItemImageLang>();
					for (File itemImgFile : itemImgFiles){
						if (itemImgFile.isDirectory()){
							continue;
						}
						String itemImgFileName = itemImgFile.getName();
						if (CACHE_FILE_NAME.equals(itemImgFileName)){
							continue;
						}
						if (StringUtils.isNotBlank(isHaveColorPropMap.get(itemId))){
							isHaveColorProp = true;
						}
						/** 检查图片名称 */
						checkUploadImgName(itemId, itemCode, itemImgFileName, isHaveColorProp, itemImgTypeMap, itemPropMap);

						/** 是否有颜色属性 */
						String imgExp = ImageOpeartion.getExp(itemImgFileName);
						String[] strs = itemImgFileName.replace(itemCode, "").replace(imgExp, "").split("-");
						Long itemPropId = null;
						String type = null;
						String position = null;
						if (isHaveColorProp){
							/** 商品有颜色属性 */
							String color = strs[1];
							itemPropId = itemPropMap.get(itemId + "|" + color);
							type = itemImgTypeMap.get(strs[2]);
							position = strs[3];
						}else{
							/** 商品没有颜色属性 */
							type = itemImgTypeMap.get(strs[1]);
							position = strs[2];
						}
						/** 生成原图 */
						String picName = ImageOpeartion.getPicName();
						String srcPicName = picName + imgExp;
						File outFile = new File(tmpPath + File.separator + userDefinedPath + File.separator + srcPicName);
						tmpImgFilePathMap.put("source" + picName, srcPicName);
						if (!outFile.getParentFile().exists()){
							outFile.getParentFile().mkdirs();// 创建文件夹
						}

						FileInputStream fisItemImgFile = new FileInputStream(itemImgFile);
						FileOutputStream fosOutFile = new FileOutputStream(outFile);
						try{
							IOUtils.copy(fisItemImgFile, fosOutFile);
						}catch (Exception e){ // 异常时关闭文件
							throw new Exception(e);
						}finally{
							fisItemImgFile.close();
							fosOutFile.close();
						}

						/** 生成缩略图 */
						String role = itemImgRoleMap.get(type);
						if (StringUtils.isBlank(role)){
							log.error("batch import item image : item image role is {} and type is {}", role, type);
							throw new BusinessException(ErrorCodes.IMPORT_IMAGE_FILE_ROLE_NOT_EXIST, new Object[] { type });
						}
						tmpImgFilePathMap = uploadManager.upload(tmpImgFilePathMap, role, outFile);

						/** 获得图片url */
						String[] roles = role.split("\\|");
						String ru = roles[roles.length - 1];
						String imgUrl = tmpImgFilePathMap.get(ru + picName);

						if(Validator.isNullOrEmpty(itemImage)){
							/** 保存商品图片 */
							itemImage = new ItemImage();
							itemImage.setItemId(itemMap.get(itemCode));
							itemImage.setItemProperties(itemPropId);
							itemImage.setPicUrl(userDefinedPath + "/" + imgUrl);
							itemImage.setCreateTime(new Date());
							itemImage.setVersion(new Date());
							itemImage.setDescription("");
							itemImage.setType(StringUtils.trim(type));
							itemImage.setPosition(Integer.valueOf(StringUtils.trim(position)));
							itemImage = itemImageDao.save(itemImage);

							itemImageMap.put(itemImgFileName.replace(imgExp, ""), itemImage);

							// 国际化
							boolean i18n = LangProperty.getI18nOnOff();
							if (i18n){
								List<String> languages = MutlLang.i18nLangs();
								for (String language : languages){
									ItemImageLang itemImageLang = new ItemImageLang();
									itemImageLang.setItemImageId(itemImage.getId());
									itemImageLang.setLang(language);
									itemImageLang.setPicUrl(itemImage.getPicUrl());
									itemImageLang.setDescription(itemImage.getDescription());
									itemImageLang = itemImageLangDao.save(itemImageLang);
									itemImageLangMap.put(itemImageLang.getItemImageId() + "_" + itemImageLang.getLang(), itemImageLang);
								}
							}
						}
					}

					for (File directoryFile : itemImgFiles){
						if (directoryFile.isDirectory()){
							boolean i18n = LangProperty.getI18nOnOff();
							if (!i18n){
								break;
							}
							String directoryName = directoryFile.getName();
							List<String> languages = MutlLang.i18nLangs();
							if (!languages.contains(directoryName)){
								throw new BusinessException(ErrorCodes.IMPORT_FILE_NOT_TRUE);
							}

							File[] files = directoryFile.listFiles();
							for (File itemImgFile : files){
								String itemImgFileName = itemImgFile.getName();
								if (CACHE_FILE_NAME.equals(itemImgFileName)){
									continue;
								}
								if (StringUtils.isNotBlank(isHaveColorPropMap.get(itemId))){
									isHaveColorProp = true;
								}
								/** 检查图片名称 */
								checkUploadImgName(itemId, itemCode, itemImgFileName, isHaveColorProp, itemImgTypeMap, itemPropMap);

								/** 是否有颜色属性 */
								String imgExp = ImageOpeartion.getExp(itemImgFileName);
								String[] strs = itemImgFileName.replace(itemCode, "").replace(imgExp, "").split("-");
								Long itemPropId = null;
								String type = null;
								String position = null;
								if (isHaveColorProp){
									/** 商品有颜色属性 */
									String color = strs[1];
									itemPropId = itemPropMap.get(itemId + "|" + color);
									type = itemImgTypeMap.get(strs[2]);
									position = strs[3];
								}else{
									/** 商品没有颜色属性 */
									type = itemImgTypeMap.get(strs[1]);
									position = strs[2];
								}
								/** 生成原图 */
								String picName = ImageOpeartion.getPicName();
								String srcPicName = picName + imgExp;
								File outFile = new File(tmpPath + File.separator + userDefinedPath + File.separator + srcPicName);
								tmpImgFilePathMap.put("source" + picName, srcPicName);
								if (!outFile.getParentFile().exists()){
									outFile.getParentFile().mkdirs();// 创建文件夹
								}

								FileInputStream fisItemImgFile = new FileInputStream(itemImgFile);
								FileOutputStream fosOutFile = new FileOutputStream(outFile);
								try{
									IOUtils.copy(fisItemImgFile, fosOutFile);
								}catch (Exception e){ // 异常时关闭文件
									throw new Exception(e);
								}finally{
									fisItemImgFile.close();
									fosOutFile.close();
								}

								/** 生成缩略图 */
								String role = itemImgRoleMap.get(type);
								if (StringUtils.isBlank(role)){
									log.error("batch import item image : item image role is {} and type is {}", role, type);
									throw new BusinessException(ErrorCodes.IMPORT_IMAGE_FILE_ROLE_NOT_EXIST, new Object[] { type });
								}
								tmpImgFilePathMap = uploadManager.upload(tmpImgFilePathMap, role, outFile);

								/** 获得图片url */
								String[] roles = role.split("\\|");
								String ru = roles[roles.length - 1];
								String imgUrl = tmpImgFilePathMap.get(ru + picName);

								/** 保存商品图片 */
								itemImage = itemImageMap.get(itemImgFileName.replace(imgExp, ""));

								ItemImageLang itemImageLang = itemImageLangMap.get(itemImage.getId() + "_" + directoryName);

								itemImageLang = itemImageLangDao.getByPrimaryKey(itemImageLang.getId());
								itemImageLang.setPicUrl(userDefinedPath + "/" + imgUrl);

							}
						}
					}
				}

				/** 将临时文件拷贝到正式目录中 */
				InputStream input = null;
				OutputStream output = null;
				for (Map.Entry<String, String> entry : tmpImgFilePathMap.entrySet()){
					File tmpFile = new File(tmpPath + File.separator + userDefinedPath + File.separator + entry.getValue());
					input = new FileInputStream(tmpFile);
					output = new FileOutputStream(path + File.separator + entry.getValue());
					try{
						IOUtils.copy(input, output);
					}finally{
						input.close();
						output.close();
					}
				}

			}else{
				throw new BusinessException(ErrorCodes.IMPORT_FILE_NOT_TRUE);
			}
		}finally{

			/** 删除临时图片文件 */
			for (Map.Entry<String, String> entry : tmpImgFilePathMap.entrySet()){
				File tmpFile = new File(tmpPath + File.separator + userDefinedPath + File.separator + entry.getValue());
				tmpFile.delete();
			}

			/** 删除压缩之后的文件夹 */
			log.debug("delete zip file path is {}", zipFile.getPath());
			FileUtils.deletefile(zipFile.getPath());
			log.debug("delete unzip file path is {}", tmpPath + File.separator + zipDir);
			FileUtils.deletefile(tmpPath + File.separator + zipDir);
		}
		return itemIdsForSolr;
	}

	/**
	 * 验证上传的zip中的文件名是否规定规格 : 图片名字规格 : 1, 有颜色属性: ${商品编码}-${颜色}-${类型}-${position}.jpg 2,没有颜色属性:${商品编码}-${类型}-${position}.jpg 1: 商品是否有颜色属性 2:
	 * 商品的类型是否存在
	 * 
	 * @param itemImgFileName
	 *            :图片名称
	 * @param isHaveColorProp
	 *            :商品是否有颜色属性
	 * @return
	 */
	private void checkUploadImgName(
			Long itemId,
			String itemCode,
			String itemImgFileName,
			Boolean isHaveColorProp,
			Map<String, String> itemImgTypeMap,
			Map<String, Long> itemPropMap) throws Exception{
		String fileName = itemImgFileName;
		/** 文件名是以商品code开始 */
		if (!fileName.startsWith(itemCode)){
			log.error(
					"item image name is {}, item code is {}. item image name does not agree with commodity item code! ",
					fileName,
					itemCode);
			throw new BusinessException(ErrorCodes.IMPORT_FILE_NAME_NOT_TRUE_DIR, new Object[] { fileName, itemCode });
		}
		fileName = fileName.replace(itemCode, "");
		String[] strs = fileName.split("-");

		if (strs == null || strs.length < 3 || StringUtils.isNotBlank(strs[0])){
			throw new BusinessException(ErrorCodes.IMPORT_FILE_NAME_NOT_TRUE, new Object[] { itemImgFileName });
		}
		/** 有无颜色属性 时, 文件名的规格 */
		String type = "";
		if (isHaveColorProp){
			String color = strs[1];
			type = strs[2];
			if (itemPropMap.get(itemId + "|" + color) == null){
				throw new BusinessException(ErrorCodes.IMPORT_FILE_COLOR_PROP_NOT_EXIST, new Object[] { itemImgFileName, color });
			}
		}else{
			type = strs[1];
		}
		if (StringUtils.isBlank(itemImgTypeMap.get(type))){
			throw new BusinessException(ErrorCodes.IMPORT_FILE_IMAGE_TYPE_NOT_EXIST, new Object[] { itemImgFileName, type });
		}
	}

	@Override
	public ItemCommand findItemCommandByCode(String code,String customBaseUrl){
		ItemCommand itemCommand = itemDao.findItemCommandByCode(code);
		if (itemCommand != null){
			List<Long> itemIds = new ArrayList<Long>();
			itemIds.add(itemCommand.getId());
			List<ItemImage> itemImageList = itemImageDao.findItemImageByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
			if (itemImageList != null && itemImageList.size() > 0){
				String picUrl = customBaseUrl + itemImageList.get(0).getPicUrl();
				itemCommand.setPicUrl(picUrl);
				return itemCommand;
			}
		}
		return itemCommand;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemManager#activeItemByIds(java.util .List, java.lang.Integer)
	 */
	@Override
	public Integer activeItemByIds(List<Long> ids,Date activeBeginTime){
		// Date activeBeginTime = null;

		// if(StringUtils.isNotBlank(activeTimeStr)){
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// activeBeginTime = format.parse(activeTimeStr);
		String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);
		Integer result = itemDao.enableOrDisableItemByIds(ids, 1, updateListTimeFlag);

		Integer timeResult = itemInfoDao.updateActiveBeginTime(ids, activeBeginTime);

		// 刷新索引
		boolean i18n = LangProperty.getI18nOnOff();
		boolean solrFlag = false;
		if (i18n){
			solrFlag = itemSolrManager.saveOrUpdateItemI18n(ids);
		}else{
			solrFlag = itemSolrManager.saveOrUpdateItem(ids);
		}
		if (!solrFlag){
			throw new BusinessException(ErrorCodes.SOLR_REFRESH_ERROR);
		}

		// }

		return timeResult;
	}

	/**
	 * @author 何波 @Description: 根据分类id 查询商品 @param cateIds @return List<ItemInfo> @throws
	 */
	@Override
	public List<ItemInfo> findItemInfosByCateIds(List<Long> cateIds){
		if (CollectionUtils.isEmpty(cateIds)){
			return null;
		}
		return itemInfoDao.findItemInfosByCateIds(cateIds);
	}

	/**
	 * @author 何波
	 * @Description:根据itemIds查询ItemInfo
	 * @param itemIds
	 * @return
	 */
	@Override
	public List<ItemInfo> findItemInfosByItemIds(List<Long> itemIds){
		if (CollectionUtils.isEmpty(itemIds)){
			return null;
		}
		return itemInfoDao.findItemInfosByItemIds(itemIds);
	}

	/**
	 * @author 何波 @Description: 处理全场优惠价格设置 @param cateIds @param itemIds @return List<ItemInfo> @throws
	 */
	@Override
	public List<ItemInfo> findCallItemInfos(List<Long> cateIds,List<Long> itemIds){
		return itemInfoDao.findCallItemInfos(cateIds, itemIds);
	}

	@Override
	public List<ItemInfo> findItemInfosBySkuids(List<Long> skuIds){
		if (CollectionUtils.isEmpty(skuIds)){
			return null;
		}
		return itemInfoDao.findItemInfosBySkuids(skuIds);
	}

	@Override
	public List<Item> findItemListByCodes(List<String> itemCodes){
		return itemDao.findItemListByCodes(itemCodes, null);
	}

	@Override
	public List<ItemCommand> findItemCommandListByIds(List<Long> itemIds){
		return itemDao.findItemCommandListByIds(itemIds);
	}

	@Override
	public void removeItemImageByItemId(Long itemId){
		List<Long> itemIds = new ArrayList<Long>();
		itemIds.add(itemId);
		itemImageDao.removeItemImageByItemIds(itemIds);
	}

	/**
	 * @author 何波 @Description: @param xls @param shopId void @throws
	 */
	private void addExcelDynamicType(HSSFWorkbook xls,Property property,int cell,boolean validate){
		Long propertyId = property.getId();
		List<PropertyValue> ps = propertyValueDao.findPropertyValueListById(propertyId);
		if (ps == null || ps.size() == 0){
			return;
		}
		List<String> valueList = new ArrayList<String>();
		for (PropertyValue pv : ps){
			valueList.add(pv.getValue());
		}
		Sheet sheet = xls.getSheetAt(0);
		// 添加商品类型选择
		constructListCell(sheet, valueList, 7, 200, cell, validate);
	}

	/** 构建属性值下拉 */
	private void constructListCell(Sheet sheet,List<String> valueList,int row,int lastRow,int startCol,boolean validate){
		if (valueList == null){
			return;
		}
		DataValidationConstraint dvConstraint = this.dealDataValidationConstraint(sheet, valueList, startCol);

		CellRangeAddressList addressList = new CellRangeAddressList(row, lastRow, startCol, startCol);

		DataValidation validation = sheet.getDataValidationHelper().createValidation(dvConstraint, addressList);
		validation.setShowErrorBox(validate);
		sheet.addValidationData(validation);
	}

	private DataValidationConstraint dealDataValidationConstraint(Sheet sheet,List<String> valueList,int startCol){
		DataValidationConstraint dc = null;
		if (valueList.size() <= MAX_PROP_VALUE){
			dc = sheet.getDataValidationHelper().createExplicitListConstraint(valueList.toArray(new String[valueList.size()]));
		}else{
			String formula = this.dealOverMaxDataValidation(sheet.getWorkbook(), valueList, startCol);
			dc = sheet.getDataValidationHelper().createFormulaListConstraint(formula);
		}
		return dc;
	}

	private String dealOverMaxDataValidation(Workbook wb,List<String> valueList,int startCol){
		Sheet s3 = wb.getSheet(VALUES_SHEET);
		Row row = null;
		for (int i = 0; i < valueList.size(); i++){
			String value = valueList.get(i);
			row = s3.getRow(i);
			if (row == null){
				row = s3.createRow(i);
			}
			row.createCell(startCol, Cell.CELL_TYPE_STRING).setCellValue(value);
		}
		String colStr = CellReference.convertNumToColString(startCol);
		return String.format("%s!$%s$1:$%s$%d", VALUES_SHEET, colStr, colStr, valueList.size());
	}

	@Override
	public ItemInfoCommand findItemInfoCommandByItemId(Long itemId){
		ItemInfo itemInfo = findItemInfoByItemId(itemId);
		ItemInfoCommand command = new ItemInfoCommand();
		LangProperty.I18nPropertyCopyToSource(itemInfo, command);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			List<Long> ids = new ArrayList<Long>();
			ids.add(itemInfo.getId());
			List<ItemInfoLang> infoLangs = itemInfoDao.findItemInfoLangList(ids, MutlLang.i18nLangs());
			String[] titles = new String[MutlLang.i18nSize()];

			String[] subTiltes = new String[MutlLang.i18nSize()];

			String[] seoTitles = new String[MutlLang.i18nSize()];

			String[] seoKeywordss = new String[MutlLang.i18nSize()];

			String[] seoDescriptions = new String[MutlLang.i18nSize()];

			String[] sketchs = new String[MutlLang.i18nSize()];

			String[] descriptions = new String[MutlLang.i18nSize()];

			String[] langs = new String[MutlLang.i18nSize()];
			if (Validator.isNotNullOrEmpty(infoLangs)){
				for (int i = 0; i < infoLangs.size(); i++){
					ItemInfoLang lang = infoLangs.get(i);
					titles[i] = lang.getTitle();
					subTiltes[i] = lang.getSubTitle();
					seoTitles[i] = lang.getSeoTitle();
					seoKeywordss[i] = lang.getSeoKeywords();
					seoDescriptions[i] = lang.getSeoDescription();
					sketchs[i] = lang.getSketch();
					descriptions[i] = lang.getDescription();

					langs[i] = lang.getLang();
				}
			}
			MutlLang title = new MutlLang();
			title.setLangs(langs);
			title.setValues(titles);
			command.setTitle(title);

			MutlLang subTilte = new MutlLang();
			subTilte.setValues(subTiltes);
			subTilte.setLangs(langs);
			command.setSubTitle(subTilte);

			MutlLang seoTitle = new MutlLang();
			seoTitle.setValues(seoTitles);
			seoTitle.setLangs(langs);
			command.setSeoTitle(seoTitle);

			MutlLang seoKeywords = new MutlLang();
			seoKeywords.setValues(seoKeywordss);
			seoKeywords.setLangs(langs);
			command.setSeoKeywords(seoKeywords);

			MutlLang seoDescription = new MutlLang();
			seoDescription.setValues(seoDescriptions);
			seoDescription.setLangs(langs);
			command.setSeoDescription(seoDescription);

			MutlLang sketch = new MutlLang();
			sketch.setValues(sketchs);
			sketch.setLangs(langs);
			command.setSketch(sketch);

			MutlLang description = new MutlLang();
			description.setValues(descriptions);
			description.setLangs(langs);
			command.setDescription(description);

		}else{
			SingleLang title = new SingleLang();
			title.setValue(itemInfo.getTitle());
			command.setTitle(title);

			SingleLang subTilte = new SingleLang();
			subTilte.setValue(itemInfo.getSubTitle());
			command.setSubTitle(subTilte);

			SingleLang seoTitle = new SingleLang();
			seoTitle.setValue(itemInfo.getSeoTitle());
			command.setSeoTitle(seoTitle);

			SingleLang seoKeywords = new SingleLang();
			seoKeywords.setValue(itemInfo.getSeoKeywords());
			command.setSeoKeywords(seoKeywords);

			SingleLang seoDescription = new SingleLang();
			seoDescription.setValue(itemInfo.getSeoDescription());
			command.setSeoDescription(seoDescription);

			SingleLang sketch = new SingleLang();
			sketch.setValue(itemInfo.getSketch());
			command.setSketch(sketch);

			SingleLang description = new SingleLang();
			description.setValue(itemInfo.getDescription());
			command.setDescription(description);
		}
		return command;
	}

	@Override
	public List<com.baozun.nebula.command.product.ItemPropertiesCommand> findItemPropertiesCommandListyByItemId(Long itemId){
		List<ItemProperties> itemProperties = findItemPropertiesListyByItemId(itemId);

		List<com.baozun.nebula.command.product.ItemPropertiesCommand> pvcs = new ArrayList<com.baozun.nebula.command.product.ItemPropertiesCommand>();

		if (Validator.isNullOrEmpty(itemProperties)){
			return pvcs;
		}

		List<Long> ids = new ArrayList<Long>();
		for (ItemProperties propertyValue : itemProperties){
			com.baozun.nebula.command.product.ItemPropertiesCommand pvc = new com.baozun.nebula.command.product.ItemPropertiesCommand();
			LangProperty.I18nPropertyCopyToSource(propertyValue, pvc);
			Long pvId = propertyValue.getId();
			ids.add(pvId);
			pvcs.add(pvc);
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			List<ItemPropertiesLang> propertyLangs = itemPropertiesDao.findItemPropertiesLangByIds(ids, MutlLang.i18nLangs());
			if (Validator.isNullOrEmpty(propertyLangs)){
				return pvcs;
			}
			Map<Long, List<ItemPropertiesLang>> map = new HashMap<Long, List<ItemPropertiesLang>>();
			for (ItemPropertiesLang propertyLang : propertyLangs){
				Long pid = propertyLang.getItemPropertiesId();
				if (map.containsKey(pid)){
					map.get(pid).add(propertyLang);
				}else{
					List<ItemPropertiesLang> pls = new ArrayList<ItemPropertiesLang>();
					pls.add(propertyLang);
					map.put(pid, pls);
				}
			}
			for (int i = 0; i < pvcs.size(); i++){
				com.baozun.nebula.command.product.ItemPropertiesCommand pvc = pvcs.get(i);
				Long propertyValueId = pvc.getPropertyValueId();
				if (propertyValueId != null){
					continue;
				}
				Long pvId = pvc.getId();
				List<ItemPropertiesLang> pls = map.get(pvId);
				if (Validator.isNullOrEmpty(pls)){
					continue;
				}
				// 名称
				String[] values = new String[MutlLang.i18nSize()];
				String[] langs = new String[MutlLang.i18nSize()];
				for (int j = 0; j < pls.size(); j++){
					ItemPropertiesLang propertyLang = pls.get(j);
					values[j] = propertyLang.getPropertyValue();
					langs[j] = propertyLang.getLang();
				}
				MutlLang mutlLang = new MutlLang();
				mutlLang.setValues(values);
				mutlLang.setLangs(langs);
				pvc.setPropertyValue(mutlLang);
			}

		}else{
			for (int i = 0; i < itemProperties.size(); i++){
				ItemProperties property = itemProperties.get(i);
				com.baozun.nebula.command.product.ItemPropertiesCommand pvc = pvcs.get(i);
				Long propertyValueId = pvc.getPropertyValueId();
				if (propertyValueId != null){
					continue;
				}
				// 名称
				SingleLang singleLang = new SingleLang();
				singleLang.setValue(property.getPropertyValue());
				pvc.setPropertyValue(singleLang);
			}
		}

		return pvcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> importItemFromFileI18n(InputStream is,Long shopId) throws BusinessException{
		BusinessException topE = null, currE = null;
		/** 字节流存到内存方便多次读取 **/
		InputStreamCacher cacher = null;
		try{
			cacher = new InputStreamCacher(is);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				is.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		ExcelReader itemUpload = excelFactory.createExcelReader("itemUpload");
		ExcelReader skuUpload = excelFactory.createExcelReader("skuUpload");
		Map<String, String> notSalePropIdMap = new LinkedHashMap<String, String>();
		Map<String, Object> itemBeans = new HashMap<String, Object>();
		itemBeans.put("propIds", notSalePropIdMap);
		ReadStatus rs = itemUpload.readSheet(cacher.getInputStream(), 0, itemBeans);

		/***
		 * 根据读取的数据 1：判断店铺是否一致 2：根据行业 查出该行业所对应的属性 3. 设置映射关系 4：根据属性对比excel属性是否一致
		 */
		/** 店铺ID检查 **/
		checkShopId(itemBeans, shopId);
		Long industryId = (Long) itemBeans.get("industryId");
		if (null == industryId){
			throw new BusinessException(ErrorCodes.INDUSTRY_NOT_MATCH_ERROR);
		}
		// 查询这个开关配置（开关的作用是控制批量新建导入模板字段内容是否排序）
		String pdUploadFieldSortFlag = sdkMataInfoManager.findValue(MataInfo.PD_UPLOAD_TEMPLATE_FIELD_SORT_BY_ID);
		Sort[] sorts = null;
		if ("true".equalsIgnoreCase(pdUploadFieldSortFlag)){
			sorts = new Sort[1];
			sorts[0] = new Sort("p.id", "asc");
		}

		// List<Property> propertyList = shopDao.findPropertyListByIndustryIdAndShopId(industryId, shopId, sorts);
		List<Property> propertyList = shopDao.findPropertyListByIndustryId(industryId, sorts);
		// 拆分成 销售属性List 非销售属性
		List<Long> notSalePropIdList = new ArrayList<Long>();
		List<Long> notSalePropIdI18nList = new ArrayList<Long>();
		List<Long> salePropIdList = new ArrayList<Long>();
		Map<Long, Boolean> isReqMap = new HashMap<Long, Boolean>();
		if (Validator.isNotNullOrEmpty(propertyList)){
			for (Property property : propertyList){
				if (property.getIsSaleProp()){
					salePropIdList.add(property.getId());
				}else{
					notSalePropIdList.add(property.getId());
					if (property.getEditingType().equals(1)){
						notSalePropIdI18nList.add(property.getId());
					}
				}
				isReqMap.put(property.getId(), property.getRequired());
			}
		}

		/** 定义动态属性excel与实体类的关系 **/
		/*** sheet1 ***/
		itemCommSheetDefinition(itemUpload, notSalePropIdList, isReqMap);

		/*** sheet2 ***/
		skuCommSheetDefiniton(skuUpload, salePropIdList, isReqMap);

		/****
		 * 再次读取 重新赋值
		 ****/
		notSalePropIdMap = new LinkedHashMap<String, String>();
		itemBeans = new HashMap<String, Object>();
		itemBeans.put("propIds", notSalePropIdMap);
		rs = itemUpload.readSheet(cacher.getInputStream(), 0, itemBeans);

		Map<String, String> salePropIdMap = new LinkedHashMap<String, String>();
		Map<String, Object> skuBeans = new HashMap<String, Object>();
		skuBeans.put("propIds", salePropIdMap);
		ReadStatus rs2 = skuUpload.readSheet(cacher.getInputStream(), 1, skuBeans);
		/**
		 * 检验属性列是否匹配
		 */
		propMatching(notSalePropIdMap, notSalePropIdList);

		propMatching(salePropIdMap, salePropIdList);

		/*** 2：判断excel必填项 */
		if (rs.getStatus() != ReadStatus.STATUS_SUCCESS){
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList){
				BusinessException e = new BusinessException(message);
				if (topE == null){
					topE = e; // b-101 : Cell{}错误, new
					currE = e;
				}else{
					currE.setLinkedException(e);
					currE = e;
				}
			}
		}
		if (rs2.getStatus() != ReadStatus.STATUS_SUCCESS){
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs2, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList){
				BusinessException e = new BusinessException(message);
				if (topE == null){
					topE = e; // b-101 : Cell{}错误, new
					currE = e;
				}else{
					currE.setLinkedException(e);
					currE = e;
				}
			}
		}
		// 读取国际化信息
		boolean i18n = LangProperty.getI18nOnOff();
		Map<String, List<ItemInfoExcelCommand>> allI18nItemInfos = new HashMap<String, List<ItemInfoExcelCommand>>();
		if (i18n){
			// 商品国际化
			ExcelReader itemInfoI18n = excelFactory.createExcelReader("itemInfoI18n");
			skuCommSheetDefinitonI18n(itemInfoI18n, notSalePropIdI18nList, isReqMap);
			// 读取商品基本动态属性值
			List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
			int num = 0;
			for (int i = 1; i < i18nLangs.size(); i++){
				String value = i18nLangs.get(i).getValue();
				String key = i18nLangs.get(i).getKey();
				if (key.equals(MutlLang.defaultLang())){
					continue;
				}
				num++;
				Map<String, Object> itemInfoExcelCommand = new HashMap<String, Object>();
				ReadStatus readStatus = itemInfoI18n.readSheet(cacher.getInputStream(), 2 + num, itemInfoExcelCommand);
				if (readStatus.getStatus() != ReadStatus.STATUS_SUCCESS){
					List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(readStatus, Locale.SIMPLIFIED_CHINESE);
					for (String message : messageList){
						BusinessException e = new BusinessException(message);
						if (topE == null){
							topE = e; // b-101 : Cell{}错误, new
							currE = e;
						}else{
							currE.setLinkedException(e);
							currE = e;
						}
					}
				}
				List<ItemInfoExcelCommand> list = (List<ItemInfoExcelCommand>) itemInfoExcelCommand.get("itemInfoI18n");
				allI18nItemInfos.put(value, list);

			}
		}

		if (topE != null){
			throw topE;
		}
		return dataValidateAndSaveI18n(
				propertyList,
				itemBeans,
				skuBeans,
				shopId,
				industryId,
				notSalePropIdList,
				salePropIdList,
				allI18nItemInfos);
	}

	@Override
	public void createOrUpdateItemImageIi18n(ItemImageLangCommand[] itemImages,Long itemId,String baseImageUrl,boolean isImageTypeGroup){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);

		Map<Long, ItemImageLangCommand> newMap = new HashMap<Long, ItemImageLangCommand>();
		List<Long> deleteList = new ArrayList<Long>();

		List<ItemImage> oldImageList = itemImageDao.findItemImageByItemPropAndItemId(paramMap);

		// 1: 遍历从jsp页面传递过来的itemImages数组, 如果
		// Id为空,则表示该itemImage是新增;否则是修改或不处理的(页面上没有修改过的数据)itemImage,
		// 并放入newMap(key:id,value:itemImage对象)中;

		Map<String, Integer> typeItemPropertiesMap = new HashMap<String, Integer>();
		boolean i18n = LangProperty.getI18nOnOff();
		int idx = 0;
		for (ItemImageLangCommand itemImagecCommand : itemImages){
			// 商品图片的排序: 通过type分组, 每组type中的图片的位置都是1,2,3...
			ItemImage itemImage = new ItemImage();
			MutlLang.I18nPropertyCopy(itemImagecCommand, itemImage);
			if (!isImageTypeGroup){
				Integer position = 1;
				String type = itemImage.getType();
				String itemProperties = itemImage.getItemProperties() == null ? "" : String.valueOf(itemImage.getItemProperties());
				if (typeItemPropertiesMap.containsKey(type + itemProperties)){
					position = typeItemPropertiesMap.get(type + itemProperties) + 1;
				}
				typeItemPropertiesMap.put(type + itemProperties, position);
				itemImage.setPosition(position);
				itemImagecCommand.setPosition(position);
			}else{
				itemImagecCommand.setPosition(idx);
				idx++;
			}
			Long id = itemImage.getId();
			if (i18n){
				MutlLang mutlLang = (MutlLang) itemImagecCommand.getPicUrl();
				MutlLang mdescs = (MutlLang) itemImagecCommand.getDescription();
				String[] descs = mdescs.getValues();
				String[] values = mutlLang.getValues();
				String[] langs = mutlLang.getLangs();
				String[] picUrls = replacePicUrl(values, baseImageUrl);
				mutlLang.setValues(picUrls);
				String dv = mutlLang.getDefaultValue();
				String descDv = mdescs.getDefaultValue();
				if (null == id){
					if (StringUtils.isNotBlank(dv)){
						itemImage.setCreateTime(new Date());
						itemImage.setVersion(new Date());
						itemImage.setModifyTime(new Date());
						itemImage.setItemId(itemId);
						itemImage.setPicUrl(dv);
						itemImage.setDescription(descDv);
						itemImage = itemImageDao.save(itemImage);
						id = itemImage.getId();
						for (int i = 0; i < picUrls.length; i++){
							String val = picUrls[i];
							String lang = langs[i];
							String desc = descs[i];
							ItemImageLang itemImageLang = new ItemImageLang();
							itemImageLang.setItemImageId(id);
							itemImageLang.setLang(lang);
							itemImageLang.setPicUrl(val);
							itemImageLang.setDescription(desc);
							itemImageLangDao.save(itemImageLang);
						}

					}
				}else{
					newMap.put(itemImage.getId(), itemImagecCommand);
				}
			}else{
				SingleLang singleLang = (SingleLang) itemImagecCommand.getPicUrl();
				String picUrl = replacePicUrl(singleLang.getValue(), baseImageUrl);
				singleLang.setValue(picUrl);
				SingleLang sdesc = (SingleLang) itemImagecCommand.getDescription();
				String desc = sdesc.getValue();
				if (null == id){
					if (StringUtils.isNotBlank(picUrl)){
						itemImage.setCreateTime(new Date());
						itemImage.setVersion(new Date());
						itemImage.setModifyTime(new Date());
						itemImage.setItemId(itemId);
						itemImage.setPicUrl(picUrl);
						itemImage.setDescription(desc);
						itemImageDao.save(itemImage);
					}
				}else{
					newMap.put(itemImage.getId(), itemImagecCommand);
				}
			}

		}
		// 2: 遍历DB中的数据, 通过Id去newMap中取itemIamge对象, 如果itemImage为null时,
		// 则该数据是被删除的数据,将id加入到deleteList中; 否则判断数据是否需要修改还是不处理
		// 3: 判断数据是否需要修改还是不处理: 通过itemImage中的description(描述),
		// type(类型:列表页,内容页,两者都), position(位置),
		// picUrl(图片路径)来判断该itemImage是否要修改
		for (ItemImage oldImage : oldImageList){
			Long oldId = oldImage.getId();
			ItemImageLangCommand newImage = newMap.get(oldId);
			if (newImage == null){
				deleteList.add(oldId);
			}else{
				// 判断数据是否需要修改还是不处理的数据
				// lxy 改成数据一样不一样都修改。
				// if (!diffImage(oldImage, newImage, baseImageUrl)) {
				if (i18n){
					MutlLang mutlLang = (MutlLang) newImage.getPicUrl();
					MutlLang mdescs = (MutlLang) newImage.getDescription();
					String[] descs = mdescs.getValues();
					String[] values = mutlLang.getValues();
					String[] langs = mutlLang.getLangs();
					String dv = mutlLang.getDefaultValue();
					String descDv = mdescs.getDefaultValue();
					Long id = newImage.getId();
					itemImageDao.updateItemImageById(
							newImage.getType(),
							replacePicUrl(dv, baseImageUrl),
							newImage.getPosition(),
							descDv,
							id);
					for (int i = 0; i < values.length; i++){
						String val = values[i];
						String lang = langs[i];
						String desc = descs[i];
						ItemImageLang db = itemImageDao.findItemImageLang(id, lang);
						if (db == null){
							ItemImageLang itemImageLang = new ItemImageLang();
							itemImageLang.setItemImageId(id);
							itemImageLang.setLang(lang);
							itemImageLang.setPicUrl(val);
							itemImageLang.setDescription(desc);
							itemImageLangDao.save(itemImageLang);
						}else{
							itemImageDao.updateItemImageLang(val, desc, lang, id);
						}
					}
				}else{
					SingleLang singleLang = (SingleLang) newImage.getPicUrl();
					String picUrl = replacePicUrl(singleLang.getValue(), baseImageUrl);
					SingleLang sdesc = (SingleLang) newImage.getDescription();
					String desc = sdesc.getValue();

					String rpicUrl = replacePicUrl(picUrl, baseImageUrl);

					itemImageDao.updateItemImageById(newImage.getType(), rpicUrl, newImage.getPosition(), desc, newImage.getId());
				}
				// }
			}
		}
		// 通过Ids删除图片信息
		if (deleteList != null && deleteList.size() > 0){
			itemImageDao.removeItemImageByIds(deleteList);
			itemImageDao.removeItemImageLangByIds(deleteList);
		}

	}

	@Override
	public List<ItemImageLangCommand> findItemImageByItemPropAndItemIdI18n(String itemProperties,Long itemId,Long propertyValueId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);
		if (StringUtils.isNotBlank(itemProperties)){
			paramMap.put("itemProperties", itemProperties);
		}
		// 多选
		if (propertyValueId != null && StringUtils.isNotBlank(String.valueOf(propertyValueId))){
			List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertiesByItemId(itemId);
			for (ItemProperties itemProp : itemPropertiesList){
				if (propertyValueId.equals(itemProp.getPropertyValueId())){
					paramMap.put("itemProperties", String.valueOf(itemProp.getId()));
				}
			}
		}
		List<ItemImage> itemImages = itemImageDao.findItemImageByItemPropAndItemId(paramMap);
		List<ItemImageLangCommand> commands = new ArrayList<ItemImageLangCommand>();
		if (itemImages == null || itemImages.size() == 0){
			return null;
		}
		List<Long> ids = new ArrayList<Long>();
		Map<Long, ItemImage> iiMap = new HashMap<Long, ItemImage>();
		for (int i = 0; i < itemImages.size(); i++){
			ItemImage ii = itemImages.get(i);
			Long id = ii.getId();
			ids.add(id);
			ItemImageLangCommand command = new ItemImageLangCommand();
			LangProperty.I18nPropertyCopyToSource(ii, command);
			commands.add(command);
			iiMap.put(id, ii);
		}
		List<ItemImageLang> itemImageLangs = itemImageDao.findItemImageLangList(ids, MutlLang.i18nLangs());
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n && (itemImageLangs == null || itemImageLangs.size() == 0)){
			return commands;
		}
		Map<Long, List<ItemImageLang>> maps = new HashMap<Long, List<ItemImageLang>>();
		for (int i = 0; i < itemImageLangs.size(); i++){
			ItemImageLang iil = itemImageLangs.get(i);
			Long itemImageId = iil.getItemImageId();
			if (maps.containsKey(itemImageId)){
				maps.get(itemImageId).add(iil);
			}else{
				List<ItemImageLang> list = new ArrayList<ItemImageLang>();
				list.add(iil);
				maps.put(itemImageId, list);
			}
		}

		for (ItemImageLangCommand command : commands){
			Long id = command.getId();

			if (i18n){
				List<ItemImageLang> list = maps.get(id);
				if (list == null || list.size() == 0){
					continue;
				}
				MutlLang url = new MutlLang();
				MutlLang desc = new MutlLang();
				String[] urlvals = new String[MutlLang.i18nSize()];
				String[] descVals = new String[MutlLang.i18nSize()];
				String[] langs = new String[MutlLang.i18nSize()];
				for (int i = 0; i < list.size(); i++){
					ItemImageLang iil = list.get(i);
					urlvals[i] = iil.getPicUrl();
					descVals[i] = iil.getDescription();
					langs[i] = iil.getLang();
				}
				url.setValues(urlvals);
				url.setLangs(langs);
				desc.setValues(descVals);
				desc.setLangs(langs);
				command.setPicUrl(url);
				command.setDescription(desc);
			}else{
				ItemImage ii = iiMap.get(id);
				SingleLang url = new SingleLang();
				SingleLang desc = new SingleLang();
				String picUrl = ii.getPicUrl();
				String descVal = ii.getDescription();
				url.setValue(picUrl);
				;
				desc.setValue(descVal);
				command.setPicUrl(url);
				command.setDescription(desc);
			}
		}
		return commands;
	}

	@Override
	public List<ItemCommand> findItemCommandByQueryMap(Map<String, Object> paramMap,List<String> itemCodeList){
		return itemDao.findItemCommandByQueryMapAndItemCodes(paramMap, itemCodeList);
	}

	@Override
	public List<ItemCommand> findItemCommandByQueryMapAndItemCodesI18n(Map<String, Object> paramMap,List<String> itemCodeList,String langKey){
		return itemDao.findItemCommandByQueryMapAndItemCodesI18n(paramMap, itemCodeList, langKey);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer findItemCountByPropertyId(Long propertyId){
		Integer count = itemPropertiesDao.findItemCountByPropertyId(propertyId);
		return count == null ? 0 : count;
	}
	
}
