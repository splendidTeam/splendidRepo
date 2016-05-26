package com.baozun.nebula.sdk.manager.promotion;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionMarkdownPriceCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

public interface SdkPromotionManager extends BaseManager{
	
	List<PromotionBrief> doPromotion(ShoppingCartCommand shoppingCartCommand);
	 
	/**
	 * 获得所有活动有效期包含在当前时间的以启用的活动
	 * @return
	 */
	public  List<PromotionCommand> publishPromotion (Date currentTime);
	
	/**
	 * 获得所有活动有效期包含在当前时间的以启用的活动
	 * @return
	 */
	List<PromotionCommand> getEffectPromotion (Date currentTime);
	
	
	/**
	 * 关闭一个已经设置完毕的促销
	 * @param promotionCommand
	 * @return
	 */ 
	void inactivatePromotionById(Long pid, Long userId); 
	
	/**
	 * 根据生效时间来确定促销生命周期
	 * @param lifecycle
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int calculateLifecycle(int lifecycle, Date startTime, Date endTime);
	
	/**
	 * 查询与之冲突的促销列表
	 * @param id
	 * @param shopId
	 * @return
	 */
	public List<PromotionQueryCommand> findConflictingPromotionListById(Long id, Long shopId);
	
	/**
	 * 
	* @author 何波
	* @Description: 检查不同活动之间礼品是否冲突
	* @param id
	* @return   
	* BackWarnEntity   
	* @throws
	 */
	public List<PromotionQueryCommand> checkConflictNotActvieGift(Long id, Long shopId);
	
	/**
	 * 启用促销
	 * @param id
	 * @param userId
	 */
	public void activatePromotionById(Long id, Long userId);
	/**
	 * 检查两个促销之间是否存在冲突
	 * @param one
	 * @param toCheckOne
	 * @return
	 */
	public Boolean checkConflictingBetweenPromotions(PromotionCommand one, PromotionCommand toCheckOne);
	/**
	 * 获得所有已经启用的活动
	 * @return
	 */
	List<PromotionCommand> findAllPromotionEnableList();
	
	/**
	 * 获取商品Id中的价格调整
	 */
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceListByItemId(Long itemId);
	/**
	 * 获取商品Id列表中的价格调整
	 */
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceListByItemIdList(List<Long> itemIdList);
}
