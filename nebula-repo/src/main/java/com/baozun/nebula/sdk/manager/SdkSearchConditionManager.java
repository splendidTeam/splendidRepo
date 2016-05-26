package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.SearchConditionCommand;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 筛选条件管理
 * @author 阳羽
 * @createtime 2014-2-10 下午12:45:50
 */
public interface SdkSearchConditionManager extends BaseManager{
	
    /**
     * 查询单个
     * @param id
     * @return
     */
    public SearchConditionCommand findSearchConditionCommandById(Long id);
    
    com.baozun.nebula.command.product.SearchConditionCommand findSearchConditionCommandI18nById(Long id);
    
	/**
	 * 获取搜索条件列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<SearchConditionCommand> findSearchConditionByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 新增或修改搜索条件
	 * @param searchCondition
	 * @return
	 */
	public SearchCondition createOrUpdateSearchCondition(SearchCondition searchCondition);
	
	public SearchCondition createOrUpdateSearchCondition(
			com.baozun.nebula.command.product.SearchConditionCommand searchCondition);
	
	/**
	 * 批量删除搜过条件
	 * @param ids 搜索条件id列表
	 * @return 受影响行
	 */
	public Integer removeSearchConditionByIds(List<Long> ids);
	
	/**
	 * 单个删除搜索条件
	 * @param id 搜索条件id
	 */
	public void removeSearchCondition(Long id);

	/**
	 * 启用搜索条件
	 * @param id 搜索条件id
	 */
	public void enableSearchCondition(Long id);
	
	/**
	 * 禁用搜索条件
	 * @param id 搜索条件id
	 */
	public void disableSearchCondition(Long id);
	
	/**
	 * 根据分类查找 SearchCondition
	 * @param cid
	 * @return
	 */
	public List<SearchConditionCommand> findConditionByCategoryId(Long cid);
	
	/**
	 * 根据分类查找 SearchCondition
	 * @param cid
	 * @return
	 */
	public List<SearchConditionCommand> findConditionByCategoryIdList(List<Long> cids);
	
	
	
	/**
	 * 根据导航查找 SearchCondition
	 * 
	 * from searchCondition where navigationId = navigationId or navigationId is null
	 * @param navigationId
	 * @return
	 */
	public List<SearchConditionCommand> findConditionByNavigation(Long navigationId);
	
	/**
	 * 根据语言查询所有搜索条件的数据
	 * @return List<MetaDataCommand>
	 * @param lang
	 * @return 
	 * @author 冯明雷
	 * @time 2016年4月28日下午5:33:44
	 */
	List<SearchConditionCommand> findSearchConditionMetDataByLang(String lang);
	
}
