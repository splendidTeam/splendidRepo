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
 * 用户状态的流转处理流程器
 * @author D.C
 * @time 2016年3月31日 上午11:00:22
 */
public class MemberStatusFlowProcessor {

	private List<StatusAction> statusActionList = new ArrayList<StatusAction>();

	public List<StatusAction> getStatusActionList() {
		return statusActionList;
	}

	public void setStatusActionList(List<StatusAction> statusActionList) {
		this.statusActionList = statusActionList;
	}

	/**
	 * 返回null时前端不做任何处理，非null时需要流转到对应的action
	 * @param memberDetails
	 * @return
	 */
	public String process(MemberDetails memberDetails) {
		for (StatusAction statusAction : statusActionList) {
			if (memberDetails.getStatus().contains(statusAction.getStatus())) {
				return statusAction.getAction();
			}
		}
		return null;
	}
	
	
	/**
	 * 状态流转映射
	 * @author D.C
	 * 上午11:13:49
	 */
	class StatusAction {
		/**
		 * 状态定义，需要和表中定义一致
		 */
		private String status;
		/**
		 * 某种状态的流转路径
		 */
		private String action;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
	}
}
