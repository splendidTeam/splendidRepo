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
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.command.product.ConsultantOperationlogCommand;
import com.baozun.nebula.command.product.ConsultantStatusCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 商品咨询Manager
 * @author Tianlong.Zhang
 *
 */
public interface ConsultantManager extends BaseManager{
	Pagination<ConsultantCommand> findConsultantListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 获得咨询状态枚举值
	 */
	List<ConsultantStatusCommand> findConsultantStatus();
	
	/**
	 * 响应咨询
	 * @param cmd
	 * @return
	 */
	boolean responseConsultant(ConsultantCommand cmd);

	/**
	 * 回复咨询
	 */
	boolean resolveConsultant(ConsultantCommand cmd);
	
	/**
	 * 更新咨询答案
	 * @param cmd
	 * @return
	 */
	boolean updateConsultant(ConsultantCommand cmd);
	
	/**
	 * 公开咨询
	 * @param cmd
	 * @return
	 */
	boolean publishConsultant(ConsultantCommand cmd);
	
	/**
	 * 取消公开咨询
	 * @param cmd
	 * @return
	 */
	boolean unpublishConsultant(ConsultantCommand cmd);
	
	/**
	 * 查找操作日志记录（分页）
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	Pagination<ConsultantOperationlogCommand> findOperationlogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

	/**
	 * @param id
	 * @return
	 */
	ConsultantCommand findConsultantById(Long id);

	/**
	 * 根据条件查询咨询列表
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	List<ConsultantCommand> findConsultantListByQueryMap(Sort[] sorts, Map<String, Object> paraMap);
}
