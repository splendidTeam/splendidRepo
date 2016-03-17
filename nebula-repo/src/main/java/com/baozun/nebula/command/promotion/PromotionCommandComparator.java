/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package com.baozun.nebula.command.promotion;

import java.util.Comparator;

public class PromotionCommandComparator implements Comparator<PromotionCommand>{

	@Override
	public int compare(PromotionCommand o1, PromotionCommand o2) {
		
		if(o1.getDefaultPriority() < o2.getDefaultPriority()){
			return -1;
		}else if(o1.getDefaultPriority() > o2.getDefaultPriority()){
			return 1;
		}else{
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
