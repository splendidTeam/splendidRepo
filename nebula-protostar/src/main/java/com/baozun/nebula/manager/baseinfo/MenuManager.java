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
import java.util.Map;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.command.MenuQueryCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.model.baseinfo.Menu;
import com.baozun.nebula.web.UserDetails;

/**
 * 菜单管理
 * 
 * @author songdianchao
 */
public interface MenuManager extends BaseManager{

	/**
	 * 查询所有菜单信息
	 * 
	 * @param user
	 *            user
	 * @return menu
	 */
	List<MenuCommand> getUserMenu(UserDetails user);
	/**
	 * 
	* @author 何波
	* @Description: 获取所有菜单
	* @return   
	* List<MenuCommand>   
	* @throws
	 */
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
	MenuQueryCommand getMenuByUrl(Map<String, Object> paraMap);
	
	/**
	 * 
	* @author 何波
	* @Description: 保存或修改
	* @param model
	* @return   
	* MenuQueryCommand   
	* @throws
	 */
	Menu edit(MenuQueryCommand model);
	
	/**
	 * 
	* @author 何波
	* @Description: 编辑功能
	* @param model
	* @return   
	* PrivilegeUrl   
	* @throws
	 */
	PrivilegeUrl editFunction(PrivilegeUrl model);
	
	/**
	 * 
	* @author 何波
	* @Description: 删除功能
	* @param id
	* @return  void
	* @throws
	 */
	void delFunction(Long id);
	/**
	 * 
	* @author 何波
	* @Description: 删除菜单
	* @param menuId
	* @param prId   
	* void   
	* @throws
	 */
	void delMenu(Long menuId ,Long prId);
	/**
	 * 
	* @author 何波
	* @Description: 通过id获取菜单
	* @param id
	* @return   
	* MenuQueryCommand   
	* @throws
	 */
	MenuQueryCommand getMenuById(Long id);
	/**
	 * 
	* @author 何波
	* @Description: 验证url或acl
	* @param id
	* @param url
	* @param acl   
	* void   
	* @throws
	 */
	void check(Long id ,String url,String acl,Long parentId);
}
