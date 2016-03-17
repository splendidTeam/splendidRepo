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
 */
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.fileupload.util.Streams;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemRate;

/**
 * 商品评论Manage
 * @author xingyu  
 * @date  
 * @version
 */
public interface ItemRateManager extends BaseManager{

	/**
	 * 分页查询 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return RateCommandPagination
	 */
	Pagination<RateCommand> findRateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	/**
	 * 回复评价
	 * @param id
	 * @param content
	 * @param operatorId
	 * @param isReply 是否回复过
	 * @param isPass  是否一并通过
	 */
	void replyRate(Long id,String content,Long operatorId,Integer isReply,Integer isPass);
	/**
	 * 批量审核通过
	 * @param itemRateIds
	 * @param operatorId
	 * @return 受影响行数
	 */
	Integer enableRateByIds(List<Long> itemRateIds,Long operatorId);
	/**
	 * 批量删除评价
	 * @param itemRateIds
	 * @param operatorId
	 * @return
	 */
	Integer disableRateByIds(List<Long> itemRateIds,Long operatorId);
	/**
	 * 审核通过
	 * @param itemRateId
	 * @param operatorId
	 */
	void enableRateById(Long itemRateId,Long operatorId);
	/**
	 * 删除评价
	 * @param itemRateId
	 * @param operatorId
	 */
	void disableRateById(Long itemRateId,Long operatorId);
	/**
	 * 获取单个评价
	 * @param itemRateId
	 * @return
	 */
	ItemRate findRateById(Long itemRateId);
	/**
	 * 导出商品评价
	 * @param paraMap
	 * @return Excel文件流
	 */
	Streams exportRate(Map<String, Object> paraMap);

}
