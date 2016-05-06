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
 *
 */
package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.product.SearchConditionItem;
import com.baozun.nebula.model.product.SearchConditionItemlang;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * @author Tianlong.Zhang
 *
 */
public interface SearchConditionItemDao extends GenericEntityDao<SearchConditionItem, Long>{
	
	@NativeQuery(model=SearchConditionItem.class)
	List<SearchConditionItem> findItemByPropertyValueId(@QueryParam("pValueId")Long pValueId); 
	
	@NativeQuery(model=SearchConditionItem.class)
	List<SearchConditionItem> findItemBySearchId(@QueryParam("searchId")Long searchId);
	
	@NativeQuery(model=SearchConditionItem.class)
	List<SearchConditionItem> findItemByPropertyId(@QueryParam("propertyId") Long propertyId);
	
	@NativeQuery(model=SearchConditionItem.class)
	List<SearchConditionItem> findSearchItemByPropertyIds(@QueryParam("propertyIds") List<Long> propertyIds);
	
	@NativeQuery(model = SearchConditionItemCommand.class, pagable = true, withGroupby = true)
    public Pagination<SearchConditionItemCommand> findSearchConditionItemByQueryMapWithPage(Page page,Sort[] sorts,
            @QueryParam Map<String, Object> paraMap);
	

    /**
     * 查询单个
     * @param id
     * @return
     */
    @NativeQuery(model = SearchConditionItemCommand.class)
    public SearchConditionItemCommand findSearchConditionItemById(@QueryParam("id")Long id);
	
	/**
	 * 新增
	 * @param searchConditionItemCommand
	 * @return
	 */
	@NativeUpdate
	public Integer insertSearchConditionItem(@QueryParam("sci")SearchConditionItem searchConditionItem);
	
	/**
	 * 修改
	 * @param searchConditionItemCommand
	 * @param id
	 * @return
	 */
    @NativeUpdate
    public Integer updateSearchConditionItemById(@QueryParam("sci")SearchConditionItem searchConditionItem,@QueryParam("id")Long id);
    
    /**
     * 禁用
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer disableSearchConditionItemById(@QueryParam("id")Long id);
    
    /**
     * 启用
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer enableSearchConditionItemById(@QueryParam("id")Long id);
    
    /**
     * 删除
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer deleteSearchConditionItemById(@QueryParam("id")Long id);
    
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @NativeUpdate
    public Integer deleteSearchConditionItemByIds(@QueryParam("ids")List<Long> ids);
    
    /**
     * 批量删除根据父ID
     * @param ids
     * @return
     */
    @NativeUpdate
    public Integer deleteSearchConditionItemByParentIds(@QueryParam("pids")List<Long> pids);
    
	
	/**
	 * 
	* @author 何波
	* @Description: 修改可选搜索条件国际化
	* @param searchCondition
	* @return   
	* Integer   
	* @throws
	 */
	@NativeUpdate
	int updateSearchConditionItemLang(@QueryParam Map<String, Object>  params);
	
	@NativeQuery(model = SearchConditionItemlang.class)
    List<SearchConditionItemlang> findSearchConditionItemLangByScids(@QueryParam("scids") List<Long> scids,@QueryParam("langs") List<String> langs);
	
	@NativeQuery(model = SearchConditionItemlang.class)
	SearchConditionItemlang findSearchConditionItemlang(@QueryParam("id")Long id,@QueryParam("lang") String lang);
	
	
	/**
	 * 根据搜索条件id和语言查询搜索条件选项
	 * @return List<SearchConditionItemCommand>
	 * @param sid
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年5月4日下午4:32:01
	 */
	@NativeQuery(model = SearchConditionItemCommand.class)
	List<SearchConditionItemCommand> findItemMetaBySIdAndLang(@QueryParam("sid")Long sid,@QueryParam("lang") String lang);
	
}
