package com.baozun.nebula.manager.product;
 
import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.manager.BaseManager;  
import com.baozun.nebula.sdk.command.ItemRateCommand;

public interface ItemRateManager extends BaseManager {
	/**
	 * 创建评价
	 * @param itemRate
	 * @return
	 */
	public void  createRate(RateCommand rateCommand);
	
	/***
	 * 查询评价列表，评价列表包含了商品订单行、评价情况
	 * @param page 
	 * @param memberId
	 */
	public Pagination<ItemRateCommand> findItemRateList(Page page, Long memberId,Integer evaluationStatus,String beginTime, String endTime);
	
	
	/***
	 * 查看评价详情
	 * @param rateId
	 * @return
	 */
	public RateCommand findItemRateById(Long rateId ,Long memberId);
	
	/**
	 * 通过itemId查询商品的评价
	 * @param page
	 * @param itemId
	 * @return
	 */
	public Pagination<RateCommand> findItemRateListByItemId(Page page, Long itemId, Sort[] sorts);
	
	/**
	 * 通过itemId查询商品的评价(根据itemIds查找)
	 * @param page
	 * @param itemIds
	 * @param sorts
	 * @return
	 */
	Pagination<RateCommand> findItemRateListByItemIds(Page page,
			List<Long> itemIds, Sort[] sorts);
	
	/**
	 * 根据itemCode查询评论数量
	 * @param itemCode
	 * @return
	 */
	public Integer findRateCountByItemCode(String itemCode);

	
}
