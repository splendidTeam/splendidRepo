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

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.product.ItemRateListCommand;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.sdk.command.ItemRateCommand;

/**
 * ItemRateDao
 * @author  Justin
 *
 */
public interface ItemRateDao extends GenericEntityDao<ItemRate,Long>{

	/**
	 * 获取所有ItemRate列表
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	List<ItemRate> findAllItemRateList();
	
	/**
	 * 通过ids获取ItemRate列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	List<ItemRate> findItemRateListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取ItemRate列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	List<ItemRate> findItemRateListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取ItemRate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	Pagination<ItemRate> findItemRateListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	/**
	 * 通过ids批量删除ItemRate
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemRateByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的ItemRate列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	List<ItemRate> findAllEffectItemRateList();
	
	/**
	 * 通过参数map获取有效的ItemRate列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	List<ItemRate> findEffectItemRateListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过ids批量通过或删除ItemRate
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisableItemRateByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state,@QueryParam("operatorId")Long operatorId);
	
	/**
	 * 回复评价
	 * @param id
	 * @param reply
	 * @param operatorId
	 * @param isReply
	 */
	@NativeUpdate
	void replyRate(@QueryParam("id")Long id,@QueryParam("reply")String reply,@QueryParam("operatorId")Long operatorId,@QueryParam("isReply")Integer isReply);
	
	/**
	 * 通过id获取ItemRate列表
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ItemRate.class)
	ItemRate findItemRateListById(@QueryParam("id")Long id);
	/**
	 * 分页获取有效的ItemRate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = RateCommand.class)
	Pagination<RateCommand> findEffectItemRateListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemRate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = RateCommand.class)
	Pagination<RateCommand> findItemRateListByItemId(Page page, Sort[] sorts, @QueryParam("itemId") Long itemId);
	
	/**
	 * 分页获取有效的ItemRate列表(根据itemIds)
	 * @param page
	 * @param sorts
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(model = RateCommand.class)
	Pagination<RateCommand> findItemRateListByItemIds(Page page, Sort[] sorts, @QueryParam("itemIds") List<Long> itemIds);
 
	/**
	 * 查询评论 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = RateCommand.class)
	RateCommand findRateCommandById(@QueryParam("rateId") Long rateId);
 
	 /**
	 * 根据用户id查询有所有交易完成的订单,评论列表
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ItemRateListCommand.class)
	Pagination<ItemRateListCommand> findeStatusNndOrderListQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的ItemRate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = RateCommand.class)
	Pagination<RateCommand> findItemRateListByMemberId(Page page, Sort[] sorts, @QueryParam("memberId") Long memberId);
	
	/**
	 * 根据itemcode查询item的评论数
	 * @param itemCode
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class,alias={"c"})
	Integer findItemRateCountByItemCode(@QueryParam("itemCode") String itemCode);
}
