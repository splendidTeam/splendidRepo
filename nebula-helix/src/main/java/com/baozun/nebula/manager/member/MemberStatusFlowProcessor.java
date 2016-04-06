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

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.web.MemberDetails;
import com.feilong.core.Validator;

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
	 * @param request
	 * @return
	 */
	public String process(MemberDetails memberDetails,HttpServletRequest request) {
		if(Validator.isNotNullOrEmpty(memberDetails.getStatus())){
			for (StatusAction statusAction : statusActionList) {
				if (memberDetails.getStatus().contains(statusAction.getStatus())) {
					if(statusAction.getWhiteList().contains(request.getRequestURI())){
						return null;
					}
					return statusAction.getAction();
				}
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
		/**
		 * 白名单，这里的url不会拦截
		 */
		private List<String> whiteList;

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

		public List<String> getWhiteList() {
			return whiteList;
		}

		public void setWhiteList(List<String> whiteList) {
			this.whiteList = whiteList;
		}
		
		
	}
}
