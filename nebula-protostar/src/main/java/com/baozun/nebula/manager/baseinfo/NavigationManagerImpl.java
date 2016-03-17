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

package com.baozun.nebula.manager.baseinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.CategoryCommand;
import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.dao.baseinfo.NavigationLangDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.CodeRepeatException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.NameRepeatException;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.baseinfo.NavigationLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.CategoryLang;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * 菜单导航管理
 * 
 * @author - 项硕
 */
@Transactional
@Service("navigationManager")
public class NavigationManagerImpl implements NavigationManager {
	
	@Autowired
	private SdkNavigationManager sdkNavigationManager;
	
	@Autowired
	private NavigationLangDao navigationLangDao;
	
	@Autowired
	private NavigationDao navigationDao;
	
	@SuppressWarnings("unused")
	private static final Logger	logger	= LoggerFactory.getLogger(NavigationManagerImpl.class);
	
	@Override
	public List<Navigation> findAllNavigationList(Sort[] sorts) {
		return sdkNavigationManager.findNavigationList(sorts);
	}

	@Override
	public Navigation createOrUpdateNavigation(Navigation navigation) {
		Long id = navigation.getId();
		if (id == null || id.equals(0L) || id.compareTo(-1L) < 0) {
			throw new IllegalArgumentException("The id of the navigation must be -1 or positive integer.");
		}
		if (new Long(-1).equals(id)) {	//新增
			navigation.setCreateTime(new Date());
			navigation.setLifecycle(1);
		} else {	//修改
			navigation.setModifyTime(new Date());
		} 
		return sdkNavigationManager.createOrUpdateNavigation(navigation);
	}

