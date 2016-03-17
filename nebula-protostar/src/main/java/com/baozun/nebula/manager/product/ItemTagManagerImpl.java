package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemTagRelationCommand;
import com.baozun.nebula.command.TagCommand;
import com.baozun.nebula.dao.product.ItemTagDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemTag;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;


@Transactional
@Service("ItemTagManager")
public class ItemTagManagerImpl implements ItemTagManager{
	
	private static final Logger	log	= LoggerFactory.getLogger(ItemTagManagerImpl.class);
	
	@Autowired
	private ItemTagDao itemTagDao;
	
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	
	@Autowired
	private ItemTagRelationDao itemTagRelationDao;
	
	@Autowired
	private ItemManager itemManager;
	
	
	
	/**
	 * ItemCategoryResultCommand这个类可以用来存放标签关联的结果
	 * 
	 */
	
	@Override
	public ItemCategoryResultCommand bindItemTag(Long[] itemIds,Long[] tagIds){

		// 无须重复关联 商品
		Map<String, List<Item>> repeatMap = new HashMap<String, List<Item>>();
		// 关联成功 商品
		Map<String, List<Item>> successMap = new HashMap<String, List<Item>>();

		// 关联 分类 失败的商品
		Map<String, List<Item>> failMap = new HashMap<String, List<Item>>();

		// 查询出跟此次要关联的数据存在重复的数据
		List<ItemTagRelation> compareItemTagList = itemTagRelationDao.findItemTagRelationByItemIdAndCategoryId(itemIds, tagIds);

		// 根据分类Id数组查询商品分类
		List<ItemTag> itemTagList = this.findTagListByTagIds(tagIds);
		// 根据商品id数组查询商品
		List<Item> itemList = itemManager.findItemListByItemIds(itemIds);

		for (Long tagId : tagIds){
			ItemTag itemTag = null;
			for (ItemTag itemTag1 : itemTagList){
				if (itemTag1.getId().equals(tagId)){
					itemTag = itemTag1;
				}
			}
			String tagName = itemTag.getName();

			for (Long itemId : itemIds){

				Item item = null;
				for (Item item1 : itemList){
					if (item1.getId().equals(itemId)){
						item = item1;
					}
				}
				// 判断 要插入的 商品+标签 是否 在关系表中已经存在 默认不存在
				boolean isRepeat = false;
				// 首先跟list比较看是否已经存在要添加关联的数据
				if (Validator.isNotNullOrEmpty(compareItemTagList)){

					for (ItemTagRelation itemTagRelation : compareItemTagList){
						isRepeat = itemTagRelation.getTagId().equals(tagId) && itemTagRelation.getItemId().equals(itemId);
						if (isRepeat){
							break;
						}
					}

					if (isRepeat){
						putItemToMap(repeatMap, tagName, item);
						continue;
					}
				}

				// 如果 没有重复的 我们就插入 , 此处加入判断 便于我们代码阅读
				if (!isRepeat){
					try{
						// 执行关联操作
						Integer result = itemTagRelationDao.bindItemTag(itemId, tagId);
						if (Integer.valueOf(1).equals(result)){
							putItemToMap(successMap, tagName, item);
						}else{
							putItemToMap(failMap, tagName, item);
						}
					}catch (Exception e){
						e.printStackTrace();
						log.info("addItemCategory error message: categoryName:{},itemId:{}", tagName, itemId);
						putItemToMap(failMap, tagName, item);
					}
				}
			}
		}

		ItemCategoryResultCommand itemCategoryResultCommand = new ItemCategoryResultCommand();
		itemCategoryResultCommand.setFailMap(failMap);
		itemCategoryResultCommand.setRepeatMap(repeatMap);
		itemCategoryResultCommand.setSuccessMap(successMap);
		
		//去重itemid,更新t_pd_item:isaddtag
		
		List<Long> ids=new ArrayList<Long>();
		Set<Long> set=new HashSet<Long>();
		for (Map.Entry<String, List<Item>> entry : successMap.entrySet()) {
			List<Item> successItemList	= (List<Item>)entry.getValue();
			
			
			for(Item item:successItemList){
				set.add(item.getId());
			}
			
		}
		Iterator<Long> it=set.iterator();
		while(it.hasNext()){
			ids.add(it.next());
		}
		itemManager.updateItemIsAddTag(ids, 1);
		
		
		return itemCategoryResultCommand;
	}
	
