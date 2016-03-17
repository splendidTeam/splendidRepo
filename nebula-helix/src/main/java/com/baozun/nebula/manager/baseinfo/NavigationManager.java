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
 */
package com.baozun.nebula.manager.baseinfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 
 * @author - 项硕
 */
public interface NavigationManager extends BaseManager{

	/**
	 * 查询所有导航
	 * 对导航的URL进行处理
	 * @param request
	 * @return
	 */
	@Deprecated
	List<Map<String, Object>> findNavigationList(HttpServletRequest request);

	/**
	 * 组织商城菜单数据，子菜单作为 List<NavigationCommand> subNavigations 属性，进行多级封装
	 * 
	 * 服装
	 * --男装
	 * ----衬衫
	 * --女装
	 * ----外套
	 * 
	 * 以上菜单结构被封装为 ：
	 * List[
	 * 			服装Command={
	 * 				subNavigations=List[
	 * 					男装Command={subNavigations=List[衬衫Command]}, 
	 * 					女装Command{subNavigations=List[外套Command]}
	 * 			]}
	 * ]
	 * 
	 * 
	 * @return
	 */
	List<NavigationCommand> findStoreNavigation();
}
