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

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.model.product.SearchConditionLang;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.search.command.MetaDataCommand;

/**
 * @author Tianlong.Zhang
 *
 */
public interface SearchConditionDao extends GenericEntityDao<SearchCondition, Long> {
	
	@NativeQuery(model =SearchCondition.class )
	List<SearchCondition> findConditionByCategoryId(@QueryParam("cid")Long cid);
	
	@NativeQuery(model = SearchConditionCommand.class, pagable = true, withGroupby = true)
    public Pagination<SearchConditionCommand> findSearchConditionByQueryMapWithPage(Page page,Sort[] sorts,
            @QueryParam Map<String, Object> paraMap);
	
	/**
	 * 查询单个
	 * @param id
	 * @return
	 */
	@NativeQuery(model = SearchConditionCommand.class)
	public SearchConditionCommand findSearchConditionById(@QueryParam("id")Long id);
	
	/**
	 * 新增
	 * @param searchConditionCommand
	 * @return
	 */
	@NativeUpdate
	public Integer insertSearchCondition(@QueryParam("sc")SearchCondition searchCondition);
	
	/**
	 * 修改
	 * @param searchConditionCommand
	 * @param id
	 * @return
	 */
    @NativeUpdate
    public Integer updateSearchConditionById(@QueryParam("sc")SearchCondition searchCondition,@QueryParam("id")Long id);
    
    /**
     * 禁用
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer disableSearchConditionById(@QueryParam("id")Long id);
    

    /**
     * 启用
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer enableSearchConditionById(@QueryParam("id")Long id);

    /**
     * 删除
     * @param id
     * @return
     */
    @NativeUpdate
    public Integer deleteSearchConditionById(@QueryParam("id")Long id);
    
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @NativeUpdate
    public Integer deleteSearchConditionByIds(@QueryParam("ids")List<Long> ids);
	
	@NativeQuery(model =SearchCondition.class )
	List<SearchCondition> findConditionByCategoryIds(@QueryParam("cids")List<Long> cids);
	
	/**
	 * 查找导航下有效的搜索条件，将本导航和平台导航的搜索条件查询出来，
	 * 如果本导航中已经有的筛选条件，平台级别的不需要查询出来，是通过name来判断的
	 * @param navId
	 * @return
	 */
	@NativeQuery(model =SearchCondition.class )
	List<SearchCondition> findConditionByNavigaion(@QueryParam("navId")Long navId);
	
	/**
	 * 
	* @author 何波
	* @Description: 修改搜索条件国际化
	* @param searchCondition
	* @return   
	* Integer   
	* @throws
	 */
	@NativeUpdate
	int updateSearchConditionLang(@QueryParam Map<String, Object>  params);
	
	@NativeQuery(model = SearchConditionLang.class)
    List<SearchConditionLang> findSearchConditionLangByScids(@QueryParam("scids") List<Long> scids,@QueryParam("langs") List<String> langs);
	
	@NativeQuery(model = SearchConditionLang.class)
	SearchConditionLang findSearchConditionLang(@QueryParam("id")Long id,@QueryParam("lang") String lang);
	
	/**
	 * 根据语言查询所有搜索条件的数据
	 * @return List<MetaDataCommand>
	 * @param lang
	 * @return 
	 * @author 冯明雷
	 * @time 2016年4月28日下午5:33:44
	 */
	@NativeQuery(model = SearchConditionCommand.class)
	List<SearchConditionCommand> findSearchConditionMetDataByLang(@QueryParam("lang") String lang);
}
