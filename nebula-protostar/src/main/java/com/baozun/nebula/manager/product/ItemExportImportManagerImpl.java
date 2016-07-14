package com.baozun.nebula.manager.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.ImpItemCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemInfoLangDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemPropertiesLangDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.extend.ItemExtendManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.feilong.core.Validator;

import loxia.dao.Sort;
import loxia.support.excel.ExcelKit;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ReadStatus;
import loxia.support.excel.definition.ExcelBlock;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.definition.ExcelSheet;

/**
 * 商品的导出和导入Manager实现类
 * 
 * @author chenguang.zhou 2015年5月29日
 */
@Transactional
@Service("itemExportImportManager")
public class ItemExportImportManagerImpl implements ItemExportImportManager {

	private final Logger			log								= LoggerFactory
																			.getLogger(ItemExportImportManagerImpl.class);

	@Autowired
	private ItemManager				itemManager;

	@Autowired
	private ShopManager				shopManager;

	@Autowired
	private IndustryManager			industryManager;

	@Autowired
	private ItemPropertiesDao		itemPropertiesDao;

	@Autowired
	private ItemCategoryDao			itemCategoryDao;

	@Autowired
	private CategoryDao				categoryDao;

	@Autowired
	private ItemDao					itemDao;

	@Autowired
	private ItemInfoDao				itemInfoDao;

	@Autowired
	private PropertyValueDao		propertyValueDao;

	@Autowired
	private ItemSolrManager			itemSolrManager;

	@Autowired
	private SdkMataInfoManager		sdkMataInfoManager;

	@Autowired
	private ExcelManipulatorFactory	excelFactory;
	
	@Autowired
	private SdkI18nLangManager		sdkI18nLangManager;

	@Autowired
	private ItemInfoLangDao			itemInfoLangDao;

	@Autowired
	private ItemPropertiesLangDao	itemPropertiesLangDao;

	@Autowired(required = false)
	private ItemExtendManager		itemExtendManager;
	
	@Autowired
	private CategoryManager         categoryManager;

	// 用于属性多个
	private static final String		DOUBLE_SLASH_SEPARATOR			= "\\|\\|";

	private static final String		EXPORT_MULTI_SELECT_SEPARATOR	= "||";

	private static final String		NUMBER_REGEX					= "^[1-9][0-9]*$";

	// 分类 筛选条件
	private static final String		BACK_SLANT_SEPARATOR			= "/";

	private static final Integer	ITEM_EXPORT_OUT_SIZE			= 100;
	// ITEM相关的sheet
	private static final Integer	XLS_SHEET_1						= 0;
	// 属性code行
	//private static final Integer	PROPERTY_CODE_ROW				= 5;
	// 属性名称行
	private static final Integer	PROPERTY_NAME_ROW				= 6;
	// 是否必填行
	//private static final Integer	IS_REQUIRED_ROW					= 7;
	// 开始写商品数据的行数(从0开始计算)
	private static final Integer	ITEM_DATA_ROW					= 8;
	
	// 国际化sheet的标题行数
	private static final Integer	LANGUAGE_TITLE_ROW				= 2;
	
	

	private final String			MAP_KEY_BASE					= "base";

	private final String			MAP_KEY_COMMON					= "common";

	private final String			CATEGORY_CODE_COLUMN			= "categoryCodes";

	private final String			ITEM_STYLE_COLUMN				= "itemStyle";

	private final String			ITEM_TYPE_COLUMN				= "itemType";

	private final String			KEY_HAS_STYLE					= "HAS_STYLE";
	
	private final String			DEFAULT_LANG					= "default";

	// 必须填写的字段
	private static String[]			REQUIRED_COLUMN					= new String[] { "code", "title", "salePrice", "itemType", ""};

	private static String[]			BIGDECIMAL_COLUMN				= new String[] { "salePrice", "listPrice" };
	
	/** 需要国际化的字段 **/
	private static final String[]	I18N_COLUMN_ARRAY				= new String[] { "code", "title", "description", "sketch", "seoTitle", "seoKeyWords", "seoDesc", "subTitle"};
	@Override
	public HSSFWorkbook itemExport(Long shopId, Long industryId, String[] selectProperties, String itemCodes,
			File excelFile) throws IOException {
		// excel文件模板不存在
		if (excelFile == null || !excelFile.isFile()) {
			log.error("excel file not must be null.");
			throw new BusinessException(ErrorCodes.EXCEL_TEMPLATE_FILE_NOT_EXISTS);
		}

		// 没有选择导出的字段
		if (selectProperties == null || selectProperties.length == 0) {
			log.error("export column not must be null.");
			throw new BusinessException(ErrorCodes.EXCEL_TEMPLATE_FILE_NOT_EXISTS);
		}
		List<String> itemCodeList = null;
		if (StringUtils.isNotBlank(itemCodes)) {
			itemCodeList = Arrays.asList(StringUtils.split(itemCodes));
			if (itemCodeList != null && !itemCodeList.isEmpty() && itemCodeList.size() > ITEM_EXPORT_OUT_SIZE) {
				throw new BusinessException(ErrorCodes.ITEM_EXPORT_ITEM_CODE_OUT_SIZE,
						new Object[] { ITEM_EXPORT_OUT_SIZE });
			} 
		}

		// 店铺信息
		ShopCommand shopCommand = shopManager.findShopById(shopId);
		// 行业信息
		Industry industry = industryManager.findIndustryById(industryId);
		// 行业属性
		//List<Property> propertyList = shopManager.findPropertyListByIndustryIdAndShopId(industry.getId(), shopCommand.getShopid(), null);
		Sort[] sorts = new Sort[1];
		sorts[0] = new Sort("p.id", "asc");
		List<Property> propertyList = shopManager.findPropertyListByIndustryId(industry.getId(),sorts);

		// 销售属性集合 现在不可以修改销售属性,以后添加
		// List<Property> salesPropertyList = new ArrayList<Property>();
		// 一般属性集合
		List<Property> commonPropertyList = new ArrayList<Property>();
		/** key: property id, value: property name */
		Map<Long, Property> comomPropertyMap = new HashMap<Long, Property>();
		Map<Long, Boolean> isRequiredMap = new HashMap<Long, Boolean>();
		if (propertyList != null && !propertyList.isEmpty()) {
			for (Property property : propertyList) {
				if (property.getIsColorProp() || property.getIsSaleProp()) {
					// salesPropertyList.add(property);
				} else {
					commonPropertyList.add(property);
					comomPropertyMap.put(property.getId(), property);
				}
				isRequiredMap.put(property.getId(), property.getRequired());
			}
		}
		
		// 国际化
		List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();

		// 生成excel模板文件
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(excelFile);
			HSSFWorkbook xls = new HSSFWorkbook(inputStream);

			// 保存商品的基本信息, 默认语言数据对应的Sheet
			generateExcelSheetByProperty(xls, comomPropertyMap, industry, shopCommand,
					xls.getSheetAt(XLS_SHEET_1), selectProperties, isRequiredMap);
			
			// 将数据写到excel模板
			if (itemCodeList != null && !itemCodeList.isEmpty()) {
				// 写入商品数据
				generateExportItemData(xls.getSheetAt(XLS_SHEET_1), industry, shopCommand, selectProperties, itemCodeList);
			}
			
			// 生成其它语言的Sheet和商品数据
			if(LangProperty.getI18nOnOff()){
				for(int i = 0, length = i18nLangs.size(); i < length; i++){
					I18nLang i18nLang = i18nLangs.get(i);
					Integer defalutLangInt = i18nLang.getDefaultlang();
					if(defalutLangInt != null && I18nLang.DEFAULT_STATUS.equals(defalutLangInt)){
						continue;
					}
					HSSFSheet sheet = xls.createSheet(i18nLang.getValue());
					// 语言的Sheet
					generateExcelSheetByPropertyI18n(xls, commonPropertyList, comomPropertyMap, industry, shopCommand, sheet, selectProperties, isRequiredMap, i18nLang);
				
					// 将数据写到excel模板
					if (itemCodeList != null && !itemCodeList.isEmpty()) {
						// 写入商品数据
						generateExportItemDataI18n(sheet, industry, shopCommand, selectProperties, itemCodeList, comomPropertyMap, i18nLang);
					}
				}
			}

			return xls;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return null;
	}

	/**
	 * 生成其它语言的Sheet模块
	 * @param xls
	 * @param commonPropertyList
	 * @param comomPropertyMap
	 * @param industry
	 * @param shopCommand
	 * @param sheetAt
	 * @param selectProperties
	 * @param isRequiredMap
	 * @param i18nLang
	 */
	private void generateExcelSheetByPropertyI18n(HSSFWorkbook xls, List<Property> commonPropertyList,
			Map<Long, Property> commonPropertyMap, Industry industry, ShopCommand shopCommand, HSSFSheet sheet,
			String[] selectProperties, Map<Long, Boolean> isRequiredMap, I18nLang i18nLang) {
		// set language key
		HSSFRow languageRow = sheet.createRow(0);
		languageRow.setHeight((short)0);
		languageRow.createCell(0).setCellValue(i18nLang.getKey());
		// 生成标题行数据
		generateTitleRow(xls, commonPropertyMap, sheet, selectProperties, isRequiredMap, LANGUAGE_TITLE_ROW, false);
	}

