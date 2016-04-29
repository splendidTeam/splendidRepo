package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.search.command.MetaDataCommand;

import loxia.dao.Sort;

/**
 * 菜单导航管理
 * @author 阳羽
 * @createtime 2014-2-10 下午12:27:08
 */
public interface SdkNavigationManager extends BaseManager{
	
	/**
	 * 获取导航菜单列表
	 * @param sorts
	 * @return
	 */
	public List<Navigation> findNavigationList(Sort[] sorts);

	/**
	 * 获取所有有效的导航菜单列表
	 * @param sorts
	 * @return
	 */
	public List<Navigation> findAvailableNavigationList(Sort[] sorts);
	
	/**
	 * 添加或修改导航菜单
	 * @param navigation
	 * @param ids 
	 * @return
	 */
	public Navigation createOrUpdateNavigation(Navigation navigation);
	
	/**
	 * 根据id删除导航菜单
	 * @param id
	 */
	public void removeNavigationById(Long id);

	/**
	 * 排序导航
	 * @param idList
	 * @param userId
	 */
	public void sortNavigationsByIds(List<Long> idList, Long userId);
	
	/**
	 * 根据url查找有效的导航菜单，主要是为导航菜单服务
	 * @param url
	 * @return
	 */
	public Navigation findEffectNavigationByUrl(String url);
	
	/**
	 * 根据语言查询所有导航的数据(只有id、name、sortNo字段)
	 * @return List<Navigation>
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年4月28日下午4:29:08
	 */
	List<MetaDataCommand> findNavigationMetaDataBylang(String lang);
	
}
