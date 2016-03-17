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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;

import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.column.ColumnModule;


/**
 * 板块-组件 管理
 * @author Tianlong.Zhang
 *
 */
public interface SdkColumnManager extends BaseManager{
	
	/**
	 * 根据页面Code 查找 页面PageCommand
	 * @param code 
	 * @return ColumnPageCommand 包含了page的基本信息 和   columnModuleMap; moduleCode 为key 的ColumnModuleCommand map
	 */
	ColumnPageCommand  findColumnModuleMapByPageCode(String pageCode);
	
	
	List<ColumnModule> findColumnModuleListByQueryMap(Map<String, Object> paraMap);
	
	
}
