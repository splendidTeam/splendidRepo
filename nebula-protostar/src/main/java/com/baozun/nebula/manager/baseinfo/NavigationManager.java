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

import java.util.List;

import loxia.dao.Sort;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.CodeRepeatException;
import com.baozun.nebula.exception.NameRepeatException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.baseinfo.Navigation;

/**
 * @author - 项硕
 */
public interface NavigationManager extends BaseManager {
	
	/**
	 * 查询所有导航
	 * @return
	 */
	public List<Navigation> findAllNavigationList(Sort[] sorts);
	
	/**
	 * 新增或更新导航
	 * 根据传入的导航参数的id来判断是新增还是更新，
	 * -1 为新增， 否则 为更新。
	 * 若是更新并且lifecycle为0，即失效，
	 * 则会将改导航的所有子孙导航都更新为失效。
	 * @param navigation
	 * @return
	 */
	public Navigation createOrUpdateNavigation(Navigation navigation);

	/**
	 * 删除导航
	 * @param navigationId
	 */
	public void removeNavigationById(Long navigationId);

	/**
	 * 排序导航
	 * 按照字符串参数ids中的id顺序进行排序
	 * @param ids	已排序的导航id集（升序）
	 * @param userId 当前用户id
	 * @return
	 */
	public void sortNavigationsByIds(String ids, Long userId);

	/**
	 * 国际化name
	 * @param navigation
	 * @return
	 * @throws CodeRepeatException
	 * @throws NameRepeatException
	 * @throws BusinessException
	 */
	public Navigation i18nCreateOrUpdateNavigation(NavigationCommand navigation) 
			throws CodeRepeatException, NameRepeatException, BusinessException ;
	
	/**
	 * 根据id获取国际化name
	 * @param navigationId
	 * @return
	 */
	public NavigationCommand i18nFindNavigationLangByNavigationId(Long navigationId);
	
	
}
