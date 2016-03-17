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
package com.baozun.nebula.sdk.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.dao.column.ColumnComponentDao;
import com.baozun.nebula.dao.column.ColumnModuleDao;
import com.baozun.nebula.dao.column.ColumnPageDao;
import com.baozun.nebula.dao.column.ColumnPublishedDao;
import com.baozun.nebula.dao.column.ColumnPublishedHistoryDao;
import com.baozun.nebula.model.column.ColumnComponent;
import com.baozun.nebula.model.column.ColumnModule;
import com.baozun.nebula.model.column.ColumnPage;
import com.baozun.nebula.model.column.ColumnPublished;
import com.baozun.nebula.sdk.manager.SdkColumnManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * @author Tianlong.Zhang
 *
 */
@Transactional
@Service("sdkColumnManager")
public class SdkColumnManagerImpl implements SdkColumnManager {
	
	private static final Logger	log					= LoggerFactory.getLogger(SdkColumnManager.class);
	
	@Autowired
	private ColumnPublishedDao columnPublishedDao;
	
	@Autowired
	private ColumnComponentDao columnComponentDao;
	
	@Autowired
	private ColumnModuleDao columnModuleDao;
	
	@Autowired
	private ColumnPageDao columnPageDao;

	@Autowired
	private ColumnPublishedHistoryDao columnPublishedHistoryDao;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkColumnManager#showPage(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public ColumnPageCommand findColumnModuleMapByPageCode(String code) {
		// 根据Page code 拿到 pageId，根据pageId 找到 pageId 对应的 ModuleIdList  .  
		//根据ModuleIdList 查找到所有的 组件
		
		ColumnPageCommand result = new ColumnPageCommand();
		Map<String,Object> pageParamMap = new HashMap<String,Object>();
		pageParamMap.put("code", code);
		List<ColumnPage> pageList = columnPageDao.findColumnPageListByQueryMap(pageParamMap);
		
		Map<Long,ColumnModuleCommand> moduleMap = new HashMap<Long,ColumnModuleCommand>();
		
		// moduleMapByModuleCode  以module code 做key 放入map
		Map<String, ColumnModuleCommand> moduleMapByModuleCode = new HashMap<String, ColumnModuleCommand>();
		
		if(Validator.isNotNullOrEmpty(pageList)){
			for(ColumnPage page: pageList){
				result.setId(page.getId());
				result.setName(page.getName());
				result.setCode(page.getCode());
				
				Map<String,Object> moduleParamMap = new HashMap<String,Object>();
				moduleParamMap.put("pageId",page.getId());
				
				// 根据pageId 查出 pageId对应的ColumnModule List
				List<ColumnModule>  moduleList = columnModuleDao.findColumnModuleListByQueryMap(moduleParamMap);
				
				List<Long> moduleIds = new ArrayList<Long>();
				for(ColumnModule module : moduleList){
					moduleIds.add(module.getId());
					
					ColumnModuleCommand moduleCmd = new ColumnModuleCommand();
					moduleCmd = (ColumnModuleCommand) ConvertUtils.convertFromTarget(moduleCmd, module);
					
					List<ColumnComponentCommand> componentCmdList = new ArrayList<ColumnComponentCommand>();
					moduleCmd.setComponentList(componentCmdList);
					
					//将 ColumnModule 整理成 moduleMap id 为key 便于下边的查找
					moduleMap.put(moduleCmd.getId(), moduleCmd);
				}
				
				
				// 根据moduleId List 查找 ColumnComponentCommand List
				List<ColumnComponent> componentList = columnComponentDao.findColumnComponentListByModuleIds(moduleIds);
				List<ColumnComponentCommand> componentCmdList = null;
				if(Validator.isNotNullOrEmpty(componentList)){
					
					//遍历ColumnComponent列表，将ColumnComponent 放入 对应的ColumnModule 中
					for(ColumnComponent component : componentList){
						Long moduleId = component.getModuleId();
						ColumnModuleCommand moduleCommand = moduleMap.get(moduleId);
						
						ColumnComponentCommand componentCmd = new ColumnComponentCommand();
						componentCmd =(ColumnComponentCommand)ConvertUtils.convertFromTarget(componentCmd, component);

						moduleCommand.getComponentList().add(componentCmd);
					}
					
				}
			}
			
			//moduleMapByModuleCode
			for(Long key : moduleMap.keySet()){
				ColumnModuleCommand moduleCommand = moduleMap.get(key);
				String moduleCode = moduleCommand.getCode();
				
				//按照sortNo 进行排序
				Collections.sort(moduleCommand.getComponentList());
				
				moduleMapByModuleCode.put(moduleCode, moduleCommand);
			}
			
			result.setColumnModuleMap(moduleMapByModuleCode);
		}
		
		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ColumnModule> findColumnModuleListByQueryMap(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return columnModuleDao.findColumnModuleListByQueryMap(paraMap);
	}

}
