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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.ItemI18nCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemPropertiesCommand;
import com.baozun.nebula.command.promotion.ItemPropertyMutlLangCommand;
import com.baozun.nebula.command.promotion.SkuPropertyMUtlLangCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemInfoLangDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.ItemPropertiesLangDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.extend.ItemExtendManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.google.gson.Gson;

/**
 * 
 * @Description:商品国际化处理类
 * @author 何波
 * @date 2014年12月19日 下午1:46:17
 * 
 */
@Transactional
@Service("itemLangManager")
public class ItemLangManagerImpl implements ItemLangManager {
	@Autowired
	private ItemDao itemDao;

	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private ItemInfoDao itemInfoDao;

	@Autowired
	private PropertyValueDao propertyValueDao;

	@Autowired
	private ItemPropertiesDao itemPropertiesDao;

	@Autowired
	private ItemCategoryManager itemCategoryManager;

	@Autowired
	private ItemPropertiesLangDao itemPropertiesLangDao;

	@Autowired
	private ItemInfoLangDao itemInfoLangDao;
	
	@Autowired
	private ItemManager itemManager;
	
	@Autowired(required = false)
	private ItemExtendManager itemExtendManager;
	
	@Autowired
	private BundleManager bundleManager;
	
	@Override
	public Item createOrUpdateSimpleItem(ItemInfoCommand itemCommand,
			Long[] propertyValueIds, Long[] categoriesIds, Long defaultCategoryId,
			ItemPropertiesCommand[] iProperties,
			SkuPropertyMUtlLangCommand[] skuPropertyCommand) throws Exception {

		// 保存商品
		itemCommand.setItemType(Item.ITEM_TYPE_SIMPLE);
		Item item = createOrUpdateItem(itemCommand, categoriesIds);
		
		ItemI18nCommand itemI18nCommand = new ItemI18nCommand();
		itemI18nCommand.setItem(item);
		
		// 商品所有的属性值集合
		List<ItemPropertiesCommand> savedItemProperties = createOrUpdateItemProperties(itemCommand, propertyValueIds, iProperties, item.getId(), skuPropertyCommand, itemI18nCommand);
		// 保存Sku
		createOrUpdateSku(itemCommand, item.getId(), skuPropertyCommand,savedItemProperties);
		
		// 执行扩展点
		if(null != itemExtendManager) {
			itemExtendManager.extendAfterCreateOrUpdateItemI18n(item, itemCommand, categoriesIds, savedItemProperties, skuPropertyCommand, itemI18nCommand);
		}
		
		// 保存商品扩展信息
		createOrUpdateItemInfo(itemCommand, item.getId());

		// 处理商品分类
		itemCategoryHandle(itemCommand, item, categoriesIds, defaultCategoryId);
		
		return item;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemLangManager#createOrUpdateBundleItem(com.baozun.nebula.command.product.ItemInfoCommand, com.baozun.nebula.command.product.BundleCommand, java.lang.Long[])
	 */
	@Override
	public Item createOrUpdateBundleItem(ItemInfoCommand itemCommand, BundleCommand bundleCommand, Long[] categoriesIds, Long defaultCategoryId)
			throws Exception {
		
		// 保存商品
		itemCommand.setItemType(Item.ITEM_TYPE_BUNDLE);
		Item item = createOrUpdateItem(itemCommand, categoriesIds);
		
		// 保存bundle扩展信息
		bundleCommand.setItemId(item.getId());
		bundleManager.createOrUpdate(bundleCommand);
		
		// 保存商品扩展信息
		createOrUpdateItemInfo(itemCommand, item.getId());
		
		// 处理商品分类
		itemCategoryHandle(itemCommand, item, categoriesIds, defaultCategoryId);
				
		return item;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer validateItemCode(String code, Long shopId) {
		return itemDao.validateItemCode(code, shopId);
	}
	
	private List<Sku> createOrUpdateSku(ItemInfoCommand itemCommand, Long itemId,
			SkuPropertyMUtlLangCommand[] skuPropertyCommandArray, List<ItemPropertiesCommand> savedItemProperties) {

		List<Sku> returnList = new ArrayList<Sku>();
		if (itemCommand.getId() != null) {

			/**
			 * 提交到server的时候，根据传过来的 skuId 和 库里已经有的列表进行比对 如果新增，那么新增的skuId 为null。
			 * 根据传过来的 PropertyId 和 propertyValue 拿到已经保存的ItemProperties. 新增Sku
			 * 如果是修改，就是根据skuId 修改。此时不涉及 itemProperties 的修改。 只会修改skuCode， 销售价，吊牌价
			 * 如果库里列表中的skuId在提交过来的SkuId 不存在，那么就删除
			 */
			List<Sku> skuListInDb = skuDao.findSkuByItemId(itemId);

			for (SkuPropertyMUtlLangCommand spc : skuPropertyCommandArray) {
				Long skuId = spc.getId();
				String extentionCode = spc.getCode();
				String skuItemProperties = getSkuItemProperties(spc, savedItemProperties);

				Sku savedSku = null;
				if (skuId != null) {// 修改
					/* 如果修改了extention_code , 将该sku的lifecycle设置为0, 并新增一条数据 */
					Sku dbSku = skuDao.findSkuById(skuId);
					if (extentionCode.equals(dbSku.getOutid())) {
						Sku skuToBeUpdate = skuDao.getByPrimaryKey(skuId);
						skuToBeUpdate.setListPrice(spc.getListPrice());
						skuToBeUpdate.setOutid(spc.getCode());
						skuToBeUpdate.setSalePrice(spc.getSalePrice());
						savedSku = skuDao.save(skuToBeUpdate);
					} else {
						dbSku.setLifecycle(Sku.LIFECYCLE_DISABLE);
						skuDao.save(dbSku);

						savedSku = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
					}
				} else {
					/*
					 * 检察sku表中是否存在, itemId, out_id,
					 * item_properties与提交的都一致,且lifecycle=0的信息 1, 存在:
					 * 修改lifecycle=1 2, 不存在: 新增一条记录
					 */

					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("itemId", itemId);
					paraMap.put("itemProperties", skuItemProperties);
					paraMap.put("outId", extentionCode);
					paraMap.put("lifecycle", Sku.LIFECYCLE_DISABLE);
					List<Sku> savedSkuList = skuDao.findSkuWithParaMap(paraMap);

					if (null != savedSkuList && !savedSkuList.isEmpty()) {
						for (Sku sku : savedSkuList) {
							sku.setLifecycle(Sku.LIFECYCLE_ENABLE);
							sku.setListPrice(spc.getListPrice());
							sku.setSalePrice(spc.getSalePrice());
							savedSku = skuDao.save(sku);
							break;
						}
					} else if (skuId == null) {// 新增
						savedSku = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
					}
				}
				returnList.add(savedSku);
			}
			// 获得要删除的skuId集合
			List<Long> skuToBeDelList = new ArrayList<Long>();
			for (Sku skuInDb : skuListInDb) {
				boolean delFlag = true;
				for (Sku curSku : returnList) {
					if (skuInDb.getId().equals(curSku.getId())) {
						delFlag = false;
						break;
					}
				}
				if (delFlag) {
					skuToBeDelList.add(skuInDb.getId());
				}
			}
			// 删除
			skuDao.deleteSkuBySkuIds(skuToBeDelList);
		} else {
			returnList = saveSkus(itemCommand, itemId, skuPropertyCommandArray, savedItemProperties);
		}

		return returnList;
	}
	private List<Sku> saveSkus(ItemInfoCommand itemCommand, Long itemId, SkuPropertyMUtlLangCommand[] skuPropertyCommandArray, List<ItemPropertiesCommand> savedItemProperties) {
		List<Sku> returnList = new ArrayList<Sku>();
		if (skuPropertyCommandArray != null && skuPropertyCommandArray.length > 0) {
			for (SkuPropertyMUtlLangCommand spc : skuPropertyCommandArray) {
				String skuItemProperties = getSkuItemProperties(spc, savedItemProperties);
				Sku skunew = createSkuBySkuPropertyCommand(skuItemProperties, spc, itemId);
				returnList.add(skunew);
			}
		}

		return returnList;
	}
	
	private String getSkuItemProperties(SkuPropertyMUtlLangCommand spc, List<ItemPropertiesCommand> savedItemProperties) {
		List<ItemPropertyMutlLangCommand> ipCmdList = spc.getPropertyList();
		List<Long> ipIds = new ArrayList<Long>();
		if (ipCmdList != null) {
			for (ItemPropertyMutlLangCommand ipCmd : ipCmdList) {
				for (ItemPropertiesCommand ip : savedItemProperties) {

					if (ip.getPropertyId().equals(ipCmd.getpId())) {
						if (ipCmd.getId() != null && ip.getPropertyValueId() != null) {// 如果是多选
							if ((ipCmd.getId().equals(ip.getPropertyValueId().toString())) && (ipCmd.getpId().equals(ip.getPropertyId()))) {
								ipIds.add(ip.getId());
							}

						} else {// 如果是自定义多选
							if (ipCmd.getValue() != null) {
								boolean i18n = LangProperty.getI18nOnOff();
								if(i18n){
									MutlLang singleLang1 =  (MutlLang) ip.getPropertyValue();
									MutlLang singleLang2 =  (MutlLang) ipCmd.getValue();
									String[] v1 =singleLang1.getValues();
									String[] v2 =singleLang2.getValues();
									if (ip.getPropertyId().equals(ipCmd.getpId()) && Arrays.toString(v1).equals(Arrays.toString(v2))) {
										ipIds.add(ip.getId());
									}
								}else{
									SingleLang singleLang =  (SingleLang) ip.getPropertyValue();
									SingleLang singleLang1 =  (SingleLang) ipCmd.getValue();
									if (ip.getPropertyId().equals(ipCmd.getpId()) && singleLang.getValue().equals(singleLang1.getValue())) {
										ipIds.add(ip.getId());
									}
								}
//								if (ipCmd.getValue().equals(ip.getPropertyValue()) && ip.getPropertyId().equals(ipCmd.getpId())) {
//									ipIds.add(ip.getId());
//								}
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
	private Sku createSkuBySkuPropertyCommand(String skuItemProperties, SkuPropertyMUtlLangCommand spc, Long itemId) {
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
	 * 
	* @author 何波
	* @Description: 处理iteminfo国际化
	* @param itemCommand
	* @param itemId
	* @return   
	* ItemInfo   
	* @throws
	 */
	private ItemInfo createOrUpdateItemInfo(ItemInfoCommand itemCommand, Long itemId) {
		ItemInfo itemInfo = new ItemInfo();
		Long id = itemCommand.getId();
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			if (id != null) {
				itemInfo = itemInfoDao.findItemInfoByItemId(id);
				Long itemInfoId = itemInfo.getId();
				itemInfo = itemInfoDao.getByPrimaryKey(itemInfoId);
				itemInfo.setModifyTime(new Date());
				itemInfo.setSalePrice(itemCommand.getSalePrice());
				itemInfo.setListPrice(itemCommand.getListPrice());
				itemInfo.setStyle(itemCommand.getStyle());
				itemInfo.setType(itemCommand.getType());
				MutlLang titleLang = (MutlLang) itemCommand.getTitle();
				String[] titles = titleLang.getValues();
				String[] titleLangs = titleLang.getLangs();
				String titleDv = titleLang.getDefaultValue();
				
				MutlLang subTitleLang = (MutlLang) itemCommand.getSubTitle();
				String[] subTitles = subTitleLang.getValues();
				String subTitleDv = subTitleLang.getDefaultValue();
				
				MutlLang descriptionLang = (MutlLang) itemCommand.getDescription();
				String[] descriptions = descriptionLang.getValues();
				String descriptionDv = descriptionLang.getDefaultValue();
				
				MutlLang sketchLang = (MutlLang) itemCommand.getSketch();
				String[] sketchs = sketchLang.getValues();
				String sketchDv = sketchLang.getDefaultValue();
				
				MutlLang seoDescriptionLang = (MutlLang) itemCommand.getSeoDescription();
				String[] seoDescriptions = seoDescriptionLang.getValues();
				String seoDescriptionDv = seoDescriptionLang.getDefaultValue();
				
				MutlLang seoKeywordsLang = (MutlLang) itemCommand.getSeoKeywords();
				String[] seoKeywords = seoKeywordsLang.getValues();
				String seoKeywordsDv = seoKeywordsLang.getDefaultValue();
				
				MutlLang seoTitleLang = (MutlLang) itemCommand.getSeoTitle();
				String[] seoTitles = seoTitleLang.getValues();
				String seoTitleDv = seoTitleLang.getDefaultValue();
				
				itemInfo.setTitle(titleDv);
				itemInfo.setSubTitle(subTitleDv);
				itemInfo.setDescription(descriptionDv);
				itemInfo.setSketch(sketchDv);
				itemInfo.setSeoDescription(seoDescriptionDv);
				itemInfo.setSeoKeywords(seoKeywordsDv);
				itemInfo.setSeoTitle(seoTitleDv);
				itemInfo = itemInfoDao.save(itemInfo);
				for (int i = 0; i < titles.length; i++) {
					String title  =  titles[i];
					String lang  =  titleLangs[i];
					String subTitle= null;
					if(subTitles!=null && subTitles.length>0){
						subTitle = subTitles[i];
					}
					String description = null;
					if(descriptions!=null && descriptions.length>0){
						description = descriptions[i];
					}
					String sketch= null;
					if(sketchs!=null && sketchs.length>0){
						sketch = sketchs[i];
					}
					String seoDescription= null;
					if(seoDescriptions!=null && seoDescriptions.length>0){
						seoDescription = seoDescriptions[i];
					}
					String seoKeyword= null;
					if(seoKeywords!=null && seoKeywords.length>0){
						seoKeyword = seoKeywords[i];
					}
					String seoTitle= null;
					if(seoTitles!=null && seoTitles.length>0){
						seoTitle = seoTitles[i];
					}
					saveOrUpdateItemInfoLang(title, subTitle, description, sketch, 
							seoDescription, seoKeyword, seoTitle, lang, itemInfoId);
				}
			}else{
				//新增
				itemInfo = new ItemInfo();
				itemInfo.setItemId(itemId);
				itemInfo.setCreateTime(new Date());
				itemInfo.setSalePrice(itemCommand.getSalePrice());
				itemInfo.setListPrice(itemCommand.getListPrice());
				itemInfo.setStyle(itemCommand.getStyle());
				itemInfo.setType(itemCommand.getType());
				MutlLang titleLang = (MutlLang) itemCommand.getTitle();
				String[] titles = titleLang.getValues();
				String[] titleLangs = titleLang.getLangs();
				String titleDv = titleLang.getDefaultValue();
				
				MutlLang subTitleLang = (MutlLang) itemCommand.getSubTitle();
				String[] subTitles = subTitleLang.getValues();
				String subTitleDv = subTitleLang.getDefaultValue();
				
				MutlLang descriptionLang = (MutlLang) itemCommand.getDescription();
				String[] descriptions = descriptionLang.getValues();
				String descriptionDv = descriptionLang.getDefaultValue();
				
				MutlLang sketchLang = (MutlLang) itemCommand.getTitle();
				String[] sketchs = sketchLang.getValues();
				String sketchDv = sketchLang.getDefaultValue();
				
				MutlLang seoDescriptionLang = (MutlLang) itemCommand.getSeoDescription();
				String[] seoDescriptions = seoDescriptionLang.getValues();
				String seoDescriptionDv = seoDescriptionLang.getDefaultValue();
				
				MutlLang seoKeywordsLang = (MutlLang) itemCommand.getSeoKeywords();
				String[] seoKeywords = seoKeywordsLang.getValues();
				String seoKeywordsDv = seoKeywordsLang.getDefaultValue();
				
				MutlLang seoTitleLang = (MutlLang) itemCommand.getSeoTitle();
				String[] seoTitles = seoTitleLang.getValues();
				String seoTitleDv = seoTitleLang.getDefaultValue();
				
				itemInfo.setTitle(titleDv);
				itemInfo.setSubTitle(subTitleDv);
				itemInfo.setDescription(descriptionDv);
				itemInfo.setSketch(sketchDv);
				itemInfo.setSeoDescription(seoDescriptionDv);
				itemInfo.setSeoKeywords(seoKeywordsDv);
				itemInfo.setSeoTitle(seoTitleDv);
				itemInfo = itemInfoDao.save(itemInfo);
				Long itemInfoId = itemInfo.getId();
				for (int i = 0; i < titles.length; i++) {
					String title  =  titles[i];
					String lang  =  titleLangs[i];
					String subTitle= null;
					if(subTitles != null && subTitles.length>0){
						subTitle = subTitles[i];
					}
					String description = null;
					if(descriptions!=null && descriptions.length>0){
						description = descriptions[i];
					} 
					String sketch= null;
					if(sketchs!=null && sketchs.length>0){
						sketch = sketchs[i];
					}
					String seoDescription= null;
					if(seoDescriptions!=null && seoDescriptions.length>0){
						seoDescription = seoDescriptions[i];
					}
					String seoKeyword= null;
					if(seoKeywords!=null && seoKeywords.length>0){
						seoKeyword = seoKeywords[i];
					}
					String seoTitle= null;
					if(seoTitles!=null && seoTitles.length>0){
						seoTitle = seoTitles[i];
					}
					saveOrUpdateItemInfoLang(title, subTitle, description, sketch, seoDescription,
							seoKeyword, seoTitle, lang, itemInfoId);
					
				}
			}
		} else {
			//单语言
			if (id != null) {
				itemInfo = itemInfoDao.findItemInfoByItemId(id);
				itemInfo = itemInfoDao.getByPrimaryKey(itemInfo.getId());
				itemInfo.setSalePrice(itemCommand.getSalePrice());
				itemInfo.setListPrice(itemCommand.getListPrice());
				itemInfo.setStyle(itemCommand.getStyle());
				itemInfo.setType(itemCommand.getType());
				
				SingleLang titleLang = (SingleLang) itemCommand.getTitle();
				String titleDv = titleLang.getValue();
				
				SingleLang subTitleLang = (SingleLang) itemCommand.getSubTitle();
				String subTitleDv = subTitleLang.getValue();
				
				SingleLang descriptionLang = (SingleLang) itemCommand.getDescription();
				String descriptionDv = descriptionLang.getValue();
				
				SingleLang sketchLang = (SingleLang) itemCommand.getSketch();
				String sketchDv = sketchLang.getValue();
				
				SingleLang seoDescriptionLang = (SingleLang) itemCommand.getSeoDescription();
				String seoDescriptionDv = seoDescriptionLang.getValue();
				
				SingleLang seoKeywordsLang = (SingleLang) itemCommand.getSeoKeywords();
				String seoKeywordsDv = seoKeywordsLang.getValue();
				
				SingleLang seoTitleLang = (SingleLang) itemCommand.getSeoTitle();
				String seoTitleDv = seoTitleLang.getValue();
				itemInfo.setTitle(titleDv);
				itemInfo.setSubTitle(subTitleDv);
				itemInfo.setDescription(descriptionDv);
				itemInfo.setSketch(sketchDv);
				itemInfo.setSeoDescription(seoDescriptionDv);
				itemInfo.setSeoKeywords(seoKeywordsDv);
				itemInfo.setSeoTitle(seoTitleDv);
				itemInfo.setModifyTime(new Date());
				itemInfo = itemInfoDao.save(itemInfo);
			}else{
				itemInfo.setSalePrice(itemCommand.getSalePrice());
				itemInfo.setListPrice(itemCommand.getListPrice());
				itemInfo.setStyle(itemCommand.getStyle());
				itemInfo.setType(itemCommand.getType());
				
				SingleLang titleLang = (SingleLang) itemCommand.getTitle();
				String titleDv = titleLang.getValue();
				
				SingleLang subTitleLang = (SingleLang) itemCommand.getSubTitle();
				String subTitleDv = subTitleLang.getValue();
				
				SingleLang descriptionLang = (SingleLang) itemCommand.getDescription();
				String descriptionDv = descriptionLang.getValue();
				
				SingleLang sketchLang = (SingleLang) itemCommand.getSketch();
				String sketchDv = sketchLang.getValue();
				
				SingleLang seoDescriptionLang = (SingleLang) itemCommand.getSeoDescription();
				String seoDescriptionDv = seoDescriptionLang.getValue();
				
				SingleLang seoKeywordsLang = (SingleLang) itemCommand.getSeoKeywords();
				String seoKeywordsDv = seoKeywordsLang.getValue();
				
				SingleLang seoTitleLang = (SingleLang) itemCommand.getSeoTitle();
				String seoTitleDv = seoTitleLang.getValue();
				itemInfo.setTitle(titleDv);
				itemInfo.setSubTitle(subTitleDv);
				itemInfo.setDescription(descriptionDv);
				itemInfo.setSketch(sketchDv);
				itemInfo.setSeoDescription(seoDescriptionDv);
				itemInfo.setSeoKeywords(seoKeywordsDv);
				itemInfo.setSeoTitle(seoTitleDv);
				//新增
				itemInfo.setItemId(itemId);
				itemInfo.setCreateTime(new Date());
				itemInfo = itemInfoDao.save(itemInfo);
			}
			
		}

		return itemInfo;
	}
	/**
	 * 
	* @author 何波
	* @Description: 修改或新增商品国家化属性
	* @param title
	* @param subTitle
	* @param description
	* @param sketch
	* @param seoDescription
	* @param seoKeyword
	* @param seoTitle
	* @param lang
	* @param itemInfoId   
	* void   
	* @throws
	 */
	public  void saveOrUpdateItemInfoLang(String title,String subTitle,String description,String sketch,
			String seoDescription,String seoKeyword,String seoTitle,String lang,Long itemInfoId){
		ItemInfoLang itemInfoLang = itemInfoDao.findItemInfoLang(itemInfoId, lang);
		if(itemInfoLang != null){
			Map<String, Object>  params = new HashMap<String, Object>();
			params.put("title", title);
			params.put("subTitle", subTitle);
			params.put("description", description);
			params.put("sketch", sketch);
			params.put("seoDescription", seoDescription);
			params.put("seoKeywords", seoKeyword);
			params.put("seoTitle", seoTitle);
			params.put("itemInfoId", itemInfoId);
			params.put("lang", lang);
			itemInfoDao.updateItemInfoLang(params);
		}else{
			ItemInfoLang infoLang = new ItemInfoLang();
			infoLang.setTitle(title);
			infoLang.setDescription(description);
			infoLang.setLang(lang);
			infoLang.setSeoDescription(seoDescription);
			infoLang.setSeoKeywords(seoKeyword);
			infoLang.setSeoTitle(seoTitle);
			infoLang.setSketch(sketch);
			infoLang.setSubTitle(subTitle);
			infoLang.setItemInfoId(itemInfoId);
			itemInfoLangDao.save(infoLang);
		}
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
	private List<ItemPropertiesCommand> createOrUpdateItemProperties(ItemInfoCommand itemCommand, Long[] propertyValueIds, ItemPropertiesCommand[] iProperties, Long itemId, 
			SkuPropertyMUtlLangCommand[] skuPropertyCommandArray, ItemI18nCommand itemI18nCommand) throws Exception {
		
		List<ItemPropertiesCommand> itemPropertyList = new ArrayList<ItemPropertiesCommand>();

		List<Long> list = new ArrayList<Long>();
		for (Long pvs : propertyValueIds) {
			list.add(pvs);
		}
		List<PropertyValue> propertyValueList = propertyValueDao.findPropertyValueListByIds(list);

		if (itemCommand.getId() != null) {// 修改

			/**
			 * 销售属性 ItemProperties的修改 根据传过来的 propertyValueIds 和
			 * propertyValueInputs 组合成 itemProperties (已经在
			 * skuPropertyCommandArray 中) 在properties 表中查找 这个itemProperties
			 * 如果没有，就新增 。 对比删除 。不涉及修改
			 */         
			//List<ItemProperties> ipListInDb = itemPropertiesDao.findItemPropertiesByItemId(itemCommand.getId());
			List<ItemPropertiesCommand> ipListInDb = itemManager.findItemPropertiesCommandListyByItemId(itemCommand.getId());
			boolean i18n = LangProperty.getI18nOnOff();
			
			// skuPropertyCommandArray 中的数据是否在 数据库中 。 循环结束后，
			// skuPropertyCommandArray对应的 itemProperties 都在
			// itemPropertyList中
			for (SkuPropertyMUtlLangCommand cmd : skuPropertyCommandArray) {
				List<ItemPropertyMutlLangCommand> ipcList = cmd.getPropertyList();
				for (ItemPropertyMutlLangCommand ipc : ipcList) {
					// 相等的条件： propertyId 相等， 并且 (pvId相等 或者pvValue相等)
					boolean existFlag = false;
					// 看表单传过来的值 是否在 数据库中。 如果存在，那么就放入 itemPropertyList
					for (ItemPropertiesCommand ipDb : ipListInDb) {
						if (ipDb.getPropertyValueId() != null) {
							if (ipDb.getPropertyId().equals(ipc.getpId()) && (null != ipc.getId()) && ((ipDb.getPropertyValueId().equals(Long.parseLong(ipc.getId()))))) {
								existFlag = true;
								// 此处添加的时候 要注意判断是否存在该属性 此处可能导致 大循环结束 属性值重复
								if (!isItemPrpertiesExistInList(ipDb, itemPropertyList)) {
									itemPropertyList.add(ipDb);
								}
								break;
							}
						}
						if (ipDb.getPropertyValue() != null) {
							if(i18n){
								MutlLang ml1 =  (MutlLang) ipDb.getPropertyValue();
								if(ml1!=null && ml1.getValues()!=null && ml1.getValues().length!=0){
									MutlLang ml2 =  (MutlLang) ipc.getValue();
									String[] v1 =ml1.getValues();
									String[] v2 = null;
									if(ml2 == null){
										v2 = new String[MutlLang.i18nSize()];
									}else{
										v2 =ml2.getValues();
									}
									if (ipDb.getPropertyId().equals(ipc.getpId()) && Arrays.toString(v1).equals(Arrays.toString(v2))) {
										existFlag =  true;
										if (!isItemPrpertiesExistInList(ipDb, itemPropertyList)) {
											itemPropertyList.add(ipDb);
										}
										break;
									}
								}
							
							}else{
								SingleLang singleLang =  (SingleLang) ipDb.getPropertyValue();
								if(singleLang!=null && StringUtils.isNotEmpty(singleLang.getValue())){
									SingleLang singleLang1 =  (SingleLang) ipc.getValue();
									if(singleLang1==null){
										singleLang1 = new SingleLang();
										singleLang1.setValue("");
									}
									if (ipDb.getPropertyId().equals(ipc.getpId()) && singleLang.getValue().equals(singleLang1.getValue())) {
										existFlag = true;
										if (!isItemPrpertiesExistInList(ipDb, itemPropertyList)) {
											itemPropertyList.add(ipDb);
										}
										break;
									}
								}
							}
//							if (ip.getPropertyId().equals(ipc.getpId()) && (ip.getPropertyValue().equals(ipc.getValue()))) {
//								existFlag = true;
//								if (!isItemPrpertiesExistInList(ip, itemPropertyList)) {
//									itemPropertyList.add(ip);
//								}
//								
//							}
						}
					}
					// 看表单传过来的值是否已经在 itemPropertyList 中了
					for (ItemPropertiesCommand ip : itemPropertyList) {
						if (ip.getPropertyValueId() != null) {
							if (ip.getPropertyId().equals(ipc.getpId()) && ((ip.getPropertyValueId().equals(Long.parseLong(ipc.getId()))))) {
								existFlag = true;
								// itemPropertyList.add(ip);
								break;
							}
						}
						if(i18n){
							if (ip.getPropertyValue() != null) {
								MutlLang ml1 =  (MutlLang) ip.getPropertyValue();
								if(ml1!=null && ml1.getValues()!=null && ml1.getValues().length!=0){
									MutlLang ml2 =  (MutlLang) ipc.getValue();
									String[] v1 =ml1.getValues();
									String[] v2 = null;
									if(ml2 == null){
										v2 = new String[MutlLang.i18nSize()];
									}else{
										v2 =ml2.getValues();
									}
									if (ip.getPropertyId().equals(ipc.getpId()) && Arrays.toString(v1).equals(Arrays.toString(v2))) {
										existFlag =  true;
										break;
									}
								}
							
//								if (ip.getPropertyId().equals(ipc.getpId()) && (ip.getPropertyValue().equals(ipc.getValue()))) {
//									existFlag = true;
//									break;
//								}
							}
						}else{
							if (ip.getPropertyValue() != null) {
								SingleLang singleLang =  (SingleLang) ip.getPropertyValue();
								if(singleLang!=null && StringUtils.isNotEmpty(singleLang.getValue())){
									SingleLang singleLang1 =  (SingleLang) ipc.getValue();
									if(singleLang1==null){
										singleLang1 = new SingleLang();
										singleLang1.setValue("");
									}
									if (ip.getPropertyId().equals(ipc.getpId()) && singleLang.getValue().equals(singleLang1.getValue())) {
										existFlag = true;
										break;
									}
								}
							
							}
//								if (ip.getPropertyId().equals(ipc.getpId()) && (ip.getPropertyValue().equals(ipc.getValue()))) {
//									existFlag = true;
//									break;
//								}
							
						}
					}
					if (!existFlag) {// 不存在， 则新增
						ItemProperties itemProperties = new ItemProperties();
						itemProperties.setItemId(itemId);
						itemProperties.setPropertyId(ipc.getpId());

						String vidStr = ipc.getId();
						Long vId = null;
						if (StringUtils.isNotBlank(vidStr)) {
							vId = Long.parseLong(vidStr);
						}
						itemProperties.setCreateTime(new Date());
						if(i18n){
							MutlLang mutlLang = (MutlLang) ipc.getValue();
							//自定义多选
							if(mutlLang != null){
								String dv = mutlLang.getDefaultValue();
								itemProperties.setPropertyValue(dv);
								itemProperties = itemPropertiesDao.save(itemProperties);
								itemI18nCommand.getItemPropertiesList().add(itemProperties);
								Long id = itemProperties.getId();
								String[] values = mutlLang.getValues();
								String[] langs = mutlLang.getLangs();
								if(values!=null){
									for (int i = 0; i < values.length; i++) {
										String val  =  values[i];
										String lang  =  langs[i];
										ItemPropertiesLang ipl = new ItemPropertiesLang();
										ipl.setItemPropertiesId(id);
										ipl.setLang(lang);
										ipl.setPropertyValue(val);
										ipl = itemPropertiesLangDao.save(ipl);
										itemI18nCommand.getItemPropertiesLangList().add(ipl);
									}
								}
							}else{
								//多选
								itemProperties.setPropertyValueId(vId);
								itemProperties = itemPropertiesDao.save(itemProperties);
								itemI18nCommand.getItemPropertiesList().add(itemProperties);
								if(i18n){
									for (int i = 0; i < MutlLang.i18nLangs().size(); i++) {
										ItemPropertiesLang ipl = new ItemPropertiesLang();
										String lang  = MutlLang.i18nLangs().get(i);
										ipl.setItemPropertiesId(itemProperties.getId());
										ipl.setLang(lang);
										ipl = itemPropertiesLangDao.save(ipl);
										itemI18nCommand.getItemPropertiesLangList().add(ipl);
									}
								}
							}
							ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
							LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand); 
							itemPropertiesCommand.setPropertyValue(mutlLang);
							itemPropertyList.add(itemPropertiesCommand);
						}else{
							SingleLang singleLang = (SingleLang) ipc.getValue();
							if(singleLang != null){
								String dv = singleLang.getValue();
								itemProperties.setPropertyValue(dv);
							}else{
								itemProperties.setPropertyValueId(vId);
							}
							itemProperties = itemPropertiesDao.save(itemProperties);
							itemI18nCommand.getItemPropertiesList().add(itemProperties);
							ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
							LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand); 
							itemPropertiesCommand.setPropertyValue(singleLang);
							itemPropertyList.add(itemPropertiesCommand);
						}
						
					}
				}
			}

			List<Long> ipIdToDelList = new ArrayList<Long>();
			//List<ItemProperties> ipToDelList = new ArrayList<ItemProperties>();
			// 数据库中原有的 数据 没有在 新的itemProperties 则删掉 （普通属性也被删掉）
			for (ItemPropertiesCommand ipInDb : ipListInDb) {
				boolean delFlag = true;
				for (ItemPropertiesCommand ip : itemPropertyList) {
					if (ipInDb.getId().equals(ip.getId())) {
						delFlag = false;
						break;
					}
				}
				if (delFlag) {
					//ipToDelList.add(ipInDb);
					ipIdToDelList.add(ipInDb.getId());
				}
			}
			if(ipIdToDelList.size()>0){
				itemPropertiesDao.deleteAllByPrimaryKey(ipIdToDelList);
				//删除对应国际化数据
				itemPropertiesDao.deleteItemPropertiesLangByIds(ipIdToDelList);
			}
			// 保存普通属性
			saveItemProperties(iProperties, itemId, itemPropertyList, itemI18nCommand);
		} else {// 新增
			saveItemProperties(itemId, skuPropertyCommandArray, itemPropertyList, propertyValueList, iProperties, itemI18nCommand);
		}
		return itemPropertyList;
	}
	private void saveItemProperties(Long itemId, SkuPropertyMUtlLangCommand[] skuPropertyCommandArray, List<ItemPropertiesCommand> itemPropertyList, 
			List<PropertyValue> propertyValueList, ItemPropertiesCommand[] iProperties, ItemI18nCommand itemI18nCommand) {
		// 先保存销售属性， 从skuPropertyCommand中拿到销售属性。 销售属性目前有两种 ，一种是 多选，另外一种是自定义多选
		if (skuPropertyCommandArray != null) {
			for (SkuPropertyMUtlLangCommand skuPropertyCommand : skuPropertyCommandArray) {
				List<ItemPropertyMutlLangCommand> ipcList = skuPropertyCommand.getPropertyList();

				for (ItemPropertyMutlLangCommand ip : ipcList) {
					if (ip.getId() != null && !ip.getId().equals("undefined")) { // 多选

						for (PropertyValue pv : propertyValueList) {
							Long vid = Long.parseLong(ip.getId());
							if (vid.equals(pv.getId())) {
								boolean existFlag = false;

								for (ItemPropertiesCommand svItemProperties : itemPropertyList) {
									if (vid.equals(svItemProperties.getPropertyValueId())) {
										existFlag = true;
										break;
									}
								}
								if (!existFlag) {
									ItemProperties itemProperties = new ItemProperties();
									itemProperties.setItemId(itemId);
									itemProperties.setPropertyId(ip.getpId());
									itemProperties.setPropertyValueId(vid);
									itemProperties.setCreateTime(new Date());
									itemProperties = itemPropertiesDao.save(itemProperties);
									itemI18nCommand.getItemPropertiesList().add(itemProperties);
									boolean i18n = LangProperty.getI18nOnOff();
									if(i18n){
										for (int i = 0; i < MutlLang.i18nLangs().size(); i++) {
											ItemPropertiesLang ipl = new ItemPropertiesLang();
											String lang  = MutlLang.i18nLangs().get(i);
											ipl.setItemPropertiesId(itemProperties.getId());
											ipl.setLang(lang);
											ipl = itemPropertiesLangDao.save(ipl);
											itemI18nCommand.getItemPropertiesLangList().add(ipl);
										}
									}
									ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
									LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand); 
									itemPropertyList.add(itemPropertiesCommand);
								}

							}
						}

					} else {// 自定义多选
						LangProperty itemPropertyValue = ip.getValue();
						boolean existFlag = false;
						for (ItemPropertiesCommand svItemProperties : itemPropertyList) {
							boolean i18n = LangProperty.getI18nOnOff();
							if(svItemProperties.getPropertyValueId()!=null){
								continue;
							}
							if(i18n){
								MutlLang muLang = (MutlLang) itemPropertyValue;
								MutlLang muLang1 =(MutlLang) svItemProperties.getPropertyValue();
								String str1 = Arrays.toString(muLang.getValues()); 
								String str2 = Arrays.toString(muLang1.getValues()); 
								if (str1.equals(str2)) {
									existFlag = true;
									break;
								}
							}else{
								SingleLang singleLang = (SingleLang) itemPropertyValue;
								SingleLang singleLang1 =(SingleLang) svItemProperties.getPropertyValue();
								if (singleLang.getValue().equals(singleLang1.getLang())) {
									existFlag = true;
									break;
								}
							}
						}
						if (!existFlag) {
							ItemProperties itemProperties = new ItemProperties();
							itemProperties.setItemId(itemId);
							itemProperties.setPropertyId(ip.getpId());
							itemProperties.setCreateTime(new Date());
							boolean i18n  = LangProperty.getI18nOnOff();
							if(i18n){
								MutlLang mutlLang = (MutlLang) ip.getValue();
								if(mutlLang != null){
									String dv = mutlLang.getDefaultValue();
									itemProperties.setPropertyValue(dv);
								}
								itemProperties = itemPropertiesDao.save(itemProperties);
								itemI18nCommand.getItemPropertiesList().add(itemProperties);
								Long id = itemProperties.getId();
								String[] values = mutlLang.getValues();
								String[] langs = mutlLang.getLangs();
								ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
								LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand); 
								itemPropertiesCommand.setPropertyValue(mutlLang);
								itemPropertyList.add(itemPropertiesCommand);
								if(values!=null){
									for (int i = 0; i < values.length; i++) {
										String val  =  values[i];
										String lang  =  langs[i];
										ItemPropertiesLang ipl = new ItemPropertiesLang();
										ipl.setItemPropertiesId(id);
										ipl.setLang(lang);
										ipl.setPropertyValue(val);
										ipl = itemPropertiesLangDao.save(ipl);
										itemI18nCommand.getItemPropertiesLangList().add(ipl);
									}
								}
							}else{
								SingleLang singleLang = (SingleLang) ip.getValue();
								if(singleLang != null){
									String dv = singleLang.getValue();
									itemProperties.setPropertyValue(dv);
								}
								itemProperties = itemPropertiesDao.save(itemProperties);
								itemI18nCommand.getItemPropertiesList().add(itemProperties);
								ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
								LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand); 
								itemPropertiesCommand.setPropertyValue(singleLang);
								itemPropertyList.add(itemPropertiesCommand);
							}
							
						}

					}
				}
			}
		}
		// 保存普通属性 
		saveItemProperties(iProperties, itemId, itemPropertyList, itemI18nCommand);
	}

	/**
	 * 
	* @author 何波
	* @Description: 保存商品属性
	* @param iProperties 需要保存商品属性多语言command
	* @param itemId
	* @param itemPropertyList  存放保存了的商品属性
	* void   
	* @throws
	 */
	private void  saveItemProperties(ItemPropertiesCommand[]  iProperties ,
				Long  itemId,List<ItemPropertiesCommand> itemPropertyList, ItemI18nCommand itemI18nCommand){
		for (int j = 0; j < iProperties.length; j++) {
			ItemPropertiesCommand itemPropertiesCommand = iProperties[j];
			ItemProperties itemProperties =  new ItemProperties();
			LangProperty.I18nPropertyCopy(itemPropertiesCommand, itemProperties);
			boolean i18n = LangProperty.getI18nOnOff();
			ItemProperties itemProperties2 = new ItemProperties();
			itemProperties2.setPicUrl(itemProperties.getPicUrl());
			if (i18n){
				LangProperty langProperty = itemPropertiesCommand.getPropertyValue();
				if(langProperty != null && itemPropertiesCommand.getPropertyValueId() == null){
					MutlLang  mutlLangPv =  (MutlLang) langProperty;
					String[] values = mutlLangPv.getValues();
					String[] langs = mutlLangPv.getLangs();
					String  dv = mutlLangPv.getDefaultValue();
					if (dv.indexOf("||") != -1) {
						//检查信息国际化信息是否对应正确
						int last = 0;
						for (String v : values) {
							int len = v.split("\\|\\|").length;
							if(last == 0){
								last = len;
							}else{
								if(len!=last){
									throw new BusinessException(30001);
								}
							}
						}
						//先保存 拆分属性
						List<ItemProperties> list = new ArrayList<ItemProperties>();
						String[] dpropertyValues =dv.split("\\|\\|");
						for (String pv : dpropertyValues) {
							itemProperties2 = new ItemProperties();
							itemProperties2.setPropertyValue(pv);
							itemProperties2.setPropertyId(itemProperties.getPropertyId());
							itemProperties2.setItemId(itemId);
							itemProperties2.setCreateTime(new Date());
							itemProperties2.setVersion(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties2);
							itemI18nCommand.getItemPropertiesList().add(itemProperties2);
							ItemPropertiesCommand itemPropertiesCommand1 = new ItemPropertiesCommand();
							LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand1); 
							itemPropertiesCommand1.setPropertyValue(mutlLangPv);
							itemPropertyList.add(itemPropertiesCommand1);
							list.add(itemProperties2);
						}
						//再保存拆分国际化
						for (int i = 0; i < values.length; i++) {
							String val = values[i];
							String lang = langs[i];
							String[] propertyValues = val.split("\\|\\|");
							int num = 0;
							for (String pv : propertyValues) {
								if(list.get(num)==null){
									throw new BusinessException(30001);
								}
								Long ipId = list.get(num).getId();
								ItemPropertiesLang ipl = new ItemPropertiesLang();
								ipl.setItemPropertiesId(ipId);
								ipl.setLang(lang);
								ipl.setPropertyValue(pv);
								ipl = itemPropertiesLangDao.save(ipl);
								itemI18nCommand.getItemPropertiesLangList().add(ipl);
								num++;
							}
						}
						
					}else{
						itemProperties2.setPropertyValue(dv);
						itemProperties2.setPropertyId(itemProperties.getPropertyId());
						itemProperties2.setItemId(itemId);
						itemProperties2.setCreateTime(new Date());
						itemProperties2.setVersion(new Date());
						itemProperties = itemPropertiesDao.save(itemProperties2);
						itemI18nCommand.getItemPropertiesList().add(itemProperties2);
						ItemPropertiesCommand itemPropertiesCommand1 = new ItemPropertiesCommand();
						LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand1); 
						itemPropertiesCommand1.setPropertyValue(mutlLangPv);
						itemPropertyList.add(itemPropertiesCommand1);
						Long ipId = itemProperties.getId();
						for (int i = 0; i < values.length; i++) {
							String val = values[i];
							String lang = langs[i];
							ItemPropertiesLang ipl = new ItemPropertiesLang();
							ipl.setItemPropertiesId(ipId);
							ipl.setLang(lang);
							ipl.setPropertyValue(val);
							ipl = itemPropertiesLangDao.save(ipl);
							itemI18nCommand.getItemPropertiesLangList().add(ipl);
						}
					}
				}else{
					//多选
					itemProperties2 = new ItemProperties();
					itemProperties2.setPropertyId(itemProperties.getPropertyId());
					itemProperties2.setPropertyValueId(itemProperties.getPropertyValueId());
					itemProperties2.setItemId(itemId);
					itemProperties2.setCreateTime(new Date());
					itemProperties2.setVersion(new Date());
					itemProperties2 = itemPropertiesDao.save(itemProperties2);
					itemI18nCommand.getItemPropertiesList().add(itemProperties2);
					if(i18n){
						for (int i = 0; i < MutlLang.i18nLangs().size(); i++) {
							ItemPropertiesLang ipl = new ItemPropertiesLang();
							String lang  = MutlLang.i18nLangs().get(i);
							ipl.setItemPropertiesId(itemProperties2.getId());
							ipl.setLang(lang);
							ipl = itemPropertiesLangDao.save(ipl);
							itemI18nCommand.getItemPropertiesLangList().add(ipl);
						}
					}
					ItemPropertiesCommand itemPropertiesCommand1 = new ItemPropertiesCommand();
					LangProperty.I18nPropertyCopyToSource(itemProperties2, itemPropertiesCommand1); 
					itemPropertyList.add(itemPropertiesCommand1);
				}
			}else{
				LangProperty langProperty = itemPropertiesCommand.getPropertyValue();
				if(langProperty != null && itemPropertiesCommand.getPropertyValueId() == null){
					SingleLang  singleLangPv =  (SingleLang) itemPropertiesCommand.getPropertyValue();
					String  propertyValue = singleLangPv.getValue();
					if (propertyValue.indexOf("||") != -1) {
						String[] propertyValues = propertyValue.split("\\|\\|");
						for (String pv : propertyValues) {
							itemProperties2.setPropertyValue(pv);
							itemProperties2.setPropertyId(itemProperties.getPropertyId());
							itemProperties2.setItemId(itemId);
							itemProperties2.setCreateTime(new Date());
							itemProperties2.setVersion(new Date());
							itemProperties = itemPropertiesDao.save(itemProperties2);
							itemI18nCommand.getItemPropertiesList().add(itemProperties2);
							ItemPropertiesCommand itemPropertiesCommand1 = new ItemPropertiesCommand();
							LangProperty.I18nPropertyCopyToSource(itemProperties, itemPropertiesCommand1); 
							itemPropertyList.add(itemPropertiesCommand1);
						}
					} else {
						//自定义多选无分隔
						itemProperties2 = new ItemProperties();
						itemProperties2.setItemId(itemId);
						itemProperties2.setCreateTime(new Date());
						itemProperties2.setPropertyId(itemProperties.getPropertyId());
						itemProperties2.setPropertyValue(propertyValue);
						itemProperties2 = itemPropertiesDao.save(itemProperties2);
						itemI18nCommand.getItemPropertiesList().add(itemProperties2);
						ItemPropertiesCommand itemPropertiesCommand1 = new ItemPropertiesCommand();
						LangProperty.I18nPropertyCopyToSource(itemProperties2, itemPropertiesCommand1); 
						itemPropertyList.add(itemPropertiesCommand1);
					}
				}else{
					//多选
					itemProperties2 = new ItemProperties();
					itemProperties2.setPropertyId(itemProperties.getPropertyId());
					itemProperties2.setPropertyValueId(itemProperties.getPropertyValueId());
					itemProperties2.setItemId(itemId);
					itemProperties2.setCreateTime(new Date());
					itemProperties2.setVersion(new Date());
					itemProperties2 = itemPropertiesDao.save(itemProperties2);
					itemI18nCommand.getItemPropertiesList().add(itemProperties2);
				}
				
			}
			}
		}
	private boolean isItemPrpertiesExistInList(ItemPropertiesCommand ip, List<ItemPropertiesCommand> ipList) {
		for (ItemPropertiesCommand i : ipList) {
			if (ip.getPropertyValueId() != null) {
				if (ip.getPropertyId().equals(i.getPropertyId()) && ip.getPropertyValueId().equals(i.getPropertyValueId())) {
					return true;
				}
			}
			if (ip.getPropertyValue() != null) {
			boolean i18n = LangProperty.getI18nOnOff();
			if(i18n){
				MutlLang singleLang1 =  (MutlLang) ip.getPropertyValue();
				MutlLang singleLang2 =  (MutlLang) i.getPropertyValue();
				String[] v1 =singleLang1.getValues();
				String[] v2 = null;
				if(singleLang2==null){
					v2 = new String[MutlLang.i18nSize()];
				}else{
					 v2 =singleLang2.getValues();
				}
				if (ip.getPropertyId().equals(i.getPropertyId()) && Arrays.toString(v1).equals(Arrays.toString(v2))) {
					return true;
				}
			}else{
				SingleLang singleLang =  (SingleLang) ip.getPropertyValue();
				SingleLang singleLang1 =  (SingleLang) i.getPropertyValue();
				if (ip.getPropertyId().equals(i.getPropertyId()) && singleLang.getValue().equals(singleLang1.getValue())) {
					return true;
				}
			}
			}
		}
		return false;
	}
	
	private Item createOrUpdateItem(ItemInfoCommand itemCommand, Long[] categoriesIds){
		
		Item item = null;
		if (itemCommand.getId() != null) {// 更新
			item = itemDao.getByPrimaryKey(itemCommand.getId());
			item.setModifyTime(new Date());
			item.setCode(itemCommand.getCode());
			if (categoriesIds != null && categoriesIds.length > 0) {
				item.setIsaddcategory(1);
			} else {
				item.setIsaddcategory(0);
			}
			item = itemDao.save(item);
		} else {// 新增

			if (itemCommand.getId() == null) {
				Integer count = validateItemCode(itemCommand.getCode(),
						itemCommand.getShopId());

				if (count > 0) {
					throw new BusinessException(ErrorCodes.PRODUCT_CODE_REPEAT);
				}
			}
			item = new Item();
			item.setType(itemCommand.getItemType());
			item.setCode(itemCommand.getCode());
			// Lifecycle状态： 0：无效 1：有效 2：删除 3：未激活
			item.setLifecycle(Item.LIFECYCLE_UNACTIVE);
			item.setCreateTime(new Date());
			item.setShopId(itemCommand.getShopId());
			item.setIndustryId(Long.parseLong(itemCommand.getIndustryId()));
			if(Item.ITEM_TYPE_SIMPLE.equals(item.getType())) {
				item.setIndustryId(Long.valueOf(itemCommand.getIndustryId()));
			}

			if (categoriesIds != null && categoriesIds.length > 0) {
				item.setIsaddcategory(1);
			} else {
				item.setIsaddcategory(0);
			}
			item.setIsAddTag(0);
			item = itemDao.save(item);
		}
		
		return item;
	}
	
	private void itemCategoryHandle(ItemInfoCommand itemCommand, Item item, Long[] categoriesIds, Long defaultCategoryId){
		if (categoriesIds != null && categoriesIds.length > 0) {
//			Long defaultId = itemCategoryManager.getDefaultItemCategoryId(categoriesIds);

//			Long[] categoryIdArray = new Long[categoriesIds.length - 1];
			
//			int index = 0;
//			for (Long id : categoriesIds) {
//				if (!defaultId.equals(id)) {
//					categoryIdArray[index] = id;
//					index++;
//				}
//			}
			
			// 绑定附加分类
			itemCategoryManager.createOrUpdateItemCategory(itemCommand,
					item.getId(), categoriesIds);
			// 绑定默认分类
			itemCategoryManager.createOrUpdateItemDefaultCategory(itemCommand,
					item.getId(), defaultCategoryId);

		} else {
			List<ItemCategory> ctgList = itemCategoryManager
					.findItemCategoryListByItemId(item.getId());
			Long[] itemIds = new Long[1];
			itemIds[0] = item.getId();
			for (ItemCategory ic : ctgList) {
				List<Long> itemIdList = new ArrayList<Long>();
				itemIdList.add(item.getId());
				itemCategoryManager.unBindItemCategory(itemIds,
						ic.getCategoryId());

			}
		}
	}
}
