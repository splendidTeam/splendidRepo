package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.ItemSortCommand;

/**
 * 商品排序管理
 * @author 阳羽
 * @createtime 2014-2-10 下午12:58:59
 */
public interface SdkItemSortManager extends BaseManager{

	/**
	 * 查询商品排序列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<ItemSortCommand> findItemSortByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 批量删除商品排序
	 * @param ids 商品排序Id列表
	 */
	public void removeItemSortByIds(List<Long> ids);
	
	/**
	 * 删除商品排序
	 * @param id 商品排序Id
	 */
	public void removeItemSortById(Long id);
	
	/**
	 * 上移商品排序
	 * @param id 商品排序Id
	 */
	public void upItemSort(Long id,Long categoryId);
	
	/**
	 * 下移商品排序
	 * @param id 商品排序Id
	 */
	public void downItemSort(Long id,Long categoryId);
	
	/**
	 * 添加商品排序
	 * @param ids 商品id列表
	 * @param categoryId 
	 */
	public void createItemSortByIds(Long[] ids, Long categoryId);
}