	@Override
	public void removeNavigationById(Long navigationId) {
		sdkNavigationManager.removeNavigationById(navigationId);
		
		//删除国际化
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			List<Long> navList =new ArrayList<Long>();
			navList.add(navigationId);
			navigationLangDao.deleteNavigationLangByNavigationIds(navList);
		}
	}

	@Override
	public void sortNavigationsByIds(String ids, Long userId) {
		String[] idArr = ids.split(",");
		if (StringUtils.isBlank(ids) || idArr.length == 0) {
			throw new IllegalArgumentException();
		}
		List<Long> idList = new ArrayList<Long>();
		for (int i = 0; i < idArr.length; i++) {
			Long id = Long.parseLong(idArr[i].trim());
			idList.add(id);
		}
		sdkNavigationManager.sortNavigationsByIds(idList, userId);
	}

	@Override
	public Navigation i18nCreateOrUpdateNavigation(NavigationCommand navigationCommand) 
			throws CodeRepeatException, NameRepeatException, BusinessException {
		Long id = navigationCommand.getId();
		Navigation navigation =new Navigation();
		navigation.setId(id);
		if (id == null || id.equals(0L) || id.compareTo(-1L) < 0) {
			throw new IllegalArgumentException("The id of the navigation must be -1 or positive integer.");
		}
		if (new Long(-1).equals(id)) {	//新增
			navigation.setCreateTime(new Date());
			navigation.setLifecycle(1);
			navigation.setParentId(navigationCommand.getParentId());
			navigation.setIsNewWin(navigationCommand.getIsNewWin());
			navigation.setOpeartorId(navigationCommand.getOpeartorId());
			navigation.setParam(navigationCommand.getParam());
			navigation.setSort(navigationCommand.getSort());
			navigation.setType(navigationCommand.getType());
			navigation.setUrl(navigationCommand.getUrl());
		} else {	//修改
			navigation.setModifyTime(new Date());
		} 
		return this.ptsI18nCreateOrUpdateNavigation(navigationCommand,navigation);
	}
	
	//LANG
	public Navigation ptsI18nCreateOrUpdateNavigation(NavigationCommand navigationCommand,Navigation navigation) {
		if (new Long(-1).equals(navigation.getId())) {
			//新增
			return extractCreateNav(navigationCommand, navigation);
			
		} else {
			//修改 
			return extractUpdateNav(navigationCommand);
		}
	}
 
	private Navigation extractUpdateNav(NavigationCommand navigationCommand) {
		Navigation dbNavi = navigationDao.getByPrimaryKey(navigationCommand.getId());
		if(dbNavi ==null ||dbNavi.getId() ==null){
			ifNotExpectedCountThrowException(1, 0);
		}
		
		
		//父节点
		Long parentId =dbNavi.getParentId();
				
		Navigation repeatParent =navigationDao.findEffectNavigationById(parentId);
		String defaultName =null;
		String pName ="";
		if(repeatParent==null&& !parentId.equals(0L)){
			throw new BusinessException(ErrorCodes.SYSTEM_NAVIGATION_PARENT_NO_EXISTS, defaultName);
		}
		if(repeatParent==null){
			pName ="ROOT";
		}else{
			pName =repeatParent.getName();
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			MutlLang mutlLang = (MutlLang)navigationCommand.getName();
			defaultName =mutlLang.getDefaultValue();
		}else{
			SingleLang singleLang =(SingleLang)navigationCommand.getName();
			defaultName=singleLang.getValue();
		}
		if(i18n){
			MutlLang mutlLang = (MutlLang)navigationCommand.getName();
			String[] values = mutlLang.getValues();
			String[] langs = mutlLang.getLangs();
			defaultName =mutlLang.getDefaultValue();
			//checkNavnameAndParent(parentId, repeatParent, defaultName);
			
			//多语言校验同级名称是否重复(t_base_navigation_lang)
			List<Navigation> navList =navigationDao.findNavigationListByParentId(parentId);
			
			//平级Ids
			List<Long> nIds =new ArrayList<Long>();
			
			if(Validator.isNotNullOrEmpty(navList)){
				for (Navigation n : navList) {
					if(n.getId().equals(dbNavi.getId())) 
						continue;
					nIds.add(n.getId());
				}
				
				for(int i=0 ;i< values.length;i++){
					String val = values[i];
					String lang =langs[i];
					
					
					int count =navigationLangDao.findNavigationLangByNameAndNavids(val, lang, nIds);
					
					if(count > 0){
						Object[] args = { pName,
								val};
						throw new NameRepeatException(
								ErrorCodes.SYSTEM_NAVIGATION_PARENT_EXIST_NAME, args);
					}
				}
			}
			
			//更新navigationLang
			for(int i=0 ;i< values.length;i++){
				String val = values[i];
				String lang =langs[i];
				
				NavigationLang navigationLang  =navigationLangDao.findNavigationLangByNavidAndLang(dbNavi.getId(), lang);
				if(navigationLang!=null){
					//update
					navigationLangDao.updateNavigationLangByNavigationIdAndLang(val, lang, dbNavi.getId());
				}else{
					//add
					NavigationLang navigationLang2 =new NavigationLang();
					navigationLang2.setNavigationId(dbNavi.getId());
					navigationLang2.setName(val);
					navigationLang2.setLang(lang);
					navigationLangDao.save(navigationLang2);
				}
				
			}
		}else{
			
			SingleLang singleLang =(SingleLang)navigationCommand.getName();
			defaultName=singleLang.getValue();
			
			checkNavnameAndParentWhenUpdate(parentId, repeatParent, defaultName,navigationCommand.getId());
		}
		dbNavi.setName(defaultName);
		dbNavi.setIsNewWin(navigationCommand.getIsNewWin());
		dbNavi.setLifecycle(navigationCommand.getLifecycle());
		dbNavi.setModifyTime(new Date());
		dbNavi.setOpeartorId(navigationCommand.getOpeartorId());
		dbNavi.setParam(navigationCommand.getParam());
		dbNavi.setSort(navigationCommand.getSort());
		dbNavi.setType(navigationCommand.getType());
		dbNavi.setUrl(navigationCommand.getUrl());
		Navigation retNav =navigationDao.save(dbNavi);
		
		if(retNav ==null){
			ifNotExpectedCountThrowException(1, 0);
		}
		
		//如果该导航状态为失效，则禁用其所有子导航
		if (retNav.getLifecycle().equals(0)) {
			disableAllChildren(retNav.getId());
		}
		
		return retNav;
	}
	/**
	 * 校验t_base_navigation同级导航名称是否已存在(除本身)
	 * @param parentId
	 * @param repeatParent
	 * @param defaultName
	 */
	private void checkNavnameAndParentWhenUpdate(Long parentId, Navigation repeatParent,
			String defaultName,Long id) {
		List<Navigation> chilRepeat =navigationDao.findEnableNavigationListByNameAndParentId(defaultName,parentId);
		if(repeatParent==null&& !parentId.equals(0L)){
			throw new BusinessException(ErrorCodes.SYSTEM_NAVIGATION_PARENT_NO_EXISTS, defaultName);
		}
		String pName ="";
		if(repeatParent==null){
			pName ="ROOT";
		}else{
			pName =repeatParent.getName();
		}
		if(Validator.isNotNullOrEmpty(chilRepeat)){
			boolean flag =false;
			for(Navigation navigation:chilRepeat){
				//更新
				if(!navigation.getId().equals(id)){
					flag =true;
				}
			}
			if(flag){
				Object[] args = { pName,
						defaultName};
				throw new NameRepeatException(
						ErrorCodes.SYSTEM_NAVIGATION_PARENT_EXIST_NAME, args);
			}
		}
	}
	
	

	/**
	 * 校验t_base_navigation父级导航(是否存在或有效)、同级导航(名称是否已存在)
	 * @param parentId
	 * @param repeatParent
	 * @param defaultName
	 */
	private void checkNavnameAndParent(Long parentId, Navigation repeatParent,
			String defaultName) {
		List<Navigation> chilRepeat =navigationDao.findEnableNavigationListByNameAndParentId(defaultName,parentId);
		if(repeatParent==null&& !parentId.equals(0L)){
			throw new BusinessException(ErrorCodes.SYSTEM_NAVIGATION_PARENT_NO_EXISTS, defaultName);
		}
		String pName ="";
		if(repeatParent==null){
			pName ="ROOT";
		}else{
			pName =repeatParent.getName();
		}
		if(Validator.isNotNullOrEmpty(chilRepeat)){
			Object[] args = { pName,
					defaultName};
			throw new NameRepeatException(
					ErrorCodes.SYSTEM_NAVIGATION_PARENT_EXIST_NAME, args);
		}
	}

	private Navigation extractCreateNav(NavigationCommand navigationCommand,
			Navigation navigation) {
		Long parentId =navigationCommand.getParentId();
		
		Navigation repeatParent =navigationDao.findEffectNavigationById(parentId);
		
		boolean i18n = LangProperty.getI18nOnOff();
		
		if(i18n){
			MutlLang mutlLang =(MutlLang)navigationCommand.getName();
			String[] values = mutlLang.getValues();
			String[] langs = mutlLang.getLangs();
			String defaultName =mutlLang.getDefaultValue();
			
			checkNavnameAndParent(parentId, repeatParent, defaultName);
			
			navigation.setName(defaultName);
			
			Navigation retNavigation =navigationDao.save(navigation);
			
			if(Validator.isNullOrEmpty(retNavigation)){
				ifNotExpectedCountThrowException(1,0);
			}
			
			Long id =retNavigation.getId();
			
			//多语言校验同级名称是否重复
			List<Navigation> navList =navigationDao.findNavigationListByParentId(parentId);
			
			//平级Ids
			List<Long> nIds =new ArrayList<Long>();
			
			if(Validator.isNotNullOrEmpty(navList)){
				for (Navigation n : navList) {
					nIds.add(n.getId());
				}
				String pName ="";
				for(int i=0 ;i< values.length;i++){
					String val = values[i];
					String lang =langs[i];
					
					if(repeatParent==null&& !parentId.equals(0L)){
						throw new BusinessException(ErrorCodes.SYSTEM_NAVIGATION_PARENT_NO_EXISTS, defaultName);
					}
					
					if(repeatParent==null){
						pName ="ROOT";
					}else{
						pName =repeatParent.getName();
					}
					int count =navigationLangDao.findNavigationLangByNameAndNavids(val, lang, nIds);
					
					if(count > 0){
						Object[] args = { pName,
								val};
						throw new NameRepeatException(
								ErrorCodes.SYSTEM_NAVIGATION_PARENT_EXIST_NAME, args);
					}
				}
			}
			//更新navigationLang
			for(int i=0 ;i< values.length;i++){
				String val = values[i];
				String lang =langs[i];
				
				NavigationLang navigationLang  =new NavigationLang();
				navigationLang.setNavigationId(id);
				navigationLang.setName(val);
				navigationLang.setLang(lang);
				navigationLangDao.save(navigationLang);
			}
			return retNavigation;
		}else{
			SingleLang singleLang = (SingleLang) navigationCommand.getName();
			String name = singleLang.getValue();
			checkNavnameAndParent(parentId, repeatParent, name);
			
			navigation.setName(name);
			return navigationDao.save(navigation);
		}
	}
	/**
	 * 根据ID，递归禁用其所有子孙导航
	 * @param id
	 */
	private void disableAllChildren(Long id){
		List<Navigation> list = navigationDao.findNavigationListByParentId(id);
		if (null != list && list.size() > 0) {
			for (Navigation n : list) {
				n = navigationDao.getByPrimaryKey(n.getId());
				n.setLifecycle(0);
				disableAllChildren(n.getId());
			}
		}
	}
	/**
	 * 如果不是 期望操作行 ,抛出异常
	 * 
	 * @param expected
	 * @param actual
	 */
	private void ifNotExpectedCountThrowException(Integer expected,
			Integer actual) {
		if (expected != actual) {
			throw new BusinessException(
					ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] {
							expected, actual });
			// throw new EffectRangeUnexpectedException(expected, actual);
		}
	}

	@Override
	public NavigationCommand i18nFindNavigationLangByNavigationId(
			Long navigationId) {
		Navigation navigation = navigationDao.getByPrimaryKey(navigationId);
		if(navigation == null){
			return null;
		}
		NavigationCommand navigationCommand = new NavigationCommand();
		LangProperty.I18nPropertyCopyToSource(navigation, navigationCommand);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			List<NavigationLang> navigationLangs = navigationLangDao.findNavigationLangListByNavidAndLangs(navigationId,MutlLang.i18nLangs());
			if(Validator.isNotNullOrEmpty(navigationLangs)){
				String[] values = new String[MutlLang.i18nSize()];
				String[] langs = new String[MutlLang.i18nSize()];
				for (int i = 0; i < navigationLangs.size(); i++) {
					NavigationLang navigationLang = navigationLangs.get(i);
					values[i] = navigationLang.getName();
					langs[i] = navigationLang.getLang();
				}
				MutlLang mutlLang = new MutlLang();
				mutlLang.setValues(values);
				mutlLang.setLangs(langs);
				navigationCommand.setName(mutlLang);
			}
		}else{
			SingleLang singleLang = new SingleLang();
			singleLang.setValue(navigation.getName());
			navigationCommand.setName(singleLang);
		}
		return navigationCommand;
	}
}
