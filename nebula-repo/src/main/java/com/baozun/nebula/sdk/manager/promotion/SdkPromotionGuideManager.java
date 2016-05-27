package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.UserDetails;

public interface SdkPromotionGuideManager extends BaseManager{
	/**
	 * 根据itemid List查询促销信息
	 * @param itemIds
	 * @return
	 */
	public Map<Long,List<PromotionCommand>> getPromotionsForItemList(List<Long> itemIds, UserDetails userDetails);
	
	/**
	 * 获取ItemId的活动，外加当前用户（游客即全员）
	 * @param itemId
	 * @param userDetails
	 * @return
	 */
	public List<PromotionCommand> getPromotionsForItem(Long itemId, UserDetails userDetails);
	
	
	/**
	 *  根据itemid List查询促销活动和单品折扣金额
	 * @param itemIds
	 * @param userDetails
	 * @return
	 */
	public Map<Long,Map<String,Object>> getPromotionsAndDisCountForItemList(List<Long> itemIds, UserDetails userDetails);

	/**
	 * 获取所有的赠品活动（后台管理使用）
	 * @param shopId
	 * @return
	 */
	public List<PromotionCommand> getAllGiftPromotion(Long shopId);
	
	/**
	 * 获取活动By ID
	 * @param shopId
	 * @return
	 */
	public PromotionCommand getPromotionById(Long prmId);
	/**
	 * 获取订单行上的单品折扣金额
	 * @return
	 */
	public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemListByOrderId(Long orderId);
}

