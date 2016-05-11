package com.baozun.nebula.model.promotion;

import java.util.Comparator;

import com.baozun.nebula.calculateEngine.condition.AtomicCondition;

public class PromotionConditionStepComparator implements Comparator<AtomicCondition>{
	@Override
	public int compare(AtomicCondition o1, AtomicCondition o2) {
		AtomicCondition step0=(AtomicCondition)o1;
		AtomicCondition step1=(AtomicCondition)o2;
		
		int flag = step0.getStepPriority().compareTo(step1.getStepPriority());
		  
		return flag;
	} 
}