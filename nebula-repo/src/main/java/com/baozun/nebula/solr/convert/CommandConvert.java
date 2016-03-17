package com.baozun.nebula.solr.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemForCategoryCommand;
import com.baozun.nebula.command.ItemFromSolrCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemListResultCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.ItemSolrI18nCommand;
import com.baozun.nebula.command.ItemTagCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.product.CategoryLang;
import com.baozun.nebula.model.product.ItemImageLang;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.ItemCategoryCommand;
import com.baozun.nebula.solr.command.ItemDataFromSolr;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.command.SolrGroupCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.utils.PaginationForSolr;
import com.baozun.nebula.solr.utils.PinYinUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.JsonFormatUtil;

/**
 * 用于ItemForSolrCommand与ItemCommand之间的转换
 * 
 * @author jumbo
 * 
 */
public class CommandConvert {
	
	private static final Logger log = LoggerFactory.getLogger(CommandConvert.class);
	
	/**
	 * 如果商品没有被指定置顶排序号，则默认设置为(一百万)1000000，如果在pts中设置的值比这个大则可以实现置尾排序
	 */
	public static final Integer DEFAULT_CATEGROY_ORDER = 1000000;

	/**
	 * 将Solr中查询的ItemForSolrCommand转换成正常的ItemCommand
	 * 
	 * @param itemSolr
	 * @return
	 */
	public static ItemSolrCommand itemForSolrCommandConverterItemCommand(
			ItemForSolrCommand itemSolr) {
		ItemSolrCommand item = new ItemSolrCommand();
		item.setId(itemSolr.getId());
		item.setCode(itemSolr.getCode());
		item.setTitle(itemSolr.getTitle());
		item.setSubTitle(itemSolr.getSubTitle());
		item.setSketch(itemSolr.getSketch());
		item.setDescription(itemSolr.getDescription());
		item.setShopName(itemSolr.getShopName());
		item.setIndustryName(itemSolr.getIndustryName());
		item.setList_price(itemSolr.getList_price());
		item.setSale_price(itemSolr.getSale_price());
		item.setModifyTime(itemSolr.getModifyTime());
		item.setListTime(itemSolr.getListTime());
		item.setDelistTime(itemSolr.getDelistTime());
		item.setActiveBeginTime(itemSolr.getActiveBeginTime());
		item.setSeoDescription(itemSolr.getSeoDescription());
		item.setSeoKeywords(itemSolr.getSeoKeywords());
		item.setSeoTitle(itemSolr.getSeoTitle());
		item.setRankavg(itemSolr.getRankavg());
		item.setStyle(itemSolr.getStyle());
		item.setSalesCount(itemSolr.getSalesCount());

		Date beginTime = itemSolr.getActiveBeginTime();
		item.setItemIsDisplay(true);
		// item.setTagId(itemSolr.getTagId());
		// item.setShopId(itemSolr.getShopId());
		// item.setIndustryId(itemSolr.getIndustryId());
		// item.setLifecycle(itemSolr.getLifecycle());
		// item.setCreateTime(itemSolr.getCreateTime());
		// item.setTemplateId(itemSolr.getTemplateId());
		// item.setIsaddcategory(itemSolr.getIsaddcategory());
		// item.setIsAddTag(itemSolr.getIsAddTag());
		// item.setIndustrySortNo(itemSolr.getIndustrySortNo());
		// item.setActiveEndTime(itemSolr.getActiveEndTime());
		// item.setSpread(itemSolr.getSpread());
		// if (Validator.isNullOrEmpty(beginTime)
		// && beginTime.compareTo(now)<0) {
		// item.setItemIsDisplay(true);
		// } else {
		// item.setItemIsDisplay(false);
		// }

		Map<String, List<String>> dynamicForSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicWithOutSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueForSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueWithOutSearchMap = new HashMap<String, List<String>>();
		Map<String, List<Integer>> dynamicSortForSearchMap = new HashMap<String, List<Integer>>();
		Map<String, List<Integer>> dynamicSortWithOutSearchMap = new HashMap<String, List<Integer>>();
		Map<String, String> itemPropertiesForCustomerMap = new HashMap<String, String>();
		//筛选色 商品颜色对照
		Map<String, List<String>> dynamicColorRefMap = new HashMap<String, List<String>>();

		List<Long> imgColorList = new ArrayList<Long>();
		List<String> imagerList = new ArrayList<String>();
		List<Integer> positionList = new ArrayList<Integer>();
		List<Integer> sortList = new ArrayList<Integer>();
		Map<String, Integer> categoryOrderMap = new HashMap<String, Integer>();
		Map<String, String> categoryNameMap = new HashMap<String, String>();
		Map<String, String> categoryCodeMap = new HashMap<String, String>();
		Map<String, String> tagNameMap = new HashMap<String, String>();
		Map<String, Long> categoryParentMap = new HashMap<String, Long>();

		List<ItemImageCommand> imgList = new ArrayList<ItemImageCommand>();
		Map<Long, ItemForCategoryCommand> categoryMap = new HashMap<Long, ItemForCategoryCommand>();
		List<ItemPropertiesCommand> dynForSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> dynWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> itemPropertiesForCustomerList = new ArrayList<ItemPropertiesCommand>();
		List<ItemTagCommand> tagList = new ArrayList<ItemTagCommand>();
		Map<Long, Long> colorList = new HashMap<Long, Long>();

		dynamicForSearchMap = itemSolr.getDynamicForSearchMap();
		dynamicWithOutSearchMap = itemSolr.getDynamicWithoutSearchMap();
		imagerList = itemSolr.getImageUrl();
		categoryOrderMap = itemSolr.getCategoryOrder();
		categoryParentMap = itemSolr.getCategoryParent();
		categoryCodeMap = itemSolr.getCategoryCode();
		categoryNameMap = itemSolr.getCategoryName();
		
		dynamicColorRefMap=itemSolr.getDynamicColorRefMap();

		dynamicValueForSearchMap = itemSolr.getDynamicNameForSearchMap();
		dynamicValueWithOutSearchMap = itemSolr
				.getDynamicNameWithoutSearchMap();
		itemPropertiesForCustomerMap = itemSolr.getDynamicForCustomerMap();
		// List<String> refList = new ArrayList<String>();
		// imgColorList = itemSolr.getImgColor();
		// positionList = itemSolr.getPosition();
		// tagNameMap = itemSolr.getTagNameMap();
		// dynamicSortForSearchMap = itemSolr.getDynamicForSortMap();
		// if (null != itemSolr.getColor()) {
		// for (Long colorId : itemSolr.getColor()) {
		// colorList.put(colorId, colorId);
		// }
		// }
		// refList = itemSolr.getRefInfo();

		if (null != dynamicForSearchMap) {
			for (String key : dynamicForSearchMap.keySet()) {
				List<String> propertyIdList = new ArrayList<String>();
				List<String> propertyValueList = new ArrayList<String>();
				List<Integer> propertySortList = new ArrayList<Integer>();
				Boolean isColor = false;
				String pId = key.replace(SkuItemParam.dynamicCondition, "");
				if (null != dynamicForSearchMap) {
					propertyIdList = dynamicForSearchMap.get(key);
				}
				if (null != dynamicValueForSearchMap) {
					if(LangProperty.getI18nOnOff()){
						propertyValueList = dynamicValueForSearchMap.get(SkuItemParam.dynamicConditionValueForSearch + LangUtil.getCurrentLang() + "_" + pId);
					}else{
						propertyValueList = dynamicValueForSearchMap.get(SkuItemParam.dynamicConditionValueForSearch + pId);
					}
				}
				
				/*
				 * if (null != dynamicSortForSearchMap) { propertySortList =
				 * dynamicSortForSearchMap
				 * .get(SkuItemParam.dynamicConditionSortForSearch + pId); }
				 * String colorId = key.replace(SkuItemParam.dynamicCondition,
				 * ""); if (null != colorList.get(Long.parseLong(colorId))) {
				 * isColor = true; }
				 */
				for (int i = 0; i < propertyIdList.size(); i++) {
					ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
					itemPropertiesCommand.setItemId(itemSolr.getId());
					itemPropertiesCommand.setProperty_id(pId);
					itemPropertiesCommand.setPropertyValue(propertyIdList
							.get(i));
					itemPropertiesCommand.setIs_color_prop(isColor);
					if (null != propertyValueList
							&& propertyValueList.size() > 0) {
						itemPropertiesCommand.setProValue(propertyValueList
								.get(i));
					}
					if (null != propertySortList && propertySortList.size() > 0) {
						itemPropertiesCommand.setProSort(propertySortList
								.get(i));
					}
					dynForSearchList.add(itemPropertiesCommand);
				}
			}
		}

		// TODO
		if (null != dynamicWithOutSearchMap) {
			for (String key : dynamicWithOutSearchMap.keySet()) {
				List<String> propertyIdList = new ArrayList<String>();
				List<String> propertyValueList = new ArrayList<String>();
				List<Integer> propertySortList = new ArrayList<Integer>();
				Boolean isColor = false;
				String pId = key.replace(
						SkuItemParam.dynamicConditionWithOutSearch, "");
				if (null != dynamicWithOutSearchMap) {
					propertyIdList = dynamicWithOutSearchMap.get(key);
				}
				if (null != dynamicValueWithOutSearchMap) {
					propertyValueList = dynamicValueWithOutSearchMap
							.get(SkuItemParam.dynamicConditionValueWithOutSearch
									+ pId);
				}
				/*
				 * if (null != dynamicSortWithOutSearchMap) { propertySortList =
				 * dynamicSortWithOutSearchMap
				 * .get(SkuItemParam.dynamicConditionSortWithOutSearch + pId); }
				 * String colorId =
				 * key.replace(SkuItemParam.dynamicConditionWithOutSearch, "");
				 * if (null != colorList.get(Long.parseLong(colorId))) { isColor
				 * = true; }
				 */
				for (int i = 0; i < propertyIdList.size(); i++) {
					ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
					itemPropertiesCommand.setItemId(itemSolr.getId());
					itemPropertiesCommand.setProperty_id(pId);
					itemPropertiesCommand.setPropertyValue(propertyIdList
							.get(i));
					itemPropertiesCommand.setIs_color_prop(isColor);
					if (null != propertyValueList
							&& propertyValueList.size() > 0) {
						itemPropertiesCommand.setProValue(propertyValueList
								.get(i));
					}
					if (null != propertySortList && propertySortList.size() > 0) {
						itemPropertiesCommand.setProSort(propertySortList
								.get(i));
					}
					dynWithOutSearchList.add(itemPropertiesCommand);
				}
			}
		}

		if (null != itemPropertiesForCustomerMap) {
			for (String key : itemPropertiesForCustomerMap.keySet()) {
				ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
				itemPropertiesCommand.setItemId(itemSolr.getId());
				itemPropertiesCommand.setProperty_id(key.replace(
						SkuItemParam.dynamicConditionValueForCustomer, ""));
				itemPropertiesCommand
						.setPropertyValue(itemPropertiesForCustomerMap.get(key));
				itemPropertiesForCustomerList.add(itemPropertiesCommand);
			}
		}

		if (null != categoryNameMap && null != categoryOrderMap) {
			for (String key : categoryOrderMap.keySet()) {
				ItemForCategoryCommand itemForCategoryCommand = new ItemForCategoryCommand();
				String baseKey = key.replace(SkuItemParam.categoryorder, "");
				itemForCategoryCommand.setCategoryId(Long.parseLong(baseKey));
				itemForCategoryCommand.setCategoryName(categoryNameMap
						.get(SkuItemParam.categoryname + baseKey));
				itemForCategoryCommand.setSort_no(categoryOrderMap.get(key));
				itemForCategoryCommand.setItemId(itemSolr.getId());
				itemForCategoryCommand.setCode(categoryCodeMap
						.get(SkuItemParam.categorycode + baseKey));
				Long parentId = categoryParentMap
						.get(SkuItemParam.categoryParent + baseKey);
				itemForCategoryCommand.setParent_id(null == parentId ? Long
						.parseLong("0") : parentId);
				categoryMap.put(itemForCategoryCommand.getCategoryId(),
						itemForCategoryCommand);
			}
		} else if (null != categoryNameMap) {
			for (String key : categoryNameMap.keySet()) {
				String baseKey = key.replace(SkuItemParam.categoryname, "");
				ItemForCategoryCommand itemForCategoryCommand = new ItemForCategoryCommand();
				itemForCategoryCommand.setCategoryId(Long.parseLong(baseKey));
				itemForCategoryCommand
						.setCategoryName(categoryNameMap.get(key));
				itemForCategoryCommand.setItemId(itemSolr.getId());
				Long parentId = categoryParentMap
						.get(SkuItemParam.categoryParent + baseKey);
				itemForCategoryCommand.setParent_id(null == parentId ? Long
						.parseLong("0") : parentId);
				itemForCategoryCommand.setCode(categoryCodeMap.get(SkuItemParam.categorycode+baseKey));
				categoryMap.put(itemForCategoryCommand.getCategoryId(),
						itemForCategoryCommand);
			}
		} else if (null != categoryOrderMap) {
			for (String key : categoryOrderMap.keySet()) {
				ItemForCategoryCommand itemForCategoryCommand = new ItemForCategoryCommand();
				String baseKey = key.replace(SkuItemParam.categoryorder, "");
				itemForCategoryCommand.setCategoryId(Long.parseLong(baseKey));
				itemForCategoryCommand.setSort_no(Integer
						.parseInt(categoryOrderMap.get(key).toString()));
				itemForCategoryCommand.setItemId(itemSolr.getId());
				Long parentId = categoryParentMap
						.get(SkuItemParam.categoryParent
								+ key.replace(SkuItemParam.categoryname, ""));
				itemForCategoryCommand.setParent_id(null == parentId ? Long
						.parseLong("0") : parentId);
				// itemForCategoryCommand.setCode(categoryCodeMap.get(SkuItemParam.categorycode+baseKey));
				categoryMap.put(itemForCategoryCommand.getCategoryId(),
						itemForCategoryCommand);
			}
		}

		if (null != imagerList) {
			for (int i = 0; i < imagerList.size(); i++) {
				int sortNo = 0;
				if (null != sortList && sortList.size() > 0) {
					sortNo = Integer.parseInt(sortList.get(i).toString());
				}
				ItemImageCommand itemImageCommand = new ItemImageCommand();
				itemImageCommand.setItemId(itemSolr.getId());
				itemImageCommand.setPicUrl(imagerList.get(i));
				if (null != positionList && positionList.size() > 0) {
					itemImageCommand.setPosition(positionList.get(i));
				}
				if (null != imgColorList && imgColorList.size() > 0) {
					itemImageCommand.setColor(imgColorList.get(i));
				}
				itemImageCommand.setSort_no(sortNo);
				imgList.add(itemImageCommand);
			}
		}

		if (null != tagNameMap) {
			for (String key : tagNameMap.keySet()) {
				ItemTagCommand itemTagCommand = new ItemTagCommand();
				itemTagCommand.setTag_id(Long.parseLong(key.replace(
						SkuItemParam.tagName, "")));
				itemTagCommand.setName(key);
				itemTagCommand.setItemId(itemSolr.getId());
				tagList.add(itemTagCommand);
			}
		}

		if (null != tagNameMap) {
			for (String key : tagNameMap.keySet()) {
				ItemTagCommand itemTagCommand = new ItemTagCommand();
				itemTagCommand.setTag_id(Long.parseLong(key.replace(
						SkuItemParam.tagName, "")));
				itemTagCommand.setName(key);
				itemTagCommand.setItemId(itemSolr.getId());
				tagList.add(itemTagCommand);
			}
		}
		
		item.setImageList(imgList);
		item.setCategoryMap(categoryMap);
		item.setDynamicListForSearch(dynForSearchList);
		item.setDynamicListWithOutSearch(dynWithOutSearchList);
		item.setDynamicListForCustomer(itemPropertiesForCustomerList);
		item.setTagList(tagList);
		item.setDynamicColorRef(dynamicColorRefMap);
		
		return item;
	}

