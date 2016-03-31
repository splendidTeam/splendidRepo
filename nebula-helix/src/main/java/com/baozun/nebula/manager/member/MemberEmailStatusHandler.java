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

import com.baozun.nebula.web.MemberDetails;

/**
 * @author D.C
 * @time 2016年3月31日 上午11:03:26
 */
public class MemberEmailStatusHandler implements MemberStatusHandler {
	public void execute(MemberDetails memberDetails) {
		if(MemberDetails.Status.EMAIL_ACTIVE.getValue().equals(memberDetails.getStatus())) {
			return;
		} else if(MemberDetails.Status.EMAIL_INACTIVE.getValue().equals(memberDetails.getStatus())) {
			return;
		}
	}


	@Override
	public String nextStep() {
		return null;
	}
}
