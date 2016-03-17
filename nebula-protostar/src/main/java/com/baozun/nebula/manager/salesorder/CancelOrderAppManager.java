package com.baozun.nebula.manager.salesorder;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;


import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.web.command.CancelApplicationCommand;

/**
 * 申请取消订单相关操作
 * @author qiang.yang
 * @createtime 2013-11-27 PM 17:12
 *
 */
public interface CancelOrderAppManager extends BaseManager {
	
	/**
	 * 查询取消订单列表
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	public Pagination<CancelApplicationCommand> findCancelApplicationListByQueryMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> queryMap);


}
