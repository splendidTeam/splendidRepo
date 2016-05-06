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
 *
 */

package com.baozun.nebula.dao.baseinfo;

import java.util.List;

import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.search.command.MetaDataCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

/**
 * @author - 项硕
 */
public interface NavigationDao extends GenericEntityDao<Navigation, Long> {

	/**
	 * 获取所有Navigation列表
	 * @return
	 */
	@NativeQuery(model = Navigation.class)
	List<Navigation> findAllNavigationList(Sort[] sorts);
	
	/**
	 * 获取所有有效的导航菜单列表
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = Navigation.class)
	List<Navigation> findAvailableNavigationList(Sort[] sorts);

	/**
	 * 根据父ID获取Navigation列表
	 * @param pid
	 * @return
	 */
	@NativeQuery(model = Navigation.class)
	List<Navigation> findNavigationListByParentId(@QueryParam("pid") Long pid);
	
	/**
	 * 根据父ID、名称查询有效导航列表
	 * @param name
	 * @param pid
	 * @return
	 */
	@NativeQuery(model = Navigation.class)
	List<Navigation> findEnableNavigationListByNameAndParentId(@QueryParam("name") String name,@QueryParam("pid") Long pid);

	/**
	 * 根据ID将导航的lifecycle设置为2，即删除该导航
	 * @param id
	 */
	@NativeUpdate
	int removeNavigationById(@QueryParam("id")Long id);
	
	@NativeQuery(model = Navigation.class)
	Navigation findNavigationByCategoryId(@QueryParam("cid") Long cid);
	
	/**
	 * 根据id查找有效导航菜单
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Navigation.class)
	Navigation findEffectNavigationById(@QueryParam("id")Long id);
	
	@NativeQuery(model = Navigation.class)
	Navigation findEffectNavigationByName(@QueryParam("name")String name);
	
	/**
	 * 根据url查找有效的导航菜单，主要是为导航菜单服务
	 * @param url
	 * @return
	 */
	@NativeQuery(model=Navigation.class)
	Navigation findEffectNavigationByUrl(@QueryParam("url") String url);
	
	
	/**
	 * 根据语言查询所有导航的数据(只有id、name、sortNo字段)
	 * @return List<Navigation>
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年4月28日下午4:29:08
	 */
	@NativeQuery(model = MetaDataCommand.class)
	List<MetaDataCommand> findNavigationMetaDataBylang(@QueryParam("lang") String lang);
	
	/***通过商品集合ID找到对应的前台导航
	 * @param itemCollectionId 商品集合ID
	 */	
	@NativeQuery(model = Navigation.class)
	Navigation findByItemCollectionId(@QueryParam("itemCollectId")Long itemCollectId);
	
}
