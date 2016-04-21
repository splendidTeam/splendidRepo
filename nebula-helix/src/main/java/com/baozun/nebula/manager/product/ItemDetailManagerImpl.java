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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.ErrorCodesFoo;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkPromotionGuideManager;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.google.gson.Gson;

/**
 * 商品详细业务逻辑实现层
 * 
 * @author chenguang.zhou
 * @date 2014-3-6 下午06:16:44
 **/
@Transactional
@Service("itemDetailManager")
public class ItemDetailManagerImpl implements ItemDetailManager {

	private static final Logger			log					= LoggerFactory.getLogger(ItemDetailManagerImpl.class);

	@Autowired
	private SdkItemManager				sdkItemManager;

	@Autowired
	private SdkPromotionGuideManager	sdkPromotionGuideManager;

	@Autowired
	private ItemListManager				itemListManager;

	@Transactional(readOnly=true)
	@Override
	public ItemBaseCommand findItemBaseInfo(Long itemId, String customBaseUrl) {
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoLang(itemId);
		if (itemBaseCommand == null) {
			throw new BusinessException(ErrorCodesFoo.ITEM_NOT_EXIST);
		}
		// 给商品描述的html中的图片加上域名地址(customBaseUrl)
		String desc = itemBaseCommand.getDescription();
		if (StringUtils.isNotBlank(customBaseUrl) && StringUtils.isNotBlank(desc)) {
			Document document = Jsoup.parse(desc);
			Elements elements = document.select("img");
			for (Element element : elements) {
				String src = customBaseUrl + element.attr("src");
				element.attr("src", src);
			}
			itemBaseCommand.setDescription(document.body().html());
		}

		return itemBaseCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public ItemBaseCommand findItemBaseInfoByCode(String code) {
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoByCode(code);
		if (itemBaseCommand == null) {
			throw new BusinessException(ErrorCodesFoo.ITEM_NOT_EXIST);
		}
		return itemBaseCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public List<CurmbCommand> findCurmbList(Long itemId) {
		ItemCategory itemCategory = sdkItemManager.findDefaultCateoryByItemId(itemId);
		List<CurmbCommand> curmbCommandList = null;
		if (itemCategory != null) {
			// 默认分类
			curmbCommandList = itemListManager.findCurmbList(itemCategory.getCategoryId());
		}
		return curmbCommandList;
	}

	@Transactional(readOnly=true)
	@Override
	public Map<String, Object> findDynamicProperty(Long itemId) {
		List<ItemProperties> dbItemPropertiesList = sdkItemManager.findItemPropertiesByItemId(itemId);
		// 商品的动态属性Map
		Map<String, Object> responseMap = getDynamicPropertyMap(dbItemPropertiesList, itemId);
		return responseMap;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Map<String, Object> gatherDynamicProperty(Long itemId) {
		List<ItemProperties> dbItemPropertiesList = sdkItemManager.findItemPropertiesByItemId(itemId);
		// 商品的动态属性Map
		Map<String, Object> responseMap = gatherDynamicPropertyMap(dbItemPropertiesList, itemId);
		return responseMap;
	}

	@Transactional(readOnly=true)
	@Override
	public SkuCommand findInventory(Long itemId, String itemProperties) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);
		if (StringUtils.isNotBlank(itemProperties)) {
			String[] itemPropStrs = itemProperties.split(",");
			List<Long> itemProps = new ArrayList<Long>();
			for (String str : itemPropStrs) {
				itemProps.add(Long.valueOf(str));
			}
			Collections.sort(itemProps);
			Gson gson = new Gson();
			itemProperties = gson.toJson(itemProps);
			paramMap.put("itemProperties", itemProperties);
		}
		SkuCommand skuCommand = sdkItemManager.findInventory(paramMap);

		// 当库存为null是修改为0
		if (skuCommand != null && skuCommand.getAvailableQty() == null) {
			skuCommand.setAvailableQty(0);
		}

		if (skuCommand == null) {
			log.info("itemId is {} and itemProperties is {} don't exist sku!", itemId, itemProperties);
		}
		return skuCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public List<ItemImage> findItemImgList(Long itemId, String itemProperties, String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);
		if (StringUtils.isNotBlank(itemProperties)) {
			paramMap.put("itemProperties", Long.valueOf(itemProperties));
		}
		paramMap.put("type", type);
		List<ItemImage> itemImageList = sdkItemManager.findItemImgNormsByItemIdItemProp(paramMap);
		return itemImageList;
	}

	@Transactional(readOnly=true)
	@Override
	public List<ItemCommand> recentViewItemList(List<Long> itemIds, String imgType) {
		List<Long> itemIdList = new ArrayList<Long>();
		for (Long itemId : itemIds) {
			if (itemId != null) {
				itemIdList.add(itemId);
			}
		}
		List<ItemCommand> itemCommandList = sdkItemManager.findItemCommandByItemIds(itemIdList);
		List<ItemImageCommand> itemImageCommandList = sdkItemManager.findItemImagesByItemIds(itemIdList, imgType);
		Map<Long, String> itemImageMap = new HashMap<Long, String>();
		for (ItemImageCommand itemImageCommand : itemImageCommandList) {
			List<ItemImage> itemImageList = itemImageCommand.getItemIamgeList();
			if (null != itemImageList && itemImageList.size() > 0) {
				itemImageMap.put(itemImageCommand.getItemId(), itemImageList.get(0).getPicUrl());
			}
		}

		Map<Long, ItemCommand> recentViewMap = new HashMap<Long, ItemCommand>();
		for (ItemCommand itemCommand : itemCommandList) {
			recentViewMap.put(itemCommand.getId(), itemCommand);
		}
		itemCommandList.clear();
		for (Long itemId : itemIdList) {
			ItemCommand itemCommand = recentViewMap.get(itemId);
			if (null != itemCommand) {
				itemCommand.setPicUrl(itemImageMap.get(itemId));
				itemCommandList.add(itemCommand);
			}
		}
		return itemCommandList;
	}

	@Transactional(readOnly=true)
	@Override
	public String findItemDesc(Long itemId) {
		ItemInfo itemInfo = sdkItemManager.findItemInfoByItemId(itemId);
		return itemInfo.getDescription();
	}

	/**
	 * 获得商品的动态属性Map
	 * 
	 * @param dbItemPropertiesList
	 *            :数据库存中的商品属性集合
	 * 
	 * @return Map<String, Object>中存放的是: 分组名称集合(groupNameList), 销售属性集合(salePropCommandList),
	 *         一般属性集合(generalPropCommandList)
	 */
	private Map<String, Object> getDynamicPropertyMap(List<ItemProperties> dbItemPropertiesList, Long itemId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DynamicPropertyCommand> salePropCommandList = new ArrayList<DynamicPropertyCommand>();
		List<DynamicPropertyCommand> generalPropCommandList = new ArrayList<DynamicPropertyCommand>();
		DynamicPropertyCommand salePropCommand = null;
		DynamicPropertyCommand generalPropCommand = null;
		List<ItemPropertiesCommand> itemPropertiesList = null;
		// 属性Id集合
		Set<Long> propertyIdSet = new HashSet<Long>();
		List<Long> propertyIds = new ArrayList<Long>();
		// 属性值Id集合
		Set<Long> propertyValueIdSet = new HashSet<Long>();
		List<Long> propertyValueIds = new ArrayList<Long>();
		// 分组名称集合
		Map<String, String> groupNameMap = new HashMap<String, String>();
		List<String> groupNameList = new ArrayList<String>();
		// 获得"属性Id集合"和"属性值Id集合"
		for (ItemProperties itemProperties : dbItemPropertiesList) {
			Long propertyId = itemProperties.getPropertyId();
			propertyIdSet.add(propertyId);
			Long propertyValueId = itemProperties.getPropertyValueId();
			if (propertyValueId != null) {
				propertyValueIdSet.add(propertyValueId);
			}
		}
		propertyIds.addAll(propertyIdSet);
		propertyValueIds.addAll(propertyValueIdSet);
		// 通过"属性id的集合"获得"属性集合"
		Sort[] sorts = Sort.parse("sort_no asc");
		List<Property> propertyList = sdkItemManager.findPropertyListByIds(propertyIds, sorts);
		// 通过"属性值id的集合"获得"属性值集合"
		List<PropertyValue> propertyValueList = sdkItemManager.findPropertyValueListByIds(propertyValueIds);

		Map<Long, String> propertyValueMap = new HashMap<Long, String>();
		for (PropertyValue propertyValue : propertyValueList) {
			propertyValueMap.put(propertyValue.getId(), propertyValue.getValue());
		}
		
		Map<Long,Integer> propertySortMap =new HashMap<Long, Integer>();
		for (PropertyValue propertyValue : propertyValueList) {
			propertySortMap.put(propertyValue.getId(), propertyValue.getSortNo());
		}
		

		ItemPropertiesCommand tempConvertIp =null;
		
		for (Property property : propertyList) {
			Boolean isSaleProp = property.getIsSaleProp();
			// 分离销售属性与一般属性
			if (isSaleProp) {
				// 销售属性
				salePropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList) {
					if (itemProperties.getPropertyId().equals(property.getId())) {
						if (itemProperties.getPropertyValueId() == null) {
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						} else {
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							//非自定义多选 设置排序
							tempConvertIp =itemPropertiesToCommand(itemProperties);
							tempConvertIp.setProSort(propertySortMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(tempConvertIp);
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1) {
					salePropCommand.setItemProperties(itemPropertiesList.get(0));
				} else {
					salePropCommand.setItemPropertiesList(itemPropertiesList);
				}
				salePropCommand.setProperty(property);
				salePropCommandList.add(salePropCommand);
			} else {

				String groupName = property.getGroupName();
				// 当groupName在groupNameMap中不存在时, 将groupName增加到 groupNameList和groupNameMap中;
				if (StringUtils.isNotBlank(groupName)) {
					if (StringUtils.isBlank(groupNameMap.get(groupName))) {
						groupNameMap.put(groupName, groupName);
						groupNameList.add(groupName);
					}
				}

				// 一般属性
				generalPropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList) {
					if (itemProperties.getPropertyId().equals(property.getId())) {
						if (itemProperties.getPropertyValueId() == null) {
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						} else {
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1) {
					generalPropCommand.setItemProperties(itemPropertiesList.get(0));
				} else {
					generalPropCommand.setItemPropertiesList(itemPropertiesList);
				}
				generalPropCommand.setProperty(property);
				generalPropCommandList.add(generalPropCommand);
			}
		}
		List<DynamicPropertyCommand> salePropList = salePropHandle(salePropCommandList, itemId);
		responseMap.put("groupNameList", groupNameList);
		responseMap.put("salePropCommandList", salePropList);
		responseMap.put("generalPropCommandList", generalPropCommandList);
		return responseMap;
	}
	
	
	private Map<String, Object> gatherDynamicPropertyMap(List<ItemProperties> dbItemPropertiesList, Long itemId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DynamicPropertyCommand> salePropCommandList = new ArrayList<DynamicPropertyCommand>();
		List<DynamicPropertyCommand> generalPropCommandList = null;
		DynamicPropertyCommand salePropCommand = null;
		DynamicPropertyCommand generalPropCommand = null;
		List<ItemPropertiesCommand> itemPropertiesList = null;
		// 属性Id集合
		Set<Long> propertyIdSet = new HashSet<Long>();
		List<Long> propertyIds = new ArrayList<Long>();
		// 属性值Id集合
		Set<Long> propertyValueIdSet = new HashSet<Long>();
		List<Long> propertyValueIds = new ArrayList<Long>();
		// [分组名称,一般属性集合]
		Map<String, List<DynamicPropertyCommand>> generalGroupPropMap = new HashMap<String, List<DynamicPropertyCommand>>();
		// 获得"属性Id集合"和"属性值Id集合"
		for (ItemProperties itemProperties : dbItemPropertiesList) {
			Long propertyId = itemProperties.getPropertyId();
			propertyIdSet.add(propertyId);
			Long propertyValueId = itemProperties.getPropertyValueId();
			if (propertyValueId != null) {
				propertyValueIdSet.add(propertyValueId);
			}
		}
		propertyIds.addAll(propertyIdSet);
		propertyValueIds.addAll(propertyValueIdSet);
		// 通过"属性id的集合"获得"属性集合"
		Sort[] sorts = Sort.parse("sort_no asc");
		List<Property> propertyList = sdkItemManager.findPropertyListByIds(propertyIds, sorts);
		// 通过"属性值id的集合"获得"属性值集合"
		List<PropertyValue> propertyValueList = sdkItemManager.findPropertyValueListByIds(propertyValueIds);

		Map<Long, String> propertyValueMap = new HashMap<Long, String>();
		for (PropertyValue propertyValue : propertyValueList) {
			propertyValueMap.put(propertyValue.getId(), propertyValue.getValue());
		}
		
		Map<Long,Integer> propertySortMap =new HashMap<Long, Integer>();
		for (PropertyValue propertyValue : propertyValueList) {
			propertySortMap.put(propertyValue.getId(), propertyValue.getSortNo());
		}
		

		ItemPropertiesCommand tempConvertIp =null;
		
		for (Property property : propertyList) {
			Boolean isSaleProp = property.getIsSaleProp();
			// 分离销售属性与一般属性
			if (isSaleProp) {
				// 销售属性
				salePropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList) {
					if (itemProperties.getPropertyId().equals(property.getId())) {
						if (itemProperties.getPropertyValueId() == null) {
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						} else {
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							//非自定义多选 设置排序
							tempConvertIp =itemPropertiesToCommand(itemProperties);
							tempConvertIp.setProSort(propertySortMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(tempConvertIp);
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1) {
					salePropCommand.setItemProperties(itemPropertiesList.get(0));
				} else {
					salePropCommand.setItemPropertiesList(itemPropertiesList);
				}
				salePropCommand.setProperty(property);
				salePropCommandList.add(salePropCommand);
			} else {
				// 一般属性
				String groupName =property.getGroupName();
				generalPropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList) {
					if (itemProperties.getPropertyId().equals(property.getId())) {
						if (itemProperties.getPropertyValueId() == null) {
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						} else {
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1) {
					generalPropCommand.setItemProperties(itemPropertiesList.get(0));
				} else {
					generalPropCommand.setItemPropertiesList(itemPropertiesList);
				}
				generalPropCommand.setProperty(property);
				if(Validator.isNotNullOrEmpty(generalGroupPropMap)&&
						generalGroupPropMap.get(groupName).contains(generalPropCommand)){
					continue ;
				}else{
					generalPropCommandList = Validator.isNullOrEmpty(generalGroupPropMap) ? new ArrayList<DynamicPropertyCommand>() :
						generalGroupPropMap.get(groupName);
				}
				generalPropCommandList.add(generalPropCommand);
				generalGroupPropMap.put(groupName, generalPropCommandList);
			}
		}
		List<DynamicPropertyCommand> salePropList = salePropHandle(salePropCommandList, itemId);
		responseMap.put("salePropCommandList", salePropList);
		responseMap.put("generalGroupPropMap", generalGroupPropMap);
		return responseMap;
	}

	/**
	 * 处理销售属性
	 * 
	 * @param salePropCommandList
	 * @return
	 */
	private List<DynamicPropertyCommand> salePropHandle(List<DynamicPropertyCommand> salePropCommandList, Long itemId) {
		List<DynamicPropertyCommand> salePropList = new ArrayList<DynamicPropertyCommand>();
		List<ItemPropertiesCommand> itemPropCommandList = null;
		// key:extentionCode, value:库存数
		Map<String, Integer> extentionCodeMap = new HashMap<String, Integer>();
		// 通过ItemId获得sku集合
		List<Sku> skuList = sdkItemManager.findSkuByItemId(itemId);
		// 获取outId集合
		List<String> outIdList = new ArrayList<String>();
		for (Sku sku : skuList) {
			String outId = sku.getOutid();
			if (StringUtils.isNotBlank(outId)) {
				outIdList.add(outId);
			}
		}
		// 通过extentionCode集合获得商品的库存信息
		List<SkuInventory> skuInventoryList = sdkItemManager.findSkuInventoryByExtentionCodes(outIdList);
		for (SkuInventory skuInventory : skuInventoryList) {
			extentionCodeMap.put(skuInventory.getExtentionCode(), skuInventory.getAvailableQty());
		}

		for (DynamicPropertyCommand saleProp : salePropCommandList) {
			ItemPropertiesCommand itemProperties = saleProp.getItemProperties();
			// 单个属性值
			if (null != itemProperties) {
				sku: for (Sku sku : skuList) {
					String properties = sku.getProperties();
					String extentionCode = sku.getOutid();
					if (StringUtils.isNotBlank(properties)) {
						// 条件:
						// 1, itemProperties.Id是否在sku.properties中
						// 2, extentionCode不为空, 且库存大于0
						properties = properties.substring(1, properties.length() - 1);
						if (properties.indexOf(",") != -1) {
							String[] props = properties.split(",");
							for (String str : props) {
								if (str.equals(String.valueOf(itemProperties.getItem_properties_id()))
										&& isExistSkuInventory(extentionCode, extentionCodeMap)) {
									itemProperties.setIsEnabled(true);
									break sku;
								}
							}
						} else {
							if (properties.equals(String.valueOf(itemProperties.getItem_properties_id()))
									&& isExistSkuInventory(extentionCode, extentionCodeMap)) {
								itemProperties.setIsEnabled(true);
								break sku;
							}
						}
					}
				}
				saleProp.setItemProperties(itemProperties);
			} else {
				// 多个属性值
				itemPropCommandList = new ArrayList<ItemPropertiesCommand>();
				for (ItemPropertiesCommand itemProp : saleProp.getItemPropertiesList()) {
					sku: for (Sku sku : skuList) {
						String properties = sku.getProperties();
						String extentionCode = sku.getOutid();
						if (StringUtils.isNotBlank(properties)) {
							// itemProperties.Id是否在sku.properties中
							properties = properties.substring(1, properties.length() - 1);
							if (properties.indexOf(",") != -1) {
								String[] props = properties.split(",");
								for (String str : props) {
									if (str.equals(String.valueOf(itemProp.getItem_properties_id()))
											&& isExistSkuInventory(extentionCode, extentionCodeMap)) {
										itemProp.setIsEnabled(true);
										break sku;
									}
								}
							} else {
								if (properties.equals(String.valueOf(itemProp.getItem_properties_id()))
										&& isExistSkuInventory(extentionCode, extentionCodeMap)) {
									itemProp.setIsEnabled(true);
									break sku;
								}
							}
						}
					}
					itemPropCommandList.add(itemProp);
				}
				saleProp.setItemPropertiesList(itemPropCommandList);
			}
			salePropList.add(saleProp);
		}
		return salePropList;
	}

	/**
	 * 是否存在库存 :extentionCode不为空, 且库存大于0
	 * 
	 * @param extentionCode
	 * @param extentionCodeMap
	 * @return
	 */
	private Boolean isExistSkuInventory(String extentionCode, Map<String, Integer> extentionCodeMap) {
		if (StringUtils.isNotBlank(extentionCode) && extentionCodeMap != null
				&& extentionCodeMap.get(extentionCode) != null && extentionCodeMap.get(extentionCode) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param itemProperties
	 * @return
	 */
	private ItemPropertiesCommand itemPropertiesToCommand(ItemProperties itemProperties) {
		ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
		itemPropertiesCommand.setIsEnabled(false);
		itemPropertiesCommand.setPropertyId(itemProperties.getPropertyId());
		itemPropertiesCommand.setPropertyValue(itemProperties.getPropertyValue());
		itemPropertiesCommand.setItem_properties_id(itemProperties.getId());
		itemPropertiesCommand.setItemId(itemProperties.getItemId());
		return itemPropertiesCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public List<SkuCommand> findInventoryByItemId(Long itemId) {
		return sdkItemManager.findInventoryByItemId(itemId);
	}

	@Transactional(readOnly=true)
	@Override
	public ItemCommand findItemByCode(String itemCode) {
		ItemCommand itemCommand = sdkItemManager.findItemByCode(itemCode);
		if (itemCommand == null) {
			throw new BusinessException(ErrorCodes.ITEM_NOT_EXIST);
		}
		return itemCommand;
	}

	@Transactional(readOnly=true)
	@Override
	public Float findItemAvgReview(String itemCode) {
		List<Float> rankAvgList = new ArrayList<Float>();
		Float rankAvg = 5.0F;
		
		DataFromSolr dataFromSolr = findItemInfoByItemCodeFromSolr(itemCode);
		List<ItemResultCommand> itemResultCommandList = null;
		if (dataFromSolr != null && dataFromSolr.getItems() != null
				&& (itemResultCommandList = dataFromSolr.getItems().getItems()) != null) {
			for (ItemResultCommand itemResultCommand : itemResultCommandList) {
				rankAvgList = itemResultCommand.getRankavg();
			}
		}
		// 当评价平均分为null或0.0时, 修改为5.0
		if (rankAvgList != null && rankAvgList.size() > 0) {
			for (Float rankAvgF : rankAvgList) {
				if (null != rankAvgF && !rankAvgF.equals(0.0F)) {
					rankAvg = rankAvgF;
				}
			}
		}
		return rankAvg;
	}
	
	/**
	 * 从solr中查询itemInfo数据，查询条件为itemCode
	 * @param itemCode
	 * @return
	 */
	private DataFromSolr findItemInfoByItemCodeFromSolr(String itemCode){
		QueryConditionCommand queryConditionCommand = new QueryConditionCommand();
		queryConditionCommand.setCode(itemCode);
		queryConditionCommand.setLifecycle(1);
		return sdkItemManager.findItemInfoByItemCode(queryConditionCommand);
	}

	@Transactional(readOnly=true)
	@Override
	public List<ItemImage> findItemImgNormsByItemIdItemProp(Long itemId, String itemProperties, String type) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemId", itemId);
		if (StringUtils.isNotBlank(itemProperties)) {
			paramMap.put("itemProperties", Long.valueOf(itemProperties));
		}
		paramMap.put("type", type);
		return sdkItemManager.findItemImgNormsByItemIdItemProp(paramMap);
	}

	
	@Override
	public List<PromotionCommand> findPromotionByItemId(Long itemId, UserDetails userDetails) {
		List<PromotionCommand> promoptionCommandList = sdkPromotionGuideManager.getPromotionsForItem(itemId, userDetails);
		return promoptionCommandList;
	}

	@Override
	public List<PromotionCommand> findPromotionByItemId(Long itemId, HttpServletRequest request) {
		Object userInfo = request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
		UserDetails userDetails = null;
		if (null != userInfo){
			userDetails = (UserDetails) ConvertUtils.convertTwoObject(new UserDetails(), userInfo);
		}
		List<PromotionCommand> promoptionCommandList = sdkPromotionGuideManager.getPromotionsForItem(itemId, userDetails);
		return promoptionCommandList;
	}

	@Transactional(readOnly=true)
	@Override
	public List<ItemCommand> findItemListByItemId(Long itemId, String type) {
		ItemCommand itemCommand = sdkItemManager.findItemCommandById(itemId);
		if (itemCommand == null) {
			throw new BusinessException(ErrorCodes.ITEM_NOT_EXIST);
		}
		String style = itemCommand.getStyle();
		List<ItemCommand> itemComandList = sdkItemManager.findItemCommandByStyle(style);

		List<Long> itemIds = new ArrayList<Long>();
		for (ItemCommand ic : itemComandList) {
			itemIds.add(ic.getId());
		}

		List<ItemImageCommand> itemImgList = sdkItemManager.findItemImagesByItemIds(itemIds, type);
		Map<Long, String> itemImgMap = new HashMap<Long, String>();
		for (ItemImageCommand iic : itemImgList) {
			List<ItemImage> itemImageList = iic.getItemIamgeList();
			if (null != itemImageList && itemImageList.size() > 0) {
				String picUrl = itemImageList.get(0).getPicUrl();
				if (StringUtils.isNotBlank(picUrl)) {
					itemImgMap.put(iic.getItemId(), picUrl);
				}
			}
		}

		for (ItemCommand ic : itemComandList) {
			ic.setPicUrl(itemImgMap.get(ic.getId()));
		}

		return itemComandList;
	}

	@Transactional(readOnly=true)
	@Override
	public ItemCategory findDefaultCateoryByItemId(Long itemId) {
		ItemCategory itemCategory = sdkItemManager.findDefaultCateoryByItemId(itemId);
		return itemCategory;
	}

	@Transactional(readOnly=true)
	@Override
	public Integer findItemFavCount(String itemCode) {
		List<Integer> favoredCountList = null;
		Integer favoredCount = 0;
		DataFromSolr dataFromSolr = findItemInfoByItemCodeFromSolr(itemCode);
		List<ItemResultCommand> itemResultCommandList = null;
		if (dataFromSolr != null && dataFromSolr.getItems() != null
				&& (itemResultCommandList = dataFromSolr.getItems().getItems()) != null) {
			for (ItemResultCommand itemResultCommand : itemResultCommandList) {
				favoredCountList = itemResultCommand.getFavouriteCount();
			}
		}

		if (null != favoredCountList && favoredCountList.size() > 0) {
			for (Integer itemFavCount : favoredCountList) {
				if (null != itemFavCount) {
					favoredCount = itemFavCount;
				}
			}
		}
		return favoredCount;
	}
	
	public Integer findItemSalesCount(String itemCode){
		List<Integer> salesCountList = null;
		Integer salesCount = 0;
		DataFromSolr dataFromSolr = findItemInfoByItemCodeFromSolr(itemCode);
		List<ItemResultCommand> itemResultCommandList = null;
		if (dataFromSolr != null && dataFromSolr.getItems() != null
				&& (itemResultCommandList = dataFromSolr.getItems().getItems()) != null) {
			for (ItemResultCommand itemResultCommand : itemResultCommandList) {
				salesCountList = itemResultCommand.getSalesCount();
			}
		}
		
		// 当评价平均分为null或0.0时, 修改为5.0
		if (salesCountList != null && salesCountList.size() > 0) {
			for (Integer sales : salesCountList) {
				if (null != sales && !sales.equals(0)) {
					salesCount = sales;
				}
			}
		}
		return salesCount;
	}

	@Transactional(readOnly=true)
	@Override
	public List<ItemPropertiesCommand> findPropertyValueByPropertyNameAndItemIds(List<Long> itemIds, String propName) {
		List<ItemPropertiesCommand> itemPropCommand = sdkItemManager.findPropertyValueByPropertyNameAndItemIdsI18n(itemIds,
				propName);
		return itemPropCommand;
	}

	@Override
	public ItemBaseCommand findItemBaseInfoLang(Long itemId,
			String customBaseUrl) {
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoLang(itemId);
		if (itemBaseCommand == null) {
			throw new BusinessException(ErrorCodes.ITEM_NOT_EXIST);
		}
		// 给商品描述的html中的图片加上域名地址(customBaseUrl)
		String desc = itemBaseCommand.getDescription();
		if (StringUtils.isNotBlank(customBaseUrl) && StringUtils.isNotBlank(desc)) {
			Document document = Jsoup.parse(desc);
			Elements elements = document.select("img");
			for (Element element : elements) {
				String src = customBaseUrl + element.attr("src");
				element.attr("src", src);
			}
			itemBaseCommand.setDescription(document.body().html());
		}

		return itemBaseCommand;
	}

	/* 
	 * @see com.baozun.nebula.manager.product.ItemDetailManager#findEffectiveSkuInvByItemId(java.lang.Long)
	 */
	@Override
	public List<SkuCommand> findEffectiveSkuInvByItemId(Long itemId) {
		List<SkuCommand> skuCommands =sdkItemManager.findEffectiveSkuInvByItemId(itemId);
		return skuCommands;
	}

}
