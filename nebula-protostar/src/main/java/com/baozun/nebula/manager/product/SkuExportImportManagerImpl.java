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

import loxia.dao.Sort;
import loxia.support.excel.ExcelKit;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ReadStatus;
import loxia.support.excel.definition.ExcelBlock;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.definition.ExcelSheet;

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

import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.SkuExportCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.ImpSkuCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemPropertiesLangDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.extend.ItemExtendManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.feilong.core.Validator;

@Transactional
@Service("skuExportImportManager")
public class SkuExportImportManagerImpl implements SkuExportImportManager {
	

	private final Logger			log								= LoggerFactory.getLogger(ItemExportImportManagerImpl.class);

	@Autowired
	private ItemManager				itemManager;

	@Autowired
	private ShopManager				shopManager;

	@Autowired
	private IndustryManager			industryManager;

	@Autowired
	private ItemPropertiesDao		itemPropertiesDao;

	@Autowired
	private ExcelManipulatorFactory	excelFactory;
	
	@Autowired
	private SdkI18nLangManager		sdkI18nLangManager;

	@Autowired(required = false)
	private ItemExtendManager		itemExtendManager;
	
	@Autowired
	private PropertyDao 			propertyDao;
	
	@Autowired
	private SkuDao 					skuDao;
	
	@Autowired
	private ItemPropertiesLangDao 	itemPropertiesLangDao;
	
	@Autowired
	private PropertyValueDao 		propertyValueDao;


	private static final String		NUMBER_REGEX					= "^[1-9][0-9]*$";

	// 分类 筛选条件

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
	
	// 必须填写的字段
		private static String[]			REQUIRED_COLUMN					= new String[] { "code", "title", "salePrice", "itemType", ""};

		private static String[]			BIGDECIMAL_COLUMN				= new String[] { "salePrice", "listPrice" };
	
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
		
