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

package com.baozun.nebula.manager.sns;

import java.util.Date;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.sns.Consultants;

/**
 * @author - 项硕
 */
public interface ConsultantsManager extends BaseManager {

	/**
	 * 按条件查询咨询列表
	 * @param memberId
	 * @param beginTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public Pagination<ConsultantCommand> findConsultantsList(Long memberId, Date beginTime, Date endTime, Page page);

	/**
	 * 创建咨询
	 * @param consultants
	 * @return
	 */
	public Consultants createConsultants(Consultants consultants);
	
	/**
	 * 通过商品Id查询商品的咨询
	 *
	 * @param page
	 * @param itemId
	 * @return
	 */
	public Pagination<ConsultantCommand> findConsultantsListByItemId(Page page, Long itemId, Sort[] sorts);
}
