package com.baozun.nebula.sdk.manager;
 
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustDetailCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjust;

public interface SdkPriorityAdjustManager extends BaseManager{ 
	
	/**
	 * 根据时间点获取冲突的促销
	 * @param timePoint
	 * @param shopId
	 * @return
	 */
	public List<PromotionCommand> findConflictingPromotionListByTimePoint(Date timePoint, Long shopId); 
	
	/**
	 * 通过Json获取数据库活动调整优先级数据
	 * @param pid
	 * @return
	 */   
	Pagination<PromotionPriorityAdjustCommand> findPriorityAdjustList(Page page,Sort[] sorts,
			@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 根据Id查询优先级信息类容
	 * @param pid
	 * @return
	 */
	PromotionPriorityAdjust  findPriorityAdjustById(Long id);
	
	PromotionPriorityAdjust  findPriorityAdjustByName(String name);
	
	/**
	 * 根据id启用优先级
	 * @param id
	 * @param userId
	 */
	public void enablePriorityById(Long id, Long userId);
	
	public Integer enableOrUnablePriorityById(Long id, Long userId,Integer activeMark);

	/**
	 * 根据id禁用优先级
	 * @param id
	 * @param userId
	 */
	public void disablePriorityById(Long id, Long userId);
	
	/**
	 * 创建或修改优先级
	 * @param cmd
	 */
	public Long saveOrUpdatePriority(PromotionPriorityAdjustCommand cmd);

	/**
	 * 根据优先级ID查询详细列表
	 * @param id
	 * @return
	 */
	public List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByPriorityId(
			Long id);
	
	/**
	 * 根据ShopID，和CurrentTime获取优先级调整列表
	 * @param ShopID,CurrentTime
	 * @return 
	 */
	public List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByShopIdCurrentTime(
			List<Long> shopIds,Date currentTime);
	
	public List<PromotionCommand> promotionAdjustPriority(List<PromotionCommand> originalPromosList,Long shopId, Date currentTime);
	
	public List<PromotionPriorityAdjustCommand> findEffectivePriorityList(Long shopId);
}
