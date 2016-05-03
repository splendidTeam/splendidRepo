package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.SearchConditionItem;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.search.command.MetaDataCommand;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 筛选条件选项管理
 * @author 阳羽
 * @createtime 2014-2-10 下午12:59:07
 */
public interface SdkSearchConditionItemManager extends BaseManager{

	/**
	 * 获取搜索条件选项
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<SearchConditionItemCommand> findSearchConditionItemByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 查询单个
	 * @param id
	 * @return
	 */
	public SearchConditionItemCommand findSearchConditionItemCommandById(Long id);
	
	com.baozun.nebula.command.product.SearchConditionItemCommand findSearchConditionItemCommandI18nById(Long id);
	
	/**
	 *添加或修改搜索条件选项
	 * @param searchConditionItem
	 * @return
	 */
	public SearchConditionItem createOrUpdateConditionItem(SearchConditionItem searchConditionItem);
	
	SearchConditionItem createOrUpdateConditionItem(com.baozun.nebula.command.product.SearchConditionItemCommand searchConditionItemCommand);
	
	/**
	 * 批量删除搜索条件选项
	 * @param ids 筛选条件选项id列表
	 * @return 受影响行
	 */
	public Integer removeSearchConditionItemByIds(List<Long> ids);
	
    /**
     * 批量删除搜索条件选项根据父ID
     * @param ids 筛选条件选项id列表
     * @return 受影响行
     */
    public Integer removeSearchConditionItemByPids(List<Long> pids);

	/**
	 * 单个删除搜索条件选项
	 * @param id 筛选条件选项id
	 */
	public void removeSearchConditionItemById(Long id);
	
	/**
	 * 启用搜索条件选项
	 * @param id 筛选条件选项id
	 */
	public void enableSearchConditionItem(Long id);
	
	/**
	 * 禁用搜索条件选项
	 * @param id 筛选条件选项id
	 */
	public void disableSearchConditionItem(Long id);
	
	public List<SearchConditionItemCommand> findItemByPropertyValueId(Long pValueId);
	
	public List<SearchConditionItemCommand> findItemBySId(Long sId);
	
	public SearchConditionItemCommand findItemById(Long id);
	
	public List<SearchConditionItemCommand> findItemByPropertyId(Long propertyId);
	
	
	/**
	 * 根据语言查询所有搜索条件选项的数据(只有propertyId、name、sortNo字段)
	 * @return List<MetaDataCommand>
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年5月3日下午4:32:46
	 */
	List<MetaDataCommand> findSearchConditionItemMetDataByLang(String lang);
}
