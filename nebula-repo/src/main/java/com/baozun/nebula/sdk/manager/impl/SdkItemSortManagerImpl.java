package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategorySortDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.sdk.command.ItemSortCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkCategoryManager;
import com.baozun.nebula.sdk.manager.SdkItemSortManager;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午01:06:15
 */
@Transactional
@Service("sdkItemSortManager")
public class SdkItemSortManagerImpl implements SdkItemSortManager {
	@Autowired
	private ItemCategorySortDao itemCategorySortDao;

	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemSortCommand> findItemSortByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return itemCategorySortDao.findItemSortByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public void removeItemSortByIds(List<Long> ids) {
		for (Long id : ids) {
			removeItemSortById(id);
		}
	}

	@Override
	public void removeItemSortById(Long id) {
		ItemCategory itemCate = itemCategorySortDao.getByPrimaryKey(id);
		if (itemCate != null) {
			if (itemCate.getSortNo() != null) {
				itemCate.setSortNo(null);
				itemCategorySortDao.save(itemCate);
			} else 
				throw new BusinessException(Constants.ITEM_SORT_UNSORTED);
		} else 
			throw new BusinessException(Constants.ITEM_SORT_NOTEXIST);
	}

	@Override
	public void upItemSort(Long id,Long categoryId) {
		ItemCategory itemCate = itemCategorySortDao.getByPrimaryKey(id);
		if (itemCate != null) {
		    List<Long> idList=getChildIdList(categoryId);
			ItemCategory itemCateAbove = itemCategorySortDao.findItemAbove(itemCate.getSortNo(), idList);
			if (itemCateAbove != null) {
				swap(itemCate, itemCateAbove);
				itemCategorySortDao.save(itemCateAbove);
				itemCategorySortDao.save(itemCate);
			} else 
				throw new BusinessException(Constants.ITEM_SORT_NOHIGHER);
		} else 
			throw new BusinessException(Constants.ITEM_SORT_NOTEXIST);
	}

	@Override
	public void downItemSort(Long id,Long categoryId) {
		ItemCategory itemCate = itemCategorySortDao.getByPrimaryKey(id);
		if (itemCate != null) {
		    List<Long> idList=getChildIdList(categoryId);
			ItemCategory itemCateUnder = itemCategorySortDao.findItemUnder(itemCate.getSortNo(), idList);
			if (itemCateUnder != null) {
				swap(itemCate, itemCateUnder);
				itemCategorySortDao.save(itemCateUnder);
				itemCategorySortDao.save(itemCate);
			} else 
				throw new BusinessException(Constants.ITEM_SORT_NOLOWER);
		} else 
			throw new BusinessException(Constants.ITEM_SORT_NOTEXIST);
	}

	/**
     * 获取一个节点下所有节点ID
     * @param categoryId
     * @return
     */
	@Transactional(readOnly=true)
    private List<Long> getChildIdList(Long cateId){
        if(cateId==null){
            return null;
        }
        List<Long> idList=new ArrayList<Long>();
        Long categoryId=Long.valueOf(cateId);
        List<Category> categories=categoryDao.findEnableCategoryList(null);
        putChildIdList(categoryId, idList ,categories);
        return idList;
    }
    
    /**
     * 查询一个节点下所有节点ID并且放入idList
     * @param categoryId
     * @param idList
     */
    private void putChildIdList(Long categoryId,List<Long> idList,List<Category> categories){
        idList.add(categoryId);
        List<Category> categoryList=getCategoryListByParentId(categories,categoryId);
        if(null==categoryList||categoryList.isEmpty()){
            return;
        }
        for(Category cate:categoryList){
            putChildIdList(cate.getId(), idList,categories);
        }
    }
	
    private List<Category> getCategoryListByParentId(List<Category> categories,Long categoryId){
        if(categories==null||categories.isEmpty()){
            return null;
        }
        List<Category> result=new ArrayList<Category>();
        for(Category category:categories){
            if(categoryId.equals(category.getParentId())){
                result.add(category);
            }
        }
        categories.removeAll(result);
        return result;
    }
    
	@Override
	public void createItemSortByIds(Long[] ids, Long categoryId) {
		Integer lastInt;
		ItemCategory last = itemCategorySortDao.findItemBottom(null);
		if (last != null) {
			lastInt = last.getSortNo();
		} else {
			lastInt = 0;
		}
		for (Long id : ids) {
			ItemCategory itemCate = itemCategorySortDao.getByPrimaryKey(id);
			if (itemCate != null) {
				if (itemCate.getSortNo() == null) {
					itemCate.setSortNo(++lastInt);
					itemCategorySortDao.save(itemCate);
				} else 
					throw new BusinessException(Constants.ITEM_SORT_SORTED);
			} else 
				throw new BusinessException(Constants.ITEM_SORT_NOTEXIST);
		}
	}

	private void swap(ItemCategory v1, ItemCategory v2) {
		Integer x = v1.getSortNo();
		v1.setSortNo(v2.getSortNo());
		v2.setSortNo(x);
	}

}
