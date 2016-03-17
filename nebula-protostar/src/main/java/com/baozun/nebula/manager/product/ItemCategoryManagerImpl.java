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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;

/**
 * @author yi.huang
 * @date 2013-6-28 下午04:15:26
 */
@Transactional
@Service("itemCategoryManager")
public class ItemCategoryManagerImpl implements ItemCategoryManager {

	private static final Logger	log	= LoggerFactory.getLogger(ItemCategoryManagerImpl.class);

	@Autowired
	private ItemCategoryDao		itemCategoryDao;
	@Autowired
	private CategoryDao			categoryDao;

	@Autowired
	private CategoryManager		categoryManager;

	@Autowired
	private ItemManager			itemManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager#addItemCategorys (java.lang.Long[], java.lang.Long[])
	 */
	@Override
	public ItemCategoryResultCommand bindItemCategory(Long[] itemIds, Long[] categoryIds) {

		// 无须重复关联 商品
		Map<String, List<Item>> repeatMap = new HashMap<String, List<Item>>();
		// 关联成功 商品
		Map<String, List<Item>> successMap = new HashMap<String, List<Item>>();

		// 关联 分类 失败的商品
		Map<String, List<Item>> failMap = new HashMap<String, List<Item>>();

		// 查询出跟此次要关联的数据存在重复的数据
		List<ItemCategory> compareItemCategoryList = itemCategoryDao.findItemCategoryByItemIdAndCategoryId(itemIds,
				categoryIds);

		// 根据分类Id数组查询商品分类
		List<Category> categoryList = categoryManager.findCategoryListByCategoryIds(categoryIds);
		// 根据商品id数组查询商品
		List<Item> itemList = itemManager.findItemListByItemIds(itemIds);

		for (Long categoryId : categoryIds) {
			Category category = null;
			for (Category category1 : categoryList) {
				if (category1.getId().equals(categoryId)) {
					category = category1;
				}
			}
			String categoryName = category.getName();

			for (Long itemId : itemIds) {

				Item item = null;
				for (Item item1 : itemList) {
					if (item1.getId().equals(itemId)) {
						item = item1;
					}
				}
				// 判断 要插入的 商品+分类 是否 在关系表中已经存在 默认不存在
				boolean isRepeat = false;
				// 首先跟list比较看是否已经存在要添加关联的数据
				if (Validator.isNotNullOrEmpty(compareItemCategoryList)) {

					for (ItemCategory itemCategory : compareItemCategoryList) {
						isRepeat = itemCategory.getCategoryId().equals(categoryId)
								&& itemCategory.getItemId().equals(itemId);
						if (isRepeat) {
							break;
						}
					}

					if (isRepeat) {
						putItemToMap(repeatMap, categoryName, item);
						continue;
					}
				}

				// 如果 没有重复的 我们就插入 , 此处加入判断 便于我们代码阅读
				if (!isRepeat) {
					try {
						// 执行关联操作
						Integer result = itemCategoryDao.bindItemCategory(itemId, categoryId, false);
						if (Integer.valueOf(1).equals(result)) {
							putItemToMap(successMap, categoryName, item);
						} else {
							putItemToMap(failMap, categoryName, item);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.info("addItemCategory error message: categoryName:{},itemId:{}", categoryName, itemId);
						putItemToMap(failMap, categoryName, item);
					}
				}
				//绑定分类，不再设置默认分类,pts额外提供设置方法

				// 拿到最新的 分类 进行判断 1.从未绑定过 2.原来有绑定过 这两种情况都已经覆盖到了
				/*Long curItemCategoryIdArray[] = null;

				List<ItemCategory> curItemCategorys = itemCategoryDao.findItemCategoryListByItemId(itemId);
				if (curItemCategorys != null && curItemCategorys.size() > 0) {
					curItemCategoryIdArray = new Long[curItemCategorys.size()];

					for (int i = 0; i < curItemCategorys.size(); i++) {
						curItemCategoryIdArray[i] = curItemCategorys.get(i).getCategoryId();
					}
				}

				// 修改默认分类和附加分类
				if (curItemCategoryIdArray != null && curItemCategoryIdArray.length > 0) {
					Long defaultId = getDefaultItemCategoryId(curItemCategoryIdArray);

					Long[] categoryIdArray = new Long[curItemCategoryIdArray.length - 1];
					int index = 0;
					for (Long id : curItemCategoryIdArray) {
						if (!defaultId.equals(id)) {
							categoryIdArray[index] = id;
							index++;
						}
					}

					// 绑定附加分类

					ItemCommand itemCommand = new ItemCommand();// 此处 直接new 了一个command 是因为 createOrUpdateItemCategory
																// 中传入itemCommand 只是为了拿到ItemId
					itemCommand.setId(item.getId());

					this.createOrUpdateItemCategory(itemCommand, item.getId(), categoryIdArray);

					// 绑定默认分类
					this.createOrUpdateItemDefaultCategory(itemCommand, item.getId(), defaultId);

				}*/
			}
		}

		ItemCategoryResultCommand itemCategoryResultCommand = new ItemCategoryResultCommand();
		itemCategoryResultCommand.setFailMap(failMap);
		itemCategoryResultCommand.setRepeatMap(repeatMap);
		itemCategoryResultCommand.setSuccessMap(successMap);

		// 去重itemid,更新t_pd_item:isaddcategory

		List<Long> ids = new ArrayList<Long>();
		Set<Long> set = new HashSet<Long>();
		for (Map.Entry<String, List<Item>> entry : successMap.entrySet()) {
			List<Item> successItemList = (List<Item>) entry.getValue();

			for (Item item : successItemList) {
				set.add(item.getId());
			}

		}
		Iterator<Long> it = set.iterator();
		while (it.hasNext()) {
			ids.add(it.next());
		}
		itemManager.updateItemIsAddCategory(ids, 1);

		return itemCategoryResultCommand;
	}

	/**
	 * 设置 map 信息
	 * 
	 * @param map
	 *            map repeatMap,successMap,failMap
	 * @param categoryName
	 *            分类名称
	 * @param item
	 *            item
	 */
	private void putItemToMap(Map<String, List<Item>> map, String categoryName, Item item) {
		List<Item> itemList = map.get(categoryName);

		if (Validator.isNullOrEmpty(itemList)) {
			itemList = new ArrayList<Item>();
		}
		itemList.add(item);
		map.put(categoryName, itemList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager#deleteItemCategory (java.lang.Long[], java.lang.Long)
	 */
	@Override
	public boolean unBindItemCategory(Long[] itemIds, Long categoryId) {
		/*
		 * int expected = itemIds.length; int actual =
		 */
		itemCategoryDao.unBindItemCategory(itemIds, categoryId);
		// if (expected == actual){
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < itemIds.length; i++) {
			ids.add(itemIds[i]);
		}
		// 筛选出剔除掉该分类后还属于其他分类的itemId,然后remove

		List<ItemCategory> icList = null;

		/*for (int i = ids.size() - 1; i >= 0; i--) {
			icList = new ArrayList<ItemCategory>();
			Long itemId = ids.get(i);
			icList = itemCategoryDao.findItemCategoryListByItemId(itemId);// 根据itemId 查询Item 绑定的 categoryList
			if (icList != null && icList.size() > 0) {// 如果还绑定有其他的 cId， 不修改 item是否绑定分类的状态。 同时 修改绑定默认分类的逻辑

				Long curItemCategoryIdArray[] = null;

				List<ItemCategory> curItemCategorys = itemCategoryDao.findItemCategoryListByItemId(itemId);
				if (curItemCategorys != null && curItemCategorys.size() > 0) {
					curItemCategoryIdArray = new Long[curItemCategorys.size()];

					for (int k = 0; k < curItemCategorys.size(); k++) {
						curItemCategoryIdArray[k] = curItemCategorys.get(k).getCategoryId();
					}
				}

				// 修改默认分类和附加分类
				if (curItemCategoryIdArray != null && curItemCategoryIdArray.length > 0) {
					Long defaultId = getDefaultItemCategoryId(curItemCategoryIdArray);

					Long[] categoryIdArray = new Long[curItemCategoryIdArray.length - 1];
					int index = 0;
					for (Long id : curItemCategoryIdArray) {
						if (!defaultId.equals(id)) {
							categoryIdArray[index] = id;
							index++;
						}
					}

					// 绑定附加分类

					ItemCommand itemCommand = new ItemCommand();// 此处 直接new 了一个command 是因为 createOrUpdateItemCategory
																// 中传入itemCommand 只是为了拿到ItemId
					itemCommand.setId(itemId);

					this.createOrUpdateItemCategory(itemCommand, itemId, categoryIdArray);

					// 绑定默认分类
					this.createOrUpdateItemDefaultCategory(itemCommand, itemId, defaultId);

					ids.remove(i);
				}
			}
		}*/
		
		for(int i = ids.size()-1;i >= 0;i--){
			icList=new ArrayList<ItemCategory>();
			icList=itemCategoryDao.findItemCategoryListByItemId(ids.get(i));
			if(icList!=null&&icList.size()>0){
				ids.remove(i);
			}
		}
		
		itemManager.updateItemIsAddCategory(ids, 0);

		return true;
		/*
		 * }else{ throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected,
		 * actual }); // throw new EffectRangeUnexpectedException(expected, actual); }
		 */
	}
	
	@Override
	public ItemCategoryResultCommand setDefaultCategory(Long[] itemIds, Long categoryId) {
		
		Long[] categoryIds =new Long[]{categoryId};
		
		// 无须重复关联 商品
		Map<String, List<Item>> repeatMap = new HashMap<String, List<Item>>();
		// 关联成功 商品
		Map<String, List<Item>> successMap = new HashMap<String, List<Item>>();

		// 关联 分类 失败的商品
		Map<String, List<Item>> failMap = new HashMap<String, List<Item>>();

		// 查询出跟此次要关联的数据存在重复的数据
		List<ItemCategory> compareItemCategoryList = itemCategoryDao.findItemCategoryByItemIdAndCategoryId(itemIds,
				categoryIds);

		// 根据分类Id数组查询商品分类
		List<Category> categoryList = categoryManager.findCategoryListByCategoryIds(categoryIds);
		// 根据商品id数组查询商品
		List<Item> itemList = itemManager.findItemListByItemIds(itemIds);

		
		Category category = null;
		for (Category category1 : categoryList) {
			if (category1.getId().equals(categoryId)) {
				category = category1;
			}
		}
		String categoryName = category.getName();

		for (Long itemId : itemIds) {

			Item item = null;
			for (Item item1 : itemList) {
				if (item1.getId().equals(itemId)) {
					item = item1;
				}
			}
			// 判断 要插入的 商品+分类 是否 在关系表中已经存在 默认不存在
			boolean isRepeat = false;
			// 首先跟list比较看是否已经存在要添加关联的数据
			if (Validator.isNotNullOrEmpty(compareItemCategoryList)) {

				for (ItemCategory itemCategory : compareItemCategoryList) {
					isRepeat = itemCategory.getCategoryId().equals(categoryId)
							&& itemCategory.getItemId().equals(itemId);
					if (isRepeat) {
						break;
					}
				}

				if (isRepeat) {
					putItemToMap(repeatMap, categoryName, item);
				}
			}

			// 如果 没有重复的 我们就插入 , 此处加入判断 便于我们代码阅读
			if (!isRepeat) {
				try {
					// 执行关联操作
					Integer result = itemCategoryDao.bindItemCategory(itemId, categoryId, false);
					if (Integer.valueOf(1).equals(result)) {
						putItemToMap(successMap, categoryName, item);
					} else {
						putItemToMap(failMap, categoryName, item);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("addItemCategory error message: categoryName:{},itemId:{}", categoryName, itemId);
					putItemToMap(failMap, categoryName, item);
				}
			}
			// 拿到最新的 分类 进行判断 1.从未绑定过 2.原来有绑定过 这两种情况都已经覆盖到了
			Long curItemCategoryIdArray[] = null;

			List<ItemCategory> curItemCategorys = itemCategoryDao.findItemCategoryListByItemId(itemId);
			if (curItemCategorys != null && curItemCategorys.size() > 0) {
				curItemCategoryIdArray = new Long[curItemCategorys.size()];

				for (int i = 0; i < curItemCategorys.size(); i++) {
					curItemCategoryIdArray[i] = curItemCategorys.get(i).getCategoryId();
				}
			}

			// 修改默认分类和附加分类
			if (curItemCategoryIdArray != null && curItemCategoryIdArray.length > 0) {
				
				Long[] categoryIdArray = new Long[curItemCategoryIdArray.length - 1];
				int index = 0;
				for (Long id : curItemCategoryIdArray) {
					if (!categoryId.equals(id)) {
						categoryIdArray[index] = id;
						index++;
					}
				}

				// 绑定附加分类

				ItemCommand itemCommand = new ItemCommand();// 此处 直接new 了一个command 是因为 createOrUpdateItemCategory
															// 中传入itemCommand 只是为了拿到ItemId
				itemCommand.setId(item.getId());

				this.createOrUpdateItemCategory(itemCommand, item.getId(), categoryIdArray);

				// 绑定默认分类
				this.createOrUpdateItemDefaultCategory(itemCommand, item.getId(), categoryId);

			}
		}
		

		ItemCategoryResultCommand itemCategoryResultCommand = new ItemCategoryResultCommand();
		itemCategoryResultCommand.setFailMap(failMap);
		itemCategoryResultCommand.setRepeatMap(repeatMap);
		itemCategoryResultCommand.setSuccessMap(successMap);

		// 去重itemid,更新t_pd_item:isaddcategory

		List<Long> ids = new ArrayList<Long>();
		Set<Long> set = new HashSet<Long>();
		for (Map.Entry<String, List<Item>> entry : successMap.entrySet()) {
			List<Item> successItemList = (List<Item>) entry.getValue();

			for (Item item : successItemList) {
				set.add(item.getId());
			}

		}
		Iterator<Long> it = set.iterator();
		while (it.hasNext()) {
			ids.add(it.next());
		}
		itemManager.updateItemIsAddCategory(ids, 1);

		return itemCategoryResultCommand;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager# findItemListByCategoryId(java.lang.Long,
	 * loxia.dao.Page, loxia.dao.Sort[])
	 */
	@Transactional(readOnly = true)
	@Override
	public Pagination<Item> findItemListByCategoryId(Long categoryId, Page page, Sort[] sorts) {

		return itemCategoryDao.findItemListByCategoryId(categoryId, page, sorts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager# deleteItemCategoryByItemId(Long itemId)
	 */
	@Override
	public int deleteItemCategoryByItemId(Long itemId) {
		int result = itemCategoryDao.deleteItemCategoryByItemId(itemId);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager# findItemCategoryListByItemId(@QueryParam("itemId")
	 * Long itemId)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ItemCategory> findItemCategoryListByItemId(Long itemId) {
		List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByItemId(itemId);
		return itemCategoryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager# createOrUpdateItemCategory(ItemCommand
	 * itemCommand,Long itemId,Long[] categoryIds)
	 */
	@Override
	public int createOrUpdateItemCategory(ItemCommand itemCommand, Long itemId, Long[] categoryIds) {
		int counts = 0;
		// 删除绑定分类
		if (itemCommand.getId() != null) {
			int del = this.deleteItemCategoryByItemId(itemCommand.getId());
		}
		if (categoryIds != null && categoryIds.length > 0) {
			for (Long categoryId : categoryIds) {
				int count = itemCategoryDao.bindItemCategory(itemId, categoryId, false);
				counts += count;
			}
		}

		return counts;

	}

	@Override
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemCtListByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap, Long shopId) {
		// TODO Auto-generated method stub

		Pagination<ItemCommand> itemList = itemCategoryDao.findItemListEmptyCategoryByQueryMapWithPage(page,
				sorts, paraMap, shopId);

		List<ItemCategoryCommand> itemCategory = itemCategoryDao.findAllItemCategoryList();
		
		
		

		List<ItemCommand> items = itemList.getItems();

		List<String> categoryNameList = null;
		
		List<Long> itemIds =new ArrayList<Long>();

		for (int i = 0; i < items.size(); i++) {
			categoryNameList = new ArrayList<String>();
			for (int j = 0; j < itemCategory.size(); j++) {
				if (items.get(i).getId().equals(itemCategory.get(j).getItemId())) {
					categoryNameList.add(itemCategory.get(j).getCategoryName());
				}
			}
			items.get(i).setCategoryNames(categoryNameList);
			itemIds.add(items.get(i).getId());
		}
		
		//默认分类设置
		
		List<ItemCategoryCommand> itemCategoryList =itemCategoryDao.findDefaultCategoryByItemIds(itemIds);
		
		Map<Long,String> itemCategoryMap =new HashMap<Long, String>();
		for (ItemCategoryCommand itemCategoryCommand : itemCategoryList) {
			itemCategoryMap.put(itemCategoryCommand.getItemId(), itemCategoryCommand.getCategoryName());
		}
		for (int i = 0; i < items.size(); i++) {
			
			items.get(i).setDefCategory(itemCategoryMap.get(items.get(i).getId()));
		}
		
		itemList.setItems(items);

		return itemList;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemCtListByQueryMapWithPageByCIDS(Page page, Sort[] sorts,
			Map<String, Object> paraMap, Long shopId) {
		Pagination<ItemCommand> emptyCtyItemList = itemCategoryDao.findItemListEmptyCategoryByQueryMapWithPageByCIDS(
				page, sorts, paraMap, shopId);

		List<ItemCategoryCommand> itemCategory = itemCategoryDao.findAllItemCategoryList();

		List<ItemCommand> items = emptyCtyItemList.getItems();

		List<String> categoryNameList = null;

		for (int i = 0; i < items.size(); i++) {
			categoryNameList = new ArrayList<String>();
			for (int j = 0; j < itemCategory.size(); j++) {
				if (items.get(i).getId().equals(itemCategory.get(j).getItemId())) {
					categoryNameList.add(itemCategory.get(j).getCategoryName());
				}
			}
			items.get(i).setCategoryNames(categoryNameList);
		}

		emptyCtyItemList.setItems(items);

		return emptyCtyItemList;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemNoctListByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap, Long shopId) {
		// TODO Auto-generated method stub

		/*
		 * //已分类商品
		 * 
		 * List<Long> ids=new ArrayList<Long>();
		 * 
		 * List<ItemCategoryCommand> itemCategory=itemDao.findAllItemCategoryList();
		 * 
		 * for (int i = 0; i < itemCategory.size(); i++){ ids.add(itemCategory.get(i).getItemId()); }
		 */
		Pagination<ItemCommand> noctyItemList = itemCategoryDao.findItemNoctListByQueryMapWithPage(page, sorts,
				paraMap, shopId);

		return noctyItemList;
	}

	/**
	 * 如果所选商品中没有一个是属于该分类的，返回false
	 */
	public Boolean validateUnBindByItemIdsAndCategoryId(Long[] itemIds, Long categoryId) {
		// TODO Auto-generated method stub

		Long[] categoryIds = new Long[] { categoryId };

		// 查询出跟此次要关联的数据存在重复的数据
		List<ItemCategory> compareItemCategoryList = itemCategoryDao.findItemCategoryByItemIdAndCategoryId(itemIds,
				categoryIds);

		Boolean flag = compareItemCategoryList != null && compareItemCategoryList.size() > 0 ? true : false;

		return flag;
	}

	@Override
	public ItemCategoryCommand findDefaultCategoryByItemId(Long itemId) {
		// TODO Auto-generated method stub
		return itemCategoryDao.findDefaultCategoryByItemId(itemId);
	}

	@Override
	public Integer createOrUpdateItemDefaultCategory(ItemCommand itemCommand, Long itemId, Long defaultCategoryId) {
		// TODO Auto-generated method stub
		return itemCategoryDao.bindItemCategory(itemId, defaultCategoryId, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager# findItemByDefaultCategoryId(java.lang.Long)
	 */
	@Override
	public List<ItemCategory> findItemByDefaultCategoryId(Long categoryId) {
		// TODO Auto-generated method stub
		return itemCategoryDao.findItemByDefaultCategoryId(categoryId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemCategoryManager#getDefaultItemCategoryId(java.lang.Long[])
	 */
	@Override
	public Long getDefaultItemCategoryId(Long[] categoryIds) {
		if (categoryIds != null && categoryIds.length > 0) {
			Arrays.sort(categoryIds);

			return categoryIds[0];
		}
		return null;
	}

	@Override
	public List<ItemCategory> findItemCategoryListByCategoryId(Long categoryId) {
		List<Long> categoryIds = new ArrayList<Long>();
		categoryIds.add(categoryId);
		categoryIds = getChildrenCategoryIdListRecursion(categoryId, categoryIds);
		List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByCategoryIds(categoryIds);
		return itemCategoryList;
	}

	/**
	 * 根据 categoryId 递归查找 子节点(不包括自己)
	 * 
	 * @param parentId
	 *            父id
	 * @param categories
	 * @return
	 */
	private List<Long> getChildrenCategoryIdListRecursion(Long categoryId, List<Long> idList) {
		if (Validator.isNullOrEmpty(idList)) {
			idList = new ArrayList<Long>();
		}
		List<Category> childrenCategoryList = categoryDao.findCategoryListByParentId(categoryId);

		if (Validator.isNotNullOrEmpty(childrenCategoryList)) {

			for (Category category : childrenCategoryList) {
				Long id = category.getId();
				idList.add(id);

				idList = getChildrenCategoryIdListRecursion(id, idList);

			}
		}

		return idList;
	}
	
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemListEmptyCategoryByQueryMapWithPageNoShopid(Page page, Sort[] sorts,
			Map<String, Object> paraMap) {

		Pagination<ItemCommand> emptyCtyItemList = itemCategoryDao.findItemListEmptyCategoryByQueryMapWithPageNoShopid(page,
				sorts, paraMap);

		List<ItemCategoryCommand> itemCategory = itemCategoryDao.findAllItemCategoryList();

		List<ItemCommand> items = emptyCtyItemList.getItems();

		List<String> categoryNameList = null;

		for (int i = 0; i < items.size(); i++) {
			categoryNameList = new ArrayList<String>();
			for (int j = 0; j < itemCategory.size(); j++) {
				if (items.get(i).getId().equals(itemCategory.get(j).getItemId())) {
					categoryNameList.add(itemCategory.get(j).getCategoryName());
				}
			}
			items.get(i).setCategoryNames(categoryNameList);
		}

		emptyCtyItemList.setItems(items);

		return emptyCtyItemList;
	}

	@Override
	public int createOrUpdateItemCategory(ItemInfoCommand itemCommand,
			Long itemId, Long[] categoryIds) {
		int counts = 0;
		// 删除绑定分类
		if (itemCommand.getId() != null) {
			int del = this.deleteItemCategoryByItemId(itemCommand.getId());
		}
		if (categoryIds != null && categoryIds.length > 0) {
			for (Long categoryId : categoryIds) {
				int count = itemCategoryDao.bindItemCategory(itemId, categoryId, false);
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Integer createOrUpdateItemDefaultCategory(
			ItemInfoCommand itemCommand, Long itemId, Long defaultCategoryId) {
		return itemCategoryDao.bindItemCategory(itemId, defaultCategoryId, true);
	}

}