	/**
	 * 获得选中的列是商品属性的列
	 * 
	 * @param selectProperties
	 * @return
	 */
	private List<Long> getSelectedPropertyCode(String[] selectProperties) {
		List<Long> propertyIdList = new ArrayList<Long>();
		for (String propertyCode : selectProperties) {
			if (propertyCode.matches(NUMBER_REGEX)) {
				propertyIdList.add(Long.valueOf(propertyCode));
			}
		}
		return propertyIdList;
	}

	/**
	 * 生成excel文件中的Sheet
	 * 
	 * @param xls
	 * @param propertyList
	 * @param industry
	 * @param shopCommand
	 * @param sheet
	 * @param selectProperties
	 */
	private void generateExcelSheetByProperty(HSSFWorkbook xls, Map<Long, Property> commonProperyMap, Industry industry, 
			ShopCommand shopCommand, HSSFSheet sheet, String[] selectProperties, Map<Long, Boolean> isRequiredMap) {
		// 设置店铺信息和行业信息
		setExcelSheetHeader(industry, shopCommand, sheet);

		generateTitleRow(xls, commonProperyMap, sheet, selectProperties, isRequiredMap, PROPERTY_NAME_ROW, true);
	}

	/**
	 * 生成标题行, code行, 是否必填行
	 * @param xls
	 * @param comomProperyMap	
	 * @param sheet
	 * @param selectProperties
	 * @param isRequiredMap
	 */
	private void generateTitleRow(HSSFWorkbook xls, Map<Long, Property> commonProperyMap, HSSFSheet sheet,
			String[] selectProperties, Map<Long, Boolean> isRequiredMap, Integer rowNum, Boolean isDefaultLang) {
		HSSFCellStyle cellStyle = createCellStyle(xls);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short)400);
		// 先创建行, 再写列的数据和样式
		sheet.createRow(rowNum - 1).setHeight((short)0);;// 隐藏code行, 并设置高度
		sheet.createRow(rowNum);// 标题行
		sheet.createRow(rowNum + 1);// 是否必填行, 并设置高度
		// 生成选中的属性
		int columnNum = 0;
		for (int i = 0, len = selectProperties.length; i < len; i++) {
			String propertyCode = selectProperties[i];
			
			// 不是默认语言, 该字段不需要国际化, 且不是商品属性
			if(!isDefaultLang && !checkIsI18nColumn(propertyCode) && !propertyCode.matches(NUMBER_REGEX)){
				continue;
			}
			
			if(!isDefaultLang && propertyCode.matches(NUMBER_REGEX)){
				Property property = commonProperyMap.get(Long.valueOf(propertyCode));
				// 单选与多选 这里不国际化
				if(Property.EDITING_TYPE_RADIA.equals(property.getEditingType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType())){
					continue;
				}
			}
			if(log.isDebugEnabled()){
				log.debug("--  title row -- propertyCode[{}], columnNum[{}], rowNum[{}]", propertyCode, columnNum, rowNum);
			}
			sheet.getRow(rowNum - 1).createCell(isDefaultLang?i:columnNum).setCellValue(propertyCode);
			if (converCodeToName(propertyCode) != null && !propertyCode.matches(NUMBER_REGEX)) {
				sheet.getRow(rowNum).createCell(isDefaultLang?i:columnNum).setCellValue(converCodeToName(propertyCode));
			} else {
				sheet.getRow(rowNum).createCell(isDefaultLang?i:columnNum).setCellValue(commonProperyMap.get(Long.valueOf(propertyCode)).getName());
			}
			sheet.getRow(rowNum).getCell(isDefaultLang?i:columnNum).setCellStyle(cellStyle);
			sheet.getRow(rowNum + 1).createCell(isDefaultLang?i:columnNum).setCellValue(getColumnRequired(propertyCode, isRequiredMap));
			
			if(!isDefaultLang && checkIsI18nColumn(propertyCode) && !propertyCode.matches(NUMBER_REGEX)){
				columnNum++;
			}
			
			if(!isDefaultLang && propertyCode.matches(NUMBER_REGEX)){
				Property property = commonProperyMap.get(Long.valueOf(propertyCode));
				// 单选与多选 这里不国际化
				if(!(Property.EDITING_TYPE_RADIA.equals(property.getEditingType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType()))){
					columnNum++;
				}
			}
		}
	}

	/**
	 * 生成导出商品的数据
	 * 
	 * @param sheet
	 * @param industry
	 * @param shopCommand
	 * @param selectProperties
	 * @param itemCodeList
	 */
	private void generateExportItemData(HSSFSheet sheet, Industry industry, ShopCommand shopCommand,
			String[] selectProperties, List<String> itemCodeList) {
		// 根据shopId, industryId, itemCodeList查询商品信息
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopCommand.getShopid());
		paramMap.put("industryId", industry.getId());
		List<ItemCommand> itemCommandList = itemManager.findItemCommandByQueryMap(paramMap, itemCodeList);

		/** key : propertyId-itemId, value : ItemProperties */
		Map<String, ItemPropertiesCommand> itemPropertiesMap = new HashMap<String, ItemPropertiesCommand>();
		List<Long> propertyIdList = getSelectedPropertyCode(selectProperties);
		if (propertyIdList != null && !propertyIdList.isEmpty()) {
			List<ItemPropertiesCommand> itemPropertiesList = itemPropertiesDao.findItemPropertiesByQueryMap(paramMap, propertyIdList, itemCodeList);
			for (ItemPropertiesCommand itemProperties : itemPropertiesList) {
				ItemPropertiesCommand itemPopertiesTmp = null;
				String key = itemProperties.getPropertyId() + "-" + itemProperties.getItemId();
				// 商品属性为自定义多选或多选
				if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(itemProperties.getType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(itemProperties.getType())) {
					itemPopertiesTmp = itemPropertiesMap.get(key);
					if (itemPopertiesTmp != null) {
						itemPopertiesTmp.setPropertyValue(itemPopertiesTmp.getPropertyValue() + EXPORT_MULTI_SELECT_SEPARATOR + itemProperties.getPropertyValue());
						itemPropertiesMap.put(key, itemPopertiesTmp);
					} else {
						itemPropertiesMap.put(key, itemProperties);
					}
				}
				else {
					itemPropertiesMap.put(key, itemProperties);
				}
			}
		}

		// 选中了商品分类列时
		/** key:itemId, value:categoryCode/categoryCode/categoryCode */
		Map<Long, String> itemCategoryMap = new HashMap<Long, String>();
		if (Arrays.asList(selectProperties).contains(CATEGORY_CODE_COLUMN)) {
			List<ItemCategoryCommand> itemCategoryCommandList = itemCategoryDao.findItemCategoryCommandByQueryMap(
					itemCodeList, paramMap);
			if (itemCategoryCommandList != null) {
				Long itemId = null;
				String categoryStr = null;
				
				for (ItemCategoryCommand itemCategory : itemCategoryCommandList) {
					itemId = itemCategory.getItemId();
					categoryStr = itemCategoryMap.get(itemCategory.getItemId());
					
					if (categoryStr == null) {
						categoryStr = itemCategory.getCategoryCode();
					} else {
						if(itemCategory.getIsDefault() != null && itemCategory.getIsDefault()){
							categoryStr = itemCategory.getCategoryCode() + BACK_SLANT_SEPARATOR + categoryStr;
						}else{
							categoryStr += (BACK_SLANT_SEPARATOR + itemCategory.getCategoryCode());
						}
					}
					itemCategoryMap.put(itemId, categoryStr);
				}
			}
		}

		List<String> selectedProperties = Arrays.asList(selectProperties);
		ItemCommand itemCommand = null;
		for (int i = 0; i < itemCommandList.size(); i++) {
			itemCommand = itemCommandList.get(i);
			HSSFRow row = sheet.getRow(ITEM_DATA_ROW + i);
			if (row == null) {
				row = sheet.createRow(ITEM_DATA_ROW + i);
			}
			HSSFRow codeRow = null;
			for (int j = 0; j < selectedProperties.size(); j++) {
				codeRow = sheet.getRow(5);
				if (codeRow == null) {
					continue;
				}
				String propertyCode = selectedProperties.get(j);//codeRow.getCell(j).getStringCellValue();
				Object value = getColumnDataByPropertyCode(propertyCode, selectedProperties, itemCommand,
						itemCategoryMap);
				if (value == null) {
					if (!propertyCode.matches(NUMBER_REGEX)) {
						value = "";
					} else {
						// 一般属性的值
						ItemPropertiesCommand itemProperties = itemPropertiesMap.get(propertyCode + "-"
								+ itemCommand.getId());
						if (itemProperties != null) {
							value = itemProperties.getPropertyValue();
						} else {
							value = "";
						}
					}
				}
				row.createCell(j).setCellValue(String.valueOf(value));
			}
		}
	}
	
	/**
	 * 生成导出商品的数据 国际化数据
	 * @param sheet
	 * @param industry
	 * @param shopCommand
	 * @param selectProperties
	 * @param itemCodeList
	 * @param comomPropertyMap
	 */
	private void generateExportItemDataI18n(HSSFSheet sheet, Industry industry, ShopCommand shopCommand,
			String[] selectProperties, List<String> itemCodeList, Map<Long, Property> commonPropertyMap, I18nLang i18nLang) {
		// 根据shopId, industryId, itemCodeList查询商品信息
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopCommand.getShopid());
		paramMap.put("industryId", industry.getId());
		List<ItemCommand> itemCommandList = itemManager.findItemCommandByQueryMapAndItemCodesI18n(paramMap, itemCodeList, i18nLang.getKey());
		
		/** key : propertyId-itemId, value : ItemProperties */
		Map<String, ItemPropertiesCommand> itemPropertiesMap = new HashMap<String, ItemPropertiesCommand>();
		List<Long> propertyIdList = getSelectedPropertyCode(selectProperties);
		if (propertyIdList != null && !propertyIdList.isEmpty()) {
			List<ItemPropertiesCommand> itemPropertiesList = itemPropertiesDao.findItemPropertiesByQueryMapI18n(paramMap, propertyIdList, itemCodeList, i18nLang.getKey());
			for (ItemPropertiesCommand itemProperties : itemPropertiesList) {
				ItemPropertiesCommand itemPopertiesTmp = null;
				String key = itemProperties.getPropertyId() + "-" + itemProperties.getItemId();
				// 商品属性为自定义多选或多选
				if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(itemProperties.getType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(itemProperties.getType())) {
					itemPopertiesTmp = itemPropertiesMap.get(key);
					if (itemPopertiesTmp != null) {
						itemPopertiesTmp.setPropertyValue(itemPopertiesTmp.getPropertyValue() + EXPORT_MULTI_SELECT_SEPARATOR + itemProperties.getPropertyValue());
						itemPropertiesMap.put(key, itemPopertiesTmp);
					} else {
						itemPropertiesMap.put(key, itemProperties);
					}
				}
				else {
					itemPropertiesMap.put(key, itemProperties);
				}
			}
		}

		List<String> selectedProperties = Arrays.asList(selectProperties);
		ItemCommand itemCommand = null;
		for (int i = 0; i < itemCommandList.size(); i++) {
			itemCommand = itemCommandList.get(i);
			HSSFRow row = sheet.getRow(4 + i);
			if (row == null) {
				row = sheet.createRow(4 + i);
			}
			HSSFRow codeRow = null;
			int columnNum = 0;
			for (String selectProperty : selectedProperties) {
				// 不需要国际化的字段
				if(!checkIsI18nColumn(selectProperty) && !selectProperty.matches(NUMBER_REGEX)){
					continue;
				}
				
				if(selectProperty.matches(NUMBER_REGEX)){
					Property property = commonPropertyMap.get(Long.valueOf(selectProperty));
					// 单选与多选 这里不国际化
					if(Property.EDITING_TYPE_RADIA.equals(property.getEditingType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType())){
						continue;
					}
				}
				
				
				codeRow = sheet.getRow(1);
				if (codeRow == null) {
					continue;
				}
				String propertyCode = codeRow.getCell(columnNum).getStringCellValue();

				Object value = getColumnDataByPropertyCode(propertyCode, selectedProperties, itemCommand, null);
				if (value == null) {
					if (!propertyCode.matches(NUMBER_REGEX)) {
						value = "";
					} else {
						// 一般属性的值
						ItemPropertiesCommand itemProperties = itemPropertiesMap.get(propertyCode + "-" + itemCommand.getId());
						if (itemProperties != null) {
							value = itemProperties.getPropertyValue();
						} else {
							value = "";
						}
					}
				}
				row.createCell(columnNum).setCellValue(String.valueOf(value));
				if(checkIsI18nColumn(propertyCode) && !selectProperty.matches(NUMBER_REGEX)){
					columnNum++;
				}
				
				if(selectProperty.matches(NUMBER_REGEX)){
					Property property = commonPropertyMap.get(Long.valueOf(selectProperty));
					// 单选与多选 这里不国际化
					if(!(Property.EDITING_TYPE_RADIA.equals(property.getEditingType()) || Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType()))){
						columnNum++;
					}
				}
			}
		}
	}

	/**
	 * 获取列是否必填
	 * 
	 * @param propertyCode
	 * @param isRequiredMap
	 * @return
	 */
	private String getColumnRequired(String propertyCode, Map<Long, Boolean> isRequiredMap) {

		if (propertyCode == null) {
			return "";
		}
		String requiredStr = "必填";
		String notRequiredStr = "非必填";

		String propmt = " 如:赠品或主卖品";

		String value = sdkMataInfoManager.findValue(KEY_HAS_STYLE);
		if (value != null && Boolean.valueOf(value)) {
			REQUIRED_COLUMN[REQUIRED_COLUMN.length] = ITEM_STYLE_COLUMN;
		}

		if (log.isDebugEnabled()) {
			if (propertyCode.matches(NUMBER_REGEX)) {
				if (isRequiredMap.get(propertyCode) != null && isRequiredMap.get(propertyCode)) {
					log.debug("propertyCode[{}] is {}", propertyCode, requiredStr);
				} else {
					log.debug("propertyCode[{}] is {}", propertyCode, notRequiredStr);
				}
			} else {
				if (Arrays.asList(REQUIRED_COLUMN).contains(propertyCode)) {
					log.debug("propertyCode[{}] is {}", propertyCode, requiredStr);
				} else {
					log.debug("propertyCode[{}] is {}", propertyCode, notRequiredStr);
				}
			}
		}

		if (propertyCode.matches(NUMBER_REGEX)) {
			Long propertyId = Long.valueOf(propertyCode);
			if (isRequiredMap.get(propertyId) != null && isRequiredMap.get(propertyId)) {
				return requiredStr;
			} else {
				return notRequiredStr;
			}
		} else {
			if (Arrays.asList(REQUIRED_COLUMN).contains(propertyCode)) {
				if (ITEM_TYPE_COLUMN.equals(propertyCode)) {
					return requiredStr + propmt;
				}
				return requiredStr;
			} else {
				if (ITEM_TYPE_COLUMN.equals(propertyCode)) {
					return requiredStr + propmt;
				}
				return notRequiredStr;
			}
		}
	}

	/**
	 * 获取列的数据
	 * 
	 * @param code
	 * @param itemCommand
	 * @return
	 */
	private Object getColumnDataByPropertyCode(String propertyCode, List<String> selectedProperties,
			ItemCommand itemCommand, Map<Long, String> itemCategoryMap) {
		if (!selectedProperties.contains(propertyCode)) {
			return null;
		}

		if (itemCommand == null) {
			return null;
		}

		if ("code".equals(propertyCode)) {
			return itemCommand.getCode();
		} else if ("title".equals(propertyCode)) {
			return itemCommand.getTitle();
		} else if ("subTitle".equals(propertyCode)) {
			return itemCommand.getSubTitle();
		} else if ("itemStyle".equals(propertyCode)) {
			return itemCommand.getStyle();
		} else if ("salePrice".equals(propertyCode)) {
			return itemCommand.getSalePrice();
		} else if ("listPrice".equals(propertyCode)) {
			return itemCommand.getListPrice();
		} else if ("seoTitle".equals(propertyCode)) {
			return itemCommand.getSeoTitle();
		} else if ("seoKeyWords".equals(propertyCode)) {
			return itemCommand.getSeoKeywords();
		} else if ("seoDesc".equals(propertyCode)) {
			return itemCommand.getSeoDescription();
		} else if ("sketch".equals(propertyCode)) {
			return itemCommand.getSketch();
		} else if ("description".equals(propertyCode)) {
			return itemCommand.getDescription();
		} else if ("itemType".equals(propertyCode)) {
			if (itemCommand.getType() == null)
				return "主卖品";
			switch (itemCommand.getType()) {
				case 1:
					return "主卖品";
				case 0:
					return "赠品";
				default:
					return "主卖品";
			}
		} else if ("categoryCodes".equals(propertyCode)) {
			return itemCategoryMap.get(itemCommand.getId());
		}
		return null;
	}

	/**
	 * 给单元格添加样式
	 * 
	 * @param xls
	 * @return
	 */
	private HSSFCellStyle createCellStyle(HSSFWorkbook xls) {
		/********************************************* 样式start ***************************************/
		// 对齐
		HSSFCellStyle cellStyle = xls.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 颜色与填充样式
		cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		/********************************************* 样式end ***************************************/
		return cellStyle;
	}

	/**
	 * 将页面传来的Code转化成Name
	 * 
	 * @param code
	 * @return
	 */
	private String converCodeToName(String code) {
		if ("code".equals(code)) {
			return "商品编码";
		} else if ("title".equals(code)) {
			return "商品名称";
		} else if ("subTitle".equals(code)) {
			return "商品副标题";
		} else if ("itemStyle".equals(code)) {
			return "商品款式";
		} else if ("itemType".equals(code)) {
			return "商品类型";
		} else if ("categoryCodes".equals(code)) {
			return "商品分类";
		} else if ("salePrice".equals(code)) {
			return "销售价";
		} else if ("listPrice".equals(code)) {
			return "挂牌价";
		} else if ("seoTitle".equals(code)) {
			return "SEO搜索标题";
		} else if ("seoKeyWords".equals(code)) {
			return "SEO搜索关键字";
		} else if ("seoDesc".equals(code)) {
			return "SEO搜索描述";
		} else if ("sketch".equals(code)) {
			return "商品概述";
		} else if ("description".equals(code)) {
			return "商品详细描述";
		}
		return null;
	}

	/**
	 * 设置excel文件头上的店铺信息和行业信息
	 * 
	 * @param industry
	 * @param shopCommand
	 * @param sheet
	 */
	private void setExcelSheetHeader(Industry industry, ShopCommand shopCommand, HSSFSheet sheet) {
		// 设置第五行的高度
		sheet.createRow(4).setHeight((short)0);
		// set shop name
		sheet.getRow(2).createCell(1).setCellValue(shopCommand.getShopname());
		// set shop id
		sheet.getRow(4).createCell(0).setCellValue(shopCommand.getShopid());
		// set industry name
		sheet.getRow(3).createCell(1).setCellValue(industry.getName());
		// set industry id
		sheet.getRow(4).createCell(1).setCellValue(industry.getId());
		// 是否开启国际化
		if(LangProperty.getI18nOnOff()){
			// set defaut language
			sheet.getRow(4).createCell(2).setCellValue(sdkI18nLangManager.getDefaultlang());
		}
	}
	

	@Override
	public List<Long> itemImport(InputStream inputStream, Long shopId) {

		BusinessException topException = null, currentException = null;

		InputStreamCacher cacher = null;
		try {
			cacher = new InputStreamCacher(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		HSSFWorkbook importXls = null;
		try {
			importXls = new HSSFWorkbook(cacher.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		HSSFSheet sheet = importXls.getSheetAt(XLS_SHEET_1);

		ExcelReader itemImport = excelFactory.createExcelReader("itemImport");

		// 读取excel中的数据
		Map<String, Object> itemBeans = new HashMap<String, Object>();
		ReadStatus rs = itemImport.readSheet(cacher.getInputStream(), 0, itemBeans);

		// 检查店铺ID
		checkShopId(shopId, itemBeans.get("shopId"));

		// 检查行业
		Long industryId = (Long) itemBeans.get("industryId");
		if (null == industryId) {
			throw new BusinessException(ErrorCodes.INDUSTRY_NOT_MATCH_ERROR);
		}
		
		/***************开启国际化后, 验证默认语言(第一个Sheet是存放默认语言的数据)***************/
		String defaultLang = (String) itemBeans.get("defaultLang");
		if(LangProperty.getI18nOnOff()){
			if(StringUtils.isBlank(defaultLang)){
				// 开启国际化后, 默认语言不可以为空
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_DEFAULT_LANG_IS_ERROR);
			}
			
			String dbDefaultLang = sdkI18nLangManager.getDefaultlang();
			if(!defaultLang.equals(dbDefaultLang)){
				// 默认语言被修改过,请重新下载模板
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_DEFAULT_LANG_IS_ERROR);
			}
		}else{
			defaultLang = DEFAULT_LANG;
		}

		// 检查行业属性
		//List<Property> propertyList = shopManager.findPropertyListByIndustryIdAndShopId(industryId, shopId, null);
        Sort[] sorts = new Sort[1];
		sorts[0] = new Sort("p.id", "asc");
		List<Property> propertyList = shopManager.findPropertyListByIndustryId(industryId, sorts);
		
		// 拆分成 销售属性List 非销售属性
		List<Long> notSalePropIdList = new ArrayList<Long>();

		List<Long> salePropIdList = new ArrayList<Long>();

		Map<Long, Boolean> isRequiredMap = new HashMap<Long, Boolean>();
		Map<Long, Property> propertyMap = new HashMap<Long, Property>();
		if (Validator.isNotNullOrEmpty(propertyList)) {
			for (Property property : propertyList) {
				if (property.getIsSaleProp()) {
					salePropIdList.add(property.getId());
				} else {
					notSalePropIdList.add(property.getId());
				}
				isRequiredMap.put(property.getId(), property.getRequired());
				propertyMap.put(property.getId(), property);
			}
		}
		/** key:language key, value:excel data **/
		Map<String, Map<String, Object>> itemBeansI18n = new HashMap<String, Map<String, Object>>();
		
		// excel文件中存在的列 (key[common or base]:value[key[列编号]:value[列号]])
		Map<String, Map<String, Integer>> propertyCodeMap = getPropertyCode(sheet, 5);

		//检查excel中商品属性是否存在
		checkCommonProperty(propertyCodeMap.get(MAP_KEY_COMMON), notSalePropIdList);

		//读取excel文件中的数据
		itemCommandSheetDefinition(itemImport, isRequiredMap, sheet, propertyCodeMap, propertyMap);

		itemBeans = new HashMap<String, Object>();
		// 读取excel文件中的商品数据到itemBeans中
		rs = itemImport.readSheet(cacher.getInputStream(), 0, itemBeans);

		if (log.isDebugEnabled()) {
			log.debug(" ------- import item propery code {}", JsonFormatUtil.format(propertyCodeMap));
			log.debug(" ------- import item data is {}", JsonFormatUtil.format(itemBeans));
		}

		// loxia成功读取excel数据, 操作excel数据
		if (ReadStatus.STATUS_SUCCESS != rs.getStatus()) {
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList) {
				BusinessException e = new BusinessException(message);
				if (topException == null) {
					topException = e;
					currentException = e;
				} else {
					currentException.setLinkedException(e);
					currentException = e;
				}
			}
		}else{
			itemBeansI18n.put(defaultLang, itemBeans);
		}
		
		// 判断系统是否开启国际化
		if(LangProperty.getI18nOnOff()){
			List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
			
			for(I18nLang i18nLang : i18nLangList){
				if(I18nLang.DEFAULT_STATUS.equals(i18nLang.getDefaultlang())){
					continue;
				}
				//读取excel文件中的数据
				String languageValue = i18nLang.getValue();
				HSSFSheet tmpSheet = importXls.getSheet(languageValue);
				
				// excel文件中存在的列 (key[common or base]:value[key[列编号]:value[列号]])
				Map<String, Map<String, Integer>> propertyCodeI18nMap = getPropertyCode(tmpSheet, 1);
				
				if(tmpSheet == null){
					// 通过语言名称不能获取Sheet
					throw new BusinessException(ErrorCodes.ITEM_IMPORT_LANGUAGE_VALUE_NOT_EXISTS);
				}
				int sheetNo = importXls.getSheetIndex(tmpSheet);
				
				ExcelReader itemImportI18n = excelFactory.createExcelReader("itemImportI18n");
				
				itemCommandSheetDefinitionI18n(itemImportI18n, isRequiredMap, tmpSheet, propertyCodeI18nMap, propertyMap);
	
				Map<String, Object> tmpItemBeans = new HashMap<String, Object>();
				// 读取excel文件中的商品数据到itemBeans中
				rs = itemImportI18n.readSheet(cacher.getInputStream(), sheetNo, tmpItemBeans);
	
				if (log.isDebugEnabled()) {
					log.debug("------- import item propery code {}", JsonFormatUtil.format(propertyCodeI18nMap));
					log.debug("------- import item data is {}", JsonFormatUtil.format(tmpItemBeans));
				}
	
				// loxia成功读取excel数据, 操作excel数据
				if (ReadStatus.STATUS_SUCCESS != rs.getStatus()) {
					List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
					for (String message : messageList) {
						BusinessException e = new BusinessException(message);
						if (topException == null) {
							topException = e;
							currentException = e;
						} else {
							currentException.setLinkedException(e);
							currentException = e;
						}
					}
				}else{
					// 读取excel数据成功后, 保存数据到MAP中
					itemBeansI18n.put(i18nLang.getKey(), tmpItemBeans);
				}
			}
		}

		if (topException != null) {
			throw topException;
		}
		// 查询该配置是否存在
		String pdValidCode = sdkMataInfoManager.findValue(MataInfo.PD_VALID_CODE);
		
		// 正则验证商品编号
		BusinessException error = checkItemCode(itemBeans, pdValidCode);
		if(error != null){
			throw error;
		}
		// 处理商品分类数据
		processItemCategory(itemBeansI18n, propertyCodeMap.get(MAP_KEY_BASE));
		// 处理商品属性数据
		Map<String, String> itemPropertiesI18nMap = processItemProperties(itemBeansI18n, propertyCodeMap.get(MAP_KEY_COMMON), propertyMap, notSalePropIdList);
		// 处理商品信息数据
		Map<String, ImpItemCommand> itemInfoMap = processItemInfo(itemBeansI18n);
		
		// 修改商品信息(分类信息, 商品属性, 商品信息)
		//Map<String, ItemProperties> needI18nItemProperiesMap = new HashMap<String, ItemProperties>();
		List<Long> itemIdsForSolr = batchUpdateImportItem(itemBeansI18n, propertyCodeMap, propertyMap, shopId, itemInfoMap, itemPropertiesI18nMap);
		
		// 执行扩展点
		if (null != itemExtendManager) {
			itemExtendManager.extendAfterItemImport(itemBeansI18n, propertyCodeMap, propertyMap);
		}
		return itemIdsForSolr;
	}
	
	/**
	 * 正则验证商品编号
	 * @param itemBeans 商品信息
	 * @param pdValidCode 正则表达式
	 */
	private BusinessException checkItemCode(Map<String, Object> itemBeans, String pdValidCode){
		List<ImpItemCommand> impItemCommandList = (List<ImpItemCommand>) itemBeans.get("impItemCommand");
		BusinessException topE = null, currE = null;
		for (ImpItemCommand itemCommand : impItemCommandList) {
			// 配置了正则并且不满足正则条件的情况下
			if(Validator.isNotNullOrEmpty(pdValidCode)){
				if(!Pattern.matches(pdValidCode, itemCommand.getCode())){
					BusinessException e = new BusinessException(ErrorCodes.ITEM_CODE_VALID_ERROR, new Object[] { itemCommand.getCode(), pdValidCode});
					
					if (topE == null) {
						topE = e; // b-101 : Cell{}错误, new
						// Object[]{ExcelUtil.getCell(1,2)}
						currE = e;
					} else {
						currE.setLinkedException(e);
						currE = e;
					}
				}
			}
		}
		return topE;
	}

	/**
	 * 处理商品信息数据
	 * @param itemBeansI18n
	 * @return 
	 * 		key:langKey_itemCode(如:zh_CN_itemCode), vlaue: ImpItemCommand
	 */
	private Map<String, ImpItemCommand> processItemInfo(Map<String, Map<String, Object>> itemBeansMap) {
		// TODO Auto-generated method stub
		if(LangProperty.getI18nOnOff()){
			/** key:langKey_itemCode(如:zh_CN_itemCode), vlaue: ImpItemCommand **/
			Map<String, ImpItemCommand> itemInfoMap = new HashMap<String, ImpItemCommand>();
			List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
			for(I18nLang i18nLang : i18nLangList){
				String langKey = i18nLang.getKey();
				List<ImpItemCommand> impItemCommandList = getImportItemCommand(itemBeansMap, langKey);
				
				if(impItemCommandList != null && !impItemCommandList.isEmpty()){
					for(ImpItemCommand itemCommand : impItemCommandList){
						itemInfoMap.put(langKey + "-" + itemCommand.getCode() , itemCommand);
					}
				}
			}
			return itemInfoMap;
		}
		return null;
	}

	/**
	 * 加载i18n中的数据
	 * @param itemImport
	 * @param isRequiredMap
	 * @param sheet
	 * @param propertyCodeMap
	 * @param propertyMap
	 */
	private void itemCommandSheetDefinitionI18n(ExcelReader itemImport, Map<Long, Boolean> isRequiredMap,
			HSSFSheet sheet, Map<String, Map<String, Integer>> propertyCodeMap, Map<Long, Property> propertyMap) {
		ExcelSheet sheetDefinition = itemImport.getDefinition().getExcelSheets().get(0);
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A5", "A5");
		int startRow = 4;
		// 选择的商品基本信息 如:title, code...
		for (Map.Entry<String, Integer> entry : propertyCodeMap.get(MAP_KEY_BASE).entrySet()) {
			String key = entry.getKey();
			ExcelCell prop = new ExcelCell();
			prop.setCol(entry.getValue());
			prop.setRow(startRow);
			prop.setDataName(key);
			prop.setMandatory(Arrays.asList(REQUIRED_COLUMN).contains(entry.getKey()));
			prop.setType(Arrays.asList(BIGDECIMAL_COLUMN).contains(entry.getKey()) ? "bigdecimal" : "string");
			blockDefinition.addCell(prop);
		}

		// 选择的商品的一般属性, 在行业中定义的非销售属性
		for (Map.Entry<String, Integer> entry : propertyCodeMap.get(MAP_KEY_COMMON).entrySet()) {
			ExcelCell prop = new ExcelCell();
			prop.setCol(entry.getValue());
			prop.setRow(startRow);
			prop.setDataName("props.p" + entry.getKey());
			prop.setMandatory(isRequiredMap.get(Long.valueOf(entry.getKey())));
			prop.setType(getPropertyValueType(propertyMap.get(Long.valueOf(entry.getKey())).getValueType()));
			blockDefinition.addCell(prop);
		}
		
	}

	/**
	 * 读取excel文件中的数据
	 * 
	 * @param itemImport
	 * @param isRequiredMap
	 * @param sheet
	 * @param propertyCodeMap
	 */
	private void itemCommandSheetDefinition(ExcelReader itemImport, Map<Long, Boolean> isRequiredMap, HSSFSheet sheet,
			Map<String, Map<String, Integer>> propertyCodeMap, Map<Long, Property> propertyMap) {
		ExcelSheet sheetDefinition = itemImport.getDefinition().getExcelSheets().get(0);
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A9", "A9");
		int startRow = ITEM_DATA_ROW;
		// 选择的商品基本信息 如:title, code...
		for (Map.Entry<String, Integer> entry : propertyCodeMap.get(MAP_KEY_BASE).entrySet()) {
			ExcelCell prop = new ExcelCell();
			prop.setCol(entry.getValue());
			prop.setRow(startRow);
			prop.setDataName(entry.getKey());
			prop.setMandatory(Arrays.asList(REQUIRED_COLUMN).contains(entry.getKey()));
			prop.setType(Arrays.asList(BIGDECIMAL_COLUMN).contains(entry.getKey()) ? "bigdecimal" : "string");
			blockDefinition.addCell(prop);
		}

		// 选择的商品的一般属性, 在行业中定义的非销售属性
		for (Map.Entry<String, Integer> entry : propertyCodeMap.get(MAP_KEY_COMMON).entrySet()) {
			ExcelCell prop = new ExcelCell();
			prop.setCol(entry.getValue());
			prop.setRow(startRow);
			prop.setDataName("props.p" + entry.getKey());
			prop.setMandatory(isRequiredMap.get(Long.valueOf(entry.getKey())));
			prop.setType(getPropertyValueType(propertyMap.get(Long.valueOf(entry.getKey())).getValueType()));
			blockDefinition.addCell(prop);
		}
	}

	/**
	 * 检查店铺
	 * 
	 * @param shopId
	 * @param sheet
	 */
	private void checkShopId(Long shopId, Object xlsShopId) {
		if (xlsShopId == null || !xlsShopId.equals(shopId)) {
			throw new BusinessException(ErrorCodes.SHOP_NOT_MATCH_ERROR);
		}
	}

	/**
	 * 检查excel中商品属性是否存在
	 * 
	 * @param commonPropertyMap
	 * @param commonPropertyIdList
	 */
	private void checkCommonProperty(Map<String, Integer> commonPropertyMap, List<Long> commonPropertyIdList) {
		for (Map.Entry<String, Integer> entry : commonPropertyMap.entrySet()) {
			Long xlsPropertyId = Long.valueOf(entry.getKey());
			if (!commonPropertyIdList.contains(xlsPropertyId)) {
				throw new BusinessException(ErrorCodes.PROP_NOT_MATCH_ERROR);
			}
		}
	}

	/**
	 * 获取excel文件中的导入商品字段
	 * 
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Integer>> getPropertyCode(HSSFSheet sheet, int rowNo) {
		HSSFRow row = sheet.getRow(rowNo);
		Map<String, Integer> commonPropertyCodeMap = new HashMap<String, Integer>();
		Map<String, Integer> basePropertyCodeMap = new HashMap<String, Integer>();
		Iterator<Cell> iterator = row.iterator();
		for (; iterator.hasNext();) {
			Cell cell = iterator.next();
			String value = String.valueOf(getValue(cell));
			if (value != null && !"null".equals(value)) {
				if (value.matches(NUMBER_REGEX)) {
					commonPropertyCodeMap.put(value, cell.getColumnIndex());
				} else {
					basePropertyCodeMap.put(value, cell.getColumnIndex());
				}
			}
		}
		Map<String, Map<String, Integer>> propertyCodeMap = new HashMap<String, Map<String, Integer>>();
		propertyCodeMap.put(MAP_KEY_COMMON, commonPropertyCodeMap);
		propertyCodeMap.put(MAP_KEY_BASE, basePropertyCodeMap);
		return propertyCodeMap;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	private Object getValue(Cell cell) {
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case HSSFCell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case HSSFCell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue();
			default:
				return cell.getStringCellValue();
		}
	}

	/**
	 * 整理商品分类数据
	 * 
	 * @param itemBeans
	 * @param propertyCodeMap
	 */
	private void processItemCategory(Map<String, Map<String, Object>> itemBeansMap, Map<String, Integer> propertyCodeMap) {
		if (propertyCodeMap.containsKey(CATEGORY_CODE_COLUMN)) {
			String defaultLangKey = sdkI18nLangManager.getDefaultlang();
			List<ImpItemCommand> impItemCommandList = getImportItemCommand(itemBeansMap, defaultLangKey);
			if(impItemCommandList == null){
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_ITEM_DATA_IS_EMPTY);
			}
//			Map<String, Long> categoryMap = getEnableCategoryMap();
			Map<String, Long> leafNodeCategoryMap = categoryManager.getLeafNodeCategoryMap();//产品挂分类必须挂在叶子节点
			List<ItemCategory> itemCategoryList = null;
			ItemCategory itemCategory = null;
			// 保存不存在的分类CODE
			List<String> notExistsCategoryCodeList = new ArrayList<String>();
			for (ImpItemCommand itemCommand : impItemCommandList) {
				itemCategoryList = new ArrayList<ItemCategory>();
				String categoryCodes = itemCommand.getCategoryCodes();
				if (StringUtils.isBlank(categoryCodes)) {
					continue;
				}
				String[] categoryCodeStrs = categoryCodes.split(BACK_SLANT_SEPARATOR);
				Long categoryId = null;
				for (String categoryCode : categoryCodeStrs) {
					categoryId = leafNodeCategoryMap.get(categoryCode);
					if (categoryId != null) {
						itemCategory = new ItemCategory();
						itemCategory.setCategoryId(leafNodeCategoryMap.get(categoryCode));
						itemCategoryList.add(itemCategory);
					} else {
						notExistsCategoryCodeList.add(categoryCode);
					}
				}
				itemCommand.setItemCategoryList(itemCategoryList);
			}

			if (!notExistsCategoryCodeList.isEmpty()) {
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_CATEGORY_NOT_EXISTS,
						new Object[] { notExistsCategoryCodeList.toString() });
			}
		}
	}

	/**
	 * 整理商品属性数据
	 * @param itemBeansMap
	 * @param propertyCodeMap
	 * @param propertyMap
	 * @param commonPropertyIdList
	 * @return
	 * 		key:langKey-itemCode-propertyId or langKey-itemCode-propertyId-propertyValue, value:ItemProperties
	 */
	
	private Map<String, String> processItemProperties(Map<String, Map<String, Object>> itemBeansMap, Map<String, Integer> propertyCodeMap,
			Map<Long, Property> propertyMap, List<Long> commonPropertyIdList) {

		if (propertyCodeMap != null && !propertyCodeMap.isEmpty()) {
			Map<String, String> itemPropertiesI18nMap = new HashMap<String, String>();
			List<String> notExistPropertyValueList = new ArrayList<String>();
			
			List<ImpItemCommand> impItemCommandList = getImportItemCommand(itemBeansMap, sdkI18nLangManager.getDefaultlang());
			if(impItemCommandList == null){
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_ITEM_DATA_IS_EMPTY);
			}
			Map<Long, Map<String, Long>> propValMap = getPropValMap(commonPropertyIdList);
			// 处理商品属性
			processItemProperites(itemBeansMap, propertyMap, itemPropertiesI18nMap, impItemCommandList, propValMap, notExistPropertyValueList);
			if (!notExistPropertyValueList.isEmpty()) {
				throw new BusinessException(ErrorCodes.ITEM_IMPORT_PROPERTY_NOT_EXISTS, new Object[] { notExistPropertyValueList.toString() });
			}
			return itemPropertiesI18nMap;
		}
		
		return null;
	}

	/**
	 * 单个处理商品属性
	 * @param propertyMap
	 * @param itemPropertiesI18nMap
	 * @param impItemCommandList
	 * @param propValMap
	 * @param notExistPropertyValueList
	 * @param langKey
	 * 			当国际化没有开启时, 此参数无效
	 * @param isDefaultLang
	 * 			当国际化没有开启时, 此参数无效
	 */
	private void processItemProperites(Map<String, Map<String, Object>> itemBeansMap, Map<Long, Property> propertyMap,
			Map<String, String> itemPropertiesI18nMap, List<ImpItemCommand> impItemCommandList,
			Map<Long, Map<String, Long>> propValMap, List<String> notExistPropertyValueList) {
		
		Map<String, Map<String, String>> propertValueI18nMap = new HashMap<String, Map<String, String>>();
		List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
		
		for(I18nLang i18nLang : i18nLangList){
			String langKey = i18nLang.getKey();
			List<ImpItemCommand> tempItemCommandList = getImportItemCommand(itemBeansMap, langKey);
			for(ImpItemCommand impItemCommand : tempItemCommandList){
				propertValueI18nMap.put(langKey + "-" + impItemCommand.getCode(), impItemCommand.getProps());
			}
		}
		
		for (ImpItemCommand impItemCommand : impItemCommandList) {
			List<ItemProperties> ipslist = new ArrayList<ItemProperties>();
			String code = impItemCommand.getCode();
			Map<String, String> prop = impItemCommand.getProps();
			Iterator<String> it = prop.keySet().iterator();
			while (it.hasNext()) {

				String key = String.valueOf(it.next());
				Long propId = Long.valueOf(key.substring(1));// 去掉前面的"p"
				Property property = propertyMap.get(propId);

				/** 编辑类型 ：1 单行输入2可输入单选3单选4多选 */
				ItemProperties itemProperties = new ItemProperties();

				itemProperties.setPropertyId(propId);

				// 1:单行输入
				if (Property.EDITING_TYPE_INPUT.equals(property.getEditingType())) {
					itemProperties.setPropertyValue(prop.get(key));
					ipslist.add(itemProperties);
					if(LangProperty.getI18nOnOff()){
						for(I18nLang i18nLang : i18nLangList){
							String tmpLangKey = i18nLang.getKey();
							itemPropertiesI18nMap.put(tmpLangKey + "-" + code + "-" + propId, propertValueI18nMap.get(tmpLangKey + "-" + code).get(key));
						}
					}
				} 
				// 2:可输入单选
				else if (Property.EDITING_TYPE_CUSTOM_RADIO.equals(property.getEditingType())) {
					Long pvId = null;
					if (null != propValMap.get(propId)) {
						pvId = propValMap.get(propId).get(prop.get(key));
					}
					if (pvId == null) {
						itemProperties.setPropertyValue(prop.get(key));
					} else {
						itemProperties.setPropertyValueId(pvId);
					}
					ipslist.add(itemProperties);
					if(LangProperty.getI18nOnOff()){
						for(I18nLang i18nLang : i18nLangList){
							String tmpLangKey = i18nLang.getKey();
							itemPropertiesI18nMap.put(tmpLangKey+"-"+code+"-"+propId, propertValueI18nMap.get(tmpLangKey+"-"+code).get(key));
						}
					}
				} 
				// 3:单选
				else if (Property.EDITING_TYPE_RADIA.equals(property.getEditingType())) {
					if (Validator.isNotNullOrEmpty(prop.get(key))) {
						Long pvId = null;
						if (null != propValMap.get(propId)) {
							pvId = propValMap.get(propId).get(prop.get(key));
						}
						if (pvId == null) {
							notExistPropertyValueList.add(prop.get(key));
						} else {
							itemProperties.setPropertyValueId(pvId);
						}
						ipslist.add(itemProperties);
					}
				} 
				// 4 - 多选
				else if (Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType())) {
					if (Validator.isNotNullOrEmpty(prop.get(key))) {

						String propVals = prop.get(key);

						String[] strs = StringUtils.split(propVals, DOUBLE_SLASH_SEPARATOR);//propVals.split(DOUBLE_SLASH_SEPARATOR);

						for (String str : strs) {
							Long pvId = null;
							itemProperties = new ItemProperties();
							itemProperties.setPropertyId(propId);
							if (null != propValMap.get(propId)) {
								pvId = propValMap.get(propId).get(str);
							}
							if (pvId == null) {
								notExistPropertyValueList.add(prop.get(key));
							} else {
								itemProperties.setPropertyValueId(pvId);
							}
							ipslist.add(itemProperties);
						}
					}
				}
				// 5 - 自定义多选
				else if(Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(property.getEditingType())){
					if (Validator.isNotNullOrEmpty(prop.get(key))) {
						String propVals = prop.get(key);

						String[] strs = StringUtils.split(propVals, DOUBLE_SLASH_SEPARATOR);//propVals.split(DOUBLE_SLASH_SEPARATOR);
						int index = 0;
						for (String propertyValue : strs) {
							itemProperties = new ItemProperties();
							itemProperties.setPropertyId(propId);
							itemProperties.setPropertyValue(propertyValue);
							ipslist.add(itemProperties);
							
							if(LangProperty.getI18nOnOff()){
								for(I18nLang i18nLang : i18nLangList){
									String tmpLangKey = i18nLang.getKey();
									itemPropertiesI18nMap.put(tmpLangKey+"-"+code+"-"+propId+"-"+propertyValue, StringUtils.split(propertValueI18nMap.get(tmpLangKey + "-" + code).get(key), DOUBLE_SLASH_SEPARATOR)[index]);
								}
							}
							index++;
						}
					}
				}
			}
			impItemCommand.setItemProps(ipslist);
		}
	}
	
	/**
	 * 获取商品集合
	 * @param itemBeansMap
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ImpItemCommand> getImportItemCommand(Map<String, Map<String, Object>> itemBeansMap, String langKey) {
		Map<String, Object> itemBeans = new HashMap<String, Object>();
		if(LangProperty.getI18nOnOff()){
			itemBeans = itemBeansMap.get(langKey);
		}else{
			itemBeans = itemBeansMap.get(DEFAULT_LANG);
		}
		List<ImpItemCommand> impItemCommandList = (List<ImpItemCommand>) itemBeans.get("impItemCommand");
		return impItemCommandList;
	}

	/**
	 * 获取属性值
	 * 
	 * @param propertyIds
	 * @return
	 */
	private Map<Long, Map<String, Long>> getPropValMap(List<Long> propertyIds) {

		Map<Long, Map<String, Long>> propValMap = new HashMap<Long, Map<String, Long>>();

		List<PropertyValue> propertyValueList = propertyValueDao.findPropertyValueListByPropIds(propertyIds);

		if (Validator.isNotNullOrEmpty(propertyValueList)) {
			Long propId = null;
			Map<String, Long> propertyValueMap = new HashMap<String, Long>();

			int index = 0;
			for (PropertyValue propertyValue : propertyValueList) {
				index++;
				if (null == propId) {
					propId = propertyValue.getPropertyId();
				}

				if (propId.equals(propertyValue.getPropertyId())) {
					propertyValueMap.put(propertyValue.getValue(), propertyValue.getId());
				} else {
					propValMap.put(propId, propertyValueMap);
					propId = propertyValue.getPropertyId();
					propertyValueMap = new HashMap<String, Long>();
					propertyValueMap.put(propertyValue.getValue(), propertyValue.getId());
				}

				if (index == propertyValueList.size()) {
					propValMap.put(propId, propertyValueMap);
				}

			}

		}

		return propValMap;
	}

	/**
	 * 获取可用的商品分类
	 * 
	 * @return
	 */
	private Map<String, Long> getEnableCategoryMap() {
		Map<String, Long> categoryMap = new HashMap<String, Long>();
		List<Category> categoryList = categoryDao.findEnableCategoryList(null);
		if (Validator.isNotNullOrEmpty(categoryList)) {
			for (Category category : categoryList) {
				categoryMap.put(category.getCode(), category.getId());
			}
		}
		return categoryMap;
	}

	/**
	 * 批量保存导入的商品数据
	 * 
	 * @param itemBeans
	 * @param selectedColumnMap
	 */
	private List<Long> batchUpdateImportItem(Map<String, Map<String, Object>> itemBeansMap,
			Map<String, Map<String, Integer>> selectedColumnMap, Map<Long, Property> propertyMap,
			Long shopId, Map<String, ImpItemCommand> itemInfoMap, Map<String, String> itemDataI18nMap) {
		String defaultLangKey = sdkI18nLangManager.getDefaultlang();
		List<ImpItemCommand> impItemCommandList = getImportItemCommand(itemBeansMap, defaultLangKey);
		if (impItemCommandList != null && !impItemCommandList.isEmpty()) {
			List<String> itemCodes = new ArrayList<String>();
			
			for (ImpItemCommand impItemCommand : impItemCommandList) {
				itemCodes.add(impItemCommand.getCode());
			}

			List<Item> itemList = itemDao.findItemListByCodes(itemCodes, null);

			// 导入的商品编码中与DB中商品编码不一致
			if (itemList == null || itemList.size() != impItemCommandList.size()) {
				if (itemList == null || itemList.isEmpty()) {
					throw new BusinessException(ErrorCodes.ITEM_IMPORT_CODE_NOT_EXISTS, new Object[] { itemCodes.toString() });
				}
				// DB中不存在的商品编码集合
				List<String> notExistsItemCodeList = new ArrayList<String>();

				List<String> dbItemCodeList = new ArrayList<String>();
				for (Item item : itemList) {
					dbItemCodeList.add(item.getCode());
				}

				for (String code : itemCodes) {
					if (!dbItemCodeList.contains(code)) {
						notExistsItemCodeList.add(code);
					}
				}

				if (!notExistsItemCodeList.isEmpty()) {
					throw new BusinessException(ErrorCodes.ITEM_IMPORT_CODE_NOT_EXISTS, new Object[] { notExistsItemCodeList.toString() });
				}
			}

			List<Long> itemIds = new ArrayList<Long>();
			// 刷新SOLR索引的商品ID集合:只有商品是上架时, 才可以刷到SOLR索引里
			List<Long> itemIdsForSolr = new ArrayList<Long>();
			/** key:itemcode; value:Item对象 */
			Map<String, Item> itemCommandMap = new HashMap<String, Item>();
			for (Item item : itemList) {
				itemIds.add(item.getId());
				itemCommandMap.put(item.getCode(), item);
				if (Item.LIFECYCLE_ENABLE.equals(item.getLifecycle())) {
					itemIdsForSolr.add(item.getId());
				}
			}

			// 自定义多选 销售属性(或者非销售1 单行输入2可输入单选) 涉及itemproperties
			// key = itemId_propertyId_propertyValue
			// value = id
			Map<String, Long> itemPropVMap = new HashMap<String, Long>();

			// itemproperties
			// key = itemId_propertyId_propertyValueId
			// value = id
			Map<String, Long> itemPropIdMap = new HashMap<String, Long>();

			// itemproperties
			// key = itemId_propertyId
			// value = id
			Map<String, Long> itemPropMap = new HashMap<String, Long>();
			
			// DB中商品属性ID集合
			List<Long> dbItemPropertiesIdList = new ArrayList<Long>();

			if (Validator.isNotNullOrEmpty(itemIds)) {
				List<ItemProperties> itemPropertiesList = itemPropertiesDao.findItemPropertieListByItemIds(itemIds);
				if (Validator.isNotNullOrEmpty(itemPropertiesList)) {
					for (ItemProperties itemProperties : itemPropertiesList) {
						String key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId();
						itemPropMap.put(key, itemProperties.getId());
						if (itemProperties.getPropertyValue() != null) {
							key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
							itemPropVMap.put(key, itemProperties.getId());
						} else {
							key = itemProperties.getItemId() + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
							itemPropIdMap.put(key, itemProperties.getId());
						}
						
						if(selectedColumnMap.get(MAP_KEY_COMMON).keySet().contains(String.valueOf(itemProperties.getPropertyId()))){
							dbItemPropertiesIdList.add(itemProperties.getId());
						}
					}
				}
			}

			// 选中的列集合
			List<String> selectedColumnList = new ArrayList<String>();
			for (Map.Entry<String, Map<String, Integer>> entry : selectedColumnMap.entrySet()) {
				Map<String, Integer> propertyCodeMap = entry.getValue();
				for (Map.Entry<String, Integer> columnMap : propertyCodeMap.entrySet()) {
					selectedColumnList.add(columnMap.getKey());
				}
			}

			// 保存商品相关的数据
			Item item = null;
			List<Long> updatedItemPropertiesIdList = new ArrayList<Long>();
			for (ImpItemCommand impItemCommand : impItemCommandList) {
				item = itemCommandMap.get(impItemCommand.getCode());

				Long itemId = item.getId();
				String itemCode = item.getCode();
				/*************************** 修改商品信息 ***************************/
				// 保存商品信息数据
				ItemInfo itemInfo = saveItemInfo(selectedColumnList, impItemCommand, itemId);
				// 保存商品信息国际化数据
				saveItemInfoLang(impItemCommand, selectedColumnList, itemInfo, itemInfoMap);

				/*************************** 修改商品分类: 先删除, 再添加 ***************************/
				// 下载模板时, 选中商品分类后, 导入商品时, 才修改商品分类信息
				saveItemCatgory(selectedColumnList, impItemCommand, itemId);

				/*************************** 修改商品属性(非销售属性) ***************************/
				List<ItemProperties> importPropertyList = impItemCommand.getItemProps();
				
				if (Validator.isNotNullOrEmpty(importPropertyList)) {
					for (ItemProperties itemProperties : importPropertyList) {
						itemProperties.setItemId(itemId);
						Property prop = propertyMap.get(itemProperties.getPropertyId());
						// 1 - 单行输入
						if (Property.EDITING_TYPE_INPUT.equals(prop.getEditingType())) {
							String key = itemId + "_" + itemProperties.getPropertyId();
							ItemProperties itemProp = null;
							if (Validator.isNullOrEmpty(itemPropMap.get(key))) {
								itemProp = saveImpItemproperties(itemProperties);
								itemPropMap.put(key, itemProp.getId());
							} else {
								itemProperties.setId(itemPropMap.get(key));
								updateImpItemproperties(itemProperties);
								updatedItemPropertiesIdList.add(itemProperties.getId());
								itemProp = itemProperties;
							}
							saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
						}
						// 2 - 可输入单选
						else if (Property.EDITING_TYPE_CUSTOM_RADIO.equals(prop.getEditingType())) {
							ItemProperties itemProp = null;
							if (itemProperties.getPropertyValue() != null) {
								String key = itemId + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
								if (Validator.isNullOrEmpty(itemPropVMap.get(key))) {
									itemProp = saveImpItemproperties(itemProperties);
									itemPropVMap.put(key, itemProp.getId());
									saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
								}
							}
							if (itemProperties.getPropertyValueId() != null) {
								String key = itemId + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
								if (Validator.isNullOrEmpty(itemPropIdMap.get(key))) {
									itemProp = saveImpItemproperties(itemProperties);
									itemPropIdMap.put(key, itemProp.getId());
									saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
								}
							}
							
						}
						// 3 - 单选
						else if (Property.EDITING_TYPE_RADIA.equals(prop.getEditingType())) {
							String key = itemId + "_" + itemProperties.getPropertyId();
							if (Validator.isNullOrEmpty(itemPropMap.get(key))) {
								ItemProperties itemProp = saveImpItemproperties(itemProperties);
								itemPropMap.put(key, itemProp.getId());
								saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
							} else {
								itemProperties.setId(itemPropMap.get(key));
								updateImpItemproperties(itemProperties);
								updatedItemPropertiesIdList.add(itemProperties.getId());
							}
						}
						// 4 - 多选
						else if (Property.EDITING_TYPE_MULTI_SELECT.equals(prop.getEditingType())) {
							String key = itemId + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValueId();
							ItemProperties itemProp = saveImpItemproperties(itemProperties);
							itemPropIdMap.put(key, itemProp.getId());
							saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
						}
						// 5 - 自定义多选
						else if(Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(prop.getEditingType())){
							String key = itemId + "_" + itemProperties.getPropertyId() + "_" + itemProperties.getPropertyValue();
							ItemProperties itemProp = null;
							if (Validator.isNullOrEmpty(itemPropVMap.get(key))) {
								itemProp = saveImpItemproperties(itemProperties);
								itemPropVMap.put(key, itemProp.getId());
							} else {
								itemProperties.setId(itemPropVMap.get(key));
								updateImpItemproperties(itemProperties);
								updatedItemPropertiesIdList.add(itemProperties.getId());
								itemProp = itemProperties;
							}
							
							saveItemPropertiesI18n(itemProp, itemBeansMap, itemDataI18nMap, itemCode);
						}
					}
				}
				
			}

			// 要删除的商品属性: DB中选中列的商品属性与修改过的商品属性对比,DB中多出来的属性,就是要删除的商品属性
			List<Long> deleteItemPropertiesIdList = new ArrayList<Long>();
			if(updatedItemPropertiesIdList != null && !updatedItemPropertiesIdList.isEmpty()){
				for(Long itemPropertiesId : dbItemPropertiesIdList){
					if(!updatedItemPropertiesIdList.contains(itemPropertiesId)){
						deleteItemPropertiesIdList.add(itemPropertiesId);
					}
				}
			} else {
				deleteItemPropertiesIdList.addAll(dbItemPropertiesIdList);
			}
			
			if(!deleteItemPropertiesIdList.isEmpty()){
				itemPropertiesDao.deleteItemPropertiesByIds(deleteItemPropertiesIdList);
				//删除多语言
				itemPropertiesDao.deleteItemPropertiesLangByIds(deleteItemPropertiesIdList);
			}
			
			return itemIdsForSolr;
		}
		return null;
	}

	/**
	 * 保存商品信息
	 * @param selectedColumnList
	 * @param impItemCommand
	 * @param itemId
	 * @return
	 */
	private ItemInfo saveItemInfo(List<String> selectedColumnList, ImpItemCommand impItemCommand, Long itemId) {
		ItemInfo itemInfo = itemInfoDao.findItemInfoByItemId(itemId);
		if (isSelectedColumn("title", selectedColumnList)) {
			itemInfo.setTitle(impItemCommand.getTitle());
		}
		if (isSelectedColumn("subTitle", selectedColumnList)) {
			itemInfo.setSubTitle(impItemCommand.getSubTitle());
		}
		if (isSelectedColumn("itemStyle", selectedColumnList)) {
			itemInfo.setStyle(impItemCommand.getItemStyle());
		}
		if (isSelectedColumn("itemType", selectedColumnList)) {
			if ("赠品".equals(impItemCommand.getItemType())) {
				itemInfo.setType(ItemInfo.TYPE_GIFT);
			} else {
				itemInfo.setType(ItemInfo.TYPE_MAIN);
			}
		}
		if (isSelectedColumn("salePrice", selectedColumnList)) {
			itemInfo.setSalePrice(impItemCommand.getSalePrice());
		}
		if (isSelectedColumn("listPrice", selectedColumnList)) {
			itemInfo.setListPrice(impItemCommand.getListPrice());
		}
		if (isSelectedColumn("seoTitle", selectedColumnList)) {
			itemInfo.setSeoTitle(impItemCommand.getSeoTitle());
		}
		if (isSelectedColumn("seoKeyWords", selectedColumnList)) {
			itemInfo.setSeoKeywords(impItemCommand.getSeoKeyWords());
		}
		if (isSelectedColumn("seoDesc", selectedColumnList)) {
			itemInfo.setSeoDescription(impItemCommand.getSeoDesc());
		}
		if (isSelectedColumn("sketch", selectedColumnList)) {
			itemInfo.setSketch(impItemCommand.getSketch());
		}
		if (isSelectedColumn("description", selectedColumnList)) {
			itemInfo.setDescription(impItemCommand.getDescription());
		}
		// 保存商品信息
		return itemInfoDao.save(itemInfo);
	}

	/**
	 * 保存商品信息国际化数据
	 * @param currentItemCommand
	 * @param selectedColumnList
	 * @param itemInfo
	 * @param itemInfoMap
	 */
	private void saveItemInfoLang(ImpItemCommand currentItemCommand, List<String> selectedColumnList, ItemInfo itemInfo, Map<String, ImpItemCommand> itemInfoMap) {
		if(LangProperty.getI18nOnOff()){
			List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
			for(I18nLang i18nLang : i18nLangList){
				String langKey = i18nLang.getKey();
				ImpItemCommand impItemCommand = itemInfoMap.get(langKey + "-" + currentItemCommand.getCode());
				ItemInfoLang itemInfoLang = itemInfoLangDao.findItemInfoLangByItemInfoIdAndLang(itemInfo.getId(), langKey);
				itemInfoLang.setLang(langKey);
				if (isSelectedColumn("title", selectedColumnList)) {
					itemInfoLang.setTitle(impItemCommand.getTitle());
				}
				if (isSelectedColumn("subTitle", selectedColumnList)) {
					itemInfoLang.setSubTitle(impItemCommand.getSubTitle());
				}
				if (isSelectedColumn("seoTitle", selectedColumnList)) {
					itemInfoLang.setSeoTitle(impItemCommand.getSeoTitle());
				}
				if (isSelectedColumn("seoKeyWords", selectedColumnList)) {
					itemInfoLang.setSeoKeywords(impItemCommand.getSeoKeyWords());
				}
				if (isSelectedColumn("seoDesc", selectedColumnList)) {
					itemInfoLang.setSeoDescription(impItemCommand.getSeoDesc());
				}
				if (isSelectedColumn("sketch", selectedColumnList)) {
					itemInfoLang.setSketch(impItemCommand.getSketch());
				}
				if (isSelectedColumn("description", selectedColumnList)) {
					itemInfoLang.setDescription(impItemCommand.getDescription());
				}
				// 保存商品信息
				itemInfoLangDao.save(itemInfoLang);
			}
		}
	}
	
	/**
	 * 保存商品分类信息: 先删除, 再添加
	 * 下载模板时, 选中商品分类后, 导入商品时, 才修改商品分类信息
	 * @param selectedColumnList
	 * @param impItemCommand
	 * @param itemId
	 */
	private void saveItemCatgory(List<String> selectedColumnList, ImpItemCommand impItemCommand, Long itemId) {
		if (isSelectedColumn(CATEGORY_CODE_COLUMN, selectedColumnList)) {
			itemCategoryDao.deleteItemCategoryByItemId(itemId);
			List<ItemCategory> itemCategoryList = impItemCommand.getItemCategoryList();
			if (Validator.isNotNullOrEmpty(itemCategoryList)) {
				Long[] defaultCategoryIds = new Long[itemCategoryList.size()];
				int i = 0;
				for (ItemCategory itemCategory : itemCategoryList) {
					defaultCategoryIds[i] = itemCategory.getCategoryId();
					i++;
				}
				Arrays.sort(defaultCategoryIds);
				// 分类列中的第一个设置为商品默认分类
				Long defaultCategoryId = defaultCategoryIds[0];
				for (ItemCategory itemCategory : itemCategoryList) {
					boolean isDefault = false;
					if (defaultCategoryId.equals(itemCategory.getCategoryId())) {
						isDefault = true;
					}
					itemCategoryDao.bindItemCategory(itemId, itemCategory.getCategoryId(), isDefault);
				}
			}
		}
	}
	
	/**
	 * 保存商品属性国际化数据
	 * @param itemProperties
	 * @param itemBeansMap
	 * @param itemDataI18nMap
	 */
	private void saveItemPropertiesI18n(ItemProperties itemProperties, Map<String, Map<String, Object>> itemBeansMap, Map<String, String> itemDataI18nMap, String code){
		if(LangProperty.getI18nOnOff()){
			
			if(log.isDebugEnabled()){
				log.debug("-- itemDataI18nMap -- {}", JsonFormatUtil.format(itemDataI18nMap));
			}
			
			List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
			for(I18nLang i18nLang : i18nLangList){
				String langKey = i18nLang.getKey();
				String propertyValue = itemDataI18nMap.get(langKey+"-"+code+"-"+itemProperties.getPropertyId());
				if(propertyValue == null){
					propertyValue = itemDataI18nMap.get(langKey+"-"+code+"-"+itemProperties.getPropertyId()+"-"+itemProperties.getPropertyValue());
				}
				ItemPropertiesLang itemPropertiesLang = itemPropertiesLangDao.findItemPropertiesLangByItemPropertiesIdAndLang(itemProperties.getId(), langKey);
				if(itemPropertiesLang == null){
					itemPropertiesLang = new ItemPropertiesLang();
					itemPropertiesLang.setItemPropertiesId(itemProperties.getId());
					itemPropertiesLang.setLang(langKey);
				}
				itemPropertiesLang.setPropertyValue(propertyValue);
				
				itemPropertiesLangDao.save(itemPropertiesLang);
			}
		}
	}

	/**
	 * 获取商品属性值类型
	 * 
	 * @param valueType
	 * @return
	 */
	private String getPropertyValueType(Integer valueType) {
		// 值类型 1 文本 2 数值 3日期 4日期时间
		if (valueType == null)
			return "string";
		switch (valueType) {
			case 1:
				return "string";
			case 2:
				return "integer";
			case 3:
			case 4:
				return "date";
			default:
				return "string";
		}
	}

	/**
	 * 判断是否是选中的列, 只修改选中的列
	 * 
	 * @param propertyCode
	 * @param selectedColumnMap
	 * @return
	 */
	private Boolean isSelectedColumn(String propertyCode, List<String> selectedColumnList) {
		if (selectedColumnList.contains(propertyCode)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 保存商品属性
	 * 
	 * @param itemProperties
	 */
	private ItemProperties saveImpItemproperties(ItemProperties itemProperties) {
		itemProperties.setCreateTime(new Date());
		return itemPropertiesDao.save(itemProperties);
	}

	/**
	 * 修改商品属性
	 * 
	 * @param itemProperties
	 */
	private ItemProperties updateImpItemproperties(ItemProperties itemProperties) {
		ItemProperties ipt = itemPropertiesDao.getByPrimaryKey(itemProperties.getId());
		ipt.setPropertyValue(itemProperties.getPropertyValue());
		ipt.setPropertyValueId(itemProperties.getPropertyValueId());
		ipt.setModifyTime(new Date());
		return itemPropertiesDao.save(ipt);
	}
	
	/**
	 * 是否是i18n字段
	 * @param propertyCode
	 * @return
	 */
	private boolean checkIsI18nColumn(String propertyCode){
		if(Arrays.asList(I18N_COLUMN_ARRAY).contains(propertyCode)){
			return true;
		}
		return false;
	}
}
