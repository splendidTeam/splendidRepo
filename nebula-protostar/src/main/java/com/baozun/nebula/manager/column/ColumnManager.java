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
 */
package com.baozun.nebula.manager.column;

import java.util.List;

import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.column.ColumnComponent;

/**
 * 模块管理Manager
 * 
 * @author chenguang.zhou
 * @date 2014年4月4日 上午9:16:46
 */
public interface ColumnManager extends BaseManager{
	
	/**
	 * 通过pageCode查询版块-组件信息
	 * @param pageCode
	 * @return
	 */
	public List<ColumnComponentCommand> findColumnComponentByPageCode(String pageCode);
	
	/**
	 * 通过pageCode查询版块-模块信息
	 * @param pageCode
	 * @return
	 */
	public List<ColumnModuleCommand> findColumnModuleByPageCode(String pageCode);
	
	/**
	 * 保存板块-组件信息
	 * @param columnComponents
	 * @throws Exception 
	 */
	public void saveColumnComponent(ColumnComponent[] columnComponents, String customBaseUrl, String moduleCode, String pageCode,String publishTime, Long targetId) throws Exception;

	/**
	 * 保存板块-组件信息
	 * @param columnComponents
	 * @throws Exception 
	 */
	public void saveColumnComponent(ColumnComponent[] columnComponents, String customBaseUrl, String moduleCode, String pageCode,String publishTime) throws Exception;
	
	/**
	 * 页面整体发布
	 * @param pageCode
	 * @return
	 */
	public boolean publishColumnPage(String pageCode);
	
	/**
	 * 模块单个发布
	 * @param pageCode
	 * @param moduleCode
	 * @return
	 */
	public boolean publishColumnModule(String pageCode,String moduleCode);
	
	/**
	 * 通过targetId获取板块-组件信息
	 * @param targetId
	 * @return
	 */
	public List<ColumnComponent> findColumnCompByTargetId(Long targetId, String moduleCode, String pageCode);
	
}
