/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.task;

import java.util.Date;

import com.baozun.nebula.manager.column.ColumnManager;

/**
 * @author Tianlong.Zhang
 * 
 */
public class PublishModuleTask implements DynamicTask {

	private static String NAME_PREFIX = "TIME_TASK_PAGE_PUBLISH_";

	private ColumnManager columnManager;

	private String pageCode;

	private String moduleCode;

	/**
	 * @param columnManager
	 * @param pageCode
	 * @param moduleCode
	 * @param publishDate
	 */
	public PublishModuleTask(ColumnManager columnManager, String pageCode,
			String moduleCode) {
		super();
		this.columnManager = columnManager;
		this.pageCode = pageCode;
		this.moduleCode = moduleCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.task.DynamicTask#invoke()
	 */
	@Override
	public void invoke() throws Exception {
		columnManager.publishColumnModule(pageCode, moduleCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.task.DynamicTask#getTaskName()
	 */
	@Override
	public String getTaskName() {
		StringBuilder sb = new StringBuilder(NAME_PREFIX);
		sb.append(pageCode).append("_").append(moduleCode);
		return sb.toString();
	}

}
