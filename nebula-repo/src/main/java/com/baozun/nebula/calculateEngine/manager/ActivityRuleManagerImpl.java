package com.baozun.nebula.calculateEngine.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baozun.nebula.command.promotion.PromotionCommand;

@Service("activityRuleManager")
public class ActivityRuleManagerImpl implements ActivityRuleManager {

	/**
	 * 返回所有促销规则PromotionCommand
	 */
	@Override
	public List<PromotionCommand> getActivityRuleData(List<PromotionCommand> promotionCommandList) {
	//	List<PromotionCommand> promotionCommandList = new ArrayList<PromotionCommand>();
		// TODO Auto-generated method stub
		return promotionCommandList;
	}

}
