package com.baozun.nebula.solr.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.BaseItemForSolrCommand;
import com.baozun.nebula.command.ItemForCategoryCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.ItemSolrI18nCommand;
import com.baozun.nebula.command.ItemTagCommand;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemPropertiesDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.model.product.CategoryLang;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemImageLang;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.ItemSortScore;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.handler.ItemSortScoreHandler;
import com.baozun.nebula.sdk.manager.SdkItemSortScoreManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemFavouriteCountForSolrCommand;
import com.baozun.nebula.solr.command.ItemRateForSolrCommand;
import com.baozun.nebula.solr.command.ItemSalesCountForSolrCommand;
import com.baozun.nebula.solr.dao.BaseItemForSolrCategoryCommandDao;
import com.baozun.nebula.solr.dao.BaseItemForSolrCommandDao;
import com.baozun.nebula.solr.dao.BaseItemForSolrImageCommandDao;
import com.baozun.nebula.solr.dao.BaseItemForSolrPorpertiesCommandDao;
import com.baozun.nebula.solr.dao.BaseItemForSolrTagCommandDao;
import com.baozun.nebula.solr.dao.ItemForSolrFavouritedCountCommandDao;
import com.baozun.nebula.solr.dao.ItemForSolrRateCommandDao;
import com.baozun.nebula.solr.dao.ItemForSolrSalesCountCommandDao;
import com.feilong.core.Validator;

@Service("ItemInfoManager")
@Transactional
public class ItemInfoManagerImpl implements ItemInfoManager {

	private static final Logger log = LoggerFactory.getLogger(ItemInfoManagerImpl.class);
	
	@Autowired
	private BaseItemForSolrImageCommandDao baseItemForSolrImageCommandDao;
	
	@Autowired
	private BaseItemForSolrCategoryCommandDao baseItemForSolrCategoryCommandDao;
	
	@Autowired
	private BaseItemForSolrPorpertiesCommandDao baseItemForSolrPorpertiesCommandDao;
	
	@Autowired
	private BaseItemForSolrCommandDao baseItemForSolrCommandDao;
	
	@Autowired
	private BaseItemForSolrTagCommandDao baseItemForSolrTagCommandDao;
	
	@Autowired
	private ItemForSolrRateCommandDao itemForSolrRateCommandDao;
	
	@Autowired
	private ItemForSolrSalesCountCommandDao itemForSolrSalesCountCommandDao;
	
	@Autowired
	private ItemForSolrFavouritedCountCommandDao itemForSolrFavouritedCountCommandDao;
	
	@Autowired
	private SdkItemSortScoreManager sdkItemSortScoreManager;
	
	
	@Autowired(required = false)
	private ItemSortScoreHandler itemSortScoreHandler;
	
	@Autowired
	private ItemInfoDao itemInfoDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ItemPropertiesDao itemPropertiesDao;
	
	@Autowired
	private ItemImageDao itemImageDao;
	
	@Autowired
	private PropertyValueDao propertyValueDao;
	
	@Override 
	public List<ItemSolrCommand> findItemCommandByItemId(Integer[] itemTypes, List<Long> itemIds) {
		return setItemSolrCommand(itemTypes, itemIds);
	}