	/**
	 * 根据传入的ItemCommand查询相关的ItemCategoryCommand和ItemTagRelationCommand，
	 * 整合成ItemForSolrCommand
	 * 
	 * @param ItemSolrCommand
	 * @param activeBeginTime
	 * @param activeEndTime
	 * @param channels
	 * @return
	 */
	public static ItemForSolrCommand itemCommandConverterItemForSolrCommand(
			ItemSolrCommand itemSolrCommand) {
		/**
		 * 汉语拼音汉字在后
		 */
		List<String> pinyinAllList_a = new ArrayList<String>();

		/**
		 * 汉语拼音汉字在前
		 */
		List<String> pinyinAllList_b = new ArrayList<String>();

		ItemForSolrCommand itemSolr = new ItemForSolrCommand();
		itemSolr.setId(itemSolrCommand.getId());
		itemSolr.setCode(itemSolrCommand.getCode());

		itemSolr.setTitle(itemSolrCommand.getTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getTitle());

		itemSolr.setSubTitle(itemSolrCommand.getSubTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSubTitle());

		itemSolr.setSketch(itemSolrCommand.getSketch());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSketch());

		// 去掉description中的 html字符。
		String description = itemSolrCommand.getDescription();
		itemSolr.setDescription(description);

		String descriptionForSearch = null;
		if (null != description) {
			descriptionForSearch = Jsoup.parse(description).text();
		}

