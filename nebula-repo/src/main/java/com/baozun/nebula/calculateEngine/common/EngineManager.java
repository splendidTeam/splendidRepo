/**
 * 
 */
package com.baozun.nebula.calculateEngine.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionMarkdownPriceCommand;
import com.baozun.nebula.command.promotion.PromotionPriorityAdjustDetailCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;

/**
 * @author 陆君
 * @param <T>
 * @creattime 2013-11-19
 */
@Component
public class EngineManager {

	private List<PromotionCommand> promotionCommandList;

	private List<ItemTagRuleCommand> itemScopeList;

	private List<MemberTagRuleCommand> crowdScopeList;

	private ItemScopeEngine itemScopeEngine;

	private CrowdScopeEngine crowdScopeEngine;

	private List<MemberTagRuleCommand> customMemberGroups;

	private List<ItemTagRuleCommand> customProductCombos;

	private List<LimitCommand> limitCommandList;
	
	private  List<PromotionPriorityAdjustDetailCommand> promotionAdjustDetailList;
	
	private  List<PromotionMarkdownPrice> promotionMarkdownPriceList;
	
	private static class SingletonHolder {
		public final static EngineManager instance = new EngineManager();
	}

	public static EngineManager getInstance() {
		return SingletonHolder.instance;
	}

	private EngineManager() {
		itemScopeEngine = new ItemScopeEngine();
		crowdScopeEngine = new CrowdScopeEngine();
	}

	/**
	 * 启动促销引擎；在设置促销条件之后调用这个方法
	 */
	public void build() {
		if (null != this.itemScopeList) {
			itemScopeEngine.buildEngine(itemScopeList);
		}
		if (null != this.crowdScopeList) {
			crowdScopeEngine.buildEngine(crowdScopeList);
		}
	}

	public List<PromotionCommand> getPromotionCommandList() {
		return promotionCommandList;
	}

	public void setPromotionCommandList(List<PromotionCommand> promotionCommandList) {
		this.promotionCommandList = promotionCommandList;
	}

	public List<ItemTagRuleCommand> getItemScopeList() {
		return itemScopeList;
	}

	public void setItemScopeList(List<ItemTagRuleCommand> itemScopeList) {
		this.itemScopeList = itemScopeList;
	}

	public List<MemberTagRuleCommand> getCrowdScopeList() {
		return crowdScopeList;
	}

	public void setCrowdScopeList(List<MemberTagRuleCommand> crowdScopeList) {
		this.crowdScopeList = crowdScopeList;
	}

	public ItemScopeEngine getItemScopeEngine() {
		return itemScopeEngine;
	}

	public void setItemScopeEngine(ItemScopeEngine itemScopeEngine) {
		this.itemScopeEngine = itemScopeEngine;
	}

	public CrowdScopeEngine getCrowdScopeEngine() {
		return crowdScopeEngine;
	}

	public void setCrowdScopeEngine(CrowdScopeEngine crowdScopeEngine) {
		this.crowdScopeEngine = crowdScopeEngine;
	}

	public List<MemberTagRuleCommand> getCustomMemberGroups() {
		return customMemberGroups;
	}

	public void setCustomMemberGroups(List<MemberTagRuleCommand> customMemberGroups) {
		this.customMemberGroups = customMemberGroups;
	}

	public List<ItemTagRuleCommand> getCustomProductCombos() {
		return customProductCombos;
	}

	public void setCustomProductCombos(List<ItemTagRuleCommand> customProductCombos) {
		this.customProductCombos = customProductCombos;
	}

	public List<LimitCommand> getLimitCommandList() {
		return limitCommandList;
	}

	public void setLimitCommandList(List<LimitCommand> limitCommandList) {
		this.limitCommandList = limitCommandList;
	}

	public List<PromotionPriorityAdjustDetailCommand> getPromotionAdjustDetailList() {
		return promotionAdjustDetailList;
	}

	public void setPromotionAdjustDetailList(
			List<PromotionPriorityAdjustDetailCommand> promotionAdjustDetailList) {
		this.promotionAdjustDetailList = promotionAdjustDetailList;
	}

	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceList() {
		return promotionMarkdownPriceList;
	}

	public void setPromotionMarkdownPriceList(
			List<PromotionMarkdownPrice> promotionMarkdownPriceList) {
		this.promotionMarkdownPriceList = promotionMarkdownPriceList;
	}
}
