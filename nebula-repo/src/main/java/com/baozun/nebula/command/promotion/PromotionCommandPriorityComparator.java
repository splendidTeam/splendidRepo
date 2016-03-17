package com.baozun.nebula.command.promotion;

import java.util.Comparator;

public class PromotionCommandPriorityComparator  implements Comparator<PromotionCommand>{

	@Override
	public int compare(PromotionCommand o1, PromotionCommand o2) {
		/**手工优先级设置**/
		if(o1.getPriority() < o2.getPriority()){
			return -1;
		}else if(o1.getPriority() > o2.getPriority()){
			return 1;
		}else{
			/**活动类型优先级设置**/
			int cType1 = o1.getConditionTypeId();
			int cType2 = o2.getConditionTypeId();
			if(cType1 < cType2){
				return -1;
			}else if(cType1 > cType2){
				return 1;
			}else{
					/**发布时间优先级设置**/
					int retval = 0;
		        	long time1 = o1.getPublishTime().getTime();
		        	long time2 = o2.getPublishTime().getTime();
		        	if(time1 < time2)
		        		retval = 1;
		        	else if(time1 > time2)
		        		retval = -1;
		        	return retval;
			}
		}
	}
}
