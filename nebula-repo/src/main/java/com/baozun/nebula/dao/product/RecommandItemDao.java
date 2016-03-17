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

import com.baozun.nebula.command.product.RecommandItemCommand;
import com.baozun.nebula.model.product.RecommandItem;

/**
 * @author Tianlong.Zhang
 *
 */
public interface RecommandItemDao extends GenericEntityDao<RecommandItem, Long>{
	
	@NativeQuery(model=RecommandItem.class)
	public List<RecommandItem> findRecItemByTypeAndParam(@QueryParam("type") Integer type,@QueryParam("param") Long param);

	@NativeQuery(model=RecommandItem.class)
	public List<RecommandItem> findRecItemByTypeAndParams(@QueryParam("type") Integer type,@QueryParam("ids") List<Long> params);

	/**
	 * 查询推荐
	 * @param page
	 * @param sorts
	 * @param paramMap
	 * @return
	 */
	@NativeQuery(model = RecommandItemCommand.class)
	List<RecommandItemCommand> findRecommandItemListByParaMap(@QueryParam Map<String, Object> paramMap);

	/**
	 * 查询推荐(分页)
	 * @param page
	 * @param sorts
	 * @param paramMap
	 * @return
	 */
	@NativeQuery(model = RecommandItemCommand.class)
	Pagination<RecommandItemCommand> findRecommandItemListByParaMapWithPage(Page page,Sort[] sorts, @QueryParam Map<String, Object> paramMap);
	
	/**
	 * 通过Id查询推荐
	 * @param id
	 * @return
	 */
	@NativeQuery(model = RecommandItemCommand.class)
	RecommandItemCommand findRecommandItemById(@QueryParam("id") Long id);
	
	/**
	 * 通过推荐id集合删除推荐信息
	 * @param ids
	 */
	@NativeUpdate
	void removeRecommandItemByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 通过id修改推荐信息
	 */
	@NativeUpdate
	void updateRecommandItemById(@QueryParam("id") Long id, 
			@QueryParam("itemId") Long itemId,
			@QueryParam("param") Long param,
			@QueryParam("type") Integer type,
			@QueryParam("sort") Integer sort,
			@QueryParam("userId") Long userId);
	
	/**
	 * 启用推荐
	 * @param id
	 * @param lifecycle
	 */
	@NativeUpdate
	void enabledReceommandItemById(@QueryParam("id") Long id);

	/**
	 * 禁用推荐
	 * @param id
	 * @param lifecycle
	 */
	@NativeUpdate
	void disenabledReceommandItemById(@QueryParam("id") Long id);

	/**
	 * 根据编号列表查询推荐商品列表
	 * @param codeList
	 * @return
	 */
	@NativeQuery(model = RecommandItemCommand.class)
	public List<RecommandItemCommand> findRecommandItemListByCodeList(@QueryParam("codeList") List<String> codeList);
}
