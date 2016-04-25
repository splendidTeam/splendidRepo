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
package com.baozun.nebula.manager.system;

/**
 * 访问控制策略类
 * @author D.C
 * @version 2016年3月28日 下午2:05:58
 */
public class RollingTimeWindow {
	/**
	 * Allowed operations per time window
	 */ 
	private Long limit;
	/**
	 * Rolling time window in seconds
	 */
	private Long window;
	
	
	
	public RollingTimeWindow(Long limit, Long window) {
		super();
		this.limit = limit;
		this.window = window;
	}
	
	
	public RollingTimeWindow() {
		super();
	}



	public Long getLimit() {
		return limit;
	}
	public void setLimit(Long limit) {
		this.limit = limit;
	}
	public Long getWindow() {
		return window;
	}
	public void setWindow(Long window) {
		this.window = window;
	}
	@Override
	public String toString() {
		return "RollingTimeWindow [limit=" + limit + ", window=" + window + "]";
	}
	
}
