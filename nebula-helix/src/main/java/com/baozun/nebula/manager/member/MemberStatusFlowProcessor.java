/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.member;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.web.MemberDetails;

/**
 * @author D.C
 * @time 2016年3月31日 上午11:00:22
 */
public class MemberStatusFlowProcessor {
	List<MemberStatusHandler> handlers = new ArrayList<MemberStatusHandler>();
	
	void doProcess(MemberDetails memberDetails) {
		for(MemberStatusHandler handler : handlers) {
			handler.execute(memberDetails);
		}
	}
}