	@Override
	public boolean unBindItemTag(Long[] itemIds,Long tagId){
		/*int expected = itemIds.length;
		int actual = */
		itemTagRelationDao.unBindItemTag(itemIds, tagId);
		//if (expected == actual){
			List<Long> ids=new ArrayList<Long>();
			for(int i = 0;i <itemIds.length;i++){
				ids.add(itemIds[i]);
			}
			//筛选出剔除掉该分类后还属于其他分类的itemId,然后remove
			
			List<ItemTagRelation> icList=null;
			
			for(int i = ids.size()-1;i >= 0;i--){
				icList=new ArrayList<ItemTagRelation>();
				icList=this.findItemTagRelationListByItemId(ids.get(i));
				if(icList!=null&&icList.size()>0){
					ids.remove(i);
				}
			}
			itemManager.updateItemIsAddTag(ids, 0);
			
			return true;
		/*}else{
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected, actual });
			// throw new EffectRangeUnexpectedException(expected, actual);
		}*/
	}
	
	public List<ItemTagRelation> findItemTagRelationListByItemId(Long itemId){
		return itemTagRelationDao.findItemTagRelationListByItemId(itemId);
	}

	@Transactional(readOnly = true)
	public List<TagCommand> findEffectItemTagListByQueryMap(Map<String, Object> paraMap,Sort[] sorts){
		// TODO Auto-generated method stub
		List<TagCommand> tagCommandList = itemTagDao.findEffectItemTagListByQueryMap(paraMap, sorts);
		
		
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add("TAG_TYPE");
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}
		for (TagCommand tagCommand : tagCommandList){
			
			tagCommand.setTypeName(optionMap.get("TAG_TYPE-" + tagCommand.getType()));
		}
		
		
		return tagCommandList;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemTagListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){
		// TODO Auto-generated method stub
		
		Pagination<ItemCommand> ItemList = itemTagRelationDao.
				findItemListEmptyTagByQueryMapWithPage(page, sorts, paraMap,shopId);

		
		
		//标签
		List<ItemTagRelationCommand> itemTagList = itemTagRelationDao.findAllItemTagRelationList();
		
		List<ItemCommand> items = ItemList.getItems();
		
		List<String> tagNameList=null;
		for (int i = 0; i < items.size(); i++){
			tagNameList = new ArrayList<String>();
			
			for(int j = 0; j < itemTagList.size(); j++){
				if(items.get(i).getId().equals(itemTagList.get(j).getItemId())){
					tagNameList.add(itemTagList.get(j).getTagName());
				}
			}
			items.get(i).setTagNames(tagNameList);
			
		}
		ItemList.setItems(items);
		
		return ItemList;
	}
	@Override
	@Transactional(readOnly = true)
	public Pagination<ItemCommand> findItemNoTagListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){
		// TODO Auto-generated method stub
		
		Pagination<ItemCommand> noctyItemList = itemTagRelationDao.findItemListNoTagByQueryMapWithPage(page, sorts, paraMap, shopId);

		return noctyItemList;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<ItemTag> findTagListByTagIds(Long[] tagIds){
		return itemTagDao.findItemTagListByIds(tagIds);
	}
	
	/**
	 * 设置 map 信息
	 * 
	 * @param map
	 *            map repeatMap,successMap,failMap
	 * @param tagName
	 *            标签名称
	 * @param item
	 *            item
	 */
	private void putItemToMap(Map<String, List<Item>> map,String tagName,Item item){
		List<Item> itemList = map.get(tagName);

		if (Validator.isNullOrEmpty(itemList)){
			itemList = new ArrayList<Item>();
		}
		itemList.add(item);
		map.put(tagName, itemList);
	}

	public Boolean validateUnBindByItemIdsAndTagId(Long[] itemIds,Long tagId){
		// TODO Auto-generated method stub
		Long[] tagIds =new Long[]{tagId};
		List<ItemTagRelation> compareItemTagList = itemTagRelationDao.findItemTagRelationByItemIdAndCategoryId(itemIds, tagIds);
		
		Boolean flag =compareItemTagList!=null&&compareItemTagList.size()>0?true:false;
		return flag;
	}

	/**
	 * 增加商品标签
	 * @param tagName
	 */
	
	public void createOrUpdateItemTag(ItemTag itemTag){
		// TODO Auto-generated method stub
		itemTagDao.save(itemTag);
	}

	/**
	 * 判断商品标签名是否重复
	 * @param tagName
	 * @return
	 */
	public boolean validateTagName(String tagName){
		// TODO Auto-generated method stub
		ItemTag itemTag = itemTagDao.validateTagName(tagName);
		
		if(itemTag!=null){
			return true;
		}
		return false;
	}

	public boolean removeTagByIds(List<Long> ids){
		// TODO Auto-generated method stub
		int expected = ids.size();
		int actual = itemTagDao.removeTagByIds(ids);
		
		if(expected==actual){
			return true;
		}else{
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected, actual });
		}
	}
}
