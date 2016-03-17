package com.baozun.nebula.model.promotion;

import java.util.Comparator;

public class LinePromotionSizeComparator implements Comparator<LinePromotionSize>{
	@Override
	public int compare(LinePromotionSize o1, LinePromotionSize o2) {
		// TODO Auto-generated method stub
		LinePromotionSize step0=(LinePromotionSize)o1;
		LinePromotionSize step1=(LinePromotionSize)o2;
		
		if (step0.getPromotionSize()==step1.getPromotionSize())
			return 0;
		if (step0.getPromotionSize()>step1.getPromotionSize())
			return -1;
		else
			return 1;
	}
}