		Sort[] sorts = new Sort[1];
		sorts[0] = new Sort("p.id", "asc");
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("isSaleProp", true);
		List<Property>  propertyList = propertyDao.findEffectPropertyListByQueryMap(map);
		Map<String, String> salesPropertyMap=new HashMap<String, String>();
		if(Validator.isNotNullOrEmpty(propertyList)){
			for(Property property : propertyList){
				salesPropertyMap.put(property.getId().toString(), property.getName());
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
			generateExcelSheetByProperty(xls, industry, shopCommand,
					xls.getSheetAt(XLS_SHEET_1), selectProperties, salesPropertyMap);
			
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
					generateExcelSheetByPropertyI18n(xls, industry, shopCommand, sheet, selectProperties, salesPropertyMap, i18nLang);
				
					// 将数据写到excel模板
					if (itemCodeList != null && !itemCodeList.isEmpty()) {
						// 写入商品数据
						generateExportItemDataI18n(sheet, industry, shopCommand, selectProperties, itemCodeList, i18nLang);
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
	private void generateExcelSheetByPropertyI18n(HSSFWorkbook xls, 
			 Industry industry, ShopCommand shopCommand, HSSFSheet sheet,
			String[] selectProperties,Map<String, String> salesPropertyMap, I18nLang i18nLang) {
		// set language key
		HSSFRow languageRow = sheet.createRow(0);
		languageRow.setHeight((short)0);
		languageRow.createCell(0).setCellValue(i18nLang.getKey());
		// 生成标题行数据
		generateTitleRow(xls, sheet, selectProperties,salesPropertyMap, LANGUAGE_TITLE_ROW, false);
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
	private void generateExcelSheetByProperty(HSSFWorkbook xls, Industry industry, 
			ShopCommand shopCommand, HSSFSheet sheet, String[] selectProperties, Map<String, String> salesPropertyMap) {
		// 设置店铺信息和行业信息
		setExcelSheetHeader(industry, shopCommand, sheet);

		generateTitleRow(xls, sheet, selectProperties, salesPropertyMap, PROPERTY_NAME_ROW, true);
	}

	/**
	 * 生成标题行, code行, 是否必填行
	 * @param xls
	 * @param comomProperyMap	
	 * @param sheet
	 * @param selectProperties
	 * @param isRequiredMap
	 */
	private void generateTitleRow(HSSFWorkbook xls, HSSFSheet sheet,
			String[] selectProperties, Map<String, String> salesPropertyMap, Integer rowNum, Boolean isDefaultLang) {
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
			
			if(log.isDebugEnabled()){
				log.debug("--  title row -- propertyCode[{}], columnNum[{}], rowNum[{}]", propertyCode, columnNum, rowNum);
			}
			sheet.getRow(rowNum - 1).createCell(isDefaultLang?i:columnNum).setCellValue(propertyCode);
			
			sheet.getRow(rowNum).createCell(isDefaultLang?i:columnNum).setCellValue(converCodeToName(propertyCode, salesPropertyMap));
			
			sheet.getRow(rowNum).getCell(isDefaultLang?i:columnNum).setCellStyle(cellStyle);
			sheet.getRow(rowNum + 1).createCell(isDefaultLang?i:columnNum).setCellValue("必填");
			
			if(!isDefaultLang){
				columnNum++;
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
		List<SkuExportCommand> skuExportCommandList = itemManager.findSkuExportCommandByQueryMap(paramMap, itemCodeList);

		/** key : propertyId-itemId, value : ItemProperties */
		Map<String, ItemPropertiesCommand> itemPropertiesMap = new HashMap<String, ItemPropertiesCommand>();
		List<Long> propertyIdList = getSelectedPropertyCode(selectProperties);
		if (propertyIdList != null && !propertyIdList.isEmpty()) {
			List<ItemPropertiesCommand> itemPropertiesList = itemPropertiesDao.findItemPropertiesByQueryMap(paramMap, propertyIdList, itemCodeList);
			for (ItemPropertiesCommand itemProperties : itemPropertiesList) {
				String key = itemProperties.getId()+"-"+itemProperties.getPropertyId() + "-" + itemProperties.getItemId();
			
				itemPropertiesMap.put(key, itemProperties);
			}
		}


		List<String> selectedProperties = Arrays.asList(selectProperties);
		SkuExportCommand skuExportCommand = null;
		for (int i = 0; i < skuExportCommandList.size(); i++) {
			skuExportCommand = skuExportCommandList.get(i);
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
				Object value = getColumnDataByPropertyCode(propertyCode, selectedProperties, skuExportCommand,
						null);
				if (value == null) {
					
					String[] propertys = skuExportCommand.getProperties().replace("[", "").replace("]", "").split(",");
					if (!propertyCode.matches(NUMBER_REGEX)) {
						value = "";
					} else {
						// 一般属性的值(颜色属性)
						ItemPropertiesCommand itemProperties = null;
						if(Validator.isNotNullOrEmpty(propertys)){
							if(Validator.isNotNullOrEmpty(propertys[0])){
								itemProperties = itemPropertiesMap.get(propertys[0]+"-"+propertyCode + "-"+ skuExportCommand.getId());
							}
							if(Validator.isNullOrEmpty(itemProperties) && Validator.isNotNullOrEmpty(propertys[1])){
								itemProperties = itemPropertiesMap.get(propertys[1]+"-"+propertyCode + "-"+ skuExportCommand.getId());
							}
						}
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
			String[] selectProperties, List<String> itemCodeList, I18nLang i18nLang) {
		// 根据shopId, industryId, itemCodeList查询商品信息
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopCommand.getShopid());
		paramMap.put("industryId", industry.getId());
		List<SkuExportCommand> skuExportCommandList = itemManager.findSkuExportCommandByQueryMap(paramMap, itemCodeList);

		/** key : propertyId-itemId, value : ItemProperties */
		Map<String, ItemPropertiesCommand> itemPropertiesMap = new HashMap<String, ItemPropertiesCommand>();
		List<Long> propertyIdList = getSelectedPropertyCode(selectProperties);
		if (propertyIdList != null && !propertyIdList.isEmpty()) {
			List<ItemPropertiesCommand> itemPropertiesList = itemPropertiesDao.findItemPropertiesByQueryMapI18n(paramMap, propertyIdList, itemCodeList, i18nLang.getKey());
			for (ItemPropertiesCommand itemProperties : itemPropertiesList) {
				String key = itemProperties.getId()+"-"+itemProperties.getPropertyId() + "-" + itemProperties.getItemId();
			
				itemPropertiesMap.put(key, itemProperties);
			}
		}


		List<String> selectedProperties = Arrays.asList(selectProperties);
		SkuExportCommand skuExportCommand = null;
		for (int i = 0; i < skuExportCommandList.size(); i++) {
			skuExportCommand = skuExportCommandList.get(i);
			HSSFRow row = sheet.getRow(4 + i);
			if (row == null) {
				row = sheet.createRow(4 + i);
			}
			HSSFRow codeRow = null;
			for (int j = 0; j < selectedProperties.size(); j++) {
				codeRow = sheet.getRow(1);
				if (codeRow == null) {
					continue;
				}
				String propertyCode = selectedProperties.get(j);//codeRow.getCell(j).getStringCellValue();
				Object value = getColumnDataByPropertyCode(propertyCode, selectedProperties, skuExportCommand,
						null);
				if (value == null) {
					
					String[] propertys = skuExportCommand.getProperties().replace("[", "").replace("]", "").split(",");
					if (!propertyCode.matches(NUMBER_REGEX)) {
						value = "";
					} else {
						// 一般属性的值(颜色属性)
						ItemPropertiesCommand itemProperties = null;
						if(Validator.isNotNullOrEmpty(propertys)){
							if(Validator.isNotNullOrEmpty(propertys[0])){
								itemProperties = itemPropertiesMap.get(propertys[0]+"-"+propertyCode + "-"+ skuExportCommand.getId());
							}
							if(Validator.isNullOrEmpty(itemProperties) && Validator.isNotNullOrEmpty(propertys[1])){
								itemProperties = itemPropertiesMap.get(propertys[1]+"-"+propertyCode + "-"+ skuExportCommand.getId());
							}
						}
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
	 * 获取列的数据
	 * 
	 * @param code
	 * @param itemCommand
	 * @return
	 */
	private Object getColumnDataByPropertyCode(String propertyCode, List<String> selectedProperties,
			SkuExportCommand skuExportCommand, Map<Long, String> itemCategoryMap) {
		if (!selectedProperties.contains(propertyCode)) {
			return null;
		}

		if (skuExportCommand == null) {
			return null;
		}

		if ("code".equals(propertyCode)) {
			return skuExportCommand.getCode();
		} else if ("salePrice".equals(propertyCode)) {
			return skuExportCommand.getSalePrice();
		} else if ("listPrice".equals(propertyCode)) {
			return skuExportCommand.getListPrice();
		} else if ("upc".equals(propertyCode)) {
			return skuExportCommand.getUpc();
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
	private String converCodeToName(String code, Map<String, String> salesPropertyMap) {
		if ("code".equals(code)) {
			return "商品编码";
		}else if ("upc".equals(code)) {
			return "商品UPC编码";
		} else if ("salePrice".equals(code)) {
			return "销售价";
		} else if ("listPrice".equals(code)) {
			return "挂牌价";
		} else if (salesPropertyMap.containsKey(code)){
			return salesPropertyMap.get(code);
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

	
	/***************************************************以下是商品sku导入*****************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		ExcelReader itemImport = excelFactory.createExcelReader("skuImport");

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

		// 检查行业属性
		//List<Property> propertyList = shopManager.findPropertyListByIndustryIdAndShopId(industryId, shopId, null);
        Sort[] sorts = new Sort[1];
		sorts[0] = new Sort("p.id", "asc");
		List<Property> propertyList = shopManager.findPropertyListByIndustryId(industryId, sorts);
		
		// 拆分成 销售属性List 非销售属性
//		List<Long> notSalePropIdList = new ArrayList<Long>();

		List<Long> salePropIdList = new ArrayList<Long>();

		Map<Long, Boolean> isRequiredMap = new HashMap<Long, Boolean>();
		Map<Long, Property> propertyMap = new HashMap<Long, Property>();
		if (Validator.isNotNullOrEmpty(propertyList)) {
			for (Property property : propertyList) {
				if (property.getIsSaleProp()) {
					salePropIdList.add(property.getId());
				} else {
//					notSalePropIdList.add(property.getId());
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
//		checkCommonProperty(propertyCodeMap.get(MAP_KEY_COMMON), notSalePropIdList);

		//读取excel文件中的数据
		itemCommandSheetDefinition(itemImport, isRequiredMap, sheet, propertyCodeMap, propertyMap);

		itemBeans = new HashMap<String, Object>();
		// 读取excel文件中的商品数据到itemBeans中
		rs = itemImport.readSheet(cacher.getInputStream(), 0, itemBeans);

		if (log.isDebugEnabled()) {
			log.debug(" ------- import item propery code {}", JsonFormatUtil.format(propertyCodeMap));
			log.debug(" ------- import item data is {}", JsonFormatUtil.format(itemBeans));
		}
		
		
		/***************开启国际化后, 验证默认语言(第一个Sheet是存放默认语言的数据)***************/
		String defaultLang = (String) itemBeans.get("defaultLang");

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
		
//		sheet = importXls.getSheetAt(2);
		if(importXls.getNumberOfSheets() >= 2){
			//循环获取其他语言
			for(int i = 1; i < importXls.getNumberOfSheets(); i++){
				sheet = importXls.getSheetAt(XLS_SHEET_1 + i);
				itemImport = excelFactory.createExcelReader("skuImportI18n");
				
				//读取excel文件中的数据
				itemCommandSheetDefinitionI18n(itemImport, isRequiredMap, sheet, propertyCodeMap, propertyMap);

				itemBeans = new HashMap<String, Object>();
				// 读取excel文件中的商品数据到itemBeans中
				rs = itemImport.readSheet(cacher.getInputStream(), XLS_SHEET_1 + i, itemBeans);
				
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
					if(itemBeans.containsKey("defaultLang")){
						itemBeansI18n.put((String) itemBeans.get("defaultLang"), itemBeans);
					}
				}
			}
		}
		
		// 判断系统是否开启国际化
		if(LangProperty.getI18nOnOff()){
			Map<String, ImpSkuCommand> map = new HashMap<String, ImpSkuCommand>();
			 for (Iterator i = itemBeansI18n.keySet().iterator(); i.hasNext();) {
				 String key =  (String) i.next();
				 itemBeans = itemBeansI18n.get(key);
				 List<ImpSkuCommand> impSkuCommandList = (List<ImpSkuCommand>) itemBeans.get("ImpSkuCommand");
				 for(ImpSkuCommand impSkuCommand : impSkuCommandList){
					 if(map.containsKey(impSkuCommand.getUpc())){
						 ImpSkuCommand skuCommand = map.get(impSkuCommand.getUpc());
						 //循环销售属性
						
						 for (Iterator iter = skuCommand.getProps().keySet().iterator(); iter.hasNext();) {
							 String propKey =  (String) iter.next();
							 String propValue = skuCommand.getProps().get(propKey);
							 String propValueI18n = "";
							 if(Validator.isNotNullOrEmpty(impSkuCommand.getProps())){
								 propValueI18n = impSkuCommand.getProps().get(propKey);
							 }
							 
							 if(Validator.isNotNullOrEmpty(propValueI18n)){
								 propValue =  propValue +","+key+"|"+propValueI18n;
								 skuCommand.getProps().put(propKey, propValue);
							 }else{
								 //抛异常销售属性没有
								 throw new BusinessException(ErrorCodes.IMPORT_SKU_PORP_NOT_COMPLETE);
							 }
						 }
						 
						 //合并重复的国际化导入信息
						 map.put(skuCommand.getUpc(), skuCommand);
					 }else{
						 if(Validator.isNotNullOrEmpty(impSkuCommand.getProps())){
							 for (Iterator iter = impSkuCommand.getProps().keySet().iterator(); iter.hasNext();) {
								 String propKey =  (String) iter.next();
								 String propValue = impSkuCommand.getProps().get(propKey);
								 
								 if(Validator.isNotNullOrEmpty(propValue)){
									 propValue =  key+"|"+propValue;
									 impSkuCommand.getProps().put(propKey, propValue);
								 }else{
									 //抛异常销售属性没有
									 throw new BusinessException(ErrorCodes.IMPORT_SKU_PORP_NOT_COMPLETE);
								 }
							 }
						 }else{
							 //抛异常
							 throw new BusinessException(ErrorCodes.IMPORT_SKU_PORP_NOT_COMPLETE);
						 }
						 
						 map.put(impSkuCommand.getUpc(), impSkuCommand);
					 }
				 }
			 }
			 
			 
			 //验证销售属性是否全部都存在
			 for (Iterator i = map.keySet().iterator(); i.hasNext();) {
				 String key =  (String) i.next();
				 
				 ImpSkuCommand impSkuCommand = map.get(key);
				 
				 for (Iterator iter = impSkuCommand.getProps().keySet().iterator(); iter.hasNext();) {
					 String propKey =  (String) iter.next();
					 String propValue = impSkuCommand.getProps().get(propKey);
					 
					 //销售属性id
					 Long pro_id = Long.valueOf(propKey.replace("p", ""));
					 Long pro_val_id = null;
					 
					 String[] propValues = propValue.split(",");
					 if(Validator.isNotNullOrEmpty(propValues)){
						 for(String str : propValues){
							 String[] strs = str.split("\\|");
							 if(strs.length >= 2){
								 PropertyValue propertyValue = propertyValueDao.findPropertyI18nByValue(pro_id, strs[1], strs[0]);
								 
								 if(Validator.isNullOrEmpty(propertyValue)){
									//抛异常(国际化属性不存在)
									 throw new BusinessException(ErrorCodes.IMPORT_SKU_IL8N_PORP_NOT_COMPLETE);
								 }
								 
								 if(Validator.isNullOrEmpty(pro_val_id)){
									 pro_val_id = propertyValue.getId();
								 }else{
									 if(!pro_val_id.equals(propertyValue.getId())){
										//抛异常(国际化属性不存在)
										 throw new BusinessException(ErrorCodes.IMPORT_SKU_IL8N_PORP_NOT_COMPLETE);
									 }
								 }
							 }else{
								 //抛异常
								 throw new BusinessException(ErrorCodes.IMPORT_SKU_PORP_NOT_COMPLETE);
							 }
						 }
					 }else{
						//抛异常
						throw new BusinessException(ErrorCodes.IMPORT_SKU_PORP_NOT_COMPLETE);
					 }
					 
					 //使用国际化属性 临时存储 销售属性值
					 impSkuCommand.getPropsI18n().put(pro_id.toString(), pro_val_id.toString());
				 }
			 }
			 
			 //循环修改数据
			 for (Iterator i = map.keySet().iterator(); i.hasNext();) {
				 String key =  (String) i.next();
				 
				 ImpSkuCommand impSkuCommand = map.get(key);
				 
				 Sku sku = skuDao.findSkuByExtentionCode(impSkuCommand.getUpc());
				 
				 if(Validator.isNullOrEmpty(sku) || Validator.isNullOrEmpty(sku.getItemId())){
					 //导入的UPC数据不正确
					 throw new BusinessException(ErrorCodes.IMPORT_SKU_UPC_ERROR);
				 }else{
					 ItemInfoCommand  itemInfoCommand = itemManager.findItemInfoCommandByItemId(sku.getItemId());
					 if(Validator.isNullOrEmpty(itemInfoCommand)){
						 //导入的UPC数据不正确
						 throw new BusinessException(ErrorCodes.IMPORT_SKU_UPC_ERROR);
					 }
					 
					 /*if(!impSkuCommand.getCode().equals(itemInfoCommand.getCode())){
						 //upc和code不一致
						 throw new BusinessException(ErrorCodes.IMPORT_SKU_UPC_ERROR);
					 }*/
					 
					 if(impSkuCommand.getListPrice().compareTo(itemInfoCommand.getListPrice()) > 0){
						//listPrice过大
						 throw new BusinessException(ErrorCodes.IMPORT_SKU_UPC_ERROR);
					 }
					 if(impSkuCommand.getSalePrice().compareTo(itemInfoCommand.getSalePrice()) > 0){
						 //salePrice过大
						 throw new BusinessException(ErrorCodes.IMPORT_SKU_SALEPRICE_ERROR);
					 }
					 sku.setSalePrice(impSkuCommand.getSalePrice());
					 sku.setListPrice(impSkuCommand.getListPrice());
					 
				 }
				 
				 
				 String skuProperties = sku.getProperties();
				 skuProperties = skuProperties.replace("[", "").replace("]", "");
				 String[] propertyIds = skuProperties.split(",");
				 if(Validator.isNotNullOrEmpty(propertyIds)){
					 for(String propertyId : propertyIds){
						 List<Long> ids = new ArrayList<Long>();
						 ids.add(Long.valueOf(propertyId.trim()));
						 itemPropertiesDao.deleteItemPropertiesLangByIds(ids);
						 itemPropertiesDao.deleteItemPropertiesByIds(ids);
					 }
				 }
				 
				 skuProperties ="[";
				 
				 for (Iterator iter = impSkuCommand.getPropsI18n().keySet().iterator(); iter.hasNext();) {
					 String propKey =  (String) iter.next();
					 String propValue = impSkuCommand.getPropsI18n().get(propKey);
					 
					 ItemProperties itemProperties = new ItemProperties();

			        itemProperties.setItemId(sku.getItemId());
			        itemProperties.setPropertyId(Long.valueOf(propKey));
			        itemProperties.setPropertyValueId(Long.valueOf(propValue));

			        Date now = new Date();
			        itemProperties.setCreateTime(now);
			        itemProperties.setVersion(now);
			        // 创建商品属性关联
			        itemProperties = itemPropertiesDao.save(itemProperties);
			        
			        //拼接sku的skuProperties
			        skuProperties = skuProperties + itemProperties.getId()+",";
			        
			        List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
				 	
			        if (Validator.isNotNullOrEmpty(i18nLangList)){
			            //国际化属性
			            for (I18nLang lang : i18nLangList){
			                ItemPropertiesLang itemPropertiesLang = new ItemPropertiesLang();
			                itemPropertiesLang.setItemPropertiesId(itemProperties.getId());
			                itemPropertiesLang.setLang(lang.getKey());
			                itemPropertiesLangDao.save(itemPropertiesLang);
			            }
			        }
				 }
				 
				 //拼接sku表的属性组
				 if(Validator.isNotNullOrEmpty(skuProperties)){
					 skuProperties = skuProperties.substring(0, skuProperties.length()-1);
					 skuProperties = skuProperties +"]";
					 
					 sku.setProperties(skuProperties);
					 skuDao.save(sku);
				 }else{
					 throw new BusinessException(ErrorCodes.IMPORT_SKU_PROPERTYS_NOT_COMPLETE);
				 }
				 
			 }
			 
			 
			 
			
			
			
		}

		return null;
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
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A9", "D9");
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
		ExcelBlock blockDefinition = sheetDefinition.getExcelBlock("A5", "D5");
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

}
