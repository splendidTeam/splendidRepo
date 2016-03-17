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
package com.baozun.nebula.manager.column;

import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * @author Tianlong.Zhang
 *
 */
public interface ColumnManager extends BaseManager{
	
	/**
	 * 根据页面Code 查找 页面module Map, 并将 component 表示的 type 或者是 item信息查询出来
	 * @param code 
	 * @return moduleCode 为key 的ColumnModuleCommand map
	 */
	ColumnPageCommand  findColumnModuleMapByPageCode(String pageCode);
	
	/**
	 * 获取已经发布的信息
	 * @param pageCode
	 * @return
	 */
	ColumnPageCommand getPublishedPageByCode(String pageCode) ;
	
}
