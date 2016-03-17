package com.baozun.nebula.calculateEngine.manager;


import java.util.List;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;

public interface ActivityRuleManager extends BaseManager{

	public List<PromotionCommand> getActivityRuleData(List<PromotionCommand> promotionCommandList);
	
}
