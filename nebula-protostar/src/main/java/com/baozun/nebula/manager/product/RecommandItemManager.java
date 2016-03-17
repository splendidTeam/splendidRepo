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
 */
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.RecommandItemCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.RecommandItem;
/**
 * 推荐商品Manager
 * 
 * @author chenguang.zhou
 * @date 2014年4月10日 下午2:36:21
 */
public interface RecommandItemManager extends BaseManager {

	/**
	 * 查询推荐商品
	 * @param page
	 * @param sorts
	 * @param paramMap
	 * @return
	 */
	List<RecommandItemCommand> findRecommandItemListByParaMap( Map<String, Object> paramMap);
	
	
	/**
	 * 查询推荐商品(分页)
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<RecommandItemCommand> findRecommandItemListByParaMap(Page page, Sort[] sorts, Map<String, Object> paraMap);
	
	/**
	 * 创建或修改推荐
	 * @param recommandItem
	 * @return
	 */
	void createOrUpdateRecItem(RecommandItem[] rcommandItems, Long userId, Integer type, Long param);
	
	
	/**
	 * 通过Id删除推荐
	 * @param id
	 */
	void removeRecommandItemById(Long id);
	
	/**
	 * 批量删除推荐
	 * @param id
	 */
	void removeRecommandItemByIds(Long[] ids);
	
	/**
	 * 通过Id查询推荐
	 * @param id
	 * @return
	 */
	RecommandItemCommand findRecommandItemById(Long id);
	
	/**
	 * 禁用/启用推荐
	 * @param Id
	 */
	void enabledOrDisenabledRecItem(Long id, Integer status);

	/**
	 * 根据编号列表查询推荐商品列表
	 * @param codeList
	 */
	List<RecommandItemCommand> findRecommandItemListByCodeList(List<String> codeList);
}