	@Override
	public List<ItemSolrCommand> findItemCommand(Integer[] itemTypes) {
		return setItemSolrCommand(itemTypes, null);
	}
	@Override
	@Transactional(readOnly=true)
	public List<ItemSolrCommand> setItemSolrCommand(Integer[] itemTypes, List<Long> itemIds){
		List<ItemSolrCommand> itemCommandList = new ArrayList<ItemSolrCommand>();
		
		List<BaseItemForSolrCommand> baseItemForSolrCommandList = new ArrayList<BaseItemForSolrCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandForSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemForCategoryCommand> itemForCategoryCommandList = new ArrayList<ItemForCategoryCommand>();
		List<ItemTagCommand> itemTagCommandList = new ArrayList<ItemTagCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandForCustomerList = new ArrayList<ItemPropertiesCommand>();
		List<ItemRateForSolrCommand> itemForSolrRateCommandList = new ArrayList<ItemRateForSolrCommand>();
		List<ItemImageCommand> itemImageCommandList = new ArrayList<ItemImageCommand>();
		List<ItemSalesCountForSolrCommand> itemForSolrSalesCountCommandList = new ArrayList<ItemSalesCountForSolrCommand>();
		List<ItemFavouriteCountForSolrCommand> itemFavouriteCountForSolrCommandList = new ArrayList<ItemFavouriteCountForSolrCommand>();
		/*
		 * 1. 获取默认排序规则信息
		 */
		List<ItemSortScore> scoreList = sdkItemSortScoreManager.findAllEffectItemSortScoreList();
		if(null!=itemIds && itemIds.size()>0){
			baseItemForSolrCommandList = baseItemForSolrCommandDao.findItemCommandByItemId(itemTypes, itemIds);
			itemPropertiesCommandForSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesByItemIdForSearch(itemIds);
			itemForCategoryCommandList = baseItemForSolrCategoryCommandDao.findItemCategoryByItemId(itemIds);
			itemTagCommandList = baseItemForSolrTagCommandDao.findItemTagByItemId(itemIds);
			itemImageCommandList = baseItemForSolrImageCommandDao.findItemImageByItemId(itemIds,ItemImage.IMG_TYPE_LIST);
			itemPropertiesCommandForCustomerList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesCustomerForSearchById(itemIds);
			itemForSolrRateCommandList = itemForSolrRateCommandDao.findItemRateById(itemIds);
			itemForSolrSalesCountCommandList = itemForSolrSalesCountCommandDao.findItemCountById(itemIds);
			itemFavouriteCountForSolrCommandList = itemForSolrFavouritedCountCommandDao.findItemCountById(itemIds);
			itemPropertiesCommandWithOutSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesByItemIdWithOutSearch(itemIds);
		}else{
			baseItemForSolrCommandList = baseItemForSolrCommandDao.findItemCommand(itemTypes);
			itemPropertiesCommandForSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesForSearch();
			itemForCategoryCommandList = baseItemForSolrCategoryCommandDao.findItemCategory();
			itemImageCommandList = baseItemForSolrImageCommandDao.findItemImage(ItemImage.IMG_TYPE_LIST);
			itemTagCommandList = baseItemForSolrTagCommandDao.findItemTag();
			itemPropertiesCommandForCustomerList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesCustomerForSearch();
			itemForSolrRateCommandList = itemForSolrRateCommandDao.findItemRate();
			itemForSolrSalesCountCommandList = itemForSolrSalesCountCommandDao.findItemCount();
			itemFavouriteCountForSolrCommandList = itemForSolrFavouritedCountCommandDao.findItemCount();
			itemPropertiesCommandWithOutSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesWithOutSearch();
		}
		
		List<ItemPropertiesCommand> allItemColorList = new ArrayList<ItemPropertiesCommand>();
		
			for(BaseItemForSolrCommand baseItemForSolrCommand : baseItemForSolrCommandList){
				// 目前仅普通商品需要刷新solr 
				// changed by yue.ch in 2016-6-22
				if(!Item.ITEM_TYPE_SIMPLE.equals(baseItemForSolrCommand.getType())) {
					continue;
				}
				List<ItemPropertiesCommand> itemPropertiesForSearchList = new ArrayList<ItemPropertiesCommand>();
				List<ItemPropertiesCommand> itemPropertiesWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
				List<ItemPropertiesCommand> itemPropertiesForCustomerList = new ArrayList<ItemPropertiesCommand>();
				Map<Long,ItemForCategoryCommand> itemForCategoryMap = new HashMap<Long,ItemForCategoryCommand>();
				List<ItemImageCommand> itemImageList = new ArrayList<ItemImageCommand>();
				List<ItemTagCommand> itemTagList = new ArrayList<ItemTagCommand>();
				ItemSolrCommand itemSolrCommand = new ItemSolrCommand();
				itemSolrCommand.setId(baseItemForSolrCommand.getId());
//				itemSolrCommand.setTagId(baseItemForSolrCommand.getId());
				itemSolrCommand.setCode(baseItemForSolrCommand.getCode());
				itemSolrCommand.setTitle(baseItemForSolrCommand.getTitle());
				itemSolrCommand.setSubTitle(baseItemForSolrCommand.getSubTitle());
				itemSolrCommand.setSketch(baseItemForSolrCommand.getSketch());
				
				itemSolrCommand.setDescription(baseItemForSolrCommand.getDescription());
				itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
				itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
				itemSolrCommand.setIndustryId(baseItemForSolrCommand.getIndustryId());
				itemSolrCommand.setIndustryName(baseItemForSolrCommand.getIndustryName());
				itemSolrCommand.setLifecycle(baseItemForSolrCommand.getLifecycle());
				itemSolrCommand.setCreateTime(baseItemForSolrCommand.getCreateTime());
				itemSolrCommand.setList_price(baseItemForSolrCommand.getList_price());
				itemSolrCommand.setSale_price(baseItemForSolrCommand.getSale_price());
				itemSolrCommand.setModifyTime(baseItemForSolrCommand.getModifyTime());
				itemSolrCommand.setListTime(baseItemForSolrCommand.getListTime());
				itemSolrCommand.setDelistTime(baseItemForSolrCommand.getDelistTime());
				itemSolrCommand.setTemplateId(baseItemForSolrCommand.getTemplateId());
				itemSolrCommand.setIsaddcategory(baseItemForSolrCommand.getIsaddcategory());
				itemSolrCommand.setIsAddTag(baseItemForSolrCommand.getIsAddTag());
				itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
				itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
				Date beginTime = baseItemForSolrCommand.getActiveBeginTime();
				
				if(beginTime == null){
					beginTime = new Date();
				}
				
				itemSolrCommand.setActiveBeginTime(beginTime);
				itemSolrCommand.setActiveEndTime(baseItemForSolrCommand.getActiveEndTime());
				itemSolrCommand.setSeoDescription(baseItemForSolrCommand.getSeoDescription());
				itemSolrCommand.setSeoKeywords(baseItemForSolrCommand.getSeoKeywords());
				itemSolrCommand.setSeoTitle(baseItemForSolrCommand.getSeoTitle());
//				itemSolrCommand.setSpread("0");
				itemSolrCommand.setStyle(baseItemForSolrCommand.getStyle());
				
				for(ItemRateForSolrCommand itemRateForSolrCommand : itemForSolrRateCommandList){
					if(null != itemRateForSolrCommand.getItemId() && itemRateForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId()) && itemRateForSolrCommand.getCount()>0 && null!=itemRateForSolrCommand.getCount()){
							String parten = "#.#";
							DecimalFormat decimal = new DecimalFormat(parten);
							itemSolrCommand.setRankavg(Float.parseFloat(decimal.format(itemRateForSolrCommand.getSum()/itemRateForSolrCommand.getCount())));
					}
				}
				
				if(null==itemSolrCommand.getRankavg()){
					itemSolrCommand.setRankavg(Float.parseFloat("0.0"));
				}
				
				if(Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveBeginTime()) && Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveEndTime())){
					itemSolrCommand.setItemIsDisplay(true);
				}else{
					itemSolrCommand.setItemIsDisplay(false);
				}
				
				for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForCustomerList){
					if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemPropertiesForCustomerList.add(itemPropertiesCommand);
					}
				}
				
				for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandWithOutSearchList){
					if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemPropertiesWithOutSearchList.add(itemPropertiesCommand);
					}
				}
				
				for(ItemSalesCountForSolrCommand itemSalesCountForSolrCommand : itemForSolrSalesCountCommandList){
					if(null != itemSalesCountForSolrCommand.getItemId() && itemSalesCountForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemSolrCommand.setSalesCount(itemSalesCountForSolrCommand.getCount());
					}
				}
				
				for(ItemFavouriteCountForSolrCommand favCountCommand : itemFavouriteCountForSolrCommandList){
					if(null != favCountCommand.getItemId() && favCountCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemSolrCommand.setFavoredCount(favCountCommand.getCount());
					}
				}
				
				if(null==itemSolrCommand.getSalesCount()){
					itemSolrCommand.setSalesCount(0);
				}
				
				for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForSearchList){
					if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						if(itemPropertiesCommand.getIs_color_prop()){
							allItemColorList.add(itemPropertiesCommand);
						}
						itemPropertiesForSearchList.add(itemPropertiesCommand);
					}
				}
				
				for(ItemForCategoryCommand itemForCategoryCommand : itemForCategoryCommandList){
					if(null != itemForCategoryCommand.getItemId() && itemForCategoryCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemForCategoryMap.put(itemForCategoryCommand.getCategoryId(),itemForCategoryCommand);
						Long parentId = itemForCategoryCommand.getParent_id();
						while(null!=parentId){
							ItemForCategoryCommand itemForCategoryCommandParent = new ItemForCategoryCommand();
							itemForCategoryCommandParent = baseItemForSolrCategoryCommandDao.findParentCategoryById(parentId);
							if(null!=itemForCategoryCommandParent){
								parentId = itemForCategoryCommandParent.getParent_id();
								itemForCategoryCommandParent.setItemId(baseItemForSolrCommand.getId());
								itemForCategoryMap.put(itemForCategoryCommandParent.getCategoryId(),itemForCategoryCommandParent);
							}else{
								parentId = null;
							}
						}
					}
				}
				
				for(ItemImageCommand itemImageCommand : itemImageCommandList){
					if(null != itemImageCommand.getItemId() && itemImageCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemImageList.add(itemImageCommand);
					}
				}
				
				for(ItemTagCommand itemTagCommand : itemTagCommandList){
					if(null != itemTagCommand.getItemId() && itemTagCommand.getItemId().equals(baseItemForSolrCommand.getId())){
						itemTagList.add(itemTagCommand);
					}
				}
				
				itemSolrCommand.setCategoryMap(itemForCategoryMap);
				itemSolrCommand.setDynamicListForSearch(itemPropertiesForSearchList);
				itemSolrCommand.setImageList(itemImageList);
				itemSolrCommand.setTagList(itemTagList);
				itemSolrCommand.setDynamicListForCustomer(itemPropertiesForCustomerList);
				itemSolrCommand.setDefaultSort(getDefaultSort(itemSolrCommand,scoreList));
				itemCommandList.add(itemSolrCommand);
			}
			
			/*
			if(Validator.isNotNullOrEmpty(allItemColorList)&&Validator.isNotNullOrEmpty(itemImageCommandList)){
				for(ItemImageCommand imgCmd : itemImageCommandList){
					for(ItemPropertiesCommand ipCmd : allItemColorList){
						if(imgCmd.getItemProId().equals(ipCmd.getItem_properties_id())){
							imgCmd.setColor(ipCmd.getItem_properties_id());
						}
					}
				}
				
			}
			*/
			/*
			for(BaseItemForSolrCommand baseItemForSolrCommand : baseItemForSolrCommandList){
				for(ItemPropertiesCommand itemColorCommand : allItemColorList){
					if(baseItemForSolrCommand.getId().equals(itemColorCommand.getItemId())){
						List<ItemPropertiesCommand> itemPropertiesForSearchList = new ArrayList<ItemPropertiesCommand>();
						List<ItemPropertiesCommand> itemPropertiesForCustomerList = new ArrayList<ItemPropertiesCommand>();
						Map<Long,ItemForCategoryCommand> itemForCategoryMap = new HashMap<Long,ItemForCategoryCommand>();
						List<ItemImageCommand> itemImageList = new ArrayList<ItemImageCommand>();
						List<ItemTagCommand> itemTagList = new ArrayList<ItemTagCommand>();
						
						ItemSolrCommand itemSolrCommand = new ItemSolrCommand();
						itemSolrCommand.setId(baseItemForSolrCommand.getId());
						itemSolrCommand.setTagId(Long.parseLong(baseItemForSolrCommand.getId()+""+(itemColorCommand.getProperty_id()==null?"":itemColorCommand.getProperty_id())+""+(itemColorCommand.getPropertyValue()==null?"":itemColorCommand.getPropertyValue())));
						itemSolrCommand.setCode(baseItemForSolrCommand.getCode());
						itemSolrCommand.setTitle(baseItemForSolrCommand.getTitle());
						itemSolrCommand.setSubTitle(baseItemForSolrCommand.getSubTitle());
						itemSolrCommand.setSketch(baseItemForSolrCommand.getSketch());
						itemSolrCommand.setDescription(baseItemForSolrCommand.getDescription());
						itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
						itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
						itemSolrCommand.setIndustryId(baseItemForSolrCommand.getIndustryId());
						itemSolrCommand.setIndustryName(baseItemForSolrCommand.getIndustryName());
						itemSolrCommand.setLifecycle(baseItemForSolrCommand.getLifecycle());
						itemSolrCommand.setCreateTime(baseItemForSolrCommand.getCreateTime());
						itemSolrCommand.setList_price(baseItemForSolrCommand.getList_price());
						itemSolrCommand.setSale_price(baseItemForSolrCommand.getSale_price());
						itemSolrCommand.setModifyTime(baseItemForSolrCommand.getModifyTime());
						itemSolrCommand.setListTime(baseItemForSolrCommand.getListTime());
						itemSolrCommand.setDelistTime(baseItemForSolrCommand.getDelistTime());
						itemSolrCommand.setTemplateId(baseItemForSolrCommand.getTemplateId());
						itemSolrCommand.setIsaddcategory(baseItemForSolrCommand.getIsaddcategory());
						itemSolrCommand.setIsAddTag(baseItemForSolrCommand.getIsAddTag());
						itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
						itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
						itemSolrCommand.setActiveBeginTime(baseItemForSolrCommand.getActiveBeginTime());
						itemSolrCommand.setActiveEndTime(baseItemForSolrCommand.getActiveEndTime());
						itemSolrCommand.setSeoDescription(baseItemForSolrCommand.getSeoDescription());
						itemSolrCommand.setSeoKeywords(baseItemForSolrCommand.getSeoKeywords());
						itemSolrCommand.setSeoTitle(baseItemForSolrCommand.getSeoTitle());
						itemSolrCommand.setSpread("1");
						
						for(ItemSalesCountForSolrCommand itemSalesCountForSolrCommand : itemForSolrSalesCountCommandList){
							if(null != itemSalesCountForSolrCommand.getItemId() && itemSalesCountForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								itemSolrCommand.setSalesCount(itemSalesCountForSolrCommand.getCount());
							}
						}
						
						if(null!=itemSolrCommand.getSalesCount()){
							itemSolrCommand.setSalesCount(0);
						}
						
						for(ItemRateForSolrCommand itemRateForSolrCommand : itemForSolrRateCommandList){
							if(null != itemRateForSolrCommand.getItemId() && itemRateForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								String parten = "#.#";
								DecimalFormat decimal = new DecimalFormat(parten);
								//itemSolrCommand.setRankavg(Float.parseFloat(decimal.format(itemRateForSolrCommand.getSum()/itemRateForSolrCommand.getCount())));
							}
						}
						
						if(Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveBeginTime()) && Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveEndTime())){
							itemSolrCommand.setItemIsDisplay(true);
						}else{
							itemSolrCommand.setItemIsDisplay(false);
						}
						
						for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForCustomerList){
							if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								itemPropertiesForCustomerList.add(itemPropertiesCommand);
							}
						}
						
						for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForSearchList){
							if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								if(itemPropertiesCommand.getIs_color_prop() && Validator.isNotNullOrEmpty(itemPropertiesCommand.getPropertyValue()) && (Validator.isNotNullOrEmpty(itemColorCommand.getPropertyValue())) && itemPropertiesCommand.getPropertyValue().equals(itemColorCommand.getPropertyValue())){
									itemPropertiesForSearchList.add(itemPropertiesCommand);
								}else if(!itemPropertiesCommand.getIs_color_prop()){
									itemPropertiesForSearchList.add(itemPropertiesCommand);
								}
							}
						}
						
						for(ItemForCategoryCommand itemForCategoryCommand : itemForCategoryCommandList){
							if(null != itemForCategoryCommand.getItemId() && itemForCategoryCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								itemForCategoryMap.put(itemForCategoryCommand.getCategoryId(),itemForCategoryCommand);
								Long parentId = itemForCategoryCommand.getParent_id();
								while(null!=parentId){
									ItemForCategoryCommand itemForCategoryCommandParent = new ItemForCategoryCommand();
									itemForCategoryCommandParent = baseItemForSolrCategoryCommandDao.findParentCategoryById(parentId);
									if(null!=itemForCategoryCommandParent){
										parentId = itemForCategoryCommandParent.getParent_id();
										itemForCategoryCommandParent.setItemId(baseItemForSolrCommand.getId());
										itemForCategoryMap.put(itemForCategoryCommandParent.getCategoryId(),itemForCategoryCommandParent);
									}else{
										parentId = null;
									}
								}
							}
						}
						
						itemImageList = baseItemForSolrImageCommandDao.findItemImageByInfo(itemIds,Long.parseLong(itemColorCommand.getProperty_id()),Long.parseLong(itemColorCommand.getPropertyValue()));
						
						for(ItemTagCommand itemTagCommand : itemTagCommandList){
							if(null != itemTagCommand.getItemId() && itemTagCommand.getItemId().equals(baseItemForSolrCommand.getId())){
								itemTagList.add(itemTagCommand);
							}
						}
						
						itemSolrCommand.setCategoryMap(itemForCategoryMap);
						itemSolrCommand.setDynamicListForSearch(itemPropertiesForSearchList);
						itemSolrCommand.setImageList(itemImageList);
						itemSolrCommand.setTagList(itemTagList);
						itemSolrCommand.setDynamicListForCustomer(itemPropertiesForCustomerList);
						itemCommandList.add(itemSolrCommand);
					}
				}
			}
		*/
		return itemCommandList;
	}
	
	/**
	 * 设置默认排序
	 * 
	 * @param itemSolrCommand
	 */
	private Integer getDefaultSort(final ItemSolrCommand itemSolrCommand,List<ItemSortScore> scoreList){
		Integer defaultSort = 0;
		if(Validator.isNotNullOrEmpty(scoreList)){
			for(ItemSortScore itemSortScore : scoreList){
				/*
				 * 2. 获取相应排序值
				 */
				defaultSort = defaultSort + getSortScore(itemSortScore,itemSolrCommand);
			}
		}
		/* 
		 * 3. 获取商城的值
		 */
		if(null != itemSortScoreHandler){
			try {
				defaultSort = defaultSort + itemSortScoreHandler.getSortScore(scoreList, itemSolrCommand);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return defaultSort;
	}
	
	/**
	 * 获取排序值
	 * 
	 * @param itemSortScore
	 * @param itemSolrCommand
	 * @return
	 */
	private int getSortScore(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int sortScore = 0;
		if(null == itemSortScore || null == itemSolrCommand){
			return 0;
		}
		if(Constants.SORT_SCORE_TYPE_CATEGORY.equals(itemSortScore.getType())){
			/*
			 * 类目信息处理
			 */
			sortScore = sortScore + handleCategory(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_PROPETY.equals(itemSortScore.getType())) {
			/*
			 * 属性处理
			 */
			sortScore = sortScore + handleProperty(itemSortScore,itemSolrCommand); 
		} else if(Constants.SORT_SCORE_TYPE_PRICE.equals(itemSortScore.getType())) {
			/*
			 * 价格处理
			 */
			sortScore = sortScore + handleListPrice(itemSortScore,itemSolrCommand); 
		} else if(Constants.SORT_SCORE_TYPE_SALES_VOLUME.equals(itemSortScore.getType())){
			/*
			 * 销量
			 */
			sortScore = sortScore + handleSalesVolume(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_COLLECTIONS.equals(itemSortScore.getType())){
			/*
			 * 收藏数量
			 */
			sortScore = sortScore + handleCollection(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_SALES_PRICE.equals(itemSortScore.getType())){
			/*
			 * 价格处理
			 */
			sortScore = sortScore + handleSalesPrice(itemSortScore,itemSolrCommand); 
		}
		return sortScore;
	}
	
	private int getSortScoreI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int sortScore = 0;
		if(null == itemSortScore || null == itemSolrCommand){
			return 0;
		}
		if(Constants.SORT_SCORE_TYPE_CATEGORY.equals(itemSortScore.getType())){
			/*
			 * 类目信息处理
			 */
			sortScore = sortScore + handleCategoryI18n(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_PROPETY.equals(itemSortScore.getType())) {
			/*
			 * 属性处理
			 */
			sortScore = sortScore + handlePropertyI18n(itemSortScore,itemSolrCommand); 
		} else if(Constants.SORT_SCORE_TYPE_PRICE.equals(itemSortScore.getType())) {
			/*
			 * 价格处理
			 */
			sortScore = sortScore + handleListPriceI18n(itemSortScore,itemSolrCommand); 
		} else if(Constants.SORT_SCORE_TYPE_SALES_VOLUME.equals(itemSortScore.getType())){
			/*
			 * 销量
			 */
			sortScore = sortScore + handleSalesVolumeI18n(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_COLLECTIONS.equals(itemSortScore.getType())){
			/*
			 * 收藏数量
			 */
			sortScore = sortScore + handleCollectionI18n(itemSortScore,itemSolrCommand);
		} else if(Constants.SORT_SCORE_TYPE_SALES_PRICE.equals(itemSortScore.getType())){
			/*
			 * 价格处理
			 */
			sortScore = sortScore + handleSalesPriceI18n(itemSortScore,itemSolrCommand); 
		}
		return sortScore;
	}
	
	/**
	 * 价格处理
	 * 
	 * @param itemSortScore
	 * @param itemSolrCommand
	 * @return
	 */
	private int handleSalesPrice(ItemSortScore itemSortScore,
			ItemSolrCommand itemSolrCommand) {
		int sortScore = 0;
		Double price = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 价格转换
		 */
		try {
			price = Double.parseDouble(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == price){
			return 0;
		}
		Double salesPrice = itemSolrCommand.getSale_price();
		if(null == salesPrice){
			return 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), price, salesPrice)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	private int handleSalesPriceI18n(ItemSortScore itemSortScore,
			ItemSolrI18nCommand itemSolrCommand) {
		int sortScore = 0;
		Double price = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 价格转换
		 */
		try {
			price = Double.parseDouble(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == price){
			return 0;
		}
		Double salesPrice = itemSolrCommand.getSale_price();
		if(null == salesPrice){
			return 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), price, salesPrice)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}


	/**
	 * 处理类目
	 * 
	 * @param itemSortScore
	 * @param baseItemForSolrCommand
	 * @return
	 */
	private int handleCategory(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int retSortScore = 0;
		Long categoryId = itemSortScore.getCategoryId();
		if(null == categoryId || null == itemSortScore.getScore()){
			return 0;
		}
		Map<Long, ItemForCategoryCommand> categoryMap = itemSolrCommand.getCategoryMap();
		if(null == categoryMap){
			return 0;
		}
		for(Long cId : categoryMap.keySet()){
			if(categoryId.equals(cId)){
				retSortScore = retSortScore + itemSortScore.getScore();
			}
		}
	    return retSortScore;
	}
	
	private int handleCategoryI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int retSortScore = 0;
		Long categoryId = itemSortScore.getCategoryId();
		if(null == categoryId || null == itemSortScore.getScore()){
			return 0;
		}
		Map<Long, ItemForCategoryCommand> categoryMap = itemSolrCommand.getCategoryMap();
		if(null == categoryMap){
			return 0;
		}
		for(Long cId : categoryMap.keySet()){
			if(categoryId.equals(cId)){
				retSortScore = retSortScore + itemSortScore.getScore();
			}
		}
	    return retSortScore;
	}
	
	/**
	 * 处理属性
	 * 
	 * @param itemSortScore
	 * @param baseItemForSolrCommand
	 * @return
	 */
	
	private int handleProperty(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int retSortScore = 0;
		Long propertyId = itemSortScore.getPropertyId();
		String params = itemSortScore.getParam();
		if(null == propertyId || null == params 
				|| null == itemSortScore.getScore()
				|| null == itemSortScore.getOperator()){
			return 0;
		}
		List<ItemPropertiesCommand> itemPropCommand = itemSolrCommand.getDynamicListForSearch();
		List<ItemPropertiesCommand> itemCusPropCommand = itemSolrCommand.getDynamicListForCustomer();
		retSortScore = retSortScore + propScoreHandle(itemPropCommand, itemSortScore);
		retSortScore = retSortScore + propScoreHandle(itemCusPropCommand, itemSortScore);
	    return retSortScore;
	}
	
	private int handlePropertyI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int retSortScore = 0;
		Long propertyId = itemSortScore.getPropertyId();
		String params = itemSortScore.getParam();
		if(null == propertyId || null == params 
				|| null == itemSortScore.getScore()
				|| null == itemSortScore.getOperator()){
			return 0;
		}
		List<ItemPropertiesCommand> itemPropCommand = itemSolrCommand.getDynamicListForSearch();
		List<ItemPropertiesCommand> itemCusPropCommand = itemSolrCommand.getDynamicListForCustomer();
		retSortScore = retSortScore + propScoreHandle(itemPropCommand, itemSortScore);
		retSortScore = retSortScore + propScoreHandle(itemCusPropCommand, itemSortScore);
	    return retSortScore;
	}
	
	/**
	 * 根据属性列表获取熟悉
	 * 
	 * @param itemPropCommand
	 * @param itemSortScore
	 * @return
	 */
	private int propScoreHandle(List<ItemPropertiesCommand> itemPropCommand,ItemSortScore itemSortScore){
		int retSortScore = 0;
		Long propertyId = itemSortScore.getPropertyId();
		String params = itemSortScore.getParam();
		Double propValDou = null;
		String oper = itemSortScore.getOperator();
		propValDou = convertToDouble(params);
		if(Validator.isNotNullOrEmpty(itemPropCommand)){
			if(null != propValDou && 
					(Constants.SORT_SCORE_OPER_GREATER.equals(oper) || Constants.SORT_SCORE_OPER_LESS.equals(oper))){
				for(ItemPropertiesCommand propComand : itemPropCommand){
					Double propValGet = convertToDouble(propComand.getProValue());
					if(null != propValGet && (propertyId+"").equals(propComand.getProperty_id()) && 
							compareVal(oper, propValDou, propValGet)){
						retSortScore = retSortScore + itemSortScore.getScore();
					}
				}
			} else if(Constants.SORT_SCORE_OPER_EQUAL.equals(oper)) {
				for(ItemPropertiesCommand propComand : itemPropCommand){
					if((propertyId+"").equals(propComand.getProperty_id()) && 
							params.equals(propComand.getProValue())){
						retSortScore = retSortScore + itemSortScore.getScore();
					}
				}
			}
		}
		return retSortScore;
	}
	
	/**
	 * 转换为double
	 * 
	 * @param val
	 * @return
	 */
	private Double convertToDouble(String val){
		Double ret = null;
		try {
			ret = Double.parseDouble(val);
		} catch(Exception e){
		}
		return ret;
	}
	
	/**
	 * 处理价格
	 * 
	 * @param itemSortScore
	 * @param baseItemForSolrCommand
	 * @return
	 */
	
	private int handleListPrice(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int sortScore = 0;
		Double price = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 价格转换
		 */
		try {
			price = Double.parseDouble(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == price){
			return 0;
		}
		Double listPrice = itemSolrCommand.getList_price();
		if(null == listPrice){
			return 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), price, listPrice)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	private int handleListPriceI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int sortScore = 0;
		Double price = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 价格转换
		 */
		try {
			price = Double.parseDouble(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == price){
			return 0;
		}
		Double listPrice = itemSolrCommand.getList_price();
		if(null == listPrice){
			return 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), price, listPrice)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	/**
	 * 处理销量
	 * 
	 * @param itemSortScore
	 * @param baseItemForSolrCommand
	 * @return
	 */
	private int handleSalesVolume(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int sortScore = 0;
		Integer salesCount = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 格式转换
		 */
		try {
			salesCount = Integer.parseInt(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == salesCount){
			return 0;
		}
		Integer itemSalesCount = itemSolrCommand.getSalesCount();
		if(null == itemSalesCount){
			itemSalesCount = 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), salesCount, itemSalesCount)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	private int handleSalesVolumeI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int sortScore = 0;
		Integer salesCount = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 格式转换
		 */
		try {
			salesCount = Integer.parseInt(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == salesCount){
			return 0;
		}
		Integer itemSalesCount = itemSolrCommand.getSalesCount();
		if(null == itemSalesCount){
			itemSalesCount = 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), salesCount, itemSalesCount)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	/**
	 * 处理收藏
	 * 
	 * @param itemSortScore
	 * @param baseItemForSolrCommand
	 * @return
	 */
	public int handleCollection(ItemSortScore itemSortScore,ItemSolrCommand itemSolrCommand){
		int sortScore = 0;
		Integer favoriteCount = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 格式转换
		 */
		try {
			favoriteCount = Integer.parseInt(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == favoriteCount){
			return 0;
		}
		Integer itemFacoriteCount = itemSolrCommand.getFavoredCount();
		if(null == itemFacoriteCount){
			itemFacoriteCount = 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), favoriteCount, itemFacoriteCount)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	public int handleCollectionI18n(ItemSortScore itemSortScore,ItemSolrI18nCommand itemSolrCommand){
		int sortScore = 0;
		Integer favoriteCount = null;
		if(null == itemSortScore || null == itemSortScore.getScore()){
			return 0;
		}
		/*
		 * 格式转换
		 */
		try {
			favoriteCount = Integer.parseInt(itemSortScore.getParam());
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		if(null == favoriteCount){
			return 0;
		}
		Integer itemFacoriteCount = itemSolrCommand.getFavoredCount();
		if(null == itemFacoriteCount){
			itemFacoriteCount = 0;
		}
		/*
		 * 规则加值
		 */
		if(compareVal(itemSortScore.getOperator(), favoriteCount, itemFacoriteCount)){
			sortScore = sortScore + itemSortScore.getScore();
		}
		return sortScore;
	}
	
	/**
	 * 比较两个值
	 * 
	 * @param oper
	 * @param source
	 * @param dest
	 * @return
	 */
	private boolean compareVal(String oper,Double source,Double dest){
		if(Constants.SORT_SCORE_OPER_GREATER.equals(oper)){
			if(dest.compareTo(source) >= 0){
				return true;
			}
		} else if(Constants.SORT_SCORE_OPER_LESS.equals(oper)) {
			if(dest.compareTo(source) <= 0){
				return true;
			}
		} else if(Constants.SORT_SCORE_OPER_EQUAL.equals(oper)){
			if(dest.compareTo(source) == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 比较两个值
	 * 
	 * @param oper
	 * @param source
	 * @param dest
	 * @return
	 */
	private boolean compareVal(String oper,Integer source,Integer dest){
		if(Constants.SORT_SCORE_OPER_GREATER.equals(oper)){
			if(dest.compareTo(source) >= 0){
				return true;
			}
		} else if(Constants.SORT_SCORE_OPER_LESS.equals(oper)) {
			if(dest.compareTo(source) <= 0){
				return true;
			}
		} else if(Constants.SORT_SCORE_OPER_EQUAL.equals(oper)){
			if(dest.compareTo(source) == 0){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ItemSolrI18nCommand> findItemCommandByItemIdI18n(Integer[] itemTypes, List<Long> itemIds) {
		return setItemSolrCommandI18n(itemTypes, itemIds);
	}
	
	public List<ItemSolrI18nCommand> setItemSolrCommandI18n(Integer[] itemTypes, List<Long> itemIds){
		List<ItemSolrI18nCommand> itemCommandList = new ArrayList<ItemSolrI18nCommand>();
		
		List<BaseItemForSolrCommand> baseItemForSolrCommandList = new ArrayList<BaseItemForSolrCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandForSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
		List<ItemForCategoryCommand> itemForCategoryCommandList = new ArrayList<ItemForCategoryCommand>();
		List<ItemTagCommand> itemTagCommandList = new ArrayList<ItemTagCommand>();
		List<ItemPropertiesCommand> itemPropertiesCommandForCustomerList = new ArrayList<ItemPropertiesCommand>();
		List<ItemRateForSolrCommand> itemForSolrRateCommandList = new ArrayList<ItemRateForSolrCommand>();
		List<ItemImageCommand> itemImageCommandList = new ArrayList<ItemImageCommand>();
		List<ItemSalesCountForSolrCommand> itemForSolrSalesCountCommandList = new ArrayList<ItemSalesCountForSolrCommand>();
		List<ItemFavouriteCountForSolrCommand> itemFavouriteCountForSolrCommandList = new ArrayList<ItemFavouriteCountForSolrCommand>();
		/*
		 * 1. 获取默认排序规则信息
		 */
		List<ItemSortScore> scoreList = sdkItemSortScoreManager.findAllEffectItemSortScoreList();
		if(null!=itemIds && itemIds.size()>0){
			baseItemForSolrCommandList = baseItemForSolrCommandDao.findItemCommandByItemId(itemTypes, itemIds);
			//商品属性国际化
			itemPropertiesCommandForSearchList = findItemPropertiesByItemIdForSearchI18n(itemIds);
			//TODO 国际化分类
			itemForCategoryCommandList = findItemCategoryByItemIdI18n(itemIds);
			itemTagCommandList = baseItemForSolrTagCommandDao.findItemTagByItemId(itemIds);
			//商品图片处理
			itemImageCommandList = findItemImageByItemId(itemIds);
			//自定义多选国际化
			itemPropertiesCommandForCustomerList = findItemPropertiesCustomerForSearchByIdI18n(itemIds);//
			itemForSolrRateCommandList = itemForSolrRateCommandDao.findItemRateById(itemIds);
			itemForSolrSalesCountCommandList = itemForSolrSalesCountCommandDao.findItemCountById(itemIds);
			itemFavouriteCountForSolrCommandList = itemForSolrFavouritedCountCommandDao.findItemCountById(itemIds);
			itemPropertiesCommandWithOutSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesByItemIdWithOutSearch(itemIds);
		}else{
			baseItemForSolrCommandList = baseItemForSolrCommandDao.findItemCommand(itemTypes);
			//商品属性国际化
			itemPropertiesCommandForSearchList =findItemPropertiesByItemIdForSearchI18n(null);
			//TODO 国际化分类
			itemForCategoryCommandList = findItemCategoryByItemIdI18n(null);
			itemImageCommandList = findItemImageByItemId(null);;
			itemTagCommandList = baseItemForSolrTagCommandDao.findItemTag();
			//自定义多选国际化
			itemPropertiesCommandForCustomerList = findItemPropertiesCustomerForSearchByIdI18n(null);
			itemForSolrRateCommandList = itemForSolrRateCommandDao.findItemRate();
			itemForSolrSalesCountCommandList = itemForSolrSalesCountCommandDao.findItemCount();
			itemFavouriteCountForSolrCommandList = itemForSolrFavouritedCountCommandDao.findItemCount();
			itemPropertiesCommandWithOutSearchList = baseItemForSolrPorpertiesCommandDao.findItemPropertiesWithOutSearch();
		}
		
		List<ItemPropertiesCommand> allItemColorList = new ArrayList<ItemPropertiesCommand>();
		//查询商品国际化信息
		Map<Long, List<ItemInfoLang>> mappers =  findItemCommandByItemIdDbI18n(itemIds);
		for(BaseItemForSolrCommand baseItemForSolrCommand : baseItemForSolrCommandList){
			List<ItemPropertiesCommand> itemPropertiesForSearchList = new ArrayList<ItemPropertiesCommand>();
			List<ItemPropertiesCommand> itemPropertiesWithOutSearchList = new ArrayList<ItemPropertiesCommand>();
			List<ItemPropertiesCommand> itemPropertiesForCustomerList = new ArrayList<ItemPropertiesCommand>();
			Map<Long,ItemForCategoryCommand> itemForCategoryMap = new HashMap<Long,ItemForCategoryCommand>();
			List<ItemImageCommand> itemImageList = new ArrayList<ItemImageCommand>();
			List<ItemTagCommand> itemTagList = new ArrayList<ItemTagCommand>();
			ItemSolrI18nCommand itemSolrCommand = new ItemSolrI18nCommand();
			Long itemId = baseItemForSolrCommand.getId();
			itemSolrCommand.setId(itemId);
			itemSolrCommand.setCode(baseItemForSolrCommand.getCode());
			
			itemSolrCommand.setTitle(baseItemForSolrCommand.getTitle());
			itemSolrCommand.setSubTitle(baseItemForSolrCommand.getSubTitle());
			itemSolrCommand.setSketch(baseItemForSolrCommand.getSketch());
			itemSolrCommand.setSeoTitle(baseItemForSolrCommand.getSeoTitle());
			//国际化动态属性
			List<ItemInfoLang> infoLangs =  mappers.get(itemId);
			Map<String, String> disMap = new HashMap<String, String>();
			Map<String, String> subTilteMap = new HashMap<String, String>();
			Map<String, String> sketchMap = new HashMap<String, String>();
			Map<String, String> descForSolrMap = new HashMap<String, String>();
			Map<String, String> descMap = new HashMap<String, String>();
			Map<String, String> seoTitleMap = new HashMap<String, String>();
			Map<String, String> seoKeywordsMap = new HashMap<String, String>();
			Map<String, String> seoDescMap = new HashMap<String, String>();
			
			for (int i = 0; infoLangs != null && i < infoLangs.size(); i++) {
				ItemInfoLang infoLang = infoLangs.get(i);
				if(infoLang == null){
					continue;
				}
				String lang = infoLang.getLang();
				
				String title = infoLang.getTitle();
				disMap.put(SkuItemParam.dynamic_title+lang, infoLang.getTitle());
				
				String subTilte = infoLang.getSubTitle();
				subTilteMap.put(SkuItemParam.dynamic_subTitle+lang, subTilte);
				
				String seoTitle = infoLang.getSeoTitle();
				seoTitleMap.put(SkuItemParam.dynamic_seoTitle+lang, seoTitle);
				
				String seoKeywords = infoLang.getSeoKeywords();
				seoKeywordsMap.put(SkuItemParam.dynamic_seoKeywords+lang, seoKeywords);
				
				String seoDesc = infoLang.getSeoDescription();
				seoDescMap.put(SkuItemParam.dynamic_seoDescription+lang, seoDesc);
				
				String sketch = infoLang.getSketch();
				sketchMap.put(SkuItemParam.dynamic_sketch+lang, sketch);
				
				String desc = infoLang.getDescription();
				descMap.put(SkuItemParam.dynamic_description+lang, desc);
				if (null != desc) {
					desc = Jsoup.parse(desc).text();
				}
				descForSolrMap.put(SkuItemParam.dynamic_descriptionForSearch+lang, desc);
			}
			//设置国际化动态属性
			itemSolrCommand.setDynamicTitle(disMap);
			itemSolrCommand.setDynamicSubTitle(subTilteMap);
			itemSolrCommand.setDynamicSeoTitle(seoTitleMap);
			itemSolrCommand.setDynamicSeoKeywords(seoKeywordsMap);
			itemSolrCommand.setDynamicSeoDescription(seoDescMap);
			itemSolrCommand.setDynamicSketch(sketchMap);
			itemSolrCommand.setDynamicDescription(descMap);
			
			itemSolrCommand.setDynamicDescriptionForSearch(descForSolrMap);
			
			itemSolrCommand.setDescription(baseItemForSolrCommand.getDescription());
			itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
			itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
			itemSolrCommand.setIndustryId(baseItemForSolrCommand.getIndustryId());
			itemSolrCommand.setIndustryName(baseItemForSolrCommand.getIndustryName());
			itemSolrCommand.setLifecycle(baseItemForSolrCommand.getLifecycle());
			itemSolrCommand.setCreateTime(baseItemForSolrCommand.getCreateTime());
			itemSolrCommand.setList_price(baseItemForSolrCommand.getList_price());
			itemSolrCommand.setSale_price(baseItemForSolrCommand.getSale_price());
			itemSolrCommand.setModifyTime(baseItemForSolrCommand.getModifyTime());
			itemSolrCommand.setListTime(baseItemForSolrCommand.getListTime());
			itemSolrCommand.setDelistTime(baseItemForSolrCommand.getDelistTime());
			itemSolrCommand.setTemplateId(baseItemForSolrCommand.getTemplateId());
			itemSolrCommand.setIsaddcategory(baseItemForSolrCommand.getIsaddcategory());
			itemSolrCommand.setIsAddTag(baseItemForSolrCommand.getIsAddTag());
			itemSolrCommand.setShopId(baseItemForSolrCommand.getShopId());
			itemSolrCommand.setShopName(baseItemForSolrCommand.getShopName());
			Date beginTime = baseItemForSolrCommand.getActiveBeginTime();
			
			if(beginTime == null){
				beginTime = new Date();
			}
			
			itemSolrCommand.setActiveBeginTime(beginTime);
			itemSolrCommand.setActiveEndTime(baseItemForSolrCommand.getActiveEndTime());
			itemSolrCommand.setSeoDescription(baseItemForSolrCommand.getSeoDescription());
			itemSolrCommand.setSeoKeywords(baseItemForSolrCommand.getSeoKeywords());
			
//				itemSolrCommand.setSpread("0");
			itemSolrCommand.setStyle(baseItemForSolrCommand.getStyle());
			
			for(ItemRateForSolrCommand itemRateForSolrCommand : itemForSolrRateCommandList){
				if(null != itemRateForSolrCommand.getItemId() && itemRateForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId()) && itemRateForSolrCommand.getCount()>0 && null!=itemRateForSolrCommand.getCount()){
						String parten = "#.#";
						DecimalFormat decimal = new DecimalFormat(parten);
						itemSolrCommand.setRankavg(Float.parseFloat(decimal.format(itemRateForSolrCommand.getSum()/itemRateForSolrCommand.getCount())));
				}
			}
			
			if(null==itemSolrCommand.getRankavg()){
				itemSolrCommand.setRankavg(Float.parseFloat("0.0"));
			}
			
			if(Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveBeginTime()) && Validator.isNullOrEmpty(baseItemForSolrCommand.getActiveEndTime())){
				itemSolrCommand.setItemIsDisplay(true);
			}else{
				itemSolrCommand.setItemIsDisplay(false);
			}
			
			for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForCustomerList){
				if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemPropertiesForCustomerList.add(itemPropertiesCommand);
				}
			}
			
			for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandWithOutSearchList){
				if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemPropertiesWithOutSearchList.add(itemPropertiesCommand);
				}
			}
			
			for(ItemSalesCountForSolrCommand itemSalesCountForSolrCommand : itemForSolrSalesCountCommandList){
				if(null != itemSalesCountForSolrCommand.getItemId() && itemSalesCountForSolrCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemSolrCommand.setSalesCount(itemSalesCountForSolrCommand.getCount());
				}
			}
			
			for(ItemFavouriteCountForSolrCommand favCountCommand : itemFavouriteCountForSolrCommandList){
				if(null != favCountCommand.getItemId() && favCountCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemSolrCommand.setFavoredCount(favCountCommand.getCount());
				}
			}
			
			if(null==itemSolrCommand.getSalesCount()){
				itemSolrCommand.setSalesCount(0);
			}
			
			for(ItemPropertiesCommand itemPropertiesCommand : itemPropertiesCommandForSearchList){
				if(null != itemPropertiesCommand.getItemId() && itemPropertiesCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					if(itemPropertiesCommand.getIs_color_prop()){
						allItemColorList.add(itemPropertiesCommand);
					}
					itemPropertiesForSearchList.add(itemPropertiesCommand);
				}
			}
			
			for(ItemForCategoryCommand itemForCategoryCommand : itemForCategoryCommandList){
				if(null != itemForCategoryCommand.getItemId() && itemForCategoryCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemForCategoryMap.put(itemForCategoryCommand.getCategoryId(),itemForCategoryCommand);
					Long parentId = itemForCategoryCommand.getParent_id();
					while(null!=parentId){
						ItemForCategoryCommand itemForCategoryCommandParent = new ItemForCategoryCommand();
						itemForCategoryCommandParent = baseItemForSolrCategoryCommandDao.findParentCategoryById(parentId);
						if(null!=itemForCategoryCommandParent){
							List<Long> categoryIds = new ArrayList<Long>();
							categoryIds.add(parentId);
							
							List<CategoryLang> cls =  categoryDao.findCategoryLangList(categoryIds, MutlLang.i18nLangs());
							itemForCategoryCommandParent.setCategoryLangs(cls);
							parentId = itemForCategoryCommandParent.getParent_id();
							itemForCategoryCommandParent.setItemId(baseItemForSolrCommand.getId());
							itemForCategoryMap.put(itemForCategoryCommandParent.getCategoryId(),itemForCategoryCommandParent);
						}else{
							parentId = null;
						}
					}
				}
			}
			
			for(ItemImageCommand itemImageCommand : itemImageCommandList){
				if(null != itemImageCommand.getItemId() && itemImageCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemImageList.add(itemImageCommand);
				}
			}
			
			for(ItemTagCommand itemTagCommand : itemTagCommandList){
				if(null != itemTagCommand.getItemId() && itemTagCommand.getItemId().equals(baseItemForSolrCommand.getId())){
					itemTagList.add(itemTagCommand);
				}
			}
			
			itemSolrCommand.setCategoryMap(itemForCategoryMap);
			itemSolrCommand.setDynamicListForSearch(itemPropertiesForSearchList);
			itemSolrCommand.setImageList(itemImageList);
			itemSolrCommand.setTagList(itemTagList);
			itemSolrCommand.setDynamicListForCustomer(itemPropertiesForCustomerList);
			itemSolrCommand.setDefaultSort(getDefaultSortI18n(itemSolrCommand,scoreList));
			itemCommandList.add(itemSolrCommand);
		}
		
		/*
		if(Validator.isNotNullOrEmpty(allItemColorList)&&Validator.isNotNullOrEmpty(itemImageCommandList)){
			for(ItemImageCommand imgCmd : itemImageCommandList){
				for(ItemPropertiesCommand ipCmd : allItemColorList){
					if(imgCmd.getItemProId().equals(ipCmd.getItem_properties_id())){
						imgCmd.setColor(ipCmd.getItem_properties_id());
					}
				}
			}
			
		}
		*/
	
		return itemCommandList;
	}
	
	private Integer getDefaultSortI18n(final ItemSolrI18nCommand itemSolrCommand,List<ItemSortScore> scoreList){
		Integer defaultSort = 0;
		if(Validator.isNotNullOrEmpty(scoreList)){
			for(ItemSortScore itemSortScore : scoreList){
				/*
				 * 2. 获取相应排序值
				 */
				defaultSort = defaultSort + getSortScoreI18n(itemSortScore,itemSolrCommand);
			}
			/* 
			 * 3. 获取商城的值
			 */
			if(null != itemSortScoreHandler){
				try {
					defaultSort = defaultSort + itemSortScoreHandler.getSortScore(scoreList, itemSolrCommand);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return defaultSort;
	}
	
	private Map<Long, List<ItemInfoLang>> findItemCommandByItemIdDbI18n(List<Long> itemIds) {
		//获取所有商品id对应的item_info_id
		List<Long> itemInfoIds = new ArrayList<Long>();
		List<ItemInfo> itemInfos = itemInfoDao.findItemInfoListByItemIds(itemIds);
		Map<Long, Long>  itemIdToItemInfoId = new  HashMap<Long, Long>();
	    if(itemInfos != null && itemInfos.size() > 0){
			for (ItemInfo info : itemInfos) {
				Long id = info.getId();
				Long itemId = info.getItemId();
				itemInfoIds.add(id);
				itemIdToItemInfoId.put(id, itemId);
			}
	    }
		List<ItemInfoLang> infoLangs = itemInfoDao.findItemInfoLangList(itemInfoIds,MutlLang.i18nLangs());
		Map<Long, List<ItemInfoLang>> maps = new HashMap<Long, List<ItemInfoLang>>();
		for (ItemInfoLang itemInfoLang : infoLangs) {
			Long itemInfoId = itemInfoLang.getItemInfoId();
			Long itemId = itemIdToItemInfoId.get(itemInfoId);
			if(maps.containsKey(itemId)){
				maps.get(itemId).add(itemInfoLang);
			}else{
				List<ItemInfoLang> mappers = new ArrayList<ItemInfoLang>();
				mappers.add(itemInfoLang);
				maps.put(itemId, mappers);
			}
		}
		
		return maps;

	}
	
	private List<ItemForCategoryCommand> findItemCategoryByItemIdI18n(List<Long> itemIds) {
		List<ItemForCategoryCommand> itemForCategoryCommands = null;
		if(itemIds==null){
			itemForCategoryCommands = baseItemForSolrCategoryCommandDao.findItemCategory();
		}else{
			itemForCategoryCommands = baseItemForSolrCategoryCommandDao.findItemCategoryByItemId(itemIds);
		}
		if (itemForCategoryCommands == null
				|| itemForCategoryCommands.size() == 0) {
			return itemForCategoryCommands;
		}
		// 查询对应的国际化信息
		List<Long> cIds = new ArrayList<Long>();
		for (ItemForCategoryCommand categoryCommand : itemForCategoryCommands) {
			Long cId = categoryCommand.getCategoryId();
			cIds.add(cId);
		}
		List<CategoryLang> categoryLangs = categoryDao.findCategoryLangList(
				cIds, MutlLang.i18nLangs());
		for (ItemForCategoryCommand categoryCommand : itemForCategoryCommands) {
			Long cId = categoryCommand.getCategoryId();
			for (int i = 0;categoryLangs!=null && i <categoryLangs.size(); i++) {
				CategoryLang cl = categoryLangs.get(i);
				Long clId = cl.getCategoryid();
				if (cId.equals(clId)) {
					List<CategoryLang> list = categoryCommand.getCategoryLangs();
					if(list==null){
						List<CategoryLang> cls = new ArrayList<CategoryLang>();
						cls.add(cl);
						categoryCommand.setCategoryLangs(cls);
					}else{
						list.add(cl);
					}
				}
			}
		}
		return itemForCategoryCommands;

	}
	
	private List<ItemPropertiesCommand> findItemPropertiesByItemIdForSearchI18n(List<Long> itemIds) {
		List<ItemPropertiesCommand> itemPropertiesCommands = null;
		if(itemIds==null){
			itemPropertiesCommands = baseItemForSolrPorpertiesCommandDao.findItemPropertiesForSearch();
		}else{
			itemPropertiesCommands = baseItemForSolrPorpertiesCommandDao.findItemPropertiesByItemIdForSearch(itemIds);
		}
		
		if (itemPropertiesCommands == null || itemPropertiesCommands.size() == 0) {
			return itemPropertiesCommands;
		}
		
		// 查询对应的国际化信息
		List<Long> itemPropertyIds = new ArrayList<Long>(); //商品属性的ID列表
		List<Long> propertyValueIds = new ArrayList<Long>(); //商品属性可选值的ID列表
		for (ItemPropertiesCommand command : itemPropertiesCommands) {
			Long cId = command.getItem_properties_id();
			itemPropertyIds.add(cId);
			
			if(Validator.isNotNullOrEmpty(command.getPropertyValue())) {
				Long propertyValueId = Long.valueOf(command.getPropertyValue());
				if(!propertyValueIds.contains(propertyValueId)) {
					propertyValueIds.add(Long.valueOf(command.getPropertyValue()));
				}
			}
		}
		
		List<ItemPropertiesLang> itemPropertiesLangs = itemPropertiesDao.findItemPropertiesLangByIds(itemPropertyIds, MutlLang.i18nLangs());
		List<PropertyValueLang> propertyValueLangs = propertyValueDao.findPropertyValueLangByPvids(propertyValueIds, MutlLang.i18nLangs());
		
		for (ItemPropertiesCommand command : itemPropertiesCommands) {
			Long cId = command.getItem_properties_id();
			for (int i = 0; itemPropertiesLangs != null && i < itemPropertiesLangs.size(); i++) {
				ItemPropertiesLang itemPropertiesLang = itemPropertiesLangs.get(i);
				Long clId = itemPropertiesLang.getItemPropertiesId();
				if (cId.equals(clId)) {
					List<ItemPropertiesLang> list = command.getItemPropertiesLangs();
					if(list==null){
						List<ItemPropertiesLang> cls = new ArrayList<ItemPropertiesLang>();
						cls.add(itemPropertiesLang);
						command.setItemPropertiesLangs(cls);
					}else{
						list.add(itemPropertiesLang);
					}
				}
			}
			
			if(Validator.isNotNullOrEmpty(command.getPropertyValue())) {
				Long propertyValueId = Long.valueOf(command.getPropertyValue());
				for(int i = 0; propertyValueLangs != null && i < propertyValueLangs.size(); i++) {
					PropertyValueLang pvLang = propertyValueLangs.get(i);
					if(propertyValueId.equals(pvLang.getPropertyValueId())) {
						List<PropertyValueLang> pvlList = command.getPropertyValueLangs();
						if(pvlList == null) {
							pvlList = new ArrayList<PropertyValueLang>();
							pvlList.add(pvLang);
							command.setPropertyValueLangs(pvlList);
						} else {
							pvlList.add(pvLang);
						}
					}
				}
			}
		}
		return itemPropertiesCommands;

	}
	
	private List<ItemPropertiesCommand> findItemPropertiesCustomerForSearchByIdI18n(List<Long> itemIds) {
		List<ItemPropertiesCommand> itemPropertiesCommands = null;
		if(itemIds==null){
			itemPropertiesCommands = baseItemForSolrPorpertiesCommandDao.findItemPropertiesCustomerForSearch();
		}else{
			itemPropertiesCommands = baseItemForSolrPorpertiesCommandDao.findItemPropertiesCustomerForSearchById(itemIds);
		}
		if (itemPropertiesCommands == null
				|| itemPropertiesCommands.size() == 0) {
			return itemPropertiesCommands;
		}
		// 查询对应的国际化信息
		List<Long> cIds = new ArrayList<Long>();
		for (ItemPropertiesCommand command : itemPropertiesCommands) {
			Long cId = command.getItem_properties_id();
			cIds.add(cId);
		}
		List<ItemPropertiesLang> categoryLangs = itemPropertiesDao.findItemPropertiesLangByIds
				(cIds, MutlLang.i18nLangs());
		for (ItemPropertiesCommand command : itemPropertiesCommands) {
			Long cId = command.getItem_properties_id();
			for (int i = 0;categoryLangs!=null && i <categoryLangs.size(); i++) {
				ItemPropertiesLang cl = categoryLangs.get(i);
				Long clId = cl.getItemPropertiesId();
				if (cId.equals(clId)) {
					List<ItemPropertiesLang> list = command.getItemPropertiesLangs();
					if(list==null){
						List<ItemPropertiesLang> cls = new ArrayList<ItemPropertiesLang>();
						cls.add(cl);
						command.setItemPropertiesLangs(cls);
					}else{
						list.add(cl);
					}
				}
			}
		}
		return itemPropertiesCommands;

	}
	private List<ItemImageCommand> findItemImageByItemId(List<Long> itemIds){
		List<ItemImageCommand> commands = null;
		if(Validator.isNullOrEmpty(itemIds)){
			commands = baseItemForSolrImageCommandDao.findItemImage(ItemImage.IMG_TYPE_LIST);
		}else{
			commands = baseItemForSolrImageCommandDao.findItemImageByItemId(itemIds,ItemImage.IMG_TYPE_LIST);
		}
		if (commands == null || commands.size() == 0) {
			return commands;
		}
		// 查询对应的国际化信息
		List<Long> ids = new ArrayList<Long>();
		for (ItemImageCommand command : commands) {
			Long id = command.getId();
			ids.add(id);
		}
		List<ItemImageLang> itemImageLangs = itemImageDao.findItemImageLangList(ids, MutlLang.i18nLangs());
		for (ItemImageCommand command : commands) {
			Long cId = command.getId();
			for (int i = 0;itemImageLangs!=null && i <itemImageLangs.size(); i++) {
				ItemImageLang cl = itemImageLangs.get(i);
				Long clId = cl.getItemImageId();
				if (cId.equals(clId)) {
					List<ItemImageLang> list = command.getItemImageLangs();
					if(list==null){
						List<ItemImageLang> cls = new ArrayList<ItemImageLang>();
						cls.add(cl);
						command.setItemImageLangs(cls);
					}else{
						list.add(cl);
					}
				}
			}
		}
		return commands;
	}

	/* 
	 * @see com.baozun.nebula.solr.manager.ItemInfoManager#findItemExtraViewCommand(java.lang.Long)
	 */
	@Override
	public ItemSolrCommand findItemExtraViewCommand(Long itemId) {
		ItemSolrCommand itemSolrCommand = new ItemSolrCommand();
		List<Long> itemIds =new ArrayList<Long>();
		itemIds.add(itemId);
		List<ItemRateForSolrCommand> itemForSolrRateCommandList = itemForSolrRateCommandDao.findItemRateById(itemIds);
		List<ItemSalesCountForSolrCommand> itemForSolrSalesCountCommandList = itemForSolrSalesCountCommandDao.findItemCountById(itemIds);
		List<ItemFavouriteCountForSolrCommand> itemFavouriteCountForSolrCommandList = itemForSolrFavouritedCountCommandDao.findItemCountById(itemIds);
		if(Validator.isNotNullOrEmpty(itemForSolrRateCommandList)){
			ItemRateForSolrCommand itemRateForSolrCommand =itemForSolrRateCommandList.get(0);
			if(null != itemRateForSolrCommand.getItemId() && itemRateForSolrCommand.getCount()>0 && null!=itemRateForSolrCommand.getCount()){
				String parten = "#.#";
				DecimalFormat decimal = new DecimalFormat(parten);
				itemSolrCommand.setRankavg(Float.parseFloat(decimal.format(itemRateForSolrCommand.getSum()/itemRateForSolrCommand.getCount())));
			}
		}
		if(Validator.isNotNullOrEmpty(itemForSolrSalesCountCommandList)){
			ItemSalesCountForSolrCommand itemSalesCountForSolrCommand =itemForSolrSalesCountCommandList.get(0);
			if(null != itemSalesCountForSolrCommand.getItemId()){
				itemSolrCommand.setSalesCount(itemSalesCountForSolrCommand.getCount());
			}
		}
		if(Validator.isNotNullOrEmpty(itemFavouriteCountForSolrCommandList)){
			ItemFavouriteCountForSolrCommand favCountCommand =itemFavouriteCountForSolrCommandList.get(0);
			if(null != favCountCommand.getItemId()){
				itemSolrCommand.setFavoredCount(favCountCommand.getCount());
			}
		}
		return itemSolrCommand;
	}

	
}
