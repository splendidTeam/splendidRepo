/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.OptionGroupCommand;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.RolePrivilege;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.system.ChooseOption;

/**
 * @author Justin
 *
 */
@Transactional
@Service("chooseOptionManager")
public class ChooseOptionManagerImpl implements ChooseOptionManager {

	@Autowired
	private ChooseOptionDao chooseOptionDao;
	@Transactional(readOnly = true)
	public List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode,String language){
		
		Map<String, Object> paraMap=new HashMap<String, Object>();
				
		paraMap.put("groupCode", groupCode);
		paraMap.put("labelLang",language);
		Sort[] sorts=new Sort[1];
		sorts[0]=new Sort("sort_no","asc");
		Pagination<ChooseOption> page=chooseOptionDao.findEffectChooseOptionListByQueryMapWithPage(0, Integer.MAX_VALUE, sorts, paraMap);
		
		return page.getItems();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.ChooseOptionManager#findEffectChooseOptionListByGroupCode(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ChooseOption> findEffectChooseOptionListByGroupCode(String groupCode) {
		// TODO Auto-generated method stub
		return this.findEffectChooseOptionListByGroupCode(groupCode, LocaleContextHolder.getLocale().toString());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OptionGroupCommand> findOptionGroupListByQueryMapWithPage(Sort[] sorts, Map<String, Object> paraMap) {
		List<OptionGroupCommand> optionGroupCommandList = chooseOptionDao.findOptionGroupList(sorts, paraMap);
		return optionGroupCommandList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pagination<ChooseOption> findOptionListByQueryMapWithPage(String groupCode,Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<ChooseOption> coList=chooseOptionDao.findChooseOptionListByQueryMapWithPage(groupCode,page, sorts, paraMap);
		return coList;
	}
	
	@Override
	public Integer enableOrDisableOptionByIds(List<Long> ids,Integer state){
		Integer result = chooseOptionDao.enableOrDisableOptionByIds(ids, state);
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ChooseOption findChooseOptionById(Long chooseOptionId){
		// TODO Auto-generated method stub
		ChooseOption chooseOption = chooseOptionDao.findChooseOptionById(chooseOptionId);
		return chooseOption;
	}
	@Transactional(readOnly = true)
	public Boolean validateOptionGroupCode(String groupCode){
		// TODO Auto-generated method stub
		
		Boolean existFlag = false;
		ChooseOption chooseOption = chooseOptionDao.validateOptionGroupCode(groupCode);
		
		if(chooseOption!=null){
			existFlag = true;
		}
		
		return existFlag;
	}
	
	
	//通过groupCode  optionValue验证是否存在
	@Override
	public Boolean validateOptionValue(String groupCode,String optionValue){
		// TODO Auto-generated method stub
		
		Boolean existFlag = false;
		ChooseOption chooseOption = chooseOptionDao.validateOptionValue(groupCode, optionValue);
		
		if(chooseOption!=null){
			existFlag = true;
		}
		
		return existFlag;
	}

	public void createOrUpdateOptionGroup(ChooseOption[] chooseOption){
		// TODO Auto-generated method stub
		for (ChooseOption cop : chooseOption){
			chooseOptionDao.save(cop);
		}
	}
	
	
	
	
	//修改或新增通用选项信息
	@Override
	public void createOrUpdateOption(ChooseOption chooseOption) {
		// TODO Auto-generated method stub
		ChooseOption saveChooseOption = null;
		// 若不为空，表示update操作
		if (chooseOption.getId()!= null) {
			saveChooseOption = chooseOptionDao.getByPrimaryKey(chooseOption.getId());
			saveChooseOption.setOptionLabel(chooseOption.getOptionLabel());
			saveChooseOption.setOptionValue(chooseOption.getOptionValue());
			saveChooseOption.setSortNo(chooseOption.getSortNo());
			
			saveChooseOption =chooseOptionDao.save(saveChooseOption);

			// roleDao.flush();
			// update操作时，先全部删除
		
		} else {
			saveChooseOption=new ChooseOption();
			//设置新增信息的状态为有效
			saveChooseOption.setLifecycle(chooseOption.getLifecycle());
			saveChooseOption.setGroupCode(chooseOption.getGroupCode());
			saveChooseOption.setOptionLabel(chooseOption.getOptionLabel());
			saveChooseOption.setOptionValue(chooseOption.getOptionValue());
			saveChooseOption.setSortNo(chooseOption.getSortNo());
			saveChooseOption = chooseOptionDao.save(saveChooseOption);
		}
	}

	@Override
	public List<ChooseOption> findOptionListByGroupCode(String groupCode){
		// TODO Auto-generated method stub
		return chooseOptionDao.findOptionListByGroupCode(groupCode);
	}

	@Override
	public List<ChooseOption> findAllOptionList(){
		// TODO Auto-generated method stub
		return chooseOptionDao.findAllChooseOptionList();
	}
}
