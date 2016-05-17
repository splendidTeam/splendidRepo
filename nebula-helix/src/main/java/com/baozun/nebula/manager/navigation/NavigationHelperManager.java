package com.baozun.nebula.manager.navigation;

import com.baozun.nebula.command.product.FilterNavigationCommand;
import com.baozun.nebula.manager.BaseManager;

public interface NavigationHelperManager extends BaseManager{

	/**
	 * 根据uri和params 匹配对应的 导航信息
	 * @param uri
	 * 			uri
	 * @param queryStr
	 * @return
	 */
	public FilterNavigationCommand matchNavigationByUrl(String uri,String queryStr);
}