		itemSolr.setDescriptionForSearch(descriptionForSearch);
		itemSolr.setShopName(itemSolrCommand.getShopName());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getShopName());

		itemSolr.setIndustryName(itemSolrCommand.getIndustryName());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getIndustryName());

		itemSolr.setList_price(itemSolrCommand.getList_price());
		itemSolr.setSale_price(itemSolrCommand.getSale_price());
		itemSolr.setModifyTime(itemSolrCommand.getModifyTime());
		itemSolr.setListTime(itemSolrCommand.getListTime());
		itemSolr.setDelistTime(itemSolrCommand.getDelistTime());
		itemSolr.setActiveBeginTime(itemSolrCommand.getActiveBeginTime());
		itemSolr.setSeoDescription(itemSolrCommand.getSeoDescription());
		itemSolr.setSeoKeywords(itemSolrCommand.getSeoKeywords());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSeoKeywords());
		itemSolr.setRankavg(itemSolrCommand.getRankavg());
		itemSolr.setStyle(itemSolrCommand.getStyle());
		itemSolr.setSalesCount(itemSolrCommand.getSalesCount());

		itemSolr.setSeoTitle(itemSolrCommand.getSeoTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSeoTitle());

		if (Validator.isNullOrEmpty(itemSolrCommand.getActiveBeginTime())
				&& Validator.isNullOrEmpty(itemSolrCommand.getActiveEndTime())) {
			itemSolr.setItemIsDisplay(true);
		} else {
			itemSolr.setItemIsDisplay(false);
		}

		// itemSolr.setActiveEndTime(itemSolrCommand.getActiveEndTime());
		// itemSolr.setTemplateId(itemSolrCommand.getTemplateId());
		// itemSolr.setIsaddcategory(itemSolrCommand.getIsaddcategory());
		// itemSolr.setIsAddTag(itemSolrCommand.getIsAddTag());
		// itemSolr.setIndustrySortNo(itemSolrCommand.getIndustrySortNo());
		// itemSolr.setLifecycle(itemSolrCommand.getLifecycle());
		// itemSolr.setCreateTime(itemSolrCommand.getCreateTime());
		// itemSolr.setShopId(itemSolrCommand.getShopId());
		// itemSolr.setIndustryId(itemSolrCommand.getIndustryId());
		// itemSolr.setSpread(itemSolrCommand.getSpread());
		// itemSolr.setTagId(itemSolrCommand.getTagId());

		List<ItemPropertiesCommand> dynamicForSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> dynamicWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> dynamicCustomerList = new ArrayList<ItemPropertiesCommand>();
		List<ItemImageCommand> imgOrColorList = new ArrayList<ItemImageCommand>();
		Map<Long, ItemForCategoryCommand> categoryMap = new HashMap<Long, ItemForCategoryCommand>();
		List<ItemTagCommand> tagList = new ArrayList<ItemTagCommand>();

		Map<String, List<String>> dynamicForSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueForSearchMap = new HashMap<String, List<String>>();

		Map<String, List<String>> dynamicWithOutSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueWithOutSearchMap = new HashMap<String, List<String>>();

		Map<String, String> dynamicValueForCoustomerMap = new HashMap<String, String>();
		List<Long> imgColorList = new ArrayList<Long>();
		List<String> imgList = new ArrayList<String>();
		List<Integer> positionList = new ArrayList<Integer>();
		Map<String, String> tagMap = new HashMap<String, String>();
		Map<String, Integer> categoryOrderMap = new HashMap<String, Integer>();
		Map<String, String> categoryNameMap = new HashMap<String, String>();
		Map<String, String> categoryCodeMap = new HashMap<String, String>();
		Map<String, Long> categoryParentMap = new HashMap<String, Long>();
		List<String> allCategoryCodes =new ArrayList<String>();
		List<Long>  allCategoryIds =new ArrayList<Long>();

		dynamicForSearchList = itemSolrCommand.getDynamicListForSearch();
		dynamicWithOutSearchList = itemSolrCommand
				.getDynamicListWithOutSearch();
		imgOrColorList = itemSolrCommand.getImageList();
		categoryMap = itemSolrCommand.getCategoryMap();
		tagList = itemSolrCommand.getTagList();
		dynamicCustomerList = itemSolrCommand.getDynamicListForCustomer();

		Map<String, String> dynamincForSearchId = new HashMap<String, String>();
		Map<String, String> dynamincWithOutSearchId = new HashMap<String, String>();

		// List<Integer> sortNoMap = new ArrayList<Integer>();
		// Map<String, List<Integer>> dynamicWithOutSearchSortMap = new
		// HashMap<String, List<Integer>>();
		// Map<String, List<Integer>> dynamicForSearchSortMap = new
		// HashMap<String, List<Integer>>();
		// List<Long> colorList = new ArrayList<Long>();

		if (null != dynamicForSearchList && dynamicForSearchList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicForSearchList) {
				dynamincForSearchId.put(itemPropertiesCommand.getProperty_id(),
						"");
			}

			for (String key : dynamincForSearchId.keySet()) {
				List<String> propertyValue = new ArrayList<String>();
				List<String> proValue = new ArrayList<String>();
				List<Integer> proSort = new ArrayList<Integer>();
				for (ItemPropertiesCommand itemPropertiesCommand : dynamicForSearchList) {
					if (key.equals(itemPropertiesCommand.getProperty_id())) {
						propertyValue.add(itemPropertiesCommand
								.getPropertyValue() == null ? ""
								: itemPropertiesCommand.getPropertyValue());
						proValue.add(itemPropertiesCommand.getProValue() == null ? ""
								: itemPropertiesCommand.getProValue());
						convertToPinYing(pinyinAllList_a, pinyinAllList_b,
								itemPropertiesCommand.getProValue());
						proSort.add(itemPropertiesCommand.getProSort() == null ? 0
								: itemPropertiesCommand.getProSort());

						dynamicForSearchMap.put(SkuItemParam.dynamicCondition
								+ itemPropertiesCommand.getProperty_id(),
								propertyValue);
						dynamicValueForSearchMap.put(
								SkuItemParam.dynamicConditionValueForSearch
										+ itemPropertiesCommand
												.getProperty_id(), proValue);
						/*
						 * dynamicForSearchSortMap.put(
						 * SkuItemParam.dynamicConditionSortForSearch +
						 * itemPropertiesCommand.getProperty_id(), proSort);
						 * 
						 * if (itemPropertiesCommand.getIs_color_prop()) {
						 * colorList.add(Long.parseLong(itemPropertiesCommand
						 * .getProperty_id())); }
						 */
					}
				}
			}
		}

		if (null != dynamicWithOutSearchList
				&& dynamicWithOutSearchList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicWithOutSearchList) {
				dynamincWithOutSearchId.put(
						itemPropertiesCommand.getProperty_id(), "");
			}

			for (String key : dynamincWithOutSearchId.keySet()) {
				List<String> propertyValue = new ArrayList<String>();
				List<String> proValue = new ArrayList<String>();
				List<Integer> proSort = new ArrayList<Integer>();
				for (ItemPropertiesCommand itemPropertiesCommand : dynamicWithOutSearchList) {
					if (key.equals(itemPropertiesCommand.getProperty_id())) {
						propertyValue.add(itemPropertiesCommand
								.getPropertyValue() == null ? ""
								: itemPropertiesCommand.getPropertyValue());
						proValue.add(itemPropertiesCommand.getProValue() == null ? ""
								: itemPropertiesCommand.getProValue());
						proSort.add(itemPropertiesCommand.getProSort() == null ? 0
								: itemPropertiesCommand.getProSort());

						dynamicWithOutSearchMap.put(
								SkuItemParam.dynamicConditionWithOutSearch
										+ itemPropertiesCommand
												.getProperty_id(),
								propertyValue);
						dynamicValueWithOutSearchMap.put(
								SkuItemParam.dynamicConditionValueWithOutSearch
										+ itemPropertiesCommand
												.getProperty_id(), proValue);
						/*
						 * dynamicWithOutSearchSortMap.put(
						 * SkuItemParam.dynamicConditionSortWithOutSearch +
						 * itemPropertiesCommand.getProperty_id(), proSort);
						 * 
						 * /* if (itemPropertiesCommand.getIs_color_prop()) {
						 * colorList.add(Long.parseLong(itemPropertiesCommand
						 * .getProperty_id())); }
						 */
					}
				}
			}
		}

		if (null != dynamicCustomerList && dynamicCustomerList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicCustomerList) {
				dynamicValueForCoustomerMap.put(
						SkuItemParam.dynamicConditionValueForCustomer
								+ itemPropertiesCommand.getProperty_id(),
						itemPropertiesCommand.getPropertyValue());
				convertToPinYing(pinyinAllList_a, pinyinAllList_b,
						itemPropertiesCommand.getPropertyValue());
			}
		}
		
		for (Long key : categoryMap.keySet()) {
			ItemForCategoryCommand itemForCategoryCommand = new ItemForCategoryCommand();
			itemForCategoryCommand = categoryMap.get(key);
			categoryOrderMap.put(SkuItemParam.categoryorder
					+ itemForCategoryCommand.getCategoryId(),
					Validator.isNullOrEmpty(itemForCategoryCommand.getSort_no()) ? DEFAULT_CATEGROY_ORDER : itemForCategoryCommand.getSort_no());
			categoryNameMap.put(SkuItemParam.categoryname
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getCategoryName());
			convertToPinYing(pinyinAllList_a, pinyinAllList_b,
					itemForCategoryCommand.getCategoryName());
			categoryParentMap.put(SkuItemParam.categoryParent
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getParent_id());
			categoryCodeMap.put(SkuItemParam.categorycode
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getCode());
			
			allCategoryCodes.add(itemForCategoryCommand.getCode());
			allCategoryIds.add(itemForCategoryCommand.getCategoryId());
		}

		if (null != imgOrColorList && imgOrColorList.size() > 0) {
			for (ItemImageCommand itemImageCommand : imgOrColorList) {
				imgColorList.add(itemImageCommand.getItemProId() == null ? null
						: itemImageCommand.getItemProId());
				imgList.add(itemImageCommand.getPicUrl() == null ? ""
						: itemImageCommand.getPicUrl());
				positionList.add(itemImageCommand.getPosition() == null ? 0
						: itemImageCommand.getPosition());

			}
		}

		if (null != tagList && tagList.size() > 0) {
			for (ItemTagCommand itemTagCommand : tagList) {
				tagMap.put(SkuItemParam.tagName + itemTagCommand.getTag_id(),
						itemTagCommand.getName());
				convertToPinYing(pinyinAllList_a, pinyinAllList_b,
						itemTagCommand.getName());
			}
		}

		itemSolr.setImageUrl(imgList);
		itemSolr.setSort_no(0);
		itemSolr.setCategoryOrder(categoryOrderMap);
		itemSolr.setCategoryCode(categoryCodeMap);		
		itemSolr.setAllCategoryCodes(allCategoryCodes);
		itemSolr.setAllCategoryIds(allCategoryIds);
		itemSolr.setCategoryName(categoryNameMap);
		itemSolr.setDynamicForSearchMap(dynamicForSearchMap);
		itemSolr.setDynamicWithoutSearchMap(dynamicWithOutSearchMap);
		itemSolr.setDynamicNameForSearchMap(dynamicValueForSearchMap);
		itemSolr.setDynamicNameWithoutSearchMap(dynamicValueWithOutSearchMap);
		itemSolr.setDynamicForCustomerMap(dynamicValueForCoustomerMap);
		itemSolr.setCategoryParent(categoryParentMap);
		itemSolr.setFavoredCount(itemSolrCommand.getFavoredCount());
		itemSolr.setPinyinAllList_A(pinyinAllList_a);
		itemSolr.setPinyinAllList_B(pinyinAllList_b);
		itemSolr.setDefault_sort(itemSolrCommand.getDefaultSort());

		// itemSolr.setTagNameMap(tagMap);
		// itemSolr.setColor(colorList);
		// itemSolr.setRefInfo(refList);
		// itemSolr.setDynamicForSortMap(dynamicForSearchSortMap);
		// itemSolr.setPosition(positionList);
		// itemSolr.setImgColor(imgColorList);
		return itemSolr;
	}

	
	public static ItemForSolrI18nCommand itemCommandConverterItemForSolrI18nCommand(
			ItemSolrI18nCommand itemSolrCommand) {
				
		/**
		 * 汉语拼音汉字在后
		 */
		List<String> pinyinAllList_a = new ArrayList<String>();

		/**
		 * 汉语拼音汉字在前
		 */
		List<String> pinyinAllList_b = new ArrayList<String>();

		ItemForSolrI18nCommand itemSolr = new ItemForSolrI18nCommand();
		Long itemId = itemSolrCommand.getId();
		itemSolr.setId(itemId);
		itemSolr.setCode(itemSolrCommand.getCode());

		itemSolr.setTitle(itemSolrCommand.getTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getTitle());

		itemSolr.setSubTitle(itemSolrCommand.getSubTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSubTitle());

		itemSolr.setSketch(itemSolrCommand.getSketch());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSketch());

		// 去掉description中的 html字符。
		String description = itemSolrCommand.getDescription();
		itemSolr.setDescription(description);
		//TODO 商品详细描述 国际化
		String descriptionForSearch = null;
		if (null != description) {
			descriptionForSearch = Jsoup.parse(description).text();
		}

		itemSolr.setDescriptionForSearch(descriptionForSearch);
		itemSolr.setShopName(itemSolrCommand.getShopName());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getShopName());

		itemSolr.setIndustryName(itemSolrCommand.getIndustryName());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getIndustryName());

		itemSolr.setList_price(itemSolrCommand.getList_price());
		itemSolr.setSale_price(itemSolrCommand.getSale_price());
		itemSolr.setModifyTime(itemSolrCommand.getModifyTime());
		itemSolr.setListTime(itemSolrCommand.getListTime());
		itemSolr.setDelistTime(itemSolrCommand.getDelistTime());
		itemSolr.setActiveBeginTime(itemSolrCommand.getActiveBeginTime());
		itemSolr.setSeoDescription(itemSolrCommand.getSeoDescription());
		itemSolr.setSeoKeywords(itemSolrCommand.getSeoKeywords());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSeoKeywords());
		itemSolr.setRankavg(itemSolrCommand.getRankavg());
		itemSolr.setStyle(itemSolrCommand.getStyle());
		itemSolr.setSalesCount(itemSolrCommand.getSalesCount());

		itemSolr.setSeoTitle(itemSolrCommand.getSeoTitle());
		convertToPinYing(pinyinAllList_a, pinyinAllList_b,
				itemSolrCommand.getSeoTitle());

		if (Validator.isNullOrEmpty(itemSolrCommand.getActiveBeginTime())
				&& Validator.isNullOrEmpty(itemSolrCommand.getActiveEndTime())) {
			itemSolr.setItemIsDisplay(true);
		} else {
			itemSolr.setItemIsDisplay(false);
		}

		List<ItemPropertiesCommand> dynamicForSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> dynamicWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> dynamicCustomerList = new ArrayList<ItemPropertiesCommand>();
		List<ItemImageCommand> imgOrColorList = new ArrayList<ItemImageCommand>();
		Map<Long, ItemForCategoryCommand> categoryMap = new HashMap<Long, ItemForCategoryCommand>();
		List<ItemTagCommand> tagList = new ArrayList<ItemTagCommand>();

		Map<String, List<String>> dynamicForSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueForSearchMap = new HashMap<String, List<String>>();

		Map<String, List<String>> dynamicWithOutSearchMap = new HashMap<String, List<String>>();
		Map<String, List<String>> dynamicValueWithOutSearchMap = new HashMap<String, List<String>>();

		Map<String, String> dynamicValueForCoustomerMap = new HashMap<String, String>();
		List<Long> imgColorList = new ArrayList<Long>();
		List<String> imgList = new ArrayList<String>();
		List<Integer> positionList = new ArrayList<Integer>();
		Map<String, String> tagMap = new HashMap<String, String>();
		Map<String, Integer> categoryOrderMap = new HashMap<String, Integer>();
		Map<String, String> categoryNameMap = new HashMap<String, String>();
		Map<String, String> dynCategoryNameMap = new HashMap<String, String>();
		Map<String, String> categoryCodeMap = new HashMap<String, String>();
		Map<String, Long> categoryParentMap = new HashMap<String, Long>();
		List<String> allCategoryCodes =new ArrayList<String>();
		List<Long>  allCategoryIds =new ArrayList<Long>();

		dynamicForSearchList = itemSolrCommand.getDynamicListForSearch();
		//TODO 商品属性国际化
		dynamicWithOutSearchList = itemSolrCommand.getDynamicListWithOutSearch();
		imgOrColorList = itemSolrCommand.getImageList();
		categoryMap = itemSolrCommand.getCategoryMap();
		tagList = itemSolrCommand.getTagList();
		//TODO 商品属性自定义国际化
		dynamicCustomerList = itemSolrCommand.getDynamicListForCustomer();

		Map<String, List<String>> dynUrlmaps =new HashMap<String, List<String>>();
		Map<String, String> dynamincForSearchId = new HashMap<String, String>();
		Map<String, String> dynamincWithOutSearchId = new HashMap<String, String>();

		if (null != dynamicForSearchList && dynamicForSearchList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicForSearchList) {
				dynamincForSearchId.put(itemPropertiesCommand.getProperty_id(), "");
			}

			for (String key : dynamincForSearchId.keySet()) {
				List<String> propertyValue = new ArrayList<String>();
				List<String> proValue = new ArrayList<String>();
				List<Integer> proSort = new ArrayList<Integer>();
				/**key:language_propertyId, value:property value list**/
				Map<String, List<String>> languagePropertyMap = new HashMap<String, List<String>>();
				for (ItemPropertiesCommand itemPropertiesCommand : dynamicForSearchList) {
					if (key.equals(itemPropertiesCommand.getProperty_id())) {
						
						//取出商品属性国际化信息
						List<ItemPropertiesLang> itemPropertiesLangList = itemPropertiesCommand.getItemPropertiesLangs();
						
						if(itemPropertiesLangList != null && !itemPropertiesLangList.isEmpty()){
							for (ItemPropertiesLang itemPropertiesLang : itemPropertiesLangList) {
								String value = itemPropertiesLang.getPropertyValue();
								String lang = itemPropertiesLang.getLang();
								if(StringUtils.isNotBlank(value)){
									proValue = languagePropertyMap.get(lang + "_" +key);
									if(proValue != null){
										proValue.add(value);
									}else{
										proValue = new ArrayList<String>();
										proValue.add(value);
									}
									languagePropertyMap.put(lang + "_" +key, proValue);
								}
							}
						}
						
						
						List<PropertyValueLang> propertyValueLangList = itemPropertiesCommand.getPropertyValueLangs();
						if(propertyValueLangList != null && !propertyValueLangList.isEmpty()) {
							for(PropertyValueLang propertyValueLang : propertyValueLangList) {
								String value = propertyValueLang.getValue();
								String lang = propertyValueLang.getLang();
								proValue = languagePropertyMap.get(lang + "_" +key);
								if(StringUtils.isNotBlank(value)){
									if(proValue != null){
										proValue.add(value);
									}else{
										proValue = new ArrayList<String>();
										proValue.add(value);
									}
									languagePropertyMap.put(lang + "_" +key, proValue);
								}
							}
						}
						
						propertyValue.add(itemPropertiesCommand.getPropertyValue() == null ? "" : itemPropertiesCommand.getPropertyValue());
						convertToPinYing(pinyinAllList_a, pinyinAllList_b, itemPropertiesCommand.getProValue());
						proSort.add(itemPropertiesCommand.getProSort() == null ? 0 : itemPropertiesCommand.getProSort());
						dynamicForSearchMap.put(SkuItemParam.dynamicCondition + itemPropertiesCommand.getProperty_id(), propertyValue);
						
						
//						propertyValue.add(itemPropertiesCommand.getPropertyValue() == null ? "" : itemPropertiesCommand.getPropertyValue());
//					
//						convertToPinYing(pinyinAllList_a, pinyinAllList_b, itemPropertiesCommand.getProValue());
//						proSort.add(itemPropertiesCommand.getProSort() == null ? 0 : itemPropertiesCommand.getProSort());
//						
//						dynamicForSearchMap.put(SkuItemParam.dynamicCondition + itemPropertiesCommand.getProperty_id(), propertyValue);
//						
//						//取出商品属性国际化信息
//						List<ItemPropertiesLang> ipls = itemPropertiesCommand.getItemPropertiesLangs();
//						
//						if(ipls != null && ipls.size() > 0){
//							for (ItemPropertiesLang ipl : ipls) {
//								String val = ipl.getPropertyValue();
//								String lang = ipl.getLang();
//								if(Validator.isNotNullOrEmpty(val) && !proValue.contains(val)){
//									proValue.add(val);
//								}
//							}
//						}
//						List<PropertyValueLang> pvlList = itemPropertiesCommand.getPropertyValueLangs();
//						if(pvlList != null && pvlList.size() > 0) {
//							for(PropertyValueLang pvl : pvlList) {
//								String val = pvl.getValue();
//								if(Validator.isNotNullOrEmpty(val) && !proValue.contains(val)){
//									proValue.add(val);
//								}
//							}
//						}
//						
//						dynamicValueForSearchMap.put(SkuItemParam.dynamicConditionValueForSearch + itemPropertiesCommand.getProperty_id(), proValue);
					}
				}
				
				if(log.isDebugEnabled()){
					log.debug("--languagePropertyMap--:{}", JsonFormatUtil.format(languagePropertyMap));
				}
				
				for(Map.Entry<String, List<String>> entry : languagePropertyMap.entrySet()){
					if(StringUtils.isNotBlank(entry.getKey()) && entry.getValue() != null){
						dynamicValueForSearchMap.put(SkuItemParam.dynamicConditionValueForSearch + entry.getKey(), entry.getValue());
					}
				}
			}
		}

		if (null != dynamicWithOutSearchList
				&& dynamicWithOutSearchList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicWithOutSearchList) {
				dynamincWithOutSearchId.put(
						itemPropertiesCommand.getProperty_id(), "");
			}

			for (String key : dynamincWithOutSearchId.keySet()) {
				List<String> propertyValue = new ArrayList<String>();
				List<String> proValue = new ArrayList<String>();
				List<Integer> proSort = new ArrayList<Integer>();
				for (ItemPropertiesCommand itemPropertiesCommand : dynamicWithOutSearchList) {
					if (key.equals(itemPropertiesCommand.getProperty_id())) {
						propertyValue.add(itemPropertiesCommand
								.getPropertyValue() == null ? ""
								: itemPropertiesCommand.getPropertyValue());
						proValue.add(itemPropertiesCommand.getProValue() == null ? ""
								: itemPropertiesCommand.getProValue());
						proSort.add(itemPropertiesCommand.getProSort() == null ? 0
								: itemPropertiesCommand.getProSort());

						dynamicWithOutSearchMap.put(
								SkuItemParam.dynamicConditionWithOutSearch
										+ itemPropertiesCommand
												.getProperty_id(),
								propertyValue);
						dynamicValueWithOutSearchMap.put(
								SkuItemParam.dynamicConditionValueWithOutSearch
										+ itemPropertiesCommand
												.getProperty_id(), proValue);
					}
				}
			}
		}

		if (null != dynamicCustomerList && dynamicCustomerList.size() > 0) {
			for (ItemPropertiesCommand itemPropertiesCommand : dynamicCustomerList) {
				List<ItemPropertiesLang> ipls = itemPropertiesCommand.getItemPropertiesLangs();
				String pId = itemPropertiesCommand.getProperty_id();
				if(ipls!=null && ipls.size()>0){
					for (ItemPropertiesLang ipl : ipls) {
						String val = ipl.getPropertyValue();
						String lang = ipl.getLang();
						dynamicValueForCoustomerMap.put(
								SkuItemParam.dynamicConditionValueForCustomer
										+ lang+"_"+pId,val);
					}
				}else{
					dynamicValueForCoustomerMap.put(
							SkuItemParam.dynamicConditionValueForCustomer
									+ pId,itemPropertiesCommand.getPropertyValue());
				}
				convertToPinYing(pinyinAllList_a, pinyinAllList_b,
						itemPropertiesCommand.getPropertyValue());
			}
		}
		
		for (Long key : categoryMap.keySet()) {
			ItemForCategoryCommand itemForCategoryCommand = new ItemForCategoryCommand();
			itemForCategoryCommand = categoryMap.get(key);
			categoryOrderMap.put(SkuItemParam.categoryorder
					+ itemForCategoryCommand.getCategoryId(),
					Validator.isNullOrEmpty(itemForCategoryCommand.getSort_no()) ? DEFAULT_CATEGROY_ORDER : itemForCategoryCommand.getSort_no());
			//TODO 分类名称国际化
			List<CategoryLang> cls = itemForCategoryCommand.getCategoryLangs();
			if(cls!=null && cls.size()>0){
				for (CategoryLang cl : cls) {
					String lang = cl.getLang();
					String cName = cl.getName();
					dynCategoryNameMap.put(SkuItemParam.dynamic_category_name
							+ lang+"_"+itemForCategoryCommand.getCategoryId(),
							cName);
				}
			}
			categoryNameMap.put(SkuItemParam.categoryname
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getCategoryName());
			convertToPinYing(pinyinAllList_a, pinyinAllList_b,
					itemForCategoryCommand.getCategoryName());
			categoryParentMap.put(SkuItemParam.categoryParent
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getParent_id());
			categoryCodeMap.put(SkuItemParam.categorycode
					+ itemForCategoryCommand.getCategoryId(),
					itemForCategoryCommand.getCode());
			allCategoryCodes.add(itemForCategoryCommand.getCode());
			allCategoryIds.add(itemForCategoryCommand.getCategoryId());
			
		}
		
		if (null != imgOrColorList && imgOrColorList.size() > 0) {
			for (ItemImageCommand itemImageCommand : imgOrColorList) {
				imgColorList.add(itemImageCommand.getItemProId() == null ? null
						: itemImageCommand.getItemProId());
				List<ItemImageLang> list = itemImageCommand.getItemImageLangs();
				if(list == null || list.isEmpty()){
					continue;
				}
				for (int i = 0; i < list.size(); i++) {
					ItemImageLang iil = list.get(i);
					String imageUrl = iil.getPicUrl() == null ? ""
							: iil.getPicUrl();
					String lang = iil.getLang();
					if(dynUrlmaps.containsKey(SkuItemParam.dynamic_imageUrl+lang)){
						dynUrlmaps.get(SkuItemParam.dynamic_imageUrl+lang).add(imageUrl);
					}else{
						List<String> urlList = new ArrayList<String>();
						urlList.add(imageUrl);
						dynUrlmaps.put(SkuItemParam.dynamic_imageUrl+lang, urlList);
					}
				}
				//语言分隔
				String imageUrl = itemImageCommand.getPicUrl() == null ? ""
								: itemImageCommand.getPicUrl();
				imgList.add(imageUrl);
				positionList.add(itemImageCommand.getPosition() == null ? 0
						: itemImageCommand.getPosition());
			}
		}

		if (null != tagList && tagList.size() > 0) {
			for (ItemTagCommand itemTagCommand : tagList) {
				tagMap.put(SkuItemParam.tagName + itemTagCommand.getTag_id(),
						itemTagCommand.getName());
				convertToPinYing(pinyinAllList_a, pinyinAllList_b,
						itemTagCommand.getName());
			}
		}

		itemSolr.setImageUrl(imgList);
		itemSolr.setSort_no(0);
		itemSolr.setDynamicCategoryName(dynCategoryNameMap);
		itemSolr.setCategoryOrder(categoryOrderMap);
		itemSolr.setCategoryCode(categoryCodeMap);
		itemSolr.setAllCategoryCodes(allCategoryCodes);
		itemSolr.setAllCategoryIds(allCategoryIds);
		itemSolr.setCategoryName(categoryNameMap);
		itemSolr.setDynamicForSearchMap(dynamicForSearchMap);
		itemSolr.setDynamicWithoutSearchMap(dynamicWithOutSearchMap);
		itemSolr.setDynamicNameForSearchMap(dynamicValueForSearchMap);
		itemSolr.setDynamicNameWithoutSearchMap(dynamicValueWithOutSearchMap);
		itemSolr.setDynamicForCustomerMap(dynamicValueForCoustomerMap);
		itemSolr.setCategoryParent(categoryParentMap);
		itemSolr.setFavoredCount(itemSolrCommand.getFavoredCount());
		itemSolr.setPinyinAllList_A(pinyinAllList_a);
		itemSolr.setPinyinAllList_B(pinyinAllList_b);
		itemSolr.setDefault_sort(itemSolrCommand.getDefaultSort());
		
		itemSolr.setDynamicTitle(itemSolrCommand.getDynamicTitle());
		itemSolr.setDynamicSubTitle(itemSolrCommand.getDynamicSubTitle());
		itemSolr.setDynamicSeoTitle(itemSolrCommand.getDynamicSeoTitle());
		itemSolr.setDynamicSketch(itemSolrCommand.getDynamicSketch());
		itemSolr.setDynamicDescription(itemSolrCommand.getDynamicDescription());
		itemSolr.setDynamicDescriptionForSearch(itemSolrCommand.getDynamicDescriptionForSearch());
		itemSolr.setDynamicSeoKeywords(itemSolrCommand.getDynamicSeoKeywords());
		itemSolr.setDynamicSeoDescription(itemSolrCommand.getDynamicSeoDescription());
		itemSolr.setDynamicImageUrl(dynUrlmaps);
		
		return itemSolr;
	}

	/**
	 * 将传入的SolrGroupData转换成前台可用的ItemCommand List 整合成ItemForSolrCommand
	 * 
	 * @param <T>
	 * 
	 * @param solrGroupData
	 * @return
	 */
	public static <T> DataFromSolr solrGroupConverterDataFromSolrWithOutGroup(
			SolrGroupData<T> solrGroupData, Integer currentPage, Integer size) {
		DataFromSolr dataFromSolr = new DataFromSolr();
		Map<String, String> categoryMap = new HashMap<String, String>();
		List<ItemResultCommand> list = new ArrayList<ItemResultCommand>();
		List<ItemForSolrCommand> it = (List<ItemForSolrCommand>) solrGroupData
				.getSolrCommandMap();
		if (null != it && it.size() > 0) {
			for (ItemForSolrCommand itemForSolrCommand : it) {
				list.add(getResult(itemForSolrCommand, null));
				if (null != itemForSolrCommand.getCategoryName()
						&& itemForSolrCommand.getCategoryName().size() > 0) {
					Map<String, String> itemCategoryMap = itemForSolrCommand
							.getCategoryName();
					for (String categoryKey : itemCategoryMap.keySet()) {
						String categoryId = categoryKey.replace(
								SkuItemParam.categoryname, "");
						categoryMap.put(categoryId, categoryId);
					}
				}
			}
		}

		Integer start = (currentPage - 1) * size;
		Long count = Long.parseLong(list.size() + "");
		PaginationForSolr<ItemResultCommand> pagination = getPagination(start,
				size, count, list);
		dataFromSolr.setItems(pagination);
		dataFromSolr.setNumber(solrGroupData.getNumFound());

		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
		dataFromSolr.setFacetMap(facetMap);
		dataFromSolr.setFacetQueryMap(facetQueryMap);
		dataFromSolr.setCategoryMap(categoryMap);
		return dataFromSolr;
	}

	@SuppressWarnings("unchecked")
	public static <T> DataFromSolr solrGroupConverterDataFromSolrWithGroup(
			SolrGroupData<T> solrGroupData, Integer currentPage, Integer size) {
		DataFromSolr dataFromSolr = new DataFromSolr();
		Map<String, String> categoryMap = new HashMap<String, String>();
		List<ItemResultCommand> list = new ArrayList<ItemResultCommand>();
		Map<String, SolrGroupCommand<T>> it = solrGroupData
				.getSolrGroupCommandMap();
		for (String key : it.keySet()) {
			SolrGroupCommand<T> solrGroupCommand = it.get(key);
			List<SolrGroup<T>> solrGroupList = solrGroupCommand
					.getItemForSolrCommandList();
			for (SolrGroup<T> solrGroup : solrGroupList) {
				List<ItemForSolrCommand> itemForSolrCommandList = (List<ItemForSolrCommand>) solrGroup
						.getBeans();
				list.add(getResult(null, itemForSolrCommandList));
				for (ItemForSolrCommand itemForSolrCommand : itemForSolrCommandList) {
					if (null != itemForSolrCommand.getCategoryName()
							&& itemForSolrCommand.getCategoryName().size() > 0) {
						Map<String, String> itemCategoryMap = itemForSolrCommand
								.getCategoryName();
						for (String categoryKey : itemCategoryMap.keySet()) {
							String categoryId = categoryKey.replace(
									SkuItemParam.categoryname, "");
							categoryMap.put(categoryId, categoryId);
						}
					}
				}
			}
		}

		Integer start = (currentPage - 1) * size;
		PaginationForSolr<ItemResultCommand> pagination = getPagination(start,
				size, solrGroupData.getNumFound(), list);
		dataFromSolr.setItems(pagination);
		dataFromSolr.setNumber(solrGroupData.getNumFound());

		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
		dataFromSolr.setFacetMap(facetMap);
		dataFromSolr.setFacetQueryMap(facetQueryMap);
		dataFromSolr.setCategoryMap(categoryMap);
		return dataFromSolr;
	}

	/**
	 * 将传入的SolrGroupData转换成前台可用的ItemCommand List
	 * 
	 * @param <T>
	 * 
	 * @param solrGroupData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ItemDataFromSolr solrGroupConverterItemDataFromSolrWithOutGroup(
			SolrGroupData<T> solrGroupData, Integer currentPage, Integer size) {
		ItemDataFromSolr dataFromSolr = new ItemDataFromSolr();
		List<ItemSolrCommand> list = new ArrayList<ItemSolrCommand>();
		ItemCategoryCommand itemCategoryCommandP = new ItemCategoryCommand();
		List<ItemForSolrCommand> it = (List<ItemForSolrCommand>) solrGroupData
				.getSolrCommandMap();
		TreeMap<Long, ItemForCategoryCommand> category = new TreeMap<Long, ItemForCategoryCommand>();
		TreeMap<Long, ItemForCategoryCommand> categoryMap = new TreeMap<Long, ItemForCategoryCommand>();
		
		itemCategoryCommandP.setId(0L);
		itemCategoryCommandP.setName("root");
		itemCategoryCommandP.setCode("root");
		itemCategoryCommandP.setPreId(null);
		
		if (null != it && it.size() > 0) {
			for (ItemForSolrCommand itemForSolrCommand : it) {
				ItemSolrCommand item = itemForSolrCommandConverterItemCommand(itemForSolrCommand);
				list.add(item);
				if(null!=item.getCategoryMap()){
					category.putAll(item.getCategoryMap());
					for(Long keyId : category.keySet()){
						categoryMap.put(keyId, category.get(keyId));
					}
				}
			}
		}
		
		
		if(null!=categoryMap){
			setCategoryLink(itemCategoryCommandP,categoryMap);
		}
		Integer start = (currentPage - 1) * size;
		Long count = Long.parseLong(list.size() + "");
		PaginationForSolr<ItemSolrCommand> pagination = getItemPaginationWithOutGroup(
				start, size, count, list);
		dataFromSolr.setItemsListWithOutGroup(pagination);
		dataFromSolr.setNumber(solrGroupData.getNumFound());

		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
		dataFromSolr.setFacetMap(facetMap);
		dataFromSolr.setFacetQueryMap(facetQueryMap);
		dataFromSolr.setItemCategoryCommand(itemCategoryCommandP);
		return dataFromSolr;
	}

	/**
	 * 将传入的SolrGroupData转换成前台可用的ItemCommand List
	 * 
	 * @param <T>
	 * 
	 * @param solrGroupData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ItemDataFromSolr solrGroupConverterItemDataFromSolrWithGroup(
			SolrGroupData<T> solrGroupData, Integer currentPage, Integer size) {
		ItemDataFromSolr dataFromSolr = new ItemDataFromSolr();
		List<ItemForSolrCommand> itemForSolrCommandList = new ArrayList<ItemForSolrCommand>();
		ItemCategoryCommand itemCategoryCommandP = new ItemCategoryCommand();
		List<List<ItemSolrCommand>> allGroupList = new ArrayList<List<ItemSolrCommand>>();
		List<ItemSolrCommand> list = null;
		TreeMap<Long, ItemForCategoryCommand> category = new TreeMap<Long, ItemForCategoryCommand>();
		TreeMap<Long, ItemForCategoryCommand> categoryMap = new TreeMap<Long, ItemForCategoryCommand>();
		Map<String, SolrGroupCommand<T>> it = solrGroupData
				.getSolrGroupCommandMap();
		for (String key : it.keySet()) {
			SolrGroupCommand<T> solrGroupCommand = it.get(key);
			List<SolrGroup<T>> solrGroupList = solrGroupCommand
					.getItemForSolrCommandList();
			for (SolrGroup<T> solrGroup : solrGroupList) {
				itemForSolrCommandList = (List<ItemForSolrCommand>) solrGroup
						.getBeans();
				list = new ArrayList<ItemSolrCommand>();
				for (ItemForSolrCommand itemForSolrCommand : itemForSolrCommandList) {
					ItemSolrCommand item = itemForSolrCommandConverterItemCommand(itemForSolrCommand);
					list.add(item);
					itemCategoryCommandP.setId(0L);
					itemCategoryCommandP.setName("root");
					itemCategoryCommandP.setCode("root");
					itemCategoryCommandP.setPreId(null);
					
					if(null!=item.getCategoryMap()){
						category.putAll(item.getCategoryMap());
						for(Long keyId : category.keySet()){
							categoryMap.put(keyId, category.get(keyId));
						}
					}
				}
				allGroupList.add(list);
				list = null;
			}
		}

		if(null!=categoryMap){
			setCategoryLink(itemCategoryCommandP,categoryMap);
		}
		
		Integer start = (currentPage - 1) * size;
		PaginationForSolr<List<ItemSolrCommand>> pagination = getItemPaginationWithGroup(
				start, size, solrGroupData.getNumFound(), allGroupList);
		dataFromSolr.setItemsListWithGroup(pagination);
		dataFromSolr.setNumber(solrGroupData.getNumFound());

		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
		dataFromSolr.setFacetMap(facetMap);
		dataFromSolr.setFacetQueryMap(facetQueryMap);
		dataFromSolr.setItemCategoryCommand(itemCategoryCommandP);
		return dataFromSolr;
	}

	/**
	 * 将传入的SolrGroupData转换成前台可用的ItemCommand List 整合成ItemForSolrCommand
	 * 
	 * @param <T>
	 * @param <T>
	 * @param solrGroupData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<ItemResultCommand> solrGroupConverterDataFromSolrByCode(
			SolrGroupData<T> solrGroupData) {
		List<ItemResultCommand> list = new ArrayList<ItemResultCommand>();
		List<ItemForSolrCommand> it = (List<ItemForSolrCommand>) solrGroupData
				.getSolrCommandMap();
		if (null != it && it.size() > 0) {
			for (ItemForSolrCommand itemForSolrCommand : it) {
				list.add(getResult(itemForSolrCommand, null));
			}
		}
		return list;
	}

	/**
	 * 将传入的SolrGroupData转换成前台可用的ItemCommand List 整合成ItemForSolrCommand
	 * 
	 * @param <T>
	 * @param <T>
	 * @param solrGroupData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<ItemSolrCommand> solrGroupConverterEligibleDataFromSolrByCode(
			SolrGroupData<T> solrGroupData) {
		List<ItemSolrCommand> list = new ArrayList<ItemSolrCommand>();
		List<ItemForSolrCommand> it = (List<ItemForSolrCommand>) solrGroupData
				.getSolrCommandMap();
		if (null != it && it.size() > 0) {
			for (ItemForSolrCommand itemForSolrCommand : it) {
				list.add(itemForSolrCommandConverterItemCommand(itemForSolrCommand));
			}
		}
		return list;
	}

	public static ItemResultCommand getResult(
			ItemForSolrCommand itemForSolrCommand,
			List<ItemForSolrCommand> itemForSolrCommandList) {
		ItemResultCommand itemResultCommand = new ItemResultCommand();

		List<Long> idList = null;
		List<String> codeList = null;
		List<String> titleList = null;
		List<String> subTitleList = null;
		List<String> sketchList = null;
		List<String> descriptionList = null;
		List<String> shopNameList = null;
		List<Long> shopIdList = null;
		List<Long> industryIdList = null;
		List<Integer> industrySortNoList = null;
		List<String> industryNameList = null;
		List<Integer> lifecycleList = null;
		List<Date> createTimeList = null;
		List<Double> list_priceList = null;
		List<Double> sale_priceList = null;
		List<Date> modifyTimeList = null;
		List<Date> listTimeList = null;
		List<Date> delistTimeList = null;
		List<Date> activeBeginTimeList = null;
		List<Date> activeEndTimeList = null;
		List<Boolean> itemIsDisplayList = null;
		List<List<String>> imageUrlList = null;
		List<Float> rankavg = null;
		List<Integer> salesCount = null;
		List<Integer> favouriteCount = null;
		List<String> style = null;
		List<Map<String, String>> categoryCodeMap = null;

		if ((null != itemForSolrCommandList && itemForSolrCommandList.size() > 0)
				|| null != itemForSolrCommand) {
			idList = new ArrayList<Long>();
			codeList = new ArrayList<String>();
			titleList = new ArrayList<String>();
			subTitleList = new ArrayList<String>();
			sketchList = new ArrayList<String>();
			descriptionList = new ArrayList<String>();
			shopNameList = new ArrayList<String>();
			shopIdList = new ArrayList<Long>();
			industryIdList = new ArrayList<Long>();
			industrySortNoList = new ArrayList<Integer>();
			industryNameList = new ArrayList<String>();
			lifecycleList = new ArrayList<Integer>();
			createTimeList = new ArrayList<Date>();
			list_priceList = new ArrayList<Double>();
			sale_priceList = new ArrayList<Double>();
			modifyTimeList = new ArrayList<Date>();
			listTimeList = new ArrayList<Date>();
			delistTimeList = new ArrayList<Date>();
			activeBeginTimeList = new ArrayList<Date>();
			activeEndTimeList = new ArrayList<Date>();
			itemIsDisplayList = new ArrayList<Boolean>();
			imageUrlList = new ArrayList<List<String>>();
			rankavg = new ArrayList<Float>();
			salesCount = new ArrayList<Integer>();
			style = new ArrayList<String>();
			favouriteCount = new ArrayList<Integer>();
			categoryCodeMap = new ArrayList<Map<String, String>>();

			if (null != itemForSolrCommandList
					&& itemForSolrCommandList.size() > 0) {
				for (int i = 0; i < itemForSolrCommandList.size(); i++) {
					idList.add(itemForSolrCommandList.get(i).getId());
					codeList.add(itemForSolrCommandList.get(i).getCode());
					titleList.add(itemForSolrCommandList.get(i).getTitle());
					subTitleList.add(itemForSolrCommandList.get(i)
							.getSubTitle());
					sketchList.add(itemForSolrCommandList.get(i).getSketch());
					descriptionList.add(itemForSolrCommandList.get(i)
							.getDescription());
					shopNameList.add(itemForSolrCommandList.get(i)
							.getShopName());
					// shopIdList.add(itemForSolrCommandList.get(i).getShopId());
					// industryIdList.add(itemForSolrCommandList.get(i).getIndustryId());
					// industrySortNoList.add(itemForSolrCommandList.get(i).getIndustrySortNo());
					industryNameList.add(itemForSolrCommandList.get(i)
							.getIndustryName());
					// lifecycleList.add(itemForSolrCommandList.get(i).getLifecycle());
					// createTimeList.add(itemForSolrCommandList.get(i).getCreateTime());
					list_priceList.add(itemForSolrCommandList.get(i)
							.getList_price());
					sale_priceList.add(itemForSolrCommandList.get(i)
							.getSale_price());
					modifyTimeList.add(itemForSolrCommandList.get(i)
							.getModifyTime());
					listTimeList.add(itemForSolrCommandList.get(i)
							.getListTime());
					delistTimeList.add(itemForSolrCommandList.get(i)
							.getDelistTime());
					activeBeginTimeList.add(itemForSolrCommandList.get(i)
							.getActiveBeginTime());
					// activeEndTimeList.add(itemForSolrCommandList.get(i).getActiveEndTime());
					itemIsDisplayList.add(itemForSolrCommandList.get(i)
							.getItemIsDisplay());
					// if(null!=itemForSolrCommandList.get(i).getImageUrl() &&
					// itemForSolrCommandList.get(i).getImageUrl().size()>0){
					// imageUrlList.add(itemForSolrCommandList.get(i).getImageUrl().get(0));
					// }else{
					// imageUrlList.add("");
					// }
					if (null != itemForSolrCommandList.get(i).getImageUrl()
							&& itemForSolrCommandList.get(i).getImageUrl()
									.size() > 0) {
						imageUrlList.add(itemForSolrCommandList.get(i)
								.getImageUrl());
					} else {
						imageUrlList.add(new ArrayList<String>());
					}
					rankavg.add(itemForSolrCommandList.get(i).getRankavg());
					salesCount.add(itemForSolrCommandList.get(i)
							.getSalesCount());
					style.add(itemForSolrCommandList.get(i).getStyle());
					favouriteCount.add(itemForSolrCommandList.get(i)
							.getFavoredCount());
					categoryCodeMap.add(solrCategoryConvert(
							SkuItemParam.categorycode, itemForSolrCommandList
									.get(i).getCategoryCode()));
				}
			}

			if (null != itemForSolrCommand) {
				idList.add(itemForSolrCommand.getId());
				codeList.add(itemForSolrCommand.getCode());
				titleList.add(itemForSolrCommand.getTitle());
				subTitleList.add(itemForSolrCommand.getSubTitle());
				sketchList.add(itemForSolrCommand.getSketch());
				descriptionList.add(itemForSolrCommand.getDescription());
				shopNameList.add(itemForSolrCommand.getShopName());
				// shopIdList.add(itemForSolrCommand.getShopId());
				// industryIdList.add(itemForSolrCommand.getIndustryId());
				// industrySortNoList.add(itemForSolrCommand.getIndustrySortNo());
				industryNameList.add(itemForSolrCommand.getIndustryName());
				// lifecycleList.add(itemForSolrCommand.getLifecycle());
				// createTimeList.add(itemForSolrCommand.getCreateTime());
				list_priceList.add(itemForSolrCommand.getList_price());
				sale_priceList.add(itemForSolrCommand.getSale_price());
				modifyTimeList.add(itemForSolrCommand.getModifyTime());
				listTimeList.add(itemForSolrCommand.getListTime());
				delistTimeList.add(itemForSolrCommand.getDelistTime());
				activeBeginTimeList
						.add(itemForSolrCommand.getActiveBeginTime());
				// activeEndTimeList.add(itemForSolrCommand.getActiveEndTime());
				itemIsDisplayList.add(itemForSolrCommand.getItemIsDisplay());
				// if(null!=itemForSolrCommand.getImageUrl() &&
				// itemForSolrCommand.getImageUrl().size()>0){
				// imageUrlList.add(itemForSolrCommand.getImageUrl().get(0));
				// }else{
				// imageUrlList.add("");
				// }

				if (null != itemForSolrCommand.getImageUrl()
						&& itemForSolrCommand.getImageUrl().size() > 0) {
					imageUrlList.add(itemForSolrCommand.getImageUrl());
				} else {
					imageUrlList.add(new ArrayList<String>());
				}

				rankavg.add(itemForSolrCommand.getRankavg());
				salesCount.add(itemForSolrCommand.getSalesCount());
				style.add(itemForSolrCommand.getStyle());
				favouriteCount.add(itemForSolrCommand.getFavoredCount());
				categoryCodeMap.add(solrCategoryConvert(
						SkuItemParam.categorycode,
						itemForSolrCommand.getCategoryCode()));
			}
		}

		itemResultCommand.setId(idList);
		itemResultCommand.setCode(codeList);
		itemResultCommand.setTitle(titleList);
		itemResultCommand.setSubTitle(subTitleList);
		itemResultCommand.setSketch(sketchList);
		itemResultCommand.setDescription(descriptionList);
		itemResultCommand.setShopName(shopNameList);
		itemResultCommand.setShopId(shopIdList);
		itemResultCommand.setIndustryId(industryIdList);
		itemResultCommand.setIndustrySortNo(industrySortNoList);
		itemResultCommand.setIndustryName(industryNameList);
		itemResultCommand.setLifecycle(lifecycleList);
		itemResultCommand.setCreateTime(createTimeList);
		itemResultCommand.setList_price(list_priceList);
		itemResultCommand.setSale_price(sale_priceList);
		itemResultCommand.setModifyTime(modifyTimeList);
		itemResultCommand.setListTime(listTimeList);
		itemResultCommand.setDelistTime(delistTimeList);
		itemResultCommand.setActiveBeginTime(activeBeginTimeList);
		itemResultCommand.setActiveEndTime(activeEndTimeList);
		itemResultCommand.setItemIsDisplay(itemIsDisplayList);
		itemResultCommand.setImg(imageUrlList);
		itemResultCommand.setRankavg(rankavg);
		itemResultCommand.setSalesCount(salesCount);
		itemResultCommand.setStyle(style);
		itemResultCommand.setFavouriteCount(favouriteCount);
		itemResultCommand.setCategoryCodeMap(categoryCodeMap);

		idList = null;
		codeList = null;
		titleList = null;
		subTitleList = null;
		sketchList = null;
		descriptionList = null;
		shopNameList = null;
		shopIdList = null;
		industryIdList = null;
		industrySortNoList = null;
		industryNameList = null;
		lifecycleList = null;
		createTimeList = null;
		list_priceList = null;
		sale_priceList = null;
		modifyTimeList = null;
		listTimeList = null;
		delistTimeList = null;
		activeBeginTimeList = null;
		activeEndTimeList = null;
		itemIsDisplayList = null;
		imageUrlList = null;
		style = null;

		return itemResultCommand;
	}

	/**
	 * solr Category转换
	 * 
	 * @param categoryMap
	 * @return
	 */
	private static Map<String, String> solrCategoryConvert(String pattern,
			Map<String, String> categoryMap) {
		Map<String, String> retMap = new HashMap<String, String>();
		if (null == pattern || null == categoryMap || categoryMap.size() == 0) {
			return retMap;
		}
		for (String categoryKey : categoryMap.keySet()) {
			String categoryId = categoryKey.replace(pattern, "");
			retMap.put(categoryId, categoryMap.get(categoryKey));
		}
		return retMap;
	}

	protected static PaginationForSolr<ItemResultCommand> getPagination(
			int start, int rows, Long count, List<ItemResultCommand> items) {
		PaginationForSolr<ItemResultCommand> page = new PaginationForSolr<ItemResultCommand>();
		page.setCount(count);
		page.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		page.setStart(start);
		page.setSize(rows);
		page.setTotalPages(rows == 0 ? 0 : (int) page.getCount() / rows
				+ (page.getCount() % rows == 0 ? 0 : 1));
		page.setItems(items);
		return page;
	}

	protected static PaginationForSolr<ItemSolrCommand> getItemPaginationWithOutGroup(
			int start, int rows, Long count, List<ItemSolrCommand> items) {
		PaginationForSolr<ItemSolrCommand> page = new PaginationForSolr<ItemSolrCommand>();
		page.setCount(count);
		page.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		page.setStart(start);
		page.setSize(rows);
		page.setTotalPages(rows == 0 ? 0 : (int) page.getCount() / rows
				+ (page.getCount() % rows == 0 ? 0 : 1));
		page.setItems(items);
		return page;
	}

	protected static PaginationForSolr<List<ItemSolrCommand>> getItemPaginationWithGroup(
			int start, int rows, Long count,
			List<List<ItemSolrCommand>> itemLists) {
		PaginationForSolr<List<ItemSolrCommand>> page = new PaginationForSolr<List<ItemSolrCommand>>();
		page.setCount(count);
		page.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		page.setStart(start);
		page.setSize(rows);
		page.setTotalPages(rows == 0 ? 0 : (int) page.getCount() / rows
				+ (page.getCount() % rows == 0 ? 0 : 1));
		page.setItems(itemLists);
		return page;
	}

	public static void convertToPinYing(List<String> pinyingAllList_a,
			List<String> pinyingAllList_b, String value) {
		if (Validator.isNotNullOrEmpty(value)) {
			pinyingAllList_a.add(PinYinUtil.getPinYinHeadChar(value) + "@"
					+ value);
			pinyingAllList_a.add(PinYinUtil.getPinYin(value) + "@" + value);

			pinyingAllList_b.add(value + "@"
					+ PinYinUtil.getPinYinHeadChar(value));
			pinyingAllList_b.add(value + "@" + PinYinUtil.getPinYin(value));
		}
	}

	public static PaginationForSolr<ItemListResultCommand> convert(
			PaginationForSolr<ItemResultCommand> resultPage) {
		PaginationForSolr<ItemListResultCommand> page = new PaginationForSolr<ItemListResultCommand>();

		if (Validator.isNotNullOrEmpty(resultPage)) {
			List<ItemResultCommand> itemResultList = resultPage.getItems();

			if (Validator.isNotNullOrEmpty(itemResultList)) {
				List<ItemListResultCommand> resultList = new ArrayList<ItemListResultCommand>();

				for (ItemResultCommand itemResultCommand : itemResultList) {
					ItemListResultCommand resultCmd = CommandConvert
							.convertToItemListResultCommand(itemResultCommand);

					resultList.add(resultCmd);
				}

				page.setItems(resultList);
				page.setCurrentPage(resultPage.getCurrentPage());
				page.setCount(resultPage.getCount());
				page.setSize(resultPage.getSize());
				page.setStart(resultPage.getStart());
				page.setTotalPages(resultPage.getTotalPages());
			} else {
				page.setItems(new ArrayList<ItemListResultCommand>());
			}

		}

		return page;
	}

	public static ItemListResultCommand convertToItemListResultCommand(
			ItemResultCommand itemResultCommand) {
		ItemListResultCommand cmd = new ItemListResultCommand();

		List<ItemCommand> cmdList = cmd.getItemCmdList();

		List<Long> id = itemResultCommand.getId();

		List<String> code = itemResultCommand.getCode();

		List<String> title = itemResultCommand.getTitle();

		List<String> subTitle = itemResultCommand.getSubTitle();

		List<String> sketch = itemResultCommand.getSketch();

		List<String> description = itemResultCommand.getDescription();

		List<String> shopName = itemResultCommand.getShopName();

		List<Long> shopId = itemResultCommand.getShopId();

		List<Long> industryId = itemResultCommand.getIndustryId();

		List<Integer> industrySortNo = itemResultCommand.getIndustrySortNo();

		List<String> industryName = itemResultCommand.getIndustryName();

		List<Integer> lifecycle = itemResultCommand.getLifecycle();

		List<Date> createTime = itemResultCommand.getCreateTime();

		List<Double> list_price = itemResultCommand.getList_price();

		List<Double> sale_price = itemResultCommand.getSale_price();

		List<Date> modifyTime = itemResultCommand.getModifyTime();

		List<Date> listTime = itemResultCommand.getListTime();

		List<Date> delistTime = itemResultCommand.getDelistTime();

		List<Date> activeBeginTime = itemResultCommand.getActiveBeginTime();

		List<Date> activeEndTime = itemResultCommand.getActiveEndTime();

		List<Boolean> itemIsDisplay = itemResultCommand.getItemIsDisplay();

		List<List<String>> img = itemResultCommand.getImg();

		List<Float> rankavg = itemResultCommand.getRankavg();

		List<Integer> salesCount = itemResultCommand.getSalesCount();

		List<Map<String, String>> categoryCodeList = itemResultCommand
				.getCategoryCodeMap();

		List<String> style = itemResultCommand.getStyle();
		if (Validator.isNotNullOrEmpty(id)) {
			for (int i = 0; i < id.size(); i++) {
				ItemFromSolrCommand itemCommand = new ItemFromSolrCommand();
				itemCommand.setId(id.get(i));
				itemCommand.setCode(code.get(i));
				itemCommand.setTitle(title.get(i));
				itemCommand.setSubTitle(subTitle.get(i));
				itemCommand.setSketch(sketch.get(i));
				itemCommand.setDescription(description.get(i));
				itemCommand.setShopName(shopName.get(i));
				// itemCommand.setShopId(shopId.get(i));
				itemCommand.setStyle(style.get(i));
				// Long indusd = industryId.get(i);
				// String idsIdStr = (indusd!=null)?indusd.toString():null;
				// itemCommand.setIndustryId(idsIdStr);
				// itemCommand.setIndustrySortNo(industrySortNo.get(i));
				itemCommand.setIndustryName(industryName.get(i));
				// itemCommand.setLifecycle(lifecycle.get(i));
				// itemCommand.setCreateTime(createTime.get(i));

				itemCommand.setListPrice(getPrice(list_price.get(i)));
				itemCommand.setSalePrice(getPrice(sale_price.get(i)));
				itemCommand.setModifyTime(modifyTime.get(i));
				itemCommand.setListTime(listTime.get(i));
				itemCommand.setDelistTime(delistTime.get(i));
				itemCommand.setActiveBeginTime(activeBeginTime.get(i));
				// itemCommand.setActiveEndTime(activeEndTime.get(i));
				itemCommand.setItemIsDisplay(itemIsDisplay.get(i));
				itemCommand.setCategoryCodeMap(categoryCodeList.get(i));
				List<String> imgList = img.get(i);

				String imgStr = "";

				if (Validator.isNotNullOrEmpty(imgList)) {
					itemCommand.setImgList(imgList);
				} else {
					imgList = new ArrayList<String>();
					imgList.add(imgStr);
					itemCommand.setImgList(imgList);
				}

				Float rank = rankavg.get(i);
				rank = (rank != null && !rank.equals(new Float(0))) ? rank * 20
						: 100;
				itemCommand.setRankavg(rank);
				itemCommand.setSalesCount(salesCount.get(i));

				cmdList.add(itemCommand);
			}
		}

		return cmd;
	}

	private static BigDecimal getPrice(Double price) {
		if (null == price) {
			return null;
		} else {
			BigDecimal result = new BigDecimal(price).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			return result;
		}
	}
	
	private static ItemCategoryCommand setCategoryLink(ItemCategoryCommand mainItemCategoryCommand,TreeMap<Long, ItemForCategoryCommand> categoryMap){
		for(Long key : categoryMap.keySet()){
			if(mainItemCategoryCommand.getId()==categoryMap.get(key).getParent_id()){
				ItemForCategoryCommand itemForCategoryCommandC= categoryMap.get(key);
				setItemCategoryCommand(mainItemCategoryCommand,itemForCategoryCommandC);
			}else{
				ItemForCategoryCommand itemForCategoryCommandC= categoryMap.get(key);
				setChildCategory(mainItemCategoryCommand.getNextItemCategoryCommand(),itemForCategoryCommandC);
			}
		}
		return mainItemCategoryCommand;
	}
	
	private static void setChildCategory(List<ItemCategoryCommand> mainItemCategoryCommand,ItemForCategoryCommand itemForCategoryCommandC){
		if(null!=mainItemCategoryCommand){
			for(ItemCategoryCommand itemChildCategoryCommand : mainItemCategoryCommand){
				if(itemChildCategoryCommand.getId()==itemForCategoryCommandC.getParent_id()){
					setItemCategoryCommand(itemChildCategoryCommand,itemForCategoryCommandC);
				}else{
					setChildCategory(itemChildCategoryCommand.getNextItemCategoryCommand(),itemForCategoryCommandC);
				}
			}
		}
	}
	
	private static void setItemCategoryCommand(ItemCategoryCommand itemCategoryCommand,ItemForCategoryCommand itemForCategoryCommand){
		List<ItemCategoryCommand> childCategoryList = new ArrayList<ItemCategoryCommand>();
		if(null!=itemCategoryCommand.getNextItemCategoryCommand()){
			childCategoryList =	itemCategoryCommand.getNextItemCategoryCommand(); 
		}
		ItemCategoryCommand itemCategoryCommandC = new ItemCategoryCommand();
		itemCategoryCommandC.setId(itemForCategoryCommand.getCategoryId());
		itemCategoryCommandC.setCode(itemForCategoryCommand.getCode());
		itemCategoryCommandC.setName(itemForCategoryCommand.getCategoryName());
		itemCategoryCommandC.setPreId(itemForCategoryCommand.getParent_id());
		childCategoryList.add(itemCategoryCommandC);
		itemCategoryCommand.setNextItemCategoryCommand(childCategoryList);
		childCategoryList = null;
	}
	
}
