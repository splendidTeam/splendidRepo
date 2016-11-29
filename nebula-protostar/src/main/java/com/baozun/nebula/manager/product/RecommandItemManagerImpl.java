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
 */
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.RecommandItemCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.RecommandItemDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.RecommandItem;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * 推荐商品Manager实现
 * 
 * @author chenguang.zhou
 * @date 2014年4月10日 下午2:38:10
 */
@Service
@Transactional
public class RecommandItemManagerImpl implements RecommandItemManager {

	@Autowired
	private RecommandItemDao	recommandItemDao;

	@Autowired
	private ChooseOptionManager	chooseOptionManager;

	@Autowired
	private ItemDao				itemDao;

	@Autowired
	private CategoryDao			categoryDao;
	
	@Autowired
	private CacheManager 		cacheManager;

	/** 推荐类型 */
	private final static String	RT_GROUP_CODE	= "RECOMMAND_TYPE";

	/** 推荐参数 */
	private final static String	RP_GROUP_CODE	= "RECOMMAND_PARAM";

	@Override
	public List<RecommandItemCommand> findRecommandItemListByParaMap(Map<String, Object> paramMap) {

		List<RecommandItemCommand> recommandCommandList = recommandItemDao.findRecommandItemListByParaMap(paramMap);

		return getRecommandCommandPage(recommandCommandList);
	}
	
	@Override
	public Pagination<RecommandItemCommand> findRecommandItemListByParaMap(Page page, Sort[] sorts, Map<String, Object> paramMap) {
		Pagination<RecommandItemCommand> recommandItemPage = recommandItemDao.findRecommandItemListByParaMapWithPage(page, sorts, paramMap);
		List<RecommandItemCommand> recommandItemCommandList = getRecommandCommandPage(recommandItemPage.getItems());
		recommandItemPage.setItems(recommandItemCommandList);
		return recommandItemPage;
	}

	/**
	 * 处理查询的推荐集合
	 * 
	 * @param recommandCommandPage
	 * @return
	 */
	private List<RecommandItemCommand> getRecommandCommandPage(List<RecommandItemCommand> recommandCommandList) {
		/** 推荐类型 */
		List<ChooseOption> rtChooseOptionList = chooseOptionManager.findOptionListByGroupCode(RT_GROUP_CODE);
		Map<String, String> recTypeMap = new HashMap<String, String>();
		for (ChooseOption chooseOption : rtChooseOptionList) {
			recTypeMap.put(chooseOption.getOptionValue(), chooseOption.getOptionLabel());
		}

		/** 推荐参数 */
		List<ChooseOption> rpChooseOptionList = chooseOptionManager.findOptionListByGroupCode(RP_GROUP_CODE);
		Map<String, String> recParamMap = new HashMap<String, String>();
		for (ChooseOption chooseOption : rpChooseOptionList) {
			recParamMap.put(chooseOption.getOptionValue(), chooseOption.getOptionLabel());
		}
		List<Long> itemIdList = new ArrayList<Long>();
		List<Long> categoryIdList = new ArrayList<Long>();
		if (recommandCommandList != null && recommandCommandList.size() > 0) {
			for (RecommandItemCommand recommandItemCommand : recommandCommandList) {
				Integer type = recommandItemCommand.getType();
				Long param = recommandItemCommand.getParam();
				if (RecommandItem.TYPE_ITEM.equals(type)) {
					itemIdList.add(param);
				} else if (RecommandItem.TYPE_CATEGORY.equals(type)) {
					categoryIdList.add(param);
				}
			}
			/** itemMap: key-itemId, value: itemCommand */
			Map<Long, ItemCommand> itemMap = new HashMap<Long, ItemCommand>();
			List<ItemCommand> itemCommandList = itemDao.findItemCommandListByIds(itemIdList);
			for (ItemCommand itemCommand : itemCommandList) {
				itemMap.put(itemCommand.getId(), itemCommand);
			}
			/** categoryMap: key-categoryId, value: category */
			Map<Long, Category> categoryMap = new HashMap<Long, Category>();
			List<Category> categoryList = categoryDao.findCategoryListByIds(categoryIdList);
			for (Category category : categoryList) {
				categoryMap.put(category.getId(), category);
			}

			for (RecommandItemCommand recommandItemCommand : recommandCommandList) {
				// 推荐类型
				Integer type = recommandItemCommand.getType();
				String typeName = recTypeMap.get(String.valueOf(type));
				recommandItemCommand.setTypeName(typeName);
				// 推荐参数
				Long param = recommandItemCommand.getParam();
				String paramName = "";
				if (RecommandItem.TYPE_PUBLIC.equals(type)) {
					paramName = recParamMap.get(String.valueOf(param));
				} else if (RecommandItem.TYPE_ITEM.equals(type)) {
					ItemCommand itemCommand = itemMap.get(param);
					paramName = itemCommand.getTitle();
					recommandItemCommand.setItemCode(itemCommand.getCode());
				} else if (RecommandItem.TYPE_CATEGORY.equals(type)) {
					Category category = categoryMap.get(param);
					recommandItemCommand.setCategoryCode(category.getCode());
					paramName = category.getName();
				}
				recommandItemCommand.setParamName(paramName);
			}
		}
		return recommandCommandList;
	}

