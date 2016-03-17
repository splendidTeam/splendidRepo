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
package com.baozun.nebula.dao.baseinfo;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.command.MenuQueryCommand;
import com.baozun.nebula.model.baseinfo.Menu;

/**
 * 菜单dao
 * 
 * @author Justin
 * 
 */
public interface MenuDao extends GenericEntityDao<Menu, Long> {
	/**
	 * 查询所有有效的menu
	 * 
	 * @return
	 */
	@NativeQuery(model = Menu.class)
	List<Menu> findAll();
	/**
	 * 查询用户在某个机构下的可以访问的menu列表
	 * @param userId
	 * @param orgaId
	 * @return
	 */
	@NativeQuery(model = MenuCommand.class)
	List<MenuCommand> findUserMenu(@QueryParam("userId") Long userId,
			@QueryParam("orgaId") Long orgaId);
	
	/**
	 * 
	* @author 何波
	* @Description: 获取所有菜单
	* @return   
	* List<MenuCommand>   
	* @throws
	 */
	@NativeQuery(model = MenuCommand.class)
	List<MenuCommand> getAllMenus();
	/**
	 * 
	* @author 何波
	* @Description: 菜单分页查询
	* @param page
	* @param sorts
	* @param paraMap
	* @return   
	* Pagination<PageTemplate>   
	* @throws
	 */
	@NativeQuery(model = MenuQueryCommand.class)
	MenuQueryCommand getMenuByUrl(@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery(alias = "CNT",clazzes = Integer.class)
	int findChildrenMenus(@QueryParam("pid") Long pid);
	

}