	@Override
	public void createOrUpdateRecItem(RecommandItem[] rcommandItems, Long userId, Integer type, Long param) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", type);
		paramMap.put("param", param);

		List<RecommandItemCommand> dbRecItemCommandList = recommandItemDao.findRecommandItemListByParaMap(paramMap);

		Map<Long, RecommandItem> newRecItemMap = new HashMap<Long, RecommandItem>();
		List<Long> removeRecItemIds = new ArrayList<Long>();
		// 保存推荐信息
		Integer sort = 1;
		for (RecommandItem recommandItem : rcommandItems) {
			recommandItem.setSort(sort);
			Long id = recommandItem.getId();

			if (null == id) {
				recommandItem.setLifecycle(RecommandItem.STATUS_ENABLE);
				recommandItem.setCreateTime(new Date());
				recommandItem.setOpeartorId(userId);
				recommandItemDao.save(recommandItem);
			} else {
				newRecItemMap.put(id, recommandItem);
			}
			sort++;
		} 

		/** 是要删除的推荐 还是要修改的推荐 */
		for (RecommandItemCommand recommandItemCommand : dbRecItemCommandList) {
			Long recId = recommandItemCommand.getId();
			RecommandItem recommandItem = newRecItemMap.get(recId);
			if (null == recommandItem) {
				removeRecItemIds.add(recId);
			} else {
				if(checkRecommandItemInfo(recommandItemCommand, recommandItem)){
					//System.out.println("-->>> ");
					recommandItemDao.updateRecommandItemById(recId, recommandItem.getItemId(), param, type, recommandItem.getSort(), userId);
				}
			}
		}
		/** 删除推荐 */
		if (null != removeRecItemIds && removeRecItemIds.size() > 0) {
			recommandItemDao.removeRecommandItemByIds(removeRecItemIds);
		}
		/** 清除推荐缓存 **/
		removeRecommandItemCache(type, param);
	}

	/**
	 * 判断推荐商品中的信息是否被修改过,是就update,
	 * @param dbRecItemCommand
	 * @param newRecItemCommand
	 * @return
	 */
	private boolean checkRecommandItemInfo(RecommandItemCommand dbRecItemCommand, RecommandItem newRecItem){
		Long newItemId = newRecItem.getItemId();
		Long oldItemId = dbRecItemCommand.getItemId();
		if(!(null != newItemId && newItemId.equals(oldItemId))){
			return true;
		}
		
		Long newParam = newRecItem.getParam();
		Long oldParam = dbRecItemCommand.getParam();
		if(!(null != newParam && newParam.equals(oldParam))){
			return true;
		}
		
		Integer newSort = newRecItem.getSort();
		Integer oldSort = dbRecItemCommand.getSort();
		if(!(null != newSort && newSort.equals(oldSort))){
			return true;
		}
		return false;
	}
	
	@Override
	public void removeRecommandItemById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		recommandItemDao.removeRecommandItemByIds(ids);
		
	}

	@Override
	public void removeRecommandItemByIds(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		recommandItemDao.removeRecommandItemByIds(idList);
	}

	@Override
	public RecommandItemCommand findRecommandItemById(Long id) {
		RecommandItemCommand recommandItemCommand = recommandItemDao.findRecommandItemById(id);
		Integer type = recommandItemCommand.getType();
		Long param = recommandItemCommand.getParam();
		if (RecommandItem.TYPE_CATEGORY.equals(type)) {
			Category category = categoryDao.findCategoryById(param);
			recommandItemCommand.setCategoryName(category.getName());
		} else if (RecommandItem.TYPE_ITEM.equals(type)) {
			Item item = itemDao.findItemById(param);
			recommandItemCommand.setItemCode(item.getCode());
		}
		return recommandItemCommand;
	}

	@Override
	public void enabledOrDisenabledRecItem(Long id, Integer status) {
		if (status == 0) {
			recommandItemDao.disenabledReceommandItemById(id);
		} else {
			recommandItemDao.enabledReceommandItemById(id);
		}
	}

	@Override
	public List<RecommandItemCommand> findRecommandItemListByCodeList(List<String> codeList) {
		List<RecommandItemCommand> list = recommandItemDao.findRecommandItemListByCodeList(codeList);
		for (RecommandItemCommand cmd : list) {
			Integer type = cmd.getType();
			Long param = cmd.getParam();
			if (RecommandItem.TYPE_CATEGORY.equals(type)) {
				Category category = categoryDao.findCategoryById(param);
				cmd.setCategoryName(category.getName());
			} else if (RecommandItem.TYPE_ITEM.equals(type)) {
				Item item = itemDao.findItemById(param);
				cmd.setItemCode(item.getCode());
			}
		}
		return list;
	}
	
	public void removeRecommandItemCache(Integer type, Long param){
		String key = CacheKeyConstant.CACHE_KEY_SHOP_RECOMMAND_ITEM_LIST + "_" + type + "_" + param;
		cacheManager.removeMapValue(key, CacheKeyConstant.CACHE_KEY_RECOMMANDITEM);
	}
}
